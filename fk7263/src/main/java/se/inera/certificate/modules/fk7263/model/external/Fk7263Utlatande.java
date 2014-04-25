package se.inera.certificate.modules.fk7263.model.external;

import static se.inera.certificate.model.util.Iterables.find;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDate;

import se.inera.certificate.model.Kod;
import se.inera.certificate.model.LocalDateInterval;
import se.inera.certificate.model.Referens;
import se.inera.certificate.model.Utlatande;
import se.inera.certificate.model.Vardkontakt;
import se.inera.certificate.model.util.Predicate;
import se.inera.certificate.modules.fk7263.model.codes.ObservationsKoder;
import se.inera.certificate.modules.fk7263.model.converter.DateTimeConverter;

/**
 * @author marced
 */
public class Fk7263Utlatande extends Utlatande {

    private Fk7263Patient patient;

    private Fk7263HosPersonal skapadAv;

    private List<Fk7263Aktivitet> aktiviteter = new ArrayList<>();

    private List<Fk7263Observation> observationer = new ArrayList<>();

    private List<Vardkontakt> vardkontakter = new ArrayList<>();

    private List<Referens> referenser = new ArrayList<>();

    public Fk7263Patient getPatient() {
        return patient;
    }

    public void setPatient(Fk7263Patient patient) {
        this.patient = patient;
    }

    public Fk7263HosPersonal getSkapadAv() {
        return skapadAv;
    }

    public void setSkapadAv(Fk7263HosPersonal skapadAv) {
        this.skapadAv = skapadAv;
    }

    public List<Fk7263Aktivitet> getAktiviteter() {
        if (aktiviteter == null) {
            aktiviteter = new ArrayList<>();
        }
        return aktiviteter;
    }

    public List<Fk7263Observation> getObservationer() {
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

    public List<Fk7263Observation> getObservationsByKod(Kod observationsKod) {
        List<Fk7263Observation> observations = new ArrayList<>();
        for (Fk7263Observation observation : this.observationer) {
            if (observation.getObservationskod() != null && observation.getObservationskod().equals(observationsKod)) {
                observations.add(observation);
            }
        }
        return observations;
    }

    public List<Fk7263Observation> getObservationsByKategori(Kod observationsKategori) {
        List<Fk7263Observation> observations = new ArrayList<>();
        for (Fk7263Observation observation : this.observationer) {
            if (observation.getObservationskategori() != null
                    && observation.getObservationskategori().equals(observationsKategori)) {
                observations.add(observation);
            }
        }
        return observations;
    }

    public Fk7263Observation findObservationByKategori(final Kod observationsKategori) {
        return find(observationer, new Predicate<Fk7263Observation>() {
            @Override
            public boolean apply(Fk7263Observation observation) {
                return observation.getObservationskategori() != null
                        && observation.getObservationskategori().equals(observationsKategori);
            }
        }, null);
    }

    public Fk7263Observation findObservationByKod(final Kod observationsKod) {
        return find(observationer, new Predicate<Fk7263Observation>() {
            @Override
            public boolean apply(Fk7263Observation observation) {
                return observation.getObservationskod() != null
                        && observation.getObservationskod().equals(observationsKod);
            }
        }, null);
    }

    public Fk7263Aktivitet getAktivitet(final Kod aktivitetsKod) {
        if (aktiviteter == null) {
            return null;
        }

        return find(aktiviteter, new Predicate<Fk7263Aktivitet>() {
            @Override
            public boolean apply(Fk7263Aktivitet aktivitet) {
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
     * Certificate specific implementation of when a valid from date is. Iterate through all From dates and return the
     * earliest (non-null) date.
     *
     * @return LocalDate
     */
    @Override
    public LocalDate getValidFromDate() {
        List<Fk7263Observation> nedsattningar = getObservationsByKod(ObservationsKoder.ARBETSFORMAGA);
        LocalDate fromDate = null;

        for (Fk7263Observation nedsattning : nedsattningar) {
            LocalDateInterval nextObservationsperiod = DateTimeConverter.toLocalDateInterval(nedsattning
                    .getObservationsperiod());

            if (nextObservationsperiod != null) {
                if (fromDate == null || fromDate.isAfter(nextObservationsperiod.getFrom())) {
                    fromDate = nextObservationsperiod.getFrom();
                }
            }
        }
        return fromDate;
    }

    /**
     * Certificate specific implementation of when a valid from date is. Iterate through all Tom dates and return the
     * latest (non-null) date.
     *
     * @return localDate
     */
    public LocalDate getValidToDate() {
        List<Fk7263Observation> nedsattningar = getObservationsByKod(ObservationsKoder.ARBETSFORMAGA);
        LocalDate toDate = null;

        for (Fk7263Observation nedsattning : nedsattningar) {
            LocalDateInterval nextObservationsperiod = DateTimeConverter.toLocalDateInterval(nedsattning
                    .getObservationsperiod());

            if (nextObservationsperiod != null) {
                if (toDate == null || toDate.isBefore(nextObservationsperiod.getFrom())) {
                    toDate = nextObservationsperiod.getTom();
                }
            }
        }
        return toDate;
    }
}
