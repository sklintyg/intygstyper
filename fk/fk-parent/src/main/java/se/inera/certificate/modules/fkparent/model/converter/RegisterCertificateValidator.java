package se.inera.certificate.modules.fkparent.model.converter;

/* TODO: this class should be moved to test directory and put in a test-jar. */

import java.io.ByteArrayInputStream;

import javax.annotation.Nonnull;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;

import com.google.common.base.Charsets;
import se.inera.intyg.common.support.xml.SchemaValidatorBuilder;

public class RegisterCertificateValidator {
    private static final String RESPONDER_SCHEMA = "interactions/RegisterCertificateInteraction/RegisterCertificateResponder_2.0.xsd";
    private static final String GENERAL_SCHEMA = "core_components/clinicalprocess_healthcond_certificate_2.0.xsd";
    private static final String TYPES_SCHEMA = "core_components/clinicalprocess_healthcond_certificate_types_2.0.xsd";

    private Schema generalSchema;

    public void initGeneralSchema() throws Exception {
        SchemaValidatorBuilder schemaValidatorBuilder = new SchemaValidatorBuilder();
        schemaValidatorBuilder.registerResource(TYPES_SCHEMA);
        schemaValidatorBuilder.registerResource(GENERAL_SCHEMA);
        Source rootSource = schemaValidatorBuilder.registerResource(RESPONDER_SCHEMA);
        generalSchema = schemaValidatorBuilder.build(rootSource);
    }

    public boolean validateGeneral(@Nonnull final String xmlContent) throws Exception {
        initGeneralSchema();
        StreamSource xmlSource = new StreamSource(new ByteArrayInputStream(xmlContent.getBytes(Charsets.UTF_8)));
        try {
            generalSchema.newValidator().validate(xmlSource);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

}
