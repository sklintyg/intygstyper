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

import se.inera.certificate.modules.ts_parent.codes.Kodverk;
import static se.inera.certificate.modules.ts_parent.codes.Kodverk.ID_KONTROLL;

/**
 * Represents the code used by this module to define id-kontroll, Needs to be updated when proper codes are delivered..
 */
public enum IdKontrollKod  {

    /** ID-kort (IDK1). */
    ID_KORT("IDK1", "SIS-märkt ID-kort, svenskt nationellt ID-kort eller ID-kort utfärdat av Skatteverket.",
            ID_KONTROLL),

    /** SIS-märkt företagskort eller tjänstekort (IDK2). */
    FORETAG_ELLER_TJANSTEKORT("IDK2", "SIS-märkt företagskort eller tjänstekort.", ID_KONTROLL),

    /** Svenskt körkort (IDK3). */
    KORKORT("IDK3", "Svenskt körkort", ID_KONTROLL),

    /** Personlig kännedom (IDK4). */
    PERS_KANNEDOM("IDK4", "Personlig kännedom", ID_KONTROLL),

    /** Försäkran enligt 18 kap §4 (IDK5). */
    FORSAKRAN_KAP18(
            "IDK5",
            "Försäkran enligt 18 kap 4 § i Transportstyrelsens föreskrifter (TSFS 2010:125, senast ändrade genom TSFS 2013:2)",
            ID_KONTROLL),

    /** Pass (IDK6). */
    PASS(
            "IDK6",
            "Svenskt EU-pass, annat EU-pass utfärdade från och med 1 september 2006, pass utfärdat av Island, Liechtenstein, Norge eller Schweiz fron och med den 1 september 2006.",
            ID_KONTROLL);

    private final String codeSystemName;

    private final String codeSystem;

    private final String codeSystemVersion;

    private String code;

    private String description;

    private IdKontrollKod(String code, String desc, Kodverk kodverk) {
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
    
    public static IdKontrollKod fromCode(String code){
        for (IdKontrollKod idKontrollKod : IdKontrollKod.values()) {
            if(idKontrollKod.code.equals(code)){
                return idKontrollKod;
            }
        }
        throw new IllegalArgumentException();
    }

}
