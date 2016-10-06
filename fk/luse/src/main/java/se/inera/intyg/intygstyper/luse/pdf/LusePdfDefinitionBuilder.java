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

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;

import se.inera.intyg.common.services.texts.model.IntygTexts;
import se.inera.intyg.common.support.model.InternalDate;
import se.inera.intyg.common.support.model.Status;
import se.inera.intyg.common.support.model.common.internal.Vardenhet;
import se.inera.intyg.common.support.modules.support.ApplicationOrigin;
import se.inera.intyg.intygstyper.fkparent.model.internal.Diagnos;
import se.inera.intyg.intygstyper.fkparent.model.internal.Tillaggsfraga;
import se.inera.intyg.intygstyper.fkparent.model.internal.Underlag;
import se.inera.intyg.intygstyper.luse.model.internal.LuseUtlatande;
import se.inera.intyg.intygstyper.luse.pdf.common.FkFormIdentityEventHandler;
import se.inera.intyg.intygstyper.luse.pdf.common.FkPersonnummerEventHandler;
import se.inera.intyg.intygstyper.luse.pdf.common.PageNumberingEventHandler;
import se.inera.intyg.intygstyper.luse.pdf.common.PdfConstants;
import se.inera.intyg.intygstyper.luse.pdf.common.PdfGeneratorException;
import se.inera.intyg.intygstyper.luse.pdf.common.model.FkCategory;
import se.inera.intyg.intygstyper.luse.pdf.common.model.FkCheckbox;
import se.inera.intyg.intygstyper.luse.pdf.common.model.FkDiagnosKodField;
import se.inera.intyg.intygstyper.luse.pdf.common.model.FkImage;
import se.inera.intyg.intygstyper.luse.pdf.common.model.FkLabel;
import se.inera.intyg.intygstyper.luse.pdf.common.model.FkPage;
import se.inera.intyg.intygstyper.luse.pdf.common.model.FkPdfDefinition;
import se.inera.intyg.intygstyper.luse.pdf.common.model.FkValueField;
import se.inera.intyg.intygstyper.luse.pdf.common.model.PdfComponent;

/**
 * Created by marced on 18/08/16.
 */
// CHECKSTYLE:OFF MagicNumber
public class LusePdfDefinitionBuilder {

    private static final float KATEGORI_FULL_WIDTH = 180f;
    private static final float KATEGORI_OFFSET_X = 15f;

    public static final float CHECKBOX_UNDER_TOPLABEL_PADDING = 1.5f; // TODO: fix so that this isnt needed
    private static final float FRAGA_5_DELFRAGA_HEIGHT = 25f;
    private static final float FRAGA_5_DELFRAGA_RUBRIK_HEIGHT = 9f;

    private static final float FRAGA_7_DELFRAGA_HEIGHT = 22f;
    private static final float FRAGA_7_DELFRAGA_RUBRIK_HEIGHT = 5f;

    private static final float FRAGA_8_DELFRAGA_HEIGHT = 27f;
    private static final float FRAGA_8_DELFRAGA_RUBRIK_HEIGHT = 6.5f;

    private static final float FRAGA_9_DELFRAGA_HEIGHT = 28f;
    private static final String DATE_PATTERN = "yyyy-MM-dd";

    private IntygTexts intygTexts;

    public FkPdfDefinition buildPdfDefinition(LuseUtlatande intyg, List<Status> statuses, ApplicationOrigin applicationOrigin, IntygTexts intygTexts)
            throws PdfGeneratorException {
        this.intygTexts = intygTexts;

        try {
            FkPdfDefinition def = new FkPdfDefinition();

            // Add page envent handlers
            def.addPageEvent(new PageNumberingEventHandler());
            // TODO: Should blankettId / formId be configurable properties?
            def.addPageEvent(new FkFormIdentityEventHandler("FK 7800 (001 F 001) Fastställd av Försäkringskassan", "7800", "01"));
            def.addPageEvent(new FkPersonnummerEventHandler(intyg.getGrundData().getPatient().getPersonId().getPersonnummer()));

            def.addPage(createPage1(intyg));
            def.addPage(createPage2(intyg));
            def.addPage(createPage3(intyg));
            def.addPage(createPage4(intyg));
            def.addPage(createPage5(intyg));

            return def;
        } catch (IOException | DocumentException e) {
            throw new PdfGeneratorException("Failed to create FkPdfDefinition", e);
        }

    }

