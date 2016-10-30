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

package se.inera.intyg.intygstyper.lisjp.model.internal;

import com.fasterxml.jackson.annotation.*;

/**
 * Implements KV_FKMU_0007.
 */
public enum PrognosDagarTillArbeteTyp {
    /**
     * 30 dagarm.
     */
    DAGAR_30("TRETTIO_DGR", "30 dagar"),
    /**
     * 60 dagar.
     */
    DAGAR_60("SEXTIO_DGR", "60 dagar"),
    /**
     * 90 dagar.
     */
    DAGAR_90("NITTIO_DGR", "90 dagar"),
    /**
     * 180 dagar.
     */
    DAGAR_180("HUNDRAATTIO_DAGAR", "180 dagar"),
    /**
     * 365 dagar.
     */
    DAGAR_365("TREHUNDRASEXTIOFEM_DAGAR", "365 dagar");

    private final String id;
    private final String label;

    PrognosDagarTillArbeteTyp(String id, String label) {
        this.id = id;
        this.label = label;
    }

    @JsonValue
    public String getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    @JsonCreator
    public static PrognosDagarTillArbeteTyp fromId(@JsonProperty("id") String id) {
        for (PrognosDagarTillArbeteTyp typ : values()) {
            if (typ.id.equals(id)) {
                return typ;
            }
        }
        throw new IllegalArgumentException();
    }

}
