package se.inera.certificate.modules.ts_bas.pdf.xpath;

/**
 * Defines xPath expressions and templates used to create all expressions needed by Transportstyrelsen.
 */
public class XPathExpressions {

    public static final StringXPathExpression INVANARE_ADRESS_FALT1_XPATH = new StringXPathExpression(
            "concat(p:utlatande/p:patient/p:fornamn, ' ', p:utlatande/p:patient/p:efternamn)");

    public static final StringXPathExpression INVANARE_ADRESS_FALT2_XPATH = new StringXPathExpression(
            "p:utlatande/p:patient/p:postadress");

    public static final StringXPathExpression INVANARE_ADRESS_FALT3_XPATH = new StringXPathExpression(
            "concat(p:utlatande/p:patient/p:postnummer, ' ', p:utlatande/p:patient/p:postort)");

    public static final StringXPathExpression INVANARE_PERSONNUMMER_XPATH = new StringXPathExpression(
            "translate(p:utlatande/p:patient/p:person-id/@extension, '-', '')");

    public static final String INTYG_AVSER_TEMPLATE = "p:utlatande/p:intygAvser/@code = '%s'";

    public static final String AKTIVITET_FOREKOMST_TEMPLATE = "p:utlatande/p:aktivitet/p:forekomst[parent::p:aktivitet/p:aktivitetskod/@code='%s'] = 'true'";

    public static final String ID_KONTROLL_TEMPLATE = "p:utlatande/p:vardkontakt/p:idKontroll/@code = '%s'";

    public static final String OBSERVATION_FOREKOMST_TEMPLATE = "p:utlatande/p:observation/p:forekomst[parent::p:observation/p:observationskod/@code='%s'] = 'true'";

    public static final String OBSERVATION_VARDE_INT_CODE_LATERALITET = "substring-before(p:utlatande/p:observation/p:varde[(parent::p:observation/p:observationskod/@code='%s') and (parent::p:observation/p:lateralitet/@code='%s')]/@value, '.')";

    public static final String OBSERVATION_VARDE_DEC_CODE_LATERALITET = "substring-after(p:utlatande/p:observation/p:varde[(parent::p:observation/p:observationskod/@code='%s') and (parent::p:observation/p:lateralitet/@code='%s')]/@value, '.')";

    public static final String OBSERVATION_FOREKOMST_CODE_LATERALITET = "p:utlatande/p:observation/p:forekomst[(parent::p:observation/p:observationskod/@code='%s') and (parent::p:observation/p:lateralitet/@code='%s')]='true'";

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
