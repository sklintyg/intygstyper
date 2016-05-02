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
