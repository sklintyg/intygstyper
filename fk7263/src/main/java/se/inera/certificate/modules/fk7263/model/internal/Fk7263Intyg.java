package se.inera.certificate.modules.fk7263.model.internal;

import static se.inera.certificate.model.util.Iterables.find;
import static se.inera.certificate.model.util.Strings.emptyToNull;
import static se.inera.certificate.model.util.Strings.join;

import java.util.ArrayList;
import java.util.List;

import se.inera.certificate.model.Aktivitet;
import se.inera.certificate.model.Kod;
import se.inera.certificate.model.Observation;
import se.inera.certificate.model.Prognos;
import se.inera.certificate.model.Referens;
import se.inera.certificate.model.Vardenhet;
import se.inera.certificate.model.Vardkontakt;
import se.inera.certificate.model.util.Predicate;
import se.inera.certificate.modules.fk7263.model.codes.Aktivitetskoder;
import se.inera.certificate.modules.fk7263.model.codes.ObservationsKoder;
import se.inera.certificate.modules.fk7263.model.codes.Prognoskoder;
import se.inera.certificate.modules.fk7263.model.codes.Sysselsattningskoder;
import se.inera.certificate.modules.fk7263.model.external.Fk7263Utlatande;
import se.inera.certificate.modules.fk7263.model.external.StatusMeta;

/**
 * @author andreaskaltenbach
 */
public class Fk7263Intyg extends Fk7263Utlatande {

    public static final String DATE_PATTERN = "yyyy-MM-dd";

    private List<StatusMeta> status;

    public Fk7263Intyg() {

    }

    public Fk7263Intyg(Fk7263Utlatande external) {
        this.setId(external.getId());
        this.setTyp(external.getTyp());
        this.setAktiviteter(external.getAktiviteter());
        this.setKommentars(external.getKommentars());
        this.setObservations(external.getObservations());
        this.setPatient(external.getPatient());
        this.setReferenser(external.getReferenser());
        this.setSigneringsDatum(external.getSigneringsDatum());
        this.setSkapadAv(external.getSkapadAv());
        this.setSkickatDatum(external.getSkickatDatum());
        this.setVardkontakter(external.getVardkontakter());

    }

    public String getForskrivarkodOchArbetsplatskod() {
        List<String> parts = new ArrayList<>();
        if (getSkapadAv() != null) {
            parts.add(getSkapadAv().getForskrivarkod());

            if (getSkapadAv().getVardenhet() != null) {
                parts.add(getSkapadAv().getVardenhet().getArbetsplatskod().getExtension());
            }
        }
        return emptyToNull(join(" - ", parts));
    }

    public String getNamnfortydligandeOchAdress() {
        if (getSkapadAv() == null || getSkapadAv().getVardenhet() == null) {
            return "";
        }

        Vardenhet enhet = getSkapadAv().getVardenhet();
        String nameAndAddress = getSkapadAv().getNamn() + "\n" + enhet.getNamn() + "\n" + enhet.getPostadress() + "\n" + enhet.getPostnummer() + " " + enhet.getPostort() + "\n"
                + enhet.getTelefonnummer();
        return nameAndAddress;
    }

    public String getSigneringsDatumAsString() {
        return getSigneringsDatum().toString(DATE_PATTERN);
    }

    public String getRekommenderarOvrigtText() {
        return getAktivitetsText(Aktivitetskoder.OVRIGT);
    }

    private String getAktivitetsText(Kod aktivitetskod) {
        Aktivitet activity = getAktivitet(aktivitetskod);
        if (activity != null) {
            return activity.getBeskrivning();
        } else {
            return null;
        }
    }

    public String getAktivitetsnedsattningBeskrivning() {
        Observation aktivitetsbegransning = getAktivitetsbegransning();
        return (aktivitetsbegransning != null) ? aktivitetsbegransning.getBeskrivning() : null;
    }

    public String getFunktionsnedsattningBeskrivning() {
        Observation funktionsnedsattning = getFunktionsnedsattning();
        return (funktionsnedsattning != null) ? funktionsnedsattning.getBeskrivning() : null;
    }

    public Observation getFunktionsnedsattning() {
        return findObservationByKategori(ObservationsKoder.KROPPSFUNKTIONER);
    }

    public Observation getMedicinsktTillstand() {
        return findObservationByKategori(ObservationsKoder.DIAGNOS);
    }

    public Observation getBedomtTillstand() {
        return findObservationByKod(ObservationsKoder.FORLOPP);
    }

    public Observation getAktivitetsbegransning() {
        return findObservationByKategori(ObservationsKoder.AKTIVITETER_OCH_DELAKTIGHET);
    }

