package se.inera.certificate.modules.ts_diabetes.pdf.xpath;

import static se.inera.certificate.modules.ts_diabetes.pdf.xpath.XPathExpressions.*;

/**
 * Defines all xPath expressions that are needed in order to extract the information of the transport model in the way
 * the PDF is structured.
 */
public enum TransportToPDFMapping {

    /** */
    TS_UTGAVA(null, TS_UTGAVA_XPATH),

    /** */
    TS_VERSION(null, TS_VERSION_XPATH);

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
