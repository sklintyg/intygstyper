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

package se.inera.certificate.modules.luae_na.model.utils;

import java.io.File;
import java.io.IOException;

import javax.xml.bind.JAXB;

import com.fasterxml.jackson.databind.ObjectMapper;

import se.inera.certificate.modules.luae_na.model.internal.AktivitetsersattningNAUtlatande;
import se.inera.intyg.common.util.integration.integration.json.CustomObjectMapper;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v2.RegisterCertificateType;

public final class ResourceConverterUtils {

    private ResourceConverterUtils() {
    }

    private static final ObjectMapper OBJECT_MAPPER = new CustomObjectMapper();

    public static RegisterCertificateType toTransport(File resource) throws IOException {
        return JAXB.unmarshal(resource, RegisterCertificateType.class);
    }

    public static AktivitetsersattningNAUtlatande toInternal(File resource)
            throws IOException {
        return OBJECT_MAPPER.readValue(resource, AktivitetsersattningNAUtlatande.class);
    }

    public static AktivitetsersattningNAUtlatande toInternal(String resource)
            throws IOException {
        return OBJECT_MAPPER.readValue(resource, AktivitetsersattningNAUtlatande.class);
    }
}
