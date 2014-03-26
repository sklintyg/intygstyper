package se.inera.certificate.modules.support.api.dto;

public enum TransportModelVersion {
    /** */
    LEGACY_LAKARUTLATANDE("urn:riv:insuranceprocess:healthreporting:mu7263:3"),
    /** */
    UTLATANDE_V1("urn:riv:clinicalprocess:healthcond:certificate:1");

    private final String namespace;

    private TransportModelVersion(String namespace) {
        this.namespace = namespace;
    }

    public String getNamespace() {
        return namespace;
    }
}
