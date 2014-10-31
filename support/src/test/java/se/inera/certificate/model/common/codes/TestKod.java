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
package se.inera.certificate.model.common.codes;

import se.inera.certificate.model.Kod;

public enum TestKod implements CodeSystem {

    CODE_RED("CODE_RED", "Code Red"), CODE_BLUE("CODE_BLUE", "Code Blue"), CODE_BLACK("", "");

    public static final String CODE_SYSTEM_NAME = "ColorCodes";

    public static final String CODE_SYSTEM = "CC";

    public static final String CODE_SYSTEM_VERSION = "1.0";

    private String code;

    private String description;

    private TestKod(String code, String description) {
        this.code = code;
        this.description = description;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
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

    @Override
    public boolean matches(Kod kod) {
        return CodeConverter.matches(this, kod);
    }
}
