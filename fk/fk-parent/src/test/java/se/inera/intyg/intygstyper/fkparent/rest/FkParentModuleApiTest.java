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
package se.inera.intyg.intygstyper.fkparent.rest;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

import java.io.*;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.*;

import javax.xml.bind.JAXB;
import javax.xml.transform.Source;
import javax.xml.ws.soap.SOAPFaultException;

import org.apache.commons.io.FileUtils;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;
import org.oclc.purl.dsdl.svrl.SchematronOutputType;
import org.springframework.core.io.ClassPathResource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;

import se.inera.intyg.common.support.common.enumerations.PartKod;
import se.inera.intyg.common.support.model.StatusKod;
import se.inera.intyg.common.support.model.common.internal.*;
import se.inera.intyg.common.support.model.common.internal.Patient;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.support.model.util.ModelCompareUtil;
import se.inera.intyg.common.support.modules.support.api.dto.*;
import se.inera.intyg.common.support.modules.support.api.exception.*;
import se.inera.intyg.common.support.modules.support.api.exception.ExternalServiceCallException.ErrorIdEnum;
import se.inera.intyg.common.util.integration.integration.json.CustomObjectMapper;
import se.inera.intyg.intygstyper.fkparent.integration.RegisterCertificateValidator;
import se.inera.intyg.intygstyper.fkparent.model.converter.*;
import se.inera.intyg.intygstyper.fkparent.model.validator.InternalDraftValidator;
import se.inera.intyg.intygstyper.fkparent.support.ResultTypeUtil;
import se.riv.clinicalprocess.healthcond.certificate.getCertificate.v1.*;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v2.*;
import se.riv.clinicalprocess.healthcond.certificate.revokeCertificate.v1.*;
import se.riv.clinicalprocess.healthcond.certificate.types.v2.Part;
import se.riv.clinicalprocess.healthcond.certificate.types.v2.Statuskod;
import se.riv.clinicalprocess.healthcond.certificate.v2.*;

@RunWith(MockitoJUnitRunner.class)
public class FkParentModuleApiTest {

    private static final String INTYG_ID = "test-id";
    private static final String LOGICAL_ADDRESS = "logicalAddress";
    private static File registerCertificateFile;
    private static File getCertificateFile;
    private static File revokeCertificateFile;
    private static Utlatande utlatande;
    private static String json;

    @Mock
    private InternalDraftValidator<Utlatande> internalDraftValidator;

    @Mock
    private WebcertModelFactory<Utlatande> webcertModelFactory;

    @Mock
    private RegisterCertificateResponderInterface registerCertificateResponderInterface;

    @Mock
    private ModelCompareUtil<Utlatande> modelCompareUtil;

    @Mock
    private GetCertificateResponderInterface getCertificateResponderInterface;

    @Mock
    private RegisterCertificateValidator validator;

    @Mock
    private SvarIdHelper<Utlatande> svarIdHelper;

    @Mock
    private RevokeCertificateResponderInterface revokeCertificateClient;

    @Spy
    private ObjectMapper objectMapper = new CustomObjectMapper();

    @SuppressWarnings("unchecked")
    @InjectMocks
    FkParentModuleApi<Utlatande> moduleApi = mock(FkParentModuleApi.class, Mockito.CALLS_REAL_METHODS);

    @BeforeClass
    public static void set() throws Exception {
        registerCertificateFile = new ClassPathResource("registerCertificate.xml").getFile();
        getCertificateFile = new ClassPathResource("getCertificate.xml").getFile();
        revokeCertificateFile = new ClassPathResource("revokeCertificate.xml").getFile();
        utlatande = IntygTestDataBuilder.getUtlatande();
        json = new CustomObjectMapper().writeValueAsString(utlatande);
    }

    @Before
    public void setup() throws Exception {
        Field field = FkParentModuleApi.class.getDeclaredField("type");
        field.setAccessible(true);
        field.set(moduleApi, TestUtlatande.class);
        Field field2 = FkParentModuleApi.class.getDeclaredField("validator");
        field2.setAccessible(true);
        field2.set(moduleApi, validator);
    }

