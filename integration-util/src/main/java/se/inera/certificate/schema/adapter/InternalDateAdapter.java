package se.inera.certificate.schema.adapter;

import java.text.DecimalFormat;

import se.inera.certificate.model.InternalDate;

public final class InternalDateAdapter {

    private InternalDateAdapter() {
    }

    public static String printInternalDate(InternalDate date) {

        if (date == null) {
            return null;
        }

        return date.getDate();
    }

    public static InternalDate parseInternalDate(String string) {
        return new InternalDate(string);
    }

    public static InternalDate parseInternalDate(int year, int month, int day) {
        //Build a nice datestring adding 0 to single digits etc.
        DecimalFormat df = new DecimalFormat("00");
        String dateString = String.format("%d-%s-%s", year, df.format(month), df.format(day));
        return new InternalDate(dateString);
    }
}