    private FkPage createPage1(LuseUtlatande intyg) throws IOException, DocumentException {

        List<PdfComponent> allElements = new ArrayList<>();

        // Meta elements such as FK logotype, information text(s) etc.
        Resource resource = new ClassPathResource("images/forsakringskassans_logotyp.jpg");
        FkImage logo = new FkImage(IOUtils.toByteArray(resource.getInputStream()))
                .withLinearScale(0.253f)
                .offset(16f, 20f);
        allElements.add(logo);

        FkLabel fortsBladText = new FkLabel("Använd fortsättningsblad som finns i slutet av blanketten om utrymmet i fälten inte räcker till.")
                .offset(17.5f, 20.5f)
                .withVerticalAlignment(Element.ALIGN_TOP)
                .size(67f, 10f)
                .withLeading(.0f, 1.2f);
        allElements.add(fortsBladText);

        FkLabel inteKannerPatientenText = new FkLabel("Om du inte känner patienten ska hen styrka sin\nidentitet genom legitimation med foto (SOSFS 2005:29).")
                .offset(17.5f, 31.5f)
                .withVerticalAlignment(Element.ALIGN_TOP)
                .size(76f, 10f)
                .withLeading(.0f, 1.2f);
        allElements.add(inteKannerPatientenText);

        FkLabel mainHeader = new FkLabel("Läkarutlåtande")
                .offset(107.5f, 10f)
                .size(40, 12f)
                .withVerticalAlignment(Element.ALIGN_TOP)
                .withFont(PdfConstants.FONT_FRAGERUBRIK);
        FkLabel subHeader = new FkLabel("för sjukersättning")
                .offset(107.5f, 14f)
                .size(40, 15)
                .withVerticalAlignment(Element.ALIGN_TOP)
                .withFont(PdfConstants.FONT_BOLD_9);
        allElements.add(mainHeader);
        allElements.add(subHeader);

        FkLabel patientNamnLbl = new FkLabel("Patientens namn")
                .offset(107.5f, 18f)
                .size(62.5f, 15)
                .withVerticalAlignment(Element.ALIGN_TOP)
                .withFont(PdfConstants.FONT_STAMPER_LABEL);
        FkLabel patientPnrLbl = new FkLabel("Personnummer")
                .offset(170f, 18f)
                .size(35f, 15f)
                .withVerticalAlignment(Element.ALIGN_TOP)
                .withFont(PdfConstants.FONT_STAMPER_LABEL);
        allElements.add(patientNamnLbl);
        allElements.add(patientPnrLbl);

        FkLabel patientNamn = new FkLabel(intyg.getGrundData().getPatient().getFullstandigtNamn())
                .offset(107.5f, 22f)
                .size(62.5f, 15)
                .withVerticalAlignment(Element.ALIGN_TOP);
        FkLabel patientPnr = new FkLabel(intyg.getGrundData().getPatient().getPersonId().getPersonnummer())
                .offset(170f, 22f)
                .size(35f, 15f).withFont(PdfConstants.FONT_VALUE_TEXT)
                .withVerticalAlignment(Element.ALIGN_TOP);
        allElements.add(patientNamn);
        allElements.add(patientPnr);

        FkLabel skickaBlankettenTillLbl = new FkLabel("Skicka blanketten till")
                .offset(113.2f, 37.5f)
                .size(28f, 5f)
                .withVerticalAlignment(Element.ALIGN_TOP)
                .withFont(PdfConstants.FONT_STAMPER_LABEL);
        allElements.add(skickaBlankettenTillLbl);

        FkLabel inlasningsCentralRad1 = new FkLabel("Försäkringskassans inläsningscentral")
                .withVerticalAlignment(Element.ALIGN_TOP)
                .size(60f, 6f)
                .offset(113.2f, 42f);
        FkLabel inlasningsCentralRad2 = new FkLabel("839 88 Östersund")
                .withVerticalAlignment(Element.ALIGN_TOP)
                .size(60f, 6f)
                .offset(113.2f, 48.75f);

        allElements.add(inlasningsCentralRad1);
        allElements.add(inlasningsCentralRad2);

        // Ledtext: Vad är sjukersättning
        FkLabel luseDescriptonText = new FkLabel(getText("FRM_2.RBK"))
                .withLeading(0.0f, 1.2f)
                .withVerticalAlignment(Element.ALIGN_TOP)
                .offset(19.5f, 77.5f)
                .size(174f, 60f);

        allElements.add(luseDescriptonText);

        // START KATEGORI 1. (Utlåtandet är baserat på....)
        FkCategory fraga1 = new FkCategory("1. " + getText("FRG_1.RBK"))
                .offset(KATEGORI_OFFSET_X, 150f)
                .size(KATEGORI_FULL_WIDTH, 52f)
                .withBorders(Rectangle.BOX);

        float CHECKBOXROW_DEFAULT_HEIGHT = 9f;
        float CHECKBOX_DEFAULT_WIDTH = 72.2f;
        float cbYOffset = 2;

        // Special case of first topLabel
        fraga1.addChild(new FkValueField("")
                .offset(CHECKBOX_DEFAULT_WIDTH, 0f)
                .size(CHECKBOX_DEFAULT_WIDTH, 4f)
                .withTopLabel("datum (år, månad, dag)"));

        fraga1.addChild(new FkCheckbox(getText("KV_FKMU_0001.UNDERSOKNING.RBK"), intyg.getUndersokningAvPatienten() != null)
                .offset(0f, cbYOffset)
                .size(CHECKBOX_DEFAULT_WIDTH, CHECKBOXROW_DEFAULT_HEIGHT));
        fraga1.addChild(new FkValueField(nullSafeString(intyg.getUndersokningAvPatienten()))
                .offset(CHECKBOX_DEFAULT_WIDTH, cbYOffset)
                .size(CHECKBOX_DEFAULT_WIDTH, CHECKBOXROW_DEFAULT_HEIGHT)
                .withValueTextAlignment(PdfPCell.ALIGN_MIDDLE));

        cbYOffset += CHECKBOXROW_DEFAULT_HEIGHT;
        fraga1.addChild(new FkCheckbox(getText("KV_FKMU_0001.JOURNALUPPGIFTER.RBK"), intyg.getJournaluppgifter() != null)
                .offset(0f, cbYOffset)
                .size(CHECKBOX_DEFAULT_WIDTH, CHECKBOXROW_DEFAULT_HEIGHT));
        fraga1.addChild(new FkValueField(nullSafeString(intyg.getJournaluppgifter()))
                .offset(CHECKBOX_DEFAULT_WIDTH, cbYOffset)
                .size(CHECKBOX_DEFAULT_WIDTH, CHECKBOXROW_DEFAULT_HEIGHT)
                .withValueTextAlignment(PdfPCell.ALIGN_MIDDLE));

        cbYOffset += CHECKBOXROW_DEFAULT_HEIGHT;
        fraga1.addChild(new FkCheckbox(getText("KV_FKMU_0001.ANHORIG.RBK"), intyg.getAnhorigsBeskrivningAvPatienten() != null)
                .offset(0f, cbYOffset)
                .size(CHECKBOX_DEFAULT_WIDTH, CHECKBOXROW_DEFAULT_HEIGHT));
        fraga1.addChild(new FkValueField(nullSafeString(intyg.getAnhorigsBeskrivningAvPatienten()))
                .offset(CHECKBOX_DEFAULT_WIDTH, cbYOffset)
                .size(CHECKBOX_DEFAULT_WIDTH, CHECKBOXROW_DEFAULT_HEIGHT)
                .withValueTextAlignment(PdfPCell.ALIGN_MIDDLE));

        cbYOffset += CHECKBOXROW_DEFAULT_HEIGHT;
        fraga1.addChild(new FkCheckbox(getText("KV_FKMU_0001.ANNAT.RBK"), intyg.getAnnatGrundForMU() != null)
                .offset(0f, cbYOffset)
                .size(CHECKBOX_DEFAULT_WIDTH, CHECKBOXROW_DEFAULT_HEIGHT));
        fraga1.addChild(new FkValueField(nullSafeString(intyg.getAnnatGrundForMU()))
                .offset(CHECKBOX_DEFAULT_WIDTH, cbYOffset)
                .size(CHECKBOX_DEFAULT_WIDTH, CHECKBOXROW_DEFAULT_HEIGHT)

                .withValueTextAlignment(PdfPCell.ALIGN_MIDDLE));

        fraga1.addChild(new FkLabel(getText("DFR_1.3.RBK"))
                .offset(7f, 36f)
                .size(58f, 9f)
                .withVerticalAlignment(Element.ALIGN_MIDDLE));
        fraga1.addChild(new FkValueField(nullSafeString(intyg.getAnnatGrundForMUBeskrivning()))
                .offset(CHECKBOX_DEFAULT_WIDTH, 36f)
                .size(CHECKBOX_DEFAULT_WIDTH, 9f)
                .withValueFont(PdfConstants.FONT_INLINE_FIELD_LABEL)
                .withValueTextAlignment(Element.ALIGN_MIDDLE));

        // Dessa fält har top border
        // FRG_2.RBK
        fraga1.addChild(new FkLabel(getText("FRG_2.RBK"))
                .offset(0f, 42.8f)
                .size(CHECKBOX_DEFAULT_WIDTH, CHECKBOXROW_DEFAULT_HEIGHT)
                .withBorders(Rectangle.TOP)
                .withVerticalAlignment(Element.ALIGN_BOTTOM));
        fraga1.addChild(new FkValueField(nullSafeString(intyg.getKannedomOmPatient()))
                .offset(CHECKBOX_DEFAULT_WIDTH, 42.8f)
                .size(107.8f, CHECKBOXROW_DEFAULT_HEIGHT)
                .withBorders(Rectangle.TOP));

        allElements.add(fraga1);

        FkCategory fraga2 = new FkCategory("2. " + getText("FRG_3.RBK"))
                .offset(KATEGORI_OFFSET_X, 215f)
                .size(KATEGORI_FULL_WIDTH, 63f)
                .withBorders(Rectangle.BOX);
        fraga2.addChild(new FkCheckbox("Nej", intyg.getUnderlagFinns() != null && intyg.getUnderlagFinns() == false)
                .offset(0, 0)
                .withCellWith(6f)
                .size(20.5f, CHECKBOXROW_DEFAULT_HEIGHT));
        fraga2.addChild(new FkCheckbox("Ja, fyll i nedan.", safeBoolean(intyg.getUnderlagFinns()))
                .offset(21.5f, 0)
                .size(40f, CHECKBOXROW_DEFAULT_HEIGHT));

        // This is just so that we dont have to enter all y offsets manually
        float yOffset;
        int row = 1;

        // Need to always iterate 3 times even if just 1 underlag exists (to draw the empty form parts)
        for (int i = 0; i < 3; i++) {
            String label = "";
            String datum = "";
            String hamtasFran = null;

            if (i < intyg.getUnderlag().size()) {
                Underlag underlag = intyg.getUnderlag().get(i);
                label = underlag.getTyp().getLabel();
                datum = underlag.getDatum().getDate();
                hamtasFran = underlag.getHamtasFran();
            }

            yOffset = (CHECKBOXROW_DEFAULT_HEIGHT * row);
            // Ange utredning eller underlag
            fraga2.addChild(new FkValueField(label)
                    .offset(0, yOffset)
                    .size(72.3f, CHECKBOXROW_DEFAULT_HEIGHT)
                    .withBorders(Rectangle.TOP)
                    .withTopLabel(getText("FRG_4.RBK")));

            fraga2.addChild(new FkValueField(datum)
                    .offset(72.3f, yOffset)
                    .size(50f, CHECKBOXROW_DEFAULT_HEIGHT)
                    .withBorders(Rectangle.TOP)
                    .withTopLabel("datum (år, månad, dag"));

            fraga2.addChild(new FkLabel("Bifogas")
                    .offset(120f, yOffset)
                    .size(15f, CHECKBOXROW_DEFAULT_HEIGHT)
                    .withBorders(Rectangle.TOP));
            fraga2.addChild(new FkCheckbox("Ja", hamtasFran != null)
                    .offset(132.5f, yOffset)
                    .size(30f, CHECKBOXROW_DEFAULT_HEIGHT)
                    .withBorders(Rectangle.TOP));
            fraga2.addChild(new FkCheckbox("Nej", hamtasFran == null)
                    .offset(155f, yOffset)
                    .size(25f, CHECKBOXROW_DEFAULT_HEIGHT)
                    .withBorders(Rectangle.TOP));
            row++;
            yOffset = (CHECKBOXROW_DEFAULT_HEIGHT * row);

            // Från vilken vårdgivare kan Försäkringskassan hämta information om utredningen/underlaget?
            fraga2.addChild(new FkValueField(nullSafeString(hamtasFran))
                    .offset(0, yOffset)
                    .size(KATEGORI_FULL_WIDTH, CHECKBOXROW_DEFAULT_HEIGHT)
                    .withBorders(Rectangle.TOP)
                    .withTopLabel(getText("DFR_4.3.RBK")));
            row++;
        }
        allElements.add(fraga2);

        FkPage thisPage = new FkPage();
        thisPage.getChildren().addAll(allElements);
        return thisPage;

    }

