/**
 * Copyright (C) 2013 Inera AB (http://www.inera.se)
 *
 * This file is part of Inera Certificate Modules (http://code.google.com/p/inera-certificate-modules).
 *
 * Inera Certificate Modules is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Inera Certificate Modules is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.inera.certificate.modules.rli.rest;

import java.io.IOException;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.ServiceUnavailableException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.xml.bind.JAXB;

import junit.framework.Assert;

import org.apache.cxf.jaxrs.client.WebClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import se.inera.certificate.common.v1.Utlatande;
import se.inera.certificate.integration.json.CustomObjectMapper;
import se.inera.certificate.integration.rest.dto.CertificateContentMeta;
import se.inera.certificate.modules.rli.rest.dto.CertificateContentHolder;

/**
 * Sets up an actual HTTP server and client to test the {@link RliModuleApi} service. This is the place to verify that
 * response headers and response statuses etc are correct.
 */
@ContextConfiguration(locations = ("/rli-test-config.xml"))
@RunWith(SpringJUnit4ClassRunner.class)
public class RliModuleApiTest {

    /** An HTTP client proxy wired to the test HTTP server. */
    @Autowired
    private RliModuleApi rliModule;

    @Autowired
    private CustomObjectMapper objectMapper;

    @Test
    public void testUnmarshallScenarios() throws Exception {
        /* Sjuk scenarios */
        {
            rliModule.unmarshall(getXmlResource("scenarios/transport/sjuk-1.xml"));
            assertResponseStatus(Status.OK);
        }
        {
            rliModule.unmarshall(getXmlResource("scenarios/transport/sjuk-2.xml"));
            assertResponseStatus(Status.OK);
        }
        {
            rliModule.unmarshall(getXmlResource("scenarios/transport/sjuk-3.xml"));
            assertResponseStatus(Status.OK);
        }
        {
            rliModule.unmarshall(getXmlResource("scenarios/transport/sjuk-4.xml"));
            assertResponseStatus(Status.OK);
        }

        /* Gravid scenarios */
        {
            rliModule.unmarshall(getXmlResource("scenarios/transport/gravid-1.xml"));
            assertResponseStatus(Status.OK);
        }
        {
            rliModule.unmarshall(getXmlResource("scenarios/transport/gravid-2.xml"));
            assertResponseStatus(Status.OK);
        }
        {
            rliModule.unmarshall(getXmlResource("scenarios/transport/gravid-3.xml"));
            assertResponseStatus(Status.OK);
        }
        {
            rliModule.unmarshall(getXmlResource("scenarios/transport/gravid-4.xml"));
            assertResponseStatus(Status.OK);
        }
    }

    @Test(expected = BadRequestException.class)
    public void testUnmarshallBroken() throws Exception {
        rliModule.unmarshall(getXmlResource("scenarios/sjuk-broken.xml"));
    }

    @Test
    public void testMarshall() throws Exception {
        rliModule.marshall(getJsonResource("rli-example-1.json"));

        assertResponseStatus(Status.OK);
    }

    @Test
    public void testValidate() throws Exception {
        rliModule.validate(getJsonResource("rli-example-1.json"));

        assertResponseStatus(Status.NO_CONTENT);
    }

    @Test
    public void testValidateWithErrors() throws Exception {
        try {
            rliModule.validate(getJsonResource("rli-example-1-with-errors.json"));
            Assert.fail("Expected BadRequestException");

        } catch (BadRequestException e) {
            Assert.assertFalse(e.getResponse().getEntity().toString().isEmpty());
        }
    }

    @Test
    public void testPdf() throws Exception {
        CertificateContentHolder holder = new CertificateContentHolder();
        holder.setCertificateContent(getJsonResource("rli-example-1.json"));
        holder.setCertificateContentMeta(new CertificateContentMeta());

        rliModule.pdf(holder);

        assertResponseStatus(Status.OK);
        assertResponseHeader("filename=lakarutlatande_19121212-1212_2013-01-01-130812.pdf", "Content-Disposition");
    }

    @Test
    public void testConvertExternalToInternal() throws Exception {
        CertificateContentHolder holder = new CertificateContentHolder();
        holder.setCertificateContent(getJsonResource("rli-example-1.json"));
        holder.setCertificateContentMeta(new CertificateContentMeta());
        rliModule.convertExternalToInternal(holder);

        assertResponseStatus(Status.OK);
    }

    @Test(expected = ServiceUnavailableException.class)
    public void testConvertInternalToExternal() throws Exception {
        // TODO: Change test when service is implemented
        rliModule.convertInternalToExternal(new se.inera.certificate.modules.rli.model.internal.mi.Utlatande());
    }

    @Test
    public void testRegisterCertificateRoudtrip() throws Exception {
        se.inera.certificate.modules.rli.model.external.Utlatande utlatande;
        try {
            /* Sjuk scenarios */
            {
                utlatande = rliModule.unmarshall(getXmlResource("scenarios/transport/sjuk-1.xml"));
                rliModule.validate(utlatande);
            }
            {
                utlatande = rliModule.unmarshall(getXmlResource("scenarios/transport/sjuk-2.xml"));
                rliModule.validate(utlatande);
            }
            {
                utlatande = rliModule.unmarshall(getXmlResource("scenarios/transport/sjuk-3.xml"));
                rliModule.validate(utlatande);
            }
            {
                utlatande = rliModule.unmarshall(getXmlResource("scenarios/transport/sjuk-4.xml"));
                rliModule.validate(utlatande);
            }

            /* Gravid scenarios */
            {
                utlatande = rliModule.unmarshall(getXmlResource("scenarios/transport/gravid-1.xml"));
                rliModule.validate(utlatande);
            }
            {
                utlatande = rliModule.unmarshall(getXmlResource("scenarios/transport/gravid-2.xml"));
                rliModule.validate(utlatande);
            }
            {
                utlatande = rliModule.unmarshall(getXmlResource("scenarios/transport/gravid-3.xml"));
                rliModule.validate(utlatande);
            }
            {
                utlatande = rliModule.unmarshall(getXmlResource("scenarios/transport/gravid-4.xml"));
                rliModule.validate(utlatande);
            }
        } catch (WebApplicationException e) {
            e.printStackTrace();
            System.out.println("**** " + e.getResponse().getEntity());
            Assert.fail();
        }
    }

    private void assertResponseStatus(Status status) {
        Assert.assertEquals(status.getStatusCode(), getClientResponse().getStatus());
    }

    private void assertResponseHeader(String expectedValue, String headerName) {
        Assert.assertEquals(expectedValue, getClientResponse().getHeaderString(headerName));
    }

    private Response getClientResponse() {
        return WebClient.client(rliModule).getResponse();
    }

    private Utlatande getXmlResource(String resource) throws IOException {
        return JAXB.unmarshal(new ClassPathResource(resource).getFile(), Utlatande.class);
    }

    private se.inera.certificate.modules.rli.model.external.Utlatande getJsonResource(String resource)
            throws IOException {
        return objectMapper.readValue(new ClassPathResource(resource).getFile(),
                se.inera.certificate.modules.rli.model.external.Utlatande.class);
    }
}