    @Test
    public void testValidateDraft() throws Exception {
        when(internalDraftValidator.validateDraft(any(Utlatande.class)))
                .thenReturn(new ValidateDraftResponse(ValidationStatus.VALID, new ArrayList<>()));

        ValidateDraftResponse res = moduleApi.validateDraft(json);

        assertNotNull(res);
        assertEquals(ValidationStatus.VALID, res.getStatus());
        verify(internalDraftValidator).validateDraft(any(Utlatande.class));
    }

    @Test
    public void testCreateNewInternal() throws Exception {
        CreateNewDraftHolder draftCertificateHolder = new CreateNewDraftHolder(INTYG_ID, new HoSPersonal(), new Patient());
        String res = moduleApi.createNewInternal(draftCertificateHolder);

        assertNotNull(res);
        verify(webcertModelFactory).createNewWebcertDraft(draftCertificateHolder);
    }

    @Test(expected = ModuleConverterException.class)
    public void testCreateNewInternalConverterException() throws Exception {
        when(webcertModelFactory.createNewWebcertDraft(any(CreateNewDraftHolder.class))).thenThrow(new ConverterException());
        moduleApi.createNewInternal(new CreateNewDraftHolder(INTYG_ID, new HoSPersonal(), new Patient()));
    }

    @Test
    public void testCreateNewInternalFromTemplate() throws Exception {
        CreateDraftCopyHolder draftCopyHolder = new CreateDraftCopyHolder(INTYG_ID, new HoSPersonal());
        String res = moduleApi.createNewInternalFromTemplate(draftCopyHolder, json);

        assertNotNull(res);
        verify(webcertModelFactory).createCopy(eq(draftCopyHolder), any(Utlatande.class));
    }

    @Test(expected = ModuleConverterException.class)
    public void testCreateNewInternalFromTemplateConverterException() throws Exception {
        when(webcertModelFactory.createCopy(any(CreateDraftCopyHolder.class), any(Utlatande.class))).thenThrow(new ConverterException());
        moduleApi.createNewInternalFromTemplate(new CreateDraftCopyHolder(INTYG_ID, new HoSPersonal()), json);
    }

    @Test
    public void testCreateRenewalFromTemplate() throws Exception {
        CreateDraftCopyHolder draftCopyHolder = new CreateDraftCopyHolder(INTYG_ID, new HoSPersonal());
        String res = moduleApi.createRenewalFromTemplate(draftCopyHolder, json);

        assertNotNull(res);
        verify(webcertModelFactory).createCopy(eq(draftCopyHolder), any(Utlatande.class));
    }

    @Test(expected = ModuleConverterException.class)
    public void testCreateRenewalFromTemplateConverterException() throws Exception {
        when(webcertModelFactory.createCopy(any(CreateDraftCopyHolder.class), any(Utlatande.class))).thenThrow(new ConverterException());
        moduleApi.createRenewalFromTemplate(new CreateDraftCopyHolder(INTYG_ID, new HoSPersonal()), json);
    }

    @Test(expected = ExternalServiceCallException.class)
    public void testSendCertificateToRecipientResponseError() throws Exception {
        String xmlBody = FileUtils.readFileToString(registerCertificateFile, Charsets.UTF_8);

        RegisterCertificateResponseType response = new RegisterCertificateResponseType();
        response.setResult(ResultTypeUtil.errorResult(ErrorIdType.TECHNICAL_ERROR, "error"));
        when(registerCertificateResponderInterface.registerCertificate(eq(LOGICAL_ADDRESS), any(RegisterCertificateType.class))).thenReturn(response);
        moduleApi.sendCertificateToRecipient(xmlBody, LOGICAL_ADDRESS, "recipientId");
    }

