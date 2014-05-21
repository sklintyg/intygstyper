package se.inera.certificate.modules.fk7263.model.converter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.core.io.ClassPathResource;

import se.inera.certificate.fk7263.insuranceprocess.healthreporting.mu7263.v3.Lakarutlatande;
import se.inera.certificate.integration.json.CustomObjectMapper;
import se.inera.certificate.model.Observation;
import se.inera.certificate.modules.fk7263.model.external.Fk7263Utlatande;

/**
 * @author marced
 */
public class TransportToExternalFk7263LegacyConverterTest {

    private JAXBContext jaxbContext;
    private Unmarshaller unmarshaller;
    private ObjectMapper objectMapper;
    private final static String resourceRoot = "TransportToExternalFk7263LegacyConverterTest/";

    private JAXBElement<Lakarutlatande> readUtlatandeTypeFromFile(String file)
            throws JAXBException, IOException {
        JAXBElement<Lakarutlatande> utlatandeElement = unmarshaller.unmarshal(
                new StreamSource(new ClassPathResource(file).getInputStream()),
                Lakarutlatande.class);
        return utlatandeElement;
    }

    @Before
    public void setUp() throws JAXBException, IOException {
        jaxbContext = JAXBContext
                .newInstance(se.inera.certificate.fk7263.insuranceprocess.healthreporting.mu7263.v3.Lakarutlatande.class);
        unmarshaller = jaxbContext.createUnmarshaller();
        objectMapper = new CustomObjectMapper();
    }

    @Test
    public void testConversionWithMaximalCertificate() throws JAXBException,
            IOException, JSONException {

        // read utlatandeType from file

        JAXBElement<Lakarutlatande> utlatandeElement = readUtlatandeTypeFromFile(resourceRoot
                + "legacy-maximalt-fk7263-transport.xml");

        Fk7263Utlatande externalModel = TransportToExternalFk7263LegacyConverter
                .convert(utlatandeElement.getValue());

        // serialize utlatande to JSON and compare with expected JSON
        JsonNode tree = objectMapper.valueToTree(externalModel);
        JsonNode expectedTree = objectMapper.readTree(new ClassPathResource(
                resourceRoot + "legacy-maximalt-fk7263-external.json")
                .getInputStream());

        JSONAssert.assertEquals(expectedTree.toString(), tree.toString(), false);
    }

    @Test
    public void testConversionWithMinimalCertificate() throws JAXBException,
            IOException, JSONException {

        // read utlatandeType from file
        JAXBElement<Lakarutlatande> utlatandeElement = readUtlatandeTypeFromFile(resourceRoot
                + "legacy-minimalt-fk7263-transport.xml");

        Fk7263Utlatande externalModel = TransportToExternalFk7263LegacyConverter
                .convert(utlatandeElement.getValue());

        // serialize utlatande to JSON and compare with expected JSON
        JsonNode tree = objectMapper.valueToTree(externalModel);
        JsonNode expectedTree = objectMapper.readTree(new ClassPathResource(
                resourceRoot + "legacy-minimalt-fk7263-external.json")
                .getInputStream());

        JSONAssert.assertEquals(expectedTree.toString(), tree.toString(), false);
    }

    @Test
    public void testConversionWithNoPrognosAngivelseButMotivering() throws JAXBException,
            IOException, JSONException {

        // read utlatandeType from file
        JAXBElement<Lakarutlatande> utlatandeElement = readUtlatandeTypeFromFile(resourceRoot
                + "legacy-fk7263-without-prognoskod-transport.xml");

        Fk7263Utlatande externalModel = TransportToExternalFk7263LegacyConverter
                .convert(utlatandeElement.getValue());

        // serialize utlatande to JSON and compare with expected JSON
        JsonNode tree = objectMapper.valueToTree(externalModel);
        JsonNode expectedTree = objectMapper.readTree(new ClassPathResource(
                resourceRoot + "legacy-fk7263-without-prognoskod-external.json")
                .getInputStream());

        JSONAssert.assertEquals(expectedTree.toString(), tree.toString(), false);
    }

    // Below are tests for different ways of filling out an FK7263-form,
    // see readme in
    // /resource/TransportToExternalFK763LegacyConverterTest/legacy for more
    // info

