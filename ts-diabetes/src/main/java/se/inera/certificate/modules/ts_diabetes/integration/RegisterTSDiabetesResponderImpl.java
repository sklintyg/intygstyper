package se.inera.certificate.modules.ts_diabetes.integration;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import se.inera.certificate.integration.module.exception.CertificateAlreadyExistsException;
import se.inera.certificate.integration.module.exception.InvalidCertificateException;
import se.inera.certificate.modules.support.api.CertificateHolder;
import se.inera.certificate.modules.support.api.exception.ModuleException;
import se.inera.certificate.modules.ts_diabetes.model.converter.TransportToInternalConverter;
import se.inera.certificate.modules.ts_diabetes.model.internal.Utlatande;
import se.inera.certificate.modules.ts_diabetes.rest.ModuleService;
import se.inera.certificate.modules.ts_diabetes.util.ConverterUtil;
import se.inera.intygstjanster.ts.services.RegisterTSDiabetesResponder.v1.RegisterTSDiabetesResponderInterface;
import se.inera.intygstjanster.ts.services.RegisterTSDiabetesResponder.v1.RegisterTSDiabetesResponseType;
import se.inera.intygstjanster.ts.services.RegisterTSDiabetesResponder.v1.RegisterTSDiabetesType;
import se.inera.intygstjanster.ts.services.utils.ResultTypeUtil;

import com.fasterxml.jackson.databind.ObjectMapper;

public class RegisterTSDiabetesResponderImpl implements RegisterTSDiabetesResponderInterface {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegisterTSDiabetesResponderImpl.class);

    @Autowired
    private ModuleService moduleService;

    @Autowired
    @Qualifier("ts-diabetes-objectMapper")
    private ObjectMapper objectMapper;
    
    @Autowired
    private ConverterUtil converterUtil;

    @Override
    public RegisterTSDiabetesResponseType registerTSDiabetes(String logicalAddress, RegisterTSDiabetesType parameters) {
        RegisterTSDiabetesResponseType response = new RegisterTSDiabetesResponseType();

        try {
            Utlatande utlatande = TransportToInternalConverter.convert(parameters.getIntyg());
            String xml = xmlToString(parameters);
            
            CertificateHolder certificateHolder = converterUtil.toCertificateHolder(utlatande);
            certificateHolder.setOriginalCertificate(xml);
            
            moduleService.getModuleContainer().certificateReceived(certificateHolder);

            response.setResultat(ResultTypeUtil.okResult());
        } catch (JAXBException | ModuleException | CertificateAlreadyExistsException | InvalidCertificateException e) {
            e.printStackTrace();
        }

        return response;
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
