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

package se.inera.intyg.intygstyper.fk7263.integration.stub;

import static com.google.common.collect.Maps.newHashMap;
import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import se.inera.intyg.intygstyper.fk7263.integration.stub.FkMedicalCertificatesStore;

public class FkMedicalCertificatesStoreTest {

    private FkMedicalCertificatesStore store = null;
    
    @Before
    public void before() {
        store = new FkMedicalCertificatesStore();

        Map<String, String> properties = newHashMap();
        properties.put("Personnummer", "19121212-1212");
        properties.put("Makulerad", "NEJ");
        store.addCertificate("id-0001", newHashMap(properties));

        properties.clear();
        properties.put("Personnummer", "19121212-2222");
        properties.put("Makulerad", "JA");
        store.addCertificate("id-0002", newHashMap(properties));
    }
    
    @Test
    public void testGetCount() throws Exception {
        assertEquals(2, store.getCount());
    }
    
    @Test
    public void testGetAll() throws Exception {
        Map<String, Map<String,String>> all = store.getAll();
        assertEquals(2, all.size());
    }
    
    @Test
    public void testMakulera() throws Exception {
        store.makulera("id-0001", "meddelande");
        assertEquals("JA", store.getAll().get("id-0001").get("Makulerad"));
        assertEquals("meddelande", store.getAll().get("id-0001").get("Meddelande"));
    }
    
    @Test
    public void testClear() throws Exception {
        store.clear();
        assertEquals(0, store.getCount());
    }
}
