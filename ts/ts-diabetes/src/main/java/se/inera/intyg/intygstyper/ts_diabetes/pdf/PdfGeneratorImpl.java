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
package se.inera.intyg.intygstyper.ts_diabetes.pdf;

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
import se.inera.intyg.intygstyper.ts_diabetes.model.internal.*;
import se.inera.intyg.intygstyper.ts_diabetes.support.TsDiabetesEntryPoint;
import se.inera.intyg.intygstyper.ts_parent.codes.DiabetesKod;
import se.inera.intyg.intygstyper.ts_parent.codes.IdKontrollKod;
import se.inera.intyg.intygstyper.ts_parent.pdf.PdfGenerator;
import se.inera.intyg.intygstyper.ts_parent.pdf.PdfGeneratorException;

public class PdfGeneratorImpl implements PdfGenerator<Utlatande> {

    @Autowired(required = false)
    private IntygTextsService intygTexts;

    private static final String PDF_PATH_V02_U06 = "pdf/TSTRK1031-V02U06.pdf";

    // Constants for printing ID and origin in left margin
    private static final int MARGIN_TEXT_START_X = 46;
    private static final int MARGIN_TEXT_START_Y = 137;
    private static final int MARGIN_TEXT_FONTSIZE = 7;
    private static final String MINA_INTYG_MARGIN_TEXT = "Intyget är utskrivet från Mina Intyg.";
    private static final String WEBCERT_MARGIN_TEXT = "Intyget är utskrivet från Webcert.";

    private static final StringField INVANARE_ADRESS_FALT1 = new StringField("Falt__1");
    private static final StringField INVANARE_ADRESS_FALT2 = new StringField("Falt__2");
    private static final StringField INVANARE_ADRESS_FALT3 = new StringField("Falt__3");
    private static final StringField INVANARE_PERSONNUMMER = new StringField("Falt__4");

    private static final CheckGroupField<IntygAvserKategori> INTYG_AVSER;
    private static final String SPECIALIST_I_ALLMANMEDICIN_TITLE = "Specialist i allmänmedicin";

    static {
        INTYG_AVSER = new CheckGroupField<>();
        INTYG_AVSER.addField(IntygAvserKategori.AM, "Falt_4");
        INTYG_AVSER.addField(IntygAvserKategori.A1, "Falt_5");
        INTYG_AVSER.addField(IntygAvserKategori.A2, "Falt_6");
        INTYG_AVSER.addField(IntygAvserKategori.A, "Falt_7");
        INTYG_AVSER.addField(IntygAvserKategori.B, "Falt_8");
        INTYG_AVSER.addField(IntygAvserKategori.BE, "Falt_9");
        INTYG_AVSER.addField(IntygAvserKategori.TRAKTOR, "Falt_10");
        INTYG_AVSER.addField(IntygAvserKategori.C1, "Falt_11");
        INTYG_AVSER.addField(IntygAvserKategori.C1E, "Falt_12");
        INTYG_AVSER.addField(IntygAvserKategori.C, "Falt_13");
        INTYG_AVSER.addField(IntygAvserKategori.CE, "Falt_14");
        INTYG_AVSER.addField(IntygAvserKategori.D1, "Falt_15");
        INTYG_AVSER.addField(IntygAvserKategori.D1E, "Falt_16");
        INTYG_AVSER.addField(IntygAvserKategori.D, "Falt_17");
        INTYG_AVSER.addField(IntygAvserKategori.DE, "Falt_18");
        INTYG_AVSER.addField(IntygAvserKategori.TAXI, "Falt_19");
    }

    private static final CheckField ID_KONTROLL_IDKORT = new CheckField("Falt_20");
    private static final CheckField ID_KONTROLL_FORETAG_TJANSTEKORT = new CheckField("Falt_21");
    private static final CheckField ID_KONTROLL_SVENSKT_KORKORT = new CheckField("Falt_22");
    private static final CheckField ID_KONTROLL_PERSONLIG_KANNEDOM = new CheckField("Falt_23");
    private static final CheckField ID_KONTROLL_FORSAKRAN = new CheckField("Falt_24");
    private static final CheckField ID_KONTROLL_PASS = new CheckField("Falt_25");

    private static final StringField DIABETES_AR_FOR_DIAGNOS = new StringField("Falt__31");
    private static final CheckField DIABETES_TYP_1 = new CheckField("Falt_32");
    private static final CheckField DIABETES_TYP_2 = new CheckField("Falt_33");

