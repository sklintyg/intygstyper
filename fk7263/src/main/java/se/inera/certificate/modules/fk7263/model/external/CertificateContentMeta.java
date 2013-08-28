package se.inera.certificate.modules.fk7263.model.external;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CertificateContentMeta implements Serializable {

    private String id;
    private String type;
    private String fromDate;
    private String tomDate;

    private List<StatusMeta> statuses = new ArrayList<>();

    public List<StatusMeta> getStatuses() {
        return statuses;
    }

    public void setStatuses(List<StatusMeta> statuses) {
        this.statuses = statuses;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTomDate() {
        return tomDate;
    }

    public void setTomDate(String tomDate) {
        this.tomDate = tomDate;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }
}
