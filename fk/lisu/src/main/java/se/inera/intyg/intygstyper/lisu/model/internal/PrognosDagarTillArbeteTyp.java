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
 * Implements KV_FKMU_0007.
 */
public enum PrognosDagarTillArbeteTyp {
    /**
     * 30 dagarm, id 1.
     */
    DAGAR_30(1, "30_DAGAR", "30 dagar"),
    /**
     * 60 dagar, id 2.
     */
    DAGAR_60(2, "60_DAGAR", "60 dagar"),
    /**
     * 90 dagar, id 3.
     */
    DAGAR_90(3, "90_DAGAR", "90 dagar"),
    /**
     * 180 dagar, id 4.
     */
    DAGAR_180(4, "180_DAGAR", "180 dagar");

    private final int id;
    private final String transportId;
    private final String label;

    PrognosDagarTillArbeteTyp(int id, String transportId, String label) {
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
    public static PrognosDagarTillArbeteTyp fromId(@JsonProperty("id") int id) {
        for (PrognosDagarTillArbeteTyp typ : values()) {
            if (typ.id == id) {
                return typ;
            }
        }
        throw new IllegalArgumentException();
    }

    public static PrognosDagarTillArbeteTyp fromTransportId(String transportId) {
        for (PrognosDagarTillArbeteTyp typ : values()) {
            if (typ.transportId.equals(transportId)) {
                return typ;
            }
        }
        throw new IllegalArgumentException();
    }
}
