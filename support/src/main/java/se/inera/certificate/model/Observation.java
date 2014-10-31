package se.inera.certificate.model;

import java.util.Collections;
import java.util.List;

import org.joda.time.LocalDateTime;

public class Observation {

    private Id id;

    private Kod observationskategori;

    private Kod observationskod;

    private PartialInterval observationsperiod;

    private String beskrivning;

    private LocalDateTime observationstid;

    private Boolean forekomst;

    public final Id getId() {
        return id;
    }

    public final void setId(Id id) {
        this.id = id;
    }

    public final Kod getObservationskategori() {
        return observationskategori;
    }

    public final void setObservationskategori(Kod observationskategori) {
        this.observationskategori = observationskategori;
    }

    public final Kod getObservationskod() {
        return observationskod;
    }

    public final void setObservationskod(Kod observationskod) {
        this.observationskod = observationskod;
    }

    public final PartialInterval getObservationsperiod() {
        return observationsperiod;
    }

    public final void setObservationsperiod(PartialInterval observationsperiod) {
        this.observationsperiod = observationsperiod;
    }

    public final String getBeskrivning() {
        return beskrivning;
    }

    public final void setBeskrivning(String beskrivning) {
        this.beskrivning = beskrivning;
    }

    /**
     * Returns the list of varde for this observation.
     * <p/>
     * Note that this implementation only returns an immutable empty list of {@link Object}s. Subclasses which override
     * this method should do this by concretise the return type like:
     * <p/>
     * <code>List&lt;PhysicalQuantity></code>
     *
     * @return A list of {@link Object}s.
     */
    public List<? extends Object> getVarde() {
        return Collections.emptyList();
    }

    public final LocalDateTime getObservationstid() {
        return observationstid;
    }

    public final void setObservationstid(LocalDateTime observationstid) {
        this.observationstid = observationstid;
    }

    public final Boolean getForekomst() {
        return forekomst;
    }

    public final void setForekomst(Boolean forekomst) {
        this.forekomst = forekomst;
    }

    public Utforarroll getUtforsAv() {
        return null;
    }
}
