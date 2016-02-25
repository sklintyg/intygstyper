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
package se.inera.intyg.intygstyper.ts_diabetes.model.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.joda.time.LocalDateTime;
import org.junit.Before;
import org.junit.Test;

import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.support.modules.support.api.dto.CreateNewDraftHolder;
import se.inera.intyg.common.support.modules.support.api.dto.HoSPersonal;
import se.inera.intyg.common.support.modules.support.api.dto.Patient;
import se.inera.intyg.common.support.modules.support.api.dto.Personnummer;
import se.inera.intyg.common.support.modules.support.api.dto.Vardenhet;
import se.inera.intyg.common.support.modules.support.api.dto.Vardgivare;
import se.inera.intyg.intygstyper.ts_diabetes.model.internal.Utlatande;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class WebcertModelFactoryTest {

    private WebcertModelFactory factory;

    @Before
    public void setUp() throws Exception {
        factory = new WebcertModelFactory();
    }

    @Test
    public void testCreateEditableModel() throws JsonParseException, JsonMappingException, IOException {
        // Programmatically creating a CreateNewDraftHolder
        Patient patient = new Patient("Johnny", "Jobs", "Appleseed", new Personnummer("19121212-1212"), "Testvägen 12", "13337", "Huddinge");
        Vardgivare vardgivare = new Vardgivare("SE0000000000-HAHAHHSAA", "Vårdgivarnamn");
        Vardenhet vardenhet = new Vardenhet("SE0000000000-1337", "Vårdenhet Väst", "Enhetsvägen 12", "54321", "Tumba",
                "08-1337", null, "0123456789", vardgivare);
        HoSPersonal skapadAv = new HoSPersonal("19101010-1010", "Doktor Alban", null, null, null, vardenhet);

        CreateNewDraftHolder draftCertHolder = new CreateNewDraftHolder("testID", skapadAv, patient);

        se.inera.intyg.intygstyper.ts_diabetes.model.internal.Utlatande utlatande = null;

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
    }

    @Test
    public void testUpdateSkapadAv() {
        final String personId = "personid";
        final String fullstandigtName = "fullständigt namn";
        final String forskrivarKod = "förskrivarkod";
        final String befattning = "befattning";
        final List<String> specialiseringar = Arrays.asList("specialitet1", "specialitet2");
        final LocalDateTime signingDate = LocalDateTime.now();

        Utlatande utlatande = new Utlatande();
        utlatande.getGrundData().setSkapadAv(new se.inera.intyg.common.support.model.common.internal.HoSPersonal());
        HoSPersonal hosPerson = new HoSPersonal(personId, fullstandigtName, forskrivarKod, befattning, specialiseringar, new Vardenhet("hsaId",
                "name", "postadress", "postnummer", "postort", "telefonnummer", "epost", "arbetsplatskod", new Vardgivare("hsaId", "namn")));
        factory.updateSkapadAv(utlatande, hosPerson, signingDate);

        assertEquals(personId, utlatande.getGrundData().getSkapadAv().getPersonId());
        assertEquals(fullstandigtName, utlatande.getGrundData().getSkapadAv().getFullstandigtNamn());
        assertEquals(forskrivarKod, utlatande.getGrundData().getSkapadAv().getForskrivarKod());
        assertEquals(signingDate, utlatande.getGrundData().getSigneringsdatum());
        assertEquals(Arrays.asList(befattning), utlatande.getGrundData().getSkapadAv().getBefattningar());
        assertEquals(specialiseringar, utlatande.getGrundData().getSkapadAv().getSpecialiteter());

        // assert befattningar and specialiteter are cleared before they are updated
        factory.updateSkapadAv(utlatande, hosPerson, signingDate);
        assertEquals(Arrays.asList(befattning), utlatande.getGrundData().getSkapadAv().getBefattningar());
        assertEquals(specialiseringar, utlatande.getGrundData().getSkapadAv().getSpecialiteter());
    }
}
