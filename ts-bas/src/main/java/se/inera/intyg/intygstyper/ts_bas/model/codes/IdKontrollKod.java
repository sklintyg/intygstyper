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
package se.inera.intyg.intygstyper.ts_bas.model.codes;

import se.inera.intyg.intygstyper.ts_parent.codes.Kodverk;
import static se.inera.intyg.intygstyper.ts_parent.codes.Kodverk.ID_KONTROLL;

/**
 * Represents the code used by this module to define id-kontroll,
 * Needs to be updated when proper codes are delivered..
 */
public enum IdKontrollKod {

    ID_KORT("IDK_1", "SIS-märkt ID-kort, svenskt nationellt ID-kort eller ID-kort utfärdat av Skatteverket.", ID_KONTROLL),
    FORETAG_ELLER_TJANSTEKORT("IDK_2", "SIS-märkt företagskort eller tjänstekort.", ID_KONTROLL),
    KORKORT("IDK_3", "Svenskt körkort", ID_KONTROLL),
    PERS_KANNEDOM("IDK_4", "Personlig kännedom", ID_KONTROLL),
    FORSAKRAN_KAP18("IDK_5", "Försäkran enligt 18 kap 4 § i Transportstyrelsens föreskrifter (TSFS 2010:125, senast ändrade genom TSFS 2013:2)", ID_KONTROLL),
    PASS("IDK_6", "Svenskt EU-pass, annat EU-pass utfärdade från och med 1 september 2006, pass utfärdat av Island, Liechtenstein, Norge eller Schweiz från och med den 1 september 2006.", ID_KONTROLL);

    private final String codeSystemName;

    private final String codeSystem;

    private final String codeSystemVersion;

    private final String code;

    private final String description;

    IdKontrollKod(String code, String desc, Kodverk kodverk) {
        this.code = code;
        this.description = desc;
        this.codeSystemName = kodverk.getCodeSystemName();
        this.codeSystem = kodverk.getCodeSystem();
        this.codeSystemVersion = kodverk.getCodeSystemVersion();
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

}
