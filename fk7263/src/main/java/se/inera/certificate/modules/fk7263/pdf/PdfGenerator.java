package se.inera.certificate.modules.fk7263.pdf;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;

import se.inera.certificate.model.InternalLocalDateInterval;
import se.inera.certificate.model.util.Strings;
import se.inera.certificate.modules.fk7263.model.internal.Fk7263Intyg;
import se.inera.certificate.modules.support.ApplicationOrigin;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.CMYKColor;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

/**
 * @author andreaskaltenbach
 */
public class PdfGenerator {

    // General stuff
    private static final float LINE_WIDTH = 0.6f;

    private static final int SIGNATURE_NOT_REQUIRED_PADDING_BOTTOM = 10;
    private static final int SIGNATURE_NOT_REQUIRED_PADDING_LEFT = 5;
    private static final int SIGNATURE_NOT_REQUIRED_FONT_SIZE = 8;
    // Coordinates for masking "Skicka till försäkringskassan.."
    private static final int MASK_HEIGTH = 70;
    private static final int MASK_WIDTH = 250;
    private static final int MASK_START_X = 300;
    private static final int MASK_START_Y = 670;

    // Constants used for watermarking
    private static final int MARK_AS_COPY_HEIGTH = 30;
    private static final int MARK_AS_COPY_WIDTH = 250;
    private static final int MARK_AS_COPY_START_X = 50;
    private static final int MARK_AS_COPY_START_Y = 690;
    private static final int WATERMARK_TEXT_PADDING = 10;
    private static final int WATERMARK_FONTSIZE = 12;

    private static final String WATERMARK_TEXT = "Detta är en utskrift av ett elektroniskt intyg";

    // Right margin texts
    private static final String MINA_INTYG_MARGIN_TEXT = "Intyget är utskrivet från Mina Intyg.";
    private static final String WEBCERT_MARGIN_TEXT = "Intyget är elektroniskt undertecknat. Intyget är utskrivet från Webcert.";

    // Constants for printing ID and origin in right margin
    private static final int MARGIN_TEXT_START_X = 565;
    private static final int MARGIN_TEXT_START_Y = 27;
    private static final int MARGIN_TEXT_FONTSIZE = 7;

    // Constants for printing "Fysisk underskrift krävs ej av intygsmottagare"
    private static final int SIGNATURE_NOT_REQUIRED_START_X = 263;
    private static final int SIGNATURE_NOT_REQUIRED_START_Y = 105;
    private static final float SIGNATURE_NOT_REQUIRED_WIDTH = 289.6f;
    private static final int SIGNATURE_NOT_REQUIRED_HEIGHT = 25;
    private static final String SIGNATURE_NOT_REQUIRED_TEXT = "Fysisk underskrift krävs ej av intygsmottagare";
    private static final CMYKColor SIGNATURE_NOT_REQUIRED_COLOR = new CMYKColor(0.01f, 0.04f, 0.42f, 0.0f);

    private static final String DATE_PATTERN = "yyyy-MM-dd";

    private static final String PATIENT_NAME = "form1[0].subform[0].flt_PatNamn[0]";
    private static final String PATIENT_SSN = "form1[0].subform[0].flt_PatPersonnummer[0]";

    private static final String DIAGNOS = "form1[0].subform[0].flt_DiagnosDiagnoser_flt2[0]";

    private static final String DISEASE_CAUSE = "form1[0].subform[0].flt_AktuelltSjukdomsForlopp_flt3[0]";

    private static final String SUSPENSION_DUE_TO_INFECTION = "form1[0].subform[0].ksr_AvstangningEnlSMiL_flt1[0]";

    private static final String DIAGNOS_CODE = "form1[0].subform[0].flt_Diagnoskod_flt2[0]";

    private static final String BASED_ON_EXAMINATION = "form1[0].subform[0].ksr_MinUndersokningAvPat_flt4[0]";

    private static final String BASED_ON_EXAMINATION_TIME = "form1[0].subform[0].flt_DatumMinUndersokningAvPat_flt4[0]";

    private static final String BASED_ON_PHONE_CONTACT = "form1[0].subform[0].ksr_MinTelefonKontaktMedPat_flt4[0]";

    private static final String BASED_ON_PHONE_CONTACT_TIME = "form1[0].subform[0].flt_DatumMinTelefonKontaktMedPat_flt4[0]";

