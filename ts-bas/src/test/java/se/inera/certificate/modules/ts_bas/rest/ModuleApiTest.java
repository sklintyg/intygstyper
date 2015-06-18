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
package se.inera.certificate.modules.ts_bas.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.Difference;
import org.custommonkey.xmlunit.DifferenceConstants;
import org.custommonkey.xmlunit.DifferenceListener;
import org.custommonkey.xmlunit.ElementNameAndTextQualifier;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import se.inera.certificate.integration.json.CustomObjectMapper;
import se.inera.certificate.modules.support.ApplicationOrigin;
import se.inera.certificate.modules.support.api.ModuleApi;
import se.inera.certificate.modules.support.api.dto.CertificateResponse;
import se.inera.certificate.modules.support.api.dto.CreateDraftCopyHolder;
import se.inera.certificate.modules.support.api.dto.CreateNewDraftHolder;
import se.inera.certificate.modules.support.api.dto.HoSPersonal;
import se.inera.certificate.modules.support.api.dto.InternalModelHolder;
import se.inera.certificate.modules.support.api.dto.InternalModelResponse;
import se.inera.certificate.modules.support.api.dto.Patient;
import se.inera.certificate.modules.support.api.dto.Vardenhet;
import se.inera.certificate.modules.support.api.dto.Vardgivare;
import se.inera.certificate.modules.support.api.exception.ModuleException;
import se.inera.certificate.modules.ts_bas.model.internal.Utlatande;
import se.inera.certificate.modules.ts_bas.utils.ResourceConverterUtils;
import se.inera.certificate.modules.ts_bas.utils.Scenario;
import se.inera.certificate.modules.ts_bas.utils.ScenarioFinder;
import se.inera.certificate.modules.ts_bas.utils.ScenarioNotFoundException;
import se.inera.intyg.common.schemas.intygstjansten.ts.utils.ResultTypeUtil;
import se.inera.intygstjanster.ts.services.GetTSBasResponder.v1.GetTSBasResponderInterface;
import se.inera.intygstjanster.ts.services.GetTSBasResponder.v1.GetTSBasResponseType;
import se.inera.intygstjanster.ts.services.GetTSBasResponder.v1.GetTSBasType;
import se.inera.intygstjanster.ts.services.RegisterTSBasResponder.v1.RegisterTSBasResponderInterface;
import se.inera.intygstjanster.ts.services.RegisterTSBasResponder.v1.RegisterTSBasResponseType;
import se.inera.intygstjanster.ts.services.RegisterTSBasResponder.v1.RegisterTSBasType;
import se.inera.intygstjanster.ts.services.v1.ErrorIdType;
import se.inera.intygstjanster.ts.services.v1.IntygMeta;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Sets up an actual HTTP server and client to test the {@link ModuleApi} service. This is the place to verify that
 * response headers and response statuses etc are correct.
 */
@ContextConfiguration(locations = ("/ts-bas-test-config.xml"))
@RunWith(SpringJUnit4ClassRunner.class)
public class ModuleApiTest {

    @Autowired
    @Qualifier("tsBasObjectMapper")
    private ObjectMapper mapper;

    private RegisterTSBasResponderInterface registerTSBasResponderInterface;
    private RegisterTSBasResponseType registerResponse;

    private GetTSBasResponderInterface getTSBasResponderInterface;

    /** An HTTP client proxy wired to the test HTTP server. */
    @Autowired
    private TsBasModuleApi moduleApi;


    @Autowired
    private CustomObjectMapper objectMapper;

    @Before
    public void setUpMocks() {
        registerTSBasResponderInterface = Mockito.mock(RegisterTSBasResponderInterface.class);
        getTSBasResponderInterface = Mockito.mock(GetTSBasResponderInterface.class);
        moduleApi.setRegisterTSBasResponderClient(registerTSBasResponderInterface);
        moduleApi.setGetTSBasResponderClient(getTSBasResponderInterface);
        registerResponse = new RegisterTSBasResponseType();
    }

    @Test
    public void testPdf() throws Exception {
        for (Scenario scenario : ScenarioFinder.getInternalScenarios("valid-*")) {
            moduleApi.pdf(createInternalHolder(scenario.asInternalModel()), null, ApplicationOrigin.MINA_INTYG, false);
        }
    }

    @Test
    public void copyContainsOriginalData() throws Exception {
        Scenario scenario = ScenarioFinder.getInternalScenario("valid-maximal");
        InternalModelHolder internalHolder = createInternalHolder(scenario.asInternalModel());

        InternalModelResponse holder = moduleApi.createNewInternalFromTemplate(createNewDraftCopyHolder(), internalHolder);

        assertNotNull(holder);
        Utlatande utlatande = ResourceConverterUtils.toInternal(holder.getInternalModel());
        assertEquals(true, utlatande.getSyn().getSynfaltsdefekter());
    }

    @Test
    public void createNewInternal() throws ModuleException {
        CreateNewDraftHolder holder = createNewDraftHolder();

        InternalModelResponse response = moduleApi.createNewInternal(holder);

        assertNotNull(response.getInternalModel());
    }

