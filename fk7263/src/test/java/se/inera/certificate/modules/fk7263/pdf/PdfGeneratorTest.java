package se.inera.certificate.modules.fk7263.pdf;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import se.inera.certificate.integration.json.CustomObjectMapper;
import se.inera.certificate.modules.fk7263.model.Fk7263Intyg;

import com.itextpdf.text.DocumentException;
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
        fk7263_pdf = new ClassPathResource("fk7263.pdf").getFile();
        fk7263_json = new ClassPathResource("fk7263.json").getFile();
    }

    @Test
    public void testPdfGeneration() throws IOException, DocumentException {

        Fk7263Intyg intyg = new CustomObjectMapper().readValue(fk7263_json, Fk7263Intyg.class);

        // generate PDF
        byte[] generatorResult = new PdfGenerator(intyg, false).getBytes();
        AcroFields expectedFields = readExpectedFields();

        // read expected PDF fields
        PdfReader reader = new PdfReader(generatorResult);
        AcroFields generatedFields = reader.getAcroFields();

        // compare expected field values with field values in generated PDF
        for (String fieldKey: expectedFields.getFields().keySet()) {
            assertEquals("Value for field " + fieldKey + " is not the expected",
                    expectedFields.getField(fieldKey), generatedFields.getField(fieldKey));
        }
    }

    @Test
    public void pdfGenerationRemovesFormFields() throws IOException, DocumentException {
        Fk7263Intyg intyg = new CustomObjectMapper().readValue(fk7263_json, Fk7263Intyg.class);
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
    @Ignore
    @Test
    public void createTestPdf() throws IOException, DocumentException {

        Fk7263Intyg intyg = new CustomObjectMapper().readValue(fk7263_json, Fk7263Intyg.class);

        // generate PDF
        byte[] generatorResult = new PdfGenerator(intyg, false).getBytes();

        File newTestPdf = new File("utlatande.pdf");
        boolean created = newTestPdf.createNewFile();
        if (created) {
            FileOutputStream out = new FileOutputStream(newTestPdf);
            out.write(generatorResult);
            out.close();
        } else {
            fail();
        }

    }

    private AcroFields readExpectedFields() throws IOException {
        PdfReader pdfReader = new PdfReader(fk7263_pdf.getAbsolutePath());
        return pdfReader.getAcroFields();
    }
}
