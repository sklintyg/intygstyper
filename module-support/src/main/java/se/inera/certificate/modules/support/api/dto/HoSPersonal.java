package se.inera.certificate.modules.support.api.dto;

import static org.springframework.util.Assert.hasText;
import static org.springframework.util.Assert.notNull;

import java.util.ArrayList;
import java.util.List;

public class HoSPersonal {

    private final String hsaId;

    private final String namn;

    private final String forskrivarkod;

    private final String befattning;

    private List<String> specialiseringar;

    private final Vardenhet vardenhet;

    public HoSPersonal(String hsaId, String namn, String forskrivarkod, String befattning, List<String> specialiseringar, Vardenhet vardenhet) {
        hasText(hsaId, "'hsaId' must not be empty");
        hasText(namn, "'namn' must not be empty");
        notNull(vardenhet, "'vardenhet' must not be null");
        this.hsaId = hsaId;
        this.namn = namn;
        this.vardenhet = vardenhet;
        this.forskrivarkod = forskrivarkod;
        this.befattning = befattning;
        if (specialiseringar != null) {
            this.specialiseringar = new ArrayList<String>();
            this.specialiseringar.addAll(specialiseringar);
        }
    }

    public String getHsaId() {
        return hsaId;
    }

    public String getNamn() {
        return namn;
    }

    public String getForskrivarkod() {
        return forskrivarkod;
    }

    public String getBefattning() {
        return befattning;
    }

    public Vardenhet getVardenhet() {
        return vardenhet;
    }

    public List<String> getSpecialiseringar() {
        return specialiseringar;
    }
}
