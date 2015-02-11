package se.inera.certificate.modules.fk7263.model.util;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;

import se.inera.certificate.model.InternalLocalDateInterval;
import se.inera.certificate.modules.fk7263.model.internal.Utlatande;

/**
 * Util for checking a models consistency in different states.
 * @author erik
 *
 */
public class ModelCompareUtil {

    /**
     * Check if two models differ.
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
        return Arrays.equals(oldSjukskrivningsgrad,newSjukskrivningsgrad) ? false : true;
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
            || checkPerioderDiffers(oldUtlatande.getNedsattMed75(),newUtlatande.getNedsattMed75())
            || checkPerioderDiffers(oldUtlatande.getNedsattMed50(),newUtlatande.getNedsattMed50())
            || checkPerioderDiffers(oldUtlatande.getNedsattMed25(),newUtlatande.getNedsattMed25());
        
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
        boolean differs = false;
        String[] diagnoskoderOld = {oldUtlatande.getDiagnosKod(), oldUtlatande.getDiagnosKod2(), oldUtlatande.getDiagnosKod3()}; 
        String[] diagnoskoderNew = {newUtlatande.getDiagnosKod(), newUtlatande.getDiagnosKod2(), newUtlatande.getDiagnosKod3()}; 

        String[] diagnosbeskrivningOld = {oldUtlatande.getDiagnosBeskrivning1(), oldUtlatande.getDiagnosBeskrivning2(), oldUtlatande.getDiagnosBeskrivning3()}; 
        String[] diagnosbeskrivningNew = {newUtlatande.getDiagnosBeskrivning1(), newUtlatande.getDiagnosBeskrivning2(), newUtlatande.getDiagnosBeskrivning3()}; 

        differs = checkStringArray(diagnoskoderOld, diagnoskoderNew) || checkStringArray(diagnosbeskrivningOld, diagnosbeskrivningNew);

        return differs;
    }

    private boolean checkStringArray(String[] oldArray, String[] newArray) {
        for (int i = 0; i < oldArray.length; i++) {
            if ((!StringUtils.isEmpty(oldArray[i]) && StringUtils.isEmpty(newArray[i])) || (StringUtils.isEmpty(oldArray[i]) && !StringUtils.isEmpty(newArray[i]))) {
                return true;
            } else if (oldArray[i] != null && newArray[i] != null) {
                return oldArray[i].equals(newArray[i]) ? false : true;
            } 
        }
        return false;
    }
}
