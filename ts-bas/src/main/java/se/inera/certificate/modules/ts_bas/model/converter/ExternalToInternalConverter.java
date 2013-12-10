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

import se.inera.certificate.modules.ts_bas.model.internal.mi.Utlatande;
import se.inera.certificate.modules.ts_bas.rest.dto.CertificateContentHolder;

/**
 * Converter for converting the external format to the internal view format.
 * 
 * 
 * @author Niklas Pettersson, R2M
 * 
 */
public class ExternalToInternalConverter {

    private static final Logger LOG = LoggerFactory.getLogger(ExternalToInternalConverter.class);

    public Utlatande convert(CertificateContentHolder certificateContentHolder)
            throws ConverterException {
        // TODO: Implement
        LOG.trace("Converting external model to internal");
        return null;
    }
}