    private static final String BASED_ON_JOURNAL = "form1[0].subform[0].ksr_Journaluppgifter_flt4[0]";

    private static final String BASED_ON_JOURNAL_TIME = "form1[0].subform[0].flt_DatumJournaluppgifter_flt4[0]";

    private static final String BASED_ON_OTHER = "form1[0].subform[0].ksr_AnnatAngeVadiFalt13_flt4[0]";

    private static final String BASED_ON_OTHER_DATE = "form1[0].subform[0].flt_DatumAnnatAngeVadiFalt13_flt4[0]";

    private static final String DISABILITIES = "form1[0].subform[0].flt_Funktionsnedsattning_flt4[0]";

    private static final String ACTIVITY_LIMITATION = "form1[0].subform[0].flt_Aktivitetsbegransning_flt5[0]";

    private static final String RECOMMENDATIONS_CONTACT_AF = "form1[0].subform[0].ksr_KontaktMedArbetsformedlingen_flt6a[0]";

    private static final String RECOMMENDATIONS_CONTACT_COMPANY_CARE = "form1[0].subform[0].ksr_KontaktMedForetagsHalsovarden_flt6a[0]";

    private static final String RECOMMENDATIONS_OTHER = "form1[0].subform[0].ksr_OvrigtAngeVad_flt6a[0]";

    private static final String RECOMMENDATIONS_OTHER_TEXT = "form1[0].subform[0].flt_OvrigtAngeVad_flt6a[0]";

    private static final String MEASURES_CURRENT = "form1[0].subform[0].ksr_InomSjukvardenAngeVilken_flt6b[0]";

    private static final String MEASURES_CURRENT_TEXT = "form1[0].subform[0].flt_InomSjukvardenAngeVilken_flt6b[0]";

    private static final String MEASURES_OTHER = "form1[0].subform[0].ksr_AnnanAtgardAngeVilken_flt6b[0]";

    private static final String MEASURES_OTHER_TEXT = "form1[0].subform[0].flt_AnnanAtgardAngeVilken_flt6b[0]";

    private static final String RECOMMENDATION_REHAB_YES = "form1[0].subform[1].ksr_Ja_flt7[0]";

    private static final String RECOMMENDATION_REHAB_NO = "form1[0].subform[1].ksr_Nej_flt7[0]";

    private static final String RECOMMENDATION_REHAB_UNKNOWN = "form1[0].subform[1].ksr_GarInteAttBedoma_flt7[0]";

    private static final String CURRENT_WORK = "form1[0].subform[1].ksr_NuvarandeArbete_flt8a[0]";

    private static final String CURRENT_WORK_TEXT_1 = "form1[0].subform[1].flt_NuvarandeArbete_flt8a[0]";

    private static final String UNEMPLOYMENT = "form1[0].subform[1].ksr_Arbetsloshet_flt8a[0]";

    private static final String PARENTAL_LEAVE = "form1[0].subform[1].ksr_ForaldraledigMedForaldrapenning_flt8a[0]";

    private static final String REDUCED_WORK_CAPACITY_25 = "form1[0].subform[1].ksr_NedsattMed14_flt8b[0]";

    private static final String REDUCED_WORK_CAPACITY_50 = "form1[0].subform[1].ksr_NedsattMedHalften_flt8b[0]";

    private static final String REDUCED_WORK_CAPACITY_75 = "form1[0].subform[1].ksr_NedsattMed34_flt8b[0]";

    private static final String REDUCED_WORK_CAPACITY_FULL = "form1[0].subform[1].ksr_HeltNedsatt_flt8b[0]";

    private static final String REDUCED_WORK_CAPACITY_25_FROM = "form1[0].subform[1].flt_NedsattMed14From_flt8b[0]";

    private static final String REDUCED_WORK_CAPACITY_25_TOM = "form1[0].subform[1].flt_NedsattMed14Tom_flt8b[0]";

    private static final String REDUCED_WORK_CAPACITY_50_FROM = "form1[0].subform[1].flt_NedsattMedHalftenFrom_flt8b[0]";

    private static final String REDUCED_WORK_CAPACITY_50_TOM = "form1[0].subform[1].flt_NedsattMedHalftenTom_flt8b[0]";

