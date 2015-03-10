package se.inera.certificate.modules.fk7263.integration;

import java.io.StringWriter;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.w3.wsaddressing10.AttributedURIType;

import se.inera.certificate.integration.module.exception.CertificateAlreadyExistsException;
import se.inera.certificate.logging.LogMarkers;
import se.inera.certificate.model.converter.util.ConverterException;
import se.inera.certificate.modules.fk7263.model.converter.TransportToInternal;
import se.inera.certificate.modules.fk7263.model.converter.util.ConverterUtil;
import se.inera.certificate.modules.fk7263.model.internal.Utlatande;
import se.inera.certificate.modules.fk7263.rest.Fk7263ModuleApi;
import se.inera.certificate.modules.fk7263.validator.ProgrammaticTransportValidator;
import se.inera.certificate.modules.support.api.CertificateHolder;
import se.inera.certificate.validate.CertificateValidationException;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificate.v3.rivtabp20.RegisterMedicalCertificateResponderInterface;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificateresponder.v3.ObjectFactory;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificateresponder.v3.RegisterMedicalCertificateResponseType;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificateresponder.v3.RegisterMedicalCertificateType;
import se.inera.ifv.insuranceprocess.healthreporting.utils.ResultOfCallUtil;

import com.google.common.base.Throwables;

/**
 *
 */
public class RegisterMedicalCertificateResponderImpl implements RegisterMedicalCertificateResponderInterface {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegisterMedicalCertificateResponderImpl.class);

    private static final String FK7263 = "fk7263";

    private boolean wireTapped = false;
    
    private ObjectFactory objectFactory;

    private JAXBContext jaxbContext;

    @Autowired
    private Fk7263ModuleApi moduleApi;

    @Autowired
    private ConverterUtil converterUtil;

    public void setConverterUtil(ConverterUtil converterUtil) {
        this.converterUtil = converterUtil;
    }

    @PostConstruct
    public void initializeJaxbContext() throws JAXBException {
        jaxbContext = JAXBContext.newInstance(RegisterMedicalCertificateType.class);
        objectFactory = new ObjectFactory();
    }

    public boolean isWireTapped() {
        return wireTapped;
    }

    public void setWireTapped(boolean wireTapped) {
        this.wireTapped = wireTapped;
    }

    @Override
    public RegisterMedicalCertificateResponseType registerMedicalCertificate(AttributedURIType logicalAddress,
            RegisterMedicalCertificateType registerMedicalCertificate) {
        RegisterMedicalCertificateResponseType response = new RegisterMedicalCertificateResponseType();

        try {
            validateTransport(registerMedicalCertificate);
            Utlatande utlatande = TransportToInternal.convert(registerMedicalCertificate.getLakarutlatande());

            String xml = xmlToString(registerMedicalCertificate);
            CertificateHolder certificateHolder = converterUtil.toCertificateHolder(utlatande);
            certificateHolder.setOriginalCertificate(xml);
            certificateHolder.setWireTapped(wireTapped);

            moduleApi.getModuleContainer().certificateReceived(certificateHolder);

            response.setResult(ResultOfCallUtil.okResult());

        } catch (CertificateAlreadyExistsException e) {
            response.setResult(ResultOfCallUtil.infoResult("Certificate already exists"));
            String certificateId = registerMedicalCertificate.getLakarutlatande().getLakarutlatandeId();
            String issuedBy =  registerMedicalCertificate.getLakarutlatande().getSkapadAvHosPersonal().getEnhet().getEnhetsId().getExtension();
            LOGGER.warn(LogMarkers.VALIDATION, "Validation warning for intyg " + certificateId + " issued by " + issuedBy + ": Certificate already exists - ignored.");

        } catch (CertificateValidationException e) {
            response.setResult(ResultOfCallUtil.failResult(e.getMessage()));
            LOGGER.error(LogMarkers.VALIDATION, e.getMessage());

        } catch (ConverterException e) {
            response.setResult(ResultOfCallUtil.failResult(e.getMessage()));
            LOGGER.error(LogMarkers.VALIDATION, e.getMessage());

        } catch (JAXBException e) {
            LOGGER.error("JAXB error in Webservice: ", e);
            Throwables.propagate(e);

        } catch (Exception e) {
            LOGGER.error("Error in Webservice: ", e);
            Throwables.propagate(e);
        }

        return response;
    }

    private void validateTransport(RegisterMedicalCertificateType registerMedicalCertificate) throws CertificateValidationException {
        List<String> validationErrors = new ProgrammaticTransportValidator(registerMedicalCertificate.getLakarutlatande()).validate();
        if (!validationErrors.isEmpty()) {
            throw new CertificateValidationException(validationErrors);
        }
    }

    private String xmlToString(RegisterMedicalCertificateType registerMedicalCertificate) throws JAXBException {
        StringWriter stringWriter = new StringWriter();
        JAXBElement<RegisterMedicalCertificateType> requestElement = objectFactory
                .createRegisterMedicalCertificate(registerMedicalCertificate);
        jaxbContext.createMarshaller().marshal(requestElement, stringWriter);
        return stringWriter.toString();
    }
}
