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

package se.inera.intyg.intygstyper.ts_diabetes.validator.internal;

import se.inera.intyg.intygstyper.ts_diabetes.model.internal.IntygAvserKategori;
import se.inera.intyg.intygstyper.ts_diabetes.model.internal.Utlatande;
import se.inera.intyg.intygstyper.ts_parent.codes.IntygAvserKod;

public class ValidationContext {

    private final Utlatande utlatande;

    public ValidationContext(Utlatande utlatande) {
        this.utlatande = utlatande;
    }

    public boolean isHogreBehorighetContext() {
        for (IntygAvserKategori intygAvser : utlatande.getIntygAvser().getKorkortstyp()) {
            IntygAvserKod intygAvserEnum = IntygAvserKod.valueOf(intygAvser.name());
            if (intygAvserEnum != null && IntygAvserKod.isHogreKorkortsbehorighet(intygAvserEnum)) {
                return true;
            }
        }
        return false;
    }
}
