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
package se.inera.certificate.modules.rli.model.external.common;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.Partial;

import se.inera.certificate.model.Kod;
import se.inera.certificate.model.PhysicalQuantity;

public class Observation {

    private Kod observationskategori;
    private Kod observationskod;
    private Partial observationstid;
    private String beskrivning;
    private PartialDateInterval observationsperiod;
    private List<PhysicalQuantity> vardes;
    private Boolean forekomst;
    private Boolean patientenInstammer;
    private Utforarroll utforsAv;
    private List<Prognos> prognos;

    public Kod getObservationskategori() {
        return observationskategori;
    }

    public void setObservationskategori(Kod observationskategori) {
        this.observationskategori = observationskategori;
    }

    public Kod getObservationskod() {
        return observationskod;
    }

    public void setObservationskod(Kod observationskod) {
        this.observationskod = observationskod;
    }

    public Partial getObservationstid() {
        return observationstid;
    }

    public void setObservationstid(Partial observationstid) {
        this.observationstid = observationstid;
    }

    public String getBeskrivning() {
        return beskrivning;
    }

    public void setBeskrivning(String beskrivning) {
        this.beskrivning = beskrivning;
    }

    public PartialDateInterval getObservationsperiod() {
        return observationsperiod;
    }

    public void setObservationsperiod(PartialDateInterval observationsperiod) {
        this.observationsperiod = observationsperiod;
    }

    public List<PhysicalQuantity> getVardes() {
        if (vardes == null) {
            vardes = new ArrayList<PhysicalQuantity>();
        }
        return this.vardes;
    }

    public Boolean getForekomst() {
        return forekomst;
    }

    public void setForekomst(Boolean forekomst) {
        this.forekomst = forekomst;
    }

    public Boolean getPatientenInstammer() {
        return patientenInstammer;
    }

    public void setPatientenInstammer(Boolean patientenInstammer) {
        this.patientenInstammer = patientenInstammer;
    }

    public Utforarroll getUtforsAv() {
        return utforsAv;
    }

    public void setUtforsAv(Utforarroll utforsAv) {
        this.utforsAv = utforsAv;
    }

    public List<Prognos> getPrognos() {
        if (prognos == null) {
            prognos = new ArrayList<Prognos>();
        }
        return this.prognos;
    }

}
