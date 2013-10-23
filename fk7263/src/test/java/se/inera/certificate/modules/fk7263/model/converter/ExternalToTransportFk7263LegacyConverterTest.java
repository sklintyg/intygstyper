package se.inera.certificate.modules.fk7263.model.converter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;
import java.io.IOException;
import java.io.StringWriter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.Difference;
import org.custommonkey.xmlunit.DifferenceConstants;
import org.custommonkey.xmlunit.DifferenceListener;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import se.inera.certificate.fk7263.insuranceprocess.healthreporting.mu7263.v3.Lakarutlatande;
import se.inera.certificate.fk7263.insuranceprocess.healthreporting.registermedicalcertificate.v3.RegisterMedicalCertificate;
import se.inera.certificate.integration.json.CustomObjectMapper;
import se.inera.certificate.modules.fk7263.model.external.Fk7263Utlatande;

/**
 * @author marced, andreaskaltenbach
 */
public class ExternalToTransportFk7263LegacyConverterTest {

    @Test
    public void testConversion() throws JAXBException, IOException, SAXException {

        ObjectMapper objectMapper = new CustomObjectMapper();
        Fk7263Utlatande externalFormat = objectMapper.readValue(new ClassPathResource("ExternalToTransportFk7263LegacyConverterTest/maximalt-fk7263.json").getInputStream(), Fk7263Utlatande.class);


        RegisterMedicalCertificate registerMedicalCertificate = ExternalToTransportFk7263LegacyConverter.getJaxbObject(externalFormat);

        JAXBContext jaxbContext = JAXBContext.newInstance(RegisterMedicalCertificate.class, Lakarutlatande.class);
        StringWriter stringWriter = new StringWriter();
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.marshal(wrapJaxb(registerMedicalCertificate), stringWriter);

        // read expected XML and compare with resulting RegisterMedicalCertificate
        String expectation = FileUtils.readFileToString(new ClassPathResource("ExternalToTransportFk7263LegacyConverterTest/register-medical-certificate-maximalt-fk7263.xml").getFile());

        XMLUnit.setIgnoreWhitespace(true);
        Diff diff = new Diff(expectation, stringWriter.toString());
        diff.overrideDifferenceListener(new NamespacePrefixNameIgnoringListener());

        Assert.assertTrue(diff.toString(), diff.identical());
    }

    @Test
    public void testConversionMinimalSmiL() throws JAXBException, IOException, SAXException {

        ObjectMapper objectMapper = new CustomObjectMapper();
        Fk7263Utlatande externalFormat = objectMapper.readValue(new ClassPathResource("ExternalToTransportFk7263LegacyConverterTest/minimalt-SmiL-fk7263.json").getInputStream(), Fk7263Utlatande.class);


        RegisterMedicalCertificate registerMedicalCertificateType = ExternalToTransportFk7263LegacyConverter.getJaxbObject(externalFormat);

        JAXBContext jaxbContext = JAXBContext.newInstance(RegisterMedicalCertificate.class, Lakarutlatande.class);
        StringWriter stringWriter = new StringWriter();
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.marshal(wrapJaxb(registerMedicalCertificateType), stringWriter);

        // read expected XML and compare with resulting RegisterMedicalCertificate
        String expectation = FileUtils.readFileToString(new ClassPathResource("ExternalToTransportFk7263LegacyConverterTest/register-medical-certificate-minimalt-SmiL-fk7263.xml").getFile());

        XMLUnit.setIgnoreWhitespace(true);
        Diff diff = new Diff(expectation, stringWriter.toString());
        diff.overrideDifferenceListener(new NamespacePrefixNameIgnoringListener());

        Assert.assertTrue(diff.toString(), diff.identical());
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
