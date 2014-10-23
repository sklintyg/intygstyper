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
package se.inera.certificate.model.converter.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTimeFieldType;
import org.joda.time.Partial;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import se.inera.certificate.model.PartialInterval;

/**
 * Utility for converting a joda-time Partial to String and vice versa.
 *
 * @author Niklas Pettersson, R2M
 */
public final class PartialConverter {

    private static final Pattern DATE_STR_REGEX = Pattern.compile("([0-9]{4})(?:\\-([0-9]{2})(?:\\-([0-9]{2}))?)?");

    private static final DateTimeFormatter YEAR_PATTERN = DateTimeFormat.forPattern("yyyy");

    private static final DateTimeFormatter YEAR_MONTH_PATTERN = DateTimeFormat.forPattern("yyyy-MM");

    private static final DateTimeFormatter YEAR_MONTH_DAY_PATTERN = DateTimeFormat.forPattern("yyyy-MM-dd");

    private static final DateTimeFieldType[] PARTIAL_FIELDS = new DateTimeFieldType[]{DateTimeFieldType.year(),
            DateTimeFieldType.monthOfYear(), DateTimeFieldType.dayOfMonth()};

    private PartialConverter() {
    }

    /**
     * Converts a Partial to a string on the form yyyy(-MM(-dd)).
     *
     * @param partial A partial with at least the year field set.
     * @return A String
     */
    public static String partialToString(Partial partial) {
        if (partial.isSupported(DateTimeFieldType.year())) {
            if (partial.isSupported(DateTimeFieldType.monthOfYear())) {
                if (partial.isSupported(DateTimeFieldType.dayOfMonth())) {
                    return partial.toString(YEAR_MONTH_DAY_PATTERN);
                }

                return partial.toString(YEAR_MONTH_PATTERN);
            }

            return partial.toString(YEAR_PATTERN);
        }

        return null;
    }

    /**
     * Converts a date String to a Partial.
     *
     * @param dateStr A date expressed as yyyy(-MM(-dd)).
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

        DateTimeFieldType[] dateFields = new DateTimeFieldType[dateValues.length];
        System.arraycopy(PARTIAL_FIELDS, 0, dateFields, 0, dateValues.length);
        return new Partial(dateFields, dateValues);
    }

    private static int[] extractDateValuesFromDateStr(String dateStrs) {

        List<Integer> dateValueList = new ArrayList<Integer>();

        Matcher matcher = DATE_STR_REGEX.matcher(dateStrs);

        String dateValueStr;

        if (!matcher.matches()) {
            throw new IllegalArgumentException("Date input can not be parsed");
        }

        for (int i = 1; i < matcher.groupCount() + 1; i++) {
            dateValueStr = matcher.group(i);
            if (dateValueStr == null) {
                break;
            }
            dateValueList.add(Integer.parseInt(dateValueStr));
        }

        return ArrayUtils.toPrimitive(dateValueList.toArray(new Integer[0]));
    }

    public static PartialInterval toPartialInterval(String from, String tom) {
        PartialInterval result = new PartialInterval();
        result.setFrom(stringToPartial(from));
        result.setTom(stringToPartial(tom));

        return result;
    }
}
