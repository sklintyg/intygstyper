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

package se.inera.certificate.modules.luae_fs.rest;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.*;

import java.io.IOException;

import javax.xml.soap.SOAPFactory;
import javax.xml.ws.soap.SOAPFaultException;

import org.apache.commons.io.FileUtils;
import org.joda.time.LocalDateTime;
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

import se.inera.certificate.modules.luae_fs.model.converter.WebcertModelFactory;
import se.inera.certificate.modules.luae_fs.model.converter.util.ConverterUtil;
import se.inera.certificate.modules.luae_fs.model.internal.LuaefsUtlatande;
import se.inera.certificate.modules.luae_fs.support.LuaefsEntryPoint;
import se.inera.intyg.common.schemas.clinicalprocess.healthcond.certificate.utils.v2.ResultTypeUtil;
import se.inera.intyg.common.services.texts.IntygTextsService;
import se.inera.intyg.common.support.model.StatusKod;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.Utlatande;
import se.inera.intyg.common.support.model.converter.util.WebcertModelFactoryUtil;
import se.inera.intyg.common.support.modules.service.WebcertModuleService;
import se.inera.intyg.common.support.modules.support.api.dto.*;
import se.inera.intyg.common.support.modules.support.api.dto.Patient;
import se.inera.intyg.common.support.modules.support.api.dto.Vardgivare;
import se.inera.intyg.common.support.modules.support.api.exception.ExternalServiceCallException;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleException;
import se.inera.intyg.common.util.integration.integration.json.CustomObjectMapper;
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
    private static final String DIAGNOS_KLARTEXT_1 = "klartext1";
    private static final String DIAGNOS_KLARTEXT_2 = "klartext2";
    private static final String TEST_HSA_ID = "hsaId";
    private static final String TEST_PATIENT_PERSONNR = "121212121212";

    @Mock
    private RegisterCertificateResponderInterface registerCertificateResponderInterface;

    @Mock
    private GetCertificateResponderInterface getCertificateResponderInterface;

    @Mock
    private IntygTextsService intygTextsServiceMock;

    @Spy
    private WebcertModelFactory webcertModelFactory;

    @Mock
    private ConverterUtil converterUtil;

    @Spy
    private ObjectMapper objectMapper = new CustomObjectMapper();

    @Mock
    private WebcertModuleService moduleService;

    @Mock
    private RevokeCertificateResponderInterface revokeClient;

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

        GetCertificateResponseType result = createGetCertificateResponseType(StatusKod.SENTTO, "FK");

        when(getCertificateResponderInterface.getCertificate(anyString(), any())).thenReturn(result);
        final CertificateResponse response = moduleApi.getCertificate("id", LOGICAL_ADDRESS);
        assertFalse(response.isRevoked());
    }

    @Test
    public void testGetCertificateWhenRevoked() throws Exception {

        GetCertificateResponseType result = createGetCertificateResponseType(StatusKod.CANCEL, "FK");

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
        when(converterUtil.fromJsonString(anyString())).thenReturn(utlatande);

        RegisterCertificateResponseType result = createReturnVal(ResultCodeType.OK);
        when(registerCertificateResponderInterface.registerCertificate(anyString(), any())).thenReturn(result);

        InternalModelHolder spy = spy(new InternalModelHolder(json, null));
        moduleApi.registerCertificate(spy, LOGICAL_ADDRESS);
    }

    @Test
    public void testCreateRenewalFromTemplate() throws Exception {

        final String json = FileUtils
                .readFileToString(new ClassPathResource("LuaefsModuleApiTest/valid-utkast-sample.json").getFile());
        InternalModelHolder spy = spy(new InternalModelHolder(json, null));
        CreateDraftCopyHolder draftCertificateHolder = new CreateDraftCopyHolder("1", createHosPersonal());

        final InternalModelResponse renewalFromTemplate = moduleApi.createRenewalFromTemplate(draftCertificateHolder, spy);

        LuaefsUtlatande copy = (LuaefsUtlatande) moduleApi.getUtlatandeFromJson(renewalFromTemplate.getInternalModel());
        assertEquals(TEST_HSA_ID, copy.getGrundData().getSkapadAv().getPersonId());

        verify(webcertModelFactory).createCopy(eq(draftCertificateHolder), any(LuaefsUtlatande.class));
    }

    @Test
    public void testCreateNewInternalFromTemplate() throws Exception {

        final String json = FileUtils
                .readFileToString(new ClassPathResource("LuaefsModuleApiTest/valid-utkast-sample.json").getFile());
        InternalModelHolder spy = spy(new InternalModelHolder(json, null));
        CreateDraftCopyHolder draftCertificateHolder = new CreateDraftCopyHolder("1", createHosPersonal());

        final InternalModelResponse renewalFromTemplate = moduleApi.createNewInternalFromTemplate(draftCertificateHolder, spy);

        LuaefsUtlatande copy = (LuaefsUtlatande) moduleApi.getUtlatandeFromJson(renewalFromTemplate.getInternalModel());
        assertEquals(TEST_HSA_ID, copy.getGrundData().getSkapadAv().getPersonId());

        verify(webcertModelFactory).createCopy(eq(draftCertificateHolder), any(LuaefsUtlatande.class));
    }

    @Test
    public void testCreateNewInternal() throws Exception {

        CreateNewDraftHolder createNewDraftHolder = new CreateNewDraftHolder("1", createHosPersonal(), createPatient());

        final InternalModelResponse renewalFromTemplate = moduleApi.createNewInternal(createNewDraftHolder);

        LuaefsUtlatande copy = (LuaefsUtlatande) moduleApi.getUtlatandeFromJson(renewalFromTemplate.getInternalModel());
        assertEquals(TEST_HSA_ID, copy.getGrundData().getSkapadAv().getPersonId());
        assertEquals(TEST_PATIENT_PERSONNR, copy.getGrundData().getPatient().getPersonId().getPersonnummer());

    }

    @Test(expected = ExternalServiceCallException.class)
    public void testRegisterCertificateThrowsExternalServiceCallExceptionOnErrorResultCode() throws IOException, ModuleException {

        RegisterCertificateResponseType result = createReturnVal(ResultCodeType.ERROR);
        when(registerCertificateResponderInterface.registerCertificate(anyString(), any())).thenReturn(result);

        InternalModelHolder spy = spy(new InternalModelHolder("json", null));
        moduleApi.registerCertificate(spy, LOGICAL_ADDRESS);
    }

    /**
     * Verify that grundData is updated with supplied hosPerson
     *
     * @throws IOException
     * @throws ModuleException
     */
    @Test
    public void testUpdateBeforeSaveUpdatesHosPersonal() throws IOException, ModuleException {

        final String json = FileUtils
                .readFileToString(new ClassPathResource("LuaefsModuleApiTest/valid-utkast-sample.json").getFile());

        LuaefsUtlatande utlatandeBeforeSave = (LuaefsUtlatande) moduleApi.getUtlatandeFromJson(json);
        assertNotEquals(TEST_HSA_ID, utlatandeBeforeSave.getGrundData().getSkapadAv().getPersonId());

        when(converterUtil.fromJsonString(anyString())).thenReturn(utlatandeBeforeSave);

        RegisterCertificateResponseType result = createReturnVal(ResultCodeType.OK);
        when(registerCertificateResponderInterface.registerCertificate(anyString(), any())).thenReturn(result);

        InternalModelHolder spy = spy(new InternalModelHolder(json, null));
        final InternalModelResponse internalModelResponse = moduleApi.updateBeforeSave(spy, createHosPersonal());
        final Utlatande utlatandeFromJson = moduleApi.getUtlatandeFromJson(internalModelResponse.getInternalModel());
        assertEquals(TEST_HSA_ID, utlatandeFromJson.getGrundData().getSkapadAv().getPersonId());

    }

    /**
     * Test that utlatande diagnoser is decorated with additional displayName
     *
     * @throws Exception
     */
    @Test
    public void testDecorateUtlatande() throws Exception {

        when(moduleService.getDescriptionFromDiagnosKod(eq("S0"), eq("ICD_10_SE"))).thenReturn(DIAGNOS_KLARTEXT_1);
        when(moduleService.getDescriptionFromDiagnosKod(eq("S1"), eq("ICD_10_SE"))).thenReturn(DIAGNOS_KLARTEXT_2);

        final String json = FileUtils
                .readFileToString(new ClassPathResource("LuaefsModuleApiTest/valid-utkast-sample.json").getFile());

        final String decoratedJson = moduleApi.decorateUtlatande(json);

        LuaefsUtlatande decoratedUtlatande = (LuaefsUtlatande) moduleApi.getUtlatandeFromJson(decoratedJson);

        assertNotNull(decoratedUtlatande);

        assertEquals(DIAGNOS_KLARTEXT_1, decoratedUtlatande.getDiagnoser().get(0).getDiagnosDisplayName());
        assertEquals(DIAGNOS_KLARTEXT_2, decoratedUtlatande.getDiagnoser().get(1).getDiagnosDisplayName());
        assertEquals(null, decoratedUtlatande.getDiagnoser().get(2).getDiagnosDisplayName());
    }

    @Test
    public void testMarshall() throws Exception {
        final String json = FileUtils
                .readFileToString(new ClassPathResource("LuaefsModuleApiTest/valid-utkast-sample.json").getFile());
        final String marshalled = moduleApi.marshall(json);
        assertTrue(marshalled.length() > 0);
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
        se.inera.intyg.common.support.model.common.internal.HoSPersonal skapadAv = WebcertModelFactoryUtil
                .convertHosPersonalToEdit(createHosPersonal());
        gd.setSkapadAv(skapadAv);

        Utlatande utlatande = LuaefsUtlatande.builder().setId(intygId).setGrundData(gd).setTextVersion("").build();

        String res = moduleApi.createRevokeRequest(utlatande, skapadAv, meddelande);
        assertNotNull(res);
        assertNotEquals("", res);
    }
    private GetCertificateResponseType createGetCertificateResponseType(final StatusKod statusKod, final String part)
            throws IOException, ModuleException {
        GetCertificateResponseType response = new GetCertificateResponseType();

        String xmlContents = Resources.toString(Resources.getResource("luae_fs-simple-valid.xml"), Charsets.UTF_8);
        Utlatande utlatandeFromXml = moduleApi.getUtlatandeFromXml(xmlContents);
        Intyg intyg = moduleApi.getIntygFromUtlatande(utlatandeFromXml);

        intyg.getStatus().add(createStatus(statusKod.name(), part));

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
        return new Patient("fornamn", "mellannamn", "efternamn", new Personnummer(TEST_PATIENT_PERSONNR), "postadress", "12345", "postort");
    }

    private HoSPersonal createHosPersonal() {
        return new HoSPersonal(TEST_HSA_ID, "Doktor A", "12345", "Läkare", null, createVardenhet());
    }

    private Vardenhet createVardenhet() {
        return new Vardenhet("hsaId", "ve1", "gatan", "12345", "orten", "122333", null, null, new Vardgivare("vg1", "vg1"));
    }

    private RegisterCertificateResponseType createReturnVal(ResultCodeType res) {
        ResultType value = new ResultType();

        RegisterCertificateResponseType responseType = new RegisterCertificateResponseType();

        value.setResultCode(res);
        responseType.setResult(value);
        return responseType;
    }
}
