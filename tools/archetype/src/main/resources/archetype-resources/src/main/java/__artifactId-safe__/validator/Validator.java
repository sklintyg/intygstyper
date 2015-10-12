#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
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
package ${package}.${artifactId-safe}.validator;

import java.util.List;

import ${package}.support.api.dto.ValidateDraftResponse;
import ${package}.${artifactId-safe}.model.external.Utlatande;
import ${package}.${artifactId-safe}.validator.external.ExternalValidatorInstance;
import ${package}.${artifactId-safe}.validator.internal.InternalValidatorInstance;

public class Validator {

    /**
     * Validates an external Utlatande.
     *
     * @param utlatande
     *            ${package}.${artifactId-safe}.model.external.Utlatande
     * @return List of validation errors, or an empty string if validated correctly
     */
    public List<String> validateExternal(Utlatande utlatande) {
        ExternalValidatorInstance instance = new ExternalValidatorInstance();
        return instance.validate(utlatande);
    }

    /**
     * Validates an external Utlatande.
     *
     * @param utlatande
     *            ${package}.${artifactId-safe}.model.external.Utlatande
     * @return List of validation errors, or an empty string if validated correctly
     */
    public ValidateDraftResponse validateInternal(
            ${package}.${artifactId-safe}.model.internal.Utlatande utlatande) {
        InternalValidatorInstance instance = new InternalValidatorInstance();
        return instance.validate(utlatande);
    }
}
