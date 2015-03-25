package se.inera.certificate.modules.ts_bas.integration.stub;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

@Component
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
