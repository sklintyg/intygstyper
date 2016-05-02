/*
 * Copyright (C) 2016 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package se.inera.intyg.intygstyper.ts_parent.transformation.test;

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
