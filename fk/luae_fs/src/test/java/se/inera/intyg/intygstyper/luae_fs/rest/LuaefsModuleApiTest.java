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

package se.inera.intyg.intygstyper.luae_fs.rest;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.*;
import static se.inera.intyg.intygstyper.fkparent.model.converter.RespConstants.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

import javax.xml.soap.SOAPFactory;
import javax.xml.ws.soap.SOAPFaultException;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.util.ReflectionTestUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;

import se.inera.intyg.common.services.texts.IntygTextsService;
import se.inera.intyg.common.support.common.enumerations.PartKod;
import se.inera.intyg.common.support.model.StatusKod;
import se.inera.intyg.common.support.model.common.internal.*;
import se.inera.intyg.common.support.model.common.internal.Patient;
import se.inera.intyg.common.support.model.common.internal.Vardgivare;
import se.inera.intyg.common.support.modules.service.WebcertModuleService;
import se.inera.intyg.common.support.modules.support.api.dto.*;
import se.inera.intyg.common.support.modules.support.api.exception.ExternalServiceCallException;
import se.inera.intyg.common.support.modules.support.api.exception.ExternalServiceCallException.ErrorIdEnum;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleException;
import se.inera.intyg.common.util.integration.integration.json.CustomObjectMapper;
import se.inera.intyg.intygstyper.fkparent.support.ResultTypeUtil;
import se.inera.intyg.intygstyper.luae_fs.model.converter.SvarIdHelperImpl;
import se.inera.intyg.intygstyper.luae_fs.model.converter.WebcertModelFactoryImpl;
import se.inera.intyg.intygstyper.luae_fs.model.internal.LuaefsUtlatande;
import se.inera.intyg.intygstyper.luae_fs.support.LuaefsEntryPoint;
import se.inera.intyg.intygstyper.luae_fs.utils.ScenarioFinder;
import se.riv.clinicalprocess.healthcond.certificate.getCertificate.v1.GetCertificateResponderInterface;
import se.riv.clinicalprocess.healthcond.certificate.getCertificate.v1.GetCertificateResponseType;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v2.RegisterCertificateResponderInterface;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v2.RegisterCertificateResponseType;
import se.riv.clinicalprocess.healthcond.certificate.revokeCertificate.v1.RevokeCertificateResponderInterface;
import se.riv.clinicalprocess.healthcond.certificate.revokeCertificate.v1.RevokeCertificateResponseType;
import se.riv.clinicalprocess.healthcond.certificate.types.v2.Part;
import se.riv.clinicalprocess.healthcond.certificate.types.v2.Statuskod;
import se.riv.clinicalprocess.healthcond.certificate.v2.*;

/**
 * Created by marced on 26/04/16.
 */
@RunWith(MockitoJUnitRunner.class)
public class LuaefsModuleApiTest {

    private static final String LOGICAL_ADDRESS = "logical address";
    private static final String TEST_HSA_ID = "hsaId";
    private static final String TEST_PATIENT_PERSONNR = "121212121212";

    @Mock
    private RegisterCertificateResponderInterface registerCertificateResponderInterface;

    @Mock
    private GetCertificateResponderInterface getCertificateResponderInterface;

    @Mock
    private IntygTextsService intygTextsServiceMock;

    @Spy
    private WebcertModelFactoryImpl webcertModelFactory;

    @Spy
    private ObjectMapper objectMapper = new CustomObjectMapper();

    @Mock
    private WebcertModuleService moduleService;

    @Mock
    private RevokeCertificateResponderInterface revokeClient;

    @Spy
    private SvarIdHelperImpl svarIdHelper;

    @InjectMocks
    private LuaefsModuleApi moduleApi;

    @Before
    public void setUp() throws Exception {

        ReflectionTestUtils.setField(webcertModelFactory, "intygTexts", intygTextsServiceMock);
        when(intygTextsServiceMock.getLatestVersion(LuaefsEntryPoint.MODULE_ID)).thenReturn("1.0");

    }

