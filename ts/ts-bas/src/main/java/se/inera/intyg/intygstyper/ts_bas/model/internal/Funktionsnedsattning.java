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

public class Funktionsnedsattning {

    private Boolean funktionsnedsattning;

    private String beskrivning;

    //För att hjälpa passagerare in och ut samt med säkerhetsbälte
    private Boolean otillrackligRorelseformaga;

    public Boolean getFunktionsnedsattning() {
        return funktionsnedsattning;
    }

    public void setFunktionsnedsattning(Boolean funktionsnedsattning) {
        this.funktionsnedsattning = funktionsnedsattning;
    }

    public String getBeskrivning() {
        return beskrivning;
    }

    public void setBeskrivning(String beskrivning) {
        this.beskrivning = beskrivning;
    }

    public Boolean getOtillrackligRorelseformaga() {
        return otillrackligRorelseformaga;
    }

    public void setOtillrackligRorelseformaga(Boolean otillrackligRorelseformaga) {
        this.otillrackligRorelseformaga = otillrackligRorelseformaga;
    }


}
