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

import java.util.stream.Stream;

public enum IdKontrollKod {

    ID_KORT("IDK1", "ID-kort"),
    FORETAG_ELLER_TJANSTEKORT("IDK2", "Företagskort eller tjänstekort"),
    KORKORT("IDK3", "Svenskt körkort"),
    PERS_KANNEDOM("IDK4", "Personlig kännedom"),
    FORSAKRAN_KAP18("IDK5", "Försäkran enligt 18 kap. 4 §"),
    PASS("IDK6", "Pass");

    final String code;
    final String description;

    IdKontrollKod(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static IdKontrollKod fromCode(String code) {
        return Stream.of(IdKontrollKod.values()).filter(s -> code.equals(s.getCode())).findFirst()
                .orElseThrow(() -> new IllegalArgumentException(code));
    }
}
