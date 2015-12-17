/*
 * Copyright (C) 2015 Inera AB (http://www.inera.se)
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

package se.inera.intyg.intygstyper.ts_diabetes.model.codes;

import java.util.EnumSet;

public enum KorkortsKod {

    /** Motorcykel (A). */
    A("A", ""),

    /** Lätt motorcykel (A1). */
    A1("A_1", ""),

    /** Mellanstor motorcykel (A2). */
    A2("A_2", ""),

    /** Moped klass 1 (AM). */
    AM("AM", ""),

    /** Personbil (B). */
    B("B", ""),

    /** Personbil, lätt lastbil och ett eller flera släpfordon, max 3.5 ton (BE). */
    BE("BE", ""),

    /** Traktor (TRAKTOR). */
    TRAKTOR("TRAKTOR", ""),

    /** Medeltung lastbil (C1). */
    C1("C_1", "Medeltung lastbil"),

    /** Medeltung lastbil med tungt släpfordon (C1E). */
    C1E("C_1_E", "Medeltung lastbil med tungt släpfordon"),

    /** Tung lastbil (C). */
    C("C", "Tung lastbil"),

    /** Tung lastbil med tungt släpfordon (CE). */
    CE("CE", "Tung lastbil med tungt släpfordon"),

    /** Mellanstor buss (D1). */
    D1("D_1", "Mellanstor buss"),

    /** Mellanstor buss med tungt släpfordon (D1E). */
    D1E("D_1_E", "Mellanstor buss med tungt släpfordon"),

    /** Buss (D). */
    D("D", "Buss"),

    /** Buss med tungt släpfordon (DE). */
    DE("DE", "Buss med tungt släpfordon"),

    /** Taxi (TAXI). */
    TAXI("TAXI", "Taxi"),

    /** Intyget avser inget av ovanstående (ANNAT). */
    ANNAT("ANNAT", "Intyget avser inget av ovanstående");

    /** Körkortsbehörigheter av högre typ. */
    public static final EnumSet<KorkortsKod> HOGRE_KORKORTSBEHORIGHET = EnumSet.of(C1, C1E, C, CE, D1, D1E, D, DE,
            TAXI);

    private static String codeSystemVersion = null;

    private String code;

    private String description;

    KorkortsKod(String code, String desc) {
        this.code = code;
        this.description = desc;
    }

    public static boolean isHogreBehorighet(String value) {
        for (KorkortsKod k: HOGRE_KORKORTSBEHORIGHET) {
            if (value.equals(k.name())) {
                return true;
            }
        }
        return false;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public String getCodeSystem() {
        return "24c41b8d-258a-46bf-a08a-b90738b28770";
    }

    public String getCodeSystemName() {
        return "kv_intyget_avser";
    }

    public String getCodeSystemVersion() {
        return codeSystemVersion;
    }

}
