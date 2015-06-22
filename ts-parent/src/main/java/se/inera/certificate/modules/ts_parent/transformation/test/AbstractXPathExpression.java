package se.inera.certificate.modules.ts_parent.transformation.test;

import javax.xml.namespace.QName;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Node;

/**
 * Abstract helper class that aids in creating concrete types of an {@link XPathExpression}.
 *
 * @param <T>
 *            The return type of the expression.
 */
public abstract class AbstractXPathExpression<T> implements XPathExpression<T> {

    /** The xPath expression. */
    private final String xPathString;

    /** The return type of the evaluation. */
    private final QName returnType;

    /**
     * Constructor for use by implementing classes.
     *
     * @param xPathString
     *            The xPath expression.
     * @param returnType
     *            The return type of the evaluation.
     */
    protected AbstractXPathExpression(String xPathString, QName returnType) {
        this.xPathString = xPathString;
        this.returnType = returnType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public T evaluate(XPath xPath, Node document) throws XPathExpressionException {
        return (T) xPath.evaluate(xPathString, document, returnType);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getXPathString() {
        return xPathString;
    }
}
