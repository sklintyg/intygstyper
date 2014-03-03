package se.inera.certificate.modules.fk7263.rest;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.ServerErrorException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import se.inera.certificate.fk7263.insuranceprocess.healthreporting.mu7263.v3.Lakarutlatande;
import se.inera.certificate.fk7263.insuranceprocess.healthreporting.registermedicalcertificate.v3.RegisterMedicalCertificate;
import se.inera.certificate.fk7263.model.v1.Utlatande;
import se.inera.certificate.model.util.Strings;
import se.inera.certificate.modules.fk7263.model.converter.ConverterException;
import se.inera.certificate.modules.fk7263.model.converter.ExternalToInternalConverter;
import se.inera.certificate.modules.fk7263.model.converter.ExternalToTransportConverter;
import se.inera.certificate.modules.fk7263.model.converter.ExternalToTransportFk7263LegacyConverter;
import se.inera.certificate.modules.fk7263.model.converter.TransportToExternalConverter;
import se.inera.certificate.modules.fk7263.model.converter.TransportToExternalFk7263LegacyConverter;
import se.inera.certificate.modules.fk7263.model.converter.WebcertModelFactory;
import se.inera.certificate.modules.fk7263.model.external.Fk7263CertificateContentHolder;
import se.inera.certificate.modules.fk7263.model.external.Fk7263Utlatande;
import se.inera.certificate.modules.fk7263.model.internal.Fk7263Intyg;
import se.inera.certificate.modules.fk7263.pdf.PdfGenerator;
import se.inera.certificate.modules.fk7263.pdf.PdfGeneratorException;
import se.inera.certificate.modules.fk7263.rest.dto.CreateNewDraftCertificateHolder;
import se.inera.certificate.modules.fk7263.rest.dto.ValidateDraftResponseHolder;
import se.inera.certificate.modules.fk7263.validator.ExternalValidator;
import se.inera.certificate.modules.fk7263.validator.InternalDraftValidator;
import se.inera.certificate.modules.fk7263.validator.InternalValidator;
import se.inera.certificate.modules.fk7263.validator.ProgrammaticLegacyTransportSchemaValidator;
import se.inera.certificate.validate.ValidationException;

/**
 * @author andreaskaltenbach, marced
 */
public class Fk7263ModuleApi implements ModuleApi {

    private static final Logger LOG = LoggerFactory.getLogger(Fk7263ModuleApi.class);

    @Autowired
    private WebcertModelFactory webcertModelFactory;

    @Autowired
    private InternalDraftValidator internalDraftValidator;

    @Context
    private HttpServletResponse httpResponse;

    private static final String REGISTER_MEDICAL_CERTIFICATE_VERSION = "1.0";
    private static final String UTLATANDE_VERSION = "2.0";

    /**
     * {@inheritDoc}
     * 
     * <p>
     * Unmmarshalls transport format xml into fitting JaxB Object tree and perform schema validation. If no schema
     * validation errors are found, next validation step is external model format transformation and validation Last
     * step is to validate internal format specific rules.
     */
    public Fk7263Utlatande unmarshall(String transportXml) {

        Object jaxbObject = TransportXmlUtils.unmarshallTransportXML(transportXml);
        Fk7263Utlatande externalModel = null;
        List<String> validationErrors = new ArrayList<String>();

        // Perform Schema type validation
        if (jaxbObject instanceof RegisterMedicalCertificate) {
            externalModel = convertTransportJaxbToModel(jaxbObject);
            validationErrors.addAll(new ProgrammaticLegacyTransportSchemaValidator(externalModel).validate());
        } else if (jaxbObject instanceof Utlatande) {
            try {
                TransportXmlUtils.validateSchema(transportXml);
                externalModel = convertTransportJaxbToModel(jaxbObject);
            } catch (ValidationException ex) {
                validationErrors.add(ex.getMessage());
            }
        } else {
            LOG.warn("Unsupported XML message: {}", transportXml);
            throw new RuntimeException("Unsupported XML message: " + transportXml);
        }

        // If no validation errors so far, continue with external validation...
        if (validationErrors.isEmpty()) {
            validationErrors.addAll((new ExternalValidator(externalModel).validate()));
        }
        // If no validation errors so far, continue with internal validation...
        if (validationErrors.isEmpty()) {
            try {
                Fk7263Intyg internalModel = toInternal(externalModel);
                validationErrors.addAll((new InternalValidator(internalModel).validate()));
            } catch (ConverterException e) {
                validationErrors.add("Failed to convert utlatande to internal model");
            }
        }

        if (validationErrors.isEmpty()) {
            return externalModel;
        } else {

            String response = responseBody(extractCertificateId(jaxbObject), extractEnhetsId(jaxbObject),
                    Strings.join(",", validationErrors));
            throw new WebApplicationException(Response.status(Status.BAD_REQUEST).entity(response).build());
        }
    }

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

