package se.inera.certificate.modules.ts_bas.pdf;

import static org.junit.Assert.assertNotNull;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

import junit.framework.Assert;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.util.xml.SimpleNamespaceContext;
import org.w3c.dom.Node;

import se.inera.certificate.modules.support.ApplicationOrigin;
import se.inera.certificate.modules.ts_bas.pdf.xpath.TransportToPDFMapping;
import se.inera.certificate.modules.ts_bas.pdf.xpath.XPathEvaluator;
import se.inera.certificate.modules.ts_bas.utils.Scenario;
import se.inera.certificate.modules.ts_bas.utils.ScenarioFinder;
import se.inera.certificate.modules.ts_bas.utils.ScenarioNotFoundException;
import se.inera.intygstjanster.ts.services.v1.TSBasIntyg;

import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;

public class PdfGeneratorWithXPathCheckTest {

    private final PdfGenerator pdfGen;

    public PdfGeneratorWithXPathCheckTest() {
        pdfGen = new PdfGenerator(false);
    }

    /**
     * Transportstryrelsen needs a set of xPath expressions that can extract the data saved in the PDF from the
     * transport model. This test validates both that:
     * <ul>
     * <li>The PDF was correctly generated.
     * <li>The xPath expressions are correct.
     * </ul>
     *
     * @throws Exception
     *             if an error uccurred.
     */
    // TODO activate this when XSLtransformation is finished!
    @Test
    @Ignore
    public void testGeneratePdfAndValidateFieldsWithXPath() throws Exception {
        for (Scenario scenario : ScenarioFinder.getInternalScenarios("valid-*")) {
            // Generate a PDF from the internal model
            byte[] pdf = pdfGen.generatePDF(scenario.asInternalModel(), ApplicationOrigin.MINA_INTYG);
            assertNotNull("Error in scenario " + scenario.getName(), pdf);

            // Open a reader to the newly created PDF
            PdfReader pdfReader = new PdfReader(pdf);
            AcroFields fields = pdfReader.getAcroFields();

            // Create an xPath evaluator that operates on the transport model.
            XPathEvaluator xPath = createXPathEvaluator(scenario.asTransportModel().getIntyg());

            // Assert that all defined mappings match
            for (TransportToPDFMapping mapping : TransportToPDFMapping.values()) {
                // Only check mappings that maps to a field
                if (mapping.getField() != null) {
                    Object pdfValue = getField(fields, mapping.getField());
                    Object xPathValue = xPath.evaluate(mapping.getxPath());
                    String message = String.format("Scenario: %s, Name: %s, Field: %s - ", scenario.getName(),
                            mapping.name(), mapping.getField());
                    Assert.assertEquals(message, xPathValue, pdfValue);
                }
            }

            pdfReader.close();
        }
    }

    private Object getField(AcroFields fields, String fieldName) {
        switch (fields.getFieldType(fieldName)) {
        case AcroFields.FIELD_TYPE_CHECKBOX:
            return fields.getField(fieldName).equals("On");

        case AcroFields.FIELD_TYPE_TEXT:
            return fields.getField(fieldName);

        case AcroFields.FIELD_TYPE_NONE:
            throw new IllegalStateException("Field " + fieldName + " was not found.");
        default:
            break;
        }

        throw new IllegalStateException("Unexpected field type: " + fields.getFieldType(fieldName));
    }

    private XPathEvaluator createXPathEvaluator(TSBasIntyg transportModel) throws ParserConfigurationException,
            JAXBException, ScenarioNotFoundException {
        XPath xPath = XPathFactory.newInstance().newXPath();
        SimpleNamespaceContext namespaces = new SimpleNamespaceContext();
        namespaces.bindNamespaceUri("p", "urn:local:se:intygstjanster:services:1");
        xPath.setNamespaceContext(namespaces);
        Node document = generateDocumentFor(transportModel);

        return new XPathEvaluator(xPath, document);
    }

    private Node generateDocumentFor(TSBasIntyg transportModel) throws ParserConfigurationException, JAXBException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder parser = factory.newDocumentBuilder();
        Node node = parser.newDocument();

        JAXBElement<TSBasIntyg> jaxbElement = new JAXBElement<TSBasIntyg>(new QName("basIntyg"), TSBasIntyg.class, transportModel);

        JAXBContext context = JAXBContext.newInstance(TSBasIntyg.class);
        context.createMarshaller().marshal(jaxbElement, node);

        return node;
    }
}
