package se.inera.intyg.intygstyper.ts_parent.transformation.test;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Node;

/**
 * Utility that wraps an xPath expression (string) and return type. The xPath can then be valuated given a xPath engine
 * and a DOM document.
 *
 * @param <T>
 *            The return type of the evaluated xPath.
 *
 * @see XPathEvaluator
 */
public interface XPathExpression<T> {

    /**
     * Evaluates the xPath expression.
     *
     * @param xPath
     *            The xPath engine to use.
     * @param document
     *            The DOM document to evaluate against.
     * @return The result of the evaluation.
     *
     * @throws XPathExpressionException
     *             if an error occurred.
     */
    T evaluate(XPath xPath, Node document) throws XPathExpressionException;

    /**
     * Returns the textual representation of the xPath expression.
     *
     * @return The xPath as a string.
     */
    String getXPathString();
}
