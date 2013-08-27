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

public class Undersokning {

    private OrsakAvbokning orsakForAvbokning;

    private String forstaUndersokningDatum;

    private String forstaUndersokningPlats;

    private String undersokningDatum;

    private String undersokningPlats;

    private KomplikationStyrkt komplikationStyrkt;

    private Graviditet graviditet;

    private String komplikationBeskrivning;

    public Undersokning() {

    }

    public OrsakAvbokning getOrsakforavbokning() {
        return orsakForAvbokning;
    }

    public void setOrsakforavbokning(OrsakAvbokning orsakForAvbokning) {
        this.orsakForAvbokning = orsakForAvbokning;
    }

    public String getForstaundersokningsdatum() {
        return forstaUndersokningDatum;
    }

    public void setForstaundersokningsdatum(String forstaUndersokningDatum) {
        this.forstaUndersokningDatum = forstaUndersokningDatum;
    }

    public String getForstaundersokningsplats() {
        return forstaUndersokningPlats;
    }

    public void setForstaundersokningsplats(String forstaUndersokningPlats) {
        this.forstaUndersokningPlats = forstaUndersokningPlats;
    }

    public String getUndersokningsdatum() {
        return undersokningDatum;
    }

    public void setUndersokningsdatum(String undersokningDatum) {
        this.undersokningDatum = undersokningDatum;
    }

    public String getUndersokningsplats() {
        return undersokningPlats;
    }

    public void setUndersokningsplats(String undersokningPlats) {
        this.undersokningPlats = undersokningPlats;
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
        return komplikationBeskrivning;
    }

    public void setKomplikationsbeskrivning(String komplikationBeskrivning) {
        this.komplikationBeskrivning = komplikationBeskrivning;
    }

}
