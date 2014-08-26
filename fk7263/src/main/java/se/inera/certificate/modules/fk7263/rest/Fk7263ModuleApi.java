package se.inera.certificate.modules.fk7263.rest;

import static se.inera.certificate.modules.support.api.dto.TransportModelVersion.LEGACY_LAKARUTLATANDE;
import static se.inera.certificate.modules.support.api.dto.TransportModelVersion.UTLATANDE_V1;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import se.inera.certificate.fk7263.insuranceprocess.healthreporting.mu7263.v3.Lakarutlatande;
import se.inera.certificate.fk7263.insuranceprocess.healthreporting.registermedicalcertificate.v3.RegisterMedicalCertificate;
import se.inera.certificate.fk7263.model.v1.Utlatande;
import se.inera.certificate.modules.fk7263.model.converter.ConverterException;
import se.inera.certificate.modules.fk7263.model.converter.ExternalToInternalConverter;
import se.inera.certificate.modules.fk7263.model.converter.ExternalToTransportConverter;
import se.inera.certificate.modules.fk7263.model.converter.ExternalToTransportFk7263LegacyConverter;
import se.inera.certificate.modules.fk7263.model.converter.InternalToExternalConverter;
import se.inera.certificate.modules.fk7263.model.converter.TransportToExternalConverter;
import se.inera.certificate.modules.fk7263.model.converter.TransportToExternalFk7263LegacyConverter;
import se.inera.certificate.modules.fk7263.model.converter.WebcertModelFactory;
import se.inera.certificate.modules.fk7263.model.external.Fk7263Utlatande;
import se.inera.certificate.modules.fk7263.model.internal.Fk7263Intyg;
import se.inera.certificate.modules.fk7263.pdf.PdfGenerator;
import se.inera.certificate.modules.fk7263.pdf.PdfGeneratorException;
import se.inera.certificate.modules.fk7263.validator.ExternalValidator;
import se.inera.certificate.modules.fk7263.validator.InternalDraftValidator;
import se.inera.certificate.modules.fk7263.validator.InternalValidator;
import se.inera.certificate.modules.fk7263.validator.ProgrammaticLegacyTransportSchemaValidator;
import se.inera.certificate.modules.support.ApplicationOrigin;
import se.inera.certificate.modules.support.api.ModuleApi;
import se.inera.certificate.modules.support.api.dto.CreateNewDraftHolder;
import se.inera.certificate.modules.support.api.dto.ExternalModelHolder;
import se.inera.certificate.modules.support.api.dto.ExternalModelResponse;
import se.inera.certificate.modules.support.api.dto.HoSPersonal;
import se.inera.certificate.modules.support.api.dto.InternalModelHolder;
import se.inera.certificate.modules.support.api.dto.InternalModelResponse;
import se.inera.certificate.modules.support.api.dto.PdfResponse;
import se.inera.certificate.modules.support.api.dto.TransportModelHolder;
import se.inera.certificate.modules.support.api.dto.TransportModelResponse;
import se.inera.certificate.modules.support.api.dto.TransportModelVersion;
import se.inera.certificate.modules.support.api.dto.ValidateDraftResponse;
import se.inera.certificate.modules.support.api.exception.ModuleConverterException;
import se.inera.certificate.modules.support.api.exception.ModuleException;
import se.inera.certificate.modules.support.api.exception.ModuleSystemException;
import se.inera.certificate.modules.support.api.exception.ModuleValidationException;
import se.inera.certificate.modules.support.api.exception.ModuleVersionUnsupportedException;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author andreaskaltenbach, marced
 */
public class Fk7263ModuleApi implements ModuleApi {

    private static final Logger LOG = LoggerFactory.getLogger(Fk7263ModuleApi.class);

    @Autowired
    private WebcertModelFactory webcertModelFactory;

    @Autowired
    private InternalDraftValidator internalDraftValidator;

    @Autowired
    private ExternalToInternalConverter externalToInternalConverter;

    @Autowired
    private ExternalToTransportConverter externalToTransportConverter;

    @Autowired
    private InternalToExternalConverter internalToExternalConverter;

    @Autowired
    @Qualifier("fk7263-objectMapper")
    private ObjectMapper objectMapper;

    @Autowired
    @Qualifier("fk7263-jaxbContext")
    private JAXBContext jaxbContext;

    @Autowired
    @Qualifier("fk7263-jaxbContext-legacy")
    private JAXBContext jaxbContextLegacy;

