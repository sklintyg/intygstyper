package se.inera.certificate.modules.fk7263.model.util;

import java.util.Arrays;
import java.util.Objects;

import com.google.common.annotations.VisibleForTesting;
import org.apache.commons.lang3.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import se.inera.certificate.model.InternalLocalDateInterval;
import se.inera.certificate.modules.fk7263.model.internal.Utlatande;
import se.inera.certificate.modules.service.WebcertModuleService;

/**
 * Util for checking a models consistency in different states.
 * 
 * @author erik
 *
 */
public class ModelCompareUtil {

    @Autowired(required = false)
    private WebcertModuleService moduleService;

    @VisibleForTesting
    void setModuleService(WebcertModuleService moduleService) {
        this.moduleService = moduleService;
    }

    /**
     * Check if two models differ.
     * 
     * @param oldUtlatande
     * @param newUtlatande
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
        return Arrays.equals(oldSjukskrivningsgrad, newSjukskrivningsgrad) ? false : true;
    }

    private int[] makeIntMatrix(Utlatande source) {
        int[] matrix = new int[4];
        matrix[0] = isValid(source.getNedsattMed100()) ? 1 : 0;
        matrix[1] = isValid(source.getNedsattMed75()) ? 1 : 0;
        matrix[2] = isValid(source.getNedsattMed50()) ? 1 : 0;
        matrix[3] = isValid(source.getNedsattMed25()) ? 1 : 0;
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
            return oldPeriod.equals(newPeriod) ? false : true;
        } else if ((isValid(oldPeriod) && !isValid(newPeriod)) || (!isValid(oldPeriod) && isValid(newPeriod))) {
            return true;
        } else {
            return false;
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
        boolean oldValid = moduleService.validateDiagnosisCode(oldDiagnosKod.diagnosKod, oldDiagnosKod.diagnosKodSystem);
        boolean newValid = moduleService.validateDiagnosisCode(newDiagnosKod.diagnosKod, newDiagnosKod.diagnosKodSystem);

        if (oldValid != newValid) {
            return true;
        }
        if (oldValid && newValid && !oldDiagnosKod.equals(newDiagnosKod)) {
            return true;
        }
        return false;
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
        public final String diagnosKod;
        public final String diagnosKodSystem;

        public DiagnosKod(String diagnosKod, String diagnosKodSystem) {
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

            return Objects.equals(diagnosKod, that.diagnosKod) &&
                    Objects.equals(diagnosKodSystem, that.diagnosKodSystem);
        }

        @Override
        public int hashCode() {
            return Objects.hash(diagnosKod, diagnosKodSystem);
        }

    }

}
