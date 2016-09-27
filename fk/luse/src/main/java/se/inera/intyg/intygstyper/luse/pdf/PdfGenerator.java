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
package se.inera.intyg.intygstyper.luse.pdf;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.Utilities;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

import se.inera.intyg.common.support.model.Status;
import se.inera.intyg.common.support.modules.support.ApplicationOrigin;
import se.inera.intyg.intygstyper.luse.model.internal.LuseUtlatande;
import se.inera.intyg.intygstyper.luse.pdf.fields.FkCheckbox;
import se.inera.intyg.intygstyper.luse.pdf.fields.FkField;
import se.inera.intyg.intygstyper.luse.pdf.fields.FkValueField;

/**
 * Created by marced on 18/08/16.
 */
// CHECKSTYLE:OFF MagicNumber
public class PdfGenerator {
    private static final String FK_LOGO_PATH = "classpath:pdf-assets/rehab_pdf_logo.png";

    private static final String UNICODE_CAPABLE_FONT_PATH = "/pdf-assets/FreeSans.ttf";
    private static final float QUESTION_MARGIN_LEFT_MM = 15.5f;

    private static final float QUESTION_RECT_WIDTH = 180f;
    private static final float QUESTION_RECT_BORDER_WIDTH = Utilities.millimetersToPoints(0.3f);
    private static final float QUESTION_RUBRIK_PADDING_LEFT = Utilities.millimetersToPoints(0.0f);

    protected LuseUtlatande intyg;

    private PathMatchingResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

    private Font unicodeCapableFont;

    private Document document;
    private PdfWriter writer;
    private PdfContentByte canvas;

    public byte[] generatePdf(LuseUtlatande intyg, List<Status> statuses, ApplicationOrigin applicationOrigin) throws PdfGeneratorException {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try {
            unicodeCapableFont = new Font(BaseFont.createFont(UNICODE_CAPABLE_FONT_PATH, BaseFont.IDENTITY_H, BaseFont.EMBEDDED), 9, Font.NORMAL);

            document = new Document();
            document.setPageSize(PageSize.A4);
            document.setMargins(Utilities.millimetersToPoints(22), Utilities.millimetersToPoints(0), Utilities.millimetersToPoints(0),
                    Utilities.millimetersToPoints(0));

            writer = PdfWriter.getInstance(document, bos);

            // Add handlers for page events
            writer.setPageEvent(new PageNumberingEventHandler());
            writer.setPageEvent(new FkFormIdentityEventHandler("FK 7800 (001 F 001) Fastställd av Försäkringskassan", "7800", "01"));

            document.open();

            canvas = writer.getDirectContent();

            // Add the front page with meta info
            createPage1();
            document.newPage();
            createPage1();

            // Finish off by closing the document (will invoke the event handlers)
            document.close();

        } catch (DocumentException | IOException | RuntimeException e) {
            throw new PdfGeneratorException("Failed to create PDF", e);
        }

        return bos.toByteArray();
    }

