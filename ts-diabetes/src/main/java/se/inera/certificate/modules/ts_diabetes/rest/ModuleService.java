/**
 * Copyright (C) 2013 Inera AB (http://www.inera.se)
 *
 * This file is part of Inera Certificate Modules (http://code.google.com/p/inera-certificate-modules).
 *
 * Inera Certificate Modules is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Inera Certificate Modules is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.inera.certificate.modules.ts_diabetes.rest;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.soap.SOAPMessage;

import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import se.inera.certificate.model.Status;
import se.inera.certificate.model.converter.util.ConverterException;
import se.inera.certificate.modules.support.ApplicationOrigin;
import se.inera.certificate.modules.support.api.ModuleApi;
import se.inera.certificate.modules.support.api.ModuleContainerApi;
import se.inera.certificate.modules.support.api.dto.CertificateMetaData;
import se.inera.certificate.modules.support.api.dto.CertificateResponse;
import se.inera.certificate.modules.support.api.dto.CreateDraftCopyHolder;
import se.inera.certificate.modules.support.api.dto.CreateNewDraftHolder;
import se.inera.certificate.modules.support.api.dto.HoSPersonal;
import se.inera.certificate.modules.support.api.dto.InternalModelHolder;
import se.inera.certificate.modules.support.api.dto.InternalModelResponse;
import se.inera.certificate.modules.support.api.dto.PdfResponse;
import se.inera.certificate.modules.support.api.dto.ValidateDraftResponse;
import se.inera.certificate.modules.support.api.exception.ExternalServiceCallException;
import se.inera.certificate.modules.support.api.exception.ModuleConverterException;
import se.inera.certificate.modules.support.api.exception.ModuleException;
import se.inera.certificate.modules.support.api.exception.ModuleSystemException;
import se.inera.certificate.modules.support.api.notification.NotificationMessage;
import se.inera.certificate.modules.ts_diabetes.model.converter.InternalToTransportConverter;
import se.inera.certificate.modules.ts_diabetes.model.converter.TransportToInternalConverter;
import se.inera.certificate.modules.ts_diabetes.model.converter.WebcertModelFactory;
import se.inera.certificate.modules.ts_diabetes.model.internal.Utlatande;
import se.inera.certificate.modules.ts_diabetes.pdf.PdfGenerator;
import se.inera.certificate.modules.ts_diabetes.pdf.PdfGeneratorException;
import se.inera.certificate.modules.ts_diabetes.util.TSDiabetesCertificateMetaTypeConverter;
import se.inera.certificate.modules.ts_diabetes.validator.Validator;
import se.inera.certificate.modules.ts_parent.integration.SendTSClient;
import se.inera.certificate.modules.ts_parent.transformation.XslTransformer;
import se.inera.intygstjanster.ts.services.GetTSDiabetesResponder.v1.GetTSDiabetesResponderInterface;
import se.inera.intygstjanster.ts.services.GetTSDiabetesResponder.v1.GetTSDiabetesResponseType;
import se.inera.intygstjanster.ts.services.GetTSDiabetesResponder.v1.GetTSDiabetesType;
import se.inera.intygstjanster.ts.services.RegisterTSDiabetesResponder.v1.RegisterTSDiabetesResponderInterface;
import se.inera.intygstjanster.ts.services.RegisterTSDiabetesResponder.v1.RegisterTSDiabetesResponseType;
import se.inera.intygstjanster.ts.services.RegisterTSDiabetesResponder.v1.RegisterTSDiabetesType;
import se.inera.intygstjanster.ts.services.v1.ResultCodeType;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * The contract between the certificate module and the generic components (Intygstjänsten and Mina-Intyg).
 *
 * @author Gustav Norbäcker, R2M
 */
public class ModuleService implements ModuleApi {

    private static final Logger LOG = LoggerFactory.getLogger(ModuleService.class);

    @Autowired
    private Validator validator;

    @Autowired
    private PdfGenerator pdfGenerator;

    @Autowired
    private WebcertModelFactory webcertModelFactory;

    @Autowired
    @Qualifier("ts-diabetes-jaxbContext")
    private JAXBContext jaxbContext;

