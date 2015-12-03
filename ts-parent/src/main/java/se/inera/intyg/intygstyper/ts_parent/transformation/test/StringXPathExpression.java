package se.inera.intyg.intygstyper.ts_parent.transformation.test;

import javax.xml.xpath.XPathConstants;

/**
 * An xPath expression that is evaluated as an textual result.
 */
public class StringXPathExpression extends AbstractXPathExpression<String> {

    /**
     * Creates a string xPath expression from an xPath string.
     *
     * @param xPathString
     *            The xPath string resulting in a textual result.
     */
    public StringXPathExpression(String xPathString) {
        super(xPathString, XPathConstants.STRING);
    }
}
