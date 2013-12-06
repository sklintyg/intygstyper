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
package se.inera.certificate.modules.rli.model.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.joda.time.DateTimeFieldType;
import org.joda.time.Partial;
import org.junit.Test;

public class PartialConverterTest {

    @Test
    public void testPartialToStringWithYear() {

        Partial partial = new Partial().with(DateTimeFieldType.year(), 2013);

        String partialAsString = PartialConverter.partialToString(partial);

        assertNotNull(partialAsString);
        assertEquals("2013", partialAsString);
    }

    @Test
    public void testPartialToStringWithYearAndMonth1() {

        Partial partial = new Partial().with(DateTimeFieldType.year(), 2013).with(DateTimeFieldType.monthOfYear(), 3);

        String partialAsString = PartialConverter.partialToString(partial);

        assertNotNull(partialAsString);
        assertEquals("2013-03", partialAsString);
    }

    @Test
    public void testPartialToStringWithYearAndMonth2() {

        Partial partial = new Partial().with(DateTimeFieldType.year(), 2013).with(DateTimeFieldType.monthOfYear(), 12);

        String partialAsString = PartialConverter.partialToString(partial);

        assertNotNull(partialAsString);
        assertEquals("2013-12", partialAsString);
    }

    @Test
    public void testPartialToStringWithYearAndMonthAndDay1() {

        Partial partial = new Partial().with(DateTimeFieldType.year(), 2013).with(DateTimeFieldType.monthOfYear(), 4)
                .with(DateTimeFieldType.dayOfMonth(), 5);

        String partialAsString = PartialConverter.partialToString(partial);

        assertNotNull(partialAsString);
        assertEquals("2013-04-05", partialAsString);
    }

    @Test
    public void testPartialToStringWithYearAndMonthAndDay2() {

        Partial partial = new Partial().with(DateTimeFieldType.year(), 2008).with(DateTimeFieldType.monthOfYear(), 12)
                .with(DateTimeFieldType.dayOfMonth(), 11);

        String partialAsString = PartialConverter.partialToString(partial);

        assertNotNull(partialAsString);
        assertEquals("2008-12-11", partialAsString);
    }

    @Test
    public void testStringToPartialWithNullAndEmptyString() {

        Partial partial;

        partial = PartialConverter.stringToPartial(null);
        assertNull(partial);

        partial = PartialConverter.stringToPartial("");
        assertNull(partial);

        partial = PartialConverter.stringToPartial("  ");
        assertNull(partial);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStringToPartialWithGarbage() {

        PartialConverter.stringToPartial("goobledygook");

    }

    @Test
    public void testStringToPartialWithYear() {

        String dateStr = "2013 ";

        Partial partial = PartialConverter.stringToPartial(dateStr);

        assertNotNull(partial);
        assertTrue("Year should be supported", partial.isSupported(DateTimeFieldType.year()));
    }

    @Test
    public void testStringToPartialWithYearAndMonth() {

        String dateStr = " 2015-08 ";

        Partial partial = PartialConverter.stringToPartial(dateStr);

        assertNotNull(partial);
        assertTrue("Year should be supported", partial.isSupported(DateTimeFieldType.year()));
        assertTrue("Month should be supported", partial.isSupported(DateTimeFieldType.monthOfYear()));
    }

    @Test
    public void testStringToPartialWithYearAndMonthAndDay() {

        String dateStr = "  1997-12-12 ";

        Partial partial = PartialConverter.stringToPartial(dateStr);

        assertNotNull(partial);
        assertTrue("Year should be supported", partial.isSupported(DateTimeFieldType.year()));
        assertTrue("Month should be supported", partial.isSupported(DateTimeFieldType.monthOfYear()));
        assertTrue("Month should be supported", partial.isSupported(DateTimeFieldType.dayOfMonth()));
    }

    @Test
    public void testRoundTripWithYear() {

        String inputDateStr = "2011";

        Partial partial = PartialConverter.stringToPartial(inputDateStr);

        assertNotNull(partial);

        String outputDateStr = PartialConverter.partialToString(partial);

        assertNotNull(outputDateStr);
        assertEquals("In and out does not match", inputDateStr, outputDateStr);
    }

    @Test
    public void testRoundTripWithYearMonth() {

        String inputDateStr = "2003-10";

        Partial partial = PartialConverter.stringToPartial(inputDateStr);

        assertNotNull(partial);

        String outputDateStr = PartialConverter.partialToString(partial);

        assertNotNull(outputDateStr);
        assertEquals("In and out does not match", inputDateStr, outputDateStr);
    }

    @Test
    public void testRoundTripWithYearMonthDay() {

        String inputDateStr = "2012-07-14";

        Partial partial = PartialConverter.stringToPartial(inputDateStr);

        assertNotNull(partial);

        String outputDateStr = PartialConverter.partialToString(partial);

        assertNotNull(outputDateStr);
        assertEquals("In and out does not match", inputDateStr, outputDateStr);
    }

}
