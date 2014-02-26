package se.inera.certificate.modules.ts_diabetes.pdf.xpath;

/**
 * Defines xPath expressions and templates used to create all expressions needed by Transportstyrelsen.
 */
public class XPathExpressions {

    public static final StringXPathExpression TS_UTGAVA_XPATH = new StringXPathExpression("p:utlatande/p:utgava");

    public static final StringXPathExpression TS_VERSION_XPATH = new StringXPathExpression("p:utlatande/p:version");

    public static final StringXPathExpression INVANARE_ADRESS_FALT1_XPATH = new StringXPathExpression(
            "concat(p:utlatande/p:patient/p:fornamn, ' ', p:utlatande/p:patient/p:efternamn)");

    public static final StringXPathExpression INVANARE_ADRESS_FALT2_XPATH = new StringXPathExpression(
            "p:utlatande/p:patient/p:postadress");

    public static final StringXPathExpression INVANARE_ADRESS_FALT3_XPATH = new StringXPathExpression(
            "concat(p:utlatande/p:patient/p:postnummer, ' ', p:utlatande/p:patient/p:postort)");

    public static final StringXPathExpression INVANARE_PERSONNUMMER_XPATH = new StringXPathExpression(
            "translate(p:utlatande/p:patient/p:person-id/@extension, '-', '')");

    public static final StringXPathExpression OVRIG_BESKRIVNING_XPATH = new StringXPathExpression(
            "p:utlatande/p:kommentar");

    public static final DateXPathExpression INTYGSDATUM_XPATH = new DateXPathExpression(
            "p:utlatande/p:signeringsdatum", "yyMMdd");

    public static final StringXPathExpression VARDINRATTNINGENS_NAMN_XPATH = new StringXPathExpression(
            "/p:utlatande/p:skapadAv/p:enhet/p:enhetsnamn");

    public static final StringXPathExpression ADRESS_OCH_ORT_XPATH = new StringXPathExpression(
            "concat (/p:utlatande/p:skapadAv/p:enhet/p:postadress, ', ', /p:utlatande/p:skapadAv/p:enhet/p:postnummer, ' ', /p:utlatande/p:skapadAv/p:enhet/p:postort)");

    public static final StringXPathExpression TELEFON_XPATH = new StringXPathExpression(
            "/p:utlatande/p:skapadAv/p:enhet/p:telefonnummer");

    public static final StringXPathExpression NAMNFORTYDLIGANDE_XPATH = new StringXPathExpression(
            "/p:utlatande/p:skapadAv/p:fullstandigtNamn");

    public static final StringXPathExpression SPECIALISTKOMPETENS_BESKRVNING_XPATH = new StringXPathExpression(
            "/p:utlatande/p:skapadAv/p:specialitet/@code");

    public static final StringXPathExpression DIABETES_AR_FOR_DIAGNOS_XPATH = new StringXPathExpression(
            "p:utlatande/p:observation/p:observationsperiod[(parent::p:observation/p:observationskod/@code='E10' or parent::p:observation/p:observationskod/@code='E11') and (parent::p:observation/p:forekomst = 'true')]/p:from");

    public static final StringXPathExpression DIABETIKER_INSULINBEHANDLING_SEDAN_XPATH = new StringXPathExpression(
            "p:utlatande/p:observation/p:observationsperiod[(parent::p:observation/p:observationskod/@code='170747006') and (parent::p:observation/p:forekomst = 'true')]/p:from");

    public static final StringXPathExpression ALLVARLIG_HYPOGLYKEMI_VAKET_TILLSTAND_DATUM_XPATH = new StringXPathExpression(
            "translate(p:utlatande/p:observation/p:observationstid[parent::p:observation/p:observationskod/@code='OBS24'], '-', '')");

    public static final String INTYG_AVSER_TEMPLATE = "p:utlatande/p:intygAvser/@code = '%s'";

    public static final String AKTIVITET_FOREKOMST_TEMPLATE = "p:utlatande/p:aktivitet/p:forekomst[parent::p:aktivitet/p:aktivitetskod/@code='%s'] = '%s'";

    public static final String ID_KONTROLL_TEMPLATE = "p:utlatande/p:vardkontakt/p:idKontroll/@code = '%s'";

    public static final String OBSERVATION_FOREKOMST_TEMPLATE = "p:utlatande/p:observation/p:forekomst[parent::p:observation/p:observationskod/@code='%s'] = '%s'";

    public static final String OBSERVATION_VARDE_INT_CODE_LATERALITET = "substring-before(p:utlatande/p:observation/p:varde[(parent::p:observation/p:observationskod/@code='%s') and (parent::p:observation/p:lateralitet/@code='%s')]/@value, '.')";

    public static final String OBSERVATION_VARDE_DEC_CODE_LATERALITET = "substring-after(p:utlatande/p:observation/p:varde[(parent::p:observation/p:observationskod/@code='%s') and (parent::p:observation/p:lateralitet/@code='%s')]/@value, '.')";

    public static final String OBSERVATION_BESKRIVNING_TEMPLATE = "p:utlatande/p:observation/p:beskrivning[(parent::p:observation/p:observationskod/@code='%s') and (parent::p:observation/p:forekomst = 'true')]";

    public static final String OBSERVATION_BILAGA_TEMPLATE = "p:utlatande/p:bilaga/p:forekomst[parent::p:bilaga/p:bilagetyp/@code='%s']='%s'";

    public static final String REKOMMENDATION_KORKORTSBEHORIGHET_TEMPLATE = "p:utlatande/p:rekommendation/p:korkortsbehorighet/@code = '%s'";

    public static final String REKOMMENDATION_VARDE_TEMPLATE = "p:utlatande/p:rekommendation/p:varde[parent::p:rekommendation/p:rekommendationskod/@code='%s']='%s'";

    public static final String REKOMMENDATION_BESKRIVNING_TEMPLATE = "p:utlatande/p:rekommendation/p:beskrivning[parent::p:rekommendation/p:rekommendationskod/@code='%s']";

    /**
     * Creates a {@link BooleanXPathExpression} from a string template and arguments.
     * 
     * @param template
     *            The string template to use.
     * @param args
     *            The additional arguments to inject in the template.
     * 
     * @return A boolean xPath expression.
     */
    public static BooleanXPathExpression booleanXPath(String template, Object... args) {
        return new BooleanXPathExpression(String.format(template, args));
    }

    /**
     * Creates a {@link StringXPathExpression} from a string template and arguments.
     * 
     * @param template
     *            The string template to use.
     * @param args
     *            The additional arguments to inject in the template.
     * 
     * @return A string xPath expression.
     */
    public static StringXPathExpression stringXPath(String template, Object... args) {
        return new StringXPathExpression(String.format(template, args));
    }
}
