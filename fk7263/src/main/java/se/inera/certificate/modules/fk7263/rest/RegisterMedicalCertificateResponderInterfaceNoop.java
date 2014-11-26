package se.inera.certificate.modules.fk7263.rest;

import org.w3.wsaddressing10.AttributedURIType;

import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificate.v3.rivtabp20.RegisterMedicalCertificateResponderInterface;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificateresponder.v3.RegisterMedicalCertificateResponseType;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificateresponder.v3.RegisterMedicalCertificateType;

/**
 * Noop implementation of WS client interface, needed for autowiring by other components but not relevant in all contexts.
 */
public class RegisterMedicalCertificateResponderInterfaceNoop implements RegisterMedicalCertificateResponderInterface {

    @Override
    public RegisterMedicalCertificateResponseType registerMedicalCertificate(AttributedURIType logicalAddress,
            RegisterMedicalCertificateType parameters) {
        throw new RuntimeException("Not implemented in this context");
    }

}
