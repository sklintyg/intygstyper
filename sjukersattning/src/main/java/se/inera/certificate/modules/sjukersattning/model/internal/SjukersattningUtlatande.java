package se.inera.certificate.modules.sjukersattning.model.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import se.inera.certificate.model.InternalDate;
import se.inera.certificate.model.common.internal.Utlatande;
import se.inera.certificate.modules.sjukersattning.support.SjukersattningEntryPoint;

public final class SjukersattningUtlatande extends Utlatande {
    private InternalDate undersokningAvPatienten;
    private InternalDate telefonkontaktMedPatienten;
    private InternalDate journaluppgifter;
    private InternalDate kannedomOmPatient;

    private List<Underlag> underlag = new ArrayList<>();

    private List<Diagnos> diagnoser = new ArrayList<>();

    private List<BehandlingsAtgard> atgarder = new ArrayList<>();

    private String diagnostisering;
    private boolean nyBedomningDiagnos;

    private List<Funktionsnedsattning> funktionsnedsattnings = new ArrayList<>();

    private String aktivitetsbegransning;

    private String pagaendeBehandling;
    private String avslutadBehandling;
    private String planeradBehandling;

    private String aktivitetsFormaga;
    private String prognos;

    private String ovrigt;
    private boolean kontaktMedFk;

    public SjukersattningUtlatande() {
        super();
        setTyp(SjukersattningEntryPoint.MODULE_ID);
        // TODO: remove
        // funktionsnedsattnings.add(new Funktionsnedsattning(Funktionsnedsattning.Funktionsomrade.ANNAN_KROPPSLIG,
        // "Helt lam"));
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
                Objects.deepEquals(this.underlag, that.underlag) &&
                Objects.deepEquals(this.diagnoser, that.diagnoser) &&
                Objects.deepEquals(this.atgarder, that.atgarder) &&
                Objects.equals(this.diagnostisering, that.diagnostisering) &&
                Objects.equals(this.nyBedomningDiagnos, that.nyBedomningDiagnos) &&
                Objects.deepEquals(this.funktionsnedsattnings, that.funktionsnedsattnings) &&
                Objects.equals(this.aktivitetsbegransning, that.aktivitetsbegransning) &&
                Objects.equals(this.pagaendeBehandling, that.pagaendeBehandling) &&
                Objects.equals(this.avslutadBehandling, that.avslutadBehandling) &&
                Objects.equals(this.planeradBehandling, that.planeradBehandling) &&
                Objects.equals(this.aktivitetsFormaga, that.aktivitetsFormaga) &&
                Objects.equals(this.prognos, that.prognos) &&
                Objects.equals(this.ovrigt, that.ovrigt) &&
                Objects.equals(this.kontaktMedFk, that.kontaktMedFk);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                this.undersokningAvPatienten, this.telefonkontaktMedPatienten, this.journaluppgifter,
                this.kannedomOmPatient, this.underlag, this.diagnoser, this.atgarder, this.diagnostisering,
                this.nyBedomningDiagnos, this.funktionsnedsattnings, this.aktivitetsbegransning,
                this.pagaendeBehandling, this.avslutadBehandling, this.planeradBehandling, this.aktivitetsFormaga,
                this.prognos, this.ovrigt, this.kontaktMedFk);
    }

    public List<Diagnos> getDiagnoser() {
        return diagnoser;
    }

    public void setDiagnoser(List<Diagnos> diagnoser) {
        this.diagnoser = diagnoser;
    }

    public List<BehandlingsAtgard> getAtgarder() {
        return atgarder;
    }

    public void setAtgarder(List<BehandlingsAtgard> atgarder) {
        this.atgarder = atgarder;
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

    public List<Funktionsnedsattning> getFunktionsnedsattnings() {
        return funktionsnedsattnings;
    }

    public void setFunktionsnedsattnings(List<Funktionsnedsattning> funktionsnedsattnings) {
        this.funktionsnedsattnings = funktionsnedsattnings;
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

    public String getOvrigt() {
        return ovrigt;
    }

    public void setOvrigt(String ovrigt) {
        this.ovrigt = ovrigt;
    }

    public String getAktivitetsFormaga() {
        return aktivitetsFormaga;
    }

    public void setAktivitetsFormaga(String aktivitetsFormaga) {
        this.aktivitetsFormaga = aktivitetsFormaga;
    }

    public String getPrognos() {
        return prognos;
    }

    public void setPrognos(String prognos) {
        this.prognos = prognos;
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
