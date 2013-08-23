package se.inera.certificate.modules.rli.model.converters;

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
