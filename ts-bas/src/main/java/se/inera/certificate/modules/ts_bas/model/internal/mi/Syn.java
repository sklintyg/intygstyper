package se.inera.certificate.modules.ts_bas.model.internal.mi;

public class Syn {

    private Boolean synfaltsdefekter;

    private Boolean nattblindhet;

    private Boolean progressivOgonsjukdom;

    private Boolean diplopi;

    private Boolean nystagmus;

    private Synskarpevarden hogerOga;

    private Synskarpevarden vansterOga;

    private Synskarpevarden binokulart;
    
    private Boolean korrektionsglasensStyrka;
    
    // TODO: Add fields for synundersökning here

    public Boolean getSynfaltsdefekter() {
        return synfaltsdefekter;
    }

    public void setSynfaltsdefekter(Boolean synfaltsdefekter) {
        this.synfaltsdefekter = synfaltsdefekter;
    }

    public Boolean getNattblindhet() {
        return nattblindhet;
    }

    public void setNattblindhet(Boolean nattblindhet) {
        this.nattblindhet = nattblindhet;
    }

    public Boolean getProgressivOgonsjukdom() {
        return progressivOgonsjukdom;
    }

    public void setProgressivOgonsjukdom(Boolean progressivOgonsjukdom) {
        this.progressivOgonsjukdom = progressivOgonsjukdom;
    }

    public Boolean getDiplopi() {
        return diplopi;
    }

    public void setDiplopi(Boolean diplopi) {
        this.diplopi = diplopi;
    }

    public Boolean getNystagmus() {
        return nystagmus;
    }

    public void setNystagmus(Boolean nystagmus) {
        this.nystagmus = nystagmus;
    }

    public Synskarpevarden getSynskarpaHoger() {
        return this.hogerOga;
    }
    
    public void setSynskarpaHoger(Double utanKorr, Double medKorr, Boolean kontaktlins) {
        if (hogerOga == null) {
            hogerOga = new Synskarpevarden();
        }
        hogerOga.setUtanKorrektion(utanKorr);
        hogerOga.setMedKorrektion(medKorr != null ? medKorr : null);
        hogerOga.setKontatlins(kontaktlins);
    }

    public Synskarpevarden getSynskarpaVanster() {
        return this.vansterOga;
    }
    
    public void setSynskarpaVanster(Double utanKorr, Double medKorr, Boolean kontaktlins) {
        if (vansterOga == null) {
            vansterOga = new Synskarpevarden();
        }
        vansterOga.setUtanKorrektion(utanKorr);
        vansterOga.setMedKorrektion(medKorr != null ? medKorr : null);
        vansterOga.setKontatlins(kontaktlins);
    }
    
    public Synskarpevarden getSynskarpaBinokulart() {
        return this.binokulart;
    }
    
    public void setSynskarpaBinokulart(Double utanKorr, Double medKorr) {
        if (binokulart == null) {
            binokulart = new Synskarpevarden();
        }
        binokulart.setUtanKorrektion(utanKorr);
        binokulart.setMedKorrektion(medKorr != null ? medKorr : null);
        binokulart.setKontatlins(null);
    }

    public Boolean getKorrektionsglasensStyrka() {
        return korrektionsglasensStyrka;
    }

    public void setKorrektionsglasensStyrka(Boolean korrektionsglasensStyrka) {
        this.korrektionsglasensStyrka = korrektionsglasensStyrka;
    }
    
}
