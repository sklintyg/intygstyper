package se.inera.certificate.modules.sjukpenning.integration;

import riv.clinicalprocess.healthcond.certificate.getcertificateresponder._1.GetCertificateResponseType;
import riv.clinicalprocess.healthcond.certificate.getcertificateresponder._1.GetCertificateType;
import se.riv.clinicalprocess.healthcond.certificate.getCertificate.v1.GetCertificateResponderInterface;

public class GetSjukpenningResponderDummy implements GetCertificateResponderInterface {

    @Override
    public GetCertificateResponseType getCertificate(String s, GetCertificateType getCertificateType) {
        return null;
    }

}
