#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
/**
 * Copyright (C) 2013 Inera AB (http://www.inera.se)
 *
 * This file is part of Inera Certificate Modules (http://code.google.com/p/inera-certificate-modules).
 *
 * Inera Certificate Modules is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Inera Certificate Modules is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ${package}.${artifactId-safe}.pdf;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import ${package}.support.ApplicationOrigin;
import ${package}.${artifactId-safe}.model.internal.Utlatande;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

public class PdfGenerator {

    private static final String DATEFORMAT_FOR_FILENAMES = "yyMMdd";

    private final boolean formFlattening;

    public PdfGenerator(boolean formFlattening) {
        this.formFlattening = formFlattening;
    }

    public String generatePdfFilename(Utlatande utlatande) {
        String personId = utlatande.getPatient().getPersonid();
        String certificateSignatureDate = utlatande.getSigneringsdatum().toString(DATEFORMAT_FOR_FILENAMES);

        return String.format("lakarutlatande_%s_-%s.pdf", personId, certificateSignatureDate);
    }

    public byte[] generatePDF(Utlatande utlatande, ApplicationOrigin applicationOrigin) throws PdfGeneratorException {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            PdfReader pdfReader = new PdfReader("pdf/blankett.pdf");
            PdfStamper pdfStamper = new PdfStamper(pdfReader, outputStream);
            pdfStamper.setFormFlattening(formFlattening);
            AcroFields fields = pdfStamper.getAcroFields();
            populatePdfFields(utlatande, fields, applicationOrigin);
            pdfStamper.close();

            return outputStream.toByteArray();

        } catch (Exception e) {
            throw new PdfGeneratorException(e);
        }
    }

    /**
     * Method for filling out the fields of a pdf with data from the internal model
     *
     * @param utlatande
     *            {@link ${package}.${artifactId-safe}.model.internal.mi.Utlatande} containing data for populating
     *            the pdf
     * @param fields
     *            The fields of the pdf
     * @throws DocumentException
     * @throws IOException
     */
    private void populatePdfFields(Utlatande utlatande, AcroFields fields, ApplicationOrigin applicationOrigin) throws IOException, DocumentException {
        // TODO: Implement
    }
}
