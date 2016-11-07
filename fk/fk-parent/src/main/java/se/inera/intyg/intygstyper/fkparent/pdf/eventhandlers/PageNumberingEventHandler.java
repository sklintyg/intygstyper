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
package se.inera.intyg.intygstyper.fkparent.pdf.eventhandlers;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.Utilities;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

import se.inera.intyg.intygstyper.fkparent.pdf.PdfConstants;

/**
 * A generic page numbering event handler.
 */
// CHECKSTYLE:OFF MagicNumber
public class PageNumberingEventHandler extends PdfPageEventHelper {
    private static final int WIDTH = 30;
    private static final int HEIGHT = 16;
    /**
     * The template with the issueInfoTemplate number of pages.
     */
    PdfTemplate total;

    /**
     * Creates the PdfTemplate that will hold the issueInfoTemplate number of pages.
     *
     * @see com.itextpdf.text.pdf.PdfPageEventHelper#onOpenDocument(com.itextpdf.text.pdf.PdfWriter,
     *      com.itextpdf.text.Document)
     */
    @Override
    public void onOpenDocument(PdfWriter writer, Document document) {
        total = writer.getDirectContent().createTemplate(WIDTH, HEIGHT);
    }

    /**
     * Adds a header to every page.
     *
     * @see com.itextpdf.text.pdf.PdfPageEventHelper#onEndPage(com.itextpdf.text.pdf.PdfWriter,
     *      com.itextpdf.text.Document)
     */
    @Override
    public void onEndPage(PdfWriter writer, Document document) {

        try {
            PdfPTable table = new PdfPTable(2);

            table.setTotalWidth(Utilities.millimetersToPoints(20));
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            table.addCell(new Phrase(String.valueOf(writer.getPageNumber()), PdfConstants.FONT_PAGE_NUMBERING));

            PdfPCell cell = new PdfPCell(Image.getInstance(total));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            table.writeSelectedRows(0, -1, Utilities.millimetersToPoints(181f), document.getPageSize().getTop() - Utilities.millimetersToPoints(8f),
                    writer.getDirectContent());

        } catch (DocumentException de) {
            throw new ExceptionConverter(de);
        }
    }

    /**
     * Fills out the issueInfoTemplate number of pages before the document is closed.
     *
     * @see com.itextpdf.text.pdf.PdfPageEventHelper#onCloseDocument(com.itextpdf.text.pdf.PdfWriter,
     *      com.itextpdf.text.Document)
     */
    @Override
    public void onCloseDocument(PdfWriter writer, Document document) {
        // CHECKSTYLE:OFF MagicNumber
        ColumnText.showTextAligned(total,
                Element.ALIGN_LEFT,
                new Phrase("(" + Integer.toString(writer.getPageNumber() - 1) + ")", PdfConstants.FONT_PAGE_NUMBERING), 0, Utilities.millimetersToPoints(1f),
                0);
    }
}
