package se.inera.certificate.model;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

/**
 * A way of handling Dates in our internal model that allows faulty user input,
 * this is needed at this stage because of the auto save function among other things.
 * <br/><br/>
 *
 * This class contains util methods for various things such as getting a string as a {@link LocalDate} etc.
 * @author erik
 */
public class InternalDate {

    private static final String DATE_FORMAT = "[1-2][0-9]{3,3}(-((0[1-9])|(1[0-2]))(-((0[1-9])|([1-2][0-9])|(3[0-1]))))";

    /**Parser used for parsing LocalDate[s] from Strings, uses {@code ISODateTimeFormat}. */
    private static final DateTimeFormatter PARSER = ISODateTimeFormat.dateTimeParser();

    private String date;

    public InternalDate() {
    }
    
    /**
     * Constuct an {@link InternalDate} from a String.
     * @param date a String
     */
    public InternalDate(String date) {
            this.date = date;
    }

    /**
     * Constuct an {@link InternalDate} from a {@link LocalDate},
     * primarily used when converting from external to internal model.
     * @param date a {@link LocalDate}
     * @throws {@link ModelException} if null is passed 
     */
    public InternalDate(LocalDate date) {
        if (date == null) {
            throw new ModelException("Got null while creating date object");
        }
        this.date = date.toString(ISODateTimeFormat.date());
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Attempts to parse the String held to a LocalDate.
     * @return {@link LocalDate} if parsing was successful
     * @throws ModelException if parsing failed
     */
    public LocalDate asLocalDate() {
        LocalDate localDate;
        try {
            localDate = PARSER.parseLocalDate(date);
        } catch (IllegalArgumentException ie) {
            throw new ModelException(String.format("Could not parse %s to LocalDate, failed with message: %s", date, ie.getMessage()));
        }
        return localDate;
    }

    public boolean isValidDate() {
        return date.matches(DATE_FORMAT);
    }
}
