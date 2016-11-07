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
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.Utilities;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import se.inera.intyg.intygstyper.fkparent.pdf.PdfConstants;

/**
 * A simple container for other fields, roughly equivalent of a FK SIT model concept "Frågakategori" that holds a label.
 *
 * NOTE: Since it's easier to measure borders on a pdf printout, and the fact that on FK pdf's the label of a fieldgroup
 * is placed above tha actual border and content, we actually place the label outsite (above) this componets effective
 * bounding box.
 *
 * Created by marced on 29/09/16.
 */
public class FkFieldGroup extends PdfComponent<FkFieldGroup> {
    private static final float CATEGORY_LABEL_HEIGHT_MM = 15f;
    private static final float CATEGORY_LABEL_BORDER_MARGIN_MM = 1f;
    private String label;

    public FkFieldGroup(String label) {
        this.label = label;
    }

    @Override
    public void render(Document document, PdfWriter writer, float x, float y) throws DocumentException {

        // The label of a field group is actually placed above/outside the specified area. this is because it is much
        // easier to measure using the borders around fields on the paper copy.
        PdfPTable table = new PdfPTable(1);
        table.setTotalWidth(Utilities.millimetersToPoints(width));

        // labelCell
        PdfPCell labelCell = new PdfPCell(new Phrase(label, PdfConstants.FONT_FRAGERUBRIK));
        labelCell.setFixedHeight(Utilities.millimetersToPoints(CATEGORY_LABEL_HEIGHT_MM));
        labelCell.setBorder(Rectangle.NO_BORDER);
        labelCell.setUseAscender(true); // needed to make vertical alignment correct
        labelCell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        // We align it to the bottom, since then wrapped rows will grow upwards.
        labelCell.setVerticalAlignment(PdfPCell.ALIGN_BOTTOM);
        table.addCell(labelCell);

        // Output the table above the actual bordered category component area, and with some margin.
        table.writeSelectedRows(0, -1, Utilities.millimetersToPoints(x),
                Utilities.millimetersToPoints(y + CATEGORY_LABEL_HEIGHT_MM + CATEGORY_LABEL_BORDER_MARGIN_MM), writer.getDirectContent());

        super.render(document, writer, x, y);
    }

}
