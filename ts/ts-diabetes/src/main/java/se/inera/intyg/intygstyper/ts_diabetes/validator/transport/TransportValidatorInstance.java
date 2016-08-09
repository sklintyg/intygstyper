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

package se.inera.intyg.intygstyper.ts_diabetes.validator.transport;

import java.util.ArrayList;
import java.util.List;

import se.inera.intyg.common.support.Constants;
import se.inera.intygstjanster.ts.services.v1.TSDiabetesIntyg;

public class TransportValidatorInstance {

    private ValidationContext context;

    private final List<String> validationErrors;

    public TransportValidatorInstance() {
        validationErrors = new ArrayList<String>();
    }

    public List<String> validate(TSDiabetesIntyg utlatande) {
        context = new ValidationContext(utlatande);
        validateIds(utlatande);
        if (context.isHogreContext()) {
            validateHogreBehorighetContext(utlatande);
        }
        return validationErrors;
    }

    private void validateIds(TSDiabetesIntyg utlatande) {
        // PersonId
        if (utlatande.getGrundData().getPatient() != null) {
            String id = utlatande.getGrundData().getPatient().getPersonId().getRoot();
            if (!id.equals(Constants.PERSON_ID_OID) && !id.equals(Constants.SAMORDNING_ID_OID)) {
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

    private ValidationContext getContext() {
        return this.context;
    }

    private void validateHogreBehorighetContext(TSDiabetesIntyg utlatande) {
        if (getContext().isHogreContext()) {
            if (utlatande.getHypoglykemier().isGenomforEgenkontrollBlodsocker() == null) {
                validationErrors
                        .add("'Egenkontroll av blodsocker' must be present when intygAvser contains any of [C1, C1E, C, CE, D1, D1E, D, DE or TAXI]");
            }

            if (utlatande.getHypoglykemier().isHarAllvarligForekomstVakenTid() == null) {
                validationErrors
                        .add("'Allvarlig förekomst av hypoglykemi vaken tid' must be present when intygAvser contains any of [C1, C1E, C, CE, D1, D1E, D, DE or TAXI]");
            }
        }
    }
}
