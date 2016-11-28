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
package se.inera.intyg.intygstyper.ts_bas.pdf;

import static se.inera.intyg.intygstyper.ts_parent.codes.RespConstants.BEFATTNINGSKOD_LAKARE_EJ_LEG_AT;
import static se.inera.intyg.intygstyper.ts_parent.codes.RespConstants.BEFATTNINGSKOD_LAKARE_LEG_ST;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.*;

import se.inera.intyg.common.services.texts.IntygTextsService;
import se.inera.intyg.common.services.texts.model.IntygTexts;
import se.inera.intyg.common.support.model.common.internal.Patient;
import se.inera.intyg.common.support.model.common.internal.Vardenhet;
import se.inera.intyg.common.support.modules.support.ApplicationOrigin;
import se.inera.intyg.common.support.modules.support.api.dto.Personnummer;
import se.inera.intyg.intygstyper.ts_bas.model.internal.*;
import se.inera.intyg.intygstyper.ts_bas.support.TsBasEntryPoint;
import se.inera.intyg.intygstyper.ts_parent.codes.DiabetesKod;
import se.inera.intyg.intygstyper.ts_parent.codes.IdKontrollKod;
import se.inera.intyg.intygstyper.ts_parent.pdf.PdfGenerator;
import se.inera.intyg.intygstyper.ts_parent.pdf.PdfGeneratorException;

public class PdfGeneratorImpl implements PdfGenerator<Utlatande> {

    @Autowired(required = false)
    private IntygTextsService intygTexts;

    private static final String PDF_PATH_V06U07 = "pdf/TSTRK1007-V0607.pdf";
    // Constants for printing ID and origin in left margin
    private static final int MARGIN_TEXT_START_X = 46;
    private static final int MARGIN_TEXT_START_Y = 137;
    private static final int MARGIN_TEXT_FONTSIZE = 7;
    private static final String MINA_INTYG_MARGIN_TEXT = "Intyget är utskrivet från Mina Intyg.";
    private static final String WEBCERT_MARGIN_TEXT = "Intyget är utskrivet från Webcert.";
    private static final String SPECIALIST_I_ALLMANMEDICIN_TITLE = "Specialist i allmänmedicin";

    private static final StringField INVANARE_ADRESS_FALT1 = new StringField("Falt");
    private static final StringField INVANARE_ADRESS_FALT2 = new StringField("Falt__1");
    private static final StringField INVANARE_ADRESS_FALT3 = new StringField("Falt__2");
    private static final StringField INVANARE_PERSONNUMMER = new StringField("Falt__3");

    private static final CheckGroupField<IntygAvserKategori> INTYG_AVSER;


    static {
        INTYG_AVSER = new CheckGroupField<>();
        INTYG_AVSER.addField(IntygAvserKategori.C1, "Falt_10");
        INTYG_AVSER.addField(IntygAvserKategori.C1E, "Falt_11");
        INTYG_AVSER.addField(IntygAvserKategori.C, "Falt_12");
        INTYG_AVSER.addField(IntygAvserKategori.CE, "Falt_13");
        INTYG_AVSER.addField(IntygAvserKategori.D1, "Falt_14");
        INTYG_AVSER.addField(IntygAvserKategori.D1E, "Falt_15");
        INTYG_AVSER.addField(IntygAvserKategori.D, "Falt_16");
        INTYG_AVSER.addField(IntygAvserKategori.DE, "Falt_17");
        INTYG_AVSER.addField(IntygAvserKategori.TAXI, "Falt_18");
        INTYG_AVSER.addField(IntygAvserKategori.ANNAT, "Falt_19");
    }

    private static final CheckField ID_KONTROLL_IDKORT = new CheckField("Falt_66");
    private static final CheckField ID_KONTROLL_FORETAG_TJANSTEKORT = new CheckField("Falt_67");
    private static final CheckField ID_KONTROLL_SVENSKT_KORKORT = new CheckField("Falt_68");
    private static final CheckField ID_KONTROLL_PERSONLIG_KANNEDOM = new CheckField("Falt_69");
    private static final CheckField ID_KONTROLL_FORSAKRAN = new CheckField("Falt_70");
    private static final CheckField ID_KONTROLL_PASS = new CheckField("Falt_71");

