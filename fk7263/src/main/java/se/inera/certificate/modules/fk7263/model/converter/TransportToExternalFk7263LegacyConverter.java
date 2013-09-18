package se.inera.certificate.modules.fk7263.model.converter;

import static se.inera.certificate.model.util.Iterables.addAll;
import static se.inera.certificate.model.util.Iterables.addExisting;
import static se.inera.certificate.modules.fk7263.model.codes.ObservationsKoder.DIAGNOS;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.joda.time.Partial;

import se.inera.certificate.model.Aktivitet;
import se.inera.certificate.model.Arbetsuppgift;
import se.inera.certificate.model.HosPersonal;
import se.inera.certificate.model.Id;
import se.inera.certificate.model.Kod;
import se.inera.certificate.model.LocalDateInterval;
import se.inera.certificate.model.Observation;
import se.inera.certificate.model.PartialInterval;
import se.inera.certificate.model.PhysicalQuantity;
import se.inera.certificate.model.Prognos;
import se.inera.certificate.model.Referens;
import se.inera.certificate.model.Sysselsattning;
import se.inera.certificate.model.Vardenhet;
import se.inera.certificate.model.Vardgivare;
import se.inera.certificate.model.Vardkontakt;
import se.inera.certificate.modules.fk7263.model.codes.Aktivitetskoder;
import se.inera.certificate.modules.fk7263.model.codes.ObservationsKoder;
import se.inera.certificate.modules.fk7263.model.codes.Prognoskoder;
import se.inera.certificate.modules.fk7263.model.codes.Referenstypkoder;
import se.inera.certificate.modules.fk7263.model.codes.Sysselsattningskoder;
import se.inera.certificate.modules.fk7263.model.codes.Vardkontakttypkoder;
import se.inera.certificate.modules.fk7263.model.converter.util.IsoTypeConverter;
import se.inera.certificate.modules.fk7263.model.external.Fk7263Patient;
import se.inera.certificate.modules.fk7263.model.external.Fk7263Utlatande;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.ArbetsformagaNedsattningType;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.ArbetsformagaType;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.BedomtTillstandType;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.FunktionstillstandType;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.Lakarutlatande;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.MedicinsktTillstandType;
import se.inera.ifv.insuranceprocess.healthreporting.v2.EnhetType;
import se.inera.ifv.insuranceprocess.healthreporting.v2.HosPersonalType;
import se.inera.ifv.insuranceprocess.healthreporting.v2.VardgivareType;

/**
 * Converts se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.Lakarutlatande Jaxb structure to External model
 * @author marced
 */
public final class TransportToExternalFk7263LegacyConverter {
    public static final String FK_7263 = "fk7263";

    private TransportToExternalFk7263LegacyConverter() {
    }

    /**
     * 
     */
    public static Fk7263Utlatande convert(Lakarutlatande source) {
        Fk7263Utlatande fk7263utlatande = new Fk7263Utlatande();

        // TODO: What about root and other ID properties?
        fk7263utlatande.setId(new Id(source.getLakarutlatandeId()));
        // TODO: what about other Kod attributes, version, etc?
        Kod typKod = new Kod(FK_7263);
        fk7263utlatande.setTyp(typKod);

        if (source.getKommentar() != null && !source.getKommentar().isEmpty()) {
            fk7263utlatande.setKommentars(Arrays.asList(source.getKommentar()));
        }
        fk7263utlatande.setSigneringsDatum(source.getSigneringsdatum());
        fk7263utlatande.setSkickatDatum(source.getSkickatDatum());
        fk7263utlatande.setSkapadAv(convert(source.getSkapadAvHosPersonal()));
        fk7263utlatande.setPatient(convert(source.getPatient()));

        addExisting(fk7263utlatande.getObservations(), convert(source.getMedicinsktTillstand()));
        addExisting(fk7263utlatande.getObservations(), convert(source.getBedomtTillstand()));

        for (FunktionstillstandType funktionstillstand : source.getFunktionstillstands()) {

            addAll(fk7263utlatande.getObservations(), convert(funktionstillstand));

            if (funktionstillstand.getArbetsformaga() != null) {

                if (funktionstillstand.getArbetsformaga().getArbetsuppgift() != null) {
                    fk7263utlatande.getPatient().getArbetsuppgifts().add(convert(funktionstillstand.getArbetsformaga().getArbetsuppgift()));
                }
                if (funktionstillstand.getArbetsformaga().getSysselsattnings() != null) {
                    for (se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.SysselsattningType sysselsattning : funktionstillstand.getArbetsformaga().getSysselsattnings()) {
                        fk7263utlatande.getPatient().getSysselsattnings().add(convert(sysselsattning, source.getPatient()));
                    }
                }
            }
        }

        for (se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.AktivitetType aktivitetType : source.getAktivitets()) {
            addExisting(fk7263utlatande.getAktiviteter(), convert(aktivitetType));
        }

        for (se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.ReferensType referensType : source.getReferens()) {
            addExisting(fk7263utlatande.getReferenser(), convert(referensType));
        }

        for (se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.VardkontaktType vardkontaktType : source.getVardkontakts()) {
            addExisting(fk7263utlatande.getVardkontakter(), convert(vardkontaktType));
        }

        return fk7263utlatande;
    }

