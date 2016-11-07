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
package se.inera.intyg.intygstyper.fkparent.pdf.model;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.Utilities;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import se.inera.intyg.intygstyper.fkparent.pdf.PdfConstants;

/**
 * An implementation of a generic label component. Besides the common configuration properties, other adjustments can be
 * made to padding and leading etc.
 *
 */
public class FkLabel extends PdfComponent<FkLabel> {

    private final String label;
    private int verticalAlignment = PdfPCell.ALIGN_MIDDLE;
    private int horizontalAlignment = PdfPCell.ALIGN_LEFT;
    private Font font = PdfConstants.FONT_INLINE_FIELD_LABEL;
    private float fixedLeading = 0.0f;
    private float multipliedLeading = 1.0f;
    private float topPadding = 1f;

    public FkLabel(String label) {
        this.label = label;
    }

    public FkLabel withHorizontalAlignment(int alignment) {
        this.horizontalAlignment = alignment;
        return this;
    }

    public FkLabel withVerticalAlignment(int alignment) {
        this.verticalAlignment = alignment;
        return this;
    }

    public FkLabel withFont(Font font) {
        this.font = font;
        return this;
    }

    public FkLabel withLeading(float fixedLeading, float multipliedLeading) {
        this.fixedLeading = fixedLeading;
        this.multipliedLeading = multipliedLeading;
        return this;
    }

    public FkLabel withTopPadding(float padding) {
        this.topPadding = padding;
        return this;
    }

    @Override
    public void render(Document document, PdfWriter writer, float x, float y) throws DocumentException {
        PdfPTable table = new PdfPTable(1);
        table.setTotalWidth(Utilities.millimetersToPoints(width));

        // labelCell
        PdfPCell labelCell = new PdfPCell(new Phrase(label, font));

        // Make sure the tablecell has the correct height
        labelCell.setFixedHeight(Utilities.millimetersToPoints(height));
        labelCell.setBorder(Rectangle.NO_BORDER);
        labelCell.setUseAscender(true); // needed to make vertical alignment correct
        labelCell.setHorizontalAlignment(horizontalAlignment);
        labelCell.setVerticalAlignment(verticalAlignment);
        labelCell.setPaddingTop(Utilities.millimetersToPoints(topPadding));
        labelCell.setLeading(fixedLeading, multipliedLeading);
        table.addCell(labelCell);

        table.writeSelectedRows(0, -1, Utilities.millimetersToPoints(x), Utilities.millimetersToPoints(y), writer.getDirectContent());

        super.render(document, writer, x, y);
    }

}
