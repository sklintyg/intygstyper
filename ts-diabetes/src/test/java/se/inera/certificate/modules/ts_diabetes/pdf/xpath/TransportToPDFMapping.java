package se.inera.certificate.modules.ts_diabetes.pdf.xpath;

import static se.inera.certificate.modules.ts_diabetes.pdf.xpath.XPathExpressions.ADRESS_OCH_ORT_XPATH;
import static se.inera.certificate.modules.ts_diabetes.pdf.xpath.XPathExpressions.AKTIVITET_FOREKOMST_TEMPLATE;
import static se.inera.certificate.modules.ts_diabetes.pdf.xpath.XPathExpressions.ALLVARLIG_HYPOGLYKEMI_VAKET_TILLSTAND_DATUM_XPATH;
import static se.inera.certificate.modules.ts_diabetes.pdf.xpath.XPathExpressions.DIABETES_AR_FOR_DIAGNOS_XPATH;
import static se.inera.certificate.modules.ts_diabetes.pdf.xpath.XPathExpressions.DIABETIKER_INSULINBEHANDLING_SEDAN_XPATH;
import static se.inera.certificate.modules.ts_diabetes.pdf.xpath.XPathExpressions.ID_KONTROLL_TEMPLATE;
import static se.inera.certificate.modules.ts_diabetes.pdf.xpath.XPathExpressions.INTYGSDATUM_XPATH;
import static se.inera.certificate.modules.ts_diabetes.pdf.xpath.XPathExpressions.INTYG_AVSER_TEMPLATE;
import static se.inera.certificate.modules.ts_diabetes.pdf.xpath.XPathExpressions.INVANARE_ADRESS_FALT1_XPATH;
import static se.inera.certificate.modules.ts_diabetes.pdf.xpath.XPathExpressions.INVANARE_ADRESS_FALT2_XPATH;
import static se.inera.certificate.modules.ts_diabetes.pdf.xpath.XPathExpressions.INVANARE_ADRESS_FALT3_XPATH;
import static se.inera.certificate.modules.ts_diabetes.pdf.xpath.XPathExpressions.INVANARE_PERSONNUMMER_XPATH;
import static se.inera.certificate.modules.ts_diabetes.pdf.xpath.XPathExpressions.NAMNFORTYDLIGANDE_XPATH;
import static se.inera.certificate.modules.ts_diabetes.pdf.xpath.XPathExpressions.OBSERVATION_BESKRIVNING_TEMPLATE;
import static se.inera.certificate.modules.ts_diabetes.pdf.xpath.XPathExpressions.OBSERVATION_BILAGA_TEMPLATE;
import static se.inera.certificate.modules.ts_diabetes.pdf.xpath.XPathExpressions.OBSERVATION_FOREKOMST_TEMPLATE;
import static se.inera.certificate.modules.ts_diabetes.pdf.xpath.XPathExpressions.OBSERVATION_VARDE_DEC_CODE_LATERALITET;
import static se.inera.certificate.modules.ts_diabetes.pdf.xpath.XPathExpressions.OBSERVATION_VARDE_INT_CODE_LATERALITET;
import static se.inera.certificate.modules.ts_diabetes.pdf.xpath.XPathExpressions.OVRIG_BESKRIVNING_XPATH;
import static se.inera.certificate.modules.ts_diabetes.pdf.xpath.XPathExpressions.REKOMMENDATION_BESKRIVNING_TEMPLATE;
import static se.inera.certificate.modules.ts_diabetes.pdf.xpath.XPathExpressions.REKOMMENDATION_VARDE_TEMPLATE;
import static se.inera.certificate.modules.ts_diabetes.pdf.xpath.XPathExpressions.REKOMMENDATION_KORKORTSBEHORIGHET_TEMPLATE;
import static se.inera.certificate.modules.ts_diabetes.pdf.xpath.XPathExpressions.SPECIALISTKOMPETENS_BESKRVNING_XPATH;
import static se.inera.certificate.modules.ts_diabetes.pdf.xpath.XPathExpressions.TELEFON_XPATH;
import static se.inera.certificate.modules.ts_diabetes.pdf.xpath.XPathExpressions.TS_UTGAVA_XPATH;
import static se.inera.certificate.modules.ts_diabetes.pdf.xpath.XPathExpressions.TS_VERSION_XPATH;
import static se.inera.certificate.modules.ts_diabetes.pdf.xpath.XPathExpressions.VARDINRATTNINGENS_NAMN_XPATH;
import static se.inera.certificate.modules.ts_diabetes.pdf.xpath.XPathExpressions.booleanXPath;
import static se.inera.certificate.modules.ts_diabetes.pdf.xpath.XPathExpressions.stringXPath;

