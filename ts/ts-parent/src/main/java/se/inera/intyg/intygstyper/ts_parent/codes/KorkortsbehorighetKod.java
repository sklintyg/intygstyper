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

public enum KorkortsbehorighetKod {

    C1("VAR1", "C1"),
    C1E("VAR2", "C1E"),
    C("VAR3", "C"),
    CE("VAR4", "CE"),
    D1("VAR5", "D1"),
    D1E("VAR6", "D1E"),
    D("VAR7", "D"),
    DE("VAR8", "DE"),
    TAXI("VAR9", "Taxi"),
    ANNAT("VAR10", "Annat (AM, A1, A2, A, B, BE eller Traktor)"),
    KANINTETEASTALLNING("VAR11", "Kan inte ta ställning"),
    AM("VAR12", "AM"),
    A1("VAR13", "A1"),
    A2("VAR14", "A2"),
    A("VAR15", "A"),
    B("VAR16", "B"),
    BE("VAR17", "BE"),
    TRAKTOR("VAR18", "Traktor");

    final String code;
    final String description;

    KorkortsbehorighetKod(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
