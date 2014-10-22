package se.inera.certificate.schema.adapter;

import java.text.DecimalFormat;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import se.inera.certificate.model.InternalDate;

public final class InternalDateAdapter {

    private InternalDateAdapter() {
    }

    /**
     * Print the string wrapped by the InternalDate object.
     *
     * @param date
     *            the InternalDate-object
     * @return the string wrapped
     */
    public static String printInternalDate(InternalDate date) {

        if (date == null) {
            return null;
        }

        return date.getDate();
    }

    /**
     * Attempt to parse a string to a LocalDate wrapped by an InternalDate, attempts several parsing strategies on the
     * string, if none are successful the bare string will be wrapped allowing for non-valid dates to be saved as well.
     *
     * @param string
     * @return InternalDate wrapping either a valid LocalDate, or an arbitrary string
     */
    public static InternalDate parseInternalDate(String string) {
        LocalDate localDate = getLocalDate(string);
        if (localDate == null) {
            return new InternalDate(string);
        } else {
            return new InternalDate(localDate);
        }
    }

    /**
     * Make an InternalDate from int values, ensures that two digits are used in all positions (i.e '09' not '9').
     * @param year
     * @param month
     * @param day
     * @return InternalDate
     */
    public static InternalDate parseInternalDate(int year, int month, int day) {
        // Build a nice datestring adding 0 to single digits etc.
        DecimalFormat df = new DecimalFormat("00");
        String dateString = String.format("%d-%s-%s", year, df.format(month), df.format(day));
        return new InternalDate(dateString);
    }

    private static LocalDate getLocalDate(String str) {
        DateTimeFormatter formatter = ISODateTimeFormat.date();
        try {
            return LocalDate.parse(str, formatter);

        } catch (Exception e) {
            formatter = ISODateTimeFormat.dateTime();
        }
        try {
            return LocalDate.parse(str, formatter);
        } catch (Exception e) {
            formatter = ISODateTimeFormat.dateHourMinuteSecondFraction();
        }
        try {
            return LocalDate.parse(str, formatter);
        } catch (Exception e) {
            return null;
        }
    }

}
