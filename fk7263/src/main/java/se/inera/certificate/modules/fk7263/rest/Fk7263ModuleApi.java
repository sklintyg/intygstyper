package se.inera.certificate.modules.fk7263.rest;

import java.io.IOException;
import java.io.StringWriter;

import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.w3.wsaddressing10.AttributedURIType;

import se.inera.certificate.clinicalprocess.healthcond.certificate.getmedicalcertificateforcare.v1.GetMedicalCertificateForCareRequestType;
import se.inera.certificate.clinicalprocess.healthcond.certificate.getmedicalcertificateforcare.v1.GetMedicalCertificateForCareResponderInterface;
import se.inera.certificate.clinicalprocess.healthcond.certificate.getmedicalcertificateforcare.v1.GetMedicalCertificateForCareResponseType;
import se.inera.certificate.model.converter.util.ConverterException;
import se.inera.certificate.modules.fk7263.model.converter.InternalToTransport;
import se.inera.certificate.modules.fk7263.model.converter.TransportToInternal;
import se.inera.certificate.modules.fk7263.model.converter.WebcertModelFactory;
import se.inera.certificate.modules.fk7263.model.converter.util.ConverterUtil;
import se.inera.certificate.modules.fk7263.model.internal.Utlatande;
import se.inera.certificate.modules.fk7263.model.util.ModelCompareUtil;
import se.inera.certificate.modules.fk7263.pdf.PdfGenerator;
import se.inera.certificate.modules.fk7263.pdf.PdfGeneratorException;
import se.inera.certificate.modules.fk7263.validator.InternalDraftValidator;
import se.inera.certificate.modules.support.ApplicationOrigin;
import se.inera.certificate.modules.support.api.ModuleApi;
import se.inera.certificate.modules.support.api.ModuleContainerApi;
import se.inera.certificate.modules.support.api.dto.CertificateMetaData;
import se.inera.certificate.modules.support.api.dto.CertificateResponse;
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
import se.inera.certificate.schema.util.ClinicalProcessCertificateMetaTypeConverter;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificate.v3.rivtabp20.RegisterMedicalCertificateResponderInterface;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificateresponder.v3.RegisterMedicalCertificateResponseType;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificateresponder.v3.RegisterMedicalCertificateType;
import se.inera.ifv.insuranceprocess.healthreporting.v2.ResultCodeEnum;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author andreaskaltenbach, marced
 */
public class Fk7263ModuleApi implements ModuleApi {

    private static final Logger LOG = LoggerFactory.getLogger(Fk7263ModuleApi.class);

    @Value("${intygstjanst.logicaladdress}")
    private String intygstjanstLogicalAddress;

    @Autowired
    private WebcertModelFactory webcertModelFactory;

    @Autowired
    private InternalDraftValidator internalDraftValidator;

    @Autowired
    @Qualifier("fk7263-objectMapper")
    private ObjectMapper objectMapper;

    @Autowired(required=false)
    @Qualifier("registerMedicalCertificateClient")
    private RegisterMedicalCertificateResponderInterface registerMedicalCertificateClient;

    @Autowired
    private ModelCompareUtil modelCompareUtil;

    public void setRegisterMedicalCertificateClient(RegisterMedicalCertificateResponderInterface registerMedicalCertificateClient) {
        this.registerMedicalCertificateClient = registerMedicalCertificateClient;
    }
    
    @Autowired(required=false)
    private GetMedicalCertificateForCareResponderInterface getMedicalCertificateForCareResponderInterface;

    public void setGetMedicalCertificateForCareResponderInterface(
            GetMedicalCertificateForCareResponderInterface getMedicalCertificateForCareResponderInterface) {
        this.getMedicalCertificateForCareResponderInterface = getMedicalCertificateForCareResponderInterface;
    }

    @Autowired
    private ConverterUtil converterUtil;