    @Test
    public void testSendCertificateToRecipientShouldUseXml() {
        when(registerCertificateResponderInterface.registerCertificate(anyString(), any())).thenReturn(createReturnVal(ResultCodeType.OK));
        try {
            String xmlContents = Resources.toString(Resources.getResource("luae_fs-simple-valid.xml"), Charsets.UTF_8);
            moduleApi.sendCertificateToRecipient(xmlContents, LOGICAL_ADDRESS, null);

            verify(registerCertificateResponderInterface, times(1)).registerCertificate(same(LOGICAL_ADDRESS), any());

        } catch (ModuleException | IOException e) {
            fail();
        }
    }

    @Test(expected = ModuleException.class)
    public void testSendCertificateToRecipientFailsWithoutXmlModel() throws ModuleException {
        when(registerCertificateResponderInterface.registerCertificate(anyString(), any())).thenReturn(createReturnVal(ResultCodeType.OK));
        moduleApi.sendCertificateToRecipient(null, LOGICAL_ADDRESS, null);
    }

    @Test(expected = ExternalServiceCallException.class)
    public void testSendCertificateToRecipientFailsForNonOkResponse() throws Exception {
        String xmlContents = Resources.toString(Resources.getResource("luae_fs-simple-valid.xml"), Charsets.UTF_8);
        when(registerCertificateResponderInterface.registerCertificate(anyString(), any())).thenReturn(createReturnVal(ResultCodeType.ERROR));
        moduleApi.sendCertificateToRecipient(xmlContents, LOGICAL_ADDRESS, null);
    }

    @Test
    public void testGetCertificate() throws Exception {

        GetCertificateResponseType result = createGetCertificateResponseType(StatusKod.SENTTO, PartKod.FKASSA);

        when(getCertificateResponderInterface.getCertificate(anyString(), any())).thenReturn(result);
        final CertificateResponse response = moduleApi.getCertificate("id", LOGICAL_ADDRESS);
        assertFalse(response.isRevoked());
    }

    @Test
    public void testGetCertificateWhenRevoked() throws Exception {

        GetCertificateResponseType result = createGetCertificateResponseType(StatusKod.CANCEL, PartKod.FKASSA);

        when(getCertificateResponderInterface.getCertificate(anyString(), any())).thenReturn(result);
        final CertificateResponse response = moduleApi.getCertificate("id", LOGICAL_ADDRESS);
        assertTrue(response.isRevoked());
    }

    @Test(expected = ModuleException.class)
    public void testGetCertificateWhenSOAPExceptionThowsModuleException() throws Exception {
        SOAPFaultException ex = new SOAPFaultException(SOAPFactory.newInstance().createFault());
        doThrow(ex).when(getCertificateResponderInterface).getCertificate(anyString(),
                any());

        moduleApi.getCertificate("id", LOGICAL_ADDRESS);

    }

    @Test
    public void testRegisterCertificate() throws IOException, ModuleException {

        final String json = FileUtils
                .readFileToString(new ClassPathResource("LuaefsModuleApiTest/valid-utkast-sample.json").getFile());

        LuaefsUtlatande utlatande = (LuaefsUtlatande) moduleApi.getUtlatandeFromJson(json);
        when(objectMapper.readValue(json, LuaefsUtlatande.class)).thenReturn(utlatande);

        RegisterCertificateResponseType result = createReturnVal(ResultCodeType.OK);
        when(registerCertificateResponderInterface.registerCertificate(anyString(), any())).thenReturn(result);

        moduleApi.registerCertificate(json, LOGICAL_ADDRESS);
    }

    @Test
    public void testRegisterCertificateAlreadyExists() throws Exception {
        final String logicalAddress = "logicalAddress";
        final String internalModel = "internal model";

        doReturn(ScenarioFinder.getInternalScenario("pass-minimal").asInternalModel()).when(objectMapper).readValue(anyString(), eq(LuaefsUtlatande.class));

        RegisterCertificateResponseType response = new RegisterCertificateResponseType();
        response.setResult(ResultTypeUtil.infoResult("Certificate already exists"));

        when(registerCertificateResponderInterface.registerCertificate(eq(logicalAddress), any())).thenReturn(response);

        try {
            moduleApi.registerCertificate(internalModel, logicalAddress);
            fail();
        } catch (ExternalServiceCallException e) {
            assertEquals(ErrorIdEnum.VALIDATION_ERROR, e.getErroIdEnum());
            assertEquals("Certificate already exists", e.getMessage());
        }
    }

