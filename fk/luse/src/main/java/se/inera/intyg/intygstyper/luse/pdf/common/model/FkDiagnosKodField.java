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
package se.inera.intyg.intygstyper.luse.pdf.common.model;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.Utilities;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

import se.inera.intyg.intygstyper.luse.pdf.common.PdfConstants;

/**
 * Created by marced on 27/09/16.
 */
public class FkDiagnosKodField extends PdfComponent<FkDiagnosKodField> {

    private String fieldLabel;
    private final String value;

    private boolean withTopLabel = false;

    public FkDiagnosKodField(String value) {
        this.value = value;
    }

    public FkDiagnosKodField withTopLabel(String topLabel) {
        this.withTopLabel = true;
        this.fieldLabel = topLabel;
        return this;
    }

    @Override
    public void render(PdfContentByte canvas, float x, float y) throws DocumentException {

        PdfPTable table = new PdfPTable(4);
        char[] code = new char[] { ' ', ' ', ' ', ' ' };
        int b = 0;
        for (char c : value.toCharArray()) {
            code[b++] = c;
        }
        float[] columnWidths = new float[] {
                Utilities.millimetersToPoints(7.8f),
                Utilities.millimetersToPoints(7.8f),
                Utilities.millimetersToPoints(7.8f),
                Utilities.millimetersToPoints(7.8f) };
        // Utilities.millimetersToPoints(40f - (7.8f*4)) };
        table.setTotalWidth(columnWidths);
        for (int a = 1; a < 5; a++) {
            canvas.moveTo(Utilities.millimetersToPoints(x + 7.8f * a), Utilities.millimetersToPoints(y - height));
            canvas.lineTo(Utilities.millimetersToPoints(x + 7.8f * a), Utilities.millimetersToPoints(y - height + 2.5f));
        }

        for (char c : code) {
            PdfPCell charCell = new PdfPCell(new Phrase(String.valueOf(c), PdfConstants.FONT_NORMAL_11));
            charCell.setBorder(Rectangle.NO_BORDER);
            charCell.setFixedHeight(Utilities.millimetersToPoints(height));
            charCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            charCell.setVerticalAlignment(PdfPCell.ALIGN_BOTTOM);

            table.addCell(charCell);
        }

        if (withTopLabel) {
            float pinX = Utilities.millimetersToPoints(x); // TODO: make pin optional also
            float labelX = pinX + table.getRow(0).getCells()[0].getPaddingLeft();
            float labelY = Utilities.millimetersToPoints(y) - PdfConstants.FONT_INLINE_FIELD_LABEL_SMALL.getCalculatedSize();

            ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, new Phrase(fieldLabel, PdfConstants.FONT_INLINE_FIELD_LABEL_SMALL),
                    labelX, labelY, 0);
            canvas.moveTo(pinX, Utilities.millimetersToPoints(y));
            canvas.lineTo(pinX, Utilities.millimetersToPoints(y) - PdfConstants.FONT_INLINE_FIELD_LABEL_SMALL.getCalculatedSize());
        }

        table.writeSelectedRows(0, -1, Utilities.millimetersToPoints(x), Utilities.millimetersToPoints(y), canvas);

        super.render(canvas, x, y);

    }

}
