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
package se.inera.certificate.modules.ts_bas.validator.external;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import se.inera.certificate.clinicalprocess.healthcond.certificate.v1.HosPersonal;
import se.inera.certificate.clinicalprocess.healthcond.certificate.v1.Vardkontakt;
import se.inera.certificate.schema.Constants;
import se.inera.certificate.validate.PersonnummerValidator;
import se.inera.intygstjanster.ts.services.types.v1.II;
import se.inera.intygstjanster.ts.services.v1.IdentitetStyrkt;
import se.inera.intygstjanster.ts.services.v1.Patient;
import se.inera.intygstjanster.ts.services.v1.SkapadAv;
import se.inera.intygstjanster.ts.services.v1.TSBasIntyg;
import se.inera.intygstjanster.ts.services.v1.Vardenhet;
import se.inera.intygstjanster.ts.services.v1.Vardgivare;

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

    TransportValidatorInstance(List<String> validationErrors, ValidationContext context) {
        this.validationErrors = validationErrors;
        this.context = context;
    }

    public List<String> validate(TSBasIntyg utlatande) {
        context = new ValidationContext(utlatande);

        validateUtlatande(utlatande);
        validatePatient(utlatande.getGrundData().getPatient());
        validateHosPersonal(utlatande.getGrundData().getSkapadAv());
        validateVardkontakter(utlatande.getIdentitetStyrkt());

        // Do context related validation
        if (context.isPersontransportContext()) {
            // TODO Put context based stuff here
        }

        return validationErrors;
    }

    public List<String> getValidationErrors() {
        return validationErrors;
    }

    public ValidationContext getContext() {
        return context;
    }

    /**
     * Validates that required attributes connected with the actual class Utlatande are present.
     */
    private void validateUtlatande(TSBasIntyg utlatande) {
        assertNotNull(utlatande.getIntygsId(), "id");
        assertNotNull(utlatande.getIntygsTyp(), "utlatandetyp");
        assertNotNull(utlatande.getGrundData().getSigneringsTidstampel(), "signeringsdatum");
    }

    /**
     * Make sure Utlatande contains 1 Patient.
     */
    private void validatePatient(Patient patient) {
        if (assertNotNull(patient, "patient").failed()) {
            return;
        }
        assertValidPersonId(patient.getPersonId(), "patient.id");
        assertNotEmpty(patient.getFornamn(), "patient.fornamn");
        assertNotEmpty(patient.getEfternamn(), "patient.efternamn");
        assertNotEmpty(patient.getPostadress(), "patient.postadress");
        assertNotEmpty(patient.getPostnummer(), "patient.postnummer");
        assertNotEmpty(patient.getPostort(), "patient.postort");
    }

    /**
     * Validate HosPersonal, includes validating the HsaId, making sure a name is supplied and that a valid Vardenhet is
     * present.
     *
     * @param skapadAv
     *            {@link HosPersonal}
     */
    private void validateHosPersonal(SkapadAv skapadAv) {
        if (assertNotNull(skapadAv, "skapadAv").failed()) {
            return;
        }

        assertValidHsaId(skapadAv.getPersonId(), "skapadAv.id");

        assertNotEmpty(skapadAv.getFullstandigtNamn(), "skapadAv.fullstandigtNamn");
        // for (Kod befattning : skapadAv.getBefattning()) {
        // assertKodInEnum(befattning, BefattningKod.class, "skapadAv.befattning");
        // }
        // for (String specialitet : skapadAv.getSpecialiteter()) {
        // assertKodInEnum(specialitet, SpecialitetKod.class, "skapadAv.specialitet");
        // }

        validateVardenhet(skapadAv.getVardenhet(), "skapadAv");
    }

    /**
     * Validates Vardenhet contains required information.
     *
     * @param vardenhet
     *            {@link Vardenhet}
     * @param prefix
     *            {@link String} indicates where this instance of vardenhet is used in the model (i.e
     *            skapadAv.vardenhet...)
     */
    private void validateVardenhet(Vardenhet vardenhet, String prefix) {
        if (assertNotNull(vardenhet, prefix + ".vardenhet").failed()) {
            return;
        }

        assertValidHsaId(vardenhet.getEnhetsId(), prefix + ".vardenhet.id");
        assertNotEmpty(vardenhet.getEnhetsnamn(), prefix + ".vardenhet.namn");
        assertNotEmpty(vardenhet.getPostadress(), prefix + ".vardenhet.portadress");
        assertNotEmpty(vardenhet.getPostort(), prefix + ".vardenhet.postort");
        assertNotEmpty(vardenhet.getPostnummer(), prefix + ".vardenhet.postnummer");
        assertNotEmpty(vardenhet.getTelefonnummer(), prefix + ".vardenhet.telefonnummer");

        validateVardgivare(vardenhet.getVardgivare(), prefix + ".vardgivare");
    }

    /**
     * Validate vardgivare.
     *
     * @param vardgivare
     *            {@link Vardgivare}
     * @param prefix
     *            {@link String} prefix, where in the model this instance is used
     */
    private void validateVardgivare(Vardgivare vardgivare, String prefix) {
        if (assertNotNull(vardgivare, prefix + ".vardgivare").failed()) {
            return;
        }

        assertValidHsaId(vardgivare.getVardgivarid(), prefix + ".vardgivare.id");
        assertNotEmpty(vardgivare.getVardgivarnamn(), prefix + ".vardgivare.namn");
    }

    /**
     * Validate a list of Vardkontakter.
     *
     * @param vardkontakter
     *            List of {@link Vardkontakt}
     */
    private void validateVardkontakter(IdentitetStyrkt identitetStyrkt) {
        // assertKodInEnum(identitetStyrkt.getIdkontroll(), IdKontrollKod.class, "vardkontakt.idkontroll");
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
