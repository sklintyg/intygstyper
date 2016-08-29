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

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.support.services.BefattningService;
import se.inera.intyg.intygstyper.ts_bas.model.internal.Utlatande;
import se.inera.intyg.intygstyper.ts_bas.utils.ScenarioFinder;
import se.inera.intyg.intygstyper.ts_bas.utils.ScenarioNotFoundException;
import se.inera.intygstjanster.ts.services.RegisterTSBasResponder.v1.RegisterTSBasType;
import se.inera.intygstjanster.ts.services.types.v1.II;
import se.inera.intygstjanster.ts.services.v1.*;

/**
 * Test class for TransportToExternal, contains methods for setting up Utlatande using both the transport model and the
 * external model, and populating each with mock data.
 *
 * @author erik
 *
 */
public class TransportToInternalTest {

    private static final String ENHETSNAMN = "enhetsnamn";
    private static final String ENHETSID = "enhetsid";
    private static final String VARDGIVARNAMN = "vardgivarnamn";
    private static final String POSTADRESS = "postadress";
    private static final String POSTNUMMER = "postnummer";
    private static final String POSTORT = "postort";
    private static final String TELEFONNUMMER = "telefonnummer";
    private static final String VARDGIVARID = "vardgivarid";
    private static final List<String> SPECIALIST_KOMPETENS = Arrays.asList("a", "b", "c");
    private static final String FULLSTANDIGT_NAMN = "test testorsson";
    private static final String PERSONID = "personid";

    @BeforeClass
    public static void setup() throws Exception {
        new BefattningService().init();
    }

    @Test
    public void testConvert() throws Exception {
        RegisterTSBasType transportModel = ScenarioFinder.getTransportScenario("valid-minimal").asTransportModel();
        transportModel.getIntyg().getGrundData().setSkapadAv(buildSkapadAv());
        Utlatande res = TransportToInternal.convert(transportModel.getIntyg());
        assertEquals(LocalDateTime.of(2013, 8, 12, 15, 57, 0), res.getGrundData().getSigneringsdatum());
        HoSPersonal hosPersonal = res.getGrundData().getSkapadAv();
        assertEquals(ENHETSNAMN, hosPersonal.getVardenhet().getEnhetsnamn());
        assertEquals(ENHETSID, hosPersonal.getVardenhet().getEnhetsid());
        assertEquals(VARDGIVARNAMN, hosPersonal.getVardenhet().getVardgivare().getVardgivarnamn());
        assertEquals(POSTADRESS, hosPersonal.getVardenhet().getPostadress());
        assertEquals(POSTNUMMER, hosPersonal.getVardenhet().getPostnummer());
        assertEquals(POSTORT, hosPersonal.getVardenhet().getPostort());
        assertEquals(TELEFONNUMMER, hosPersonal.getVardenhet().getTelefonnummer());
        assertEquals(VARDGIVARID, hosPersonal.getVardenhet().getVardgivare().getVardgivarid());
        assertEquals(FULLSTANDIGT_NAMN, hosPersonal.getFullstandigtNamn());
        assertEquals(PERSONID, hosPersonal.getPersonId());
        assertEquals(SPECIALIST_KOMPETENS, hosPersonal.getSpecialiteter());
    }

    @Test
    public void testConvertMapsSpecialistkompetens() throws ScenarioNotFoundException, ConverterException {
        final String specialistkompetens = "Hörselrubbningar";
        RegisterTSBasType transportModel = ScenarioFinder.getTransportScenario("valid-minimal").asTransportModel();
        transportModel.getIntyg().getGrundData().getSkapadAv().getSpecialiteter().clear();
        transportModel.getIntyg().getGrundData().getSkapadAv().getSpecialiteter().add(specialistkompetens);
        Utlatande res = TransportToInternal.convert(transportModel.getIntyg());
        HoSPersonal skapadAv = res.getGrundData().getSkapadAv();
        assertEquals(1, skapadAv.getSpecialiteter().size());
        assertEquals(specialistkompetens, skapadAv.getSpecialiteter().get(0));
    }

    @Test
    public void testConvertMapsBefattningDescriptionToCodeIfPossible() throws ScenarioNotFoundException, ConverterException {
        final String befattning = "Läkare legitimerad, specialiseringstjänstgöring";
        final String code = "203010";
        RegisterTSBasType transportModel = ScenarioFinder.getTransportScenario("valid-minimal").asTransportModel();
        transportModel.getIntyg().getGrundData().getSkapadAv().getBefattningar().clear();
        transportModel.getIntyg().getGrundData().getSkapadAv().getBefattningar().add(befattning);
        Utlatande res = TransportToInternal.convert(transportModel.getIntyg());
        HoSPersonal skapadAv = res.getGrundData().getSkapadAv();
        assertEquals(1, skapadAv.getBefattningar().size());
        assertEquals(code, skapadAv.getBefattningar().get(0));
    }

    @Test
    public void testConvertKeepBefattningCodeIfDescriptionNotFound() throws ScenarioNotFoundException, ConverterException {
        String befattningskod = "kod";
        RegisterTSBasType transportModel = ScenarioFinder.getTransportScenario("valid-minimal").asTransportModel();
        transportModel.getIntyg().getGrundData().getSkapadAv().getBefattningar().clear();
        transportModel.getIntyg().getGrundData().getSkapadAv().getBefattningar().add(befattningskod);
        Utlatande res = TransportToInternal.convert(transportModel.getIntyg());
        HoSPersonal skapadAv = res.getGrundData().getSkapadAv();
        assertEquals(1, skapadAv.getBefattningar().size());
        assertEquals(befattningskod, skapadAv.getBefattningar().get(0));
    }

    private SkapadAv buildSkapadAv() {
        SkapadAv skapadAv = new SkapadAv();
        skapadAv.setPersonId(buildII(PERSONID));
        skapadAv.setFullstandigtNamn(FULLSTANDIGT_NAMN);
        skapadAv.getSpecialiteter().addAll(SPECIALIST_KOMPETENS);

        Vardenhet vardenhet = new Vardenhet();

        Vardgivare vardgivare = new Vardgivare();
        vardgivare.setVardgivarid(buildII(VARDGIVARID));
        vardgivare.setVardgivarnamn(VARDGIVARNAMN);
        vardenhet.setVardgivare(vardgivare);

        vardenhet.setEnhetsId(buildII(ENHETSID));
        vardenhet.setEnhetsnamn(ENHETSNAMN);
        vardenhet.setPostadress(POSTADRESS);
        vardenhet.setPostnummer(POSTNUMMER);
        vardenhet.setPostort(POSTORT);
        vardenhet.setTelefonnummer(TELEFONNUMMER);
        skapadAv.setVardenhet(vardenhet);

        return skapadAv;
    }

    private II buildII(String extension) {
        II ii = new II();
        ii.setExtension(extension);
        return ii;
    }
}
