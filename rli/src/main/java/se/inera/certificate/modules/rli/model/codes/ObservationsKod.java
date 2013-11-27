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
package se.inera.certificate.modules.rli.model.codes;

import se.inera.certificate.model.Kod;

/**
 * Represents all the codes used by this module to define observationer.
 */
public enum ObservationsKod implements ICodeSystem {

    /** Sjukdom */
    SJUKDOM("39104002", "Sjukdom"),

    /** Gravid */
    GRAVIDITET("289908002", "Gravid"),

    /** Komplikation vid graviditet */
    KOMPLIKATION_VID_GRAVIDITET("90821003", "Komplikation vid graviditet");

    private static String codeSystemName = "SNOMED-CT";

    private static String codeSystem = "1.2.752.116.2.1.1.1";

    private static String codeSystemVersion = null;

    private String code;

    private String description;

    private ObservationsKod(String code, String desc) {
        this.code = code;
        this.description = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCodeSystem() {
        return codeSystem;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCodeSystemName() {
        return codeSystemName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCodeSystemVersion() {
        return codeSystemVersion;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean matches(Kod kod) {
        return CodeConverter.matches(this, kod);
    }
}
