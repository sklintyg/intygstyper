package se.inera.certificate.modules.sjukersattning.model.internal;

import java.util.Objects;

public class Diagnos {

    private String diagnosKod;

    private String diagnosKodSystem;

    private String diagnosBeskrivning;

    public Diagnos() {
    }

    public Diagnos(String diagnosKod, String diagnosKodSystem, String diagnosBeskrivning) {
        this.diagnosKod = diagnosKod;
        this.diagnosKodSystem = diagnosKodSystem;
        this.diagnosBeskrivning = diagnosBeskrivning;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (getClass() != object.getClass()) {
            return false;
        }
        final Diagnos that = (Diagnos) object;
        return Objects.equals(this.diagnosKod, that.diagnosKod) && Objects.equals(this.diagnosKodSystem, that.diagnosKodSystem) &&
                Objects.equals(this.diagnosBeskrivning, that.diagnosBeskrivning);
    }

    @Override
    public int hashCode() {
        return Objects.hash(diagnosKod, diagnosKodSystem, diagnosBeskrivning);
    }

    public String getDiagnosKod() {
        return diagnosKod;
    }

    public void setDiagnosKod(String diagnosKod) {
        this.diagnosKod = diagnosKod;
    }

    public String getDiagnosKodSystem() {
        return diagnosKodSystem;
    }

    public void setDiagnosKodSystem(String diagnosKodSystem) {
        this.diagnosKodSystem = diagnosKodSystem;
    }

    public String getDiagnosBeskrivning() {
        return diagnosBeskrivning;
    }

    public void setDiagnosBeskrivning(String diagnosBeskrivning) {
        this.diagnosBeskrivning = diagnosBeskrivning;
    }
}
