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

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.*;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Sysselsattning {

    @JsonCreator
    public static Sysselsattning create(@JsonProperty("typ") SysselsattningsTyp typ) {
        return new AutoValue_Sysselsattning(typ);
    }

    @Nullable
    public abstract SysselsattningsTyp getTyp();

    public enum SysselsattningsTyp {
        NUVARANDE_ARBETE("NUVARANDE_ARBETE", "Nuvarande arbete"),
        ARBETSSOKANDE("ARBETSSOKANDE", "Arbetssökande"),
        FORADLRARLEDIGHET_VARD_AV_BARN("FORALDRALEDIG", "Föräldraledighet för vård av barn"),
        STUDIER("STUDIER", "Studier"),
        ARBETSMARKNADSPOLITISKT_PROGRAM("PROGRAM", "Deltagande i arbetsmarknadspolitiskt program");

        private final String id;
        private final String label;

        SysselsattningsTyp(String id, String label) {
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
        public static SysselsattningsTyp fromId(@JsonProperty("id") String id) {
            for (SysselsattningsTyp typ : values()) {
                if (typ.id.equals(id)) {
                    return typ;
                }
            }
            throw new IllegalArgumentException();
        }

    }
}
