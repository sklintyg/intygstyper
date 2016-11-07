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
import com.itextpdf.text.Utilities;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

import se.inera.intyg.intygstyper.fkparent.pdf.PdfConstants;

/**
 * Outputs certificate id and source application text in the right margin, rotated 90 deg.
 */
public class FkPrintedByEventHandler extends PdfPageEventHelper {

    private static final float PRINTEDBY_X = Utilities.millimetersToPoints(200f);
    private static final float PRINTEDBY_Y = Utilities.millimetersToPoints(80f);
    private static final float ROTATION = 90f;
    private String intygsId;
    private String applicationOriginText;

    public FkPrintedByEventHandler(String intygsId, String applicationOriginText) {
        this.intygsId = intygsId;
        this.applicationOriginText = applicationOriginText;
    }

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        PdfContentByte canvas = writer.getDirectContentUnder();

        ColumnText.showTextAligned(canvas, Element.ALIGN_CENTER,
                new Phrase(String.format("Intygs-ID: %s. %s", intygsId, applicationOriginText), PdfConstants.FONT_STAMPER_LABEL), PRINTEDBY_X, PRINTEDBY_Y,
                ROTATION);

    }
}
