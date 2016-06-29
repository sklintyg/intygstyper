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

package se.inera.intyg.intygstyper.lisu.model.internal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Implements KV_FKMU_0006.
 */
public enum PrognosTyp {
    /**
     * Med stor sannolikhet (id 1).
     */
    MED_STOR_SANNOLIKHET(1, "STOR_SANNOLIKHET", "Patienten kommer med stor sannolikhet att kunna återgå helt i nuvarande sysselsättning efter denna sjukskrivning"),
    /**
     * Sannolikt ej återgå till nuvarande sysselsättning (id 3).
     */
    SANNOLIKT_EJ_ATERGA_TILL_SYSSELSATTNING(3, "SANNOLIKT_INTE", "Patienten kan sannolikt inte återgå till nuvarande sysselsättning"),
    /**
     * Prognos oklar (id 4).
     */
    PROGNOS_OKLAR(4, "PROGNOS_OKLAR", "Prognos för återgång i nuvarande sysselsättning är oklar"),
    /**
     * Sannolikt återgå i sysselsättning efter x antal dagar (id 5).
     */
    ATER_X_ANTAL_DGR(5, "ATER_X_ANTAL_DGR", "Patienten kommer med stor sannolikhet att återgå helt i nuvarande sysselsättning efter x antal dagar");

    private final int id;
    private final String transportId;
    private final String label;

    PrognosTyp(int id, String transportId, String label) {
        this.id = id;
        this.transportId = transportId;
        this.label = label;
    }

    @JsonValue
    public int getId() {
        return id;
    }

    public String getTransportId() {
        return transportId;
    }

    public String getLabel() {
        return label;
    }

    @JsonCreator
    public static PrognosTyp fromId(@JsonProperty("id") int id) {
        for (PrognosTyp typ : values()) {
            if (typ.id == id) {
                return typ;
            }
        }
        throw new IllegalArgumentException();
    }

    public static PrognosTyp fromTransportId(String transportId) {
        for (PrognosTyp typ : values()) {
            if (typ.transportId.equals(transportId)) {
                return typ;
            }
        }
        throw new IllegalArgumentException();
    }

}
