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

package se.inera.intyg.intygstyper.ts_bas.model.internal;

public class Sjukhusvard {
    //har patienten vårdats på sjukhus eller haft kontakt med läkare med anledning av punkterna 1-13
    private Boolean sjukhusEllerLakarkontakt;

    private String tidpunkt;

    private String vardinrattning;

    private String anledning;

    public Boolean getSjukhusEllerLakarkontakt() {
        return sjukhusEllerLakarkontakt;
    }

    public void setSjukhusEllerLakarkontakt(Boolean sjukhusEllerLakarkontakt) {
        this.sjukhusEllerLakarkontakt = sjukhusEllerLakarkontakt;
    }

    public String getTidpunkt() {
        return tidpunkt;
    }

    public void setTidpunkt(String tidpunkt) {
        this.tidpunkt = tidpunkt;
    }

    public String getVardinrattning() {
        return vardinrattning;
    }

    public void setVardinrattning(String vardinrattning) {
        this.vardinrattning = vardinrattning;
    }

    public String getAnledning() {
        return anledning;
    }

    public void setAnledning(String anledning) {
        this.anledning = anledning;
    }

}
