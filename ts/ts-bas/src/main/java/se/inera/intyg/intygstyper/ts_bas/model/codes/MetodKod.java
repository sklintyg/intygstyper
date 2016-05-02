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
package se.inera.intyg.intygstyper.ts_bas.model.codes;

/**
 * Represents all the codes used by this module to define HoSPersonal.
 */
public enum MetodKod {

    /** Donders konfrontationsmetod (används vid synfältsprövning). */
    DONDERS_KONFRONTATIONSMETOD("MET1", "Donders konfrontationsmetod");

    private static String codeSystemName = "kv_metod";

    private static String codeSystem = "b0c078c6-512a-42a5-ab42-a3380f369ac3";

    private static String codeSystemVersion = null;

    private String code;

    private String description;

    MetodKod(String code, String desc) {
        this.code = code;
        this.description = desc;
    }

    public String getCode() {
        return this.code;
    }

    public String getDescription() {
        return this.description;
    }

    public String getCodeSystem() {
        return codeSystem;
    }

    public String getCodeSystemName() {
        return codeSystemName;
    }

    public String getCodeSystemVersion() {
        return codeSystemVersion;
    }
}
