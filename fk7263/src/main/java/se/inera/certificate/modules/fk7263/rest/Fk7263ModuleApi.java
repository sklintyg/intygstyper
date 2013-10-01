package se.inera.certificate.modules.fk7263.rest;

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
import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import com.itextpdf.text.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.xml.sax.SAXException;
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
import se.inera.certificate.modules.fk7263.validator.UtlatandeValidator;
import se.inera.certificate.validate.ValidationException;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.Lakarutlatande;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificate.v3.RegisterMedicalCertificate;

/**
 * @author andreaskaltenbach, marced
 */
public class Fk7263ModuleApi {

    private static final Logger LOG = LoggerFactory.getLogger(Fk7263ModuleApi.class);

    private static final Unmarshaller unmarshaller;

    private static final String CONTENT_DISPOSITION = "Content-Disposition";
    private static final String DATE_FORMAT = "yyyyMMdd";

    // Create unmarshaller for the transport format(s) this module can handle
    static {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Utlatande.class, RegisterMedicalCertificate.class);
            unmarshaller = jaxbContext.createUnmarshaller();
        } catch (JAXBException e) {
            throw new RuntimeException("Failed to create JAXB context", e);
        }
    }

    private static final Validator registerMedicalCertificateSchemaValidator;
    private static final Validator utlatandeSchemaValidator;

    private static final String REGISTER_MEDICAL_CERTIFICATE_VERSION = "1.0";
    private static final String UTLATANDE_VERSION = "2.0";

    // create schema validators
    static {
        try {
            Source registerMedicalCertificateSchemaFile = new StreamSource(new ClassPathResource(
                    "/schemas/v3/RegisterMedicalCertificateInteraction/RegisterMedicalCertificate_3.0.xsd").getFile());

            Source utlatandeSchemaFile = new StreamSource(new ClassPathResource("/schemas/fk7263_model.xsd").getFile());

            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

            Schema registerMedicalCertificateSchema = schemaFactory
                    .newSchema(new Source[] { registerMedicalCertificateSchemaFile });

            Schema utlatandeSchema = schemaFactory.newSchema(utlatandeSchemaFile);

            registerMedicalCertificateSchemaValidator = registerMedicalCertificateSchema.newValidator();
            utlatandeSchemaValidator = utlatandeSchema.newValidator();

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

        RegisterMedicalCertificate registerMedicalCertificate = null;
        Utlatande utlatande = null;

        if (jaxbObject instanceof RegisterMedicalCertificate) {
            registerMedicalCertificate = (RegisterMedicalCertificate) jaxbObject;
        }
        if (jaxbObject instanceof Utlatande) {
            utlatande = (Utlatande) jaxbObject;
        }

        if (registerMedicalCertificate == null && utlatande == null) {
            LOG.warn("Unsupported XML message: " + transportXml);
            throw new RuntimeException("Unsupported XML message: " + transportXml);
        }

        // perform schema validation
        try {
            if (registerMedicalCertificate != null) {
                validateSchema(registerMedicalCertificateSchemaValidator, transportXml);
            }
            if (utlatande != null) {
                validateSchema(utlatandeSchemaValidator, transportXml);
            }
        } catch (ValidationException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }

        Fk7263Utlatande externalModel = convertTransportJaxbToModel(jaxbObject);
        Fk7263Intyg internalModel = toInternal(externalModel);

        // validate
        List<String> validationErrors = new UtlatandeValidator(internalModel).validate();

        if (validationErrors.isEmpty()) {
            return Response.ok(externalModel).build();
        } else {
            String response = Strings.join(",", validationErrors);
            return Response.status(Response.Status.BAD_REQUEST).entity(response).build();
        }
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
        LOG.debug("Converting " + legacyUtlatande.getClass().getCanonicalName() + " to externalModuleFormat");
        return TransportToExternalFk7263LegacyConverter.convert(legacyUtlatande);
    }

    private Fk7263Utlatande convertToModel(Utlatande genericUtlatande) {
        LOG.debug("Converting " + genericUtlatande.getClass().getCanonicalName() + " to externalModuleFormat");
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

        Fk7263Intyg intyg = new ExternalToInternalConverter(utlatande).convert();

        List<String> validationErrors = new UtlatandeValidator(intyg).validate();

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
     * {@link se.inera.certificate.integration.rest.ModuleRestApi} Jackson will try to satisfy the signature of this
     * method once it's been resolved, so the real contract is actually the json structure.
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
        return String.format("lakarutlatande_%s_%s-%s.pdf",
                intyg.getPatientPersonnummer(),
                intyg.getGiltighet().getFrom().toString(DATE_FORMAT),
                intyg.getGiltighet().getTom().toString(DATE_FORMAT));
    }
}
