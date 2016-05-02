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

package se.inera.intyg.intygstyper.ts_parent.integration.stub;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TSCertificateStore {

    private ConcurrentHashMap<String, Map<String, String>> certificates = new ConcurrentHashMap<String, Map<String, String>>();

    public void addCertificate(String id, Map<String, String> props) {
        certificates.put(id, props);
    }

    public int getCount() {
        return certificates.size();
    }

    public Map<String, Map<String, String>> getAll() {
        return new HashMap<String, Map<String, String>>(certificates);
    }

    public void clear() {
        certificates.clear();
    }

    public void makulera(String id, String meddelande) {
        Map<String, String> m = certificates.get(id);
        if (m == null) {
            m = new HashMap<>();
        }
        m.put("Makulerad", "JA");
        m.put("Meddelande", meddelande);
        certificates.put(id, m);
    }
}
