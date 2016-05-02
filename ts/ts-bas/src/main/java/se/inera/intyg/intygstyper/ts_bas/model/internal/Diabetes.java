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

public class Diabetes {

    private Boolean harDiabetes;

    private String diabetesTyp;

    private Boolean kost;

    private Boolean insulin;

    private Boolean tabletter;

    public Boolean getHarDiabetes() {
        return harDiabetes;
    }

    public void setHarDiabetes(Boolean harDiabetes) {
        this.harDiabetes = harDiabetes;
    }

    public String getDiabetesTyp() {
        return diabetesTyp;
    }

    public void setDiabetesTyp(String diabetesTyp) {
        this.diabetesTyp = diabetesTyp;
    }

    public Boolean getKost() {
        return kost;
    }

    public void setKost(Boolean kost) {
        this.kost = kost;
    }

    public Boolean getInsulin() {
        return insulin;
    }

    public void setInsulin(Boolean insulin) {
        this.insulin = insulin;
    }

    public Boolean getTabletter() {
        return tabletter;
    }

    public void setTabletter(Boolean tabletter) {
        this.tabletter = tabletter;
    }

}
