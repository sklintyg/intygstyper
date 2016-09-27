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
package se.inera.intyg.intygstyper.luse.pdf.fields;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.Utilities;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

import se.inera.intyg.intygstyper.luse.pdf.PdfConstants;

/**
 * Created by marced on 27/09/16.
 */
public class FkValueField implements FkField {

    private final String fieldId;
    private String fieldLabel;
    private float fieldLabelWidth = 0;
    private final float mmValueWidth;
    private final float mmHeight;
    private final String value;
    private boolean withTopLabel = false;
    private boolean topBorder = false;

    public FkValueField(String fieldId, float mmValueWidth, float mmHeight, String value) {
        this.fieldId = fieldId;

        this.mmValueWidth = mmValueWidth;
        this.mmHeight = mmHeight;
        this.value = value;
    }

    public FkValueField withLabel(String label, float width) {
        this.withTopLabel = false;
        this.fieldLabel = label;
        this.fieldLabelWidth = width;
        return this;
    }

    public FkValueField withTopLabel(String topLabel) {
        this.withTopLabel = true;
        this.fieldLabel = topLabel;
        return this;
    }

    @Override
    public Rectangle render(PdfContentByte canvas, float x, float yTop) throws DocumentException {
        PdfPTable table = new PdfPTable(2);

        table.setTotalWidth(new float[] { Utilities.millimetersToPoints(fieldLabelWidth), Utilities.millimetersToPoints(mmValueWidth) });

        String leftSideLabelText = withTopLabel ? "" : fieldLabel;
        PdfPCell labelCell = new PdfPCell(new Phrase(leftSideLabelText, PdfConstants.FONT_NORMAL_9));
        labelCell.setBorder(topBorder ? PdfPCell.TOP : PdfPCell.NO_BORDER);
        labelCell.setFixedHeight(Utilities.millimetersToPoints(mmHeight));
        labelCell.setUseAscender(true);
        labelCell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        labelCell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        table.addCell(labelCell);

        // value cell
        PdfPCell valueCell = new PdfPCell(new Phrase(value, PdfConstants.FONT_NORMAL_9));
        valueCell.setBorder(topBorder ? PdfPCell.TOP : PdfPCell.NO_BORDER);
        valueCell.setFixedHeight(Utilities.millimetersToPoints(mmHeight));
        valueCell.setUseAscender(true);
        valueCell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        valueCell.setVerticalAlignment(PdfPCell.ALIGN_BOTTOM);
        table.addCell(valueCell);
        if (fieldLabel != null && withTopLabel) {
            float pinX = Utilities.millimetersToPoints(x); // TODO: make pin optional also
            float labelX = pinX + valueCell.getPaddingLeft();
            float labelY = Utilities.millimetersToPoints(yTop) - PdfConstants.FONT_INLINE_FIELD_LABEL_SMALL.getCalculatedSize();

            ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, new Phrase(fieldLabel, PdfConstants.FONT_INLINE_FIELD_LABEL_SMALL),
                    labelX, labelY, 0);
            canvas.moveTo(pinX, Utilities.millimetersToPoints(yTop));
            canvas.lineTo(pinX, Utilities.millimetersToPoints(yTop) - PdfConstants.FONT_INLINE_FIELD_LABEL_SMALL.getCalculatedSize());
        }

        table.writeSelectedRows(0, -1, Utilities.millimetersToPoints(x), Utilities.millimetersToPoints(yTop), canvas);
        // Return the effective Bounding box (expressed in millimeters)
        Rectangle result = new Rectangle(x, yTop - Utilities.pointsToMillimeters(table.getTotalHeight()),
                x + Utilities.pointsToMillimeters(table.getTotalWidth()), yTop);
        return result;
    }

    public FkValueField withTopBorder() {
        this.topBorder = true;
        return this;
    }
}
