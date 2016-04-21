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

package se.inera.certificate.modules.fkparent.model.internal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.auto.value.AutoValue;
import se.inera.intyg.common.support.model.InternalDate;

import javax.annotation.Nullable;

@AutoValue
public abstract class Underlag {

    Underlag() {
    }

    @JsonCreator
    public static Underlag create(@JsonProperty("typ") UnderlagsTyp typ,
            @JsonProperty("datum") InternalDate datum,
            @JsonProperty("hamtasFran") String hamtasFran) {
        return new AutoValue_Underlag(typ, datum, hamtasFran);
    }

    @Nullable
    public abstract UnderlagsTyp getTyp();

    @Nullable
    public abstract InternalDate getDatum();

    @Nullable
    public abstract String getHamtasFran();

    public enum UnderlagsTyp {
        NEUROPSYKIATRISKT_UTLATANDE(1, "NEUROPSYKIATRISKT", "Neuropsykiatriskt utlåtande"),
        UNDERLAG_FRAN_HABILITERINGEN(2, "HABILITERING", "Underlag från habiliteringen"),
        UNDERLAG_FRAN_ARBETSTERAPEUT(3, "ARBETSTERAPEUT", "Underlag från arbetsterapeut"),
        UNDERLAG_FRAN_FYSIOTERAPEUT(4, "FYSIOTERAPEUT", "Underlag från fysioterapeut"),
        UNDERLAG_FRAN_LOGOPED(5, "LOGOPED", "Underlag från logoped"),
        UNDERLAG_FRANPSYKOLOG(6, "PSYKOLOG", "Underlag från psykolog"),
        UNDERLAG_FRANFORETAGSHALSOVARD(7, "FORETAGSHALSOVARD", "Underlag från företagshälsovård"),
        UNDERLAG_FRANSKOLHALSOVARD(8, "SKOLHALSOVARD", "Underlag från skolhälsovård"),
        UTREDNING_AV_ANNAN_SPECIALISTKLINIK(9, "SPECIALISTKLINIK", "Utredning av annan specialistklinik"),
        UTREDNING_FRAN_VARDINRATTNING_UTOMLANDS(10, "VARD_UTOMLANDS", "Utredning från vårdinrättning utomlands"),
        OVRIGT(11, "OVRIGT_UTLATANDE", "Övrigt");

        private final int id;
        private final String transportId;
        private final String label;

        UnderlagsTyp(int id, String transportId, String label) {
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
        public static UnderlagsTyp fromId(@JsonProperty("id") int id) {
            for (UnderlagsTyp typ : values()) {
                if (typ.id == id) {
                    return typ;
                }
            }
            throw new IllegalArgumentException();
        }

        public static UnderlagsTyp fromTransportId(String transportId) {
            for (UnderlagsTyp typ : values()) {
                if (typ.transportId.equals(transportId)) {
                    return typ;
                }
            }
            throw new IllegalArgumentException();
        }

    }

}
