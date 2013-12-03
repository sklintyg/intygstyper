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

import javax.ws.rs.BadRequestException;
import javax.ws.rs.ServiceUnavailableException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import junit.framework.Assert;

import org.apache.cxf.jaxrs.client.WebClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import se.inera.certificate.integration.json.CustomObjectMapper;
import se.inera.certificate.integration.rest.dto.CertificateContentMeta;
import se.inera.certificate.modules.rli.rest.dto.CertificateContentHolder;
import se.inera.certificate.modules.rli.utils.Scenario;
import se.inera.certificate.modules.rli.utils.ScenarioCreator;

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
        for (Scenario scenario : ScenarioCreator.getTransportScenarios("sjuk-?")) {
            rliModule.unmarshall(scenario.asTransportModel());
            assertResponseStatus(Status.OK);
        }
        /* Gravid scenarios */
        for (Scenario scenario : ScenarioCreator.getTransportScenarios("gravid-?")) {
            rliModule.unmarshall(scenario.asTransportModel());
            assertResponseStatus(Status.OK);
        }
    }

    @Test(expected = BadRequestException.class)
    public void testUnmarshallBroken() throws Exception {
        for (Scenario scenario : ScenarioCreator.getTransportScenarios("*-broken")) {
            rliModule.unmarshall(scenario.asTransportModel());
        }
    }

    @Test
    public void testMarshall() throws Exception {
        rliModule.marshall(ScenarioCreator.getExternalScenario("sjuk-1").asExternalModel());
        assertResponseStatus(Status.OK);
    }

    @Test
    public void testValidate() throws Exception {
        rliModule.validate(ScenarioCreator.getExternalScenario("sjuk-1").asExternalModel());
        assertResponseStatus(Status.NO_CONTENT);
    }

    @Test
    public void testValidateWithErrors() throws Exception {
        try {
            rliModule.validate(ScenarioCreator.getExternalScenario("sjuk-broken").asExternalModel());
            Assert.fail("Expected BadRequestException");

        } catch (BadRequestException e) {
            Assert.assertFalse(e.getResponse().getEntity().toString().isEmpty());
        }
    }

    @Test
    public void testPdf() throws Exception {
        CertificateContentHolder holder = new CertificateContentHolder();
        holder.setCertificateContent(ScenarioCreator.getExternalScenario("sjuk-1").asExternalModel());
        holder.setCertificateContentMeta(new CertificateContentMeta());

        rliModule.pdf(holder);

        assertResponseStatus(Status.OK);
        assertResponseHeader("filename=lakarutlatande_19331122-4400_2013-01-01-130812.pdf", "Content-Disposition");
    }

    @Test
    public void testConvertExternalToInternal() throws Exception {
        CertificateContentHolder holder = new CertificateContentHolder();
        holder.setCertificateContent(ScenarioCreator.getExternalScenario("sjuk-1").asExternalModel());
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
        /* Sjuk scenarios */
        for (Scenario scenario : ScenarioCreator.getTransportScenarios("sjuk-?")) {
            utlatande = rliModule.unmarshall(scenario.asTransportModel());
            rliModule.validate(utlatande);
        }

        /* Gravid scenarios */
        for (Scenario scenario : ScenarioCreator.getTransportScenarios("gravid-?")) {
            utlatande = rliModule.unmarshall(scenario.asTransportModel());
            rliModule.validate(utlatande);
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
}
