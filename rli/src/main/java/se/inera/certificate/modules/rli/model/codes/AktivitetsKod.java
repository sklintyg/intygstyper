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
 * Represents all the codes used by this module to define aktiviteter.
 */
public enum AktivitetsKod implements ICodeSystem {

    /** Klinisk undersökning (AV020) */
    KLINISK_UNDERSOKNING("AV020", "klinisk undersökning UNS", "KVÅ", "1.2.752.116.1.3.2.1.4"),

    /** Första undersökning (AKT13) */
    FORSTA_UNDERSOKNING("AKT13", "Första undersökning", "kv_aktiviteter_intyg", "8040b4d1-67dc-42e1-a938-de5374e9526a"),

    /** Omvårdnadsåtgärd (9632001) */
    OMVARDNADSATGARD("9632001", "Omvårdnadsåtgärd", "SNOMED-CT", "1.2.752.116.2.1.1.1");

    private String code;
    private String description;
    private String codeSystemName;
    private String codeSystem;

    private AktivitetsKod(String code, String description, String codeSystemName, String codeSystem) {
        this.code = code;
        this.description = description;
        this.codeSystemName = codeSystemName;
        this.codeSystem = codeSystem;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCode() {
        return this.code;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDescription() {
        return this.description;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCodeSystem() {
        return this.codeSystem;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCodeSystemName() {
        return this.codeSystemName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCodeSystemVersion() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean matches(Kod kod) {
        return CodeConverter.matches(this, kod);
    }
}
