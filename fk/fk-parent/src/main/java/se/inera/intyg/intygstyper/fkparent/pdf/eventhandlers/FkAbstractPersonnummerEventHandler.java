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
import com.itextpdf.text.Element;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.Utilities;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

import se.inera.intyg.intygstyper.fkparent.pdf.PdfConstants;

/**
 * Generic personnumber page event handler, allowing concrete subclasses to specify where and on which pages to output
 * personnummer.
 */
public abstract class FkAbstractPersonnummerEventHandler extends PdfPageEventHelper {

    // Table with (in mm)
    private static final float TABLE_WIDTH = 30f;
    private String personnummer;

    public FkAbstractPersonnummerEventHandler(String personnummer) {
        this.personnummer = personnummer;
    }

    protected abstract int getActiveFromPage();

    protected abstract int getActiveToPage();

    protected abstract float getXOffset();

    protected abstract float getYOffset();

    /**
     * Adds the personnummer to every page in from-tom interval.
     *
     * @see com.itextpdf.text.pdf.PdfPageEventHelper#onEndPage(com.itextpdf.text.pdf.PdfWriter,
     *      com.itextpdf.text.Document)
     */
    @Override
    public void onEndPage(PdfWriter writer, Document document) {

        PdfPTable table = new PdfPTable(1);
        table.setTotalWidth(Utilities.millimetersToPoints(TABLE_WIDTH));
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_TOP);
        table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        if (writer.getPageNumber() >= getActiveFromPage() && writer.getPageNumber() <= getActiveToPage()) {

            // Page 2+
            table.addCell(new Phrase("Personnummer", PdfConstants.FONT_STAMPER_LABEL));
            table.completeRow();
            table.addCell(new Phrase(String.valueOf(personnummer), PdfConstants.FONT_VALUE_TEXT));

            table.writeSelectedRows(0, -1, Utilities.millimetersToPoints(getXOffset()),
                    document.getPageSize().getTop() - Utilities.millimetersToPoints(getYOffset()),
                    writer.getDirectContent());
        }
    }
}
