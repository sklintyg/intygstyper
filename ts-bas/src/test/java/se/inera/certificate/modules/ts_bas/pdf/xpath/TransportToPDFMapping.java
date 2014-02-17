package se.inera.certificate.modules.ts_bas.pdf.xpath;

import static se.inera.certificate.modules.ts_bas.pdf.xpath.XPathExpressions.*;

/**
 * Defines all xPath expressions that are needed in order to extract the information of the transport model in the way
 * the PDF is structured.
 */
public enum TransportToPDFMapping {

    /** */
    INVANARE_ADRESS_FALT1("Falt", INVANARE_ADRESS_FALT1_XPATH),

    /** */
    INVANARE_ADRESS_FALT2("Falt__1", INVANARE_ADRESS_FALT2_XPATH),

    /** */
    INVANARE_ADRESS_FALT3("Falt__2", INVANARE_ADRESS_FALT3_XPATH),

    /** */
    INVANARE_PERSONNUMMER("Falt__3", INVANARE_PERSONNUMMER_XPATH),

    /** */
    INTYG_AVSER_C1("Falt_10", booleanXPath(INTYG_AVSER_TEMPLATE, "IAV1")),

    /** */
    INTYG_AVSER_C1E("Falt_11", booleanXPath(INTYG_AVSER_TEMPLATE, "IAV2")),

    /** */
    INTYG_AVSER_C("Falt_12", booleanXPath(INTYG_AVSER_TEMPLATE, "IAV3")),

    /** */
    INTYG_AVSER_CE("Falt_13", booleanXPath(INTYG_AVSER_TEMPLATE, "IAV4")),

    /** */
    INTYG_AVSER_D1("Falt_14", booleanXPath(INTYG_AVSER_TEMPLATE, "IAV5")),

    /** */
    INTYG_AVSER_D1E("Falt_15", booleanXPath(INTYG_AVSER_TEMPLATE, "IAV6")),

    /** */
    INTYG_AVSER_D("Falt_16", booleanXPath(INTYG_AVSER_TEMPLATE, "IAV7")),

    /** */
    INTYG_AVSER_DE("Falt_17", booleanXPath(INTYG_AVSER_TEMPLATE, "IAV8")),

    /** */
    INTYG_AVSER_TAXI("Falt_18", booleanXPath(INTYG_AVSER_TEMPLATE, "IAV9")),

    /** */
    VARD_PA_SJUKHUS_YES("Falt_61", booleanXPath(AKTIVITET_FOREKOMST_TEMPLATE, "AKT19")),

    /** */
    VARD_PA_SJUKHUS_NO("Falt_62", booleanXPath(AKTIVITET_FOREKOMST_TEMPLATE, "AKT19").negate()),

    /** */
    ID_KONTROLL_IDKORT("Falt_66", booleanXPath(ID_KONTROLL_TEMPLATE, "IDK1")),

    /** */
    ID_KONTROLL_FORETAG_TJANSTEKORT("Falt_67", booleanXPath(ID_KONTROLL_TEMPLATE, "IDK2")),

    /** */
    ID_KONTROLL_SVENSKT_KORKORT("Falt_68", booleanXPath(ID_KONTROLL_TEMPLATE, "IDK3")),

    /** */
    ID_KONTROLL_PERSONLIG_KANNEDOM("Falt_69", booleanXPath(ID_KONTROLL_TEMPLATE, "IDK4")),

    /** */
    ID_KONTROLL_FORSAKRAN("Falt_70", booleanXPath(ID_KONTROLL_TEMPLATE, "IDK5")),

    /** */
    ID_KONTROLL_PASS("Falt_71", booleanXPath(ID_KONTROLL_TEMPLATE, "IDK6")),

