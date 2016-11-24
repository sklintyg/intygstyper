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
import com.itextpdf.text.pdf.*;

import se.inera.intyg.intygstyper.fkparent.pdf.PdfConstants;

/**
 * Field component that can handle overflowing texts. Text that could not be written within the components dimensions is truncated and the remainder is kept for
 * later use (rendered on dynamic pages).
 *
 * Created by marced on 2016-10-20.
 */
public class FkOverflowableValueField extends PdfComponent<FkOverflowableValueField> {

    private static final String SEE_APPENDIX_PAGE_TEXT = "... Se fortsättningsblad!";
    // What is considered a word boundary
    private static final String WHITESPACE_REGEXP = "\\s";

    private static final float INDENTATION_LEFT = 2f;
    private static final float INDENTATION_RIGHT = 2f;

    private static final float LEADING_FACTOR = 1.2f;

    // Overflowing valuefields should hold the label even if it is displayed outside of this component as a fkLabel.
    // If overflow occurs, we need to present the label when outputting the remaining part on the extra pages at the end
    // of the document.
    private final String label;
    private final String value;
    // Holder for any overflowing text content not fitting the form area
    private String overflowingText;

    // Defines if the label should be rendered inline in the form (some use a separate FKLabel)
    private boolean showLabelOnTop;

    // Defines if newline characters should be kept in value text or not
    private boolean keepNewlines = false;

    private Font valueFont = PdfConstants.FONT_VALUE_TEXT_ARIAL_COMPATIBLE;
    private Font overflowFont = PdfConstants.FONT_VALUE_TEXT_OVERFLOWINFO_ARIAL_COMPATIBLE;
    private Font topLabelFont = PdfConstants.FONT_INLINE_FIELD_LABEL_SMALL;

    public FkOverflowableValueField(String value, String label) {
        this.value = value != null ? value : "";
        this.label = label;
    }

    public FkOverflowableValueField showLabelOnTop() {
        this.showLabelOnTop = true;
        return this;
    }

    public FkOverflowableValueField keepNewLines() {
        this.keepNewlines = true;
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

        String textValue = value;
        // Fo some fields, we may wich to keep newline characters, such as an "Övrigt" field that has contributions from
        // multiple sources, ie. other fields contents are added to this field when signing.
        if (!keepNewlines) {
            textValue = trimNewLines(textValue);
        }
        // If were showing the label above, adjust the area available to the actual content.
        if (showLabelOnTop) {
            float labelX = Utilities.millimetersToPoints(x) + INDENTATION_LEFT;
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
        int charsToWrite = findFittingLength(canvas, targetRect, textValue, "");

        if (charsToWrite < textValue.length()) {
            // Entire text did NOT fit - find how much of original value that WILL fit (including the
            // "to-be-continued" text)
            charsToWrite = findFittingLength(canvas, targetRect, textValue, SEE_APPENDIX_PAGE_TEXT);

            if (charsToWrite > 0) {
                // Write the text that fits..
                writeText(canvas, targetRect, textValue.substring(0, charsToWrite), SEE_APPENDIX_PAGE_TEXT, false);
                // Save overflowing text for later. Since we break on a whitespace, trim it.
                overflowingText = textValue.substring(charsToWrite).trim();
            }

        } else {
            // NO overflow detected - write entire text for real this time - with now suffix text added.
            writeText(canvas, targetRect, textValue, "", false);
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

        p.setLeading(valueFont.getSize() * LEADING_FACTOR);
        p.setIndentationLeft(INDENTATION_LEFT);
        p.setIndentationRight(INDENTATION_RIGHT);

        ct.addElement(p);

        return ct.go(simulate);

    }

}
