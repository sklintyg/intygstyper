package se.inera.certificate.modules.ts_bas.model.internal;

public class Sjukhusvard {
    
    //har patienten vårdats på sjukhus eller haft kontakt med läkare med anledning av punkterna 1-13
    private Boolean sjukhusEllerLakarkontakt;
    
    private String tidpunkt;
    
    private String vardinrattning;
    
    private String anledning;

    public Boolean getSjukhusEllerLakarkontakt() {
        return sjukhusEllerLakarkontakt;
    }

    public void setSjukhusEllerLakarkontakt(Boolean sjukhusEllerLakarkontakt) {
        this.sjukhusEllerLakarkontakt = sjukhusEllerLakarkontakt;
    }

    public String getTidpunkt() {
        return tidpunkt;
    }

    public void setTidpunkt(String tidpunkt) {
        this.tidpunkt = tidpunkt;
    }

    public String getVardinrattning() {
        return vardinrattning;
    }

    public void setVardinrattning(String vardinrattning) {
        this.vardinrattning = vardinrattning;
    }

    public String getAnledning() {
        return anledning;
    }

    public void setAnledning(String anledning) {
        this.anledning = anledning;
    }
    
    

}
