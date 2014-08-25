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

import static org.junit.Assert.assertNotNull;
import static ${package}.support.api.dto.TransportModelVersion.UTLATANDE_V1;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import se.inera.certificate.modules.support.api.dto.ExternalModelHolder;
import se.inera.certificate.modules.support.api.dto.InternalModelResponse;
import se.inera.certificate.modules.${artifactId-safe}.model.internal.Utlatande;
import se.inera.certificate.modules.${artifactId-safe}.utils.ResourceConverterUtils;
import se.inera.certificate.modules.${artifactId-safe}.utils.Scenario;
import se.inera.certificate.modules.${artifactId-safe}.utils.ScenarioFinder;
import ${package}.support.ApplicationOrigin;
import ${package}.support.api.ModuleApi;
import ${package}.support.api.dto.CreateNewDraftHolder;
import ${package}.support.api.dto.ExternalModelResponse;
import ${package}.support.api.dto.HoSPersonal;
import ${package}.support.api.dto.InternalModelHolder;
import ${package}.support.api.dto.Patient;
import ${package}.support.api.dto.TransportModelHolder;
import ${package}.support.api.dto.Vardenhet;
import ${package}.support.api.dto.Vardgivare;
import ${package}.support.api.exception.ModuleException;
import ${package}.support.api.exception.ModuleValidationException;
import ${package}.${artifactId-safe}.utils.ModelAssert;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Sets up an actual HTTP server and client to test the {@link ModuleApi} service. This is the place to verify that
 * response headers and response statuses etc are correct.
 */
@ContextConfiguration(locations = ("/${artifactId}-test-config.xml"))
@RunWith(SpringJUnit4ClassRunner.class)
public class ModuleApiTest {

    /** An HTTP client proxy wired to the test HTTP server. */
    @Autowired
    private ${package}.support.api.ModuleApi moduleApi;

    @Autowired
    private JAXBContext jaxbContext;

    @Autowired
    private ObjectMapper mapper;

    @Test
    public void testUnmarshallScenarios() throws Exception {
        for (Scenario scenario : ScenarioFinder.getTransportScenarios("valid-*")) {
            moduleApi.unmarshall(createTransportHolder(scenario.asTransportModel()));
        }
    }

    @Test
    public void testUnmarshallBroken() throws Exception {
        for (Scenario scenario : ScenarioFinder.getTransportScenarios("invalid-*")) {
            try {
                moduleApi.unmarshall(createTransportHolder(scenario.asTransportModel()));
            } catch (ModuleValidationException ignore) {
            }
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
            moduleApi.pdf(createExternalHolder(scenario.asExternalModel()), ApplicationOrigin.MINA_INTYG);

            // String contentDisposition = getClientResponse().getHeaderString("Content-Disposition");
            // Assert.assertTrue("Error in scenario " + scenario.getName(),
            // contentDisposition.startsWith("filename=lakarutlatande"));
        }
    }

    @Test
    public void testConvertExternalToInternal() throws Exception {
        for (Scenario scenario : ScenarioFinder.getExternalScenarios("valid-*")) {
            ${package}.${artifactId-safe}.model.external.Utlatande externalModel = scenario.asExternalModel();
            moduleApi.convertExternalToInternal(createExternalHolder(externalModel));
        }
    }

    @Test
    public void testConvertInternalToExternal() throws Exception {
        for (Scenario scenario : ScenarioFinder.getInternalScenarios("valid-*")) {

            ExternalModelResponse externalModelReponse = moduleApi
                    .convertInternalToExternal(createInternalHolder(scenario.asInternalModel()));
            ${package}.${artifactId-safe}.model.external.Utlatande actual = mapper.readValue(
                    externalModelReponse.getExternalModelJson(),
                    ${package}.${artifactId-safe}.model.external.Utlatande.class);

            ${package}.${artifactId-safe}.model.external.Utlatande expected = scenario.asExternalModel();

            ModelAssert.assertEquals("Error in scenario " + scenario.getName(), expected, actual);
        }
    }

    @Test
    public void testRegisterCertificateRoundtrip() throws Exception {
        ${package}.${artifactId-safe}.model.external.Utlatande extUtlatande;
        Utlatande intUtlatande;
        for (Scenario scenario : ScenarioFinder.getTransportScenarios("valid-*")) {
            extUtlatande = (${package}.${artifactId-safe}.model.external.Utlatande) moduleApi.unmarshall(
                    createTransportHolder(scenario.asTransportModel())).getExternalModel();
            moduleApi.validate(createExternalHolder(extUtlatande));
            String intUtlatandeString = moduleApi.convertExternalToInternal(createExternalHolder(extUtlatande))
                    .getInternalModel();
            intUtlatande = mapper.readValue(intUtlatandeString, Utlatande.class);

            Utlatande expected = scenario.asInternalModel();

            ModelAssert.assertEquals("Error in scenario " + scenario.getName(), expected, intUtlatande);
        }
    }

    @Test
    public void copyCreatesBlank() throws Exception {
        for (Scenario scenario : ScenarioFinder.getExternalScenarios("valid-*")) {
            ExternalModelHolder internalHolder = createExternalHolder(scenario.asExternalModel());

            InternalModelResponse holder = moduleApi.createNewInternalFromTemplate(createNewDraftHolder(), internalHolder);

            assertNotNull(holder);
            Utlatande utlatande = ResourceConverterUtils.toInternal(holder.getInternalModel());
            assertNotNull(utlatande.getTyp());
        }
    }

    @Test
    public void createNewInternal() throws ModuleException {
        CreateNewDraftHolder holder = createNewDraftHolder();

        InternalModelResponse response = moduleApi.createNewInternal(holder);

        assertNotNull(response.getInternalModel());
    }

    private CreateNewDraftHolder createNewDraftHolder() {
        Vardgivare vardgivare = new Vardgivare("hsaId0", "vardgivare");
        Vardenhet vardenhet = new Vardenhet("hsaId1", "namn", null, null, null, null, null, null, vardgivare);
        HoSPersonal hosPersonal = new HoSPersonal("Id1", "Grodan Boll", "forskrivarkod", "befattning", null, vardenhet);
        Patient patient = new Patient("Kalle",null,"Kula","19121212-1212",null,null,null);
        return new CreateNewDraftHolder("Id1", hosPersonal, patient);
    }

    private TransportModelHolder createTransportHolder(se.inera.certificate.${artifactId-safe}.model.v1.Utlatande transportModel)
            throws JAXBException {
        StringWriter writer = new StringWriter();
        jaxbContext.createMarshaller().marshal(transportModel, writer);
        return new TransportModelHolder(writer.toString());
    }

    private ExternalModelHolder createExternalHolder(
            ${package}.${artifactId-safe}.model.external.Utlatande externalModel) throws JsonProcessingException {
        return new ExternalModelHolder(mapper.writeValueAsString(externalModel));
    }

    private InternalModelHolder createInternalHolder(Utlatande internalModel) throws JsonProcessingException {
        return new InternalModelHolder(mapper.writeValueAsString(internalModel));
    }
}
