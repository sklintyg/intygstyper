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
package se.inera.intyg.intygstyper.ts_bas.rest;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

import javax.xml.bind.*;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPMessage;

import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.fasterxml.jackson.databind.ObjectMapper;

import se.inera.intyg.common.support.model.Status;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.support.model.converter.util.XslTransformer;
import se.inera.intyg.common.support.modules.support.ApplicationOrigin;
import se.inera.intyg.common.support.modules.support.api.ModuleApi;
import se.inera.intyg.common.support.modules.support.api.ModuleContainerApi;
import se.inera.intyg.common.support.modules.support.api.dto.*;
import se.inera.intyg.common.support.modules.support.api.exception.*;
import se.inera.intyg.common.support.modules.support.api.notification.NotificationMessage;
import se.inera.intyg.intygstyper.ts_bas.model.converter.*;
import se.inera.intyg.intygstyper.ts_bas.model.converter.util.ConverterUtil;
import se.inera.intyg.intygstyper.ts_bas.model.internal.Utlatande;
import se.inera.intyg.intygstyper.ts_bas.pdf.PdfGenerator;
import se.inera.intyg.intygstyper.ts_bas.pdf.PdfGeneratorException;
import se.inera.intyg.intygstyper.ts_bas.validator.TsBasValidator;
import se.inera.intyg.intygstyper.ts_parent.integration.SendTSClient;
import se.inera.intygstjanster.ts.services.GetTSBasResponder.v1.*;
import se.inera.intygstjanster.ts.services.RegisterTSBasResponder.v1.*;
import se.inera.intygstjanster.ts.services.v1.ResultCodeType;

/**
 * The contract between the certificate module and the generic components (Intygstjänsten, Mina-Intyg & Webcert).
 *
 */
public class TsBasModuleApi implements ModuleApi {

    private static final Logger LOG = LoggerFactory.getLogger(TsBasModuleApi.class);

    @Autowired(required = false)
    private GetTSBasResponderInterface getTSBasResponderInterface;

    @Autowired(required = false)
    @Qualifier("registerTSBasClient")
    private RegisterTSBasResponderInterface registerTSBasResponderInterface;

    @Autowired(required = false)
    @Qualifier("tsBasSendCertificateClient")
    private SendTSClient sendTsBasClient;

    @Autowired
    private TsBasValidator validator;

    @Autowired
    private PdfGenerator pdfGenerator;

    @Autowired
    private WebcertModelFactory webcertModelFactory;

    @Autowired(required = true)
    @Qualifier("tsBasObjectMapper")
    private ObjectMapper objectMapper;

    @Autowired(required = true)
    @Qualifier("tsBasModelConverterUtil")
    private ConverterUtil converterUtil;

    @Autowired(required = false)
    @Qualifier("tsBasXslTransformer")
    private XslTransformer xslTransformer;

    private ModuleContainerApi moduleContainer;

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

    /**
     * @{inheritDoc}
     */
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

    /**
     * @{inheritDoc}
     */
    @Override
    public PdfResponse pdfEmployer(InternalModelHolder internalModel, List<Status> statuses, ApplicationOrigin applicationOrigin) throws ModuleException {
        throw new ModuleException("Feature not supported");
    }

    @Override
    public void registerCertificate(InternalModelHolder internalModel, String logicalAddress) throws ModuleException {

        RegisterTSBasType request = new RegisterTSBasType();
        try {
            request = InternalToTransport.convert(converterUtil.fromJsonString(internalModel.getInternalModel()));
        } catch (ConverterException e) {
            LOG.error("Failed to convert to transport format during registerTSBas", e);
            throw new ExternalServiceCallException("Failed to convert to transport format during registerTSBas", e);
        }

        RegisterTSBasResponseType response =
                registerTSBasResponderInterface.registerTSBas(logicalAddress, request);

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

        try {
            SOAPMessage response = sendTsBasClient.registerCertificate(transformedPayload, logicalAddress);
            SOAPEnvelope contents = response.getSOAPPart().getEnvelope();
            if (contents.getBody().hasFault()) {
                throw new ExternalServiceCallException(contents.getBody().getFault().getTextContent());
            }
        } catch (Exception e) {
            LOG.error("Error in sendCertificateToRecipient with msg: " + e.getMessage(), e);
            throw new ModuleException("Error in sendCertificateToRecipient.", e);
        }
    }

