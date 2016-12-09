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

package se.inera.intyg.intygstyper.fkparent.model.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.ImmutableList;

import se.inera.intyg.common.support.model.InternalDate;
import se.inera.intyg.common.support.modules.service.WebcertModuleService;
import se.inera.intyg.intygstyper.fkparent.model.internal.Diagnos;
import se.inera.intyg.intygstyper.fkparent.model.internal.SitUtlatande;

@RunWith(MockitoJUnitRunner.class)
public class FkParentModelCompareUtilTest {

    private static final String VALID_CODE_1 = "A00";
    private static final String VALID_CODE_1_CODE_SYSTEM = "ICD";
    private static final String VALID_CODE_2 = "A01";
    private static final String VALID_CODE_2_CODE_SYSTEM = "KSH";
    private static final String INVALID_CODE = "sdfdsf";

    @Mock
    private WebcertModuleService moduleService;

    @InjectMocks
    private FkParentModelCompareUtil compareUtil = mock(FkParentModelCompareUtil.class, Mockito.CALLS_REAL_METHODS);

    @Before
    public void setup() {
        when(moduleService.validateDiagnosisCode(VALID_CODE_1, VALID_CODE_1_CODE_SYSTEM)).thenReturn(true);
        when(moduleService.validateDiagnosisCode(VALID_CODE_2, VALID_CODE_2_CODE_SYSTEM)).thenReturn(true);
        when(moduleService.validateDiagnosisCode(eq(INVALID_CODE), anyString())).thenReturn(false);
    }

    @Test
    public void testDiagnosesAreValid() {
        SitUtlatande source = mock(SitUtlatande.class);
        when(source.getDiagnoser()).thenReturn(
                ImmutableList.of(buildDiagnos(VALID_CODE_1, VALID_CODE_1_CODE_SYSTEM), buildDiagnos(VALID_CODE_2, VALID_CODE_2_CODE_SYSTEM)));
        boolean res = compareUtil.diagnosesAreValid(source);

        assertTrue(res);
        verify(moduleService).validateDiagnosisCode(VALID_CODE_1, VALID_CODE_1_CODE_SYSTEM);
        verify(moduleService).validateDiagnosisCode(VALID_CODE_2, VALID_CODE_2_CODE_SYSTEM);
    }

    @Test
    public void testDiagnosesAreValidFalse() {
        SitUtlatande source = mock(SitUtlatande.class);
        when(source.getDiagnoser()).thenReturn(
                ImmutableList.of(buildDiagnos(VALID_CODE_1, VALID_CODE_1_CODE_SYSTEM), buildDiagnos(INVALID_CODE, VALID_CODE_2_CODE_SYSTEM)));
        boolean res = compareUtil.diagnosesAreValid(source);

        assertFalse(res);
        verify(moduleService).validateDiagnosisCode(VALID_CODE_1, VALID_CODE_1_CODE_SYSTEM);
        verify(moduleService).validateDiagnosisCode(INVALID_CODE, VALID_CODE_2_CODE_SYSTEM);
    }

    @Test
    public void testDiagnosesAreValidIgnoresEmptyCodes() {
        SitUtlatande source = mock(SitUtlatande.class);
        when(source.getDiagnoser()).thenReturn(
                ImmutableList.of(buildDiagnos(null, VALID_CODE_1_CODE_SYSTEM)));
        boolean res = compareUtil.diagnosesAreValid(source);

        assertTrue(res);
        verifyZeroInteractions(moduleService);
    }

    @Test
    public void testDatesAreValid() {
        boolean res = compareUtil.datesAreValid(new InternalDate(LocalDate.now()), new InternalDate(LocalDate.now().minusDays(3)));

        assertTrue(res);
    }

    @Test
    public void testDatesAreValidFalse() {
        boolean res = compareUtil.datesAreValid(new InternalDate("notADate"), new InternalDate(LocalDate.now().minusDays(3)));

        assertFalse(res);
    }

    @Test
    public void testDatesAreValidIgnoresNullDates() {
        boolean res = compareUtil.datesAreValid(null, null);

        assertTrue(res);
    }

    @Test
    public void testDatesAreValidNoDates() {
        boolean res = compareUtil.datesAreValid();

        assertTrue(res);
    }

    private Diagnos buildDiagnos(String code, String codeSystem) {
        return Diagnos.create(code, codeSystem, null, null);
    }
}
