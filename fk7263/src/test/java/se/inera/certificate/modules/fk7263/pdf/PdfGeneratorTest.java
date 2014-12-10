package se.inera.certificate.modules.fk7263.pdf;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

import org.joda.time.LocalDateTime;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import se.inera.certificate.integration.json.CustomObjectMapper;
import se.inera.certificate.modules.fk7263.model.internal.Fk7263Intyg;
import se.inera.certificate.modules.fk7263.utils.Scenario;
import se.inera.certificate.modules.fk7263.utils.ScenarioFinder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;

/**
 * @author andreaskaltenbach
 */
public class PdfGeneratorTest {

    private static File fk7263Pdf;
    private static File fk7263Json;
    private static File expectedPdfContent;

    private ObjectMapper mapper = new ObjectMapper(); 

    @BeforeClass
    public static void readFiles() throws IOException {
        fk7263Pdf = new ClassPathResource("PdfGeneratorTest/utlatande.pdf").getFile();
        fk7263Json = new ClassPathResource("PdfGeneratorTest/utlatande.json").getFile();
        expectedPdfContent = new ClassPathResource("PdfGeneratorTest/expectedPdfContent.json").getFile();
    }

    @Test
    public void testPdfGeneration() throws IOException, PdfGeneratorException {

        @SuppressWarnings("unchecked")
        Map<String, String> pdfContent = mapper.readValue(expectedPdfContent, Map.class);

        Fk7263Intyg intyg = new CustomObjectMapper().readValue(fk7263Json, Fk7263Intyg.class);

        // generate PDF
        byte[] generatorResult = new PdfGenerator(intyg, false).getBytes();
        AcroFields expectedFields = readExpectedFields();

        // read expected PDF fields
        PdfReader reader = new PdfReader(generatorResult);
        AcroFields generatedFields = reader.getAcroFields();

        // compare expected field values with field values in generated PDF
        for (String fieldKey: expectedFields.getFields().keySet()) {
            assertEquals("Value for field " + fieldKey + " is not the expected",
                    pdfContent.get(fieldKey), generatedFields.getField(fieldKey));
        }
    }

    @Test
    public void pdfGenerationRemovesFormFields() throws IOException, PdfGeneratorException {
        Fk7263Intyg intyg = new CustomObjectMapper().readValue(fk7263Json, Fk7263Intyg.class);
        byte[] generatorResult = new PdfGenerator(intyg).getBytes();
        PdfReader reader = new PdfReader(generatorResult);
        AcroFields generatedFields = reader.getAcroFields();

        assertEquals(0, generatedFields.getFields().size());
    }
    
    /**
     * This test creates a new document to compare against. The new document ends up in the project root.
     *
     * @throws IOException
     * @throws DocumentException
     */
    @Test
    public void testGeneratePdf() throws Exception {
        for (Scenario scenario : ScenarioFinder.getInternalScenarios("valid-*")) {
            byte[] pdf = new PdfGenerator(scenario.asInternalModel()).getBytes();
            assertNotNull("Error in scenario " + scenario.getName(), pdf);
            writePdfToFile(pdf, scenario);
        }
    }
    
    private void writePdfToFile(byte[] pdf, Scenario scenario) throws IOException {
        String dir = System.getProperty("pdfOutput.dir");
        if (dir == null) {
            return;
        }

        File file = new File(String.format("%s/%s_%s.pdf", dir, scenario.getName(), LocalDateTime.now().toString("yyyyMMdd_HHmm")));
        FileOutputStream fop = new FileOutputStream(file);

        file.createNewFile();

        fop.write(pdf);
        fop.flush();
        fop.close();
    }

    private AcroFields readExpectedFields() throws IOException {
        PdfReader pdfReader = new PdfReader(fk7263Pdf.getAbsolutePath());
        return pdfReader.getAcroFields();
    }
}
