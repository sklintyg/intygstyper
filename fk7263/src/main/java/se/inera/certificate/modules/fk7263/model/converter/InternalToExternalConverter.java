package se.inera.certificate.modules.fk7263.model.converter;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.inera.certificate.model.Arbetsuppgift;
import se.inera.certificate.model.Id;
import se.inera.certificate.model.InternalLocalDateInterval;
import se.inera.certificate.model.Kod;
import se.inera.certificate.model.LocalDateInterval;
import se.inera.certificate.model.PhysicalQuantity;
import se.inera.certificate.model.Sysselsattning;
import se.inera.certificate.model.Vardkontakt;
import se.inera.certificate.model.converter.util.ConverterException;
import se.inera.certificate.model.converter.util.InternalConverterUtil;
import se.inera.certificate.model.converter.util.InternalToExternalConverterUtil;
import se.inera.certificate.modules.fk7263.model.codes.Aktivitetskoder;
import se.inera.certificate.modules.fk7263.model.codes.ObservationsKoder;
import se.inera.certificate.modules.fk7263.model.codes.Prognoskoder;
import se.inera.certificate.modules.fk7263.model.codes.Referenstypkoder;
import se.inera.certificate.modules.fk7263.model.codes.Sysselsattningskoder;
import se.inera.certificate.modules.fk7263.model.codes.Vardkontakttypkoder;
import se.inera.certificate.modules.fk7263.model.external.Fk7263Aktivitet;
import se.inera.certificate.modules.fk7263.model.external.Fk7263Observation;
import se.inera.certificate.modules.fk7263.model.external.Fk7263ObservationsSamband;
import se.inera.certificate.modules.fk7263.model.external.Fk7263Patient;
import se.inera.certificate.modules.fk7263.model.external.Fk7263Referens;
import se.inera.certificate.modules.fk7263.model.external.Fk7263Utlatande;
import se.inera.certificate.modules.fk7263.model.internal.Fk7263Intyg;
import se.inera.certificate.modules.support.api.exception.ModuleException;

import java.util.ArrayList;
import java.util.List;

import static se.inera.certificate.modules.fk7263.model.codes.Kodverk.ICD_10;

/**
 * Used to convert from internal to external model, i.e for converting input from webcert to a signable certificate.
 *
 * @author erik
 */
public class InternalToExternalConverter {

    private static final Logger LOG = LoggerFactory.getLogger(InternalToExternalConverter.class);

    private static final String HSA_ID_ROOT = "1.2.752.129.2.1.4.1";
    private static final String ARBETSPLATSKOD_ROOT = "1.2.752.29.4.71";

    private static final Kod FK7263_TYP = new Kod("f6fb361a-e31d-48b8-8657-99b63912dd9b", "kv_utlåtandetyp_intyg", null, "fk7263");
    private static final Id HUVUDDIAGNOS_ID = new Id("1.2.752.129.2.1.2.1", "1");
    private static final Id BIDIAGNOS_1_ID = new Id("1.2.752.129.2.1.2.1", "2");
    private static final Id BIDIAGNOS_2_ID = new Id("1.2.752.129.2.1.2.1", "3");

    public static final double FORMOGA_1_4 = 25D;
    public static final double FORMOGA_1_2 = 50D;
    public static final double FORMOGA_3_4 = 75D;

