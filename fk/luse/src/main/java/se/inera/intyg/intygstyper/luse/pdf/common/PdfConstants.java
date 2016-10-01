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
package se.inera.intyg.intygstyper.luse.pdf.common;

import com.itextpdf.text.Font;

/**
 * @author marced.
 */
public final class PdfConstants {
    /**
     * Fonts that will be used in PDF.
     */
    public static final Font FONT_NORMAL_7 = new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL);
    public static final Font FONT_NORMAL_9 = new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL);
    public static final Font FONT_NORMAL_10 = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);
    public static final Font FONT_NORMAL_11 = new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL);

    public static final Font FONT_FRAGERUBRIK = new Font(Font.FontFamily.HELVETICA, 9.5f, Font.BOLD);

    public static final Font FONT_NORMAL = new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL);

    public static final Font FONT_INLINE_FIELD_LABEL = new Font(Font.FontFamily.HELVETICA, 8.5f, Font.NORMAL);
    public static final Font FONT_INLINE_FIELD_LABEL_SMALL = new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL);


    // constructors

    private PdfConstants() {
    }

}
