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
package se.inera.certificate.modules.rli.validator.external;

import java.util.ArrayList;
import java.util.List;

import se.inera.certificate.model.Id;
import se.inera.certificate.model.Kod;
import se.inera.certificate.model.Rekommendation;
import se.inera.certificate.modules.rli.model.codes.AktivitetsKod;
import se.inera.certificate.modules.rli.model.codes.ArrangemangsKod;
import se.inera.certificate.modules.rli.model.codes.BefattningsKod;
import se.inera.certificate.modules.rli.model.codes.CodeConverter;
import se.inera.certificate.modules.rli.model.codes.CodeSystem;
import se.inera.certificate.modules.rli.model.codes.HSpersonalKod;
import se.inera.certificate.modules.rli.model.codes.ObservationsKod;
import se.inera.certificate.modules.rli.model.codes.RekommendationsKod;
import se.inera.certificate.modules.rli.model.codes.SjukdomskannedomKod;
import se.inera.certificate.modules.rli.model.codes.UtforarrollKod;
import se.inera.certificate.modules.rli.model.codes.UtlatandeKod;
import se.inera.certificate.modules.rli.model.external.Aktivitet;
import se.inera.certificate.modules.rli.model.external.Arrangemang;
import se.inera.certificate.modules.rli.model.external.HosPersonal;
import se.inera.certificate.modules.rli.model.external.Observation;
import se.inera.certificate.modules.rli.model.external.Patient;
import se.inera.certificate.modules.rli.model.external.Utforarroll;
import se.inera.certificate.modules.rli.model.external.Utlatande;
import se.inera.certificate.modules.rli.model.external.Vardenhet;
import se.inera.certificate.validate.IdValidator;
import se.inera.certificate.validate.SimpleIdValidatorBuilder;

public class ExternalValidatorInstance {

    private final List<String> validationErrors;

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

