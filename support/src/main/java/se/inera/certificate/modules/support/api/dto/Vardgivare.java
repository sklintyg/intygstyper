package se.inera.certificate.modules.support.api.dto;

import static org.springframework.util.Assert.hasText;

public class Vardgivare {

    private final String hsaId;

    private final String namn;

    public Vardgivare(String hsaId, String namn) {
        hasText(hsaId, "'hsaId' must not be empty");
        hasText(namn, "'namn' must not be empty");
        this.hsaId = hsaId;
        this.namn = namn;
    }

    public String getHsaId() {
        return hsaId;
    }

    public String getNamn() {
        return namn;
    }
}
