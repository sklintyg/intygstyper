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

import se.inera.certificate.integration.json.CustomObjectMapper;
import se.inera.certificate.modules.fk7263.model.external.Fk7263Utlatande;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.Lakarutlatande;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author marced
 */
public class TransportToExternalFk7263LegacyConverterTest {

    @Test
    public void testConversion() throws JAXBException, IOException {

        // read utlatandeType from file
        JAXBContext jaxbContext = JAXBContext.newInstance(se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.Lakarutlatande.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        
        JAXBElement<Lakarutlatande> utlatandeElement = unmarshaller.unmarshal(new StreamSource(new ClassPathResource(
                "TransportToExternalFk7263LegacyConverterTest/legacy-maximalt-fk7263-transport.xml").getInputStream()), Lakarutlatande.class);

        Fk7263Utlatande externalModel = TransportToExternalFk7263LegacyConverter.convert(utlatandeElement.getValue());

        // serialize utlatande to JSON and compare with expected JSON
        ObjectMapper objectMapper = new CustomObjectMapper();
        JsonNode tree = objectMapper.valueToTree(externalModel);
        JsonNode expectedTree = objectMapper.readTree(new ClassPathResource("TransportToExternalFk7263LegacyConverterTest/legacy-maximalt-fk7263-external.json").getInputStream());

        assertEquals("JSON does not match expectation. Resulting JSON is \n" + tree.toString() + "\n", expectedTree, tree);
    }
    
    @Test
    public void testConversionWithMinimalCertificate() throws JAXBException, IOException {

        // read utlatandeType from file
        JAXBContext jaxbContext = JAXBContext.newInstance(se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.Lakarutlatande.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        
        JAXBElement<Lakarutlatande> utlatandeElement = unmarshaller.unmarshal(new StreamSource(new ClassPathResource(
                "TransportToExternalFk7263LegacyConverterTest/legacy-minimalt-fk7263-transport.xml").getInputStream()), Lakarutlatande.class);

        Fk7263Utlatande externalModel = TransportToExternalFk7263LegacyConverter.convert(utlatandeElement.getValue());
        
        // serialize utlatande to JSON and compare with expected JSON
        ObjectMapper objectMapper = new CustomObjectMapper();
        JsonNode tree = objectMapper.valueToTree(externalModel);
        JsonNode expectedTree = objectMapper.readTree(new ClassPathResource("TransportToExternalFk7263LegacyConverterTest/legacy-minimalt-fk7263-external.json").getInputStream());

        assertEquals("JSON does not match expectation. Resulting JSON is \n" + tree.toString() + "\n", expectedTree, tree);
    }
    
    @Test
    public void testInvalidPnrGetsCorrected() throws JAXBException, IOException {

        // read utlatandeType from file
        JAXBContext jaxbContext = JAXBContext.newInstance(se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.Lakarutlatande.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        
        JAXBElement<Lakarutlatande> utlatandeElement = unmarshaller.unmarshal(new StreamSource(new ClassPathResource(
                "TransportToExternalFk7263LegacyConverterTest/legacy-minimalt-fk7263-ofullstandigt-pnr-transport.xml").getInputStream()), Lakarutlatande.class);

        Fk7263Utlatande externalModel = TransportToExternalFk7263LegacyConverter.convert(utlatandeElement.getValue());
        
        // serialize utlatande to JSON and compare with expected JSON
        ObjectMapper objectMapper = new CustomObjectMapper();
        JsonNode tree = objectMapper.valueToTree(externalModel);
        JsonNode expectedTree = objectMapper.readTree(new ClassPathResource("TransportToExternalFk7263LegacyConverterTest/legacy-minimalt-fk7263-external.json").getInputStream());

        assertEquals("JSON does not match expectation. Resulting JSON is \n" + tree.toString() + "\n", expectedTree, tree);
    }
}
