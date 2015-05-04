package se.inera.certificate.modules.ts_parent.transformation.test;

public enum KorkortsKodToIntygAvserMapping {

    /** Medeltung lastbil (C1). */
    C_1("IAV1", "VAR1"),

    /** Medeltung lastbil med tungt släpfordon (C1E). */
    C_1_E("IAV2", "VAR2"),

    /** Tung lastbil (C). */
    C("IAV3", "VAR3"),

    /** Tung lastbil med tungt släpfordon (CE). */
    CE("IAV4", "VAR4"),

    /** Mellanstor buss (D1). */
    D_1("IAV5", "VAR5"),

    /** Mellanstor buss med tungt släpfordon (D1E). */
    D_1_E("IAV6", "VAR6"),

    /** Buss (D). */
    D("IAV7", "VAR7"),

    /** Buss med tungt släpfordon (DE). */
    DE("IAV8", "VAR8"),

    /** Taxi (TAXI). */
    TAXI("IAV9", "VAR9"),

    /** Intyget avser inget av ovanstående (ANNAT). */
    ANNAT("IAV10", "VAR10"),

    AM("IAV11", "VAR12"),

    A1("IAV12", "VAR13"),

    A2("IAV13", "VAR14"),

    A("IAV14", "VAR15"),

    B("IAV15", "VAR16"),

    BE("IAV16", "VAR17"),

    TRAKTOR("IAV17", "VAR18"),

    KANINTETASTALLNING(null, "VAR11");

    private String intygAvser;

    private String rekommendation;

    private KorkortsKodToIntygAvserMapping(String intygAvser, String rekommendation) {
        this.intygAvser = intygAvser;
        this.rekommendation = rekommendation;
    }

    public String getIntygAvser() {
        return intygAvser;
    }

    public String getRekommendation() {
        return rekommendation;
    }
}
