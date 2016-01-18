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

public class Diabetes {

    private String diabetestyp;
    private String observationsperiod;
    private Boolean endastKost;
    private Boolean tabletter;
    private Boolean insulin;
    private String insulinBehandlingsperiod;
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

    public String getAnnanBehandlingBeskrivning() {
        return annanBehandlingBeskrivning;
    }

    public void setAnnanBehandlingBeskrivning(String annanBehandlingBeskrivning) {
        this.annanBehandlingBeskrivning = annanBehandlingBeskrivning;
    }

}
