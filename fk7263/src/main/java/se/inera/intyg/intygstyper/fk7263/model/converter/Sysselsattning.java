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

package se.inera.intyg.intygstyper.fk7263.model.converter;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public enum Sysselsattning {
    NUVARANDE_ARBETE(1, "NUVARANDE_ARBETE", "Nuvarande arbete"),
    ARBETSSOKANDE(2, "ARBETSSOKANDE", "Arbetssökande"),
    FORADLRARLEDIGHET_VARD_AV_BARN(3, "FORALDRALEDIG", "Föräldraledighet för vård av barn");

    private final int id;
    private final String transportId;
    private final String label;

    Sysselsattning(int id, String transportId, String label) {
        this.id = id;
        this.transportId = transportId;
        this.label = label;
    }

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
    public static Sysselsattning fromId(@JsonProperty("id") int id) {
        for (Sysselsattning typ : values()) {
            if (typ.id == id) {
                return typ;
            }
        }
        throw new IllegalArgumentException();
    }

    public static Sysselsattning fromTransportId(String transportId) {
        for (Sysselsattning typ : values()) {
            if (typ.transportId.equals(transportId)) {
                return typ;
            }
        }
        throw new IllegalArgumentException();
    }

}
