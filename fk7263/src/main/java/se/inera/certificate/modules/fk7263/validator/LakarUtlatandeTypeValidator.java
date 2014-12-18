package se.inera.certificate.modules.fk7263.validator;

import java.util.List;

import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.LakarutlatandeType;
import se.inera.ifv.insuranceprocess.healthreporting.v2.PatientType;
import se.inera.ifv.insuranceprocess.healthreporting.validate.PatientValidator;

/**
 * @author andreaskaltenbach
 */
public class LakarUtlatandeTypeValidator {

    private LakarutlatandeType lakarUtlatandeType;
    private List<String> validationErrors = null;

    public LakarUtlatandeTypeValidator(LakarutlatandeType lakarutlatandeType, List<String> validationErrors) {
        this.lakarUtlatandeType = lakarutlatandeType;
        this.validationErrors = validationErrors;
    }

    public void validateAndCorrect() {
        if (lakarUtlatandeType.getLakarutlatandeId() == null || lakarUtlatandeType.getLakarutlatandeId().isEmpty()) {
            validationErrors.add("No Lakarutlatande Id found!");
        }
        validateAndCorrectPatient();
    }

    private void validateAndCorrectPatient() {
        PatientType patient = lakarUtlatandeType.getPatient();
        validationErrors.addAll(PatientValidator.validateAndCorrect(patient));
    }

}