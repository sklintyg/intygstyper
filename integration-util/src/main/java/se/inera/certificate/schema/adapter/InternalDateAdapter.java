package se.inera.certificate.schema.adapter;

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
}
