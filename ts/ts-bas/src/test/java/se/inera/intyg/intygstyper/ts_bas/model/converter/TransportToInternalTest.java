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

import org.junit.BeforeClass;
import org.junit.Test;

import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.support.services.BefattningService;
import se.inera.intyg.intygstyper.ts_bas.model.internal.Utlatande;
import se.inera.intyg.intygstyper.ts_bas.utils.ScenarioFinder;
import se.inera.intyg.intygstyper.ts_bas.utils.ScenarioNotFoundException;
import se.inera.intygstjanster.ts.services.RegisterTSBasResponder.v1.RegisterTSBasType;

/**
 * Test class for TransportToExternal, contains methods for setting up Utlatande using both the transport model and the
 * external model, and populating each with mock data.
 *
 * @author erik
 *
 */
public class TransportToInternalTest {

    @BeforeClass
    public static void setup() throws Exception {
        new BefattningService().init();
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
}
