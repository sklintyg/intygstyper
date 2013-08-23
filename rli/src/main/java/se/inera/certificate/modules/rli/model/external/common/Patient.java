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

public class Patient {

    private Id personId;
    private List<String> fornamns;
    private List<String> efternamns;
    private List<String> mellannamns;
    private String postadress;
    private String postnummer;
    private String postort;
    private List<PatientRelation> patientRelations;
    private List<Arbetsuppgift> arbetsuppgifts;
    private List<Sysselsattning> sysselsattnings;

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

    public String getPostadress() {
        return postadress;
    }

    public void setPostadress(String postadress) {
        this.postadress = postadress;
    }

    public String getPostnummer() {
        return postnummer;
    }

    public void setPostnummer(String postnummer) {
        this.postnummer = postnummer;
    }

    public String getPostort() {
        return postort;
    }

    public void setPostort(String postort) {
        this.postort = postort;
    }

    public List<PatientRelation> getPatientRelations() {
        if (patientRelations == null) {
            patientRelations = new ArrayList<PatientRelation>();
        }
        return this.patientRelations;
    }

    public List<Arbetsuppgift> getArbetsuppgifts() {
        if (arbetsuppgifts == null) {
            arbetsuppgifts = new ArrayList<Arbetsuppgift>();
        }
        return this.arbetsuppgifts;
    }

    public List<Sysselsattning> getSysselsattnings() {
        if (sysselsattnings == null) {
            sysselsattnings = new ArrayList<Sysselsattning>();
        }
        return this.sysselsattnings;
    }
}
