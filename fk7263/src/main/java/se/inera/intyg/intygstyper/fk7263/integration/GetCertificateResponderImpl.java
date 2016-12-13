/*
 * Copyright (C) 2016 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.inera.intyg.intygstyper.fk7263.integration;

import java.io.StringReader;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXB;
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

import com.google.common.base.Throwables;

import se.inera.ifv.insuranceprocess.healthreporting.getcertificate.rivtabp20.v1.GetCertificateResponderInterface;
import se.inera.ifv.insuranceprocess.healthreporting.getcertificateresponder.v1.CertificateType;
import se.inera.ifv.insuranceprocess.healthreporting.getcertificateresponder.v1.GetCertificateRequestType;
import se.inera.ifv.insuranceprocess.healthreporting.getcertificateresponder.v1.GetCertificateResponseType;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificateresponder.v3.ObjectFactory;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificateresponder.v3.RegisterMedicalCertificateType;
import se.inera.intyg.common.schemas.insuranceprocess.healthreporting.converter.ModelConverter;
import se.inera.intyg.common.schemas.insuranceprocess.healthreporting.utils.ResultOfCallUtil;
import se.inera.intyg.common.support.integration.module.exception.InvalidCertificateException;
import se.inera.intyg.common.support.integration.module.exception.MissingConsentException;
import se.inera.intyg.common.support.modules.support.api.CertificateHolder;
import se.inera.intyg.common.support.modules.support.api.ModuleContainerApi;
import se.inera.intyg.common.support.modules.support.api.dto.Personnummer;
import se.inera.intyg.common.util.logging.LogMarkers;

/**
 * @author andreaskaltenbach
 */
public class GetCertificateResponderImpl implements
        GetCertificateResponderInterface {

    protected static final Logger LOGGER = LoggerFactory.getLogger(GetCertificateResponderImpl.class);

    private JAXBContext jaxbContext;
    private ObjectFactory objectFactory;

    @Autowired(required = false)
    private ModuleContainerApi moduleContainer;

    @PostConstruct
    public void initializeJaxbContext() throws JAXBException {
        jaxbContext = JAXBContext.newInstance(RegisterMedicalCertificateType.class);
        objectFactory = new ObjectFactory();
    }

    @Override
    public GetCertificateResponseType getCertificate(AttributedURIType logicalAddress, GetCertificateRequestType request) {
        GetCertificateResponseType response = new GetCertificateResponseType();

        String certificateId = request.getCertificateId();

        if (certificateId == null || certificateId.length() == 0) {
            LOGGER.info(LogMarkers.VALIDATION, "Tried to get certificate with non-existing certificateId '.");
            response.setResult(ResultOfCallUtil.failResult("Validation error: missing certificateId"));
            return response;
        }

        final String nationalIdentityNumber = request.getNationalIdentityNumber();
        if (nationalIdentityNumber == null || nationalIdentityNumber.length() == 0) {
            LOGGER.info(LogMarkers.VALIDATION, "Tried to get certificate with non-existing nationalIdentityNumber '.");
            response.setResult(ResultOfCallUtil.failResult("Validation error: missing nationalIdentityNumber"));
            return response;
        }
        Personnummer personnummer = new Personnummer(nationalIdentityNumber);

        CertificateHolder certificate = null;

        try {
            certificate = moduleContainer.getCertificate(certificateId, personnummer, true);
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

            RegisterMedicalCertificateType registerMedicalCertificate = JAXB.unmarshal(new StringReader(certificate.getOriginalCertificate()),
                    RegisterMedicalCertificateType.class);
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
