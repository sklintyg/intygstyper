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

package se.inera.intyg.intygstyper.fk7263.pdf;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by marced on 19/08/16.
 */
public class EmployeeOptionalFieldsTest {

    @Test
    public void testExists() throws Exception {
        assertFalse(EmployeeOptionalFields.exists("does-not-exist"));
        assertTrue(EmployeeOptionalFields.exists(EmployeeOptionalFields.AKTIVITETSBEGRANSNING.value()));
    }

    @Test
    public void testIsPresent() throws Exception {
        assertFalse(EmployeeOptionalFields.AKTIVITETSBEGRANSNING.isPresent(Arrays.asList("1", "2")));
        assertTrue(EmployeeOptionalFields.AKTIVITETSBEGRANSNING.isPresent(Arrays.asList("1", EmployeeOptionalFields.AKTIVITETSBEGRANSNING.value())));
    }

    @Test
    public void testFromValue() throws Exception {
        assertEquals(EmployeeOptionalFields.AKTIVITETSBEGRANSNING,
                EmployeeOptionalFields.fromValue(EmployeeOptionalFields.AKTIVITETSBEGRANSNING.value()));
    }

    @Test (expected = IllegalArgumentException.class)
    public void testFromValueFail() throws Exception {
        EmployeeOptionalFields.fromValue("does-not-exist");
    }

    @Test
    public void testContainsAll() throws Exception {

        assertFalse(EmployeeOptionalFields.containsAllValues(new ArrayList<String>()));
        assertFalse(EmployeeOptionalFields.containsAllValues(null));
        assertFalse(EmployeeOptionalFields.containsAllValues(Arrays.asList("1", "2")));
        assertFalse(EmployeeOptionalFields.containsAllValues(Arrays.asList(EmployeeOptionalFields.AKTIVITETSBEGRANSNING.value())));

        final List<String> allValues = Stream.of(EmployeeOptionalFields.values()).map(EmployeeOptionalFields::value).collect(Collectors.toList());
        assertTrue(EmployeeOptionalFields.containsAllValues(allValues));
    }

}
