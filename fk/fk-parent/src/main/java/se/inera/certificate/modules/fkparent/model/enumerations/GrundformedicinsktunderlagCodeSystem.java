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

import se.inera.certificate.modules.fkparent.model.converter.RespConstants;

public enum GrundformedicinsktunderlagCodeSystem {
    KV_FKMU_0001_1(RespConstants.UNDERSOKNING_AV_PATIENT, "Min undersökning av patienten"),
    KV_FKMU_0001_2(RespConstants.TELEFONKONTAKT_MED_PATIENT, "Min telefonkontakt med patienten"),
    KV_FKMU_0001_3(RespConstants.JOURNALUPPGIFTER, "Journaluppgifter från den"),
    KV_FKMU_0001_4(RespConstants.ANHORIGSBESKRIVNING, "Anhörigs beskrivning av patienten"),
    KV_FKMU_0001_5(RespConstants.ANNAT, "Annat");

    private final String code;
    private final String description;

    GrundformedicinsktunderlagCodeSystem(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static String getDescription(String code) {
        for (GrundformedicinsktunderlagCodeSystem value : values()) {
            if (code.equals(value.code)) {
                return value.description;
            }
        }
        return "";
    }
}
