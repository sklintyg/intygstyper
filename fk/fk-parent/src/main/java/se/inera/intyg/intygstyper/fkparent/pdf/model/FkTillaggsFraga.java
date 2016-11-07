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

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfWriter;

import se.inera.intyg.intygstyper.fkparent.pdf.PdfConstants;

/**
 * Representation of a tillaggsfraga (just a question label and an text answer).
 */
public class FkTillaggsFraga extends PdfComponent<FkTillaggsFraga> {
    private String label;
    private String value;

    public FkTillaggsFraga(String label, String value) {
        this.label = label;
        this.value = value;
    }

    @Override
    public void render(Document document, PdfWriter writer, float x, float y) throws DocumentException {
        Paragraph p = new Paragraph();
        p.setIndentationLeft(2f);
        p.setIndentationRight(2f);
        p.setKeepTogether(true);

        p.add(Chunk.NEWLINE);
        p.add(new Phrase(label, PdfConstants.FONT_FRAGERUBRIK));
        p.add(Chunk.NEWLINE);

        p.add(new Phrase(value, PdfConstants.FONT_VALUE_TEXT_ARIAL_COMPATIBLE));

        document.add(p);

        super.render(document, writer, x, y);
    }
}
