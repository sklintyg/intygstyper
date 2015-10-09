package se.inera.certificate.modules.sjukersattning.model.internal;

import se.inera.certificate.model.InternalDate;
import se.inera.certificate.model.common.internal.Utlatande;
import se.inera.certificate.modules.sjukersattning.support.SjukersattningEntryPoint;

public class SjukersattningUtlatande extends Utlatande {

    private InternalDate undersokningAvPatienten;
    private InternalDate telefonkontaktMedPatienten;
    private InternalDate journaluppgifter;
    private InternalDate kannedomOmPatient;

    // TODO: annat underlag för behandling

    private String diagnosKod1;
    private String diagnosKodsystem1;
    private String diagnosBeskrivning1;

    private String diagnosKod2;
    private String diagnosKodsystem2;
    private String diagnosBeskrivning2;

    private String diagnosKod3;
    private String diagnosKodsystem3;
    private String diagnosBeskrivning3;

    // TODO: behandlingsåtgärder

    private String diagnostisering;
    private boolean nyBedomningDiagnos;

    private String funktionsnedsattning;

    private String aktivitetsbegransning;

    private String pagaendeBehandling;
    private String planeradBehandling;

    private String vadPatientenKanGora;
    private String prognosNarPatientKanAterga;

    private String kommentar;
    private boolean kontaktMedFk;

    // ==================================================================================================

    public SjukersattningUtlatande() {
        super();
        setTyp(SjukersattningEntryPoint.MODULE_ID);
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

    public InternalDate getKannedomOmPatient() {
        return kannedomOmPatient;
    }

    public void setKannedomOmPatient(InternalDate kannedomOmPatient) {
        this.kannedomOmPatient = kannedomOmPatient;
    }

    public String getDiagnostisering() {
        return diagnostisering;
    }

    public void setDiagnostisering(String diagnostisering) {
        this.diagnostisering = diagnostisering;
    }

    public boolean isNyBedomningDiagnos() {
        return nyBedomningDiagnos;
    }

    public void setNyBedomningDiagnos(boolean nyBedomningDiagnos) {
        this.nyBedomningDiagnos = nyBedomningDiagnos;
    }

}
