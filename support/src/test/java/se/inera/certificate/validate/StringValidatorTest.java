package se.inera.certificate.validate;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

import java.util.Calendar;

import org.junit.Test;

public class StringValidatorTest {
    
    @Test
    public void testValidateStringIsNumbers() {
        final String numbers = "2011";
        final String other = "abd43";
        final String decimal = "2.2";
        final String wrongDelimiterDecimal = "2,2";

        StringValidator validator = new StringValidator();
        assertTrue(validator.validateStringIsNumber(numbers));
        assertFalse(validator.validateStringIsNumber(other));
        assertTrue(validator.validateStringIsNumber(decimal));
        assertFalse(validator.validateStringIsNumber(wrongDelimiterDecimal));
    }

    @Test
    public void testValidateStringIsYear() {
        final String nineteenEleven = "1911";
        final String twentyEleven = "2011";
        final String thirtyEleven = "3011";

        Calendar now = Calendar.getInstance();
        int thisYear = now.get(Calendar.YEAR);
        final String validThisYear = Integer.toString(thisYear);
        final String validButFuture = Integer.toString(thisYear + 1);
        
        final String eighteenHundred = "1800";
        final String nineHundred = "0900";
        final String other = "2011-12-12";
        final String inTheFuture = "5666";
        
        StringValidator validator = new StringValidator();
        assertTrue(validator.validateStringIsYear(twentyEleven));
        assertTrue(validator.validateStringIsYear(nineteenEleven));
        assertFalse(validator.validateStringIsYear(thirtyEleven));
        assertFalse(validator.validateStringIsYear(nineHundred));
        assertFalse(validator.validateStringIsYear(eighteenHundred));

        //Check that current year is valid, but next year is not
        assertTrue(validator.validateStringIsYear(validThisYear));
        assertFalse(validator.validateStringIsYear(validButFuture));

        assertFalse(validator.validateStringIsYear(inTheFuture));
        
        assertFalse(validator.validateStringIsYear(other));
    }

    @Test
    public void testValidateNullStringGenerates() {
        StringValidator validator = new StringValidator();
        assertFalse(validator.validateStringIsNumber(null));
        assertFalse(validator.validateStringIsYear(null));
    }
}
