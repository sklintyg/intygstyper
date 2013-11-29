package se.inera.certificate.modules.fk7263.rest;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.xml.sax.SAXException;

import se.inera.certificate.fk7263.insuranceprocess.healthreporting.mu7263.v3.Lakarutlatande;
import se.inera.certificate.fk7263.insuranceprocess.healthreporting.registermedicalcertificate.v3.RegisterMedicalCertificate;
import se.inera.certificate.fk7263.model.v1.Utlatande;
import se.inera.certificate.model.util.Strings;
import se.inera.certificate.modules.fk7263.model.converter.ExternalToInternalConverter;
import se.inera.certificate.modules.fk7263.model.converter.ExternalToTransportConverter;
import se.inera.certificate.modules.fk7263.model.converter.ExternalToTransportFk7263LegacyConverter;
import se.inera.certificate.modules.fk7263.model.converter.TransportToExternalConverter;
import se.inera.certificate.modules.fk7263.model.converter.TransportToExternalFk7263LegacyConverter;
import se.inera.certificate.modules.fk7263.model.external.Fk7263CertificateContentHolder;
import se.inera.certificate.modules.fk7263.model.external.Fk7263Utlatande;
import se.inera.certificate.modules.fk7263.model.internal.Fk7263Intyg;
import se.inera.certificate.modules.fk7263.pdf.PdfGenerator;
import se.inera.certificate.modules.fk7263.validator.ProgrammaticLegacyTransportSchemaValidator;
import se.inera.certificate.modules.fk7263.validator.UtlatandeValidator;
import se.inera.certificate.validate.ValidationException;

import com.itextpdf.text.DocumentException;

/**
 * @author andreaskaltenbach, marced
 */
public class Fk7263ModuleApi {

    private static final Logger LOG = LoggerFactory.getLogger(Fk7263ModuleApi.class);

    private static final String DATE_FORMAT = "yyyyMMdd";

    private static JAXBContext jaxbContext;

    // Create JAXB context for the transport format(s) this module can handle
    static {
        try {
            jaxbContext = JAXBContext.newInstance(Utlatande.class, RegisterMedicalCertificate.class);
        } catch (JAXBException e) {
            throw new RuntimeException("Failed to create JAXB context", e);
        }
    }

    private static Schema utlatandeSchema;

    private static final String REGISTER_MEDICAL_CERTIFICATE_VERSION = "1.0";
    private static final String UTLATANDE_VERSION = "2.0";

    // create schema for validation
    static {
        try {
            Source utlatandeSchemaFile = new StreamSource(new ClassPathResource("/schemas/fk7263_model.xsd").getFile());
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            utlatandeSchema = schemaFactory.newSchema(utlatandeSchemaFile);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read schema file", e);
        } catch (SAXException e) {
            throw new RuntimeException("Failed to create schema from XSD file", e);
        }
    }

