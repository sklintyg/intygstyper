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

import java.util.EnumSet;
import java.util.stream.Stream;

public enum IntygAvserKod {

    C1("IAV1", "C1"),
    C1E("IAV2", "C1E"),
    C("IAV3", "C"),
    CE("IAV4", "CE"),
    D1("IAV5", "D1"),
    D1E("IAV6", "D1E"),
    D("IAV7", "D"),
    DE("IAV8", "DE"),
    TAXI("IAV9", "Taxi"),
    ANNAT("IAV10", "Annat"),
    AM("IAV11", "AM"),
    A1("IAV12", "A1"),
    A2("IAV13", "A2"),
    A("IAV14", "A"),
    B("IAV15", "B"),
    BE("IAV16", "BE"),
    TRAKTOR("IAV17", "Traktor");

    /** Körkortsbehörigheter som innefattar persontransport. */
    private static final EnumSet<IntygAvserKod> PERSONTRANSPORT = EnumSet.of(D1, D1E, D, DE, TAXI);

    /** Körkortsbehörigheter av högre typ. */
    private static final EnumSet<IntygAvserKod> HOGRE_KORKORTSBEHORIGHET = EnumSet.of(C1, C1E, C, CE, D1, D1E, D, DE, TAXI);

    final String code;
    final String description;

    IntygAvserKod(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static IntygAvserKod fromCode(String code) {
        return Stream.of(IntygAvserKod.values()).filter(s -> code.equals(s.getCode())).findFirst()
                .orElseThrow(() -> new IllegalArgumentException(code));
    }

    public static boolean isPersontransport(IntygAvserKod kod) {
        return PERSONTRANSPORT.contains(kod);
    }

    public static boolean isHogreKorkortsbehorighet(IntygAvserKod kod) {
        return HOGRE_KORKORTSBEHORIGHET.contains(kod);
    }
}
