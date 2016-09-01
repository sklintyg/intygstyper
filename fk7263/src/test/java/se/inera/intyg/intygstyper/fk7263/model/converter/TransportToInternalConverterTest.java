/*
 * Copyright (C) 2016 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package se.inera.intyg.intygstyper.fk7263.model.converter;

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

import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.LakarutlatandeType;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.util.integration.integration.json.CustomObjectMapper;
import se.inera.intyg.intygstyper.fk7263.model.internal.Utlatande;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author marced
 */
public class TransportToInternalConverterTest {

    private JAXBContext jaxbContext;
    private Unmarshaller unmarshaller;
    private ObjectMapper objectMapper;
    private static final String RESOURCE_ROOT = "TransportToInternalConverterTest/";

    private JAXBElement<LakarutlatandeType> readUtlatandeTypeFromFile(String file)
            throws JAXBException, IOException {
        JAXBElement<LakarutlatandeType> utlatandeElement = unmarshaller.unmarshal(
                new StreamSource(new ClassPathResource(file).getInputStream()),
                LakarutlatandeType.class);
        return utlatandeElement;
    }

    @Before
    public void setUp() throws JAXBException, IOException {
        jaxbContext = JAXBContext.newInstance(LakarutlatandeType.class);
        unmarshaller = jaxbContext.createUnmarshaller();
        objectMapper = new CustomObjectMapper();
    }

    @Test
    public void testConversionWithWhitespaces() throws Exception {
        JAXBElement<LakarutlatandeType> utlatandeElement = readUtlatandeTypeFromFile(RESOURCE_ROOT
                + "intyg-med-blanksteg.xml");

        Utlatande internalModel = TransportToInternal
                .convert(utlatandeElement.getValue());

        // serialize utlatande to JSON and compare with expected JSON
        JsonNode tree = objectMapper.valueToTree(internalModel);
        JsonNode expectedTree = objectMapper.readTree(new ClassPathResource(
                RESOURCE_ROOT + "intyg-med-blanksteg.json")
                .getInputStream());
        JSONAssert.assertEquals(expectedTree.toString(), tree.toString(), false);
    }

    @Test
    public void testConversionWithMaximalCertificate() throws JAXBException,
            IOException, JSONException, ConverterException {

        // read utlatandeType from file

        JAXBElement<LakarutlatandeType> utlatandeElement = readUtlatandeTypeFromFile(RESOURCE_ROOT
                + "legacy-maximalt-fk7263-transport.xml");

        Utlatande internalModel = TransportToInternal
                .convert(utlatandeElement.getValue());

        // serialize utlatande to JSON and compare with expected JSON
        JsonNode tree = objectMapper.valueToTree(internalModel);
        JsonNode expectedTree = objectMapper.readTree(new ClassPathResource(
                RESOURCE_ROOT + "legacy-maximalt-fk7263-internal.json")
                .getInputStream());
        JSONAssert.assertEquals(expectedTree.toString(), tree.toString(), false);
    }

    @Test
    public void testConversionWithMinimalCertificate() throws JAXBException,
            IOException, JSONException, ConverterException {

        // read utlatandeType from file
        JAXBElement<LakarutlatandeType> utlatandeElement = readUtlatandeTypeFromFile(RESOURCE_ROOT
                + "legacy-minimalt-fk7263-transport.xml");

        Utlatande internalModel = TransportToInternal
                .convert(utlatandeElement.getValue());

        // serialize utlatande to JSON and compare with expected JSON
        JsonNode tree = objectMapper.valueToTree(internalModel);
        JsonNode expectedTree = objectMapper.readTree(new ClassPathResource(
                RESOURCE_ROOT + "legacy-minimalt-fk7263-internal.json")
                .getInputStream());

        JSONAssert.assertEquals(expectedTree.toString(), tree.toString(), false);
    }

    @Test
    public void testConversionWithNoPrognosAngivelseButMotivering() throws JAXBException,
            IOException, JSONException, ConverterException {

        // read utlatandeType from file
        JAXBElement<LakarutlatandeType> utlatandeElement = readUtlatandeTypeFromFile(RESOURCE_ROOT
                + "legacy-fk7263-without-prognoskod-transport.xml");

        Utlatande internalModel = TransportToInternal
                .convert(utlatandeElement.getValue());

        // serialize utlatande to JSON and compare with expected JSON
        JsonNode tree = objectMapper.valueToTree(internalModel);
        JsonNode expectedTree = objectMapper.readTree(new ClassPathResource(
                RESOURCE_ROOT + "legacy-fk7263-without-prognoskod-internal.json")
                .getInputStream());

        JSONAssert.assertEquals(expectedTree.toString(), tree.toString(), false);
    }

