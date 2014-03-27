package se.inera.certificate.modules.fk7263.model.converter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;

import static org.custommonkey.xmlunit.DifferenceConstants.NAMESPACE_PREFIX_ID;
import static org.junit.Assert.assertTrue;

import org.apache.commons.io.FileUtils;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.Difference;
import org.custommonkey.xmlunit.DifferenceListener;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import se.inera.certificate.fk7263.model.v1.Utlatande;
import se.inera.certificate.integration.json.CustomObjectMapper;
import se.inera.certificate.modules.fk7263.model.external.Fk7263Utlatande;

/**
 * @author andreaskaltenbach
 */
public class ExternalToTransportConverterTest {

    private ExternalToTransportConverter converter = new ExternalToTransportConverter();

    @Test
    public void testConversion() throws JAXBException, IOException, SAXException {

        Fk7263Utlatande fk7263Utlatande = new CustomObjectMapper().readValue(new ClassPathResource(
                "ExternalToTransportConverterTest/utlatande.json").getFile(), Fk7263Utlatande.class);

        Utlatande utlatande = converter.convert(fk7263Utlatande);

        // read utlatandeType from file
        File xmlFile = new ClassPathResource("ExternalToTransportConverterTest/utlatande.xml").getFile();
        JAXBContext jaxbContext = JAXBContext.newInstance(Utlatande.class);

        Marshaller marshaller = jaxbContext.createMarshaller();
        StringWriter stringWriter = new StringWriter();
        marshaller.marshal(utlatande, stringWriter);

        XMLUnit.setIgnoreWhitespace(true);
        XMLUnit.setNormalizeWhitespace(true);
        Diff diff = new Diff(FileUtils.readFileToString(xmlFile), stringWriter.toString());
        diff.overrideDifferenceListener(new NamespacePrefixNameIgnoringListener());

        assertTrue(diff.toString(), diff.identical());
    }

    private class NamespacePrefixNameIgnoringListener implements DifferenceListener {
        public int differenceFound(Difference difference) {
            if (NAMESPACE_PREFIX_ID == difference.getId()) {
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
