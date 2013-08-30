package se.inera.certificate.modules.fk7263.model.converter;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import se.inera.certificate.fk7263.model.v1.Utlatande;
import se.inera.certificate.integration.json.CustomObjectMapper;
import se.inera.certificate.modules.fk7263.model.external.Fk7263Utlatande;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author marced, andreaskaltenbach
 */
public class TransportToExternalConverterTest {

    @Test
    public void testConversion() throws JAXBException, IOException {

        // read fk7263 xml from file
        JAXBContext jaxbContext = JAXBContext.newInstance(se.inera.certificate.fk7263.model.v1.Utlatande.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        JAXBElement<Utlatande> utlatandeElement = unmarshaller.unmarshal(new StreamSource(new ClassPathResource("TransportToExternalConverterTest/maximalt-fk7263.xml").getInputStream()), Utlatande.class);

        Fk7263Utlatande externalFormat = TransportToExternalConverter.convert(utlatandeElement.getValue());

        // serialize utlatande to JSON and compare with expected JSON
        ObjectMapper objectMapper = new CustomObjectMapper();
        JsonNode tree = objectMapper.valueToTree(externalFormat);
        JsonNode expectedTree = objectMapper.readTree(new ClassPathResource("TransportToExternalConverterTest/maximalt-fk7263.json").getInputStream());

        assertEquals("JSON does not match expectation. Resulting JSON is \n" + tree.toString() + "\n", expectedTree, tree);
    }
}
