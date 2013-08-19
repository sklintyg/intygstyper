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
package se.inera.certificate.modules.rli.model.external.common;

/**
 * @author andreaskaltenbach
 */
public class Kod {

    private String codeSystem;
    private String codeSystemName;
    private String codeSystemVersion;
    private String code;

    public Kod() {}

    public Kod(String codeSystem, String codeSystemName, String codeSystemVersion, String code) {
        this.codeSystem = codeSystem;
        this.codeSystemName = codeSystemName;
        this.codeSystemVersion = codeSystemVersion;
        this.code = code;
    }

    public Kod(String code) {
        this.code = code;
    }

    public String getCodeSystem() {
        return codeSystem;
    }

    public void setCodeSystem(String codeSystem) {
        this.codeSystem = codeSystem;
    }

    public String getCodeSystemName() {
        return codeSystemName;
    }

    public void setCodeSystemName(String codeSystemName) {
        this.codeSystemName = codeSystemName;
    }

    public String getCodeSystemVersion() {
        return codeSystemVersion;
    }

    public void setCodeSystemVersion(String codeSystemVersion) {
        this.codeSystemVersion = codeSystemVersion;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Kod kod = (Kod) o;

        if (code != null ? !code.equals(kod.code) : kod.code != null) return false;
        if (codeSystem != null ? !codeSystem.equals(kod.codeSystem) : kod.codeSystem != null) return false;
        if (codeSystemName != null ? !codeSystemName.equals(kod.codeSystemName) : kod.codeSystemName != null)
            return false;
        if (codeSystemVersion != null ? !codeSystemVersion.equals(kod.codeSystemVersion) : kod.codeSystemVersion != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = codeSystem != null ? codeSystem.hashCode() : 0;
        result = 31 * result + (codeSystemName != null ? codeSystemName.hashCode() : 0);
        result = 31 * result + (codeSystemVersion != null ? codeSystemVersion.hashCode() : 0);
        result = 31 * result + (code != null ? code.hashCode() : 0);
        return result;
    }
}
