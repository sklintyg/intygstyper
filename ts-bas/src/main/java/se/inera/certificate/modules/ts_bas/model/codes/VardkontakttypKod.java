package se.inera.certificate.modules.ts_bas.model.codes;

import se.inera.certificate.model.Kod;
import se.inera.certificate.model.common.codes.CodeConverter;
import se.inera.certificate.model.common.codes.CodeSystem;

/**
 *
 */
public enum VardkontakttypKod implements CodeSystem {

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

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCode() {
        return this.code;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDescription() {
        return this.description;
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