    @Test
    public void testConversionWithKSH97PAsCodeSystem() throws JAXBException,
            IOException, JSONException, ConverterException {

        // read utlatandeType from file
        JAXBElement<LakarutlatandeType> utlatandeElement = readUtlatandeTypeFromFile(RESOURCE_ROOT
                + "legacy-fk7263-with-ksh97.xml");

        Utlatande internalModel = TransportToInternal
                .convert(utlatandeElement.getValue());

        // serialize utlatande to JSON and compare with expected JSON
        JsonNode tree = objectMapper.valueToTree(internalModel);
        JsonNode expectedTree = objectMapper.readTree(new ClassPathResource(
                RESOURCE_ROOT + "legacy-fk7263-with-ksh97.json")
                .getInputStream());

        JSONAssert.assertEquals(expectedTree.toString(), tree.toString(), false);
    }
//
//    // Below are tests for different ways of filling out an FK7263-form,
//    // see readme in
//    // /resource/TransportToExternalFK763LegacyConverterTest/legacy for more
//    // info

    /**
     * Tests scenario 1, with fields: 1, 8b, 14 - 17.
     *
     * @throws JAXBException
     * @throws IOException
     * @throws JSONException
     * @throws ConverterException
     */
    @Test
    public void testScenario1() throws JAXBException, IOException, JSONException, ConverterException {
        JAXBElement<LakarutlatandeType> utlatandeElement = readUtlatandeTypeFromFile(RESOURCE_ROOT
                + "legacy/scenario1.xml");
        Utlatande internalModel = TransportToInternal
                .convert(utlatandeElement.getValue());

        // serialize utlatande to JSON and compare with expected JSON
        JsonNode tree = objectMapper.valueToTree(internalModel);
        JsonNode expectedTree = objectMapper.readTree(new ClassPathResource(
                RESOURCE_ROOT + "legacy/scenario1.json").getInputStream());

        JSONAssert.assertEquals(expectedTree.toString(), tree.toString(), false);
    }

    /**
     * Tests scenario 2 with fields: 1, 8b, 10, 14-17.
     *
     * @throws JAXBException
     * @throws IOException
     * @throws JSONException
     * @throws ConverterException
     */
    @Test
    public void testScenario2() throws JAXBException, IOException, JSONException, ConverterException {
        JAXBElement<LakarutlatandeType> utlatandeElement = readUtlatandeTypeFromFile(RESOURCE_ROOT
                + "legacy/scenario2.xml");
        Utlatande internalModel = TransportToInternal
                .convert(utlatandeElement.getValue());

        // serialize utlatande to JSON and compare with expected JSON
        JsonNode tree = objectMapper.valueToTree(internalModel);
        JsonNode expectedTree = objectMapper.readTree(new ClassPathResource(
                RESOURCE_ROOT + "legacy/scenario2.json").getInputStream());

        JSONAssert.assertEquals(expectedTree.toString(), tree.toString(), false);
    }

    /**
     * Tests scenario 3 with fields: 1, 8b, 10, 13, 14-17.
     *
     * @throws JAXBException
     * @throws IOException
     * @throws JSONException
     * @throws ConverterException
     */
    @Test
    public void testScenario3() throws JAXBException, IOException, JSONException, ConverterException {
        JAXBElement<LakarutlatandeType> utlatandeElement = readUtlatandeTypeFromFile(RESOURCE_ROOT
                + "legacy/scenario3.xml");
        Utlatande internalModel = TransportToInternal
                .convert(utlatandeElement.getValue());

        // serialize utlatande to JSON and compare with expected JSON
        JsonNode tree = objectMapper.valueToTree(internalModel);
        JsonNode expectedTree = objectMapper.readTree(new ClassPathResource(
                RESOURCE_ROOT + "legacy/scenario3.json").getInputStream());

        JSONAssert.assertEquals(expectedTree.toString(), tree.toString(), false);
    }

