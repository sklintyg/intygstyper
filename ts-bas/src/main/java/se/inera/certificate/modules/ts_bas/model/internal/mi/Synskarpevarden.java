package se.inera.certificate.modules.ts_bas.model.internal.mi;

/**
 * Class encapsulating the values for synskarpa for a single eye
 * 
 * @author erik
 * 
 */
public class Synskarpevarden {

    Double utanKorrektion;
    Double medKorrektion;
    Boolean kontaktlins;

    public double getUtanKorrektion() {
        return utanKorrektion;
    }

    public void setUtanKorrektion(Double utanKorrektion) {
        this.utanKorrektion = utanKorrektion;
    }

    public double getMedKorrektion() {
        return medKorrektion;
    }

    public void setMedKorrektion(Double medKorrektion) {
        this.medKorrektion = medKorrektion;
    }

    public Boolean getKontaktlins() {
        return kontaktlins;
    }

    public void setKontaktlins(Boolean kontaktlins) {
        this.kontaktlins = kontaktlins;
    }
}
