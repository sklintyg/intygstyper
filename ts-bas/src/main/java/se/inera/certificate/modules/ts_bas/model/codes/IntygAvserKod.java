package se.inera.certificate.modules.ts_bas.model.codes;

import java.util.EnumSet;

import se.inera.certificate.model.Kod;

public enum IntygAvserKod implements CodeSystem {

    /** Medeltung lastbil (C1) */
    C1("IAV1", "Medeltung lastbil"),

    /** Medeltung lastbil med tungt släpfordon (C1E) */
    C1E("IAV2", "Medeltung lastbil med tungt släpfordon"),

    /** Tung lastbil (C) */
    C("IAV3", "Tung lastbil"),

    /** Tung lastbil med tungt släpfordon (CE) */
    CE("IAV4", "Tung lastbil med tungt släpfordon"),

    /** Mellanstor buss (D1) */
    D1("IAV5", "Mellanstor buss"),

    /** Mellanstor buss med tungt släpfordon (D1E) */
    D1E("IAV6", "Mellanstor buss med tungt släpfordon"),

    /** Buss (D) */
    D("IAV7", "Buss"),

    /** Buss med tungt släpfordon (DE) */
    DE("IAV8", "Buss med tungt släpfordon"),

    /** Taxi (TAXI) */
    TAXI("IAV9", "Taxi"),

    /** Intyget avser inget av ovanstående (ANNAT) */
    ANNAT("IAV10", "Intyget avser inget av ovanstående"),
    
    /** med tungt släpfordon (AM) */
    AM("IAV11", "Moped klass I"),

    /** Lätt motorcykel (A1) */
    A1("IAV12", "Lätt motorcykel"),

    /** Mellanstor motorcykel (A2) */
    A2("IAV13", "Mellanstor motorcykel"),

    /** Motorcykel (A) */
    A("IAV14", "Motorcykel"),

    /** Personbil och lätt lastbil (B) */
    B("IAV15", "Personbil och lätt lastbil"),

    /** Personbil och lätt lastbil med tungt släpfordon (BE) */
    BE("IAV16", "Personbil och lätt lastbil med tungt släpfordon"),
    
    TRAKTOR("IAV17", "Traktor");

    /** Körkortsbehörigheter av högre typ */
    public static final EnumSet<IntygAvserKod> HOGRE_KORKORTSBEHORIGHET = EnumSet.of(C1, C1E, C, CE, D1, D1E, D, DE,
            TAXI);

    /** Körkortsbehörigheter som innefattar persontransport */
    public static final EnumSet<IntygAvserKod> PERSONTRANSPORT = EnumSet.of(D1, D1E, D, DE, TAXI);

    private static String codeSystemName = "kv_intygavser_intyg";

    // TODO: Change to correct OID when available
    private static String codeSystem = "todo";

    private static String codeSystemVersion = null;

    private String code;

    private String description;

    private IntygAvserKod(String code, String desc) {
        this.code = code;
        this.description = desc;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCode() {
        return code;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDescription() {
        return description;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCodeSystem() {
        return codeSystem;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCodeSystemName() {
        return codeSystemName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCodeSystemVersion() {
        return codeSystemVersion;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean matches(Kod kod) {
        return CodeConverter.matches(this, kod);
    }
}
