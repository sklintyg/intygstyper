package se.inera.certificate.modules.ts_bas.model.codes;

public enum VardkontakttypKod {

    /** "5880005", "Min undersökning med patienten". */
    MIN_UNDERSOKNING("5880005", "Min undersökning med patienten");

    private static String codeSystemName = "SNOMED-CT";

    private static String codeSystem = "1.2.752.116.2.1.1.1";

    private static String codeSystemVersion = null;

    private String code;

    private String description;

    private VardkontakttypKod(String code, String desc) {
        this.code = code;
        this.description = desc;
    }

    public String getCode() {
        return this.code;
    }

    public String getDescription() {
        return this.description;
    }

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
