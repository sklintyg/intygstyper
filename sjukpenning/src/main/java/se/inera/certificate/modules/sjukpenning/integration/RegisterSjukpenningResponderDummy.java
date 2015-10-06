package se.inera.certificate.modules.sjukpenning.integration;

import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v2.RegisterCertificateResponderInterface;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v2.RegisterCertificateResponseType;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v2.RegisterCertificateType;

public class RegisterSjukpenningResponderDummy implements RegisterCertificateResponderInterface {

    @Override
    public RegisterCertificateResponseType registerCertificate(String s, RegisterCertificateType registerCertificateType) {
        return null;
    }

}
