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
package se.inera.certificate.modules.ts_bas.validator;

import java.util.ArrayList;
import java.util.List;

import se.inera.certificate.model.Id;
import se.inera.certificate.model.Kod;
import se.inera.certificate.model.Patient;
import se.inera.certificate.model.Vardenhet;
import se.inera.certificate.model.Vardgivare;
import se.inera.certificate.modules.ts_bas.model.codes.CodeConverter;
import se.inera.certificate.modules.ts_bas.model.codes.CodeSystem;
import se.inera.certificate.modules.ts_bas.model.codes.HSpersonalKod;
import se.inera.certificate.modules.ts_bas.model.codes.IdKontrollKod;
import se.inera.certificate.modules.ts_bas.model.codes.IntygAvserKod;
import se.inera.certificate.modules.ts_bas.model.codes.SpecialitetKod;
import se.inera.certificate.modules.ts_bas.model.codes.UtlatandeKod;
import se.inera.certificate.modules.ts_bas.model.codes.VardkontakttypKod;
import se.inera.certificate.modules.ts_bas.model.external.Aktivitet;
import se.inera.certificate.modules.ts_bas.model.external.HosPersonal;
import se.inera.certificate.modules.ts_bas.model.external.Observation;
import se.inera.certificate.modules.ts_bas.model.external.Rekommendation;
import se.inera.certificate.modules.ts_bas.model.external.Utlatande;
import se.inera.certificate.modules.ts_bas.model.external.Vardkontakt;
import se.inera.certificate.validate.IdValidator;
import se.inera.certificate.validate.SimpleIdValidatorBuilder;

public class ExternalValidatorInstance {

    protected final List<String> validationErrors;

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

    /* package */ExternalValidatorInstance(List<String> validationErrors) {
        this.validationErrors = validationErrors;
    }

    public List<String> validate(Utlatande utlatande) {
        validateUtlatande(utlatande);
        validatePatient(utlatande.getPatient());
        validateHosPersonal(utlatande.getSkapadAv());
        validateAktiviteter(utlatande.getAktiviteter());
        validateVardkontakter(utlatande.getVardkontakter());
        validateRekommendationer(utlatande.getRekommendationer());
        validateObservationer(utlatande.getObservationer());

        return validationErrors;
    }

    /**
     * Validates that required attributes connected with the actual class Utlatande are present
     */
    private void validateUtlatande(Utlatande utlatande) {
        assertNotNull(utlatande.getId(), "id");
        assertKodInEnum(utlatande.getTyp(), UtlatandeKod.class, "utlatandetyp");
        assertNotNull(utlatande.getSigneringsdatum(), "signeringsdatum");
        for (Kod intygAvser : utlatande.getIntygAvser()) {
            assertKodInEnum(intygAvser, IntygAvserKod.class, "intygAvser");
        }
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

    private void validateHosPersonal(HosPersonal skapadAv) {
        if (assertNotNull(skapadAv, "skapadAv").failed()) {
            return;
        }

        assertValidHsaId(skapadAv.getId(), "skapadAv.id");

        assertNotEmpty(skapadAv.getNamn(), "skapadAv.fullstandigtNamn");
        // for (Kod befattning : skapadAv.getBefattning()) {
        // assertKodInEnum(befattning, BefattningKod.class, "skapadAv.befattning");
        // }
        for (Kod specialitet : skapadAv.getSpecialiteter()) {
            assertKodInEnum(specialitet, SpecialitetKod.class, "skapadAv.specialitet");
        }

        validateVardenhet(skapadAv.getVardenhet(), "skapadAv");
    }

    private void validateVardenhet(Vardenhet vardenhet, String prefix) {
        if (assertNotNull(vardenhet, prefix + ".vardenhet").failed()) {
            return;
        }

        assertValidHsaId(vardenhet.getId(), prefix + ".vardenhet.id");
        assertNotEmpty(vardenhet.getNamn(), prefix + ".vardenhet.namn");
        assertNotEmpty(vardenhet.getPostadress(), prefix + ".vardenhet.portadress");
        assertNotEmpty(vardenhet.getPostort(), prefix + ".vardenhet.postort");
        assertNotEmpty(vardenhet.getPostnummer(), prefix + ".vardenhet.postnummer");
        assertNotEmpty(vardenhet.getTelefonnummer(), prefix + ".vardenhet.telefonnummer");

        validateVardgivare(vardenhet.getVardgivare(), prefix + ".vardgivare");
    }

    private void validateVardgivare(Vardgivare vardgivare, String prefix) {
        if (assertNotNull(vardgivare, prefix + ".vardgivare").failed()) {
            return;
        }

        assertValidHsaId(vardgivare.getId(), prefix + ".vardgivare.id");
        assertNotEmpty(vardgivare.getNamn(), prefix + ".vardgivare.namn");
    }

    private void validateAktiviteter(List<Aktivitet> aktiviteter) {
        AktiviteterValidationInstance instance = new AktiviteterValidationInstance(validationErrors);

        instance.validateAktiviteter(aktiviteter);
    }

    private void validateVardkontakter(List<Vardkontakt> vardkontakter) {
        if (vardkontakter.size() != 1) {
            validationError("Expected only one vardkontakt");
            return;
        }

        Vardkontakt vardkontakt = vardkontakter.get(0);
        assertKodInEnum(vardkontakt.getVardkontakttyp(), VardkontakttypKod.class, "vardkontakt.vardkontakttyp");
        assertKodInEnum(vardkontakt.getIdkontroll(), IdKontrollKod.class, "vardkontakt.idkontroll");
    }

    private void validateRekommendationer(List<Rekommendation> rekommendationer) {
        RekommendationerValidationInstance instance = new RekommendationerValidationInstance(validationErrors);

        instance.validateRekommendationer(rekommendationer);
    }

    private void validateObservationer(List<Observation> observationer) {
        ObservationerValidationInstance instance = new ObservationerValidationInstance(validationErrors);

        instance.validateObservationer(observationer);
    }

    protected String getDisplayCode(Kod kod) {
        if (kod == null || kod.getCode() == null) {
            return "[?]";
        }

        return "[" + kod.getCode() + "]";
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
    private static enum AssertionResult {
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
