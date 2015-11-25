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
package se.inera.certificate.modules.rli.model.converter;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.junit.Before;
import org.junit.Test;
import se.inera.intyg.common.support.modules.support.api.dto.CreateNewDraftHolder;
import se.inera.intyg.common.support.modules.support.api.dto.HoSPersonal;
import se.inera.intyg.common.support.modules.support.api.dto.Patient;
import se.inera.intyg.common.support.modules.support.api.dto.Vardenhet;
import se.inera.intyg.common.support.modules.support.api.dto.Vardgivare;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class WebcertModelFactoryTest {

    private WebcertModelFactory factory;

    @Before
    public void setUp() throws Exception {
        factory = new WebcertModelFactory();
    }

    @Test
    public void testCreateEditableModel() throws JsonParseException, JsonMappingException, IOException {
        // Programmatically creating a CreateNewDraftHolder
        Patient patient = new Patient("Johnny", "Jobs", "Appleseed", "19121212-1212", "Testvägen 12", "13337", "Huddinge");
        Vardgivare vardgivare = new Vardgivare("SE0000000000-HAHAHHSAA", "Vårdgivarnamn");
        Vardenhet vardenhet = new Vardenhet("SE0000000000-1337", "Vårdenhet Väst", "Enhetsvägen 12", "54321", "Tumba",
                "08-1337", null, "0123456789", vardgivare);
        HoSPersonal skapadAv = new HoSPersonal("19101010-1010", "Doktor Alban", null, null, null, vardenhet);
        CreateNewDraftHolder draftCertHolder = new CreateNewDraftHolder("testID", skapadAv, patient);

        se.inera.certificate.modules.rli.model.internal.wc.Utlatande utlatande = null;

        try {
            utlatande = factory.createNewWebcertDraft(draftCertHolder);
        } catch (ConverterException e) {
            e.printStackTrace();
        }

        assertNotNull(utlatande);
        assertNotNull(utlatande.getSkapadAv());
        assertNotNull(utlatande.getPatient());

        /** Just verify some stuff from the json to make sure all is well.. */
        assertEquals("testID", utlatande.getUtlatandeid());
        assertEquals("Johnny Jobs Appleseed", utlatande.getPatient().getFullstandigtNamn());
        assertEquals("Testvägen 12", utlatande.getPatient().getPostadress());
        assertEquals("13337", utlatande.getPatient().getPostnummer());
        assertEquals("Huddinge", utlatande.getPatient().getPostort());

    }
}