    private void createPage1() throws IOException, DocumentException {

        float yStart = 150f;
        createQuestionBox("1. Utlåtandet är baserat på", yStart, 51.5f);

        FkCheckbox cbUndersokningAvPat = new FkCheckbox("1.23", "min Undersökning av patienten", 65f, 9f, false);
        FkValueField dlUndersokningAvPat = new FkValueField("1.23.1", 64f, 9f, "2016-12-09").withTopLabel("datum (Är månad , dag)");

        FkCheckbox cbJournalUppgifter = new FkCheckbox("1.24", "journaluppgifter från den", 65f, 9f, true);
        FkValueField dlJournalUppgifter = new FkValueField("1.24.2", 64f, 9f, "2016-12-09");

        FkCheckbox cbAnhorigBeskrivning = new FkCheckbox("1.25", "anhörig/annas beskrivning av patienten", 65f, 9f, true);
        FkValueField dlAnhorigBeskrivning = new FkValueField("1.25.1", 64f, 9f, "2015-12-09");

        FkCheckbox cbAnnat = new FkCheckbox("1.26", "annat", 65f, 9f, true);
        FkValueField dlAnnat = new FkValueField("1.26.1", 64f, 9f, "2015-12-09");

        FkCheckbox cbAnnatBeskrivning = new FkCheckbox("1.26", "Ange vad annat är:", 65f, 9f);
        FkValueField dlAnnatBeskrivning = new FkValueField("1.26.1", 64f, 9f,
                "Hej och hå vad långa texter det blir  sf sfsd");

        yStart = renderFieldRow(Arrays.asList(cbUndersokningAvPat, dlUndersokningAvPat), yStart);
        yStart = renderFieldRow(Arrays.asList(cbJournalUppgifter, dlJournalUppgifter), yStart);
        yStart = renderFieldRow(Arrays.asList(cbAnhorigBeskrivning, dlAnhorigBeskrivning), yStart);
        yStart = renderFieldRow(Arrays.asList(cbAnnat, dlAnnat), yStart);
        yStart = renderFieldRow(Arrays.asList(cbAnnatBeskrivning, dlAnnatBeskrivning), yStart);

        FkValueField kantPatSedan = new FkValueField("1.26.1", QUESTION_RECT_WIDTH - 72f, 7f, "2016-05-05")
                .withLabel("Jag har känt patienten sedan", 72f)
                .withTopBorder();
        yStart = renderFieldRow(Arrays.asList(kantPatSedan), yStart);

        yStart = 86f;
        createQuestionBox("2. Är utlåtandet även baserat på andra medicinska utredningar eller underlag?", yStart, 68f);
        FkCheckbox cb1 = new FkCheckbox("1.29.1", "Nej", 12.5f, 9f, false);
        FkCheckbox cb2 = new FkCheckbox("1.29.2", "Ja. Fyll i nedan", 80f, 9f, true);
        yStart = renderFieldRow(Arrays.asList(cb1, cb2), yStart);

        for (int i = 0; i < 3; i++) {
            FkValueField underlag1Typ = new FkValueField("1.26.1", 72f, 9f, "Underlag från fysioTeraperut" + i).withTopLabel("Ange utredning eller underlag")
                    .withTopBorder();
            FkValueField underlag1Datum = new FkValueField("1.26.1", 40f, 9f, "2016-05-05").withTopLabel("datum (år, månad, dag)").withTopBorder();
            FkCheckbox underlag1BifogasJa = new FkCheckbox("1.26", "Ja", 20f, 9f, true).withPrefixLabel("Bifogas", 15f).withTopBorder();
            FkCheckbox underlag1BifogasNej = new FkCheckbox("1.26", "Nej", 20f, 9f, false).withTopBorder();

            yStart = renderFieldRow(Arrays.asList(underlag1Typ, underlag1Datum, underlag1BifogasJa, underlag1BifogasNej), yStart);

        }

    }

    private float renderFieldRow(List<FkField> fkFields, float mmTopY) throws DocumentException {
        Rectangle lastRender = new Rectangle(QUESTION_MARGIN_LEFT_MM, mmTopY);

        for (FkField field : fkFields) {
            lastRender = field.render(canvas, lastRender.getRight(), lastRender.getTop());

        }
        // Return new yOffset for next row rencering call
        return lastRender.getBottom();
    }

    private void createCheckboxLabel(Rectangle checkboxRect, String checkboxLabel) {
        /*
         * ----
         * | X | Label
         * ----
         */

        /*
         * float padding = (checkboxRect.getHeight() - PdfConstants.FONT_INLINE_FIELD_LABEL.getSize()) / 2f;
         * ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, new Phrase(checkboxLabel,
         * PdfConstants.FONT_INLINE_FIELD_LABEL),
         * checkboxRect.getRight() + CHECKBOX_LABEL_PADDING_LEFT, checkboxRect.getBottom() + padding * 2, 0);
         */
    }

    private void createQuestionBox(String frageId, float yStart, float yHeight) {
        ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, new Phrase(frageId, PdfConstants.FONT_FRAGERUBRIK),
                Utilities.millimetersToPoints(QUESTION_MARGIN_LEFT_MM + QUESTION_RUBRIK_PADDING_LEFT), Utilities.millimetersToPoints(yStart + 1.0f), 0);
        Rectangle rect = new Rectangle(Utilities.millimetersToPoints(QUESTION_MARGIN_LEFT_MM), Utilities.millimetersToPoints(yStart - yHeight),
                Utilities.millimetersToPoints(QUESTION_MARGIN_LEFT_MM + QUESTION_RECT_WIDTH), Utilities.millimetersToPoints(yStart));
        rect.setBorder(Rectangle.BOX);
        rect.setBorderWidth(QUESTION_RECT_BORDER_WIDTH);
        rect.setBorderColor(BaseColor.BLACK);
        canvas.rectangle(rect);
    }

}
