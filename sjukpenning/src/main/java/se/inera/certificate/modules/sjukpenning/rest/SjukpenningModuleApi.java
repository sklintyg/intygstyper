package se.inera.certificate.modules.sjukpenning.rest;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

import javax.xml.bind.JAXB;

import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import se.inera.certificate.model.Status;
import se.inera.certificate.model.converter.util.ConverterException;
import se.inera.certificate.modules.sjukpenning.model.converter.InternalToTransport;
import se.inera.certificate.modules.sjukpenning.model.converter.TransportToInternal;
import se.inera.certificate.modules.sjukpenning.model.converter.WebcertModelFactory;
import se.inera.certificate.modules.sjukpenning.model.converter.util.ConverterUtil;
import se.inera.certificate.modules.sjukpenning.model.internal.SjukpenningUtlatande;
import se.inera.certificate.modules.sjukpenning.validator.InternalDraftValidator;
import se.inera.certificate.modules.support.ApplicationOrigin;
import se.inera.certificate.modules.support.api.ModuleApi;
import se.inera.certificate.modules.support.api.ModuleContainerApi;
import se.inera.certificate.modules.support.api.dto.*;
import se.inera.certificate.modules.support.api.exception.ExternalServiceCallException;
import se.inera.certificate.modules.support.api.exception.ModuleConverterException;
import se.inera.certificate.modules.support.api.exception.ModuleException;
import se.inera.certificate.modules.support.api.exception.ModuleSystemException;
import se.inera.certificate.modules.support.api.notification.NotificationMessage;
import se.inera.intygstjanster.fk.services.getsjukpenningresponder.v1.GetSjukpenningResponderInterface;
import se.inera.intygstjanster.fk.services.getsjukpenningresponder.v1.GetSjukpenningResponseType;
import se.inera.intygstjanster.fk.services.getsjukpenningresponder.v1.GetSjukpenningType;
import se.inera.intygstjanster.fk.services.registersjukpenningresponder.v1.RegisterSjukpenningResponderInterface;
import se.inera.intygstjanster.fk.services.registersjukpenningresponder.v1.RegisterSjukpenningResponseType;
import se.inera.intygstjanster.fk.services.registersjukpenningresponder.v1.RegisterSjukpenningType;
import se.inera.intygstjanster.fk.services.v1.ErrorIdType;
import se.inera.intygstjanster.fk.services.v1.ResultCodeType;

import com.fasterxml.jackson.databind.ObjectMapper;

public class SjukpenningModuleApi implements ModuleApi {

    private static final Logger LOG = LoggerFactory.getLogger(SjukpenningModuleApi.class);

    @Autowired
    private WebcertModelFactory webcertModelFactory;

    @Autowired
    private InternalDraftValidator internalDraftValidator;

    @Autowired
    private ConverterUtil converterUtil;

    @Autowired
    @Qualifier("sjukpenning-objectMapper")
    private ObjectMapper objectMapper;

    private ModuleContainerApi moduleContainer;

    @Autowired
    private RegisterSjukpenningResponderInterface registerSjukpenningResponderInterface;

    @Autowired
    private GetSjukpenningResponderInterface getSjukpenningResponderInterface;