    @Test
    public void testRegisterCertificate() throws JsonProcessingException, ScenarioNotFoundException {
        registerResponse.setResultat(ResultTypeUtil.okResult());
        Mockito.when(registerTSBasResponderInterface.registerTSBas(Mockito.eq("OK"), Mockito.any(RegisterTSBasType.class)))
                .thenReturn(registerResponse);

        String logicalAddress = "OK";
        List<String> failResults = new ArrayList<>();
        for (Scenario scenario : ScenarioFinder.getInternalScenarios("valid-*")) {
            InternalModelHolder internalModel = createInternalHolder(scenario.asInternalModel());
            try {
                moduleApi.registerCertificate(internalModel, logicalAddress);
            } catch (ModuleException me) {
                failResults.add(me.getMessage());
            }
        }
        assertTrue(failResults.isEmpty());
    }

    @Test
    public void testRegisterCertificateFailed() throws JsonProcessingException, ScenarioNotFoundException {
        registerResponse.setResultat(ResultTypeUtil.errorResult(ErrorIdType.APPLICATION_ERROR, "failed"));
        Mockito.when(registerTSBasResponderInterface.registerTSBas(Mockito.eq("FAIL"), Mockito.any(RegisterTSBasType.class)))
                .thenReturn(registerResponse);

        String logicalAddress = "FAIL";
        String failResult = "";
        Scenario scenario = ScenarioFinder.getInternalScenario("invalid-missing-identitet");
        InternalModelHolder internalModel = createInternalHolder(scenario.asInternalModel());
        try {
            moduleApi.registerCertificate(internalModel, logicalAddress);
        } catch (ModuleException me) {
            failResult = me.getMessage();
        }
        assertTrue(!failResult.isEmpty());
    }

    @Test
    public void testGetCertificate() throws ModuleException, ScenarioNotFoundException {
        GetTSBasResponseType result = new GetTSBasResponseType();
        result.setIntyg(ScenarioFinder.getTransportScenario("valid-maximal").asTransportModel().getIntyg());
        result.setMeta(createMeta());
        result.setResultat(ResultTypeUtil.okResult());
        Mockito.when(getTSBasResponderInterface.getTSBas(Mockito.eq("TS"), Mockito.any(GetTSBasType.class)))
            .thenReturn(result);

        CertificateResponse internal = moduleApi.getCertificate("cert-id", "TS");
        assertNotNull(internal);
    }

    @Test
    public void testMarshall() throws ScenarioNotFoundException, ModuleException, IOException, SAXException {
        Utlatande internal = ScenarioFinder.getInternalScenario("valid-maximal").asInternalModel();
        StringWriter writer = new StringWriter();
        try {
            objectMapper.writeValue(writer, internal);
        } catch (IOException e) {
            throw new ModuleException("Failed to serialize internal model", e);
        }
        String xml = writer.toString();
        String actual = moduleApi.marshall(xml);
        String expected = FileUtils.readFileToString(new ClassPathResource("scenarios/transport/valid-maximal.xml").getFile());

        XMLUnit.setIgnoreWhitespace(true);
        Diff diff = XMLUnit.compareXML(expected, actual);
        diff.overrideDifferenceListener(new NamespacePrefixNameIgnoringListener());
        diff.overrideElementQualifier(new ElementNameAndTextQualifier());
        Assert.assertTrue(diff.toString(), diff.similar());
    }

    private class NamespacePrefixNameIgnoringListener implements DifferenceListener {
        public int differenceFound(Difference difference) {
            if (DifferenceConstants.NAMESPACE_PREFIX_ID == difference.getId()) {
                // differences in namespace prefix IDs are ok (eg. 'ns1' vs 'ns2'), as long as the namespace URI is the
                // same
                return RETURN_IGNORE_DIFFERENCE_NODES_IDENTICAL;
            } else {
                return RETURN_ACCEPT_DIFFERENCE;
            }
        }

        public void skippedComparison(Node control, Node test) {
        }
    }

    private IntygMeta createMeta() throws ScenarioNotFoundException {
        IntygMeta meta = new IntygMeta();
        meta.setAdditionalInfo("C");
        meta.setAvailable("true");
        return meta;
    }

    private CreateNewDraftHolder createNewDraftHolder() {
        Vardgivare vardgivare = new Vardgivare("hsaId0", "vardgivare");
        Vardenhet vardenhet = new Vardenhet("hsaId1", "namn", null, null, null, null, null, null, vardgivare);
        HoSPersonal hosPersonal = new HoSPersonal("Id1", "Grodan Boll", "forskrivarkod", "befattning", null, vardenhet);
        Patient patient = new Patient("Kalle", null, "Kula", "19121212-1212", null, null, null);
        return new CreateNewDraftHolder("Id1", hosPersonal, patient);
    }

    private CreateDraftCopyHolder createNewDraftCopyHolder() {
        Vardgivare vardgivare = new Vardgivare("hsaId0", "vardgivare");
        Vardenhet vardenhet = new Vardenhet("hsaId1", "namn", null, null, null, null, null, null, vardgivare);
        HoSPersonal hosPersonal = new HoSPersonal("Id1", "Grodan Boll", "forskrivarkod", "befattning", null, vardenhet);
        return new CreateDraftCopyHolder("Id1", hosPersonal);
    }

    private InternalModelHolder createInternalHolder(Utlatande internalModel) throws JsonProcessingException {
        return new InternalModelHolder(mapper.writeValueAsString(internalModel));
    }
}
