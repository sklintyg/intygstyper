/**
 * Copyright (C) 2013 Inera AB (http://www.inera.se)
 *
 * This file is part of Inera Certificate Modules (http://code.google.com/p/inera-certificate-modules).
 *
 * Inera Certificate Modules is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Inera Certificate Modules is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.inera.certificate.model.common.codes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import se.inera.certificate.model.Kod;

public class CodeConverterTest {

    @Test
    public void testCodeConverter() {

        Kod res = CodeConverter.toKod(TestKod.CODE_RED);

        assertNotNull(res);
        assertEquals("CODE_RED", res.getCode());
        assertEquals("CC", res.getCodeSystem());
        assertEquals("ColorCodes", res.getCodeSystemName());
        assertEquals("1.0", res.getCodeSystemVersion());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCodeConverterIllegalInput() {

        CodeConverter.toKod(TestKod.CODE_BLACK);

    }

    /**
     * Checks that a {@link CodeSystem} and a {@link Kod} can be compared without null pointer exceptions.
     * 
     * @throws Exception
     */
    @Test
    public void testMatches() throws Exception {
        assertTrue(CodeConverter.matches(TestKod.CODE_RED, new Kod(TestKod.CODE_SYSTEM, TestKod.CODE_SYSTEM_NAME,
                TestKod.CODE_SYSTEM_VERSION, "CODE_RED")));
        assertFalse(CodeConverter.matches(TestKod.CODE_RED, new Kod(TestKod.CODE_SYSTEM, TestKod.CODE_SYSTEM_NAME,
                null, "CODE_RED")));
        assertFalse(CodeConverter.matches(TestKod.CODE_RED, new Kod(TestKod.CODE_SYSTEM, null, null, "CODE_RED")));
        assertFalse(CodeConverter.matches(TestKod.CODE_RED, new Kod(null, null, null, "CODE_RED")));

        CodeSystem emptyKod = new EmptyCodeSystem();
        assertFalse(CodeConverter.matches(emptyKod, new Kod(TestKod.CODE_SYSTEM, TestKod.CODE_SYSTEM_NAME,
                TestKod.CODE_SYSTEM_VERSION, "CODE_RED")));
        assertFalse(CodeConverter.matches(emptyKod, new Kod(TestKod.CODE_SYSTEM, TestKod.CODE_SYSTEM_NAME, null,
                "CODE_RED")));
        assertFalse(CodeConverter.matches(emptyKod, new Kod(TestKod.CODE_SYSTEM, null, null, "CODE_RED")));
        assertFalse(CodeConverter.matches(emptyKod, new Kod(null, null, null, "CODE_RED")));
        assertTrue(CodeConverter.matches(emptyKod, new Kod(null, null, null, null)));
    }
}
