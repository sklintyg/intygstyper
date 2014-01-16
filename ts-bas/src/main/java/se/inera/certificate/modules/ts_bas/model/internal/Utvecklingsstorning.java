package se.inera.certificate.modules.ts_bas.model.internal;

public class Utvecklingsstorning {
    
    private Boolean psykiskUtvecklingsstorning;
    
    //ADHD, Aspergers syndrom, DAMP etc.
    private Boolean harSyndrom;

    public Boolean getPsykiskUtvecklingsstorning() {
        return psykiskUtvecklingsstorning;
    }

    public void setPsykiskUtvecklingsstorning(Boolean psykiskUtvecklingsstorning) {
        this.psykiskUtvecklingsstorning = psykiskUtvecklingsstorning;
    }

    public Boolean getHarSyndrom() {
        return harSyndrom;
    }

    public void setHarSyndrom(Boolean harSyndrom) {
        this.harSyndrom = harSyndrom;
    }
    
}
