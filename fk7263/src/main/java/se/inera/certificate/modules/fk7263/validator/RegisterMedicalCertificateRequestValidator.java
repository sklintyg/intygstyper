package se.inera.certificate.modules.fk7263.validator;

import java.util.ArrayList;
import java.util.List;

import se.inera.certificate.validate.CertificateValidationException;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificateresponder.v3.RegisterMedicalCertificateType;

public class RegisterMedicalCertificateRequestValidator {
    private RegisterMedicalCertificateType registerRequest = null;
    private List<String> validationErrors = new ArrayList<>();

    public RegisterMedicalCertificateRequestValidator(RegisterMedicalCertificateType registerMedicalCertificate) {
        this.registerRequest = registerMedicalCertificate;
    }

    public void validateAndCorrect() throws CertificateValidationException {
        // use commmon validators for common elements
        new LakarUtlatandeTypeValidator(registerRequest.getLakarutlatande(), validationErrors).validateAndCorrect();

        if (!validationErrors.isEmpty()) {
            throw new CertificateValidationException(validationErrors);
        }
    }
}