    /**
     * Tests scenario 4 with fields: 2b, 4a, 4b, 5, 8a, 8b, 14-17.
     *
     * @throws JAXBException
     * @throws IOException
     * @throws JSONException
     * @throws ConverterException
     */
    @Test
    public void testScenario4() throws JAXBException, IOException, JSONException, ConverterException {
        JAXBElement<LakarutlatandeType> utlatandeElement = readUtlatandeTypeFromFile(RESOURCE_ROOT
                + "legacy/scenario4.xml");
        Utlatande internalModel = TransportToInternal
                .convert(utlatandeElement.getValue());

        // serialize utlatande to JSON and compare with expected JSON
        JsonNode tree = objectMapper.valueToTree(internalModel);
        JsonNode expectedTree = objectMapper.readTree(new ClassPathResource(
                RESOURCE_ROOT + "legacy/scenario4.json").getInputStream());

        JSONAssert.assertEquals(expectedTree.toString(), tree.toString(), false);
    }

    /**
     * Tests scenario 5 with fields: 2a, 2b, 4a, 4b, 5, 8a, 8b, 14-17.
     *
     * @throws JAXBException
     * @throws IOException
     * @throws JSONException
     * @throws ConverterException
     */
    @Test
    public void testScenario5() throws JAXBException, IOException, JSONException, ConverterException {
        JAXBElement<LakarutlatandeType> utlatandeElement = readUtlatandeTypeFromFile(RESOURCE_ROOT
                + "legacy/scenario5.xml");
        Utlatande internalModel = TransportToInternal
                .convert(utlatandeElement.getValue());

        // serialize utlatande to JSON and compare with expected JSON
        JsonNode tree = objectMapper.valueToTree(internalModel);
        JsonNode expectedTree = objectMapper.readTree(new ClassPathResource(
                RESOURCE_ROOT + "legacy/scenario5.json").getInputStream());

        JSONAssert.assertEquals(expectedTree.toString(), tree.toString(), false);
    }

    /**
     * Tests scenario 6 with fields: 2a, 2b, 3, 4a, 4b, 5, 8a, 8b, 14-17.
     *
     * @throws JAXBException
     * @throws IOException
     * @throws JSONException
     * @throws ConverterException
     */
    @Test
    public void testScenario6() throws JAXBException, IOException, JSONException, ConverterException {
        JAXBElement<LakarutlatandeType> utlatandeElement = readUtlatandeTypeFromFile(RESOURCE_ROOT
                + "legacy/scenario6.xml");
        Utlatande internalModel = TransportToInternal
                .convert(utlatandeElement.getValue());

        // serialize utlatande to JSON and compare with expected JSON
        JsonNode tree = objectMapper.valueToTree(internalModel);
        JsonNode expectedTree = objectMapper.readTree(new ClassPathResource(
                RESOURCE_ROOT + "legacy/scenario6.json").getInputStream());

        JSONAssert.assertEquals(expectedTree.toString(), tree.toString(), false);
    }

    /**
     * Tests scenario 7 with fields: 2b, 4a, 4b, 5, 6b, 8a, 8b, 11, 13, 14-17.
     *
     * @throws JAXBException
     * @throws IOException
     * @throws JSONException
     * @throws ConverterException
     */
    @Test
    public void testScenario7() throws JAXBException, IOException, JSONException, ConverterException {
        JAXBElement<LakarutlatandeType> utlatandeElement = readUtlatandeTypeFromFile(RESOURCE_ROOT
                + "legacy/scenario7.xml");
        Utlatande internalModel = TransportToInternal
                .convert(utlatandeElement.getValue());

        // serialize utlatande to JSON and compare with expected JSON
        JsonNode tree = objectMapper.valueToTree(internalModel);
        JsonNode expectedTree = objectMapper.readTree(new ClassPathResource(
                RESOURCE_ROOT + "legacy/scenario7.json").getInputStream());

        JSONAssert.assertEquals(expectedTree.toString(), tree.toString(), false);
    }

