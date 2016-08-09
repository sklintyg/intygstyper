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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringWriter;

import javax.xml.bind.*;
import javax.xml.namespace.QName;

import org.apache.commons.io.FileUtils;
import org.custommonkey.xmlunit.*;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.fasterxml.jackson.databind.ObjectMapper;

import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.LakarutlatandeType;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificateresponder.v3.RegisterMedicalCertificateType;
import se.inera.intyg.common.support.Constants;
import se.inera.intyg.common.support.model.common.internal.*;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.support.modules.support.api.dto.Personnummer;
import se.inera.intyg.common.util.integration.integration.json.CustomObjectMapper;
import se.inera.intyg.intygstyper.fk7263.model.internal.Utlatande;
import se.inera.intyg.intygstyper.fk7263.utils.*;

/**
 * @author marced, andreaskaltenbach
 */
public class InternalToTransportConverterTest {

    @Test
    public void testConvertUtlatande() throws Exception {
        for (Scenario scenario : ScenarioFinder.getInternalScenarios("valid-*")) {
            Utlatande intUtlatande = scenario.asInternalModel();

            RegisterMedicalCertificateType actual = InternalToTransport.getJaxbObject(intUtlatande);

            RegisterMedicalCertificateType expected = scenario.asTransportModel();

            ModelAssert.assertEquals("Error in scenario " + scenario.getName(), expected, actual);
        }
    }

    @Test
    public void testConversionUtanFalt5() throws JAXBException, IOException, SAXException, ConverterException {

        ObjectMapper objectMapper = new CustomObjectMapper();
        Utlatande internalFormat = objectMapper.readValue(
                new ClassPathResource("InternalToTransportConverterTest/fk7263-utan-falt5.json").getInputStream(), Utlatande.class);

        RegisterMedicalCertificateType registerMedicalCertificate = InternalToTransport.getJaxbObject(internalFormat);

        JAXBContext jaxbContext = JAXBContext.newInstance(RegisterMedicalCertificateType.class, LakarutlatandeType.class);
        StringWriter stringWriter = new StringWriter();
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.marshal(wrapJaxb(registerMedicalCertificate), stringWriter);

        // read expected XML and compare with resulting RegisterMedicalCertificateType
        String expectation = FileUtils.readFileToString(new ClassPathResource("InternalToTransportConverterTest/fk7263-utan-falt5.xml")
                .getFile());

        XMLUnit.setIgnoreWhitespace(true);
        XMLUnit.setIgnoreAttributeOrder(true);
        Diff diff = XMLUnit.compareXML(expectation, stringWriter.toString());
        diff.overrideDifferenceListener(new NamespacePrefixNameIgnoringListener());
        diff.overrideElementQualifier(new ElementNameAndTextQualifier());
        assertTrue(diff.toString(), diff.similar());
    }

   @Test
    public void testConversionMaximal() throws JAXBException, IOException, SAXException, ConverterException {

        ObjectMapper objectMapper = new CustomObjectMapper();
        Utlatande internalFormat = objectMapper.readValue(
                new ClassPathResource("InternalToTransportConverterTest/maximalt-fk7263-internal.json").getInputStream(), Utlatande.class);

        RegisterMedicalCertificateType registerMedicalCertificate = InternalToTransport.getJaxbObject(internalFormat);

        JAXBContext jaxbContext = JAXBContext.newInstance(RegisterMedicalCertificateType.class, LakarutlatandeType.class);
        StringWriter stringWriter = new StringWriter();
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.marshal(wrapJaxb(registerMedicalCertificate), stringWriter);

        // read expected XML and compare with resulting RegisterMedicalCertificateType
        String expectation = FileUtils.readFileToString(new ClassPathResource("InternalToTransportConverterTest/maximalt-fk7263-transport.xml")
                .getFile());

        XMLUnit.setIgnoreWhitespace(true);
        XMLUnit.setIgnoreAttributeOrder(true);
        Diff diff = XMLUnit.compareXML(expectation, stringWriter.toString());
        diff.overrideDifferenceListener(new NamespacePrefixNameIgnoringListener());
        diff.overrideElementQualifier(new ElementNameAndTextQualifier());
        assertTrue(diff.toString(), diff.similar());
    }

