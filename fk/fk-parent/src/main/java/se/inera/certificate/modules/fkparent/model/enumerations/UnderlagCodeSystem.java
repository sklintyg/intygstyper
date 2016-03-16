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

public enum UnderlagCodeSystem {
    KV_FKMU_0005_1("1", "Neuropsykiatriskt utlåtande"),
    KV_FKMU_0005_2("2", "Underlag från habiliteringen"),
    KV_FKMU_0005_3("3",
            "Underlag från arbetsterapeut"),
    KV_FKMU_0005_4("4", "Underlag från fysioterapeut"),
    KV_FKMU_0005_5("5",
            "Underlag från logoped"),
    KV_FKMU_0005_6("6", "Underlag från psykolog"),
    KV_FKMU_0005_7("7",
            "Underlag från företagshälsovård"),
    KV_FKMU_0005_8("8", "Underlag från skolhälsovård"),
    KV_FKMU_0005_9("9",
            "Utredning av annan specialistklinik"),
    KV_FKMU_0005_10("10",
            "Utredning från vårdinrättning utomlands"),
    KV_FKMU_0005_11("11", "Övrigt");

    private final String code;
    private final String description;

    UnderlagCodeSystem(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static String getDescription(String code) {
        for (UnderlagCodeSystem value : values()) {
            if (code.equals(value.code)) {
                return value.description;
            }
        }
        return "";
    }
}
