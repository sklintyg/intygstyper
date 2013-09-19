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
package se.inera.certificate.modules.rli.model.codes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class CodeEnumsTest {

    @Test
    public void testArrangemangsTyp() {

        String code = "420008001";

        ArrangemangsTyp res = CodeConverter.fromCode(code, ArrangemangsTyp.class);

        assertNotNull(res);
        assertEquals(code, res.getCode());
    }

    @Test
    public void testObservationsKod() {

        String code = "39104002";

        ObservationsKod res = CodeConverter.fromCode(code, ObservationsKod.class);

        assertNotNull(res);
        assertEquals(code, res.getCode());
    }

    @Test
    public void testSjukdomsKannedom() {

        String code = "SJK1";

        SjukdomsKannedom res = CodeConverter.fromCode(code, SjukdomsKannedom.class);

        assertNotNull(res);
        assertEquals(code, res.getCode());
    }

    @Test
    public void testRekommendationsKod() {

        String code = "REK2";

        RekommendationsKod res = CodeConverter.fromCode(code, RekommendationsKod.class);

        assertNotNull(res);
        assertEquals(code, res.getCode());
    }

}