    @Test
    public void testConversionWithDiagnosisAsKSH97() throws JAXBException, IOException, SAXException, ConverterException {

        ObjectMapper objectMapper = new CustomObjectMapper();
        Utlatande internalFormat = objectMapper.readValue(
                new ClassPathResource("InternalToTransportConverterTest/maximalt-fk7263-with-ksh97.json").getInputStream(), Utlatande.class);

        RegisterMedicalCertificateType registerMedicalCertificate = InternalToTransport.getJaxbObject(internalFormat);

        JAXBContext jaxbContext = JAXBContext.newInstance(RegisterMedicalCertificateType.class, LakarutlatandeType.class);
        StringWriter stringWriter = new StringWriter();
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.marshal(wrapJaxb(registerMedicalCertificate), stringWriter);

        // read expected XML and compare with resulting RegisterMedicalCertificateType
        String expectation = FileUtils.readFileToString(new ClassPathResource("InternalToTransportConverterTest/maximalt-fk7263-with-ksh97.xml")
                .getFile());

        XMLUnit.setIgnoreWhitespace(true);
        XMLUnit.setIgnoreAttributeOrder(true);
        Diff diff = XMLUnit.compareXML(expectation, stringWriter.toString());
        diff.overrideDifferenceListener(new NamespacePrefixNameIgnoringListener());
        diff.overrideElementQualifier(new ElementNameAndTextQualifier());
        assertTrue(diff.toString(), diff.similar());
    }

    @Test
    public void testConversionMinimal() throws JAXBException, IOException, SAXException, ConverterException {

        ObjectMapper objectMapper = new CustomObjectMapper();
        Utlatande externalFormat = objectMapper.readValue(
                new ClassPathResource("InternalToTransportConverterTest/minimalt-fk7263-internal.json").getInputStream(), Utlatande.class);

        RegisterMedicalCertificateType registerMedicalCertificateType = InternalToTransport.getJaxbObject(externalFormat);

        JAXBContext jaxbContext = JAXBContext.newInstance(RegisterMedicalCertificateType.class, LakarutlatandeType.class);
        StringWriter stringWriter = new StringWriter();
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.marshal(wrapJaxb(registerMedicalCertificateType), stringWriter);

        // read expected XML and compare with resulting RegisterMedicalCertificateType
        String expectation = FileUtils.readFileToString(new ClassPathResource("InternalToTransportConverterTest/minimalt-fk7263-transport.xml")
                .getFile());

        XMLUnit.setIgnoreWhitespace(true);
        Diff diff = new Diff(expectation, stringWriter.toString());
        diff.overrideDifferenceListener(new NamespacePrefixNameIgnoringListener());
        assertTrue(diff.toString(), diff.similar());
    }

    @Test
    public void testConversionKommentar() throws JAXBException, IOException, SAXException, ConverterException {

        ObjectMapper objectMapper = new CustomObjectMapper();
        Utlatande externalFormat = objectMapper.readValue(
                new ClassPathResource("InternalToTransportConverterTest/friviligttext-fk7263-internal.json").getInputStream(), Utlatande.class);
        RegisterMedicalCertificateType registerMedicalCertificateType = InternalToTransport.getJaxbObject(externalFormat);
        String expected = "8b: " + "nedsattMed25Beskrivning. " + "nedsattMed50Beskrivning. " + "nedsattMed75Beskrivning. kommentar";
        String result = registerMedicalCertificateType.getLakarutlatande().getKommentar();
        assertEquals(expected, result);
    }

    @Test
    public void testConversionOrimligtDatum() throws JAXBException, IOException, SAXException, ConverterException {


        ObjectMapper objectMapper = new CustomObjectMapper();
        Utlatande externalFormat = objectMapper.readValue(
                new ClassPathResource("InternalToTransportConverterTest/minimalt-fk7263-internal-orimligt-datum.json").getInputStream(), Utlatande.class);

        RegisterMedicalCertificateType registerMedicalCertificateType = InternalToTransport.getJaxbObject(externalFormat);

        JAXBContext jaxbContext = JAXBContext.newInstance(RegisterMedicalCertificateType.class, LakarutlatandeType.class);
        StringWriter stringWriter = new StringWriter();
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.marshal(wrapJaxb(registerMedicalCertificateType), stringWriter);

        // read expected XML and compare with resulting RegisterMedicalCertificateType
        String expectation = FileUtils.readFileToString(new ClassPathResource("InternalToTransportConverterTest/minimalt-fk7263-transport-orimligt-datum.xml")
                .getFile());

        XMLUnit.setIgnoreWhitespace(true);
        Diff diff = new Diff(expectation, stringWriter.toString());
        diff.overrideDifferenceListener(new NamespacePrefixNameIgnoringListener());
        assertTrue(diff.toString(), diff.similar());
    }

