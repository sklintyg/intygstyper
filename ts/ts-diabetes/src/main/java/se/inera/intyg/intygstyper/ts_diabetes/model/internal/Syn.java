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

package se.inera.intyg.intygstyper.ts_diabetes.model.internal;

public class Syn {

    private Boolean separatOgonlakarintyg;

    private Boolean synfaltsprovningUtanAnmarkning;

    private Synskarpevarden hoger;

    private Synskarpevarden vanster;

    private Synskarpevarden binokulart;

    private Boolean diplopi;

    private Boolean synfaltsprovning;

    private Boolean provningOgatsRorlighet;

    public Boolean getSeparatOgonlakarintyg() {
        return separatOgonlakarintyg;
    }

    public void setSeparatOgonlakarintyg(Boolean separatOgonlakarintyg) {
        this.separatOgonlakarintyg = separatOgonlakarintyg;
    }

    public Boolean getSynfaltsprovningUtanAnmarkning() {
        return synfaltsprovningUtanAnmarkning;
    }

    public void setSynfaltsprovningUtanAnmarkning(Boolean synfaltsprovningUtanAnmarkning) {
        this.synfaltsprovningUtanAnmarkning = synfaltsprovningUtanAnmarkning;
    }

    public void setHoger(Double utanKorr, Double medKorr) {
        if (hoger == null) {
            hoger = new Synskarpevarden();
        }
        hoger.setUtanKorrektion(utanKorr);
        hoger.setMedKorrektion(medKorr != null ? medKorr : null);
    }

    public void setVanster(Double utanKorr, Double medKorr) {
        if (vanster == null) {
            vanster = new Synskarpevarden();
        }
        vanster.setUtanKorrektion(utanKorr);
        vanster.setMedKorrektion(medKorr != null ? medKorr : null);
    }

    public void setBinokulart(Double utanKorr, Double medKorr) {
        if (binokulart == null) {
            binokulart = new Synskarpevarden();
        }
        binokulart.setUtanKorrektion(utanKorr);
        binokulart.setMedKorrektion(medKorr != null ? medKorr : null);
    }

    public Synskarpevarden getHoger() {
        return hoger;
    }

    public void setHoger(Synskarpevarden hoger) {
        this.hoger = hoger;
    }

    public Synskarpevarden getVanster() {
        return vanster;
    }

    public void setVanster(Synskarpevarden vanster) {
        this.vanster = vanster;
    }

    public Synskarpevarden getBinokulart() {
        return binokulart;
    }

    public void setBinokulart(Synskarpevarden binokulart) {
        this.binokulart = binokulart;
    }

    public Boolean getDiplopi() {
        return diplopi;
    }

    public void setDiplopi(Boolean diplopi) {
        this.diplopi = diplopi;
    }

    public Boolean getSynfaltsprovning() {
        return synfaltsprovning;
    }

    public void setSynfaltsprovning(Boolean synfaltsprovning) {
        this.synfaltsprovning = synfaltsprovning;
    }

    public Boolean getProvningOgatsRorlighet() {
        return provningOgatsRorlighet;
    }

    public void setProvningOgatsRorlighet(Boolean provningOgatsRorlighet) {
        this.provningOgatsRorlighet = provningOgatsRorlighet;
    }

}
