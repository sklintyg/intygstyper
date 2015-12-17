/*
 * Copyright (C) 2015 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package se.inera.intyg.intygstyper.fk7263.model.converter;

import org.joda.time.LocalDate;
import org.joda.time.Partial;

import se.inera.intyg.common.support.model.InternalLocalDateInterval;
import se.inera.intyg.common.support.model.LocalDateInterval;
import se.inera.intyg.common.support.model.PartialInterval;
import se.inera.intyg.common.support.model.converter.util.ConverterException;

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
