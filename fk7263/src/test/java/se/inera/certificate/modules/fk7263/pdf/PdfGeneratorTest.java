package se.inera.certificate.modules.fk7263.pdf;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import se.inera.certificate.integration.json.CustomObjectMapper;
import se.inera.certificate.modules.fk7263.model.Fk7263Intyg;

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
    public void testPdfGeneration() throws IOException, DocumentException {

        Fk7263Intyg intyg = new CustomObjectMapper().readValue(fk7263_json, Fk7263Intyg.class);

        // generate PDF
        byte[] generatorResult = new PdfGenerator(intyg).getBytes();
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

    private AcroFields readExpectedFields() throws IOException {
        PdfReader pdfReader = new PdfReader(fk7263_pdf.getAbsolutePath());
        return pdfReader.getAcroFields();
    }
}
