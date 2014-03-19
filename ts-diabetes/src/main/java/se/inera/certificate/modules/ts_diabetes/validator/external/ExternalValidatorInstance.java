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
package se.inera.certificate.modules.ts_diabetes.validator.external;

import java.util.ArrayList;
import java.util.List;

import se.inera.certificate.model.Id;
import se.inera.certificate.model.Kod;
import se.inera.certificate.model.Patient;
import se.inera.certificate.modules.ts_diabetes.model.codes.CodeConverter;
import se.inera.certificate.modules.ts_diabetes.model.codes.CodeSystem;
import se.inera.certificate.modules.ts_diabetes.model.codes.HSpersonalKod;
import se.inera.certificate.modules.ts_diabetes.model.codes.UtlatandeKod;
import se.inera.certificate.modules.ts_diabetes.model.external.Bilaga;
import se.inera.certificate.modules.ts_diabetes.model.external.Utlatande;
import se.inera.certificate.validate.IdValidator;
import se.inera.certificate.validate.SimpleIdValidatorBuilder;

public class ExternalValidatorInstance {

    protected final List<String> validationErrors;

    protected ValidationContext context;

    private AktiviteterValidationInstance aktivitetInstance;
    private ObservationerValidationInstance observationInstance;
    private RekommendationerValidationInstance rekommendationInstance;

    private final static IdValidator ID_VALIDATOR;

    static {
        SimpleIdValidatorBuilder builder = new SimpleIdValidatorBuilder();
        builder.withPersonnummerValidator(false);
        builder.withSamordningsnummerValidator(false);
        builder.withHsaIdValidator();

        ID_VALIDATOR = builder.build();
    }

    public ExternalValidatorInstance() {
        validationErrors = new ArrayList<>();
    }

    /* package */ExternalValidatorInstance(List<String> validationErrors, ValidationContext context) {
        this.validationErrors = validationErrors;
        this.context = context;
    }

    public List<String> validate(Utlatande utlatande) {
        context = new ValidationContext(utlatande);
        aktivitetInstance = new AktiviteterValidationInstance(this, utlatande.getAktiviteter());
        observationInstance = new ObservationerValidationInstance(this, utlatande.getObservationer());
        rekommendationInstance = new RekommendationerValidationInstance(this, utlatande.getRekommendationer());

        validateUtlatande(utlatande);
        validatePatient(utlatande.getPatient());
        validateBilaga(utlatande.getBilaga());

        aktivitetInstance.validateAktiviteter();
        rekommendationInstance.validateRekommendationer();
        observationInstance.validateObservationer();

        return validationErrors;
    }

    /**
     * Validates that required attributes connected with the actual class Utlatande are present
     */
    private void validateUtlatande(Utlatande utlatande) {
        assertNotNull(utlatande.getId(), "id");
        assertKodInEnum(utlatande.getTyp(), UtlatandeKod.class, "utlatandetyp");
        assertNotNull(utlatande.getSigneringsdatum(), "signeringsdatum");
    }

    /**
     * Validate that a bilaga is present
     * 
     */
    private void validateBilaga(Bilaga bilaga) {
        assertNotNull(bilaga, "Bilaga");
    }

    /**
     * Make sure Utlatande contains 1 Patient
     */
    private void validatePatient(Patient patient) {
        if (assertNotNull(patient, "patient").failed()) {
            return;
        }

        assertValidPersonId(patient.getId(), "patient.id");

        if (patient.getFornamn().size() < 1) {
            validationError("patient.fornamn must contain at least one name");
        }
        for (String fornamn : patient.getFornamn()) {
            assertNotEmpty(fornamn, "patient.fornamn");
        }
        assertNotEmpty(patient.getEfternamn(), "patient.efternamn");

        assertNotEmpty(patient.getPostadress(), "patient.postadress");
        assertNotEmpty(patient.getPostnummer(), "patient.postnummer");
        assertNotEmpty(patient.getPostort(), "patient.postort");
    }

