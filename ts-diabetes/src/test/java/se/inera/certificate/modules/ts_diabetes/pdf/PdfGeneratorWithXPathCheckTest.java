package se.inera.certificate.modules.ts_diabetes.pdf;

import static org.junit.Assert.assertNotNull;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.util.xml.SimpleNamespaceContext;
import org.w3c.dom.Node;

import se.inera.certificate.modules.ts_diabetes.pdf.xpath.TransportToPDFMapping;
import se.inera.certificate.modules.ts_diabetes.pdf.xpath.XPathEvaluator;
import se.inera.certificate.modules.ts_diabetes.utils.Scenario;
import se.inera.certificate.modules.ts_diabetes.utils.ScenarioFinder;
import se.inera.certificate.modules.ts_diabetes.utils.ScenarioNotFoundException;
import se.inera.certificate.ts_diabetes.model.v1.Utlatande;

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
    @Test
    public void testGeneratePdfAndValidateFieldsWithXPath() throws Exception {
        for (Scenario scenario : ScenarioFinder.getInternalScenarios("valid-*")) {
            // Generate a PDF from the internal model
            byte[] pdf = pdfGen.generatePDF(scenario.asInternalModel());
            assertNotNull("Error in scenario " + scenario.getName(), pdf);

            // Open a reader to the newly created PDF
            PdfReader pdfReader = new PdfReader(pdf);
            AcroFields fields = pdfReader.getAcroFields();

            // Create an xPath evaluator that operates on the transport model.
            XPathEvaluator xPath = createXPathEvaluator(scenario.asTransportModel());

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
        }

        throw new IllegalStateException("Unexpected field type: " + fields.getFieldType(fieldName));
    }

    private XPathEvaluator createXPathEvaluator(Utlatande transportModel) throws ParserConfigurationException,
            JAXBException, ScenarioNotFoundException {
        XPath xPath = XPathFactory.newInstance().newXPath();
        SimpleNamespaceContext namespaces = new SimpleNamespaceContext();
        namespaces.bindNamespaceUri("p", "urn:riv:clinicalprocess:healthcond:certificate:1");
        namespaces.bindNamespaceUri("p2", "urn:riv:clinicalprocess:healthcond:certificate:ts-diabetes:1");
        xPath.setNamespaceContext(namespaces);
        Node document = generateDocumentFor(transportModel);

        return new XPathEvaluator(xPath, document);
    }

    private Node generateDocumentFor(Utlatande transportModel) throws ParserConfigurationException, JAXBException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder parser = factory.newDocumentBuilder();
        Node node = parser.newDocument();

        JAXBContext context = JAXBContext.newInstance(Utlatande.class);
        context.createMarshaller().marshal(transportModel, node);

        return node;
    }
}