    /**
     * Tests scenario 8 with fields: 2b, 4a, 4b, 5, 8a, 8b, 11, 14-17.
     *
     * @throws JAXBException
     * @throws IOException
     * @throws JSONException
     * @throws ConverterException
     */
    @Test
    public void testScenario8() throws JAXBException, IOException, JSONException, ConverterException {
        JAXBElement<LakarutlatandeType> utlatandeElement = readUtlatandeTypeFromFile(RESOURCE_ROOT
                + "legacy/scenario8.xml");
        Utlatande internalModel = TransportToInternal
                .convert(utlatandeElement.getValue());

        // serialize utlatande to JSON and compare with expected JSON
        JsonNode tree = objectMapper.valueToTree(internalModel);
        JsonNode expectedTree = objectMapper.readTree(new ClassPathResource(
                RESOURCE_ROOT + "legacy/scenario8.json").getInputStream());

        JSONAssert.assertEquals(expectedTree.toString(), tree.toString(), false);
    }

    /**
     * Tests scenario 9 with fields: 2a, 2b, 4a, 4b, 5, 7, 8a, 8b, 9, 10, 11,
     * 14-17.
     *
     * @throws JAXBException
     * @throws IOException
     * @throws JSONException
     * @throws ConverterException
     */
    @Test
    public void testScenario9() throws JAXBException, IOException, JSONException, ConverterException {
        JAXBElement<LakarutlatandeType> utlatandeElement = readUtlatandeTypeFromFile(RESOURCE_ROOT
                + "legacy/scenario9.xml");
        Utlatande internalModel = TransportToInternal
                .convert(utlatandeElement.getValue());

        // serialize utlatande to JSON and compare with expected JSON
        JsonNode tree = objectMapper.valueToTree(internalModel);
        JsonNode expectedTree = objectMapper.readTree(new ClassPathResource(
                RESOURCE_ROOT + "legacy/scenario9.json").getInputStream());
        JSONAssert.assertEquals(expectedTree.toString(), tree.toString(), false);
    }

    /**
     * Tests scenario 10 with fields: 2a, 2b, 3, 4a, 4b, 5, 6b, 7, 8a, 8b, 9,
     * 10, 12, 14-17.
     *
     * @throws JAXBException
     * @throws IOException
     * @throws JSONException
     * @throws ConverterException
     */
    @Test
    public void testScenario10() throws JAXBException, IOException, JSONException, ConverterException {
        JAXBElement<LakarutlatandeType> utlatandeElement = readUtlatandeTypeFromFile(RESOURCE_ROOT
                + "legacy/scenario10.xml");
        Utlatande internalModel = TransportToInternal
                .convert(utlatandeElement.getValue());

        // serialize utlatande to JSON and compare with expected JSON
        JsonNode tree = objectMapper.valueToTree(internalModel);
        JsonNode expectedTree = objectMapper.readTree(new ClassPathResource(
                RESOURCE_ROOT + "legacy/scenario10.json").getInputStream());

        JSONAssert.assertEquals(expectedTree.toString(), tree.toString(), false);
    }

    /**
     * Tests scenario 11 with fields: 2a, 2b, 3, 4a, 4b, 5, 6a, 6b, 7, 8a, 8b,
     * 9, 10, 14-17.
     *
     * @throws JAXBException
     * @throws IOException
     * @throws JSONException
     * @throws ConverterException
     */
    @Test
    public void testScenario11() throws JAXBException, IOException, JSONException, ConverterException {
        JAXBElement<LakarutlatandeType> utlatandeElement = readUtlatandeTypeFromFile(RESOURCE_ROOT
                + "legacy/scenario11.xml");
        Utlatande internalModel = TransportToInternal
                .convert(utlatandeElement.getValue());

        // serialize utlatande to JSON and compare with expected JSON
        JsonNode tree = objectMapper.valueToTree(internalModel);
        JsonNode expectedTree = objectMapper.readTree(new ClassPathResource(
                RESOURCE_ROOT + "legacy/scenario11.json").getInputStream());

        JSONAssert.assertEquals(expectedTree.toString(), tree.toString(), false);
    }

    /**
     * Tests scenario 12 with fields: 2a, 2b, 3, 4a, 4b, 5, 6a, 6b, 7, 8a, 8b,
     * 9, 10, 12, 13, 14-17.
     *
     * @throws JAXBException
     * @throws IOException
     * @throws JSONException
     * @throws ConverterException
     */
    @Test
    public void testScenario12() throws JAXBException, IOException, JSONException, ConverterException {
        JAXBElement<LakarutlatandeType> utlatandeElement = readUtlatandeTypeFromFile(RESOURCE_ROOT
                + "legacy/scenario12.xml");
        Utlatande internalModel = TransportToInternal
                .convert(utlatandeElement.getValue());

        // serialize utlatande to JSON and compare with expected JSON
        JsonNode tree = objectMapper.valueToTree(internalModel);
        JsonNode expectedTree = objectMapper.readTree(new ClassPathResource(
                RESOURCE_ROOT + "legacy/scenario12.json").getInputStream());

        JSONAssert.assertEquals(expectedTree.toString(), tree.toString(), false);
    }

