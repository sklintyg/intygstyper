#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
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
package ${package}.${artifactId}.model.converter;

import iso.v21090.dt.v1.CD;
import iso.v21090.dt.v1.II;
import se.inera.certificate.common.v1.ArbetsplatsKod;
import se.inera.certificate.common.v1.HsaId;
import se.inera.certificate.common.v1.PersonId;
import se.inera.certificate.common.v1.UtlatandeId;
import se.inera.certificate.common.v1.UtlatandeTyp;
import se.inera.certificate.model.Id;
import se.inera.certificate.model.Kod;

public final class IsoTypeConverter {

    public static Id toId(II ii) {
        if (ii == null)
            return null;
        return new Id(ii.getRoot(), ii.getExtension());
    }

    public static II toII(Id id) {
        if (id == null)
            return null;

        II ii = new II();
        ii.setRoot(id.getRoot());
        ii.setExtension(id.getExtension());
        return ii;
    }

    public static HsaId toHsaId(Id id) {
        if (id == null)
            return null;

        HsaId hsaId = new HsaId();
        hsaId.setRoot(id.getRoot());
        hsaId.setExtension(id.getExtension());
        return hsaId;
    }

    public static PersonId toPersonId(Id id) {
        if (id == null)
            return null;

        PersonId personId = new PersonId();
        personId.setRoot(id.getRoot());
        personId.setExtension(id.getExtension());
        return personId;
    }

    public static ArbetsplatsKod toArbetsplatsKod(Id id) {
        if (id == null)
            return null;

        ArbetsplatsKod arbetsplatskod = new ArbetsplatsKod();
        arbetsplatskod.setRoot(id.getRoot());
        arbetsplatskod.setExtension(id.getExtension());
        return arbetsplatskod;
    }

    public static UtlatandeId toUtlatandeId(Id id) {
        if (id == null)
            return null;

        UtlatandeId utlatandeId = new UtlatandeId();
        utlatandeId.setRoot(id.getRoot());
        utlatandeId.setExtension(id.getExtension());
        return utlatandeId;
    }

    public static Kod toKod(CD cd) {
        if (cd == null)
            return null;
        return new Kod(cd.getCodeSystem(), cd.getCodeSystemName(), cd.getCodeSystemVersion(), cd.getCode());
    }

    public static CD toCD(Kod kod) {
        if (kod == null)
            return null;
        CD cd = new CD();
        cd.setCode(kod.getCode());
        cd.setCodeSystem(kod.getCodeSystem());
        cd.setCodeSystemName(kod.getCodeSystemName());
        if (kod.getCodeSystemVersion() != null) {
            cd.setCodeSystemVersion(kod.getCodeSystemVersion());
        }
        return cd;
    }

    public static UtlatandeTyp toUtlatandeTyp(Kod kod) {
        if (kod == null)
            return null;
        UtlatandeTyp utlatandeTyp = new UtlatandeTyp();
        utlatandeTyp.setCode(kod.getCode());
        utlatandeTyp.setCodeSystem(kod.getCodeSystem());
        utlatandeTyp.setCodeSystemName(kod.getCodeSystemName());
        utlatandeTyp.setCodeSystemVersion(kod.getCodeSystemVersion());

        return utlatandeTyp;
    }
}
