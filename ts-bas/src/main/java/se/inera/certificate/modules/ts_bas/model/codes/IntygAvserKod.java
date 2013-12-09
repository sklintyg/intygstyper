package se.inera.certificate.modules.ts_bas.model.codes;

import java.util.EnumSet;

import se.inera.certificate.model.Kod;

public enum IntygAvserKod implements CodeSystem {

    /** med tungt släpfordon (AM) */
    AM("AM", "Moped klass I"),

    /** Lätt motorcykel (A1) */
    A1("A1", "Lätt motorcykel"),

    /** Mellanstor motorcykel (A2) */
    A2("A2", "Mellanstor motorcykel"),

    /** Motorcykel (A) */
    A("A", "Motorcykel"),

    /** Personbil och lätt lastbil (B) */
    B("B", "Personbil och lätt lastbil"),

    /** Personbil och lätt lastbil med tungt släpfordon (BE) */
    BE("BE", "Personbil och lätt lastbil med tungt släpfordon"),

    /** Medeltung lastbil (C1) */
    C1("C1", "Medeltung lastbil"),

    /** Medeltung lastbil med tungt släpfordon (C1E) */
    C1E("C1E", "Medeltung lastbil med tungt släpfordon"),

    /** Tung lastbil (C) */
    C("C", "Tung lastbil"),

    /** Tung lastbil med tungt släpfordon (CE) */
    CE("CE", "Tung lastbil med tungt släpfordon"),

    /** Mellanstor buss (D1) */
    D1("D1", "Mellanstor buss"),

    /** Mellanstor buss med tungt släpfordon (D1E) */
    D1E("D1E", "Mellanstor buss med tungt släpfordon"),

    /** Buss (D) */
    D("D", "Buss"),

    /** Buss med tungt släpfordon (DE) */
    DE("DE", "Buss med tungt släpfordon"),

    /** Taxi (TAXI) */
    TAXI("TAXI", "Taxi"),

    /** Intyget avser inget av ovanstående (ANNAT) */
    ANNAT("ANNAT", "Intyget avser inget av ovanstående");

    /** Körkortsbehörigheter av högre typ */
    public static final EnumSet<IntygAvserKod> HOGRE_KORKORTSBEHORIGHET = EnumSet.of(C1, C1E, C, CE, D1, D1E, D, DE,
            TAXI);

    /** Körkortsbehörigheter som innefattar persontransport */
    public static final EnumSet<IntygAvserKod> PERSONTRANSPORT = EnumSet.of(D1, D1E, D, DE, TAXI);

    private static String codeSystemName = "kv_intygavser_intyg";

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
