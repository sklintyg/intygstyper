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
package se.inera.certificate.modules.rli.validator.internal;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.inera.certificate.modules.rli.model.internal.wc.Arrangemang;
import se.inera.certificate.modules.rli.model.internal.wc.Patient;
import se.inera.certificate.modules.rli.model.internal.wc.Rekommendation;
import se.inera.certificate.modules.rli.model.internal.wc.HoSPersonal;
import se.inera.certificate.modules.rli.model.internal.wc.Undersokning;

import se.inera.certificate.modules.support.api.dto.ValidateDraftResponse;
import se.inera.certificate.modules.support.api.dto.ValidationMessage;
import se.inera.certificate.modules.support.api.dto.ValidationStatus;

public class InternalValidatorInstance {

    private static final String POSTNUMMER_FORMAT = "\\d{3}\\s?\\d{2}";

    private static Logger LOG = LoggerFactory.getLogger(InternalValidatorInstance.class);

    private List<ValidationMessage> validationMessages;

    public InternalValidatorInstance() {
        validationMessages = new ArrayList<>();
    }

    /**
     * Validates an internal draft of an {@link Utlatande} (this means the object being validated is not necessarily
     * complete)
     * 
     * @param utlatande
     *            an internal {@link Utlatande}
     * @return a {@link ValidateDraftResponseHolder} with a status and a list of validationErrors
     */
    public ValidateDraftResponse validate(se.inera.certificate.modules.rli.model.internal.wc.Utlatande utlatande) {
        if (utlatande == null) {
            addValidationError("utlatande", "ts.validation.utlatande.missing");

        } else {
            // TODO: Validate stuff here..
            validateHoSPersonal(utlatande.getSkapadAv());
            validateArrangemang(utlatande.getArrangemang());
            validatePatient(utlatande.getPatient());
            validateRekommendation(utlatande.getRekommendation());
            validateUndersokning(utlatande.getUndersokning());
        }
        ValidateDraftResponse response = new ValidateDraftResponse(getValidationStatus(), validationMessages);

        return response;
    }

    private void validateUndersokning(Undersokning undersokning) {
        // TODO Auto-generated method stub

    }

    private void validateHoSPersonal(final HoSPersonal skapadAv) {
        if (skapadAv == null) {
            LOG.debug("No HoSPersonal found");
            return;
        }
        assertNotNullOrEmpty(skapadAv.getVardenhet().getEnhetsnamn(), "vardenhet.namn",
                "rli.validation.vardenhet.namn.missing");

    }

    private void validateRekommendation(Rekommendation rekommendation) {
        // TODO Auto-generated method stub

    }

    private void validatePatient(Patient patient) {
        // TODO Auto-generated method stub

    }

    private void validateArrangemang(Arrangemang arrangemang) {
        assertNotNullOrEmpty(arrangemang.getPlats(), "arrangemang.plats", "rli.validation.arrangemang.plats.missing");

    }

    /**
     * Check for null or empty String, if so add a validation error for field with errorCode
     * 
     * @param string
     *            the String to check
     * @param field
     *            the target field in the model
     * @param errorCode
     *            the errorCode to log in validation errors
     */
    private AssertionResult assertNotNullOrEmpty(String string, String field, String errorCode) {
        if (string == null || string.isEmpty()) {
            addValidationError(field, errorCode);
            LOG.debug(field + " " + errorCode);
            return AssertionResult.FAILURE;
        }
        return AssertionResult.SUCCESS;
    }

    /**
     * Make sure a string representing a date conforms to the desired format
     * 
     * @param dateString
     *            the date
     * @param dateFormat
     *            the format
     * @return true if it does, false otherwise
     */
    private boolean isValidDate(String dateString, String dateFormat) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern(dateFormat);
        try {
            LocalDate.parse(dateString, formatter);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * Check if there are validation errors
     * 
     * @return {@link ValidationStatus.COMPLETE} if there are no errors, and {@link ValidationStatus.INCOMPLETE}
     *         otherwise
     */
    private ValidationStatus getValidationStatus() {
        return (validationMessages.isEmpty()) ? se.inera.certificate.modules.support.api.dto.ValidationStatus.VALID
                : se.inera.certificate.modules.support.api.dto.ValidationStatus.INVALID;
    }

    /**
     * Create a ValidationMessage and add it to the {@link ValidateDraftResponseHolder}
     * 
     * @param field
     *            a String with the name of the field
     * @param msg
     *            a String with an error code for the front end implementation
     */
    private void addValidationError(String field, String msg) {
        validationMessages.add(new ValidationMessage(field, msg));
        LOG.debug(field + " " + msg);
    }

    /**
     * Since the validator assertions doesn't throw exceptions on assertion failure, they instead return an assertion
     * result. This might be used to implement conditional logic based on if an assertion {@link #failed()} or was
     * {@link #success()}ful.
     */
    protected static enum AssertionResult {
        SUCCESS(true), FAILURE(false);

        private AssertionResult(boolean assertSuccessfull) {
            this.assertSuccessful = assertSuccessfull;
        }

        private final boolean assertSuccessful;

        public boolean failed() {
            return !assertSuccessful;
        }

        public boolean success() {
            return assertSuccessful;
        }
    }

}
