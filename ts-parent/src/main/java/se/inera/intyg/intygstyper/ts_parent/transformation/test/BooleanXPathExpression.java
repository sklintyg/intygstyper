package se.inera.intyg.intygstyper.ts_parent.transformation.test;

import javax.xml.xpath.XPathConstants;

/**
 * An xPath expression that is evaluated as an boolean result.
 */
public class BooleanXPathExpression extends AbstractXPathExpression<Boolean> {

    /**
     * Creates a boolean xPath expression from an xPath string.
     *
     * @param xPathString
     *            The xPath string resulting in a boolean result.
     */
    public BooleanXPathExpression(String xPathString) {
        super(xPathString, XPathConstants.BOOLEAN);
    }
}