    /**
     * {@inheritDoc}
     * <p/>
     * Unmmarshalls transport format xml into fitting JaxB Object tree and perform schema validation. If no schema
     * validation errors are found, next validation step is external model format transformation and validation Last
     * step is to validate internal format specific rules.
     */
    @Override
    public ExternalModelResponse unmarshall(TransportModelHolder transportModel) throws ModuleException {
        Object jaxbObject = TransportXmlUtils.unmarshallTransportXML(transportModel.getTransportModel());
        Fk7263Utlatande externalModel = null;

        if (jaxbObject instanceof RegisterMedicalCertificate) {
            // Convert and validate legacy transport model
            LOG.debug("Converting {} to external model", jaxbObject.getClass().getCanonicalName());
            Lakarutlatande utlatande = ((RegisterMedicalCertificate) jaxbObject).getLakarutlatande();
            externalModel = TransportToExternalFk7263LegacyConverter.convert(utlatande);

            validateLegacySchema(externalModel);

        } else if (jaxbObject instanceof Utlatande) {
            // Convert and validate utlatande transport model
            TransportXmlUtils.validateSchema(transportModel.getTransportModel());

            LOG.debug("Converting {}  to external model", jaxbObject.getClass().getCanonicalName());
            try {
                externalModel = TransportToExternalConverter.convert((Utlatande) jaxbObject);
            } catch (ConverterException e) {
                throw new ModuleConverterException(String.format("Conversion failed with stacktrace: %s", e));
            }

        } else {
            LOG.error("Unsupported XML message: {}", transportModel.getTransportModel());
            throw new ModuleSystemException("Unsupported XML message type");
        }

        validateExternal(externalModel);

        return toExternalModelResponse(externalModel);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TransportModelResponse marshall(ExternalModelHolder externalModel, TransportModelVersion version)
            throws ModuleException {
        Fk7263Utlatande utlatande = getExternal(externalModel);

        if (LEGACY_LAKARUTLATANDE.equals(version)) {
            validateExternal(utlatande);
            validateLegacySchema(utlatande);

            RegisterMedicalCertificate registerMedicalCertificateJaxb = ExternalToTransportFk7263LegacyConverter.getJaxbObject(utlatande);

            return toTransportModelResponse(registerMedicalCertificateJaxb);
        }

        if (UTLATANDE_V1.equals(version)) {
            TransportModelResponse response = toTransportModelResponse(externalToTransportConverter.convert(utlatande));

            validateExternal(utlatande);
            TransportXmlUtils.validateSchema(response.getTransportModel());

            return response;
        }

        throw new ModuleVersionUnsupportedException("FK7263 does not support transport model version " + version);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validate(ExternalModelHolder externalModel) throws ModuleException {
        validateExternal(getExternal(externalModel));
    }

    private void validateExternal(Fk7263Utlatande externalModel) throws ModuleValidationException {
        List<String> validationErrors = new ExternalValidator(externalModel).validate();

        // If no validation errors so far, continue with internal validation...
        if (validationErrors.isEmpty()) {
            try {
                Fk7263Intyg internalModel = externalToInternalConverter.convert(externalModel);
                validationErrors.addAll((new InternalValidator(internalModel).validate()));
            } catch (ConverterException e) {
                validationErrors.add("Failed to convert utlatande to internal model");
            }
        }

        if (!validationErrors.isEmpty()) {
            throw new ModuleValidationException(validationErrors);
        }
    }

    private void validateLegacySchema(Fk7263Utlatande externalModel) throws ModuleValidationException {
        List<String> validationErrors = new ProgrammaticLegacyTransportSchemaValidator(externalModel).validate();

        if (!validationErrors.isEmpty()) {
            throw new ModuleValidationException(validationErrors);
        }
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
    public PdfResponse pdf(ExternalModelHolder externalModel, ApplicationOrigin applicationOrigin) throws ModuleException {
        try {
            Fk7263Intyg intyg = externalToInternalConverter.convert(getExternal(externalModel));
            PdfGenerator pdfGenerator = new PdfGenerator(intyg, applicationOrigin);
            return new PdfResponse(pdfGenerator.getBytes(), pdfGenerator.generatePdfFilename());

        } catch (ConverterException e) {
            LOG.error("Failed to generate PDF - conversion to internal model failed", e);
            throw new ModuleConverterException("Failed to generate PDF - conversion to internal model failed", e);

        } catch (PdfGeneratorException e) {
            LOG.error("Failed to generate PDF for certificate!", e);
            throw new ModuleSystemException("Failed to generate PDF for certificate!", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InternalModelResponse convertExternalToInternal(ExternalModelHolder externalModel) throws ModuleException {
        try {
            return toInteralModelResponse(externalToInternalConverter.convert(getExternal(externalModel)));
        } catch (ConverterException e) {
            LOG.error("Could not convert external model to internal model", e);
            throw new ModuleConverterException("Could not convert external model to internal model", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ExternalModelResponse convertInternalToExternal(InternalModelHolder internalModel) throws ModuleException {
        try {
            return toExternalModelResponse(internalToExternalConverter.convert(getInternal(internalModel)));
        } catch (ConverterException e) {
            LOG.error("Could not convert external model to internal model", e);
            throw new ModuleConverterException("Could not convert external model to internal model", e);
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
    public InternalModelResponse createNewInternalFromTemplate(CreateNewDraftHolder draftCertificateHolder, ExternalModelHolder template) throws ModuleException {
        try {
            se.inera.certificate.modules.fk7263.model.internal.Fk7263Intyg internal = externalToInternalConverter.convert(getExternal(template));
            return toInteralModelResponse(webcertModelFactory.createNewWebcertDraft(draftCertificateHolder, internal));
        } catch (ConverterException e) {
            LOG.error("Could not create a new internal Webcert model", e);
            throw new ModuleConverterException("Could not create a new internal Webcert model", e);
        }
    }

    // Private transformation methods for building responses

    private se.inera.certificate.modules.fk7263.model.external.Fk7263Utlatande getExternal(
            ExternalModelHolder externalModel) throws ModuleException {
        try {
            return objectMapper.readValue(externalModel.getExternalModel(),
                    se.inera.certificate.modules.fk7263.model.external.Fk7263Utlatande.class);

        } catch (IOException e) {
            throw new ModuleSystemException("Failed to deserialize external model", e);
        }
    }

    private se.inera.certificate.modules.fk7263.model.internal.Fk7263Intyg getInternal(InternalModelHolder internalModel)
            throws ModuleException {
        try {
            return objectMapper.readValue(internalModel.getInternalModel(),
                    se.inera.certificate.modules.fk7263.model.internal.Fk7263Intyg.class);

        } catch (IOException e) {
            throw new ModuleSystemException("Failed to deserialize internal model", e);
        }
    }

    private TransportModelResponse toTransportModelResponse(Object transportModel) throws ModuleException {
        try {
            StringWriter writer = new StringWriter();
            if (transportModel instanceof RegisterMedicalCertificate) {
                jaxbContextLegacy.createMarshaller().marshal(transportModel, writer);
            } else {
                jaxbContext.createMarshaller().marshal(transportModel, writer);
            }
            return new TransportModelResponse(writer.toString());

        } catch (JAXBException e) {
            throw new ModuleSystemException("Failed to marshall transport model", e);
        }
    }

    private ExternalModelResponse toExternalModelResponse(
            se.inera.certificate.modules.fk7263.model.external.Fk7263Utlatande externalModel) throws ModuleException {
        try {
            StringWriter writer = new StringWriter();
            objectMapper.writeValue(writer, externalModel);
            return new ExternalModelResponse(writer.toString(), externalModel);

        } catch (IOException e) {
            throw new ModuleSystemException("Failed to serialize external model", e);
        }
    }

    private InternalModelResponse toInteralModelResponse(
            se.inera.certificate.modules.fk7263.model.internal.Fk7263Intyg internalModel) throws ModuleException {
        try {
            StringWriter writer = new StringWriter();
            objectMapper.writeValue(writer, internalModel);
            return new InternalModelResponse(writer.toString());

        } catch (IOException e) {
            throw new ModuleSystemException("Failed to serialize internal model", e);
        }
    }

    @Override
    public String getComplementaryInfo(ExternalModelHolder externalModel) throws ModuleException {
        Fk7263Utlatande utlatande = getExternal(externalModel);
        return String.format("%s till %s", utlatande.getValidFromDate(), utlatande.getValidToDate());
    }

    @Override
    public InternalModelResponse updateInternal(InternalModelHolder internalModel, HoSPersonal hosPerson, LocalDateTime signingDate) throws ModuleException {
        try {
            Fk7263Intyg intyg = getInternal(internalModel);
            webcertModelFactory.updateSkapadAv(intyg, hosPerson, signingDate);
            return toInteralModelResponse(intyg);

        } catch (ModuleException e) {
            throw new ModuleException("Convert error of internal model", e);
        }
    }
}
