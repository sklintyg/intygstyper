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
package se.inera.certificate.modules.ts_bas.model.codes;

import se.inera.certificate.modules.ts_bas.support.TsBasEntryPoint;

/**
 * Represents the code used by this module to define the Utlåtandetyp.
 */
public enum UtlatandeKod {

    TS_BAS_U07_V06("07", "06",
            "Läkarintyg- avseende högre körkortsbehörigheter eller taxiförarlegitimation- på begäran från Transportstyrelsen"),
    TS_BAS_U06_V06("06", "06",
            "Läkarintyg- avseende högre körkortsbehörigheter eller taxiförarlegitimation- på begäran från Transportstyrelsen"),

    // NOTE: USED FOR TESTING ONLY
    // Yes it's ugly but we really wants to test that the utgava and version handling is working as expected and its
    // extremely hard to mock an enum for a test.
    TS_BAS_OLD_KOD("old-utgava", "old-version",
            "Läkarintyg- avseende högre körkortsbehörigheter eller taxiförarlegitimation- på begäran från Transportstyrelsen");

    private static final String codeSystemName = "kv_utlåtandetyp_intyg";

    private static final String codeSystem = "f6fb361a-e31d-48b8-8657-99b63912dd9b";

    private static final String codeSystemVersion = null;

    private static final String code = TsBasEntryPoint.MODULE_ID;

    private final String tsUtgava;

    private final String tsVersion;

    private final String description;

    private final String EXTERNAL_NAME = "TSTRK1007";

    private UtlatandeKod(String tsUtgava, String tsVersion, String desc) {
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

    public String getCodeSystem() {
        return codeSystem;
    }

    public String getCodeSystemName() {
        return codeSystemName;
    }

    public String getCodeSystemVersion() {
        return codeSystemVersion;
    }

    public String getTsUtgava() {
        return tsUtgava;
    }

    public String getTsVersion() {
        return tsVersion;
    }

    public String getTypForTransportConvertion() {
        return EXTERNAL_NAME + " (U" + this.tsUtgava + ", V" + this.tsVersion + ")";
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

    /**
     * Returns the specific version that new intygs should use when created.
     *
     * @return the current version of utlatande.
     */
    public static UtlatandeKod getCurrentVersion() {
        return TS_BAS_U07_V06;
    }
}
