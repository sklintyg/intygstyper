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
package se.inera.certificate.modules.rli.validator;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import se.inera.certificate.model.Kod;
import se.inera.certificate.modules.rli.model.codes.AktivitetsKod;
import se.inera.certificate.modules.rli.model.external.Aktivitet;
import se.inera.certificate.modules.rli.model.external.Arrangemang;
import se.inera.certificate.modules.rli.model.external.Utlatande;
import se.inera.certificate.modules.rli.model.external.common.HosPersonal;
import se.inera.certificate.modules.rli.model.external.common.Observation;
import se.inera.certificate.modules.rli.model.external.common.Patient;
import se.inera.certificate.modules.rli.model.external.common.Rekommendation;
import se.inera.certificate.validate.IdValidator;
import se.inera.certificate.validate.SimpleIdValidatorBuilder;

/**
 * @author erik
 * 
 */
public class ExternalValidatorImpl implements ExternalValidator {

    private IdValidator idValidator;

    public ExternalValidatorImpl() {
        idValidator = new SimpleIdValidatorBuilder().withPersonnummerValidator(false)
                .withSamordningsnummerValidator(false).build();
    }

    /*
     * (non-Javadoc)
     * 
     * @see se.inera.certificate.modules.rli.validator.ExternalValidator#validate()
     */
    @Override
    public List<String> validate(Utlatande utlatande) {

        List<String> validationErrors = new ArrayList<String>();

        validateUtlatande(utlatande, validationErrors);
        validatePatient(utlatande, validationErrors);
        validateSkapasAv(utlatande, validationErrors);
        validateRekommendationer(utlatande, validationErrors);
        validateAktiviteter(utlatande, validationErrors);
        validateObservationer(utlatande, validationErrors);
        validateArrangemang(utlatande, validationErrors);

        return validationErrors;
    }

    /*
     * (non-Javadoc)
     * 
     * Validates that required attributes connected with the actual class Utlatande are present
     */
    private void validateUtlatande(Utlatande utlatande, List<String> validationErrors) {

        if (utlatande.getId() == null) {
            validationErrors.add("No Utlatande ID found");
        }

        if (utlatande.getTyp() == null) {
            validationErrors.add("No Utlatandetyp found");
        }

        if (utlatande.getSigneringsdatum() == null) {
            validationErrors.add("No signeringsdatum found");
        }

    }

