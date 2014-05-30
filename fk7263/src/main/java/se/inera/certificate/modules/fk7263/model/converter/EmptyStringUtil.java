package se.inera.certificate.modules.fk7263.model.converter;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmptyStringUtil {

    private static final Pattern ESCAPED_STRING_PATTERN = Pattern.compile("(%\\d{2})+");
    private static final Pattern ESCAPED_PART_STRING_PATTERN = Pattern.compile("(%\\d{2})");
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("00");

    /**
     * Escapes a whitespace string with placeholder characters on the pattern (%\\d{2})+,
     * (i.e a single space becomes %32), returns the original string if input was not comprised of whitespace
     * characters.
     *
     * @param string
     *            the string to escape, must be whitespace characters in order to work
     * @return a series of escape characters, or the original string if input was not whitespace characters
     */
    public static String escapeEmptyString(String string) {
        if (string.trim().isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (char c : string.toCharArray()) {
                sb.append(escape(c));
            }
            return sb.toString();

        } else {
            return string;
        }
    }

    private static String escape(char c) {
        return "%" + DECIMAL_FORMAT.format((int) c);
    }

    private static char unescape(String s) {
        return (char) Integer.parseInt(s.substring(1));
    }

    /**
     * Attempts to remove the placeholder codes for an escaped string,
     * thus restoring it to it's pre-escaped state, returns the original string if the input does not match the
     * placeholder pattern.
     *
     * @param string
     *            the escaped string
     * @return the unescaped string, or the original if matching failed.
     */
    public static String unescapeEmptyString(String string) {
        if (string == null) {
            return null;
        }
        Matcher whole = ESCAPED_STRING_PATTERN.matcher(string);
        Matcher part = ESCAPED_PART_STRING_PATTERN.matcher(string);
        StringBuilder sb = new StringBuilder();
        if (whole.matches()) {
            while (part.find()) {
                sb.append(unescape(part.group(1)));
            }
            return sb.toString();
        } else {
            return string;
        }
    }
}