    /** */
    SYNFALTSDEFEKTER_YES("Falt_196", booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "H53.4")),

    /** */
    SYNFALTSDEFEKTER_NO("Falt_197", booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "H53.4").negate()),

    /** */
    NATTBLINDHET_YES("Falt_198", booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "H53.6")),

    /** */
    NATTBLINDHET_NO("Falt_199", booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "H53.6").negate()),

    /** */
    PROGRESIV_OGONSJUKDOM_YES("Falt_200", booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "OBS1")),

    /** */
    PROGRESIV_OGONSJUKDOM_NO("Falt_201", booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "OBS1").negate()),

    /** */
    DIPLOPI_YES("Falt_202", booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "H53.2")),

    /** */
    DIPLOPI_NO("Falt_203", booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "H53.2").negate()),

    /** */
    NYSTAGMUS_YES("Falt_204", booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "H55.9")),

    /** */
    NYSTAGMUS_NO("Falt_205", booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "H55.9").negate()),

    /** */
    EJ_KORRIGERAD_SYNSKARPA_HOGER_INT("Falt__206", stringXPath(OBSERVATION_VARDE_INT_CODE_LATERALITET, "420050001",
            "24028007")),

    /** */
    EJ_KORRIGERAD_SYNSKARPA_HOGER_DEC("Falt__207", stringXPath(OBSERVATION_VARDE_DEC_CODE_LATERALITET, "420050001",
            "24028007")),

    /** */
    KORRIGERAD_SYNSKARPA_HOGER_INT("Falt__208", stringXPath(OBSERVATION_VARDE_INT_CODE_LATERALITET, "397535007",
            "24028007")),

    /** */
    KORRIGERAD_SYNSKARPA_HOGER_DEC("Falt__209", stringXPath(OBSERVATION_VARDE_DEC_CODE_LATERALITET, "397535007",
            "24028007")),

    /** */
    EJ_KORRIGERAD_SYNSKARPA_VANSTER_INT("Falt__210", stringXPath(OBSERVATION_VARDE_INT_CODE_LATERALITET, "420050001",
            "7771000")),

    /** */
    EJ_KORRIGERAD_SYNSKARPA_VANSTER_DEC("Falt__211", stringXPath(OBSERVATION_VARDE_DEC_CODE_LATERALITET, "420050001",
            "7771000")),

    /** */
    KORRIGERAD_SYNSKARPA_VANSTER_INT("Falt__212", stringXPath(OBSERVATION_VARDE_INT_CODE_LATERALITET, "397535007",
            "7771000")),

    /** */
    KORRIGERAD_SYNSKARPA_VANSTER_DEC("Falt__213", stringXPath(OBSERVATION_VARDE_DEC_CODE_LATERALITET, "397535007",
            "7771000")),

    /** */
    EJ_KORRIGERAD_SYNSKARPA_BINOKULART_INT("Falt__214", stringXPath(OBSERVATION_VARDE_INT_CODE_LATERALITET,
            "420050001", "51440002")),

    /** */
    EJ_KORRIGERAD_SYNSKARPA_BINOKULART_DEC("Falt__215", stringXPath(OBSERVATION_VARDE_DEC_CODE_LATERALITET,
            "420050001", "51440002")),

    /** */
    KORRIGERAD_SYNSKARPA_BINOKULART_INT("Falt__216", stringXPath(OBSERVATION_VARDE_INT_CODE_LATERALITET, "397535007",
            "51440002")),

    /** */
    KORRIGERAD_SYNSKARPA_BINOKULART_DEC("Falt__217", stringXPath(OBSERVATION_VARDE_DEC_CODE_LATERALITET, "397535007",
            "51440002")),

    /** */
    KONTAKTLINSER_HOGER("Falt_218", booleanXPath(OBSERVATION_FOREKOMST_CODE_LATERALITET, "285049007", "24028007")),

    /** */
    KONTAKTLINSER_VANSTER("Falt_219", booleanXPath(OBSERVATION_FOREKOMST_CODE_LATERALITET, "285049007", "7771000")),

    /** */
    UNDERSOKNING_PLUS8_KORREKTIONSGRAD("Falt_220", booleanXPath(AKTIVITET_FOREKOMST_TEMPLATE, "AKT17"));

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
     * Utility that dumps all xPath expressions in a CSV format. Usefull for exporting the xPaths to a human readable
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
