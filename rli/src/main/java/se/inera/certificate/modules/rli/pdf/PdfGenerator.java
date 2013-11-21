package se.inera.certificate.modules.rli.pdf;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.commons.lang3.StringUtils;

import se.inera.certificate.modules.rli.model.internal.mi.KomplikationStyrkt;
import se.inera.certificate.modules.rli.model.internal.mi.OrsakAvbokning;
import se.inera.certificate.modules.rli.model.internal.mi.Utlatande;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

/**
 * @author erik
 */
public class PdfGenerator {

    private static final String PATIENT_SSN = "Patient_personnummer";
    private static final String PATIENT_NAME = "Patient_namn";
    private static final String PATIENT_POSTAL_CODE = "Patient_postnummer";
    private static final String PATIENT_POSTAL_ADDRESS = "Patient_postadress";
    private static final String PATIENT_POSTAL_CITY = "Patient_postort";

    private static final String TRAVEL_DESTINATION = "Resmal";
    private static final String TRAVEL_BOOKING_NUMBER = "Bokningsreferens";
    private static final String TRAVEL_BOOKING_DATE = "Bokningsdatum";
    private static final String TRAVEL_CANCEL_DATE = "Avbestallningsdatum";
    private static final String TRAVEL_DEPARTURE_DATE = "Avresedatum";

    // Special field only set when patient is pregnant
    private static final String BABY_DUE_DATE = "Barnet_beraknas_fodas";

    private static final String SJK1 = "SJK1";
    private static final String SJK2 = "SJK2";
    private static final String SJK3 = "SJK3";
    private static final String SJK4 = "SJK4";
    private static final String SJK5 = "SJK5";

    private static final String CONFIRMED_BY_DR = "Forsta_undersokning_lakare_intygar_check";

    private static final String CONFIRMED_BY_PATIENT = "Forsta_undersokning_patient_intygar_check";

    private static final String DATE_BY_PATIENT = "Forsta_undersokning_datum_patient_intygar";
    private static final String PLACE_BY_PATIENT = "Forsta_undersokning_plats_patient_intygar";
    private static final String DATE_BY_DR = "Forsta_undersokning_datum_lakare_intygar";
    private static final String PLACE_BY_DR = "Forsta_undersokning_plats_lakare_intygar";

    private static final String DATE_EXAM = "Undersokning_datum";
    private static final String PLACE_EXAM = "Undersokning_plats";

    private static final String SIGNED = "Signerat_av";

    private static final String COMMENTS = "Ovriga_upplysningar";

    private static final String CAREUNIT_NAME = "Vardenhet_namn";
    private static final String CAREUNIT_ADDRESS = "Vardenhet_postadress";
    private static final String CAREUNIT_POSTALNUMBER = "Vardenhet_postnummer";
    private static final String CAREUNIT_CITY = "Vardenhet_postort";
    private static final String CAREUNIT_TELEPHONE = "Vardenhet_telefon";
    private static final String CAREUNIT_EMAIL = "Vardenhet_epost";

    private static final String DATEFORMAT_FOR_FILENAMES = "yyMMdd";
    private static final String DATE_FORMAT = "yyyy-MM-dd";

    public PdfGenerator() {

    }

    public String generatePdfFilename(Utlatande utlatande) {
        return String
                .format("lakarutlatande_%s_%s-%s.pdf", utlatande.getPatient().getPersonid(), utlatande.getArrangemang()
                        .getBokningsdatum(), utlatande.getSigneringsdatum().toString(DATEFORMAT_FOR_FILENAMES));
    }

