#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
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
package ${package}.${artifactId-safe}.rest;

import static org.unitils.reflectionassert.ReflectionAssert.assertLenientEquals;
import static ${package}.${artifactId-safe}.utils.ResourceConverterUtils.wrapExternalWithHolder;

import javax.ws.rs.BadRequestException;
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
import ${package}.${artifactId-safe}.model.internal.mi.Utlatande;
import ${package}.${artifactId-safe}.rest.dto.CertificateContentHolder;
import ${package}.${artifactId-safe}.utils.Scenario;
import ${package}.${artifactId-safe}.utils.ScenarioFinder;

/**
 * Sets up an actual HTTP server and client to test the {@link ModuleApi} service. This is the place to verify that
 * response headers and response statuses etc are correct.
 */
@ContextConfiguration(locations = ("/${artifactId}-test-config.xml"))
@RunWith(SpringJUnit4ClassRunner.class)
public class ModuleApiTest {

    /** An HTTP client proxy wired to the test HTTP server. */
    @Autowired
    private ModuleApi moduleApi;

    @Autowired
    private CustomObjectMapper objectMapper;

    @Test
    public void testUnmarshallScenarios() throws Exception {
        for (Scenario scenario : ScenarioFinder.getTransportScenarios("valid-*")) {
            moduleApi.unmarshall(scenario.asTransportModel());
            assertResponseStatus("Error in scenario " + scenario.getName(), Status.OK);
        }
    }

    @Test(expected = BadRequestException.class)
    public void testUnmarshallBroken() throws Exception {
        for (Scenario scenario : ScenarioFinder.getTransportScenarios("invalid-*")) {
            moduleApi.unmarshall(scenario.asTransportModel());
        }
    }

    @Test
    public void testMarshall() throws Exception {
        for (Scenario scenario : ScenarioFinder.getExternalScenarios("valid-*")) {
            moduleApi.marshall(scenario.asExternalModel());
            assertResponseStatus("Error in scenario " + scenario.getName(), Status.OK);
        }

    }

    @Test
    public void testValidate() throws Exception {
        for (Scenario scenario : ScenarioFinder.getExternalScenarios("valid-*")) {
            moduleApi.validate(scenario.asExternalModel());
            assertResponseStatus("Error in scenario " + scenario.getName(), Status.NO_CONTENT);
        }
    }

    @Test
    public void testValidateWithErrors() throws Exception {
        for (Scenario scenario : ScenarioFinder.getExternalScenarios("invalid-*")) {
            try {
                moduleApi.validate(scenario.asExternalModel());
                Assert.fail("Expected BadRequestException, running scenario " + scenario.getName());

            } catch (BadRequestException e) {
                Assert.assertFalse("Error in scenario " + scenario.getName(), e.getResponse().getEntity().toString()
                        .isEmpty());
            }
        }
    }

    @Test
    public void testPdf() throws Exception {
        for (Scenario scenario : ScenarioFinder.getExternalScenarios("valid-*")) {
            CertificateContentHolder holder = scenario.asExternalModelWithHolder();

            moduleApi.pdf(holder);

            assertResponseStatus("Error in scenario " + scenario.getName(), Status.OK);
            String contentDisposition = getClientResponse().getHeaderString("Content-Disposition");
            Assert.assertTrue("Error in scenario " + scenario.getName(),
                    contentDisposition.startsWith("filename=lakarutlatande"));
        }
    }

    @Test
    public void testConvertExternalToInternal() throws Exception {
        CertificateContentHolder holder = new CertificateContentHolder();
        for (Scenario scenario : ScenarioFinder.getExternalScenarios("valid-*")) {
            holder.setCertificateContent(scenario.asExternalModel());
            holder.setCertificateContentMeta(new CertificateContentMeta());
            moduleApi.convertExternalToInternal(holder);
            assertResponseStatus("Error in scenario " + scenario.getName(), Status.OK);
        }
    }

    @Test
    public void testConvertInternalToExternal() throws Exception {
        for (Scenario scenario : ScenarioFinder.getInternalWCScenarios("valid-*")) {
            moduleApi.convertInternalToExternal(scenario.asInternalWCModel());
            assertResponseStatus("Error in scenario " + scenario.getName(), Status.OK);
        }
    }

    @Test
    public void testRegisterCertificateRoudtrip() throws Exception {
        ${package}.${artifactId-safe}.model.external.Utlatande extUtlatande;
        Utlatande intUtlatande;
        for (Scenario scenario : ScenarioFinder.getTransportScenarios("valid-*")) {
            extUtlatande = moduleApi.unmarshall(scenario.asTransportModel());
            moduleApi.validate(extUtlatande);
            intUtlatande = moduleApi.convertExternalToInternal(wrapExternalWithHolder(extUtlatande));

            Utlatande expected = scenario.asInternalMIModel();
            assertLenientEquals("Error in scenario " + scenario.getName(), expected, intUtlatande);
        }
    }

    private void assertResponseStatus(String message, Status status) {
        Assert.assertEquals(message, status.getStatusCode(), getClientResponse().getStatus());
    }

    private Response getClientResponse() {
        return WebClient.client(moduleApi).getResponse();
    }
}