    /*
     * (non-Javadoc)
     * 
     * Validates Arrangemang in Utlatande First makes sure Arrangemang is not null, if so, the required attributes are
     * validated in turn and any errors are added to validation error list
     */
    private void validateArrangemang(Utlatande utlatande, List<String> validationErrors) {
        Arrangemang arrangemang = utlatande.getArrangemang();

        if (arrangemang == null) {
            validationErrors.add("Arrangemang was null");
        } else {

            // Make sure the correct SNOMED-CT code for Resa is present
            if (!arrangemang.getArrangemangstyp().getCode().equals("420008001")) {
                validationErrors.add("Code in arrangemang must be SNOMED-CT code: 420008001 (resa)");
            }

            if (arrangemang.getBokningsdatum() == null) {
                validationErrors.add("No bokningsdatum found in arrangemang");
            }

            if (arrangemang.getArrangemangstid() == null) {
                validationErrors.add("No arrangemangstid found in arrangemang");
            }

            if (arrangemang.getPlats() == null) {
                validationErrors.add("No plats found in arrangemang");
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * Validates Arrangemang in Utlatande First makes sure Arrangemang is not null, if so, the required attributes are
     * validated in turn and any errors are added to validation error list
     */
    private void validateSkapasAv(Utlatande utlatande, List<String> validationErrors) {
        HosPersonal skapatAv = utlatande.getSkapadAv();

        if (skapatAv == null) {
            validationErrors.add("Skapat av was null");
        } else {

            if (skapatAv.getPersonalId() == null) {
                validationErrors.add("No personalId found in Skapas av");
            }

            if (skapatAv.getFullstandigtNamn() == null) {
                validationErrors.add("No fullstandigt namn found in Skapas av");
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * Make sure Utlatande contains 1..* Observation
     */
    private void validateObservationer(Utlatande utlatande, List<String> validationErrors) {
        List<Observation> observationer = utlatande.getObservationer();

        if (observationer.isEmpty()) {
            validationErrors.add("No observations found");
        } else {
            for (Observation obs : observationer) {
                if (obs.getObservationsperiod() == null) {
                    validationErrors
                            .add("No observationsperiod found in: " + obs.getObservationskod().getCode() + "\n");
                }
            }

        }

    }

    /*
     * (non-Javadoc)
     * 
     * Validate Aktiviteter,
     */
    private void validateAktiviteter(Utlatande utlatande, List<String> validationErrors) {
        List<Aktivitet> aktiviteter = utlatande.getAktiviteter();

        if (aktiviteter.isEmpty()) {
            validationErrors.add("No aktiviteter found");
        } else {
            /*
             * Make sure Utlatande contains 1..2 Aktiviteter and nothing else
             */
            if (aktiviteter.size() < 1 || aktiviteter.size() > 2) {
                validationErrors.add("Utlatande does not contain 1 or 2 Aktiviteter");
            } else {
                /*
                 * Make sure at lest one Aktivitet contains the Aktivitetskod representing Klinisk Undersokning
                 */
                if (CollectionUtils.countMatches(aktiviteter, new AktivitetsKodPredicate(
                        AktivitetsKod.KLINISK_UNDERSOKNING)) < 1) {
                    validationErrors.add("At least one Aktivitet of type Klinisk Undersokning must be present");
                }
            }
        }
    }

    /*
     * (non-Javadoc) Make sure Utlatande contains 1..* Rekommendation
     */
    private void validateRekommendationer(Utlatande utlatande, List<String> validationErrors) {
        List<Rekommendation> rekommendationer = utlatande.getRekommendationer();
        String rekCodeRegex = "REK[1-7]{1}";
        String sjukCodeRegex = "SJK[1-4]{1}";

        if (rekommendationer.isEmpty()) {
            validationErrors.add("No Rekommendation found");
        } else {
            for (Rekommendation rek : rekommendationer) {
                if (rek.getRekommendationskod() == null) {
                    validationErrors.add("Missing rekommendationskod");
                }
                if (!Pattern.matches(rekCodeRegex, rek.getRekommendationskod().getCode())) {
                    validationErrors.add("Invalid rekommendationskod (valid codes are REK1 - REK7)");
                }
                if (rek.getSjukdomskannedom() == null) {
                    validationErrors.add("Missing sjukdomskannedom");
                }
                if (!Pattern.matches(sjukCodeRegex, rek.getSjukdomskannedom().getCode())) {
                    validationErrors.add("Invalid sjukdomskannedom (valid codes are SJK1 - SJK4)");
                }

            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * Make sure Utlatande contains 1 Patient
     */
    private void validatePatient(Utlatande utlatande, List<String> validationErrors) {

        Patient patient = utlatande.getPatient();

        if (patient == null) {
            validationErrors.add("Patient was null");
        } else {
            validationErrors.addAll(idValidator.validate(patient.getPersonId()));

            /*
             * Make sure Fornamn and Efternamn contains at least one item
             */
            if (patient.getFornamns().isEmpty()) {
                validationErrors.add("At least one Fornamn must be provided for Patient");
            }
            if (patient.getEfternamn() == null) {
                validationErrors.add("A Efternamn must be provided for Patient");
            }
        }
    }

    /**
     * Predicate class used in validateAktiviteter
     * 
     * @author erik
     * 
     */
    public class AktivitetsKodPredicate implements Predicate {

        private AktivitetsKod aktKodEnum;

        public AktivitetsKodPredicate(AktivitetsKod aktKodEnum) {
            this.aktKodEnum = aktKodEnum;
        }

        @Override
        public boolean evaluate(Object obj) {

            if (!(obj instanceof Aktivitet)) {
                return false;
            }

            Aktivitet akt = (Aktivitet) obj;
            Kod aktKod = akt.getAktivitetskod();

            return (aktKod.getCode().equals(aktKodEnum.getCode()));
        }

    }
}
