package se.inera.certificate.modules.fk7263.pdf;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static se.inera.certificate.modules.fk7263.model.Fk7263Intyg.DATE_PATTERN;
import static se.inera.certificate.modules.fk7263.model.Fk7263Intyg.Nedsattningsgrad_Helt_nedsatt;
import static se.inera.certificate.modules.fk7263.model.Fk7263Intyg.Nedsattningsgrad_Nedsatt_med_1_2;
import static se.inera.certificate.modules.fk7263.model.Fk7263Intyg.Nedsattningsgrad_Nedsatt_med_1_4;
import static se.inera.certificate.modules.fk7263.model.Fk7263Intyg.Nedsattningsgrad_Nedsatt_med_3_4;
import static se.inera.certificate.modules.fk7263.model.Fk7263Intyg.Referens_Annat;
import static se.inera.certificate.modules.fk7263.model.Fk7263Intyg.Referens_Journaluppgifter;
import static se.inera.certificate.modules.fk7263.model.Fk7263Intyg.Vardkontakt_Min_telefonkontakt_med_patienten;
import static se.inera.certificate.modules.fk7263.model.Fk7263Intyg.Vardkontakt_Min_undersokning_av_patienten;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import se.inera.certificate.model.Aktivitet;
import se.inera.certificate.model.Arbetsuppgift;
import se.inera.certificate.model.Observation;
import se.inera.certificate.model.Referens;
import se.inera.certificate.model.Vardkontakt;
import se.inera.certificate.model.codes.ObservationsKoder;
import se.inera.certificate.model.util.Strings;
import se.inera.certificate.modules.fk7263.model.Fk7263Intyg;


/**
 * @author andreaskaltenbach
 */
public class PdfGenerator {

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
    public static final String PDF_TEMPLATE = "pdf/RFV7263_008_J_003_statisk.pdf";

    private Fk7263Intyg intyg;
    private ByteArrayOutputStream outputStream;
    private PdfReader pdfReader;
    private PdfStamper pdfStamper;
    private AcroFields fields;

    public PdfGenerator(Fk7263Intyg intyg) throws IOException, DocumentException {
        this.intyg = intyg;

        outputStream = new ByteArrayOutputStream();

        pdfReader = new PdfReader(PDF_TEMPLATE);
        pdfStamper = new PdfStamper(pdfReader, this.outputStream);
        fields = pdfStamper.getAcroFields();

        generatePdf();

        pdfStamper.close();
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

        setField(CONTACT_WITH_FK, intyg.getKontaktMedForsakringskassanAktuell() != null);

        fillPrognose();

        fillOther();
    }

    private void fillPatientDetails() {
        fillText(PATIENT_NAME, intyg.getPatient().getFullstandigtNamn());
        fillText(PATIENT_SSN, intyg.getPatient().getId().getExtension());
    }

    private void fillSignerNameAndAddress() {
        fillText(SIGN_NAME, intyg.getNamnfortydligandeOchAdress());
        fillText(SIGN_DATE, intyg.getSigneringsDatumAsString());
    }

    private void fillTravel() {
        if (intyg.getForandratRessattAktuellt() != null) {
            checkField(RECOMMENDATION_TRAVEL_YES);
        }
        if (intyg.getForandratRessattEjAktuellt() != null) {
            checkField(RECOMMENDATION_TRAVEL_NO);
        }
    }

    private void fillPrognose() {
        setField(WORK_CAPACITY_FORECAST_YES, intyg.isPrognosFullAterstallning());
        setField(WORK_CAPACITY_FORECAST_PARTLY, intyg.isPrognosDelvisAterstallning());
        setField(WORK_CAPACITY_FORECAST_NO, intyg.isPrognosEjAterstallning());
        if (intyg.isPrognosAterstallningGarEjBedomma()) {
            checkField(WORK_CAPACITY_FORECAST_UNKNOWN);
            // TODO Motivering only for this option?
            fillText(WORK_CAPACITY_TEXT, intyg.getPrognosText());
        }
    }

    private void fillNedsattning(Observation nedsattning, String checkboxFieldName,
                                 String fromDateFieldName, String toDateFieldName) {
        if (nedsattning != null) {
            checkField(checkboxFieldName);
            if (nedsattning.getObservationsPeriod() != null) {
                fillText(fromDateFieldName, nedsattning.getObservationsPeriod().getFrom().toString(DATE_PATTERN));
                fillText(toDateFieldName, nedsattning.getObservationsPeriod().getTom().toString(DATE_PATTERN));
            }
        }
    }

    private void fillCapacity() {
        fillNedsattning(intyg.getNedsattning(Nedsattningsgrad_Helt_nedsatt), REDUCED_WORK_CAPACITY_FULL, REDUCED_WORK_CAPACITY_FULL_FROM, REDUCED_WORK_CAPACITY_FULL_TOM);
        fillNedsattning(intyg.getNedsattning(Nedsattningsgrad_Nedsatt_med_3_4), REDUCED_WORK_CAPACITY_75, REDUCED_WORK_CAPACITY_75_FROM, REDUCED_WORK_CAPACITY_75_TOM);
        fillNedsattning(intyg.getNedsattning(Nedsattningsgrad_Nedsatt_med_1_2), REDUCED_WORK_CAPACITY_50, REDUCED_WORK_CAPACITY_50_FROM, REDUCED_WORK_CAPACITY_50_TOM);
        fillNedsattning(intyg.getNedsattning(Nedsattningsgrad_Nedsatt_med_1_4), REDUCED_WORK_CAPACITY_25, REDUCED_WORK_CAPACITY_25_FROM, REDUCED_WORK_CAPACITY_25_TOM);
    }

