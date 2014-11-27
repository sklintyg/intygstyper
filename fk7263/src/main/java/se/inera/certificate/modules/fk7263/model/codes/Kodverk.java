package se.inera.certificate.modules.fk7263.model.codes;

public enum Kodverk {

    SNOMED_CT("1.2.752.116.2.1.1.1", "SNOMED-CT", null),

    ICD_10("1.2.752.116.1.1.1.1.1", "ICD-10", null);

    private Kodverk(String codeSystem, String codeSystemName, String codeSystemVersion) {
        this.codeSystem = codeSystem;
        this.codeSystemName = codeSystemName;
        this.codeSystemVersion = codeSystemVersion;
    }

    private final String codeSystem;

    private final String codeSystemName;

    private final String codeSystemVersion;

    public String getCodeSystem() {
        return codeSystem;
    }

    public String getCodeSystemName() {
        return codeSystemName;
    }

    public String getCodeSystemVersion() {
        return codeSystemVersion;
    }
}
