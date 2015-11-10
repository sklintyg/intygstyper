package se.inera.certificate.modules.sjukersattning.model.internal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class BehandlingsAtgard {

    protected BehandlingsAtgard() {
    }

    @JsonCreator
    public static BehandlingsAtgard create(@JsonProperty("atgardsKod") String atgardsKod,
            @JsonProperty("atgardsKodSystem") String atgardsKodSystem,
            @JsonProperty("atgardsBeskrivning") String atgardsBeskrivning) {
        return new AutoValue_BehandlingsAtgard(atgardsKod, atgardsKodSystem, atgardsBeskrivning);
    }

    public abstract String getAtgardsKod();

    public abstract String getAtgardsKodSystem();

    public abstract String getAtgardsBeskrivning();

}