    private FkPage createPage2(LuseUtlatande intyg) throws IOException, DocumentException {

        List<PdfComponent> allElements = new ArrayList<>();

        // Diagnos/diagnoser för sjukdom som orsakar nedsatt arbetsförmåga
        // --------------------------------------------------------------------------
        FkCategory fraga3 = new FkCategory("3. " + getText("FRG_6.RBK"))
                .offset(KATEGORI_OFFSET_X, 24f)
                .size(KATEGORI_FULL_WIDTH, 76.2f)
                .withBorders(Rectangle.BOX);
        Diagnos currentDiagnos = safeGetDiagnos(intyg, 0);
        FkValueField diagnos1 = new FkValueField(currentDiagnos.getDiagnosBeskrivning())
                .size(140f, 11f)
                .withTopPadding(2f)
                .withValueTextAlignment(PdfPCell.ALIGN_TOP)
                .withBorders(Rectangle.BOX);

        // Diagnoskod enligt ICD-10 SE"
        FkDiagnosKodField diagnosKodField1 = new FkDiagnosKodField(currentDiagnos.getDiagnosKod())
                .withTopLabel(getText("DFR_6.2.RBK"))
                .size(40f, 11f)
                .offset(140f, 0f)
                .withBorders(Rectangle.BOTTOM);
        fraga3.addChild(diagnos1);
        fraga3.addChild(diagnosKodField1);

        currentDiagnos = safeGetDiagnos(intyg, 1);
        FkValueField diagnos2 = new FkValueField(currentDiagnos.getDiagnosBeskrivning())
                .size(140f, 9f)
                .offset(0f, 11f)
                .withValueTextAlignment(PdfPCell.ALIGN_TOP)
                .withBorders(Rectangle.BOX);
        FkDiagnosKodField diagnosKodField2 = new FkDiagnosKodField(currentDiagnos.getDiagnosKod())
                .size(40f, 9f)
                .offset(140f, 11f)
                .withBorders(Rectangle.BOTTOM);
        fraga3.addChild(diagnos2);
        fraga3.addChild(diagnosKodField2);

        currentDiagnos = safeGetDiagnos(intyg, 2);
        FkValueField diagnos3 = new FkValueField(currentDiagnos.getDiagnosBeskrivning())
                .size(140f, 9f)
                .offset(0f, 20f)
                .withBorders(Rectangle.BOX)
                .withValueTextAlignment(PdfPCell.ALIGN_TOP);
        FkDiagnosKodField diagnosKodField3 = new FkDiagnosKodField(currentDiagnos.getDiagnosKod())
                .size(40f, 9f)
                .offset(140f, 20f)
                .withBorders(Rectangle.BOTTOM);
        fraga3.addChild(diagnos3);
        fraga3.addChild(diagnosKodField3);

        // När och var ställdes diagnosen/diagnoserna?"
        FkValueField narOchHur = new FkValueField(intyg.getDiagnosgrund())
                .size(KATEGORI_FULL_WIDTH, 27f)
                .offset(0f, 29f)
                .withBorders(Rectangle.BOTTOM)
                .withValueTextAlignment(PdfPCell.ALIGN_TOP)
                .withTopLabel(getText("FRG_7.RBK"));
        fraga3.addChild(narOchHur);

        // Finns skäl till att revidera/uppdatera tidigare satt diagnos?
        FkValueField revidera = new FkValueField("")
                .size(KATEGORI_FULL_WIDTH, 11f)
                .offset(0f, 56f)
                .withBorders(Rectangle.BOTTOM)
                .withTopLabel(getText("FRG_45.RBK"));

        FkCheckbox noCheckbox = new FkCheckbox("Nej", intyg.getNyBedomningDiagnosgrund() != null && !intyg.getNyBedomningDiagnosgrund())
                .size(24.5f, 11f)
                .offset(0f, CHECKBOX_UNDER_TOPLABEL_PADDING)
                .withBorders(Rectangle.NO_BORDER);
        FkCheckbox yesCheckbox = new FkCheckbox("Ja. Fyll i nedan.", safeBoolean(intyg.getNyBedomningDiagnosgrund()))
                .size(84.5f, 11f)
                .offset(22f, CHECKBOX_UNDER_TOPLABEL_PADDING)
                .withBorders(Rectangle.NO_BORDER);

        revidera.getChildren().add(noCheckbox);
        revidera.getChildren().add(yesCheckbox);

        fraga3.addChild(revidera);

        // Beskriv vilken eller vilka diagnoser som avses
        FkValueField vilkenAvses = new FkValueField(intyg.getDiagnosForNyBedomning())
                .size(KATEGORI_FULL_WIDTH, 9f)
                .offset(0f, 67f)
                .withTopLabel(getText("DFR_45.2.RBK"));
        fraga3.addChild(vilkenAvses);

        allElements.add(fraga3);

        // Fraga 4. Bakgrund - beskriv kortfattat förloppet för aktuella sjukdomar
        // --------------------------------------------------------------------------
        FkCategory fraga4 = new FkCategory("4. " + getText("FRG_5.RBK"))
                .offset(KATEGORI_OFFSET_X, 113f)
                .size(KATEGORI_FULL_WIDTH, 22.5f)
                .withBorders(Rectangle.BOX);

        FkValueField bakgrund = new FkValueField(intyg.getSjukdomsforlopp())
                .size(KATEGORI_FULL_WIDTH, 20f)
                .offset(0f, 0f).withValueTextAlignment(Element.ALIGN_TOP);
        fraga4.addChild(bakgrund);
        allElements.add(fraga4);

        // Fraga 5. Funktionsnedsättning - beskriv undersökningsfynd
        // --------------------------------------------------------------------------
        FkCategory fraga5 = new FkCategory("5. " + getText("DFR_10.1.RBK"))
                .offset(KATEGORI_OFFSET_X, 151.5f)
                .size(KATEGORI_FULL_WIDTH, 135f)
                .withBorders(Rectangle.BOX);
        float fraga5YOffset = 0;

        // Intellektuell funktion
        fraga5.addChild(
                new FkLabel(getText("FRG_8.RBK"))
                        .offset(0, fraga5YOffset)
                        .size(KATEGORI_FULL_WIDTH, FRAGA_5_DELFRAGA_RUBRIK_HEIGHT)
                        .withBorders(Rectangle.BOTTOM));
        fraga5YOffset += FRAGA_5_DELFRAGA_RUBRIK_HEIGHT;
        fraga5.addChild(new FkValueField(intyg.getFunktionsnedsattningIntellektuell())
                .offset(0f, fraga5YOffset)
                .size(KATEGORI_FULL_WIDTH, FRAGA_5_DELFRAGA_HEIGHT)
                .withValueTextAlignment(PdfPCell.ALIGN_TOP));
        fraga5YOffset += FRAGA_5_DELFRAGA_HEIGHT;

        // Kommunikation och social interaktion
        fraga5.addChild(
                new FkLabel(getText("FRG_9.RBK"))
                        .offset(0, fraga5YOffset)
                        .size(KATEGORI_FULL_WIDTH, FRAGA_5_DELFRAGA_RUBRIK_HEIGHT)
                        .withBorders(Rectangle.BOX));
        fraga5YOffset += FRAGA_5_DELFRAGA_RUBRIK_HEIGHT;

        fraga5.addChild(new FkValueField(intyg.getFunktionsnedsattningKommunikation())
                .offset(0f, fraga5YOffset)
                .size(KATEGORI_FULL_WIDTH, FRAGA_5_DELFRAGA_HEIGHT)
                .withValueTextAlignment(PdfPCell.ALIGN_TOP));

        fraga5YOffset += FRAGA_5_DELFRAGA_HEIGHT;
        // Uppmärksamhet, koncentration och exekutiv funktion
        fraga5.addChild(new FkLabel(getText("FRG_10.RBK"))
                .offset(0, fraga5YOffset)
                .size(KATEGORI_FULL_WIDTH, FRAGA_5_DELFRAGA_RUBRIK_HEIGHT)
                .withBorders(Rectangle.BOX));
        fraga5YOffset += FRAGA_5_DELFRAGA_RUBRIK_HEIGHT;

        fraga5.addChild(new FkValueField(intyg.getFunktionsnedsattningKoncentration())
                .offset(0f, fraga5YOffset)
                .size(KATEGORI_FULL_WIDTH, FRAGA_5_DELFRAGA_HEIGHT)
                .withValueTextAlignment(PdfPCell.ALIGN_TOP));
        fraga5YOffset += FRAGA_5_DELFRAGA_HEIGHT;

        // Annan psykisk funktion
        fraga5.addChild(
                new FkLabel(getText("FRG_11.RBK"))
                        .offset(0, fraga5YOffset)
                        .size(KATEGORI_FULL_WIDTH, FRAGA_5_DELFRAGA_RUBRIK_HEIGHT)
                        .withBorders(Rectangle.BOX));
        fraga5YOffset += FRAGA_5_DELFRAGA_RUBRIK_HEIGHT;
        fraga5.addChild(new FkValueField(intyg.getFunktionsnedsattningPsykisk())
                .offset(0f, fraga5YOffset)
                .size(KATEGORI_FULL_WIDTH, FRAGA_5_DELFRAGA_HEIGHT)
                .withValueTextAlignment(PdfPCell.ALIGN_TOP));

        allElements.add(fraga5);

        FkPage thisPage = new FkPage();
        thisPage.getChildren().addAll(allElements);
        return thisPage;

    }