    /**
     * @param transportXml
     * @return
     */
    @POST
    @Path("/unmarshall")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_JSON)
    public Response unmarshall(String transportXml) {

        Object jaxbObject = unmarshallTransportXML(transportXml);

        boolean schemaValidated = false;

        if (jaxbObject instanceof RegisterMedicalCertificate) {
            schemaValidated = false;
        } else if (jaxbObject instanceof Utlatande) {
            Utlatande utlatande = (Utlatande) jaxbObject;
            // perform schema validation
            try {
                Validator validator = utlatandeSchema.newValidator();
                validateSchema(validator, transportXml);
                schemaValidated = true;
            } catch (ValidationException ex) {
                String utlatandeId = utlatande.getUtlatandeId().getExtension();
                String enhetsId = null;
                if (utlatande.getSkapadAv() != null && utlatande.getSkapadAv().getEnhet() != null
                        && utlatande.getSkapadAv().getEnhet().getEnhetsId() != null) {
                    utlatande.getSkapadAv().getEnhet().getEnhetsId().getExtension();
                }
                String message = responseBody(utlatandeId, enhetsId, ex.getMessage());
                return Response.status(Response.Status.BAD_REQUEST).entity(message).build();
            }
        } else {
            LOG.warn("Unsupported XML message: {}", transportXml);
            throw new RuntimeException("Unsupported XML message: " + transportXml);
        }


        Fk7263Utlatande externalModel = convertTransportJaxbToModel(jaxbObject);
        List<String> validationErrors = new ArrayList<String>();

        if (!schemaValidated) {
            validationErrors = new ProgrammaticLegacyTransportSchemaValidator(externalModel).validate();
        }

        if (validationErrors.isEmpty()) {
            Fk7263Intyg internalModel = toInternal(externalModel);
            validationErrors.addAll((new UtlatandeValidator(internalModel).validate()));
        }

        if (validationErrors.isEmpty()) {
            return Response.ok(externalModel).build();
        } else {

            String response = responseBody(extractCertificateId(externalModel), extractEnhetsId(externalModel),
                    Strings.join(",", validationErrors));
            return Response.status(Response.Status.BAD_REQUEST).entity(response).build();
        }
    }

    private String extractCertificateId(Fk7263Utlatande externalModel) {
        if (externalModel != null && externalModel.getId() != null) {
            return externalModel.getId().getRoot();
        }
        return "<not set>";
    }

    private String extractEnhetsId(Fk7263Utlatande externalModel) {
        if (externalModel != null && externalModel.getSkapadAv() != null
                && externalModel.getSkapadAv().getVardenhet() != null
                && externalModel.getSkapadAv().getVardenhet().getId() != null) {
            return externalModel.getSkapadAv().getVardenhet().getId().getExtension();
        }
        return "<not set>";
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

    private void validateSchema(Validator validator, String xml) {

        try {
            validator.validate(new StreamSource(new StringReader(xml)));
        } catch (SAXException e) {
            throw new ValidationException(e.getMessage());
        } catch (IOException e) {
            LOG.error("Failed to validate message against schema", e);
            throw new RuntimeException("Failed to validate message against schema", e);
        }
    }

    /**
     * Converts different types of transportJaxb object to the external module model format.
     * 
     * @param jaxbObject
     * @return Fk7263Utlatande
     */
    private Fk7263Utlatande convertTransportJaxbToModel(Object jaxbObject) {
        if (jaxbObject instanceof Utlatande) {
            return convertToModel((Utlatande) jaxbObject);
        } else if (jaxbObject instanceof RegisterMedicalCertificate) {
            return convertToModel(((RegisterMedicalCertificate) jaxbObject).getLakarutlatande());
        } else {
            throw new RuntimeException("Cannot convert transport format");
        }
    }

    private Fk7263Utlatande convertToModel(Lakarutlatande legacyUtlatande) {
        LOG.debug("Converting {} to externalModuleFormat", legacyUtlatande.getClass().getCanonicalName());
        return TransportToExternalFk7263LegacyConverter.convert(legacyUtlatande);
    }

    private Fk7263Utlatande convertToModel(Utlatande genericUtlatande) {
        LOG.debug("Converting {}  to externalModuleFormat", genericUtlatande.getClass().getCanonicalName());
        return TransportToExternalConverter.convert(genericUtlatande);
    }

    /**
     * Unmarshal xml string into jaxb
     * 
     * @param transportXml
     * @return jaxbObject if unmarshalling was successful
     */
    private Object unmarshallTransportXML(String transportXml) {
        try {
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            return unmarshaller.unmarshal(new StringReader(transportXml));
        } catch (JAXBException e) {
            LOG.error("Failed to unmarshal transportXml", e);
            throw new RuntimeException(e);
        }
    }

    @POST
    @Path("/marshall")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_XML)
    public Response marshall(@HeaderParam("X-Schema-Version") String version, Fk7263Utlatande externalModel) {

        if (REGISTER_MEDICAL_CERTIFICATE_VERSION.equals(version)) {
            RegisterMedicalCertificate registerMedicalCertificateJaxb = ExternalToTransportFk7263LegacyConverter
                    .getJaxbObject(externalModel);
            return Response.ok(registerMedicalCertificateJaxb).build();
        }

        if (UTLATANDE_VERSION.equals(version)) {
            Utlatande utlatande = new ExternalToTransportConverter(externalModel).convert();
            return Response.ok(utlatande).build();
        }

        return Response.status(Response.Status.NOT_IMPLEMENTED)
                .entity("FK7263 module does not support version " + version).build();
    }

    @POST
    @Path("/valid")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response validate(Fk7263Utlatande utlatande) {
        Fk7263Intyg internalModel = null;
        // validate external properties first
        List<String> validationErrors = new ProgrammaticLegacyTransportSchemaValidator(utlatande).validate();
        if (validationErrors.isEmpty()) {
            // passed first level validation, now validate business logic with internal model validation
            internalModel = toInternal(utlatande);
            validationErrors.addAll((new UtlatandeValidator(internalModel).validate()));
        }

        if (validationErrors.isEmpty()) {
            return Response.ok().build();
        } else {
            String response = Strings.join(",", validationErrors);
            return Response.status(Response.Status.BAD_REQUEST).entity(response).build();
        }
    }

    private Fk7263Intyg toInternal(Fk7263Utlatande external) {
        return new ExternalToInternalConverter(external).convert();
    }

    /**
     * The signature of this method must be compatible with the specified "interface" in
     * se.inera.certificate.integration.rest.ModuleRestApi Jackson will try to satisfy the signature of this method once
     * it's been resolved, so the real contract is actually the json structure.
     * 
     * @param contentHolder
     * @return
     */
    @POST
    @Path("/pdf")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("application/pdf")
    public Response pdf(Fk7263CertificateContentHolder contentHolder) {
        // create the internal model that pdf generator expects
        Fk7263Intyg intyg = toInternal(contentHolder.getCertificateContent());
        try {
            byte[] generatedPdf = new PdfGenerator(intyg).getBytes();
            return Response.ok(generatedPdf).header("Content-Disposition", "filename=" + pdfFileName(intyg)).build();
        } catch (IOException e) {
            LOG.error("Failed to generate PDF for certificate #" + intyg.getId(), e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } catch (DocumentException e) {
            LOG.error("Failed to generate PDF for certificate #" + intyg.getId(), e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PUT
    @Path("/internal")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response convertExternalToInternal(Fk7263CertificateContentHolder contentHolder) {

        Fk7263Intyg internal = toInternal(contentHolder.getCertificateContent());
        internal.setStatus(contentHolder.getCertificateContentMeta().getStatuses());

        return Response.ok(internal).build();
    }

    @PUT
    @Path("/external")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response convertInternalToExternal(Object utlatande) {
        return Response.ok(utlatande).build();
    }

    protected String pdfFileName(Fk7263Intyg intyg) {
        return String.format("lakarutlatande_%s_%s-%s.pdf", intyg.getPatientPersonnummer(), intyg.getGiltighet()
                .getFrom().toString(DATE_FORMAT), intyg.getGiltighet().getTom().toString(DATE_FORMAT));
    }
}
