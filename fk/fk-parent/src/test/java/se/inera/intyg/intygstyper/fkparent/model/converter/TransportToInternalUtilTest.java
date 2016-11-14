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
package se.inera.intyg.intygstyper.fkparent.model.converter;

import static org.junit.Assert.assertEquals;
import static se.inera.intyg.common.support.modules.converter.InternalConverterUtil.aCV;
import static se.inera.intyg.common.support.modules.converter.InternalConverterUtil.aSvar;
import static se.inera.intyg.intygstyper.fkparent.model.converter.RespConstants.DIAGNOS_SVAR_ID_6;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import se.inera.intyg.intygstyper.fkparent.model.internal.Diagnos;
import se.riv.clinicalprocess.healthcond.certificate.v2.Svar;

public class TransportToInternalUtilTest {

    @Test
    public void handleDiagnosSvarTest() throws Exception {
        final String diagnosKod1 = "S666";
        final String diagnosKodSystem1 = "1.2.752.116.1.1.1.1.3";
        final String diagnosBeskrivning1 = "Skada på multipla böjmuskler och deras senor på handleds- och handnivå";
        final String diagnosDisplayName1 = "displayName1";
        final String diagnosKod2 = "Z731";
        final String diagnosKodSystem2 = "1.2.752.116.1.1.1.1.3";
        final String diagnosBeskrivning2 = "Accentuering av personlighetsdrag";
        final String diagnosDisplayName2 = "displayName2";
        final String diagnosKod3 = "A039";
        final String diagnosKodSystem3 = "1.2.752.116.1.1.1.1.3";
        final String diagnosBeskrivning3 = "Shigellos, ospecificerad";
        final String diagnosDisplayName3 = "displayName3";

        Svar svar = aSvar(DIAGNOS_SVAR_ID_6).withDelsvar("6.1", diagnosBeskrivning1)
                .withDelsvar("6.2", aCV(diagnosKodSystem1, diagnosKod1, diagnosDisplayName1))
                .withDelsvar("6.3", diagnosBeskrivning2)
                .withDelsvar("6.4", aCV(diagnosKodSystem2, diagnosKod2, diagnosDisplayName2))
                .withDelsvar("6.5", diagnosBeskrivning3)
                .withDelsvar("6.6", aCV(diagnosKodSystem3, diagnosKod3, diagnosDisplayName3)).build();
        List<Diagnos> res = new ArrayList<>();

        TransportToInternalUtil.handleDiagnos(res, svar);

        assertEquals(3, res.size());
        assertEquals(diagnosKod1, res.get(0).getDiagnosKod());
        assertEquals("ICD_10_SE", res.get(0).getDiagnosKodSystem());
        assertEquals(diagnosBeskrivning1, res.get(0).getDiagnosBeskrivning());
        assertEquals(diagnosDisplayName1, res.get(0).getDiagnosDisplayName());
        assertEquals(diagnosKod2, res.get(1).getDiagnosKod());
        assertEquals("ICD_10_SE", res.get(1).getDiagnosKodSystem());
        assertEquals(diagnosBeskrivning2, res.get(1).getDiagnosBeskrivning());
        assertEquals(diagnosDisplayName2, res.get(1).getDiagnosDisplayName());
        assertEquals(diagnosKod3, res.get(2).getDiagnosKod());
        assertEquals("ICD_10_SE", res.get(2).getDiagnosKodSystem());
        assertEquals(diagnosBeskrivning3, res.get(2).getDiagnosBeskrivning());
        assertEquals(diagnosDisplayName3, res.get(2).getDiagnosDisplayName());
    }

