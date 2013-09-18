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

    private List<String> kommentars = new ArrayList<>();

    private LocalDateTime signeringsDatum;

    private LocalDateTime skickatDatum;

    private Fk7263Patient patient;

    private HosPersonal skapadAv;

    private List<Aktivitet> aktiviteter = new ArrayList<>();

    private List<Observation> observations = new ArrayList<>();

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

    public List<String> getKommentars() {
        return kommentars;
    }

    public void setKommentars(List<String> kommentar) {
        this.kommentars = kommentar;
    }

    public LocalDateTime getSigneringsDatum() {
        return signeringsDatum;
    }

    public void setSigneringsDatum(LocalDateTime signeringsDatum) {
        this.signeringsDatum = signeringsDatum;
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
        return aktiviteter;
    }

    public void setAktiviteter(List<Aktivitet> aktiviteter) {
        this.aktiviteter = aktiviteter;
    }

    public List<Observation> getObservations() {
        return observations;
    }

    public void setObservations(List<Observation> observations) {
        this.observations = observations;
    }

    public List<Vardkontakt> getVardkontakter() {
        return vardkontakter;
    }

    public void setVardkontakter(List<Vardkontakt> vardkontakter) {
        this.vardkontakter = vardkontakter;
    }

    public List<Referens> getReferenser() {
        return referenser;
    }

    public void setReferenser(List<Referens> referenser) {
        this.referenser = referenser;
    }

    public LocalDateTime getSkickatDatum() {
        return skickatDatum;
    }

    public void setSkickatDatum(LocalDateTime skickatDatum) {
        this.skickatDatum = skickatDatum;
    }

    public List<Observation> getObservationsByKod(Kod observationsKod) {
        List<Observation> observations = new ArrayList<>();
        for (Observation observation : this.observations) {
            if (observation.getObservationsKod() != null && observation.getObservationsKod().equals(observationsKod)) {
                observations.add(observation);
            }
        }
        return observations;
    }

    public List<Observation> getObservationsByKategori(Kod observationsKategori) {
        List<Observation> observations = new ArrayList<>();
        for (Observation observation : this.observations) {
            if (observation.getObservationsKategori() != null
                    && observation.getObservationsKategori().equals(observationsKategori)) {
                observations.add(observation);
            }
        }
        return observations;
    }

    public Observation findObservationByKategori(final Kod observationsKategori) {
        return find(observations, new Predicate<Observation>() {
            @Override
            public boolean apply(Observation observation) {
                return observation.getObservationsKategori() != null
                        && observation.getObservationsKategori().equals(observationsKategori);
            }
        }, null);
    }

    public Observation findObservationByKod(final Kod observationsKod) {
        return find(observations, new Predicate<Observation>() {
            @Override
            public boolean apply(Observation observation) {
                return observation.getObservationsKod() != null
                        && observation.getObservationsKod().equals(observationsKod);
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
     * Certificate specific implementation of when a valid from date is
     * @return
     */
    public Partial getValidFromDate() {
        List<Observation> nedsattningar = getObservationsByKod(ObservationsKoder.ARBETSFORMAGA);
        Partial fromDate = null;

        for (Observation nedsattning : nedsattningar) {
            Partial aktivitetsbegransningFromDate = nedsattning.getObservationsPeriod().getFrom();
            if (fromDate == null || fromDate.isAfter(aktivitetsbegransningFromDate)) {
                fromDate = aktivitetsbegransningFromDate;
            }
        }
        return fromDate;
    }

    /**
     * Certificate specific implementation of when a valid from date is
     * 
     * @return
     */
    public Partial getValidToDate() {
        List<Observation> nedsattningar = getObservationsByKod(ObservationsKoder.ARBETSFORMAGA);
        Partial toDate = null;

        for (Observation nedsattning : nedsattningar) {
            Partial aktivitetsbegransningToDate = nedsattning.getObservationsPeriod().getTom();
            if (toDate == null || toDate.isBefore(aktivitetsbegransningToDate)) {
                toDate = aktivitetsbegransningToDate;
            }
        }
        return toDate;
    }  
}
