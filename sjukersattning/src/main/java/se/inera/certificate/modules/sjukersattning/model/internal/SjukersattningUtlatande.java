package se.inera.certificate.modules.sjukersattning.model.internal;

import javax.annotation.Nullable;

import se.inera.certificate.model.InternalDate;
import se.inera.certificate.model.common.internal.GrundData;
import se.inera.certificate.model.common.internal.Utlatande;
import se.inera.certificate.modules.sjukersattning.support.SjukersattningEntryPoint;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;

import java.util.List;

@AutoValue
@JsonDeserialize(builder = AutoValue_SjukersattningUtlatande.Builder.class)
public abstract class SjukersattningUtlatande implements Utlatande {

    @Override
    public String getTyp() {
        return SjukersattningEntryPoint.MODULE_ID;
    }

    @Override
    public abstract String getId();

    @Override
    public abstract GrundData getGrundData();

    @Nullable
    public abstract InternalDate getUndersokningAvPatienten();

    @Nullable
    public abstract InternalDate getTelefonkontaktMedPatienten();

    @Nullable
    public abstract InternalDate getJournaluppgifter();

    @Nullable
    public abstract InternalDate getKannedomOmPatient();

    public abstract ImmutableList<Underlag> getUnderlag();

    public abstract ImmutableList<Diagnos> getDiagnoser();

    public abstract ImmutableList<BehandlingsAtgard> getAtgarder();

    @Nullable
    public abstract String getDiagnostisering();

    @Nullable
    public abstract Boolean getNyBedomningDiagnos();

    public abstract ImmutableList<Funktionsnedsattning> getFunktionsnedsattningar();

    @Nullable
    public abstract String getAktivitetsbegransning();

    @Nullable
    public abstract String getPagaendeBehandling();

    @Nullable
    public abstract String getAvslutadBehandling();

    @Nullable
    public abstract String getPlaneradBehandling();

    @Nullable
    public abstract String getAktivitetsFormaga();

    @Nullable
    public abstract String getPrognos();

    @Nullable
    public abstract String getOvrigt();

    @Nullable
    public abstract Boolean getKontaktMedFk();

    public abstract Builder toBuilder();

    public static Builder builder() {
        return new AutoValue_SjukersattningUtlatande.Builder().
                setUnderlag(ImmutableList.<Underlag> of()).
                setDiagnoser(ImmutableList.<Diagnos> of()).
                setAtgarder(ImmutableList.<BehandlingsAtgard> of()).
                setFunktionsnedsattningar(ImmutableList.<Funktionsnedsattning> of());
    }

    @AutoValue.Builder
    public static abstract class Builder {

        public abstract SjukersattningUtlatande build();

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

        @JsonProperty("kannedomOmPatient")
        public abstract Builder setKannedomOmPatient(InternalDate kannedomOmPatient);

        @JsonProperty("underlag")
        public Builder setUnderlag(List<Underlag> underlag) {
            return setUnderlag(ImmutableList.copyOf(underlag));
        }

        public abstract Builder setUnderlag(ImmutableList<Underlag> underlag);

        @JsonProperty("diagnoser")
        public Builder setDiagnoser(List<Diagnos> diagnoser) {
            return setDiagnoser(ImmutableList.copyOf(diagnoser));
        }

        public abstract Builder setDiagnoser(ImmutableList<Diagnos> diagnoser);

        @JsonProperty("atgarder")
        public Builder setAtgarder(List<BehandlingsAtgard> atgarder) {
            return setAtgarder(ImmutableList.copyOf(atgarder));
        }

        public abstract Builder setAtgarder(ImmutableList<BehandlingsAtgard> atgarder);

        @JsonProperty("diagnostisering")
        public abstract Builder setDiagnostisering(String diagnostisering);

        @JsonProperty("nyBedomningDiagnos")
        public abstract Builder setNyBedomningDiagnos(Boolean nyBedomningDiagnos);

        @JsonProperty("funktionsnedsattningar")
        public Builder setFunktionsnedsattningar(List<Funktionsnedsattning> funktionsnedsattningar) {
            return setFunktionsnedsattningar(ImmutableList.copyOf(funktionsnedsattningar));
        }

        public abstract Builder setFunktionsnedsattningar(ImmutableList<Funktionsnedsattning> funktionsnedsattningar);

        @JsonProperty("aktivitetsbegransning")
        public abstract Builder setAktivitetsbegransning(String aktivitetsbegransning);

        @JsonProperty("pagaendeBehandling")
        public abstract Builder setPagaendeBehandling(String pagaendeBehandling);

        @JsonProperty("avslutadBehandling")
        public abstract Builder setAvslutadBehandling(String avslutadBehandling);

        @JsonProperty("planeradBehandling")
        public abstract Builder setPlaneradBehandling(String planeradBehandling);

        @JsonProperty("aktivitetsFormaga")
        public abstract Builder setAktivitetsFormaga(String aktivitetsFormaga);

        @JsonProperty("prognos")
        public abstract Builder setPrognos(String prognos);

        @JsonProperty("ovrigt")
        public abstract Builder setOvrigt(String ovrigt);

        @JsonProperty("kontaktMedFk")
        public abstract Builder setKontaktMedFk(Boolean kontaktMedFk);
    }

}
