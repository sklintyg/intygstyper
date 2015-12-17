package se.inera.certificate.modules.sjukersattning.model.texts;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Alternative {

    Alternative() {
    }

    @JsonCreator
    public static Alternative create(@JsonProperty("id") int id, @JsonProperty("description") String description) {
        return new AutoValue_Alternative(id, description);
    }

    public abstract int getId();

    public abstract String getDescription();

}
