/*
 * Copyright (C) 2015 Inera AB (http://www.inera.se)
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

package se.inera.certificate.modules.sjukersattning.model.internal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.auto.value.AutoValue;

import javax.annotation.Nullable;

@AutoValue
public abstract class Funktionsnedsattning {

    Funktionsnedsattning() {
    }

    @JsonCreator
    public static Funktionsnedsattning create(@JsonProperty("funktionsomrade") Funktionsomrade funktionsomrade,
            @JsonProperty("beskrivning") String beskrivning) {
        return new AutoValue_Funktionsnedsattning(funktionsomrade, beskrivning);
    }

    public abstract Funktionsomrade getFunktionsomrade();

    @Nullable
    public abstract String getBeskrivning();

    public enum Funktionsomrade {
        INTELLEKTUELL(1, "Intellektuell funktion"), KOMMUNIKATION(2, "Kommunikation och social interaktion"),
        KONCENTRATION(3, "Uppmärksamhet och koncentration"), ANNAN_PSYKISK(4, "Annan psykisk funktion"),
        SYN_HORSEL_TAL(5, "Syn, hörsel och tal"), BALANS(6, "Balans, koordination och motorik"),
        ANNAN_KROPPSLIG(7, "Annan kroppslig funktion"), OKAND(8, "Okänd");

        private int id;

        private String label;

        Funktionsomrade(int id, String label) {
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
        public static Funktionsomrade fromId(@JsonProperty("id") int id) {
            for (Funktionsomrade funktionsomrade : values()) {
                if (funktionsomrade.id == id) {
                    return funktionsomrade;
                }
            }
            throw new IllegalArgumentException();
        }
    }

}
