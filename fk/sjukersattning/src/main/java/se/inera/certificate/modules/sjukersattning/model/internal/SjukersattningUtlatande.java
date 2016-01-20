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

package se.inera.certificate.modules.sjukersattning.model.internal;

import javax.annotation.Nullable;

import se.inera.certificate.modules.sjukersattning.support.SjukersattningEntryPoint;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import se.inera.intyg.common.support.model.InternalDate;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.Utlatande;

import java.util.List;

@AutoValue
@JsonDeserialize(builder = AutoValue_SjukersattningUtlatande.Builder.class)
public abstract class SjukersattningUtlatande implements Utlatande {

    SjukersattningUtlatande() {
    }

    @Override
    public String getTyp() {
        return SjukersattningEntryPoint.MODULE_ID;
    }

    @Override
    public abstract String getId();

    @Override
    public abstract GrundData getGrundData();

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
    public abstract ImmutableList<Diagnos> getDiagnoser();

    // Fråga 7.1
    @Nullable
    public abstract String getDiagnosgrund();

    // Fråga 7.2
    @Nullable
    public abstract Boolean getNyBedomningDiagnosgrund();

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
    public abstract String getSubstansIntag();

    // Kategori 8 – Medicinska förutsättningar för arbete
    // Fråga 22.1
    @Nullable
    public abstract String getMedicinskaForutsattningarForArbete();

    // Fråga 23.1
    @Nullable
    public abstract String getAktivitetsFormaga();

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

    /* Retrieve a builder from an existing SjukersattningUtlatande object. The builder can then be used
    to create a new copy with modified attributes. */
    public abstract Builder toBuilder();

    public static Builder builder() {
        return new AutoValue_SjukersattningUtlatande.Builder().
                setUnderlag(ImmutableList.<Underlag> of()).
                setDiagnoser(ImmutableList.<Diagnos> of()).
                setTillaggsfragor(ImmutableList.<Tillaggsfraga>of());
    }

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract SjukersattningUtlatande build();

        @JsonProperty("id")
        public abstract Builder setId(String id);

        @JsonProperty("grundData")
        public abstract Builder setGrundData(GrundData grundData);

        @JsonProperty("undersokningAvPatienten")
        public abstract Builder setUndersokningAvPatienten(InternalDate undersokningAvPatienten);

        @JsonProperty("journaluppgifter")
        public abstract Builder setJournaluppgifter(InternalDate journaluppgifter);

        @JsonProperty("anhorigsBeskrivningAvPatienten")
        public abstract Builder setAnhorigsBeskrivningAvPatienten(InternalDate anhorigsBeskrivningAvPatienten);

        @JsonProperty("annatGrundForMU")
        public abstract Builder setAnnatGrundForMU(InternalDate annatGrundForMU);

        @JsonProperty("annatGrundForMUBeskrivning")
        public abstract Builder setAnnatGrundForMUBeskrivning(String annatGrundForMUBeskrivning);

        @JsonProperty("kannedomOmPatient")
        public abstract Builder setKannedomOmPatient(InternalDate kannedomOmPatient);

        @JsonProperty("underlagFinns")
        public abstract Builder setUnderlagFinns(Boolean underlagFinns);

        @JsonProperty("underlag")
        public Builder setUnderlag(List<Underlag> underlag) {
            return setUnderlag(ImmutableList.copyOf(underlag));
        }

        /* package private */
        abstract Builder setUnderlag(ImmutableList<Underlag> underlag);

        @JsonProperty("sjukdomsforlopp")
        public abstract Builder setSjukdomsforlopp(String sjukdomsforlopp);

        @JsonProperty("diagnoser")
        public Builder setDiagnoser(List<Diagnos> diagnoser) {
            return setDiagnoser(ImmutableList.copyOf(diagnoser));
        }

        /* package private */
        abstract Builder setDiagnoser(ImmutableList<Diagnos> diagnoser);

        @JsonProperty("diagnosgrund")
        public abstract Builder setDiagnosgrund(String diagnosgrund);

        @JsonProperty("nyBedomningDiagnosgrund")
        public abstract Builder setNyBedomningDiagnosgrund(Boolean nyBedomningDiagnosgrund);

        @JsonProperty("funktionsnedsattningIntellektuell")
        public abstract Builder setFunktionsnedsattningIntellektuell(String funktionsnedsattningIntellektuell);

        @JsonProperty("funktionsnedsattningKommunikation")
        public abstract Builder setFunktionsnedsattningKommunikation(String funktionsnedsattningKommunikation);

        @JsonProperty("funktionsnedsattningKoncentration")
        public abstract Builder setFunktionsnedsattningKoncentration(String funktionsnedsattningKoncentration);

        @JsonProperty("funktionsnedsattningPsykisk")
        public abstract Builder setFunktionsnedsattningPsykisk(String funktionsnedsattningPsykisk);

        @JsonProperty("funktionsnedsattningSynHorselTal")
        public abstract Builder setFunktionsnedsattningSynHorselTal(String funktionsnedsattningSynHorselTal);

        @JsonProperty("funktionsnedsattningBalansKoordination")
        public abstract Builder setFunktionsnedsattningBalansKoordination(String funktionsnedsattningBalansKoordination);

        @JsonProperty("funktionsnedsattningAnnan")
        public abstract Builder setFunktionsnedsattningAnnan(String funktionsnedsattningAnnan);

        @JsonProperty("aktivitetsbegransning")
        public abstract Builder setAktivitetsbegransning(String aktivitetsbegransning);

        @JsonProperty("pagaendeBehandling")
        public abstract Builder setPagaendeBehandling(String pagaendeBehandling);

        @JsonProperty("avslutadBehandling")
        public abstract Builder setAvslutadBehandling(String avslutadBehandling);

        @JsonProperty("planeradBehandling")
        public abstract Builder setPlaneradBehandling(String planeradBehandling);

        @JsonProperty("substansIntag")
        public abstract Builder setSubstansIntag(String substansIntag);

        @JsonProperty("medicinskaForutsattningarForArbete")
        public abstract Builder setMedicinskaForutsattningarForArbete(String medicinskaForutsattningarForArbete);

        @JsonProperty("aktivitetsFormaga")
        public abstract Builder setAktivitetsFormaga(String aktivitetsFormaga);

        @JsonProperty("ovrigt")
        public abstract Builder setOvrigt(String ovrigt);

        @JsonProperty("kontaktMedFk")
        public abstract Builder setKontaktMedFk(Boolean kontaktMedFk);

        @JsonProperty("anledningTillKontakt")
        public abstract Builder setAnledningTillKontakt(String anledningTillKontakt);

        @JsonProperty("tillaggsfragor")
        public Builder setTillaggsfragor(List<Tillaggsfraga> tillaggsfragor) {
            return setTillaggsfragor(ImmutableList.copyOf(tillaggsfragor));
        }

        /* package private */
        abstract Builder setTillaggsfragor(ImmutableList<Tillaggsfraga> tillaggsfragor);
    }

}
