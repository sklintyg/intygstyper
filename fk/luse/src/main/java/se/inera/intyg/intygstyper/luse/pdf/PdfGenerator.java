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
import java.util.ArrayList;
import java.util.List;

import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.Utilities;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

import se.inera.intyg.common.support.model.Status;
import se.inera.intyg.common.support.modules.support.ApplicationOrigin;
import se.inera.intyg.intygstyper.luse.model.internal.LuseUtlatande;
import se.inera.intyg.intygstyper.luse.pdf.model.FkCheckbox;
import se.inera.intyg.intygstyper.luse.pdf.model.FkLabel;
import se.inera.intyg.intygstyper.luse.pdf.model.FkQuestion;
import se.inera.intyg.intygstyper.luse.pdf.model.FkValueField;
import se.inera.intyg.intygstyper.luse.pdf.model.PdfComponent;

/**
 * Created by marced on 18/08/16.
 */
// CHECKSTYLE:OFF MagicNumber
public class PdfGenerator {
    private static final String FK_LOGO_PATH = "classpath:pdf-assets/rehab_pdf_logo.png";

    private static final String UNICODE_CAPABLE_FONT_PATH = "/pdf-assets/FreeSans.ttf";
    private static final float QUESTION_MARGIN_LEFT_MM = 15.5f;

    private static final float QUESTION_RECT_WIDTH = 180f;

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

        List<PdfComponent> allElements = new ArrayList<>();

        PdfComponent fraga1 = new FkQuestion("1. Utlåtandet är baserat på").offset(15f, 50f).size(180f, 55f).withBorders(Rectangle.BOX);

        float ROW_HEIGHT = 9f;
        float CHECKBOX_DEFAULT_WIDTH = 65f;

        fraga1.addChild(new FkCheckbox("min Undersökning av patienten", false).offset(0f, 0f).size(CHECKBOX_DEFAULT_WIDTH, ROW_HEIGHT));

        PdfComponent dlUndersokningAvPat = new FkValueField("2016-12-09").offset(CHECKBOX_DEFAULT_WIDTH, 0f).size(65f, ROW_HEIGHT);
        // TODO: withTopLabel is currently not defined in superclass since it only applies to this subtype and it messes with the
        // builder pattern. What to do?
        ((FkValueField) dlUndersokningAvPat).withTopLabel("datum (Är månad , dag)");
        fraga1.addChild(dlUndersokningAvPat);

        fraga1.addChild(new FkCheckbox("journaluppgifter från den", true).offset(0f, ROW_HEIGHT).size(CHECKBOX_DEFAULT_WIDTH, ROW_HEIGHT));
        fraga1.addChild(new FkValueField("2016-12-09").offset(65f, ROW_HEIGHT).size(CHECKBOX_DEFAULT_WIDTH, 9f));

        fraga1.addChild(new FkCheckbox("anhörig/anaNas beskrivning av patienten", true).offset(0f, ROW_HEIGHT * 2).size(CHECKBOX_DEFAULT_WIDTH, ROW_HEIGHT));
        fraga1.addChild(new FkValueField("2016-12-09").offset(CHECKBOX_DEFAULT_WIDTH, ROW_HEIGHT * 2).size(CHECKBOX_DEFAULT_WIDTH, 9f));

        fraga1.addChild(new FkCheckbox("annat", true).offset(0f, ROW_HEIGHT * 3).size(CHECKBOX_DEFAULT_WIDTH, ROW_HEIGHT));
        fraga1.addChild(new FkValueField("2015-12-09").offset(CHECKBOX_DEFAULT_WIDTH, ROW_HEIGHT * 3).size(65f, 9f));

        fraga1.addChild(new FkLabel("Ange vad annat är:").offset(7f, ROW_HEIGHT * 4).size(58f, ROW_HEIGHT));
        fraga1.addChild(new FkValueField("Hej och hå vad långa texter det blir  sf sfsd").offset(65f, ROW_HEIGHT * 4).size(65f, ROW_HEIGHT));

        // Dessa fält har top border
        fraga1.addChild(new FkLabel("Jag har känt patienten sedan").offset(0f, ROW_HEIGHT * 5).size(65f, ROW_HEIGHT).withBorders(Rectangle.TOP));
        fraga1.addChild(new FkValueField("2016-09-22").offset(65f, ROW_HEIGHT * 5).size(115f, ROW_HEIGHT).withBorders(Rectangle.TOP));

        allElements.add(fraga1);

        PdfComponent fraga2 = new FkQuestion("2. Är utlåtandet även baserat på andra medicinska utredningar eller underlag?").offset(15f, 150f)
                .size(180f, 55f).withBorders(Rectangle.BOX);
        fraga2.addChild(new FkCheckbox("Nej", true).offset(0, 0).size(20.5f, ROW_HEIGHT));
        fraga2.addChild(new FkCheckbox("Ja, fyll i nedan.", true).offset(21.5f, 0).size(40f, ROW_HEIGHT));

        // This is just so that we dont have to enter all y offsets manually
        float yOffset = ROW_HEIGHT;
        for (int i = 0; i < 3; i++) {
            yOffset += ROW_HEIGHT * i;
            PdfComponent<FkValueField> underlag = new FkValueField("Underlag från fysioTeraperut" + i).offset(0, yOffset).size(80, ROW_HEIGHT).withBorders(Rectangle.TOP);
            // TODO: same type erasure problem here.. maybe just move withToplabel to superclass?
            ((FkValueField) underlag).withTopLabel("Ange utredning eller underlag");
            fraga2.addChild(underlag);

            PdfComponent underlagDatum = new FkValueField("2016-05-05").offset(80, yOffset).size(40f, ROW_HEIGHT).withBorders(Rectangle.TOP);
            ((FkValueField) underlagDatum).withTopLabel("datum (år, månad, dag");
            fraga2.addChild(underlagDatum);

            fraga2.addChild(new FkLabel("Bifogas").offset(120f, yOffset).size(15f, ROW_HEIGHT).withBorders(Rectangle.TOP));
            fraga2.addChild(new FkCheckbox("Ja", true).offset(135f, yOffset).size(30f, ROW_HEIGHT).withBorders(Rectangle.TOP));
            fraga2.addChild(new FkCheckbox("Nej", false).offset(155f, yOffset).size(20f, ROW_HEIGHT).withBorders(Rectangle.TOP));

            yOffset += ROW_HEIGHT;
            PdfComponent underlagHamtas = new FkValueField("Hämtas från " + i).offset(0, yOffset).size(180f, ROW_HEIGHT).withBorders(Rectangle.TOP);
            ((FkValueField) underlagHamtas).withTopLabel("Från vilken vårdgivare kan Försäkringskassa hämta information om utRedningen/underlaget?");
            fraga2.addChild(underlagHamtas);
        }
        allElements.add(fraga2);

        // Start rendering a page at upper left corner
        doRendering(allElements, 0f, Utilities.pointsToMillimeters(document.getPageSize().getTop()));

    }

    private void doRendering(List<PdfComponent> pdfRenderables, float x, float y) throws DocumentException {

        for (PdfComponent field : pdfRenderables) {
            field.render(canvas, x + field.getParentOffsetX(), y - field.getParentOffsetY());

        }

    }

}
