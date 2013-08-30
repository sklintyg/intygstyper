package se.inera.certificate.modules.fk7263.rest;

import javax.ws.rs.core.Response;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import se.inera.certificate.fk7263.model.v1.Utlatande;
import se.inera.certificate.integration.json.CustomObjectMapper;
import se.inera.certificate.modules.fk7263.model.external.Fk7263Utlatande;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificate.v3.RegisterMedicalCertificate;

/**
 * @author andreaskaltenbach
 */
public class Fk7263ModuleApiTest {

    private Fk7263ModuleApi fk7263ModuleApi = new Fk7263ModuleApi();

    @Test
    public void testSchemaValidationDuringUnmarshall() throws IOException {

        String registerMedicalCertificate = FileUtils.readFileToString(new ClassPathResource(
                "Fk7263ModuleApiTest/registerMedicalCertificate_invalid.xml").getFile());
        Response response = fk7263ModuleApi.unmarshall(registerMedicalCertificate);

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        assertTrue(response.getEntity().toString().startsWith("cvc-complex-type.2.4.b:"));

        String utlatande = FileUtils
                .readFileToString(new ClassPathResource("Fk7263ModuleApiTest/utlatande_invalid.xml").getFile());
        response = fk7263ModuleApi.unmarshall(utlatande);

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        assertTrue(response.getEntity().toString().startsWith("cvc-complex-type.2.4.b:"));
    }

    @Test
    public void testMarshallWithVersion_1_0() throws IOException {
        Fk7263Utlatande utlatande = new CustomObjectMapper().readValue(new ClassPathResource(
                "Fk7263ModuleApiTest/utlatande.json").getFile(), Fk7263Utlatande.class);

        Response response = fk7263ModuleApi.marshall("1.0", utlatande);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        assertEquals(RegisterMedicalCertificate.class, response.getEntity().getClass());
    }

    @Test
    public void testMarshallWithVersion_2_0() throws IOException {
        Fk7263Utlatande utlatande = new CustomObjectMapper().readValue(new ClassPathResource(
                "Fk7263ModuleApiTest/utlatande.json").getFile(), Fk7263Utlatande.class);

        Response response = fk7263ModuleApi.marshall("2.0", utlatande);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        assertEquals(Utlatande.class, response.getEntity().getClass());
    }

    @Test
    public void testMarshallWithInvalidVersion() throws IOException {
        Fk7263Utlatande utlatande = new CustomObjectMapper().readValue(new ClassPathResource(
                "Fk7263ModuleApiTest/utlatande.json").getFile(), Fk7263Utlatande.class);

        Response response = fk7263ModuleApi.marshall(null, utlatande);
        assertEquals(Response.Status.NOT_IMPLEMENTED.getStatusCode(), response.getStatus());
    }
}
