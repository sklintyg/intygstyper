/**
 * Copyright (C) 2013 Inera AB (http://www.inera.se)
 *
 * This file is part of Inera Certificate (http://code.google.com/p/inera-certificate).
 *
 * Inera Certificate is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Inera Certificate is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.inera.certificate.modules.fk7263.integration;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.w3.wsaddressing10.AttributedURIType;
import org.w3c.dom.Document;

import se.inera.certificate.integration.module.exception.InvalidCertificateException;
import se.inera.certificate.integration.module.exception.MissingConsentException;
import se.inera.certificate.logging.LogMarkers;
import se.inera.certificate.modules.fk7263.model.converter.InternalToTransport;
import se.inera.certificate.modules.fk7263.model.converter.util.ConverterUtil;
import se.inera.certificate.modules.fk7263.rest.Fk7263ModuleApi;
import se.inera.certificate.modules.support.api.CertificateHolder;
import se.inera.ifv.insuranceprocess.healthreporting.getcertificate.v1.rivtabp20.GetCertificateResponderInterface;
import se.inera.ifv.insuranceprocess.healthreporting.getcertificateresponder.v1.CertificateType;
import se.inera.ifv.insuranceprocess.healthreporting.getcertificateresponder.v1.GetCertificateRequestType;
import se.inera.ifv.insuranceprocess.healthreporting.getcertificateresponder.v1.GetCertificateResponseType;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificateresponder.v3.ObjectFactory;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificateresponder.v3.RegisterMedicalCertificateType;
import se.inera.intyg.common.schemas.insuranceprocess.healthreporting.converter.ModelConverter;
import se.inera.intyg.common.schemas.insuranceprocess.healthreporting.utils.ResultOfCallUtil;

import com.google.common.base.Throwables;

/**
 * @author andreaskaltenbach
 */
public class GetCertificateResponderImpl implements
        GetCertificateResponderInterface {

    protected static final Logger LOGGER = LoggerFactory.getLogger(GetCertificateResponderImpl.class);

    private JAXBContext jaxbContext;
    private ObjectFactory objectFactory;

    @PostConstruct
    public void initializeJaxbContext() throws JAXBException {
        jaxbContext = JAXBContext.newInstance(RegisterMedicalCertificateType.class);
        objectFactory = new ObjectFactory();
    }
    
    @Autowired
    private Fk7263ModuleApi moduleApi;

    @Autowired
    private ConverterUtil converterUtil;

    public void setConverterUtil(ConverterUtil converterUtil) {
        this.converterUtil = converterUtil;
    }

    @Override
    public GetCertificateResponseType getCertificate(AttributedURIType logicalAddress, GetCertificateRequestType request) {
        GetCertificateResponseType response = new GetCertificateResponseType();

        String certificateId = request.getCertificateId();
        String nationalIdentityNumber = request.getNationalIdentityNumber();

        if (certificateId == null || certificateId.length() == 0) {
            LOGGER.info(LogMarkers.VALIDATION, "Tried to get certificate with non-existing certificateId '.");
            response.setResult(ResultOfCallUtil.failResult("Validation error: missing certificateId"));
            return response;
        }

        if (nationalIdentityNumber == null || nationalIdentityNumber.length() == 0) {
            LOGGER.info(LogMarkers.VALIDATION, "Tried to get certificate with non-existing nationalIdentityNumber '.");
            response.setResult(ResultOfCallUtil.failResult("Validation error: missing nationalIdentityNumber"));
            return response;
        }

        CertificateHolder certificate = null;
        
        try {
            certificate = moduleApi.getModuleContainer().getCertificate(certificateId, nationalIdentityNumber, true);
            if (certificate.isRevoked()) {
                response.setResult(ResultOfCallUtil.infoResult(String.format("Certificate '%s' has been revoked", certificateId)));
            } else {
                response.setMeta(ModelConverter.toCertificateMetaType(certificate));
                attachCertificateDocument(certificate, response);
                response.setResult(ResultOfCallUtil.okResult());
            }
        } catch (InvalidCertificateException | MissingConsentException e) {
            response.setResult(ResultOfCallUtil.failResult(e.getMessage()));
        }

        return response;
    }

    protected void attachCertificateDocument(CertificateHolder certificate, GetCertificateResponseType response) {
        try {
            
            // Create the Document
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document document = db.newDocument();

            RegisterMedicalCertificateType registerMedicalCertificate = InternalToTransport.getJaxbObject(converterUtil.fromJsonString(certificate.getDocument()));
            JAXBElement<RegisterMedicalCertificateType> registerMedicalCertificateElement = objectFactory
                    .createRegisterMedicalCertificate(registerMedicalCertificate);

            // Marshal the Object to a Document
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.marshal(registerMedicalCertificateElement, document);
            CertificateType certificateType = new CertificateType();
            certificateType.getAny().add(document.getDocumentElement());
            response.setCertificate(certificateType);

        } catch (Exception e) {
            Throwables.propagate(e);
        }
    }

    
}