    private static final CheckField DIABETIKER_ENBART_KOST = new CheckField("Falt_34");
    private static final CheckField DIABETIKER_TABLETTBEHANDLING = new CheckField("Falt_35");
    private static final CheckField DIABETIKER_INSULINBEHANDLING = new CheckField("Falt_36");
    private static final CheckField DIABETIKER_INSULINBEHANDLING_SEDAN_CHECK = new CheckField("Falt_37");
    private static final StringField DIABETIKER_INSULINBEHANDLING_SEDAN = new StringField("Falt__38");
    private static final StringField DIABETIKER_ANNAN_BEHANDLING = new StringField("Falt__39");

    private static final YesNoField KUNSKAP_ATGARD_HYPOGLYKEMI = new YesNoField("Falt_40", "Falt_41");
    private static final YesNoField HYPOGLYKEMIER_MED_TECKEN_PA_NEDSATT_HJARNFUNKTION = new YesNoField("Falt_44",
            "Falt_45");
    private static final YesNoField SAKNAR_FORMAGA_KANNA_HYPOGLYKEMI = new YesNoField("Falt_48", "Falt_49");
    private static final YesNoField ALLVARLIG_HYPOGLYKEMI = new YesNoField("Falt_27", "Falt_28");
    private static final StringField ALLVARLIG_HYPOGLYKEMI_ANTAL = new StringField("Falt__50");
    private static final YesNoField ALLVARLIG_HYPOGLYKEMI_I_TRAFIKEN = new YesNoField("Falt_51", "Falt_52");
    private static final StringField ALLVARLIG_HYPOGLYKEMI_I_TRAFIKEN_BESKRIVNING = new StringField("Falt__501");

    private static final YesNoField EGENOVERVAKNING_BLODGLUKOS = new YesNoField("Falt_53", "Falt_54");
    private static final YesNoField ALLVARLIG_HYPOGLYKEMI_VAKET_TILLSTAND = new YesNoField("Falt_55", "Falt_56");
    private static final StringField ALLVARLIG_HYPOGLYKEMI_VAKET_TILLSTAND_DATUM = new StringField("Falt__61");

    private static final YesNoField OGONLAKARINTYG = new YesNoField("Falt_62", "Falt_63");
    private static final YesNoField SYNFALTSUNDERSOKNING = new YesNoField("Falt_64", "Falt_65");
    private static final DecimalField EJ_KORRIGERAD_SYNSKARPA_HOGER = new DecimalField("Falt__66", "Falt__67");
    private static final DecimalField EJ_KORRIGERAD_SYNSKARPA_VANSTER = new DecimalField("Falt__70", "Falt__71");
    private static final DecimalField EJ_KORRIGERAD_SYNSKARPA_BINOKULART = new DecimalField("Falt__74", "Falt__75");
    private static final DecimalField KORRIGERAD_SYNSKARPA_HOGER = new DecimalField("Falt__68", "Falt__69");
    private static final DecimalField KORRIGERAD_SYNSKARPA_VANSTER = new DecimalField("Falt__72", "Falt__73");
    private static final DecimalField KORRIGERAD_SYNSKARPA_BINOKULART = new DecimalField("Falt__76", "Falt__77");
    private static final YesNoField DIPLOPI = new YesNoField("Falt_78", "Falt_79");

    private static final CheckGroupField<BedomningKorkortstyp> BEDOMNING;

    static {
        BEDOMNING = new CheckGroupField<>();
        BEDOMNING.addField(BedomningKorkortstyp.AM, "Falt_108");
        BEDOMNING.addField(BedomningKorkortstyp.A1, "Falt_81");
        BEDOMNING.addField(BedomningKorkortstyp.A2, "Falt_107");
        BEDOMNING.addField(BedomningKorkortstyp.A, "Falt_82");
        BEDOMNING.addField(BedomningKorkortstyp.B, "Falt_83");
        BEDOMNING.addField(BedomningKorkortstyp.BE, "Falt_84");
        BEDOMNING.addField(BedomningKorkortstyp.TRAKTOR, "Falt_85");
        BEDOMNING.addField(BedomningKorkortstyp.C1, "Falt_80");
        BEDOMNING.addField(BedomningKorkortstyp.C1E, "Falt_109");
        BEDOMNING.addField(BedomningKorkortstyp.C, "Falt_86");
        BEDOMNING.addField(BedomningKorkortstyp.CE, "Falt_87");
        BEDOMNING.addField(BedomningKorkortstyp.D1, "Falt_110");
        BEDOMNING.addField(BedomningKorkortstyp.D1E, "Falt_111");
        BEDOMNING.addField(BedomningKorkortstyp.D, "Falt_88");
        BEDOMNING.addField(BedomningKorkortstyp.DE, "Falt_89");
        BEDOMNING.addField(BedomningKorkortstyp.TAXI, "Falt_90");
    }

