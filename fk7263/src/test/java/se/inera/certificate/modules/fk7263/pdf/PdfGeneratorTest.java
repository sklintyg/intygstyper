package se.inera.certificate.modules.fk7263.pdf;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.joda.time.LocalDateTime;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import se.inera.certificate.integration.json.CustomObjectMapper;
import se.inera.certificate.modules.fk7263.model.internal.Fk7263Intyg;
import se.inera.certificate.modules.fk7263.utils.Scenario;
import se.inera.certificate.modules.fk7263.utils.ScenarioFinder;
import se.inera.certificate.modules.support.ApplicationOrigin;

import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;

/**
 * @author andreaskaltenbach
 */
public class PdfGeneratorTest {

    private static File fk7263_pdf;
    private static File fk7263_json;

    @BeforeClass
    public static void readFiles() throws IOException {
        fk7263_pdf = new ClassPathResource("PdfGeneratorTest/utlatande.pdf").getFile();
        fk7263_json = new ClassPathResource("PdfGeneratorTest/utlatande.json").getFile();
    }

    @Test
    public void testPdfGeneration() throws IOException, PdfGeneratorException {

        Fk7263Intyg intyg = new CustomObjectMapper().readValue(fk7263_json, Fk7263Intyg.class);

        // generate PDF
        byte[] generatorResult = new PdfGenerator(intyg, false, ApplicationOrigin.WEBCERT).getBytes();
        AcroFields expectedFields = readExpectedFields();

        // read expected PDF fields
        PdfReader reader = new PdfReader(generatorResult);
        AcroFields generatedFields = reader.getAcroFields();

        // compare expected field values with field values in generated PDF
        for (String fieldKey : expectedFields.getFields().keySet()) {
            assertEquals("Value for field " + fieldKey + " is not the expected",
                    expectedFields.getField(fieldKey), generatedFields.getField(fieldKey));
        }
    }

    @Test
    public void testPdfGenerationWithMasking() throws Exception {

        Fk7263Intyg intyg = new CustomObjectMapper().readValue(fk7263_json, Fk7263Intyg.class);
        // generate PDF
        byte[] generatorResult = new PdfGenerator(intyg, true, ApplicationOrigin.MINA_INTYG).getBytes();
        writePdfToFile(generatorResult, "Mina-intyg");
    }

    @Test
    public void testPdfGenerationFromWebcert() throws Exception {
        Fk7263Intyg intyg = new CustomObjectMapper().readValue(fk7263_json, Fk7263Intyg.class);
        // generate PDF
        byte[] generatorResult = new PdfGenerator(intyg, true, ApplicationOrigin.WEBCERT).getBytes();
        writePdfToFile(generatorResult, "Webcert");
    }

    @Test
    public void pdfGenerationRemovesFormFields() throws IOException, PdfGeneratorException {
        Fk7263Intyg intyg = new CustomObjectMapper().readValue(fk7263_json, Fk7263Intyg.class);
        byte[] generatorResult = new PdfGenerator(intyg, ApplicationOrigin.WEBCERT).getBytes();

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
            byte[] pdf = new PdfGenerator(scenario.asInternalModel(), ApplicationOrigin.WEBCERT).getBytes();
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

    private void writePdfToFile(byte[] pdf, String namingPrefix) throws IOException {
        String dir = System.getProperty("pdfOutput.dir");
        if (dir == null) {
            return;
        }

        File file = new File(String.format("%s/%s_masked_send_to_information.pdf", dir, namingPrefix));
        FileOutputStream fop = new FileOutputStream(file);

        file.createNewFile();

        fop.write(pdf);
        fop.flush();
        fop.close();
    }

    private AcroFields readExpectedFields() throws IOException {
        PdfReader pdfReader = new PdfReader(fk7263_pdf.getAbsolutePath());
        return pdfReader.getAcroFields();
    }
}
