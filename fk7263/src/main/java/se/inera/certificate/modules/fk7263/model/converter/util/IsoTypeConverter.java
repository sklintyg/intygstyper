package se.inera.certificate.modules.fk7263.model.converter.util;

import se.inera.certificate.fk7263.iso.v21090.dt.v1.CD;
import se.inera.certificate.fk7263.iso.v21090.dt.v1.II;
import se.inera.certificate.fk7263.model.v1.ArbetsplatsKod;
import se.inera.certificate.fk7263.model.v1.HsaId;
import se.inera.certificate.fk7263.model.v1.PersonId;
import se.inera.certificate.fk7263.model.v1.UtlatandeId;
import se.inera.certificate.fk7263.model.v1.UtlatandeTyp;
import se.inera.certificate.model.Id;
import se.inera.certificate.model.Kod;

/**
 * @author marced
 */
public class IsoTypeConverter {

    private IsoTypeConverter() {
    }

    public static Id toId(II ii) {
        if (ii == null) {
            return null;
        }
        return new Id(ii.getRoot(), ii.getExtension());
    }

    public static Id toId(HsaId hsaId) {
        if (hsaId == null) {
            return null;
        }
        return new Id(hsaId.getRoot(), hsaId.getExtension());
    }

    public static Id toId(PersonId personId) {
        if (personId == null) {
            return null;
        }
        return new Id(personId.getRoot(), personId.getExtension());
    }

    public static Id toId(ArbetsplatsKod arbetsplatskod) {
        if (arbetsplatskod == null) {
            return null;
        }
        return new Id(arbetsplatskod.getRoot(), arbetsplatskod.getExtension());
    }

    public static Id toId(UtlatandeId utlatandeId) {
        if (utlatandeId == null) {
            return null;
        }
        return new Id(utlatandeId.getRoot(), utlatandeId.getExtension());
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

    public static HsaId toHsaId(Id id) {
        if (id == null) {
            return null;
        }

        HsaId hsaId = new HsaId();
        hsaId.setRoot(id.getRoot());
        hsaId.setExtension(id.getExtension());
        return hsaId;
    }

    public static PersonId toPersonId(Id id) {
        if (id == null) {
            return null;
        }

        PersonId personId = new PersonId();
        personId.setRoot(id.getRoot());
        personId.setExtension(id.getExtension());
        return personId;
    }

    public static ArbetsplatsKod toArbetsplatsKod(Id id) {
        if (id == null) {
            return null;
        }

        ArbetsplatsKod arbetsplatskod = new ArbetsplatsKod();
        arbetsplatskod.setRoot(id.getRoot());
        arbetsplatskod.setExtension(id.getExtension());
        return arbetsplatskod;
    }

    public static UtlatandeId toUtlatandeId(Id id) {
        if (id == null) {
            return null;
        }

        UtlatandeId utlatandeId = new UtlatandeId();
        utlatandeId.setRoot(id.getRoot());
        utlatandeId.setExtension(id.getExtension());
        return utlatandeId;
    }

    public static Kod toKod(CD cd) {
        if (cd == null) {
            return null;
        }
        return new Kod(cd.getCodeSystem(), cd.getCodeSystemName(), cd.getCodeSystemVersion(), cd.getCode());
    }

    public static Kod toKod(UtlatandeTyp utlatandeTyp) {
        if (utlatandeTyp == null) {
            return null;
        }
        return new Kod(utlatandeTyp.getCodeSystem(), utlatandeTyp.getCode());
    }

    public static CD toCD(Kod kod) {
        if (kod == null) {
            return null;
        }
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
        if (kod == null) {
            return null;
        }
        UtlatandeTyp utlatandeTyp = new UtlatandeTyp();
        utlatandeTyp.setCode(kod.getCode());
        utlatandeTyp.setCodeSystem(kod.getCodeSystem());

        return utlatandeTyp;
    }

}
