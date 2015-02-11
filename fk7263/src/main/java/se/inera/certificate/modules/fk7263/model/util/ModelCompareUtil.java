package se.inera.certificate.modules.fk7263.model.util;

import java.util.Arrays;
import java.util.Objects;

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
        matrix[0] = source.getNedsattMed100() != null ? 1 : 0;
        matrix[1] = source.getNedsattMed75() != null ? 1 : 0;
        matrix[2] = source.getNedsattMed50() != null ? 1 : 0;
        matrix[3] = source.getNedsattMed25() != null ? 1 : 0;
        return matrix;
    }

    private boolean sjukskrivningsperiodDiffers(Utlatande oldUtlatande, Utlatande newUtlatande) {
        return checkPerioderDiffers(oldUtlatande.getNedsattMed100(), newUtlatande.getNedsattMed100())
                || checkPerioderDiffers(oldUtlatande.getNedsattMed75(), newUtlatande.getNedsattMed75())
                || checkPerioderDiffers(oldUtlatande.getNedsattMed50(), newUtlatande.getNedsattMed50())
                || checkPerioderDiffers(oldUtlatande.getNedsattMed25(), newUtlatande.getNedsattMed25());

    }

    private boolean checkPerioderDiffers(InternalLocalDateInterval oldPeriod, InternalLocalDateInterval newPeriod) {
        if (oldPeriod != null && newPeriod != null) {
            return oldPeriod.equals(newPeriod) ? false : true;
        } else if ((oldPeriod == null && newPeriod != null) || (oldPeriod != null && newPeriod == null)) {
            return true;
        } else {
            return false;
        }
    }

    private boolean diagnoseDiffers(Utlatande oldUtlatande, Utlatande newUtlatande) {
        DiagnosKod[] diagnoskoderOld = { new DiagnosKod(oldUtlatande.getDiagnosKod(), oldUtlatande.getDiagnosKodsystem1()),
                new DiagnosKod(oldUtlatande.getDiagnosKod2(), oldUtlatande.getDiagnosKodsystem2()),
                new DiagnosKod(oldUtlatande.getDiagnosKod3(), oldUtlatande.getDiagnosKodsystem3()) };
        DiagnosKod[] diagnoskoderNew = { new DiagnosKod(newUtlatande.getDiagnosKod(), newUtlatande.getDiagnosKodsystem1()),
                new DiagnosKod(newUtlatande.getDiagnosKod2(), newUtlatande.getDiagnosKodsystem2()),
                new DiagnosKod(newUtlatande.getDiagnosKod3(), newUtlatande.getDiagnosKodsystem3()) };

        String[] diagnosbeskrivningOld = { oldUtlatande.getDiagnosBeskrivning1(), oldUtlatande.getDiagnosBeskrivning2(),
                oldUtlatande.getDiagnosBeskrivning3() };
        String[] diagnosbeskrivningNew = { newUtlatande.getDiagnosBeskrivning1(), newUtlatande.getDiagnosBeskrivning2(),
                newUtlatande.getDiagnosBeskrivning3() };

        return diagnoseCodesDiffer(diagnoskoderOld, diagnoskoderNew) || diagnoseBeskrivningsDiffer(diagnosbeskrivningOld, diagnosbeskrivningNew);
    }

    private boolean diagnoseCodesDiffer(DiagnosKod[] oldArray, DiagnosKod[] newArray) {
        for (int i = 0; i < oldArray.length; i++) {
            DiagnosKod oldDiagnosKod = oldArray[i];
            DiagnosKod newDiagnosKod = newArray[i];
            boolean oldValid = moduleService.validateDiagnosisCode(oldDiagnosKod.diagnosKod, oldDiagnosKod.diagnosKodSystem);
            boolean newValid = moduleService.validateDiagnosisCode(newDiagnosKod.diagnosKod, newDiagnosKod.diagnosKodSystem);

            if (oldValid != newValid) {
                return true;
            }
            if (oldValid && newValid && !oldDiagnosKod.equals(newDiagnosKod)) {
                return true;
            }
        }
        return false;
    }

    private boolean diagnoseBeskrivningsDiffer(String[] oldArray, String[] newArray) {
        for (int i = 0; i < oldArray.length; i++) {
            if ((!StringUtils.isEmpty(oldArray[i]) && StringUtils.isEmpty(newArray[i]))
                    || (StringUtils.isEmpty(oldArray[i]) && !StringUtils.isEmpty(newArray[i]))) {
                return true;
            } else if (oldArray[i] != null && newArray[i] != null) {
                return oldArray[i].equals(newArray[i]) ? false : true;
            }
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
