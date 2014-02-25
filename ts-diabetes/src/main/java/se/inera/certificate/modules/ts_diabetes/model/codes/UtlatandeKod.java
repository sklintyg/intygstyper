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
package se.inera.certificate.modules.ts_diabetes.model.codes;

import se.inera.certificate.model.Kod;
import se.inera.certificate.modules.ts_diabetes.model.codes.UtlatandeKod;

/**
 * Represents the code used by this module to define the Utlåtandetyp.
 */
public enum UtlatandeKod implements CodeSystem {

    // TODO: Create specific Code representing module
    TS_DIABETES("TSTRK1031 (U06, V02)", "06", "02", "Läkarintyg diabetes avseende lämpligheten att inneha körkort m.m.");

    private static String codeSystemName = "kv_utlåtandetyp_intyg";

    private static String codeSystem = "f6fb361a-e31d-48b8-8657-99b63912dd9b";

    private static String codeSystemVersion = null;

    private String code;

    private final String tsUtgava;

    private final String tsVersion;

    private String description;

    private UtlatandeKod(String code, String tsUtgava, String tsVersion, String desc) {
        this.code = code;
        this.tsUtgava = tsUtgava;
        this.tsVersion = tsVersion;
        this.description = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String getCodeSystem() {
        return codeSystem;
    }

    @Override
    public String getCodeSystemName() {
        return codeSystemName;
    }

    @Override
    public String getCodeSystemVersion() {
        return codeSystemVersion;
    }

    public String getTsUtgava() {
        return tsUtgava;
    }

    public String getTsVersion() {
        return tsVersion;
    }

    @Override
    public boolean matches(Kod kod) {
        return CodeConverter.matches(this, kod);
    }

    public void assertVersion(String tsUtgava, String tsVersion) {
        if (!this.tsUtgava.equals(tsUtgava)) {
            throw new IllegalArgumentException("TS utgava doesn't match " + this.name());
        }
        if (!this.tsVersion.equals(tsVersion)) {
            throw new IllegalArgumentException("TS version doesn't match " + this.name());
        }
    }

    public static UtlatandeKod getVersionFromTSParams(String tsUtgava, String tsVersion) {
        for (UtlatandeKod utlatandeKod : values()) {
            if (utlatandeKod.tsUtgava.equals(tsUtgava) && utlatandeKod.tsVersion.equals(tsVersion)) {
                return utlatandeKod;
            }
        }

        return null;
    }
}
