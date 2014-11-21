package se.inera.certificate.modules.fk7263.integration.stub;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.xml.ws.WebServiceProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.w3.wsaddressing10.AttributedURIType;

import se.inera.certificate.integration.module.ModuleApiFactory;
import se.inera.certificate.integration.module.exception.ModuleNotFoundException;
import se.inera.certificate.model.converter.util.ConverterException;
import se.inera.certificate.modules.fk7263.model.converter.TransportToInternal;
import se.inera.certificate.modules.fk7263.model.internal.Utlatande;
import se.inera.certificate.modules.fk7263.validator.InternalValidator;
import se.inera.certificate.modules.fk7263.validator.ProgrammaticTransportValidator;
import se.inera.certificate.modules.support.api.ModuleApi;
import se.inera.certificate.validate.CertificateValidationException;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificate.v3.rivtabp20.RegisterMedicalCertificateResponderInterface;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificateresponder.v3.RegisterMedicalCertificateResponseType;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificateresponder.v3.RegisterMedicalCertificateType;
import se.inera.ifv.insuranceprocess.healthreporting.utils.ResultOfCallUtil;

/**
 * @author par.wenaker
 */
@Transactional
@WebServiceProvider(targetNamespace = "urn:riv:insuranceprocess:healthreporting:RegisterMedicalCertificate:3:rivtabp20", serviceName = "RegisterMedicalCertificateResponderService", wsdlLocation = "schemas/v3/RegisterMedicalCertificateInteraction/RegisterMedicalCertificateInteraction_3.1_rivtabp20.wsdl")
public class RegisterMedicalCertificateResponderStub implements RegisterMedicalCertificateResponderInterface {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegisterMedicalCertificateResponderStub.class);
    private static final String FK7263 = "fk7263";
    
    private ModuleApi moduleApi;

    @Autowired
    private FkMedicalCertificatesStore fkMedicalCertificatesStore;

    @Autowired
    private ModuleApiFactory moduleApiFactory;

    @PostConstruct
    public void initializeJModuleApi() throws ModuleNotFoundException {
        // Since only FK7263 uses RegisterMedicalCertificateType we can hard code it here.
        moduleApi = moduleApiFactory.getModuleEntryPoint(FK7263).getModuleApi();
    }

    @Override
    public RegisterMedicalCertificateResponseType registerMedicalCertificate(AttributedURIType logicalAddress, RegisterMedicalCertificateType request) {

        RegisterMedicalCertificateResponseType response = new RegisterMedicalCertificateResponseType();

        try {
            validateTransport(request);
            Utlatande utlatande = TransportToInternal.convert(request.getLakarutlatande());
            validateInternal(utlatande);
            String id = utlatande.getId();

            Map<String, String> props = new HashMap<>();
            props.put("Personnummer", utlatande.getIntygMetadata().getPatient().getPersonId());
            props.put("Makulerad", "NEJ");

            LOGGER.info("STUB Received request");
            fkMedicalCertificatesStore.addCertificate(id, props);
        } catch (CertificateValidationException e) {
            response.setResult(ResultOfCallUtil.failResult(e.getMessage()));
            return response;
        } catch (ConverterException e) {
            response.setResult(ResultOfCallUtil.failResult("Unable to convert certificate to internal format"));
            return response;
        }
        response.setResult(ResultOfCallUtil.okResult());
        return response;
    }

    protected void validateTransport(RegisterMedicalCertificateType registerMedicalCertificate) throws CertificateValidationException {
        List<String> validationErrors = new ProgrammaticTransportValidator(registerMedicalCertificate.getLakarutlatande()).validate();
        if (!validationErrors.isEmpty()) {
            throw new CertificateValidationException(validationErrors);
        }
    }

    protected void validateInternal(Utlatande utlatande) throws CertificateValidationException {
        List<String> validationErrors = new InternalValidator(utlatande).validate();
        if (!validationErrors.isEmpty()) {
            throw new CertificateValidationException(validationErrors);
        }
    }

}
