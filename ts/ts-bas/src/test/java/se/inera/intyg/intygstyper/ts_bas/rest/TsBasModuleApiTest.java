/*
 * Copyright (C) 2016 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.inera.intyg.intygstyper.ts_bas.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static se.inera.intyg.common.support.modules.converter.InternalConverterUtil.aCV;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import se.inera.intyg.common.schemas.intygstjansten.ts.utils.ResultTypeUtil;
import se.inera.intyg.common.services.texts.IntygTextsService;
import se.inera.intyg.common.support.modules.support.ApplicationOrigin;
import se.inera.intyg.common.support.modules.support.api.ModuleApi;
import se.inera.intyg.common.support.modules.support.api.dto.*;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleException;
import se.inera.intyg.common.util.integration.integration.json.CustomObjectMapper;
import se.inera.intyg.intygstyper.ts_bas.model.converter.UtlatandeToIntyg;
import se.inera.intyg.intygstyper.ts_bas.model.converter.WebcertModelFactoryImpl;
import se.inera.intyg.intygstyper.ts_bas.model.internal.Utlatande;
import se.inera.intyg.intygstyper.ts_bas.pdf.PdfGeneratorImpl;
import se.inera.intyg.intygstyper.ts_bas.utils.*;
import se.inera.intygstjanster.ts.services.GetTSBasResponder.v1.*;
import se.inera.intygstjanster.ts.services.RegisterTSBasResponder.v1.*;
import se.inera.intygstjanster.ts.services.v1.ErrorIdType;
import se.inera.intygstjanster.ts.services.v1.IntygMeta;
import se.riv.clinicalprocess.healthcond.certificate.types.v2.IntygId;
import se.riv.clinicalprocess.healthcond.certificate.v2.Intyg;
import se.riv.clinicalprocess.healthcond.certificate.v2.Svar;
import se.riv.clinicalprocess.healthcond.certificate.v2.Svar.Delsvar;

/**
 * Sets up an actual HTTP server and client to test the {@link ModuleApi} service. This is the place to verify that
 * response headers and response statuses etc are correct.
 */
@RunWith(MockitoJUnitRunner.class)
public class TsBasModuleApiTest {

    @InjectMocks
    private TsBasModuleApi moduleApi;

    @Spy
    private ObjectMapper objectMapper = new CustomObjectMapper();

    @Mock
    private RegisterTSBasResponderInterface registerTSBasResponderInterface;

    @Mock
    private GetTSBasResponderInterface getTSBasResponderInterface;

    @Spy
    private WebcertModelFactoryImpl webcertModelFactory = new WebcertModelFactoryImpl();

    @Mock
    private PdfGeneratorImpl pdfGenerator;

    @Mock
    private IntygTextsService intygTexts;

    @Before
    public void setup() throws Exception {
        // use reflection to set IntygTextsService mock in webcertModelFactory
        Field field = WebcertModelFactoryImpl.class.getDeclaredField("intygTexts");
        field.setAccessible(true);
        field.set(webcertModelFactory, intygTexts);
    }

    @Test
    public void testPdf() throws Exception {
        when(pdfGenerator.generatePDF(any(Utlatande.class), any(ApplicationOrigin.class))).thenReturn(new byte[] {});
        when(pdfGenerator.generatePdfFilename(any(Utlatande.class))).thenReturn("filename");
        for (Scenario scenario : ScenarioFinder.getInternalScenarios("valid-*")) {
            moduleApi.pdf(objectMapper.writeValueAsString(scenario.asInternalModel()), null, ApplicationOrigin.MINA_INTYG);
        }
    }

    @Test
    public void copyContainsOriginalData() throws Exception {
        Scenario scenario = ScenarioFinder.getInternalScenario("valid-maximal");
        String internalHolder = objectMapper.writeValueAsString(scenario.asInternalModel());

        String holder = moduleApi.createNewInternalFromTemplate(createNewDraftCopyHolder(), internalHolder);

        assertNotNull(holder);
        Utlatande utlatande = objectMapper.readValue(holder, Utlatande.class);
        assertEquals(true, utlatande.getSyn().getSynfaltsdefekter());
    }

    @Test
    public void createNewInternal() throws ModuleException {
        CreateNewDraftHolder holder = createNewDraftHolder();

        String response = moduleApi.createNewInternal(holder);

        assertNotNull(response);
    }

