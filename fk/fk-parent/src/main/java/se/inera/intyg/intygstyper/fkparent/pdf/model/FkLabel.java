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

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.Utilities;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

import se.inera.intyg.intygstyper.fkparent.pdf.PdfConstants;

/**
 * Created by marced on 27/09/16.
 */
public class FkLabel extends PdfComponent<FkLabel> {

    private final String label;
    private int valueTextVerticalAlignment = PdfPCell.ALIGN_MIDDLE;
    private Font font = PdfConstants.FONT_INLINE_FIELD_LABEL;
    private float fixedLeading = 0.0f;
    private float multipliedLeading = 1.0f;
    private float topPadding = 1f;

    public FkLabel(String label) {
        this.label = label;
    }

    public FkLabel withVerticalAlignment(int alignment) {
        this.valueTextVerticalAlignment = alignment;
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
    public void render(PdfContentByte canvas, float x, float y) throws DocumentException {
        PdfPTable table = new PdfPTable(1);
        table.setTotalWidth(Utilities.millimetersToPoints(width));

        // labelCell
        PdfPCell labelCell = new PdfPCell(new Phrase(label, font));

        // Make sure the tablecell has the correct height
        labelCell.setFixedHeight(Utilities.millimetersToPoints(height));
        labelCell.setBorder(Rectangle.NO_BORDER);
        labelCell.setUseAscender(true); // needed to make vertical alignment correct
        labelCell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        labelCell.setVerticalAlignment(valueTextVerticalAlignment);
        labelCell.setPaddingTop(Utilities.millimetersToPoints(topPadding));
        labelCell.setLeading(fixedLeading, multipliedLeading);
        table.addCell(labelCell);

        table.writeSelectedRows(0, -1, Utilities.millimetersToPoints(x), Utilities.millimetersToPoints(y), canvas);

        super.render(canvas, x, y);
    }

}
