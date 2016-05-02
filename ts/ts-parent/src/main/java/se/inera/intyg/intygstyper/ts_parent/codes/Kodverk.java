/*
 * Copyright (C) 2016 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package se.inera.intyg.intygstyper.ts_parent.codes;

public enum Kodverk {

    /** SNOMED-CT (1.2.752.116.2.1.1.1). */
    SNOMED_CT("1.2.752.116.2.1.1.1", "SNOMED-CT", null),

    /** ICD-10 (1.2.752.116.1.1.1.1.3). */
    ICD_10("1.2.752.116.1.1.1.1.3", "ICD-10", null),

    /** kv_aktiviteter_intyg (8040b4d1-67dc-42e1-a938-de5374e9526a). */
    AKTIVITETER("8040b4d1-67dc-42e1-a938-de5374e9526a", "kv_aktiviteter_intyg", null),

    /** kv_observationer_intyg (335d4bed-7e1d-4f81-ae7d-b39b266ef1a3). */
    OBSERVATIONER("335d4bed-7e1d-4f81-ae7d-b39b266ef1a3", "kv_observationer_intyg", null),

    /** kv_id_kontroll (e7cc8f30-a353-4c42-b17a-a189b6876647). */
    ID_KONTROLL("e7cc8f30-a353-4c42-b17a-a189b6876647", "kv_id_kontroll", null);

    Kodverk(String codeSystem, String codeSystemName, String codeSystemVersion) {
        this.codeSystem = codeSystem;
        this.codeSystemName = codeSystemName;
        this.codeSystemVersion = codeSystemVersion;
    }

    private final String codeSystem;

    private final String codeSystemName;

    private final String codeSystemVersion;

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
