package se.inera.certificate.modules.support.api.dto;

import static org.springframework.util.Assert.hasText;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.joda.time.LocalDate;

/**
 * Wrapper class that holds meta information about a certificate, such as the list of statuses
 */
public class CertificateContentMeta {

    private final String id;

    private final String type;

    private final String patientId;

    private final LocalDate fromDate;

    private final LocalDate tomDate;

    private final List<CertificateStatus> statuses;

    public CertificateContentMeta(String id, String type, String patientId, LocalDate fromDate, LocalDate tomDate,
            List<CertificateStatus> statuses) {
        hasText(id, "'id' must not be empty");
        hasText(type, "'type' must not be empty");
        hasText(patientId, "'patientId' must not be empty");
        this.id = id;
        this.type = type;
        this.patientId = patientId;
        this.fromDate = fromDate;
        this.tomDate = tomDate;
        if (statuses != null) {
            this.statuses = new ArrayList<>(statuses);
        } else {
            this.statuses = Collections.emptyList();
        }
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public LocalDate getTomDate() {
        return tomDate;
    }

    public List<CertificateStatus> getStatuses() {
        return Collections.unmodifiableList(statuses);
    }

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public String getPatientId() {
        return patientId;
    }
}
