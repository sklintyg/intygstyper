package se.inera.certificate.modules.fk7263.model.converter;

import static se.inera.certificate.model.util.Iterables.addAll;
import static se.inera.certificate.model.util.Iterables.addExisting;
import static se.inera.certificate.modules.fk7263.model.codes.ObservationsKoder.DIAGNOS;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.inera.certificate.fk7263.insuranceprocess.healthreporting.mu7263.v3.AktivitetType;
import se.inera.certificate.fk7263.insuranceprocess.healthreporting.mu7263.v3.ArbetsformagaNedsattningType;
import se.inera.certificate.fk7263.insuranceprocess.healthreporting.mu7263.v3.ArbetsformagaType;
import se.inera.certificate.fk7263.insuranceprocess.healthreporting.mu7263.v3.ArbetsuppgiftType;
import se.inera.certificate.fk7263.insuranceprocess.healthreporting.mu7263.v3.BedomtTillstandType;
import se.inera.certificate.fk7263.insuranceprocess.healthreporting.mu7263.v3.FunktionstillstandType;
import se.inera.certificate.fk7263.insuranceprocess.healthreporting.mu7263.v3.Lakarutlatande;
import se.inera.certificate.fk7263.insuranceprocess.healthreporting.mu7263.v3.MedicinsktTillstandType;
import se.inera.certificate.fk7263.insuranceprocess.healthreporting.mu7263.v3.ReferensType;
import se.inera.certificate.fk7263.insuranceprocess.healthreporting.mu7263.v3.SysselsattningType;
import se.inera.certificate.fk7263.insuranceprocess.healthreporting.mu7263.v3.VardkontaktType;
import se.inera.certificate.fk7263.insuranceprocess.healthreporting.v2.EnhetType;
import se.inera.certificate.fk7263.insuranceprocess.healthreporting.v2.HosPersonalType;
import se.inera.certificate.fk7263.insuranceprocess.healthreporting.v2.PatientType;
import se.inera.certificate.fk7263.insuranceprocess.healthreporting.v2.VardgivareType;
import se.inera.certificate.logging.LogMarkers;
import se.inera.certificate.model.Arbetsuppgift;
import se.inera.certificate.model.Id;
import se.inera.certificate.model.Kod;
import se.inera.certificate.model.LocalDateInterval;
import se.inera.certificate.model.PhysicalQuantity;
import se.inera.certificate.model.Sysselsattning;
import se.inera.certificate.model.Vardgivare;
import se.inera.certificate.model.Vardkontakt;
import se.inera.certificate.modules.fk7263.model.codes.Aktivitetskoder;
import se.inera.certificate.modules.fk7263.model.codes.ObservationsKoder;
import se.inera.certificate.modules.fk7263.model.codes.Prognoskoder;
import se.inera.certificate.modules.fk7263.model.codes.Referenstypkoder;
import se.inera.certificate.modules.fk7263.model.codes.Sysselsattningskoder;
import se.inera.certificate.modules.fk7263.model.codes.Vardkontakttypkoder;
import se.inera.certificate.modules.fk7263.model.converter.util.IsoTypeConverter;
import se.inera.certificate.modules.fk7263.model.external.Fk7263Aktivitet;
import se.inera.certificate.modules.fk7263.model.external.Fk7263HosPersonal;
import se.inera.certificate.modules.fk7263.model.external.Fk7263Observation;
import se.inera.certificate.modules.fk7263.model.external.Fk7263Patient;
import se.inera.certificate.modules.fk7263.model.external.Fk7263Referens;
import se.inera.certificate.modules.fk7263.model.external.Fk7263Utlatande;
import se.inera.certificate.modules.fk7263.model.external.Fk7263Vardenhet;

/**
 * Converts se.inera.certificate.fk7263.insuranceprocess.healthreporting.mu7263.v3.Lakarutlatande Jaxb structure to
 * External model.
 *
 * @author marced
 */
