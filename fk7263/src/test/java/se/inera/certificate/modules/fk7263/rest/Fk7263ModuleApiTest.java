package se.inera.certificate.modules.fk7263.rest;

import javax.ws.rs.core.Response;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

/**
 * @author andreaskaltenbach
 */
public class Fk7263ModuleApiTest {

    private Fk7263ModuleApi fk7263ModuleApi = new Fk7263ModuleApi();

    @Test
    public void testSchemaValidationDuringUnmarshall() throws IOException {

        String registerMedicalCertificate = FileUtils.readFileToString(new ClassPathResource("Fk7263ModuleApiTest/registerMedicalCertificate_invalid.xml").getFile());
        Response response = fk7263ModuleApi.unmarshall(registerMedicalCertificate);

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        assertEquals("cvc-complex-type.2.4.b: Innehållet i elementet 'lakarutlatande' är inte fullständigt. Något av '{\"urn:riv:insuranceprocess:healthreporting:mu7263:3\":lakarutlatande-id}' förväntas.", response.getEntity());


        String utlatande = FileUtils.readFileToString(new ClassPathResource("Fk7263ModuleApiTest/utlatande_invalid.xml").getFile());
        response = fk7263ModuleApi.unmarshall(utlatande);

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        assertEquals("cvc-complex-type.2.4.b: Innehållet i elementet 'utlatande' är inte fullständigt. Något av '{\"urn:intyg:common-model:1\":utlatande-id}' förväntas.", response.getEntity());
    }

}
