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

import java.util.List;

import se.inera.intyg.common.support.modules.support.api.dto.ValidateDraftResponse;
import se.inera.certificate.modules.ts_bas.model.internal.Utlatande;
import se.inera.certificate.modules.ts_bas.validator.internal.InternalValidatorInstance;
import se.inera.certificate.modules.ts_bas.validator.transport.TransportValidatorInstance;
import se.inera.intygstjanster.ts.services.v1.TSBasIntyg;

public class TsBasValidator {
    /**
     * Validates an internal Utlatande.
     *
     * @param utlatande
     *            se.inera.certificate.modules.ts_bas.model.internal.Utlatande
     * @return List of validation errors, or an empty string if validated correctly.
     */
    public ValidateDraftResponse validateInternal(Utlatande utlatande) {
        InternalValidatorInstance instance = new InternalValidatorInstance();
        return instance.validate(utlatande);
    }

    /**
     * Performs programmatic validation a TSBasIntyg on the transport format.
     *
     * @param TSBasIntyg
     * @return List of validation errors, or an empty string if validated correctly.
     */
    public List<String> validateTransport(TSBasIntyg intyg) {
        TransportValidatorInstance instance = new TransportValidatorInstance();
        return instance.validate(intyg);
    }
}
