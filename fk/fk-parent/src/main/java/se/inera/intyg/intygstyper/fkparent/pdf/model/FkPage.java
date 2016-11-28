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

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;

import se.inera.intyg.intygstyper.fkparent.pdf.PdfConstants;

/**
 * A simple container for components with an optional title text.
 */
public class FkPage extends PdfComponent<FkPage> {
    private String pageTitle;

    public FkPage() {
        // Default constructor
    }

    public FkPage(String pageTitle) {
        this.pageTitle = pageTitle;
    }

    @Override
    public void render(Document document, PdfWriter writer, float x, float y) throws DocumentException {

        document.newPage();

        if (pageTitle != null) {
            Paragraph header = new Paragraph(pageTitle, PdfConstants.FONT_PAGETITLE);
            header.setIndentationLeft(2f);
            document.add(header);
        }

        super.render(document, writer, x, y);
    }
}