    protected void validationError(String error) {
        validationErrors.add(error);
    }

    protected AssertionResult assertNotNull(Object value, String element) {
        if (value == null) {
            validationError(element + " was not found");
            return AssertionResult.FAILURE;
        }
        return AssertionResult.SUCCESS;
    }

    protected AssertionResult assertNull(Object value, String element) {
        if (value != null) {
            validationError(element + " should not be defined");
            return AssertionResult.FAILURE;
        }
        return AssertionResult.SUCCESS;
    }

    protected AssertionResult assertNotEmpty(String value, String element) {
        if (value == null || value.isEmpty()) {
            validationError(element + " was empty");
            return AssertionResult.FAILURE;
        }
        return AssertionResult.SUCCESS;
    }

    /**
     * Assert that a Kod is present in a specified Enum
     * 
     * @param kod
     *            the {@link Kod} to check
     * @param expectedEnum
     *            the enum-class extending {@link CodeSystem} against which the check is performed
     * @param element
     *            {@link String} identifying the element
     * @return {@link AssertationResult}
     */
    protected AssertionResult assertKodInEnum(Kod kod, Class<? extends CodeSystem> expectedEnum, String element) {
        if (assertNotNull(kod, element).success()) {
            try {
                CodeConverter.fromCode(kod, expectedEnum);
                return AssertionResult.SUCCESS;
            } catch (Exception e) {
                validationError(String.format("Kod '%s' in %s was unknown", kod.getCode(), element));
            }
        }
        return AssertionResult.FAILURE;
    }

    /**
     * Util method for pretty-printing a Kod
     * 
     * @param kod
     *            {@link Kod}
     * @return a nicely formatted {@link #String} containing the {@link Kod}
     */
    protected String getDisplayCode(Kod kod) {
        if (kod == null || kod.getCode() == null) {
            return "[?]";
        }

        return "[" + kod.getCode() + "]";
    }

    /**
     * Assert that a certain Kod is present a specified number of times
     * 
     * @param kodSet
     *            an {@link Iterable} of {@link Kod}'s
     * @param kodToCount
     *            the {@link Kod} to check against the kodSet
     * @param minCount
     *            required number of occurrences
     * @param maxCount
     *            maximum number of occurrences
     * @param element
     *            String identifying the context
     * @return {@link AssertationResult}
     */
    protected AssertionResult assertKodCountBetween(Iterable<Kod> kodSet, Kod kodToCount, int minCount, int maxCount,
            String element) {
        int count = 0;
        for (Kod kod : kodSet) {
            if (kod.equals(kodToCount)) {
                count++;
            }
        }

        if (maxCount < count || count < minCount) {
            if (minCount == maxCount) {
                validationError(String.format("%s must exist %s times; existed %s times", element
                        + getDisplayCode(kodToCount), minCount, count));
            } else {
                validationError(String.format("%s must exist between %s and %s times; existed %s times", element
                        + getDisplayCode(kodToCount), minCount, maxCount, count));
            }
            return AssertionResult.FAILURE;
        }

        return AssertionResult.SUCCESS;
    }

    protected void assertValidPersonId(Id id, String element) {
        if (assertNotNull(id, element).success()) {
            if (!id.getRoot().equals("1.2.752.129.2.1.3.1") && !id.getRoot().equals("1.2.752.129.2.1.3.3")) {
                validationError(element + " should be a personnummer or samordningsnummer");
            }
            validationErrors.addAll(ID_VALIDATOR.validate(id));
        }
    }

    protected void assertValidHsaId(Id id, String element) {
        if (assertNotNull(id, element).success()) {
            if (!id.getRoot().equals(HSpersonalKod.HSA_ID.getCode())) {
                validationError(element + " should be an HSA-ID with root: " + HSpersonalKod.HSA_ID.getCode());
            }
            validationErrors.addAll(ID_VALIDATOR.validate(id));
        }
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
