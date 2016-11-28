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

import static se.inera.intyg.intygstyper.fkparent.model.converter.RespConstants.*;

import java.util.List;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;

import se.inera.intyg.common.support.model.InternalDate;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.Utlatande;
import se.inera.intyg.intygstyper.fkparent.model.internal.*;
import se.inera.intyg.intygstyper.luae_fs.support.LuaefsEntryPoint;

@AutoValue
@JsonDeserialize(builder = AutoValue_LuaefsUtlatande.Builder.class)
public abstract class LuaefsUtlatande implements Utlatande, SitUtlatande {

    @Override
    public abstract String getId();

    @Override
    public abstract GrundData getGrundData();

    @Override
    public abstract String getTextVersion();

    @Override
    public String getTyp() {
        return LuaefsEntryPoint.MODULE_ID;
    }


    // - - - - - - - - - - - - - - - - - - - - - -
    // Kategori 1 – Grund för medicinskt underlag
    // - - - - - - - - - - - - - - - - - - - - - -

    // Fråga 1
    @Nullable
    public abstract InternalDate getUndersokningAvPatienten();

    @Nullable
    public abstract InternalDate getJournaluppgifter();

    @Nullable
    public abstract InternalDate getAnhorigsBeskrivningAvPatienten();

    @Nullable
    public abstract InternalDate getAnnatGrundForMU();

    // Fråga 1.3 Vilken annan grund finns för MU
    @Nullable
    public abstract String getAnnatGrundForMUBeskrivning();

    // Fråga 2 Kännedom om patienten
    // Fråga 2.1 - Datum för kännedom om patienten
    @Nullable
    public abstract InternalDate getKannedomOmPatient();


    // - - - - - - - - - - - - - - - - - - - - - -
    // Kategori 2 - Andra medicinska utredningar och underlag
    // - - - - - - - - - - - - - - - - - - - - - -
    // Fråga 3 Finns det andra medicinska utredningar eller underlag
    // Fråga 3.1 Finns det andra medicinska utredningar eller underlag
    @Nullable
    public abstract Boolean getUnderlagFinns();

    // Fråga 4 Ange andra medicinska utredningar eller underlag
    // Fråga 4.1 - Utredning eller underlagstyp
    // Fråga 4.2 - Datum för utredning eller underlag
    // Fråga 4.3 - Var utredningen kan hämtas in
    @Nullable
    public abstract ImmutableList<Underlag> getUnderlag();


    // - - - - - - - - - - - - - - - - - - - - - -
    // Kategori 3 - Diagnos
    // - - - - - - - - - - - - - - - - - - - - - -
    // Fråga 6 Typ av diagnos
    // Fråga 6.1 - Diagnostext
    // Fråga 6.2 - Diagnoskod för ICD-10
    @Override
    public abstract ImmutableList<Diagnos> getDiagnoser();


    // - - - - - - - - - - - - - - - - - - - - - -
    // Kategori 4 - Funktionsnedsättning
    // - - - - - - - - - - - - - - - - - - - - - -
    // Fråga 15 Funktionsnedsättningens debut, utveckling och visar sig nu
    // Fråga 15.1 - Beskriv funktionsnedsättningens debut, utveckling och nu
    @Nullable
    public abstract String getFunktionsnedsattningDebut();

    // Fråga 16 - Funktionsnedsättningens påverkan på skolgången?
    // Fråga 16.1 - På vilket sätt har skolgången påverkats?
    @Nullable
    public abstract String getFunktionsnedsattningPaverkan();


    // - - - - - - - - - - - - - - - - - - - - - -
    // Kategori 5 - Övrigt
    // - - - - - - - - - - - - - - - - - - - - - -
    // Fråga 25 Övrigt
    // Fråga 25.1 - Typ av övriga upplysningar
    @Nullable
    public abstract String getOvrigt();


    // - - - - - - - - - - - - - - - - - - - - - -
    // Kategori 6 - Kontakt
    // - - - - - - - - - - - - - - - - - - - - - -
    // Fråga 26.1
    @Nullable
    public abstract Boolean getKontaktMedFk();

    // Fråga 26.2
    @Nullable
    public abstract String getAnledningTillKontakt();


    // - - - - - - - - - - - - - - - - - - - - - -
    // Tilläggsfrågor
    // - - - - - - - - - - - - - - - - - - - - - -
    @Nullable
    public abstract ImmutableList<Tillaggsfraga> getTillaggsfragor();


    /*
     * Retrieve a builder from an existing LuaefsUtlatande object. The builder can then be used
     * to create a new copy with modified attributes.
     */
    public abstract Builder toBuilder();