    @Test(expected = ExternalServiceCallException.class)
    public void testSendCertificateToRecipientSoapFault() throws Exception {
        String xmlBody = FileUtils.readFileToString(registerCertificateFile, Charsets.UTF_8);

        when(registerCertificateResponderInterface.registerCertificate(eq(LOGICAL_ADDRESS), any(RegisterCertificateType.class)))
                .thenThrow(mock(SOAPFaultException.class));
        moduleApi.sendCertificateToRecipient(xmlBody, LOGICAL_ADDRESS, "recipientId");
    }

    @Test
    public void testSendCertificateToRecipientResponseResultMissing() throws Exception {
        String xmlBody = FileUtils.readFileToString(registerCertificateFile, Charsets.UTF_8);

        when(registerCertificateResponderInterface.registerCertificate(eq(LOGICAL_ADDRESS), any(RegisterCertificateType.class)))
                .thenReturn(new RegisterCertificateResponseType());
        moduleApi.sendCertificateToRecipient(xmlBody, LOGICAL_ADDRESS, "recipientId");
        verify(registerCertificateResponderInterface).registerCertificate(eq(LOGICAL_ADDRESS), any(RegisterCertificateType.class));
    }

    @Test
    public void testSendCertificateToRecipient() throws Exception {
        String xmlBody = FileUtils.readFileToString(registerCertificateFile, Charsets.UTF_8);

        RegisterCertificateResponseType response = new RegisterCertificateResponseType();
        response.setResult(ResultTypeUtil.okResult());
        when(registerCertificateResponderInterface.registerCertificate(eq(LOGICAL_ADDRESS), any(RegisterCertificateType.class))).thenReturn(response);
        moduleApi.sendCertificateToRecipient(xmlBody, LOGICAL_ADDRESS, "recipientId");
        verify(registerCertificateResponderInterface).registerCertificate(eq(LOGICAL_ADDRESS), any(RegisterCertificateType.class));
    }

    @Test(expected = ModuleException.class)
    public void testSendCertificateToRecipientXmlBodyMissing() throws Exception {
        moduleApi.sendCertificateToRecipient(null, LOGICAL_ADDRESS, "recipientId");
    }

    @Test(expected = ModuleException.class)
    public void testSendCertificateToRecipientLogicalAddressMissing() throws Exception {
        moduleApi.sendCertificateToRecipient("xml", "", "recipientId");
    }

    @Test
    public void testShouldNotifyTrue() throws Exception {
        when(modelCompareUtil.isValidForNotification(any(Utlatande.class))).thenReturn(true);
        boolean res = moduleApi.shouldNotify("", json);
        assertTrue(res);
        verify(modelCompareUtil).isValidForNotification(any(Utlatande.class));
    }

    @Test
    public void testShouldNotifyFalse() throws Exception {
        when(modelCompareUtil.isValidForNotification(any(Utlatande.class))).thenReturn(false);
        boolean res = moduleApi.shouldNotify("", json);
        assertFalse(res);
        verify(modelCompareUtil).isValidForNotification(any(Utlatande.class));
    }

    @Test
    public void testGetCertificate() throws Exception {
        GetCertificateResponseType getCertificateResponse = JAXB.unmarshal(getCertificateFile, GetCertificateResponseType.class);
        when(getCertificateResponderInterface.getCertificate(eq(LOGICAL_ADDRESS), any(GetCertificateType.class))).thenReturn(getCertificateResponse);
        doReturn(utlatande).when(moduleApi).transportToInternal(any(Intyg.class));

        CertificateResponse res = moduleApi.getCertificate(INTYG_ID, LOGICAL_ADDRESS);
        assertNotNull(res);
        assertEquals(INTYG_ID, res.getMetaData().getCertificateId());
        assertFalse(res.isRevoked());
        ArgumentCaptor<GetCertificateType> parametersCaptor = ArgumentCaptor.forClass(GetCertificateType.class);
        verify(getCertificateResponderInterface).getCertificate(eq(LOGICAL_ADDRESS), parametersCaptor.capture());
        assertEquals(INTYG_ID, parametersCaptor.getValue().getIntygsId().getExtension());
        assertNotNull(parametersCaptor.getValue().getIntygsId().getRoot());
    }

