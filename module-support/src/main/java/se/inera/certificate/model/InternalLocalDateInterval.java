package se.inera.certificate.model;

import org.joda.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * A way of handling date intervals in our internal model that allows faulty user input,
 * this is needed at this stage because of the auto save function among other things. <br/>
 * <br/>
 *
 * This class contains util methods for various things such as getting the start or end dates as {@link LocalDate}[s]
 * etc.
 *
 * @author erik
 */
public class InternalLocalDateInterval {


    private InternalDate from;
    private InternalDate tom;

    public InternalLocalDateInterval() {
    }

    /**
     * Construct an InternalLocalDateInterval from strings.
     *
     * @param from
     *            String representing start date
     * @param tom
     *            String representing end date
     * @throws ModelException if from or tom is null
     */
    public InternalLocalDateInterval(String from, String tom) {
        if (from == null || tom == null) {
            throw new ModelException("Got null while trying to create InternalLocalDateInterval");
        }
        this.from = new InternalDate(from);
        this.tom = new InternalDate(tom);
    }
    public InternalLocalDateInterval(InternalDate from, InternalDate tom) {
        if (from == null || tom == null) {
            throw new ModelException("Got null while trying to create InternalLocalDateInterval");
        }
        this.from = from;
        this.tom = tom;
    }

    public InternalDate getFrom() {
        return from;
    }

    public void setFrom(InternalDate from) {
        this.from = from;
    }

    public InternalDate getTom() {
        return tom;
    }

    public void setTom(InternalDate tom) {
        this.tom = tom;
    }

    /**
     * Attempts to parse the String held as start date to a LocalDate.
     *
     * @return {@link LocalDate} if parsing was successful
     */
    public LocalDate fromAsLocalDate() {
        if (from == null) {
            return null;
        }
        try {
            return from.asLocalDate();
        } catch (ModelException e) {
            return null;
        }
    }

    /**
     * Attempts to parse the String held as end date to a LocalDate.
     *
     * @return {@link LocalDate} if parsing was successful
     */
    public LocalDate tomAsLocalDate() {
        if (tom == null) {
            return null;
        }
        try {
            return tom.asLocalDate();
        } catch (ModelException e) {
            return null;
        }
    }

    @JsonIgnore
    public boolean isValid() {
        if (this.from == null || this.tom == null) {
            return false;
        }
        if (from.isValidDate() && tom.isValidDate()) {
            return tom.asLocalDate().isAfter(from.asLocalDate());
        } else {
            return false;
        }
    }
}
