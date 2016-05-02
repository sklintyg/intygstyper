/*
 * Copyright (C) 2016 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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
