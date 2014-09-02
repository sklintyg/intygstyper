package se.inera.certificate.modules.ts_bas.pdf.xpath;

import static se.inera.certificate.modules.ts_bas.pdf.xpath.XPathExpressions.ADRESS_OCH_ORT_XPATH;
import static se.inera.certificate.modules.ts_bas.pdf.xpath.XPathExpressions.AKTIVITET_BESKRIVNING_TEMPLATE;
import static se.inera.certificate.modules.ts_bas.pdf.xpath.XPathExpressions.AKTIVITET_FOREKOMST_TEMPLATE;
import static se.inera.certificate.modules.ts_bas.pdf.xpath.XPathExpressions.AT_LAKARE_CHECK_XPATH;
import static se.inera.certificate.modules.ts_bas.pdf.xpath.XPathExpressions.ID_KONTROLL_TEMPLATE;
import static se.inera.certificate.modules.ts_bas.pdf.xpath.XPathExpressions.INTYGSDATUM_XPATH;
import static se.inera.certificate.modules.ts_bas.pdf.xpath.XPathExpressions.INTYG_AVSER_TEMPLATE;
import static se.inera.certificate.modules.ts_bas.pdf.xpath.XPathExpressions.INVANARE_ADRESS_FALT1_XPATH;
import static se.inera.certificate.modules.ts_bas.pdf.xpath.XPathExpressions.INVANARE_ADRESS_FALT2_XPATH;
import static se.inera.certificate.modules.ts_bas.pdf.xpath.XPathExpressions.INVANARE_ADRESS_FALT3_XPATH;
import static se.inera.certificate.modules.ts_bas.pdf.xpath.XPathExpressions.INVANARE_PERSONNUMMER_XPATH;
import static se.inera.certificate.modules.ts_bas.pdf.xpath.XPathExpressions.NAMNFORTYDLIGANDE_XPATH;
import static se.inera.certificate.modules.ts_bas.pdf.xpath.XPathExpressions.OBSERVATION_BESKRIVNING_TEMPLATE;
import static se.inera.certificate.modules.ts_bas.pdf.xpath.XPathExpressions.OBSERVATION_FOREKOMST_CODE_LATERALITET;
import static se.inera.certificate.modules.ts_bas.pdf.xpath.XPathExpressions.OBSERVATION_FOREKOMST_TEMPLATE;
import static se.inera.certificate.modules.ts_bas.pdf.xpath.XPathExpressions.OBSERVATION_VARDE_DEC_CODE_LATERALITET;
import static se.inera.certificate.modules.ts_bas.pdf.xpath.XPathExpressions.OBSERVATION_VARDE_INT_CODE_LATERALITET;
import static se.inera.certificate.modules.ts_bas.pdf.xpath.XPathExpressions.OVRIG_BESKRIVNING_XPATH;
import static se.inera.certificate.modules.ts_bas.pdf.xpath.XPathExpressions.REKOMMENDATION_BESKRIVNING_TEMPLATE;
import static se.inera.certificate.modules.ts_bas.pdf.xpath.XPathExpressions.REKOMMENDATION_VARDE_TEMPLATE;
import static se.inera.certificate.modules.ts_bas.pdf.xpath.XPathExpressions.SPECIALISTKOMPETENS_CHECK_XPATH;
import static se.inera.certificate.modules.ts_bas.pdf.xpath.XPathExpressions.ST_LAKARE_CHECK_XPATH;
import static se.inera.certificate.modules.ts_bas.pdf.xpath.XPathExpressions.TELEFON_XPATH;
import static se.inera.certificate.modules.ts_bas.pdf.xpath.XPathExpressions.TS_UTGAVA_XPATH;
import static se.inera.certificate.modules.ts_bas.pdf.xpath.XPathExpressions.TS_VERSION_XPATH;
import static se.inera.certificate.modules.ts_bas.pdf.xpath.XPathExpressions.VARDINRATTNINGENS_NAMN_XPATH;
import static se.inera.certificate.modules.ts_bas.pdf.xpath.XPathExpressions.VARD_PA_SJUKHUS_TID_XPATH;
import static se.inera.certificate.modules.ts_bas.pdf.xpath.XPathExpressions.VARD_PA_SJUKHUS_VARDINRATTNING_XPATH;
import static se.inera.certificate.modules.ts_bas.pdf.xpath.XPathExpressions.booleanXPath;
import static se.inera.certificate.modules.ts_bas.pdf.xpath.XPathExpressions.stringXPath;

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
    SYNFALTSDEFEKTER_YES("Falt_196", booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "H53.4", "true")),

    /** */
    SYNFALTSDEFEKTER_NO("Falt_197", booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "H53.4", "false")),

    /** */
    NATTBLINDHET_YES("Falt_198", booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "H53.6", "true")),

    /** */
    NATTBLINDHET_NO("Falt_199", booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "H53.6", "false")),

    /** */
    PROGRESIV_OGONSJUKDOM_YES("Falt_200", booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "OBS1", "true")),

    /** */
    PROGRESIV_OGONSJUKDOM_NO("Falt_201", booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "OBS1", "false")),

    /** */
    DIPLOPI_YES("Falt_202", booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "H53.2", "true")),

    /** */
    DIPLOPI_NO("Falt_203", booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "H53.2", "false")),

    /** */
    NYSTAGMUS_YES("Falt_204", booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "H55.9", "true")),

    /** */
    NYSTAGMUS_NO("Falt_205", booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "H55.9", "false")),

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
    UNDERSOKNING_PLUS8_KORREKTIONSGRAD("Falt_220", booleanXPath(AKTIVITET_FOREKOMST_TEMPLATE, "AKT17", "true")),

    /** */
    ANFALL_BALANSRUBBNING_YRSEL_YES("Falt_5", booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "OBS2", "true")),

    /** */
    ANFALL_BALANSRUBBNING_YRSEL_NO("Falt_6", booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "OBS2", "false")),

    /** */
    SVARIGHET_SAMTAL_4M_YES("Falt_7", booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "OBS3", "true")),

    /** */
    SVARIGHET_SAMTAL_4M_NO("Falt_8", booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "OBS3", "false")),

    /** */
    FORSAMRAD_RORLIGHET_FRAMFORA_FORDON_YES("Falt_20", booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "OBS4", "true")),

    /** */
    FORSAMRAD_RORLIGHET_FRAMFORA_FORDON_NO("Falt_21", booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "OBS4", "false")),

    /** */
    FORSAMRAD_RORLIGHET_FRAMFORA_FORDON_BESKRIVNING("FaltDiv", stringXPath(OBSERVATION_BESKRIVNING_TEMPLATE, "OBS4")),

    /** */
    FORSAMRAD_RORLIGHET_HJALPA_PASSAGERARE_YES("Falt_22", booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "OBS5", "true")),

    /** */
    FORSAMRAD_RORLIGHET_HJALPA_PASSAGERARE_NO("Falt_23", booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "OBS5", "false")),

    /** */
    HJART_KARLSJUKDOM_TRAFIKSAKERHETSRISK_YES("Falt_24", booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "OBS6", "true")),

    /** */
    HJART_KARLSJUKDOM_TRAFIKSAKERHETSRISK_NO("Falt_25", booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "OBS6", "false")),

    /** */
    TECKEN_PA_HJARNSKADA_YES("Falt_26", booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "OBS8", "true")),

    /** */
    TECKEN_PA_HJARNSKADA_NO("Falt_27", booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "OBS8", "false")),

    /** */
    RISKFAKTORER_STROKE_YES("Falt_28", booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "OBS7", "true")),

    /** */
    RISKFAKTORER_STROKE_NO("Falt_29", booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "OBS7", "false")),

    /** */
    RISKFAKTORER_STROKE_BESKRVNING("FaltDiv1", stringXPath(OBSERVATION_BESKRIVNING_TEMPLATE, "OBS7")),

    /** */
    HAR_DIABETES_YES("Falt_30", booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "73211009", "true")),

    /** */
    HAR_DIABETES_NO("Falt_31", booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "73211009", "false")),

    /** */
    DIABETES_TYP_1("Falt_32", booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "E10", "true")),

    /** */
    DIABETES_TYP_2("Falt_33", booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "E11", "true")),

    /** */
    DIABETIKER_KOSTBEHANDLING("Falt_34", booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "OBS9", "true")),

    /** */
    DIABETIKER_TABLETTBEHANDLING("Falt_35", booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "170746002", "true")),

    /** */
    DIABETIKER_INSULINBEHANDLING("Falt_36", booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "170747006", "true")),

    /** */
    TECKEN_PA_NEUROLOGISK_SJUKDOM_YES("Falt_37", booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "407624006", "true")),

    /** */
    TECKEN_PA_NEUROLOGISK_SJUKDOM_NO("Falt_38", booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "407624006", "false")),

    /** */
    MEDVETANDESTORNING_YES("Falt_39", booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "G40.9", "true")),

    /** */
    MEDVETANDESTORNING_NO("Falt_40", booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "G40.9", "false")),

    /** */
    MEDVETANDESTORNING_BESKRIVNING("FaltDiv2", stringXPath(OBSERVATION_BESKRIVNING_TEMPLATE, "G40.9")),

    /** */
    NEDSATT_NJURFUNKTION_TRAFIKSAKERHETSRISK_YES("Falt_41", booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "OBS11",
            "true")),

    /** */
    NEDSATT_NJURFUNKTION_TRAFIKSAKERHETSRISK_NO("Falt_42", booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "OBS11",
            "false")),

    /** */
    SVIKTANDE_KOGNITIV_FUNKTION_YES("Falt_43", booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "OBS12", "true")),

    /** */
    SVIKTANDE_KOGNITIV_FUNKTION_NO("Falt_44", booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "OBS12", "false")),

    /** */
    SOMN_VAKENHETSSTORNING_YES("Falt_45", booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "OBS13", "true")),

    /** */
    SOMN_VAKENHETSSTORNING_NO("Falt_46", booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "OBS13", "false")),

    /** */
    TECKEN_PA_MISSBRUK_YES("Falt_47", booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "OBS14", "true")),

    /** */
    TECKEN_PA_MISSBRUK_NO("Falt_48", booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "OBS14", "false")),

    /** */
    VARDINSATS_MISSBRUK_BEROENDE_YES("Falt_49", booleanXPath(AKTIVITET_FOREKOMST_TEMPLATE, "AKT15", "true")),

    /** */
    VARDINSATS_MISSBRUK_BEROENDE_NO("Falt_50", booleanXPath(AKTIVITET_FOREKOMST_TEMPLATE, "AKT15", "false")),

    /** */
    BEHOV_AV_PROVTAGNING_MISSBRUK_YES("Falt_51", booleanXPath(AKTIVITET_FOREKOMST_TEMPLATE, "AKT14", "true")),

    /** */
    BEHOV_AV_PROVTAGNING_MISSBRUK_NO("Falt_52", booleanXPath(AKTIVITET_FOREKOMST_TEMPLATE, "AKT14", "false")),

    /** */
    LAKEMEDELSANVANDNING_TRAFIKSAKERHETSRISK_YES("Falt_53", booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "OBS15",
            "true")),

    /** */
    LAKEMEDELSANVANDNING_TRAFIKSAKERHETSRISK_NO("Falt_54", booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "OBS15",
            "false")),

    /** */
    LAKEMEDELSANVANDNING_TRAFIKSAKERHETSRISK_BESKRIVNING("FaltDiv3", stringXPath(OBSERVATION_BESKRIVNING_TEMPLATE,
            "OBS15")),

    /** */
    PSYKISK_SJUKDOM_YES("Falt_55", booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "OBS16", "true")),

    /** */
    PSYKISK_SJUKDOM_NO("Falt_56", booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "OBS16", "false")),

    /** */
    PSYKISK_UTVECKLINGSSTORNING_YES("Falt_57", booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "129104009", "true")),

    /** */
    PSYKISK_UTVECKLINGSSTORNING_NO("Falt_58", booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "129104009", "false")),

    /** */
    ADHD_DAMP_MM_YES("Falt_59", booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "OBS17", "true")),

    /** */
    ADHD_DAMP_MM_NO("Falt_60", booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "OBS17", "false")),

    /** */
    VARD_PA_SJUKHUS_YES("Falt_61", booleanXPath(AKTIVITET_FOREKOMST_TEMPLATE, "AKT19", "true")),

    /** */
    VARD_PA_SJUKHUS_NO("Falt_62", booleanXPath(AKTIVITET_FOREKOMST_TEMPLATE, "AKT19", "false")),

    /** */
    VARD_PA_SJUKHUS_TID("Falt__84", VARD_PA_SJUKHUS_TID_XPATH),

    /** */
    VARD_PA_SJUKHUS_VARDINRATTNING("Falt__85", VARD_PA_SJUKHUS_VARDINRATTNING_XPATH),

    /** */
    VARD_PA_SJUKHUS_ANLEDNING("Falt__86", stringXPath(AKTIVITET_BESKRIVNING_TEMPLATE, "AKT19")),

    /** */
    STADIGVARANDE_MEDICINERING_YES("Falt_63", booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "OBS18", "true")),

    /** */
    STADIGVARANDE_MEDICINERING_NO("Falt_64", booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "OBS18", "false")),

    /** */
    STADIGVARANDE_MEDICINERING_BESKRIVNING("FaltDiv4", stringXPath(OBSERVATION_BESKRIVNING_TEMPLATE, "OBS18")),

    /** */
    OVRIG_BESKRIVNING("FaltDiv5", OVRIG_BESKRIVNING_XPATH),

    /** */
    BEDOMNING_C1("Falt_65", booleanXPath(REKOMMENDATION_VARDE_TEMPLATE, "VAR1")),

    /** */
    BEDOMNING_C1E("Falt_72", booleanXPath(REKOMMENDATION_VARDE_TEMPLATE, "VAR2")),

    /** */
    BEDOMNING_C("Falt_73", booleanXPath(REKOMMENDATION_VARDE_TEMPLATE, "VAR3")),

    /** */
    BEDOMNING_CE("Falt_74", booleanXPath(REKOMMENDATION_VARDE_TEMPLATE, "VAR4")),

    /** */
    BEDOMNING_D1("Falt_75", booleanXPath(REKOMMENDATION_VARDE_TEMPLATE, "VAR5")),

    /** */
    BEDOMNING_D1E("Falt_76", booleanXPath(REKOMMENDATION_VARDE_TEMPLATE, "VAR6")),

    /** */
    BEDOMNING_D("Falt_77", booleanXPath(REKOMMENDATION_VARDE_TEMPLATE, "VAR7")),

    /** */
    BEDOMNING_DE("Falt_78", booleanXPath(REKOMMENDATION_VARDE_TEMPLATE, "VAR8")),

    /** */
    BEDOMNING_TAXI("Falt_79", booleanXPath(REKOMMENDATION_VARDE_TEMPLATE, "VAR9")),

    /** */
    BEDOMNING_INTE_TA_STALLNING("Falt_80", booleanXPath(REKOMMENDATION_VARDE_TEMPLATE, "VAR11")),

    /** */
    BEDOMNING_ANANT("Falt_81", booleanXPath(REKOMMENDATION_VARDE_TEMPLATE, "VAR10")),

    /** */
    BEDOMNING_BOR_UNDERSOKAS_SPECIALIST("FaltDiv6", stringXPath(REKOMMENDATION_BESKRIVNING_TEMPLATE, "REK9")),

    /** */
    INTYGSDATUM("Falt__82", INTYGSDATUM_XPATH),

    /** */
    VARDINRATTNINGENS_NAMN("Falt__83", VARDINRATTNINGENS_NAMN_XPATH),

    /** */
    ADRESS_OCH_ORT("Falt__87", ADRESS_OCH_ORT_XPATH),

    /** */
    TELEFON("Falt__88", TELEFON_XPATH),

    /** */
    NAMNFORTYDLIGANDE("Falt__90", NAMNFORTYDLIGANDE_XPATH),

    /** */
    SPECIALISTKOMPETENS_CHECK("Falt_91", SPECIALISTKOMPETENS_CHECK_XPATH),

    /** */
    // TODO Implement when specialitet is no longer an CV.
    // SPECIALISTKOMPETENS_BESKRVNING("Falt_92", SPECIALISTKOMPETENS_BESKRVNING_XPATH),

    /** */
    ST_LAKARE_CHECK("Falt_93", ST_LAKARE_CHECK_XPATH),

    /** */
    AT_LAKARE_CHECK("Falt_94", AT_LAKARE_CHECK_XPATH);

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
