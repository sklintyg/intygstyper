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
package se.inera.certificate.modules.rli.model.internal;

public class Patient {

    private String personId;

    private String fullstandigtnamn;

    private String fornamn;

    private String efternamn;

    private String mellannamn;

    private String postadress;
    
    private String postnummer;
    
    private String postort;

    public Patient() {

    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getFullstandigtnamn() {
        return fullstandigtnamn;
    }

    public void setFullstandigtnamn(String fullstandigtNamn) {
        this.fullstandigtnamn = fullstandigtNamn;
    }

    public String getFornamn() {
        return fornamn;
    }

    public void setFornamn(String forNamn) {
        this.fornamn = forNamn;
    }

    public String getEfternamn() {
        return efternamn;
    }

    public void setEfternamn(String efterNamn) {
        this.efternamn = efterNamn;
    }

    public String getMellannamn() {
        return mellannamn;
    }

    public void setMellannamn(String mellanNamn) {
        this.mellannamn = mellanNamn;
    }

    public String getPostadress() {
        return postadress;
    }

    public void setPostadress(String postAdress) {
        this.postadress = postAdress;
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

}
