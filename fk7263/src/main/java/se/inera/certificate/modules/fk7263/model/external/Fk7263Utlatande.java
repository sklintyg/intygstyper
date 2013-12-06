package se.inera.certificate.modules.fk7263.model.external;

import static se.inera.certificate.model.util.Iterables.find;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDateTime;
import org.joda.time.Partial;

import se.inera.certificate.model.Aktivitet;
import se.inera.certificate.model.HosPersonal;
import se.inera.certificate.model.Id;
import se.inera.certificate.model.Kod;
import se.inera.certificate.model.Observation;
import se.inera.certificate.model.PartialInterval;
import se.inera.certificate.model.Referens;
import se.inera.certificate.model.Vardkontakt;
import se.inera.certificate.model.util.Predicate;
import se.inera.certificate.modules.fk7263.model.codes.ObservationsKoder;

/**
 * @author marced
 */
public class Fk7263Utlatande {

    private Id id;

    private Kod typ;

    private List<String> kommentarer = new ArrayList<>();

    private LocalDateTime signeringsdatum;

    private LocalDateTime skickatdatum;

    private Fk7263Patient patient;

    private HosPersonal skapadAv;

    private List<Aktivitet> aktiviteter = new ArrayList<>();

    private List<Observation> observationer = new ArrayList<>();

    private List<Vardkontakt> vardkontakter = new ArrayList<>();

    private List<Referens> referenser = new ArrayList<>();

    public Id getId() {
        return id;
    }

    public void setId(Id id) {
        this.id = id;
    }

    public Kod getTyp() {
        return typ;
    }

    public void setTyp(Kod typ) {
        this.typ = typ;
    }

    public List<String> getKommentarer() {
        if (kommentarer == null) {
            kommentarer = new ArrayList<>();
        }
        return kommentarer;
    }

    public LocalDateTime getSigneringsdatum() {
        return signeringsdatum;
    }

    public void setSigneringsdatum(LocalDateTime signeringsdatum) {
        this.signeringsdatum = signeringsdatum;
    }

    public Fk7263Patient getPatient() {
        return patient;
    }

    public void setPatient(Fk7263Patient patient) {
        this.patient = patient;
    }

    public HosPersonal getSkapadAv() {
        return skapadAv;
    }

    public void setSkapadAv(HosPersonal skapadAv) {
        this.skapadAv = skapadAv;
    }

    public List<Aktivitet> getAktiviteter() {
        if (aktiviteter == null) {
            aktiviteter = new ArrayList<>();
        }
        return aktiviteter;
    }

    public List<Observation> getObservationer() {
        if (observationer == null) {
            observationer = new ArrayList<>();
        }
        return observationer;
    }

    public List<Vardkontakt> getVardkontakter() {
        if (vardkontakter == null) {
            vardkontakter = new ArrayList<>();
        }
        return vardkontakter;
    }

    public List<Referens> getReferenser() {
        if (referenser == null) {
            referenser = new ArrayList<>();
        }
        return referenser;
    }

    public LocalDateTime getSkickatdatum() {
        return skickatdatum;
    }

    public void setSkickatdatum(LocalDateTime skickatdatum) {
        this.skickatdatum = skickatdatum;
    }

    public List<Observation> getObservationsByKod(Kod observationsKod) {
        List<Observation> observations = new ArrayList<>();
        for (Observation observation : this.observationer) {
            if (observation.getObservationskod() != null && observation.getObservationskod().equals(observationsKod)) {
                observations.add(observation);
            }
        }
        return observations;
    }

    public List<Observation> getObservationsByKategori(Kod observationsKategori) {
        List<Observation> observations = new ArrayList<>();
        for (Observation observation : this.observationer) {
            if (observation.getObservationskategori() != null
                    && observation.getObservationskategori().equals(observationsKategori)) {
                observations.add(observation);
            }
        }
        return observations;
    }

    public Observation findObservationByKategori(final Kod observationsKategori) {
        return find(observationer, new Predicate<Observation>() {
            @Override
            public boolean apply(Observation observation) {
                return observation.getObservationskategori() != null
                        && observation.getObservationskategori().equals(observationsKategori);
            }
        }, null);
    }

    public Observation findObservationByKod(final Kod observationsKod) {
        return find(observationer, new Predicate<Observation>() {
            @Override
            public boolean apply(Observation observation) {
                return observation.getObservationskod() != null
                        && observation.getObservationskod().equals(observationsKod);
            }
        }, null);
    }

    public Aktivitet getAktivitet(final Kod aktivitetsKod) {
        if (aktiviteter == null) {
            return null;
        }

        return find(aktiviteter, new Predicate<Aktivitet>() {
            @Override
            public boolean apply(Aktivitet aktivitet) {
                return aktivitetsKod.equals(aktivitet.getAktivitetskod());
            }
        }, null);
    }

    public Vardkontakt getVardkontakt(final Kod vardkontaktTyp) {
        return find(vardkontakter, new Predicate<Vardkontakt>() {
            @Override
            public boolean apply(Vardkontakt vardkontakt) {
                return vardkontaktTyp.equals(vardkontakt.getVardkontakttyp());
            }
        }, null);
    }

    public Referens getReferens(final Kod referensTyp) {
        return find(referenser, new Predicate<Referens>() {
            @Override
            public boolean apply(Referens referens) {
                return referensTyp.equals(referens.getReferenstyp());
            }
        }, null);
    }

    /**
     * Certificate specific implementation of when a valid from date is.
     * Iterate through all From dates and return the earliest (non-null) date.
     * @return
     */
    public Partial getValidFromDate() {
        List<Observation> nedsattningar = getObservationsByKod(ObservationsKoder.ARBETSFORMAGA);
        Partial fromDate = null;

        for (Observation nedsattning : nedsattningar) {
            PartialInterval nextObservationsperiod = nedsattning.getObservationsperiod();
            if (nextObservationsperiod != null) {
                if (fromDate == null || fromDate.isAfter(nextObservationsperiod.getFrom())) {
                    fromDate = nextObservationsperiod.getFrom();
                }
            }
        }
        return fromDate;
    }

    /**
     * Certificate specific implementation of when a valid from date is.
     * Iterate through all Tom dates and return the latest (non-null) date.
     * 
     * @return
     */
    public Partial getValidToDate() {
        List<Observation> nedsattningar = getObservationsByKod(ObservationsKoder.ARBETSFORMAGA);
        Partial toDate = null;

        for (Observation nedsattning : nedsattningar) {
            PartialInterval nextObservationsperiod = nedsattning.getObservationsperiod();
            if (nextObservationsperiod != null) {
                if (toDate == null || toDate.isBefore(nextObservationsperiod.getTom())) {
                    toDate = nextObservationsperiod.getTom();
                }
            }
        }
        return toDate;
    }
}
