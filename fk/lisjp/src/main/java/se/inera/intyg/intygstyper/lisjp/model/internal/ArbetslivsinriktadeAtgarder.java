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

import com.fasterxml.jackson.annotation.*;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class ArbetslivsinriktadeAtgarder {

    @JsonCreator
    public static ArbetslivsinriktadeAtgarder create(@JsonProperty("typ") ArbetslivsinriktadeAtgarderVal typ) {
        return new AutoValue_ArbetslivsinriktadeAtgarder(typ);
    }

    public abstract ArbetslivsinriktadeAtgarderVal getTyp();

    public enum ArbetslivsinriktadeAtgarderVal {

        INTE_AKTUELLT("EJ_AKTUELLT", "Inte aktuellt"),
        ARBETSTRANING("ARBETSTRANING", "Arbetsträning"),
        ARBETSANPASSNING("ARBETSANPASSNING", "Arbetsanpassning"),
        SOKA_NYTT_ARBETE("SOKA_NYTT_ARBETE", "Söka nytt arbete"),
        BESOK_PA_ARBETSPLATSEN("BESOK_ARBETSPLATS", "Besök på arbetsplatsen"),
        ERGONOMISK_BEDOMNING("ERGONOMISK", "Ergonomisk bedömning"),
        HJALPMEDEL("HJALPMEDEL", "Hjälpmedel"),
        KONFLIKTHANTERING("KONFLIKTHANTERING", "Konflikthantering"),
        KONTAKT_MED_FORETAGSHALSOVARD("KONTAKT_FHV", "Kontakt med företagshälsovård"),
        OMFORDELNING_AV_ARBETSUPPGIFTER("OMFORDELNING", "Omfördelning av arbetsuppgifter"),
        OVRIGT("OVRIGA_ATGARDER", "Övrigt");

        private final String id;
        private final String label;

        ArbetslivsinriktadeAtgarderVal(String id, String label) {
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
        public static ArbetslivsinriktadeAtgarderVal fromId(@JsonProperty("id") String id) {
            for (ArbetslivsinriktadeAtgarderVal typ : values()) {
                if (typ.id.equals(id)) {
                    return typ;
                }
            }
            throw new IllegalArgumentException();
        }

    }

}
