package se.inera.certificate.modules.fk7263.rest;

import se.inera.certificate.clinicalprocess.healthcond.certificate.getmedicalcertificateforcare.v1.GetMedicalCertificateForCareRequestType;
import se.inera.certificate.clinicalprocess.healthcond.certificate.getmedicalcertificateforcare.v1.GetMedicalCertificateForCareResponderInterface;
import se.inera.certificate.clinicalprocess.healthcond.certificate.getmedicalcertificateforcare.v1.GetMedicalCertificateForCareResponseType;

public class GetMedicalCertificateForCareResponderInterfaceNoop implements GetMedicalCertificateForCareResponderInterface {

    @Override
    public GetMedicalCertificateForCareResponseType getMedicalCertificateForCare(String logicalAddress,
            GetMedicalCertificateForCareRequestType parameters) {
        throw new RuntimeException("Not implemented in this context");
    }

}
