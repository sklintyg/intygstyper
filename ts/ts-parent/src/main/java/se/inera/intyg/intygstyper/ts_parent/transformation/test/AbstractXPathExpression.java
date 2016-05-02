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
