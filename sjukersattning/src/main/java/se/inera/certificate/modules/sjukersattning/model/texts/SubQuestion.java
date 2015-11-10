package se.inera.certificate.modules.sjukersattning.model.texts;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class SubQuestion {

    protected SubQuestion() {
    }

    @JsonCreator
    public static SubQuestion create(@JsonProperty("id") String id, @JsonProperty("title") String title, @JsonProperty("helpText") String helpText) {
        return new AutoValue_SubQuestion(id, title, helpText);
    }

    public abstract String getId();

    public abstract String getTitle();

    @Nullable
    public abstract String getHelpText();
}
