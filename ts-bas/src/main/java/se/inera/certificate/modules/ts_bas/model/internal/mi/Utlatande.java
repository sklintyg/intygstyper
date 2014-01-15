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
package se.inera.certificate.modules.ts_bas.model.internal.mi;

import java.util.List;

import org.joda.time.LocalDateTime;

public class Utlatande {

    private String utlatandeid;

    private String typAvUtlatande;

    private List<String> kommentarer;

    private LocalDateTime signeringsdatum;

    private LocalDateTime skickatdatum;

    private HoSPersonal skapadAv;

    private Patient patient;

    private Syn syn;

    private HorselBalans horselBalans;

    private Funktionsnedsattning funktionsnersattning;

    private HjartKarl hjartKarl;

    private Diabetes diabetes;

    private Neurologi neurologi;

    private Medvetandestorning medvertandestörning;

    private Njurar njurar;

    private Kognitivt kognitivt;

    private SomnVakenhet somnVakenhet;

    private NarkotikaLakemedel narkotikaLakemedel;

    private Psykiskt psykiskt;

    private Utvecklingsstorning utvecklingsstorning;

    private Sjukhusvard sjukhusvard;

    private Medicinering medicinering;

    private Bedomning bedomning;

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

    public Syn getSyn() {
        return syn;
    }

    public void setSyn(Syn syn) {
        this.syn = syn;
    }

    public HorselBalans getHorselBalans() {
        return horselBalans;
    }

    public void setHorselBalans(HorselBalans horselBalans) {
        this.horselBalans = horselBalans;
    }

    public Funktionsnedsattning getFunktionsnersattning() {
        return funktionsnersattning;
    }

    public void setFunktionsnersattning(Funktionsnedsattning funktionsnersattning) {
        this.funktionsnersattning = funktionsnersattning;
    }

    public HjartKarl getHjartKarl() {
        return hjartKarl;
    }

    public void setHjartKarl(HjartKarl hjartKarl) {
        this.hjartKarl = hjartKarl;
    }

    public Diabetes getDiabetes() {
        return diabetes;
    }

    public void setDiabetes(Diabetes diabetes) {
        this.diabetes = diabetes;
    }

    public Neurologi getNeurologi() {
        return neurologi;
    }

    public void setNeurologi(Neurologi neurologi) {
        this.neurologi = neurologi;
    }

    public Medvetandestorning getMedvertandestörning() {
        return medvertandestörning;
    }

    public void setMedvertandestörning(Medvetandestorning medvertandestörning) {
        this.medvertandestörning = medvertandestörning;
    }

    public Njurar getNjurar() {
        return njurar;
    }

    public void setNjurar(Njurar njurar) {
        this.njurar = njurar;
    }

    public Kognitivt getKognitivt() {
        return kognitivt;
    }

    public void setKognitivt(Kognitivt kognitivt) {
        this.kognitivt = kognitivt;
    }

    public SomnVakenhet getSomnVakenhet() {
        return somnVakenhet;
    }

    public void setSomnVakenhet(SomnVakenhet somnVakenhet) {
        this.somnVakenhet = somnVakenhet;
    }

    public NarkotikaLakemedel getNarkotikaLakemedel() {
        return narkotikaLakemedel;
    }

    public void setNarkotikaLakemedel(NarkotikaLakemedel narkotikaLakemedel) {
        this.narkotikaLakemedel = narkotikaLakemedel;
    }

    public Psykiskt getPsykiskt() {
        return psykiskt;
    }

    public void setPsykiskt(Psykiskt psykiskt) {
        this.psykiskt = psykiskt;
    }

    public Utvecklingsstorning getUtvecklingsstorning() {
        return utvecklingsstorning;
    }

    public void setUtvecklingsstorning(Utvecklingsstorning utvecklingsstorning) {
        this.utvecklingsstorning = utvecklingsstorning;
    }

    public Sjukhusvard getSjukhusvard() {
        return sjukhusvard;
    }

    public void setSjukhusvard(Sjukhusvard sjukhusvard) {
        this.sjukhusvard = sjukhusvard;
    }

    public Medicinering getMedicinering() {
        return medicinering;
    }

    public void setMedicinering(Medicinering medicinering) {
        this.medicinering = medicinering;
    }

    public Bedomning getBedomning() {
        return bedomning;
    }

    public void setBedomning(Bedomning bedomning) {
        this.bedomning = bedomning;
    }
}
