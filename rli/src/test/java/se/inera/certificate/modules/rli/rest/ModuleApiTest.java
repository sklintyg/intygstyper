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

import static org.unitils.reflectionassert.ReflectionAssert.assertLenientEquals;
import static se.inera.certificate.modules.support.api.dto.TransportModelVersion.UTLATANDE_V1;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import se.inera.certificate.integration.json.CustomObjectMapper;
import se.inera.certificate.modules.rli.model.internal.mi.Utlatande;
import se.inera.certificate.modules.rli.utils.Scenario;
import se.inera.certificate.modules.rli.utils.ScenarioFinder;
import se.inera.certificate.modules.support.api.ModuleApi;
import se.inera.certificate.modules.support.api.dto.ExternalModelHolder;
import se.inera.certificate.modules.support.api.dto.InternalModelHolder;
import se.inera.certificate.modules.support.api.dto.TransportModelHolder;
import se.inera.certificate.modules.support.api.exception.ModuleException;
import se.inera.certificate.modules.support.api.exception.ModuleValidationException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Sets up an actual HTTP server and client to test the {@link ModuleApi} service. This is the place to verify that
 * response headers and response statuses etc are correct.
 */
@ContextConfiguration(locations = ("/rli-test-config.xml"))
@RunWith(SpringJUnit4ClassRunner.class)
public class ModuleApiTest {

    @Autowired
    private se.inera.certificate.modules.support.api.ModuleApi moduleApi;

    @Autowired
    private JAXBContext jaxbContext;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private CustomObjectMapper objectMapper;

    @Test
    public void testUnmarshallScenarios() throws Exception {
        for (Scenario scenario : ScenarioFinder.getTransportScenarios("valid-*")) {
            moduleApi.unmarshall(createTransportHolder(scenario.asTransportModel()));
        }
    }

    @Test(expected = ModuleException.class)
    public void testUnmarshallBroken() throws Exception {
        for (Scenario scenario : ScenarioFinder.getTransportScenarios("invalid-*")) {
            moduleApi.unmarshall(createTransportHolder(scenario.asTransportModel()));
        }
    }

    @Test
    public void testMarshall() throws Exception {
        for (Scenario scenario : ScenarioFinder.getExternalScenarios("valid-*")) {
            moduleApi.marshall(createExternalHolder(scenario.asExternalModel()), UTLATANDE_V1);
        }

    }

    @Test
    public void testValidate() throws Exception {
        for (Scenario scenario : ScenarioFinder.getExternalScenarios("valid-*")) {
            moduleApi.validate(createExternalHolder(scenario.asExternalModel()));
        }
    }

    @Test
    public void testValidateWithErrors() throws Exception {
        for (Scenario scenario : ScenarioFinder.getExternalScenarios("invalid-*")) {
            try {
                moduleApi.validate(createExternalHolder(scenario.asExternalModel()));
                Assert.fail("Expected ModuleValidationException, running scenario " + scenario.getName());

            } catch (ModuleValidationException e) {
                Assert.assertFalse("Error in scenario " + scenario.getName(), e.getValidationEntries().isEmpty());
            }
        }
    }

    @Test
    public void testPdf() throws Exception {
        for (Scenario scenario : ScenarioFinder.getExternalScenarios("valid-*")) {
            ExternalModelHolder holder = createExternalHolder(scenario.asExternalModel());

            moduleApi.pdf(holder);

        }
    }

    @Test
    public void testConvertExternalToInternal() throws Exception {
        for (Scenario scenario : ScenarioFinder.getExternalScenarios("valid-*")) {
            moduleApi.convertExternalToInternal(createExternalHolder(scenario.asExternalModel()));
        }
    }

    @Test
    public void testConvertInternalToExternal() throws Exception {
        for (Scenario scenario : ScenarioFinder.getInternalWCScenarios("valid-*")) {
            moduleApi.convertInternalToExternal(createInternalWcHolder(scenario.asInternalWCModel()));
        }
    }

    @Test
    public void testRegisterCertificateRoudtrip() throws Exception {
        se.inera.certificate.modules.rli.model.external.Utlatande extUtlatande;
        Utlatande intUtlatande;
        for (Scenario scenario : ScenarioFinder.getTransportScenarios("valid-*")) {
            extUtlatande = (se.inera.certificate.modules.rli.model.external.Utlatande) moduleApi.unmarshall(
                    createTransportHolder(scenario.asTransportModel())).getExternalModel();

            moduleApi.validate(createExternalHolder(extUtlatande));
            String intUtlatandeString = moduleApi.convertExternalToInternal(createExternalHolder(extUtlatande))
                    .getInternalModel();
            intUtlatande = mapper.readValue(intUtlatandeString, Utlatande.class);

            Utlatande expected = scenario.asInternalMIModel();
            assertLenientEquals("Error in scenario " + scenario.getName(), expected, intUtlatande);
        }
    }

    private TransportModelHolder createTransportHolder(se.inera.certificate.rli.model.v1.Utlatande transportModel)
            throws JAXBException {
        StringWriter writer = new StringWriter();
        jaxbContext.createMarshaller().marshal(transportModel, writer);
        return new TransportModelHolder(writer.toString());
    }

    private ExternalModelHolder createExternalHolder(
            se.inera.certificate.modules.rli.model.external.Utlatande externalModel) throws JsonProcessingException {
        return new ExternalModelHolder(mapper.writeValueAsString(externalModel));
    }

    private InternalModelHolder createInternalWcHolder(
            se.inera.certificate.modules.rli.model.internal.wc.Utlatande internalModel) throws JsonProcessingException {
        return new InternalModelHolder(mapper.writeValueAsString(internalModel));
    }

}
