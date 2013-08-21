/**
 * Copyright (C) 2013 Inera AB (http://www.inera.se)
 *
 * This file is part of Inera Certificate Modules (http://code.google.com/p/inera-certificate-modules).
 *
 * Inera Certificate Modules is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Inera Certificate Modules is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.inera.certificate.modules.rli.model.converters;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.joda.time.DateTimeFieldType;
import org.joda.time.Partial;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Utility for converting a joda-time Partial to String and vice versa.
 * 
 * @author Niklas Pettersson, R2M
 * 
 */
public final class PartialConverter {

    private static final String DATE_STR_REGEX = "([0-9]{4})(?:\\-([0-9]{2})(?:\\-([0-9]{2}))?)?";

    private PartialConverter() {

    }

    /**
     * Converts a Partial to a string on the form yyyy(-MM(-dd)).
     * 
     * @param partial
     *            A partial with at least the year field set.
     * @return A String
     */
    public static String partialToString(Partial partial) {

        if (partial == null) {
            return null;
        }

        StringBuilder datePattern = new StringBuilder();

        // Year
        if (partial.isSupported(DateTimeFieldType.year())) {
            datePattern.append("yyyy");
        }

        // Month
        if (partial.isSupported(DateTimeFieldType.monthOfYear())) {
            datePattern.append("-MM");
        }

        // Day
        if (partial.isSupported(DateTimeFieldType.dayOfMonth())) {
            datePattern.append("-dd");
        }

        DateTimeFormatter dfmt = DateTimeFormat.forPattern(datePattern
                .toString());

        return partial.toString(dfmt);
    }

    /**
     * Converts a date String to a Partial.
     * 
     * @param dateStr
     *            A date expressed as yyyy(-MM(-dd)).
     * @return A Partial or null if the supplied string is empty.
     */
    public static Partial stringToPartial(String dateStr) {

        String trimmedDateStr = StringUtils.trimToNull(dateStr);

        if (trimmedDateStr == null) {
            return null;
        }

        int[] dateValues = extractDateValuesFromDateStr(trimmedDateStr);

        if (dateValues == null) {
            return null;
        }

        DateTimeFieldType[] dateFields;

        if (dateValues.length == 1) {
            dateFields = new DateTimeFieldType[] { DateTimeFieldType.year() };
            return new Partial(dateFields, dateValues);
        } else if (dateValues.length == 2) {
            dateFields = new DateTimeFieldType[] { DateTimeFieldType.year(),
                    DateTimeFieldType.monthOfYear() };
            return new Partial(dateFields, dateValues);
        } else if (dateValues.length == 3) {
            dateFields = new DateTimeFieldType[] { DateTimeFieldType.year(),
                    DateTimeFieldType.monthOfYear(),
                    DateTimeFieldType.dayOfMonth() };
            return new Partial(dateFields, dateValues);
        }

        return null;
    }

    private static int[] extractDateValuesFromDateStr(String dateStrs) {

        List<Integer> dateValueList = new ArrayList<Integer>();

        Pattern regEx = Pattern.compile(DATE_STR_REGEX);
        Matcher matcher = regEx.matcher(dateStrs);

        String dateValueStr;

        if (!matcher.matches()) {
            throw new IllegalArgumentException("Date input can not be parsed");
        }

        for (int i = 1; i < matcher.groupCount() + 1; i++) {
            dateValueStr = matcher.group(i);
            if (dateValueStr == null) {
                break;
            }
            dateValueList.add(NumberUtils.createInteger(dateValueStr));
        }

        int[] dateValueArr = new int[dateValueList.size()];

        for (int i = 0; i < dateValueArr.length; i++) {
            dateValueArr[i] = dateValueList.get(i);
        }

        return dateValueArr;
    }
}
