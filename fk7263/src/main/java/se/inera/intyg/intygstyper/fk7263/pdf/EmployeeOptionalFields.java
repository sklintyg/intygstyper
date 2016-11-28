/*
 * Copyright (C) 2016 Inera AB (http://www.inera.se)
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

package se.inera.intyg.intygstyper.fk7263.pdf;

import java.util.List;
import java.util.stream.Stream;

/**
 * Created by marced on 18/08/16.
 */
public enum EmployeeOptionalFields {

    SMITTSKYDD("smittskydd"),
    DIAGNOS("diagnos"),
    AKTUELLT_SJUKDOMS_FORLOPP("aktuelltSjukdomsforlopp"),
    FUNKTIONSNEDSATTNING("funktionsnedsattning"),
    AKTIVITETSBEGRANSNING("aktivitetsbegransning"),
    INTYGET_BASERAS_PA("intygetBaserasPa"),
    REKOMMENDATIONER_UTOM_FORETAGSHALSOVARD("rekommendationerUtomForetagsHalsoVard"),
    BEHANDLING_ATGARD("planeradBehandling"),
    REHABILITERING("rehabilitering"),
    ARBETSFORMAGA_RELATIVT_TILL_UTOM_NUVARANDE_ARBETE("arbetsFormagaRelativtUtomNuvarandeArbete"),
    ARBETSFORMAGA("arbetsFormaga"),
    PROGNOS("prognos"),
    KONTAKT_MED_FK("fkKontakt"),
    OVRIGT("ovrigt");

    private final String value;

    EmployeeOptionalFields(final String value) {

        this.value = value;
    }

    public String value() {
        return value;
    }

    public boolean isPresent(List<String> values) {
        return values != null && values.stream().filter(s -> value.equals(s)).findAny().isPresent();

    }

    public static boolean exists(String value) {
        return Stream.of(EmployeeOptionalFields.values()).filter(s -> value.equals(s.value())).findAny().isPresent();

    }

    public static EmployeeOptionalFields fromValue(String value) {
        return Stream.of(EmployeeOptionalFields.values()).filter(s -> value.equals(s.value())).findFirst()
                .orElseThrow(() -> new IllegalArgumentException(value));
    }

    /**
     * Util method that checks if the list contains all values in the enum.
     *
     * @param values
     * @return
     */
    public static boolean containsAllValues(List<String> values) {
        return values != null && !values.isEmpty()
                && Stream.of(EmployeeOptionalFields.values()).map(EmployeeOptionalFields::value).allMatch(s -> values.contains(s));

    }

}
