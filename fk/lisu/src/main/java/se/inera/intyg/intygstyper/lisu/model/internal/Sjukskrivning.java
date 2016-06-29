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

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.*;
import com.google.auto.value.AutoValue;

import se.inera.intyg.common.support.model.InternalLocalDateInterval;

@AutoValue
public abstract class Sjukskrivning {
    Sjukskrivning() {
    }

    @JsonCreator
    public static Sjukskrivning create(@JsonProperty("sjukskrivningsgrad") SjukskrivningsGrad sjukskrivningsgrad,
            @JsonProperty("period") InternalLocalDateInterval period) {
        return new AutoValue_Sjukskrivning(sjukskrivningsgrad, period);
    }

    @Nullable
    public abstract SjukskrivningsGrad getSjukskrivningsgrad();

    @Nullable
    public abstract InternalLocalDateInterval getPeriod();

    public enum SjukskrivningsGrad {
        /**
         * Helt nedsatt (id 1).
         */
        HELT_NEDSATT(1, "HELT_NEDSATT", "100%"),
        /**
         * Nedsatt till 3/4 (id 2).
         */
        NEDSATT_3_4(2, "TRE_FJARDEDEL", "75%"),
        /**
         * Nedsatt till hälften (id 3).
         */
        NEDSATT_HALFTEN(3, "HALFTEN", "50%"),
        /**
         * Nedsatt till 1/4 (id 4).
         */
        NEDSATT_1_4(4, "EN_FJARDEDEL", "25%");

        private final int id;
        private final String transportId;
        private final String label;

        SjukskrivningsGrad(int id, String transportId, String label) {
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
        public static SjukskrivningsGrad fromId(@JsonProperty("id") int id) {
            for (SjukskrivningsGrad typ : values()) {
                if (typ.id == id) {
                    return typ;
                }
            }
            throw new IllegalArgumentException();
        }

        public static SjukskrivningsGrad fromTransportId(String transportId) {
            for (SjukskrivningsGrad typ : values()) {
                if (typ.transportId.equals(transportId)) {
                    return typ;
                }
            }
            throw new IllegalArgumentException();
        }

    }

}
