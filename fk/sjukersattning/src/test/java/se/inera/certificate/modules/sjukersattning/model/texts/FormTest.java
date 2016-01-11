package se.inera.certificate.modules.sjukersattning.model.texts;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringWriter;

import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class FormTest {

    private static ObjectMapper objectMapper = new ObjectMapper();

    @BeforeClass
    public static void init() {
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    @Test
    public void testCategory() {
        Form form = getForm();
        System.out.println(form);
    }

    @Test
    public void testConversion() throws Exception {
        Form originalForm = getForm();
        String json = toJsonString(originalForm);
        System.out.println(json);
        Form convertedForm = fromJsonString(json);
        assertEquals(originalForm, convertedForm);
    }

    private Form getForm() {
        SubQuestion subQuestion11 = SubQuestion.create("1.1", "Title 1.1", null);
        SubQuestion subQuestion12 = SubQuestion.create("1.2", "Title 1.2", "Fill in this value");
        Question question1 = Question.create("1", "Title 1", "Some sub questions", asList(subQuestion11, subQuestion12));
        Category category1 = Category.create("1", "Category 1", "So many questions", asList(question1));
        Alternative alternative1 = Alternative.create(1, "Alternativ 1");
        Alternative alternative2 = Alternative.create(2, "Alternativ 2");
        Alternatives alternatives = Alternatives.create("Flera alternativ", asList(alternative1, alternative2));
        return Form.create("sjukersattning", "Läkarintyg, sjukersättning", "Läkarintyg för sjukersättning", asList(category1), asList(alternatives));
    }

    private String toJsonString(Form form) throws IOException {
        StringWriter writer = new StringWriter();
        objectMapper.writeValue(writer, form);
        return writer.toString();
    }

    public Form fromJsonString(String s) throws IOException {
        return objectMapper.readValue(s, Form.class);
    }

}