/**
 * Defines all xPath expressions that are needed in order to extract the information of the transport model in the way
 * the PDF is structured.
 */
public enum TransportToPDFMapping {

    /** */
    TS_UTGAVA(null, TS_UTGAVA_XPATH),

    /** */
    TS_VERSION(null, TS_VERSION_XPATH),

    /** */
    INVANARE_ADRESS_FALT1("Falt__1", INVANARE_ADRESS_FALT1_XPATH),

    /** */
    INVANARE_ADRESS_FALT2("Falt__2", INVANARE_ADRESS_FALT2_XPATH),

    /** */
    INVANARE_ADRESS_FALT3("Falt__3", INVANARE_ADRESS_FALT3_XPATH),

    /** */
    INVANARE_PERSONNUMMER("Falt__4", INVANARE_PERSONNUMMER_XPATH),

    /** */
    INTYG_AVSER_AM("Falt_4", booleanXPath(INTYG_AVSER_TEMPLATE, "IAV11")),

    /** */
    INTYG_AVSER_A1("Falt_5", booleanXPath(INTYG_AVSER_TEMPLATE, "IAV12")),

    /** */
    INTYG_AVSER_A2("Falt_6", booleanXPath(INTYG_AVSER_TEMPLATE, "IAV13")),

    /** */
    INTYG_AVSER_A("Falt_7", booleanXPath(INTYG_AVSER_TEMPLATE, "IAV14")),

    /** */
    INTYG_AVSER_B("Falt_8", booleanXPath(INTYG_AVSER_TEMPLATE, "IAV15")),

    /** */
    INTYG_AVSER_BE("Falt_9", booleanXPath(INTYG_AVSER_TEMPLATE, "IAV16")),

    /** */
    INTYG_AVSER_TRAKTOR("Falt_10", booleanXPath(INTYG_AVSER_TEMPLATE, "IAV17")),

    /** */
    INTYG_AVSER_C1("Falt_11", booleanXPath(INTYG_AVSER_TEMPLATE, "IAV1")),

    /** */
    INTYG_AVSER_C1E("Falt_12", booleanXPath(INTYG_AVSER_TEMPLATE, "IAV2")),

    /** */
    INTYG_AVSER_C("Falt_13", booleanXPath(INTYG_AVSER_TEMPLATE, "IAV3")),

    /** */
    INTYG_AVSER_CE("Falt_14", booleanXPath(INTYG_AVSER_TEMPLATE, "IAV4")),

    /** */
    INTYG_AVSER_D1("Falt_15", booleanXPath(INTYG_AVSER_TEMPLATE, "IAV5")),

    /** */
    INTYG_AVSER_D1E("Falt_16", booleanXPath(INTYG_AVSER_TEMPLATE, "IAV6")),

    /** */
    INTYG_AVSER_D("Falt_17", booleanXPath(INTYG_AVSER_TEMPLATE, "IAV7")),

    /** */
    INTYG_AVSER_DE("Falt_18", booleanXPath(INTYG_AVSER_TEMPLATE, "IAV8")),

    /** */
    INTYG_AVSER_TAXI("Falt_19", booleanXPath(INTYG_AVSER_TEMPLATE, "IAV9")),

    /** */
    ID_KONTROLL_IDKORT("Falt_20", booleanXPath(ID_KONTROLL_TEMPLATE, "IDK1")),

    /** */
    ID_KONTROLL_FORETAG_TJANSTEKORT("Falt_21", booleanXPath(ID_KONTROLL_TEMPLATE, "IDK2")),

    /** */
    ID_KONTROLL_SVENSKT_KORKORT("Falt_22", booleanXPath(ID_KONTROLL_TEMPLATE, "IDK3")),

    /** */
    ID_KONTROLL_PERSONLIG_KANNEDOM("Falt_23", booleanXPath(ID_KONTROLL_TEMPLATE, "IDK4")),

    /** */
    ID_KONTROLL_FORSAKRAN("Falt_24", booleanXPath(ID_KONTROLL_TEMPLATE, "IDK5")),