    @Test
    public void handleDiagnosSvarTwoDiagnoses() throws Exception {
        final String diagnosKod1 = "S666";
        final String diagnosKodSystem1 = "1.2.752.116.1.1.1.1.3";
        final String diagnosBeskrivning1 = "Skada på multipla böjmuskler och deras senor på handleds- och handnivå";
        final String diagnosDisplayName1 = "displayName1";
        final String diagnosKod2 = "Z731";
        final String diagnosKodSystem2 = "1.2.752.116.1.1.1.1.3";
        final String diagnosBeskrivning2 = "Accentuering av personlighetsdrag";
        final String diagnosDisplayName2 = "displayName2";

        Svar svar = aSvar(DIAGNOS_SVAR_ID_6).withDelsvar("6.1", diagnosBeskrivning1)
                .withDelsvar("6.2", aCV(diagnosKodSystem1, diagnosKod1, diagnosDisplayName1))
                .withDelsvar("6.3", diagnosBeskrivning2)
                .withDelsvar("6.4", aCV(diagnosKodSystem2, diagnosKod2, diagnosDisplayName2)).build();
        List<Diagnos> res = new ArrayList<>();

        TransportToInternalUtil.handleDiagnos(res, svar);

        assertEquals(2, res.size());
        assertEquals(diagnosKod1, res.get(0).getDiagnosKod());
        assertEquals("ICD_10_SE", res.get(0).getDiagnosKodSystem());
        assertEquals(diagnosBeskrivning1, res.get(0).getDiagnosBeskrivning());
        assertEquals(diagnosDisplayName1, res.get(0).getDiagnosDisplayName());
        assertEquals(diagnosKod2, res.get(1).getDiagnosKod());
        assertEquals("ICD_10_SE", res.get(1).getDiagnosKodSystem());
        assertEquals(diagnosBeskrivning2, res.get(1).getDiagnosBeskrivning());
        assertEquals(diagnosDisplayName2, res.get(1).getDiagnosDisplayName());
    }

    @Test
    public void handleDiagnosSvarOneDiagnosis() throws Exception {
        final String diagnosKod1 = "S666";
        final String diagnosKodSystem1 = "1.2.752.116.1.1.1.1.3";
        final String diagnosBeskrivning1 = "Skada på multipla böjmuskler och deras senor på handleds- och handnivå";
        final String diagnosDisplayName1 = "displayName1";

        Svar svar = aSvar(DIAGNOS_SVAR_ID_6).withDelsvar("6.1", diagnosBeskrivning1)
                .withDelsvar("6.2", aCV(diagnosKodSystem1, diagnosKod1, diagnosDisplayName1)).build();
        List<Diagnos> res = new ArrayList<>();

        TransportToInternalUtil.handleDiagnos(res, svar);

        assertEquals(1, res.size());
        assertEquals(diagnosKod1, res.get(0).getDiagnosKod());
        assertEquals("ICD_10_SE", res.get(0).getDiagnosKodSystem());
        assertEquals(diagnosBeskrivning1, res.get(0).getDiagnosBeskrivning());
        assertEquals(diagnosDisplayName1, res.get(0).getDiagnosDisplayName());
    }

    @Test
    public void handleDiagnosSvarKSH97P() throws Exception {
        final String diagnosKod1 = "A00-";
        final String diagnosKodSystem1 = "1.2.752.116.1.3.1.4.1";
        final String diagnosBeskrivning1 = "Kolera";
        final String diagnosDisplayName1 = "displayName1";

        Svar svar = aSvar(DIAGNOS_SVAR_ID_6).withDelsvar("6.1", diagnosBeskrivning1)
                .withDelsvar("6.2", aCV(diagnosKodSystem1, diagnosKod1, diagnosDisplayName1)).build();
        List<Diagnos> res = new ArrayList<>();

        TransportToInternalUtil.handleDiagnos(res, svar);

        assertEquals(1, res.size());
        assertEquals(diagnosKod1, res.get(0).getDiagnosKod());
        assertEquals("KSH_97_P", res.get(0).getDiagnosKodSystem());
        assertEquals(diagnosBeskrivning1, res.get(0).getDiagnosBeskrivning());
        assertEquals(diagnosDisplayName1, res.get(0).getDiagnosDisplayName());
    }

}
