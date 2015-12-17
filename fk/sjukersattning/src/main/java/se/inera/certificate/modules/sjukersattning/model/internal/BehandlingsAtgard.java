package se.inera.certificate.modules.sjukersattning.model.internal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;

import javax.annotation.Nullable;

@AutoValue
public abstract class BehandlingsAtgard {

    BehandlingsAtgard() {
    }

    @JsonCreator
    public static BehandlingsAtgard create(@JsonProperty("atgardsKod") String atgardsKod,
            @JsonProperty("atgardsKodSystem") String atgardsKodSystem,
            @JsonProperty("atgardsBeskrivning") String atgardsBeskrivning) {
        return new AutoValue_BehandlingsAtgard(atgardsKod, atgardsKodSystem, atgardsBeskrivning);
    }

    @Nullable
    public abstract String getAtgardsKod();

    @Nullable
    public abstract String getAtgardsKodSystem();

    @Nullable
    public abstract String getAtgardsBeskrivning();

}
