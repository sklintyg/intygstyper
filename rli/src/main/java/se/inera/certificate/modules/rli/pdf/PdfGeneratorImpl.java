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

            populatePdfFields(utlatande, fields);

            pdfStamper.close();
            
        } catch (IOException | DocumentException e) {
            throw new PdfGeneratorException("Pdf generation failed",e);
        }

        return outputStream.toByteArray();
    }
    
    /**
     * Method for filling out the fields of a pdf with data from the RLI-internal model
     * 
     * @param utlatande 
     *          {@link se.inera.certificate.modules.rli.model.internal.Utlatande} containing data
     *          for populating the pdf
     * @param fields    
     *          The fields of the pdf 
     */
    private void populatePdfFields(Utlatande utlatande, AcroFields fields) {
        fillPatientDetails(utlatande, fields);
        fillSignerNameAndAddress(utlatande, fields);
    }

    
     //TODO: write methods corresponding to the RLI specific PDF once this is delivered. 
    
    private void fillPatientDetails(Utlatande utlatande, AcroFields fields) {
        fillText(fields, PATIENT_NAME, utlatande.getPatient().getFullstandigtnamn());
        fillText(fields, PATIENT_SSN, utlatande.getPatient().getPersonId());
    }

    private void fillSignerNameAndAddress(Utlatande utlatande, AcroFields fields) {
        fillText(fields, SIGN_NAME, utlatande.getSkapadAv().getFullstandigtnamn());
        fillText(fields, SIGN_DATE, utlatande.getSigneringsDatum().toString());
    }

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
