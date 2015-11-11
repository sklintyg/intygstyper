package se.inera.certificate.modules.sjukpenning.model.internal;

import se.inera.certificate.model.InternalDate;
import se.inera.certificate.model.InternalLocalDateInterval;
import se.inera.certificate.model.common.internal.GrundData;
import se.inera.certificate.model.common.internal.Utlatande;
import se.inera.certificate.modules.sjukpenning.support.SjukpenningEntryPoint;

public class SjukpenningUtlatande implements Utlatande {

    private String typ;

    private String id;

    private GrundData grundData;

    private boolean avstangningSmittskydd;

    private InternalDate undersokningAvPatienten;
    private InternalDate telefonkontaktMedPatienten;
    private InternalDate journaluppgifter;

    private boolean nuvarandeArbete;
    private String nuvarandeArbetsuppgifter;

    private boolean arbetsloshet;
    private boolean foraldraledighet;
    private boolean studier;
    private boolean arbetsmarknadsProgram;

    private String diagnosKod1;
    private String diagnosKodsystem1;
    private String diagnosBeskrivning1;

    private String diagnosKod2;
    private String diagnosKodsystem2;
    private String diagnosBeskrivning2;

    private String diagnosKod3;
    private String diagnosKodsystem3;
    private String diagnosBeskrivning3;

    private String funktionsnedsattning;
    private String aktivitetsbegransning;

    private String pagaendeBehandling;
    private String planeradBehandling;

    private InternalLocalDateInterval nedsattMed25;
    private InternalLocalDateInterval nedsattMed50;
    private InternalLocalDateInterval nedsattMed75;
    private InternalLocalDateInterval nedsattMed100;

    private boolean ressattTillArbeteAktuellt;
    private boolean rekommendationOverSocialstyrelsensBeslutsstod;

    private String vadPatientenKanGora;
    private String prognosNarPatientKanAterga;

    private boolean atgardInteAktuellt;
    private boolean atgardArbetstraning;
    private boolean atgardArbetsanpassning;
    private boolean atgardSokaNyttArbete;
    private boolean atgardBesokPaArbete;
    private boolean atgardErgonomi;
    private boolean atgardHjalpmedel;
    private boolean atgardKonflikthantering;
    private boolean atgardOmfordelning;
    private boolean atgardForetagshalsovard;
    private boolean atgardOvrigt;

    private String kommentar;
    private boolean kontaktMedFk;

    // ==================================================================================================

    public SjukpenningUtlatande() {
        super();
        setTyp(SjukpenningEntryPoint.MODULE_ID);
    }

    public boolean isAvstangningSmittskydd() {
        return avstangningSmittskydd;
    }

    public void setAvstangningSmittskydd(boolean avstangningSmittskydd) {
        this.avstangningSmittskydd = avstangningSmittskydd;
    }

    public String getDiagnosKod1() {
        return diagnosKod1;
    }

    public void setDiagnosKod1(String diagnosKod) {
        this.diagnosKod1 = diagnosKod;
    }

    public String getDiagnosKodsystem1() {
        return diagnosKodsystem1;
    }

    public void setDiagnosKodsystem1(String diagnosKodsystem1) {
        this.diagnosKodsystem1 = diagnosKodsystem1;
    }

    public String getDiagnosBeskrivning1() {
        return diagnosBeskrivning1;
    }

    public void setDiagnosBeskrivning1(String diagnosBeskrivning) {
        this.diagnosBeskrivning1 = diagnosBeskrivning;
    }

    public String getDiagnosKod2() {
        return diagnosKod2;
    }

    public void setDiagnosKod2(String diagnosKod2) {
        this.diagnosKod2 = diagnosKod2;
    }

    public String getDiagnosBeskrivning2() {
        return diagnosBeskrivning2;
    }

    public void setDiagnosBeskrivning2(String diagnosBeskrivning2) {
        this.diagnosBeskrivning2 = diagnosBeskrivning2;
    }

    public String getDiagnosKod3() {
        return diagnosKod3;
    }

    public void setDiagnosKod3(String diagnosKod3) {
        this.diagnosKod3 = diagnosKod3;
    }

