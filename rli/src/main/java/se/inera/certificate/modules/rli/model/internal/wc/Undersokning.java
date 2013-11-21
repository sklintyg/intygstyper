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
package se.inera.certificate.modules.rli.model.internal.wc;

public class Undersokning {

    private OrsakAvbokning orsakForAvbokning;

    private Utforare utforsAv;
    
    private Vardenhet utforsVid;
    
    private String forstaUndersokningsdatum;

    private String forstaUndersokningsplats;

    private String undersokningsdatum;

    private String undersokningsplats;

    private KomplikationStyrkt komplikationStyrkt;

    private Graviditet graviditet;

    private String komplikationsbeskrivning;

    public Undersokning() {

    }

    public OrsakAvbokning getOrsakforavbokning() {
        return orsakForAvbokning;
    }

    public void setOrsakforavbokning(OrsakAvbokning orsakForAvbokning) {
        this.orsakForAvbokning = orsakForAvbokning;
    }

    public String getForstaUndersokningsdatum() {
        return forstaUndersokningsdatum;
    }

    public void setForstaUndersokningsdatum(String forstaUndersokningDatum) {
        this.forstaUndersokningsdatum = forstaUndersokningDatum;
    }

    public String getForstaUndersokningsplats() {
        return forstaUndersokningsplats;
    }

    public void setForstaUndersokningsplats(String forstaUndersokningPlats) {
        this.forstaUndersokningsplats = forstaUndersokningPlats;
    }

    public String getUndersokningsdatum() {
        return undersokningsdatum;
    }

    public void setUndersokningsdatum(String undersokningDatum) {
        this.undersokningsdatum = undersokningDatum;
    }

    public String getUndersokningsplats() {
        return undersokningsplats;
    }

    public void setUndersokningsplats(String undersokningPlats) {
        this.undersokningsplats = undersokningPlats;
    }

    public KomplikationStyrkt getKomplikationstyrkt() {
        return komplikationStyrkt;
    }

    public void setKomplikationstyrkt(KomplikationStyrkt komplikationStyrkt) {
        this.komplikationStyrkt = komplikationStyrkt;
    }

    public Graviditet getGraviditet() {
        return graviditet;
    }

    public void setGraviditet(Graviditet graviditet) {
        this.graviditet = graviditet;
    }

    public String getKomplikationsbeskrivning() {
        return komplikationsbeskrivning;
    }

    public void setKomplikationsbeskrivning(String komplikationBeskrivning) {
        this.komplikationsbeskrivning = komplikationBeskrivning;
    }

    public Utforare getUtforsAv() {
        return utforsAv;
    }

    public void setUtforsAv(Utforare utforsAv) {
        this.utforsAv = utforsAv;
    }

    public Vardenhet getUtforsVid() {
        return utforsVid;
    }

    public void setUtforsVid(Vardenhet utforsVid) {
        this.utforsVid = utforsVid;
    }

}
