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
package se.inera.certificate.modules.rli.model.internal.mi;

import se.inera.certificate.modules.rli.model.codes.ArrangemangsKod;

public class Arrangemang {

    private String bokningsreferens;

    private String bokningsdatum;

    private String plats;

    private String arrangemangsdatum;
    
    private String arrangemangslutdatum;

    private ArrangemangsKod arrangemangstyp;

    private String avbestallningsdatum;

    public Arrangemang() {

    }

    public String getBokningsreferens() {
        return bokningsreferens;
    }

    public void setBokningsreferens(String bokningsreferens) {
        this.bokningsreferens = bokningsreferens;
    }

    public String getBokningsdatum() {
        return bokningsdatum;
    }

    public void setBokningsdatum(String bokningsdatum) {
        this.bokningsdatum = bokningsdatum;
    }

    public String getPlats() {
        return plats;
    }

    public void setPlats(String plats) {
        this.plats = plats;
    }

    public String getArrangemangsdatum() {
        return arrangemangsdatum;
    }

    public void setArrangemangsdatum(String arrangemangsdatum) {
        this.arrangemangsdatum = arrangemangsdatum;
    }

    public ArrangemangsKod getArrangemangstyp() {
        return arrangemangstyp;
    }

    public void setArrangemangstyp(ArrangemangsKod arrangemangstyp) {
        this.arrangemangstyp = arrangemangstyp;
    }

    public String getAvbestallningsdatum() {
        return avbestallningsdatum;
    }

    public void setAvbestallningsdatum(String avbestallningsdatum) {
        this.avbestallningsdatum = avbestallningsdatum;
    }

    public String getArrangemangslutdatum() {
        return arrangemangslutdatum;
    }

    public void setArrangemangslutdatum(String arrangemangslutdatum) {
        this.arrangemangslutdatum = arrangemangslutdatum;
    }

}
