package se.inera.certificate.model.converter.util;

import org.apache.commons.lang3.CharUtils;

import se.inera.certificate.model.Id;

public final class InternalConverterUtil {

    private static final int SAMORDNING_MONTH_INDEX = 6;

    private static final int SAMORDNING_MONTH_VALUE_MIN = 6;

    private static final String PERS_ID_ROOT = "1.2.752.129.2.1.3.1";
    private static final String SAMORDNING_ROOT = "1.2.752.129.2.1.3.3";

    private InternalConverterUtil() {
    };

    public static boolean detectIfSamordningsNummer(String personNr) {
        char dateDigit = personNr.charAt(SAMORDNING_MONTH_INDEX);
        return (CharUtils.toIntValue(dateDigit) >= SAMORDNING_MONTH_VALUE_MIN);
    }

    public static Id createPersonId(String patientPersonnummer) {
        return (InternalConverterUtil.detectIfSamordningsNummer(patientPersonnummer)) ? new Id(SAMORDNING_ROOT, patientPersonnummer)
                : new Id(PERS_ID_ROOT, patientPersonnummer);
    }
}