    /**
     * Tests scenario 1, with fields: 1, 8b, 14 - 17.
     *
     * @throws JAXBException
     * @throws IOException
     * @throws JSONException
     */
    @Test
    public void testScenario1() throws JAXBException, IOException, JSONException {
        JAXBElement<Lakarutlatande> utlatandeElement = readUtlatandeTypeFromFile(resourceRoot
                + "legacy/scenario1.xml");
        Fk7263Utlatande externalModel = TransportToExternalFk7263LegacyConverter
                .convert(utlatandeElement.getValue());

        // serialize utlatande to JSON and compare with expected JSON
        JsonNode tree = objectMapper.valueToTree(externalModel);
        JsonNode expectedTree = objectMapper.readTree(new ClassPathResource(
                resourceRoot + "legacy/scenario1.json").getInputStream());

        JSONAssert.assertEquals(expectedTree.toString(), tree.toString(), false);
    }

    /**
     * Tests scenario 2 with fields: 1, 8b, 10, 14-17.
     *
     * @throws JAXBException
     * @throws IOException
     * @throws JSONException
     */
    @Test
    public void testScenario2() throws JAXBException, IOException, JSONException {
        JAXBElement<Lakarutlatande> utlatandeElement = readUtlatandeTypeFromFile(resourceRoot
                + "legacy/scenario2.xml");
        Fk7263Utlatande externalModel = TransportToExternalFk7263LegacyConverter
                .convert(utlatandeElement.getValue());

        // serialize utlatande to JSON and compare with expected JSON
        JsonNode tree = objectMapper.valueToTree(externalModel);
        JsonNode expectedTree = objectMapper.readTree(new ClassPathResource(
                resourceRoot + "legacy/scenario2.json").getInputStream());

        JSONAssert.assertEquals(expectedTree.toString(), tree.toString(), false);
    }

    /**
     * Tests scenario 3 with fields: 1, 8b, 10, 13, 14-17.
     *
     * @throws JAXBException
     * @throws IOException
     * @throws JSONException
     */
    @Test
    public void testScenario3() throws JAXBException, IOException, JSONException {
        JAXBElement<Lakarutlatande> utlatandeElement = readUtlatandeTypeFromFile(resourceRoot
                + "legacy/scenario3.xml");
        Fk7263Utlatande externalModel = TransportToExternalFk7263LegacyConverter
                .convert(utlatandeElement.getValue());

        // serialize utlatande to JSON and compare with expected JSON
        JsonNode tree = objectMapper.valueToTree(externalModel);
        JsonNode expectedTree = objectMapper.readTree(new ClassPathResource(
                resourceRoot + "legacy/scenario3.json").getInputStream());

        JSONAssert.assertEquals(expectedTree.toString(), tree.toString(), false);
    }

    /**
     * Tests scenario 4 with fields: 2b, 4a, 4b, 5, 8a, 8b, 14-17.
     *
     * @throws JAXBException
     * @throws IOException
     * @throws JSONException
     */
    @Test
    public void testScenario4() throws JAXBException, IOException, JSONException {
        JAXBElement<Lakarutlatande> utlatandeElement = readUtlatandeTypeFromFile(resourceRoot
                + "legacy/scenario4.xml");
        Fk7263Utlatande externalModel = TransportToExternalFk7263LegacyConverter
                .convert(utlatandeElement.getValue());

        // serialize utlatande to JSON and compare with expected JSON
        JsonNode tree = objectMapper.valueToTree(externalModel);
        JsonNode expectedTree = objectMapper.readTree(new ClassPathResource(
                resourceRoot + "legacy/scenario4.json").getInputStream());

        JSONAssert.assertEquals(expectedTree.toString(), tree.toString(), false);
    }

    /**
     * Tests scenario 5 with fields: 2a, 2b, 4a, 4b, 5, 8a, 8b, 14-17.
     *
     * @throws JAXBException
     * @throws IOException
     * @throws JSONException
     */
    @Test
    public void testScenario5() throws JAXBException, IOException, JSONException {
        JAXBElement<Lakarutlatande> utlatandeElement = readUtlatandeTypeFromFile(resourceRoot
                + "legacy/scenario5.xml");
        Fk7263Utlatande externalModel = TransportToExternalFk7263LegacyConverter
                .convert(utlatandeElement.getValue());

        // serialize utlatande to JSON and compare with expected JSON
        JsonNode tree = objectMapper.valueToTree(externalModel);
        JsonNode expectedTree = objectMapper.readTree(new ClassPathResource(
                resourceRoot + "legacy/scenario5.json").getInputStream());

        JSONAssert.assertEquals(expectedTree.toString(), tree.toString(), false);
    }

