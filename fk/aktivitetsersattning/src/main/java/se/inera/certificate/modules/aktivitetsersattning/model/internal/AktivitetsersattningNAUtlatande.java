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
package se.inera.certificate.modules.aktivitetsersattning.model.internal;

import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.AKTIVITETSBEGRANSNING_SVAR_JSON_ID_17;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.AKTIVITETSFORMAGA_SVAR_JSON_ID_23;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.ANLEDNING_TILL_KONTAKT_DELSVAR_JSON_ID_26;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.AVSLUTADBEHANDLING_SVAR_JSON_ID_18;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.DIAGNOSGRUND_NY_BEDOMNING_SVAR_JSON_ID_7;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.DIAGNOSGRUND_SVAR_JSON_ID_7;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.DIAGNOS_SVAR_JSON_ID_6;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.FORSLAG_TILL_ATGARD_SVAR_JSON_ID_24;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.FUNKTIONSNEDSATTNING_ANNAN_SVAR_JSON_ID_14;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.FUNKTIONSNEDSATTNING_BALANSKOORDINATION_SVAR_JSON_ID_13;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.FUNKTIONSNEDSATTNING_INTELLEKTUELL_SVAR_JSON_ID_8;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.FUNKTIONSNEDSATTNING_KOMMUNIKATION_SVAR_JSON_ID_9;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.FUNKTIONSNEDSATTNING_KONCENTRATION_SVAR_JSON_ID_10;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.FUNKTIONSNEDSATTNING_PSYKISK_SVAR_JSON_ID_11;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.FUNKTIONSNEDSATTNING_SYNHORSELTAL_SVAR_JSON_ID_12;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.GRUNDDATA_SVAR_JSON_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_ANHORIGS_BESKRIVNING_SVAR_JSON_ID_1;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_ANNAT_SVAR_JSON_ID_1;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_BESKRIVNING_DELSVAR_JSON_ID_1;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_JOURNALUPPGIFTER_SVAR_JSON_ID_1;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_UNDERSOKNING_AV_PATIENT_SVAR_JSON_ID_1;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.ID_JSON_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.KANNEDOM_SVAR_JSON_ID_2;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.KONTAKT_ONSKAS_SVAR_JSON_ID_26;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.MEDICINSKAFORUTSATTNINGARFORARBETE_SVAR_JSON_ID_22;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.OVRIGT_SVAR_JSON_ID_25;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.PAGAENDEBEHANDLING_SVAR_JSON_ID_19;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.PLANERADBEHANDLING_SVAR_JSON_ID_20;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.SJUKDOMSFORLOPP_SVAR_JSON_ID_5;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.SUBSTANSINTAG_SVAR_JSON_ID_21;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.TEXTVERSION_JSON_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.TILLAGGSFRAGOR_SVAR_JSON_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.UNDERLAGFINNS_SVAR_JSON_ID_3;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.UNDERLAG_SVAR_JSON_ID_4;

import java.util.List;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;

import se.inera.certificate.modules.aktivitetsersattning.support.AktivitetsersattningNAEntryPoint;
import se.inera.certificate.modules.fkparent.model.internal.Diagnos;
import se.inera.certificate.modules.fkparent.model.internal.SitUtlatande;
import se.inera.intyg.common.support.model.InternalDate;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.Utlatande;

@AutoValue
@JsonDeserialize(builder = AutoValue_AktivitetsersattningNAUtlatande.Builder.class)
public abstract class AktivitetsersattningNAUtlatande implements Utlatande, SitUtlatande {

    public abstract String getId();

    public String getTyp() {
       return AktivitetsersattningNAEntryPoint.MODULE_ID;
    }

    public abstract GrundData getGrundData();

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

 // Fråga 18
    @Nullable
    public abstract String getAvslutadBehandling();

 // Kategori 7 – Medicinska behandlingar/åtgärder
    // Fråga 19
    @Nullable
    public abstract String getPagaendeBehandling();

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

    // Fråga 24
    @Nullable
    public abstract String getForslagTillAtgard();

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
        return new AutoValue_AktivitetsersattningNAUtlatande.Builder().
                setUnderlag(ImmutableList.<Underlag> of()).
                setDiagnoser(ImmutableList.<Diagnos> of()).
                setTillaggsfragor(ImmutableList.<Tillaggsfraga>of());
    }

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract AktivitetsersattningNAUtlatande build();

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

        @JsonProperty(DIAGNOSGRUND_NY_BEDOMNING_SVAR_JSON_ID_7)
        public abstract Builder setNyBedomningDiagnosgrund(Boolean nyBedomningDiagnosgrund);

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

        @JsonProperty(AKTIVITETSFORMAGA_SVAR_JSON_ID_23)
        public abstract Builder setFormagaTrotsBegransning(String formagaTrotsBegransning);

        @JsonProperty(FORSLAG_TILL_ATGARD_SVAR_JSON_ID_24)
        public abstract Builder setForslagTillAtgard(String forslagTillAtgard);

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
