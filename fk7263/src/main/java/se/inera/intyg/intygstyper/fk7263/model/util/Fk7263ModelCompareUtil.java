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

package se.inera.intyg.intygstyper.fk7263.model.util;

import java.util.Arrays;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import se.inera.intyg.common.support.model.InternalLocalDateInterval;
import se.inera.intyg.common.support.modules.service.WebcertModuleService;
import se.inera.intyg.intygstyper.fk7263.model.internal.Utlatande;

/**
 * Util for checking a models consistency in different states.
 *
 * @author erik
 *
 */
public class Fk7263ModelCompareUtil {

    @Autowired(required = false)
    private WebcertModuleService moduleService;

    /**
     * Check if two models differ.
     * @return true if they do, false otherwise
     */
    public boolean modelDiffers(Utlatande oldUtlatande, Utlatande newUtlatande) {
        return diagnoseDiffers(oldUtlatande, newUtlatande)
                || sjukskrivningsperiodDiffers(oldUtlatande, newUtlatande)
                || sjukskrivningsgradDiffers(oldUtlatande, newUtlatande);
    }

    private boolean sjukskrivningsgradDiffers(Utlatande oldUtlatande, Utlatande newUtlatande) {
        int[] oldSjukskrivningsgrad = makeIntMatrix(oldUtlatande);
        int[] newSjukskrivningsgrad = makeIntMatrix(newUtlatande);
        return !Arrays.equals(oldSjukskrivningsgrad, newSjukskrivningsgrad);
    }

    private int[] makeIntMatrix(Utlatande source) {
        final int totalNumberOfNedsattMedValues = 4;
        final int indexNedsattMed100 = 0;
        final int indexNedsattMed75 = 1;
        final int indexNedsattMed50 = 2;
        final int indexNedsattMed25 = 3;

        int[] matrix = new int[totalNumberOfNedsattMedValues];
        matrix[indexNedsattMed100] = isValid(source.getNedsattMed100()) ? 1 : 0;
        matrix[indexNedsattMed75] = isValid(source.getNedsattMed75()) ? 1 : 0;
        matrix[indexNedsattMed50] = isValid(source.getNedsattMed50()) ? 1 : 0;
        matrix[indexNedsattMed25] = isValid(source.getNedsattMed25()) ? 1 : 0;
        return matrix;
    }

    private boolean sjukskrivningsperiodDiffers(Utlatande oldUtlatande, Utlatande newUtlatande) {
        return checkPerioderDiffers(oldUtlatande.getNedsattMed100(), newUtlatande.getNedsattMed100())
                || checkPerioderDiffers(oldUtlatande.getNedsattMed75(), newUtlatande.getNedsattMed75())
                || checkPerioderDiffers(oldUtlatande.getNedsattMed50(), newUtlatande.getNedsattMed50())
                || checkPerioderDiffers(oldUtlatande.getNedsattMed25(), newUtlatande.getNedsattMed25());

    }

    private boolean checkPerioderDiffers(InternalLocalDateInterval oldPeriod, InternalLocalDateInterval newPeriod) {
        if (isValid(oldPeriod) && isValid(newPeriod)) {
            return !oldPeriod.equals(newPeriod);
        } else {
            return (isValid(oldPeriod) && !isValid(newPeriod)) || (!isValid(oldPeriod) && isValid(newPeriod));
        }
    }

    private boolean isValid(InternalLocalDateInterval period) {
        return period != null && period.isValid();
    }

    private boolean diagnoseDiffers(Utlatande oldUtlatande, Utlatande newUtlatande) {
        DiagnosKod diagnoskodOld = new DiagnosKod(oldUtlatande.getDiagnosKod(), oldUtlatande.getDiagnosKodsystem1());
        DiagnosKod diagnoskodNew = new DiagnosKod(newUtlatande.getDiagnosKod(), newUtlatande.getDiagnosKodsystem1());

        String diagnosbeskrivningOld = oldUtlatande.getDiagnosBeskrivning1();
        String diagnosbeskrivningNew = newUtlatande.getDiagnosBeskrivning1();

        return diagnoseCodeDiffer(diagnoskodOld, diagnoskodNew) || diagnoseBeskrivningDiffer(diagnosbeskrivningOld, diagnosbeskrivningNew);
    }

    private boolean diagnoseCodeDiffer(DiagnosKod oldDiagnosKod, DiagnosKod newDiagnosKod) {
        boolean oldValid = moduleService.validateDiagnosisCode(oldDiagnosKod.getDiagnosKod(), oldDiagnosKod.getDiagnosKodSystem());
        boolean newValid = moduleService.validateDiagnosisCode(newDiagnosKod.getDiagnosKod(), newDiagnosKod.getDiagnosKodSystem());

        if (oldValid != newValid) {
            return true;
        }
        return oldValid && newValid && !oldDiagnosKod.equals(newDiagnosKod);
    }

    private boolean diagnoseBeskrivningDiffer(String oldBeskrivning, String newBeskrivning) {
        if ((!StringUtils.isEmpty(oldBeskrivning) && StringUtils.isEmpty(newBeskrivning))
                || (StringUtils.isEmpty(oldBeskrivning) && !StringUtils.isEmpty(newBeskrivning))) {
            return true;
        } else if (oldBeskrivning != null && newBeskrivning != null && !oldBeskrivning.equals(newBeskrivning)) {
            return true;
        }
        return false;
    }

    private static final class DiagnosKod {
        private final String diagnosKod;
        private final String diagnosKodSystem;

        DiagnosKod(String diagnosKod, String diagnosKodSystem) {
            this.diagnosKod = diagnosKod;
            this.diagnosKodSystem = diagnosKodSystem;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            DiagnosKod that = (DiagnosKod) o;

            return Objects.equals(getDiagnosKod(), that.getDiagnosKod())
                    && Objects.equals(getDiagnosKodSystem(), that.getDiagnosKodSystem());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getDiagnosKod(), getDiagnosKodSystem());
        }

        public String getDiagnosKod() {
            return diagnosKod;
        }

        public String getDiagnosKodSystem() {
            return diagnosKodSystem;
        }

    }

}
