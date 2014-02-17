package se.inera.certificate.modules.ts_bas.pdf.xpath;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Node;

/**
 * Helper class that encapsulates an xPath engine and a DOM document on which {@link XPathExpression}s can be evaluated.
 */
public class XPathEvaluator {

    /** The xPath engine to use. */
    private final XPath xPathEngine;

    /** The DOM document to evaluate against. */
    private final Node document;

    /**
     * Creates a new xPath evaluator.
     * 
     * @param xPathEngine
     *            The xPath engine to use.
     * @param document
     *            The DOM document to evaluate against.
     */
    public XPathEvaluator(XPath xPathEngine, Node document) {
        this.xPathEngine = xPathEngine;
        this.document = document;
    }

    /**
     * Evaluates the given {@link XPathExpression} against the wrapped DOM document, producing a result of the type of
     * the expression.
     * 
     * @param expression
     *            The expression to evaluate.
     * 
     * @return The result of the expression.
     * 
     * @throws XPathExpressionException
     *             if an error occurred.
     */
    public <T> T evaluate(XPathExpression<T> expression) throws XPathExpressionException {
        return expression.evaluate(xPathEngine, document);
    }
}
