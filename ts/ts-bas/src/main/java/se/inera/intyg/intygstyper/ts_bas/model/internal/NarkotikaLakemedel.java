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

public class NarkotikaLakemedel {

    private Boolean teckenMissbruk;

    private Boolean foremalForVardinsats;

    private Boolean provtagningBehovs;

    private Boolean lakarordineratLakemedelsbruk;

    private String lakemedelOchDos;

    public Boolean getTeckenMissbruk() {
        return teckenMissbruk;
    }

    public void setTeckenMissbruk(Boolean teckenMissbruk) {
        this.teckenMissbruk = teckenMissbruk;
    }

    public Boolean getForemalForVardinsats() {
        return foremalForVardinsats;
    }

    public void setForemalForVardinsats(Boolean foremalForVardinsats) {
        this.foremalForVardinsats = foremalForVardinsats;
    }

    public Boolean getProvtagningBehovs() {
        return provtagningBehovs;
    }

    public void setProvtagningBehovs(Boolean provtagningBehovs) {
        this.provtagningBehovs = provtagningBehovs;
    }

    public Boolean getLakarordineratLakemedelsbruk() {
        return lakarordineratLakemedelsbruk;
    }

    public void setLakarordineratLakemedelsbruk(Boolean lakarordineratLakemedelsbruk) {
        this.lakarordineratLakemedelsbruk = lakarordineratLakemedelsbruk;
    }

    public String getLakemedelOchDos() {
        return lakemedelOchDos;
    }

    public void setLakemedelOchDos(String lakemedelOchDos) {
        this.lakemedelOchDos = lakemedelOchDos;
    }


}
