/**
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

import java.util.List;

import se.inera.certificate.modules.rli.model.external.Utlatande;

/**
 * @author erik
 * 
 */
public class ExternalValidatorImpl implements ExternalValidator {

    /*
     * (non-Javadoc)
     * 
     * @see se.inera.certificate.modules.rli.validator.ExternalValidator#validate()
     */
    @Override
    public List<String> validate(Utlatande utlatande) {
        ExternalValidatorInstance instance = new ExternalValidatorInstance();
        return instance.validate(utlatande);
    }
}
