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
package se.inera.certificate.modules.rli.model.edit;

import java.util.List;

import org.joda.time.LocalDateTime;

public class Utlatande {

    private String utlatandeid;
    
    private String utlatandeidroot;

    private String typAvUtlatande;

    private List<String> kommentarer;

    private LocalDateTime signeringsdatum;

    private LocalDateTime skickatdatum;

    private HoSPersonal skapadAv;

    private Patient patient;

    private Arrangemang arrangemang;

    private Undersokning undersokning;

    private Rekommendation rekommendation;

    private List<Status> status;

    public Utlatande() {

    }

    public String getUtlatandeid() {
        return utlatandeid;
    }

    public void setUtlatandeid(String utlatandeId) {
        this.utlatandeid = utlatandeId;
    }

    public String getTypAvUtlatande() {
        return typAvUtlatande;
    }

    public void setTypAvUtlatande(String typAvUtlatande) {
        this.typAvUtlatande = typAvUtlatande;
    }

    public List<String> getKommentarer() {
        return kommentarer;
    }

    public void setKommentarer(List<String> kommentarer) {
        this.kommentarer = kommentarer;
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

    public HoSPersonal getSkapadAv() {
        return skapadAv;
    }

    public void setSkapadAv(HoSPersonal skapadAv) {
        this.skapadAv = skapadAv;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Arrangemang getArrangemang() {
        return arrangemang;
    }

    public void setArrangemang(Arrangemang arrangemang) {
        this.arrangemang = arrangemang;
    }

    public Undersokning getUndersokning() {
        return undersokning;
    }

    public void setUndersokning(Undersokning undersokning) {
        this.undersokning = undersokning;
    }

    public Rekommendation getRekommendation() {
        return rekommendation;
    }

    public void setRekommendation(Rekommendation rekommendation) {
        this.rekommendation = rekommendation;
    }

    public List<Status> getStatus() {
        return status;
    }

    public void setStatus(List<Status> status) {
        this.status = status;
    }

    public String getUtlatandeidroot() {
        return utlatandeidroot;
    }

    public void setUtlatandeidroot(String utlatandeidroot) {
        this.utlatandeidroot = utlatandeidroot;
    }

}