    public Observation getArbetsformagaAktivitetsbegransning() {
        return findObservationByKod(ObservationsKoder.ARBETSFORMAGA);
    }

    public boolean isPrognosDelvisAterstallning() {
        return Prognoskoder.ATERSTALLAS_DELVIS.equals(getPrognosKod());
    }

    public boolean isPrognosEjAterstallning() {
        return Prognoskoder.INTE_ATERSTALLAS.equals(getPrognosKod());
    }

    public boolean isPrognosFullAterstallning() {
        return Prognoskoder.ATERSTALLAS_HELT.equals(getPrognosKod());
    }

    public boolean isPrognosAterstallningGarEjBedomma() {
        return Prognoskoder.DET_GAR_INTE_ATT_BEDOMA.equals(getPrognosKod());
    }

    public String getPrognosText() {
        Observation arbetsformaga = getArbetsformaga();
        if (arbetsformaga != null) {
            Prognos prognos = arbetsformaga.getPrognos();
            if (prognos != null) {
                return prognos.getBeskrivning();
            }
        }
        return null;
    }

    private Kod getPrognosKod() {
        Observation arbetsformaga = getArbetsformaga();
        if (arbetsformaga != null) {
            Prognos prognos = arbetsformaga.getPrognos();
            if (prognos != null) {
                return prognos.getPrognosKod();
            }
        }
        return null;
    }

    public Observation getArbetsformaga(final Double nedsattningsgrad) {
        return find(getObservationsByKod(ObservationsKoder.ARBETSFORMAGA), new Predicate<Observation>() {
            @Override
            public boolean apply(Observation arbetsformaga) {
                return arbetsformaga.getVarde() != null && !arbetsformaga.getVarde().isEmpty() && nedsattningsgrad.equals(arbetsformaga.getVarde().get(0).getQuantity());
            }
        }, null);
    }

    // Helper properties for netsattningsgrader to be included in JSON
    public Observation getNedsattning25percent() {
        return getArbetsformaga(75.0);
    }

    public Observation getNedsattning50percent() {
        return getArbetsformaga(50.0);
    }

    public Observation getNedsattning75percent() {
        return getArbetsformaga(25.0);
    }

    public Observation getNedsattning100percent() {
        return getArbetsformaga(0.0);
    }

    public boolean isArbetsformagaIForhallandeTillArbetsloshet() {
        return containsSysselsattningKod(Sysselsattningskoder.ARBETSLOSHET);
    }

    public boolean isArbetsformagaIForhallandeTillForaldraledighet() {
        return containsSysselsattningKod(Sysselsattningskoder.MAMMALEDIG) || containsSysselsattningKod(Sysselsattningskoder.PAPPALEDIG);
    }

    public boolean isArbetsformagaIForhallandeTillNuvarandeArbete() {
        return containsSysselsattningKod(Sysselsattningskoder.NUVARANDE_ARBETE);
    }

    private boolean containsSysselsattningKod(Kod sysselsattningKod) {
        if (getPatient() != null && getPatient().getSysselsattnings() != null) {
            for (se.inera.certificate.model.Sysselsattning sysselsattning : getPatient().getSysselsattnings()) {
                if (sysselsattningKod.equals(sysselsattning.getSysselsattningsTyp())) {
                    return true;
                }
            }
        }
        return false;
    }

    public Observation getArbetsformaga() {
        return findObservationByKod(ObservationsKoder.ARBETSFORMAGA);
    }

    public Aktivitet getForandratRessattAktuellt() {
        return getAktivitet(Aktivitetskoder.FORANDRA_RESSATT_TILL_ARBETSPLATSEN_AR_AKTUELLT);
    }

    public Aktivitet getForandratRessattEjAktuellt() {
        return getAktivitet(Aktivitetskoder.FORANDRA_RESSATT_TILL_ARBETSPLATSEN_AR_EJ_AKTUELLT);
    }

    public Aktivitet getKontaktMedForsakringskassanAktuell() {
        return getAktivitet(Aktivitetskoder.KONTAKT_MED_FK_AR_AKTUELL);
    }

    public Aktivitet getArbetsinriktadRehabiliteringAktuell() {
        return getAktivitet(Aktivitetskoder.ARBETSLIVSINRIKTAD_REHABILITERING_AR_AKTUELL);
    }

    public Aktivitet getArbetsinriktadRehabiliteringEjAktuell() {
        return getAktivitet(Aktivitetskoder.ARBETSLIVSINRIKTAD_REHABILITERING_AR_INTE_AKTUELL);
    }

