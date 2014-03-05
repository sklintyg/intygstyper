package se.inera.certificate.modules.ts_diabetes.model.codes;

public enum Kodverk {

    /** SNOMED-CT (1.2.752.116.2.1.1.1) */
    SNOMED_CT("1.2.752.116.2.1.1.1", "SNOMED-CT", null),

    /** ICD-10 (1.2.752.116.1.1.1.1.1) */
    ICD_10("1.2.752.116.1.1.1.1.1", "ICD-10", null),

    /** kv_aktiviteter_intyg (8040b4d1-67dc-42e1-a938-de5374e9526a) */
    AKTIVITETER("8040b4d1-67dc-42e1-a938-de5374e9526a", "kv_aktiviteter_intyg", null),

    // TODO: Change to correct OID when this is available
    /** kv_observationer_intyg (to-come) */
    OBSERVATIONER("to-come", "kv_observationer_intyg", null),

    // TODO: Change to correct OID when this is available
    /** kv_id_kontroll (???) */
    ID_KONTROLL("???", "kv_id_kontroll", null);

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
