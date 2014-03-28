package se.inera.certificate.modules.fk7263.rest;

import static se.inera.certificate.modules.support.api.dto.TransportModelVersion.LEGACY_LAKARUTLATANDE;
import static se.inera.certificate.modules.support.api.dto.TransportModelVersion.UTLATANDE_V1;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import se.inera.certificate.fk7263.insuranceprocess.healthreporting.mu7263.v3.Lakarutlatande;
import se.inera.certificate.fk7263.insuranceprocess.healthreporting.registermedicalcertificate.v3.RegisterMedicalCertificate;
import se.inera.certificate.fk7263.model.v1.Utlatande;
import se.inera.certificate.model.util.Strings;
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
import se.inera.certificate.modules.support.api.ModuleApi;
import se.inera.certificate.modules.support.api.dto.CreateNewDraftHolder;
import se.inera.certificate.modules.support.api.dto.ExternalModelHolder;
import se.inera.certificate.modules.support.api.dto.ExternalModelResponse;
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
import se.inera.certificate.validate.ValidationException;

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

    /**
     * {@inheritDoc}
     * 
     * <p>
     * Unmmarshalls transport format xml into fitting JaxB Object tree and perform schema validation. If no schema
     * validation errors are found, next validation step is external model format transformation and validation Last
     * step is to validate internal format specific rules.
     */
    @Override
    public ExternalModelResponse unmarshall(TransportModelHolder transportModel) throws ModuleException {
        Object jaxbObject = TransportXmlUtils.unmarshallTransportXML(transportModel.getTransportModel());
        Fk7263Utlatande externalModel = null;
        List<String> validationErrors = new ArrayList<String>();

        if (jaxbObject instanceof RegisterMedicalCertificate) {
            // Convert and validate legacy transport model
            LOG.debug("Converting {} to external model", jaxbObject.getClass().getCanonicalName());
            Lakarutlatande utlatande = ((RegisterMedicalCertificate) jaxbObject).getLakarutlatande();
            externalModel = TransportToExternalFk7263LegacyConverter.convert(utlatande);

            validationErrors.addAll(new ProgrammaticLegacyTransportSchemaValidator(externalModel).validate());

        } else if (jaxbObject instanceof Utlatande) {
            // Convert and validate utlatande transport model
            try {
                TransportXmlUtils.validateSchema(transportModel.getTransportModel());

                LOG.debug("Converting {}  to external model", jaxbObject.getClass().getCanonicalName());
                externalModel = TransportToExternalConverter.convert((Utlatande) jaxbObject);
            } catch (ValidationException ex) {
                validationErrors.add(ex.getMessage());
            }

        } else {
            LOG.error("Unsupported XML message: {}", transportModel.getTransportModel());
            throw new ModuleSystemException("Unsupported XML message type");
        }

        // If no validation errors so far, continue with external validation...
        if (validationErrors.isEmpty()) {
            validationErrors.addAll(validateExternal(externalModel));
        }

        if (validationErrors.isEmpty()) {
            return toExternalModelResponse(externalModel);

        } else {
            String certificateId = extractCertificateId(jaxbObject);

            throw new ModuleValidationException(validationErrors, String.format("Validation failed for intyg %s",
                    certificateId));
        }
    }

    /**
     * Extract the certificate-ID from a jaxbobject of legacy or normal type.
     * 
     * @param jaxbObject
     *            the {@link Object}
     * @return a String with the id
     */
    private String extractCertificateId(Object jaxbObject) {
        String certificateId = "<not set>";
        if (jaxbObject != null) {
            if (jaxbObject instanceof RegisterMedicalCertificate) {
                RegisterMedicalCertificate cert = (RegisterMedicalCertificate) jaxbObject;
                if (cert.getLakarutlatande() != null) {
                    certificateId = cert.getLakarutlatande().getLakarutlatandeId();
                }
            } else if (jaxbObject instanceof Utlatande) {
                Utlatande cert = (Utlatande) jaxbObject;
                if (cert.getUtlatandeId() != null) {
                    certificateId = "[id-root: " + cert.getUtlatandeId().getRoot() + " id-extension: "
                            + cert.getUtlatandeId().getExtension() + "]";
                }
            }
        }
        return certificateId;
    }

    /**
     * Extract the enhets-ID from a jaxbobject of legacy or normal type.
     * 
     * @param jaxbObject
     *            the {@link Object}
     * @return a String with the id
     */
    private String extractEnhetsId(Object jaxbObject) {
        String enhetsId = "<not set>";
        if (jaxbObject != null) {
            if (jaxbObject instanceof RegisterMedicalCertificate) {
                RegisterMedicalCertificate cert = (RegisterMedicalCertificate) jaxbObject;
                if (cert.getLakarutlatande() != null && cert.getLakarutlatande().getSkapadAvHosPersonal() != null
                        && cert.getLakarutlatande().getSkapadAvHosPersonal().getEnhet() != null
                        && cert.getLakarutlatande().getSkapadAvHosPersonal().getEnhet().getEnhetsId() != null) {

                    enhetsId = cert.getLakarutlatande().getSkapadAvHosPersonal().getEnhet().getEnhetsId()
                            .getExtension();
                }
            } else if (jaxbObject instanceof Utlatande) {
                Utlatande cert = (Utlatande) jaxbObject;
                if (cert.getSkapadAv() != null && cert.getSkapadAv().getEnhet() != null
                        && cert.getSkapadAv().getEnhet().getEnhetsId() != null) {
                    enhetsId = cert.getSkapadAv().getEnhet().getEnhetsId().getExtension();
                }
            }
        }
        return enhetsId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TransportModelResponse marshall(ExternalModelHolder externalModel, TransportModelVersion version)
            throws ModuleException {
        if (LEGACY_LAKARUTLATANDE.equals(version)) {
            RegisterMedicalCertificate registerMedicalCertificateJaxb = ExternalToTransportFk7263LegacyConverter
                    .getJaxbObject(getExternal(externalModel));
            return toTransportModelResponse(registerMedicalCertificateJaxb);
        }

        if (UTLATANDE_V1.equals(version)) {
            Utlatande utlatande = externalToTransportConverter.convert(getExternal(externalModel));
            return toTransportModelResponse(utlatande);
        }

        throw new ModuleVersionUnsupportedException("FK7263 does not support transport model version " + version);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String validate(ExternalModelHolder externalModel) throws ModuleException {
        List<String> validationErrors = validateExternal(getExternal(externalModel));

        if (validationErrors.isEmpty()) {
            return null;
        } else {
            String response = Strings.join(",", validationErrors);
            throw new ModuleConverterException(response);
        }
    }

    private List<String> validateExternal(Fk7263Utlatande externalModel) {
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

        return validationErrors;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ValidateDraftResponse validateDraft(InternalModelHolder internalModel) throws ModuleException {
        internalDraftValidator = new InternalDraftValidator();
        return internalDraftValidator.validateDraft(getInternal(internalModel));

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PdfResponse pdf(ExternalModelHolder externalModel) throws ModuleException {
        try {
            Fk7263Intyg intyg = externalToInternalConverter.convert(getExternal(externalModel));
            PdfGenerator pdfGenerator = new PdfGenerator(intyg);
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
            return toInteralModelResponse(webcertModelFactory.createNewWebcertDraft(draftCertificateHolder));

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
            jaxbContext.createMarshaller().marshal(transportModel, writer);
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

}
