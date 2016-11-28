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

package se.inera.intyg.intygstyper.luse.model.internal;

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
import se.inera.intyg.intygstyper.luse.support.LuseEntryPoint;

@AutoValue
@JsonDeserialize(builder = AutoValue_LuseUtlatande.Builder.class)
public abstract class LuseUtlatande implements Utlatande, SitUtlatande {

    @Override
    public String getTyp() {
        return LuseEntryPoint.MODULE_ID;
    }

    @Override
    public abstract String getId();

    @Override
    public abstract GrundData getGrundData();

    @Override
    public abstract String getTextVersion();

    // Kategori 1 – Grund för medicinskt underlag
    // Fråga 1
    @Nullable
    public abstract InternalDate getUndersokningAvPatienten();

    @Nullable
    public abstract InternalDate getJournaluppgifter();

    @Nullable
    public abstract InternalDate getAnhorigsBeskrivningAvPatienten();

    @Nullable
    public abstract InternalDate getAnnatGrundForMU();

    @Nullable
    public abstract String getAnnatGrundForMUBeskrivning();

    @Nullable
    public abstract String getMotiveringTillInteBaseratPaUndersokning();

    // Fråga 2
    @Nullable
    public abstract InternalDate getKannedomOmPatient();

    // Kategori 2 – Andra medicinska utredningar och underlag
    // Fråga 3
    @Nullable
    public abstract Boolean getUnderlagFinns();

    // Fråga 4
    public abstract ImmutableList<Underlag> getUnderlag();

    // Kategori 3 - Sjukdomsförlopp
    // Fråga 5
    @Nullable
    public abstract String getSjukdomsforlopp();

    // Kategori 4 - Diagnos
    // Fråga 6
    @Override
    public abstract ImmutableList<Diagnos> getDiagnoser();

    // Delfråga 7.1
    @Nullable
    public abstract String getDiagnosgrund();

    // Delfråga45.1
    @Nullable
    public abstract Boolean getNyBedomningDiagnosgrund();

    // Delfråga 45.2
    @Nullable
    public abstract String getDiagnosForNyBedomning();

    // Kategori 5 – Funktionsnedsättning
    // Fråga 8
    @Nullable
    public abstract String getFunktionsnedsattningIntellektuell();

    // Fråga 9
    @Nullable
    public abstract String getFunktionsnedsattningKommunikation();

    // Fråga 10
    @Nullable
    public abstract String getFunktionsnedsattningKoncentration();

    // Fråga 11
    @Nullable
    public abstract String getFunktionsnedsattningPsykisk();

    // Fråga 12
    @Nullable
    public abstract String getFunktionsnedsattningSynHorselTal();

    // Fråga 13
    @Nullable
    public abstract String getFunktionsnedsattningBalansKoordination();

    // Fråga 14
    @Nullable
    public abstract String getFunktionsnedsattningAnnan();

    // Kategori 6 - AktivitetsbegränsningBeskrivning
    // Fråga 17
    @Nullable
    public abstract String getAktivitetsbegransning();

    // Kategori 7 – Medicinska behandlingar/åtgärder
    // Fråga 19
    @Nullable
    public abstract String getPagaendeBehandling();

    // Fråga 18
    @Nullable
    public abstract String getAvslutadBehandling();

    // Fråga 20
    @Nullable
    public abstract String getPlaneradBehandling();

    // Fråga 21
    @Nullable
    public abstract String getSubstansintag();

    // Kategori 8 – Medicinska förutsättningar för arbete
    // Fråga 22.1
    @Nullable
    public abstract String getMedicinskaForutsattningarForArbete();

    // Fråga 23.1
    @Nullable
    public abstract String getFormagaTrotsBegransning();

    // Kategori 9 - Övrigt
    // Fråga 25
    @Nullable
    public abstract String getOvrigt();

    // Kategori 10 - Kontakt
    // Fråga 26.1
    @Nullable
    public abstract Boolean getKontaktMedFk();

    // Fråga 26.2
    @Nullable
    public abstract String getAnledningTillKontakt();

    // Tilläggsfrågor
    public abstract ImmutableList<Tillaggsfraga> getTillaggsfragor();

    /* Retrieve a builder from an existing LuseUtlatande object. The builder can then be used
    to create a new copy with modified attributes. */
    public abstract Builder toBuilder();

    public static Builder builder() {
        return new AutoValue_LuseUtlatande.Builder().
                setUnderlag(ImmutableList.<Underlag> of()).
                setDiagnoser(ImmutableList.<Diagnos> of()).
                setTillaggsfragor(ImmutableList.<Tillaggsfraga>of());
    }

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract LuseUtlatande build();

        @JsonProperty(ID_JSON_ID)
        public abstract Builder setId(String id);

        @JsonProperty(GRUNDDATA_SVAR_JSON_ID)
        public abstract Builder setGrundData(GrundData grundData);

        @JsonProperty(TEXTVERSION_JSON_ID)
        public abstract Builder setTextVersion(String textVersion);

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

        @JsonProperty(MOTIVERING_TILL_INTE_BASERAT_PA_UNDERLAG_ID_1)
        public abstract Builder setMotiveringTillInteBaseratPaUndersokning(String motiveringTillInteBaseratPaUndersokning);

        @JsonProperty(KANNEDOM_SVAR_JSON_ID_2)
        public abstract Builder setKannedomOmPatient(InternalDate kannedomOmPatient);

