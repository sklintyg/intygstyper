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

package se.inera.certificate.modules.sjukpenning_utokad.model.internal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.auto.value.AutoValue;

import javax.annotation.Nullable;

@AutoValue
public abstract class Sysselsattning {

    Sysselsattning() {
    }

    @JsonCreator
    public static Sysselsattning create(@JsonProperty("typ") SysselsattningsTyp typ) {
        return new AutoValue_Sysselsattning(typ);
    }

    @Nullable
    public abstract SysselsattningsTyp getTyp();

    public enum SysselsattningsTyp {
        NUVARANDE_ARBETE(1, "NUVARANDE_ARBETE", "Nuvarande arbete"),
        ARBETSSOKANDE(2, "ARBETSSOKANDE", "Arbetssökande"),
        FORADLRARLEDIGHET_VARD_AV_BARN(3, "FORALDRALEDIG", "Föräldraledighet för vård av barn"),
        STUDIER(4, "STUDIER", "Studier"),
        ARBETSMARKNADSPOLITISKT_PROGRAM(5, "PROGRAM", "Deltagande i arbetsmarknadspolitiskt program");

        private final int id;
        private final String transportId;
        private final String label;

        SysselsattningsTyp(int id, String transportId, String label) {
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
        public static SysselsattningsTyp fromId(@JsonProperty("id") int id) {
            for (SysselsattningsTyp typ : values()) {
                if (typ.id == id) {
                    return typ;
                }
            }
            throw new IllegalArgumentException();
        }

        public static SysselsattningsTyp fromTransportId(String transportId) {
            for (SysselsattningsTyp typ : values()) {
                if (typ.transportId.equals(transportId)) {
                    return typ;
                }
            }
            throw new IllegalArgumentException();
        }

    }
}
