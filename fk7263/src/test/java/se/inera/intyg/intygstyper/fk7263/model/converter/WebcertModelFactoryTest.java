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

package se.inera.intyg.intygstyper.fk7263.model.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import se.inera.intyg.common.support.modules.support.api.dto.*;
import se.inera.intyg.common.util.integration.integration.json.CustomObjectMapper;
import se.inera.intyg.intygstyper.fk7263.model.converter.util.ConverterUtil;
import se.inera.intyg.intygstyper.fk7263.model.internal.Utlatande;

public class WebcertModelFactoryTest {

    private static final Logger LOG = LoggerFactory.getLogger(WebcertModelFactoryTest.class);

    private WebcertModelFactory factory;

    @Before
    public void setup() {
        this.factory = new WebcertModelFactory();
    }

    @Test
    public void testCreateCopy() throws Exception {

        Utlatande utlatande = readUtlatandeFromFile("WebcertModelFactoryTest/utlatande-intyg-1.json");

        CreateDraftCopyHolder copyData = createDraftCopyHolder("new-intyg-1", false, false);

        Utlatande copy = factory.createCopy(copyData, utlatande);

        assertNotNull(copy);

        assertEquals("new-intyg-1", utlatande.getId());
        assertEquals("fk7263", utlatande.getTyp());

        assertEquals("TST12345678", copy.getGrundData().getSkapadAv().getPersonId());
        assertNotNull(copy.getGrundData().getSkapadAv().getFullstandigtNamn());
        assertNotNull(copy.getGrundData().getSkapadAv().getForskrivarKod());
        assertNotNull(copy.getGrundData().getSkapadAv().getBefattningar());

        assertEquals("VE1", copy.getGrundData().getSkapadAv().getVardenhet().getEnhetsid());
        assertNotNull(copy.getGrundData().getSkapadAv().getVardenhet().getEnhetsnamn());
        assertNotNull(copy.getGrundData().getSkapadAv().getVardenhet().getVardgivare());

        assertEquals("VG1", copy.getGrundData().getSkapadAv().getVardenhet().getVardgivare().getVardgivarid());
        assertNotNull(copy.getGrundData().getSkapadAv().getVardenhet().getVardgivare().getVardgivarnamn());

        assertNull("Signeringsdatum should be emtpy", copy.getGrundData().getSigneringsdatum());
    }

    @Test
    public void testCreateCopyWithNewPatientData() throws Exception {

        Utlatande utlatande = readUtlatandeFromFile("WebcertModelFactoryTest/utlatande-intyg-1.json");

        CreateDraftCopyHolder copyData = createDraftCopyHolder("new-intyg-2", true, false);

        Utlatande copy = factory.createCopy(copyData, utlatande);

        assertNotNull(copy);

        assertEquals("new-intyg-2", utlatande.getId());
        assertEquals("fk7263", utlatande.getTyp());

        assertEquals("19121212-1212", copy.getGrundData().getPatient().getPersonId().getPersonnummer());
        assertEquals("Test", copy.getGrundData().getPatient().getFornamn());
        assertEquals("Testorsson", copy.getGrundData().getPatient().getEfternamn());
        assertNotNull(copy.getGrundData().getPatient().getPostadress());
        assertNotNull(copy.getGrundData().getPatient().getPostnummer());
    }

    @Test
    public void testCreateCopyWithNewPatientPersonId() throws Exception {

        Utlatande utlatande = readUtlatandeFromFile("WebcertModelFactoryTest/utlatande-intyg-1.json");

        CreateDraftCopyHolder copyData = createDraftCopyHolder("new-intyg-3", false, true);

        assertEquals("19121212-1212", utlatande.getGrundData().getPatient().getPersonId().getPersonnummer());

        Utlatande copy = factory.createCopy(copyData, utlatande);

        assertNotNull(copy);

        assertEquals("new-intyg-3", utlatande.getId());
        assertEquals("fk7263", utlatande.getTyp());

        assertEquals("19141414-1414", copy.getGrundData().getPatient().getPersonId().getPersonnummer());
        assertEquals("Test", copy.getGrundData().getPatient().getFornamn());
        assertEquals("Testorsson", copy.getGrundData().getPatient().getEfternamn());
    }

    private CreateDraftCopyHolder createDraftCopyHolder(String intygsCopyId, boolean addPatient, boolean addNewPersonId) {
        Vardgivare vardgivare = new Vardgivare("VG1", "Vardgivaren");
        Vardenhet vardenhet = new Vardenhet("VE1", "Sjukhuset", "Plåstergatan", null, null, null, null, null, vardgivare);
        HoSPersonal skapadAv = new HoSPersonal("TST12345678", "Dr Dengroth", "1234", "Proktolog", null, vardenhet);
        CreateDraftCopyHolder copyData = new CreateDraftCopyHolder(intygsCopyId, skapadAv);

        if (addPatient) {
            Patient patient = new Patient("Test", "Prov", "Testorsson", new Personnummer("19121212-1212"), "Gågatan", "12345", "Staden");
            copyData.setPatient(patient);
        }

        if (addNewPersonId) {
            copyData.setNewPersonnummer(new Personnummer("19141414-1414"));
        }

        return copyData;
    }

    private static Utlatande readUtlatandeFromFile(String file) throws Exception {

        String utlatandeSrc = readStringFromFile(file);
        ConverterUtil converter = new ConverterUtil();
        converter.setObjectMapper(new CustomObjectMapper());
        return converter.fromJsonString(utlatandeSrc);
    }

    private static String readStringFromFile(String filePath) {
        try {
            LOG.info("Reading test data from: {}", filePath);
            ClassPathResource resource = new ClassPathResource(filePath);
            return IOUtils.toString(resource.getInputStream(), "UTF-8");
        } catch (IOException e) {
            LOG.error("Could not read test data from: {}, error {}", filePath, e.getMessage());
            return null;
        }
    }

}
