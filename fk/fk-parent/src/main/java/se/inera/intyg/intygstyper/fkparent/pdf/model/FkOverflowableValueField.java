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
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.Utilities;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

import se.inera.intyg.intygstyper.fkparent.pdf.PdfConstants;

/**
 * Field component that can handle overflowing texts. Tet that could not be written within it's size is kept for later
 * rendering on dynamic pages.
 *
 * Created by marced on 2016-10-20.
 */
// CHECKSTYLE:OFF MagicNumber
public class FkOverflowableValueField extends PdfComponent<FkOverflowableValueField> {
    private static final String SEE_APPENDIX_PAGE_TEXT = "...Se fortsättningsblad!";
    // What is considered a word boundary
    private static final String WHITESPACE_REGEXP = "\\s";
    private static final float INLINE_LABEL_INDENTATION_LEFT = 2f;

    // Overflowing valuefields should hold the label even if it is displayed outside of this component as a fkLabel.
    // If overflow occurs, we need to present the label when outputting the remaining part on the extra pages at the end
    // of the document.
    private final String label;
    private final String value;

    // Defines if the label should be rendered inline in the form (some use a separate FKLabel)
    private boolean showLabelOnTop;

    // Holder for any overflowing text content not fitting the form area
    private String overflowingText;

    private Font valueFont = PdfConstants.FONT_VALUE_TEXT_ARIAL_COMPATIBLE;
    private Font overflowFont = PdfConstants.FONT_VALUE_TEXT_OVERFLOWINFO_ARIAL_COMPATIBLE;
    private Font topLabelFont = PdfConstants.FONT_INLINE_FIELD_LABEL_SMALL;

    public FkOverflowableValueField(String value, String label) {
        this.value = value != null ? trimNewLines(value) : "";
        this.label = label;
    }

    public FkOverflowableValueField showLabelOnTop() {
        this.showLabelOnTop = true;
        return this;
    }

    public String getLabel() {
        return label;
    }

    public String getOverFlowingText() {
        return overflowingText;
    }

    private String trimNewLines(String value) {
        return value.replaceAll("[\\r\\n]+", " ").trim();
    }

    @Override
    public void render(Document document, PdfWriter writer, float x, float y) throws DocumentException {
        final PdfContentByte canvas = writer.getDirectContent();
        float effectiveHeight = height;
        float effectiveY = y;

        // If were showing the label above, adjust the area available to the actual content.
        if (showLabelOnTop) {
            float labelX = Utilities.millimetersToPoints(x) + INLINE_LABEL_INDENTATION_LEFT;
            float labelY = Utilities.millimetersToPoints(y) - topLabelFont.getCalculatedSize();

            ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, new Phrase(label, topLabelFont),
                    labelX, labelY, 0);
            effectiveHeight -= Utilities.pointsToMillimeters(topLabelFont.getCalculatedSize());
            effectiveY -= Utilities.pointsToMillimeters(topLabelFont.getCalculatedSize());
        }

        Rectangle targetRect = new Rectangle(
                Utilities.millimetersToPoints(x),
                Utilities.millimetersToPoints(y - effectiveHeight),
                Utilities.millimetersToPoints(x + width),
                Utilities.millimetersToPoints(effectiveY));

        // First: Check if entire text will fit..
        int charsToWrite = findFittingLength(canvas, targetRect, value, "");

        if (charsToWrite < value.length()) {
            // Entire text did NOT fit - find how much of original value that will fit (while adding the
            // "to-be-continued" text)
            charsToWrite = findFittingLength(canvas, targetRect, value, SEE_APPENDIX_PAGE_TEXT);

            if (charsToWrite > 0) {
                // Write the text that fits..
                writeText(canvas, targetRect, value.substring(0, charsToWrite), SEE_APPENDIX_PAGE_TEXT, false);
                // Save overflowing text for later. Since we break on a whitespace, trim it.
                overflowingText = value.substring(charsToWrite).trim();
            }

        } else {
            // NO overflow detected - write entire text for real this time - with now suffix text added.
            int status = writeText(canvas, targetRect, value, "", false);

        }

        super.render(document, writer, x, y);

    }

    private boolean isWordBoundaryChar(String value, int index) {
        return index < value.length() && value.substring(index, index + 1).matches(WHITESPACE_REGEXP);
    }

    private int findFittingLength(PdfContentByte canvas, Rectangle boundingRect, String text, String overflowInfoText) throws DocumentException {
        boolean foundFittingLength = false;
        int length = text.length();
        while (length > 0 && !foundFittingLength) {

            int status = writeText(canvas, boundingRect, text.substring(0, length), overflowInfoText, true);
            foundFittingLength = !ColumnText.hasMoreText(status);

            // If wer'e in the middle of a "word" this is NOT a valid breaking point - keep looking
            if (!isWordBoundaryChar(text, length)) {

                if (length < text.length()) {

                    foundFittingLength = false;
                }
            }
            // Still not satisfied - keep removing characters..
            if (!foundFittingLength) {
                length--;
            }
        }
        return length;
    }

    private int writeText(PdfContentByte canvas, Rectangle boundingRect, String text, String overflowInfoText, boolean simulate) throws DocumentException {
        ColumnText ct = new ColumnText(canvas);

        ct.setSimpleColumn(boundingRect);

        final Paragraph p = new Paragraph(text, valueFont);
        if (overflowInfoText.length() > 0) {
            p.add(new Phrase(overflowInfoText, overflowFont));
        }

        p.setLeading(valueFont.getSize() * 1.2f);
        p.setIndentationLeft(INLINE_LABEL_INDENTATION_LEFT);
        p.setIndentationRight(2f);

        ct.addElement(p);

        return ct.go(simulate);

    }

}
