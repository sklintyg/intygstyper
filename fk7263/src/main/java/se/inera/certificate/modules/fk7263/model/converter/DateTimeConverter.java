package se.inera.certificate.modules.fk7263.model.converter;

import org.joda.time.LocalDate;
import org.joda.time.Partial;

import se.inera.certificate.model.InternalLocalDateInterval;
import se.inera.certificate.model.LocalDateInterval;
import se.inera.certificate.model.PartialInterval;
import se.inera.certificate.model.converter.util.ConverterException;

public final class DateTimeConverter {

    private DateTimeConverter() {
    }

    /**
     * Convert a PartialInterval to LocalDateInterval.
     *
     * @param partialInterval
     *            the {@link PartialInterval} to convert
     * @return {@link LocalDateInterval}
     */
    public static LocalDateInterval toLocalDateInterval(PartialInterval partialInterval) {
        if (partialInterval != null) {
            LocalDate from = (partialInterval.getFrom() == null ? null : new LocalDate(partialInterval.getFrom()));
            LocalDate tom = (partialInterval.getTom() == null ? null : new LocalDate(partialInterval.getTom()));
            return new LocalDateInterval(from, tom);
        } else {
            return null;
        }
    }

    /**
     * Convert an InternalLocalDateInterval to PartialInterval.
     *
     * @param localDateInterval
     *            the c to convert
     * @return {@link PartialInterval}
     */
    public static PartialInterval toPartialInterval(InternalLocalDateInterval localDateInterval) {
        if (localDateInterval != null) {
            return new PartialInterval(new Partial(localDateInterval.getFrom().asLocalDate()),
                    new Partial(localDateInterval.getTom().asLocalDate()));
        } else {
            return null;
        }
    }

    /**
     * Convert a LocalDateInterval to PartialInterval.
     *
     * @param localDateInterval
     *            the {@link LocalDateInterval} to convert
     * @return {@link PartialInterval}
     */
    public static PartialInterval toPartialInterval(LocalDateInterval localDateInterval) {
        return new PartialInterval(new Partial(localDateInterval.getFrom()), new Partial(localDateInterval.getTom()));
    }

    /**
     * Convert PartialInterval to InternalLocalDateInterval.
     *
     * @param partialInterval
     *            {@link PartialInterval}
     * @return {@link InternalLocalDateInterval}
     * @throws ConverterException
     */
    public static InternalLocalDateInterval toInternalLocalDateInterval(PartialInterval partialInterval) throws ConverterException {
        if (partialInterval == null) {
            throw new ConverterException("Failed to convert PartialInterval to InternalLocalDateInterval, got null");
        } else if (partialInterval.getFrom() != null && partialInterval.getTom() != null) {
            InternalLocalDateInterval interval = new InternalLocalDateInterval(partialInterval.getFrom().toString(), partialInterval.getTom()
                    .toString());
            return interval;
        } else {
            throw new ConverterException("Failed to convert PartialInterval to InternalLocalDateInterval, one of from and tom was null");
        }
    }

}
