package se.inera.certificate.modules.ts_bas.model.codes;

public enum Kodverk {

    SNOMED_CT("1.2.752.116.2.1.1.1", "SNOMED-CT", null),

    ICD_10("1.2.752.116.1.1.1.1.1", "ICD-10", null),

    AKTIVITETER("8040b4d1-67dc-42e1-a938-de5374e9526a", "kv_aktiviteter_intyg", null),

    OBSERVATIONER("to-come", "kv_observationer_intyg", null);

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