        @JsonProperty(UNDERLAGFINNS_SVAR_JSON_ID_3)
        public abstract Builder setUnderlagFinns(Boolean underlagFinns);

        @JsonProperty(UNDERLAG_SVAR_JSON_ID_4)
        public Builder setUnderlag(List<Underlag> underlag) {
            return setUnderlag(ImmutableList.copyOf(underlag));
        }

        /* package private */
        abstract Builder setUnderlag(ImmutableList<Underlag> underlag);

        @JsonProperty(SJUKDOMSFORLOPP_SVAR_JSON_ID_5)
        public abstract Builder setSjukdomsforlopp(String sjukdomsforlopp);

        @JsonProperty(DIAGNOS_SVAR_JSON_ID_6)
        public Builder setDiagnoser(List<Diagnos> diagnoser) {
            return setDiagnoser(ImmutableList.copyOf(diagnoser));
        }

        /* package private */
        abstract Builder setDiagnoser(ImmutableList<Diagnos> diagnoser);

        @JsonProperty(DIAGNOSGRUND_SVAR_JSON_ID_7)
        public abstract Builder setDiagnosgrund(String diagnosgrund);

        @JsonProperty(DIAGNOSGRUND_NY_BEDOMNING_SVAR_JSON_ID_45)
        public abstract Builder setNyBedomningDiagnosgrund(Boolean nyBedomningDiagnosgrund);

        @JsonProperty(DIAGNOS_FOR_NY_BEDOMNING_SVAR_JSON_ID_45)
        public abstract Builder setDiagnosForNyBedomning(String diagnosForNyBedomning);

        @JsonProperty(FUNKTIONSNEDSATTNING_INTELLEKTUELL_SVAR_JSON_ID_8)
        public abstract Builder setFunktionsnedsattningIntellektuell(String funktionsnedsattningIntellektuell);

        @JsonProperty(FUNKTIONSNEDSATTNING_KOMMUNIKATION_SVAR_JSON_ID_9)
        public abstract Builder setFunktionsnedsattningKommunikation(String funktionsnedsattningKommunikation);

        @JsonProperty(FUNKTIONSNEDSATTNING_KONCENTRATION_SVAR_JSON_ID_10)
        public abstract Builder setFunktionsnedsattningKoncentration(String funktionsnedsattningKoncentration);

        @JsonProperty(FUNKTIONSNEDSATTNING_PSYKISK_SVAR_JSON_ID_11)
        public abstract Builder setFunktionsnedsattningPsykisk(String funktionsnedsattningPsykisk);

        @JsonProperty(FUNKTIONSNEDSATTNING_SYNHORSELTAL_SVAR_JSON_ID_12)
        public abstract Builder setFunktionsnedsattningSynHorselTal(String funktionsnedsattningSynHorselTal);

        @JsonProperty(FUNKTIONSNEDSATTNING_BALANSKOORDINATION_SVAR_JSON_ID_13)
        public abstract Builder setFunktionsnedsattningBalansKoordination(String funktionsnedsattningBalansKoordination);

        @JsonProperty(FUNKTIONSNEDSATTNING_ANNAN_SVAR_JSON_ID_14)
        public abstract Builder setFunktionsnedsattningAnnan(String funktionsnedsattningAnnan);

        @JsonProperty(AKTIVITETSBEGRANSNING_SVAR_JSON_ID_17)
        public abstract Builder setAktivitetsbegransning(String aktivitetsbegransning);

        @JsonProperty(PAGAENDEBEHANDLING_SVAR_JSON_ID_19)
        public abstract Builder setPagaendeBehandling(String pagaendeBehandling);

        @JsonProperty(AVSLUTADBEHANDLING_SVAR_JSON_ID_18)
        public abstract Builder setAvslutadBehandling(String avslutadBehandling);

        @JsonProperty(PLANERADBEHANDLING_SVAR_JSON_ID_20)
        public abstract Builder setPlaneradBehandling(String planeradBehandling);

        @JsonProperty(SUBSTANSINTAG_SVAR_JSON_ID_21)
        public abstract Builder setSubstansintag(String substansintag);

        @JsonProperty(MEDICINSKAFORUTSATTNINGARFORARBETE_SVAR_JSON_ID_22)
        public abstract Builder setMedicinskaForutsattningarForArbete(String medicinskaForutsattningarForArbete);

        @JsonProperty(FORMAGATROTSBEGRANSNING_SVAR_JSON_ID_23)
        public abstract Builder setFormagaTrotsBegransning(String formagaTrotsBegransning);

        @JsonProperty(OVRIGT_SVAR_JSON_ID_25)
        public abstract Builder setOvrigt(String ovrigt);

        @JsonProperty(KONTAKT_ONSKAS_SVAR_JSON_ID_26)
        public abstract Builder setKontaktMedFk(Boolean kontaktMedFk);

        @JsonProperty(ANLEDNING_TILL_KONTAKT_DELSVAR_JSON_ID_26)
        public abstract Builder setAnledningTillKontakt(String anledningTillKontakt);

        @JsonProperty(TILLAGGSFRAGOR_SVAR_JSON_ID)
        public Builder setTillaggsfragor(List<Tillaggsfraga> tillaggsfragor) {
            return setTillaggsfragor(ImmutableList.copyOf(tillaggsfragor));
        }

        /* package private */
        abstract Builder setTillaggsfragor(ImmutableList<Tillaggsfraga> tillaggsfragor);
    }

}
