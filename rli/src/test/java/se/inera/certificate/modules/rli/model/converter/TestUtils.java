/**
 * Copyright (C) 2013 Inera AB (http://www.inera.se)
 *
 * This file is part of Inera Certificate Modules (http://code.google.com/p/inera-certificate-modules).
 *
 * Inera Certificate Modules is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Inera Certificate Modules is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.inera.certificate.modules.rli.model.converter;

import org.joda.time.DateTimeFieldType;
import org.joda.time.Partial;

public final class TestUtils {

    private TestUtils() {

    }

    public static Partial constructPartial(int year) {
        return constructPartial(year, 0, 0);
    }

    public static Partial constructPartial(int year, int month) {
        return constructPartial(year, month, 0);
    }

    public static Partial constructPartial(int year, int month, int day) {

        Partial p = new Partial();

        if (year > 0) {
            p = p.with(DateTimeFieldType.year(), year);
        }

        if (month > 0) {
            p = p.with(DateTimeFieldType.monthOfYear(), month);
        }

        if (day > 0) {
            p = p.with(DateTimeFieldType.dayOfMonth(), day);
        }

        return p;
    }
}
