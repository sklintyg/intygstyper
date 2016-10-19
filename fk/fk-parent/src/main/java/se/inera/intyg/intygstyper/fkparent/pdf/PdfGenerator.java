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

import java.io.ByteArrayOutputStream;
import java.util.List;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Utilities;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

import se.inera.intyg.common.support.model.common.internal.Utlatande;
import se.inera.intyg.common.support.modules.support.api.dto.Personnummer;
import se.inera.intyg.intygstyper.fkparent.pdf.model.FkPage;
import se.inera.intyg.intygstyper.fkparent.pdf.model.FkPdfDefinition;

/**
 * Created by marced on 30/09/16.
 */
public class PdfGenerator {

    public static byte[] generatePdf(FkPdfDefinition model) throws PdfGeneratorException {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try {

            Document document = new Document();
            document.setPageSize(PageSize.A4);

            PdfWriter writer = PdfWriter.getInstance(document, bos);

            // Add handlers for page events
            for (PdfPageEventHelper eventHelper : model.getPageEvents()) {
                writer.setPageEvent(eventHelper);
            }

            document.open();

            PdfContentByte canvas = writer.getDirectContent();

            List<FkPage> pages = model.getPages();
            for (int i = 0, pagesSize = pages.size(); i < pagesSize; i++) {
                FkPage page = pages.get(i);

                page.render(canvas, 0f, Utilities.pointsToMillimeters(document.getPageSize().getTop()));
                if (i < (pagesSize - 1)) {
                    document.newPage();
                }
            }

            // Finish off by closing the document (will invoke the event handlers)
            document.close();

        } catch (DocumentException | RuntimeException e) {
            throw new PdfGeneratorException("Failed to create PDF", e);
        }

        return bos.toByteArray();
    }

    public static String generatePdfFilename(Utlatande utlatande, String moduleId) {
        Personnummer personId = utlatande.getGrundData().getPatient().getPersonId();
        final String personnummerString = personId.getPersonnummer() != null ? personId.getPersonnummer() : "NoPnr";
        return String.format("%s_lakarutlatande_%s.pdf", moduleId, personnummerString);
    }
}