    @Test
    public void testGetCertificateRevoked() throws Exception {
        GetCertificateResponseType getCertificateResponse = JAXB.unmarshal(getCertificateFile, GetCertificateResponseType.class);
        IntygsStatus revokedStatus = new IntygsStatus();
        revokedStatus.setPart(new Part());
        revokedStatus.getPart().setCode(PartKod.HSVARD.name());
        revokedStatus.setStatus(new Statuskod());
        revokedStatus.getStatus().setCode(StatusKod.CANCEL.name());
        getCertificateResponse.getIntyg().getStatus().add(revokedStatus);
        when(getCertificateResponderInterface.getCertificate(eq(LOGICAL_ADDRESS), any(GetCertificateType.class))).thenReturn(getCertificateResponse);
        doReturn(utlatande).when(moduleApi).transportToInternal(any(Intyg.class));

        CertificateResponse res = moduleApi.getCertificate(INTYG_ID, LOGICAL_ADDRESS);
        assertNotNull(res);
        assertEquals(INTYG_ID, res.getMetaData().getCertificateId());
        assertTrue(res.isRevoked());
        verify(getCertificateResponderInterface).getCertificate(eq(LOGICAL_ADDRESS), any(GetCertificateType.class));
    }

    @Test(expected = ModuleException.class)
    public void testGetCertificateConvertException() throws Exception {
        GetCertificateResponseType getCertificateResponse = JAXB.unmarshal(getCertificateFile, GetCertificateResponseType.class);
        when(getCertificateResponderInterface.getCertificate(eq(LOGICAL_ADDRESS), any(GetCertificateType.class))).thenReturn(getCertificateResponse);
        doThrow(new ConverterException()).when(moduleApi).transportToInternal(any(Intyg.class));

        moduleApi.getCertificate(INTYG_ID, LOGICAL_ADDRESS);
    }

    @Test(expected = ModuleException.class)
    public void testGetCertificateSoapFault() throws Exception {
        when(getCertificateResponderInterface.getCertificate(eq(LOGICAL_ADDRESS), any(GetCertificateType.class)))
                .thenThrow(mock(SOAPFaultException.class));

        moduleApi.getCertificate(INTYG_ID, LOGICAL_ADDRESS);
    }

    @Test
    public void testRegisterCertificate() throws Exception {
        RegisterCertificateType registerCertificateType = JAXB.unmarshal(registerCertificateFile, RegisterCertificateType.class);
        doReturn(registerCertificateType).when(moduleApi).internalToTransport(any(Utlatande.class));
        RegisterCertificateResponseType response = new RegisterCertificateResponseType();
        response.setResult(ResultTypeUtil.okResult());
        when(registerCertificateResponderInterface.registerCertificate(eq(LOGICAL_ADDRESS), any(RegisterCertificateType.class))).thenReturn(response);

        moduleApi.registerCertificate(json, LOGICAL_ADDRESS);
    }

    @Test(expected = ModuleConverterException.class)
    public void testRegisterCertificateConverterException() throws Exception {
        doThrow(new ConverterException()).when(moduleApi).internalToTransport(any(Utlatande.class));

        moduleApi.registerCertificate(json, LOGICAL_ADDRESS);
    }

    @Test
    public void testRegisterCertificateResponseError() throws Exception {
        RegisterCertificateType registerCertificateType = JAXB.unmarshal(registerCertificateFile, RegisterCertificateType.class);
        doReturn(registerCertificateType).when(moduleApi).internalToTransport(any(Utlatande.class));
        RegisterCertificateResponseType response = new RegisterCertificateResponseType();
        response.setResult(ResultTypeUtil.errorResult(ErrorIdType.APPLICATION_ERROR, "error"));
        when(registerCertificateResponderInterface.registerCertificate(eq(LOGICAL_ADDRESS), any(RegisterCertificateType.class))).thenReturn(response);

        try {
            moduleApi.registerCertificate(json, LOGICAL_ADDRESS);
            fail("should throw");
        } catch (ExternalServiceCallException e) {
            assertEquals(ErrorIdEnum.APPLICATION_ERROR, e.getErroIdEnum());
        }
    }