    @Test
    public void testConversionMinimalSmiL() throws JAXBException, IOException, SAXException, ConverterException {

        ObjectMapper objectMapper = new CustomObjectMapper();
        Utlatande externalFormat = objectMapper.readValue(
                new ClassPathResource("InternalToTransportConverterTest/minimalt-SmiL-fk7263-internal.json").getInputStream(), Utlatande.class);

        RegisterMedicalCertificateType registerMedicalCertificateType = InternalToTransport.getJaxbObject(externalFormat);

        JAXBContext jaxbContext = JAXBContext.newInstance(RegisterMedicalCertificateType.class, LakarutlatandeType.class);
        StringWriter stringWriter = new StringWriter();
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.marshal(wrapJaxb(registerMedicalCertificateType), stringWriter);

        // read expected XML and compare with resulting RegisterMedicalCertificateType
        String expectation = FileUtils.readFileToString(new ClassPathResource("InternalToTransportConverterTest/minimalt-SmiL-fk7263-transport.xml")
                .getFile());

        XMLUnit.setIgnoreWhitespace(true);
        Diff diff = new Diff(expectation, stringWriter.toString());
        diff.overrideDifferenceListener(new NamespacePrefixNameIgnoringListener());
        assertTrue(diff.toString(), diff.similar());
    }

    @Test
    public void testPersonnummerRoot() throws Exception {
        final String personnummer = "19121212-1212";
        Utlatande utlatande = new Utlatande();
        GrundData grundData = new GrundData();
        Patient patient = new Patient();
        patient.setPersonId(new Personnummer(personnummer));
        grundData.setPatient(patient);
        HoSPersonal skapadAv = new HoSPersonal();
        Vardenhet vardenhet = new Vardenhet();
        vardenhet.setVardgivare(new Vardgivare());
        skapadAv.setVardenhet(vardenhet);
        grundData.setSkapadAv(skapadAv);
        utlatande.setGrundData(grundData);
        RegisterMedicalCertificateType res = InternalToTransport.getJaxbObject(utlatande);
        assertEquals(Constants.PERSON_ID_OID, res.getLakarutlatande().getPatient().getPersonId().getRoot());
        assertEquals(personnummer, res.getLakarutlatande().getPatient().getPersonId().getExtension());
    }

    @Test
    public void testSamordningRoot() throws Exception {
        final String personnummer = "19800191-0002";
        Utlatande utlatande = new Utlatande();
        GrundData grundData = new GrundData();
        Patient patient = new Patient();
        patient.setPersonId(new Personnummer(personnummer));
        grundData.setPatient(patient);
        HoSPersonal skapadAv = new HoSPersonal();
        Vardenhet vardenhet = new Vardenhet();
        vardenhet.setVardgivare(new Vardgivare());
        skapadAv.setVardenhet(vardenhet);
        grundData.setSkapadAv(skapadAv);
        utlatande.setGrundData(grundData);
        RegisterMedicalCertificateType res = InternalToTransport.getJaxbObject(utlatande);
        assertEquals(Constants.SAMORDNING_ID_OID, res.getLakarutlatande().getPatient().getPersonId().getRoot());
        assertEquals(personnummer, res.getLakarutlatande().getPatient().getPersonId().getExtension());
    }

    private JAXBElement<?> wrapJaxb(RegisterMedicalCertificateType ws) {
        JAXBElement<?> jaxbElement = new JAXBElement<RegisterMedicalCertificateType>(
                new QName("urn:riv:insuranceprocess:healthreporting:RegisterMedicalCertificateResponder:3", "RegisterMedicalCertificate"),
                RegisterMedicalCertificateType.class, ws);
        return jaxbElement;
    }

    private class NamespacePrefixNameIgnoringListener implements DifferenceListener {
        public int differenceFound(Difference difference) {
            if (DifferenceConstants.NAMESPACE_PREFIX_ID == difference.getId()) {
                // differences in namespace prefix IDs are ok (eg. 'ns1' vs 'ns2'), as long as the namespace URI is the
                // same
                return RETURN_IGNORE_DIFFERENCE_NODES_IDENTICAL;
            } else {
                return RETURN_ACCEPT_DIFFERENCE;
            }
        }

        public void skippedComparison(Node control, Node test) {
        }
    }
}
