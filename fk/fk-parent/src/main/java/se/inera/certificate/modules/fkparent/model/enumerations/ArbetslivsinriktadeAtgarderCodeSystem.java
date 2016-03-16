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

public enum ArbetslivsinriktadeAtgarderCodeSystem {
    KV_FKMU_0004_1("1", "Inte aktuellt"),
    KV_FKMU_0004_2("2", "Arbetsträning"),
    KV_FKMU_0004_3("3", "Arbetsanspassning"),
    KV_FKMU_0004_4("4", "Söka nytt arbete"),
    KV_FKMU_0004_5("5", "Besök på arbetsplatsen"),
    KV_FKMU_0004_6("6", "Ergonomisk bedömning"),
    KV_FKMU_0004_7("7", "Hjälpmedel"),
    KV_FKMU_0004_8("8", "Konflikthantering"),
    KV_FKMU_0004_9("9", "Kontakt med företagshälsovård"),
    KV_FKMU_0004_10("10", "Omfördelning av arbetsuppgifter"),
    KV_FKMU_0004_11("11", "Övrigt");

    private final String code;
    private final String description;

    ArbetslivsinriktadeAtgarderCodeSystem(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static String getDescription(String code) {
        for (ArbetslivsinriktadeAtgarderCodeSystem value : values()) {
            if (code.equals(value.code)) {
                return value.description;
            }
        }
        return "";
    }
    
}
