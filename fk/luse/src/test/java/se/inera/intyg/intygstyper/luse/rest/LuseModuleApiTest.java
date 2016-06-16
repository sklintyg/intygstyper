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

package se.inera.intyg.intygstyper.luse.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;
import javax.xml.ws.soap.SOAPFaultException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;

import se.inera.intyg.common.schemas.clinicalprocess.healthcond.certificate.utils.v2.ResultTypeUtil;
import se.inera.intyg.common.support.model.common.internal.*;
import se.inera.intyg.common.support.model.common.internal.Patient;
import se.inera.intyg.common.support.model.common.internal.Vardgivare;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.support.modules.service.WebcertModuleService;
import se.inera.intyg.common.support.modules.support.api.dto.*;
import se.inera.intyg.common.support.modules.support.api.exception.ExternalServiceCallException;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleException;
import se.inera.intyg.intygstyper.luse.model.converter.WebcertModelFactoryImpl;
import se.inera.intyg.intygstyper.luse.model.internal.LuseUtlatande;
import se.inera.intyg.intygstyper.luse.model.utils.ScenarioFinder;
import se.inera.intyg.intygstyper.luse.model.utils.ScenarioNotFoundException;
import se.inera.intyg.intygstyper.luse.validator.InternalDraftValidatorImpl;
import se.riv.clinicalprocess.healthcond.certificate.getCertificate.v1.*;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v2.*;
import se.riv.clinicalprocess.healthcond.certificate.revokeCertificate.v1.RevokeCertificateResponderInterface;
import se.riv.clinicalprocess.healthcond.certificate.revokeCertificate.v1.RevokeCertificateResponseType;
import se.riv.clinicalprocess.healthcond.certificate.v2.*;

@RunWith(MockitoJUnitRunner.class)
public class LuseModuleApiTest {

    private static final String LOGICAL_ADDRESS = "logical address";

    @Mock
    private RegisterCertificateResponderInterface registerCertificateResponderInterface;

    @Mock
    private WebcertModuleService moduleService;

    @Mock
    private WebcertModelFactoryImpl webcertModelFactory;

    @Mock
    private InternalDraftValidatorImpl internalDraftValidator;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private GetCertificateResponderInterface getCertificateResponder;

    @Mock
    private RevokeCertificateResponderInterface revokeClient;

    @InjectMocks
    private LuseModuleApi moduleApi;

    @Test
    public void testSendCertificateShouldUseXml() {
        when(registerCertificateResponderInterface.registerCertificate(anyString(), any())).thenReturn(createReturnVal(ResultCodeType.OK));
        try {
            String xmlContents = Resources.toString(Resources.getResource("luse.xml"), Charsets.UTF_8);
            moduleApi.sendCertificateToRecipient(xmlContents, LOGICAL_ADDRESS, null);

            verify(registerCertificateResponderInterface, times(1)).registerCertificate(same(LOGICAL_ADDRESS), any());

        } catch (ModuleException | IOException e) {
            fail();
        }
    }

    @Test(expected = ModuleException.class)
    public void testSendCertificateShouldFailWhenErrorIsReturned() throws ModuleException {
        when(registerCertificateResponderInterface.registerCertificate(anyString(), any())).thenReturn(createReturnVal(ResultCodeType.ERROR));
        try {
            String xmlContents = Resources.toString(Resources.getResource("luse.xml"), Charsets.UTF_8);
            moduleApi.sendCertificateToRecipient(xmlContents, LOGICAL_ADDRESS, null);
        } catch (IOException e) {
            fail();
        }
    }

    @Test(expected = ModuleException.class)
    public void testSendCertificateShouldFailOnNullModelHolder() throws ModuleException {
        moduleApi.sendCertificateToRecipient(null, LOGICAL_ADDRESS, null);
    }

    @Test(expected = ModuleException.class)
    public void testSendCertificateShouldFailOnEmptyXml() throws ModuleException {
        moduleApi.sendCertificateToRecipient(null, LOGICAL_ADDRESS, null);
    }

    @Test(expected = ModuleException.class)
    public void testSendCertificateShouldFailOnNullLogicalAddress() throws ModuleException {
        moduleApi.sendCertificateToRecipient("blaha", null, null);
    }