    @Override
    public CertificateResponse getCertificate(String certificateId, String logicalAddress) throws ModuleException {
        GetTSBasType request = new GetTSBasType();
        request.setIntygsId(certificateId);

        GetTSBasResponseType response = getTSBasResponderInterface.getTSBas(logicalAddress, request);

        switch (response.getResultat().getResultCode()) {
        case INFO:
        case OK:
            return convert(response, false);
        case ERROR:
            switch (response.getResultat().getErrorId()) {
            case REVOKED:
                return convert(response, true);
            case VALIDATION_ERROR:
                throw new ModuleException("getTSBas WS call: VALIDATION_ERROR :" + response.getResultat().getResultText());
            default:
                throw new ModuleException("getTSBas WS call: ERROR :" + response.getResultat().getResultText());
            }
        default:
            throw new ModuleException("getTSBas WS call: ERROR :" + response.getResultat().getResultText());
        }
    }

    @Override
    public boolean isModelChanged(String persistedState, String currentState) throws ModuleException {
        return !persistedState.equals(currentState);
    }

    @Override
    public Object createNotification(NotificationMessage notificationMessage) throws ModuleException {
        throw new UnsupportedOperationException("Unsupported for this module");
    }

    @Override
    public String marshall(String jsonString) throws ModuleException {
        String xmlString = null;
        try {
            Utlatande internal = objectMapper.readValue(jsonString, Utlatande.class);
            RegisterTSBasType external = InternalToTransport.convert(internal);
            StringWriter writer = new StringWriter();

            JAXBElement<RegisterTSBasType> jaxbElement = new JAXBElement<RegisterTSBasType>(new QName("ns3:RegisterTSBas"), RegisterTSBasType.class,
                    external);
            JAXBContext context = JAXBContext.newInstance(RegisterTSBasType.class);
            context.createMarshaller().marshal(jaxbElement, writer);
            xmlString = writer.toString();

        } catch (ConverterException | JAXBException | IOException e) {
            LOG.error("Error occured while marshalling", e);
            throw new ModuleException(e);
        }
        return xmlString;
    }

    @Override
    public Utlatande getUtlatandeFromJson(String utlatandeJson) throws IOException {
        return objectMapper.readValue(utlatandeJson, Utlatande.class);
    }

    // - - - - - Private scope - - - - - //
    private CertificateResponse convert(GetTSBasResponseType response, boolean revoked) throws ModuleException {
        try {
            Utlatande utlatande = TransportToInternal.convert(response.getIntyg());
            String internalModel = objectMapper.writeValueAsString(utlatande);

            CertificateMetaData metaData = TsBasMetaDataConverter.toCertificateMetaData(response.getMeta(), response.getIntyg());
            return new CertificateResponse(internalModel, utlatande, metaData, revoked);
        } catch (Exception e) {
            throw new ModuleException(e);
        }
    }

    private InternalModelResponse updateInternal(InternalModelHolder internalModel, HoSPersonal hosPerson, LocalDateTime signingDate)
            throws ModuleException {
        Utlatande utlatande = getInternal(internalModel);
        webcertModelFactory.updateSkapadAv(utlatande, hosPerson, signingDate);
        return toInteralModelResponse(utlatande);
    }

    private se.inera.intyg.intygstyper.ts_bas.model.internal.Utlatande getInternal(InternalModelHolder internalModel)
            throws ModuleException {
        try {
            return objectMapper.readValue(internalModel.getInternalModel(),
                    se.inera.intyg.intygstyper.ts_bas.model.internal.Utlatande.class);

        } catch (IOException e) {
            throw new ModuleSystemException("Failed to deserialize internal model", e);
        }
    }

    private InternalModelResponse toInteralModelResponse(
            se.inera.intyg.intygstyper.ts_bas.model.internal.Utlatande internalModel) throws ModuleException {
        try {
            StringWriter writer = new StringWriter();
            objectMapper.writeValue(writer, internalModel);
            return new InternalModelResponse(writer.toString());

        } catch (IOException e) {
            throw new ModuleSystemException("Failed to serialize internal model", e);
        }
    }

    // Only used for testing
    protected void setRegisterTSBasResponderClient(RegisterTSBasResponderInterface registerTSBasResponderInterface) {
        this.registerTSBasResponderInterface = registerTSBasResponderInterface;
    }

    protected void setGetTSBasResponderClient(GetTSBasResponderInterface getTSBasResponderInterface) {
        this.getTSBasResponderInterface = getTSBasResponderInterface;
    }

    @Override
    public String transformToStatisticsService(String inputXml) throws ModuleException {
        return inputXml;
    }
}