    private void fillCapacityRelativeTo() {
        if (intyg.isArbetsformagaIForhallandeTillNuvarandeArbete()) {
            checkField(CURRENT_WORK);
            if (intyg.getPatient().getArbetsuppgifts() != null && !intyg.getPatient().getArbetsuppgifts().isEmpty()) {

                List<String> arbetsuppgifter = new ArrayList<>();
                for (Arbetsuppgift arbetsuppgift : intyg.getPatient().getArbetsuppgifts()) {
                    arbetsuppgifter.add(arbetsuppgift.getTypAvArbetsuppgift());
                }
                String arbetsuppgift = Strings.join(", ", arbetsuppgifter);
                fillText(CURRENT_WORK_TEXT_1, arbetsuppgift);
            }
        }
        setField(UNEMPLOYMENT, intyg.isArbetsformagaIForhallandeTillArbetsloshet());
        setField(PARENTAL_LEAVE, intyg.isArbetsformagaIForhallandeTillForaldraledighet());
    }

    private void fillRehabilitation() {
        setField(RECOMMENDATION_REHAB_YES, intyg.getArbetsinriktadRehabiliteringAktuell() != null);
        setField(RECOMMENDATION_REHAB_NO, intyg.getArbetsinriktadRehabiliteringEjAktuell() != null);
        setField(RECOMMENDATION_REHAB_UNKNOWN, intyg.getArbetsinriktadRehabiliteringEjBedombar() != null);
    }

    private void fillRecommendations() {
        setField(RECOMMENDATIONS_CONTACT_AF, intyg.getRekommenderarKontaktMedArbetsformedlingen() != null);
        setField(RECOMMENDATIONS_CONTACT_COMPANY_CARE, intyg.getRekommenderarKontaktMedForetagshalsovarden() != null);
        if (intyg.getRekommenderarOvrigt() != null) {
            checkField(RECOMMENDATIONS_OTHER);
            fillText(RECOMMENDATIONS_OTHER_TEXT, intyg.getRekommenderarOvrigtText());
        }
    }

    private void fillActivityLimitation() {
        fillText(ACTIVITY_LIMITATION, intyg.getAktivitetsnedsattningBeskrivning());
    }

    private void fillDisability() {
        fillText(DISABILITIES, intyg.getFunktionsnedsattningBeskrivning());
    }

    private void fillOther() {
        if (intyg.getKommentars() != null && !intyg.getKommentars().isEmpty()) {
            fillText(OTHER_INFORMATION, intyg.getKommentars().get(0));
        }
    }

    private void fillBasedOn() {

        Vardkontakt minUndersokning = intyg.getVardkontakt(Vardkontakt_Min_undersokning_av_patienten);
        if (minUndersokning != null) {
            checkField(BASED_ON_EXAMINATION);
            fillText(BASED_ON_EXAMINATION_TIME, minUndersokning.getVardkontaktstid().getStart().toString(DATE_PATTERN));
        }

        Vardkontakt minTelefonkontakt = intyg.getVardkontakt(Vardkontakt_Min_telefonkontakt_med_patienten);
        if (minTelefonkontakt != null) {
            checkField(BASED_ON_PHONE_CONTACT);
            fillText(BASED_ON_PHONE_CONTACT_TIME, minTelefonkontakt.getVardkontaktstid().getStart().toString(DATE_PATTERN));
        }

        Referens journalReferens = intyg.getReferens(Referens_Journaluppgifter);
        if (journalReferens != null) {
            checkField(BASED_ON_JOURNAL);
            fillText(BASED_ON_JOURNAL_TIME, journalReferens.getDatum().toString(DATE_PATTERN));
        }

        Referens annanReferens = intyg.getReferens(Referens_Annat);
        if (annanReferens != null) {
            checkField(BASED_ON_OTHER);
            fillText(BASED_ON_OTHER_DATE, annanReferens.getDatum().toString(DATE_PATTERN));
        }
    }

    private void fillMeasures() {

        Aktivitet planeradBehandlingInomSjukvarden = intyg.getAtgardInomSjukvarden();
        if (planeradBehandlingInomSjukvarden != null) {
            checkField(MEASURES_CURRENT);
            fillText(MEASURES_CURRENT_TEXT, planeradBehandlingInomSjukvarden.getBeskrivning());
        }

        Aktivitet planeradAnnanAtgard = intyg.getAnnanAtgard();
        if (planeradAnnanAtgard != null) {
            checkField(MEASURES_OTHER);
            fillText(MEASURES_OTHER_TEXT, planeradAnnanAtgard.getBeskrivning());
        }
    }

    private void fillDiseaseCause() {
        List<Observation> sjukdomsforlopp = intyg.getObservationsByKategori(ObservationsKoder.BEDOMT_TILLSTAND);
        if (sjukdomsforlopp != null && !sjukdomsforlopp.isEmpty()) {
            fillText(DISEASE_CAUSE, sjukdomsforlopp.get(0).getBeskrivning());
        }
    }

    private void fillIsSuspenseDueToInfection() {
        setField(SUSPENSION_DUE_TO_INFECTION, intyg.getAvstangningEnligtSmittskyddslagen() != null);
    }

    private void fillSignerCodes() {
        fillText(DOCTORCODE_AND_WORKPLACE, intyg.getForskrivarkodOchArbetsplatskod());
    }

    private void fillDiagnose() {
        List<Observation> diagnosList = intyg.getObservationsByKategori(ObservationsKoder.MEDICINSKT_TILLSTAND);
        if (diagnosList != null && !diagnosList.isEmpty()) {
            Observation diagnos = diagnosList.get(0);
            if (diagnos.getObservatonsKod() != null) {
                fillText(DIAGNOS_CODE, diagnos.getObservatonsKod().getCode());
            }
            fillText(DIAGNOS, diagnos.getBeskrivning());
        }
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