    public static Builder builder() {
        return new AutoValue_LuaefsUtlatande.Builder()
                .setDiagnoser(ImmutableList.<Diagnos> of())
                .setTillaggsfragor(ImmutableList.<Tillaggsfraga> of())
                .setUnderlag(ImmutableList.<Underlag> of());
    }

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract LuaefsUtlatande build();

        @JsonProperty(ID_JSON_ID)
        public abstract Builder setId(String id);

        @JsonProperty(GRUNDDATA_SVAR_JSON_ID)
        public abstract Builder setGrundData(GrundData grundData);

        @JsonProperty(TEXTVERSION_JSON_ID)
        public abstract Builder setTextVersion(String textVersion);

        // Fråga 1
        @JsonProperty(GRUNDFORMEDICINSKTUNDERLAG_UNDERSOKNING_AV_PATIENT_SVAR_JSON_ID_1)
        public abstract Builder setUndersokningAvPatienten(InternalDate undersokningAvPatienten);

        @JsonProperty(GRUNDFORMEDICINSKTUNDERLAG_JOURNALUPPGIFTER_SVAR_JSON_ID_1)
        public abstract Builder setJournaluppgifter(InternalDate journaluppgifter);

        @JsonProperty(GRUNDFORMEDICINSKTUNDERLAG_ANHORIGS_BESKRIVNING_SVAR_JSON_ID_1)
        public abstract Builder setAnhorigsBeskrivningAvPatienten(InternalDate anhorigsBeskrivningAvPatienten);

        @JsonProperty(GRUNDFORMEDICINSKTUNDERLAG_ANNAT_SVAR_JSON_ID_1)
        public abstract Builder setAnnatGrundForMU(InternalDate annatGrundForMU);

        @JsonProperty(GRUNDFORMEDICINSKTUNDERLAG_BESKRIVNING_DELSVAR_JSON_ID_1)
        public abstract Builder setAnnatGrundForMUBeskrivning(String annatGrundForMUBeskrivning);

        // Fråga 2
        @JsonProperty(KANNEDOM_SVAR_JSON_ID_2)
        public abstract Builder setKannedomOmPatient(InternalDate kannedomOmPatient);

        // Fråga 3
        @JsonProperty(UNDERLAGFINNS_SVAR_JSON_ID_3)
        public abstract Builder setUnderlagFinns(Boolean underlagFinns);

        // Fråga 4
        @JsonProperty(UNDERLAG_SVAR_JSON_ID_4)
        public Builder setUnderlag(List<Underlag> underlag) {
            return setUnderlag(ImmutableList.copyOf(underlag));
        }

        /* package private */
        abstract Builder setUnderlag(ImmutableList<Underlag> underlag);

        // Fråga 6
        @JsonProperty(DIAGNOS_SVAR_JSON_ID_6)
        public Builder setDiagnoser(List<Diagnos> diagnoser) {
            return setDiagnoser(ImmutableList.copyOf(diagnoser));
        }

        /* package private */
        abstract Builder setDiagnoser(ImmutableList<Diagnos> diagnoser);

        // Fråga 15
        @JsonProperty(FUNKTIONSNEDSATTNING_DEBUT_SVAR_JSON_ID_15)
        public abstract Builder setFunktionsnedsattningDebut(String funktionsnedsattningDebut);

        // Fråga 16
        @JsonProperty(FUNKTIONSNEDSATTNING_PAVERKAN_SVAR_JSON_ID_16)
        public abstract Builder setFunktionsnedsattningPaverkan(String funktionsnedsattningPaverkan);

        // Fråga 25
        @JsonProperty(OVRIGT_SVAR_JSON_ID_25)
        public abstract Builder setOvrigt(String ovrigt);

        // Fråga 26
        @JsonProperty(KONTAKT_ONSKAS_SVAR_JSON_ID_26)
        public abstract Builder setKontaktMedFk(Boolean kontaktMedFk);

        @JsonProperty(ANLEDNING_TILL_KONTAKT_DELSVAR_JSON_ID_26)
        public abstract Builder setAnledningTillKontakt(String anledningTillKontakt);

        // Tilläggsfrågor
        @JsonProperty(TILLAGGSFRAGOR_SVAR_JSON_ID)
        public Builder setTillaggsfragor(List<Tillaggsfraga> tillaggsfragor) {
            return setTillaggsfragor(ImmutableList.copyOf(tillaggsfragor));
        }

        /* package private */
        abstract Builder setTillaggsfragor(ImmutableList<Tillaggsfraga> tillaggsfragor);
    }

}