    public String getDiagnosBeskrivning3() {
        return diagnosBeskrivning3;
    }

    public void setDiagnosBeskrivning3(String diagnosBeskrivning3) {
        this.diagnosBeskrivning3 = diagnosBeskrivning3;
    }

    public String getDiagnosKodsystem2() {
        return diagnosKodsystem2;
    }

    public void setDiagnosKodsystem2(String diagnosKodsystem2) {
        this.diagnosKodsystem2 = diagnosKodsystem2;
    }

    public String getDiagnosKodsystem3() {
        return diagnosKodsystem3;
    }

    public void setDiagnosKodsystem3(String diagnosKodsystem3) {
        this.diagnosKodsystem3 = diagnosKodsystem3;
    }

    public String getFunktionsnedsattning() {
        return funktionsnedsattning;
    }

    public void setFunktionsnedsattning(String funktionsnedsattning) {
        this.funktionsnedsattning = funktionsnedsattning;
    }

    public InternalDate getUndersokningAvPatienten() {
        return undersokningAvPatienten;
    }

    public void setUndersokningAvPatienten(InternalDate undersokningAvPatienten) {
        this.undersokningAvPatienten = undersokningAvPatienten;
    }

    public InternalDate getTelefonkontaktMedPatienten() {
        return telefonkontaktMedPatienten;
    }

    public void setTelefonkontaktMedPatienten(InternalDate telefonkontaktMedPatienten) {
        this.telefonkontaktMedPatienten = telefonkontaktMedPatienten;
    }

    public InternalDate getJournaluppgifter() {
        return journaluppgifter;
    }

    public void setJournaluppgifter(InternalDate journaluppgifter) {
        this.journaluppgifter = journaluppgifter;
    }

    public String getAktivitetsbegransning() {
        return aktivitetsbegransning;
    }

    public void setAktivitetsbegransning(String aktivitetsbegransning) {
        this.aktivitetsbegransning = aktivitetsbegransning;
    }

    public String getPagaendeBehandling() {
        return pagaendeBehandling;
    }

    public void setPagaendeBehandling(String pagaendeBehandling) {
        this.pagaendeBehandling = pagaendeBehandling;
    }

    public String getPlaneradBehandling() {
        return planeradBehandling;
    }

    public void setPlaneradBehandling(String planeradBehandling) {
        this.planeradBehandling = planeradBehandling;
    }

    public boolean isNuvarandeArbete() {
        return nuvarandeArbete;
    }

