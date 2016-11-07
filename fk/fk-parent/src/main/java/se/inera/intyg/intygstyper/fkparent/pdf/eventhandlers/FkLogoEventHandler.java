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

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Utilities;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * Generic fk-logo stamper. It's rendered at a fixed position, but page interval is configurable.
 */
public class FkLogoEventHandler extends PdfPageEventHelper {
    private static String logoPath = "images/forsakringskassans_logotyp.jpg";
    private static final float LINEAR_SCALE = 0.253f * 100f;
    private static final float LEFT_OFFSET = Utilities.millimetersToPoints(16f);
    private static final float TOP_OFFSET = Utilities.millimetersToPoints(20f);

    private int activeFromPage;
    private int activeToPage;
    private Image fkLogo = null;

    public FkLogoEventHandler(int activeFromPage, int activeToPage) throws DocumentException {
        this.activeFromPage = activeFromPage;
        this.activeToPage = activeToPage;
        try {
            Resource resource = new ClassPathResource(logoPath);
            fkLogo = Image.getInstance(IOUtils.toByteArray(resource.getInputStream()));
            fkLogo.scalePercent(LINEAR_SCALE);
        } catch (IOException e) {
            throw new DocumentException("Unable to initialise FkLogoEventHandler: " + e.getMessage());
        }
    }

    /**
     * Adds a FK logo to every page in the from-tom interval.
     *
     * @see PdfPageEventHelper#onEndPage(PdfWriter,
     *      Document)
     */
    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        if (writer.getPageNumber() >= activeFromPage && writer.getPageNumber() <= activeToPage) {

            try {
                fkLogo.setAbsolutePosition(LEFT_OFFSET, document.getPageSize().getTop() - TOP_OFFSET);
                writer.getDirectContent().addImage(fkLogo);
            } catch (DocumentException e) {
                throw new RuntimeException(e);
            }

        }
    }
}
