package se.inera.certificate.modules.support.api.dto;

import static org.springframework.util.Assert.hasText;

public class TransportModelResponse {

    private final String transportModel;

    public TransportModelResponse(String transportModel) {
        hasText(transportModel, "'transportModel' must not be empty");
        this.transportModel = transportModel;
    }

    public String getTransportModel() {
        return transportModel;
    }
}
