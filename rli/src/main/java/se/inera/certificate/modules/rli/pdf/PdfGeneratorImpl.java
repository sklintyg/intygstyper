package se.inera.certificate.modules.rli.pdf;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.commons.lang3.StringUtils;

import se.inera.certificate.modules.rli.model.internal.Utlatande;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

/**
 * @author erik
 */
public class PdfGeneratorImpl implements PdfGenerator {

    private static final String PATIENT_SSN = "Personnummer";
    private static final String PATIENT_NAME = "FullstandigtNamn";
    private static final String PATIENT_POSTAL_CODE = "Postnummer";
    private static final String PATIENT_POSTAL_ADDRESS = "Postadress";
    private static final String PATIENT_POSTAL_CITY = "Postort";

    private static final String TRAVEL_DESTINATION = "Resmal";
    private static final String TRAVEL_BOOKING_NUMBER = "Bokningsnummer";
    private static final String TRAVEL_BOOKING_DATE = "Bokningsdatum";
    private static final String TRAVEL_CANCEL_DATE = "Avbokningsdatum";
    private static final String TRAVEL_DEPARTURE_DATE = "Avresedatum";

    private static final String SICK_KNOWLEDGE = "Sjukdomskannedom";

    private static final String CERT_SIGN_DATE = "Signeringdatum";

    private static final String PHYSICIAN_NAME = "Lakarensnamn";

    private static final String COMMENTS = "Kommentarer";

    // SJK4Beskrivning 4

    public static final String PDF_SJUK_TEMPLATE = "pdf/RLI_template.pdf";

    private static final String dateFormat = "yyMMdd";

    public PdfGeneratorImpl() {

    }

    @Override
    public String generatePdfFilename(Utlatande utlatande) {
        return String.format("lakarutlatande_%s_%s-%s.pdf", utlatande.getPatient().getPersonid(), utlatande
                .getArrangemang().getBokningsdatum(), utlatande.getSigneringsdatum().toString(dateFormat));
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
            PdfReader pdfReader = new PdfReader(PDF_SJUK_TEMPLATE);

            PdfStamper pdfStamper = new PdfStamper(pdfReader, outputStream);
            pdfStamper.setFormFlattening(true);

            AcroFields fields = pdfStamper.getAcroFields();

            populatePdfFields(utlatande, fields);

            pdfStamper.close();

        } catch (IOException | DocumentException e) {
            throw new PdfGeneratorException("Pdf generation failed", e);
        }

        return outputStream.toByteArray();
    }

    /**
     * Method for filling out the fields of a pdf with data from the RLI-internal model
     * 
     * @param utlatande
     *            {@link se.inera.certificate.modules.rli.model.internal.Utlatande} containing data for populating the
     *            pdf
     * @param fields
     *            The fields of the pdf
     */
    private void populatePdfFields(Utlatande utlatande, AcroFields fields) {
        fillPatientDetails(utlatande, fields);

        fillSignerNameAndAddress(utlatande, fields);

        fillTravelInformation(utlatande, fields);

        fillMisc(utlatande, fields);
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
        fillText(fields, PHYSICIAN_NAME, utlatande.getSkapadAv().getFullstandigtNamn());

        fillText(fields, CERT_SIGN_DATE, utlatande.getSigneringsdatum().toString(dateFormat));
    }

    /**
     * 
     * Fill fields with travel information
     * 
     * @param utlatande
     * @param fields
     */
    private void fillTravelInformation(Utlatande utlatande, AcroFields fields) {
        fillText(fields, TRAVEL_BOOKING_DATE, utlatande.getArrangemang().getBokningsdatum());
        fillText(fields, TRAVEL_BOOKING_NUMBER, utlatande.getArrangemang().getBokningsreferens());
        fillText(fields, TRAVEL_CANCEL_DATE, utlatande.getArrangemang().getAvbestallningsdatum());
        fillText(fields, TRAVEL_DEPARTURE_DATE, utlatande.getArrangemang().getArrangemangsdatum());
        fillText(fields, TRAVEL_DESTINATION, utlatande.getArrangemang().getPlats());
    }

    private void fillMisc(Utlatande utlatande, AcroFields fields) {
        String text = StringUtils.join(utlatande.getKommentarer(), " ");
        fillText(fields, COMMENTS, text.toString());
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
