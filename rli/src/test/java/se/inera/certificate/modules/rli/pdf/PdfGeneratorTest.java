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
package se.inera.certificate.modules.rli.pdf;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.joda.time.LocalDateTime;
import org.junit.Test;

import se.inera.certificate.modules.rli.utils.Scenario;
import se.inera.certificate.modules.rli.utils.ScenarioFinder;

public class PdfGeneratorTest {

    private PdfGenerator pdfGen;

    public PdfGeneratorTest() {
        pdfGen = new PdfGenerator();
    }

    @Test
    public void testGeneratePdfPatientIsSick() throws Exception {
        for (Scenario scenario : ScenarioFinder.getInternalMIScenarios("valid-sjuk-?")) {
            byte[] pdf = pdfGen.generatePDF(scenario.asInternalMIModel());
            assertNotNull("Error in scenario " + scenario.getName(), pdf);
            writePdfToFile(pdf);
        }
    }

    @Test
    public void testGeneratePdfPatientIsPregnant() throws Exception {
        for (Scenario scenario : ScenarioFinder.getInternalMIScenarios("valid-gravid-?")) {
            byte[] pdf = pdfGen.generatePDF(scenario.asInternalMIModel());
            assertNotNull("Error in scenario " + scenario.getName(), pdf);
            writePdfToFile(pdf);
        }
    }

    private void writePdfToFile(byte[] pdf) throws IOException {
        String dir = System.getProperty("pdfOutput.dir");
        if (dir == null) {
            return;
        }

        File file = new File(dir + "/RLI_intyg_" + LocalDateTime.now().toString("yyyyMMdd_HHmm") + pdf.hashCode()
                + ".pdf");
        FileOutputStream fop = new FileOutputStream(file);

        file.createNewFile();

        fop.write(pdf);
        fop.flush();
        fop.close();
    }
}