package se.inera.certificate.modules.sjukersattning.model.texts;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;

@AutoValue
public abstract class Alternatives {

    Alternatives() {
    }

    @JsonCreator
    public static Alternatives create(@JsonProperty("description") String description, @JsonProperty("alternatives") List<Alternative> alternatives) {
        return new AutoValue_Alternatives(description, ImmutableList.copyOf(alternatives));
    }

    public abstract String getDescription();

    public abstract List<Alternative> getAlternatives();

}