    @Test
    public void testRegisterCertificate() throws JsonProcessingException, ScenarioNotFoundException {
        RegisterTSBasResponseType registerResponse = new RegisterTSBasResponseType();
        registerResponse.setResultat(ResultTypeUtil.okResult());
        Mockito.when(registerTSBasResponderInterface.registerTSBas(Mockito.eq("OK"), Mockito.any(RegisterTSBasType.class)))
                .thenReturn(registerResponse);

        String logicalAddress = "OK";
        List<String> failResults = new ArrayList<>();
        for (Scenario scenario : ScenarioFinder.getInternalScenarios("valid-*")) {
            String internalModel = objectMapper.writeValueAsString(scenario.asInternalModel());
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
        RegisterTSBasResponseType registerResponse = new RegisterTSBasResponseType();
        registerResponse.setResultat(ResultTypeUtil.errorResult(ErrorIdType.APPLICATION_ERROR, "failed"));
        Mockito.when(registerTSBasResponderInterface.registerTSBas(Mockito.eq("FAIL"), Mockito.any(RegisterTSBasType.class)))
                .thenReturn(registerResponse);

        String logicalAddress = "FAIL";
        String failResult = "";
        Scenario scenario = ScenarioFinder.getInternalScenario("invalid-missing-identitet");
        String internalModel = objectMapper.writeValueAsString(scenario.asInternalModel());
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
    public void getAdditionalInfoFromUtlatandeTest() throws Exception {
        Utlatande utlatande = ScenarioFinder.getInternalScenario("valid-maximal").asInternalModel();
        Intyg intyg = UtlatandeToIntyg.convert(utlatande);

        String result = moduleApi.getAdditionalInfo(intyg);
        assertEquals("C1, C1E, C, CE, D1, D1E, D, DE, TAXI, ANNAT", result);
    }

    @Test
    public void getAdditionalInfoOneResultTest() throws ModuleException {
        Intyg intyg = new Intyg();
        intyg.setIntygsId(new IntygId());
        intyg.getIntygsId().setExtension("intygId");
        Svar s = new Svar();
        s.setId("1");
        Delsvar delsvar = new Delsvar();
        delsvar.setId("1.1");
        delsvar.getContent().add(aCV(null, "IAV6", null));
        s.getDelsvar().add(delsvar);
        intyg.getSvar().add(s);

        String result = moduleApi.getAdditionalInfo(intyg);
        assertEquals("D1E", result);
    }

    @Test
    public void getAdditionalInfoMultipleResultsTest() throws ModuleException {
        Intyg intyg = new Intyg();
        intyg.setIntygsId(new IntygId());
        intyg.getIntygsId().setExtension("intygId");
        Svar s = new Svar();
        s.setId("1");
        Delsvar delsvar = new Delsvar();
        delsvar.setId("1.1");
        delsvar.getContent().add(aCV(null, "IAV1", null));
        s.getDelsvar().add(delsvar);
        Svar s2 = new Svar();
        s2.setId("1");
        Delsvar delsvar2 = new Delsvar();
        delsvar2.setId("1.1");
        delsvar2.getContent().add(aCV(null, "IAV3", null));
        s2.getDelsvar().add(delsvar2);
        Svar s3 = new Svar();
        s3.setId("1");
        Delsvar delsvar3 = new Delsvar();
        delsvar3.setId("1.1");
        delsvar3.getContent().add(aCV(null, "IAV9", null));
        s3.getDelsvar().add(delsvar3);
        intyg.getSvar().add(s);
        intyg.getSvar().add(s2);
        intyg.getSvar().add(s3);

        String result = moduleApi.getAdditionalInfo(intyg);
        assertEquals("C1, C, TAXI", result);
    }

    @Test
    public void getAdditionalInfoSvarNotFoundTest() throws ModuleException {
        Intyg intyg = new Intyg();
        intyg.setIntygsId(new IntygId());
        intyg.getIntygsId().setExtension("intygId");
        Svar s = new Svar();
        s.setId("2"); // wrong svarId
        Delsvar delsvar = new Delsvar();
        delsvar.setId("1.1");
        delsvar.getContent().add(aCV(null, "IAV6", null));
        s.getDelsvar().add(delsvar);
        intyg.getSvar().add(s);

        String result = moduleApi.getAdditionalInfo(intyg);
        assertNull(result);
    }

    @Test
    public void getAdditionalInfoDelsvarNotFoundTest() throws ModuleException {
        Intyg intyg = new Intyg();
        intyg.setIntygsId(new IntygId());
        intyg.getIntygsId().setExtension("intygId");
        Svar s = new Svar();
        s.setId("1");
        Delsvar delsvar = new Delsvar();
        delsvar.setId("1.3"); // wrong delsvarId
        delsvar.getContent().add(aCV(null, "IAV6", null));
        s.getDelsvar().add(delsvar);
        intyg.getSvar().add(s);

        String result = moduleApi.getAdditionalInfo(intyg);
        assertNull(result);
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
        Patient patient = new Patient("Kalle", null, "Kula", new Personnummer("19121212-1212"), null, null, null);
        return new CreateNewDraftHolder("Id1", hosPersonal, patient);
    }

    private CreateDraftCopyHolder createNewDraftCopyHolder() {
        Vardgivare vardgivare = new Vardgivare("hsaId0", "vardgivare");
        Vardenhet vardenhet = new Vardenhet("hsaId1", "namn", null, null, null, null, null, null, vardgivare);
        HoSPersonal hosPersonal = new HoSPersonal("Id1", "Grodan Boll", "forskrivarkod", "befattning", null, vardenhet);
        return new CreateDraftCopyHolder("Id1", hosPersonal);
    }

}
