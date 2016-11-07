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

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.RectangleReadOnly;
import com.itextpdf.text.Utilities;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

import se.inera.intyg.intygstyper.fkparent.pdf.PdfConstants;

/**
 * An implementation of a checkbox that can be checked with an "X" and have a label.
 */
// CHECKSTYLE:OFF MagicNumber
public class FkCheckbox extends PdfComponent<FkCheckbox> {

    // internal property config
    private static final Rectangle CHECKBOX_DIMENSIONS_IN_POINTS = new RectangleReadOnly(Utilities.millimetersToPoints(4.5f),
            Utilities.millimetersToPoints(4.5f));
    private static final float CHECKBOX_BORDER_WIDTH = Utilities.millimetersToPoints(0.4f);
    private static final float CHECKBOX_X_LINE_WIDTH = Utilities.millimetersToPoints(0.2f);
    private static final float CHECKBOX_INNER_PADDING = Utilities.millimetersToPoints(0.7f);
    private static final float CHECKBOX_LABEL_LEFTPADDING = Utilities.millimetersToPoints(1.5f);
    private float cellWidth = 7f;

    private final String fieldLabel;
    private final boolean isChecked;

    private int verticalAlignment = PdfPCell.ALIGN_MIDDLE;

    public FkCheckbox(String fieldLabel, boolean isChecked) {
        this.fieldLabel = fieldLabel;
        this.isChecked = isChecked;
    }

    public FkCheckbox withCellWith(float cellWidth) {
        this.cellWidth = cellWidth;
        return this;
    }

    public FkCheckbox withVerticalAlignment(int alignment) {
        this.verticalAlignment = alignment;
        return this;
    }

    @Override
    public void render(Document document, PdfWriter writer, float x, float y) throws DocumentException {
        PdfPTable table = new PdfPTable(2);
        table.setTotalWidth(
                new float[] { Utilities.millimetersToPoints(cellWidth), Utilities.millimetersToPoints(width - cellWidth) });

        Image checkbox = createCheckbox(writer.getDirectContent(), isChecked);

        PdfPCell checkboxCell = new PdfPCell(checkbox);
        checkboxCell.setFixedHeight(Utilities.millimetersToPoints(height));
        checkboxCell.setBorder(Rectangle.NO_BORDER);
        checkboxCell.setUseAscender(true);
        checkboxCell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        checkboxCell.setVerticalAlignment(verticalAlignment);
        table.addCell(checkboxCell);

        // labelCell
        PdfPCell labelCell = new PdfPCell(new Phrase(fieldLabel, PdfConstants.FONT_INLINE_FIELD_LABEL));

        // Make sure the table has the correct height
        labelCell.setFixedHeight(Utilities.millimetersToPoints(height));
        labelCell.setBorder(Rectangle.NO_BORDER);
        labelCell.setUseAscender(true);
        labelCell.setPaddingLeft(CHECKBOX_LABEL_LEFTPADDING);
        labelCell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        labelCell.setVerticalAlignment(verticalAlignment);
        table.addCell(labelCell);

        table.writeSelectedRows(0, -1, Utilities.millimetersToPoints(x), Utilities.millimetersToPoints(y), writer.getDirectContent());
        super.render(document, writer, x, y);
    }

    private Image createCheckbox(PdfContentByte canvas, boolean isChecked) throws BadElementException {

        final PdfTemplate template = canvas.createTemplate(CHECKBOX_DIMENSIONS_IN_POINTS.getWidth(), CHECKBOX_DIMENSIONS_IN_POINTS.getHeight());
        Rectangle rect = new Rectangle(0, 0, CHECKBOX_DIMENSIONS_IN_POINTS.getWidth(), CHECKBOX_DIMENSIONS_IN_POINTS.getHeight());
        rect.setBorder(Rectangle.BOX);
        rect.setBorderWidth(CHECKBOX_BORDER_WIDTH);
        rect.setBorderColor(BaseColor.BLACK);
        template.rectangle(rect);

        if (isChecked) {
            // Draw an X in the rectangle
            template.setLineWidth(CHECKBOX_X_LINE_WIDTH);
            template.moveTo(rect.getLeft() + CHECKBOX_INNER_PADDING, rect.getTop() - CHECKBOX_INNER_PADDING);
            template.lineTo(rect.getRight() - CHECKBOX_INNER_PADDING, rect.getBottom() + CHECKBOX_INNER_PADDING);
            template.moveTo(rect.getRight() - CHECKBOX_INNER_PADDING, rect.getTop() - CHECKBOX_INNER_PADDING);
            template.lineTo(rect.getLeft() + CHECKBOX_INNER_PADDING, rect.getBottom() + CHECKBOX_INNER_PADDING);
        }

        template.stroke();

        return Image.getInstance(template);
    }

}
