package se.inera.certificate.modules.ts_diabetes.model.internal;

public class Diabetes {

    private String diabetestyp;
    private String observationsperiod;
    private Boolean endastKost;
    private Boolean tabletter;
    private Boolean insulin;
    private String insulinBehandlingsperiod;
    private Boolean annanBehandling;
    private String annanBehandlingBeskrivning;

    public String getDiabetestyp() {
        return diabetestyp;
    }

    public void setDiabetestyp(String diabetestyp) {
        this.diabetestyp = diabetestyp;
    }

    public String getObservationsperiod() {
        return observationsperiod;
    }

    public void setObservationsperiod(String observationsperiod) {
        this.observationsperiod = observationsperiod;
    }

    public Boolean getEndastKost() {
        return endastKost;
    }

    public void setEndastKost(Boolean endastKost) {
        this.endastKost = endastKost;
    }

    public Boolean getTabletter() {
        return tabletter;
    }

    public void setTabletter(Boolean tabletter) {
        this.tabletter = tabletter;
    }

    public Boolean getInsulin() {
        return insulin;
    }

    public void setInsulin(Boolean insulin) {
        this.insulin = insulin;
    }

    public String getInsulinBehandlingsperiod() {
        return insulinBehandlingsperiod;
    }

    public void setInsulinBehandlingsperiod(String insulinBehandlingsperiod) {
        this.insulinBehandlingsperiod = insulinBehandlingsperiod;
    }

    public Boolean getAnnanBehandling() {
        return annanBehandling;
    }

    public void setAnnanBehandling(Boolean annanBehandling) {
        this.annanBehandling = annanBehandling;
    }

    public String getAnnanBehandlingBeskrivning() {
        return annanBehandlingBeskrivning;
    }

    public void setAnnanBehandlingBeskrivning(String annanBehandlingBeskrivning) {
        this.annanBehandlingBeskrivning = annanBehandlingBeskrivning;
    }

}
