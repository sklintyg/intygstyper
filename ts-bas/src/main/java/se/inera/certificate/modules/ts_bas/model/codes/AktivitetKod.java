package se.inera.certificate.modules.ts_bas.model.codes;

import static se.inera.certificate.modules.ts_bas.model.codes.Kodverk.*;

import se.inera.certificate.model.Kod;

public enum AktivitetKod implements CodeSystem {

    /** provtagning avseende aktuellt bruk av alkohol eller narkotika (AKT14) */
    PROVTAGNING_ALKOHO_NARKOTIKA("AKT14", "provtagning avseende aktuellt bruk av alkohol eller narkotika", AKTIVITETER),

    /** Vårdinsats för missbruk eller beroende av alkohol, narkotika eller läkemedel (AKT15) */
    VARDINSATS_MISSBRUK_BEROENDE("AKT15",
            "Vårdinsats för missbruk eller beroende av alkohol, narkotika eller läkemedel", AKTIVITETER),

    /** Tablettbehandling (AKT16) */
    TABLETTBEHANDLING("AKT16", "Tablettbehandling", AKTIVITETER),

    /** Kostbehandling (284071006) */
    KOSTBEHANDLING("284071006", "Kostbehandling", SNOMED_CT),

    /** Insulinbehandling (225302006) */
    INSULINBEHANDLING("225302006", "Insulinbehandling", SNOMED_CT),

    /** Undersökning med > +8 dioptriers korrektionsglad (AKT17) */
    UNDERSOKNING_PLUS8_KORREKTIONSGRAD("AKT17", "Undersökning med > +8 dioptriers korrektionsglad", AKTIVITETER),
    
    /** Synfältsprövning */
    SYNFALTSUNDERSOKNING("86944008", "Synfältsundersökning", SNOMED_CT);

    private final String code;

    private final String description;

    private final String codeSystem;

    private final String codeSystemName;

    private final String codeSystemVersion;

    private AktivitetKod(String code, String description, Kodverk kodverk) {
        this.code = code;
        this.description = description;
        this.codeSystem = kodverk.getCodeSystem();
        this.codeSystemName = kodverk.getCodeSystemName();
        this.codeSystemVersion = kodverk.getCodeSystemVersion();
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
