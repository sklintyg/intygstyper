package se.inera.certificate.modules.fk7263.rest;

import static se.inera.certificate.common.enumerations.Recipients.FK;
import static se.inera.certificate.common.util.StringUtil.isNullOrEmpty;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.annotations.VisibleForTesting;
import iso.v21090.dt.v1.CD;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.w3.wsaddressing10.AttributedURIType;
import se.inera.certificate.model.Status;
import se.inera.certificate.model.converter.util.ConverterException;
import se.inera.certificate.modules.fk7263.model.converter.ArbetsformagaToGiltighet;
import se.inera.certificate.modules.fk7263.model.converter.InternalToNotification;
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
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.AktivitetType;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.Aktivitetskod;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.LakarutlatandeType;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.MedicinsktTillstandType;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificate.rivtabp20.v3.RegisterMedicalCertificateResponderInterface;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificateresponder.v3.RegisterMedicalCertificateResponseType;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificateresponder.v3.RegisterMedicalCertificateType;
import se.inera.ifv.insuranceprocess.healthreporting.v2.ResultCodeEnum;
import se.inera.intyg.clinicalprocess.healthcond.certificate.getmedicalcertificateforcare.v1.GetMedicalCertificateForCareRequestType;
import se.inera.intyg.clinicalprocess.healthcond.certificate.getmedicalcertificateforcare.v1.GetMedicalCertificateForCareResponderInterface;
import se.inera.intyg.clinicalprocess.healthcond.certificate.getmedicalcertificateforcare.v1.GetMedicalCertificateForCareResponseType;
import se.inera.intyg.common.schemas.clinicalprocess.healthcond.certificate.converter.ClinicalProcessCertificateMetaTypeConverter;
import se.riv.clinicalprocess.healthcond.certificate.v1.ErrorIdType;

import javax.xml.bind.JAXB;
import javax.xml.ws.soap.SOAPFaultException;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;


/**
 * @author andreaskaltenbach, marced
 */
public class Fk7263ModuleApi implements ModuleApi {

    private static final Logger LOG = LoggerFactory.getLogger(Fk7263ModuleApi.class);

    /* (non-Javadoc)
     *
     * Must only be used to set the code system name when certificate
     * is sent to Försäkringskassan. See JIRA issue WEBCERT-1442
     */
    static final String CODESYSTEMNAME_ICD10 = "ICD-10";

    @Autowired
    private WebcertModelFactory webcertModelFactory;

    @Autowired
    private InternalDraftValidator internalDraftValidator;
    
    @Autowired
    private InternalToNotification internalToNotficationConverter;

    @Autowired
    @Qualifier("fk7263-objectMapper")
    private ObjectMapper objectMapper;

    @Autowired(required = false)
    @Qualifier("registerMedicalCertificateClient")
    private RegisterMedicalCertificateResponderInterface registerMedicalCertificateClient;

    @Autowired
    private ModelCompareUtil modelCompareUtil;

    @Autowired(required = false)
    private GetMedicalCertificateForCareResponderInterface getMedicalCertificateForCareResponderInterface;

    @Autowired
    private ConverterUtil converterUtil;

    private ModuleContainerApi moduleContainer;

