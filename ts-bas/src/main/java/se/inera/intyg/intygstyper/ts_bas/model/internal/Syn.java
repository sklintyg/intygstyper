package se.inera.intyg.intygstyper.ts_bas.model.internal;

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

    public void setSynskarpaHoger(Double utanKorr, Double medKorr, Boolean kontaktlins) {
        if (hogerOga == null) {
            hogerOga = new Synskarpevarden();
        }
        hogerOga.setUtanKorrektion(utanKorr);
        hogerOga.setMedKorrektion(medKorr);
        hogerOga.setKontaktlins(kontaktlins);
    }

    public void setSynskarpaVanster(Double utanKorr, Double medKorr, Boolean kontaktlins) {
        if (vansterOga == null) {
            vansterOga = new Synskarpevarden();
        }
        vansterOga.setUtanKorrektion(utanKorr);
        vansterOga.setMedKorrektion(medKorr);
        vansterOga.setKontaktlins(kontaktlins);
    }

    public void setSynskarpaBinokulart(Double utanKorr, Double medKorr) {
        if (binokulart == null) {
            binokulart = new Synskarpevarden();
        }
        binokulart.setUtanKorrektion(utanKorr);
        binokulart.setMedKorrektion(medKorr);
        binokulart.setKontaktlins(null);
    }

    public Boolean getKorrektionsglasensStyrka() {
        return korrektionsglasensStyrka;
    }

    public void setKorrektionsglasensStyrka(Boolean korrektionsglasensStyrka) {
        this.korrektionsglasensStyrka = korrektionsglasensStyrka;
    }

    public Synskarpevarden getHogerOga() {
        return hogerOga;
    }

    public void setHogerOga(Synskarpevarden hogerOga) {
        this.hogerOga = hogerOga;
    }

    public Synskarpevarden getVansterOga() {
        return vansterOga;
    }

    public void setVansterOga(Synskarpevarden vansterOga) {
        this.vansterOga = vansterOga;
    }

    public Synskarpevarden getBinokulart() {
        return binokulart;
    }

    public void setBinokulart(Synskarpevarden binokulart) {
        this.binokulart = binokulart;
    }

}
