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

package se.inera.intyg.intygstyper.ts_diabetes.model.util;

import se.inera.intyg.common.support.model.util.ModelCompareUtil;
import se.inera.intyg.intygstyper.ts_diabetes.model.internal.Utlatande;

public class TsDiabetesModelCompareUtil implements ModelCompareUtil<Utlatande> {

    @Override
    public boolean isValidForNotification(Utlatande utlatande) {
        // Until someone actually specifies metrics for this, we assume TS-diabetes is always valid for change notifications.
        return true;
    }

}
