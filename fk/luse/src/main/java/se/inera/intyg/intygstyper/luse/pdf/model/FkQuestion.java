/*
 * Copyright (C) 2016 Inera AB (http://www.inera.se)
 *
 * This file is part of rehabstod (https://github.com/sklintyg/rehabstod).
 *
 * rehabstod is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * rehabstod is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.inera.intyg.intygstyper.luse.pdf.model;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Utilities;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;

import se.inera.intyg.intygstyper.luse.pdf.PdfConstants;

/**
 * Created by marced on 29/09/16.
 */
public class FkQuestion extends PdfComponent {
    private String label;

    public FkQuestion(String label) {
        this.label = label;
    }

    @Override
    public void render(PdfContentByte canvas, float x, float y) throws DocumentException {
        // Render label above x,(y - space between children and label)
        ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, new Phrase(label, PdfConstants.FONT_FRAGERUBRIK),
                Utilities.millimetersToPoints(x), Utilities.millimetersToPoints(y + 2.0f), 0);

        super.render(canvas, x, y);
    }

}
