package se.inera.certificate.modules.fk7263.model.internal;

import static se.inera.certificate.model.util.Strings.emptyToNull;
import static se.inera.certificate.model.util.Strings.join;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

import se.inera.certificate.model.LocalDateInterval;

/**
 * @author andreaskaltenbach
 */
public class Fk7263Intyg {

    private String id;
    private LocalDateInterval giltighet;

    private LocalDateTime skickatDatum;

    private String patientNamn;
    private String patientPersonnummer;

    private boolean avstangningSmittskydd;

    private String diagnosKod;
    private String diagnosBeskrivning;

    private String wcDiagnosBeskrivning1;
    private String wcDiagnosKod2;
    private String wcDiagnosBeskrivning2;
    private String wcDiagnosKod3;
    private String wcDiagnosBeskrivning3;
    private Boolean wcFleraDiagnoser;

    private String sjukdomsforlopp;

    private String funktionsnedsattning;

    private LocalDate undersokningAvPatienten;
    private LocalDate telefonkontaktMedPatienten;
    private LocalDate journaluppgifter;
    private LocalDate annanReferens;

    private String annanReferensBeskrivning;

    private String aktivitetsbegransning;

    private boolean rekommendationKontaktArbetsformedlingen;
    private boolean rekommendationKontaktForetagshalsovarden;
    private String rekommendationOvrigt;

    private String atgardInomSjukvarden;
    private String annanAtgard;

    private boolean rehabiliteringAktuell;
    private boolean rehabiliteringEjAktuell;
    private boolean rehabiliteringGarInteAttBedoma;

    private String nuvarandeArbetsuppgifter;
    private boolean arbetsloshet;
    private boolean foraldrarledighet;

    private LocalDateInterval nedsattMed25;
    private LocalDateInterval nedsattMed50;
    private LocalDateInterval nedsattMed75;
    private LocalDateInterval nedsattMed100;

    private String arbetsformagaPrognos;

    private boolean arbetsformataPrognosJa;
    private boolean arbetsformataPrognosJaDelvis;
    private boolean arbetsformataPrognosNej;
    private boolean arbetsformataPrognosGarInteAttBedoma;

    private String prognosis;

    private String arbetsformagaPrognosGarInteAttBedomBeskrivning;

    private boolean ressattTillArbeteAktuellt;
    private boolean ressattTillArbeteEjAktuellt;

    private boolean kontaktMedFk;

    private String kommentar;

    private LocalDateTime signeringsdatum;

    private Vardperson vardperson;

    public boolean isAvstangningSmittskydd() {
        return avstangningSmittskydd;
    }

    public void setAvstangningSmittskydd(boolean avstangningSmittskydd) {
        this.avstangningSmittskydd = avstangningSmittskydd;
    }

    public String getDiagnosKod() {
        return diagnosKod;
    }

    public void setDiagnosKod(String diagnosKod) {
        this.diagnosKod = diagnosKod;
    }

    public String getDiagnosBeskrivning() {
        return diagnosBeskrivning;
    }

    public void setDiagnosBeskrivning(String diagnosBeskrivning) {
        this.diagnosBeskrivning = diagnosBeskrivning;
    }

    public String getWcDiagnosBeskrivning1() {
        return wcDiagnosBeskrivning1;
    }

    public void setWcDiagnosBeskrivning1(String wcDiagnosBeskrivning1) {
        this.wcDiagnosBeskrivning1 = wcDiagnosBeskrivning1;
    }

    public String getWcDiagnosKod2() {
        return wcDiagnosKod2;
    }

    public void setWcDiagnosKod2(String wcDiagnosKod2) {
        this.wcDiagnosKod2 = wcDiagnosKod2;
    }

    public String getWcDiagnosBeskrivning2() {
        return wcDiagnosBeskrivning2;
    }

    public void setWcDiagnosBeskrivning2(String wcDiagnosBeskrivning2) {
        this.wcDiagnosBeskrivning2 = wcDiagnosBeskrivning2;
    }

    public String getWcDiagnosKod3() {
        return wcDiagnosKod3;
    }

    public void setWcDiagnosKod3(String wcDiagnosKod3) {
        this.wcDiagnosKod3 = wcDiagnosKod3;
    }

    public String getWcDiagnosBeskrivning3() {
        return wcDiagnosBeskrivning3;
    }

    public void setWcDiagnosBeskrivning3(String wcDiagnosBeskrivning3) {
        this.wcDiagnosBeskrivning3 = wcDiagnosBeskrivning3;
    }

    public Boolean isWcFleraDiagnoser() {
        return wcFleraDiagnoser;
    }

    public void setWcFleraDiagnoser(Boolean wcFleraDiagnoser) {
        this.wcFleraDiagnoser = wcFleraDiagnoser;
    }

    public String getSjukdomsforlopp() {
        return sjukdomsforlopp;
    }

    public void setSjukdomsforlopp(String sjukdomsforlopp) {
        this.sjukdomsforlopp = sjukdomsforlopp;
    }

    public String getFunktionsnedsattning() {
        return funktionsnedsattning;
    }

