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
import org.apache.cxf.common.util.StringUtils;

import se.inera.certificate.model.HosPersonal;
import se.inera.certificate.model.Kod;
import se.inera.certificate.model.Observation;
import se.inera.certificate.model.Patient;
import se.inera.certificate.model.Rekommendation;
import se.inera.certificate.modules.rli.model.codes.AktivitetsKod;
import se.inera.certificate.modules.rli.model.codes.ArrangemangsTyp;
import se.inera.certificate.modules.rli.model.codes.HSpersonalTyp;
import se.inera.certificate.modules.rli.model.codes.ObservationsKod;
import se.inera.certificate.modules.rli.model.external.Aktivitet;
import se.inera.certificate.modules.rli.model.external.Arrangemang;
import se.inera.certificate.modules.rli.model.external.Utlatande;
import se.inera.certificate.validate.IdValidator;
import se.inera.certificate.validate.SimpleIdValidatorBuilder;

/**
 * @author erik
 * 
 */
public class ExternalValidatorImpl implements ExternalValidator {

    private IdValidator idValidator;

    public ExternalValidatorImpl() {
        idValidator = new SimpleIdValidatorBuilder().withPersonnummerValidator(true)
                .withSamordningsnummerValidator(true).build();

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

    /**
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

    /**
     * 
     * Validates Arrangemang in Utlatande First makes sure Arrangemang is not null, if so, the required attributes are
     * validated in turn and any errors are added to validation error list
     */
    private void validateArrangemang(Utlatande utlatande, List<String> validationErrors) {
        Arrangemang arrangemang = utlatande.getArrangemang();

        if (arrangemang == null) {
            validationErrors.add("Arrangemang was null");
            return;
        }
        if (arrangemang.getArrangemangstyp().getCode() == null
                || !arrangemang.getArrangemangstyp().getCode().equals(ArrangemangsTyp.RESA.getCode())) {
            validationErrors.add("Code in arrangemang must be SNOMED-CT code: " + ArrangemangsTyp.RESA.getCode());
        }

        checkBokningsdatum(arrangemang, validationErrors);

        if (arrangemang.getArrangemangstid() == null) {
            validationErrors.add("No arrangemangstid found in arrangemang");
        }

        if (arrangemang.getPlats() == null) {
            validationErrors.add("No plats found in arrangemang");
        }

    }

    private void checkBokningsdatum(Arrangemang arrangemang, List<String> validationErrors) {
        if (arrangemang.getBokningsdatum() == null) {
            validationErrors.add("Bokningsdatum was null");
        }
    }

    private void validateSkapasAv(Utlatande utlatande, List<String> validationErrors) {
        HosPersonal skapatAv = utlatande.getSkapadAv();

        if (skapatAv == null) {
            validationErrors.add("Skapat av was null");
            return;
        }

        if (skapatAv.getId() != null) {
            /**
             * Make sure the id specified has the correct root (HSA-IDs have 1.2.752.129.2.1.4.1)
             */

            if (!skapatAv.getId().getRoot().equals(HSpersonalTyp.HSA_ID.getCode())) {
                validationErrors.add("PersonalId should be an HSA-ID with root: " + HSpersonalTyp.HSA_ID.getCode());
            }
        } else {
            validationErrors.add("No personal ID found");
        }
        if (skapatAv.getNamn() == null) {
            validationErrors.add("No fullstandigt namn found in Skapas av");
        }

    }

    /**
     * 
     * Make sure Utlatande contains 1..* Observation
     */
    private void validateObservationer(Utlatande utlatande, List<String> validationErrors) {
        List<Observation> observationer = utlatande.getObservationer();

        if (observationer.isEmpty()) {
            validationErrors.add("No observations found");
            return;
        }

        for (Observation obs : observationer) {
            checkObservation(obs, validationErrors);
        }

    }

    private void checkObservation(Observation source, List<String> validationErrors) {
        if (source.getObservationskod().getCode() == ObservationsKod.GRAVIDITET.getCode()
                && source.getObservationsperiod() == null) {
            validationErrors.add("No observationsperiod found in: " + source.getObservationskod().getCode());
        }

        if (source.getUtforsAv().getAntasAv().getId() == null) {
            validationErrors.add("No Personal Id found in Utfors av -> Antas av");
            return;
        }

        if (!source.getUtforsAv().getAntasAv().getId().getRoot().equals(HSpersonalTyp.HSA_ID.getCode())) {
            validationErrors.add("PersonalId should be an HSA-ID with extension: " + HSpersonalTyp.HSA_ID.getCode());
        }
    }

    /**
     * 
     * Validate Aktiviteter,
     */
    private void validateAktiviteter(Utlatande utlatande, List<String> validationErrors) {
        List<Aktivitet> aktiviteter = utlatande.getAktiviteter();

        if (aktiviteter.isEmpty()) {
            validationErrors.add("No aktiviteter found");
            return;
        }
        /**
         * Make sure Utlatande contains 1..2 Aktiviteter and nothing else
         */
        if (aktiviteter.size() < 1 && aktiviteter.size() > 2) {
            validationErrors.add("Utlatande does not contain 1 or 2 Aktiviteter");
            return;
        }
        /**
         * Make sure one instance of aktivitet is of type KLINISK UNDERSOKNING
         */
        Aktivitet akt = (Aktivitet) CollectionUtils.find(aktiviteter, new AktivitetsKodPredicate(
                AktivitetsKod.KLINISK_UNDERSOKNING));

        if (akt != null) {
            if (akt.getAktivitetstid() == null) {
                validationErrors
                        .add("Aktivitetstid must be specified for Aktivitet of type Klinisk Undersokning (AV020 / UNS)");
            }
        } else {
            /**
             * Akt was null
             */
            validationErrors.add("No aktivitet of type KLINISK UNDERSOKNING found");
        }
    }

    /**
     * Make sure Utlatande contains 1..* Rekommendationer and that a correct code is used.
     */
    public void validateRekommendationer(Utlatande utlatande, List<String> validationErrors) {
        final String REK_CODE = "REK[1-2, 5-7]{1}";
        final String SJK_CODE = "SJK[1-4]{1}";

        List<Rekommendation> rekommendationer = utlatande.getRekommendationer();
        if (rekommendationer.isEmpty()) {
            validationErrors.add("No Rekommendation found");
            return;
        }

        for (Rekommendation rek : rekommendationer) {
            if (rek.getRekommendationskod() == null
                    || !Pattern.matches(REK_CODE, rek.getRekommendationskod().getCode())) {
                validationErrors.add("No valid rekommendationskod found");
            }
            if (rek.getSjukdomskannedom() == null || !Pattern.matches(SJK_CODE, rek.getSjukdomskannedom().getCode())) {
                validationErrors.add("No valid sjukdomskannedomskod found");
            }
        }

    }

    /**
     * Make sure Utlatande contains 1 Patient
     */
    private void validatePatient(Utlatande utlatande, List<String> validationErrors) {

        Patient patient = utlatande.getPatient();

        if (patient == null) {
            validationErrors.add("Patient was null");
            return;
        }

        if (patient.getId() == null) {
            validationErrors.add("Patient ID was null");
            return;
        }

        validationErrors.addAll(idValidator.validate(patient.getId()));

        /**
         * Make sure Fornamn and Efternamn contains at least one item
         */
        if (patient.getFornamn().isEmpty()) {
            validationErrors.add("At least one Fornamn must be provided for Patient");
        }

        if (StringUtils.isEmpty(patient.getEfternamn())) {
            validationErrors.add("An Efternamn must be provided for Patient");
        }

    }

    /**
     * Predicate for AktivitetsKod
     * 
     * @author erik
     * 
     */
    private class AktivitetsKodPredicate implements Predicate {

        private final AktivitetsKod aktKodEnum;

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