    private static Vardkontakt convert(se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.VardkontaktType source) {
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
        vardkontaktTid.setStart(source.getVardkontaktstid());
        vardkontaktTid.setEnd(source.getVardkontaktstid());
        vardkontakt.setVardkontaktstid(vardkontaktTid);
        return vardkontakt;
    }

    private static Referens convert(se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.ReferensType source) {
        Referens referens = new Referens();

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
        referens.setDatum(source.getDatum());
        return referens;
    }

    private static Aktivitet convert(se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.AktivitetType source) {
        Aktivitet aktivitet = new Aktivitet();

        Kod aktivitetsCode = null;

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

    private static Sysselsattning convert(se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.SysselsattningType source, se.inera.ifv.insuranceprocess.healthreporting.v2.PatientType patient) {

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
            int v = Integer.parseInt(personnr.substring(personnr.indexOf('-')).substring(3, 4)) % 2;
            switch (v) {
            case 0:
                sysselsattningsKod = Sysselsattningskoder.MAMMALEDIG;
                break;
            case 1:
                sysselsattningsKod = Sysselsattningskoder.PAPPALEDIG;
                break;
            }
            break;
        default:
            throw new IllegalArgumentException("Unknown SysselsattningsType: " + source.getTypAvSysselsattning());
        }
        sysselsattning.setSysselsattningsTyp(sysselsattningsKod);
        return sysselsattning;
    }

    private static Arbetsuppgift convert(se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.ArbetsuppgiftType source) {

        Arbetsuppgift arbetsuppgift = new Arbetsuppgift();
        arbetsuppgift.setTypAvArbetsuppgift(source.getTypAvArbetsuppgift());
        return arbetsuppgift;
    }

    private static List<Observation> convert(FunktionstillstandType source) {

        List<Observation> observations = new ArrayList<Observation>();

        Observation observation = new Observation();

        switch (source.getTypAvFunktionstillstand()) {
        case AKTIVITET:
            observation.setObservationsKategori(ObservationsKoder.AKTIVITETER_OCH_DELAKTIGHET);
            break;
        case KROPPSFUNKTION:
            observation.setObservationsKategori(ObservationsKoder.KROPPSFUNKTIONER);
            break;
        default:
            throw new IllegalArgumentException("Unknown FunktionstillstandType: " + source.getTypAvFunktionstillstand());
        }
        observation.setBeskrivning(source.getBeskrivning());
        observations.add(observation);

        if (source.getArbetsformaga() != null) {
            observations.addAll(convert(source.getArbetsformaga()));
        }

        return observations;
    }

