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

package se.inera.intyg.intygstyper.fkparent.support;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;
import java.util.TreeMap;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.google.common.collect.ImmutableMap;

import se.inera.intyg.common.services.texts.model.IntygTexts;
import se.inera.intyg.common.services.texts.repo.IntygTextsRepositoryImpl;

@RunWith(MockitoJUnitRunner.class)
public class FkAbstractModuleEntryPointTest {

    private static final String MODULE_ID = "moduleId";

    @Mock
    private IntygTextsRepositoryImpl repo;

    @InjectMocks
    private FkAbstractModuleEntryPoint entryPoint = mock(FkAbstractModuleEntryPoint.class);

    @Before
    public void setup() {
        ReflectionTestUtils.setField(entryPoint, "repo", Optional.of(repo));
        when(entryPoint.getDetailedModuleDescription()).thenCallRealMethod();
        when(entryPoint.getExternalId()).thenCallRealMethod();
        when(entryPoint.getModuleId()).thenReturn(MODULE_ID);
    }

    @Test
    public void testGetDetailedModuleDescription() {
        final String version = "1.0";
        final String detailedText = "detailed text";
        when(repo.getLatestVersion(MODULE_ID)).thenReturn(version);
        IntygTexts texts = new IntygTexts(version, MODULE_ID, LocalDate.now().minusDays(1), null,
                new TreeMap<>(ImmutableMap.of(FkAbstractModuleEntryPoint.DETAILED_DESCRIPTION_TEXT_KEY, detailedText)), null, null);
        when(repo.getTexts(MODULE_ID, version)).thenReturn(texts);

        String res = entryPoint.getDetailedModuleDescription();

        assertEquals(detailedText, res);
        verify(repo).getLatestVersion(MODULE_ID);
        verify(repo).getTexts(MODULE_ID, version);
    }

    @Test
    public void testGetDetailedModuleDescriptionNoRepo() {
        ReflectionTestUtils.setField(entryPoint, "repo", Optional.empty());

        String res = entryPoint.getDetailedModuleDescription();

        assertNull(res);
    }

    @Test
    public void testGetExternalId() {
        String res = entryPoint.getExternalId();

        assertEquals("MODULEID", res); // upper case
    }

}