    /**
     * Converts from the internal to the external model.
     *
     * @param source
     *            {@link Fk7263Intyg}
     * @return {@link Fk7263Utlatande}
     * @throws ConverterException
     */
    public Fk7263Utlatande convert(Fk7263Intyg source) throws ConverterException {
        LOG.trace("Starting conversion from internalToExternal");
        if (source == null) {
            throw new ConverterException("Internal intyg was null");
        }

        Fk7263Utlatande utlatande = new Fk7263Utlatande();

        utlatande.setId(new Id(source.getId(), null));
        utlatande.setTyp(FK7263_TYP);

        utlatande.getAktiviteter().addAll(buildExternalAktiviteter(source));
        utlatande.getObservationer().addAll(buildExternalObservationer(source));
        utlatande.getObservationssamband().addAll(buildObseravationsSamband(source));
        utlatande.getReferenser().addAll(buildExternalReferenser(source));
        utlatande.getVardkontakter().addAll(buildExternalVardkontakter(source));

        List<String> kommentarer = buildKommentarer(source);
        if (!kommentarer.isEmpty()) {
            utlatande.getKommentarer().addAll(kommentarer);
        }

        utlatande.setPatient(buildExternalPatient(source));
        utlatande.setSigneringsdatum(source.getIntygMetadata().getSigneringsdatum());
        utlatande.setSkickatdatum(source.getIntygMetadata().getSkickatdatum());
        utlatande.setSkapadAv(InternalToExternalConverterUtil.convertToExtHosPersonal(source.getIntygMetadata().getSkapadAv()));
        return utlatande;

    }

    /**
     * Build a List of {@link String}[s] from different fields in the internal model.
     *
     * @param source
     *            internal representation
     * @return List of {@link String} information was found going into the kommentarer fields
     */
    private List<String> buildKommentarer(Fk7263Intyg source) {
        List<String> kommentarer = new ArrayList<>();

        if (!isNullOrEmpty(source.getKommentar())) {
            kommentarer.add(source.getKommentar());
        }

        return kommentarer;
    }

    /**
     * Create a List of {@link Vardkontakt} from the internal model.
     *
     * @param source
     *            internal representation
     * @return List of {@link Vardkontakt}
     */
    private List<Vardkontakt> buildExternalVardkontakter(Fk7263Intyg source) {
        List<Vardkontakt> vardkontakter = new ArrayList<>();

        if (source.getUndersokningAvPatienten() != null) {
            Vardkontakt vardkontakt = new Vardkontakt();
            vardkontakt.setVardkontaktstid(new LocalDateInterval(source.getUndersokningAvPatienten().asLocalDate(),
                    source.getUndersokningAvPatienten().asLocalDate()));
            vardkontakt.setVardkontakttyp(Vardkontakttypkoder.MIN_UNDERSOKNING_AV_PATIENTEN);
            vardkontakter.add(vardkontakt);
        }

        if (source.getTelefonkontaktMedPatienten() != null) {
            Vardkontakt vardkontakt = new Vardkontakt();
            vardkontakt.setVardkontaktstid(new LocalDateInterval(source.getTelefonkontaktMedPatienten().asLocalDate(),
                    source.getTelefonkontaktMedPatienten().asLocalDate()));
            vardkontakt.setVardkontakttyp(Vardkontakttypkoder.MIN_TELEFONKONTAKT_MED_PATIENTEN);
            vardkontakter.add(vardkontakt);
        }

        return vardkontakter;
    }

    /**
     * <<<<<<< HEAD
     * Create a List of {@link se.inera.certificate.model.Referens} from the internal model.
     *
     * =======
     * Create a List of {@link Fk7263Referens} from the internal model.
     *
     * >>>>>>> master
     *
     * @param source
     *            internal representation
     * @return List of {@link Fk7263Referens}
     */
    private List<Fk7263Referens> buildExternalReferenser(Fk7263Intyg source) {
        List<Fk7263Referens> referenser = new ArrayList<>();

        if (source.getJournaluppgifter() != null) {
            Fk7263Referens referens = new Fk7263Referens();
            referens.setReferenstyp(Referenstypkoder.JOURNALUPPGIFT);
            referens.setDatum(source.getJournaluppgifter().asLocalDate());
            referenser.add(referens);

        }

        if (source.getAnnanReferens() != null) {
            Fk7263Referens referens = new Fk7263Referens();
            referens.setReferenstyp(Referenstypkoder.ANNAT);
            referens.setDatum(source.getAnnanReferens().asLocalDate());
            referens.setBeskrivning(source.getAnnanReferensBeskrivning());
            referenser.add(referens);
        }

        return referenser;
    }

