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
package se.inera.certificate.modules.ts_bas.model.external;

import java.util.ArrayList;
import java.util.List;

import se.inera.certificate.model.Kod;
import se.inera.certificate.model.Patient;
import se.inera.certificate.model.Status;

/**
 * The utlåtande used by ts-bas. This class extends the common external model with:
 * <ul>
 * <li> new field - {@link #intygAvser}
 * <li> sub class of {@link HoSPersonal} (specialiteter, befattningar)
 * <li> sub class of {@link Aktivitet} (id, plats, metod, aktivitetsstatus, forekomst, ostruktureradTid)
 * <li> sub class of {@link Vardkontakt} (idkontroll)
 * <li> sub class of {@link Rekommendation} (varde)
 * <li> sub class of {@link Observation} (id, forekomst, lateralitet)
 * <li> new field - {@link #observationAktivitetRelation}
 * </ul>
 */
public class Utlatande extends se.inera.certificate.model.Utlatande {

    private List<Kod> intygAvser;

    private Patient patient;

    private HosPersonal skapadAv;

    private List<Aktivitet> aktiviteter;

    private List<Vardkontakt> vardkontakter;

    private List<Rekommendation> rekommendationer;

    private List<Observation> observationer;
    
    private List<ObservationAktivitetRelation> observationAktivitetRelation;

    private List<Status> status;

    public List<Kod> getIntygAvser() {
        if (intygAvser == null) {
            intygAvser = new ArrayList<Kod>();
        }
        return intygAvser;
    }

    @Override
    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public HosPersonal getSkapadAv() {
        return skapadAv;
    }

    public void setSkapadAv(HosPersonal skapadAv) {
        this.skapadAv = skapadAv;
    }

    public List<Vardkontakt> getVardkontakter() {
        if (vardkontakter == null) {
            vardkontakter = new ArrayList<>();
        }
        return vardkontakter;
    }

    public List<Aktivitet> getAktiviteter() {
        if (aktiviteter == null) {
            aktiviteter = new ArrayList<Aktivitet>();
        }
        return this.aktiviteter;
    }

    public List<Rekommendation> getRekommendationer() {
        if (rekommendationer == null) {
            rekommendationer = new ArrayList<Rekommendation>();
        }
        return this.rekommendationer;
    }

    public List<Observation> getObservationer() {
        if (observationer == null) {
            observationer = new ArrayList<Observation>();
        }
        return this.observationer;
    }
    
    public List<ObservationAktivitetRelation> getObservationAktivitetRelationer() {
        if (observationAktivitetRelation == null) {
            observationAktivitetRelation = new ArrayList<ObservationAktivitetRelation>();
        }
        return this.observationAktivitetRelation ;
    }

    public List<Status> getStatus() {
        if (status == null) {
            status = new ArrayList<Status>();
        }
        return this.status;
    }
}
