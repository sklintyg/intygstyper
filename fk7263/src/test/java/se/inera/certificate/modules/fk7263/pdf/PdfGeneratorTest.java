package se.inera.certificate.modules.fk7263.pdf;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.joda.time.LocalDateTime;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import se.inera.certificate.common.enumerations.Recipients;
import se.inera.certificate.integration.json.CustomObjectMapper;
import se.inera.certificate.model.CertificateState;
import se.inera.certificate.model.Status;
import se.inera.certificate.modules.fk7263.model.internal.Utlatande;
import se.inera.certificate.modules.fk7263.utils.Scenario;
import se.inera.certificate.modules.fk7263.utils.ScenarioFinder;
import se.inera.certificate.modules.support.ApplicationOrigin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;

/**
 * @author andreaskaltenbach
 */
@ContextConfiguration(locations = ("/fk7263-test-config.xml"))
@RunWith(SpringJUnit4ClassRunner.class)
public class PdfGeneratorTest {

    private static File fk7263Pdf;
    private static File fk7263Json;
    private static File fk7263falt9bortaJson;
    private static File expectedPdfContent;
    private static File expectedPdfContentEmployer;

    @Autowired
    private ObjectMapper mapper;

    @BeforeClass
    public static void readFiles() throws IOException {
        fk7263Pdf = new ClassPathResource("PdfGeneratorTest/utlatande.pdf").getFile();
        fk7263Json = new ClassPathResource("PdfGeneratorTest/utlatande.json").getFile();
        fk7263falt9bortaJson = new ClassPathResource("PdfGeneratorTest/falt9borta.json").getFile();
        expectedPdfContent = new ClassPathResource("PdfGeneratorTest/expectedPdfContent.json").getFile();
        expectedPdfContentEmployer = new ClassPathResource("PdfGeneratorTest/expectedPdfContentEmployer.json").getFile();
    }

    @Test
    public void testPdfGeneration() throws IOException, PdfGeneratorException {

        @SuppressWarnings("unchecked")
        Map<String, String> pdfContent = mapper.readValue(expectedPdfContent, Map.class);

        Utlatande intyg = new CustomObjectMapper().readValue(fk7263Json, Utlatande.class);

        // generate PDF
        byte[] generatorResult = new PdfGenerator(intyg, new ArrayList<Status>(), false, ApplicationOrigin.WEBCERT, false).getBytes();
        AcroFields expectedFields = readExpectedFields();

        // read expected PDF fields
        PdfReader reader = new PdfReader(generatorResult);
        AcroFields generatedFields = reader.getAcroFields();

        // compare expected field values with field values in generated PDF
        for (String fieldKey : expectedFields.getFields().keySet()) {
            assertEquals("Value for field " + fieldKey + " is not the expected",
                    pdfContent.get(fieldKey), generatedFields.getField(fieldKey));
        }
    }

    @Test
    public void testPdfGenerationWithMasking() throws Exception {

        Utlatande intyg = new CustomObjectMapper().readValue(fk7263Json, Utlatande.class);
        // generate PDF
        byte[] generatorResult = new PdfGenerator(intyg, new ArrayList<Status>(), ApplicationOrigin.MINA_INTYG, false).getBytes();
        writePdfToFile(generatorResult, "Mina-intyg");
    }

    @Test
    public void testPdfGenerationFromWebcert() throws Exception {
        Utlatande intyg = new CustomObjectMapper().readValue(fk7263Json, Utlatande.class);
        // generate PDF
        byte[] generatorResult = new PdfGenerator(intyg, new ArrayList<Status>(), ApplicationOrigin.WEBCERT, false).getBytes();
        writePdfToFile(generatorResult, "Webcert");
    }

    @Test
    public void testPdfGenerationFromWebcertWidthFalt9Borta() throws Exception {
        Utlatande intyg = new CustomObjectMapper().readValue(fk7263falt9bortaJson, Utlatande.class);
        // generate PDF
        byte[] generatorResult = new PdfGenerator(intyg, new ArrayList<Status>(), ApplicationOrigin.WEBCERT, false).getBytes();
        writePdfToFile(generatorResult, "Webcert");
    }

