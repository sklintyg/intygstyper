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
package se.inera.intyg.intygstyper.ts_diabetes.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static se.inera.intyg.common.support.modules.converter.InternalConverterUtil.aCV;

import java.io.StringWriter;
import java.lang.reflect.Field;

import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBException;
import javax.xml.soap.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;

import com.fasterxml.jackson.databind.ObjectMapper;

import se.inera.intyg.common.services.texts.IntygTextsService;
import se.inera.intyg.common.support.model.common.internal.*;
import se.inera.intyg.common.support.model.converter.util.XslTransformer;
import se.inera.intyg.common.support.modules.support.ApplicationOrigin;
import se.inera.intyg.common.support.modules.support.api.ModuleApi;
import se.inera.intyg.common.support.modules.support.api.dto.*;
import se.inera.intyg.common.support.modules.support.api.exception.ExternalServiceCallException;
import se.inera.intyg.common.support.modules.support.api.exception.ExternalServiceCallException.ErrorIdEnum;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleException;
import se.inera.intyg.common.util.integration.integration.json.CustomObjectMapper;
import se.inera.intyg.intygstyper.ts_diabetes.integration.RegisterTSDiabetesResponderImpl;
import se.inera.intyg.intygstyper.ts_diabetes.model.converter.UtlatandeToIntyg;
import se.inera.intyg.intygstyper.ts_diabetes.model.converter.WebcertModelFactoryImpl;
import se.inera.intyg.intygstyper.ts_diabetes.model.internal.IntygAvserKategori;
import se.inera.intyg.intygstyper.ts_diabetes.model.internal.Utlatande;
import se.inera.intyg.intygstyper.ts_diabetes.pdf.PdfGeneratorImpl;
import se.inera.intyg.intygstyper.ts_diabetes.utils.*;
import se.inera.intyg.intygstyper.ts_parent.integration.ResultTypeUtil;
import se.inera.intyg.intygstyper.ts_parent.integration.SendTSClient;
import se.inera.intygstjanster.ts.services.GetTSDiabetesResponder.v1.*;
import se.inera.intygstjanster.ts.services.RegisterTSDiabetesResponder.v1.*;
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
public class TsDiabetesModuleApiTest {

    @InjectMocks
    private TsDiabetesModuleApi moduleApi;

    @Spy
    private ObjectMapper objectMapper = new CustomObjectMapper();

    @Mock
    private RegisterTSDiabetesResponderInterface registerTSDiabetesResponderInterface;

    @Mock
    private GetTSDiabetesResponderInterface getTSDiabetesResponderInterface;

    @Spy
    private WebcertModelFactoryImpl webcertModelFactory = new WebcertModelFactoryImpl();

    @Mock
    private PdfGeneratorImpl pdfGenerator;

    @Mock
    private IntygTextsService intygTexts;

    @Mock
    private XslTransformer xslTransformer;

    @Mock
    private SendTSClient sendTsDiabetesClient;

    @Before
    public void setup() throws Exception {
        // use reflection to set IntygTextsService mock in webcertModelFactory
        Field field = WebcertModelFactoryImpl.class.getDeclaredField("intygTexts");
        field.setAccessible(true);
        field.set(webcertModelFactory, intygTexts);
    }

    @Test
    public void createNewInternal() throws ModuleException {
        CreateNewDraftHolder holder = createNewDraftHolder();

        String response = moduleApi.createNewInternal(holder);

        assertNotNull(response);
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
        Scenario scenario = ScenarioFinder.getTransportScenario("valid-minimal");
        String internalHolder = objectMapper.writeValueAsString(scenario.asInternalModel());

        String holder = moduleApi.createNewInternalFromTemplate(createNewDraftCopyHolder(), internalHolder);

        assertNotNull(holder);
        Utlatande utlatande = objectMapper.readValue(holder, Utlatande.class);
        assertEquals(true, utlatande.getIntygAvser().getKorkortstyp().contains(IntygAvserKategori.A1));
    }

