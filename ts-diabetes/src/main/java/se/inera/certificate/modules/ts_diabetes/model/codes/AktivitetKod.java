package se.inera.certificate.modules.ts_diabetes.model.codes;

import static se.inera.certificate.modules.ts_diabetes.model.codes.Kodverk.AKTIVITETER;
import static se.inera.certificate.modules.ts_diabetes.model.codes.Kodverk.SNOMED_CT;

import se.inera.certificate.model.Kod;

public enum AktivitetKod implements CodeSystem {

    /** Prövning av ögats rörlighet (AKT18). */
    PROVNING_AV_OGATS_RORLIGHET("AKT18", "Prövning av ögats rörlighet ", AKTIVITETER),

    /** Synfältsprövning (86944008). */
    SYNFALTSUNDERSOKNING("86944008", "Synfältsundersökning", SNOMED_CT),

    /** Egenövervakning av blodglukos (308113006). */
    EGENOVERVAKNING_BLODGLUKOS("308113006", "Egenövervakning av blodglukos", SNOMED_CT);

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