    @Test
    public void pdfGenerationRemovesFormFields() throws IOException, PdfGeneratorException {
        Utlatande intyg = new CustomObjectMapper().readValue(fk7263Json, Utlatande.class);
        byte[] generatorResult = new PdfGenerator(intyg, new ArrayList<Status>(), ApplicationOrigin.WEBCERT, false).getBytes();

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
            byte[] pdf = new PdfGenerator(scenario.asInternalModel(), new ArrayList<Status>(), ApplicationOrigin.WEBCERT, false).getBytes();
            assertNotNull("Error in scenario " + scenario.getName(), pdf);
            writePdfToFile(pdf, scenario);
        }
    }

    @Test
    public void testGenerateEmployerCopy() throws Exception {
        @SuppressWarnings("unchecked")
        Map<String, String> pdfContent = mapper.readValue(expectedPdfContentEmployer, Map.class);

        Utlatande intyg = new CustomObjectMapper().readValue(fk7263Json, Utlatande.class);

        // generate PDF
        byte[] generatorResult = new PdfGenerator(intyg, new ArrayList<Status>(), false, ApplicationOrigin.WEBCERT, true).getBytes();
        AcroFields expectedFields = readExpectedFields();

        // read expected PDF fields
        PdfReader reader = new PdfReader(generatorResult);
        AcroFields generatedFields = reader.getAcroFields();

        // compare expected field values with field values in generated PDF
        for (String fieldKey : expectedFields.getFields().keySet()) {
            assertEquals("Value for field " + fieldKey + " is not the",
                    pdfContent.get(fieldKey), generatedFields.getField(fieldKey));
        }
    }

    @Test
    public void testPdfGenerationFromWebcertEmployerCopy() throws Exception {
        Utlatande intyg = new CustomObjectMapper().readValue(fk7263Json, Utlatande.class);
        // generate PDF
        byte[] generatorResult = new PdfGenerator(intyg, new ArrayList<Status>(), ApplicationOrigin.WEBCERT, true).getBytes();
        writePdfToFile(generatorResult, "WebcertEmployer");
    }

    /**
     * This test assert that a user can print a Intyg of type FK7263 even if it hasn't yet been sent to FK.
     * - The target property of a Status object is null in this scenario.
     * - The type property of a Status object is anything but CertificateState.SENT
     *
     * @throws Exception
     */
    @Test
    public void whenIntygIsSignedButNotSentToFKThenGeneratePDF() throws Exception {
        // Given
        Utlatande intyg = new CustomObjectMapper().readValue(fk7263Json, Utlatande.class);

        List<Status> statuses = new ArrayList<Status>();
        statuses.add(new Status(CertificateState.RECEIVED, null, LocalDateTime.now()));

        // generate PDF
        byte[] generatorResult = new PdfGenerator(intyg, statuses, ApplicationOrigin.WEBCERT, false).getBytes();
        writePdfToFile(generatorResult, "Webcert");
    }

    /**
     * This test assert that a user can print a Intyg of type FK7263 after it has been sent to FK.
     * - The target property of a Status object is FK in this scenario.
     * - The type property of a Status object is CertificateState.SENT
     *
     * @throws Exception
     */
    @Test
    public void whenIntygIsSignedAndSentToFKThenGeneratePDF() throws Exception {
        // Given
        Utlatande intyg = new CustomObjectMapper().readValue(fk7263Json, Utlatande.class);

        List<Status> statuses = new ArrayList<Status>();
        statuses.add(new Status(CertificateState.SENT, Recipients.FK.toString(), LocalDateTime.now()));

        // generate PDF
        byte[] generatorResult = new PdfGenerator(intyg, statuses, ApplicationOrigin.WEBCERT, false).getBytes();
        writePdfToFile(generatorResult, "Webcert");
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
        PdfReader pdfReader = new PdfReader(fk7263Pdf.getAbsolutePath());
        return pdfReader.getAcroFields();
    }
}
