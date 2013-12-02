#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
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
package ${package}.${artifactId}.model.codes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import se.inera.certificate.model.Kod;

public class CodeEnumsTest {

    @Test
    public void testArrangemangsTyp() {

        Kod kod = new Kod("1.2.752.116.2.1.1.1", "SNOMED-CT", null, "420008001");

        ArrangemangsKod res = CodeConverter.fromCode(kod, ArrangemangsKod.class);

        assertNotNull(res);
        assertEquals(kod, CodeConverter.toKod(res));
    }

    @Test
    public void testObservationsKod() {

        Kod kod = new Kod("1.2.752.116.2.1.1.1", "SNOMED-CT", null, "39104002");

        ObservationsKod res = CodeConverter.fromCode(kod, ObservationsKod.class);

        assertNotNull(res);
        assertEquals(kod, CodeConverter.toKod(res));
    }

    @Test
    public void testSjukdomsKannedom() {

        Kod kod = new Kod("f3a556c4-d54b-4f67-8496-d5259df70493", "kv_sjukdomskännedom_intyg", null, "SJK1");

        SjukdomskannedomKod res = CodeConverter.fromCode(kod, SjukdomskannedomKod.class);

        assertNotNull(res);
        assertEquals(kod, CodeConverter.toKod(res));
    }

    @Test
    public void testRekommendationsKod() {

        Kod kod = new Kod("5a931b99-bda8-4f1e-8a6d-6f8a3f40a459", "kv_rekommendation_intyg", null, "REK2");

        RekommendationsKod res = CodeConverter.fromCode(kod, RekommendationsKod.class);

        assertNotNull(res);
        assertEquals(kod, CodeConverter.toKod(res));
    }

}
