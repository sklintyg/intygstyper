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
package se.inera.intyg.intygstyper.luse.pdf.common.model;

import java.util.ArrayList;
import java.util.List;

import com.itextpdf.text.pdf.PdfPageEventHelper;

/**
 * Defines the pages and Page events that constitutes a filled out (FK SIT type) FDF ready to be rendered.
 *
 * Created by marced on 30/09/16.
 */
public class FkPdfDefinition {

    private List<FkPage> pages = new ArrayList<>();
    private List<PdfPageEventHelper> pageEvents = new ArrayList<>();

    public List<FkPage> getPages() {
        return pages;
    }

    public void setPages(List<FkPage> pages) {
        this.pages = pages;
    }

    public void addPage(FkPage page) {
        this.pages.add(page);
    }

    public List<PdfPageEventHelper> getPageEvents() {
        return pageEvents;
    }

    public void setPageEvents(List<PdfPageEventHelper> pageEvents) {
        this.pageEvents = pageEvents;
    }

    public void addPageEvent(PdfPageEventHelper pageEvent) {
        this.pageEvents.add(pageEvent);
    }
}