    /** */
    ID_KONTROLL_PASS("Falt_25", booleanXPath(ID_KONTROLL_TEMPLATE, "IDK6")),

    /** */
    DIABETES_AR_FOR_DIAGNOS("Falt__31", DIABETES_AR_FOR_DIAGNOS_XPATH),

    /** */
    DIABETES_TYP_1("Falt_32", booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "E10", "true")),

    /** */
    DIABETES_TYP_2("Falt_33", booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "E11", "true")),

    /** */
    DIABETIKER_ENBART_KOST("Falt_34", booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "170745003", "true")),

    /** */
    DIABETIKER_TABLETTBEHANDLING("Falt_35", booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "170746002", "true")),

    /** */
    DIABETIKER_INSULINBEHANDLING("Falt_36", booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "170747006", "true")),

    /** */
    DIABETIKER_INSULINBEHANDLING_SEDAN_CHECK("Falt_37", booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "170747006",
            "true")),

    /** */
    DIABETIKER_INSULINBEHANDLING_SEDAN("Falt__38", DIABETIKER_INSULINBEHANDLING_SEDAN_XPATH),

    /** */
    DIABETIKER_ANNAN_BEHANDLING("Falt__39", stringXPath(OBSERVATION_BESKRIVNING_TEMPLATE, "OBS10")),

    /** */
    KUNSKAP_ATGARD_HYPOGLYKEMI_YES("Falt_40", booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "OBS19", "true")),

    /** */
    KUNSKAP_ATGARD_HYPOGLYKEMI_NO("Falt_41", booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "OBS19", "false")),

    /** */
    HYPOGLYKEMIER_MED_TECKEN_PA_NEDSATT_HJARNFUNKTION_YES("Falt_44", booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE,
            "OBS20", "true")),

    /** */
    HYPOGLYKEMIER_MED_TECKEN_PA_NEDSATT_HJARNFUNKTION_NO("Falt_45", booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE,
            "OBS20", "false")),

    /** */
    SAKNAR_FORMAGA_KANNA_HYPOGLYKEMI_YES("Falt_48", booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "OBS21", "true")),

    /** */
    SAKNAR_FORMAGA_KANNA_HYPOGLYKEMI_NO("Falt_49", booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "OBS21", "false")),

    /** */
    ALLVARLIG_HYPOGLYKEMI_YES("Falt_27", booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "OBS22", "true")),

    /** */
    ALLVARLIG_HYPOGLYKEMI_NO("Falt_28", booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "OBS22", "false")),

    /** */
    ALLVARLIG_HYPOGLYKEMI_ANTAL("Falt__50", stringXPath(OBSERVATION_BESKRIVNING_TEMPLATE, "OBS22")),

    /** */
    ALLVARLIG_HYPOGLYKEMI_I_TRAFIKEN_YES("Falt_51", booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "OBS23", "true")),

    /** */
    ALLVARLIG_HYPOGLYKEMI_I_TRAFIKEN_NO("Falt_52", booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "OBS23", "false")),

    /** */
    ALLVARLIG_HYPOGLYKEMI_I_TRAFIKEN_BESKRIVNING("Falt__501", stringXPath(OBSERVATION_BESKRIVNING_TEMPLATE, "OBS23")),

    /** */
    EGENOVERVAKNING_BLODGLUKOS_YES("Falt_53", booleanXPath(AKTIVITET_FOREKOMST_TEMPLATE, "308113006", "true")),

    /** */
    EGENOVERVAKNING_BLODGLUKOS_NO("Falt_54", booleanXPath(AKTIVITET_FOREKOMST_TEMPLATE, "308113006", "false")),

    /** */
    ALLVARLIG_HYPOGLYKEMI_VAKET_TILLSTAND_YES("Falt_55", booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "OBS24", "true")),

    /** */
    ALLVARLIG_HYPOGLYKEMI_VAKET_TILLSTAND_NO("Falt_56", booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "OBS24", "false")),

    /** */
    ALLVARLIG_HYPOGLYKEMI_VAKET_TILLSTAND_DATUM("Falt__61", ALLVARLIG_HYPOGLYKEMI_VAKET_TILLSTAND_DATUM_XPATH),

    /** */
    OGONLAKARINTYG_YES("Falt_62", booleanXPath(OBSERVATION_BILAGA_TEMPLATE, "BIL1", "true")),

    /** */
    OGONLAKARINTYG_NO("Falt_63", booleanXPath(OBSERVATION_BILAGA_TEMPLATE, "BIL1", "false")),

    /** */
    SYNFALTSUNDERSOKNING_YES("Falt_64", booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "OBS25", "true")),

    /** */
    SYNFALTSUNDERSOKNING_NO("Falt_65", booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "OBS25", "false")),

    /** */
    EJ_KORRIGERAD_SYNSKARPA_HOGER_INT("Falt__66", stringXPath(OBSERVATION_VARDE_INT_CODE_LATERALITET, "420050001",
            "24028007")),

    /** */
    EJ_KORRIGERAD_SYNSKARPA_HOGER_DEC("Falt__67", stringXPath(OBSERVATION_VARDE_DEC_CODE_LATERALITET, "420050001",
            "24028007")),

    /** */
    KORRIGERAD_SYNSKARPA_HOGER_INT("Falt__68", stringXPath(OBSERVATION_VARDE_INT_CODE_LATERALITET, "397535007",
            "24028007")),

    /** */
    KORRIGERAD_SYNSKARPA_HOGER_DEC("Falt__69", stringXPath(OBSERVATION_VARDE_DEC_CODE_LATERALITET, "397535007",
            "24028007")),

    /** */
    EJ_KORRIGERAD_SYNSKARPA_VANSTER_INT("Falt__70", stringXPath(OBSERVATION_VARDE_INT_CODE_LATERALITET, "420050001",
            "7771000")),

    /** */
    EJ_KORRIGERAD_SYNSKARPA_VANSTER_DEC("Falt__71", stringXPath(OBSERVATION_VARDE_DEC_CODE_LATERALITET, "420050001",
            "7771000")),

    /** */
    KORRIGERAD_SYNSKARPA_VANSTER_INT("Falt__72", stringXPath(OBSERVATION_VARDE_INT_CODE_LATERALITET, "397535007",
            "7771000")),

    /** */
    KORRIGERAD_SYNSKARPA_VANSTER_DEC("Falt__73", stringXPath(OBSERVATION_VARDE_DEC_CODE_LATERALITET, "397535007",
            "7771000")),

    /** */
    EJ_KORRIGERAD_SYNSKARPA_BINOKULART_INT("Falt__74", stringXPath(OBSERVATION_VARDE_INT_CODE_LATERALITET, "420050001",
            "51440002")),

    /** */
    EJ_KORRIGERAD_SYNSKARPA_BINOKULART_DEC("Falt__75", stringXPath(OBSERVATION_VARDE_DEC_CODE_LATERALITET, "420050001",
            "51440002")),

    /** */
    KORRIGERAD_SYNSKARPA_BINOKULART_INT("Falt__76", stringXPath(OBSERVATION_VARDE_INT_CODE_LATERALITET, "397535007",
            "51440002")),

    /** */
    KORRIGERAD_SYNSKARPA_BINOKULART_DEC("Falt__77", stringXPath(OBSERVATION_VARDE_DEC_CODE_LATERALITET, "397535007",
            "51440002")),

    /** */
    DIPLOPI_YES("Falt_78", booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "H53.2", "true")),

    /** */
    DIPLOPI_NO("Falt_79", booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "H53.2", "false")),

    /** */
    BEDOMNING_AM("Falt_108", booleanXPath(REKOMMENDATION_KORKORTSBEHORIGHET_TEMPLATE, "VAR12")),

    /** */
    BEDOMNING_A1("Falt_81", booleanXPath(REKOMMENDATION_KORKORTSBEHORIGHET_TEMPLATE, "VAR13")),

    /** */
    BEDOMNING_A2("Falt_107", booleanXPath(REKOMMENDATION_KORKORTSBEHORIGHET_TEMPLATE, "VAR14")),

    /** */
    BEDOMNING_A("Falt_82", booleanXPath(REKOMMENDATION_KORKORTSBEHORIGHET_TEMPLATE, "VAR15")),

    /** */
    BEDOMNING_B("Falt_83", booleanXPath(REKOMMENDATION_KORKORTSBEHORIGHET_TEMPLATE, "VAR16")),

    /** */
    BEDOMNING_BE("Falt_84", booleanXPath(REKOMMENDATION_KORKORTSBEHORIGHET_TEMPLATE, "VAR17")),

    /** */
    BEDOMNING_TRAKTOR("Falt_85", booleanXPath(REKOMMENDATION_KORKORTSBEHORIGHET_TEMPLATE, "VAR18")),

    /** */
    BEDOMNING_C1("Falt_80", booleanXPath(REKOMMENDATION_KORKORTSBEHORIGHET_TEMPLATE, "VAR1")),

    /** */
    BEDOMNING_C1E("Falt_109", booleanXPath(REKOMMENDATION_KORKORTSBEHORIGHET_TEMPLATE, "VAR2")),

    /** */
    BEDOMNING_C("Falt_86", booleanXPath(REKOMMENDATION_KORKORTSBEHORIGHET_TEMPLATE, "VAR3")),

    /** */
    BEDOMNING_CE("Falt_87", booleanXPath(REKOMMENDATION_KORKORTSBEHORIGHET_TEMPLATE, "VAR4")),

    /** */
    BEDOMNING_D1("Falt_110", booleanXPath(REKOMMENDATION_KORKORTSBEHORIGHET_TEMPLATE, "VAR5")),

    /** */
    BEDOMNING_D1E("Falt_111", booleanXPath(REKOMMENDATION_KORKORTSBEHORIGHET_TEMPLATE, "VAR6")),

    /** */
    BEDOMNING_D("Falt_88", booleanXPath(REKOMMENDATION_KORKORTSBEHORIGHET_TEMPLATE, "VAR7")),

    /** */
    BEDOMNING_DE("Falt_89", booleanXPath(REKOMMENDATION_KORKORTSBEHORIGHET_TEMPLATE, "VAR8")),

    /** */
    BEDOMNING_TAXI("Falt_90", booleanXPath(REKOMMENDATION_KORKORTSBEHORIGHET_TEMPLATE, "VAR9")),

    /** */
    BEDOMNING_INTE_TA_STALLNING("Falt_91", booleanXPath(REKOMMENDATION_KORKORTSBEHORIGHET_TEMPLATE, "VAR11")),

    /** */
    LAMPLIGHET_INNEHA_BEHORIGHET_TILL_KORNINGAR_OCH_ARBETSFORMER_YES("Falt_92", booleanXPath(
            REKOMMENDATION_VARDE_TEMPLATE, "REK10", "true")),

    /** */
    LAMPLIGHET_INNEHA_BEHORIGHET_TILL_KORNINGAR_OCH_ARBETSFORMER_NO("Falt_93", booleanXPath(
            REKOMMENDATION_VARDE_TEMPLATE, "REK10", "false")),

    /** */
    OVRIG_BESKRIVNING("FaltDiv6", OVRIG_BESKRIVNING_XPATH),

    /** */
    BEDOMNING_BOR_UNDERSOKAS_SPECIALIST("Falt__94", stringXPath(REKOMMENDATION_BESKRIVNING_TEMPLATE, "REK9")),

    /** */
    INTYGSDATUM("Falt__95", INTYGSDATUM_XPATH),

    /** */
    VARDINRATTNINGENS_NAMN("Falt__96", VARDINRATTNINGENS_NAMN_XPATH),

    /** */
    ADRESS_OCH_ORT("Falt__97", ADRESS_OCH_ORT_XPATH),

    /** */
    TELEFON("Falt__98", TELEFON_XPATH),

    /** */
    NAMNFORTYDLIGANDE("Falt__101", NAMNFORTYDLIGANDE_XPATH),

    /** */
    SPECIALISTKOMPETENS_BESKRVNING("Falt__102", SPECIALISTKOMPETENS_BESKRVNING_XPATH);

    private final String field;

    private final XPathExpression<?> xPath;

    private TransportToPDFMapping(String field, XPathExpression<?> xPath) {
        this.field = field;
        this.xPath = xPath;
    }

    public String getField() {
        return field;
    }

    public XPathExpression<?> getxPath() {
        return xPath;
    }

    /**
     * Utility that dumps all xPath expressions in a CSV format. Useful for exporting the xPaths to a human readable
     * format.
     * 
     * @param args
     *            Not used.
     */
    public static void main(String... args) {
        for (TransportToPDFMapping mapping : TransportToPDFMapping.values()) {
            System.out.println(mapping.getField() + ";" + mapping.getxPath().getXPathString());
        }
    }
}