    public void setFunktionsnedsattning(String funktionsnedsattning) {
        this.funktionsnedsattning = funktionsnedsattning;
    }

    public LocalDate getUndersokningAvPatienten() {
        return undersokningAvPatienten;
    }

    public void setUndersokningAvPatienten(LocalDate undersokningAvPatienten) {
        this.undersokningAvPatienten = undersokningAvPatienten;
    }

    public LocalDate getTelefonkontaktMedPatienten() {
        return telefonkontaktMedPatienten;
    }

    public void setTelefonkontaktMedPatienten(LocalDate telefonkontaktMedPatienten) {
        this.telefonkontaktMedPatienten = telefonkontaktMedPatienten;
    }

    public LocalDate getJournaluppgifter() {
        return journaluppgifter;
    }

    public void setJournaluppgifter(LocalDate journaluppgifter) {
        this.journaluppgifter = journaluppgifter;
    }

    public LocalDate getAnnanReferens() {
        return annanReferens;
    }

    public void setAnnanReferens(LocalDate annanReferens) {
        this.annanReferens = annanReferens;
    }

    public String getAktivitetsbegransning() {
        return aktivitetsbegransning;
    }

    public void setAktivitetsbegransning(String aktivitetsbegransning) {
        this.aktivitetsbegransning = aktivitetsbegransning;
    }

    public boolean isRekommendationKontaktArbetsformedlingen() {
        return rekommendationKontaktArbetsformedlingen;
    }

    public void setRekommendationKontaktArbetsformedlingen(boolean rekommendationKontaktArbetsformedlingen) {
        this.rekommendationKontaktArbetsformedlingen = rekommendationKontaktArbetsformedlingen;
    }

    public boolean isRekommendationKontaktForetagshalsovarden() {
        return rekommendationKontaktForetagshalsovarden;
    }

    public void setRekommendationKontaktForetagshalsovarden(boolean rekommendationKontaktForetagshalsovarden) {
        this.rekommendationKontaktForetagshalsovarden = rekommendationKontaktForetagshalsovarden;
    }

    public String getRekommendationOvrigt() {
        return rekommendationOvrigt;
    }

    public void setRekommendationOvrigt(String rekommendationOvrigt) {
        this.rekommendationOvrigt = rekommendationOvrigt;
    }

    public String getAtgardInomSjukvarden() {
        return atgardInomSjukvarden;
    }

    public void setAtgardInomSjukvarden(String atgardInomSjukvarden) {
        this.atgardInomSjukvarden = atgardInomSjukvarden;
    }

    public String getAnnanAtgard() {
        return annanAtgard;
    }

    public void setAnnanAtgard(String annanAtgard) {
        this.annanAtgard = annanAtgard;
    }

    public String getPatientNamn() {
        return patientNamn;
    }

    public void setPatientNamn(String patientNamn) {
        this.patientNamn = patientNamn;
    }

    public String getPatientPersonnummer() {
        return patientPersonnummer;
    }

    public void setPatientPersonnummer(String patientPersonnummer) {
        this.patientPersonnummer = patientPersonnummer;
    }

    public boolean isRehabiliteringAktuell() {
        return rehabiliteringAktuell;
    }

    public void setRehabiliteringAktuell(boolean rehabiliteringAktuell) {
        this.rehabiliteringAktuell = rehabiliteringAktuell;
    }

    public boolean isRehabiliteringEjAktuell() {
        return rehabiliteringEjAktuell;
    }

    public void setRehabiliteringEjAktuell(boolean rehabiliteringEjAktuell) {
        this.rehabiliteringEjAktuell = rehabiliteringEjAktuell;
    }

    public boolean isRehabiliteringGarInteAttBedoma() {
        return rehabiliteringGarInteAttBedoma;
    }

    public void setRehabiliteringGarInteAttBedoma(boolean rehabiliteringGarInteAttBedoma) {
        this.rehabiliteringGarInteAttBedoma = rehabiliteringGarInteAttBedoma;
    }

    public String getNuvarandeArbetsuppgifter() {
        return nuvarandeArbetsuppgifter;
    }

    public void setNuvarandeArbetsuppgifter(String nuvarandeArbetsuppgifter) {
        this.nuvarandeArbetsuppgifter = nuvarandeArbetsuppgifter;
    }

    public boolean isArbetsloshet() {
        return arbetsloshet;
    }

    public void setArbetsloshet(boolean arbetsloshet) {
        this.arbetsloshet = arbetsloshet;
    }

    public boolean isForaldrarledighet() {
        return foraldrarledighet;
    }

    public void setForaldrarledighet(boolean foraldrarledighet) {
        this.foraldrarledighet = foraldrarledighet;
    }

    public LocalDateInterval getNedsattMed25() {
        return nedsattMed25;
    }

    public void setNedsattMed25(LocalDateInterval nedsattMed25) {
        this.nedsattMed25 = nedsattMed25;
    }

    public LocalDateInterval getNedsattMed50() {
        return nedsattMed50;
    }

    public void setNedsattMed50(LocalDateInterval nedsattMed50) {
        this.nedsattMed50 = nedsattMed50;
    }

