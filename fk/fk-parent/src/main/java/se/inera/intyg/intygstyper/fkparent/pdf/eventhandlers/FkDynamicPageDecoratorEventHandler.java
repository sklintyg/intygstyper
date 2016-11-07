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

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.Utilities;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

import se.inera.intyg.intygstyper.fkparent.pdf.PdfConstants;

/**
 * Decorates the "dynamic" overflow output pages with FK logo, borders etc common for all non-static pages in a FK SIT
 * type PDF.
 * Given that all SIT-intyg types will follow the same rules for handling overflowing fields, this event handler could
 * be
 * reused for all those types.
 */
// CHECKSTYLE:OFF MagicNumber
public class FkDynamicPageDecoratorEventHandler extends PdfPageEventHelper {
    private static final float BORDER_WIDTH = 0.2f;

    private static final float INTYG_NAME_X = Utilities.millimetersToPoints(107.5f);
    private static final float INTYG_NAME_ROW1_Y_OFFSET = Utilities.millimetersToPoints(13f);
    private static final float INTYG_NAME_ROW2_Y_OFFSET = Utilities.millimetersToPoints(16f);

    // Which page number to start decoration
    private int startFromPage;

    // Margins needed to position border
    private final float[] pageMargins;
    private String intygsNamnRow1;
    private String intygsNamnRow2;

    public FkDynamicPageDecoratorEventHandler(int startFromPage, float[] pageMargins, String intygsNamnRow1, String intygsNamnRow2) {
        this.startFromPage = startFromPage;
        this.pageMargins = pageMargins;
        this.intygsNamnRow1 = intygsNamnRow1;
        this.intygsNamnRow2 = intygsNamnRow2;
    }

    /**
     * Decorates overflow pages with intygsname and a border.
     *
     * @param writer
     *            PdfWriter
     * @param document
     *            Document
     */
    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        if (writer.getPageNumber() < startFromPage) {
            return;
        }

        drawBorder(writer, document);
        drawIntygsNamn(writer, document);

    }

    private void drawIntygsNamn(PdfWriter writer, Document document) {

        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_LEFT, new Phrase(intygsNamnRow1, PdfConstants.FONT_FRAGERUBRIK), INTYG_NAME_X,
                document.getPageSize().getTop() - INTYG_NAME_ROW1_Y_OFFSET, 0);
        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_LEFT, new Phrase(intygsNamnRow2, PdfConstants.FONT_BOLD_9), INTYG_NAME_X,
                document.getPageSize().getTop() - INTYG_NAME_ROW2_Y_OFFSET, 0);
    }

    private void drawBorder(PdfWriter writer, Document document) {
        Rectangle p = document.getPageSize();
        Rectangle rect = new Rectangle(p.getLeft(pageMargins[0]), p.getBottom(pageMargins[3]), p.getRight(pageMargins[1]), p.getTop(pageMargins[2]));
        rect.setBorder(Rectangle.BOX);
        rect.setBorderWidth(Utilities.millimetersToPoints(BORDER_WIDTH));
        rect.setBorderColor(BaseColor.BLACK);
        writer.getDirectContent().rectangle(rect);

    }

}