    @Test
    public void testRegisterCertificateGenericInfoResult() throws Exception {
        final String logicalAddress = "logicalAddress";
        final String internalModel = "internal model";

        doReturn(ScenarioFinder.getInternalScenario("pass-minimal").asInternalModel()).when(objectMapper).readValue(anyString(), eq(LuaefsUtlatande.class));

        RegisterCertificateResponseType response = new RegisterCertificateResponseType();
        response.setResult(ResultTypeUtil.infoResult("INFO"));

        when(registerCertificateResponderInterface.registerCertificate(eq(logicalAddress), any())).thenReturn(response);

        try {
            moduleApi.registerCertificate(internalModel, logicalAddress);
            fail();
        } catch (ExternalServiceCallException e) {
            assertEquals(ErrorIdEnum.APPLICATION_ERROR, e.getErroIdEnum());
            assertEquals("INFO", e.getMessage());
        }
    }

    @Test
    public void testCreateRenewalFromTemplate() throws Exception {

        final String json = FileUtils
                .readFileToString(new ClassPathResource("LuaefsModuleApiTest/valid-utkast-sample.json").getFile());
        CreateDraftCopyHolder draftCertificateHolder = new CreateDraftCopyHolder("1", createHosPersonal());

        final String renewalFromTemplate = moduleApi.createRenewalFromTemplate(draftCertificateHolder, json);

        LuaefsUtlatande copy = (LuaefsUtlatande) moduleApi.getUtlatandeFromJson(renewalFromTemplate);
        assertEquals(TEST_HSA_ID, copy.getGrundData().getSkapadAv().getPersonId());

        verify(webcertModelFactory).createCopy(eq(draftCertificateHolder), any(LuaefsUtlatande.class));
    }

    @Test
    public void testCreateNewInternalFromTemplate() throws Exception {

        final String json = FileUtils
                .readFileToString(new ClassPathResource("LuaefsModuleApiTest/valid-utkast-sample.json").getFile());
        CreateDraftCopyHolder draftCertificateHolder = new CreateDraftCopyHolder("1", createHosPersonal());

        final String renewalFromTemplate = moduleApi.createNewInternalFromTemplate(draftCertificateHolder, json);

        LuaefsUtlatande copy = (LuaefsUtlatande) moduleApi.getUtlatandeFromJson(renewalFromTemplate);
        assertEquals(TEST_HSA_ID, copy.getGrundData().getSkapadAv().getPersonId());

        verify(webcertModelFactory).createCopy(eq(draftCertificateHolder), any(LuaefsUtlatande.class));
    }

    @Test
    public void testCreateNewInternal() throws Exception {

        CreateNewDraftHolder createNewDraftHolder = new CreateNewDraftHolder("1", createHosPersonal(), createPatient());

        final String renewalFromTemplate = moduleApi.createNewInternal(createNewDraftHolder);

        LuaefsUtlatande copy = (LuaefsUtlatande) moduleApi.getUtlatandeFromJson(renewalFromTemplate);
        assertEquals(TEST_HSA_ID, copy.getGrundData().getSkapadAv().getPersonId());
        assertEquals(TEST_PATIENT_PERSONNR, copy.getGrundData().getPatient().getPersonId().getPersonnummer());

    }

    @Test(expected = ExternalServiceCallException.class)
    public void testRegisterCertificateThrowsExternalServiceCallExceptionOnErrorResultCode() throws IOException, ModuleException {

        RegisterCertificateResponseType result = createReturnVal(ResultCodeType.ERROR);
        when(registerCertificateResponderInterface.registerCertificate(anyString(), any())).thenReturn(result);
        final String json = FileUtils.readFileToString(new ClassPathResource("LuaefsModuleApiTest/valid-utkast-sample.json").getFile());

        moduleApi.registerCertificate(json, LOGICAL_ADDRESS);
    }