    private static final YesNoField SYNFALTSDEFEKTER = new YesNoField("Falt_196", "Falt_197");
    private static final YesNoField DIPLOPI = new YesNoField("Falt_202", "Falt_203");
    private static final YesNoField NYSTAGMUS = new YesNoField("Falt_204", "Falt_205");
    private static final YesNoField NATTBLINDHET = new YesNoField("Falt_198", "Falt_199");
    private static final YesNoField PROGRESIV_OGONSJUKDOM = new YesNoField("Falt_200", "Falt_201");
    private static final DecimalField EJ_KORRIGERAD_SYNSKARPA_HOGER = new DecimalField("Falt__206", "Falt__207");
    private static final DecimalField EJ_KORRIGERAD_SYNSKARPA_VANSTER = new DecimalField("Falt__210", "Falt__211");
    private static final DecimalField EJ_KORRIGERAD_SYNSKARPA_BINOKULART = new DecimalField("Falt__214", "Falt__215");
    private static final DecimalField KORRIGERAD_SYNSKARPA_HOGER = new DecimalField("Falt__208", "Falt__209");
    private static final DecimalField KORRIGERAD_SYNSKARPA_VANSTER = new DecimalField("Falt__212", "Falt__213");
    private static final DecimalField KORRIGERAD_SYNSKARPA_BINOKULART = new DecimalField("Falt__216", "Falt__217");
    private static final CheckField KONTAKTLINSER_HOGER = new CheckField("Falt_218");
    private static final CheckField KONTAKTLINSER_VANSTER = new CheckField("Falt_219");
    private static final CheckField UNDERSOKNING_PLUS8_KORREKTIONSGRAD = new CheckField("Falt_220");

    private static final YesNoField ANFALL_BALANSRUBBNING_YRSEL = new YesNoField("Falt_5", "Falt_6");
    private static final YesNoField SVARIGHET_SAMTAL_4M = new YesNoField("Falt_7", "Falt_8");

    private static final YesNoField FORSAMRAD_RORLIGHET_FRAMFORA_FORDON = new YesNoField("Falt_20", "Falt_21");
    private static final StringField FORSAMRAD_RORLIGHET_FRAMFORA_FORDON_BESKRIVNING = new StringField("FaltDiv");
    private static final YesNoField FORSAMRAD_RORLIGHET_HJALPA_PASSAGERARE = new YesNoField("Falt_22", "Falt_23");

    private static final YesNoField HJART_KARLSJUKDOM_TRAFIKSAKERHETSRISK = new YesNoField("Falt_24", "Falt_25");
    private static final YesNoField TECKEN_PA_HJARNSKADA = new YesNoField("Falt_26", "Falt_27");
    private static final YesNoField RISKFAKTORER_STROKE = new YesNoField("Falt_28", "Falt_29");
    private static final StringField RISKFAKTORER_STROKE_BESKRVNING = new StringField("FaltDiv1");

    private static final YesNoField HAR_DIABETES = new YesNoField("Falt_30", "Falt_31");
    private static final CheckField DIABETES_TYP_1 = new CheckField("Falt_32");
    private static final CheckField DIABETES_TYP_2 = new CheckField("Falt_33");
    private static final CheckField DIABETIKER_KOSTBEHANDLING = new CheckField("Falt_34");
    private static final CheckField DIABETIKER_TABLETTBEHANDLING = new CheckField("Falt_35");
    private static final CheckField DIABETIKER_INSULINBEHANDLING = new CheckField("Falt_36");

    private static final YesNoField TECKEN_PA_NEUROLOGISK_SJUKDOM = new YesNoField("Falt_37", "Falt_38");

    private static final YesNoField MEDVETANDESTORNING = new YesNoField("Falt_39", "Falt_40");
    private static final StringField MEDVETANDESTORNING_BESKRIVNING = new StringField("FaltDiv2");

    private static final YesNoField NEDSATT_NJURFUNKTION_TRAFIKSAKERHETSRISK = new YesNoField("Falt_41", "Falt_42");

    private static final YesNoField SVIKTANDE_KOGNITIV_FUNKTION = new YesNoField("Falt_43", "Falt_44");

    private static final YesNoField SOMN_VAKENHETSSTORNING = new YesNoField("Falt_45", "Falt_46");