    @Test
    public void testRegisterCertificateAlreadyExists() throws Exception {
        RegisterCertificateType registerCertificateType = JAXB.unmarshal(registerCertificateFile, RegisterCertificateType.class);
        doReturn(registerCertificateType).when(moduleApi).internalToTransport(any(Utlatande.class));
        RegisterCertificateResponseType response = new RegisterCertificateResponseType();
        response.setResult(ResultTypeUtil.infoResult("Certificate already exists"));
        when(registerCertificateResponderInterface.registerCertificate(eq(LOGICAL_ADDRESS), any(RegisterCertificateType.class))).thenReturn(response);

        try {
            moduleApi.registerCertificate(json, LOGICAL_ADDRESS);
            fail("should throw");
        } catch (ExternalServiceCallException e) {
            assertEquals(ErrorIdEnum.VALIDATION_ERROR, e.getErroIdEnum());
        }
    }

    @Test
    public void testRegisterCertificateOtherInfoResult() throws Exception {
        RegisterCertificateType registerCertificateType = JAXB.unmarshal(registerCertificateFile, RegisterCertificateType.class);
        doReturn(registerCertificateType).when(moduleApi).internalToTransport(any(Utlatande.class));
        RegisterCertificateResponseType response = new RegisterCertificateResponseType();
        response.setResult(ResultTypeUtil.infoResult("Other info"));
        when(registerCertificateResponderInterface.registerCertificate(eq(LOGICAL_ADDRESS), any(RegisterCertificateType.class))).thenReturn(response);

        try {
            moduleApi.registerCertificate(json, LOGICAL_ADDRESS);
            fail("should throw");
        } catch (ExternalServiceCallException e) {
            assertEquals(ErrorIdEnum.APPLICATION_ERROR, e.getErroIdEnum());
        }
    }

    @Test
    public void testUpdateBeforeSave() throws Exception {
        final String otherHosPersonalName = "Other Person";
        doAnswer(invocation -> (Utlatande) invocation.getArguments()[0]).when(moduleApi).decorateDiagnoserWithDescriptions(any(Utlatande.class));

        HoSPersonal hosPersonal = new HoSPersonal();
        hosPersonal.setFullstandigtNamn(otherHosPersonalName);
        String res = moduleApi.updateBeforeSave(json, hosPersonal);
        assertNotNull(res);
        assertEquals(otherHosPersonalName, moduleApi.getInternal(res).getGrundData().getSkapadAv().getFullstandigtNamn());
        assertNull(moduleApi.getInternal(res).getGrundData().getSigneringsdatum());
        verify(moduleApi).decorateDiagnoserWithDescriptions(any(Utlatande.class));
    }

    @Test(expected = ModuleException.class)
    public void testUpdateBeforeSaveInvalidJson() throws Exception {
        moduleApi.updateBeforeSave("invalidJson", new HoSPersonal());
    }

    @Test
    public void testUpdatePatientBeforeSave() throws Exception {

        Patient patient = new Patient();
        patient.setEfternamn("updated lastName");
        patient.setMellannamn("updated middle-name");
        patient.setFornamn("updated firstName");
        patient.setFullstandigtNamn("updated full name");
        patient.setPersonId(new Personnummer("19121212-1212"));
        patient.setPostadress("updated postal address");
        patient.setPostnummer("1111111");
        patient.setPostort("updated post city");

        String res = moduleApi.updateBeforeSave(json, patient);
        assertNotNull(res);
        assertEquals(patient, moduleApi.getInternal(res).getGrundData().getPatient());
    }

