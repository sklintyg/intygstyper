package se.inera.certificate.modules.ts_diabetes.integration.stub;

import javax.xml.ws.WebServiceProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.inera.certificate.clinicalprocess.healthcond.certificate.registerCertificate.v1.RegisterCertificateResponderInterface;
import se.inera.certificate.clinicalprocess.healthcond.certificate.registerCertificate.v1.RegisterCertificateResponseType;
import se.inera.certificate.clinicalprocess.healthcond.certificate.registerCertificate.v1.RegisterCertificateType;

@WebServiceProvider(targetNamespace = "urn:riv:clinicalprocess:healthcond:certificate:RegisterCertificateResponder:1::rivtabp21", serviceName = "RegisterCertificateResponderService", wsdlLocation = "schemas/v1/RegisterCertificateInteraction/RegisterCertificateInteraction_1.0_rivtabp21.wsdl")
public class RegisterCertificateResponderStub implements RegisterCertificateResponderInterface {
    
    private Logger LOGGER = LoggerFactory.getLogger(RegisterCertificateResponderStub.class);

    @Override
    public RegisterCertificateResponseType registerCertificate(String logicalAddress, RegisterCertificateType parameters) {
        LOGGER.info("Diabetes - adsasdasd");
        return null;
    }


}
