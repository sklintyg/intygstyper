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
package se.inera.intyg.intygstyper.fkparent.pdf;

import org.junit.Test;
import se.inera.intyg.common.support.modules.support.api.dto.Personnummer;
import se.inera.intyg.intygstyper.fkparent.pdf.eventhandlers.*;
import se.inera.intyg.intygstyper.fkparent.pdf.model.FkLabel;
import se.inera.intyg.intygstyper.fkparent.pdf.model.FkPage;
import se.inera.intyg.intygstyper.fkparent.pdf.model.FkPdfDefinition;

import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Created 24/11-16
 */
public class PdfGeneratorTest {

    private FkPdfDefinition buildPdfDefinition() throws java.lang.Exception {
        FkPdfDefinition def = new FkPdfDefinition();
        def.addPageEvent(new PageNumberingEventHandler());

        FkPage page = new FkPage();
        FkLabel mainHeader = new FkLabel("Test Header")
                .size(40, 12f);
        page.getChildren().add(mainHeader);

        def.addChild(page);
        return def;
    }

    @Test
    public void testGeneratePdf() throws Exception{
        FkPdfDefinition def = buildPdfDefinition();
        byte[] pdfFileBytes = PdfGenerator.generatePdf(def);
        assertNotNull(pdfFileBytes);
        assertFalse(Arrays.equals(pdfFileBytes, new byte[0]));
    }

    @Test
    public void testGeneratePdfName() throws Exception {
        final String PREFIX = "test";
        final String P_NR = "19121212-1212";

        String expected = PREFIX + "_" + P_NR + ".pdf";

        String actual = PdfGenerator.generatePdfFilename(new Personnummer(P_NR), PREFIX);

        assertEquals(expected, actual);
    }

}
