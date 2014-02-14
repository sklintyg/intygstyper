package se.inera.certificate.modules.ts_bas.pdf.xpath;

public interface XPathExpressions {

    public static final StringXPathExpression INVANARE_ADRESS_FALT1_XPATH = new StringXPathExpression(
            "concat(p:utlatande/p:patient/p:fornamn, ' ', p:utlatande/p:patient/p:efternamn)");

    public static final StringXPathExpression INVANARE_ADRESS_FALT2_XPATH = new StringXPathExpression(
            "p:utlatande/p:patient/p:postadress");

    public static final StringXPathExpression INVANARE_ADRESS_FALT3_XPATH = new StringXPathExpression(
            "concat(p:utlatande/p:patient/p:postnummer, ' ', p:utlatande/p:patient/p:postort)");

    public static final StringXPathExpression INVANARE_PERSONNUMMER_XPATH = new StringXPathExpression(
            "translate(p:utlatande/p:patient/p:person-id/@extension, '-', '')");

    public static final BooleanXPathExpression INTYG_AVSER_C1_XPATH = new BooleanXPathExpression(
            "p:utlatande/p:intygAvser/@code = 'IAV1'");

    public static final BooleanXPathExpression INTYG_AVSER_C1E_XPATH = new BooleanXPathExpression(
            "p:utlatande/p:intygAvser/@code = 'IAV2'");

    public static final BooleanXPathExpression INTYG_AVSER_C_XPATH = new BooleanXPathExpression(
            "p:utlatande/p:intygAvser/@code = 'IAV3'");

    public static final BooleanXPathExpression INTYG_AVSER_CE_XPATH = new BooleanXPathExpression(
            "p:utlatande/p:intygAvser/@code = 'IAV4'");

    public static final BooleanXPathExpression INTYG_AVSER_D1_XPATH = new BooleanXPathExpression(
            "p:utlatande/p:intygAvser/@code = 'IAV5'");

    public static final BooleanXPathExpression INTYG_AVSER_D1E_XPATH = new BooleanXPathExpression(
            "p:utlatande/p:intygAvser/@code = 'IAV6'");

    public static final BooleanXPathExpression INTYG_AVSER_D_XPATH = new BooleanXPathExpression(
            "p:utlatande/p:intygAvser/@code = 'IAV7'");

    public static final BooleanXPathExpression INTYG_AVSER_DE_XPATH = new BooleanXPathExpression(
            "p:utlatande/p:intygAvser/@code = 'IAV8'");

    public static final BooleanXPathExpression INTYG_AVSER_TAXI_XPATH = new BooleanXPathExpression(
            "p:utlatande/p:intygAvser/@code = 'IAV9'");

    public static final BooleanXPathExpression FALT_61_XPATH = new BooleanXPathExpression(
            "p:utlatande/p:aktivitet/p:forekomst[parent::p:aktivitet/p:aktivitetskod/@code='AKT19'] = 'true'");

    public static final BooleanXPathExpression ID_KONTROLL_IDKORT_XPATH = new BooleanXPathExpression(
            "p:utlatande/p:vardkontakt/p:idKontroll/@code = 'IDK1'");

    public static final BooleanXPathExpression ID_KONTROLL_FORETAG_TJANSTEKORT_XPATH = new BooleanXPathExpression(
            "p:utlatande/p:vardkontakt/p:idKontroll/@code = 'IDK2'");

    public static final BooleanXPathExpression ID_KONTROLL_SVENSKT_KORKORT_XPATH = new BooleanXPathExpression(
            "p:utlatande/p:vardkontakt/p:idKontroll/@code = 'IDK3'");

    public static final BooleanXPathExpression ID_KONTROLL_PERSONLIG_KANNEDOM_XPATH = new BooleanXPathExpression(
            "p:utlatande/p:vardkontakt/p:idKontroll/@code = 'IDK4'");

    public static final BooleanXPathExpression ID_KONTROLL_FORSAKRAN_XPATH = new BooleanXPathExpression(
            "p:utlatande/p:vardkontakt/p:idKontroll/@code = 'IDK5'");

    public static final BooleanXPathExpression ID_KONTROLL_PASS_XPATH = new BooleanXPathExpression(
            "p:utlatande/p:vardkontakt/p:idKontroll/@code = 'IDK6'");

}