    /**
     * Create a List of {@link Fk7263Observation} from the internal model.
     *
     * @param source
     *            internal representation
     * @return List of {@link Fk7263Observation}
     */
    private List<Fk7263Observation> buildExternalObservationer(Fk7263Intyg source) {
        List<Fk7263Observation> observationer = new ArrayList<>();

        // observation huvudDiagnos
        if (source.getDiagnosKod() != null) {
            Fk7263Observation observation = buildObservation(buildDiagnoseCode(source.getDiagnosKod(), source.getDiagnosBeskrivning1()),
                    ObservationsKoder.DIAGNOS, source.getDiagnosBeskrivning());
            if (notNullAndTrue(source.getSamsjuklighet())) {
                observation.setId(HUVUDDIAGNOS_ID);
            }
            observationer.add(observation);
        }

        // Check for bi-diagnos 1
        if (source.getDiagnosKod2() != null) {
            Fk7263Observation observation = buildObservation(buildDiagnoseCode(source.getDiagnosKod2(), source.getDiagnosBeskrivning2()),
                    ObservationsKoder.BIDIAGNOS, null);
            if (notNullAndTrue(source.getSamsjuklighet())) {
                observation.setId(BIDIAGNOS_1_ID);
            }
            observationer.add(observation);
        }

        // Check for bi-diagnos 2
        if (source.getDiagnosKod3() != null) {
            Fk7263Observation observation = buildObservation(buildDiagnoseCode(source.getDiagnosKod3(), source.getDiagnosBeskrivning3()),
                    ObservationsKoder.BIDIAGNOS, null);
            if (notNullAndTrue(source.getSamsjuklighet())) {
                observation.setId(BIDIAGNOS_2_ID);
            }
            observationer.add(observation);
        }

        // observation sjukdomsforlopp
        if (!isNullOrEmpty(source.getSjukdomsforlopp())) {
            observationer.add(buildObservation(ObservationsKoder.FORLOPP, null, source.getSjukdomsforlopp()));
        }

        // observation funktionsnedsattning
        if (!isNullOrEmpty(source.getFunktionsnedsattning())) {
            observationer.add(buildObservation(null, ObservationsKoder.KROPPSFUNKTIONER,
                    source.getFunktionsnedsattning()));
        }

        // observation aktivitetsbegransning
        if (!isNullOrEmpty(source.getAktivitetsbegransning())) {
            observationer.add(buildObservation(null, ObservationsKoder.AKTIVITETER_OCH_DELAKTIGHET,
                    source.getAktivitetsbegransning()));
        }

        // observation arbetsformaga (create between 1 and 4 instances)
        if (source.getNedsattMed100() != null) {
            observationer.add(buildArbetsformageObservation(ObservationsKoder.ARBETSFORMAGA,
                    source.getNedsattMed100(), new PhysicalQuantity(0.0, "percent"), null));
        }
        if (source.getNedsattMed75() != null) {
            observationer.add(buildArbetsformageObservation(ObservationsKoder.ARBETSFORMAGA,
                    source.getNedsattMed75(), new PhysicalQuantity(FORMOGA_1_4, "percent"), source.getNedsattMed75Beskrivning()));
        }
        if (source.getNedsattMed50() != null) {
            observationer.add(buildArbetsformageObservation(ObservationsKoder.ARBETSFORMAGA,
                    source.getNedsattMed50(), new PhysicalQuantity(FORMOGA_1_2, "percent"), source.getNedsattMed50Beskrivning()));
        }
        if (source.getNedsattMed25() != null) {
            observationer.add(buildArbetsformageObservation(ObservationsKoder.ARBETSFORMAGA,
                    source.getNedsattMed25(), new PhysicalQuantity(FORMOGA_3_4, "percent"), source.getNedsattMed25Beskrivning()));
        }

        // observation prognos
        if (getCorrespondingPrognosKod(source) != null) {
            observationer.add(buildPrognos(source));
        }

        return observationer;
    }

    private boolean notNullAndTrue(Boolean value) {
        if (value == null) {
            return false;
        } else {
            return value;
        }
    }