    /**
     * Verify that grundData is updated
     *
     * @throws IOException
     * @throws ModuleException
     */
    @Test
    public void testUpdateBeforeSave() throws IOException, ModuleException {

        final String json = FileUtils
                .readFileToString(new ClassPathResource("LuaefsModuleApiTest/valid-utkast-sample.json").getFile());

        LuaefsUtlatande utlatandeBeforeSave = (LuaefsUtlatande) moduleApi.getUtlatandeFromJson(json);
        assertNotEquals(TEST_HSA_ID, utlatandeBeforeSave.getGrundData().getSkapadAv().getPersonId());

        when(objectMapper.readValue(json, LuaefsUtlatande.class)).thenReturn(utlatandeBeforeSave);

        RegisterCertificateResponseType result = createReturnVal(ResultCodeType.OK);
        when(registerCertificateResponderInterface.registerCertificate(anyString(), any())).thenReturn(result);

        final String internalModelResponse = moduleApi.updateBeforeSave(json, createHosPersonal());
        final Utlatande utlatandeFromJson = moduleApi.getUtlatandeFromJson(internalModelResponse);
        assertEquals(TEST_HSA_ID, utlatandeFromJson.getGrundData().getSkapadAv().getPersonId());

        verify(moduleService, times(3)).getDescriptionFromDiagnosKod(anyString(), anyString());
    }

    @Test
    public void testRevokeCertificate() throws Exception {
        final String logicalAddress = "logicalAddress";
        String xmlContents = Resources.toString(Resources.getResource("revokerequest.xml"), Charsets.UTF_8);

        RevokeCertificateResponseType returnVal = new RevokeCertificateResponseType();
        returnVal.setResult(ResultTypeUtil.okResult());
        when(revokeClient.revokeCertificate(eq(logicalAddress), any())).thenReturn(returnVal);
        moduleApi.revokeCertificate(xmlContents, logicalAddress);
        verify(revokeClient, times(1)).revokeCertificate(eq(logicalAddress), any());
    }

    @Test(expected = ExternalServiceCallException.class)
    public void testRevokeCertificateThrowsExternalServiceCallException() throws Exception {
        final String logicalAddress = "logicalAddress";
        String xmlContents = Resources.toString(Resources.getResource("revokerequest.xml"), Charsets.UTF_8);

        RevokeCertificateResponseType returnVal = new RevokeCertificateResponseType();
        returnVal.setResult(ResultTypeUtil.errorResult(ErrorIdType.APPLICATION_ERROR, "resultText"));
        when(revokeClient.revokeCertificate(eq(logicalAddress), any())).thenReturn(returnVal);
        moduleApi.revokeCertificate(xmlContents, logicalAddress);
        fail();
    }

    @Test
    public void testCreateRevokeRequest() throws Exception {
        final String meddelande = "revokeMessage";
        final String intygId = "intygId";

        GrundData gd = new GrundData();
        gd.setPatient(new se.inera.intyg.common.support.model.common.internal.Patient());
        gd.getPatient().setPersonId(new Personnummer("191212121212"));
        HoSPersonal skapadAv = createHosPersonal();
        gd.setSkapadAv(skapadAv);

        Utlatande utlatande = LuaefsUtlatande.builder().setId(intygId).setGrundData(gd).setTextVersion("").build();

        String res = moduleApi.createRevokeRequest(utlatande, skapadAv, meddelande);
        assertNotNull(res);
        assertNotEquals("", res);
    }