    /**
     * Tests scenario 13 with fields: 2a, 2b, 3, 4a, 4b, 5, 6a, 6b, 7, 8a, 8b,
     * 9, 10, 11, 12, 13, 14-17.
     *
     * @throws JAXBException
     * @throws IOException
     * @throws JSONException
     * @throws ConverterException
     */
    @Test
    public void testScenario13() throws JAXBException, IOException, JSONException, ConverterException {
        JAXBElement<LakarutlatandeType> utlatandeElement = readUtlatandeTypeFromFile(RESOURCE_ROOT
                + "legacy/scenario13.xml");
        Utlatande internalModel = TransportToInternal
                .convert(utlatandeElement.getValue());

        // serialize utlatande to JSON and compare with expected JSON
        JsonNode tree = objectMapper.valueToTree(internalModel);
        JsonNode expectedTree = objectMapper.readTree(new ClassPathResource(
                RESOURCE_ROOT + "legacy/scenario13.json").getInputStream());

        JSONAssert.assertEquals(expectedTree.toString(), tree.toString(), false);
    }

    /**
     * Tests scenario 14, arbetsloshet.
     *
     * @throws JAXBException
     * @throws IOException
     * @throws JSONException
     * @throws ConverterException
     */
    @Test
    public void testScenario14() throws JAXBException, IOException, JSONException, ConverterException {
        JAXBElement<LakarutlatandeType> utlatandeElement = readUtlatandeTypeFromFile(RESOURCE_ROOT
                + "legacy/scenario14.xml");
        Utlatande internalModel = TransportToInternal
                .convert(utlatandeElement.getValue());

        // serialize utlatande to JSON and compare with expected JSON
        JsonNode tree = objectMapper.valueToTree(internalModel);
        JsonNode expectedTree = objectMapper.readTree(new ClassPathResource(
                RESOURCE_ROOT + "legacy/scenario14.json").getInputStream());

        JSONAssert.assertEquals(expectedTree.toString(), tree.toString(), false);
    }

    /**
     * Tests scenario 15, foraldraledighet.
     *
     * @throws JAXBException
     * @throws IOException
     * @throws JSONException
     * @throws ConverterException
     */
    @Test
    public void testScenario15() throws JAXBException, IOException, JSONException, ConverterException {
        JAXBElement<LakarutlatandeType> utlatandeElement = readUtlatandeTypeFromFile(RESOURCE_ROOT
                + "legacy/scenario15.xml");
        Utlatande internalModel = TransportToInternal
                .convert(utlatandeElement.getValue());

        // serialize utlatande to JSON and compare with expected JSON
        JsonNode tree = objectMapper.valueToTree(internalModel);
        JsonNode expectedTree = objectMapper.readTree(new ClassPathResource(
                RESOURCE_ROOT + "legacy/scenario15.json").getInputStream());

        JSONAssert.assertEquals(expectedTree.toString(), tree.toString(), false);
    }

    /**
     * Tests scenario 16, arbetsloshet but with redundant <arbetsuppgift /> present.
     * This case caused bug INTYG-1413.
     *
     * @throws JAXBException
     * @throws IOException
     * @throws JSONException
     * @throws ConverterException
     */
    @Test
    public void testScenario16() throws JAXBException, IOException, JSONException, ConverterException {
        JAXBElement<LakarutlatandeType> utlatandeElement = readUtlatandeTypeFromFile(RESOURCE_ROOT
                + "legacy/scenario16.xml");
        Utlatande internalModel = TransportToInternal
                .convert(utlatandeElement.getValue());

        // serialize utlatande to JSON and compare with expected JSON
        JsonNode tree = objectMapper.valueToTree(internalModel);
        JsonNode expectedTree = objectMapper.readTree(new ClassPathResource(
                RESOURCE_ROOT + "legacy/scenario16.json").getInputStream());

        JSONAssert.assertEquals(expectedTree.toString(), tree.toString(), false);
    }
}
