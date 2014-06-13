package se.inera.certificate.model.converter.util;

import org.apache.commons.lang3.CharUtils;

import se.inera.certificate.model.Id;

public final class InternalConverterUtil {
    private static final int SAMORDNINGS_DETERMINER = 6;

    private static final String PERS_ID_ROOT = "1.2.752.129.2.1.3.1";
    private static final String SAMORDNING_ROOT = "1.2.752.129.2.1.3.3";

    private InternalConverterUtil() {
    };

    public static boolean detectIfSamordningsNummer(String personNr) {
        char dateDigit = personNr.charAt(SAMORDNINGS_DETERMINER);
        return (CharUtils.toIntValue(dateDigit) >= SAMORDNINGS_DETERMINER);
    }

    public static Id createPersonId(String patientPersonnummer) {
        return (InternalConverterUtil.detectIfSamordningsNummer(patientPersonnummer)) ? new Id(SAMORDNING_ROOT, patientPersonnummer)
                : new Id(PERS_ID_ROOT, patientPersonnummer);
    }
}
