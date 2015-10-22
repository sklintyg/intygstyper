package se.inera.certificate.modules.sjukersattning.model.internal;

import se.inera.certificate.model.InternalDate;
import se.inera.certificate.model.common.internal.Utlatande;
import se.inera.certificate.modules.sjukersattning.support.SjukersattningEntryPoint;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class SjukersattningUtlatande extends Utlatande {

    private InternalDate undersokningAvPatienten;
    private InternalDate telefonkontaktMedPatienten;
    private InternalDate journaluppgifter;
    private InternalDate kannedomOmPatient;

    private List<Underlag> underlag = new ArrayList<>();

    private String diagnosKod1;
    private String diagnosKodsystem1;
    private String diagnosBeskrivning1;
    private String diagnosYtterligareBeskrivning1;

    private String diagnosKod2;
    private String diagnosKodsystem2;
    private String diagnosBeskrivning2;
    private String diagnosYtterligareBeskrivning2;

    private String diagnosKod3;
    private String diagnosKodsystem3;
    private String diagnosBeskrivning3;
    private String diagnosYtterligareBeskrivning3;

    // TODO: behandlingsåtgärder

    private String diagnostisering;
    private boolean nyBedomningDiagnos;

    private String funktionsnedsattning;

    private String aktivitetsbegransning;

    private String pagaendeBehandling;
    private String avslutadBehandling;
    private String planeradBehandling;

    private String vadPatientenKanGora;
    private String prognosNarPatientKanAterga;

    private String kommentar;
    private boolean kontaktMedFk;

    public SjukersattningUtlatande() {
        super();
        setTyp(SjukersattningEntryPoint.MODULE_ID);
    }

    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (getClass() != object.getClass()) {
            return false;
        }
        final SjukersattningUtlatande that = (SjukersattningUtlatande) object;
        return Objects.equals(this.undersokningAvPatienten, that.undersokningAvPatienten) &&
                Objects.equals(this.telefonkontaktMedPatienten, that.telefonkontaktMedPatienten) &&
                Objects.equals(this.journaluppgifter, that.journaluppgifter) &&
                Objects.equals(this.kannedomOmPatient, that.kannedomOmPatient) &&
                Objects.equals(this.diagnosKod1, that.diagnosKod1) &&
                Objects.equals(this.diagnosBeskrivning1, that.diagnosBeskrivning1) &&
                Objects.equals(this.diagnosYtterligareBeskrivning1, that.diagnosYtterligareBeskrivning1) &&
                Objects.equals(this.diagnosKod2, that.diagnosKod2) &&
                Objects.equals(this.diagnosBeskrivning2, that.diagnosBeskrivning2) &&
                Objects.equals(this.diagnosYtterligareBeskrivning2, that.diagnosYtterligareBeskrivning2) &&
                Objects.equals(this.diagnosKod3, that.diagnosKod3) &&
                Objects.equals(this.diagnosBeskrivning3, that.diagnosBeskrivning3) &&
                Objects.equals(this.diagnosYtterligareBeskrivning3, that.diagnosYtterligareBeskrivning3) &&
                Objects.equals(this.diagnostisering, that.diagnostisering) &&
                Objects.equals(this.nyBedomningDiagnos, that.nyBedomningDiagnos) &&
                Objects.equals(this.funktionsnedsattning, that.funktionsnedsattning) &&
                Objects.equals(this.aktivitetsbegransning, that.aktivitetsbegransning) &&
                Objects.equals(this.pagaendeBehandling, that.pagaendeBehandling) &&
                Objects.equals(this.avslutadBehandling, that.avslutadBehandling) &&
                Objects.equals(this.planeradBehandling, that.planeradBehandling) &&
                Objects.equals(this.vadPatientenKanGora, that.vadPatientenKanGora) &&
                Objects.equals(this.prognosNarPatientKanAterga, that.prognosNarPatientKanAterga) &&
                Objects.equals(this.kommentar, that.kommentar) &&
                Objects.equals(this.kontaktMedFk, that.kontaktMedFk);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                this.undersokningAvPatienten, this.telefonkontaktMedPatienten, this.journaluppgifter, this.kannedomOmPatient,
                this.diagnosKod1, this.diagnosBeskrivning1, this.diagnosYtterligareBeskrivning1,
                this.diagnosKod2, this.diagnosBeskrivning2, this.diagnosYtterligareBeskrivning2,
                this.diagnosKod3, this.diagnosBeskrivning3, this.diagnosYtterligareBeskrivning3,
                this.diagnostisering, this.nyBedomningDiagnos, this.funktionsnedsattning, this.aktivitetsbegransning,
                this.pagaendeBehandling, this.avslutadBehandling, this.planeradBehandling,
                this.vadPatientenKanGora, this.prognosNarPatientKanAterga, this.kommentar, this.kontaktMedFk);
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

    public String getDiagnosYtterligareBeskrivning1() {
        return diagnosYtterligareBeskrivning1;
    }

    public void setDiagnosYtterligareBeskrivning1(String diagnosYtterligareBeskrivning1) {
        this.diagnosYtterligareBeskrivning1 = diagnosYtterligareBeskrivning1;
    }

    public String getDiagnosYtterligareBeskrivning2() {
        return diagnosYtterligareBeskrivning2;
    }

    public void setDiagnosYtterligareBeskrivning2(String diagnosYtterligareBeskrivning2) {
        this.diagnosYtterligareBeskrivning2 = diagnosYtterligareBeskrivning2;
    }

    public String getDiagnosYtterligareBeskrivning3() {
        return diagnosYtterligareBeskrivning3;
    }

    public void setDiagnosYtterligareBeskrivning3(String diagnosYtterligareBeskrivning3) {
        this.diagnosYtterligareBeskrivning3 = diagnosYtterligareBeskrivning3;
    }

    public String getAvslutadBehandling() {
        return avslutadBehandling;
    }

    public void setAvslutadBehandling(String avslutadBehandling) {
        this.avslutadBehandling = avslutadBehandling;
    }

    public List<Underlag> getUnderlag() {
        return underlag;
    }

    public void setUnderlag(List<Underlag> underlag) {
        this.underlag = underlag;
    }
}
