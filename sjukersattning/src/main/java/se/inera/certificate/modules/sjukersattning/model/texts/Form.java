package se.inera.certificate.modules.sjukersattning.model.texts;

import java.util.List;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;

@AutoValue
public abstract class Form {

    protected Form() {
    }

    @JsonCreator
    public static Form create(@JsonProperty("id") String id, @JsonProperty("title") String title, @JsonProperty("helpText") String helpText,
            @JsonProperty("categories") List<Category> categories, @JsonProperty("alternatives") List<Alternatives> alternatives) {
        return new AutoValue_Form(id, title, helpText, ImmutableList.copyOf(categories), ImmutableList.copyOf(alternatives));
    }

    public abstract String getId();

    public abstract String getTitle();

    @Nullable
    public abstract String getHelpText();

    public abstract List<Category> getCategories();

    public abstract List<Alternatives> getAlternatives();

}
