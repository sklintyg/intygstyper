package se.inera.certificate.modules.sjukersattning.model.texts;

import java.util.List;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;

@AutoValue
public abstract class Category {

    protected Category() {
    }

    @JsonCreator
    public static Category create(@JsonProperty("id") String id, @JsonProperty("title") String title, @JsonProperty("helpText") String helpText,
            @JsonProperty("questions") List<Question> questions) {
        return new AutoValue_Category(id, title, helpText, ImmutableList.copyOf(questions));
    }

    public abstract String getId();

    public abstract String getTitle();

    @Nullable
    public abstract String getHelpText();

    public abstract List<Question> getQuestions();
}
