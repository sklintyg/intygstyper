package se.inera.intyg.intygstyper.ts_diabetes.integration;

import java.io.StringWriter;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import se.inera.intyg.common.support.integration.module.exception.CertificateAlreadyExistsException;
import se.inera.intyg.common.support.integration.module.exception.InvalidCertificateException;
import se.inera.intyg.common.util.logging.LogMarkers;
import se.inera.intyg.common.support.modules.support.api.CertificateHolder;
import se.inera.intyg.intygstyper.ts_diabetes.model.converter.TransportToInternalConverter;
import se.inera.intyg.intygstyper.ts_diabetes.model.internal.Utlatande;
import se.inera.intyg.intygstyper.ts_diabetes.rest.TsDiabetesModuleApi;
import se.inera.intyg.intygstyper.ts_diabetes.util.ConverterUtil;
import se.inera.intyg.intygstyper.ts_diabetes.validator.Validator;
import se.inera.intyg.common.support.validate.CertificateValidationException;
import se.inera.intygstjanster.ts.services.RegisterTSDiabetesResponder.v1.RegisterTSDiabetesResponderInterface;
import se.inera.intygstjanster.ts.services.RegisterTSDiabetesResponder.v1.RegisterTSDiabetesResponseType;
import se.inera.intygstjanster.ts.services.RegisterTSDiabetesResponder.v1.RegisterTSDiabetesType;
import se.inera.intyg.common.schemas.intygstjansten.ts.utils.ResultTypeUtil;
import se.inera.intygstjanster.ts.services.v1.ErrorIdType;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Throwables;

public class RegisterTSDiabetesResponderImpl implements RegisterTSDiabetesResponderInterface {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegisterTSDiabetesResponderImpl.class);

    @Autowired
    private TsDiabetesModuleApi moduleService;

    @Autowired
    @Qualifier("ts-diabetes-objectMapper")
    private ObjectMapper objectMapper;

    @Autowired
    private ConverterUtil converterUtil;

    @Autowired
    private Validator validator;

    @Override
    public RegisterTSDiabetesResponseType registerTSDiabetes(String logicalAddress, RegisterTSDiabetesType parameters) {
        RegisterTSDiabetesResponseType response = new RegisterTSDiabetesResponseType();

        try {
            validate(parameters);
            Utlatande utlatande = TransportToInternalConverter.convert(parameters.getIntyg());
            String xml = xmlToString(parameters);

            CertificateHolder certificateHolder = converterUtil.toCertificateHolder(utlatande);
            certificateHolder.setOriginalCertificate(xml);

            moduleService.getModuleContainer().certificateReceived(certificateHolder);

            response.setResultat(ResultTypeUtil.okResult());

        } catch (CertificateAlreadyExistsException ce) {
            response.setResultat(ResultTypeUtil.infoResult("Certificate already exists"));
            String certificateId = parameters.getIntyg().getIntygsId();
            String issuedBy = parameters.getIntyg().getGrundData().getSkapadAv().getVardenhet().getEnhetsId().getExtension();
            LOGGER.warn(LogMarkers.VALIDATION, "Validation warning for intyg " + certificateId + " issued by " + issuedBy
                    + ": Certificate already exists - ignored.");
        } catch (InvalidCertificateException e) {
            response.setResultat(ResultTypeUtil.errorResult(ErrorIdType.APPLICATION_ERROR, "Invalid certificate ID"));
            String certificateId = parameters.getIntyg().getIntygsId();
            String issuedBy =  parameters.getIntyg().getGrundData().getSkapadAv().getVardenhet().getEnhetsId().getExtension();
            LOGGER.error(LogMarkers.VALIDATION, "Failed to create Certificate with id " + certificateId + " issued by " + issuedBy + ": Certificate ID already exists for another person.");

        } catch (CertificateValidationException e) {
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

    private void validate(RegisterTSDiabetesType parameters) throws CertificateValidationException {
        List<String> validationErrors = validator.validateTransport(parameters.getIntyg());
        if (!validationErrors.isEmpty()) {
            throw new CertificateValidationException(validationErrors);
        }
    }

    private String xmlToString(RegisterTSDiabetesType parameters) throws JAXBException {
        JAXBContext ctx = JAXBContext.newInstance(RegisterTSDiabetesType.class);
        Marshaller marshaller = ctx.createMarshaller();

        QName qName = new QName("urn:local:se:intygstjanster:services:1", "RegisterTSDiabetesType");
        JAXBElement<RegisterTSDiabetesType> t = new JAXBElement<RegisterTSDiabetesType>(qName, RegisterTSDiabetesType.class, parameters);

        StringWriter wr = new StringWriter();
        marshaller.marshal(t, wr);

        return wr.toString();
    }
}