    private static final YesNoField TECKEN_PA_MISSBRUK = new YesNoField("Falt_47", "Falt_48");
    private static final YesNoField VARDINSATS_MISSBRUK_BEROENDE = new YesNoField("Falt_49", "Falt_50");
    private static final YesNoField BEHOV_AV_PROVTAGNING_MISSBRUK = new YesNoField("Falt_51", "Falt_52");
    private static final YesNoField LAKEMEDELSANVANDNING_TRAFIKSAKERHETSRISK = new YesNoField("Falt_53", "Falt_54");
    private static final StringField LAKEMEDELSANVANDNING_TRAFIKSAKERHETSRISK_BESKRIVNING = new StringField("FaltDiv3");

    private static final YesNoField PSYKISK_SJUKDOM = new YesNoField("Falt_55", "Falt_56");

    private static final YesNoField ADHD_DAMP_MM = new YesNoField("Falt_59", "Falt_60");
    private static final YesNoField PSYKISK_UTVECKLINGSSTORNING = new YesNoField("Falt_57", "Falt_58");

    private static final YesNoField VARD_PA_SJUKHUS = new YesNoField("Falt_61", "Falt_62");
    private static final StringField VARD_PA_SJUKHUS_TID = new StringField("Falt__84");
    private static final StringField VARD_PA_SJUKHUS_VARDINRATTNING = new StringField("Falt__85");
    private static final StringField VARD_PA_SJUKHUS_ANLEDNING = new StringField("Falt__86");

    private static final YesNoField STADIGVARANDE_MEDICINERING = new YesNoField("Falt_63", "Falt_64");
    private static final StringField STADIGVARANDE_MEDICINERING_BESKRIVNING = new StringField("FaltDiv4");

    private static final StringField OVRIG_BESKRIVNING = new StringField("FaltDiv5");

    private static final CheckGroupField<BedomningKorkortstyp> BEDOMNING;
    static {
        BEDOMNING = new CheckGroupField<>();
        BEDOMNING.addField(BedomningKorkortstyp.C1, "Falt_65");
        BEDOMNING.addField(BedomningKorkortstyp.C1E, "Falt_72");
        BEDOMNING.addField(BedomningKorkortstyp.C, "Falt_73");
        BEDOMNING.addField(BedomningKorkortstyp.CE, "Falt_74");
        BEDOMNING.addField(BedomningKorkortstyp.D1, "Falt_75");
        BEDOMNING.addField(BedomningKorkortstyp.D1E, "Falt_76");
        BEDOMNING.addField(BedomningKorkortstyp.D, "Falt_77");
        BEDOMNING.addField(BedomningKorkortstyp.DE, "Falt_78");
        BEDOMNING.addField(BedomningKorkortstyp.TAXI, "Falt_79");
        BEDOMNING.addField(BedomningKorkortstyp.ANNAT, "Falt_81");
    }
    private static final CheckField BEDOMNING_INTE_TA_STALLNING = new CheckField("Falt_80");

    private static final StringField BEDOMNING_BOR_UNDERSOKAS_SPECIALIST = new StringField("FaltDiv6");

    private static final StringField INTYGSDATUM = new StringField("Falt__82");
    private static final StringField VARDINRATTNINGENS_NAMN = new StringField("Falt__83");
    private static final StringField ADRESS_OCH_ORT = new StringField("Falt__87");
    private static final StringField TELEFON = new StringField("Falt__88");
    private static final StringField NAMNFORTYDLIGANDE = new StringField("Falt__90");

    private static final CheckField SPECIALISTKOMPETENS_CHECK = new CheckField("Falt_91");
    private static final StringField SPECIALISTKOMPETENS_BESKRVNING = new StringField("Falt_92");
    private static final CheckField ST_LAKARE_CHECK = new CheckField("Falt_93");
    private static final CheckField AT_LAKARE_CHECK = new CheckField("Falt_94");

    private final boolean formFlattening;

    public PdfGeneratorImpl(boolean formFlattening) {
        this.formFlattening = formFlattening;
    }

    @Override
    public String generatePdfFilename(Utlatande utlatande) {
        Personnummer personId = utlatande.getGrundData().getPatient().getPersonId();
        final String personnummerString = personId.getPersonnummer() != null ? personId.getPersonnummer() : "NoPnr";

        return String.format("lakarintyg_transportstyrelsen_%s.pdf", personnummerString);
    }

