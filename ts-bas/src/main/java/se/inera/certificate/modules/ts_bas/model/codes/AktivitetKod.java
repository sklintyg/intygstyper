package se.inera.certificate.modules.ts_bas.model.codes;

import static se.inera.certificate.modules.ts_bas.model.codes.Kodverk.*;

import se.inera.certificate.model.Kod;

public enum AktivitetKod implements CodeSystem {

    /** provtagning avseende aktuellt bruk av alkohol eller narkotika (AKT14) */
    PROVTAGNING_ALKOHOL_NARKOTIKA("AKT14", "provtagning avseende aktuellt bruk av alkohol eller narkotika", AKTIVITETER),

    /** Vårdinsats för missbruk eller beroende av alkohol, narkotika eller läkemedel (AKT15) */
    VARDINSATS_MISSBRUK_BEROENDE("AKT15",
            "Vårdinsats för missbruk eller beroende av alkohol, narkotika eller läkemedel", AKTIVITETER),

    /** Undersökning med > +8 dioptriers korrektionsglad (AKT17) */
    UNDERSOKNING_PLUS8_KORREKTIONSGRAD("AKT17", "Undersökning med > +8 dioptriers korrektionsglad", AKTIVITETER),
    
    /** Prövning av ögats rörlighet (AKT18) */
    PROVNING_AV_OGATS_RORLIGHET("AKT18", "Prövning av ögats rörlighet ", AKTIVITETER),
    
    /** Synfältsprövning (86944008) */
    SYNFALTSUNDERSOKNING("86944008", "Synfältsundersökning", SNOMED_CT),
    
    /** Vård på sjukhus eller kontakt med läkare (AKT19)*/
    VARD_PA_SJUKHUS("AKT19", "Vård på sjukhus eller kontakt med läkare", AKTIVITETER);
    
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
