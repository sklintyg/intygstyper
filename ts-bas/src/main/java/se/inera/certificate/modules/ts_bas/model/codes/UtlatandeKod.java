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

import se.inera.certificate.model.Kod;

/**
 * Represents the code used by this module to define the Utlåtandetyp.
 */
public enum UtlatandeKod implements CodeSystem {

    // TODO: Create specific Code representing module
    TS_BAS("TSTRK1007 (U06, V06)",
            "Läkarintyg- avseende högre körkortsbehörigheter eller taxiförarlegitimation- på begäran från Transportstyrelsen"),
    
    TS_DIABETES("TSTRK1031 (U06, V02)", "Läkarintyg diabetes avseende lämpligheten att inneha körkort m.m.");

    private static String codeSystemName = "kv_utlåtandetyp_intyg";

    private static String codeSystem = "f6fb361a-e31d-48b8-8657-99b63912dd9b";

    private static String codeSystemVersion = null;

    private String code;

    private String description;

    private UtlatandeKod(String code, String desc) {
        this.code = code;
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

    @Override
    public boolean matches(Kod kod) {
        return CodeConverter.matches(this, kod);
    }
}
