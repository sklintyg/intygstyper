package se.inera.certificate.modules.fk7263.model.converter;

import javax.xml.bind.JAXBException;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.xml.sax.SAXException;
import se.inera.certificate.integration.json.CustomObjectMapper;
import se.inera.certificate.model.converter.util.ConverterException;
import se.inera.certificate.modules.fk7263.model.external.Fk7263Utlatande;
import se.inera.certificate.modules.fk7263.model.internal.Fk7263Intyg;
import se.inera.certificate.modules.fk7263.utils.ModelAssert;

/**
 * @author andreaskaltenbach
 */
public class ExternalToInternalConverterTest {

    private ExternalToInternalConverter externalToInternalConverter;

    @Before
    public void setUp() throws Exception {
        externalToInternalConverter = new ExternalToInternalConverter();
    }

    @Test
    public void testConversion() throws JAXBException, IOException, SAXException, ConverterException {

        Fk7263Utlatande fk7263Utlatande = new CustomObjectMapper().readValue(new ClassPathResource(
                "ExternalToInternalConverterTest/utlatande_external.json").getFile(), Fk7263Utlatande.class);

        Fk7263Intyg internal = externalToInternalConverter.convert(fk7263Utlatande);

        // serialize utlatande to JSON and compare with expected JSON
        ObjectMapper objectMapper = new CustomObjectMapper();
        JsonNode tree = objectMapper.valueToTree(internal);
        JsonNode expectedTree = objectMapper.readTree(new ClassPathResource(
                "ExternalToInternalConverterTest/utlatande_internal.json").getInputStream());

        ModelAssert.assertEquals("JSON does not match expectation. Resulting JSON is \n" + tree.toString() + "\n", expectedTree,
                tree);
    }

}
