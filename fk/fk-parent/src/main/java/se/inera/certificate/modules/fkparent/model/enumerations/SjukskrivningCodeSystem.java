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
package se.inera.certificate.modules.fkparent.model.enumerations;

public enum SjukskrivningCodeSystem {
    KV_FKMU_0003_1("1", "Helt nedsatt"),
    KV_FKMU_0003_2("2", "Nedsatt med 3/4"),
    KV_FKMU_0003_3("3", "Nedsatt med hälften"),
    KV_FKMU_0003_4("4", "Nedsatt med 1/4");

    private final String code;
    private final String description;

    SjukskrivningCodeSystem(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static String getDescription(String code) {
        for (SjukskrivningCodeSystem value : values()) {
            if (code.equals(value.code)) {
                return value.description;
            }
        }
        return "";
    }
}