    @Test
    public void testGetModuleSpecificArendeParameters() throws Exception {
        LuaefsUtlatande utlatande = ScenarioFinder.getInternalScenario("pass-minimal").asInternalModel();

        Map<String, List<String>> res = moduleApi.getModuleSpecificArendeParameters(utlatande,
                Arrays.asList(MEDICINSKAFORUTSATTNINGARFORARBETE_SVAR_ID_22, FUNKTIONSNEDSATTNING_PSYKISK_SVAR_ID_11,
                        GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1, AKTIVITETSBEGRANSNING_SVAR_ID_17));

        assertNotNull(res);
        assertEquals(4, res.keySet().size());
        assertNotNull(res.get(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1));
        assertEquals(2, res.get(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1).size());
        assertEquals(GRUNDFORMEDICINSKTUNDERLAG_SVAR_JSON_ID_1, res.get(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1).get(0));
        assertEquals(GRUNDFORMEDICINSKTUNDERLAG_UNDERSOKNING_AV_PATIENT_SVAR_JSON_ID_1, res.get(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1).get(1));
        assertNotNull(res.get(MEDICINSKAFORUTSATTNINGARFORARBETE_SVAR_ID_22));
        assertEquals(1, res.get(MEDICINSKAFORUTSATTNINGARFORARBETE_SVAR_ID_22).size());
        assertEquals(MEDICINSKAFORUTSATTNINGARFORARBETE_SVAR_JSON_ID_22, res.get(MEDICINSKAFORUTSATTNINGARFORARBETE_SVAR_ID_22).get(0));
        assertNotNull(res.get(FUNKTIONSNEDSATTNING_PSYKISK_SVAR_ID_11));
        assertEquals(1, res.get(FUNKTIONSNEDSATTNING_PSYKISK_SVAR_ID_11).size());
        assertEquals(FUNKTIONSNEDSATTNING_PSYKISK_SVAR_JSON_ID_11, res.get(FUNKTIONSNEDSATTNING_PSYKISK_SVAR_ID_11).get(0));
        assertNotNull(res.get(AKTIVITETSBEGRANSNING_SVAR_ID_17));
        assertEquals(1, res.get(AKTIVITETSBEGRANSNING_SVAR_ID_17).size());
        assertEquals(AKTIVITETSBEGRANSNING_SVAR_JSON_ID_17, res.get(AKTIVITETSBEGRANSNING_SVAR_ID_17).get(0));
    }

    private GetCertificateResponseType createGetCertificateResponseType(final StatusKod statusKod, final PartKod part)
            throws IOException, ModuleException {
        GetCertificateResponseType response = new GetCertificateResponseType();

        String xmlContents = Resources.toString(Resources.getResource("luae_fs-simple-valid.xml"), Charsets.UTF_8);
        Utlatande utlatandeFromXml = moduleApi.getUtlatandeFromXml(xmlContents);
        Intyg intyg = moduleApi.getIntygFromUtlatande(utlatandeFromXml);

        intyg.getStatus().add(createStatus(statusKod.name(), part.name()));

        response.setIntyg(intyg);

        return response;
    }

    private IntygsStatus createStatus(String statuskod, String partkod) {
        IntygsStatus intygsStatus = new IntygsStatus();
        Statuskod sk = new Statuskod();
        sk.setCode(statuskod);
        intygsStatus.setStatus(sk);
        Part part = new Part();
        part.setCode(partkod);
        intygsStatus.setPart(part);
        intygsStatus.setTidpunkt(LocalDateTime.now());
        return intygsStatus;
    }

    private Patient createPatient() {
        Patient patient = new Patient();
        patient.setFornamn("fornamn");
        patient.setEfternamn("efternamn");
        patient.setPersonId(new Personnummer(TEST_PATIENT_PERSONNR));
        return patient;
    }

    private HoSPersonal createHosPersonal() {
        HoSPersonal hosPerson = new HoSPersonal();
        hosPerson.setPersonId(TEST_HSA_ID);
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

    private RegisterCertificateResponseType createReturnVal(ResultCodeType res) {
        ResultType value = new ResultType();

        RegisterCertificateResponseType responseType = new RegisterCertificateResponseType();

        value.setResultCode(res);
        responseType.setResult(value);
        return responseType;
    }
}
