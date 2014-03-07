package se.inera.certificate.modules.support.api.dto;

import static org.springframework.util.Assert.hasText;
import static org.springframework.util.Assert.notNull;

public class HoSPersonal {

    private final String hsaId;

    private final String namn;

    private final String forskrivarkod;

    private final String befattning;

    private final Vardenhet vardenhet;

    public HoSPersonal(String hsaId, String namn, String forskrivarkod, String befattning, Vardenhet vardenhet) {
        hasText(hsaId, "'hsaId' must not be empty");
        hasText(namn, "'namn' must not be empty");
        notNull(vardenhet, "'vardenhet' must not be null");
        this.hsaId = hsaId;
        this.namn = namn;
        this.forskrivarkod = forskrivarkod;
        this.befattning = befattning;
        this.vardenhet = vardenhet;
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
}
