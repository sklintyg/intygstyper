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

/**
 * Class encapsulating the values for synskarpa for a single eye.
 *
 * @author erik
 *
 */
public class Synskarpevarden {

    private Double utanKorrektion;
    private Double medKorrektion;
    private Boolean kontaktlins;

    public Double getUtanKorrektion() {
        return utanKorrektion;
    }

    public void setUtanKorrektion(Double utanKorrektion) {
        this.utanKorrektion = utanKorrektion;
    }

    public Double getMedKorrektion() {
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
