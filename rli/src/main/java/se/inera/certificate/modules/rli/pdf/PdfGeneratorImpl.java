package se.inera.certificate.modules.rli.pdf;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import se.inera.certificate.modules.rli.model.internal.Utlatande;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

/**
 * @author erik
 */
public class PdfGeneratorImpl implements PdfGenerator {

    private static final String PATIENT_NAME = "form1[0].subform[0].flt_PatNamn[0]";
    private static final String PATIENT_SSN = "form1[0].subform[0].flt_PatPersonnummer[0]";

    private static final String RECOMMENDATION_TRAVEL_YES = "form1[0].subform[1].ksr_Ja_flt11[0]";

    private static final String RECOMMENDATION_TRAVEL_NO = "form1[0].subform[1].ksr_Nej_flt11[0]";

    private static final String SIGN_DATE = "form1[0].subform[1].flt_Datum_flt14[0]";

    private static final String SIGN_NAME = "form1[0].subform[1].flt_NamnfortydligandeMottagningsadrTel_flt15[0]";

    public static final String PDF_TEMPLATE = "pdf/RFV7263_009_J_003_statisk.pdf";

    public PdfGeneratorImpl() {

    }

    /*
     * (non-Javadoc)
     * 
     * @see se.inera.certificate.modules.rli.pdf.PdfGenerator#getBytes()
     */
    @Override
    public byte[] generatePDF(Utlatande utlatande) throws PdfGeneratorException {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        
        try {
            PdfReader pdfReader = new PdfReader(PDF_TEMPLATE);
            PdfStamper pdfStamper = new PdfStamper(pdfReader, outputStream);
            AcroFields fields = pdfStamper.getAcroFields();

            generatePdf(utlatande, fields);

            pdfStamper.close();
        } catch (IOException | DocumentException e) {
            throw new PdfGeneratorException("Pdf generation failed",e);
        }

        return outputStream.toByteArray();
    }

    private void generatePdf(Utlatande utlatande, AcroFields fields) {
        fillPatientDetails(utlatande, fields);
        fillSignerNameAndAddress(utlatande, fields);

        // fillSignerCodes();

        // fillIsSuspenseDueToInfection();

        // fillDiagnose();

        // fillDiseaseCause();

        // fillBasedOn();

        // fillDisability();

        // fillActivityLimitation();

        // fillRecommendations();

        // fillMeasures();

       // fillTravel();

        // fillOther();
    }

    private void fillPatientDetails(Utlatande utlatande, AcroFields fields) {
        fillText(fields, PATIENT_NAME, utlatande.getPatient().getFullstandigtnamn());
        fillText(fields, PATIENT_SSN, utlatande.getPatient().getPersonId());
    }

    private void fillSignerNameAndAddress(Utlatande utlatande, AcroFields fields) {
        fillText(fields, SIGN_NAME, utlatande.getSkapadAv().getFullstandigtnamn());
        fillText(fields, SIGN_DATE, utlatande.getSigneringsDatum().toString());
    }

//    private void fillTravel() {
//        if (intyg.getRekommendation() == null) {
//            checkField(RECOMMENDATION_TRAVEL_YES);
//        }
//        if (intyg.getRekommendation() != null) {
//            checkField(RECOMMENDATION_TRAVEL_NO);
//        }
//    }

    // private void fillBasedOn() {
    //
    // Vardkontakt minUndersokning = intyg.getVardkontakt(Vardkontakttypkoder.MIN_UNDERSOKNING_AV_PATIENTEN);
    // if (minUndersokning != null) {
    // checkField(BASED_ON_EXAMINATION);
    // fillText(BASED_ON_EXAMINATION_TIME, minUndersokning.getVardkontaktstid().getStart().toString(DATE_PATTERN));
    // }
    //
    // Vardkontakt minTelefonkontakt = intyg.getVardkontakt(Vardkontakttypkoder.MIN_TELEFONKONTAKT_MED_PATIENTEN);
    // if (minTelefonkontakt != null) {
    // checkField(BASED_ON_PHONE_CONTACT);
    // fillText(BASED_ON_PHONE_CONTACT_TIME, minTelefonkontakt.getVardkontaktstid().getStart().toString(DATE_PATTERN));
    // }
    //
    // Referens journalReferens = intyg.getReferens(Referenstypkoder.JOURNALUPPGIFT);
    // if (journalReferens != null) {
    // checkField(BASED_ON_JOURNAL);
    // fillText(BASED_ON_JOURNAL_TIME, journalReferens.getDatum().toString(DATE_PATTERN));
    // }
    //
    // Referens annanReferens = intyg.getReferens(Referenstypkoder.ANNAT);
    // if (annanReferens != null) {
    // checkField(BASED_ON_OTHER);
    // fillText(BASED_ON_OTHER_DATE, annanReferens.getDatum().toString(DATE_PATTERN));
    // }
    // }
    //
    // private void fillMeasures() {
    //
    // Aktivitet planeradBehandlingInomSjukvarden = intyg.getAtgardInomSjukvarden();
    // if (planeradBehandlingInomSjukvarden != null) {
    // checkField(MEASURES_CURRENT);
    // fillText(MEASURES_CURRENT_TEXT, planeradBehandlingInomSjukvarden.getBeskrivning());
    // }
    //
    // Aktivitet planeradAnnanAtgard = intyg.getAnnanAtgard();
    // if (planeradAnnanAtgard != null) {
    // checkField(MEASURES_OTHER);
    // fillText(MEASURES_OTHER_TEXT, planeradAnnanAtgard.getBeskrivning());
    // }
    // }
    //
    // private void fillDiseaseCause() {
    // List<Observation> sjukdomsforlopp = intyg.getObservationsByKod(ObservationsKoder.FORLOPP);
    // if (sjukdomsforlopp != null && !sjukdomsforlopp.isEmpty()) {
    // fillText(DISEASE_CAUSE, sjukdomsforlopp.get(0).getBeskrivning());
    // }
    // }
    //
    // private void fillIsSuspenseDueToInfection() {
    // setField(SUSPENSION_DUE_TO_INFECTION, intyg.getAvstangningEnligtSmittskyddslagen() != null);
    // }
    //
    // private void fillSignerCodes() {
    // fillText(DOCTORCODE_AND_WORKPLACE, intyg.getForskrivarkodOchArbetsplatskod());
    // }
    //
    // private void fillDiagnose() {
    // List<Observation> diagnosList = intyg.getObservationsByKategori(ObservationsKoder.DIAGNOS);
    // if (diagnosList != null && !diagnosList.isEmpty()) {
    // Observation diagnos = diagnosList.get(0);
    // if (diagnos.getObservationsKod() != null) {
    // fillText(DIAGNOS_CODE, diagnos.getObservationsKod().getCode());
    // }
    // fillText(DIAGNOS, diagnos.getBeskrivning());
    // }
    // }

    private void fillText(AcroFields fields, String fieldId, String text) {
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

    private void setAsChecked(AcroFields fields, String fieldId) {
        setField(fields, fieldId, true);
    }

    private void setField(AcroFields fields, String fieldId, boolean checked) {
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
