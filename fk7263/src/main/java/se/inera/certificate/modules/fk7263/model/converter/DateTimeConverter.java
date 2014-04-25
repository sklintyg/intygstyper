package se.inera.certificate.modules.fk7263.model.converter;

import org.joda.time.LocalDate;
import org.joda.time.Partial;

import se.inera.certificate.model.LocalDateInterval;
import se.inera.certificate.model.PartialInterval;

public final class DateTimeConverter {

    private DateTimeConverter() {
    }

    /**
     * Convert a PartialInterval to LocalDateInterval.
     *
     * @param partialInterval the {@link PartialInterval} to convert
     * @return {@link LocalDateInterval}
     */
    public static LocalDateInterval toLocalDateInterval(PartialInterval partialInterval) {
        if (partialInterval != null) {
            return new LocalDateInterval(new LocalDate(partialInterval.getFrom()), new LocalDate(partialInterval.getTom()));
        } else {
            return null;
        }
    }

    /**
     * Convert a LocalDateInterval to PartialInterval.
     *
     * @param localDateInterval the {@link LocalDateInterval} to convert
     * @return {@link PartialInterval}
     */
    public static PartialInterval toPartialInterval(LocalDateInterval localDateInterval) {
        return new PartialInterval(new Partial(localDateInterval.getFrom()), new Partial(localDateInterval.getTom()));
    }

}
