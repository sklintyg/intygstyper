package se.inera.certificate.modules.fk7263.rest;

import java.io.IOException;
import java.io.StringReader;

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
import se.inera.certificate.validate.ValidationException;

/**
 * Helper methods to unmarshall and validate transport model XML.
 */
public class TransportXmlUtils {

    private static final Logger LOG = LoggerFactory.getLogger(TransportXmlUtils.class);

    private static JAXBContext jaxbContext;

    private static Schema utlatandeSchema;

    // Create JAXB context for the transport format(s) this module can handle
    static {
        try {
            jaxbContext = JAXBContext.newInstance(Utlatande.class, RegisterMedicalCertificate.class);
        } catch (JAXBException e) {
            throw new RuntimeException("Failed to create JAXB context", e);
        }
    }

    // Create schema for validation
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
     * Unmarshal xml string into jaxb. Supports both {@link Utlatande} and legacy {@link Lakarutlatande}.
     * 
     * @param transportXml
     *            the xml as a string.
     * @return jaxbObject if unmarshalling was successful
     */
    public static Object unmarshallTransportXML(String transportXml) {
        try {
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            return unmarshaller.unmarshal(new StringReader(transportXml));

        } catch (JAXBException e) {
            LOG.error("Failed to unmarshal transportXml", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Validates the XML of a {@link Utlatande}.
     * 
     * @param utlatandeXml
     *            The xml as a string.
     */
    public static void validateSchema(String utlatandeXml) {
        try {
            Validator validator = utlatandeSchema.newValidator();
            validator.validate(new StreamSource(new StringReader(utlatandeXml)));

        } catch (SAXException e) {
            throw new ValidationException(e.getMessage());

        } catch (IOException e) {
            LOG.error("Failed to validate message against schema", e);
            throw new RuntimeException("Failed to validate message against schema", e);
        }
    }
}
