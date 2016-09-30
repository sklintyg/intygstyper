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
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.Utilities;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfWriter;

import se.inera.intyg.common.support.model.Status;
import se.inera.intyg.common.support.modules.support.ApplicationOrigin;
import se.inera.intyg.intygstyper.luse.model.internal.LuseUtlatande;
import se.inera.intyg.intygstyper.luse.pdf.model.FkCheckbox;
import se.inera.intyg.intygstyper.luse.pdf.model.FkDiagnosKodField;
import se.inera.intyg.intygstyper.luse.pdf.model.FkLabel;
import se.inera.intyg.intygstyper.luse.pdf.model.FkCategory;
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
    public static final float CHECKBOX_UNDER_TOPLABEL_PADDING = 1.5f;

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
            createPage2();

            // Finish off by closing the document (will invoke the event handlers)
            document.close();

        } catch (DocumentException | IOException | RuntimeException e) {
            throw new PdfGeneratorException("Failed to create PDF", e);
        }

        return bos.toByteArray();
    }

    private void createPage2() throws IOException, DocumentException {
        List<PdfComponent> allElements = new ArrayList<>();
        FkCategory fraga3 = new FkCategory("3. Diagnos/diagnoser för sjukdom som orsakar nedsatt arbetsförmåga").offset(15f, 23f).size(180f, 76f)
                .withBorders(Rectangle.BOX);
        FkValueField diagnos1 = new FkValueField("Detta är diagnos 1, kan vara så mkt text att det blir på två rader och det är ok. sen skall det klippas eller?")
                .size(140f, 11f)
                .withValueFont(PdfConstants.FONT_NORMAL_10)
                .withValueTextAlignment(PdfPCell.ALIGN_TOP)
                .withBorders(Rectangle.BOX);

        FkDiagnosKodField diagnosKodField1 = new FkDiagnosKodField("J22")
                .withTopLabel("Diagnoskod enligt ICD-10 SE")
                .size(40f, 11f)
                .offset(140f, 0f)
                .withBorders(Rectangle.BOTTOM);
        fraga3.addChild(diagnos1);
        fraga3.addChild(diagnosKodField1);

        FkValueField diagnos2 = new FkValueField("Detta är diagnos 2")
                .size(140f, 9f)
                .offset(0f, 11f)
                .withValueFont(PdfConstants.FONT_NORMAL_10)
                .withValueTextAlignment(PdfPCell.ALIGN_TOP)
                .withBorders(Rectangle.BOX);
        FkDiagnosKodField diagnosKodField2 = new FkDiagnosKodField("K33")
                .size(40f, 9f)
                .offset(140f, 11f)
                .withBorders(Rectangle.BOTTOM);
        fraga3.addChild(diagnos2);
        fraga3.addChild(diagnosKodField2);

        FkValueField diagnos3 = new FkValueField("Detta är diagnos 3")
                .size(140f, 9f)
                .offset(0f, 20f)
                .withBorders(Rectangle.BOX)
                .withValueTextAlignment(PdfPCell.ALIGN_TOP)
                .withValueFont(PdfConstants.FONT_NORMAL_10);
        FkDiagnosKodField diagnosKodField3 = new FkDiagnosKodField("A19")
                .size(40f, 9f)
                .offset(140f, 20f)
                .withBorders(Rectangle.BOTTOM);
        fraga3.addChild(diagnos3);
        fraga3.addChild(diagnosKodField3);

        FkValueField narOchHur = new FkValueField("Diagnosen ställdes ståendes.")
                .size(180f, 27f)
                .offset(0f, 29f)
                .withBorders(Rectangle.BOTTOM)
                .withValueFont(PdfConstants.FONT_NORMAL_10)
                .withValueTextAlignment(PdfPCell.ALIGN_TOP)
                .withTopLabel("När och var ställdes diagnosen/diagnoserna?");
        fraga3.addChild(narOchHur);

        FkValueField revidera = new FkValueField("")
                .size(180f, 11f)
                .offset(0f, 56f)
                .withBorders(Rectangle.BOTTOM).withValueFont(PdfConstants.FONT_NORMAL_10)
                .withTopLabel("Finns skäll till att revidera/uppdatera tidigare satt diagnos?");

        FkCheckbox noCheckbox = new FkCheckbox("Nej", false)
                .size(24.5f, 11f)
                .offset(0f, CHECKBOX_UNDER_TOPLABEL_PADDING)
                .withBorders(Rectangle.NO_BORDER);
        FkCheckbox yesCheckbox = new FkCheckbox("Ja. Fyll i nedan.", true)
                .size(84.5f, 11f)
                .offset(22f, CHECKBOX_UNDER_TOPLABEL_PADDING)
                .withBorders(Rectangle.NO_BORDER);

        revidera.getChildren().add(noCheckbox);
        revidera.getChildren().add(yesCheckbox);

        fraga3.addChild(revidera);

        FkValueField vilkenAvses = new FkValueField("Den allra senaste diagnosen avses!")
                .size(180f, 9f)
                .offset(0f, 67f)
                .withValueFont(PdfConstants.FONT_NORMAL_10)
                .withTopLabel("Beskriv vilken eller vilka diagnoser som avses");
        fraga3.addChild(vilkenAvses);

        allElements.add(fraga3);

        // Start rendering a page at upper left corner
        doRendering(allElements, 0f, Utilities.pointsToMillimeters(document.getPageSize().getTop()));

    }

    private void createPage1() throws IOException, DocumentException {

        List<PdfComponent> allElements = new ArrayList<>();

        FkCategory fraga1 = new FkCategory("1. Utlåtandet är baserat på")
                .offset(15f, 140f)
                .size(180f, 55f)
                .withBorders(Rectangle.BOX);

        float ROW_HEIGHT = 9f;
        float CHECKBOX_DEFAULT_WIDTH = 65f;

        fraga1.addChild(new FkCheckbox("min Undersökning av patienten", false)
                .offset(0f, 0f)
                .size(CHECKBOX_DEFAULT_WIDTH, ROW_HEIGHT));

        fraga1.addChild(new FkValueField("2016-12-09")
                .offset(CHECKBOX_DEFAULT_WIDTH, 0f)
                .size(65f, ROW_HEIGHT)
                .withTopLabel("datum (Är månad , dag)"));

        fraga1.addChild(new FkCheckbox("journaluppgifter från den", true)
                .offset(0f, ROW_HEIGHT)
                .size(CHECKBOX_DEFAULT_WIDTH, ROW_HEIGHT));
        fraga1.addChild(new FkValueField("2016-12-09")
                .offset(65f, ROW_HEIGHT)
                .size(CHECKBOX_DEFAULT_WIDTH, 9f));

        fraga1.addChild(new FkCheckbox("anhörig/anaNas beskrivning av patienten", true)
                .offset(0f, ROW_HEIGHT * 2)
                .size(CHECKBOX_DEFAULT_WIDTH, ROW_HEIGHT));
        fraga1.addChild(new FkValueField("2016-12-09")
                .offset(CHECKBOX_DEFAULT_WIDTH, ROW_HEIGHT * 2)
                .size(CHECKBOX_DEFAULT_WIDTH, 9f));

        fraga1.addChild(new FkCheckbox("annat", true)
                .offset(0f, ROW_HEIGHT * 3)
                .size(CHECKBOX_DEFAULT_WIDTH, ROW_HEIGHT));
        fraga1.addChild(new FkValueField("2015-12-09")
                .offset(CHECKBOX_DEFAULT_WIDTH, ROW_HEIGHT * 3)
                .size(65f, 9f));

        fraga1.addChild(new FkLabel("Ange vad annat är:")
                .offset(7f, ROW_HEIGHT * 4)
                .size(58f, ROW_HEIGHT)
                .withAlignment(Element.ALIGN_BOTTOM));
        fraga1.addChild(new FkValueField("Hej och hå vad långa texter det blir  sf sfsd")
                .offset(65f, ROW_HEIGHT * 4)
                .size(65f, ROW_HEIGHT));

        // Dessa fält har top border
        fraga1.addChild(new FkLabel("Jag har känt patienten sedan")
                .offset(0f, ROW_HEIGHT * 5)
                .size(65f, ROW_HEIGHT)
                .withBorders(Rectangle.TOP)
                .withAlignment(Element.ALIGN_BOTTOM));
        fraga1.addChild(new FkValueField("2016-09-22")
                .offset(65f, ROW_HEIGHT * 5)
                .size(115f, ROW_HEIGHT)
                .withBorders(Rectangle.TOP));

        allElements.add(fraga1);

        FkCategory fraga2 = new FkCategory("2. Är utlåtandet även baserat på andra medicinska utredningar eller underlag?")
                .offset(15f, 210f)
                .size(180f, 70f)
                .withBorders(Rectangle.BOX);
        fraga2.addChild(new FkCheckbox("Nej", true)
                .offset(0, 0)
                .size(20.5f, ROW_HEIGHT));
        fraga2.addChild(new FkCheckbox("Ja, fyll i nedan.", true)
                .offset(21.5f, 0)
                .size(40f, ROW_HEIGHT));

        // This is just so that we dont have to enter all y offsets manually
        float yOffset;
        int row = 1;
        for (int i = 0; i < 3; i++) {
            yOffset = (ROW_HEIGHT * row);
            fraga2.addChild(new FkValueField("Underlag från fysioTeraperut" + i)
                    .offset(0, yOffset)
                    .size(80, ROW_HEIGHT)
                    .withBorders(Rectangle.TOP)
                    .withTopLabel("Ange utredning eller underlag"));

            fraga2.addChild(new FkValueField("2016-05-05")
                    .offset(80, yOffset)
                    .size(40f, ROW_HEIGHT)
                    .withBorders(Rectangle.TOP)
                    .withTopLabel("datum (år, månad, dag"));

            fraga2.addChild(new FkLabel("Bifogas")
                    .offset(120f, yOffset)
                    .size(15f, ROW_HEIGHT)
                    .withBorders(Rectangle.TOP));
            fraga2.addChild(new FkCheckbox("Ja", true)
                    .offset(135f, yOffset)
                    .size(30f, ROW_HEIGHT)
                    .withBorders(Rectangle.TOP));
            fraga2.addChild(new FkCheckbox("Nej", false)
                    .offset(155f, yOffset)
                    .size(25f, ROW_HEIGHT)
                    .withBorders(Rectangle.TOP));
            row++;
            yOffset = (ROW_HEIGHT * row);
            fraga2.addChild(new FkValueField("Hämtas från " + i)
                    .offset(0, yOffset)
                    .size(180f, ROW_HEIGHT)
                    .withBorders(Rectangle.TOP)
                    .withTopLabel("Från vilken vårdgivare kan Försäkringskassa hämta information om utRedningen/underlaget?"));
            row++;

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
