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
 * Represents all the codes used by this module to define befattningar.
 */
public enum BefattningsKod implements ICodeSystem {

    /** Överläkare (201010) */
    OVERLAKARE("201010", "Överläkare"),

    /** Distriktsläkare/Specialist allmänmedicin (201011) */
    DISTRIKTSLAKARE("201011", "Distriktsläkare/Specialist allmänmedicin"),

    /** Skolläkare (201012) */
    SKOLLAKARE("201012", "Skolläkare"),

    /** Företagsläkare (201013) */
    FORETAGSLAKARE("201013", "Företagsläkare"),

    /** Specialistläkare (202010) */
    SPECIALISTLAKARE("202010", "Specialistläkare"),

    /** ST-läkare (203010) */
    LAKARE_LEG_SPECIALISTTJANSTGORING("203010", "Legitimerad läkare under specialiseringstjänstgöring (STläkare)"),

    /** Vikarierande läkare (203090) */
    LAKARE_LEG_ANNAN("203090", "Legitimerad läkare under till exempel vikariat"),

    /** AT-läkare (204010) */
    LAKARE_EJ_LEG_ALLMANTJANSTGORING("204010", "Ej legitimerad läkare under allmäntjänstgöring (AT-läkare)"),

    /** Ej legitimerad läkare (204090) */
    LAKARE_EJ_LEG_ANNAN("204090", "Ej legitimerad läkare under till exempel vikariat eller provtjänstgöring");

    private static String codeSystemName = "HSA";

    private static String codeSystem = "1.2.752.129.2.1.4.1";

    private static String codeSystemVersion = null;

    private String code;

    private String description;

    private BefattningsKod(String code, String desc) {
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