    private static final String REDUCED_WORK_CAPACITY_75_FROM = "form1[0].subform[1].flt_NedsattMed34From_flt8b[0]";

    private static final String REDUCED_WORK_CAPACITY_75_TOM = "form1[0].subform[1].flt_NedsattMed34Tom_flt8b[0]";

    private static final String REDUCED_WORK_CAPACITY_FULL_FROM = "form1[0].subform[1].flt_HeltNedsattFrom_flt8b[0]";

    private static final String REDUCED_WORK_CAPACITY_FULL_TOM = "form1[0].subform[1].flt_HeltNedsattTom_flt8b[0]";

    private static final String WORK_CAPACITY_TEXT = "form1[0].subform[1].flt_PatArbetsformaga_flt9[0]";

    private static final String WORK_CAPACITY_FORECAST_YES = "form1[0].subform[1].ksr_Ja_flt10[0]";

    private static final String WORK_CAPACITY_FORECAST_PARTLY = "form1[0].subform[1].ksr_JaDelvis_flt10[0]";

    private static final String WORK_CAPACITY_FORECAST_NO = "form1[0].subform[1].ksr_Nej_flt10[0]";

    private static final String WORK_CAPACITY_FORECAST_UNKNOWN = "form1[0].subform[1].ksr_GarInteAttBedoma_flt10[0]";

    private static final String RECOMMENDATION_TRAVEL_YES = "form1[0].subform[1].ksr_Ja_flt11[0]";

    private static final String RECOMMENDATION_TRAVEL_NO = "form1[0].subform[1].ksr_Nej_flt11[0]";

    private static final String CONTACT_WITH_FK = "form1[0].subform[1].ksr_Ja_flt12[0]";

    private static final String OTHER_INFORMATION = "form1[0].subform[1].flt_OvrigaUpplysningar_flt13[0]";

    private static final String SIGN_DATE = "form1[0].subform[1].flt_Datum_flt14[0]";

    private static final String SIGN_NAME = "form1[0].subform[1].flt_NamnfortydligandeMottagningsadrTel_flt15[0]";

    private static final String DOCTORCODE_AND_WORKPLACE = "form1[0].subform[1].flt_ForskrivarkodOchArbetsplatskod_flt17[0]";

    public static final String PDF_TEMPLATE = "pdf/RFV7263_009_J_003_statisk.pdf";

    private static final String DATE_FORMAT = "yyyyMMdd";

    private Fk7263Intyg intyg;
    private ByteArrayOutputStream outputStream;
    private AcroFields fields;

    public PdfGenerator(Fk7263Intyg intyg, ApplicationOrigin applicationOrigin) throws PdfGeneratorException {
        this(intyg, true, applicationOrigin);
    }

    public PdfGenerator(Fk7263Intyg intyg, boolean flatten, ApplicationOrigin applicationOrigin) throws PdfGeneratorException {
        try {
            this.intyg = intyg;

            outputStream = new ByteArrayOutputStream();

            PdfReader pdfReader = new PdfReader(PDF_TEMPLATE);
            PdfStamper pdfStamper = new PdfStamper(pdfReader, this.outputStream);
            fields = pdfStamper.getAcroFields();

            generatePdf();

            // Decorate PDF depending on the origin of the pdf-call
            switch (applicationOrigin) {
            case MINA_INTYG:
                maskSendToFkInformation(pdfStamper);
                markAsElectronicCopy(pdfStamper, WATERMARK_TEXT);
                createRightMarginText(pdfStamper, pdfReader.getNumberOfPages(), intyg.getId(), MINA_INTYG_MARGIN_TEXT);
                break;
            case WEBCERT:
                createRightMarginText(pdfStamper, pdfReader.getNumberOfPages(), intyg.getId(), WEBCERT_MARGIN_TEXT);
                createSignatureNotRequiredField(pdfStamper, pdfReader.getNumberOfPages());
                break;
            default:
                break;
            }

            pdfStamper.setFormFlattening(flatten);
            pdfStamper.close();

        } catch (Exception e) {
            throw new PdfGeneratorException(e);
        }
    }

