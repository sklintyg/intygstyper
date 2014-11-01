#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
/**
 * Copyright (C) 2013 Inera AB (http://www.inera.se)
 *
 * This file is part of Inera Certificate Modules (http://code.google.com/p/inera-certificate-modules).
 *
 * Inera Certificate Modules is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Inera Certificate Modules is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ${package}.${artifactId-safe}.pdf;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.joda.time.LocalDateTime;
import org.junit.Test;

import ${package}.support.ApplicationOrigin;
import ${package}.${artifactId-safe}.utils.Scenario;
import ${package}.${artifactId-safe}.utils.ScenarioFinder;

public class PdfGeneratorTest {

    private PdfGenerator pdfGen;

    public PdfGeneratorTest() {
        pdfGen = new PdfGenerator(true);
    }

    @Test
    public void testGeneratePdf() throws Exception {
        for (Scenario scenario : ScenarioFinder.getInternalScenarios("valid-*")) {
            byte[] pdf = pdfGen.generatePDF(scenario.asInternalModel(), ApplicationOrigin.MINA_INTYG);
            assertNotNull("Error in scenario " + scenario.getName(), pdf);
            writePdfToFile(pdf, scenario.getName());
        }
    }

    @Test
    public void testGenerateWebcertPdf() throws Exception {
            Scenario s = ScenarioFinder.getInternalScenario("valid-maximal");
            byte[] pdf = pdfGen.generatePDF(s.asInternalModel(), ApplicationOrigin.WEBCERT);
            writePdfToFile(pdf, "webcert");
    }

    private void writePdfToFile(byte[] pdf, String name) throws IOException {
        String dir = System.getProperty("pdfOutput.dir");
        if (dir == null) {
            return;
        }

        File file = new File(String.format("%s/%s_%s.pdf", dir, name, LocalDateTime.now().toString("yyyyMMdd_HHmm")));
        FileOutputStream fop = new FileOutputStream(file);

        file.createNewFile();

        fop.write(pdf);
        fop.flush();
        fop.close();
    }
}
