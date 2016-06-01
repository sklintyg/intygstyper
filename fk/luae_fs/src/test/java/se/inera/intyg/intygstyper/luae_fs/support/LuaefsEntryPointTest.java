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

package se.inera.intyg.intygstyper.luae_fs.support;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.util.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import se.inera.intyg.intygstyper.fkparent.support.FkAbstractModuleEntryPoint;
import se.inera.intyg.common.services.texts.model.IntygTexts;
import se.inera.intyg.common.services.texts.repo.IntygTextsRepository;

/**
 * Created by marced on 10/05/16.
 */
@RunWith(MockitoJUnitRunner.class)
public class LuaefsEntryPointTest {

    @Mock
    IntygTextsRepository repoMock;


    private LuaefsEntryPoint entryPoint;

    @Test
    public void testGetDetailedModuleDescriptionReturnNullIfNoIntygTextsRepositorySet() throws Exception {
        entryPoint = new LuaefsEntryPoint();
        ReflectionTestUtils.setField(entryPoint, "repo", Optional.empty());
        assertNull(entryPoint.getDetailedModuleDescription());
    }

    @Test
    public void testGetDetailedModuleDescriptionReturnStringWhenIntygTextsRepositorySet() throws Exception {
        when(repoMock.getLatestVersion(anyString())).thenReturn("1.0");
        SortedMap<String, String> map = new TreeMap<>();
        map.put(FkAbstractModuleEntryPoint.DETAILED_DESCRIPTION_TEXT_KEY, "hello");

        IntygTexts intygTexts = new IntygTexts("1.0", null, null, null, map, null, null);
        when(repoMock.getTexts(anyString(), anyString())).thenReturn(intygTexts);

        entryPoint = new LuaefsEntryPoint();
        ReflectionTestUtils.setField(entryPoint, "repo", Optional.of(repoMock));

        assertEquals("hello", entryPoint.getDetailedModuleDescription());
    }
}
