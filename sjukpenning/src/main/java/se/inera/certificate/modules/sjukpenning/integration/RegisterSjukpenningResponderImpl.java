package se.inera.certificate.modules.sjukpenning.integration;


import java.io.StringWriter;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import se.inera.certificate.integration.module.exception.CertificateAlreadyExistsException;
import se.inera.certificate.logging.LogMarkers;
import se.inera.certificate.model.converter.util.ConverterException;
import se.inera.certificate.modules.sjukpenning.model.converter.TransportToInternal;
import se.inera.certificate.modules.sjukpenning.model.converter.util.ConverterUtil;
import se.inera.certificate.modules.sjukpenning.model.internal.SjukpenningUtlatande;
import se.inera.certificate.modules.sjukpenning.rest.SjukpenningModuleApi;
import se.inera.certificate.modules.sjukpenning.validator.TransportValidator;
import se.inera.certificate.modules.support.api.CertificateHolder;
import se.inera.certificate.validate.CertificateValidationException;
import se.inera.intygstjanster.fk.services.registersjukpenningresponder.v1.RegisterSjukpenningResponderInterface;
import se.inera.intygstjanster.fk.services.registersjukpenningresponder.v1.RegisterSjukpenningResponseType;
import se.inera.intygstjanster.fk.services.registersjukpenningresponder.v1.RegisterSjukpenningType;
import se.inera.intygstjanster.fk.services.v1.ErrorIdType;
import se.inera.intygstjanster.fk.services.v1.ObjectFactory;
import se.inera.intygstjanster.fk.services.v1.ResultatTyp;
import se.inera.intygstjanster.fk.services.v1.SjukpenningIntyg;

import com.google.common.base.Throwables;

public class RegisterSjukpenningResponderImpl implements RegisterSjukpenningResponderInterface {

    @Autowired
    TransportValidator validator;

    @Autowired
    private ConverterUtil converterUtil;

    @Autowired 
    private SjukpenningModuleApi moduleApi;

    private ObjectFactory objectFactory;
    private JAXBContext jaxbContext;

    private static final Logger LOGGER = LoggerFactory.getLogger(RegisterSjukpenningResponderImpl.class);

    @PostConstruct
    public void initializeJaxbContext() throws JAXBException {
        jaxbContext = JAXBContext.newInstance(RegisterSjukpenningType.class);
        objectFactory = new ObjectFactory();
    }

    @Override
    public RegisterSjukpenningResponseType registerSjukpenning(String logicalAddress, RegisterSjukpenningType registerSjukpenning) {
        RegisterSjukpenningResponseType response = new RegisterSjukpenningResponseType();

        try {
            validateTransport(registerSjukpenning);
            SjukpenningUtlatande utlatande = TransportToInternal.convert(registerSjukpenning.getIntyg());

            String xml = xmlToString(registerSjukpenning.getIntyg());
            CertificateHolder certificateHolder = converterUtil.toCertificateHolder(utlatande);
            certificateHolder.setOriginalCertificate(xml);

            moduleApi.getModuleContainer().certificateReceived(certificateHolder);

            response.setResultat(okResult());
            LOGGER.debug("Registered intyg with id: {}",registerSjukpenning.getIntyg().getIntygsId());

        } catch (CertificateAlreadyExistsException e) {
            response.setResultat(infoResult("Certificate already exists"));
            String certificateId = registerSjukpenning.getIntyg().getIntygsId();
            String issuedBy = registerSjukpenning.getIntyg().getGrundData().getSkapadAv().getVardenhet().getEnhetsId();
            LOGGER.warn(LogMarkers.VALIDATION, "Validation warning for intyg " + certificateId + " issued by " + issuedBy + ": Certificate already exists - ignored.");
       
        } catch (CertificateValidationException e) {
            response.setResultat(errorResult(ErrorIdType.VALIDATION_ERROR, e.getMessage()));
            LOGGER.error(LogMarkers.VALIDATION, e.getMessage());

        } catch (ConverterException e) {
            response.setResultat(errorResult(ErrorIdType.VALIDATION_ERROR, e.getMessage()));
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

    private ResultatTyp okResult() {
        // TODO
        return null;
    }

    private ResultatTyp infoResult(String message) {
        // TODO
        return null;
    }

    private ResultatTyp errorResult(ErrorIdType errorIdtype, String message) {
        // TODO
        return null;
    }

    private String xmlToString(SjukpenningIntyg sjukpenningIntyg) throws JAXBException {
        StringWriter stringWriter = new StringWriter();
        JAXBElement<SjukpenningIntyg> requestElement = objectFactory.createSjukpenningIntyg(sjukpenningIntyg);
        jaxbContext.createMarshaller().marshal(requestElement, stringWriter);
        return stringWriter.toString();
    }

    private void validateTransport(RegisterSjukpenningType registerSjukpenning) throws CertificateValidationException {
        List<String> validationErrors = validator.validateTransport(registerSjukpenning.getIntyg());
        if (!validationErrors.isEmpty()) {
            throw new CertificateValidationException(validationErrors);
        }
    }
}
