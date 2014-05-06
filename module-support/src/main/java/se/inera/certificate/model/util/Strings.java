package se.inera.certificate.model.util;

import java.util.Arrays;
import java.util.List;

public final class Strings {
    private Strings() {
    }

    public static boolean isNullOrEmpty(String string) {
        return string == null || string.length() == 0;
    }

    public static boolean notNullOrEmpty(String string) {
        return !isNullOrEmpty(string);
    }

    public static String emptyToNull(String string) {
        if (isNullOrEmpty(string)) {
            return null;
        } else {
            return string;
        }
    }

    public static String join(String separator, List<String> parts) {
        StringBuilder result = new StringBuilder();
        for (String part: parts) {
            if (notNullOrEmpty(part)) {
                if (result.length() > 0) {
                    result.append(separator);
                }
                result.append(part);
            }
        }
        return result.toString();
    }

    public static String join(String separator, String...parts) {
        return join(separator, Arrays.asList(parts));
    }
}