    public byte[] generatePDF(Utlatande utlatande) throws PdfGeneratorException {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            String type = utlatande.getUndersokning().getOrsakforavbokning().toString();

            PdfReader pdfReader = new PdfReader("pdf/" + type + "_blankett.pdf");
            PdfStamper pdfStamper = new PdfStamper(pdfReader, outputStream);
            pdfStamper.setFormFlattening(true);
            AcroFields fields = pdfStamper.getAcroFields();
            populatePdfFields(utlatande, fields);
            pdfStamper.close();

            return outputStream.toByteArray();

        } catch (Exception e) {
            throw new PdfGeneratorException(e);
        }
    }

    /**
     * Method for filling out the fields of a pdf with data from the RLI-internal model
     * 
     * @param utlatande
     *            {@link se.inera.certificate.modules.rli.model.internal.mi.Utlatande} containing data for populating the
     *            pdf
     * @param fields
     *            The fields of the pdf
     * @throws DocumentException
     * @throws IOException
     */
    private void populatePdfFields(Utlatande utlatande, AcroFields fields) throws IOException, DocumentException {
        fillPatientDetails(utlatande, fields);

        fillSignerNameAndAddress(utlatande, fields);

        fillVardenhetInformation(utlatande, fields);

        fillTravelInformation(utlatande, fields);

        setSJKAndREKCheckBoxes(utlatande, fields);

        fillForstaUndersokning(utlatande, fields);

        fillUndersokning(utlatande, fields);

        fillMisc(utlatande, fields);
    }

    private void fillVardenhetInformation(Utlatande utlatande, AcroFields fields) {
        fillText(fields, CAREUNIT_NAME, utlatande.getSkapadAv().getVardenhet().getEnhetsnamn());
        fillText(fields, CAREUNIT_ADDRESS, utlatande.getSkapadAv().getVardenhet().getPostadress());
        fillText(fields, CAREUNIT_POSTALNUMBER, utlatande.getSkapadAv().getVardenhet().getPostnummer());
        fillText(fields, CAREUNIT_CITY, utlatande.getSkapadAv().getVardenhet().getPostort());
        fillText(fields, CAREUNIT_TELEPHONE, utlatande.getSkapadAv().getVardenhet().getTelefonnummer());
        fillText(fields, CAREUNIT_EMAIL, utlatande.getSkapadAv().getVardenhet().getEpost());
    }

    private void fillUndersokning(Utlatande utlatande, AcroFields fields) {
        if (utlatande.getUndersokning().getUndersokningsdatum() != null) {
            fillText(fields, DATE_EXAM, utlatande.getUndersokning().getUndersokningsdatum());
        }
        /**
         * If KLINISK_UNDERSOKNING has a structured info about Plats (which it should), then use Vardenhet.Enhetsnamn
         * for Plats
         */
        if (utlatande.getUndersokning().getUtforsVid() != null) {
            fillText(fields, PLACE_EXAM, utlatande.getUndersokning().getUtforsVid().getEnhetsnamn());
        }
        /** Else use Undersokningsplats if it is specified (which it shouldn't */
        else if (utlatande.getUndersokning().getUndersokningsplats() != null) {
            fillText(fields, PLACE_EXAM, utlatande.getUndersokning().getUndersokningsplats());
        }

    }

    private void fillForstaUndersokning(Utlatande utlatande, AcroFields fields) throws IOException, DocumentException {
        String utforsAv = (utlatande.getUndersokning().getKomplikationstyrkt() == KomplikationStyrkt.AV_HOS_PERSONAL) ? "223366009"
                : "116154003";

        if (utforsAv.equals("116154003")) {
            fields.setField(CONFIRMED_BY_PATIENT, "Yes");
            fillText(fields, DATE_BY_PATIENT, utlatande.getUndersokning().getForstaUndersokningsdatum());
            fillText(fields, PLACE_BY_PATIENT, utlatande.getUndersokning().getForstaUndersokningsplats());
        } else if (utforsAv.equals("223366009")) {
            fields.setField(CONFIRMED_BY_DR, "Yes");
            fillText(fields, DATE_BY_DR, utlatande.getUndersokning().getForstaUndersokningsdatum());
            fillText(fields, PLACE_BY_DR, utlatande.getUndersokning().getForstaUndersokningsplats());
        }
    }

    private void setSJKAndREKCheckBoxes(Utlatande utlatande, AcroFields fields) throws IOException, DocumentException {
        if (utlatande.getRekommendation() == null) {
            return;
        }

        String sjuk_code = null;
        String rek_code = null;

        sjuk_code = utlatande.getRekommendation().getSjukdomskannedom().getCode();
        rek_code = utlatande.getRekommendation().getRekommendationskod().getCode();

        if (sjuk_code == SJK3 || sjuk_code == SJK4) {
            fields.setField(sjuk_code, "Yes");
            fields.setField(SJK2, "Yes");
        } else {
            fields.setField(sjuk_code, "Yes");
        }

        fields.setField(rek_code, "Yes");

    }

    /**
     * 
     * Fill fields regarding the Patient
     * 
     * @param utlatande
     * @param fields
     */
    private void fillPatientDetails(Utlatande utlatande, AcroFields fields) {
        fillText(fields, PATIENT_NAME, utlatande.getPatient().getFullstandigtNamn());
        fillText(fields, PATIENT_SSN, utlatande.getPatient().getPersonid());
        fillText(fields, PATIENT_POSTAL_ADDRESS, utlatande.getPatient().getPostadress());
        fillText(fields, PATIENT_POSTAL_CITY, utlatande.getPatient().getPostort());
        fillText(fields, PATIENT_POSTAL_CODE, utlatande.getPatient().getPostnummer());
    }

    /**
     * 
     * Fill fields regarding signer of certificate
     * 
     * @param utlatande
     * @param fields
     */
    private void fillSignerNameAndAddress(Utlatande utlatande, AcroFields fields) {
        fillText(fields, SIGNED, utlatande.getSkapadAv().getFullstandigtNamn() + "\n"
                + utlatande.getSigneringsdatum().toString(DATE_FORMAT));

    }

    /**
     * 
     * Fill fields with travel information
     * 
     * @param utlatande
     * @param fields
     */
    private void fillTravelInformation(Utlatande utlatande, AcroFields fields) {

        if (utlatande.getUndersokning().getOrsakforavbokning() == OrsakAvbokning.RESENAR_GRAVID) {
            fillText(fields, BABY_DUE_DATE, utlatande.getUndersokning().getGraviditet().getBeraknatForlossningsdatum());
        }
        fillText(fields, TRAVEL_BOOKING_DATE, utlatande.getArrangemang().getBokningsdatum());
        fillText(fields, TRAVEL_BOOKING_NUMBER, utlatande.getArrangemang().getBokningsreferens());
        fillText(fields, TRAVEL_CANCEL_DATE, utlatande.getArrangemang().getAvbestallningsdatum());
        fillText(fields, TRAVEL_DEPARTURE_DATE, utlatande.getArrangemang().getArrangemangsdatum());
        fillText(fields, TRAVEL_DESTINATION, utlatande.getArrangemang().getPlats());
    }

    private void fillMisc(Utlatande utlatande, AcroFields fields) {
        String text = StringUtils.join(utlatande.getKommentarer(), " ");
        fillText(fields, COMMENTS, text);
    }

    /**
     * 
     * Utility method for populating PDF fields with the correct kind of information
     * 
     * @param fields
     * @param fieldId
     * @param text
     */
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
}
