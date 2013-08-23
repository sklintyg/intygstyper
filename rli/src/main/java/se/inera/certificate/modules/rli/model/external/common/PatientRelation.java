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

import se.inera.certificate.model.Id;
import se.inera.certificate.model.Kod;

public class PatientRelation {

    private Kod relationskategori;
    private List<Kod> relationTyps;
    private Id personId;
    private List<String> fornamns;
    private List<String> efternamns;
    private List<String> mellannamns;
    private List<String> adresses;

    public Kod getRelationskategori() {
        return relationskategori;
    }

    public void setRelationskategori(Kod relationskategori) {
        this.relationskategori = relationskategori;
    }

    public List<Kod> getRelationTyps() {
        if (relationTyps == null) {
            relationTyps = new ArrayList<Kod>();
        }
        return this.relationTyps;
    }

    public Id getPersonId() {
        return personId;
    }

    public void setPersonId(Id personId) {
        this.personId = personId;
    }

    public List<String> getFornamns() {
        if (fornamns == null) {
            fornamns = new ArrayList<String>();
        }
        return this.fornamns;
    }

    public List<String> getEfternamns() {
        if (efternamns == null) {
            efternamns = new ArrayList<String>();
        }
        return this.efternamns;
    }

    public List<String> getMellannamns() {
        if (mellannamns == null) {
            mellannamns = new ArrayList<String>();
        }
        return this.mellannamns;
    }

    public List<String> getAdresses() {
        if (adresses == null) {
            adresses = new ArrayList<String>();
        }
        return this.adresses;
    }
}
