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

import se.inera.certificate.fk7263.insuranceprocess.healthreporting.mu7263.v3.Lakarutlatande;
import se.inera.certificate.fk7263.insuranceprocess.healthreporting.registermedicalcertificate.v3.RegisterMedicalCertificate;
import se.inera.certificate.integration.json.CustomObjectMapper;
import se.inera.certificate.model.converter.util.ConverterException;
import se.inera.certificate.modules.fk7263.model.internal.Utlatande;
import se.inera.certificate.modules.fk7263.utils.ModelAssert;
import se.inera.certificate.modules.fk7263.utils.Scenario;
import se.inera.certificate.modules.fk7263.utils.ScenarioFinder;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author marced, andreaskaltenbach
 */
public class InternalToTransportConverterTest {

    

    @Test
    public void testConvertUtlatande() throws Exception {
        for (Scenario scenario : ScenarioFinder.getInternalScenarios("*")) {
            Utlatande intUtlatande = scenario.asInternalModel();

            RegisterMedicalCertificate actual = InternalToTransport.getJaxbObject(intUtlatande);

            RegisterMedicalCertificate expected = scenario.asTransportModel();

            ModelAssert.assertEquals("Error in scenario " + scenario.getName(), expected, actual);
        }
    }

    @Test
    public void testConversionMaximal() throws JAXBException, IOException, SAXException, ConverterException {

        ObjectMapper objectMapper = new CustomObjectMapper();
        Utlatande internalFormat = objectMapper.readValue(new ClassPathResource("InternalToTransportConverterTest/maximalt-fk7263-internal.json").getInputStream(), Utlatande.class);


        RegisterMedicalCertificate registerMedicalCertificate = InternalToTransport.getJaxbObject(internalFormat);

        JAXBContext jaxbContext = JAXBContext.newInstance(RegisterMedicalCertificate.class, Lakarutlatande.class);
        StringWriter stringWriter = new StringWriter();
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.marshal(wrapJaxb(registerMedicalCertificate), stringWriter);

        // read expected XML and compare with resulting RegisterMedicalCertificate
        String expectation = FileUtils.readFileToString(new ClassPathResource("InternalToTransportConverterTest/maximalt-fk7263-transport.xml").getFile());

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
        Utlatande externalFormat = objectMapper.readValue(new ClassPathResource("InternalToTransportConverterTest/minimalt-fk7263-internal.json").getInputStream(), Utlatande.class);
        
        
        RegisterMedicalCertificate registerMedicalCertificateType = InternalToTransport.getJaxbObject(externalFormat);
        
        JAXBContext jaxbContext = JAXBContext.newInstance(RegisterMedicalCertificate.class, Lakarutlatande.class);
        StringWriter stringWriter = new StringWriter();
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.marshal(wrapJaxb(registerMedicalCertificateType), stringWriter);
        
        // read expected XML and compare with resulting RegisterMedicalCertificate
        String expectation = FileUtils.readFileToString(new ClassPathResource("InternalToTransportConverterTest/minimalt-fk7263-transport.xml").getFile());
        
        XMLUnit.setIgnoreWhitespace(true);
        Diff diff = new Diff(expectation, stringWriter.toString());
        diff.overrideDifferenceListener(new NamespacePrefixNameIgnoringListener());
        Assert.assertTrue(diff.toString(), diff.similar());
    }
    @Test
    public void testConversionMinimalSmiL() throws JAXBException, IOException, SAXException, ConverterException {

        ObjectMapper objectMapper = new CustomObjectMapper();
        Utlatande externalFormat = objectMapper.readValue(new ClassPathResource("InternalToTransportConverterTest/minimalt-SmiL-fk7263-internal.json").getInputStream(), Utlatande.class);


        RegisterMedicalCertificate registerMedicalCertificateType = InternalToTransport.getJaxbObject(externalFormat);

        JAXBContext jaxbContext = JAXBContext.newInstance(RegisterMedicalCertificate.class, Lakarutlatande.class);
        StringWriter stringWriter = new StringWriter();
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.marshal(wrapJaxb(registerMedicalCertificateType), stringWriter);

        // read expected XML and compare with resulting RegisterMedicalCertificate
        String expectation = FileUtils.readFileToString(new ClassPathResource("InternalToTransportConverterTest/minimalt-SmiL-fk7263-transport.xml").getFile());

        XMLUnit.setIgnoreWhitespace(true);
        Diff diff = new Diff(expectation, stringWriter.toString());
        diff.overrideDifferenceListener(new NamespacePrefixNameIgnoringListener());
        Assert.assertTrue(diff.toString(), diff.similar());
    }

    private JAXBElement<?> wrapJaxb(RegisterMedicalCertificate ws) {
            JAXBElement<?> jaxbElement = new JAXBElement<RegisterMedicalCertificate>(
                    new QName("urn:riv:insuranceprocess:healthreporting:RegisterMedicalCertificateResponder:3", "RegisterMedicalCertificate"),
                    RegisterMedicalCertificate.class, ws);
            return jaxbElement;
        }

    private class NamespacePrefixNameIgnoringListener implements DifferenceListener {
        public int differenceFound(Difference difference) {
            if (DifferenceConstants.NAMESPACE_PREFIX_ID == difference.getId()) {
                // differences in namespace prefix IDs are ok (eg. 'ns1' vs 'ns2'), as long as the namespace URI is the same
                return RETURN_IGNORE_DIFFERENCE_NODES_IDENTICAL;
            } else {
                return RETURN_ACCEPT_DIFFERENCE;
            }
        }

        public void skippedComparison(Node control, Node test) {
        }
    }
}