    private void createSignatureNotRequiredField(PdfStamper pdfStamper, int lastPage) throws DocumentException, IOException {
        PdfContentByte addOverlay;
        addOverlay = pdfStamper.getOverContent(lastPage);
        addOverlay.saveState();
        addOverlay.setColorFill(SIGNATURE_NOT_REQUIRED_COLOR);
        addOverlay.setColorStroke(CMYKColor.BLACK);
        addOverlay.rectangle(SIGNATURE_NOT_REQUIRED_START_X, SIGNATURE_NOT_REQUIRED_START_Y, SIGNATURE_NOT_REQUIRED_WIDTH,
                SIGNATURE_NOT_REQUIRED_HEIGHT);
        addOverlay.setLineWidth(LINE_WIDTH);
        addOverlay.fillStroke();
        addOverlay.restoreState();
        // Do text
        addOverlay = pdfStamper.getOverContent(lastPage);
        addOverlay.saveState();
        BaseFont bf = BaseFont.createFont();
        addOverlay.beginText();
        addOverlay.setFontAndSize(bf, SIGNATURE_NOT_REQUIRED_FONT_SIZE);
        addOverlay.setTextMatrix(SIGNATURE_NOT_REQUIRED_START_X + SIGNATURE_NOT_REQUIRED_PADDING_LEFT, SIGNATURE_NOT_REQUIRED_START_Y
                + SIGNATURE_NOT_REQUIRED_PADDING_BOTTOM);
        addOverlay.showText(SIGNATURE_NOT_REQUIRED_TEXT);
        addOverlay.endText();
        addOverlay.restoreState();
    }

    // Mask the information regarding where to send a physical copy of this document
    private void maskSendToFkInformation(PdfStamper pdfStamper) {
        PdfContentByte addOverlay;
        addOverlay = pdfStamper.getOverContent(1);
        addOverlay.saveState();
        addOverlay.setColorFill(CMYKColor.WHITE);
        addOverlay.setColorStroke(CMYKColor.WHITE);
        addOverlay.rectangle(MASK_START_X, MASK_START_Y, MASK_WIDTH, MASK_HEIGTH);
        addOverlay.fillStroke();
        addOverlay.restoreState();
    }

    // Mark this document as a copy of an electronically signed document
    private void markAsElectronicCopy(PdfStamper pdfStamper, String watermarkText) throws DocumentException, IOException {
        PdfContentByte addOverlay;
        addOverlay = pdfStamper.getOverContent(1);
        addOverlay.saveState();
        addOverlay.setColorFill(CMYKColor.WHITE);
        addOverlay.setColorStroke(CMYKColor.RED);
        addOverlay.rectangle(MARK_AS_COPY_START_X, MARK_AS_COPY_START_Y, MARK_AS_COPY_WIDTH, MARK_AS_COPY_HEIGTH);
        addOverlay.stroke();
        addOverlay.restoreState();
        // Do text
        addOverlay = pdfStamper.getOverContent(1);
        addOverlay.saveState();
        BaseFont bf = BaseFont.createFont();
        addOverlay.beginText();
        addOverlay.setFontAndSize(bf, WATERMARK_FONTSIZE);
        addOverlay.setTextMatrix(MARK_AS_COPY_START_X + WATERMARK_TEXT_PADDING, MARK_AS_COPY_START_Y + WATERMARK_TEXT_PADDING);
        addOverlay.showText(watermarkText);
        addOverlay.endText();
        addOverlay.restoreState();
    }

