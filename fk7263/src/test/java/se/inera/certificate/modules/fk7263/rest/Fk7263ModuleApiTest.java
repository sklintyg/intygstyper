package se.inera.certificate.modules.fk7263.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import javax.ws.rs.core.Response;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import se.inera.certificate.fk7263.insuranceprocess.healthreporting.registermedicalcertificate.v3.RegisterMedicalCertificate;
import se.inera.certificate.fk7263.model.v1.Utlatande;
import se.inera.certificate.integration.json.CustomObjectMapper;
import se.inera.certificate.modules.fk7263.model.external.Fk7263Utlatande;
import se.inera.certificate.modules.fk7263.model.internal.Fk7263Intyg;

/**
 * @author andreaskaltenbach
 */
public class Fk7263ModuleApiTest {

    private Fk7263ModuleApi fk7263ModuleApi = new Fk7263ModuleApi();

    @Test
    public void testSchemaValidationDuringUnmarshall() throws IOException {
        String utlatande = FileUtils
                .readFileToString(new ClassPathResource("Fk7263ModuleApiTest/utlatande_invalid.xml").getFile());
        Response response = fk7263ModuleApi.unmarshall(utlatande);

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        assertTrue(response.getEntity().toString().contains("Validation failed for intyg"));
        assertTrue(response.getEntity().toString().contains("123456"));
        assertTrue(response.getEntity().toString().contains("cvc-minLength-valid"));
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

    @Test
    public void testPdfFileName() {
        Fk7263Intyg intyg = new Fk7263Intyg();
        intyg.setPatientPersonnummer("19121212-1212");
        //TODO Create a proper test when model has been updated.
        //assertEquals("lakarutlatande_19121212-1212_20110124-20110331.pdf", fk7263ModuleApi.pdfFileName(intyg));
    }
}
