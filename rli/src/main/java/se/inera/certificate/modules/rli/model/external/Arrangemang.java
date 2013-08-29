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
package se.inera.certificate.modules.rli.model.external;

import org.joda.time.Partial;

import se.inera.certificate.model.PartialInterval;
import se.inera.certificate.model.Kod;

public class Arrangemang {

    private String bokningsreferens;

    private Partial bokningsdatum;

    private PartialInterval arrangemangstid;

    private Partial avbestallningsdatum;

    private Kod arrangemangstyp;

    private String plats;

    public String getBokningsreferens() {
        return bokningsreferens;
    }

    public void setBokningsreferens(String bokningsreferens) {
        this.bokningsreferens = bokningsreferens;
    }

    public Partial getBokningsdatum() {
        return bokningsdatum;
    }

    public void setBokningsdatum(Partial bokningsdatum) {
        this.bokningsdatum = bokningsdatum;
    }

    public PartialInterval getArrangemangstid() {
        return arrangemangstid;
    }

    public void setArrangemangstid(PartialInterval arrangemangstid) {
        this.arrangemangstid = arrangemangstid;
    }

    public Partial getAvbestallningsdatum() {
        return avbestallningsdatum;
    }

    public void setAvbestallningsdatum(Partial avbestallningsdatum) {
        this.avbestallningsdatum = avbestallningsdatum;
    }

    public Kod getArrangemangstyp() {
        return arrangemangstyp;
    }

    public void setArrangemangstyp(Kod arrangemangstyp) {
        this.arrangemangstyp = arrangemangstyp;
    }

    public String getPlats() {
        return plats;
    }

    public void setPlats(String plats) {
        this.plats = plats;
    }
}