    private void createRightMarginText(PdfStamper pdfStamper, int numberOfPages, String id, String text) throws DocumentException, IOException {
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

    public String generatePdfFilename() {
        return String.format("lakarutlatande_%s_%s-%s.pdf", intyg.getPatientPersonnummer(), intyg.getGiltighet()
                .getFrom().toString(DATE_FORMAT), intyg.getGiltighet().getTom().toString(DATE_FORMAT));
    }

    public byte[] getBytes() {
        return outputStream.toByteArray();
    }

    private void generatePdf() {
        fillPatientDetails();
        fillSignerNameAndAddress();

        fillSignerCodes();

        fillIsSuspenseDueToInfection();

        fillDiagnose();

        fillDiseaseCause();

        fillBasedOn();

        fillDisability();

        fillActivityLimitation();

        fillRecommendations();

        fillMeasures();

        fillRehabilitation();

        fillCapacityRelativeTo();

        fillCapacity();

        fillTravel();

        setField(CONTACT_WITH_FK, intyg.isKontaktMedFk());

        fillPrognose();

        fillOther();
    }

    private void fillPatientDetails() {
        fillText(PATIENT_NAME, intyg.getPatientNamn());
        fillText(PATIENT_SSN, intyg.getPatientPersonnummer());
    }

    private void fillSignerNameAndAddress() {
        fillText(SIGN_NAME, intyg.getNamnfortydligandeOchAdress());
        fillText(SIGN_DATE, intyg.getSigneringsdatum().toString(DATE_PATTERN));
    }

    private void fillTravel() {
        if (intyg.isRessattTillArbeteAktuellt()) {
            checkField(RECOMMENDATION_TRAVEL_YES);
        }
        if (intyg.isRessattTillArbeteEjAktuellt()) {
            checkField(RECOMMENDATION_TRAVEL_NO);
        }
    }

    private void fillPrognose() {
        setField(WORK_CAPACITY_FORECAST_YES, intyg.isArbetsformataPrognosJa());
        setField(WORK_CAPACITY_FORECAST_PARTLY, intyg.isArbetsformataPrognosJaDelvis());
        setField(WORK_CAPACITY_FORECAST_NO, intyg.isArbetsformataPrognosNej());
        if (intyg.isArbetsformataPrognosGarInteAttBedoma()) {
            checkField(WORK_CAPACITY_FORECAST_UNKNOWN);
        }
        fillText(WORK_CAPACITY_TEXT, intyg.getArbetsformagaPrognos());
    }

    private void fillNedsattning(InternalLocalDateInterval interval, String checkboxFieldName, String fromDateFieldName,
            String toDateFieldName) {
        if (interval != null) {
            checkField(checkboxFieldName);
            fillText(fromDateFieldName, interval.getFrom().getDate());
            fillText(toDateFieldName, interval.getTom().getDate());
        }
    }

    private void fillCapacity() {
        fillNedsattning(intyg.getNedsattMed100(), REDUCED_WORK_CAPACITY_FULL, REDUCED_WORK_CAPACITY_FULL_FROM,
                REDUCED_WORK_CAPACITY_FULL_TOM);
        fillNedsattning(intyg.getNedsattMed75(), REDUCED_WORK_CAPACITY_75, REDUCED_WORK_CAPACITY_75_FROM,
                REDUCED_WORK_CAPACITY_75_TOM);
        fillNedsattning(intyg.getNedsattMed50(), REDUCED_WORK_CAPACITY_50, REDUCED_WORK_CAPACITY_50_FROM,
                REDUCED_WORK_CAPACITY_50_TOM);
        fillNedsattning(intyg.getNedsattMed25(), REDUCED_WORK_CAPACITY_25, REDUCED_WORK_CAPACITY_25_FROM,
                REDUCED_WORK_CAPACITY_25_TOM);
    }

    private void fillCapacityRelativeTo() {
        if (intyg.getNuvarandeArbetsuppgifter() != null) {
            checkField(CURRENT_WORK);
            fillText(CURRENT_WORK_TEXT_1, intyg.getNuvarandeArbetsuppgifter());
        }
        setField(UNEMPLOYMENT, intyg.isArbetsloshet());
        setField(PARENTAL_LEAVE, intyg.isForaldrarledighet());
    }

    private void fillRehabilitation() {
        setField(RECOMMENDATION_REHAB_YES, intyg.isRehabiliteringAktuell());
        setField(RECOMMENDATION_REHAB_NO, intyg.isRehabiliteringEjAktuell());
        setField(RECOMMENDATION_REHAB_UNKNOWN, intyg.isRehabiliteringGarInteAttBedoma());
    }

    private void fillRecommendations() {
        setField(RECOMMENDATIONS_CONTACT_AF, intyg.isRekommendationKontaktArbetsformedlingen());
        setField(RECOMMENDATIONS_CONTACT_COMPANY_CARE, intyg.isRekommendationKontaktForetagshalsovarden());
        if (intyg.getRekommendationOvrigt() != null) {
            checkField(RECOMMENDATIONS_OTHER);
            fillText(RECOMMENDATIONS_OTHER_TEXT, intyg.getRekommendationOvrigt());
        }
    }

    private void fillActivityLimitation() {
        fillText(ACTIVITY_LIMITATION, intyg.getAktivitetsbegransning());
    }

    private void fillDisability() {
        fillText(DISABILITIES, intyg.getFunktionsnedsattning());
    }

    private void fillOther() {
        fillText(OTHER_INFORMATION, intyg.getKommentar());
    }

    private String buildOtherDiagnoses() {
        ArrayList<String> parts = new ArrayList<>();

        if (isValidString(intyg.getDiagnosBeskrivning())) {
            parts.add(intyg.getDiagnosBeskrivning());
        }
        if (isValidString(intyg.getDiagnosBeskrivning1())) {
            parts.add(intyg.getDiagnosBeskrivning1());
        }
        if (isValidString(intyg.getDiagnosKod2())) {
            parts.add(intyg.getDiagnosKod2() + " " + intyg.getDiagnosBeskrivning2());
        }
        if (isValidString(intyg.getDiagnosKod3())) {
            parts.add(intyg.getDiagnosKod3() + " " + intyg.getDiagnosBeskrivning3());
        }
        if (intyg.getSamsjuklighet() != null && intyg.getSamsjuklighet()) {
            parts.add("Samsjuklighet föreligger");
        }
        return StringUtils.trimToNull(Strings.join(", ", parts));
    }

    private boolean isValidString(String string) {
        return string != null && !string.isEmpty();
    }

    private void fillBasedOn() {

        if (intyg.getUndersokningAvPatienten() != null) {
            checkField(BASED_ON_EXAMINATION);
            fillText(BASED_ON_EXAMINATION_TIME, intyg.getUndersokningAvPatienten().getDate());
        }

        if (intyg.getTelefonkontaktMedPatienten() != null) {
            checkField(BASED_ON_PHONE_CONTACT);
            fillText(BASED_ON_PHONE_CONTACT_TIME, intyg.getTelefonkontaktMedPatienten().getDate());
        }

        if (intyg.getJournaluppgifter() != null) {
            checkField(BASED_ON_JOURNAL);
            fillText(BASED_ON_JOURNAL_TIME, intyg.getJournaluppgifter().getDate());
        }

        if (intyg.getAnnanReferens() != null) {
            checkField(BASED_ON_OTHER);
            fillText(BASED_ON_OTHER_DATE, intyg.getAnnanReferens().getDate());
        }
    }

    private void fillMeasures() {
        if (intyg.getAtgardInomSjukvarden() != null) {
            checkField(MEASURES_CURRENT);
            fillText(MEASURES_CURRENT_TEXT, intyg.getAtgardInomSjukvarden());
        }

        if (intyg.getAnnanAtgard() != null) {
            checkField(MEASURES_OTHER);
            fillText(MEASURES_OTHER_TEXT, intyg.getAnnanAtgard());
        }
    }

    private void fillDiseaseCause() {
        fillText(DISEASE_CAUSE, intyg.getSjukdomsforlopp());
    }

    private void fillIsSuspenseDueToInfection() {
        setField(SUSPENSION_DUE_TO_INFECTION, intyg.isAvstangningSmittskydd());
    }

    private void fillSignerCodes() {
        fillText(DOCTORCODE_AND_WORKPLACE, intyg.getForskrivarkodOchArbetsplatskod());
    }

    private void fillDiagnose() {
        fillText(DIAGNOS_CODE, intyg.getDiagnosKod());
        //  fillText(DIAGNOS, intyg.getDiagnosBeskrivning());
        fillText(DIAGNOS, buildOtherDiagnoses());
    }

    private void fillText(String fieldId, String text) {
        try {
            assert fields.getFieldType(fieldId) == AcroFields.FIELD_TYPE_TEXT;
            if (text != null) {
                fields.setField(fieldId, text);
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not fill field '" + fieldId + "' with value '" + text + "'", e);
        } catch (DocumentException e) {
            throw new IllegalArgumentException("Could not fill field '" + fieldId + "'", e);
        }
    }

    private void checkField(String fieldId) {
        setField(fieldId, true);
    }

    private void setField(String fieldId, boolean checked) {
        try {
            assert fields.getFieldType(fieldId) == AcroFields.FIELD_TYPE_CHECKBOX;
            if (checked) {
                fields.setField(fieldId, "1");
            }
        } catch (IOException | DocumentException e) {
            throw new IllegalArgumentException("Could not check field '" + fieldId + "'", e);
        }
    }
}