    @Test(expected = ModuleException.class)
    public void testSendCertificateShouldFailOnEmptyLogicalAddress() throws ModuleException {
        moduleApi.sendCertificateToRecipient("blaha", "", null);
    }

    @Test
    public void testValidateShouldUseValidator() throws Exception {
        when(objectMapper.readValue(eq("internal model"), eq(LuseUtlatande.class))).thenReturn(null);
        moduleApi.validateDraft("internal model");
        verify(internalDraftValidator, times(1)).validateDraft(any());
    }

    @Test
    public void testCreateNewInternal() throws Exception {
        when(webcertModelFactory.createNewWebcertDraft(any())).thenReturn(null);
        when(objectMapper.writeValueAsString(any())).thenReturn("internal model");
        moduleApi.createNewInternal(createDraftHolder());
        verify(webcertModelFactory, times(1)).createNewWebcertDraft(any());
    }

    @Test(expected = ModuleException.class)
    public void testCreateNewInternalThrowsModuleException() throws Exception {
        when(webcertModelFactory.createNewWebcertDraft(any())).thenThrow(new ConverterException());
        moduleApi.createNewInternal(createDraftHolder());
    }

    @Test
    public void testCreateNewInternalFromTemplate() throws Exception {
        when(webcertModelFactory.createCopy(any(), any())).thenReturn(null);
        when(objectMapper.readValue(eq("internal model"), eq(LuseUtlatande.class))).thenReturn(null);
        when(objectMapper.writeValueAsString(any())).thenReturn("internal model");

        moduleApi.createNewInternalFromTemplate(createCopyHolder(), "internal model");

        verify(webcertModelFactory, times(1)).createCopy(any(), any());
    }

    @Test(expected = ModuleException.class)
    public void testCreateNewInternalFromTemplateThrowsModuleException() throws Exception {
        when(webcertModelFactory.createCopy(any(), any())).thenThrow(new ConverterException());
        moduleApi.createNewInternalFromTemplate(createCopyHolder(), "internal model");
    }

    @Test
    public void testGetCertificate() throws Exception {
        final String certificateId = "certificateId";
        final String logicalAddress = "logicalAddress";
        final String internalModel = "internal model";

        when(getCertificateResponder.getCertificate(eq(logicalAddress), any())).thenReturn(createGetCertificateResponseType());
        when(objectMapper.writeValueAsString(any())).thenReturn(internalModel);

        CertificateResponse certificate = moduleApi.getCertificate(certificateId, logicalAddress);

        ArgumentCaptor<GetCertificateType> captor = ArgumentCaptor.forClass(GetCertificateType.class);
        verify(getCertificateResponder, times(1)).getCertificate(eq(logicalAddress), captor.capture());
        assertEquals(certificateId, captor.getValue().getIntygsId().getExtension());
        assertEquals(internalModel, certificate.getInternalModel());
        assertEquals(false, certificate.isRevoked());
    }

    @Test(expected = ModuleException.class)
    public void testGetCertificateThrowsModuleException() throws ModuleException, SOAPException {
        final String certificateId = "certificateId";
        final String logicalAddress = "logicalAddress";
        when(getCertificateResponder.getCertificate(eq(logicalAddress), any()))
                .thenThrow(new SOAPFaultException(SOAPFactory.newInstance().createFault()));
        moduleApi.getCertificate(certificateId, logicalAddress);
    }

    @Test
    public void testRegisterCertificate() throws Exception {
        final String logicalAddress = "logicalAddress";
        final String internalModel = "internal model";
        RegisterCertificateResponseType response = new RegisterCertificateResponseType();
        response.setResult(ResultTypeUtil.okResult());

        when(objectMapper.readValue(internalModel, LuseUtlatande.class)).thenReturn(ScenarioFinder.getInternalScenario("pass-minimal").asInternalModel());
        when(registerCertificateResponderInterface.registerCertificate(eq(logicalAddress), any())).thenReturn(response);

        moduleApi.registerCertificate(internalModel, logicalAddress);

        ArgumentCaptor<RegisterCertificateType> captor = ArgumentCaptor.forClass(RegisterCertificateType.class);
        verify(registerCertificateResponderInterface, times(1)).registerCertificate(eq(logicalAddress), captor.capture());
        assertNotNull(captor.getValue().getIntyg());
    }

