package se.inera.certificate.modules.ts_bas.pdf.xpath;

import javax.xml.namespace.QName;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Node;

public abstract class AbstractXPathExpression<T> implements XPathExpression<T> {

    protected final String xPathString;

    protected final QName returnType;

    protected AbstractXPathExpression(String xPathString, QName returnType) {
        this.xPathString = xPathString;
        this.returnType = returnType;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T evaluate(XPath xPath, Node document) throws XPathExpressionException {
        return (T) xPath.evaluate(xPathString, document, returnType);
    }
}
