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

import java.io.File;

import javax.xml.bind.JAXB;

import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import com.fasterxml.jackson.databind.ObjectMapper;

import se.inera.certificate.integration.json.CustomObjectMapper;
import se.inera.certificate.modules.ts_bas.model.internal.Utlatande;
import se.inera.certificate.modules.ts_bas.utils.ModelAssert;
import se.intygstjanster.ts.services.v1.TSBasIntyg;

/**
 * Test class for TransportToExternal, contains methods for setting up Utlatande using both the transport model and the
 * external model, and populating each with mock data.
 *
 * @author erik
 *
 */

public class TransportToInternalTest {

    ObjectMapper objectMapper = new CustomObjectMapper();

    @Test
    public void testTransportToInternal() throws Exception {
        File resource = new ClassPathResource("InternalToTransport/ts-bas-max.xml").getFile();
        TSBasIntyg transportFormat = JAXB.unmarshal(resource, TSBasIntyg.class);

        Utlatande actual = TransportToInternal.convert(transportFormat);
        Utlatande expected = objectMapper.readValue(new ClassPathResource("InternalToTransport/ts-bas-max.json").getInputStream(), Utlatande.class);

        ModelAssert.assertEquals("Error", expected, actual);
    }
}
