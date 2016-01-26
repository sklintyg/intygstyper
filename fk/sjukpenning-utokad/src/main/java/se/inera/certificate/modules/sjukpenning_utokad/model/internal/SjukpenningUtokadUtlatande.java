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

import java.util.List;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;

import se.inera.certificate.modules.sjukpenning_utokad.support.SjukpenningUtokadEntryPoint;
import se.inera.intyg.common.support.model.InternalDate;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.Utlatande;

@AutoValue
@JsonDeserialize(builder = AutoValue_SjukpenningUtokadUtlatande.Builder.class)
public abstract class SjukpenningUtokadUtlatande implements Utlatande {

    SjukpenningUtokadUtlatande() {
    }

    @Override
    public String getTyp() {
        return SjukpenningUtokadEntryPoint.MODULE_ID;
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
    public abstract InternalDate getTelefonkontaktMedPatienten();

    @Nullable
    public abstract InternalDate getJournaluppgifter();

    @Nullable
    public abstract InternalDate getAnhorigsBeskrivningAvPatienten();

    @Nullable
    public abstract InternalDate getAnnatGrundForMU();

    // Fråga  1.3 Annan grund för MU
    @Nullable
    public abstract String getAnnatGrundForMUBeskrivning();

    // Kategori 2 - Sysselsättning
    // Fråga 28
    // Fråga 28.1
    public abstract Sysselsattning getSysselsattning();

    // Fråga 29 - Nuvarande arbete
    // Fråga 29.1
    @Nullable
    public abstract String getNuvarandeArbete();

    // Fråga 30 - Arbetsmarknadspolitiskt program
    // Fråga 30.1
    @Nullable
    public abstract String getArbetsmarknadspolitisktProgram();

    // Kategori 3 - Diagnos
    // Fråga 6
    public abstract ImmutableList<Diagnos> getDiagnoser();

    // Kategori 4 - Sjukdomens konsekvenser
    // Fråga 35 - Funktionsnedsättning
    // Fråga 35.1
    public abstract String getFunktionsnedsattning();

    // Fråga 17 Aktivitetsbegränsning
    // Fråga 17.1
    public abstract String getAktivitetsbegransning();

    // Kategori 5 - Medicinska behandlingar / åtgärder
    // Fråga 19 -Pågående medicinska behandlingar
    // Fråga 19.1 - Typ av pågående medicinska behandlingar
    @Nullable
    public abstract String getPagaendeBehandling();

    // Fråga 20 - Planerad medicinsk behandling
    // Fråga 20.1
    @Nullable
    public abstract String getPlaneradBehandling();

    // Kategori 6 - Bedömning
    // Fråga 32 - Behov av sjukskrivning
    // 32.1
    public abstract ImmutableList<Sjukskrivning> getSjukskrivningar();

    // Fråga 37 - försäkringsmedicinskt beslutsstöd
    // 37.1
    @Nullable
    public abstract String getForsakringsmedicinsktBeslutsstod();

    // Fråga 33 - Arbetstidsförläggning
    // 33.1
    @Nullable
    public abstract Boolean getArbetstidsforlaggning();

    // 33.2
    @Nullable
    public abstract String getArbetstidsforlaggningMotivering();

    // Fråga 34 - Arbetsresor
    // 34.1
    @Nullable
    public abstract Boolean getArbetsresor();

    // Fråga 23 - Förmåga trots begränsning
    // 23.1
    @Nullable
    public abstract String getFormagaTrotsBegransning();

    // Fråga 39
    public abstract Prognos getPrognos();

    // Kategori 7 - Åtgärder
    // Fråga 40 - Arbetslivsinriktade åtgärder
    public abstract ImmutableList<ArbetslivsinriktadeAtgarder> getArbetslivsinriktadeAtgarder();

    @Nullable
    // Delfråga 40.2 - Arbetslivsinriktade åtgärder aktuellt beksrivning
    public abstract String getArbetslivsinriktadeAtgarderAktuelltBeskrivning();

    @Nullable
    // Delfråga 40.3 - Arbetslivsinriktade åtgärder ej aktuellt beksrivning
    public abstract String getArbetslivsinriktadeAtgarderEjAktuelltBeskrivning();

    // Kategori 8 - Övrigt
    // Fråga 25
    @Nullable
    public abstract String getOvrigt();

    // Kategori 9 - Kontakt
    // Fråga 26.1
    @Nullable
    public abstract Boolean getKontaktMedFk();

    // Fråga 26.2
    @Nullable
    public abstract String getAnledningTillKontakt();

    // Tilläggsfrågor
    @Nullable
    public abstract ImmutableList<Tillaggsfraga> getTillaggsfragor();

    /*
     * Retrieve a builder from an existing SjukersattningUtlatande object. The builder can then be used
     * to create a new copy with modified attributes.
     */
    public abstract Builder toBuilder();

    public static Builder builder() {
        return new AutoValue_SjukpenningUtokadUtlatande.Builder()
            .setDiagnoser(ImmutableList.<Diagnos> of())
            .setSjukskrivningar(ImmutableList.<Sjukskrivning> of())
            .setArbetslivsinriktadeAtgarder(ImmutableList.<ArbetslivsinriktadeAtgarder> of())
            .setTillaggsfragor(ImmutableList.<Tillaggsfraga>of());
    }

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract SjukpenningUtokadUtlatande build();

        @JsonProperty("id")
        public abstract Builder setId(String id);

        @JsonProperty("grundData")
        public abstract Builder setGrundData(GrundData grundData);

        @JsonProperty("undersokningAvPatienten")
        public abstract Builder setUndersokningAvPatienten(InternalDate undersokningAvPatienten);

        @JsonProperty("telefonkontaktMedPatienten")
        public abstract Builder setTelefonkontaktMedPatienten(InternalDate telefonkontaktMedPatienten);

        @JsonProperty("journaluppgifter")
        public abstract Builder setJournaluppgifter(InternalDate journaluppgifter);

        @JsonProperty("anhorigsBeskrivningAvPatienten")
        public abstract Builder setAnhorigsBeskrivningAvPatienten(InternalDate anhorigsBeskrivningAvPatienten);

        @JsonProperty("annatGrundForMU")
        public abstract Builder setAnnatGrundForMU(InternalDate annatGrundForMU);

        @JsonProperty("annatGrundForMUBeskrivning")
        public abstract Builder setAnnatGrundForMUBeskrivning(String annatGrundForMUBeskrivning);

        @JsonProperty("sysselsattning")
        public abstract Builder setSysselsattning(Sysselsattning sysselsattning);

        @JsonProperty("nuvarandeArbete")
        public abstract Builder setNuvarandeArbete(String nuvarandeArbete);

        @JsonProperty("arbetsmarknadspolitisktProgram")
        public abstract Builder setArbetsmarknadspolitisktProgram(String arbetsmarknadspolitisktProgram);

        @JsonProperty("diagnoser")
        public Builder setDiagnoser(List<Diagnos> diagnoser) {
            return setDiagnoser(ImmutableList.copyOf(diagnoser));
        }

        /* package private */
        abstract Builder setDiagnoser(ImmutableList<Diagnos> diagnoser);

        @JsonProperty("funktionsnedsattning")
        public abstract Builder setFunktionsnedsattning(String funktionsnedsattning);

        @JsonProperty("aktivitetsbegransning")
        public abstract Builder setAktivitetsbegransning(String aktivitetsbegransning);

        @JsonProperty("pagaendeBehandling")
        public abstract Builder setPagaendeBehandling(String pagaendeBehandling);

        @JsonProperty("planeradBehandling")
        public abstract Builder setPlaneradBehandling(String planeradBehandling);

        @JsonProperty("sjukskrivningar")
        public Builder setSjukskrivningar(List<Sjukskrivning> sjukskrivningar) {
            return setSjukskrivningar(ImmutableList.copyOf(sjukskrivningar));
        }
        /* package private */
        abstract Builder setSjukskrivningar(ImmutableList<Sjukskrivning> sjukskrivningar);

        @JsonProperty("forsakringsmedicinsktBeslutsstod")
        public abstract Builder setForsakringsmedicinsktBeslutsstod(String forskningsmedicinsktBeslutsstod);

        @JsonProperty("arbetstidsforlaggning")
        public abstract Builder setArbetstidsforlaggning(Boolean arbetstidsforlaggning);

        @JsonProperty("arbetstidsforlaggningMotivering")
        public abstract Builder setArbetstidsforlaggningMotivering(String arbetstidsforlaggningMotivering);

        @JsonProperty("arbetsresor")
        public abstract Builder setArbetsresor(Boolean arbetsresor);

        @JsonProperty("formagaTrotsBegransning")
        public abstract Builder setFormagaTrotsBegransning(String formagaTrotsBegransning);

        @JsonProperty("prognos")
        public abstract Builder setPrognos(Prognos prognos);

        @JsonProperty("arbetslivsinriktadeAtgarder")
        public Builder setArbetslivsinriktadeAtgarder(List<ArbetslivsinriktadeAtgarder> arbetslivsinriktadeAtgarder) {
            return setArbetslivsinriktadeAtgarder(ImmutableList.copyOf(arbetslivsinriktadeAtgarder));
        }

        @JsonProperty("arbetslivsinriktadeAtgarderAktuelltBeskrivning")
        public abstract Builder setArbetslivsinriktadeAtgarderAktuelltBeskrivning(String arbetslivsinriktadeAtgarderAktuelltBeskrivning);

        @JsonProperty("arbetslivsinriktadeAtgarderEjAktuelltBeskrivning")
        public abstract Builder setArbetslivsinriktadeAtgarderEjAktuelltBeskrivning(String arbetslivsinriktadeAtgarderEjAktuelltBeskrivning);

        /* package private*/
        abstract Builder setArbetslivsinriktadeAtgarder(ImmutableList<ArbetslivsinriktadeAtgarder> arbetslivsinriktadeAtgarder);

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