    @VisibleForTesting
    public void setRegisterMedicalCertificateClient(RegisterMedicalCertificateResponderInterface registerMedicalCertificateClient) {
        this.registerMedicalCertificateClient = registerMedicalCertificateClient;
    }

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
        try {
            Utlatande intyg = getInternal(internalModel);
            PdfGenerator pdfGenerator = new PdfGenerator(intyg, statuses, applicationOrigin, false);
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
    public PdfResponse pdfEmployer(InternalModelHolder internalModel, List<Status> statuses, ApplicationOrigin applicationOrigin) throws ModuleException {
        try {
            Utlatande intyg = getInternal(internalModel);
            PdfGenerator pdfGenerator = new PdfGenerator(intyg, statuses, applicationOrigin, true);
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
            return toInteralModelResponse(webcertModelFactory.createNewWebcertDraft(draftCertificateHolder));

        } catch (ConverterException e) {
            LOG.error("Could not create a new internal Webcert model", e);
            throw new ModuleConverterException("Could not create a new internal Webcert model", e);
        }
    }

    @Override
    public InternalModelResponse createNewInternalFromTemplate(CreateDraftCopyHolder draftCertificateHolder, InternalModelHolder template) throws ModuleException {
        try {
            Utlatande internal = getInternal(template);
            return toInteralModelResponse(webcertModelFactory.createCopy(draftCertificateHolder, internal));
        } catch (ConverterException e) {
            LOG.error("Could not create a new internal Webcert model", e);
            throw new ModuleConverterException("Could not create a new internal Webcert model", e);
        }
    }

    @Override
    public Object createNotification(NotificationMessage notificationMessage) throws ModuleException {
        return internalToNotficationConverter.createCertificateStatusUpdateForCareType(notificationMessage);
    }

    @Override
    public ModuleContainerApi getModuleContainer() {
        return moduleContainer;
    }

    @Override
    public void setModuleContainer(ModuleContainerApi moduleContainer) {
        this.moduleContainer = moduleContainer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendCertificateToRecipient(InternalModelHolder internalModel, String logicalAddress, String recipientId) throws ModuleException {

        // Check that we got any data at all
        if (internalModel == null) {
            throw new ModuleException("No InternalModelHolder found in call to sendCertificateToRecipient!");
        }

        if (logicalAddress == null) {
            throw new ModuleException("No LogicalAddress found in call to sendCertificateToRecipient!");
        }

        // NOTE: We don't need to check for recipientId

        Utlatande utlatande = converterUtil.fromJsonString(internalModel.getInternalModel());

        try {
            Object request = InternalToTransport.getJaxbObject(utlatande);
            sendCertificateToRecipient((RegisterMedicalCertificateType) request, logicalAddress, recipientId);

        } catch (ConverterException e) {
            throw new ModuleException(e);
        }
    }

    @Override
    public CertificateResponse getCertificate(String certificateId, String logicalAddress) throws ModuleException {
        GetMedicalCertificateForCareRequestType request = new GetMedicalCertificateForCareRequestType();
        request.setCertificateId(certificateId);

        GetMedicalCertificateForCareResponseType response = getMedicalCertificateForCareResponderInterface.
                getMedicalCertificateForCare(logicalAddress, request);

        switch (response.getResult().getResultCode()) {
            case INFO:
            case OK:
                return convert(response, false);
            case ERROR:
                ErrorIdType errorId = response.getResult().getErrorId();
                String resultText = response.getResult().getResultText();
                switch (errorId) {
                case REVOKED:
                    return convert(response, true);
                default:
                    LOG.error("Error of type {} occured when retrieving certificate '{}': {}", errorId, certificateId, resultText);
                    throw new ModuleException("Error of type " + errorId + " occured when retrieving certificate " + certificateId + ", " + resultText);
                }
            default:
                LOG.error("An unidentified error occured when retrieving certificate '{}': {}", certificateId, response.getResult().getResultText());
                throw new ModuleException("An unidentified error occured when retrieving certificate " + certificateId + ", " + response.getResult().getResultText());
        }
    }

    @Override
    public void registerCertificate(InternalModelHolder internalModel, String logicalAddress) throws ModuleException {
        sendCertificateToRecipient(internalModel, logicalAddress, null);
    }

    @Override
    public InternalModelResponse updateBeforeSave(InternalModelHolder internalModel, HoSPersonal hosPerson) throws ModuleException {
        return updateInternal(internalModel, hosPerson, null);
    }

    @Override
    public InternalModelResponse updateBeforeSigning(InternalModelHolder internalModel, HoSPersonal hosPerson, LocalDateTime signingDate) throws ModuleException {
        return updateInternal(internalModel, hosPerson, signingDate);
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

        return false;
    }

    // - - - - - Package scope - - - - - //

    /* (non-Javadoc)
     *
     * Hard code code system name to ICD-10.
     *
     * This is a special case to solve JIRA issue https://inera-certificate.atlassian.net/browse/WEBCERT-1442.
     * It should be removed when Forsakringskassan can handle code system name correctly.
     */
    RegisterMedicalCertificateType whenFkIsRecipientThenSetCodeSystemToICD10(final RegisterMedicalCertificateType request) throws ModuleException {

        LOG.debug("Recipient of RegisterMedicalCertificate certificate is Försäkringskassan");
        LOG.debug("Set element 'lakarutlatande/medicinsktTillstand/tillstandsKod/codeSystemName' to value 'ICD-10'");

        // Check that we got a lakarutlatande element
        if (request.getLakarutlatande() == null) {
            throw new ModuleException("No Lakarutlatande element found in request data!");
        }

        LakarutlatandeType lakarutlatande = request.getLakarutlatande();

        // Decide if this certificate has smittskydd checked
        boolean inSmittskydd = findAktivitetWithCode(request.getLakarutlatande().getAktivitet(),
                Aktivitetskod.AVSTANGNING_ENLIGT_SM_L_PGA_SMITTA) != null ? true : false;

        if (!inSmittskydd) {
            // Check that we got a medicinsktTillstand element
            if (lakarutlatande.getMedicinsktTillstand() == null) {
                throw new ModuleException("No medicinsktTillstand element found in request data. Cannot set codeSystemName to 'ICD-10'!");
            }

            MedicinsktTillstandType medicinsktTillstand = lakarutlatande.getMedicinsktTillstand();

            // Check that we got a tillstandskod element
            if (medicinsktTillstand.getTillstandskod() == null) {
                throw new ModuleException("No tillstandskod element found in request data. Cannot set codeSystemName to 'ICD-10'!");
            }

            CD tillstandskod = medicinsktTillstand.getTillstandskod();
            tillstandskod.setCodeSystemName(CODESYSTEMNAME_ICD10);

            // Update request
            request.getLakarutlatande().getMedicinsktTillstand().setTillstandskod(tillstandskod);

        } else {
            try {
                // tillstandskod is not mandatory when smittskydd is true, just try to set it.
                request.getLakarutlatande().getMedicinsktTillstand().getTillstandskod().setCodeSystemName(CODESYSTEMNAME_ICD10);

            } catch (NullPointerException npe) {
                LOG.debug("No tillstandskod element found in request data. "
                        + "Element is not mandatory when Smittskydd is checked. "
                        + "Cannot set codeSystemName to 'ICD-10'");
            }
        }

        return request;
    }

    @Override
    public String marshall(String jsonString) throws ModuleException {
        String xmlString = null;
        try {
            Utlatande internal = objectMapper.readValue(jsonString, Utlatande.class);
            RegisterMedicalCertificateType external = InternalToTransport.getJaxbObject(internal);
            StringWriter writer = new StringWriter();
            JAXB.marshal(external, writer);
            xmlString = writer.toString();

        } catch (IOException | ConverterException e) {
            LOG.error("Error occured while marshalling: {}", e.getStackTrace().toString());
            throw new ModuleException(e);
        }
        return xmlString;
    }

    // - - - - - Private scope - - - - - //

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

    private AktivitetType findAktivitetWithCode(List<AktivitetType> aktiviteter, Aktivitetskod aktivitetskod) throws ModuleException {
        AktivitetType foundAktivitet = null;

        try {
            if (aktiviteter != null) {
                for (int i = 0; i < aktiviteter.size(); i++) {
                    AktivitetType listAktivitet = aktiviteter.get(i);
                    if (listAktivitet.getAktivitetskod() != null && listAktivitet.getAktivitetskod().compareTo(aktivitetskod) == 0) {
                        foundAktivitet = listAktivitet;
                        break;
                    }
                }
            }
        } catch (Exception e) {
            throw new ModuleException(e.getMessage(), e);
        }

        return foundAktivitet;
    }

    private void sendCertificateToRecipient(RegisterMedicalCertificateType request, final String logicalAddress, final String recipientId) throws ModuleException {

        // This is a special case when recipient is Forsakringskassan. See JIRA issue WEBCERT-1442.
        if (!isNullOrEmpty(recipientId) && recipientId.equalsIgnoreCase(FK.toString())) {
            request = whenFkIsRecipientThenSetCodeSystemToICD10(request);
        }

        AttributedURIType address = new AttributedURIType();
        address.setValue(logicalAddress);

        try {
            RegisterMedicalCertificateResponseType response =
                    registerMedicalCertificateClient.registerMedicalCertificate(address, request);

            // check whether call was successful or not
            if (response.getResult().getResultCode() != ResultCodeEnum.OK) {
                String message = response.getResult().getResultCode() == ResultCodeEnum.INFO
                        ? response.getResult().getInfoText()
                        : response.getResult().getErrorId() + " : " + response.getResult().getErrorText();
                LOG.error("Error occured when sending certificate '{}': {}",
                        request.getLakarutlatande() != null ? request.getLakarutlatande().getLakarutlatandeId() : null,
                        message);
                throw new ExternalServiceCallException(message);
            }
        } catch (SOAPFaultException e) {
            throw new ExternalServiceCallException(e);
        }

    }


    // - - - - - Private transformation methods for building responses - - - - - //

    private se.inera.certificate.modules.fk7263.model.internal.Utlatande getInternal(InternalModelHolder internalModel)
            throws ModuleException {

        try {
            se.inera.certificate.modules.fk7263.model.internal.Utlatande utlatande = objectMapper.readValue(internalModel.getInternalModel(),
                    se.inera.certificate.modules.fk7263.model.internal.Utlatande.class);

            // Explicitly populate the giltighet interval since it is information derived from
            // the arbetsformaga but needs to be serialized into the Utkast model.
            utlatande.setGiltighet(ArbetsformagaToGiltighet.getGiltighetFromUtlatande(utlatande));
            return utlatande;

        } catch (IOException e) {
            throw new ModuleSystemException("Failed to deserialize internal model", e);
        }
    }

    private InternalModelResponse updateInternal(InternalModelHolder internalModel, HoSPersonal hosPerson, LocalDateTime signingDate) throws ModuleException {
        try {
            Utlatande intyg = getInternal(internalModel);
            webcertModelFactory.updateSkapadAv(intyg, hosPerson, signingDate);
            return toInteralModelResponse(intyg);
        } catch (ModuleException e) {
            throw new ModuleException("Error while updating internal model", e);
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

}