    public void setNuvarandeArbete(boolean nuvarandeArbete) {
        this.nuvarandeArbete = nuvarandeArbete;
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

    public boolean isForaldraledighet() {
        return foraldraledighet;
    }

    public void setForaldraledighet(boolean foraldraledighet) {
        this.foraldraledighet = foraldraledighet;
    }

    public InternalLocalDateInterval getNedsattMed25() {
        return nedsattMed25;
    }

    public void setNedsattMed25(InternalLocalDateInterval nedsattMed25) {
        this.nedsattMed25 = nedsattMed25;
    }

    public InternalLocalDateInterval getNedsattMed50() {
        return nedsattMed50;
    }

    public void setNedsattMed50(InternalLocalDateInterval nedsattMed50) {
        this.nedsattMed50 = nedsattMed50;
    }

    public InternalLocalDateInterval getNedsattMed75() {
        return nedsattMed75;
    }

    public void setNedsattMed75(InternalLocalDateInterval nedsattMed75) {
        this.nedsattMed75 = nedsattMed75;
    }

    public InternalLocalDateInterval getNedsattMed100() {
        return nedsattMed100;
    }

    public void setNedsattMed100(InternalLocalDateInterval nedsattMed100) {
        this.nedsattMed100 = nedsattMed100;
    }

    public boolean isRessattTillArbeteAktuellt() {
        return ressattTillArbeteAktuellt;
    }

    public void setRessattTillArbeteAktuellt(boolean ressattTillArbeteAktuellt) {
        this.ressattTillArbeteAktuellt = ressattTillArbeteAktuellt;
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

    public boolean isStudier() {
        return studier;
    }

    public void setStudier(boolean studier) {
        this.studier = studier;
    }

    public boolean isArbetsmarknadsProgram() {
        return arbetsmarknadsProgram;
    }

    public void setArbetsmarknadsProgram(boolean arbetsmarknadsProgram) {
        this.arbetsmarknadsProgram = arbetsmarknadsProgram;
    }

    public boolean isRekommendationOverSocialstyrelsensBeslutsstod() {
        return rekommendationOverSocialstyrelsensBeslutsstod;
    }

    public void setRekommendationOverSocialstyrelsensBeslutsstod(boolean rekommendationOverSocialstyrelsensBeslutsstod) {
        this.rekommendationOverSocialstyrelsensBeslutsstod = rekommendationOverSocialstyrelsensBeslutsstod;
    }

    public String getVadPatientenKanGora() {
        return vadPatientenKanGora;
    }

    public void setVadPatientenKanGora(String vadPatientenKanGora) {
        this.vadPatientenKanGora = vadPatientenKanGora;
    }

    public String getPrognosNarPatientKanAterga() {
        return prognosNarPatientKanAterga;
    }

    public void setPrognosNarPatientKanAterga(String prognosNarPatientKanAterga) {
        this.prognosNarPatientKanAterga = prognosNarPatientKanAterga;
    }

    public boolean isAtgardInteAktuellt() {
        return atgardInteAktuellt;
    }

    public void setAtgardInteAktuellt(boolean atgardInteAktuellt) {
        this.atgardInteAktuellt = atgardInteAktuellt;
    }

    public boolean isAtgardArbetstraning() {
        return atgardArbetstraning;
    }

    public boolean isAtgardArbetsanpassning() {
        return atgardArbetsanpassning;
    }

    public void setAtgardArbetsanpassning(boolean atgardArbetsanpassning) {
        this.atgardArbetsanpassning = atgardArbetsanpassning;
    }

    public boolean isAtgardSokaNyttArbete() {
        return atgardSokaNyttArbete;
    }

    public void setAtgardSokaNyttArbete(boolean atgardSokaNyttArbete) {
        this.atgardSokaNyttArbete = atgardSokaNyttArbete;
    }

    public boolean isAtgardBesokPaArbete() {
        return atgardBesokPaArbete;
    }

    public void setAtgardBesokPaArbete(boolean atgardBesokPaArbete) {
        this.atgardBesokPaArbete = atgardBesokPaArbete;
    }

    public boolean isAtgardErgonomi() {
        return atgardErgonomi;
    }

    public void setAtgardErgonomi(boolean atgardErgonomi) {
        this.atgardErgonomi = atgardErgonomi;
    }

    public boolean isAtgardHjalpmedel() {
        return atgardHjalpmedel;
    }

    public void setAtgardHjalpmedel(boolean atgardHjalpmedel) {
        this.atgardHjalpmedel = atgardHjalpmedel;
    }

    public boolean isAtgardKonflikthantering() {
        return atgardKonflikthantering;
    }

    public void setAtgardKonflikthantering(boolean atgardKonflikthantering) {
        this.atgardKonflikthantering = atgardKonflikthantering;
    }

    public boolean isAtgardOmfordelning() {
        return atgardOmfordelning;
    }

    public void setAtgardOmfordelning(boolean atgardOmfordelning) {
        this.atgardOmfordelning = atgardOmfordelning;
    }

    public boolean isAtgardForetagshalsovard() {
        return atgardForetagshalsovard;
    }

    public void setAtgardForetagshalsovard(boolean atgardForetagshalsovard) {
        this.atgardForetagshalsovard = atgardForetagshalsovard;
    }

    public boolean isAtgardOvrigt() {
        return atgardOvrigt;
    }

    public void setAtgardOvrigt(boolean atgardOvrigt) {
        this.atgardOvrigt = atgardOvrigt;
    }

    public void setAtgardArbetstraning(boolean atgardArbetstraning) {
        this.atgardArbetstraning = atgardArbetstraning;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getTyp() {
        return typ;
    }

    @Override
    public GrundData getGrundData() {
        return grundData;
    }

    public void setGrundData(GrundData grundData) {
        this.grundData = grundData;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTyp(String typ) {
        this.typ = typ;
    }

}