    private FkPage createPage3(LuseUtlatande intyg) throws IOException, DocumentException {

        List<PdfComponent> allElements = new ArrayList<>();

        // Fraga 5. (Fortsattning fran sida 2 - ingen rubrik)
        // --------------------------------------------------------------------------
        float fraga5YOffset = 0;
        FkCategory fraga5 = new FkCategory("")
                .offset(KATEGORI_OFFSET_X, 21.5f)
                .size(KATEGORI_FULL_WIDTH, 101f)
                .withBorders(Rectangle.BOX);

        // Sinnesfunktioner och smärta
        fraga5.addChild(
                new FkLabel(getText("FRG_12.RBK"))
                        .offset(0, fraga5YOffset)
                        .size(KATEGORI_FULL_WIDTH, FRAGA_5_DELFRAGA_RUBRIK_HEIGHT)
                        .withBorders(Rectangle.BOX));
        fraga5YOffset += FRAGA_5_DELFRAGA_RUBRIK_HEIGHT;

        fraga5.addChild(new FkValueField(intyg.getFunktionsnedsattningSynHorselTal())
                .offset(0f, fraga5YOffset)
                .size(KATEGORI_FULL_WIDTH, FRAGA_5_DELFRAGA_HEIGHT)
                .withValueTextAlignment(PdfPCell.ALIGN_TOP));
        fraga5YOffset += FRAGA_5_DELFRAGA_HEIGHT;

        // Balans, koordination och motorik
        fraga5.addChild(new FkLabel(getText("FRG_13.RBK"))
                .offset(0, fraga5YOffset)
                .size(KATEGORI_FULL_WIDTH, FRAGA_5_DELFRAGA_RUBRIK_HEIGHT)
                .withBorders(Rectangle.BOX));
        fraga5YOffset += FRAGA_5_DELFRAGA_RUBRIK_HEIGHT;

        fraga5.addChild(new FkValueField(intyg.getFunktionsnedsattningBalansKoordination())
                .offset(0f, fraga5YOffset)
                .size(KATEGORI_FULL_WIDTH, FRAGA_5_DELFRAGA_HEIGHT)
                .withValueTextAlignment(PdfPCell.ALIGN_TOP));
        fraga5YOffset += FRAGA_5_DELFRAGA_HEIGHT;

        // Annan kroppslig funktion
        fraga5.addChild(new FkLabel(getText("FRG_14.RBK"))
                .offset(0, fraga5YOffset)
                .size(KATEGORI_FULL_WIDTH, FRAGA_5_DELFRAGA_RUBRIK_HEIGHT)
                .withBorders(Rectangle.BOX));
        fraga5YOffset += FRAGA_5_DELFRAGA_RUBRIK_HEIGHT;

        fraga5.addChild(new FkValueField(intyg.getFunktionsnedsattningAnnan())
                .offset(0f, fraga5YOffset)
                .size(KATEGORI_FULL_WIDTH, FRAGA_5_DELFRAGA_HEIGHT)
                .withValueTextAlignment(PdfPCell.ALIGN_TOP));

        allElements.add(fraga5);

        // Fraga 6.Aktivitetsbegränsningar --------------------------------------------------------------------------
        FkCategory fraga6 = new FkCategory("6. " + getText("FRG_17.RBK"))
                .offset(KATEGORI_OFFSET_X, 138.5f)
                .size(KATEGORI_FULL_WIDTH, 29.2f)
                .withBorders(Rectangle.BOX);

        // Ge konkreta exempel
        fraga6.addChild(new FkValueField(intyg.getAktivitetsbegransning())
                .offset(0f, 0f)
                .size(KATEGORI_FULL_WIDTH, 25f)
                .withValueTextAlignment(PdfPCell.ALIGN_TOP)
                .withTopLabel(getText("DFR_17.1.RBK")));

        allElements.add(fraga6);

        // Fraga 7.Medicinsk behandling --------------------------------------------------------------------------
        FkCategory fraga7 = new FkCategory("7. " + getText("KAT_7.RBK"))
                .offset(KATEGORI_OFFSET_X, 181.2f)
                .size(KATEGORI_FULL_WIDTH, 110.2f)
                .withBorders(Rectangle.BOX);

        float fraga7Offset = 0f;
        fraga7.addChild(new FkLabel(getText("FRG_18.RBK") + ". " + getText("DFR_18.1.RBK"))
                .offset(0, fraga7Offset)
                .size(KATEGORI_FULL_WIDTH, FRAGA_7_DELFRAGA_RUBRIK_HEIGHT + 2f));
        fraga7Offset += FRAGA_7_DELFRAGA_RUBRIK_HEIGHT + 2f;
        fraga7.addChild(new FkValueField(intyg.getAvslutadBehandling())
                .offset(0f, fraga7Offset)
                .size(KATEGORI_FULL_WIDTH, FRAGA_7_DELFRAGA_HEIGHT)
                .withValueTextAlignment(PdfPCell.ALIGN_TOP)
                .withTopPadding(0)
                .withBorders(Rectangle.BOTTOM));
        fraga7Offset += FRAGA_7_DELFRAGA_HEIGHT;

        fraga7.addChild(new FkLabel(getText("FRG_19.RBK") + ". " + getText("DFR_19.1.RBK"))
                .offset(0, fraga7Offset)
                .size(KATEGORI_FULL_WIDTH, FRAGA_7_DELFRAGA_RUBRIK_HEIGHT));
        fraga7Offset += FRAGA_7_DELFRAGA_RUBRIK_HEIGHT;

        fraga7.addChild(new FkValueField(intyg.getPagaendeBehandling())
                .offset(0f, fraga7Offset)
                .size(KATEGORI_FULL_WIDTH, FRAGA_7_DELFRAGA_HEIGHT)
                .withValueTextAlignment(PdfPCell.ALIGN_TOP)
                .withTopPadding(0)
                .withBorders(Rectangle.BOTTOM));
        fraga7Offset += FRAGA_7_DELFRAGA_HEIGHT;

        fraga7.addChild(new FkLabel(getText("FRG_20.RBK") + ". " + getText("DFR_20.1.RBK"))
                .offset(0, fraga7Offset)
                .size(KATEGORI_FULL_WIDTH, FRAGA_7_DELFRAGA_RUBRIK_HEIGHT));
        fraga7Offset += FRAGA_7_DELFRAGA_RUBRIK_HEIGHT;

        fraga7.addChild(new FkValueField(intyg.getPlaneradBehandling())
                .offset(0f, fraga7Offset)
                .size(KATEGORI_FULL_WIDTH, FRAGA_7_DELFRAGA_HEIGHT)
                .withValueTextAlignment(PdfPCell.ALIGN_TOP)
                .withTopPadding(0)
                .withBorders(Rectangle.BOTTOM));
        fraga7Offset += FRAGA_7_DELFRAGA_HEIGHT;

        fraga7.addChild(new FkLabel(getText("FRG_21.RBK") + " (" + getText("DFR_21.1.RBK") + ")")
                .offset(0, fraga7Offset)
                .size(KATEGORI_FULL_WIDTH, FRAGA_7_DELFRAGA_RUBRIK_HEIGHT));
        fraga7Offset += FRAGA_7_DELFRAGA_RUBRIK_HEIGHT;

        fraga7.addChild(new FkValueField(intyg.getSubstansintag())
                .offset(0f, fraga7Offset)
                .size(KATEGORI_FULL_WIDTH, FRAGA_7_DELFRAGA_HEIGHT)
                .withTopPadding(0)
                .withValueTextAlignment(PdfPCell.ALIGN_TOP));

        allElements.add(fraga7);

        FkPage thisPage = new FkPage();
        thisPage.getChildren().addAll(allElements);
        return thisPage;

    }

