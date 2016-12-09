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
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import se.inera.intyg.common.services.texts.IntygTextsService;
import se.inera.intyg.common.support.model.common.internal.*;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.support.model.converter.util.WebcertModelFactoryUtil;
import se.inera.intyg.common.support.modules.support.api.dto.*;
import se.inera.intyg.intygstyper.ts_diabetes.model.internal.Utlatande;
import se.inera.intyg.intygstyper.ts_diabetes.support.TsDiabetesEntryPoint;

@RunWith(MockitoJUnitRunner.class)
public class WebcertModelFactoryTest {

    @Mock
    private IntygTextsService intygTexts;

    @InjectMocks
    private WebcertModelFactoryImpl factory;

    @Test
    public void testCreateEditableModel() throws JsonParseException, JsonMappingException, IOException, ConverterException {
        when(intygTexts.getLatestVersion(eq(TsDiabetesEntryPoint.MODULE_ID))).thenReturn("version");

        Utlatande utlatande = factory.createNewWebcertDraft(buildNewDraftData("testID"));

        assertNotNull(utlatande);
        assertEquals(TsDiabetesEntryPoint.MODULE_ID, utlatande.getTyp());
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
        HoSPersonal hosPerson = createHosPersonal();
        WebcertModelFactoryUtil.updateSkapadAv(utlatande, hosPerson, signingDate);

        assertEquals(personId, utlatande.getGrundData().getSkapadAv().getPersonId());
        assertEquals(fullstandigtName, utlatande.getGrundData().getSkapadAv().getFullstandigtNamn());
        assertEquals(forskrivarKod, utlatande.getGrundData().getSkapadAv().getForskrivarKod());
        assertEquals(signingDate, utlatande.getGrundData().getSigneringsdatum());
        assertEquals(Arrays.asList(befattning), utlatande.getGrundData().getSkapadAv().getBefattningar());
        assertEquals(specialiseringar, utlatande.getGrundData().getSkapadAv().getSpecialiteter());

        // assert befattningar and specialiteter are cleared before they are updated
        WebcertModelFactoryUtil.updateSkapadAv(utlatande, hosPerson, signingDate);
        assertEquals(Arrays.asList(befattning), utlatande.getGrundData().getSkapadAv().getBefattningar());
        assertEquals(specialiseringar, utlatande.getGrundData().getSkapadAv().getSpecialiteter());
    }

    @Test
    public void testCreateNewWebcertDraftDoesNotGenerateIncompleteSvarInRivtaV2Format() throws ConverterException {
        // this to follow schema during CertificateStatusUpdateForCareV2
        Utlatande draft = factory.createNewWebcertDraft(buildNewDraftData("INTYG_ID"));
        assertTrue(CollectionUtils.isEmpty(UtlatandeToIntyg.convert(draft).getSvar()));
    }

    @Test(expected = ConverterException.class)
    public void testCreateCopyCertificateIdMissing() throws Exception {
        factory.createCopy(new CreateDraftCopyHolder("", new HoSPersonal()), new Utlatande());
    }

    private CreateNewDraftHolder buildNewDraftData(String intygId) {
        Patient patient = new Patient();
        patient.setFornamn("Johnny");
        patient.setMellannamn("Jobs");
        patient.setEfternamn("Appleseed");
        patient.setFullstandigtNamn("Johnny Jobs Appleseed");
        patient.setPersonId(new Personnummer("19121212-1212"));
        patient.setPostadress("Testvägen 12");
        patient.setPostnummer("13337");
        patient.setPostort("Huddinge");
        return new CreateNewDraftHolder(intygId, createHosPersonal(), patient);
    }

    private HoSPersonal createHosPersonal() {
        HoSPersonal hosPerson = new HoSPersonal();
        hosPerson.setPersonId("personid");
        hosPerson.setFullstandigtNamn("fullständigt namn");
        hosPerson.setVardenhet(createVardenhet());
        hosPerson.setForskrivarKod("förskrivarkod");
        hosPerson.getBefattningar().add("befattning");
        hosPerson.getSpecialiteter().add("specialitet1");
        hosPerson.getSpecialiteter().add("specialitet2");
        return hosPerson;
    }

    private Vardenhet createVardenhet() {
        Vardenhet vardenhet = new Vardenhet();
        vardenhet.setEnhetsid("hsaId");
        vardenhet.setEnhetsnamn("ve1");
        vardenhet.setVardgivare(new Vardgivare());
        vardenhet.getVardgivare().setVardgivarid("vg1");
        vardenhet.getVardgivare().setVardgivarnamn("vg1");
        return vardenhet;
    }
}
