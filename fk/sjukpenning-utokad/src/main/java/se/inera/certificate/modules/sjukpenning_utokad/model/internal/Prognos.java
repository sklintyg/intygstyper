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
public abstract class Prognos {
    Prognos() {
    }

    @JsonCreator
    public static Prognos create(@JsonProperty("prognos") PrognosTyp typ,
            @JsonProperty("fortydligande") String fortydligande) {
        return new AutoValue_Prognos(typ, fortydligande);
    }

    @Nullable
    public abstract PrognosTyp getTyp();
    @Nullable
    public abstract String getFortydligande();

    public enum PrognosTyp {
        /**
         * Med stor sannolikhet (id 1).
         */
        MED_STOR_SANNOLIKHET(1, "STOR_SANNOLIKHET", "Med stor sannolikhet"),
        /**
         * Sannolikt sjukskrivning 180 dagar (id 2).
         */
        SANNOLIK_SJUKSKRIVNING_180_DAGAR(2, "LANGRE_AN_180", "Patienten bedöms kunna återgå i nuvarande sysselsättning, men sjukskrivningstiden är sannolikt längre än 180 dagar"),
        /**
         * Sannolikt ej återgå till nuvarande sysselsättning (id 3).
         */
        SANNOLIKT_EJ_ATERGA_TILL_SYSSELSATTNING(3, "SANNOLIKT_INTE", "Patienten kan sannolikt inte återgå till nuvarande sysselsättning"),
        /**
         * Prognos oklar (id 4).
         */
        PROGNOS_OKLAR(4, "PROGNOS_OKLAR", "Prognos för återgång i nuvarande sysselsättning är oklar");

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
}
