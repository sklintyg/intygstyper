package se.inera.certificate.modules.ts_bas.pdf.xpath;

import static se.inera.certificate.modules.ts_bas.pdf.xpath.XPathExpressions.*;

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
    INTYG_AVSER_C1("Falt_10", INTYG_AVSER_C1_XPATH),

    /** */
    INTYG_AVSER_C1E("Falt_11", INTYG_AVSER_C1E_XPATH),

    /** */
    INTYG_AVSER_C("Falt_12", INTYG_AVSER_C_XPATH),

    /** */
    INTYG_AVSER_CE("Falt_13", INTYG_AVSER_CE_XPATH),

    /** */
    INTYG_AVSER_D1("Falt_14", INTYG_AVSER_D1_XPATH),

    /** */
    INTYG_AVSER_D1E("Falt_15", INTYG_AVSER_D1E_XPATH),

    /** */
    INTYG_AVSER_D("Falt_16", INTYG_AVSER_D_XPATH),

    /** */
    INTYG_AVSER_DE("Falt_17", INTYG_AVSER_DE_XPATH),

    /** */
    INTYG_AVSER_TAXI("Falt_18", INTYG_AVSER_TAXI_XPATH),

    /** */
    FALT_61("Falt_61", FALT_61_XPATH),

    /** */
    FALT_62("Falt_62", FALT_61_XPATH.negate()),

    /** */
    ID_KONTROLL_IDKORT("Falt_66", ID_KONTROLL_IDKORT_XPATH),

    /** */
    ID_KONTROLL_FORETAG_TJANSTEKORT("Falt_67", ID_KONTROLL_FORETAG_TJANSTEKORT_XPATH),

    /** */
    ID_KONTROLL_SVENSKT_KORKORT("Falt_68", ID_KONTROLL_SVENSKT_KORKORT_XPATH),

    /** */
    ID_KONTROLL_PERSONLIG_KANNEDOM("Falt_69", ID_KONTROLL_PERSONLIG_KANNEDOM_XPATH),

    /** */
    ID_KONTROLL_FORSAKRAN("Falt_70", ID_KONTROLL_FORSAKRAN_XPATH),

    /** */
    ID_KONTROLL_PASS("Falt_71", ID_KONTROLL_PASS_XPATH);

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
}
