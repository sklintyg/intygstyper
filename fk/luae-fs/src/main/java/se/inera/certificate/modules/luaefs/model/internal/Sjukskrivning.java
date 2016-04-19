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

package se.inera.certificate.modules.luaefs.model.internal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.auto.value.AutoValue;

import se.inera.intyg.common.support.model.InternalLocalDateInterval;

import javax.annotation.Nullable;

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
        HELT_NEDSATT(1, "Helt nedsatt"),
        /**
         * Nedsatt till 3/4 (id 2).
         */
        NEDSATT_3_4(2, "Nedsatt med 3/4"),
        /**
         * Nedsatt till hälften (id 3).
         */
        NEDSATT_HALFTEN(3, "Nedsatt med hälften"),
        /**
         * Nedsatt till 1/4 (id 4).
         */
        NEDSATT_1_4(4, "Nedsatt med 1/4");

        private final int id;
        private final String label;

        SjukskrivningsGrad(int id, String label) {
            this.id = id;
            this.label = label;
        }

        @JsonValue
        public int getId() {
            return id;
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
    }

}