    private List<Fk7263ObservationsSamband> buildObseravationsSamband(Fk7263Intyg source) throws ConverterException {
        List<Fk7263ObservationsSamband> observationsSamband = new ArrayList<Fk7263ObservationsSamband>();
        if (notNullAndTrue(source.getSamsjuklighet())) {
            try {
                if (source.getDiagnosKod() != null && source.getDiagnosKod2() != null) {
                    observationsSamband.add(new Fk7263ObservationsSamband(HUVUDDIAGNOS_ID, BIDIAGNOS_1_ID));
                }
                if (source.getDiagnosKod() != null && source.getDiagnosKod3() != null) {
                    observationsSamband.add(new Fk7263ObservationsSamband(HUVUDDIAGNOS_ID, BIDIAGNOS_2_ID));
                }
            } catch (ModuleException e) {
                throw new ConverterException(String.format("Error while converting observationssamband: %s", e.getMessage()));
            }
        }
        return observationsSamband;
    }

    /**
     * Creates the slightly more complex {@link Fk7263Observation}[s] regarding arbetsformaga.
     *
     * @param kod
     *            a {@link Kod} from {@link ObservationsKoder}
     * @param period
     *            {@link LocalDateInterval}
     * @param varde
     *            {@link PhysicalQuantity}
     * @param beskrivning
     *            {@link String}
     * @return {@link Fk7263Observation}
     */
    private Fk7263Observation buildArbetsformageObservation(Kod kod, InternalLocalDateInterval period, PhysicalQuantity varde, String beskrivning) {
        Fk7263Observation obs = new Fk7263Observation();

        obs.setObservationskod(kod);
        obs.setObservationsperiod(DateTimeConverter.toPartialInterval(period));
        obs.getVarde().add(varde);
        if (beskrivning != null && !beskrivning.isEmpty()) {
            obs.setBeskrivning(beskrivning);
        }

        return obs;
    }

    /**
     * Creates a {@link Fk7263Observation} from information in the internal model.
     *
     * @param source
     *            internal representation
     * @return {@link Fk7263Observation}
     */
    private Fk7263Observation buildPrognos(Fk7263Intyg source) {
        Fk7263Observation prognos = new Fk7263Observation();
        Kod kod = getCorrespondingPrognosKod(source);
        if (kod == null) {
            LOG.trace("Got null while building prognos");
            return null;
        }
        prognos.setObservationskod(ObservationsKoder.PROGNOS);
        prognos.getVarde().add(kod);
        if (!isNullOrEmpty(source.getArbetsformagaPrognos())) {
            prognos.setBeskrivning(source.getArbetsformagaPrognos());
        }

        if (!isNullOrEmpty(source.getArbetsformagaPrognosGarInteAttBedomaBeskrivning())) {
            prognos.setKommentar(source.getArbetsformagaPrognosGarInteAttBedomaBeskrivning());
        }

        return prognos;
    }

    /**
     * Get the prognoskod that corresponds with the correct boolean in the internal model.
     *
     * @param source
     *            internal representation
     * @return {@link Kod}
     */
    private Kod getCorrespondingPrognosKod(Fk7263Intyg source) {
        Kod kod = null;
        if (source.getPrognosBedomning() != null) {
            switch (source.getPrognosBedomning()) {
            case arbetsformagaPrognosJa:
                kod = Prognoskoder.ATERSTALLAS_HELT;
                break;
            case arbetsformagaPrognosJaDelvis:
                kod = Prognoskoder.ATERSTALLAS_DELVIS;
                break;
            case arbetsformagaPrognosNej:
                kod = Prognoskoder.INTE_ATERSTALLAS;
                break;
            case arbetsformagaPrognosGarInteAttBedoma:
                kod = Prognoskoder.DET_GAR_INTE_ATT_BEDOMA;
                break;
            default:
            }
        }
        return kod;
    }

    /**
     * Create an ICD-10 diagnose code from a code string.
     *
     * @param code
     *            the code
     * @return {@link Kod} or null
     */
    private Kod buildDiagnoseCode(String code, String displayName) {
        if (code == null) {
            return null;
        }
        Kod kod = new Kod();
        kod.setCode(code);
        kod.setCodeSystem(ICD_10.getCodeSystem());
        kod.setCodeSystemName(ICD_10.getCodeSystemName());
        kod.setDisplayName(displayName);
        return kod;
    }

