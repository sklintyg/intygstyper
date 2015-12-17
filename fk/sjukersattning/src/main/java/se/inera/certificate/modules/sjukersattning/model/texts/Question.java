package se.inera.certificate.modules.sjukersattning.model.texts;

import java.util.List;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;

@AutoValue
public abstract class Question {

    Question() {
    }

    @JsonCreator
    public static Question create(@JsonProperty("id") String id, @JsonProperty("title") String title, @JsonProperty("helpText") String helpText,
            @JsonProperty("subQuestions") List<SubQuestion> subQuestions) {
        return new AutoValue_Question(id, title, helpText, ImmutableList.copyOf(subQuestions));
    }

    public abstract String getId();

    public abstract String getTitle();

    @Nullable
    public abstract String getHelpText();

    public abstract List<SubQuestion> getSubQuestions();
}
