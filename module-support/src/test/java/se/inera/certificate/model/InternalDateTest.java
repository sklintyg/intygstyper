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

    @Test (expected = ModelException.class)
    public void testInvalidAsLocalDate() {
        String justText = "blörk";
        String dateButIncorrect = "2011-13-32";
        InternalDate internalDate = new InternalDate();

        internalDate.setDate(justText);
        internalDate.asLocalDate();

        internalDate.setDate(dateButIncorrect);
        internalDate.asLocalDate();
    }
}
