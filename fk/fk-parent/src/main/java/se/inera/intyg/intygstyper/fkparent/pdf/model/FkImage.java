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

import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Utilities;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * A component that adds an image at the specified position.
 */
// CHECKSTYLE:OFF MagicNumber
public class FkImage extends PdfComponent<FkImage> {

    private static final float FULL_WIDTH = 100f;
    private byte[] imageData;

    private float linearScale = 1.0f;

    public FkImage(byte[] imageData) {
        this.imageData = imageData;
    }

    public FkImage withLinearScale(float linearScale) {
        this.linearScale = linearScale;
        return this;
    }

    @Override
    public void render(Document document, PdfWriter writer, float x, float y) throws DocumentException {
        Image fkLogo = null;
        try {
            fkLogo = Image.getInstance(imageData);
            fkLogo.setAbsolutePosition(Utilities.millimetersToPoints(x), Utilities.millimetersToPoints(y));
            fkLogo.scalePercent(linearScale * FULL_WIDTH);
            writer.getDirectContent().addImage(fkLogo);
        } catch (IOException e) {
            throw new DocumentException("Unable to render Image: " + e.getMessage());
        }

    }

}
