package se.inera.certificate.modules.ts_bas.pdf.xpath;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Node;

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

    /**
     * Creates a new expression with the inverse result of its parent expression.
     * 
     * @return A new expression with the inverse result of its parent expression.
     */
    public XPathExpression<Boolean> negate() {
        return new XPathExpression<Boolean>() {
            @Override
            public Boolean evaluate(XPath xPath, Node document) throws XPathExpressionException {
                return !BooleanXPathExpression.this.evaluate(xPath, document);
            }

            @Override
            public String getXPathString() {
                String xPathString = BooleanXPathExpression.this.getXPathString();

                // Handle two common expressions in a nicer way.
                if (xPathString.endsWith("'true'")) {
                    return xPathString.substring(0, xPathString.length() - 6) + "'false'";
                } else if (xPathString.endsWith("'false'")) {
                    return xPathString.substring(0, xPathString.length() - 7) + "'true'";
                }

                // Fall back solution
                return "not(" + xPathString + ")";
            }
        };
    }
}
