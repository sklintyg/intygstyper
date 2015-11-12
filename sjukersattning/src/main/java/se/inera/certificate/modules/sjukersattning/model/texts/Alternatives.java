package se.inera.certificate.modules.sjukersattning.model.texts;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;

import java.util.List;

@AutoValue
public abstract class Alternatives {

    protected Alternatives() {
    }

    @JsonCreator
    public static Alternatives create(@JsonProperty("description") String description, @JsonProperty("alternatives") List<Alternative> alternatives) {
        return new AutoValue_Alternatives(description, ImmutableList.copyOf(alternatives));
    }

    public abstract String getDescription();

    public abstract List<Alternative> getAlternatives();

}
