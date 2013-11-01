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
package se.inera.certificate.modules.rli.model.converters;

import se.inera.certificate.modules.rli.model.external.Utlatande;
/**
 * 
 * @author erik
 *
 */
public interface TransportToExternalConverter {
    /**
     * Converts from the transport format (se.inera.certificate.common.v1.Utlatande) to the external format
     * (se.inera.certificate.modules.rli.model.external.Utlatande).
     * 
     * @param source
     *            Utlatande in the transport format to be converted to external format
     * @return se.inera.certificate.modules.rli.model.external.Utlatande
     * @throws ConverterException 
     */
    Utlatande transportToExternal(se.inera.certificate.common.v1.Utlatande source) throws ConverterException;
}
