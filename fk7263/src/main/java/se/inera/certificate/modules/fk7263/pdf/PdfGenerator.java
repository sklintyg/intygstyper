package se.inera.certificate.modules.fk7263.pdf;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import se.inera.certificate.model.LocalDateInterval;
import se.inera.certificate.modules.fk7263.model.internal.Fk7263Intyg;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

/**
 * @author andreaskaltenbach
 */
public class PdfGenerator {

    private static final String DATE_PATTERN = "yyyy-MM-dd";

    private static final String PATIENT_NAME = "form1[0].subform[0].flt_PatNamn[0]";
    private static final String PATIENT_SSN = "form1[0].subform[0].flt_PatPersonnummer[0]";
    private static final String PATIENT_SSN_2 = "form1[0].subform[1].flt_PatPersonnummer[1]";

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
    private PdfReader pdfReader;
    private PdfStamper pdfStamper;
    private AcroFields fields;

    public PdfGenerator(Fk7263Intyg intyg) throws PdfGeneratorException {
        this(intyg, true);
    }

    public PdfGenerator(Fk7263Intyg intyg, boolean flatten) throws PdfGeneratorException {
        try {
            this.intyg = intyg;

            outputStream = new ByteArrayOutputStream();

            pdfReader = new PdfReader(PDF_TEMPLATE);
            pdfStamper = new PdfStamper(pdfReader, this.outputStream);
            fields = pdfStamper.getAcroFields();

            generatePdf();

            pdfStamper.setFormFlattening(flatten);

            pdfStamper.close();

        } catch (Exception e) {
            throw new PdfGeneratorException(e);
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
        fillText(PATIENT_SSN_2, intyg.getPatientPersonnummer());
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

    private void fillNedsattning(LocalDateInterval interval, String checkboxFieldName, String fromDateFieldName,
            String toDateFieldName) {
        if (interval != null) {
            checkField(checkboxFieldName);
            fillText(fromDateFieldName, interval.getFrom().toString(DATE_PATTERN));
            fillText(toDateFieldName, interval.getTom().toString(DATE_PATTERN));
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

    private void fillBasedOn() {

        if (intyg.getUndersokningAvPatienten() != null) {
            checkField(BASED_ON_EXAMINATION);
            fillText(BASED_ON_EXAMINATION_TIME, intyg.getUndersokningAvPatienten().toString(DATE_PATTERN));
        }

        if (intyg.getTelefonkontaktMedPatienten() != null) {
            checkField(BASED_ON_PHONE_CONTACT);
            fillText(BASED_ON_PHONE_CONTACT_TIME, intyg.getTelefonkontaktMedPatienten().toString(DATE_PATTERN));
        }

        if (intyg.getJournaluppgifter() != null) {
            checkField(BASED_ON_JOURNAL);
            fillText(BASED_ON_JOURNAL_TIME, intyg.getJournaluppgifter().toString(DATE_PATTERN));
        }

        if (intyg.getAnnanReferens() != null) {
            checkField(BASED_ON_OTHER);
            fillText(BASED_ON_OTHER_DATE, intyg.getAnnanReferens().toString(DATE_PATTERN));
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
        fillText(DIAGNOS, intyg.getDiagnosBeskrivning());
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
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not check field '" + fieldId + "'", e);
        } catch (DocumentException e) {
            throw new IllegalArgumentException("Could not check field '" + fieldId + "'", e);
        }
    }
}
