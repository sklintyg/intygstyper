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

package se.inera.intyg.intygstyper.fk7263.utils;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import se.inera.intyg.intygstyper.fk7263.model.converter.EmptyStringUtil;

public class EmptyStringUtilsTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testEscapeWhitespace() {
        assertEquals(String.format("Input %s did not match expected escaped output %s", "%32%32", "  ")
                , "%32%32", EmptyStringUtil.escapeEmptyString("  "));

        assertEquals(String.format("Input %s did not match expected escaped output %s", "%32%32", "  ")
                , "%32", EmptyStringUtil.escapeEmptyString(" "));

        assertEquals(String.format("Input %s did not match expected escaped output %s", "%9%10", "\t\n")
                , "%09%10", EmptyStringUtil.escapeEmptyString("\t\n"));

    }

    @Test
    public void testUnescapeWhitespace() {
        assertEquals(String.format("Output %s did not match expected unescaped string %s", "  ", "%32%32"),
                "  ", EmptyStringUtil.unescapeEmptyString("%32%32"));

        assertEquals(String.format("Input %s did not match expected escaped output %s", " ", "%32")
                , " ", EmptyStringUtil.unescapeEmptyString("%32"));

        assertEquals(String.format("Input %s did not match expected escaped output %s", "\t\n", "%09%10")
                , "\t\n", EmptyStringUtil.unescapeEmptyString("%09%10"));
    }

    @Test
    public void testEscapeThenUnescapeWhitespace() {
        String original = "\t\n";
        String escaped = EmptyStringUtil.escapeEmptyString(original);
        assertEquals("Escaped and unescaped did not match original string", original, EmptyStringUtil.unescapeEmptyString(escaped));
    }

    @Test
    public void testEscapeNonEmptyString() {
        String string = "Awesome test string";
        assertEquals(string, EmptyStringUtil.escapeEmptyString(string));
    }

    @Test
    public void testUnescapeNonEmptyString() {
        String string = "Awesome test string";
        assertEquals(string, EmptyStringUtil.unescapeEmptyString(string));
    }

}
