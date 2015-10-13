package se.inera.certificate.modules.ts_diabetes.model.codes;
import se.inera.certificate.modules.ts_parent.codes.Kodverk;
import static se.inera.certificate.modules.ts_parent.codes.Kodverk.ICD_10;
import static se.inera.certificate.modules.ts_parent.codes.Kodverk.OBSERVATIONER;
import static se.inera.certificate.modules.ts_parent.codes.Kodverk.SNOMED_CT;

public enum ObservationsKod  {

    /** Diplopi (H53.2). */
    DIPLOPI("H53.2", "Diplopi", ICD_10),

    /** Ej korrigerad synskärpa (420050001). */
    EJ_KORRIGERAD_SYNSKARPA("420050001", "Ej korrigerad synskärpa", SNOMED_CT),

    /** Korrigerad synskärpa (397535007). */
    KORRIGERAD_SYNSKARPA("397535007", "Korrigerad synskärpa", SNOMED_CT),

    /** Synskärpa, med glasögon (408333000). */
    SYNSKARPA_GLASOGON("408333000", "Synskärpa, med glasögon", SNOMED_CT),

    /** Diabetes mellitus typ 1 (E10). */
    DIABETES_TYP_1("E10", "Diabetes mellitus typ 1", ICD_10),

    /** Diabetes mellitus typ 2 (E11). */
    DIABETES_TYP_2("E11", "Diabetes mellitus typ 2", ICD_10),

    /** Diabetiker på tablettbehandling (170746002). */
    DIABETIKER_TABLETTBEHANDLING("170746002", "Diabetiker på tablettbehandling", SNOMED_CT),

    /** Diabetiker på enbart kostbehandling (170745003). */
    DIABETIKER_ENBART_KOST("170745003", "Diabetiker på enbart kostbehandling", SNOMED_CT),

    /** Diabetiker på insulinbehandling (170747006). */
    DIABETIKER_INSULINBEHANDLING("170747006", "Diabetiker på insulinbehandling", SNOMED_CT),

    /** Diabetiker på annan behandling (OBS10). */
    DIABETIKER_ANNAN_BEHANDLING("OBS10", "Diabetiker på annan behandling", OBSERVATIONER),

    /** Patienten har kunskap om lämpliga åtgärder vid hypoglykemi (OBS19). */
    KUNSKAP_ATGARD_HYPOGLYKEMI("OBS19", "Patienten har kunskap om lämpliga åtgärder vid hypoglykemi", OBSERVATIONER),

    /** Förekomst av hypoglykemier med tecken på nedsatt hjärnfunktion (neuroglukopena symptom) (OBS20). */
    HYPOGLYKEMIER_MED_TECKEN_PA_NEDSATT_HJARNFUNKTION("OBS20",
            "Förekomst av hypoglykemier med tecken på nedsatt hjärnfunktion", OBSERVATIONER),
    /** Patienten saknar förmåga att känna varningstecken på hypoglykemi (OBS21). */
    SAKNAR_FORMAGA_KANNA_HYPOGLYKEMI("OBS21", "Patienten saknar förmåga att känna varningstecken på hypoglykemi",
            OBSERVATIONER),
    /** Förekomst av allvarlig hypoglykemi under det senaste året (OBS22). */
    ALLVARLIG_HYPOGLYKEMI("OBS22", "Förekomst av allvarlig hypoglykemi under det senaste året", OBSERVATIONER),

    /** Förekomst av allvarlig hypoglykemi i trafiken under det senaste året (OBS23). */
    ALLVARLIG_HYPOGLYKEMI_I_TRAFIKEN("OBS23", "Förekomst av allvarlig hypoglykemi i trafiken under det senaste året",
            OBSERVATIONER),

    /** Förekomst av allvarlig hypoglykemi under vaken tid det senaste året (OBS24). */
    ALLVARLIG_HYPOGLYKEMI_VAKET_TILLSTAND("OBS24",
            "Förekomst av allvarlig hypoglykemi under vaken tid det senaste året", OBSERVATIONER),

    /** Synfältsprövning utan anmärkning (OBS25). */
    SYNFALTSPROVNING_UTAN_ANMARKNING("OBS25", "Synfältsprövning utan anmärkning", OBSERVATIONER);

    private final String code;

    private final String description;

    private final String codeSystem;

    private final String codeSystemName;

    private final String codeSystemVersion;

    ObservationsKod(String code, String description, Kodverk kodverk) {
        this.code = code;
        this.description = description;
        this.codeSystem = kodverk.getCodeSystem();
        this.codeSystemName = kodverk.getCodeSystemName();
        this.codeSystemVersion = kodverk.getCodeSystemVersion();
    }

    /**
     * {@inheritDoc}
     */
    public String getCode() {
        return code;
    }

    /**
     * {@inheritDoc}
     */
    public String getDescription() {
        return description;
    }

    /**
     * {@inheritDoc}
     */
    public String getCodeSystem() {
        return codeSystem;
    }

    /**
     * {@inheritDoc}
     */
    public String getCodeSystemName() {
        return codeSystemName;
    }

    /**
     * {@inheritDoc}
     */
    public String getCodeSystemVersion() {
        return codeSystemVersion;
    }

    /**
     * {@inheritDoc}
     */

    public boolean matches(String code) {
        return this.code.equals(code);
    }
}
