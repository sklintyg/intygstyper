package se.inera.certificate.model;

import static org.junit.Assert.assertEquals;

import org.joda.time.LocalDate;
import org.junit.Test;


public class InternalLocalDateIntervalTest {

    @Test
    public void testInternaDatesAreValid() {
        InternalDate fromString = new InternalDate("2011-01-01");
        LocalDate fromDate = new LocalDate("2011-01-01");
        InternalDate tomString = new InternalDate("2011-01-02");
        LocalDate tomDate = new LocalDate("2011-01-02");

        InternalLocalDateInterval interval = new InternalLocalDateInterval(fromString, tomString);
        assertEquals("Constructed from date did not match expected",interval.fromAsLocalDate(), fromDate);
        assertEquals("Constructed tom date did not match expected",interval.tomAsLocalDate(), tomDate);
    }
}