    /**
     * Tests scenario 6 with fields: 2a, 2b, 3, 4a, 4b, 5, 8a, 8b, 14-17.
     *
     * @throws JAXBException
     * @throws IOException
     * @throws JSONException
     */
    @Test
    public void testScenario6() throws JAXBException, IOException, JSONException {
        JAXBElement<Lakarutlatande> utlatandeElement = readUtlatandeTypeFromFile(resourceRoot
                + "legacy/scenario6.xml");
        Fk7263Utlatande externalModel = TransportToExternalFk7263LegacyConverter
                .convert(utlatandeElement.getValue());

        // serialize utlatande to JSON and compare with expected JSON
        JsonNode tree = objectMapper.valueToTree(externalModel);
        JsonNode expectedTree = objectMapper.readTree(new ClassPathResource(
                resourceRoot + "legacy/scenario6.json").getInputStream());

        JSONAssert.assertEquals(expectedTree.toString(), tree.toString(), false);
    }

    /**
     * Tests scenario 7 with fields: 2b, 4a, 4b, 5, 6b, 8a, 8b, 11, 13, 14-17.
     *
     * @throws JAXBException
     * @throws IOException
     * @throws JSONException
     */
    @Test
    public void testScenario7() throws JAXBException, IOException, JSONException {
        JAXBElement<Lakarutlatande> utlatandeElement = readUtlatandeTypeFromFile(resourceRoot
                + "legacy/scenario7.xml");
        Fk7263Utlatande externalModel = TransportToExternalFk7263LegacyConverter
                .convert(utlatandeElement.getValue());

        // serialize utlatande to JSON and compare with expected JSON
        JsonNode tree = objectMapper.valueToTree(externalModel);
        JsonNode expectedTree = objectMapper.readTree(new ClassPathResource(
                resourceRoot + "legacy/scenario7.json").getInputStream());

        JSONAssert.assertEquals(expectedTree.toString(), tree.toString(), false);
    }

    /**
     * Tests scenario 8 with fields: 2b, 4a, 4b, 5, 8a, 8b, 11, 14-17.
     *
     * @throws JAXBException
     * @throws IOException
     * @throws JSONException
     */
    @Test
    public void testScenario8() throws JAXBException, IOException, JSONException {
        JAXBElement<Lakarutlatande> utlatandeElement = readUtlatandeTypeFromFile(resourceRoot
                + "legacy/scenario8.xml");
        Fk7263Utlatande externalModel = TransportToExternalFk7263LegacyConverter
                .convert(utlatandeElement.getValue());

        // serialize utlatande to JSON and compare with expected JSON
        JsonNode tree = objectMapper.valueToTree(externalModel);
        JsonNode expectedTree = objectMapper.readTree(new ClassPathResource(
                resourceRoot + "legacy/scenario8.json").getInputStream());

        JSONAssert.assertEquals(expectedTree.toString(), tree.toString(), false);
    }

    /**
     * Tests scenario 9 with fields: 2a, 2b, 4a, 4b, 5, 7, 8a, 8b, 9, 10, 11,
     * 14-17.
     *
     * @throws JAXBException
     * @throws IOException
     * @throws JSONException
     */
    @Test
    public void testScenario9() throws JAXBException, IOException, JSONException {
        JAXBElement<Lakarutlatande> utlatandeElement = readUtlatandeTypeFromFile(resourceRoot
                + "legacy/scenario9.xml");
        Fk7263Utlatande externalModel = TransportToExternalFk7263LegacyConverter
                .convert(utlatandeElement.getValue());

        // serialize utlatande to JSON and compare with expected JSON
        JsonNode tree = objectMapper.valueToTree(externalModel);
        JsonNode expectedTree = objectMapper.readTree(new ClassPathResource(
                resourceRoot + "legacy/scenario9.json").getInputStream());
        JSONAssert.assertEquals(expectedTree.toString(), tree.toString(), false);
    }

