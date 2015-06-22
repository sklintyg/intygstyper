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
package se.inera.certificate.modules.ts_diabetes.model.internal;

import se.inera.certificate.model.common.internal.GrundData;

public class Utlatande extends se.inera.certificate.model.common.internal.Utlatande {

    private String id;

    private String typ;

    private GrundData grundData = new GrundData();

    private String kommentar;

    private Vardkontakt vardkontakt;

    private IntygAvser intygAvser;

    private Diabetes diabetes;

    private Hypoglykemier hypoglykemier;

    private Syn syn;

    private Bedomning bedomning;

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public GrundData getGrundData() {
		return grundData;
	}

	public void setGrundData(GrundData grundData) {
		this.grundData = grundData;
	}

	public String getTyp() {
        return typ;
    }

    public void setTyp(String typAvUtlatande) {
        this.typ = typAvUtlatande;
    }

    public String getKommentarer() {
        return kommentar;
    }

    public void setKommentarer(String kommentar) {
        this.kommentar = kommentar;
    }

    public String getKommentar() {
        return kommentar;
    }

    public void setKommentar(String kommentar) {
        this.kommentar = kommentar;
    }

    public Vardkontakt getVardkontakt() {
        return vardkontakt;
    }

    public void setVardkontakt(Vardkontakt vardkontakt) {
        this.vardkontakt = vardkontakt;
    }

    public IntygAvser getIntygAvser() {
        if (intygAvser == null) {
            intygAvser = new IntygAvser();
        }
        return intygAvser;
    }

    public Diabetes getDiabetes() {
        if (diabetes == null) {
            diabetes = new Diabetes();
        }
        return diabetes;
    }

    public Hypoglykemier getHypoglykemier() {
        if (hypoglykemier == null) {
            hypoglykemier = new Hypoglykemier();
        }
        return hypoglykemier;
    }

    public Syn getSyn() {
        if (syn == null) {
            syn = new Syn();
        }
        return syn;
    }

    public Bedomning getBedomning() {
        if (bedomning == null) {
            bedomning = new Bedomning();
        }
        return bedomning;
    }
}
