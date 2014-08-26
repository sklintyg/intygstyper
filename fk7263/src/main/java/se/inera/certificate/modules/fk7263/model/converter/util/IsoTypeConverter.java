package se.inera.certificate.modules.fk7263.model.converter.util;

import se.inera.certificate.fk7263.iso.v21090.dt.v1.CD;
import se.inera.certificate.fk7263.iso.v21090.dt.v1.II;
import se.inera.certificate.fk7263.iso.v21090.dt.v1.PQ;
import se.inera.certificate.fk7263.model.v1.AktivitetKod;
import se.inera.certificate.fk7263.model.v1.ArbetsplatsKod;
import se.inera.certificate.fk7263.model.v1.HsaId;
import se.inera.certificate.fk7263.model.v1.ObservationKategoriKod;
import se.inera.certificate.fk7263.model.v1.ObservationKod;
import se.inera.certificate.fk7263.model.v1.PersonId;
import se.inera.certificate.fk7263.model.v1.ReferensKod;
import se.inera.certificate.fk7263.model.v1.SysselsattningKod;
import se.inera.certificate.fk7263.model.v1.UtlatandeId;
import se.inera.certificate.fk7263.model.v1.UtlatandeTyp;
import se.inera.certificate.fk7263.model.v1.VardkontaktKod;
import se.inera.certificate.model.Id;
import se.inera.certificate.model.Kod;
import se.inera.certificate.model.PhysicalQuantity;

/**
 * @author marced
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

    public static AktivitetKod toAktivitetKod(Kod kod) {
        return toCD(kod, new AktivitetKod());
    }

    public static ObservationKod toObservationKod(Kod kod) {
        return toCD(kod, new ObservationKod());
    }

    public static ObservationKategoriKod toObservationKategoriKod(Kod kod) {
        return toCD(kod, new ObservationKategoriKod());
    }

    public static ReferensKod toReferensKod(Kod kod) {
        return toCD(kod, new ReferensKod());
    }

    public static SysselsattningKod toSysselsattningKod(Kod kod) {
        return toCD(kod, new SysselsattningKod());
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