    private String responseBody(String utlatandeId, String enhetsId, String validationErrors) {
        StringBuffer sb = new StringBuffer("Validation failed for intyg ");
        sb.append(utlatandeId);
        if (enhetsId != null) {
            sb.append(" issued by ");
            sb.append(enhetsId);
        }
        sb.append(": ");
        sb.append(validationErrors);
        return sb.toString();
    }

    /**
     * Converts different types of transportJaxb object to the external module model format.
     * 
     * @param jaxbObject
     * @return Fk7263Utlatande
     */
    private Fk7263Utlatande convertTransportJaxbToModel(Object jaxbObject) {
        if (jaxbObject instanceof Utlatande) {
            LOG.debug("Converting {}  to externalModuleFormat", jaxbObject.getClass().getCanonicalName());
            return TransportToExternalConverter.convert((Utlatande) jaxbObject);

        } else if (jaxbObject instanceof RegisterMedicalCertificate) {
            LOG.debug("Converting {} to externalModuleFormat", jaxbObject.getClass().getCanonicalName());
            return TransportToExternalFk7263LegacyConverter.convert((Lakarutlatande) jaxbObject);

        } else {
            throw new RuntimeException("Cannot convert transport format");
        }
    }

    /**
     * {@inheritDoc}
     */
    public Object marshall(String version, Fk7263Utlatande externalModel) {
        if (REGISTER_MEDICAL_CERTIFICATE_VERSION.equals(version)) {
            RegisterMedicalCertificate registerMedicalCertificateJaxb = ExternalToTransportFk7263LegacyConverter
                    .getJaxbObject(externalModel);
            return registerMedicalCertificateJaxb;
        }

        if (UTLATANDE_VERSION.equals(version)) {
            Utlatande utlatande = new ExternalToTransportConverter(externalModel).convert();
            return utlatande;
        }

        String response = "FK7263 module does not support version " + version;
        throw new WebApplicationException(Response.status(Status.NOT_IMPLEMENTED).entity(response).build());
    }

    /**
     * {@inheritDoc}
     */
    public String validate(Fk7263Utlatande utlatande) {
        Fk7263Intyg internalModel = null;
        // validate external properties first
        List<String> validationErrors = new ProgrammaticLegacyTransportSchemaValidator(utlatande).validate();
        if (validationErrors.isEmpty()) {
            // passed first level validation, now validate business logic with internal model validation
            try {
                internalModel = toInternal(utlatande);
                validationErrors.addAll((new InternalValidator(internalModel).validate()));
            } catch (ConverterException e) {
                validationErrors.add("Failed to convert utlatande to internal model");
            }
        }

        if (validationErrors.isEmpty()) {
            return null;
        } else {
            String response = Strings.join(",", validationErrors);
            throw new WebApplicationException(Response.status(Status.BAD_REQUEST).entity(response).build());
        }
    }

    /**
     * {@inheritDoc}
     */
    public ValidateDraftResponseHolder validateDraft(
            se.inera.certificate.modules.fk7263.model.internal.Fk7263Intyg utlatande) {
        return internalDraftValidator.validateDraft(utlatande);
    }

    private Fk7263Intyg toInternal(Fk7263Utlatande external) throws ConverterException {
        return new ExternalToInternalConverter(external).convert();
    }

    /**
     * {@inheritDoc}
     */
    public byte[] pdf(Fk7263CertificateContentHolder contentHolder) {
        try {
            Fk7263Intyg intyg = toInternal(contentHolder.getCertificateContent());
            PdfGenerator pdfGenerator = new PdfGenerator(intyg);
            httpResponse.addHeader("Content-Disposition", "filename=" + pdfGenerator.generatePdfFilename());
            return pdfGenerator.getBytes();

        } catch (ConverterException e) {
            LOG.error("Failed to generate PDF - conversion to internal model failed", e);
            throw new BadRequestException(e);

        } catch (PdfGeneratorException e) {
            LOG.error("Failed to generate PDF for certificate!", e);
            throw new InternalServerErrorException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public Fk7263Intyg convertExternalToInternal(Fk7263CertificateContentHolder contentHolder) {
        try {
            Fk7263Intyg internal = toInternal(contentHolder.getCertificateContent());
            internal.setStatus(contentHolder.getCertificateContentMeta().getStatuses());

            return internal;

        } catch (ConverterException e) {
            LOG.error("Could not convert external model to internal model", e);
            throw new BadRequestException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public Fk7263Utlatande convertInternalToExternal(Fk7263Intyg utlatande) {
        throw new ServerErrorException(Status.NOT_IMPLEMENTED);
    }

    /**
     * {@inheritDoc}
     */
    public se.inera.certificate.modules.fk7263.model.internal.Fk7263Intyg createNewInternal(
            CreateNewDraftCertificateHolder draftCertificateHolder) {
        try {
            return webcertModelFactory.createNewWebcertDraft(draftCertificateHolder);

        } catch (ConverterException e) {
            LOG.error("Could not create a new internal Webcert model", e);
            throw new BadRequestException(e);
        }
    }
}
