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

package se.inera.intyg.intygstyper.fkparent.model.internal;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.*;
import com.google.auto.value.AutoValue;

import se.inera.intyg.common.support.model.InternalDate;

@AutoValue
public abstract class Underlag {

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
        NEUROPSYKIATRISKT_UTLATANDE("NEUROPSYKIATRISKT", "Neuropsykiatriskt utlåtande"),
        UNDERLAG_FRAN_HABILITERINGEN("HABILITERING", "Underlag från habiliteringen"),
        UNDERLAG_FRAN_ARBETSTERAPEUT("ARBETSTERAPEUT", "Underlag från arbetsterapeut"),
        UNDERLAG_FRAN_FYSIOTERAPEUT("FYSIOTERAPEUT", "Underlag från fysioterapeut"),
        UNDERLAG_FRAN_LOGOPED("LOGOPED", "Underlag från logoped"),
        UNDERLAG_FRANPSYKOLOG("PSYKOLOG", "Underlag från psykolog"),
        UNDERLAG_FRANFORETAGSHALSOVARD("FORETAGSHALSOVARD", "Underlag från företagshälsovård"),
        UNDERLAG_FRANSKOLHALSOVARD("SKOLHALSOVARD", "Underlag från skolhälsovård"),
        UTREDNING_AV_ANNAN_SPECIALISTKLINIK("SPECIALISTKLINIK", "Utredning av annan specialistklinik"),
        UTREDNING_FRAN_VARDINRATTNING_UTOMLANDS("VARD_UTOMLANDS", "Utredning från vårdinrättning utomlands"),
        OVRIGT("OVRIGT_UTLATANDE", "Övrigt");

        private final String id;
        private final String label;

        UnderlagsTyp(String id, String label) {
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
        public static UnderlagsTyp fromId(@JsonProperty("id") String id) {
            for (UnderlagsTyp typ : values()) {
                if (typ.id.equals(id)) {
                    return typ;
                }
            }
            throw new IllegalArgumentException();
        }

    }

}
