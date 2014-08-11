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
package se.inera.certificate.modules.ts_diabetes.model.converter;

import se.inera.certificate.model.Id;
import se.inera.certificate.model.Kod;
import se.inera.certificate.model.PhysicalQuantity;
import se.inera.certificate.ts_diabetes.iso.v21090.dt.v1.CD;
import se.inera.certificate.ts_diabetes.iso.v21090.dt.v1.II;
import se.inera.certificate.ts_diabetes.iso.v21090.dt.v1.PQ;
import se.inera.certificate.ts_diabetes.model.ext.v1.BilagaKod;
import se.inera.certificate.ts_diabetes.model.ext.v1.Lateralitet;
import se.inera.certificate.ts_diabetes.model.ext.v1.Metod;
import se.inera.certificate.ts_diabetes.model.ext.v1.RelationId;
import se.inera.certificate.ts_diabetes.model.v1.AktivitetKod;
import se.inera.certificate.ts_diabetes.model.v1.ArbetsplatsKod;
import se.inera.certificate.ts_diabetes.model.v1.HsaId;
import se.inera.certificate.ts_diabetes.model.v1.IdKontrollKod;
import se.inera.certificate.ts_diabetes.model.v1.ObservationKod;
import se.inera.certificate.ts_diabetes.model.v1.PersonId;
import se.inera.certificate.ts_diabetes.model.v1.RekommendationKod;
import se.inera.certificate.ts_diabetes.model.v1.UtlatandeId;
import se.inera.certificate.ts_diabetes.model.v1.UtlatandeTyp;
import se.inera.certificate.ts_diabetes.model.v1.VardkontaktKod;

public final class IsoTypeConverter {

    private IsoTypeConverter() {
    }

    public static Id toId(II ii) {
        if (ii == null) {
            return null;
        }
        return new Id(ii.getRoot(), ii.getExtension());
    }

    public static <E extends II> E toII(Id id, E instance) {
        if (id == null) {
            return null;
        }

        instance.setRoot(id.getRoot());
        instance.setExtension(id.getExtension());
        return instance;
    }

    public static II toII(Id id) {
        return toII(id, new II());
    }

    public static HsaId toHsaId(Id id) {
        return toII(id, new HsaId());
    }

    public static PersonId toPersonId(Id id) {
        return toII(id, new PersonId());
    }

    public static ArbetsplatsKod toArbetsplatsKod(Id id) {
        return toII(id, new ArbetsplatsKod());
    }

    public static UtlatandeId toUtlatandeId(Id id) {
        return toII(id, new UtlatandeId());
    }

    public static RelationId toRelationId(Id id) {
        return toII(id, new RelationId());
    }

    public static Kod toKod(CD cd) {
        if (cd == null) {
            return null;
        }
        return new Kod(cd.getCodeSystem(), cd.getCodeSystemName(), cd.getCodeSystemVersion(), cd.getCode());
    }

    public static <E extends CD> E toCD(Kod kod, E instance) {
        if (kod == null) {
            return null;
        }
        instance.setCode(kod.getCode());
        instance.setCodeSystem(kod.getCodeSystem());
        instance.setCodeSystemName(kod.getCodeSystemName());
        instance.setCodeSystemVersion(kod.getCodeSystemVersion());
        return instance;
    }

    public static CD toCD(Kod kod) {
        return toCD(kod, new CD());
    }

    public static UtlatandeTyp toUtlatandeTyp(Kod kod) {
        return toCD(kod, new UtlatandeTyp());
    }

    public static VardkontaktKod toVardkontaktKod(Kod kod) {
        return toCD(kod, new VardkontaktKod());
    }

    public static IdKontrollKod toIdkontrollKod(Kod kod) {
        return toCD(kod, new IdKontrollKod());
    }

    public static AktivitetKod toAktivitetKod(Kod kod) {
        return toCD(kod, new AktivitetKod());
    }

    public static ObservationKod toObservationKod(Kod kod) {
        return toCD(kod, new ObservationKod());
    }

    public static RekommendationKod toRekommendationKod(Kod kod) {
        return toCD(kod, new RekommendationKod());
    }

    public static Lateralitet toLateralitetKod(Kod kod) {
        return toCD(kod, new Lateralitet());
    }

    public static Metod toMetodKod(Kod kod) {
        return toCD(kod, new Metod());
    }
    
    public static BilagaKod toBilagaKod(Kod kod) {
        return toCD(kod, new BilagaKod());
    }

    public static PhysicalQuantity toPhysicalQuantity(PQ pq) {
        if (pq == null) {
            return null;
        }

        PhysicalQuantity physicalQuantity = new PhysicalQuantity();
        physicalQuantity.setQuantity(pq.getValue());
        physicalQuantity.setUnit(pq.getUnit());
        return physicalQuantity;
    }

    public static PQ toPQ(PhysicalQuantity physicalQuantity) {
        if (physicalQuantity == null) {
            return null;
        }

        PQ pq = new PQ();
        pq.setValue(physicalQuantity.getQuantity());
        pq.setUnit(physicalQuantity.getUnit());
        return pq;
    }
}

