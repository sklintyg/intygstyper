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

import org.junit.Test;

import se.inera.intyg.common.support.common.enumerations.BefattningKod;
import se.inera.intyg.common.support.common.enumerations.SpecialistkompetensKod;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.intygstyper.ts_bas.model.internal.Utlatande;
import se.inera.intyg.intygstyper.ts_bas.utils.*;
import se.inera.intygstjanster.ts.services.RegisterTSBasResponder.v1.RegisterTSBasType;
import se.inera.intygstjanster.ts.services.v1.SkapadAv;
import se.inera.intygstjanster.ts.services.v1.TSBasIntyg;

/**
 * Unit test for InternalToExternalConverter.
 *
 * @author erik
 *
 */
public class InternalToTransportTest {

    @Test
    public void testConvertUtlatandeFromInternalToExternal() throws Exception {
        for (Scenario scenario : ScenarioFinder.getInternalScenarios("valid-*")) {
            Utlatande intUtlatande = scenario.asInternalModel();

            TSBasIntyg actual = InternalToTransport.convert(intUtlatande).getIntyg();

            TSBasIntyg expected = scenario.asTransportModel().getIntyg();

            ModelAssert.assertEquals("Error in scenario " + scenario.getName(), expected, actual);
        }
    }

    @Test
    public void testConvertMapsSpecialistkompetensCodeToDescriptionIfPossible() throws ScenarioNotFoundException, ConverterException {
        SpecialistkompetensKod specialistkompetens = SpecialistkompetensKod.ALLERGI;
        Utlatande utlatande = ScenarioFinder.getInternalScenario("valid-minimal").asInternalModel();
        utlatande.getGrundData().getSkapadAv().getSpecialiteter().clear();
        utlatande.getGrundData().getSkapadAv().getSpecialiteter().add(specialistkompetens.getCode());
        RegisterTSBasType res = InternalToTransport.convert(utlatande);
        SkapadAv skapadAv = res.getIntyg().getGrundData().getSkapadAv();
        assertEquals(1, skapadAv.getSpecialiteter().size());
        assertEquals(specialistkompetens.getDescription(), skapadAv.getSpecialiteter().get(0));
    }

    @Test
    public void testConvertKeepSpecialistkompetensCodeIfDescriptionNotFound() throws ScenarioNotFoundException, ConverterException {
        String specialistkompetenskod = "kod";
        Utlatande utlatande = ScenarioFinder.getInternalScenario("valid-minimal").asInternalModel();
        utlatande.getGrundData().getSkapadAv().getSpecialiteter().clear();
        utlatande.getGrundData().getSkapadAv().getSpecialiteter().add(specialistkompetenskod);
        RegisterTSBasType res = InternalToTransport.convert(utlatande);
        SkapadAv skapadAv = res.getIntyg().getGrundData().getSkapadAv();
        assertEquals(1, skapadAv.getSpecialiteter().size());
        assertEquals(specialistkompetenskod, skapadAv.getSpecialiteter().get(0));
    }

    @Test
    public void testConvertMapsBefattningCodeToDescriptionIfPossible() throws ScenarioNotFoundException, ConverterException {
        BefattningKod befattning = BefattningKod.LAKARE_LEG_ST;
        Utlatande utlatande = ScenarioFinder.getInternalScenario("valid-minimal").asInternalModel();
        utlatande.getGrundData().getSkapadAv().getBefattningar().clear();
        utlatande.getGrundData().getSkapadAv().getBefattningar().add(befattning.getCode());
        RegisterTSBasType res = InternalToTransport.convert(utlatande);
        SkapadAv skapadAv = res.getIntyg().getGrundData().getSkapadAv();
        assertEquals(1, skapadAv.getBefattningar().size());
        assertEquals(befattning.getDescription(), skapadAv.getBefattningar().get(0));
    }

    @Test
    public void testConvertKeepBefattningCodeIfDescriptionNotFound() throws ScenarioNotFoundException, ConverterException {
        String befattningskod = "kod";
        Utlatande utlatande = ScenarioFinder.getInternalScenario("valid-minimal").asInternalModel();
        utlatande.getGrundData().getSkapadAv().getBefattningar().clear();
        utlatande.getGrundData().getSkapadAv().getBefattningar().add(befattningskod);
        RegisterTSBasType res = InternalToTransport.convert(utlatande);
        SkapadAv skapadAv = res.getIntyg().getGrundData().getSkapadAv();
        assertEquals(1, skapadAv.getBefattningar().size());
        assertEquals(befattningskod, skapadAv.getBefattningar().get(0));
    }
}