    public List<String> validate(Utlatande utlatande) {
        validateUtlatande(utlatande);
        validatePatient(utlatande.getPatient());
        validateHosPersonal(utlatande.getSkapadAv(), "skapadAv");
        validateRekommendationer(utlatande.getRekommendationer());
        validateAktiviteter(utlatande.getAktiviteter());
        validateObservationer(utlatande.getObservationer());
        validateArrangemang(utlatande.getArrangemang());

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
     * Validates Arrangemang in Utlatande First makes sure Arrangemang is not null, if so, the required attributes are
     * validated in turn and any errors are added to validation error list
     */
    private void validateArrangemang(Arrangemang arrangemang) {
        if (assertNotNull(arrangemang, "arrangemang").failed()) {
            return;
        }

        assertNotNull(arrangemang.getBokningsdatum(), "arrangemang.bokningsdatum");
        if (assertNotNull(arrangemang.getArrangemangstid(), "arrangemang.arrangemangstid").success()) {
            assertNotNull(arrangemang.getArrangemangstid().getFrom(), "arrangemang.arrangemangstid.from");
            assertNull(arrangemang.getArrangemangstid().getTom(), "arrangemang.arrangemangstid.tom");
        }
        assertKodInEnum(arrangemang.getArrangemangstyp(), ArrangemangsKod.class, "arrangemang.arrangemangstyp");
        assertNotEmpty(arrangemang.getPlats(), "plats");
    }

    private void validateHosPersonal(HosPersonal skapatAv, String elementPrefix) {
        if (assertNotNull(skapatAv, elementPrefix).failed()) {
            return;
        }

        assertValidHsaId(skapatAv.getId(), elementPrefix + ".id");

        assertNotEmpty(skapatAv.getNamn(), elementPrefix + ".namn");

        if (skapatAv.getBefattning() != null) {
            // TODO: Change befattning to Kod when the time is right.
            assertKodInEnum(new Kod(skapatAv.getBefattning()), BefattningsKod.class, elementPrefix + ".befattning");
        }

        validateVardenhet(skapatAv.getVardenhet(), elementPrefix);
    }

    private void validateVardenhet(Vardenhet vardenhet, String elementPrefix) {
        if (assertNotNull(vardenhet, elementPrefix + ".vardenhet").failed()) {
            return;
        }

        assertValidHsaId(vardenhet.getId(), elementPrefix + ".vardenhet.id");
        assertNotEmpty(vardenhet.getNamn(), elementPrefix + ".vardenhet.namn");

        if (assertNotNull(vardenhet.getVardgivare(), elementPrefix + ".vardenhet.vardgivare").success()) {
            assertValidHsaId(vardenhet.getVardgivare().getId(), elementPrefix + ".vardenhet.vardgivare.id");
            assertNotEmpty(vardenhet.getVardgivare().getNamn(), elementPrefix + ".vardenhet.vardgivare.namn");
        }
    }

    /**
     * Make sure Utlatande contains 1..* Observation
     */
    private void validateObservationer(List<Observation> observationer) {
        if (observationer.size() < 1 || observationer.size() > 2) {
            validationError("one or two observationer must be present");
            return;
        }

        for (Observation observation : observationer) {
            String entity = "observation" + getDisplayCode(observation.getObservationskod());

            assertKodInEnum(observation.getObservationskod(), ObservationsKod.class, entity + ".observationskod");
            if (ObservationsKod.GRAVIDITET.matches(observation.getObservationskod())) {
                if (assertNotNull(observation.getObservationsperiod(), entity + ".observationsperiod").success()) {
                    assertNull(observation.getObservationsperiod().getFrom(), entity + ".observationsperiod.from");
                    assertNotNull(observation.getObservationsperiod().getTom(), entity + ".observationsperiod.tom");
                }

            } else {
                assertNull(observation.getObservationsperiod(), entity + ".observationsperiod");
            }

            if (observation.getUtforsAv() != null) {
                validateUtforsAv(observation.getUtforsAv(), entity);
            }
        }

    }

    private void validateUtforsAv(Utforarroll utforsAv, String elementPrefix) {
        assertKodInEnum(utforsAv.getUtforartyp(), UtforarrollKod.class, elementPrefix + ".utforsAv.utforartyp");

        validateHosPersonal(utforsAv.getAntasAv(), elementPrefix + ".antasAv");
    }

    /**
     * Validate Aktiviteter,
     */
    private void validateAktiviteter(List<Aktivitet> aktiviteter) {
        /**
         * Make sure Utlatande contains 1..3 Aktiviteter and nothing else
         */
        if (aktiviteter.size() < 1 && aktiviteter.size() > 3) {
            validationError("Utlatande does not contain 1 to 3 Aktiviteter");
            return;
        }

        for (Aktivitet aktivitet : aktiviteter) {
            String entity = "aktivitet" + getDisplayCode(aktivitet.getAktivitetskod());
            assertKodInEnum(aktivitet.getAktivitetskod(), AktivitetsKod.class, entity + ".aktivitetskod");

            /** Make sure Aktivitet of type KLINISK_UNDERSOKNING contains utforsVid */
            if (AktivitetsKod.KLINISK_UNDERSOKNING.matches(aktivitet.getAktivitetskod())) {
                assertNotNull(aktivitet.getAktivitetstid(), entity + ".aktivitetstid");
                assertNotNull(aktivitet.getUtforsVid(), entity + ".utforsVid");
            }
            /** Make sure Aktivitet of type FORSTA_UNDERSOKNING contains Plats */
            if (AktivitetsKod.FORSTA_UNDERSOKNING.matches(aktivitet.getAktivitetskod())) {
                assertNotNull(aktivitet.getAktivitetstid(), entity + ".aktivitetstid");
                assertNotNull(aktivitet.getPlats(), entity + ".plats");
            }

        }

    }

    /**
     * Make sure Utlatande contains 1..* Rekommendationer and that a correct code is used.
     */
    private void validateRekommendationer(List<Rekommendation> rekommendationer) {
        for (Rekommendation rek : rekommendationer) {
            assertKodInEnum(rek.getRekommendationskod(), RekommendationsKod.class, "rekommendation.rekomendationskod");
            assertKodInEnum(rek.getSjukdomskannedom(), SjukdomskannedomKod.class, "rekommendation.sjukdomskannedom");
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

    private String getDisplayCode(Kod kod) {
        if (kod == null || kod.getCode() == null) {
            return "[?]";
        }

        return "[" + kod.getCode() + "]";
    }

    private void validationError(String error) {
        validationErrors.add(error);
    }

    private AssertionResult assertNotNull(Object value, String element) {
        if (value == null) {
            validationError(element + " was not found");
            return AssertionResult.FAILURE;
        }
        return AssertionResult.SUCCESS;
    }

    private AssertionResult assertNull(Object value, String element) {
        if (value != null) {
            validationError(element + " should not be defined");
            return AssertionResult.FAILURE;
        }
        return AssertionResult.SUCCESS;
    }

    private AssertionResult assertNotEmpty(String value, String element) {
        if (value == null || value.isEmpty()) {
            validationError(element + " was empty");
            return AssertionResult.FAILURE;
        }
        return AssertionResult.SUCCESS;
    }

    private AssertionResult assertKodInEnum(Kod kod, Class<? extends CodeSystem> expectedEnum, String element) {
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

    private void assertValidPersonId(Id id, String element) {
        if (assertNotNull(id, element).success()) {
            if (!id.getRoot().equals("1.2.752.129.2.1.3.1") && !id.getRoot().equals("1.2.752.129.2.1.3.3")) {
                validationError(element + " should be a personnummer or samordningsnummer");
            }
            validationErrors.addAll(ID_VALIDATOR.validate(id));
        }
    }

    private void assertValidHsaId(Id id, String element) {
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