public final class TransportToExternalFk7263LegacyConverter {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransportToExternalFk7263LegacyConverter.class);

    public static final String FK_7263 = "fk7263";
    public static final String UTLATANDE_TYP_OID = "f6fb361a-e31d-48b8-8657-99b63912dd9b";
    public static final String UTLATANDE_CODE_SYSTEM_NAME = "kv_utlåtandetyp_intyg";
    public static final String UTLATANDE_CODE_SYSTEM_VERSION = null;
    public static final double FORMOGA_3_4 = 75;
    public static final double FORMOGA_1_2 = 50;
    public static final double FORMOGA_1_4 = 25;
    public static final int KON_INDEX = 3;

    private TransportToExternalFk7263LegacyConverter() {
    }

    public static Fk7263Utlatande convert(Lakarutlatande source) {
        Fk7263Utlatande fk7263utlatande = new Fk7263Utlatande();

        fk7263utlatande.setId(new Id(source.getLakarutlatandeId(), null));
        fk7263utlatande.setTyp(new Kod(UTLATANDE_TYP_OID, UTLATANDE_CODE_SYSTEM_NAME, UTLATANDE_CODE_SYSTEM_VERSION, FK_7263));

        if (source.getKommentar() != null && !source.getKommentar().isEmpty()) {
            fk7263utlatande.getKommentarer().add(source.getKommentar());
        }
        fk7263utlatande.setSigneringsdatum(source.getSigneringsdatum());
        fk7263utlatande.setSkickatdatum(source.getSkickatDatum());
        fk7263utlatande.setSkapadAv(convert(source.getSkapadAvHosPersonal()));
        fk7263utlatande.setPatient(convert(source.getPatient()));

        addExisting(fk7263utlatande.getObservationer(), convert(source.getMedicinsktTillstand()));
        addExisting(fk7263utlatande.getObservationer(), convert(source.getBedomtTillstand()));

        for (FunktionstillstandType funktionstillstand : source.getFunktionstillstands()) {

            addAll(fk7263utlatande.getObservationer(), convert(funktionstillstand));

            if (funktionstillstand.getArbetsformaga() != null) {
                if (fk7263utlatande.getPatient() != null) {
                    if (funktionstillstand.getArbetsformaga().getArbetsuppgift() != null) {
                        fk7263utlatande.getPatient().getArbetsuppgifter()
                                .add(convert(funktionstillstand.getArbetsformaga().getArbetsuppgift()));

                    }
                    if (funktionstillstand.getArbetsformaga().getSysselsattnings() != null) {
                        for (SysselsattningType sysselsattning : funktionstillstand.getArbetsformaga().getSysselsattnings()) {
                            fk7263utlatande.getPatient().getSysselsattningar().add(convert(sysselsattning, source.getPatient()));
                        }
                    }
                }
            }
        }

        for (AktivitetType aktivitetType : source.getAktivitets()) {
            Fk7263Aktivitet aktivitet = convert(aktivitetType);
            if (aktivitet != null) {
                addExisting(fk7263utlatande.getAktiviteter(), aktivitet);
            } else {
                LOGGER.info(LogMarkers.VALIDATION, "Validation failed for intyg " + source.getLakarutlatandeId()
                        + " issued by " + source.getSkapadAvHosPersonal().getEnhet().getEnhetsId().getExtension()
                        + ": Aktivitet with missing aktivitetskod found - ignored.");
            }
        }

        for (ReferensType referensType : source.getReferens()) {
            addExisting(fk7263utlatande.getReferenser(), convert(referensType));
        }

        for (VardkontaktType vardkontaktType : source.getVardkontakts()) {
            addExisting(fk7263utlatande.getVardkontakter(), convert(vardkontaktType));
        }

        return fk7263utlatande;
    }

    private static Vardkontakt convert(VardkontaktType source) {
        Vardkontakt vardkontakt = new Vardkontakt();

        switch (source.getVardkontakttyp()) {
        case MIN_UNDERSOKNING_AV_PATIENTEN:
            vardkontakt.setVardkontakttyp(Vardkontakttypkoder.MIN_UNDERSOKNING_AV_PATIENTEN);
            break;
        case MIN_TELEFONKONTAKT_MED_PATIENTEN:
            vardkontakt.setVardkontakttyp(Vardkontakttypkoder.MIN_TELEFONKONTAKT_MED_PATIENTEN);
            break;
        default:
            throw new IllegalArgumentException("Unknown VardkontaktType: " + source.getVardkontakttyp());
        }

        // In FK7263Legacy case, we set fromDate=endDate since we only have 1 date
        LocalDateInterval vardkontaktTid = new LocalDateInterval();
        vardkontaktTid.setFrom(source.getVardkontaktstid());
        vardkontaktTid.setTom(source.getVardkontaktstid());
        vardkontakt.setVardkontaktstid(vardkontaktTid);
        return vardkontakt;
    }

    private static Fk7263Referens convert(ReferensType source) {
        Fk7263Referens referens = new Fk7263Referens();
        if (source == null) {
            return null;
        }
        if (source.getReferenstyp() != null) {
            switch (source.getReferenstyp()) {
            case JOURNALUPPGIFTER:
                referens.setReferenstyp(Referenstypkoder.JOURNALUPPGIFT);
                break;
            case ANNAT:
                referens.setReferenstyp(Referenstypkoder.ANNAT);
                break;
            default:
                throw new IllegalArgumentException("Unknown ReferensType: " + source.getReferenstyp());
            }
        }
        referens.setDatum(source.getDatum());
        return referens;
    }

    private static Fk7263Aktivitet convert(AktivitetType source) {
        Fk7263Aktivitet aktivitet = new Fk7263Aktivitet();

        Kod aktivitetsCode;

        if (source.getAktivitetskod() == null) {
            return null;
        }

        switch (source.getAktivitetskod()) {
        case PLANERAD_ELLER_PAGAENDE_BEHANDLING_ELLER_ATGARD_INOM_SJUKVARDEN:
            aktivitetsCode = Aktivitetskoder.PLANERAD_ELLER_PAGAENDE_BEHANDLING_ELLER_ATGARD_INOM_SJUKVARDEN;
            break;
        case PLANERAD_ELLER_PAGAENDE_ANNAN_ATGARD:
            aktivitetsCode = Aktivitetskoder.PLANERAD_ELLER_PAGAENDE_ANNAN_ATGARD;
            break;
        case ARBETSLIVSINRIKTAD_REHABILITERING_AR_AKTUELL:
            aktivitetsCode = Aktivitetskoder.ARBETSLIVSINRIKTAD_REHABILITERING_AR_AKTUELL;
            break;
        case ARBETSLIVSINRIKTAD_REHABILITERING_AR_EJ_AKTUELL:
            aktivitetsCode = Aktivitetskoder.ARBETSLIVSINRIKTAD_REHABILITERING_AR_INTE_AKTUELL;
            break;
        case GAR_EJ_ATT_BEDOMMA_OM_ARBETSLIVSINRIKTAD_REHABILITERING_AR_AKTUELL:
            aktivitetsCode = Aktivitetskoder.GAR_EJ_ATT_BEDOMA_OM_ARBETSLIVSINRIKTAD_REHABILITERING_AR_AKTUELL;
            break;
        case PATIENTEN_BEHOVER_FA_KONTAKT_MED_FORETAGSHALSOVARDEN:
            aktivitetsCode = Aktivitetskoder.PATIENTEN_BOR_FA_KONTAKT_MED_FORETAGSHALSOVARDEN;
            break;
        case PATIENTEN_BEHOVER_FA_KONTAKT_MED_ARBETSFORMEDLINGEN:
            aktivitetsCode = Aktivitetskoder.PATIENTEN_BOR_FA_KONTAKT_MED_ARBETSFORMEDLINGEN;
            break;
        case FORANDRAT_RESSATT_TILL_ARBETSPLATSEN_AR_AKTUELLT:
            aktivitetsCode = Aktivitetskoder.FORANDRA_RESSATT_TILL_ARBETSPLATSEN_AR_AKTUELLT;
            break;
        case FORANDRAT_RESSATT_TILL_ARBETSPLATSEN_AR_EJ_AKTUELLT:
            aktivitetsCode = Aktivitetskoder.FORANDRA_RESSATT_TILL_ARBETSPLATSEN_AR_EJ_AKTUELLT;
            break;
        case KONTAKT_MED_FORSAKRINGSKASSAN_AR_AKTUELL:
            aktivitetsCode = Aktivitetskoder.KONTAKT_MED_FK_AR_AKTUELL;
            break;
        case AVSTANGNING_ENLIGT_SM_L_PGA_SMITTA:
            aktivitetsCode = Aktivitetskoder.AVSTANGNING_ENLIGT_SML_PGA_SMITTA;
            break;
        case OVRIGT:
            aktivitetsCode = Aktivitetskoder.OVRIGT;
            break;
        default:
            throw new IllegalArgumentException("Unknown Aktivitetskod: " + source.getAktivitetskod());
        }

        aktivitet.setBeskrivning(source.getBeskrivning());
        aktivitet.setAktivitetskod(aktivitetsCode);
        return aktivitet;
    }

    private static Sysselsattning convert(SysselsattningType source, PatientType patient) {
        Sysselsattning sysselsattning = new Sysselsattning();
        Kod sysselsattningsKod = null;

        switch (source.getTypAvSysselsattning()) {
        case ARBETSLOSHET:
            sysselsattningsKod = Sysselsattningskoder.ARBETSLOSHET;
            break;
        case NUVARANDE_ARBETE:
            sysselsattningsKod = Sysselsattningskoder.NUVARANDE_ARBETE;
            break;
        case FORALDRALEDIGHET:
            // GenderCheck
            String personnr = patient.getPersonId().getExtension();
            int v = Integer.parseInt(personnr.substring(personnr.indexOf('-')).substring(KON_INDEX, KON_INDEX + 1)) % 2;
            if (v == 0) {
                sysselsattningsKod = Sysselsattningskoder.MAMMALEDIG;
            } else if (v == 1) {
                sysselsattningsKod = Sysselsattningskoder.PAPPALEDIG;
            }
            break;
        default:
            throw new IllegalArgumentException("Unknown SysselsattningsType: " + source.getTypAvSysselsattning());
        }

        sysselsattning.setSysselsattningstyp(sysselsattningsKod);
        return sysselsattning;
    }

    private static Arbetsuppgift convert(ArbetsuppgiftType source) {
        if (source == null) {
            return null;
        }
        Arbetsuppgift arbetsuppgift = new Arbetsuppgift();
        arbetsuppgift.setTypAvArbetsuppgift(source.getTypAvArbetsuppgift());
        return arbetsuppgift;
    }

    private static List<Fk7263Observation> convert(FunktionstillstandType source) {

        List<Fk7263Observation> observations = new ArrayList<>();

        Fk7263Observation observation = new Fk7263Observation();

        switch (source.getTypAvFunktionstillstand()) {
        case AKTIVITET:
            if (source.getBeskrivning() != null && !source.getBeskrivning().isEmpty()) {
                observation.setObservationskategori(ObservationsKoder.AKTIVITETER_OCH_DELAKTIGHET);
                observation.setBeskrivning(EmptyStringUtil.escapeEmptyString(source.getBeskrivning()));
            } else {
                observation = null;
            }
            break;
        case KROPPSFUNKTION:
            if (source.getBeskrivning() != null && !source.getBeskrivning().isEmpty()) {
                observation.setObservationskategori(ObservationsKoder.KROPPSFUNKTIONER);
                observation.setBeskrivning(EmptyStringUtil.escapeEmptyString(source.getBeskrivning()));
            } else {
                observation = null;
            }
            break;
        default:
            throw new IllegalArgumentException("Unknown FunktionstillstandType: " + source.getTypAvFunktionstillstand());
        }

        if (observation != null) {
            observations.add(observation);
        }

        if (source.getArbetsformaga() != null) {
            observations.addAll(convert(source.getArbetsformaga()));
        }

        return observations;
    }

    private static List<Fk7263Observation> convert(ArbetsformagaType source) {

        Fk7263Observation prognos = null;
        if (source.getPrognosangivelse() != null || source.getMotivering() != null) {
            prognos = new Fk7263Observation();
            prognos.setObservationskod(ObservationsKoder.PROGNOS);
            if (source.getPrognosangivelse() != null) {
                switch (source.getPrognosangivelse()) {
                case ATERSTALLAS_DELVIS:
                    prognos.getVarde().add(Prognoskoder.ATERSTALLAS_DELVIS);
                    break;
                case ATERSTALLAS_HELT:
                    prognos.getVarde().add(Prognoskoder.ATERSTALLAS_HELT);
                    break;
                case DET_GAR_INTE_ATT_BEDOMMA:
                    prognos.getVarde().add(Prognoskoder.DET_GAR_INTE_ATT_BEDOMA);
                    break;
                case INTE_ATERSTALLAS:
                    prognos.getVarde().add(Prognoskoder.INTE_ATERSTALLAS);
                    break;
                default:
                    break;
                }
            }
            if (source.getMotivering() != null) {
                prognos.setBeskrivning(source.getMotivering());
            }
        }
        List<Fk7263Observation> observations = new ArrayList<>();

        if (prognos != null) {
            observations.add(prognos);
        }

        for (ArbetsformagaNedsattningType nedsattning : source.getArbetsformagaNedsattnings()) {
            Fk7263Observation arbetsformaga = convert(nedsattning);
            observations.add(arbetsformaga);
        }
        return observations;
    }

    /**
     * Convert nedsattning to formaga. Note: kind of backwards, but meaning is opposite in transport model vs domain
     * model..
     *
     * @param source
     *            source
     * @return Fk7263Observation
     */
    private static Fk7263Observation convert(ArbetsformagaNedsattningType source) {
        Fk7263Observation nedsattning = new Fk7263Observation();
        nedsattning.setObservationskod(ObservationsKoder.ARBETSFORMAGA);
        if (source.getNedsattningsgrad() != null) {
            PhysicalQuantity varde = new PhysicalQuantity();
            varde.setUnit("percent");
            switch (source.getNedsattningsgrad()) {
            case HELT_NEDSATT:
                varde.setQuantity(0.0);
                break;
            case NEDSATT_MED_3_4:
                varde.setQuantity(FORMOGA_1_4);
                break;
            case NEDSATT_MED_1_2:
                varde.setQuantity(FORMOGA_1_2);
                break;
            case NEDSATT_MED_1_4:
                varde.setQuantity(FORMOGA_3_4);
                break;
            default:
                throw new IllegalArgumentException("Unknown Nedsattningsgrad: " + source.getNedsattningsgrad());
            }
            nedsattning.getVarde().add(varde);
        }
        LocalDateInterval observationsperiod = new LocalDateInterval();
        if (source.getVaraktighetFrom() != null && source.getVaraktighetTom() != null) {
            observationsperiod.setFrom(source.getVaraktighetFrom());
            observationsperiod.setTom(source.getVaraktighetTom());
            nedsattning.setObservationsperiod(DateTimeConverter.toPartialInterval(observationsperiod));
        }
        return nedsattning;
    }

    private static Fk7263Observation convert(BedomtTillstandType bedomtTillstand) {
        if (bedomtTillstand == null) {
            return null;
        }
        Fk7263Observation observation = new Fk7263Observation();
        observation.setBeskrivning(bedomtTillstand.getBeskrivning());
        // TODO ObservationsKategori ?
        observation.setObservationskod(ObservationsKoder.FORLOPP);
        return observation;
    }

    private static Fk7263Observation convert(MedicinsktTillstandType medicinsktTillstand) {
        if (medicinsktTillstand == null) {
            return null;
        }
        Fk7263Observation observation = new Fk7263Observation();
        observation.setBeskrivning(medicinsktTillstand.getBeskrivning());

        if (medicinsktTillstand.getTillstandskod() != null) {

            observation.setObservationskategori(DIAGNOS);
            observation.setObservationskod(IsoTypeConverter.toKod(medicinsktTillstand.getTillstandskod()));
        }
        return observation;
    }

    private static Fk7263HosPersonal convert(HosPersonalType source) {
        if (source == null) {
            return null;
        }
        Fk7263HosPersonal hosPersonal = new Fk7263HosPersonal();
        hosPersonal.setId(IsoTypeConverter.toId(source.getPersonalId()));
        hosPersonal.setNamn(source.getFullstandigtNamn());
        hosPersonal.setForskrivarkod(source.getForskrivarkod());

        hosPersonal.setVardenhet(convert(source.getEnhet()));

        return hosPersonal;
    }

    /**
     * Creates and converts basic patient info.
     *
     * @param source
     *            source
     * @return Fk7263Patient
     */
    private static Fk7263Patient convert(PatientType source) {
        if (source == null) {
            return null;
        }
        Fk7263Patient patient = new Fk7263Patient();
        if (source.getPersonId() != null) {
            patient.setId(IsoTypeConverter.toId(source.getPersonId()));
        }
        // we only have fullstandigt namn available in the legacy jaxb
        patient.setEfternamn(source.getFullstandigtNamn());
        return patient;
    }

    private static Fk7263Vardenhet convert(EnhetType source) {
        if (source == null) {
            return null;
        }
        Fk7263Vardenhet vardenhet = new Fk7263Vardenhet();
        vardenhet.setId(IsoTypeConverter.toId(source.getEnhetsId()));
        vardenhet.setNamn(source.getEnhetsnamn());
        vardenhet.setArbetsplatskod(IsoTypeConverter.toId(source.getArbetsplatskod()));
        vardenhet.setPostadress(source.getPostadress());
        vardenhet.setPostnummer(source.getPostnummer());
        vardenhet.setPostort(source.getPostort());
        vardenhet.setEpost(source.getEpost());
        vardenhet.setTelefonnummer(source.getTelefonnummer());
        vardenhet.setVardgivare(convert(source.getVardgivare()));
        return vardenhet;
    }

    private static Vardgivare convert(VardgivareType source) {
        if (source == null) {
            return null;
        }
        Vardgivare vardgivare = new Vardgivare();
        vardgivare.setId(IsoTypeConverter.toId(source.getVardgivareId()));
        vardgivare.setNamn(source.getVardgivarnamn());
        return vardgivare;
    }

}
