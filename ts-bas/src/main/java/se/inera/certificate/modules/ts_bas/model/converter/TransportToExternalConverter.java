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
package se.inera.certificate.modules.ts_bas.model.converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.inera.certificate.modules.ts_bas.model.external.Utlatande;

/**
 * Converter between transport and external model.
 */
public class TransportToExternalConverter {

    private static final Logger LOG = LoggerFactory.getLogger(TransportToExternalConverter.class);

    /**
     * Converts from the transport format (se.inera.certificate.common.v1.Utlatande) to the external format
     * (se.inera.certificate.modules.ts_bas.model.external.Utlatande).
     * 
     * @param source
     *            Utlatande in the transport format to be converted to external format
     * @return se.inera.certificate.modules.ts_bas.model.external.Utlatande
     * @throws ConverterException
     */
    public Utlatande convert(se.inera.certificate.ts_bas.model.v1.Utlatande source) throws ConverterException {
        // TODO: Implement
        LOG.trace("Converting transport model to external");
        return null;
    }
}
