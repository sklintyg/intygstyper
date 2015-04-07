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
package se.inera.certificate.modules.ts_bas.validator.transport;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import se.inera.certificate.schema.Constants;
import se.inera.certificate.validate.PersonnummerValidator;
import se.inera.intygstjanster.ts.services.types.v1.II;
import se.inera.intygstjanster.ts.services.v1.TSBasIntyg;
import se.inera.intygstjanster.ts.services.v1.TSDiabetesIntyg;

import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

public class TransportValidatorInstance {

    private static PersonnummerValidator ID_VALIDATOR;

    private final List<String> validationErrors;

    private ValidationContext context;

    public TransportValidatorInstance() {
        validationErrors = new ArrayList<>();
        ID_VALIDATOR = new PersonnummerValidator();
        ID_VALIDATOR.setStrictSeparatorCheck(false);
    }

    public List<String> validate(TSBasIntyg utlatande) {
        context = new ValidationContext(utlatande);
        validateIds(utlatande);
        // Do context related validation
        if (context.isPersontransportContext()) {
            validatePersontransportRelatedElements(utlatande);
        }

        return validationErrors;
    }


    public List<String> getValidationErrors() {
        return validationErrors;
    }

    public ValidationContext getContext() {
        return context;
    }

    private void validateIds(TSBasIntyg utlatande) {
        // PersonId
        if (utlatande.getGrundData().getPatient() != null) {
            String id = utlatande.getGrundData().getPatient().getPersonId().getRoot();
            if(!id.equals(Constants.PERSON_ID_OID) && !id.equals(Constants.SAMORDNING_ID_OID)) {
                validationErrors.add(String.format("Root for patient.personnummer should be %s or %s but was %s",
                        Constants.PERSON_ID_OID, Constants.SAMORDNING_ID_OID, id));
            }
        }
        // Läkares HSAId
        if (utlatande.getGrundData().getSkapadAv() != null) {
            checkId(utlatande.getGrundData().getSkapadAv().getPersonId().getRoot(), Constants.HSA_ID_OID, "SkapadAv.hsaId");
        }
        // Vardenhet
        if (utlatande.getGrundData().getSkapadAv().getVardenhet() != null) {
            checkId(utlatande.getGrundData().getSkapadAv().getVardenhet().getEnhetsId().getRoot(), Constants.HSA_ID_OID, "vardenhet.enhetsId");
        }
        // vardgivare
        if (utlatande.getGrundData().getSkapadAv().getVardenhet().getVardgivare() != null) {
            checkId(utlatande.getGrundData().getSkapadAv().getVardenhet().getVardgivare().getVardgivarid().getRoot(), Constants.HSA_ID_OID,
                    "vardgivarId");
        }
    }
    private void checkId(String id, String expected, String field) {
        if (!id.equals(expected)) {
            validationErrors.add(String.format("Root for %s should be %s but was %s", field, expected, id));
        }
    }

    private void validatePersontransportRelatedElements(TSBasIntyg utlatande) {
        assertNotNull(utlatande.getRorelseorganensFunktioner().isHarOtillrackligRorelseformagaPassagerare(), 
                "rorelseorganensFunktioner.HarOtillrackligRorelseformagaPassagerare");
        assertNotNull(utlatande.getHorselBalanssinne().isHarSvartUppfattaSamtal4Meter(), 
                "horselBalanssinne.HarOtillrackligRorelseformagaPassagerare");
    }

    protected void validationError(String error) {
        validationErrors.add(error);
    }

    /**
     * Assert that the value of Object is not null.
     *
     * @param value
     *            {@link Object}
     * @param element
     *            {@link String} identifying element the under scrutiny
     * @return {@link AssertionResult}
     */
    protected AssertionResult assertNotNull(Object value, String element) {
        if (value == null) {
            validationError(element + " was not found");
            return AssertionResult.FAILURE;
        }
        return AssertionResult.SUCCESS;
    }

    /**
     * Assert if value is null or empty, if the object is a collection and is empty the validationError
     * "should not be empty" is raised, if its not and the object is null, the validationError "should not be defined"
     * is raised.
     *
     * @param value
     *            {@link Object}
     * @param element
     *            {@link String} identifying the element
     * @return {@link AssertionResult}
     */
    protected AssertionResult assertNull(Object value, String element) {
        if (value instanceof Collection<?>) {
            if (!((Collection<?>) value).isEmpty()) {
                validationError(element + " should not be empty");
                return AssertionResult.FAILURE;

            }
        } else if (value != null) {
            validationError(element + " should not be defined");
            return AssertionResult.FAILURE;
        }
        return AssertionResult.SUCCESS;
    }

    /**
     * Assert if a String is not empty.
     *
     * @param value
     *            {@link String}
     * @param element
     *            {@link String} identifying the examined string
     * @return {@link AssertionResult}.SUCCESS if the String was not empty or null, {@link AssertionResult} .FAILURE if
     *         it was
     */
    protected AssertionResult assertNotEmpty(String value, String element) {
        if (value == null || value.isEmpty()) {
            validationError(element + " was empty");
            return AssertionResult.FAILURE;
        }
        return AssertionResult.SUCCESS;
    }

    /**
     * Assert the supplied personId has the correct code root.
     *
     * @param id
     *            {@link Id} the id to check
     * @param element
     *            string identifying the context
     */
    protected void assertValidPersonId(II id, String element) {
        if (assertNotNull(id, element).success()) {
            if (!id.getRoot().equals("1.2.752.129.2.1.3.1") && !id.getRoot().equals("1.2.752.129.2.1.3.3")) {
                validationError(element + " should be a personnummer or samordningsnummer");
            }
            validationErrors.addAll(ID_VALIDATOR.validateExtension(id.getExtension()));
        }
    }

    /**
     * Assert the supplied HsaId has the correct code root.
     *
     * @param id
     *            {@link Id} the id to check
     * @param element
     *            string identifying the context
     */
    protected void assertValidHsaId(II id, String element) {
        if (assertNotNull(id, element).success()) {
            if (!id.getRoot().equals(Constants.HSA_ID_OID))
                validationError(element + " should be an HSA-ID with root: " + Constants.HSA_ID_OID);
        }
    }

    /**
     * Since the validator assertions doesn't throw exceptions on assertion failure, they instead return an assertion
     * result. This might be used to implement conditional logic based on if an assertion {@link #failed()} or was
     * {@link #success()}ful.
     */
    enum AssertionResult {
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