    /**
     * {@inheritDoc}
     */
    @Override
    public ValidateDraftResponse validateDraft(InternalModelHolder internalModel) throws ModuleException {
        return internalDraftValidator.validateDraft(getInternal(internalModel));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PdfResponse pdf(InternalModelHolder internalModel, List<Status> statuses, ApplicationOrigin applicationOrigin) throws ModuleException {
        // TODO
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InternalModelResponse createNewInternal(CreateNewDraftHolder draftCertificateHolder) throws ModuleException {
        try {
            return toInternalModelResponse(webcertModelFactory.createNewWebcertDraft(draftCertificateHolder));

        } catch (ConverterException e) {
            LOG.error("Could not create a new internal Webcert model", e);
            throw new ModuleConverterException("Could not create a new internal Webcert model", e);
        }
    }

    @Override
    public InternalModelResponse createNewInternalFromTemplate(CreateDraftCopyHolder draftCertificateHolder, InternalModelHolder template)
            throws ModuleException {
        try {
            SjukpenningUtlatande internal = getInternal(template);
            return toInternalModelResponse(webcertModelFactory.createCopy(draftCertificateHolder, internal));
        } catch (ConverterException e) {
            LOG.error("Could not create a new internal Webcert model", e);
            throw new ModuleConverterException("Could not create a new internal Webcert model", e);
        }
    }

    @Override
    public Object createNotification(NotificationMessage notificationMessage) throws ModuleException {
        throw new UnsupportedOperationException("Unsupported for this module");
    }

    @Override
    public ModuleContainerApi getModuleContainer() {
        return moduleContainer;
    }

    @Override
    public void setModuleContainer(ModuleContainerApi moduleContainer) {
        this.moduleContainer = moduleContainer;
    }

    @Override
    public void sendCertificateToRecipient(InternalModelHolder internalModel, String logicalAddress, String recipientId) throws ModuleException {
        // TODO
    }

    @Override
    public CertificateResponse getCertificate(String certificateId, String logicalAddress) throws ModuleException {
        GetSjukpenningType request = new GetSjukpenningType();
        request.setIntygsId(certificateId);

        GetSjukpenningResponseType response = getSjukpenningResponderInterface.getSjukpenning(logicalAddress, request);

        switch (response.getResultat().getResultCode()) {
        case INFO:
        case OK:
            return convert(response, false);
        case ERROR:
            ErrorIdType errorId = response.getResultat().getErrorId();
            String resultText = response.getResultat().getResultText();
            switch (errorId) {
            case REVOKED:
                return convert(response, true);
            default:
                LOG.error("Error of type {} occured when retrieving certificate '{}': {}", errorId, certificateId, resultText);
                throw new ModuleException("Error of type " + errorId + " occured when retrieving certificate " + certificateId + ", " + resultText);
            }
        default:
            LOG.error("An unidentified error occured when retrieving certificate '{}': {}", certificateId, response.getResultat().getResultText());
            throw new ModuleException("An unidentified error occured when retrieving certificate " + certificateId + ", "
                    + response.getResultat().getResultText());
        }

    }

    @Override
    public void registerCertificate(InternalModelHolder internalModel, String logicalAddress) throws ModuleException {
        RegisterSjukpenningType request;
        try {
            request = InternalToTransport.convert(converterUtil.fromJsonString(internalModel.getInternalModel()));
        } catch (ConverterException e) {
            LOG.error("Failed to convert to transport format during registerTSBas", e);
            throw new ExternalServiceCallException("Failed to convert to transport format during registerTSBas", e);
        }

        RegisterSjukpenningResponseType response = registerSjukpenningResponderInterface.registerSjukpenning(logicalAddress, request);

        // check whether call was successful or not
        if (response.getResultat().getResultCode() != ResultCodeType.OK) {
            String message = response.getResultat().getResultCode() == ResultCodeType.INFO
                    ? response.getResultat().getResultText()
                    : response.getResultat().getErrorId() + " : " + response.getResultat().getResultText();
            throw new ExternalServiceCallException(message);
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
    public boolean isModelChanged(String persistedState, String currentState) throws ModuleException {
        return false;
    }

    @Override
    public String marshall(String jsonString) throws ModuleException {
        String xmlString = null;
        try {
            SjukpenningUtlatande internal = objectMapper.readValue(jsonString, SjukpenningUtlatande.class);
            RegisterSjukpenningType external = InternalToTransport.convert(internal);
            StringWriter writer = new StringWriter();
            JAXB.marshal(external, writer);
            xmlString = writer.toString();

        } catch (IOException | ConverterException e) {
            LOG.error("Error occured while marshalling: {}", e.getStackTrace().toString());
            throw new ModuleException(e);
        }
        return xmlString;
    }

    private CertificateResponse convert(GetSjukpenningResponseType response, boolean revoked) throws ModuleException {
        try {
            SjukpenningUtlatande utlatande = TransportToInternal.convert(response.getIntyg());
            String internalModel = objectMapper.writeValueAsString(utlatande);
            CertificateMetaData metaData = TransportToInternal.getMetaData(response.getIntyg(), response.getMeta());
            return new CertificateResponse(internalModel, utlatande, metaData, revoked);
        } catch (Exception e) {
            throw new ModuleException(e);
        }
    }

    private SjukpenningUtlatande getInternal(InternalModelHolder internalModel)
            throws ModuleException {

        try {
            SjukpenningUtlatande utlatande = objectMapper.readValue(internalModel.getInternalModel(),
                    SjukpenningUtlatande.class);

            // Explicitly populate the giltighet interval since it is information derived from
            // the arbetsformaga but needs to be serialized into the Utkast model.
            // TODO
            // utlatande.setGiltighet(ArbetsformagaToGiltighet.getGiltighetFromUtlatande(utlatande));
            return utlatande;

        } catch (IOException e) {
            throw new ModuleSystemException("Failed to deserialize internal model", e);
        }
    }

    private InternalModelResponse updateInternal(InternalModelHolder internalModel, HoSPersonal hosPerson, LocalDateTime signingDate)
            throws ModuleException {
        try {
            SjukpenningUtlatande intyg = getInternal(internalModel);
            webcertModelFactory.updateSkapadAv(intyg, hosPerson, signingDate);
            return toInternalModelResponse(intyg);
        } catch (ModuleException e) {
            throw new ModuleException("Error while updating internal model", e);
        }
    }

    private InternalModelResponse toInternalModelResponse(SjukpenningUtlatande internalModel) throws ModuleException {
        try {
            StringWriter writer = new StringWriter();
            objectMapper.writeValue(writer, internalModel);
            return new InternalModelResponse(writer.toString());

        } catch (IOException e) {
            throw new ModuleSystemException("Failed to serialize internal model", e);
        }
    }

}