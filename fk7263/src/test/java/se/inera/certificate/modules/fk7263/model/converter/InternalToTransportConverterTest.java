package se.inera.certificate.modules.fk7263.model.converter;

import java.io.IOException;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;

import org.apache.commons.io.FileUtils;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.Difference;
import org.custommonkey.xmlunit.DifferenceConstants;
import org.custommonkey.xmlunit.DifferenceListener;
import org.custommonkey.xmlunit.ElementNameAndTextQualifier;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import se.inera.certificate.integration.json.CustomObjectMapper;
import se.inera.certificate.model.converter.util.ConverterException;
import se.inera.certificate.modules.fk7263.model.internal.Utlatande;
import se.inera.certificate.modules.fk7263.utils.ModelAssert;
import se.inera.certificate.modules.fk7263.utils.Scenario;
import se.inera.certificate.modules.fk7263.utils.ScenarioFinder;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.LakarutlatandeType;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificateresponder.v3.RegisterMedicalCertificateType;

import com.fasterxml.jackson.databind.ObjectMapper;

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
        Assert.assertTrue(diff.toString(), diff.similar());
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
        Assert.assertTrue(diff.toString(), diff.similar());
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
        Assert.assertTrue(diff.toString(), diff.similar());
    }

    @Test
    public void testConversionKommentar() throws JAXBException, IOException, SAXException, ConverterException {

        ObjectMapper objectMapper = new CustomObjectMapper();
        Utlatande externalFormat = objectMapper.readValue(
                new ClassPathResource("InternalToTransportConverterTest/friviligttext-fk7263-internal.json").getInputStream(), Utlatande.class);
        RegisterMedicalCertificateType registerMedicalCertificateType = InternalToTransport.getJaxbObject(externalFormat);
        String expected = "8b: " + "nedsattMed25Beskrivning. " + "nedsattMed50Beskrivning. " + "nedsattMed75Beskrivning. kommentar";
        String result = registerMedicalCertificateType.getLakarutlatande().getKommentar();
        Assert.assertEquals(expected, result);
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
        Assert.assertTrue(diff.toString(), diff.similar());
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
