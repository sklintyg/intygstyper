package se.inera.certificate.validate;

/**
 * Common utils used for validation.
 *
 * @author Gustav Norbäcker, R2M
 */
public final class ValidatorUtils {

    public static final int BASE_10 = 10;

    private ValidatorUtils() {
    }

    /**
     * Calculates the modulo 10 checksum of a numeric string (the luhn algorithm).
     *
     * @param number A numeric string (in order to support leading zeroes).
     * @return The modulo 10 checksum.
     */
    public static int calculateMod10(String number) {
        int cs = 0;
        int multiple = 2;
        for (int i = 0; i < number.length(); i++) {
            int code = Integer.parseInt(number.substring(i, i + 1));
            int pos = multiple * code;
            cs += pos % BASE_10 + pos / BASE_10;
            multiple = (multiple == 1 ? 2 : 1);
        }

        // Subtract the sum modulo 10 from 10.
        // The remainder becomes the checksum. If the remainder is 10 the
        // checksum i 0.
        return (BASE_10 - (cs % BASE_10)) % BASE_10;
    }
}
