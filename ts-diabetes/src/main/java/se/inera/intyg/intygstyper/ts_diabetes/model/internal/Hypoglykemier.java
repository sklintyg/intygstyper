/*
 * Copyright (C) 2016 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package se.inera.intyg.intygstyper.ts_diabetes.model.internal;

import se.inera.intyg.common.support.model.InternalDate;

public class Hypoglykemier {
    private Boolean kunskapOmAtgarder;

    private Boolean teckenNedsattHjarnfunktion;

    private Boolean saknarFormagaKannaVarningstecken;

    private Boolean allvarligForekomst;

    private String allvarligForekomstBeskrivning;

    private Boolean allvarligForekomstTrafiken;

    private String allvarligForekomstTrafikBeskrivning;

    private Boolean allvarligForekomstVakenTid;

    private InternalDate allvarligForekomstVakenTidObservationstid;

    private Boolean egenkontrollBlodsocker;

    public Boolean getKunskapOmAtgarder() {
        return kunskapOmAtgarder;
    }

    public void setKunskapOmAtgarder(Boolean kunskapOmAtgarder) {
        this.kunskapOmAtgarder = kunskapOmAtgarder;
    }

    public Boolean getTeckenNedsattHjarnfunktion() {
        return teckenNedsattHjarnfunktion;
    }

    public void setTeckenNedsattHjarnfunktion(Boolean teckenNedsattHjarnfunktion) {
        this.teckenNedsattHjarnfunktion = teckenNedsattHjarnfunktion;
    }

    public Boolean getSaknarFormagaKannaVarningstecken() {
        return saknarFormagaKannaVarningstecken;
    }

    public void setSaknarFormagaKannaVarningstecken(Boolean saknarFormagaKannaVarningstecken) {
        this.saknarFormagaKannaVarningstecken = saknarFormagaKannaVarningstecken;
    }

    public Boolean getAllvarligForekomst() {
        return allvarligForekomst;
    }

    public void setAllvarligForekomst(Boolean allvarligForekomst) {
        this.allvarligForekomst = allvarligForekomst;
    }

    public String getAllvarligForekomstBeskrivning() {
        return allvarligForekomstBeskrivning;
    }

    public void setAllvarligForekomstBeskrivning(String allvarligForekomstBeskrivning) {
        this.allvarligForekomstBeskrivning = allvarligForekomstBeskrivning;
    }

    public Boolean getAllvarligForekomstTrafiken() {
        return allvarligForekomstTrafiken;
    }

    public void setAllvarligForekomstTrafiken(Boolean allvarligForekomstTrafiken) {
        this.allvarligForekomstTrafiken = allvarligForekomstTrafiken;
    }

    public String getAllvarligForekomstTrafikBeskrivning() {
        return allvarligForekomstTrafikBeskrivning;
    }

    public void setAllvarligForekomstTrafikBeskrivning(String allvarligForekomstTrafikBeskrivning) {
        this.allvarligForekomstTrafikBeskrivning = allvarligForekomstTrafikBeskrivning;
    }

    public Boolean getAllvarligForekomstVakenTid() {
        return allvarligForekomstVakenTid;
    }

    public void setAllvarligForekomstVakenTid(Boolean allvarligForekomstVakenTid) {
        this.allvarligForekomstVakenTid = allvarligForekomstVakenTid;
    }

    public InternalDate getAllvarligForekomstVakenTidObservationstid() {
        return allvarligForekomstVakenTidObservationstid;
    }

    public void setAllvarligForekomstVakenTidObservationstid(InternalDate allvarligForekomstVakenTidObservationstid) {
        this.allvarligForekomstVakenTidObservationstid = allvarligForekomstVakenTidObservationstid;
    }

    public Boolean getEgenkontrollBlodsocker() {
        return egenkontrollBlodsocker;
    }

    public void setEgenkontrollBlodsocker(Boolean egenkontrollBlodsocker) {
        this.egenkontrollBlodsocker = egenkontrollBlodsocker;
    }

}