    /**
     * Create a single observation with kod, kategori and beskrivning.
     *
     * @param kod
     *            {@link Kod} Observationskod
     * @param kategori
     *            {@link Kod} Observationskategori
     * @param beskrivning
     *            {@link String}
     * @return {@link Fk7263Observation}
     */
    private Fk7263Observation buildObservation(Kod kod, Kod kategori, String beskrivning) {
        Fk7263Observation obs = new Fk7263Observation();
        if (kod != null) {
            obs.setObservationskod(kod);
        }
        if (kategori != null) {
            obs.setObservationskategori(kategori);
        }
        if (beskrivning != null) {
            obs.setBeskrivning(beskrivning);
        }

        return obs;
    }

    /**
     * Create a List of Fk7263Aktivitet[s] from information in the internal model.
     *
     * @param source
     *            internal representation
     * @return {@link List} of {@link Fk7263Aktivitet}-objects
     */
    private List<Fk7263Aktivitet> buildExternalAktiviteter(Fk7263Intyg source) {
        List<Fk7263Aktivitet> aktiviteter = new ArrayList<>();

        if (source.isAvstangningSmittskydd()) {
            aktiviteter.add(buildAktivitet(Aktivitetskoder.AVSTANGNING_ENLIGT_SML_PGA_SMITTA, null));
        }

        if (source.isRekommendationKontaktArbetsformedlingen()) {
            aktiviteter.add(buildAktivitet(Aktivitetskoder.PATIENTEN_BOR_FA_KONTAKT_MED_ARBETSFORMEDLINGEN, null));
        }
        if (source.isRekommendationKontaktForetagshalsovarden()) {
            aktiviteter.add(buildAktivitet(Aktivitetskoder.PATIENTEN_BOR_FA_KONTAKT_MED_FORETAGSHALSOVARDEN, null));
        }
        if (!isNullOrEmpty(source.getRekommendationOvrigt())) {
            aktiviteter.add(buildAktivitet(Aktivitetskoder.OVRIGT, source.getRekommendationOvrigt()));
        }
        if (!isNullOrEmpty(source.getAtgardInomSjukvarden())) {
            aktiviteter.add(buildAktivitet(
                    Aktivitetskoder.PLANERAD_ELLER_PAGAENDE_BEHANDLING_ELLER_ATGARD_INOM_SJUKVARDEN,
                    source.getAtgardInomSjukvarden()));
        }
        if (!isNullOrEmpty(source.getAnnanAtgard())) {
            aktiviteter.add(buildAktivitet(Aktivitetskoder.PLANERAD_ELLER_PAGAENDE_ANNAN_ATGARD,
                    source.getAnnanAtgard()));
        }
        if (source.getRehabilitering() != null) {
            switch (source.getRehabilitering()) {
            case rehabiliteringAktuell:
                aktiviteter.add(buildAktivitet(Aktivitetskoder.ARBETSLIVSINRIKTAD_REHABILITERING_AR_AKTUELL, null));
                break;
            case rehabiliteringEjAktuell:
                aktiviteter.add(buildAktivitet(Aktivitetskoder.ARBETSLIVSINRIKTAD_REHABILITERING_AR_INTE_AKTUELL, null));
                break;
            case rehabiliteringGarInteAttBedoma:
                aktiviteter.add(buildAktivitet(
                        Aktivitetskoder.GAR_EJ_ATT_BEDOMA_OM_ARBETSLIVSINRIKTAD_REHABILITERING_AR_AKTUELL, null));
                break;
            default:
            }
        }
        if (source.isRessattTillArbeteAktuellt()) {
            aktiviteter.add(buildAktivitet(Aktivitetskoder.FORANDRA_RESSATT_TILL_ARBETSPLATSEN_AR_AKTUELLT, null));
        }
        if (source.isRessattTillArbeteEjAktuellt()) {
            aktiviteter.add(buildAktivitet(Aktivitetskoder.FORANDRA_RESSATT_TILL_ARBETSPLATSEN_AR_EJ_AKTUELLT, null));
        }
        if (source.isKontaktMedFk()) {
            aktiviteter.add(buildAktivitet(Aktivitetskoder.KONTAKT_MED_FK_AR_AKTUELL, null));
        }

        return aktiviteter;
    }