    @Test(expected = ExternalServiceCallException.class)
    public void testRegisterCertificateShouldThrowExceptionOnFailedCallToIT() throws Exception {
        final String logicalAddress = "logicalAddress";
        final String internalModel = "internal model";
        RegisterCertificateResponseType response = new RegisterCertificateResponseType();
        response.setResult(ResultTypeUtil.errorResult(ErrorIdType.VALIDATION_ERROR, "resultText"));

        when(objectMapper.readValue(internalModel, LuseUtlatande.class)).thenReturn(ScenarioFinder.getInternalScenario("pass-minimal").asInternalModel());
        when(registerCertificateResponderInterface.registerCertificate(eq(logicalAddress), any())).thenReturn(response);

        moduleApi.registerCertificate(internalModel, logicalAddress);
    }

    @Test(expected = ExternalServiceCallException.class)
    public void testRegisterCertificateShouldThrowExceptionOnBadCertificate() throws Exception {
        final String logicalAddress = "logicalAddress";
        final String internalModel = "internal model";
        when(objectMapper.readValue(internalModel, LuseUtlatande.class)).thenReturn(null);

        moduleApi.registerCertificate(internalModel, logicalAddress);
    }

    @Test
    public void testGetUtlatandeFromJson() throws Exception {
        final String utlatandeJson = "utlatandeJson";
        when(objectMapper.readValue(eq(utlatandeJson), eq(LuseUtlatande.class)))
                .thenReturn(ScenarioFinder.getInternalScenario("pass-minimal").asInternalModel());
        Utlatande utlatandeFromJson = moduleApi.getUtlatandeFromJson(utlatandeJson);
        assertNotNull(utlatandeFromJson);
    }

    @Test
    public void testUpdateBeforeSave() throws Exception {
        final String internalModel = "internal model";
        when(objectMapper.readValue(anyString(), eq(LuseUtlatande.class)))
                .thenReturn(ScenarioFinder.getInternalScenario("pass-minimal").asInternalModel());
        when(objectMapper.writeValueAsString(any())).thenReturn(internalModel);
        String response = moduleApi.updateBeforeSave(internalModel, createHosPersonal());
        assertEquals(internalModel, response);
        verify(moduleService, times(1)).getDescriptionFromDiagnosKod(anyString(), anyString());
    }

    @Test
    public void testUpdateBeforeSigning() throws Exception {
        final String internalModel = "internal model";
        when(objectMapper.readValue(anyString(), eq(LuseUtlatande.class)))
                .thenReturn(ScenarioFinder.getInternalScenario("pass-minimal").asInternalModel());
        when(objectMapper.writeValueAsString(any())).thenReturn(internalModel);
        String response = moduleApi.updateBeforeSigning(internalModel, createHosPersonal(), null);
        assertEquals(internalModel, response);
        verify(moduleService, times(1)).getDescriptionFromDiagnosKod(anyString(), anyString());
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

        Utlatande utlatande = LuseUtlatande.builder().setId(intygId).setGrundData(gd).setTextVersion("").build();

        String res = moduleApi.createRevokeRequest(utlatande, skapadAv, meddelande);
        assertNotNull(res);
        assertNotEquals("", res);
    }

    private GetCertificateResponseType createGetCertificateResponseType() throws ScenarioNotFoundException {
        GetCertificateResponseType res = new GetCertificateResponseType();
        RegisterCertificateType registerType = ScenarioFinder.getInternalScenario("pass-minimal").asTransportModel();
        res.setIntyg(registerType.getIntyg());
        return res;
    }

    private CreateDraftCopyHolder createCopyHolder() {
        return new CreateDraftCopyHolder("certificateId",
                createHosPersonal());
    }

    private CreateNewDraftHolder createDraftHolder() {
        Patient patient = new Patient();
        patient.setFornamn("fornamn");
        patient.setEfternamn("efternamn");
        patient.setPersonId(new Personnummer("19121212-1212"));
        return new CreateNewDraftHolder("certificateId", createHosPersonal(), patient);
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

    private RegisterCertificateResponseType createReturnVal(ResultCodeType res) {
        RegisterCertificateResponseType retVal = new RegisterCertificateResponseType();
        ResultType value = new ResultType();
        value.setResultCode(res);
        retVal.setResult(value);
        return retVal;
    }
}