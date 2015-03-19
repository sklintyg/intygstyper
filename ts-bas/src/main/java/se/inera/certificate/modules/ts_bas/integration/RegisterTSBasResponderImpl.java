package se.inera.certificate.modules.ts_bas.integration;

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
import se.inera.certificate.modules.support.api.CertificateHolder;
import se.inera.certificate.modules.ts_bas.model.converter.TransportToInternal;
import se.inera.certificate.modules.ts_bas.model.converter.util.ConverterUtil;
import se.inera.certificate.modules.ts_bas.model.internal.Utlatande;
import se.inera.certificate.modules.ts_bas.rest.TsBasModuleApi;
import se.inera.certificate.modules.ts_bas.validator.TsBasValidator;
import se.inera.certificate.validate.CertificateValidationException;
import se.inera.intygstjanster.ts.services.RegisterTSBasResponder.v1.ObjectFactory;
import se.inera.intygstjanster.ts.services.RegisterTSBasResponder.v1.RegisterTSBasResponderInterface;
import se.inera.intygstjanster.ts.services.RegisterTSBasResponder.v1.RegisterTSBasResponseType;
import se.inera.intygstjanster.ts.services.RegisterTSBasResponder.v1.RegisterTSBasType;
import se.inera.intygstjanster.ts.services.utils.ResultTypeUtil;
import se.inera.intygstjanster.ts.services.v1.ErrorIdType;

import com.google.common.base.Throwables;

public class RegisterTSBasResponderImpl implements RegisterTSBasResponderInterface {

    @Autowired
    TsBasValidator validator;

    @Autowired
    @Qualifier("tsBasModelConverterUtil")
    private ConverterUtil converterUtil;

    @Autowired 
    private TsBasModuleApi moduleApi;

    private ObjectFactory objectFactory;
    private JAXBContext jaxbContext;

    private static final Logger LOGGER = LoggerFactory.getLogger(RegisterTSBasResponderImpl.class);

    @PostConstruct
    public void initializeJaxbContext() throws JAXBException {
        jaxbContext = JAXBContext.newInstance(RegisterTSBasType.class);
        objectFactory = new ObjectFactory();
    }

    @Override
    public RegisterTSBasResponseType registerTSBas(String logicalAddress, RegisterTSBasType registerTsBas) {
        RegisterTSBasResponseType response = new RegisterTSBasResponseType();

        try {
            validateTransport(registerTsBas);
            Utlatande utlatande = TransportToInternal.convert(registerTsBas.getIntyg());

            String xml = xmlToString(registerTsBas);
            CertificateHolder certificateHolder = converterUtil.toCertificateHolder(utlatande);
            certificateHolder.setOriginalCertificate(xml);

            moduleApi.getModuleContainer().certificateReceived(certificateHolder);

            response.setResultat(ResultTypeUtil.okResult());
            LOGGER.debug("Registered intyg with id: {}",registerTsBas.getIntyg().getIntygsId());

        } catch (CertificateAlreadyExistsException e) {
            response.setResultat(ResultTypeUtil.infoResult("Certificate already exists"));
            String certificateId = registerTsBas.getIntyg().getIntygsId();
            String issuedBy =  registerTsBas.getIntyg().getGrundData().getSkapadAv().getVardenhet().getEnhetsId().getExtension();
            LOGGER.warn(LogMarkers.VALIDATION, "Validation warning for intyg " + certificateId + " issued by " + issuedBy + ": Certificate already exists - ignored.");
       
        } catch (CertificateValidationException e) {
            response.setResultat(ResultTypeUtil.errorResult(ErrorIdType.VALIDATION_ERROR, e.getMessage()));
            LOGGER.error(LogMarkers.VALIDATION, e.getMessage());

        } catch (ConverterException e) {
            response.setResultat(ResultTypeUtil.errorResult(ErrorIdType.VALIDATION_ERROR, e.getMessage()));
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

    private String xmlToString(RegisterTSBasType registerTsBas) throws JAXBException {
        StringWriter stringWriter = new StringWriter();
        JAXBElement<RegisterTSBasType> requestElement = objectFactory
                .createRegisterTSBas(registerTsBas);
        jaxbContext.createMarshaller().marshal(requestElement, stringWriter);
        return stringWriter.toString();
    }

    private void validateTransport(RegisterTSBasType registerTsBas) throws CertificateValidationException {
        if (!validator.isSchemaValid(registerTsBas.getIntyg())) {
            throw new CertificateValidationException("Schema validation of intyg: " + registerTsBas.getIntyg().getIntygsId() + " failed");
        }
        List<String> validationErrors = validator.validateTransport(registerTsBas.getIntyg());
        if (!validationErrors.isEmpty()) {
            throw new CertificateValidationException(validationErrors);
        }
    }
}
