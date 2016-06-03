/*
 * Copyright (C) 2016 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package se.inera.intyg.intygstyper.fk7263.pdf;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.*;
import java.util.*;

import org.joda.time.LocalDateTime;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;

import se.inera.intyg.common.support.model.CertificateState;
import se.inera.intyg.common.support.model.Status;
import se.inera.intyg.common.support.modules.support.ApplicationOrigin;
import se.inera.intyg.common.util.integration.integration.json.CustomObjectMapper;
import se.inera.intyg.intygstyper.fk7263.model.internal.Utlatande;
import se.inera.intyg.intygstyper.fk7263.utils.Scenario;
import se.inera.intyg.intygstyper.fk7263.utils.ScenarioFinder;

/**
 * @author andreaskaltenbach
 */
public class PdfGeneratorTest {

    private static File fk7263Pdf;
    private static File fk7263Json;
    private static File fk7263falt9bortaJson;
    private static File expectedPdfContent;
    private static File expectedPdfContentEmployer;

    private ObjectMapper objectMapper = new CustomObjectMapper();

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
        Map<String, String> pdfContent = objectMapper.readValue(expectedPdfContent, Map.class);

        Utlatande intyg = objectMapper.readValue(fk7263Json, Utlatande.class);

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

        Utlatande intyg = objectMapper.readValue(fk7263Json, Utlatande.class);
        // generate PDF
        byte[] generatorResult = new PdfGenerator(intyg, new ArrayList<Status>(), ApplicationOrigin.MINA_INTYG, false).getBytes();
        writePdfToFile(generatorResult, "Mina-intyg");
    }

    @Test
    public void testPdfGenerationFromWebcert() throws Exception {
        Utlatande intyg = objectMapper.readValue(fk7263Json, Utlatande.class);
        // generate PDF
        byte[] generatorResult = new PdfGenerator(intyg, new ArrayList<Status>(), ApplicationOrigin.WEBCERT, false).getBytes();
        writePdfToFile(generatorResult, "Webcert");
    }

    @Test
    public void testPdfGenerationFromWebcertWidthFalt9Borta() throws Exception {
        Utlatande intyg = objectMapper.readValue(fk7263falt9bortaJson, Utlatande.class);
        // generate PDF
        byte[] generatorResult = new PdfGenerator(intyg, new ArrayList<Status>(), ApplicationOrigin.WEBCERT, false).getBytes();
        writePdfToFile(generatorResult, "Webcert");
    }

    @Test
    public void pdfGenerationRemovesFormFields() throws IOException, PdfGeneratorException {
        Utlatande intyg = objectMapper.readValue(fk7263Json, Utlatande.class);
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
        Map<String, String> pdfContent = objectMapper.readValue(expectedPdfContentEmployer, Map.class);

        Utlatande intyg = objectMapper.readValue(fk7263Json, Utlatande.class);

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
        Utlatande intyg = objectMapper.readValue(fk7263Json, Utlatande.class);
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
        Utlatande intyg = objectMapper.readValue(fk7263Json, Utlatande.class);

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
        Utlatande intyg = objectMapper.readValue(fk7263Json, Utlatande.class);

        List<Status> statuses = new ArrayList<Status>();
        statuses.add(new Status(CertificateState.SENT, "FK", LocalDateTime.now()));

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
