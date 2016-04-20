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

public enum TypAvSysselsattningCodeSystem {
    KV_FKMU_0002_1("1", "Nuvarande arbete"),
    KV_FKMU_0002_2("2", "Arbetssökande"),
    KV_FKMU_0002_3("3", "Föräldraledighet för vård av barn"),
    KV_FKMU_0002_4("4", "Studier"),
    KV_FKMU_0002_5("5", "Deltagande i arbetsmarknadspolitiskt program");

    private final String code;
    private final String description;

    TypAvSysselsattningCodeSystem(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static String getDescription(String code) {
        for (TypAvSysselsattningCodeSystem value : values()) {
            if (code.equals(value.code)) {
                return value.description;
            }
        }
        return "";
    }
}