    public Aktivitet getArbetsinriktadRehabiliteringEjBedombar() {
        return getAktivitet(Aktivitetskoder.GAR_EJ_ATT_BEDOMA_OM_ARBETSLIVSINRIKTAD_REHABILITERING_AR_AKTUELL);
    }

    public Aktivitet getAvstangningEnligtSmittskyddslagen() {
        return getAktivitet(Aktivitetskoder.AVSTANGNING_ENLIGT_SML_PGA_SMITTA);
    }

    public Aktivitet getRekommenderarKontaktMedArbetsformedlingen() {
        return getAktivitet(Aktivitetskoder.PATIENTEN_BOR_FA_KONTAKT_MED_ARBETSFORMEDLINGEN);
    }

    public Aktivitet getRekommenderarKontaktMedForetagshalsovarden() {
        return getAktivitet(Aktivitetskoder.PATIENTEN_BOR_FA_KONTAKT_MED_FORETAGSHALSOVARDEN);
    }

    public Aktivitet getRekommenderarOvrigt() {
        return getAktivitet(Aktivitetskoder.OVRIGT);
    }

    public Aktivitet getAtgardInomSjukvarden() {
        return getAktivitet(Aktivitetskoder.PLANERAD_ELLER_PAGAENDE_BEHANDLING_ELLER_ATGARD_INOM_SJUKVARDEN);
    }

    public Aktivitet getAnnanAtgard() {
        return getAktivitet(Aktivitetskoder.PLANERAD_ELLER_PAGAENDE_ANNAN_ATGARD);
    }

    public boolean isFilledAlways() {

        return true;
    }

    public boolean isFilledDiagnosis() {

        return getMedicinsktTillstand() != null;
    }

    public boolean isFilledProgress() {
        Observation bedomtTillstand = getBedomtTillstand();
        return (bedomtTillstand != null && !bedomtTillstand.getBeskrivning().trim().equals(""));
    }

    public boolean isFilledDisabilities() {
        String value = getFunktionsnedsattningBeskrivning();
        return value != null && !value.trim().equals("");
    }

    public boolean isFilledBasedOn() {
        List<String> kommentars = getKommentars();
        List<Vardkontakt> vardKontakter = getVardkontakter();
        List<Referens> referens = getReferenser();

        return (kommentars != null && kommentars.size() > 0) || (vardKontakter != null && vardKontakter.size() > 0) || (referens != null && referens.size() > 0);
    }

    public boolean isFilledLimitation() {
        String value = getAktivitetsnedsattningBeskrivning();
        return value != null && !value.trim().equals("");
    }

    public boolean isFilledRecommendations() {
        Aktivitet ovrigt = getRekommenderarOvrigt();
        return getRekommenderarKontaktMedArbetsformedlingen() != null || getRekommenderarKontaktMedForetagshalsovarden() != null || (ovrigt != null && !ovrigt.getBeskrivning().trim().equals(""));
    }

    public boolean isFilledPlannedTreatment() {
        Aktivitet annan = getAnnanAtgard();
        return getAtgardInomSjukvarden() != null || (annan != null && !annan.getBeskrivning().trim().equals(""));
    }

    public boolean isFilledWorkRehab() {
        return getArbetsinriktadRehabiliteringAktuell() != null || getArbetsinriktadRehabiliteringEjAktuell() != null || getArbetsinriktadRehabiliteringEjBedombar() != null;
    }

    public boolean isFilledPatientWorkCapacity() {
        return isArbetsformagaIForhallandeTillNuvarandeArbete() || isArbetsformagaIForhallandeTillArbetsloshet() || isArbetsformagaIForhallandeTillForaldraledighet();
    }

    public boolean isFilledPatientWorkCapacityJudgement() {
        Observation value = getArbetsformagaAktivitetsbegransning();
        if (value != null) {
            Prognos prognos = value.getPrognos();
            return prognos != null && prognos.getBeskrivning() != null && !prognos.getBeskrivning().trim().equals("");
        }
        return false;
    }

    public boolean isFilledPrognosis() {
        return isPrognosFullAterstallning() || isPrognosDelvisAterstallning() || isPrognosEjAterstallning() || isPrognosAterstallningGarEjBedomma();
    }

    public boolean isFilledPatientOtherTransport() {
        return getForandratRessattAktuellt() != null || getForandratRessattEjAktuellt() != null;
    }

    public boolean isFilledFKContact() {
        return true;
    }

    public List<StatusMeta> getStatus() {
        return status;
    }

    public void setStatus(List<StatusMeta> status) {
        this.status = status;
    }
    
 
}
