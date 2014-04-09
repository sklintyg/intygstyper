package se.inera.certificate.modules.fk7263.model.converter;

import static se.inera.certificate.modules.fk7263.model.codes.Kodverk.ICD_10;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.inera.certificate.model.Arbetsuppgift;
import se.inera.certificate.model.HosPersonal;
import se.inera.certificate.model.Id;
import se.inera.certificate.model.Kod;
import se.inera.certificate.model.LocalDateInterval;
import se.inera.certificate.model.PhysicalQuantity;
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
import se.inera.certificate.modules.fk7263.model.external.Fk7263Aktivitet;
import se.inera.certificate.modules.fk7263.model.external.Fk7263HosPersonal;
import se.inera.certificate.modules.fk7263.model.external.Fk7263Observation;
import se.inera.certificate.modules.fk7263.model.external.Fk7263Patient;
import se.inera.certificate.modules.fk7263.model.external.Fk7263Prognos;
import se.inera.certificate.modules.fk7263.model.external.Fk7263Utlatande;
import se.inera.certificate.modules.fk7263.model.external.Fk7263Vardenhet;
import se.inera.certificate.modules.fk7263.model.internal.Fk7263Intyg;
import se.inera.certificate.modules.fk7263.model.internal.Vardperson;

/**
 * Used to convert from internal to external model, i.e for converting input from webcert to a signable certificate
 * 
 * @author erik
 * 
 */
public class InternalToExternalConverter {

    private static final Logger LOG = LoggerFactory.getLogger(InternalToExternalConverter.class);

    private static final String PERS_ID_ROOT = "1.2.752.129.2.1.3.1";
    private static final String HSA_ID_ROOT = "1.2.752.129.2.1.4.1";
    private static final String ARBETSPLATSKOD_ROOT = "1.2.752.29.4.71";

    private static final Kod FK7263_TYP = new Kod("f6fb361a-e31d-48b8-8657-99b63912dd9b", "kv_utlatandetyp_intyg",
            null, "FK7263");

    /**
     * Converts from the internal to the external model
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
        utlatande.getReferenser().addAll(buildExternalReferenser(source));
        utlatande.getVardkontakter().addAll(buildExternalVardkontakter(source));

        List<String> kommentarer = buildKommentarer(source);
        if (!kommentarer.isEmpty()) {
            utlatande.getKommentarer().addAll(kommentarer);
        }

        utlatande.setPatient(buildExternalPatient(source));
        utlatande.setSigneringsdatum(source.getSigneringsdatum());
        utlatande.setSkickatdatum(source.getSkickatDatum());
        utlatande.setSkapadAv(buildExternalHosPersonal(source));
        return utlatande;

    }

    /**
     * Build a List of {@link String}[s] from different fields in the internal model
     * 
     * @return List of {@link String} information was found going into the kommentarer fields
     * @param source internal representation
     */
    private List<String> buildKommentarer(Fk7263Intyg source) {
        List<String> kommentarer = new ArrayList<>();

        if (!isNullOrEmpty(source.getKommentar())) {
            kommentarer.add(source.getKommentar());
        }
        
        if (!isNullOrEmpty(source.getArbetsformagaPrognosGarInteAttBedomBeskrivning())) {
            kommentarer.add(source.getArbetsformagaPrognosGarInteAttBedomBeskrivning());
        }
        
        if (!isNullOrEmpty(source.getAnnanReferensBeskrivning())) {
            kommentarer.add(source.getAnnanReferensBeskrivning());
        }

        return kommentarer;
    }

    /**
     * Create a List of {@link Vardkontakt} from the internal model
     * 
     * @return List of {@link Vardkontakt}
     * @param source internal representation
     */
    private List<Vardkontakt> buildExternalVardkontakter(Fk7263Intyg source) {
        List<Vardkontakt> vardkontakter = new ArrayList<>();

        if (source.getUndersokningAvPatienten() != null) {
            Vardkontakt vardkontakt = new Vardkontakt();
            vardkontakt.setVardkontaktstid(new LocalDateInterval(source.getUndersokningAvPatienten(),
                    source.getUndersokningAvPatienten()));
            vardkontakt.setVardkontakttyp(Vardkontakttypkoder.MIN_UNDERSOKNING_AV_PATIENTEN);
            vardkontakter.add(vardkontakt);
        }

        if (source.getTelefonkontaktMedPatienten() != null) {
            Vardkontakt vardkontakt = new Vardkontakt();
            vardkontakt.setVardkontaktstid(new LocalDateInterval(source.getTelefonkontaktMedPatienten(),
                    source.getTelefonkontaktMedPatienten()));
            vardkontakt.setVardkontakttyp(Vardkontakttypkoder.MIN_TELEFONKONTAKT_MED_PATIENTEN);
            vardkontakter.add(vardkontakt);
        }

        return vardkontakter;
    }

