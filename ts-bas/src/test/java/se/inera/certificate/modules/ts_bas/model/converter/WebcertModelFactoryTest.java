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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import se.inera.certificate.modules.ts_bas.rest.dto.CreateNewDraftCertificateHolder;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class WebcertModelFactoryTest {

    private WebcertModelFactory factory;

    @Before
    public void setUp() throws Exception {
        factory = new WebcertModelFactory();
    }

    @Ignore
    @Test
    public void testCreateEditableModel() throws JsonParseException, JsonMappingException, IOException {
        CreateNewDraftCertificateHolder draftCertHolder = new ObjectMapper().readValue(new ClassPathResource(
                "webcert-model-factory-request.json").getFile(), CreateNewDraftCertificateHolder.class);

        se.inera.certificate.modules.ts_bas.model.internal.wc.Utlatande utlatande = null;

        try {
            utlatande = factory.createNewWebcertDraft(draftCertHolder);
        } catch (ConverterException e) {
            e.printStackTrace();
        }

        assertNotNull(utlatande);
        assertNotNull(utlatande.getSkapadAv());

        /** Just verify some stuff from the json to make sure all is well.. */
        assertEquals("testID", utlatande.getUtlatandeid());
        assertEquals("johnny appleseed", utlatande.getPatient().getFullstandigtNamn());
    }
}
