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

package se.inera.intyg.intygstyper.lisjp.model.util;

import se.inera.intyg.common.support.model.InternalLocalDateInterval;
import se.inera.intyg.common.support.model.util.ModelCompareUtil;
import se.inera.intyg.intygstyper.fkparent.model.util.FkParentModelCompareUtil;
import se.inera.intyg.intygstyper.lisjp.model.internal.LisjpUtlatande;
import se.inera.intyg.intygstyper.lisjp.model.internal.Sjukskrivning;

public class LisjpModelCompareUtil extends FkParentModelCompareUtil implements ModelCompareUtil<LisjpUtlatande> {

    @Override
    public boolean isValidForNotification(LisjpUtlatande utlatande) {
        return diagnosesAreValid(utlatande)
                && datesAreValid(utlatande.getUndersokningAvPatienten(), utlatande.getTelefonkontaktMedPatienten(), utlatande.getJournaluppgifter(),
                        utlatande.getAnnatGrundForMU())
                && sjukskrivningarAreValid(utlatande);
    }

    private boolean sjukskrivningarAreValid(LisjpUtlatande utlatande) {
        for (Sjukskrivning s : utlatande.getSjukskrivningar()) {
            if (!isValid(s.getPeriod())) {
                return false;
            }
        }
        return true;
    }

    private boolean isValid(InternalLocalDateInterval period) {
        return period == null || period.isValid();
    }
}