    private FkPage createPage4(LuseUtlatande intyg) throws IOException, DocumentException {

        List<PdfComponent> allElements = new ArrayList<>();
        // Fraga 8. Medicinska forutsattningar
        // --------------------------------------------------------------------------
        FkCategory fraga8 = new FkCategory("8. " + getText("KAT_8.RBK"))
                .offset(KATEGORI_OFFSET_X, 25.5f)
                .size(KATEGORI_FULL_WIDTH, 54f)
                .withBorders(Rectangle.BOX);

        fraga8.addChild(new FkLabel(getText("FRG_22.RBK"))
                .offset(0, 0)
                .size(KATEGORI_FULL_WIDTH, 6.5f)
                .withTopPadding(0.5f)
                .withVerticalAlignment(PdfPCell.TOP));
        fraga8.addChild(new FkValueField(intyg.getMedicinskaForutsattningarForArbete())
                .offset(0f, 6.5f)
                .size(KATEGORI_FULL_WIDTH, FRAGA_8_DELFRAGA_HEIGHT - 6.5f)
                .withTopPadding(0)
                .withValueTextAlignment(PdfPCell.ALIGN_TOP));

        fraga8.addChild(new FkLabel(getText("FRG_23.RBK"))
                .offset(0, FRAGA_8_DELFRAGA_HEIGHT)
                .size(KATEGORI_FULL_WIDTH, 4f)
                .withTopPadding(0.5f)
                .withVerticalAlignment(PdfPCell.TOP)
                .withBorders(Rectangle.TOP));
        fraga8.addChild(new FkValueField(intyg.getFormagaTrotsBegransning())
                .offset(0f, FRAGA_8_DELFRAGA_HEIGHT + 4f)
                .size(KATEGORI_FULL_WIDTH, FRAGA_8_DELFRAGA_HEIGHT - 4f)
                .withTopPadding(0)
                .withValueTextAlignment(PdfPCell.ALIGN_TOP));

        allElements.add(fraga8);

        // Fraga 9. Övriga upplysningar --------------------------------------------------------------------------
        FkCategory fraga9 = new FkCategory("9. " + getText("FRG_25.RBK"))
                .offset(KATEGORI_OFFSET_X, 93f)
                .size(KATEGORI_FULL_WIDTH, 27f)
                .withBorders(Rectangle.BOX);

        fraga9.addChild(new FkValueField(intyg.getOvrigt())
                .offset(0f, 0f)
                .size(KATEGORI_FULL_WIDTH, FRAGA_9_DELFRAGA_HEIGHT)
                .withValueTextAlignment(PdfPCell.ALIGN_TOP));

        allElements.add(fraga9);

        // Fraga 10. Kontakt med FK --------------------------------------------------------------------------
        FkCategory fraga10 = new FkCategory("10. " + getText("FRG_26.RBK"))
                .offset(KATEGORI_OFFSET_X, 133.5f)
                .size(KATEGORI_FULL_WIDTH, 22.5f)
                .withBorders(Rectangle.BOX);
        // Jag önskar att Försäkringskassan kontaktar mig
        fraga10.addChild(new FkCheckbox(getText("DFR_26.1.RBK"), safeBoolean(intyg.getKontaktMedFk()))
                .offset(0f, 0f)
                .size(KATEGORI_FULL_WIDTH, 9f)
                .withBorders(Rectangle.BOTTOM));
        fraga10.addChild(new FkValueField(intyg.getAnledningTillKontakt())
                .offset(0f, 9f)
                .size(KATEGORI_FULL_WIDTH, 14.5f)
                .withValueTextAlignment(PdfPCell.ALIGN_TOP)
                .withTopLabel(getText("DFR_26.2.RBK")));

        allElements.add(fraga10);

        // Fraga 11. Underskrift --------------------------------------------------------------------------
        FkCategory fraga11 = new FkCategory("11. Underskrift")
                .offset(KATEGORI_OFFSET_X, 170f)
                .size(KATEGORI_FULL_WIDTH, 85.5f)
                .withBorders(Rectangle.BOX);

        fraga11.addChild(new FkValueField(intyg.getGrundData().getSigneringsdatum().format(DateTimeFormatter.ofPattern(DATE_PATTERN)))
                .offset(0f, 0f)
                .size(45f, 11f)
                .withValueTextAlignment(PdfPCell.ALIGN_BOTTOM)
                .withBorders(Rectangle.RIGHT + Rectangle.BOTTOM)
                .withTopLabel("Datum"));
        fraga11.addChild(new FkValueField("")
                .offset(45f, 0f)
                .size(KATEGORI_FULL_WIDTH - 45f, 11f)
                .withBorders(Rectangle.BOTTOM)
                .withValueTextAlignment(PdfPCell.ALIGN_BOTTOM)
                .withTopLabel("Läkarens namnteckning"));

        fraga11.addChild(new FkValueField(intyg.getGrundData().getSkapadAv().getFullstandigtNamn())
                .offset(0f, 11f)
                .size(KATEGORI_FULL_WIDTH, 11f)
                .withBorders(Rectangle.BOTTOM)
                .withValueTextAlignment(PdfPCell.ALIGN_BOTTOM)
                .withTopLabel("Namnförtydligande"));

        fraga11.addChild(new FkValueField(concatStringList(intyg.getGrundData().getSkapadAv().getBefattningar()))
                .offset(0f, 22f)
                .size(90f, 13f)
                .withValueTextAlignment(PdfPCell.ALIGN_TOP)
                .withBorders(Rectangle.RIGHT + Rectangle.BOTTOM)
                .withTopLabel("Befattning"));
        fraga11.addChild(new FkValueField(concatStringList(intyg.getGrundData().getSkapadAv().getSpecialiteter()))
                .offset(90f, 22f)
                .size(KATEGORI_FULL_WIDTH - 90f, 13f)
                .withBorders(Rectangle.BOTTOM)
                .withValueTextAlignment(PdfPCell.ALIGN_TOP)
                .withTopLabel("Eventuell specialistkompetens"));
        // TODO: Är getPersonId = HSA-id eller personnr?
        fraga11.addChild(new FkValueField(intyg.getGrundData().getSkapadAv().getPersonId())
                .offset(0f, 35f)
                .size(90f, 9f)
                .withValueTextAlignment(PdfPCell.ALIGN_BOTTOM)
                .withBorders(Rectangle.BOTTOM + Rectangle.RIGHT)
                .withTopLabel("Läkarens HSA-id"));
        fraga11.addChild(new FkValueField(intyg.getGrundData().getSkapadAv().getVardenhet().getArbetsplatsKod())
                .offset(90f, 35f)
                .size(KATEGORI_FULL_WIDTH - 90f, 9f)
                .withValueTextAlignment(PdfPCell.ALIGN_BOTTOM)
                .withBorders(Rectangle.BOTTOM)
                .withTopLabel("Arbetsplatskod"));
        // TODO: Är getPersonId = HSA-id eller personnr?
        fraga11.addChild(new FkValueField(intyg.getGrundData().getSkapadAv().getPersonId())
                .offset(0f, 45f)
                .size(KATEGORI_FULL_WIDTH, 9f)
                .withValueTextAlignment(PdfPCell.ALIGN_BOTTOM)
                .withBorders(Rectangle.BOTTOM)
                .withTopLabel("Läkarens personnummer. Anges endast om du som läkare saknar HSA-id."));

        fraga11.addChild(new FkValueField(buildVardEnhetAdress(intyg.getGrundData().getSkapadAv().getVardenhet()))
                .offset(0f, 54f)
                .size(KATEGORI_FULL_WIDTH, 30f)
                .withValueTextAlignment(PdfPCell.ALIGN_TOP)
                .withTopLabel("Vårdenhetens namn, adress och telefon."));

        // Somewhat hacky, add a label outside the category box.
        fraga11.addChild(new FkLabel("Underskriften omfattar samtliga uppgifter i intyget")
                .offset(0f, 92f)
                .size(KATEGORI_FULL_WIDTH, 10f)
                .withVerticalAlignment(PdfPCell.ALIGN_TOP));

        allElements.add(fraga11);

        FkPage thisPage = new FkPage();
        thisPage.getChildren().addAll(allElements);
        return thisPage;
    }

