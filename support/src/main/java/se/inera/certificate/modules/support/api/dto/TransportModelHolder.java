package se.inera.certificate.modules.support.api.dto;

import static org.springframework.util.Assert.hasText;

public class TransportModelHolder {

    private final String transportModel;

    public TransportModelHolder(String transportModel) {
        hasText(transportModel, "'transportModel' must not be empty");
        this.transportModel = transportModel;
    }

    public String getTransportModel() {
        return transportModel;
    }
}
