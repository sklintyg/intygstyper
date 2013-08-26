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

import java.util.ArrayList;
import java.util.List;

import se.inera.certificate.model.Id;
import se.inera.certificate.model.Kod;
import se.inera.certificate.modules.rli.model.external.common.Enhet;
import se.inera.certificate.modules.rli.model.external.common.Omfattning;
import se.inera.certificate.modules.rli.model.external.common.PartialDateInterval;
import se.inera.certificate.modules.rli.model.external.common.Utforarroll;

/**
 * The aktivitet used by RLI. This class is a copy of the common external model (defined in se.inera.certificate.model),
 * extending with:
 * <ul>
 * <li> {@link #plats}
 * </ul>
 * 
 * @author Gustav Norbäcker, R2M
 */
public class Aktivitet {
    
    private Id aktivitetsId;
    private Kod aktivitetskod;
    private PartialDateInterval aktivitetstid;
    private String beskrivning;
    private Kod aktivitetsstatus;
    private String motivering;
    private String syfte;
    private String plats;
    private Enhet utforsVidEnhet;
    private List<Utforarroll> beskrivsAvs;
    private Omfattning omfattning;

    public Id getAktivitetsId() {
        return aktivitetsId;
    }

    public void setAktivitetsId(Id aktivitetsId) {
        this.aktivitetsId = aktivitetsId;
    }

    public Kod getAktivitetskod() {
        return aktivitetskod;
    }

    public void setAktivitetskod(Kod aktivitetskod) {
        this.aktivitetskod = aktivitetskod;
    }

    public PartialDateInterval getAktivitetstid() {
        return aktivitetstid;
    }

    public void setAktivitetstid(PartialDateInterval aktivitetstid) {
        this.aktivitetstid = aktivitetstid;
    }

    public String getBeskrivning() {
        return beskrivning;
    }

    public void setBeskrivning(String beskrivning) {
        this.beskrivning = beskrivning;
    }

    public Kod getAktivitetsstatus() {
        return aktivitetsstatus;
    }

    public void setAktivitetsstatus(Kod aktivitetsstatus) {
        this.aktivitetsstatus = aktivitetsstatus;
    }

    public String getMotivering() {
        return motivering;
    }

    public void setMotivering(String motivering) {
        this.motivering = motivering;
    }

    public String getSyfte() {
        return syfte;
    }

    public void setSyfte(String syfte) {
        this.syfte = syfte;
    }

    public String getPlats() {
        return plats;
    }

    public void setPlats(String plats) {
        this.plats = plats;
    }

    public Enhet getUtforsVidEnhet() {
        return utforsVidEnhet;
    }

    public void setUtforsVidEnhet(Enhet utforsVidEnhet) {
        this.utforsVidEnhet = utforsVidEnhet;
    }

    public List<Utforarroll> getBeskrivsAvs() {
        if (beskrivsAvs == null) {
            beskrivsAvs = new ArrayList<Utforarroll>();
        }
        return this.beskrivsAvs;
    }

    public Omfattning getOmfattning() {
        return omfattning;
    }

    public void setOmfattning(Omfattning omfattning) {
        this.omfattning = omfattning;
    }
}
