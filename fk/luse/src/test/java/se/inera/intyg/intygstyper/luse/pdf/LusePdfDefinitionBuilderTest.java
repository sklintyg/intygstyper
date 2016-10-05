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

package se.inera.intyg.intygstyper.luse.pdf;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.util.ReflectionTestUtils;
import se.inera.intyg.common.services.texts.IntygTextsServiceImpl;
import se.inera.intyg.common.services.texts.model.IntygTexts;
import se.inera.intyg.common.support.modules.support.ApplicationOrigin;
import se.inera.intyg.common.util.integration.integration.json.CustomObjectMapper;
import se.inera.intyg.intygstyper.luse.model.internal.LuseUtlatande;
import se.inera.intyg.intygstyper.luse.pdf.common.PdfGenerator;
import se.inera.intyg.intygstyper.luse.pdf.common.PdfGeneratorException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author marced
 */
public class LusePdfDefinitionBuilderTest {

    private static File luseUtlatandeFullJson;
    private static File luseUtlatandeMinimalJson;

    private ObjectMapper objectMapper = new CustomObjectMapper();

    private IntygTextsServiceImpl intygTextsService;

    @BeforeClass
    public static void readFiles() throws IOException {
        luseUtlatandeFullJson = new ClassPathResource("PdfGeneratorTest/fullt_utlatande.json").getFile();
        luseUtlatandeMinimalJson = new ClassPathResource("PdfGeneratorTest/minimalt_utlatande.json").getFile();
    }

    @Before
    public void initTexts() {
        intygTextsService  = new IntygTextsServiceImpl();
        IntygTextsLuseRepositoryTestHelper intygsTextRepositoryHelper = new IntygTextsLuseRepositoryTestHelper();
        intygsTextRepositoryHelper.update();
        ReflectionTestUtils.setField(intygTextsService, "repo", intygsTextRepositoryHelper);
    }

    @Test
    public void testGenerateFull() throws IOException, PdfGeneratorException {

        @SuppressWarnings("unchecked")

        LuseUtlatande intyg = objectMapper.readValue(luseUtlatandeFullJson, LuseUtlatande.class);

        // generate PDF

        LusePdfDefinitionBuilder lusePdfDefinitionBuilder = new LusePdfDefinitionBuilder();
        PdfGenerator generator = new PdfGenerator();
        IntygTexts intygTexts = intygTextsService.getIntygTextsPojo("luse", "1.0");
        byte[] generatorResult = generator.generatePdf(lusePdfDefinitionBuilder.buildPdfDefinition(intyg, new ArrayList<>(), ApplicationOrigin.WEBCERT, intygTexts));
        writePdfToFile(generatorResult, ApplicationOrigin.WEBCERT, "-full" + System.currentTimeMillis());
    }

    @Test
    public void testGenerateMinimal() throws IOException, PdfGeneratorException {

        @SuppressWarnings("unchecked")

        LuseUtlatande intyg = objectMapper.readValue(luseUtlatandeMinimalJson, LuseUtlatande.class);

        // generate PDF

        LusePdfDefinitionBuilder lusePdfDefinitionBuilder = new LusePdfDefinitionBuilder();
        PdfGenerator generator = new PdfGenerator();
        IntygTexts intygTexts = intygTextsService.getIntygTextsPojo("luse", "1.0");
        byte[] generatorResult = generator.generatePdf(lusePdfDefinitionBuilder.buildPdfDefinition(intyg, new ArrayList<>(), ApplicationOrigin.WEBCERT, intygTexts));
        writePdfToFile(generatorResult, ApplicationOrigin.WEBCERT, "-minimal" + System.currentTimeMillis());
    }

    private void writePdfToFile(byte[] pdf, ApplicationOrigin origin, String namingPrefix) throws IOException {
        String dir = "build/tmp";// TODO: System.getProperty("pdfOutput.dir") only existed in POM file - need to find a way in gradle;
        if (dir == null) {
            return;
        }

        File file = new File(String.format("%s/%s-%s", dir, namingPrefix, "luse-default-generator.pdf"));
        FileOutputStream fop = new FileOutputStream(file);

        file.createNewFile();

        fop.write(pdf);
        fop.flush();
        fop.close();
    }

}
