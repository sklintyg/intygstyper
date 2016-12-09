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

package se.inera.intyg.intygstyper.fk7263.schemas.insuranceprocess.healthreporting.validator;

import java.util.List;

import com.google.common.base.Strings;

import se.inera.ifv.insuranceprocess.healthreporting.medcertqa.v1.VardAdresseringsType;
import se.inera.ifv.insuranceprocess.healthreporting.v2.*;
import se.inera.intyg.common.support.Constants;


/**
 * @author andreaskaltenbach
 */
public class VardAdresseringsTypeValidator {

    private VardAdresseringsType vardAdress;
    private List<String> validationErrors = null;

    public VardAdresseringsTypeValidator(VardAdresseringsType vardAdress, List<String> validationErrors) {
        this.vardAdress = vardAdress;
        this.validationErrors = validationErrors;
    }

    public void validateAndCorrect() {
        if (vardAdress == null) {
            validationErrors.add("No vardAdress element found!");
            return;
        }

        HosPersonalType hosPersonal = vardAdress.getHosPersonal();
        if (hosPersonal == null) {
            validationErrors.add("No SkapadAvHosPersonal element found!");
            return;
        }

        // Check lakar id - mandatory
        if (Strings.isNullOrEmpty(hosPersonal.getPersonalId().getExtension())) {
            validationErrors.add("No personal-id found!");
        }

        // Check lakar id o.i.d.
        if (hosPersonal.getPersonalId().getRoot() == null
                || !hosPersonal.getPersonalId().getRoot().equals(Constants.HSA_ID_OID)) {
            validationErrors.add("Wrong o.i.d. for personalId! Should be " + Constants.HSA_ID_OID);
        }

        // Check lakarnamn - mandatory
        if (Strings.isNullOrEmpty(hosPersonal.getFullstandigtNamn())) {
            validationErrors.add("No skapadAvHosPersonal fullstandigtNamn found.");
        }

        validateHosPersonalEnhet(hosPersonal.getEnhet());
    }

    private void validateHosPersonalEnhet(EnhetType enhet) {

        if (enhet == null) {
            validationErrors.add("No enhet element found!");
            return;
        }

        // Check enhets id - mandatory
        if (enhet.getEnhetsId() == null || Strings.isNullOrEmpty(enhet.getEnhetsId().getExtension())) {
            validationErrors.add("No enhets-id found!");
        }

        // Check enhets o.i.d
        if (enhet.getEnhetsId() == null || enhet.getEnhetsId().getRoot() == null
                || !enhet.getEnhetsId().getRoot().equals(Constants.HSA_ID_OID)) {
            validationErrors.add("Wrong o.i.d. for enhetsId! Should be " + Constants.HSA_ID_OID);
        }

        // Check enhetsnamn - mandatory
        if (Strings.isNullOrEmpty(enhet.getEnhetsnamn())) {
            validationErrors.add("No enhetsnamn found!");
        }

        validateVardgivare(enhet.getVardgivare());
    }

    private void validateVardgivare(VardgivareType vardgivare) {
        if (vardgivare == null) {
            validationErrors.add("No vardgivare element found!");
            return;
        }

        // Check vardgivare id - mandatory
        if (vardgivare.getVardgivareId() == null || Strings.isNullOrEmpty(vardgivare.getVardgivareId().getExtension())) {
            validationErrors.add("No vardgivare-id found!");
        }
        // Check vardgivare o.i.d.
        if (vardgivare.getVardgivareId() == null || vardgivare.getVardgivareId().getRoot() == null
                || !vardgivare.getVardgivareId().getRoot().equals(Constants.HSA_ID_OID)) {
            validationErrors.add("Wrong o.i.d. for vardgivareId! Should be " + Constants.HSA_ID_OID);
        }

        // Check vardgivarename - mandatory
        if (Strings.isNullOrEmpty(vardgivare.getVardgivarnamn())) {
            validationErrors.add("No vardgivarenamn found!");
        }

    }

}