    private static List<Observation> convert(ArbetsformagaType source) {

        Prognos prognos = null;
        if (source.getPrognosangivelse() != null) {
            prognos = new Prognos();
            switch (source.getPrognosangivelse()) {
            case ATERSTALLAS_DELVIS:
                prognos.setPrognosKod(Prognoskoder.ATERSTALLAS_DELVIS);
                break;
            case ATERSTALLAS_HELT:
                prognos.setPrognosKod(Prognoskoder.ATERSTALLAS_HELT);
                break;
            case DET_GAR_INTE_ATT_BEDOMMA:
                prognos.setPrognosKod(Prognoskoder.DET_GAR_INTE_ATT_BEDOMA);
                break;
            case INTE_ATERSTALLAS:
                prognos.setPrognosKod(Prognoskoder.INTE_ATERSTALLAS);
                break;
            }
            prognos.setBeskrivning(source.getMotivering());
        }

        List<Observation> observations = new ArrayList<>();

        for (ArbetsformagaNedsattningType nedsattning : source.getArbetsformagaNedsattnings()) {
            Observation arbetsformaga = convert(nedsattning);
            arbetsformaga.setPrognos(prognos);
            observations.add(arbetsformaga);
        }
        return observations;
    }

    /**
     * Convert nedsattning to formaga. Note: kind of backwards, but meaning is opposite in transport model vs domain model..
     * @param source
     * @return
     */
    private static Observation convert(ArbetsformagaNedsattningType source) {
        Observation nedsattning = new Observation();
        nedsattning.setObservationsKod(ObservationsKoder.ARBETSFORMAGA);

        PhysicalQuantity varde = new PhysicalQuantity();
        varde.setUnit("percent");
        switch (source.getNedsattningsgrad()) {
        case HELT_NEDSATT:
            varde.setQuantity(0.0);
            break;
        case NEDSATT_MED_3_4:
            varde.setQuantity(25.0);
            break;
        case NEDSATT_MED_1_2:
            varde.setQuantity(50.0);
            break;
        case NEDSATT_MED_1_4:
            varde.setQuantity(75.0);
            break;
        }
        nedsattning.setVarde(Arrays.<PhysicalQuantity> asList(varde));

        PartialInterval observationsperiod = new PartialInterval();
        observationsperiod.setFrom(new Partial(source.getVaraktighetFrom()));
        observationsperiod.setTom(new Partial(source.getVaraktighetTom()));
        nedsattning.setObservationsPeriod(observationsperiod);

        return nedsattning;
    }

    private static Observation convert(BedomtTillstandType bedomtTillstand) {
        Observation observation = new Observation();
        observation.setBeskrivning(bedomtTillstand.getBeskrivning());
        // TODO: ObservationsKategori ?
        observation.setObservationsKod(ObservationsKoder.FORLOPP);
        return observation;
    }

    private static Observation convert(MedicinsktTillstandType medicinsktTillstand) {
        if (medicinsktTillstand == null) {
            return null;
        }
        Observation observation = new Observation();
        observation.setBeskrivning(medicinsktTillstand.getBeskrivning());

        if (medicinsktTillstand.getTillstandskod() != null) {

            observation.setObservationsKategori(DIAGNOS);
            observation.setObservationsKod(IsoTypeConverter.toKod(medicinsktTillstand.getTillstandskod()));
        }
        return observation;
    }

    private static HosPersonal convert(HosPersonalType source) {

        HosPersonal hosPersonal = new HosPersonal();
        hosPersonal.setId(IsoTypeConverter.toId(source.getPersonalId()));
        hosPersonal.setNamn(source.getFullstandigtNamn());
        hosPersonal.setForskrivarkod(source.getForskrivarkod());

        hosPersonal.setVardenhet(convert(source.getEnhet()));

        return hosPersonal;
    }

    /**
     * Creates and converts basic patient info
     * @param source
     * @return
     */
    private static Fk7263Patient convert(se.inera.ifv.insuranceprocess.healthreporting.v2.PatientType source) {
        Fk7263Patient patient = new Fk7263Patient();
        patient.setId(IsoTypeConverter.toId(source.getPersonId()));
        // we only have fullstandigt namn available in the legacy jaxb
        patient.setEfternamns(Arrays.<String> asList(source.getFullstandigtNamn()));
        return patient;
    }

    private static Vardenhet convert(EnhetType source) {

        Vardenhet vardenhet = new Vardenhet();
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

        Vardgivare vardgivare = new Vardgivare();
        vardgivare.setId(IsoTypeConverter.toId(source.getVardgivareId()));
        vardgivare.setNamn(source.getVardgivarnamn());
        return vardgivare;
    }

}
