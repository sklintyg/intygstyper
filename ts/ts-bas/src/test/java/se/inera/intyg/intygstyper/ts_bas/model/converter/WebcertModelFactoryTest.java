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
package se.inera.intyg.intygstyper.ts_bas.model.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;
import static org.mockito.Matchers.*;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import se.inera.intyg.common.services.texts.IntygTextsService;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.support.modules.support.api.dto.*;
import se.inera.intyg.intygstyper.ts_bas.support.TsBasEntryPoint;

@RunWith(MockitoJUnitRunner.class)
public class WebcertModelFactoryTest {

    @Mock
    private IntygTextsService intygTexts;

    @InjectMocks
    private WebcertModelFactory factory;

    @Test
    public void testCreateEditableModel() throws IOException {
        when(intygTexts.getLatestVersion(eq(TsBasEntryPoint.MODULE_ID))).thenReturn("version");
        // Programmatically creating a CreateNewDraftHolder
        Patient patient = new Patient("Johnny", "Jobs", "Appleseed", new Personnummer("19121212-1212"), "Testvägen 12", "13337", "Huddinge");
        Vardgivare vardgivare = new Vardgivare("SE0000000000-HAHAHHSAA", "Vårdgivarnamn");
        Vardenhet vardenhet = new Vardenhet("SE0000000000-1337", "Vårdenhet Väst", "Enhetsvägen 12", "54321", "Tumba",
                "08-1337", null, "0123456789", vardgivare);
        HoSPersonal skapadAv = new HoSPersonal("19101010-1010", "Doktor Alban", "forskrivarKod", "befattning", null, vardenhet);
        CreateNewDraftHolder draftCertHolder = new CreateNewDraftHolder("testID", skapadAv, patient);

        se.inera.intyg.intygstyper.ts_bas.model.internal.Utlatande utlatande = null;

        try {
            utlatande = factory.createNewWebcertDraft(draftCertHolder, null);
        } catch (ConverterException e) {
            e.printStackTrace();
        }

        assertNotNull(utlatande);
        assertNotNull(utlatande.getId());
        assertNotNull(utlatande.getTyp());
        assertNotNull(utlatande.getGrundData().getSkapadAv());
        assertNotNull(utlatande.getGrundData().getPatient());

        /** Just verify some stuff from the json to make sure all is well.. */
        assertEquals("testID", utlatande.getId());
        assertEquals("Johnny Jobs Appleseed", utlatande.getGrundData().getPatient().getFullstandigtNamn());
        assertEquals("Testvägen 12", utlatande.getGrundData().getPatient().getPostadress());
        assertEquals("13337", utlatande.getGrundData().getPatient().getPostnummer());
        assertEquals("Huddinge", utlatande.getGrundData().getPatient().getPostort());

        assertEquals("version", utlatande.getTextVersion());
    }
}
