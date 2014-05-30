package se.inera.certificate.modules.fk7263.model.converter;

import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
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

    private JAXBContext jaxbContext;
    private Unmarshaller unmarshaller;
    private static final String RESOURCE_ROOT = "TransportToExternalConverterTest/";
    private ObjectMapper objectMapper;

    private JAXBElement<Utlatande> readUtlatandeTypeFromFile(String file) throws JAXBException, IOException {
        JAXBElement<Utlatande> utlatandeElement =
                unmarshaller.unmarshal(new StreamSource(new ClassPathResource(file).getInputStream()), Utlatande.class);
        return utlatandeElement;
    }

    @Before
    public void setUp() throws JAXBException, IOException {
        jaxbContext = JAXBContext
                .newInstance(se.inera.certificate.fk7263.model.v1.Utlatande.class);
        unmarshaller = jaxbContext.createUnmarshaller();
        objectMapper = new CustomObjectMapper();
    }

    @Test
    public void testConversion() throws JAXBException, IOException, JSONException {

        // read fk7263 xml from file
        JAXBElement<Utlatande> utlatandeElement = readUtlatandeTypeFromFile(RESOURCE_ROOT
                + "maximalt-fk7263.xml");

        Fk7263Utlatande externalFormat = TransportToExternalConverter
                .convert(utlatandeElement.getValue());

        // serialize utlatande to JSON and compare with expected JSON
        JsonNode tree = objectMapper.valueToTree(externalFormat);
        JsonNode expectedTree = objectMapper.readTree(new ClassPathResource(
                RESOURCE_ROOT + "maximalt-fk7263.json").getInputStream());

        JSONAssert.assertEquals(expectedTree.toString(), tree.toString(), false);
    }

    // Below are tests for different ways of filling out an FK7263-form,
    // see readme in
    // /resource/TransportToExternalFK763transportConverterTest/transport for
    // more info

    /**
     * Tests scenario 1, with fields: 1, 8b, 14 - 17.
     *
     * @throws JAXBException
     * @throws IOException
     * @throws JSONException
     */
    @Test
    public void testScenario1() throws JAXBException, IOException, JSONException {
        JAXBElement<Utlatande> utlatandeElement = readUtlatandeTypeFromFile(RESOURCE_ROOT
                + "transport/scenario1.xml");
        Fk7263Utlatande externalModel = TransportToExternalConverter
                .convert(utlatandeElement.getValue());

        // serialize utlatande to JSON and compare with expected JSON
        JsonNode tree = objectMapper.valueToTree(externalModel);
        JsonNode expectedTree = objectMapper.readTree(new ClassPathResource(
                RESOURCE_ROOT + "transport/scenario1.json").getInputStream());

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
        JAXBElement<Utlatande> utlatandeElement = readUtlatandeTypeFromFile(RESOURCE_ROOT
                + "transport/scenario2.xml");
        Fk7263Utlatande externalModel = TransportToExternalConverter
                .convert(utlatandeElement.getValue());

        // serialize utlatande to JSON and compare with expected JSON
        JsonNode tree = objectMapper.valueToTree(externalModel);
        JsonNode expectedTree = objectMapper.readTree(new ClassPathResource(
                RESOURCE_ROOT + "transport/scenario2.json").getInputStream());

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
        JAXBElement<Utlatande> utlatandeElement = readUtlatandeTypeFromFile(RESOURCE_ROOT
                + "transport/scenario3.xml");
        Fk7263Utlatande externalModel = TransportToExternalConverter
                .convert(utlatandeElement.getValue());

        // serialize utlatande to JSON and compare with expected JSON
        JsonNode tree = objectMapper.valueToTree(externalModel);
        JsonNode expectedTree = objectMapper.readTree(new ClassPathResource(
                RESOURCE_ROOT + "transport/scenario3.json").getInputStream());

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
        JAXBElement<Utlatande> utlatandeElement = readUtlatandeTypeFromFile(RESOURCE_ROOT
                + "transport/scenario4.xml");
        Fk7263Utlatande externalModel = TransportToExternalConverter
                .convert(utlatandeElement.getValue());

        // serialize utlatande to JSON and compare with expected JSON
        JsonNode tree = objectMapper.valueToTree(externalModel);
        JsonNode expectedTree = objectMapper.readTree(new ClassPathResource(
                RESOURCE_ROOT + "transport/scenario4.json").getInputStream());

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
        JAXBElement<Utlatande> utlatandeElement = readUtlatandeTypeFromFile(RESOURCE_ROOT
                + "transport/scenario5.xml");
        Fk7263Utlatande externalModel = TransportToExternalConverter
                .convert(utlatandeElement.getValue());

        // serialize utlatande to JSON and compare with expected JSON
        JsonNode tree = objectMapper.valueToTree(externalModel);
        JsonNode expectedTree = objectMapper.readTree(new ClassPathResource(
                RESOURCE_ROOT + "transport/scenario5.json").getInputStream());

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
        JAXBElement<Utlatande> utlatandeElement = readUtlatandeTypeFromFile(RESOURCE_ROOT
                + "transport/scenario6.xml");
        Fk7263Utlatande externalModel = TransportToExternalConverter
                .convert(utlatandeElement.getValue());

        // serialize utlatande to JSON and compare with expected JSON
        JsonNode tree = objectMapper.valueToTree(externalModel);
        JsonNode expectedTree = objectMapper.readTree(new ClassPathResource(
                RESOURCE_ROOT + "transport/scenario6.json").getInputStream());

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
        JAXBElement<Utlatande> utlatandeElement = readUtlatandeTypeFromFile(RESOURCE_ROOT
                + "transport/scenario7.xml");
        Fk7263Utlatande externalModel = TransportToExternalConverter
                .convert(utlatandeElement.getValue());

        // serialize utlatande to JSON and compare with expected JSON
        JsonNode tree = objectMapper.valueToTree(externalModel);
        JsonNode expectedTree = objectMapper.readTree(new ClassPathResource(
                RESOURCE_ROOT + "transport/scenario7.json").getInputStream());

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
        JAXBElement<Utlatande> utlatandeElement = readUtlatandeTypeFromFile(RESOURCE_ROOT
                + "transport/scenario8.xml");
        Fk7263Utlatande externalModel = TransportToExternalConverter
                .convert(utlatandeElement.getValue());

        // serialize utlatande to JSON and compare with expected JSON
        JsonNode tree = objectMapper.valueToTree(externalModel);
        JsonNode expectedTree = objectMapper.readTree(new ClassPathResource(
                RESOURCE_ROOT + "transport/scenario8.json").getInputStream());

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
        JAXBElement<Utlatande> utlatandeElement = readUtlatandeTypeFromFile(RESOURCE_ROOT + "transport/scenario9.xml");
        Fk7263Utlatande externalModel = TransportToExternalConverter.convert(utlatandeElement.getValue());

        // serialize utlatande to JSON and compare with expected JSON
        JsonNode tree = objectMapper.valueToTree(externalModel);
        JsonNode expectedTree = objectMapper.readTree(new ClassPathResource(
                RESOURCE_ROOT + "transport/scenario9.json").getInputStream());

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
        JAXBElement<Utlatande> utlatandeElement = readUtlatandeTypeFromFile(RESOURCE_ROOT
                + "transport/scenario10.xml");
        Fk7263Utlatande externalModel = TransportToExternalConverter
                .convert(utlatandeElement.getValue());

        // serialize utlatande to JSON and compare with expected JSON
        JsonNode tree = objectMapper.valueToTree(externalModel);
        JsonNode expectedTree = objectMapper.readTree(new ClassPathResource(
                RESOURCE_ROOT + "transport/scenario10.json").getInputStream());

        JSONAssert.assertEquals(expectedTree.toString(), tree.toString(), false);
    }

    /**
     * Tests scenario 11 with fields: 2a, 2b, 3, 4a, 4b, 5, 6a, 6b, 7, 8a, 8b,
     * 9, 10, 14-17.
     *
     * @throws JAXBException
     * @throws IOException
     * @throws JSONException
     */
    @Test
    public void testScenario11() throws JAXBException, IOException, JSONException {
        JAXBElement<Utlatande> utlatandeElement = readUtlatandeTypeFromFile(RESOURCE_ROOT
                + "transport/scenario11.xml");
        Fk7263Utlatande externalModel = TransportToExternalConverter
                .convert(utlatandeElement.getValue());

        // serialize utlatande to JSON and compare with expected JSON
        JsonNode tree = objectMapper.valueToTree(externalModel);
        JsonNode expectedTree = objectMapper.readTree(new ClassPathResource(
                RESOURCE_ROOT + "transport/scenario11.json").getInputStream());

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
        JAXBElement<Utlatande> utlatandeElement = readUtlatandeTypeFromFile(RESOURCE_ROOT
                + "transport/scenario12.xml");
        Fk7263Utlatande externalModel = TransportToExternalConverter
                .convert(utlatandeElement.getValue());

        // serialize utlatande to JSON and compare with expected JSON
        JsonNode tree = objectMapper.valueToTree(externalModel);
        JsonNode expectedTree = objectMapper.readTree(new ClassPathResource(
                RESOURCE_ROOT + "transport/scenario12.json").getInputStream());

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
        JAXBElement<Utlatande> utlatandeElement = readUtlatandeTypeFromFile(RESOURCE_ROOT
                + "transport/scenario13.xml");
        Fk7263Utlatande externalModel = TransportToExternalConverter
                .convert(utlatandeElement.getValue());

        // serialize utlatande to JSON and compare with expected JSON
        JsonNode tree = objectMapper.valueToTree(externalModel);
        JsonNode expectedTree = objectMapper.readTree(new ClassPathResource(
                RESOURCE_ROOT + "transport/scenario13.json").getInputStream());

        JSONAssert.assertEquals(expectedTree.toString(), tree.toString(), false);
    }
}
