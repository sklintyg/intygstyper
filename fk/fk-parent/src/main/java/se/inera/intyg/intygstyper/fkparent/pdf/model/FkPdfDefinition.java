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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.itextpdf.text.Utilities;
import com.itextpdf.text.pdf.PdfPageEventHelper;

/**
 * Defines the root element of a PdfComponent object tree hierarchy.
 * Besides being a root element, it also holds the specified pageEvents that should be used when rendering itself.
 *
 * Created by marced on 30/09/16.
 */
public class FkPdfDefinition extends PdfComponent<FkPdfDefinition> {

    // Default page margins (left,right,top,bottom)
    private static final float[] DEFAULT_PAGE_MARGINS = new float[] {
            Utilities.millimetersToPoints(15f),
            Utilities.millimetersToPoints(15f),
            Utilities.millimetersToPoints(40f),
            Utilities.millimetersToPoints(10f)
    };

    private List<PdfPageEventHelper> pageEvents = new ArrayList<>();

    public List<PdfPageEventHelper> getPageEvents() {
        return pageEvents;
    }

    public void addPageEvent(PdfPageEventHelper pageEvent) {
        this.pageEvents.add(pageEvent);
    }

    public float[] getPageMargins() {
        return DEFAULT_PAGE_MARGINS;
    }

    /**
     * Inspects itself for all overflowing text components.
     *
     * @return
     *         List of all FkOverflowableValueFields that reported having overflowing text
     */
    public List<FkOverflowableValueField> collectOverflowingComponents() {

        // Flatten structure and filter for relevant PdfComponents
        final List<FkOverflowableValueField> overflowingList = this.flattened()
                .filter(FkOverflowableValueField.class::isInstance)
                .map(pdfComponent -> (FkOverflowableValueField) pdfComponent)
                .filter(candidate -> candidate.getOverFlowingText() != null)
                .collect(Collectors.toList());

        return overflowingList;
    }
}
