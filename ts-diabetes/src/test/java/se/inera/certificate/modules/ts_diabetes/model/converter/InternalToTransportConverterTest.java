package se.inera.certificate.modules.ts_diabetes.model.converter;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.Charset;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;

import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.Difference;
import org.custommonkey.xmlunit.DifferenceConstants;
import org.custommonkey.xmlunit.DifferenceListener;
import org.custommonkey.xmlunit.ElementNameAndTextQualifier;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.StreamUtils;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import se.inera.certificate.integration.json.CustomObjectMapper;
import se.inera.certificate.modules.ts_diabetes.model.internal.Utlatande;
import se.intygstjanster.ts.services.v1.TSDiabetesIntyg;

import com.fasterxml.jackson.databind.ObjectMapper;

public class InternalToTransportConverterTest {

    private InputStream internalStream;
    private Utlatande utlatande;
    private InputStream transportStream;
    private String expected;

    @Before
    public void setUp() throws Exception {
        internalStream = getClass().getResourceAsStream("/scenarios/internal/valid-maximal.json");
        transportStream = getClass().getResourceAsStream("/scenarios/transport/valid-maximal.xml");

        ObjectMapper mapper = new CustomObjectMapper();
        utlatande = mapper.readValue(internalStream, Utlatande.class);
        
        expected = StreamUtils.copyToString(transportStream, Charset.defaultCharset());
    }

    @Test
    public void testConvert() throws JAXBException, SAXException, IOException {
        TSDiabetesIntyg converted = InternalToTransportConverter.convert(utlatande);

        JAXBContext ctx = JAXBContext.newInstance(TSDiabetesIntyg.class);
        Marshaller marshaller = ctx.createMarshaller();
        
        StringWriter actual = new StringWriter();
        marshaller.marshal(wrapJaxb(converted), actual);
        
        XMLUnit.setIgnoreWhitespace(true);
        XMLUnit.setIgnoreAttributeOrder(true);
        Diff diff = XMLUnit.compareXML(expected, actual.toString());
        diff.overrideDifferenceListener(new NamespacePrefixNameIgnoringListener());
        diff.overrideElementQualifier(new ElementNameAndTextQualifier());
        Assert.assertTrue(diff.toString(), diff.similar());
    }

    private JAXBElement<TSDiabetesIntyg> wrapJaxb(TSDiabetesIntyg ws) {
        QName qn = new QName("urn:local:se:intygstjanster:services:1", "TSDiabetesIntyg");
        return new JAXBElement<TSDiabetesIntyg>(qn, TSDiabetesIntyg.class, ws);
    }

    @After
    public void tearDown() throws IOException {
        internalStream.close();
        transportStream.close();
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
