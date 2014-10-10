package se.inera.certificate.model;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

/**
 * A way of handling date intervals in our internal model that allows faulty user input,
 * this is needed at this stage because of the auto save function among other things.
 * <br/><br/>
 *
 * This class contains util methods for various things such as getting the start or end dates as {@link LocalDate}[s] etc.
 * @author erik
 */
public class InternalLocalDateInterval {

    /**Parser used for parsing LocalDate[s] from Strings, uses {@code ISODateTimeFormat}. */
    private static final DateTimeFormatter PARSER = ISODateTimeFormat.dateTimeParser();

    private String from;
    private String tom;

    public InternalLocalDateInterval() {
    }

    /**
     * Construct an InternalLocalDateInterval from strings
     * @param from String representing start date 
     * @param tom String representing end date
     */
    public InternalLocalDateInterval(String from, String tom) {
        this.from = from;
        this.tom = tom;
    }

    public String getFrom() {
        return from;
    }
    public void setFrom(String from) {
        this.from = from;
    }
    public String getTom() {
        return tom;
    }
    public void setTom(String tom) {
        this.tom = tom;
    }

    /**
     * Attempts to parse the String held as start date to a LocalDate.
     * @return {@link LocalDate} if parsing was successful
     * @throws {@link ModelException} if parsing failed
     */
    public LocalDate fromAsLocalDate() {
        if (from == null) {
            return null;
        }
        LocalDate localDate;
        try {
            localDate = PARSER.parseLocalDate(from);
        } catch (IllegalArgumentException ie) {
            throw new ModelException(String.format("Could not parse %s to LocalDate, failed with message: %s", from, ie.getMessage()));
        } 
        return localDate;
    }

    /**
     * Attempts to parse the String held as end date to a LocalDate.
     * @return {@link LocalDate} if parsing was successful
     * @throws {@link ModelException} if parsing failed
     */
    public LocalDate tomAsLocalDate() {
        if (tom == null) {
            return null;
        }
        LocalDate localDate;
        try {
            localDate = PARSER.parseLocalDate(tom);
        }
        catch (IllegalArgumentException ie) {
            throw new ModelException(String.format("Could not parse %s to LocalDate, failed with message: %s", from, ie.getMessage()));
        } 
        return localDate;
    }
}
