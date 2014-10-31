package se.inera.certificate.validate;

import static org.apache.commons.lang3.math.NumberUtils.isNumber;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

/**
 * Validator intended for use in draft validation,
 * performs validation of strings against various conditions.
 *
 */
public class StringValidator {

    static final String YEAR_FORMAT = "yyyy";
    static final String CENTURY = "^(19|20)\\d{2}";
    private static final String POSTALCODE_FORMAT = "\\d{3}\\s?\\d{2}";

    /**
     * Validates that a given <code>String</code> contains numbers.
     *
     * @param source
     *            the string, should not be null
     * @return true if the string is a number, false otherwise
     */
    public boolean validateStringIsNumber(String source) {
        return isNumber(source);
    }

    /**
     * Validate that a given String contains a year of the format yyyy,<br/>
     * and that the year it contains is in the general vicinity of current date i.e 19xx-20xx,<br/>
     * and that the year if otherwise valid is not set in the future.
     *
     * @param source
     *            the string, should not be null
     * @return true if the string is a valid year, false otherwise
     */
    public boolean validateStringIsYear(String source) {
        if (StringUtils.isBlank(source)) {
            return false;
        }
        if (!source.matches(CENTURY)) {
            return false;
        }
        return !isYearInFuture(source);
    }

    /**
     * Validate if a given string matches the accepted format for postal codes.
     *
     * @param source the string
     * @return true if it does, false otherwise
     */
    public boolean validateStringAsPostalCode(String source) {
        return source.matches(POSTALCODE_FORMAT);
    }

    private boolean isYearInFuture(String source) {
        Date date;
        try {
            date = DateUtils.parseDate(source, YEAR_FORMAT);
        } catch (ParseException pe) {
            return false;
        }
        Calendar now = Calendar.getInstance();
        now.get(Calendar.YEAR);
        return date.after(now.getTime());
    }
}