    private FkPage createPage5(LuseUtlatande intyg) throws IOException, DocumentException {

        List<PdfComponent> allElements = new ArrayList<>();

        // Meta elements such as FK logotype, information text(s) etc.
        Resource resource = new ClassPathResource("images/forsakringskassans_logotyp.jpg");
        FkImage logo = new FkImage(IOUtils.toByteArray(resource.getInputStream()))
                .withLinearScale(0.253f)
                .offset(16f, 20f);
        allElements.add(logo);

        FkLabel mainHeader = new FkLabel("Läkarutlåtande")
                .offset(107.5f, 10f)
                .size(40, 12f)
                .withVerticalAlignment(Element.ALIGN_TOP)
                .withFont(PdfConstants.FONT_FRAGERUBRIK);
        FkLabel subHeader = new FkLabel("för sjukersättning")
                .offset(107.5f, 14f)
                .size(40, 15)
                .withVerticalAlignment(Element.ALIGN_TOP)
                .withFont(PdfConstants.FONT_BOLD_9);
        allElements.add(mainHeader);
        allElements.add(subHeader);

        FkLabel patientPnrLbl = new FkLabel("Personnummer")
                .offset(170f, 18f)
                .size(35f, 15f)
                .withVerticalAlignment(Element.ALIGN_TOP)
                .withFont(PdfConstants.FONT_STAMPER_LABEL);
        allElements.add(patientPnrLbl);

        FkLabel patientPnr = new FkLabel(intyg.getGrundData().getPatient().getPersonId().getPersonnummer())
                .offset(170f, 22f)
                .size(35f, 15f).withFont(PdfConstants.FONT_VALUE_TEXT)
                .withVerticalAlignment(Element.ALIGN_TOP);
        allElements.add(patientPnr);

        // Sida 5 ar en extrasida, har lagger vi ev tillaggsfragor
        FkCategory tillaggsfragor = new FkCategory("")
                .offset(KATEGORI_OFFSET_X, 40f)
                .size(KATEGORI_FULL_WIDTH, 247.2f)
                .withBorders(Rectangle.BOX);
        float offset_y = 0;
        for (Tillaggsfraga tillaggsfraga : intyg.getTillaggsfragor()) {

            tillaggsfragor.addChild(new FkValueField(tillaggsfraga.getSvar())
                    .offset(0f, offset_y)
                    .size(KATEGORI_FULL_WIDTH, 50f)
                    .withValueTextAlignment(PdfPCell.ALIGN_TOP)
                    .withTopLabel("Tilläggsfråga: " + getText("DFR_" + tillaggsfraga.getId() + ".1.RBK")));
            offset_y += 40;
        }

        allElements.add(tillaggsfragor);

        FkPage thisPage = new FkPage();
        thisPage.getChildren().addAll(allElements);
        return thisPage;
    }

