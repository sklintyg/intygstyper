package se.inera.certificate.modules.support.api.dto;

import static org.springframework.util.Assert.hasText;
import static org.springframework.util.Assert.notNull;

import org.joda.time.LocalDateTime;

public class CertificateStatus {

    private final String type;

    private final String target;

    private final LocalDateTime timestamp;

    public CertificateStatus(String type, String target, LocalDateTime timestamp) {
        hasText(type, "'type' must not be empty");
        hasText(target, "'target' must not be empty");
        notNull(timestamp, "'timestamp' must not be null");
        this.type = type;
        this.target = target;
        this.timestamp = timestamp;
    }

    public String getType() {
        return type;
    }

    public String getTarget() {
        return target;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
