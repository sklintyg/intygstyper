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

    private static Logger LOG = LoggerFactory.getLogger(InternalToExternalConverter.class);

    private static Fk7263Intyg intUtlatande;

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
     * @return {@link se.inera.certificate.modules.fk7263.model.external.Fk7263Utlatande}
     * @throws ConverterException 
     */
    public se.inera.certificate.modules.fk7263.model.external.Fk7263Utlatande convert(Fk7263Intyg source) throws ConverterException {
        LOG.trace("Starting conversion from internalToExternal");
        if (source == null) {
            throw new ConverterException("Internal intyg was null");
        }
        intUtlatande = source;

        se.inera.certificate.modules.fk7263.model.external.Fk7263Utlatande utlatande = new se.inera.certificate.modules.fk7263.model.external.Fk7263Utlatande();

        utlatande.setId(new Id(source.getId(), null));
        utlatande.setTyp(FK7263_TYP);

        utlatande.getAktiviteter().addAll(buildExternalAktiviteter());
        utlatande.getObservationer().addAll(buildExternalObservationer());
        utlatande.getReferenser().addAll(buildExternalReferenser());
        utlatande.getVardkontakter().addAll(buildExternalVardkontakter());

        List<String> kommentarer = buildKommentarer();
        if (kommentarer != null) {
            utlatande.getKommentarer().addAll(kommentarer);
        }

        utlatande.setPatient(buildExternalPatient());
        utlatande.setSigneringsdatum(intUtlatande.getSigneringsdatum());
        utlatande.setSkickatdatum(intUtlatande.getSkickatDatum());
        utlatande.setSkapadAv(buildExternalHosPersonal());
        return utlatande;

    }

    /**
     * Build a List of {@link String}[s] from different fields in the internal model
     * 
     * @return List of {@link String} or null if no information was found going into the kommentarer field
     */
    private List<String> buildKommentarer() {
        List<String> kommentarer = new ArrayList<>();

        if (!isNullOrEmpty(intUtlatande.getKommentar())) {
            kommentarer.add(intUtlatande.getKommentar());
        }
        
        if (!isNullOrEmpty(intUtlatande.getArbetsformagaPrognosGarInteAttBedomBeskrivning())) {
            kommentarer.add(intUtlatande.getArbetsformagaPrognosGarInteAttBedomBeskrivning());
        }
        
        if (!isNullOrEmpty(intUtlatande.getAnnanReferensBeskrivning())) {
            kommentarer.add(intUtlatande.getAnnanReferensBeskrivning());
        }

        return !kommentarer.isEmpty() ? kommentarer : null;
    }

    /**
     * Create a List of {@link Vardkontakt} from the internal model
     * 
     * @return List of {@link Vardkontakt}
     */
    private List<Vardkontakt> buildExternalVardkontakter() {
        List<Vardkontakt> vardkontakter = new ArrayList<>();

        if (intUtlatande.getUndersokningAvPatienten() != null) {
            Vardkontakt vardkontakt = new Vardkontakt();
            vardkontakt.setVardkontaktstid(new LocalDateInterval(intUtlatande.getUndersokningAvPatienten(),
                    intUtlatande.getUndersokningAvPatienten()));
            vardkontakt.setVardkontakttyp(Vardkontakttypkoder.MIN_UNDERSOKNING_AV_PATIENTEN);
            vardkontakter.add(vardkontakt);
        }

        if (intUtlatande.getTelefonkontaktMedPatienten() != null) {
            Vardkontakt vardkontakt = new Vardkontakt();
            vardkontakt.setVardkontaktstid(new LocalDateInterval(intUtlatande.getTelefonkontaktMedPatienten(),
                    intUtlatande.getTelefonkontaktMedPatienten()));
            vardkontakt.setVardkontakttyp(Vardkontakttypkoder.MIN_TELEFONKONTAKT_MED_PATIENTEN);
            vardkontakter.add(vardkontakt);
        }

        return vardkontakter;
    }

    /**
     * Create a List of {@link Referens} from the internal model
     * 
     * @return List of {@link Referens}
     */
    private List<Referens> buildExternalReferenser() {
        List<Referens> referenser = new ArrayList<>();

        if (intUtlatande.getJournaluppgifter() != null) {
            Referens referens = new Referens();
            referens.setReferenstyp(Referenstypkoder.JOURNALUPPGIFT);
            referens.setDatum(intUtlatande.getJournaluppgifter());
            referenser.add(referens);

        }

        if (intUtlatande.getAnnanReferens() != null) {
            Referens referens = new Referens();
            referens.setReferenstyp(Referenstypkoder.ANNAT);
            referens.setDatum(intUtlatande.getAnnanReferens());
            referenser.add(referens);
        }

        return referenser;
    }

    /**
     * Create a List of {@link Fk7263Observation} from the internal model
     * 
     * @return List of {@link Fk7263Observation}
     */
    private List<Fk7263Observation> buildExternalObservationer() {
        List<Fk7263Observation> observationer = new ArrayList<>();

        // observation huvudDiagnos
        if (intUtlatande.getDiagnosKod() != null) {
            Kod kod = buildDiagnoseCode(intUtlatande.getDiagnosKod());
            observationer.add(buildObservation(kod, ObservationsKoder.DIAGNOS, intUtlatande.getDiagnosBeskrivning()));
        }

        // observation sjukdomsforlopp
        if (!isNullOrEmpty(intUtlatande.getSjukdomsforlopp())) {
            observationer.add(buildObservation(ObservationsKoder.FORLOPP, null, intUtlatande.getSjukdomsforlopp()));
        }

        // observation funktionsnedsattning
        if (!isNullOrEmpty(intUtlatande.getFunktionsnedsattning())) {
            observationer.add(buildObservation(null, ObservationsKoder.KROPPSFUNKTIONER,
                    intUtlatande.getFunktionsnedsattning()));
        }

        // observation aktivitetsbegransning
        if (!isNullOrEmpty(intUtlatande.getAktivitetsbegransning())) {
            observationer.add(buildObservation(null, ObservationsKoder.AKTIVITETER_OCH_DELAKTIGHET,
                    intUtlatande.getAktivitetsbegransning()));
        }

        // observation arbetsformaga (create between 1 and 4 instances)
        if (intUtlatande.getNedsattMed100() != null) {
            observationer.add(buildArbetsformageObservation(ObservationsKoder.ARBETSFORMAGA,
                    intUtlatande.getNedsattMed100(), buildPrognos(), new PhysicalQuantity(0.0, "percent")));
        }
        if (intUtlatande.getNedsattMed75() != null) {
            observationer.add(buildArbetsformageObservation(ObservationsKoder.ARBETSFORMAGA,
                    intUtlatande.getNedsattMed75(), buildPrognos(), new PhysicalQuantity(25.0, "percent")));
        }
        if (intUtlatande.getNedsattMed50() != null) {
            observationer.add(buildArbetsformageObservation(ObservationsKoder.ARBETSFORMAGA,
                    intUtlatande.getNedsattMed50(), buildPrognos(), new PhysicalQuantity(50.0, "percent")));
        }
        if (intUtlatande.getNedsattMed25() != null) {
            observationer.add(buildArbetsformageObservation(ObservationsKoder.ARBETSFORMAGA,
                    intUtlatande.getNedsattMed25(), buildPrognos(), new PhysicalQuantity(75.0, "percent")));
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
     *            {@link Prognos}
     * @param varde
     *            {@link PhysicalQuantity}
     * @return {@link Fk7263Observation}
     */
    private Fk7263Observation buildArbetsformageObservation(Kod kod, LocalDateInterval period, Fk7263Prognos prognos,
            PhysicalQuantity varde) {
        Fk7263Observation obs = new Fk7263Observation();

        obs.setObservationskod(kod);
        obs.setObservationsperiod(period);

        if (prognos != null) {
            obs.getPrognoser().add(prognos);
        }

        obs.getVarde().add(varde);

        return obs;
    }

    /**
     * Creates a {@link Prognos} from information in the internal model
     * 
     * @return {@link Prognos}
     */
    private Fk7263Prognos buildPrognos() {
        Fk7263Prognos prognos = new Fk7263Prognos();
        Kod kod = getCorrespondingPrognosKod();
        if (kod == null) {
            LOG.trace("Got null while building prognos");
            return null;
        }
        prognos.setPrognoskod(kod);
        prognos.setBeskrivning(intUtlatande.getArbetsformagaPrognos());

        return prognos;
    }

    /**
     * Get the prognoskod that corresponds with the correct boolean in the internal model
     * 
     * @return {@link Kod} from {@link PrognosKoder}
     */
    private Kod getCorrespondingPrognosKod() {
        Kod kod = new Kod();
        if (intUtlatande.isArbetsformataPrognosJa()) {
            kod = Prognoskoder.ATERSTALLAS_HELT;
        } else if (intUtlatande.isArbetsformataPrognosJaDelvis()) {
            kod = Prognoskoder.ATERSTALLAS_DELVIS;

        } else if (intUtlatande.isArbetsformataPrognosNej()) {
            kod = Prognoskoder.INTE_ATERSTALLAS;

        } else if (intUtlatande.isArbetsformataPrognosGarInteAttBedoma()) {
            kod = Prognoskoder.DET_GAR_INTE_ATT_BEDOMA;
        } else {
            return null;
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
     */
    private List<Fk7263Aktivitet> buildExternalAktiviteter() {
        List<Fk7263Aktivitet> aktiviteter = new ArrayList<>();

        if (intUtlatande.isAvstangningSmittskydd()) {
            aktiviteter.add(buildAktivitet(Aktivitetskoder.AVSTANGNING_ENLIGT_SML_PGA_SMITTA, null));
        }

        if (intUtlatande.isRekommendationKontaktArbetsformedlingen()) {
            aktiviteter.add(buildAktivitet(Aktivitetskoder.PATIENTEN_BOR_FA_KONTAKT_MED_ARBETSFORMEDLINGEN, null));
        }
        if (intUtlatande.isRekommendationKontaktForetagshalsovarden()) {
            aktiviteter.add(buildAktivitet(Aktivitetskoder.PATIENTEN_BOR_FA_KONTAKT_MED_FORETAGSHALSOVARDEN, null));
        }
        if (!isNullOrEmpty(intUtlatande.getRekommendationOvrigt())) {
            aktiviteter.add(buildAktivitet(Aktivitetskoder.OVRIGT, intUtlatande.getRekommendationOvrigt()));
        }
        if (!isNullOrEmpty(intUtlatande.getAtgardInomSjukvarden())) {
            aktiviteter.add(buildAktivitet(
                    Aktivitetskoder.PLANERAD_ELLER_PAGAENDE_BEHANDLING_ELLER_ATGARD_INOM_SJUKVARDEN,
                    intUtlatande.getAtgardInomSjukvarden()));
        }
        if (!isNullOrEmpty(intUtlatande.getAnnanAtgard())) {
            aktiviteter.add(buildAktivitet(Aktivitetskoder.PLANERAD_ELLER_PAGAENDE_ANNAN_ATGARD,
                    intUtlatande.getAnnanAtgard()));
        }
        if (intUtlatande.isRehabiliteringAktuell()) {
            aktiviteter.add(buildAktivitet(Aktivitetskoder.ARBETSLIVSINRIKTAD_REHABILITERING_AR_AKTUELL, null));
        }
        if (intUtlatande.isRehabiliteringEjAktuell()) {
            aktiviteter.add(buildAktivitet(Aktivitetskoder.ARBETSLIVSINRIKTAD_REHABILITERING_AR_INTE_AKTUELL, null));
        }
        if (intUtlatande.isRehabiliteringGarInteAttBedoma()) {
            aktiviteter.add(buildAktivitet(
                    Aktivitetskoder.GAR_EJ_ATT_BEDOMA_OM_ARBETSLIVSINRIKTAD_REHABILITERING_AR_AKTUELL, null));
        }
        if (intUtlatande.isRessattTillArbeteAktuellt()) {
            aktiviteter.add(buildAktivitet(Aktivitetskoder.FORANDRA_RESSATT_TILL_ARBETSPLATSEN_AR_AKTUELLT, null));
        }
        if (intUtlatande.isRessattTillArbeteEjAktuellt()) {
            aktiviteter.add(buildAktivitet(Aktivitetskoder.FORANDRA_RESSATT_TILL_ARBETSPLATSEN_AR_EJ_AKTUELLT, null));
        }
        if (intUtlatande.isKontaktMedFk()) {
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
     */
    private Fk7263HosPersonal buildExternalHosPersonal() {
        Vardperson intVardperson = intUtlatande.getVardperson();
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
     */
    private Fk7263Patient buildExternalPatient() {
        Fk7263Patient patient = new Fk7263Patient();

        patient.setEfternamn(intUtlatande.getPatientNamn());
        patient.getFornamn().add("");
        patient.setId(new Id(PERS_ID_ROOT, intUtlatande.getPatientPersonnummer()));
        buildPatientSysselsattningar(patient);

        return patient;
    }

    /**
     * Create a List of Sysselsattning for an {@link Fk7263Patient}
     * 
     * @param patient
     *            the {@link Fk7263Patient}
     */
    private void buildPatientSysselsattningar(Fk7263Patient patient) {
        List<Sysselsattning> sysselsattningar = patient.getSysselsattningar();

        if (intUtlatande.getNuvarandeArbetsuppgifter() != null) {
            Sysselsattning sysselsattning = new Sysselsattning();
            sysselsattning.setSysselsattningstyp(Sysselsattningskoder.NUVARANDE_ARBETE);
            patient.getArbetsuppgifter().add(buildArbetsuppgift(intUtlatande.getNuvarandeArbetsuppgifter()));
            sysselsattningar.add(sysselsattning);

        }

        if (intUtlatande.isArbetsloshet()) {
            Sysselsattning sysselsattning = new Sysselsattning();
            sysselsattning.setSysselsattningstyp(Sysselsattningskoder.ARBETSLOSHET);
            sysselsattningar.add(sysselsattning);

        }

        if (intUtlatande.isForaldrarledighet() && patient.getId().getExtension() != null) {
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

    /**
     * Util method to check if a string is null or empty
     */
    private boolean isNullOrEmpty(String string) {
        if (string != null && !string.isEmpty()) {
            return false;

        } else {
            return true;
        }
    }

}
