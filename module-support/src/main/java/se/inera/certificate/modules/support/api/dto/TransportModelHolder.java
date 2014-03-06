package se.inera.certificate.modules.support.api.dto;

public class TransportModelHolder {

    private final String transportModel;

    private TransportModelHolder(String transportModel) {
        this.transportModel = transportModel;
    }

    public String getTransportModel() {
        return transportModel;
    }
}