    /**
     * Tests scenario 10 with fields: 2a, 2b, 3, 4a, 4b, 5, 6b, 7, 8a, 8b, 9,
     * 10, 12, 14-17.
     *
     * @throws JAXBException
     * @throws IOException
     * @throws JSONException
     */
    @Test
    public void testScenario10() throws JAXBException, IOException, JSONException {
        JAXBElement<Lakarutlatande> utlatandeElement = readUtlatandeTypeFromFile(resourceRoot
                + "legacy/scenario10.xml");
        Fk7263Utlatande externalModel = TransportToExternalFk7263LegacyConverter
                .convert(utlatandeElement.getValue());

        // serialize utlatande to JSON and compare with expected JSON
        JsonNode tree = objectMapper.valueToTree(externalModel);
        JsonNode expectedTree = objectMapper.readTree(new ClassPathResource(
                resourceRoot + "legacy/scenario10.json").getInputStream());

        JSONAssert.assertEquals(expectedTree.toString(), tree.toString(), false);
    }

    /**
     * Tests scenario 11 with fields: 2a, 2b, 3, 4a, 4b, 5, 6a, 6b, 7, 8a, 8b,
     * 9, 10, 14-17
     * 
     * @throws JAXBException
     * @throws IOException
     * @throws JSONException
     */
    @Test
    public void testScenario11() throws JAXBException, IOException, JSONException {
        JAXBElement<Lakarutlatande> utlatandeElement = readUtlatandeTypeFromFile(resourceRoot
                + "legacy/scenario11.xml");
        Fk7263Utlatande externalModel = TransportToExternalFk7263LegacyConverter
                .convert(utlatandeElement.getValue());

        // serialize utlatande to JSON and compare with expected JSON
        JsonNode tree = objectMapper.valueToTree(externalModel);
        JsonNode expectedTree = objectMapper.readTree(new ClassPathResource(
                resourceRoot + "legacy/scenario11.json").getInputStream());

        JSONAssert.assertEquals(expectedTree.toString(), tree.toString(), false);
    }

    /**
     * Tests scenario 12 with fields: 2a, 2b, 3, 4a, 4b, 5, 6a, 6b, 7, 8a, 8b,
     * 9, 10, 12, 13, 14-17.
     *
     * @throws JAXBException
     * @throws IOException
     * @throws JSONException
     */
    @Test
    public void testScenario12() throws JAXBException, IOException, JSONException {
        JAXBElement<Lakarutlatande> utlatandeElement = readUtlatandeTypeFromFile(resourceRoot
                + "legacy/scenario12.xml");
        Fk7263Utlatande externalModel = TransportToExternalFk7263LegacyConverter
                .convert(utlatandeElement.getValue());

        // serialize utlatande to JSON and compare with expected JSON
        JsonNode tree = objectMapper.valueToTree(externalModel);
        JsonNode expectedTree = objectMapper.readTree(new ClassPathResource(
                resourceRoot + "legacy/scenario12.json").getInputStream());

        JSONAssert.assertEquals(expectedTree.toString(), tree.toString(), false);
    }

    /**
     * Tests scenario 13 with fields: 2a, 2b, 3, 4a, 4b, 5, 6a, 6b, 7, 8a, 8b,
     * 9, 10, 11, 12, 13, 14-17.
     *
     * @throws JAXBException
     * @throws IOException
     * @throws JSONException
     */
    @Test
    public void testScenario13() throws JAXBException, IOException, JSONException {
        JAXBElement<Lakarutlatande> utlatandeElement = readUtlatandeTypeFromFile(resourceRoot
                + "legacy/scenario13.xml");
        Fk7263Utlatande externalModel = TransportToExternalFk7263LegacyConverter
                .convert(utlatandeElement.getValue());

        // serialize utlatande to JSON and compare with expected JSON
        JsonNode tree = objectMapper.valueToTree(externalModel);
        JsonNode expectedTree = objectMapper.readTree(new ClassPathResource(
                resourceRoot + "legacy/scenario13.json").getInputStream());

        JSONAssert.assertEquals(expectedTree.toString(), tree.toString(), false);
    }
}