    @Test
    public void testUpdateBeforeSigning() throws Exception {
        final String otherHosPersonalName = "Other Person";
        final LocalDateTime signDate = LocalDateTime.now();
        doAnswer(invocation -> (Utlatande) invocation.getArguments()[0]).when(moduleApi).decorateDiagnoserWithDescriptions(any(Utlatande.class));

        HoSPersonal hosPersonal = new HoSPersonal();
        hosPersonal.setFullstandigtNamn(otherHosPersonalName);
        String res = moduleApi.updateBeforeSigning(json, hosPersonal, signDate);
        assertNotNull(res);
        assertEquals(otherHosPersonalName, moduleApi.getInternal(res).getGrundData().getSkapadAv().getFullstandigtNamn());
        assertEquals(signDate, moduleApi.getInternal(res).getGrundData().getSigneringsdatum());
        verify(moduleApi).decorateDiagnoserWithDescriptions(any(Utlatande.class));
    }

    @Test(expected = ModuleException.class)
    public void testUpdateBeforeSigningInvalidJson() throws Exception {
        moduleApi.updateBeforeSigning("invalidJson", new HoSPersonal(), LocalDateTime.now());
    }

    @Test
    public void testGetUtlatandeFromJson() throws Exception {
        Utlatande res = moduleApi.getUtlatandeFromJson(json);
        assertNotNull(res);
        assertEquals(INTYG_ID, res.getId());
        assertNotNull(res.getGrundData());
    }

    @Test(expected = IOException.class)
    public void testGetUtlatandeFromJsonInvalidJson() throws Exception {
        moduleApi.getUtlatandeFromJson("invalidJson");
    }

    @Test
    public void testGetUtlatandeFromXml() throws Exception {
        String xmlBody = FileUtils.readFileToString(registerCertificateFile, Charsets.UTF_8);
        doReturn(utlatande).when(moduleApi).transportToInternal(any(Intyg.class));
        Utlatande res = moduleApi.getUtlatandeFromXml(xmlBody);
        assertNotNull(res);
        assertEquals(utlatande, res);
    }

    @Test(expected = ModuleException.class)
    public void testGetUtlatandeFromXmlConverterException() throws Exception {
        String xmlBody = FileUtils.readFileToString(registerCertificateFile, Charsets.UTF_8);
        doThrow(new ConverterException()).when(moduleApi).transportToInternal(any(Intyg.class));
        moduleApi.getUtlatandeFromXml(xmlBody);
    }

    @Test
    public void testGetIntygFromUtlatande() throws Exception {
        Intyg intyg = JAXB.unmarshal(registerCertificateFile, RegisterCertificateType.class).getIntyg();
        doReturn(intyg).when(moduleApi).utlatandeToIntyg(any(Utlatande.class));
        Intyg res = moduleApi.getIntygFromUtlatande(new TestUtlatande());
        assertNotNull(res);
        assertEquals(INTYG_ID, res.getIntygsId().getExtension());
    }

    @Test(expected = ModuleException.class)
    public void testGetIntygFromUtlatandeConverterException() throws Exception {
        doThrow(new ConverterException()).when(moduleApi).utlatandeToIntyg(any(Utlatande.class));
        moduleApi.getIntygFromUtlatande(new TestUtlatande());
    }

    @Test
    public void testTransformToStatisticsService() throws Exception {
        final String inputString = "input string";
        String res = moduleApi.transformToStatisticsService(inputString);
        assertEquals(inputString, res);
    }

    @Test
    public void testvalidateXml() throws Exception {
        String xmlBody = FileUtils.readFileToString(registerCertificateFile, Charsets.UTF_8);
        when(validator.validateSchematron(any(Source.class))).thenReturn(new SchematronOutputType());
        ValidateXmlResponse res = moduleApi.validateXml(xmlBody);
        assertNotNull(res);
        assertEquals(ValidationStatus.VALID, res.getStatus());
    }

