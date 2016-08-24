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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.xml.xpath.*;

import org.w3c.dom.Node;

/**
 * An xPath expression that is evaluated as an date string result. The output date format can be specified.
 */
public class DateXPathExpression extends AbstractXPathExpression<String> {

    private final DateTimeFormatter dateFormat;

    /**
     * Creates a boolean xPath expression from an xPath string.
     *
     * @param xPathString
     *            The xPath string resulting in a boolean result.
     * @param dateFormat
     *            The expected output date format.
     */
    public DateXPathExpression(String xPathString, String dateFormat) {
        super(xPathString, XPathConstants.STRING);
        this.dateFormat = DateTimeFormatter.ofPattern(dateFormat);
    }

    @Override
    public String evaluate(XPath xPath, Node document) throws XPathExpressionException {
        String xmlDate = super.evaluate(xPath, document);
        if (xmlDate.contains("T")) {
            return LocalDateTime.parse(xmlDate).format(dateFormat);
        } else {
            return LocalDate.parse(xmlDate).format(dateFormat);
        }
    }
}
