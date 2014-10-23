package se.inera.certificate.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.joda.time.LocalDate;
import org.junit.Test;

public class InternalDateTest {

    @Test
    public void testInternaDateIsValid() {
        String dateString = "2011-01-01";

        InternalDate date1 = new InternalDate(dateString);
        assertTrue(date1.isValidDate());
    }

    @Test
    public void testInternalDateIsNotValid() {
        String bogusString = "blahonga";
        String yearString = "2001";

        InternalDate date = new InternalDate(bogusString);
        assertFalse(date.isValidDate());

        // Allow only yyyy-MM-dd
        InternalDate date2 = new InternalDate(yearString);
        assertFalse(date2.isValidDate());
    }

    @Test
    public void testAsLocalDate() {
        String valid = "2014-12-30";
        InternalDate internalDate = new InternalDate(valid);
        LocalDate localDate = internalDate.asLocalDate();

        assertEquals(String.format("LocalDate %s did not match %s", localDate.toString(), valid), localDate.toString(), valid);
    }

    @Test
    public void testIsDateInFuture() {
        InternalDate validNotFuture = new InternalDate("2012-12-12");
        InternalDate validOneYearInFuture = new InternalDate(LocalDate.now().plusYears(1));
        InternalDate validOneDayInFuture = new InternalDate(LocalDate.now().plusDays(1));
        InternalDate validSameDate = new InternalDate(LocalDate.now());

        InternalDate invalidDate = new InternalDate("2001-01");


        assertFalse(validNotFuture.invalidOrInFuture());
        assertTrue(validOneYearInFuture.invalidOrInFuture());
        assertTrue(validOneDayInFuture.invalidOrInFuture());
        //Same date is not in the future right?
        assertFalse(validSameDate.invalidOrInFuture());

        //Should be invalid
        assertTrue(invalidDate.invalidOrInFuture());
    }

    @Test (expected = ModelException.class)
    public void testInvalidAsLocalDate() {
        InternalDate partialDate = new InternalDate("2001-");
        partialDate.asLocalDate();

        InternalDate justText = new InternalDate("blörk");
        justText.asLocalDate();

        InternalDate dateButIncorrect = new InternalDate("2011-13-32");
        dateButIncorrect.asLocalDate();
    }

    @Test (expected = ModelException.class)
    public void testInvalidEmpty() {
        InternalDate empty = new InternalDate("");
        empty.asLocalDate();
    }
}
