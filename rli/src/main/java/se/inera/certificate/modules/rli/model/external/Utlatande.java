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
package se.inera.certificate.modules.rli.model.external;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDateTime;

import se.inera.certificate.model.Id;
import se.inera.certificate.model.Kod;
import se.inera.certificate.model.Status;
import se.inera.certificate.model.HosPersonal;
import se.inera.certificate.model.Observation;
import se.inera.certificate.model.Patient;
import se.inera.certificate.model.Referens;
import se.inera.certificate.model.Rekommendation;
import se.inera.certificate.model.Vardkontakt;

/**
 * The utlåtande used by RLI. This class is a copy of the common external model (defined in se.inera.certificate.model),
 * extending with:
 * <ul>
 * <li> {@link Arrangemang}
 * <li> {@link Aktivitet}
 * </ul>
 * 
 * @author Gustav Norbäcker, R2M
 */
public class Utlatande {

    private Id id;

    private Kod typ;

    private List<String> kommentarer;

    private LocalDateTime signeringsdatum;

    private LocalDateTime skickatdatum;

    private Patient patient;

    private HosPersonal skapadAv;

    private List<HosPersonal> harDeltagandeHosPersonal;

    private List<Vardkontakt> vardkontakter;

    private List<Referens> referenser;

    private List<Aktivitet> aktiviteter;

    private List<Rekommendation> rekommendationer;

    private List<Observation> observationer;

    private List<Status> status;

    private Arrangemang arrangemang;

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
            kommentarer = new ArrayList<String>();
        }
        return this.kommentarer;
    }

    public LocalDateTime getSigneringsdatum() {
        return signeringsdatum;
    }

    public void setSigneringsdatum(LocalDateTime signeringsdatum) {
        this.signeringsdatum = signeringsdatum;
    }

    public LocalDateTime getSkickatdatum() {
        return skickatdatum;
    }

    public void setSkickatdatum(LocalDateTime skickatdatum) {
        this.skickatdatum = skickatdatum;
    }

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

    public List<HosPersonal> getHarDeltagandeHosPersonal() {
        if (harDeltagandeHosPersonal == null) {
            harDeltagandeHosPersonal = new ArrayList<HosPersonal>();
        }
        return this.harDeltagandeHosPersonal;
    }

    public List<Vardkontakt> getVardkontakter() {
        if (vardkontakter == null) {
            vardkontakter = new ArrayList<Vardkontakt>();
        }
        return this.vardkontakter;
    }

    public List<Referens> getReferenser() {
        if (referenser == null) {
            referenser = new ArrayList<Referens>();
        }
        return this.referenser;
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

    public List<Status> getStatus() {
        if (status == null) {
            status = new ArrayList<Status>();
        }
        return this.status;
    }

    public Arrangemang getArrangemang() {
        return arrangemang;
    }

    public void setArrangemang(Arrangemang arrangemang) {
        this.arrangemang = arrangemang;
    }
}
