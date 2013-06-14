package se.inera.certificate.modules.fk7263.model;

import static com.google.common.collect.Iterables.find;
import static se.inera.certificate.model.Aktivitetskod.OVRIGT;
import static se.inera.certificate.model.Prognosangivelse.ATERSTALLAS_DELVIS;
import static se.inera.certificate.model.Prognosangivelse.ATERSTALLAS_HELT;
import static se.inera.certificate.model.Prognosangivelse.DET_GAR_INTE_ATT_BEDOMMA;
import static se.inera.certificate.model.Prognosangivelse.INTE_ATERSTALLAS;
import static se.inera.certificate.model.Sysselsattning.ARBETSLOSHET;
import static se.inera.certificate.model.Sysselsattning.FORALDRALEDIGHET;
import static se.inera.certificate.model.Sysselsattning.NUVARANDE_ARBETE;

import com.google.common.base.Predicate;
import se.inera.certificate.model.Aktivitet;
import se.inera.certificate.model.Aktivitetskod;
import se.inera.certificate.model.Arbetsformaga;
import se.inera.certificate.model.ArbetsformagaNedsattning;
import se.inera.certificate.model.Lakarutlatande;
import se.inera.certificate.model.Nedsattningsgrad;
import se.inera.certificate.model.Prognosangivelse;
import se.inera.certificate.model.Vardenhet;

/**
 * @author andreaskaltenbach
 */
public class Fk7263Intyg extends Lakarutlatande {

    public static final String DATE_PATTERN = "yyyy-MM-dd";

    public String getNamnfortydligandeOchAdress() {
        Vardenhet enhet = getVardenhet();
        String nameAndAddress = getSkapadAv().getNamn() + "\n"
                + enhet.getNamn()
                + "\n" + enhet.getPostadress()
                + "\n" + enhet.getPostnummer()
                + " " + enhet.getPostort() + "\n"
                + enhet.getTelefonnummer();
        return nameAndAddress;
    }

    public String getSigneringsDatumAsString() {
        return getSigneringsDatum().toString(DATE_PATTERN);
    }

    public String getRekommenderarOvrigtText() {
        return getAktivitetsText(OVRIGT);
    }

    private String getAktivitetsText(Aktivitetskod aktivitetskod) {
        Aktivitet activity = getAktivitet(aktivitetskod);
        if (activity != null) {
            return activity.getBeskrivning();
        } else {
            return null;
        }
    }

    public String getAktivitetsnedsattningBeskrivning() {
        if (!getAktivitetsbegransningar().isEmpty()) {
            return getAktivitetsbegransningar().get(0).getBeskrivning();
        } else {
            return null;
        }
    }

    public String getFunktionsnedsattningBeskrivning() {
        if (!getFunktionsnedsattningar().isEmpty()) {
            return getFunktionsnedsattningar().get(0).getBeskrivning();
        } else {
            return null;
        }
    }

    public boolean isPrognosDelvisAterstallning() {
        return ATERSTALLAS_DELVIS == getPrognos();
    }

    public boolean isPrognosEjAterstallning() {
        return INTE_ATERSTALLAS == getPrognos();
    }

    public boolean isPrognosFullAterstallning() {
        return ATERSTALLAS_HELT == getPrognos();
    }

    public boolean isPrognosAterstallningGarEjBedomma() {
        return DET_GAR_INTE_ATT_BEDOMMA == getPrognos();
    }

    public String getPrognosText() {
        if (!getAktivitetsbegransningar().isEmpty()) {
            return getAktivitetsbegransningar().get(0).getArbetsformaga().getMotivering();
        } else {
            return null;
        }
    }

    private Prognosangivelse getPrognos() {
        if (!getAktivitetsbegransningar().isEmpty()) {
            return getAktivitetsbegransningar().get(0).getArbetsformaga().getPrognosangivelse();
        } else {
            return null;
        }
    }

    public ArbetsformagaNedsattning getNedsattning(final Nedsattningsgrad nedsattningsgrad) {
        if (!getAktivitetsbegransningar().isEmpty()) {
            return find(getAktivitetsbegransningar().get(0).getArbetsformaga().getArbetsformagaNedsattningar(), new Predicate<ArbetsformagaNedsattning>() {
                @Override
                public boolean apply(ArbetsformagaNedsattning arbetsformagaNedsattning) {
                    return arbetsformagaNedsattning.getNedsattningsgrad() == nedsattningsgrad;
                }
            }, null);
        } else {
            return null;
        }
    }

    public Arbetsformaga getArbetsformaga() {
        if (!getAktivitetsbegransningar().isEmpty()) {
            return getAktivitetsbegransningar().get(0).getArbetsformaga();
        } else {
            return null;
        }
    }

    public boolean isArbetsformagaIForhallandeTillArbetsloshet() {
        return getArbetsformaga() != null && getArbetsformaga().getSysselsattningar().contains(ARBETSLOSHET);
    }

    public boolean isArbetsformagaIForhallandeTillForaldraledighet() {
        return getArbetsformaga() != null && getArbetsformaga().getSysselsattningar().contains(FORALDRALEDIGHET);
    }

    public boolean isArbetsformagaIForhallandeTillNuvarandeArbete() {
        return getArbetsformaga() != null && getArbetsformaga().getSysselsattningar().contains(NUVARANDE_ARBETE);
    }

    public String getArbetsformagaText() {
        Arbetsformaga arbetsformaga = getArbetsformaga();
        if (arbetsformaga != null) {
            return arbetsformaga.getArbetsuppgift();
        }
        return null;
    }

    private String getArbetsformagaNedsattFrom(final Nedsattningsgrad nedsattningsgrad) {
        ArbetsformagaNedsattning arbetsformaga = getNedsattning(nedsattningsgrad);
        return arbetsformaga != null ? arbetsformaga.getVaraktighetFrom().toString(DATE_PATTERN) : null;
    }

    private String getArbetsformagaNedsattTom(final Nedsattningsgrad nedsattningsgrad) {
        ArbetsformagaNedsattning arbetsformaga = getNedsattning(nedsattningsgrad);
        return arbetsformaga != null ? arbetsformaga.getVaraktighetTom().toString(DATE_PATTERN) : null;
    }
}