    /**
     * Create a List of {@link Referens} from the internal model
     * 
     * @return List of {@link Referens}
     * @param source internal representation
     */
    private List<Referens> buildExternalReferenser(Fk7263Intyg source) {
        List<Referens> referenser = new ArrayList<>();

        if (source.getJournaluppgifter() != null) {
            Referens referens = new Referens();
            referens.setReferenstyp(Referenstypkoder.JOURNALUPPGIFT);
            referens.setDatum(source.getJournaluppgifter());
            referenser.add(referens);

        }

        if (source.getAnnanReferens() != null) {
            Referens referens = new Referens();
            referens.setReferenstyp(Referenstypkoder.ANNAT);
            referens.setDatum(source.getAnnanReferens());
            referenser.add(referens);
        }

        return referenser;
    }

    /**
     * Create a List of {@link Fk7263Observation} from the internal model
     * 
     * @return List of {@link Fk7263Observation}
     * @param source internal representation
     */
    private List<Fk7263Observation> buildExternalObservationer(Fk7263Intyg source) {
        List<Fk7263Observation> observationer = new ArrayList<>();

        // observation huvudDiagnos
        if (source.getDiagnosKod() != null) {
            Kod kod = buildDiagnoseCode(source.getDiagnosKod());
            observationer.add(buildObservation(kod, ObservationsKoder.DIAGNOS, source.getDiagnosBeskrivning()));
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
                    source.getNedsattMed100(), buildPrognos(source), new PhysicalQuantity(0.0, "percent")));
        }
        if (source.getNedsattMed75() != null) {
            observationer.add(buildArbetsformageObservation(ObservationsKoder.ARBETSFORMAGA,
                    source.getNedsattMed75(), buildPrognos(source), new PhysicalQuantity(25.0, "percent")));
        }
        if (source.getNedsattMed50() != null) {
            observationer.add(buildArbetsformageObservation(ObservationsKoder.ARBETSFORMAGA,
                    source.getNedsattMed50(), buildPrognos(source), new PhysicalQuantity(50.0, "percent")));
        }
        if (source.getNedsattMed25() != null) {
            observationer.add(buildArbetsformageObservation(ObservationsKoder.ARBETSFORMAGA,
                    source.getNedsattMed25(), buildPrognos(source), new PhysicalQuantity(75.0, "percent")));
        }

        return observationer;
    }

    /**
     * Creates the slightly more complex {@link Fk7263Observation}[s] regarding arbetsformaga
     * 
     * @param kod
     *            a {@link Kod} from {@link ObservationsKoder}
     * @param period
     *            {@link LocalDateInterval}
     * @param prognos
     *            {@link Fk7263Prognos}
     * @param varde
     *            {@link PhysicalQuantity}
     * @return {@link Fk7263Observation}
     */
    private Fk7263Observation buildArbetsformageObservation(Kod kod, LocalDateInterval period, Fk7263Prognos prognos,
            PhysicalQuantity varde) {
        Fk7263Observation obs = new Fk7263Observation();

        obs.setObservationskod(kod);
        obs.setObservationsperiod(DateTimeConverter.toPartialInterval(period));

        if (prognos != null) {
            obs.getPrognoser().add(prognos);
        }

        obs.getVarde().add(varde);

        return obs;
    }

    /**
     * Creates a {@link Fk7263Prognos} from information in the internal model
     * 
     * @return {@link Fk7263Prognos}
     * @param source internal representation
     */
    private Fk7263Prognos buildPrognos(Fk7263Intyg source) {
        Fk7263Prognos prognos = new Fk7263Prognos();
        Kod kod = getCorrespondingPrognosKod(source);
        if (kod == null) {
            LOG.trace("Got null while building prognos");
            return null;
        }
        prognos.setPrognoskod(kod);
        prognos.setBeskrivning(source.getArbetsformagaPrognos());

        return prognos;
    }

    /**
     * Get the prognoskod that corresponds with the correct boolean in the internal model
     * 
     * @return {@link Kod}
     * @param source internal representation
     */
    private Kod getCorrespondingPrognosKod(Fk7263Intyg source) {
        Kod kod;
        if (source.isArbetsformataPrognosJa()) {
            kod = Prognoskoder.ATERSTALLAS_HELT;
        } else if (source.isArbetsformataPrognosJaDelvis()) {
            kod = Prognoskoder.ATERSTALLAS_DELVIS;
        } else if (source.isArbetsformataPrognosNej()) {
            kod = Prognoskoder.INTE_ATERSTALLAS;
        } else if (source.isArbetsformataPrognosGarInteAttBedoma()) {
            kod = Prognoskoder.DET_GAR_INTE_ATT_BEDOMA;
        } else {
            kod = null;
        }

        return kod;
    }

    /**
     * Create an ICD-10 diagnose code from a code string
     * 
     * @param code
     *            the code
     * @return {@link Kod} or null
     */
    private Kod buildDiagnoseCode(String code) {
        if (code == null) {
            return null;
        }
        Kod kod = new Kod();
        kod.setCode(code);
        kod.setCodeSystem(ICD_10.getCodeSystem());
        kod.setCodeSystemName(ICD_10.getCodeSystemName());
        return kod;
    }

    /**
     * Create a single observation with kod, kategori and beskrivning
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
     * Create a List of Fk7263Aktivitet[s] from information in the internal model
     * 
     * @return {@link List} of {@link Fk7263Aktivitet}-objects
     * @param source  internal representation
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
        if (source.isRehabiliteringAktuell()) {
            aktiviteter.add(buildAktivitet(Aktivitetskoder.ARBETSLIVSINRIKTAD_REHABILITERING_AR_AKTUELL, null));
        }
        if (source.isRehabiliteringEjAktuell()) {
            aktiviteter.add(buildAktivitet(Aktivitetskoder.ARBETSLIVSINRIKTAD_REHABILITERING_AR_INTE_AKTUELL, null));
        }
        if (source.isRehabiliteringGarInteAttBedoma()) {
            aktiviteter.add(buildAktivitet(
                    Aktivitetskoder.GAR_EJ_ATT_BEDOMA_OM_ARBETSLIVSINRIKTAD_REHABILITERING_AR_AKTUELL, null));
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
     * Build singular Fk7263Aktivitet
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
     * Create a HosPersonal object from information in the internal model
     * 
     * @return {@link HosPersonal}
     * @param source internal representation
     */
    private Fk7263HosPersonal buildExternalHosPersonal(Fk7263Intyg source) {
        Vardperson intVardperson = source.getVardperson();
        Fk7263HosPersonal hosPersonal = new Fk7263HosPersonal();

        hosPersonal.setNamn(intVardperson.getNamn());
        hosPersonal.setForskrivarkod(intVardperson.getForskrivarKod());
        hosPersonal.setId(new Id(HSA_ID_ROOT, intVardperson.getHsaId()));
        hosPersonal.setVardenhet(buildExternalVardenhet(intVardperson));

        return hosPersonal;
    }

    /**
     * Creates a Vardenhet from information in the internal model
     * 
     * @param intVardperson
     *            source of Vardenhet information
     * @return {@link Vardenhet}
     */
    private Fk7263Vardenhet buildExternalVardenhet(Vardperson intVardperson) {
        Fk7263Vardenhet vardenhet = new Fk7263Vardenhet();
        vardenhet.setArbetsplatskod(new Id(ARBETSPLATSKOD_ROOT, intVardperson.getArbetsplatsKod()));
        vardenhet.setEpost(intVardperson.getEpost());
        vardenhet.setId(new Id(HSA_ID_ROOT, intVardperson.getEnhetsId()));
        vardenhet.setNamn(intVardperson.getEnhetsnamn());
        vardenhet.setPostadress(intVardperson.getPostadress());
        vardenhet.setPostort(intVardperson.getPostort());
        vardenhet.setPostnummer(intVardperson.getPostnummer());
        vardenhet.setTelefonnummer(intVardperson.getTelefonnummer());
        vardenhet.setVardgivare(buildExternalVardgivare(intVardperson));

        return vardenhet;
    }

    /**
     * Creates a Vardgivare from information in the internal model
     * 
     * @param intVardperson
     *            source of Vardgivare information
     * @return {@link Vardgivare}
     */
    private Vardgivare buildExternalVardgivare(Vardperson intVardperson) {
        Vardgivare vardgivare = new Vardgivare();
        vardgivare.setId(new Id(HSA_ID_ROOT, intVardperson.getVardgivarId()));
        vardgivare.setNamn(intVardperson.getVardgivarnamn());

        return vardgivare;
    }

    /**
     * Create an {@link Fk7263Patient} from the internal model
     * 
     * @return {@link Fk7263Patient}
     * @param source internal representation
     */
    private Fk7263Patient buildExternalPatient(Fk7263Intyg source) {
        Fk7263Patient patient = new Fk7263Patient();

        patient.setEfternamn(source.getPatientNamn());
        patient.getFornamn().add("");
        patient.setId(new Id(PERS_ID_ROOT, source.getPatientPersonnummer()));
        buildPatientSysselsattningar(patient, source);

        return patient;
    }

    /**
     * Create a List of Sysselsattning for an {@link Fk7263Patient}
     *
     * @param patient
     *            the {@link se.inera.certificate.modules.fk7263.model.external.Fk7263Patient}
     * @param source  internal representation
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
            // TODO: change this implementation when a single code for foraldrarledighet is created
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
     * Build an Arbetsuppgift from a string
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