    private String getText(String key) {
        return intygTexts.getTexter().get(key);
    }

    private String nullSafeString(String string) {
        return string != null ? string : "";
    }

    private String nullSafeString(InternalDate date) {
        return date != null ? date.getDate() : "";
    }

    private Diagnos safeGetDiagnos(LuseUtlatande intyg, int index) {
        if (index < intyg.getDiagnoser().size()) {
            return intyg.getDiagnoser().get(index);
        }
        return Diagnos.create("", "", "", "");
    }

    private boolean safeBoolean(Boolean b) {
        if (b == null) {
            return false;
        }
        return b.booleanValue();
    }

    private String concatStringList(List<String> strings) {
        StringJoiner sj = new StringJoiner(", ");
        for (String s : strings) {
            sj.add(s);
        }
        return sj.toString();
    }

    private String buildVardEnhetAdress(Vardenhet ve) {
        StringBuilder sb = new StringBuilder();
        sb.append(nullSafeString(ve.getEnhetsnamn())).append("\n")
                .append(nullSafeString(ve.getPostadress())).append("\n")
                .append(nullSafeString(ve.getPostnummer())).append(" ").append(nullSafeString(ve.getPostort())).append("\n")
                .append("Telefon:").append(nullSafeString(ve.getTelefonnummer()));
        return sb.toString();

    }

}
