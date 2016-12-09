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
package se.inera.intyg.intygstyper.ts_parent.model.converter;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.intygstyper.ts_parent.codes.DiabetesKod;
import se.inera.intygstjanster.ts.services.types.v1.II;
import se.inera.intygstjanster.ts.services.v1.DiabetesTypVarden;
import se.inera.intygstjanster.ts.services.v1.SkapadAv;

public class TransportToInternalUtilTest {

    @Test
    public void testGetTextVersion() {
        assertEquals("6.7", TransportToInternalUtil.getTextVersion("06", "07"));
        assertEquals("6.7", TransportToInternalUtil.getTextVersion("6", "7"));
        assertEquals("1.2", TransportToInternalUtil.getTextVersion("1", "2"));
    }

    @Test
    public void testConvertDiabetesTyp() {
        DiabetesKod res1 = TransportToInternalUtil.convertDiabetesTyp(DiabetesTypVarden.TYP_1);
        DiabetesKod res2 = TransportToInternalUtil.convertDiabetesTyp(DiabetesTypVarden.TYP_2);

        assertEquals(DiabetesKod.DIABETES_TYP_1, res1);
        assertEquals(DiabetesKod.DIABETES_TYP_2, res2);
    }

    @Test
    public void testBuildSkapadAvBefattningskodAndSpecialisering() {
        final String specialisering1 = "spec1";
        final String specialisering2 = "spec2";
        final String befattning1 = "befattning1";
        final String befattning2 = "befattning2";
        se.inera.intygstjanster.ts.services.v1.GrundData grundData = new se.inera.intygstjanster.ts.services.v1.GrundData();
        se.inera.intygstjanster.ts.services.v1.Patient patient = new se.inera.intygstjanster.ts.services.v1.Patient();
        patient.setPersonId(new II());
        patient.getPersonId().setExtension("19121212-1212");
        grundData.setPatient(patient);
        grundData.setSigneringsTidstampel("2016-10-11T12:12:44");
        SkapadAv skapadAv = new SkapadAv();
        skapadAv.setPersonId(new II());
        skapadAv.getSpecialiteter().add(specialisering1);
        skapadAv.getSpecialiteter().add(specialisering2);
        skapadAv.getBefattningar().add(befattning1);
        skapadAv.getBefattningar().add(befattning2);
        se.inera.intygstjanster.ts.services.v1.Vardenhet vardenhet = new se.inera.intygstjanster.ts.services.v1.Vardenhet();
        se.inera.intygstjanster.ts.services.v1.Vardgivare vardgivare = new se.inera.intygstjanster.ts.services.v1.Vardgivare();
        vardgivare.setVardgivarid(new II());
        vardenhet.setVardgivare(vardgivare);
        vardenhet.setEnhetsId(new II());
        skapadAv.setVardenhet(vardenhet);
        grundData.setSkapadAv(skapadAv);

        GrundData res = TransportToInternalUtil.buildGrundData(grundData);

        assertEquals(2, res.getSkapadAv().getSpecialiteter().size());
        assertEquals(specialisering1, res.getSkapadAv().getSpecialiteter().get(0));
        assertEquals(specialisering2, res.getSkapadAv().getSpecialiteter().get(1));
        assertEquals(2, res.getSkapadAv().getBefattningar().size());
        assertEquals(befattning1, res.getSkapadAv().getBefattningar().get(0));
        assertEquals(befattning2, res.getSkapadAv().getBefattningar().get(1));
    }
}