    @Test
    public void getAdditionalInfoFromUtlatandeTest() throws Exception {
        Utlatande utlatande = ScenarioFinder.getInternalScenario("valid-maximal").asInternalModel();
        Intyg intyg = UtlatandeToIntyg.convert(utlatande);

        String result = moduleApi.getAdditionalInfo(intyg);
        assertEquals("AM, A1, A2, A, B, BE, TRAKTOR, C1, C1E, C, CE, D1, D1E, D, DE, TAXI", result);
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
        delsvar.getContent().add(aCV(null, "IAV17", null));
        s.getDelsvar().add(delsvar);
        intyg.getSvar().add(s);

        String result = moduleApi.getAdditionalInfo(intyg);
        assertEquals("TRAKTOR", result);
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

    @Test
    public void testRegisterCertificateAlreadyExists() throws Exception {
        final String logicalAddress = "logicalAddress";
        final String internalModel = objectMapper
                .writeValueAsString(ScenarioFinder.getInternalScenario("valid-minimal").asInternalModel());

        RegisterTSDiabetesResponseType registerResponse = new RegisterTSDiabetesResponseType();
        registerResponse.setResultat(ResultTypeUtil.infoResult(RegisterTSDiabetesResponderImpl.CERTIFICATE_ALREADY_EXISTS));
        when(registerTSDiabetesResponderInterface.registerTSDiabetes(Mockito.eq(logicalAddress), Mockito.any(RegisterTSDiabetesType.class)))
                .thenReturn(registerResponse);

        try {
            moduleApi.registerCertificate(internalModel, logicalAddress);
            fail();
        } catch (ExternalServiceCallException e) {
            assertEquals(ErrorIdEnum.VALIDATION_ERROR, e.getErroIdEnum());
            assertEquals(RegisterTSDiabetesResponderImpl.CERTIFICATE_ALREADY_EXISTS, e.getMessage());
        }
    }

    @Test
    public void testRegisterCertificateGenericInfoResult() throws Exception {
        final String logicalAddress = "logicalAddress";
        final String internalModel = objectMapper
                .writeValueAsString(ScenarioFinder.getInternalScenario("valid-minimal").asInternalModel());

        RegisterTSDiabetesResponseType registerResponse = new RegisterTSDiabetesResponseType();
        registerResponse.setResultat(ResultTypeUtil.infoResult("INFO"));
        when(registerTSDiabetesResponderInterface.registerTSDiabetes(Mockito.eq(logicalAddress), Mockito.any(RegisterTSDiabetesType.class)))
                .thenReturn(registerResponse);

        try {
            moduleApi.registerCertificate(internalModel, logicalAddress);
            fail();
        } catch (ExternalServiceCallException e) {
            assertEquals(ErrorIdEnum.APPLICATION_ERROR, e.getErroIdEnum());
            assertEquals("INFO", e.getMessage());
        }
    }

    @Test(expected = ExternalServiceCallException.class)
    public void testRegisterCertificateErrorResult() throws Exception {
        final String logicalAddress = "logicalAddress";
        final String internalModel = objectMapper
                .writeValueAsString(ScenarioFinder.getInternalScenario("valid-minimal").asInternalModel());

        RegisterTSDiabetesResponseType registerResponse = new RegisterTSDiabetesResponseType();
        registerResponse.setResultat(ResultTypeUtil.errorResult(ErrorIdType.APPLICATION_ERROR, "error"));
        when(registerTSDiabetesResponderInterface.registerTSDiabetes(Mockito.eq(logicalAddress), Mockito.any(RegisterTSDiabetesType.class)))
                .thenReturn(registerResponse);

        moduleApi.registerCertificate(internalModel, logicalAddress);
    }

    @Test
    public void testSendCertificateToRecipient() throws Exception {
        final String xmlBody = "xmlBody";
        final String logicalAddress = "logicalAddress";
        final String recipientId = "recipient";
        final String transformedXml = "transformedXml";
        when(xslTransformer.transform(xmlBody)).thenReturn(transformedXml);
        SOAPMessage response = mock(SOAPMessage.class);
        when(response.getSOAPPart()).thenReturn(mock(SOAPPart.class));
        when(response.getSOAPPart().getEnvelope()).thenReturn(mock(SOAPEnvelope.class));
        when(response.getSOAPPart().getEnvelope().getBody()).thenReturn(mock(SOAPBody.class));
        when(response.getSOAPPart().getEnvelope().getBody().hasFault()).thenReturn(false);
        when(sendTsDiabetesClient.registerCertificate(transformedXml, logicalAddress)).thenReturn(response);

        moduleApi.sendCertificateToRecipient(xmlBody, logicalAddress, recipientId);

        verify(xslTransformer).transform(xmlBody);
        verify(sendTsDiabetesClient).registerCertificate(transformedXml, logicalAddress);
    }

    @Test(expected = ModuleException.class)
    public void testSendCertificateToRecipientFault() throws Exception {
        final String xmlBody = "xmlBody";
        final String logicalAddress = "logicalAddress";
        final String recipientId = "recipient";
        final String transformedXml = "transformedXml";
        when(xslTransformer.transform(xmlBody)).thenReturn(transformedXml);
        SOAPMessage response = mock(SOAPMessage.class);
        when(response.getSOAPPart()).thenReturn(mock(SOAPPart.class));
        when(response.getSOAPPart().getEnvelope()).thenReturn(mock(SOAPEnvelope.class));
        when(response.getSOAPPart().getEnvelope().getBody()).thenReturn(mock(SOAPBody.class));
        when(response.getSOAPPart().getEnvelope().getBody().hasFault()).thenReturn(true);
        when(sendTsDiabetesClient.registerCertificate(transformedXml, logicalAddress)).thenReturn(response);

        moduleApi.sendCertificateToRecipient(xmlBody, logicalAddress, recipientId);
    }

    @Test
    public void testGetCertificate() throws ModuleException, ScenarioNotFoundException {
        GetTSDiabetesResponseType result = new GetTSDiabetesResponseType();
        result.setIntyg(ScenarioFinder.getTransportScenario("valid-maximal").asTransportModel().getIntyg());
        result.setMeta(createMeta());
        result.setResultat(ResultTypeUtil.okResult());
        Mockito.when(getTSDiabetesResponderInterface.getTSDiabetes(Mockito.eq("TS"), Mockito.any(GetTSDiabetesType.class)))
                .thenReturn(result);

        CertificateResponse internal = moduleApi.getCertificate("cert-id", "TS");
        assertNotNull(internal);
    }

    @Test
    public void testGetCertificateRevokedReturnsCertificate() throws Exception {
        GetTSDiabetesResponseType result = new GetTSDiabetesResponseType();
        result.setIntyg(ScenarioFinder.getTransportScenario("valid-maximal").asTransportModel().getIntyg());
        result.setMeta(createMeta());
        result.setResultat(ResultTypeUtil.errorResult(ErrorIdType.REVOKED, "error"));
        Mockito.when(getTSDiabetesResponderInterface.getTSDiabetes(Mockito.eq("TS"), Mockito.any(GetTSDiabetesType.class)))
                .thenReturn(result);

        CertificateResponse internal = moduleApi.getCertificate("cert-id", "TS");
        assertNotNull(internal);
    }

    @Test(expected = ModuleException.class)
    public void testGetCertificateRevokedValidationError() throws Exception {
        GetTSDiabetesResponseType result = new GetTSDiabetesResponseType();
        result.setResultat(ResultTypeUtil.errorResult(ErrorIdType.VALIDATION_ERROR, "error"));
        Mockito.when(getTSDiabetesResponderInterface.getTSDiabetes(Mockito.eq("TS"), Mockito.any(GetTSDiabetesType.class)))
        .thenReturn(result);

        moduleApi.getCertificate("cert-id", "TS");
    }

    @Test(expected = ModuleException.class)
    public void testGetCertificateRevokedApplicationError() throws Exception {
        GetTSDiabetesResponseType result = new GetTSDiabetesResponseType();
        result.setResultat(ResultTypeUtil.errorResult(ErrorIdType.APPLICATION_ERROR, "error"));
        Mockito.when(getTSDiabetesResponderInterface.getTSDiabetes(Mockito.eq("TS"), Mockito.any(GetTSDiabetesType.class)))
        .thenReturn(result);

        moduleApi.getCertificate("cert-id", "TS");
    }

    @Test
    public void testGetUtlatandeFromXml() throws Exception {
        String xml = xmlToString(ScenarioFinder.getTransportScenario("valid-minimal").asTransportModel());
        Utlatande res = moduleApi.getUtlatandeFromXml(xml);

        assertNotNull(res);
    }

    @Test(expected = ModuleException.class)
    public void testGetUtlatandeFromXmlConverterException() throws Exception {
        String xml = xmlToString(new RegisterTSDiabetesType());
        moduleApi.getUtlatandeFromXml(xml);
    }

    private CreateNewDraftHolder createNewDraftHolder() {
        HoSPersonal hosPersonal = createHosPersonal();
        Patient patient = new Patient();
        patient.setFornamn("Kalle");
        patient.setEfternamn("Kula");
        patient.setPersonId(new Personnummer("19121212-1212"));
        return new CreateNewDraftHolder("Id1", hosPersonal, patient);
    }

    private CreateDraftCopyHolder createNewDraftCopyHolder() {
        return new CreateDraftCopyHolder("Id1", createHosPersonal());
    }

    private HoSPersonal createHosPersonal() {
        HoSPersonal hosPerson = new HoSPersonal();
        hosPerson.setPersonId("hsaId1");
        hosPerson.setFullstandigtNamn("Doktor A");
        hosPerson.setVardenhet(createVardenhet());
        return hosPerson;
    }

    private Vardenhet createVardenhet() {
        Vardenhet vardenhet = new Vardenhet();
        vardenhet.setEnhetsid("hsaId");
        vardenhet.setEnhetsnamn("ve1");
        vardenhet.setVardgivare(new Vardgivare());
        vardenhet.getVardgivare().setVardgivarid("vg1");
        vardenhet.getVardgivare().setVardgivarnamn("vg1");
        return vardenhet;
    }

    private IntygMeta createMeta() throws ScenarioNotFoundException {
        IntygMeta meta = new IntygMeta();
        meta.setAdditionalInfo("C");
        meta.setAvailable("true");
        return meta;
    }

    private String xmlToString(RegisterTSDiabetesType registerTsDiabetes) throws JAXBException {
        StringWriter stringWriter = new StringWriter();
        JAXB.marshal(registerTsDiabetes, stringWriter);
        return stringWriter.toString();
    }
}
