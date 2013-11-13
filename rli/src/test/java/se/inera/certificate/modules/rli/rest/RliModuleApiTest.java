package se.inera.certificate.modules.rli.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import javax.ws.rs.core.Response;
import javax.xml.bind.JAXB;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import se.inera.certificate.common.v1.Utlatande;
import se.inera.certificate.model.HosPersonal;
import se.inera.certificate.modules.rli.rest.dto.CreateNewDraftCertificateHolder;

@ContextConfiguration(locations = ("/rli-test-config.xml"))
@RunWith(SpringJUnit4ClassRunner.class)
public class RliModuleApiTest {

    @Autowired
    RliModuleApi rliModuleApi;

    @Test
    public void unmarshallTestBroken() throws IOException {
        Utlatande utlatande = unmarshallXml("sjuk-broken.xml");
        Response response = rliModuleApi.unmarshall(utlatande);
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }

    /* Sjuk scenarios */
    @Test
    public void unmarshallSjukScenario1() throws IOException {
        Utlatande utlatande = unmarshallXml("sjuk-1.xml");
        Response response = rliModuleApi.unmarshall(utlatande);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        /* Test breaking utlatande by setting Patient to null */
        utlatande.setPatient(null);
        response = rliModuleApi.unmarshall(utlatande);
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }

    @Test
    public void unmarshallSjukScenario2() throws IOException {
        Utlatande utlatande = unmarshallXml("sjuk-2.xml");
        Response response = rliModuleApi.unmarshall(utlatande);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    public void unmarshallSjukScenario3() throws IOException {
        Utlatande utlatande = unmarshallXml("sjuk-3.xml");
        Response response = rliModuleApi.unmarshall(utlatande);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    public void unmarshallSjukScenario4() throws IOException {
        Utlatande utlatande = unmarshallXml("sjuk-4.xml");
        Response response = rliModuleApi.unmarshall(utlatande);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    /* Gravid scenarios */

    @Test
    public void unmarshallGravidScenario1() throws IOException {
        Utlatande utlatande = unmarshallXml("gravid-1.xml");
        Response response = rliModuleApi.unmarshall(utlatande);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    public void unmarshallGravidScenario2() throws IOException {
        Utlatande utlatande = unmarshallXml("gravid-2.xml");
        Response response = rliModuleApi.unmarshall(utlatande);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    public void unmarshallGravidScenario3() throws IOException {
        Utlatande utlatande = unmarshallXml("gravid-3.xml");
        Response response = rliModuleApi.unmarshall(utlatande);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    public void unmarshallGravidScenario4() throws IOException {
        Utlatande utlatande = unmarshallXml("gravid-4.xml");
        Response response = rliModuleApi.unmarshall(utlatande);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    private se.inera.certificate.common.v1.Utlatande unmarshallXml(String resource) throws IOException {
        return JAXB.unmarshal(new ClassPathResource("scenarios/" + resource).getFile(), Utlatande.class);
    }

}
