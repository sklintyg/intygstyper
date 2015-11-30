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
package se.inera.certificate.modules.ts_bas.model.internal;

public class Utlatande extends se.inera.intyg.common.support.model.common.internal.Utlatande {

    private String id;

    private String typ;

    private String kommentar;

    private Vardkontakt vardkontakt;

    private IntygAvser intygAvser;

    private Syn syn;

    private HorselBalans horselBalans;

    private Funktionsnedsattning funktionsnedsattning;

    private HjartKarl hjartKarl;

    private Diabetes diabetes;

    private Neurologi neurologi;

    private Medvetandestorning medvetandestorning;

    private Njurar njurar;

    private Kognitivt kognitivt;

    private SomnVakenhet somnVakenhet;

    private NarkotikaLakemedel narkotikaLakemedel;

    private Psykiskt psykiskt;

    private Utvecklingsstorning utvecklingsstorning;

    private Sjukhusvard sjukhusvard;

    private Medicinering medicinering;

    private Bedomning bedomning;

    @Override
    public String getTyp() {
        return typ;
    }

    @Override
    public void setTyp(String typAvUtlatande) {
        this.typ = typAvUtlatande;
    }

    public String getKommentar() {
        return kommentar;
    }

    public void setKommentar(String kommentar) {
        this.kommentar = kommentar;
    }

    public Syn getSyn() {
        if (syn == null) {
            syn = new Syn();
        }
        return syn;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public HorselBalans getHorselBalans() {
        if (horselBalans == null) {
            horselBalans = new HorselBalans();
        }
        return horselBalans;
    }

    public Funktionsnedsattning getFunktionsnedsattning() {
        if (funktionsnedsattning == null) {
            funktionsnedsattning = new Funktionsnedsattning();
        }
        return funktionsnedsattning;
    }

    public HjartKarl getHjartKarl() {
        if (hjartKarl == null) {
            hjartKarl = new HjartKarl();
        }
        return hjartKarl;
    }

    public Diabetes getDiabetes() {
        if (diabetes == null) {
            diabetes = new Diabetes();
        }
        return diabetes;
    }

    public Neurologi getNeurologi() {
        if (neurologi == null) {
            neurologi = new Neurologi();
        }
        return neurologi;
    }

    public Medvetandestorning getMedvetandestorning() {
        if (medvetandestorning == null) {
            medvetandestorning = new Medvetandestorning();
        }
        return medvetandestorning;
    }

    public Njurar getNjurar() {
        if (njurar == null) {
            njurar = new Njurar();
        }
        return njurar;
    }

    public Kognitivt getKognitivt() {
        if (kognitivt == null) {
            kognitivt = new Kognitivt();
        }
        return kognitivt;
    }

    public SomnVakenhet getSomnVakenhet() {
        if (somnVakenhet == null) {
            somnVakenhet = new SomnVakenhet();
        }
        return somnVakenhet;
    }

    public NarkotikaLakemedel getNarkotikaLakemedel() {
        if (narkotikaLakemedel == null) {
            narkotikaLakemedel = new NarkotikaLakemedel();
        }
        return narkotikaLakemedel;
    }

    public Psykiskt getPsykiskt() {
        if (psykiskt == null) {
            psykiskt = new Psykiskt();
        }
        return psykiskt;
    }

    public Utvecklingsstorning getUtvecklingsstorning() {
        if (utvecklingsstorning == null) {
            utvecklingsstorning = new Utvecklingsstorning();
        }
        return utvecklingsstorning;
    }

    public Sjukhusvard getSjukhusvard() {
        if (sjukhusvard == null) {
            sjukhusvard = new Sjukhusvard();
        }
        return sjukhusvard;
    }

    public Medicinering getMedicinering() {
        if (medicinering == null) {
            medicinering = new Medicinering();
        }
        return medicinering;
    }

    public Bedomning getBedomning() {
        if (bedomning == null) {
            bedomning = new Bedomning();
        }
        return bedomning;
    }

    public Vardkontakt getVardkontakt() {
        if (vardkontakt == null) {
            vardkontakt = new Vardkontakt();
        }
        return vardkontakt;
    }

    public IntygAvser getIntygAvser() {
        if (intygAvser == null) {
            intygAvser = new IntygAvser();
        }
        return intygAvser;
    }

}
