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

package se.inera.intyg.intygstyper.ts_parent.codes;

public enum IntygAvserEnum {

    C1("IAV1", "Medeltung lastbil och enbart ett lätt släpfordon"),
    C1E("IAV2", "Medeltung lastbil och ett eller flera släpfordon oavsett vikt"),
    C("IAV3", "Tung lastbil och enbart ett lätt släpfordon"),
    CE("IAV4", "Tung lastbil och ett eller flera släpfordon oavsett vikt"),
    D1("IAV5", "Mellanstor buss"),
    D1E("IAV6", "Mellanstor buss och ett eller flera släpfordon oavsett vikt"),
    D("IAV7", "Buss"),
    DE("IAV8", "Buss och enbart ett lätt släpfordon"),
    TAXI("IAV9", "Taxiförarlegitimation"),
    ANNAT("IAV10", "Intyget kan också användas när Transportstyrelsen i andra fall begärt ett läkarintyg"),
    AM("IAV11", "Moped klass I"),
    A1("IAV12", "Lätt motorcykel"),
    A2("IAV13", "Mellanstor motorcykel"),
    A("IAV14", "Motorcykel"),
    B("IAV15", "Personbil och lätt lastbil"),
    BE("IAV16", "Personbil, lätt lastbil och ett eller flera släpfordon"),
    TRAKTOR("IAV17", "Traktor");

    private String code;
    private String description;

    IntygAvserEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