    @Override
    public byte[] generatePDF(Utlatande utlatande, ApplicationOrigin applicationOrigin) throws PdfGeneratorException {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            PdfReader pdfReader = new PdfReader(getPdfPath(utlatande));
            PdfStamper pdfStamper = new PdfStamper(pdfReader, outputStream);
            pdfStamper.setFormFlattening(formFlattening);
            AcroFields fields = pdfStamper.getAcroFields();
            populatePdfFields(utlatande, fields, applicationOrigin);

            // Decorate PDF depending on the origin of the pdf-call
            switch (applicationOrigin) {
                case MINA_INTYG:
                    createLeftMarginText(pdfStamper, pdfReader.getNumberOfPages(), utlatande.getId(), MINA_INTYG_MARGIN_TEXT);
                    break;
                case WEBCERT:
                    createLeftMarginText(pdfStamper, pdfReader.getNumberOfPages(), utlatande.getId(), WEBCERT_MARGIN_TEXT);
                    break;
                default:
                    break;
            }

            pdfStamper.close();

            return outputStream.toByteArray();

        } catch (Exception e) {
            throw new PdfGeneratorException(e);
        }
    }

    private String getPdfPath(Utlatande utlatande) throws PdfGeneratorException {
        String textVersion = utlatande.getTextVersion();
        if (textVersion == null) {
            return PDF_PATH_V06U07;
        }
        IntygTexts texts = intygTexts.getIntygTextsPojo(TsBasEntryPoint.MODULE_ID, textVersion);
        if (texts == null) {
            return PDF_PATH_V06U07;
        }
        String path = texts.getPdfPath();
        if (path == null) {
            return PDF_PATH_V06U07;
        }
        return path;
    }

    private void createLeftMarginText(PdfStamper pdfStamper, int numberOfPages, String id, String text) throws DocumentException, IOException {
        PdfContentByte addOverlay;
        BaseFont bf = BaseFont.createFont();
        // Do text
        for (int i = 1; i <= numberOfPages; i++) {
            addOverlay = pdfStamper.getOverContent(i);
            addOverlay.saveState();
            addOverlay.beginText();
            addOverlay.setFontAndSize(bf, MARGIN_TEXT_FONTSIZE);
            addOverlay.setTextMatrix(0, 1, -1, 0, MARGIN_TEXT_START_X, MARGIN_TEXT_START_Y);
            addOverlay.showText(String.format("Intygs-ID: %s. %s", id, text));
            addOverlay.endText();
            addOverlay.restoreState();
        }
    }

    /**
     * Method for filling out the fields of a pdf with data from the internal model.
     *
     * @param utlatande
     *            {@link se.inera.intyg.intygstyper.ts_bas.model.internal.Utlatande} containing data for populating
     *            the pdf
     * @param fields
     *            The fields of the pdf
     * @throws DocumentException
     * @throws IOException
     */
    private void populatePdfFields(Utlatande utlatande, AcroFields fields, ApplicationOrigin applicationOrigin) throws IOException, DocumentException {
        populatePatientInfo(utlatande.getGrundData().getPatient(), fields);
        populateIntygAvser(utlatande.getIntygAvser(), fields);
        populateIdkontroll(utlatande.getVardkontakt(), fields);
        populateSynFields(utlatande.getSyn(), fields);
        populateHorselBalansFields(utlatande.getHorselBalans(), fields);
        populateFunktionsnedsattning(utlatande.getFunktionsnedsattning(), fields);
        populateHjartKarl(utlatande.getHjartKarl(), fields);
        populateDiabetes(utlatande.getDiabetes(), fields);
        populateNeurologi(utlatande.getNeurologi(), fields);
        populateMedvetandestorning(utlatande.getMedvetandestorning(), fields);
        populateNjurar(utlatande.getNjurar(), fields);
        populateKognitivt(utlatande.getKognitivt(), fields);
        populateSomnVakenhet(utlatande.getSomnVakenhet(), fields);
        populateNarkotikaLakemedel(utlatande.getNarkotikaLakemedel(), fields);
        populatePsykiskt(utlatande.getPsykiskt(), fields);
        populateUtvecklingsstorning(utlatande.getUtvecklingsstorning(), fields);
        populateSjukhusvard(utlatande.getSjukhusvard(), fields);
        populateMedicinering(utlatande.getMedicinering(), fields);
        populateOvrigt(utlatande.getKommentar(), fields);
        populateBedomning(utlatande.getBedomning(), fields);
        populateAvslut(utlatande, fields);
    }

    private void populatePatientInfo(Patient patient, AcroFields fields) throws IOException, DocumentException {
        INVANARE_ADRESS_FALT1.setField(fields, patient.getFullstandigtNamn());
        INVANARE_ADRESS_FALT2.setField(fields, patient.getPostadress());
        INVANARE_ADRESS_FALT3.setField(fields, patient.getPostnummer() + " " + patient.getPostort());
        INVANARE_PERSONNUMMER.setField(fields, patient.getPersonId().getPersonnummerWithoutDash());
    }

    private void populateIntygAvser(IntygAvser intygAvser, AcroFields fields) throws IOException, DocumentException {
        for (IntygAvserKategori kategori : intygAvser.getKorkortstyp()) {
            INTYG_AVSER.setField(fields, kategori);
        }
    }

    private void populateIdkontroll(Vardkontakt vardkontakt, AcroFields fields) throws IOException, DocumentException {
        if (vardkontakt.getIdkontroll().equals(IdKontrollKod.ID_KORT.name())) {
            ID_KONTROLL_IDKORT.setField(fields, true);
        } else if (vardkontakt.getIdkontroll().equals(IdKontrollKod.FORETAG_ELLER_TJANSTEKORT.name())) {
            ID_KONTROLL_FORETAG_TJANSTEKORT.setField(fields, true);
        } else if (vardkontakt.getIdkontroll().equals(IdKontrollKod.KORKORT.name())) {
            ID_KONTROLL_SVENSKT_KORKORT.setField(fields, true);
        } else if (vardkontakt.getIdkontroll().equals(IdKontrollKod.PERS_KANNEDOM.name())) {
            ID_KONTROLL_PERSONLIG_KANNEDOM.setField(fields, true);
        } else if (vardkontakt.getIdkontroll().equals(IdKontrollKod.FORSAKRAN_KAP18.name())) {
            ID_KONTROLL_FORSAKRAN.setField(fields, true);
        } else if (vardkontakt.getIdkontroll().equals(IdKontrollKod.PASS.name())) {
            ID_KONTROLL_PASS.setField(fields, true);
        }
    }

    private void populateSynFields(Syn syn, AcroFields fields) throws IOException, DocumentException {
        SYNFALTSDEFEKTER.setField(fields, syn.getSynfaltsdefekter());
        NATTBLINDHET.setField(fields, syn.getNattblindhet());
        PROGRESIV_OGONSJUKDOM.setField(fields, syn.getProgressivOgonsjukdom());
        DIPLOPI.setField(fields, syn.getDiplopi());
        NYSTAGMUS.setField(fields, syn.getNystagmus());

        EJ_KORRIGERAD_SYNSKARPA_HOGER.setField(fields, syn.getHogerOga().getUtanKorrektion());
        EJ_KORRIGERAD_SYNSKARPA_VANSTER.setField(fields, syn.getVansterOga().getUtanKorrektion());
        EJ_KORRIGERAD_SYNSKARPA_BINOKULART.setField(fields, syn.getBinokulart().getUtanKorrektion());
        KORRIGERAD_SYNSKARPA_HOGER.setField(fields, syn.getHogerOga().getMedKorrektion());
        KORRIGERAD_SYNSKARPA_VANSTER.setField(fields, syn.getVansterOga().getMedKorrektion());
        KORRIGERAD_SYNSKARPA_BINOKULART.setField(fields, syn.getBinokulart().getMedKorrektion());
        KONTAKTLINSER_HOGER.setField(fields, syn.getHogerOga().getKontaktlins());
        KONTAKTLINSER_VANSTER.setField(fields, syn.getVansterOga().getKontaktlins());
        UNDERSOKNING_PLUS8_KORREKTIONSGRAD.setField(fields, syn.getKorrektionsglasensStyrka());
    }

    private void populateHorselBalansFields(HorselBalans horselBalans, AcroFields fields) throws IOException,
            DocumentException {
        ANFALL_BALANSRUBBNING_YRSEL.setField(fields, horselBalans.getBalansrubbningar());
        SVARIGHET_SAMTAL_4M.setField(fields, horselBalans.getSvartUppfattaSamtal4Meter());
    }

    private void populateFunktionsnedsattning(Funktionsnedsattning funktionsnedsattning, AcroFields fields)
            throws IOException, DocumentException {
        FORSAMRAD_RORLIGHET_FRAMFORA_FORDON.setField(fields, funktionsnedsattning.getFunktionsnedsattning());
        FORSAMRAD_RORLIGHET_FRAMFORA_FORDON_BESKRIVNING.setField(fields, funktionsnedsattning.getBeskrivning());
        FORSAMRAD_RORLIGHET_HJALPA_PASSAGERARE.setField(fields, funktionsnedsattning.getOtillrackligRorelseformaga());
    }

    private void populateHjartKarl(HjartKarl hjartKarl, AcroFields fields) throws IOException, DocumentException {
        HJART_KARLSJUKDOM_TRAFIKSAKERHETSRISK.setField(fields, hjartKarl.getHjartKarlSjukdom());
        TECKEN_PA_HJARNSKADA.setField(fields, hjartKarl.getHjarnskadaEfterTrauma());
        RISKFAKTORER_STROKE.setField(fields, hjartKarl.getRiskfaktorerStroke());
        RISKFAKTORER_STROKE_BESKRVNING.setField(fields, hjartKarl.getBeskrivningRiskfaktorer());
    }

    private void populateDiabetes(Diabetes diabetes, AcroFields fields) throws IOException, DocumentException {
        HAR_DIABETES.setField(fields, diabetes.getHarDiabetes());
        if (diabetes.getDiabetesTyp() != null) {
            if (diabetes.getDiabetesTyp().equals(DiabetesKod.DIABETES_TYP_1.name())) {
                DIABETES_TYP_1.setField(fields, true);
            } else if (diabetes.getDiabetesTyp().equals(DiabetesKod.DIABETES_TYP_2.name())) {
                DIABETES_TYP_2.setField(fields, true);
            }
        }
        DIABETIKER_KOSTBEHANDLING.setField(fields, diabetes.getKost());
        DIABETIKER_TABLETTBEHANDLING.setField(fields, diabetes.getTabletter());
        DIABETIKER_INSULINBEHANDLING.setField(fields, diabetes.getInsulin());
    }

    private void populateNeurologi(Neurologi neurologi, AcroFields fields) throws IOException, DocumentException {
        TECKEN_PA_NEUROLOGISK_SJUKDOM.setField(fields, neurologi.getNeurologiskSjukdom());
    }

    private void populateMedvetandestorning(Medvetandestorning medvetandestorning, AcroFields fields)
            throws IOException, DocumentException {
        MEDVETANDESTORNING.setField(fields, medvetandestorning.getMedvetandestorning());
        MEDVETANDESTORNING_BESKRIVNING.setField(fields, medvetandestorning.getBeskrivning());
    }

    private void populateNjurar(Njurar njurar, AcroFields fields) throws IOException, DocumentException {
        NEDSATT_NJURFUNKTION_TRAFIKSAKERHETSRISK.setField(fields, njurar.getNedsattNjurfunktion());
    }

    private void populateKognitivt(Kognitivt kognitivt, AcroFields fields) throws IOException, DocumentException {
        SVIKTANDE_KOGNITIV_FUNKTION.setField(fields, kognitivt.getSviktandeKognitivFunktion());
    }

    private void populateSomnVakenhet(SomnVakenhet somnVakenhet, AcroFields fields) throws IOException,
            DocumentException {
        SOMN_VAKENHETSSTORNING.setField(fields, somnVakenhet.getTeckenSomnstorningar());
    }

    private void populateNarkotikaLakemedel(NarkotikaLakemedel narkotikaLakemedel, AcroFields fields)
            throws IOException, DocumentException {
        TECKEN_PA_MISSBRUK.setField(fields, narkotikaLakemedel.getTeckenMissbruk());
        VARDINSATS_MISSBRUK_BEROENDE.setField(fields, narkotikaLakemedel.getForemalForVardinsats());
        BEHOV_AV_PROVTAGNING_MISSBRUK.setField(fields, narkotikaLakemedel.getProvtagningBehovs());
        LAKEMEDELSANVANDNING_TRAFIKSAKERHETSRISK.setField(fields, narkotikaLakemedel.getLakarordineratLakemedelsbruk());
        LAKEMEDELSANVANDNING_TRAFIKSAKERHETSRISK_BESKRIVNING.setField(fields, narkotikaLakemedel.getLakemedelOchDos());
    }

    private void populatePsykiskt(Psykiskt psykiskt, AcroFields fields) throws IOException, DocumentException {
        PSYKISK_SJUKDOM.setField(fields, psykiskt.getPsykiskSjukdom());
    }

    private void populateUtvecklingsstorning(Utvecklingsstorning utvecklingsstorning, AcroFields fields)
            throws IOException, DocumentException {
        PSYKISK_UTVECKLINGSSTORNING.setField(fields, utvecklingsstorning.getPsykiskUtvecklingsstorning());
        ADHD_DAMP_MM.setField(fields, utvecklingsstorning.getHarSyndrom());
    }

    private void populateSjukhusvard(Sjukhusvard sjukhusvard, AcroFields fields) throws IOException, DocumentException {
        VARD_PA_SJUKHUS.setField(fields, sjukhusvard.getSjukhusEllerLakarkontakt());
        VARD_PA_SJUKHUS_TID.setField(fields, sjukhusvard.getTidpunkt());
        VARD_PA_SJUKHUS_VARDINRATTNING.setField(fields, sjukhusvard.getVardinrattning());
        VARD_PA_SJUKHUS_ANLEDNING.setField(fields, sjukhusvard.getAnledning());
    }

    private void populateMedicinering(Medicinering medicinering, AcroFields fields) throws IOException,
            DocumentException {
        STADIGVARANDE_MEDICINERING.setField(fields, medicinering.getStadigvarandeMedicinering());
        STADIGVARANDE_MEDICINERING_BESKRIVNING.setField(fields, medicinering.getBeskrivning());
    }

    private void populateOvrigt(String kommentar, AcroFields fields) throws IOException, DocumentException {
        OVRIG_BESKRIVNING.setField(fields, kommentar);
    }

    private void populateBedomning(Bedomning bedomning, AcroFields fields) throws IOException, DocumentException {
        for (BedomningKorkortstyp korkortstyp : bedomning.getKorkortstyp()) {
            BEDOMNING.setField(fields, korkortstyp);
        }
        BEDOMNING_INTE_TA_STALLNING.setField(fields, bedomning.getKanInteTaStallning());
        BEDOMNING_BOR_UNDERSOKAS_SPECIALIST.setField(fields, bedomning.getLakareSpecialKompetens());
    }

    private void populateAvslut(Utlatande utlatande, AcroFields fields) throws IOException, DocumentException {
        INTYGSDATUM.setField(fields, utlatande.getGrundData().getSigneringsdatum().format(DateTimeFormatter.ofPattern("yyMMdd")));
        Vardenhet vardenhet = utlatande.getGrundData().getSkapadAv().getVardenhet();
        VARDINRATTNINGENS_NAMN.setField(fields, vardenhet.getEnhetsnamn());
        String adressOrt = String.format("%s, %s, %s", vardenhet.getPostort(), vardenhet.getPostadress(), vardenhet.getPostnummer());
        if (ADRESS_OCH_ORT.fieldFits(fields, adressOrt)) {
            ADRESS_OCH_ORT.setField(fields, adressOrt);
        } else {
            adressOrt = String.format("%s, %s", vardenhet.getPostort(), vardenhet.getPostadress());
            ADRESS_OCH_ORT.setField(fields, adressOrt);
        }
        TELEFON.setField(fields, vardenhet.getTelefonnummer());
        NAMNFORTYDLIGANDE.setField(fields, utlatande.getGrundData().getSkapadAv().getFullstandigtNamn());

        populateAvslutSpecialist(utlatande, fields);

        List<String> befattningar = utlatande.getGrundData().getSkapadAv().getBefattningar();
        ST_LAKARE_CHECK.setField(fields, befattningar.contains(BEFATTNINGSKOD_LAKARE_LEG_ST));
        AT_LAKARE_CHECK.setField(fields, befattningar.contains(BEFATTNINGSKOD_LAKARE_EJ_LEG_AT));
    }

    private void populateAvslutSpecialist(Utlatande utlatande, AcroFields fields) throws IOException, DocumentException {
        List<String> specialiteter = utlatande.getGrundData().getSkapadAv().getSpecialiteter();
        if (specialiteter.size() > 0) {
            SPECIALISTKOMPETENS_CHECK.setField(fields, true);

            int index = specialiteter.indexOf(SPECIALIST_I_ALLMANMEDICIN_TITLE);
            if (index > -1) {
                SPECIALISTKOMPETENS_BESKRVNING.setField(fields, specialiteter.get(index));
            } else {
                SPECIALISTKOMPETENS_BESKRVNING.setField(fields, specialiteter.stream().collect(Collectors.joining(", ")));
            }
        }
    }

    private static final class CheckField {
        private final String field;

        private CheckField(String field) {
            this.field = field;
        }

        public void setField(AcroFields fields, Boolean checkField) throws IOException, DocumentException {
            if (checkField != null && checkField) {
                fields.setField(field, "On");
            }
        }
    }

    private static class CheckGroupField<T extends Enum<?>> {
        private final Map<T, String> fieldMap = new HashMap<>();

        public void addField(T code, String field) {
            fieldMap.put(code, field);
        }

        public void setField(AcroFields fields, T code) throws IOException, DocumentException {
            String field = fieldMap.get(code);

            if (field != null) {
                fields.setField(field, "On");
            }
        }
    }

    private static final class YesNoField {
        private final String yesField;

        private final String noField;

        private YesNoField(String yesField, String noField) {
            this.yesField = yesField;
            this.noField = noField;
        }

        public void setField(AcroFields fields, Boolean setYes) throws IOException, DocumentException {
            if (setYes != null) {
                fields.setField(setYes ? yesField : noField, "On");
            }
        }
    }

    private static final class DecimalField {
        private static final int ADD_DECIMAL_PART = 10;

        private final String integerField;

        private final String decimalField;

        private DecimalField(String integerField, String decimalField) {
            this.integerField = integerField;
            this.decimalField = decimalField;
        }

        public void setField(AcroFields fields, Double value) throws IOException, DocumentException {
            if (value != null) {
                long integerPart = Math.round(Math.floor(value));
                long decimalPart = Math.round((value - integerPart) * ADD_DECIMAL_PART);

                fields.setField(integerField, String.valueOf(integerPart));
                fields.setField(decimalField, String.valueOf(decimalPart));
            }
        }
    }

    private static final class StringField {
        private final String field;

        private StringField(String field) {
            this.field = field;
        }

        public void setField(AcroFields fields, String value) throws IOException, DocumentException {
            if (value != null) {
                fields.setField(field, value);
            }
        }

        public boolean fieldFits(AcroFields fields, String value) throws IOException, DocumentException {
            List<AcroFields.FieldPosition> positions = fields.getFieldPositions(field);
            if (positions.size() > 0) {
                AcroFields.FieldPosition position = positions.get(0);
                float fieldWidth = position.position.getWidth();

                PdfDictionary dict = fields.getFieldItem(field).getMerged(0);
                if (dict == null) {
                    return true;
                }
                PdfDictionary dr = dict.getAsDict(PdfName.DR);
                if (dr == null) {
                    return true;
                }
                PdfDictionary font = dr.getAsDict(PdfName.FONT);
                if (font == null) {
                    return true;
                }

                Object[] fontInfo = AcroFields.splitDAelements(dict.getAsString(PdfName.DA).toString());
                if (fontInfo.length >= 2) {
                    String fontName = (String) fontInfo[0];
                    float fontSize = ((Float) fontInfo[1]).floatValue();

                    PdfIndirectReference fontRef = font.getAsIndirectObject(new PdfName(fontName));
                    if (fontRef != null) {
                        BaseFont fieldFont = BaseFont.createFont((PRIndirectReference) fontRef);
                        if (fieldFont != null) {
                            if (fieldFont.getWidthPoint(value, fontSize) > fieldWidth) {
                                return false;
                            }
                        }
                    }
                }
            }
            return true;
        }
    }
}
