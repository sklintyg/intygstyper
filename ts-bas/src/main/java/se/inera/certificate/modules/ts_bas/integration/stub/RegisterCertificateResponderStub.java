package se.inera.certificate.modules.ts_bas.integration.stub;

import java.util.HashMap;
import java.util.Map;

import javax.xml.ws.WebServiceProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import se.inera.certificate.clinicalprocess.healthcond.certificate.registerCertificate.v1.RegisterCertificateResponderInterface;
import se.inera.certificate.clinicalprocess.healthcond.certificate.registerCertificate.v1.RegisterCertificateResponseType;
import se.inera.certificate.clinicalprocess.healthcond.certificate.registerCertificate.v1.RegisterCertificateType;
import se.inera.certificate.clinicalprocess.healthcond.certificate.utils.ResultTypeUtil;
import se.inera.certificate.clinicalprocess.healthcond.certificate.v1.Utlatande;
import se.inera.certificate.validate.CertificateValidationException;

/**
 * @author par.wenaker
 */
@Transactional
@WebServiceProvider(targetNamespace = "urn:riv:clinicalprocess:healthcond:certificate:RegisterCertificateResponder:1", serviceName = "RegisterCertificateResponderService", wsdlLocation = "schemas/v3/RegisterMedicalCertificateInteraction/RegisterMedicalCertificateInteraction_3.1_rivtabp20.wsdl")
public class RegisterCertificateResponderStub implements RegisterCertificateResponderInterface {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegisterCertificateResponderStub.class);
    
    @Autowired
    private TSCertificateStore tsCertificatesStore;

    protected void validateTransport(RegisterCertificateType registerCertificate) throws CertificateValidationException {
        // TODO do something here
    }

    @Override
    public RegisterCertificateResponseType registerCertificate(String logicalAddress, RegisterCertificateType request) {
        RegisterCertificateResponseType response = new RegisterCertificateResponseType();

        Utlatande utlatande = request.getUtlatande();
        String id = utlatande.getUtlatandeId().getExtension();

        Map<String, String> props = new HashMap<>();
        props.put("Personnummer", utlatande.getPatient().getPersonId().getExtension());
        props.put("Makulerad", "NEJ");

        LOGGER.info("STUB Received request");
        tsCertificatesStore.addCertificate(id, props);
        response.setResult(ResultTypeUtil.okResult());
        return response;
    }

}
