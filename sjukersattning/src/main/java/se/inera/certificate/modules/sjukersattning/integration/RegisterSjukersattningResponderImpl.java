package se.inera.certificate.modules.sjukersattning.integration;


import java.io.StringWriter;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import se.inera.certificate.integration.module.exception.CertificateAlreadyExistsException;
import se.inera.certificate.logging.LogMarkers;
import se.inera.certificate.model.converter.util.ConverterException;
import se.inera.certificate.modules.sjukersattning.model.converter.TransportToInternal;
import se.inera.certificate.modules.sjukersattning.model.converter.util.ConverterUtil;
import se.inera.certificate.modules.sjukersattning.model.internal.SjukersattningUtlatande;
import se.inera.certificate.modules.sjukersattning.rest.SjukersattningModuleApi;
import se.inera.certificate.modules.sjukersattning.validator.TransportValidator;
import se.inera.certificate.modules.support.api.CertificateHolder;
import se.inera.certificate.validate.CertificateValidationException;
import se.inera.intygstjanster.fk.services.registersjukersattningresponder.v1.RegisterSjukersattningResponderInterface;
import se.inera.intygstjanster.fk.services.registersjukersattningresponder.v1.RegisterSjukersattningResponseType;
import se.inera.intygstjanster.fk.services.registersjukersattningresponder.v1.RegisterSjukersattningType;
import se.inera.intygstjanster.fk.services.v1.ErrorIdType;
import se.inera.intygstjanster.fk.services.v1.ObjectFactory;
import se.inera.intygstjanster.fk.services.v1.SjukersattningIntyg;

import com.google.common.base.Throwables;

public class RegisterSjukersattningResponderImpl implements RegisterSjukersattningResponderInterface {

    @Autowired
    TransportValidator validator;

    @Autowired
    private ConverterUtil converterUtil;

    @Autowired 
    private SjukersattningModuleApi moduleApi;

    private ObjectFactory objectFactory;
    private JAXBContext jaxbContext;

    private static final Logger LOGGER = LoggerFactory.getLogger(RegisterSjukersattningResponderImpl.class);

    @PostConstruct
    public void initializeJaxbContext() throws JAXBException {
        jaxbContext = JAXBContext.newInstance(RegisterSjukersattningType.class);
        objectFactory = new ObjectFactory();
    }

    @Override
    public RegisterSjukersattningResponseType registerSjukersattning(String logicalAddress, RegisterSjukersattningType registerSjukersattning) {
        RegisterSjukersattningResponseType response = new RegisterSjukersattningResponseType();

        try {
            validateTransport(registerSjukersattning);
            SjukersattningUtlatande utlatande = TransportToInternal.convert(registerSjukersattning.getIntyg());

            String xml = xmlToString(registerSjukersattning.getIntyg());
            CertificateHolder certificateHolder = converterUtil.toCertificateHolder(utlatande);
            certificateHolder.setOriginalCertificate(xml);

            moduleApi.getModuleContainer().certificateReceived(certificateHolder);

            response.setResultat(ResultUtil.okResult());
            LOGGER.debug("Registered intyg with id: {}",registerSjukersattning.getIntyg().getIntygsId());

        } catch (CertificateAlreadyExistsException e) {
            response.setResultat(ResultUtil.infoResult("Certificate already exists"));
            String certificateId = registerSjukersattning.getIntyg().getIntygsId();
            String issuedBy = registerSjukersattning.getIntyg().getGrundData().getSkapadAv().getVardenhet().getEnhetsId();
            LOGGER.warn(LogMarkers.VALIDATION, "Validation warning for intyg " + certificateId + " issued by " + issuedBy + ": Certificate already exists - ignored.");
       
        } catch (CertificateValidationException e) {
            response.setResultat(ResultUtil.errorResult(ErrorIdType.VALIDATION_ERROR, e.getMessage()));
            LOGGER.error(LogMarkers.VALIDATION, e.getMessage());

        } catch (ConverterException e) {
            response.setResultat(ResultUtil.errorResult(ErrorIdType.VALIDATION_ERROR, e.getMessage()));
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

    private String xmlToString(SjukersattningIntyg sjukersattningIntyg) throws JAXBException {
        StringWriter stringWriter = new StringWriter();
        JAXBElement<SjukersattningIntyg> requestElement = objectFactory.createSjukersattningIntyg(sjukersattningIntyg);
        jaxbContext.createMarshaller().marshal(requestElement, stringWriter);
        return stringWriter.toString();
    }

    private void validateTransport(RegisterSjukersattningType registerSjukersattning) throws CertificateValidationException {
        List<String> validationErrors = validator.validateTransport(registerSjukersattning.getIntyg());
        if (!validationErrors.isEmpty()) {
            throw new CertificateValidationException(validationErrors);
        }
    }

}
