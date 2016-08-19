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

package se.inera.intyg.intygstyper.fkparent.model.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import se.inera.intyg.common.support.model.InternalDate;
import se.inera.intyg.common.support.modules.service.WebcertModuleService;
import se.inera.intyg.intygstyper.fkparent.model.internal.Diagnos;
import se.inera.intyg.intygstyper.fkparent.model.internal.SitUtlatande;

public abstract class FkParentModelCompareUtil {

    @Autowired(required = false)
    protected WebcertModuleService moduleService;

    public boolean diagnosesAreValid(SitUtlatande utlatande) {
        for (Diagnos newDiagnos :  utlatande.getDiagnoser()) {
            if (!diagnoseCodeValid(newDiagnos)) {
                return false;
            }
        }
        return true;
    }

    public boolean datesAreValid(InternalDate... dates) {
        for (InternalDate date : dates) {
            if (!isValid(date)) {
                return false;
            }
        }
        return true;
    }

    private boolean isValid(InternalDate date) {
        return date == null || date.isValidDate();
    }

    private boolean diagnoseCodeValid(Diagnos diagnosKod) {
        if (StringUtils.isBlank(diagnosKod.getDiagnosKod())) {
            return true;
        } else {
            return moduleService.validateDiagnosisCode(diagnosKod.getDiagnosKod(), diagnosKod.getDiagnosKodSystem());
        }
    }
}
