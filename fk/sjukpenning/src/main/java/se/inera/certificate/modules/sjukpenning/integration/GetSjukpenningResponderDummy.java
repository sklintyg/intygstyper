package se.inera.certificate.modules.sjukpenning.integration;

import se.riv.clinicalprocess.healthcond.certificate.getCertificate.v1.GetCertificateResponderInterface;
import se.riv.clinicalprocess.healthcond.certificate.getCertificate.v1.GetCertificateResponseType;
import se.riv.clinicalprocess.healthcond.certificate.getCertificate.v1.GetCertificateType;

public class GetSjukpenningResponderDummy implements GetCertificateResponderInterface {

    @Override
    public GetCertificateResponseType getCertificate(String s, GetCertificateType getCertificateType) {
        return null;
    }

}
