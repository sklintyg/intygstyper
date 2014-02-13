package se.inera.certificate.modules.ts_bas.pdf.xpath;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Node;

public class BooleanXPathExpression extends AbstractXPathExpression<Boolean> {

    public BooleanXPathExpression(String xPathString) {
        super(xPathString, XPathConstants.BOOLEAN);
    }

    public BooleanXPathExpression negate() {
        return new BooleanXPathExpression(xPathString) {
            @Override
            public Boolean evaluate(XPath xPath, Node document) throws XPathExpressionException {
                return !super.evaluate(xPath, document);
            }
        };
    }
}
