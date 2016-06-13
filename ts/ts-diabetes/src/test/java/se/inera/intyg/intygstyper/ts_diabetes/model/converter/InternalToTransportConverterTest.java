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
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import se.inera.intyg.common.support.common.enumerations.BefattningKod;
import se.inera.intyg.common.support.common.enumerations.SpecialistkompetensKod;
import se.inera.intyg.common.support.model.common.internal.*;
import se.inera.intyg.intygstyper.ts_diabetes.model.internal.Utlatande;
import se.inera.intyg.intygstyper.ts_diabetes.utils.ScenarioFinder;
import se.inera.intyg.intygstyper.ts_diabetes.utils.ScenarioNotFoundException;
import se.inera.intygstjanster.ts.services.RegisterTSDiabetesResponder.v1.RegisterTSDiabetesType;
import se.inera.intygstjanster.ts.services.v1.SkapadAv;

public class InternalToTransportConverterTest {

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

    @Test
    public void testConvert() throws ScenarioNotFoundException {
        Utlatande utlatande = ScenarioFinder.getInternalScenario("valid-minimal").asInternalModel();
        utlatande.getGrundData().setSkapadAv(buildHosPersonal(SPECIALIST_KOMPETENS));

        RegisterTSDiabetesType res = InternalToTransportConverter.convert(utlatande);
        SkapadAv skapadAv = res.getIntyg().getGrundData().getSkapadAv();
        assertEquals(ENHETSNAMN, skapadAv.getVardenhet().getEnhetsnamn());
        assertEquals(ENHETSID, skapadAv.getVardenhet().getEnhetsId().getExtension());
        assertEquals(VARDGIVARNAMN, skapadAv.getVardenhet().getVardgivare().getVardgivarnamn());
        assertEquals(POSTADRESS, skapadAv.getVardenhet().getPostadress());
        assertEquals(POSTNUMMER, skapadAv.getVardenhet().getPostnummer());
        assertEquals(POSTORT, skapadAv.getVardenhet().getPostort());
        assertEquals(TELEFONNUMMER, skapadAv.getVardenhet().getTelefonnummer());
        assertEquals(VARDGIVARID, skapadAv.getVardenhet().getVardgivare().getVardgivarid().getExtension());
        assertEquals(FULLSTANDIGT_NAMN, skapadAv.getFullstandigtNamn());
        assertEquals(PERSONID, skapadAv.getPersonId().getExtension());

        assertEquals(SPECIALIST_KOMPETENS, skapadAv.getSpecialiteter());
    }

    @Test
    public void testConvertNullSpecialistkompetens() throws ScenarioNotFoundException {
        Utlatande utlatande = ScenarioFinder.getInternalScenario("valid-minimal").asInternalModel();
        utlatande.getGrundData().setSkapadAv(buildHosPersonal(null));
        RegisterTSDiabetesType res = InternalToTransportConverter.convert(utlatande);
        SkapadAv skapadAv = res.getIntyg().getGrundData().getSkapadAv();
        assertTrue(skapadAv.getSpecialiteter().isEmpty());
    }

    @Test
    public void testConvertMapsSpecialistkompetensCodeToDescriptionIfPossible() throws ScenarioNotFoundException {
        SpecialistkompetensKod specialistkompetens = SpecialistkompetensKod.ALLERGI;
        Utlatande utlatande = ScenarioFinder.getInternalScenario("valid-minimal").asInternalModel();
        utlatande.getGrundData().getSkapadAv().getSpecialiteter().clear();
        utlatande.getGrundData().getSkapadAv().getSpecialiteter().add(specialistkompetens.getCode());
        RegisterTSDiabetesType res = InternalToTransportConverter.convert(utlatande);
        SkapadAv skapadAv = res.getIntyg().getGrundData().getSkapadAv();
        assertEquals(1, skapadAv.getSpecialiteter().size());
        assertEquals(specialistkompetens.getDescription(), skapadAv.getSpecialiteter().get(0));
    }

    @Test
    public void testConvertKeepSpecialistkompetensCodeIfDescriptionNotFound() throws ScenarioNotFoundException {
        String specialistkompetenskod = "kod";
        Utlatande utlatande = ScenarioFinder.getInternalScenario("valid-minimal").asInternalModel();
        utlatande.getGrundData().getSkapadAv().getSpecialiteter().clear();
        utlatande.getGrundData().getSkapadAv().getSpecialiteter().add(specialistkompetenskod);
        RegisterTSDiabetesType res = InternalToTransportConverter.convert(utlatande);
        SkapadAv skapadAv = res.getIntyg().getGrundData().getSkapadAv();
        assertEquals(1, skapadAv.getSpecialiteter().size());
        assertEquals(specialistkompetenskod, skapadAv.getSpecialiteter().get(0));
    }

    @Test
    public void testConvertMapsBefattningCodeToDescriptionIfPossible() throws ScenarioNotFoundException {
        BefattningKod befattning = BefattningKod.LAKARE_LEG_ST;
        Utlatande utlatande = ScenarioFinder.getInternalScenario("valid-minimal").asInternalModel();
        utlatande.getGrundData().getSkapadAv().getBefattningar().clear();
        utlatande.getGrundData().getSkapadAv().getBefattningar().add(befattning.getCode());
        RegisterTSDiabetesType res = InternalToTransportConverter.convert(utlatande);
        SkapadAv skapadAv = res.getIntyg().getGrundData().getSkapadAv();
        assertEquals(1, skapadAv.getBefattningar().size());
        assertEquals(befattning.getDescription(), skapadAv.getBefattningar().get(0));
    }

    @Test
    public void testConvertKeepBefattningCodeIfDescriptionNotFound() throws ScenarioNotFoundException {
        String befattningskod = "kod";
        Utlatande utlatande = ScenarioFinder.getInternalScenario("valid-minimal").asInternalModel();
        utlatande.getGrundData().getSkapadAv().getBefattningar().clear();
        utlatande.getGrundData().getSkapadAv().getBefattningar().add(befattningskod);
        RegisterTSDiabetesType res = InternalToTransportConverter.convert(utlatande);
        SkapadAv skapadAv = res.getIntyg().getGrundData().getSkapadAv();
        assertEquals(1, skapadAv.getBefattningar().size());
        assertEquals(befattningskod, skapadAv.getBefattningar().get(0));
    }

    private HoSPersonal buildHosPersonal(List<String> specialistKompetens) {
        HoSPersonal hosPersonal = new HoSPersonal();
        hosPersonal.setPersonId(PERSONID);
        hosPersonal.setFullstandigtNamn(FULLSTANDIGT_NAMN);
        if (specialistKompetens != null) {
            hosPersonal.getSpecialiteter().addAll(specialistKompetens);
        }

        Vardenhet vardenhet = new Vardenhet();

        Vardgivare vardgivare = new Vardgivare();
        vardgivare.setVardgivarid(VARDGIVARID);
        vardgivare.setVardgivarnamn(VARDGIVARNAMN);
        vardenhet.setVardgivare(vardgivare);

        vardenhet.setEnhetsid(ENHETSID);
        vardenhet.setEnhetsnamn(ENHETSNAMN);
        vardenhet.setPostadress(POSTADRESS);
        vardenhet.setPostnummer(POSTNUMMER);
        vardenhet.setPostort(POSTORT);
        vardenhet.setTelefonnummer(TELEFONNUMMER);
        hosPersonal.setVardenhet(vardenhet);

        return hosPersonal;
    }
}
