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
 * Stamps the fk form issue version info on each page (left side margin).
 */
public class FkFormIdentityEventHandler extends PdfPageEventHelper {

    private static final float FORMID_X = Utilities.millimetersToPoints(12f);
    private static final float FORMID_Y = Utilities.millimetersToPoints(8.5f);

    private static final float SCANID_X = Utilities.millimetersToPoints(12f);
    private static final float SCANID_Y = Utilities.millimetersToPoints(118f);
    private static final float ROTATION = 90f;

    private String formId;
    private String blankettId;
    private final String blankettVersion;

    public FkFormIdentityEventHandler(String formId, String blankettId, String blankettVersion) {
        // FormId is static
        this.formId = formId;
        this.blankettId = blankettId;
        this.blankettVersion = blankettVersion;
    }

    /**
     * Stamps the fk issue info on each page.
     *
     * @param writer
     * @param document
     */
    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        PdfContentByte canvas = writer.getDirectContentUnder();

        ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, new Phrase(formId, PdfConstants.FONT_FORM_ID_LABEL), FORMID_X, FORMID_Y, ROTATION);
        ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT,
                new Phrase(buildPageScanId(blankettId, blankettVersion, writer.getPageNumber()), PdfConstants.FONT_PAGESCAN_ID), SCANID_X, SCANID_Y, ROTATION);

    }

    private String buildPageScanId(String blankettId, String blankettVersion, int pageNumber) {
        /*******************************************************
         * (Hittat i FK7800_001_F_001.pdf i extraherad xfa xml definition) rad 445
         * "XXXX" + "A" + "B" + "VV" *
         * *
         * XXXX = blankettnummer, t.ex. 7263 *
         * A = 1 vid enkelsidig utskrift, 2 vid dubbelsidig *
         * B = sidnummer *
         * VV = versionsnummer på skanningsmallen
         *
         * //OBS! Sätt rätt värden nedan
         * var pageNum = xfa.layout.page(this);
         * if (pageNum &lt;= 9)
         * {
         * pageNum = "0" + pageNum;
         * }
         * skanningsid.rawValue = "7800" + pageNum + "01";
         ********************************************************/
        return blankettId + String.format("%02d", pageNumber) + blankettVersion;
    }

}