    @Autowired
    @Qualifier("ts-diabetes-objectMapper")
    private ObjectMapper objectMapper;

    @Autowired(required = false)
    @Qualifier("sendTsDiabetesClient")
    private SendTSClient sendTsDiabetesClient;

    @Autowired(required = false)
    @Qualifier("diabetesGetClient")
    private GetTSDiabetesResponderInterface diabetesGetClient;

    @Autowired(required = false)
    @Qualifier("diabetesRegisterClient")
    private RegisterTSDiabetesResponderInterface diabetesRegisterClient;

    @Autowired(required = false)
    @Qualifier("tsDiabetesXslTransformer")
    private XslTransformer xslTransformer;

    private ModuleContainerApi moduleContainer;

    public ModuleService() throws Exception {
    }

    @Override
    public ValidateDraftResponse validateDraft(InternalModelHolder internalModel) throws ModuleException {
        return validator.validateInternal(getInternal(internalModel));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InternalModelResponse createNewInternal(CreateNewDraftHolder draftCertificateHolder) throws ModuleException {
        try {
            return toInteralModelResponse(webcertModelFactory.createNewWebcertDraft(draftCertificateHolder, null));

        } catch (ConverterException e) {
            LOG.error("Could not create a new internal Webcert model", e);
            throw new ModuleConverterException("Could not create a new internal Webcert model", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InternalModelResponse createNewInternalFromTemplate(CreateDraftCopyHolder draftCertificateHolder, InternalModelHolder template)
            throws ModuleException {
        try {
            Utlatande internal = getInternal(template);
            return toInteralModelResponse(webcertModelFactory.createCopy(draftCertificateHolder, internal));
        } catch (ConverterException e) {
            LOG.error("Could not create a new internal Webcert model", e);
            throw new ModuleConverterException("Could not create a new internal Webcert model", e);
        }
    }

    @Override
    public InternalModelResponse updateBeforeSave(InternalModelHolder internalModel, HoSPersonal hosPerson) throws ModuleException {
        return updateInternal(internalModel, hosPerson, null);
    }

    @Override
    public InternalModelResponse updateBeforeSigning(InternalModelHolder internalModel, HoSPersonal hosPerson, LocalDateTime signingDate)
            throws ModuleException {
        return updateInternal(internalModel, hosPerson, signingDate);
    }

    @Override
    public void setModuleContainer(ModuleContainerApi moduleContainer) {
        this.moduleContainer = moduleContainer;

    }

    @Override
    public ModuleContainerApi getModuleContainer() {
        return moduleContainer;
    }

    @Override
    public PdfResponse pdf(InternalModelHolder internalModel, List<Status> statuses, ApplicationOrigin applicationOrigin) throws ModuleException {
        try {
            return new PdfResponse(pdfGenerator.generatePDF(getInternal(internalModel), applicationOrigin),
                    pdfGenerator.generatePdfFilename(getInternal(internalModel)));
        } catch (PdfGeneratorException e) {
            LOG.error("Failed to generate PDF for certificate!", e);
            throw new ModuleSystemException("Failed to generate PDF for certificate!", e);
        }
    }

    @Override
    public void registerCertificate(InternalModelHolder internalModel, String logicalAddress) throws ModuleException {
        RegisterTSDiabetesType request = new RegisterTSDiabetesType();
        try {
            Utlatande internal = objectMapper.readValue(internalModel.getInternalModel(), Utlatande.class);
            request.setIntyg(InternalToTransportConverter.convert(internal));
        } catch (IOException e) {
            LOG.error("Failed to convert to transport format during registerTSBas", e);
            throw new ExternalServiceCallException("Failed to convert to transport format during registerTSBas", e);
        }

        RegisterTSDiabetesResponseType response =
                diabetesRegisterClient.registerTSDiabetes(logicalAddress, request);

        // check whether call was successful or not
        if (response.getResultat().getResultCode() != ResultCodeType.OK) {
            String message = response.getResultat().getResultCode() == ResultCodeType.INFO
                    ? response.getResultat().getResultText()
                    : response.getResultat().getErrorId() + " : " + response.getResultat().getResultText();
            throw new ExternalServiceCallException(message);
        }
    }

    @Override
    public void sendCertificateToRecipient(InternalModelHolder internalModel, String logicalAddress, String recipientId) throws ModuleException {
        String transformedPayload = xslTransformer.transform(internalModel.getXmlModel());
        
        SOAPMessage response = sendTsDiabetesClient.registerCertificate(transformedPayload);
//        if (response.getResult().getResultCode() !=  se.inera.certificate.clinicalprocess.healthcond.certificate.v1.ResultCodeType.OK) {
//            String message = response.getResult().getResultCode() == se.inera.certificate.clinicalprocess.healthcond.certificate.v1.ResultCodeType.INFO
//                    ? response.getResult().getResultText()
//                    : response.getResult().getErrorId() + " : " + response.getResult().getResultText();
//            throw new ExternalServiceCallException(message);
//        }
    }

    @Override
    public CertificateResponse getCertificate(String certificateId, String logicalAddress) throws ModuleException {

        GetTSDiabetesType type = new GetTSDiabetesType();
        type.setIntygsId(certificateId);

        GetTSDiabetesResponseType diabetesResponseType = diabetesGetClient.getTSDiabetes(logicalAddress, type);

        switch (diabetesResponseType.getResultat().getResultCode()) {
        case INFO:
        case OK:
            return convert(diabetesResponseType, false);
        case ERROR:
            switch (diabetesResponseType.getResultat().getErrorId()) {
            case REVOKED:
                return convert(diabetesResponseType, true);
            case VALIDATION_ERROR:
                throw new ModuleException("getMedicalCertificateForCare WS call: VALIDATION_ERROR :"
                        + diabetesResponseType.getResultat().getResultText());
            default:
                throw new ModuleException("getMedicalCertificateForCare WS call: ERROR :" + diabetesResponseType.getResultat().getResultText());
            }
        default:
            throw new ModuleException("getMedicalCertificateForCare WS call: ERROR :" + diabetesResponseType.getResultat().getResultText());
        }
    }

    private CertificateResponse convert(GetTSDiabetesResponseType diabetesResponseType, boolean revoked) throws ModuleException {
        try {
            Utlatande utlatande = TransportToInternalConverter.convert(diabetesResponseType.getIntyg());
            String internalModel = objectMapper.writeValueAsString(utlatande);
            CertificateMetaData metaData = TSDiabetesCertificateMetaTypeConverter.toCertificateMetaData(diabetesResponseType.getMeta(),
                    diabetesResponseType.getIntyg());
            return new CertificateResponse(internalModel, utlatande, metaData, revoked);
        } catch (Exception e) {
            throw new ModuleException(e);
        }
    }

    @Override
    public boolean isModelChanged(String persistedState, String currentState) throws ModuleException {
        return persistedState.equals(currentState) == false;
    }

    @Override
    public Object createNotification(NotificationMessage notificationMessage) throws ModuleException {
        throw new UnsupportedOperationException();
    }

    // - - - - - Private scope - - - - - //
    private InternalModelResponse updateInternal(InternalModelHolder internalModel, HoSPersonal hosPerson, LocalDateTime signingDate)
            throws ModuleException {
        Utlatande utlatande = getInternal(internalModel);
        webcertModelFactory.updateSkapadAv(utlatande, hosPerson, signingDate);
        return toInteralModelResponse(utlatande);
    }

    private Utlatande getInternal(InternalModelHolder internalModel)
            throws ModuleException {
        try {
            return objectMapper.readValue(internalModel.getInternalModel(),
                    Utlatande.class);

        } catch (IOException e) {
            throw new ModuleSystemException("Failed to deserialize internal model", e);
        }
    }

    private InternalModelResponse toInteralModelResponse(
            Utlatande internalModel) throws ModuleException {
        try {
            StringWriter writer = new StringWriter();
            objectMapper.writeValue(writer, internalModel);
            return new InternalModelResponse(writer.toString());

        } catch (IOException e) {
            throw new ModuleSystemException("Failed to serialize internal model", e);
        }
    }

}
