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

public class Vardenhet {

    private String enhetsId;

    private String enhetsnamn;

    private String postadress;

    private String postnummer;

    private String postort;

    private String telefonnummer;

    private String ePost;

    private Vardgivare vardgivare;

    public Vardenhet() {

    }

    public String getEnhetsId() {
        return enhetsId;
    }

    public void setEnhetsId(String enhetsId) {
        this.enhetsId = enhetsId;
    }

    public String getEnhetsnamn() {
        return enhetsnamn;
    }

    public void setEnhetsnamn(String enhetsNamn) {
        this.enhetsnamn = enhetsNamn;
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

    public void setPostnummer(String postNummer) {
        this.postnummer = postNummer;
    }

    public String getPostort() {
        return postort;
    }

    public void setPostort(String postOrt) {
        this.postort = postOrt;
    }

    public String getTelefonnummer() {
        return telefonnummer;
    }

    public void setTelefonnummer(String telefonNummer) {
        this.telefonnummer = telefonNummer;
    }

    public String getePost() {
        return ePost;
    }

    public void setePost(String ePost) {
        this.ePost = ePost;
    }

    public Vardgivare getVardgivare() {
        return vardgivare;
    }

    public void setVardgivare(Vardgivare vardgivare) {
        this.vardgivare = vardgivare;
    }

}
