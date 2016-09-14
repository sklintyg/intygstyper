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
import java.util.List;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

import se.inera.intyg.common.support.model.Status;
import se.inera.intyg.common.support.modules.support.ApplicationOrigin;
import se.inera.intyg.intygstyper.fk7263.model.internal.Utlatande;

/**
 * Created by marced on 18/08/16.
 */
public class PdfDefaultGenerator extends PdfAbstractGenerator {

    public PdfDefaultGenerator(Utlatande intyg, List<Status> statuses, ApplicationOrigin applicationOrigin)
            throws PdfGeneratorException {
        this(intyg, statuses, applicationOrigin, true);
    }

    protected PdfDefaultGenerator(Utlatande intyg, List<Status> statuses, ApplicationOrigin applicationOrigin, boolean flatten)
            throws PdfGeneratorException {
        try {
            this.intyg = intyg;

            outputStream = new ByteArrayOutputStream();

            PdfReader pdfReader = new PdfReader(PDF_TEMPLATE);
            PdfStamper pdfStamper = new PdfStamper(pdfReader, this.outputStream);
            fields = pdfStamper.getAcroFields();
            generatePdf();

            switch (applicationOrigin) {
            case MINA_INTYG:
                // perform additional decoration for MI originated pdf
                maskSendToFkInformation(pdfStamper);
                markAsElectronicCopy(pdfStamper);
                createRightMarginText(pdfStamper, pdfReader.getNumberOfPages(), intyg.getId(), MINA_INTYG_MARGIN_TEXT);
                break;
            case WEBCERT:
                // perform additional decoration for WC originated pdf
                if (isCertificateSentToFK(statuses)) {
                    maskSendToFkInformation(pdfStamper);
                    markAsElectronicCopy(pdfStamper);
                }
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

    private void generatePdf() {
        // Mandatory fields
        fillPatientDetails();
        fillRecommendationsKontaktMedForetagshalsovarden();
        fillCapacityRelativeToNuvarandeArbete();
        fillCapacityRelativeToOtherThanNuvarandeArbete();
        fillCapacity();
        fillTravel();
        fillSignerCodes();
        fillSignerNameAndAddress();

        // not mandatory - but filled in a standard pdf print copy
        fillRecommendationsOther();
        fillArbetsformaga();
        fillDiagnose();
        fillDiseaseCause();
        fillPrognose();
        fillIsSuspenseDueToInfection();
        fillBasedOn();
        fillDisability();
        fillOther();
        fillActivityLimitation();
        fillMeasures();
        fillRehabilitation();
        fillFkContact();
    }
}