    private ModuleContainerApi moduleContainer;

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
    public PdfResponse pdf(InternalModelHolder internalModel, ApplicationOrigin applicationOrigin) throws ModuleException {
        try {
            Utlatande intyg = getInternal(internalModel);
            PdfGenerator pdfGenerator = new PdfGenerator(intyg, applicationOrigin);
            return new PdfResponse(pdfGenerator.getBytes(), pdfGenerator.generatePdfFilename());
        } catch (PdfGeneratorException e) {
            LOG.error("Failed to generate PDF for certificate!", e);
            throw new ModuleSystemException("Failed to generate PDF for certificate!", e);
        }
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

    @Override
    public InternalModelResponse createNewInternalFromTemplate(CreateNewDraftHolder draftCertificateHolder, InternalModelHolder template) throws ModuleException {
        try {
            Utlatande internal = getInternal(template);
            return toInteralModelResponse(webcertModelFactory.createNewWebcertDraft(draftCertificateHolder, internal));
        } catch (ConverterException e) {
            LOG.error("Could not create a new internal Webcert model", e);
            throw new ModuleConverterException("Could not create a new internal Webcert model", e);
        }
    }

    // Private transformation methods for building responses

    private se.inera.certificate.modules.fk7263.model.internal.Utlatande getInternal(InternalModelHolder internalModel)
            throws ModuleException {
        try {
            return objectMapper.readValue(internalModel.getInternalModel(),
                    se.inera.certificate.modules.fk7263.model.internal.Utlatande.class);

        } catch (IOException e) {
            throw new ModuleSystemException("Failed to deserialize internal model", e);
        }
    }

    private InternalModelResponse toInteralModelResponse(
            se.inera.certificate.modules.fk7263.model.internal.Utlatande internalModel) throws ModuleException {
        try {
            StringWriter writer = new StringWriter();
            objectMapper.writeValue(writer, internalModel);
            return new InternalModelResponse(writer.toString());

        } catch (IOException e) {
            throw new ModuleSystemException("Failed to serialize internal model", e);
        }
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
    public void sendCertificateToRecipient(InternalModelHolder internalModel, String recipient) throws ModuleException {
        try {
            Utlatande utlatande = converterUtil.fromJsonString(internalModel.getInternalModel());
            Object request = InternalToTransport.getJaxbObject(utlatande);
            AttributedURIType address = new AttributedURIType();
            address.setValue(recipient);
            RegisterMedicalCertificateResponseType response = registerMedicalCertificateClient
                    .registerMedicalCertificate(address, (RegisterMedicalCertificateType) request);

            // check whether call was successful or not
            if (response.getResult().getResultCode() != ResultCodeEnum.OK) {
                String message = response.getResult().getResultCode() == ResultCodeEnum.INFO ?
                        response.getResult().getInfoText() :
                            response.getResult().getErrorId() + " : " + response.getResult().getErrorText();
                throw new ExternalServiceCallException(message);
            }

        } catch (ConverterException e) {
            throw new ModuleException(e);
        }
    }

    @Override
    public CertificateResponse getCertificate(String certificateId) throws ModuleException {
        GetMedicalCertificateForCareRequestType request = new GetMedicalCertificateForCareRequestType();
        request.setCertificateId(certificateId);

        GetMedicalCertificateForCareResponseType response = getMedicalCertificateForCareResponderInterface.getMedicalCertificateForCare(intygstjanstLogicalAddress,
                request);
        
        switch (response.getResult().getResultCode()) {
        case INFO:
        case OK:
            return convert(response, false);
        case ERROR:
            switch (response.getResult().getErrorId()) {
            case REVOKED:
                return convert(response, true);
            case VALIDATION_ERROR:
                throw new ModuleException("getMedicalCertificateForCare WS call: VALIDATION_ERROR :" + response.getResult().getResultText());
            default:
                throw new ModuleException("getMedicalCertificateForCare WS call: ERROR :" + response.getResult().getResultText());
            }
        default:
            throw new ModuleException("getMedicalCertificateForCare WS call: ERROR :" + response.getResult().getResultText());
        }
    }
    
    private CertificateResponse convert(GetMedicalCertificateForCareResponseType response, boolean revoked) throws ModuleException {
        try {
            Utlatande utlatande = TransportToInternal.convert(response.getLakarutlatande());
            String internalModel = objectMapper.writeValueAsString(utlatande);
            CertificateMetaData metaData = ClinicalProcessCertificateMetaTypeConverter.toCertificateMetaData(response.getMeta());
            return new CertificateResponse(internalModel, utlatande, metaData, revoked);
        } catch (Exception e) {
            throw new ModuleException(e);
        }
    }

    @Override
    public void registerCertificate(InternalModelHolder internalModel) throws ModuleException {
        sendCertificateToRecipient(internalModel, intygstjanstLogicalAddress);
    }

    @Override
    public InternalModelResponse updateBeforeSave(InternalModelHolder internalModel, HoSPersonal hosPerson) throws ModuleException {
        return updateInternal(internalModel, hosPerson, null);
    }

    @Override
    public InternalModelResponse updateBeforeSigning(InternalModelHolder internalModel, HoSPersonal hosPerson, LocalDateTime signingDate) throws ModuleException {
        return updateInternal(internalModel, hosPerson, signingDate);
    }

    private InternalModelResponse updateInternal(InternalModelHolder internalModel, HoSPersonal hosPerson, LocalDateTime signingDate) throws ModuleException {
        try{
            Utlatande intyg = getInternal(internalModel);
            webcertModelFactory.updateSkapadAv(intyg, hosPerson, signingDate);
            return toInteralModelResponse(intyg);
        } catch (ModuleException e) {
            throw new ModuleException("Error while updating internal model", e);
        }
    }

    @Override
    public boolean isModelChanged(String persistedState, String currentState) throws ModuleException  {
        Utlatande oldUtlatande;
        Utlatande newUtlatande;
        try {
            oldUtlatande = objectMapper.readValue(persistedState, Utlatande.class);
            newUtlatande = objectMapper.readValue(currentState, Utlatande.class);
        } catch (IOException e) {
            throw new ModuleException(e);
        }

        if (modelCompareUtil.modelDiffers(oldUtlatande, newUtlatande)) {
            return true;
        }
        else {
            return false;
        }
    }


}
