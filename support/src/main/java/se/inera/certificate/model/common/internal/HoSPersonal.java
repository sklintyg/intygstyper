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
package se.inera.certificate.model.common.internal;

import java.util.ArrayList;
import java.util.List;

public class HoSPersonal {

    private String personId;

    private String fullstandigtNamn;

    private String forskrivarKod;

    private List<String> befattningar;

    private List<String> specialiteter;

    private Vardenhet vardenhet;

    public HoSPersonal() {

    }

    public List<String> getSpecialiteter() {
        if (specialiteter == null) {
            specialiteter = new ArrayList<String>();
        }
        return specialiteter;
    }

    public List<String> getBefattningar() {
        if (befattningar == null) {
            befattningar = new ArrayList<String>();
        }
        return befattningar;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getFullstandigtNamn() {
        return fullstandigtNamn;
    }

    public void setFullstandigtNamn(String fullstandigtNamn) {
        this.fullstandigtNamn = fullstandigtNamn;
    }

    public String getForskrivarKod() { return forskrivarKod; }

    public void setForskrivarKod(String forskrivarKod) { this.forskrivarKod = forskrivarKod; }

    public Vardenhet getVardenhet() {
        return vardenhet;
    }

    public void setVardenhet(Vardenhet vardenhet) {
        this.vardenhet = vardenhet;
    }

}