    @Test
    public void testGetModuleSpecificArendeParameters() throws Exception {
        final String svarIdHelperAnswer1 = "svarIdHelperAnswer1";
        final String svarIdHelperAnswer2 = "svarIdHelperAnswer2";
        List<String> frageIds = Arrays.asList("1", "2", "9001");
        when(svarIdHelper.calculateFrageIdHandleForGrundForMU(any(Utlatande.class)))
                .thenReturn(Arrays.asList(svarIdHelperAnswer1, svarIdHelperAnswer2));

        Map<String, List<String>> res = moduleApi.getModuleSpecificArendeParameters(new TestUtlatande(), frageIds);
        assertEquals(3, res.keySet().size());
        assertEquals(2, res.get("1").size());
        assertEquals(svarIdHelperAnswer1, res.get("1").get(0));
        assertEquals(svarIdHelperAnswer2, res.get("1").get(1));
        assertEquals(1, res.get("2").size());
        assertEquals("kannedomOmPatient", res.get("2").get(0));
        assertEquals(1, res.get("9001").size());
        assertEquals("tillaggsfragor", res.get("9001").get(0));
        verify(svarIdHelper).calculateFrageIdHandleForGrundForMU(any(Utlatande.class));
    }

    @Test
    public void testGetAdditionalInfo() throws Exception {
        assertNull(moduleApi.getAdditionalInfo(new Intyg()));
    }

    @Test
    public void testRevokeCertificate() throws Exception {
        String xmlBody = FileUtils.readFileToString(revokeCertificateFile, Charsets.UTF_8);
        RevokeCertificateResponseType revokeResponse = new RevokeCertificateResponseType();
        revokeResponse.setResult(ResultTypeUtil.okResult());
        when(revokeCertificateClient.revokeCertificate(eq(LOGICAL_ADDRESS), any(RevokeCertificateType.class))).thenReturn(revokeResponse);

        moduleApi.revokeCertificate(xmlBody, LOGICAL_ADDRESS);
        ArgumentCaptor<RevokeCertificateType> parametersCaptor = ArgumentCaptor.forClass(RevokeCertificateType.class);
        verify(revokeCertificateClient).revokeCertificate(eq(LOGICAL_ADDRESS), parametersCaptor.capture());
        assertNotNull(parametersCaptor.getValue());
        assertEquals(INTYG_ID, parametersCaptor.getValue().getIntygsId().getExtension());
    }

    @Test(expected = ExternalServiceCallException.class)
    public void testRevokeCertificateResponseError() throws Exception {
        String xmlBody = FileUtils.readFileToString(revokeCertificateFile, Charsets.UTF_8);
        RevokeCertificateResponseType revokeResponse = new RevokeCertificateResponseType();
        revokeResponse.setResult(ResultTypeUtil.errorResult(ErrorIdType.APPLICATION_ERROR, "error"));
        when(revokeCertificateClient.revokeCertificate(eq(LOGICAL_ADDRESS), any(RevokeCertificateType.class))).thenReturn(revokeResponse);

        moduleApi.revokeCertificate(xmlBody, LOGICAL_ADDRESS);
    }

    @Test
    public void testCreateRevokeRequest() throws Exception {
        final String meddelande = "meddelande";

        String res = moduleApi.createRevokeRequest(utlatande, utlatande.getGrundData().getSkapadAv(), meddelande);
        RevokeCertificateType resultObject = JAXB.unmarshal(new StringReader(res), RevokeCertificateType.class);
        assertNotNull(resultObject);
        assertEquals(meddelande, resultObject.getMeddelande());
        assertEquals(INTYG_ID, resultObject.getIntygsId().getExtension());
    }

    public static class TestUtlatande implements Utlatande {

        private String typ;

        private String id;

        private String textVersion;

        private GrundData grundData = new GrundData();

        @Override
        public String getId() {
            return id;
        }

        @Override
        public String getTyp() {
            return typ;
        }

        @Override
        public GrundData getGrundData() {
            return grundData;
        }

        @Override
        public String getTextVersion() {
            return textVersion;
        }
    }

}
