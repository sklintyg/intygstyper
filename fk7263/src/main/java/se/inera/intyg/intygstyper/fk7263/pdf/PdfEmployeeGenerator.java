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
package se.inera.intyg.intygstyper.fk7263.pdf;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

import se.inera.intyg.common.support.model.Status;
import se.inera.intyg.common.support.modules.support.ApplicationOrigin;
import se.inera.intyg.intygstyper.fk7263.model.internal.Utlatande;

/**
 * Created by marced on 18/08/16.
 */
public class PdfEmployeeGenerator extends PdfAbstractGenerator {

    private static final int MARK_AS_EMPLOYER_HEIGTH = 55;
    private static final int MARK_AS_EMPLOYER_WIDTH = 495;
    private static final int MARK_AS_EMPLOYER_START_X = 50;
    private static final int MARK_AS_EMPLOYER_START_Y = 670;

    private static final String WATERMARK_TEXT_WC_EMPLOYER_MINIMAL_COPY = "Detta är en utskrift av ett elektroniskt intyg med minimalt innehåll. Det uppfyller sjuklönelagens krav, om inget annat regleras i kollektivavtal. Det minimala intyget kan ge arbetsgivaren sämre möjligheter att bedöma behovet av rehabilitering än ett fullständigt intyg";

    private static final String WATERMARK_TEXT_ELECTRONIC_COPY = "Detta är en utskrift av ett elektroniskt intyg";
    private static final String WATERMARK_TEXT_CONTENT_IS_CUSTOMIZED = "Detta är en anpassad utskrift av ett elektroniskt intyg. Viss information i intyget har valts bort";

    public PdfEmployeeGenerator(Utlatande intyg, List<Status> statuses, ApplicationOrigin applicationOrigin, List<String> optionalFields)
            throws PdfGeneratorException {
        this(intyg, statuses, applicationOrigin, optionalFields, true);
    }

    protected PdfEmployeeGenerator(Utlatande intyg, List<Status> statuses, ApplicationOrigin applicationOrigin, List<String> selectedOptionalFields,
            boolean flatten)
            throws PdfGeneratorException {
        try {
            this.intyg = intyg;

            outputStream = new ByteArrayOutputStream();

            PdfReader pdfReader = new PdfReader(PDF_TEMPLATE);
            PdfStamper pdfStamper = new PdfStamper(pdfReader, this.outputStream);
            fields = pdfStamper.getAcroFields();

            switch (applicationOrigin) {
            case MINA_INTYG:
                generateMIPdfWithOptionalFields(selectedOptionalFields);
                // perform additional decoration for MI originated pdf
                maskSendToFkInformation(pdfStamper);
                if (!EmployeeOptionalFields.containsAllValues(selectedOptionalFields)) {
                    markAsEmployerCopy(pdfStamper, WATERMARK_TEXT_CONTENT_IS_CUSTOMIZED);
                } else {
                    markAsElectronicCopy(pdfStamper, WATERMARK_TEXT_ELECTRONIC_COPY);
                }

                createRightMarginText(pdfStamper, pdfReader.getNumberOfPages(), intyg.getId(), MINA_INTYG_MARGIN_TEXT);
                break;
            case WEBCERT:
                generateMinimalPdf();
                // perform additional decoration for WC originated pdf
                maskSendToFkInformation(pdfStamper);
                markAsEmployerCopy(pdfStamper, WATERMARK_TEXT_WC_EMPLOYER_MINIMAL_COPY);

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

    private void generateMinimalPdf() {
        // Mandatory fields
        fillPatientDetails();
        fillRecommendationsWork();
        fillCapacityRelativeTo();
        fillCapacity();
        fillTravel();
        fillSignerCodes();
        fillSignerNameAndAddress();

    }

    private void generateMIPdfWithOptionalFields(List<String> optionalFields) {
        // Mandatory fields
        generateMinimalPdf();

        // EmployerCopy pdf's could have customized optionalfields
        if (EmployeeOptionalFields.SMITTSKYDD.isPresent(optionalFields)) {
            fillIsSuspenseDueToInfection(); // Fält 1
        }
        if (EmployeeOptionalFields.DIAGNOS.isPresent(optionalFields)) {
            fillDiagnose(); // Fält 2
        }
        if (EmployeeOptionalFields.AKTUELLT_SJUKDOMS_FORLOPP.isPresent(optionalFields)) {
            fillDiseaseCause(); // Fält 3
        }
        if (EmployeeOptionalFields.FUNKTIONSNEDSATTNING.isPresent(optionalFields)) {
            fillDisability(); // Fält 4a
        }
        if (EmployeeOptionalFields.INTYGET_BASERAS_PA.isPresent(optionalFields)) {
            fillBasedOn(); // Fält 4b
        }
        if (EmployeeOptionalFields.AKTIVITETSBEGRANSNING.isPresent(optionalFields)) {
            fillActivityLimitation(); // Fält 5
        }
        if (EmployeeOptionalFields.REKOMMENDATIONER_EJ_FORETAGSHALSOVARD.isPresent(optionalFields)) {
            fillRecommendationsOther(); // Fält 6a (utom Kontakt med företagshälsovården)
        }
        if (EmployeeOptionalFields.BEHANDLING_ATGARD.isPresent(optionalFields)) {
            fillMeasures(); // Fält 6b
        }
        if (EmployeeOptionalFields.REHABILITERING.isPresent(optionalFields)) {
            fillRehabilitation(); // Fält 7
        }

        if (EmployeeOptionalFields.ARBETSFORMAGA_RELATIVT_TILL.isPresent(optionalFields)) {
            fillCapacityRelativeTo(); // Fält 8a
        }
        if (EmployeeOptionalFields.ARBETSFORMAGA.isPresent(optionalFields)) {
            fillArbetsformaga(); // Fält 9
        }
        if (EmployeeOptionalFields.PROGNOS.isPresent(optionalFields)) {
            fillPrognose(); // Fält 10
        }
        if (EmployeeOptionalFields.KONTAKT_MED_FK.isPresent(optionalFields)) {
            fillFkContact(); // Fält 12
        }
        if (EmployeeOptionalFields.OVRIGT.isPresent(optionalFields)) {
            fillOther(); // Fält 13
        }
    }

    /**
     * Marking this document as a print meant for the employer of the patient.
     *
     * @throws DocumentException
     * @throws IOException
     */
    private void markAsEmployerCopy(PdfStamper pdfStamper, String watermarkText) throws DocumentException, IOException {
        mark(pdfStamper, watermarkText, MARK_AS_EMPLOYER_START_X, MARK_AS_EMPLOYER_START_Y, MARK_AS_EMPLOYER_HEIGTH, MARK_AS_EMPLOYER_WIDTH);
    }
}