    private static final CheckField BEDOMNING_INTE_TA_STALLNING = new CheckField("Falt_91");

    private static final YesNoField LAMPLIGHET_INNEHA_BEHORIGHET_TILL_KORNINGAR_OCH_ARBETSFORMER = new YesNoField(
            "Falt_92", "Falt_93");

    private static final StringField OVRIG_BESKRIVNING = new StringField("FaltDiv6");

    private static final StringField BEDOMNING_BOR_UNDERSOKAS_SPECIALIST = new StringField("Falt__94");

    private static final StringField INTYGSDATUM = new StringField("Falt__95");
    private static final StringField VARDINRATTNINGENS_NAMN = new StringField("Falt__96");
    private static final StringField ADRESS_OCH_ORT = new StringField("Falt__97");
    private static final StringField TELEFON = new StringField("Falt__98");
    private static final StringField NAMNFORTYDLIGANDE = new StringField("Falt__101");

    private static final StringField SPECIALISTKOMPETENS_BESKRVNING = new StringField("Falt__102");

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
            populatePdfFields(utlatande, fields);

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
            return PDF_PATH_V02_U06;
        }
        IntygTexts texts = intygTexts.getIntygTextsPojo(TsDiabetesEntryPoint.MODULE_ID, textVersion);
        if (texts == null) {
            return PDF_PATH_V02_U06;
        }
        String path = texts.getPdfPath();
        if (path == null) {
            return PDF_PATH_V02_U06;
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
     *            {@link se.inera.intyg.intygstyper.ts_diabetes.model.internal.Utlatande} containing data for
     *            populating the pdf
     * @param fields
     *            The fields of the pdf
     * @throws DocumentException
     * @throws IOException
     */
    private void populatePdfFields(Utlatande utlatande, AcroFields fields) throws IOException, DocumentException {
        populatePatientInfo(utlatande.getGrundData().getPatient(), fields);
        populateIntygAvser(utlatande.getIntygAvser(), fields);
        populateIdkontroll(utlatande.getVardkontakt(), fields);
        populateAllmant(utlatande.getDiabetes(), fields);
        populateHypoglykemier(utlatande.getHypoglykemier(), fields);
        populateSynintyg(utlatande.getSyn(), fields);
        populateBedomning(utlatande.getBedomning(), fields);
        populateOvrigt(utlatande.getKommentar(), fields);
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

    private void populateAllmant(Diabetes diabetes, AcroFields fields) throws IOException, DocumentException {
        DIABETES_AR_FOR_DIAGNOS.setField(fields, diabetes.getObservationsperiod());
        if (diabetes.getDiabetestyp() != null) {
            if (diabetes.getDiabetestyp().equals(DiabetesKod.DIABETES_TYP_1.name())) {
                DIABETES_TYP_1.setField(fields, true);
            } else if (diabetes.getDiabetestyp().equals(DiabetesKod.DIABETES_TYP_2.name())) {
                DIABETES_TYP_2.setField(fields, true);
            }
        }
        DIABETIKER_ENBART_KOST.setField(fields, diabetes.getEndastKost());
        DIABETIKER_TABLETTBEHANDLING.setField(fields, diabetes.getTabletter());
        DIABETIKER_INSULINBEHANDLING.setField(fields, diabetes.getInsulin());
        DIABETIKER_INSULINBEHANDLING_SEDAN_CHECK.setField(fields, diabetes.getInsulin());
        DIABETIKER_INSULINBEHANDLING_SEDAN.setField(fields, diabetes.getInsulinBehandlingsperiod());
        DIABETIKER_ANNAN_BEHANDLING.setField(fields, diabetes.getAnnanBehandlingBeskrivning());
    }

    private void populateHypoglykemier(Hypoglykemier hypoglykemier, AcroFields fields) throws IOException,
            DocumentException {
        KUNSKAP_ATGARD_HYPOGLYKEMI.setField(fields, hypoglykemier.getKunskapOmAtgarder());
        HYPOGLYKEMIER_MED_TECKEN_PA_NEDSATT_HJARNFUNKTION.setField(fields,
                hypoglykemier.getTeckenNedsattHjarnfunktion());
        SAKNAR_FORMAGA_KANNA_HYPOGLYKEMI.setField(fields, hypoglykemier.getSaknarFormagaKannaVarningstecken());
        ALLVARLIG_HYPOGLYKEMI.setField(fields, hypoglykemier.getAllvarligForekomst());
        ALLVARLIG_HYPOGLYKEMI_ANTAL.setField(fields, hypoglykemier.getAllvarligForekomstBeskrivning());
        ALLVARLIG_HYPOGLYKEMI_I_TRAFIKEN.setField(fields, hypoglykemier.getAllvarligForekomstTrafiken());
        ALLVARLIG_HYPOGLYKEMI_I_TRAFIKEN_BESKRIVNING.setField(fields,
                hypoglykemier.getAllvarligForekomstTrafikBeskrivning());
        EGENOVERVAKNING_BLODGLUKOS.setField(fields, hypoglykemier.getEgenkontrollBlodsocker());
        ALLVARLIG_HYPOGLYKEMI_VAKET_TILLSTAND.setField(fields, hypoglykemier.getAllvarligForekomstVakenTid());
        if (hypoglykemier.getAllvarligForekomstVakenTidObservationstid() != null) {
            ALLVARLIG_HYPOGLYKEMI_VAKET_TILLSTAND_DATUM.setField(fields, hypoglykemier
                    .getAllvarligForekomstVakenTidObservationstid().getDate().replace("-", ""));
        }
    }

    private void populateSynintyg(Syn syn, AcroFields fields) throws IOException, DocumentException {
        OGONLAKARINTYG.setField(fields, syn.getSeparatOgonlakarintyg());
        SYNFALTSUNDERSOKNING.setField(fields, syn.getSynfaltsprovningUtanAnmarkning());

        if (syn.getHoger() != null) {
            EJ_KORRIGERAD_SYNSKARPA_HOGER.setField(fields, syn.getHoger().getUtanKorrektion());
            KORRIGERAD_SYNSKARPA_HOGER.setField(fields, syn.getHoger().getMedKorrektion());
        }
        if (syn.getVanster() != null) {
            EJ_KORRIGERAD_SYNSKARPA_VANSTER.setField(fields, syn.getVanster().getUtanKorrektion());
            KORRIGERAD_SYNSKARPA_VANSTER.setField(fields, syn.getVanster().getMedKorrektion());
        }
        if (syn.getBinokulart() != null) {
            EJ_KORRIGERAD_SYNSKARPA_BINOKULART.setField(fields, syn.getBinokulart().getUtanKorrektion());
            KORRIGERAD_SYNSKARPA_BINOKULART.setField(fields, syn.getBinokulart().getMedKorrektion());
        }

        DIPLOPI.setField(fields, syn.getDiplopi());
    }

    private void populateBedomning(Bedomning bedomning, AcroFields fields) throws IOException, DocumentException {
        for (BedomningKorkortstyp korkortstyp : bedomning.getKorkortstyp()) {
            BEDOMNING.setField(fields, korkortstyp);
        }
        BEDOMNING_INTE_TA_STALLNING.setField(fields, bedomning.getKanInteTaStallning());
        LAMPLIGHET_INNEHA_BEHORIGHET_TILL_KORNINGAR_OCH_ARBETSFORMER.setField(fields,
                bedomning.getLamplighetInnehaBehorighet());
        BEDOMNING_BOR_UNDERSOKAS_SPECIALIST.setField(fields, bedomning.getLakareSpecialKompetens());
    }

    private void populateOvrigt(String kommentar, AcroFields fields) throws IOException, DocumentException {
        OVRIG_BESKRIVNING.setField(fields, kommentar);
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
    }

    private void populateAvslutSpecialist(Utlatande utlatande, AcroFields fields) throws IOException, DocumentException {
        List<String> specialiteter = utlatande.getGrundData().getSkapadAv().getSpecialiteter();
        if (specialiteter.size() > 0) {

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
