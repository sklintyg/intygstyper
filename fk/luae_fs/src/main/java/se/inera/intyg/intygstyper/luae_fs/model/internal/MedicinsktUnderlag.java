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

package se.inera.intyg.intygstyper.luae_fs.model.internal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.auto.value.AutoValue;
import se.inera.intyg.common.support.model.InternalDate;

import javax.annotation.Nullable;

@AutoValue
public abstract class MedicinsktUnderlag {

    @JsonCreator
    public static MedicinsktUnderlag create(@JsonProperty("medicinsktUnderlagTyp") MedicinsktUnderlagTyp medicinsktUnderlagTyp,
            @JsonProperty("datum") InternalDate datum, @JsonProperty("annanGrund") String annanGrund) {
        return new AutoValue_MedicinsktUnderlag(medicinsktUnderlagTyp, datum, annanGrund);
    }

    public abstract MedicinsktUnderlagTyp getMedicinsktUnderlagTyp();

    public abstract InternalDate getDatumForMedicinsktUnderlag();

    @Nullable
    public abstract String getAnnanGrundForMedicinsktUnderlag();

    public enum MedicinsktUnderlagTyp {
        MIN_UNDERSOKNING_AV_PATIENTEN(1, "Min undersökning av patienten"),
        MIN_TELEFONKONTAKT_MED_PATIENTEN(2, "Min telefonkontakt med patienten"),
        JOURNALUPPGIFTER(3, "Journaluppgifter"),
        ANHORIGS_BESKRIVNING_AV_PATIENTEN(4, "Anhörigs beskrivning av patienten"),
        ANNAT(5, "Annat");

        private final int id;
        private final String label;
        MedicinsktUnderlagTyp(int id, String label) {
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
        public static MedicinsktUnderlagTyp fromId(@JsonProperty("id") int id) {
            for (MedicinsktUnderlagTyp typ : values()) {
                if (typ.id == id) {
                    return typ;
                }
            }
            throw new IllegalArgumentException();
        }
    }
}