    /**
     * Build singular Fk7263Aktivitet.
     *
     * @param kod
     *            {@link Kod} Aktivitetskod, always mandatory
     * @param beskrivning
     *            {@link String}, or null if not present
     * @return {@link Fk7263Aktivitet}
     */
    private Fk7263Aktivitet buildAktivitet(Kod kod, String beskrivning) {
        Fk7263Aktivitet akt = new Fk7263Aktivitet();

        akt.setAktivitetskod(kod);

        if (beskrivning != null) {
            akt.setBeskrivning(beskrivning);
        }

        return akt;
    }

    /**
     * Create an {@link Fk7263Patient} from the internal model.
     *
     * @param source
     *            internal representation
     * @return {@link Fk7263Patient}
     */
    private Fk7263Patient buildExternalPatient(Fk7263Intyg source) {
        Fk7263Patient patient = new Fk7263Patient();

        patient.setEfternamn(source.getIntygMetadata().getPatient().getFullstandigtNamn());
        patient.setId(InternalConverterUtil.createPersonId(source.getIntygMetadata().getPatient().getPersonId()));

        buildPatientSysselsattningar(patient, source);

        return patient;
    }

    /**
     * Create a List of Sysselsattning for an {@link Fk7263Patient}.
     *
     * @param patient
     *            the {@link se.inera.certificate.modules.fk7263.model.external.Fk7263Patient}
     * @param source
     *            internal representation
     */
    private void buildPatientSysselsattningar(Fk7263Patient patient, Fk7263Intyg source) {
        List<Sysselsattning> sysselsattningar = patient.getSysselsattningar();

        if (source.getNuvarandeArbetsuppgifter() != null) {
            Sysselsattning sysselsattning = new Sysselsattning();
            sysselsattning.setSysselsattningstyp(Sysselsattningskoder.NUVARANDE_ARBETE);
            patient.getArbetsuppgifter().add(buildArbetsuppgift(source.getNuvarandeArbetsuppgifter()));
            sysselsattningar.add(sysselsattning);
        }

        if (source.isArbetsloshet()) {
            Sysselsattning sysselsattning = new Sysselsattning();
            sysselsattning.setSysselsattningstyp(Sysselsattningskoder.ARBETSLOSHET);
            sysselsattningar.add(sysselsattning);
        }

        if (source.isForaldrarledighet() && patient.getId().getExtension() != null) {
            Sysselsattning sysselsattning = new Sysselsattning();
            if (isFemale(patient.getId().getExtension())) {
                sysselsattning.setSysselsattningstyp(Sysselsattningskoder.MAMMALEDIG);
            } else {
                sysselsattning.setSysselsattningstyp(Sysselsattningskoder.PAPPALEDIG);
            }
            sysselsattningar.add(sysselsattning);
        }
    }

    /**
     * Determine if the person connected with the personnummer is female or male.
     *
     * @param personnummer
     *            {@link String} containing the personnummer
     * @return true if the personnummer belongs to a female, and false otherwise
     */
    private Boolean isFemale(String personnummer) {
        String secondHalf = StringUtils.split(personnummer, "-")[1];
        String toParse = String.valueOf(secondHalf.charAt(2));
        int test = Integer.parseInt(toParse);
        return test % 2 == 0;
    }

    /**
     * Build an Arbetsuppgift from a string.
     *
     * @param type
     *            string describing the Arbetsuppgift
     * @return {@link Arbetsuppgift}
     */
    private Arbetsuppgift buildArbetsuppgift(String type) {
        Arbetsuppgift arbetsuppgift = new Arbetsuppgift();
        arbetsuppgift.setTypAvArbetsuppgift(type);

        return arbetsuppgift;
    }

    private boolean isNullOrEmpty(String string) {
        return string == null || string.isEmpty();
    }
}
