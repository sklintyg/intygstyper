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

/**
 * Represents the code used by this module to define the Utlåtandetyp.
 */
public enum UtlatandeKod implements CodeSystem {

    TS_DIABETES_U06_V02("ts-diabetes", "06", "02", "Läkarintyg diabetes avseende lämpligheten att inneha körkort m.m.");

    private static final String CODE_SYSTEM_NAME = "kv_utlåtandetyp_intyg";

    private static final String CODE_SYSTEM = "f6fb361a-e31d-48b8-8657-99b63912dd9b";

    private static final String CODE_SYSTEM_VERSION = null;

    private final String code;

    private final String tsUtgava;

    private final String tsVersion;

    private final String description;

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
        return CODE_SYSTEM;
    }

    @Override
    public String getCodeSystemName() {
        return CODE_SYSTEM_NAME;
    }

    @Override
    public String getCodeSystemVersion() {
        return CODE_SYSTEM_VERSION;
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
    
    /**
     * Returns the specific version that new intygs should use when created.
     * 
     * @return the current version of utlatande.
     */
    public static UtlatandeKod getCurrentVersion() {
        return TS_DIABETES_U06_V02;
    }
}
