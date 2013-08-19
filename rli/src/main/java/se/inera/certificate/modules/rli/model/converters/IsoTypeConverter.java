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
package se.inera.certificate.modules.rli.model.converters;

import iso.v21090.dt.v1.CD;
import iso.v21090.dt.v1.II;
import se.inera.certificate.modules.rli.model.external.common.Id;
import se.inera.certificate.modules.rli.model.external.common.Kod;

/**
 * @author andreaskaltenbach
 */
public final class IsoTypeConverter {

    private IsoTypeConverter() {
    }

    public static Id toId(II ii) {
        if (ii == null) {
            return null;
        }
        return new Id(ii.getRoot(), ii.getExtension());
    }

    public static II toII(Id id) {
        if (id == null) {
            return null;
        }

        II ii = new II();
        ii.setRoot(id.getRoot());
        ii.setExtension(id.getExtension());
        return ii;
    }

    public static Kod toKod(CD cd) {
        if (cd == null) {
            return null;
        }
        return new Kod(cd.getCodeSystem(), cd.getCodeSystemName(), cd.getCodeSystemVersion(), cd.getCode());
    }

    public static CD toCD(Kod kod) {
        if (kod == null) {
            return null;
        }
        CD cd = new CD();
        cd.setCode(kod.getCode());
        cd.setCodeSystem(kod.getCodeSystem());
        cd.setCodeSystemName(kod.getCodeSystemName());
        cd.setCodeSystemVersion(kod.getCodeSystemVersion());
        return cd;
    }
}
