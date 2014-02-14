package se.inera.certificate.modules.ts_bas.pdf;

import static org.junit.Assert.assertNotNull;

import java.util.Iterator;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

import junit.framework.Assert;

import org.junit.Test;
import org.w3c.dom.Node;

import se.inera.certificate.modules.ts_bas.pdf.xpath.TransportToPDFMapping;
import se.inera.certificate.modules.ts_bas.utils.Scenario;
import se.inera.certificate.modules.ts_bas.utils.ScenarioFinder;
import se.inera.certificate.ts_bas.model.v1.Utlatande;

import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;

public class PdfGeneratorWithXPathCheckTest {

    private final PdfGenerator pdfGen;

    public PdfGeneratorWithXPathCheckTest() {
        pdfGen = new PdfGenerator(false);
    }

    @Test
    public void testGeneratePdfAndValidateFieldsWithXPath() throws Exception {
        for (Scenario scenario : ScenarioFinder.getInternalScenarios("valid-*")) {
            // Generate a PDF from the internal model
            byte[] pdf = pdfGen.generatePDF(scenario.asInternalModel());
            assertNotNull("Error in scenario " + scenario.getName(), pdf);

            // Open a reader to the newly created PDF
            PdfReader pdfReader = new PdfReader(pdf);
            AcroFields fields = pdfReader.getAcroFields();

            // Create an XPath engine and load the transport model for the engine to operate on
            XPath xPath = XPathFactory.newInstance().newXPath();
            xPath.setNamespaceContext(new DOMNamespaceResolver());
            Node document = generateDocumentFor(scenario.asTransportModel());

            // Assert that all defined mappings match
            for (TransportToPDFMapping mapping : TransportToPDFMapping.values()) {
                Object pdfValue = getField(fields, mapping.getField());
                Object xPathValue = mapping.getxPath().evaluate(xPath, document);
                String message = String.format("Scenario: %s, Name: %s, Field: %s - ", scenario.getName(),
                        mapping.name(), mapping.getField());
                Assert.assertEquals(message, xPathValue, pdfValue);
            }

            pdfReader.close();
        }
    }

    private Object getField(AcroFields fields, String fieldName) {
        switch (fields.getFieldType(fieldName)) {
        case AcroFields.FIELD_TYPE_TEXT:
            return fields.getField(fieldName);

        case AcroFields.FIELD_TYPE_CHECKBOX:
            return fields.getField(fieldName).equals("On");
        }

        return null;
    }

    private Node generateDocumentFor(Utlatande transportModel) throws ParserConfigurationException, JAXBException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder parser = factory.newDocumentBuilder();
        Node node = parser.newDocument();

        JAXBContext context = JAXBContext.newInstance(Utlatande.class);
        context.createMarshaller().marshal(transportModel, node);

        return node;
    }

    private static class DOMNamespaceResolver implements NamespaceContext {

        public String getNamespaceURI(String prefix) {
            if (prefix.equals("p")) {
                return "urn:riv:clinicalprocess:healthcond:certificate:1";
            }
            return XMLConstants.NULL_NS_URI;
        }

        public String getPrefix(String namespaceURI) {
            throw new UnsupportedOperationException();
        }

        public Iterator<String> getPrefixes(String namespaceURI) {
            throw new UnsupportedOperationException();
        }
    }
}
