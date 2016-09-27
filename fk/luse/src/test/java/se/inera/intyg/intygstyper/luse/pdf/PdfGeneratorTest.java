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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import com.fasterxml.jackson.databind.ObjectMapper;

import se.inera.intyg.common.support.model.Status;
import se.inera.intyg.common.support.modules.support.ApplicationOrigin;
import se.inera.intyg.common.util.integration.integration.json.CustomObjectMapper;
import se.inera.intyg.intygstyper.luse.model.internal.LuseUtlatande;

/**
 * @author marced
 */
public class PdfGeneratorTest {

    private static File luseUtlatandeJson;

    private ObjectMapper objectMapper = new CustomObjectMapper();

    @BeforeClass
    public static void readFiles() throws IOException {
        luseUtlatandeJson = new ClassPathResource("PdfGeneratorTest/utlatande.json").getFile();
    }

    @Test
    public void testGenerate() throws IOException, PdfGeneratorException {

        @SuppressWarnings("unchecked")

        LuseUtlatande intyg = objectMapper.readValue(luseUtlatandeJson, LuseUtlatande.class);

        // generate PDF

        PdfGenerator pdfGenerator = new PdfGenerator();
        byte[] generatorResult = pdfGenerator.generatePdf(intyg, new ArrayList<Status>(), ApplicationOrigin.WEBCERT);
        writePdfToFile(generatorResult, ApplicationOrigin.WEBCERT, "-normal" + System.currentTimeMillis());
    }

    private void writePdfToFile(byte[] pdf, ApplicationOrigin origin, String namingPrefix) throws IOException {
        String dir = "build/tmp";// TODO: System.getProperty("pdfOutput.dir") only existed in POM file - need to find a way in gradle;
        if (dir == null) {
            return;
        }

        File file = new File(String.format("%s/%s-%s-%s", dir, origin.name(), namingPrefix, "-default-generator.pdf"));
        FileOutputStream fop = new FileOutputStream(file);

        file.createNewFile();

        fop.write(pdf);
        fop.flush();
        fop.close();
    }

}