    public LocalDateInterval getNedsattMed75() {
        return nedsattMed75;
    }

    public void setNedsattMed75(LocalDateInterval nedsattMed75) {
        this.nedsattMed75 = nedsattMed75;
    }

    public LocalDateInterval getNedsattMed100() {
        return nedsattMed100;
    }

    public void setNedsattMed100(LocalDateInterval nedsattMed100) {
        this.nedsattMed100 = nedsattMed100;
    }

    public String getArbetsformagaPrognos() {
        return arbetsformagaPrognos;
    }

    public void setArbetsformagaPrognos(String arbetsformagaPrognos) {
        this.arbetsformagaPrognos = arbetsformagaPrognos;
    }

    public boolean isArbetsformataPrognosJa() {
        return arbetsformataPrognosJa;
    }

    public void setArbetsformataPrognosJa(boolean arbetsformataPrognosJa) {
        this.arbetsformataPrognosJa = arbetsformataPrognosJa;
    }

    public boolean isArbetsformataPrognosJaDelvis() {
        return arbetsformataPrognosJaDelvis;
    }

    public void setArbetsformataPrognosJaDelvis(boolean arbetsformataPrognosJaDelvis) {
        this.arbetsformataPrognosJaDelvis = arbetsformataPrognosJaDelvis;
    }

    public boolean isArbetsformataPrognosNej() {
        return arbetsformataPrognosNej;
    }

    public void setArbetsformataPrognosNej(boolean arbetsformataPrognosNej) {
        this.arbetsformataPrognosNej = arbetsformataPrognosNej;
    }

    public boolean isArbetsformataPrognosGarInteAttBedoma() {
        return arbetsformataPrognosGarInteAttBedoma;
    }

    public void setArbetsformataPrognosGarInteAttBedoma(boolean arbetsformataPrognosGarInteAttBedoma) {
        this.arbetsformataPrognosGarInteAttBedoma = arbetsformataPrognosGarInteAttBedoma;
    }

    public boolean isRessattTillArbeteAktuellt() {
        return ressattTillArbeteAktuellt;
    }

    public void setRessattTillArbeteAktuellt(boolean ressattTillArbeteAktuellt) {
        this.ressattTillArbeteAktuellt = ressattTillArbeteAktuellt;
    }

    public boolean isRessattTillArbeteEjAktuellt() {
        return ressattTillArbeteEjAktuellt;
    }

    public void setRessattTillArbeteEjAktuellt(boolean ressattTillArbeteEjAktuellt) {
        this.ressattTillArbeteEjAktuellt = ressattTillArbeteEjAktuellt;
    }

    public boolean isKontaktMedFk() {
        return kontaktMedFk;
    }

    public void setKontaktMedFk(boolean kontaktMedFk) {
        this.kontaktMedFk = kontaktMedFk;
    }

    public String getKommentar() {
        return kommentar;
    }

    public void setKommentar(String kommentar) {
        this.kommentar = kommentar;
    }

    public LocalDateTime getSigneringsdatum() {
        return signeringsdatum;
    }

    public void setSigneringsdatum(LocalDateTime signeringsdatum) {
        this.signeringsdatum = signeringsdatum;
    }

    public Vardperson getVardperson() {
        return vardperson;
    }

    public void setVardperson(Vardperson vardperson) {
        this.vardperson = vardperson;
    }

    public String getForskrivarkodOchArbetsplatskod() {
        return emptyToNull(join(" - ", vardperson.getForskrivarKod(), vardperson.getArbetsplatsKod()));
    }

    public String getNamnfortydligandeOchAdress() {
        return join("\n", vardperson.getNamn(),
                vardperson.getEnhetsnamn(),
                vardperson.getPostadress(),
                join(" ", vardperson.getPostnummer(), vardperson.getPostort()),
                vardperson.getTelefonnummer());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getSkickatDatum() {
        return skickatDatum;
    }

    public void setSkickatDatum(LocalDateTime skickatDatum) {
        this.skickatDatum = skickatDatum;
    }

    public LocalDateInterval getGiltighet() {
        return giltighet;
    }

    public void setGiltighet(LocalDateInterval giltighet) {
        this.giltighet = giltighet;
    }

    public String getArbetsformagaPrognosGarInteAttBedomBeskrivning() {
        return arbetsformagaPrognosGarInteAttBedomBeskrivning;
    }

    public void setArbetsformagaPrognosGarInteAttBedomBeskrivning(
            String arbetsformagaPrognosGarInteAttBedomBeskrivning) {
        this.arbetsformagaPrognosGarInteAttBedomBeskrivning = arbetsformagaPrognosGarInteAttBedomBeskrivning;
    }

    public String getPrognosis() {
        return prognosis;
    }

    public void setPrognosis(String prognosis) {
        this.prognosis = prognosis;
    }

    public String getAnnanReferensBeskrivning() {
        return annanReferensBeskrivning;
    }

    public void setAnnanReferensBeskrivning(String annanReferensBeskrivning) {
        this.annanReferensBeskrivning = annanReferensBeskrivning;
    }
}
