/**
 * Copyright (C) 2013 Inera AB (http://www.inera.se)
 *
 * This file is part of Inera Certificate Modules (http://code.google.com/p/inera-certificate-modules).
 *
 * Inera Certificate Modules is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Inera Certificate Modules is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.inera.certificate.modules.ts_bas.model.codes;

/**
 * Represents all the codes used by this module to define HoSPersonal.
 */
public enum DiabetesKod {

    /** Donders konfrontationsmetod (används vid synfältsprövning). */
    DIABETES_TYP_1("TYP1"),
    DIABETES_TYP_2("TYP2");

    private String code;


    private DiabetesKod(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }
}
