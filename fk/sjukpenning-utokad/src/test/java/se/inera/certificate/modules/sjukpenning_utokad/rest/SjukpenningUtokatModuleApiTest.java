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

package se.inera.certificate.modules.sjukpenning_utokad.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.Writer;

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

import se.inera.certificate.modules.sjukpenning_utokad.model.converter.WebcertModelFactory;
import se.inera.certificate.modules.sjukpenning_utokad.model.converter.util.ConverterUtil;
import se.inera.certificate.modules.sjukpenning_utokad.model.internal.SjukpenningUtokadUtlatande;
import se.inera.certificate.modules.sjukpenning_utokad.model.utils.ScenarioFinder;
import se.inera.certificate.modules.sjukpenning_utokad.model.utils.ScenarioNotFoundException;
import se.inera.certificate.modules.sjukpenning_utokad.validator.InternalDraftValidator;
import se.inera.intyg.common.schemas.clinicalprocess.healthcond.certificate.utils.v2.ResultTypeUtil;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.Utlatande;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.support.model.converter.util.WebcertModelFactoryUtil;
import se.inera.intyg.common.support.modules.service.WebcertModuleService;
import se.inera.intyg.common.support.modules.support.api.dto.*;
import se.inera.intyg.common.support.modules.support.api.dto.Patient;
import se.inera.intyg.common.support.modules.support.api.dto.Vardgivare;
import se.inera.intyg.common.support.modules.support.api.exception.ExternalServiceCallException;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleException;
import se.riv.clinicalprocess.healthcond.certificate.getCertificate.v1.*;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v2.*;
import se.riv.clinicalprocess.healthcond.certificate.revokeCertificate.v1.RevokeCertificateResponderInterface;
import se.riv.clinicalprocess.healthcond.certificate.revokeCertificate.v1.RevokeCertificateResponseType;
import se.riv.clinicalprocess.healthcond.certificate.v2.*;

@RunWith(MockitoJUnitRunner.class)
public class SjukpenningUtokatModuleApiTest {

    private static final String LOGICAL_ADDRESS = "logical address";

    @Mock
    private RegisterCertificateResponderInterface registerCertificateResponderInterface;

    @Mock
    private ConverterUtil converterUtil;

    @Mock
    private WebcertModuleService moduleService;

    @Mock
    private WebcertModelFactory webcertModelFactory;

    @Mock
    private InternalDraftValidator internalDraftValidator;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private GetCertificateResponderInterface getCertificateResponder;

    @Mock
    private RevokeCertificateResponderInterface revokeClient;

    @InjectMocks
    private SjukpenningUtokadModuleApi moduleApi;

    @Test
    public void testSendCertificateShouldUseXml() {
        when(registerCertificateResponderInterface.registerCertificate(anyString(), any())).thenReturn(createReturnVal(ResultCodeType.OK));
        try {
            String xmlContents = Resources.toString(Resources.getResource("transport/sjukpenning-utokat2.xml"), Charsets.UTF_8);
            InternalModelHolder spy = spy(new InternalModelHolder("internal model", xmlContents));
            moduleApi.sendCertificateToRecipient(spy, LOGICAL_ADDRESS, null);

            verify(spy, atLeast(1)).getXmlModel();
            verify(spy, never()).getInternalModel();
            verify(registerCertificateResponderInterface, times(1)).registerCertificate(same(LOGICAL_ADDRESS), any());

        } catch (ModuleException | IOException e) {
            fail();
        }
    }

    @Test(expected = ModuleException.class)
    public void testSendCertificateShouldFailWhenErrorIsReturned() throws ModuleException {
        when(registerCertificateResponderInterface.registerCertificate(anyString(), any())).thenReturn(createReturnVal(ResultCodeType.ERROR));
        try {
            String xmlContents = Resources.toString(Resources.getResource("transport/sjukpenning-utokat2.xml"), Charsets.UTF_8);
            moduleApi.sendCertificateToRecipient(new InternalModelHolder("internal model", xmlContents), LOGICAL_ADDRESS, null);
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
        moduleApi.sendCertificateToRecipient(new InternalModelHolder("blaha"), LOGICAL_ADDRESS, null);
    }

    @Test(expected = ModuleException.class)
    public void testSendCertificateShouldFailOnNullLogicalAddress() throws ModuleException {
        moduleApi.sendCertificateToRecipient(new InternalModelHolder("blaha", "blaha"), null, null);
    }

    @Test(expected = ModuleException.class)
    public void testSendCertificateShouldFailOnEmptyLogicalAddress() throws ModuleException {
        moduleApi.sendCertificateToRecipient(new InternalModelHolder("blaha", "blaha"), "", null);
    }

    @Test
    public void testValidateShouldUseValidator() throws Exception {
        when(objectMapper.readValue(eq("internal model"), eq(SjukpenningUtokadUtlatande.class))).thenReturn(null);
        moduleApi.validateDraft(new InternalModelHolder("internal model"));
        verify(internalDraftValidator, times(1)).validateDraft(any());
    }

    @Test
    public void testCreateNewInternal() throws Exception {
        when(webcertModelFactory.createNewWebcertDraft(any())).thenReturn(null);
        doAnswer(a -> ((Writer) a.getArguments()[0]).append("internal model")).when(objectMapper).writeValue(any(Writer.class), anyString());
        moduleApi.createNewInternal(createDraftHolder());
        verify(webcertModelFactory, times(1)).createNewWebcertDraft(any());
    }

    @Test(expected = ModuleException.class)
    public void testCreateNewInternalThrowsModuleException() throws Exception {
        when(webcertModelFactory.createNewWebcertDraft(any())).thenThrow(new ConverterException());
        moduleApi.createNewInternal(createDraftHolder());
        fail();
    }

    @Test
    public void testCreateNewInternalFromTemplate() throws Exception {
        when(webcertModelFactory.createCopy(any(), any())).thenReturn(null);
        when(objectMapper.readValue(eq("internal model"), eq(SjukpenningUtokadUtlatande.class))).thenReturn(null);
        doAnswer(a -> ((Writer) a.getArguments()[0]).append("internal model")).when(objectMapper).writeValue(any(Writer.class), anyString());

        moduleApi.createNewInternalFromTemplate(createCopyHolder(), new InternalModelHolder("internal model"));

        verify(webcertModelFactory, times(1)).createCopy(any(), any());
    }

    @Test(expected = ModuleException.class)
    public void testCreateNewInternalFromTemplateThrowsModuleException() throws Exception {
        when(webcertModelFactory.createCopy(any(), any())).thenThrow(new ConverterException());
        moduleApi.createNewInternalFromTemplate(createCopyHolder(), new InternalModelHolder("internal model"));
        fail();
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
        fail();
    }

    @Test
    public void testDecorateUtlatande() throws Exception {
        final String utlatandeJson = "utlatandeJson";
        final String internalModel = "internal model";

        when(objectMapper.readValue(eq(utlatandeJson), eq(SjukpenningUtokadUtlatande.class)))
                .thenReturn(ScenarioFinder.getInternalScenario("pass-minimal").asInternalModel());
        when(objectMapper.writeValueAsString(any())).thenReturn(internalModel);
        when(moduleService.getDescriptionFromDiagnosKod(any(), any())).thenReturn("description");

        String res = moduleApi.decorateUtlatande(utlatandeJson);

        // Better validation would be nice to have
        assertEquals(internalModel, res);
    }

    @Test
    public void testRegisterCertificate() throws Exception {
        final String logicalAddress = "logicalAddress";
        final String internalModel = "internal model";
        RegisterCertificateResponseType response = new RegisterCertificateResponseType();
        response.setResult(ResultTypeUtil.okResult());

        when(converterUtil.fromJsonString(internalModel)).thenReturn(ScenarioFinder.getInternalScenario("pass-minimal").asInternalModel());
        when(registerCertificateResponderInterface.registerCertificate(eq(logicalAddress), any())).thenReturn(response);

        moduleApi.registerCertificate(new InternalModelHolder(internalModel), logicalAddress);

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

        when(converterUtil.fromJsonString(internalModel)).thenReturn(ScenarioFinder.getInternalScenario("pass-minimal").asInternalModel());
        when(registerCertificateResponderInterface.registerCertificate(eq(logicalAddress), any())).thenReturn(response);

        moduleApi.registerCertificate(new InternalModelHolder(internalModel), logicalAddress);

        fail();
    }

    @Test(expected = ExternalServiceCallException.class)
    public void testRegisterCertificateShouldThrowExceptionOnBadCertificate() throws Exception {
        final String logicalAddress = "logicalAddress";
        final String internalModel = "internal model";
        when(converterUtil.fromJsonString(internalModel)).thenReturn(null);

        moduleApi.registerCertificate(new InternalModelHolder(internalModel), logicalAddress);

        fail();
    }

    @Test
    public void testGetUtlatandeFromJson() throws Exception {
        final String utlatandeJson = "utlatandeJson";
        when(objectMapper.readValue(eq(utlatandeJson), eq(SjukpenningUtokadUtlatande.class)))
                .thenReturn(ScenarioFinder.getInternalScenario("pass-minimal").asInternalModel());
        Utlatande utlatandeFromJson = moduleApi.getUtlatandeFromJson(utlatandeJson);
        assertNotNull(utlatandeFromJson);
    }

    @Test
    public void testMarshall() throws Exception {
        final String jsonString = "internal model";
        when(objectMapper.readValue(eq(jsonString), eq(SjukpenningUtokadUtlatande.class)))
                .thenReturn(ScenarioFinder.getInternalScenario("pass-prognos").asInternalModel());
        String res = moduleApi.marshall(jsonString);
        assertNotNull(res);
    }

    @Test(expected = ModuleException.class)
    public void testMarshallThrowsModuleException() throws Exception {
        final String jsonString = "internal model";
        when(objectMapper.readValue(eq(jsonString), eq(SjukpenningUtokadUtlatande.class))).thenThrow(new IOException());
        moduleApi.marshall(jsonString);
        fail();
    }

    @Test
    public void testUpdateBeforeSave() throws Exception {
        final String internalModel = "internal model";
        when(objectMapper.readValue(anyString(), eq(SjukpenningUtokadUtlatande.class)))
                .thenReturn(ScenarioFinder.getInternalScenario("pass-minimal").asInternalModel());
        doAnswer(a -> ((Writer) a.getArguments()[0]).append(internalModel)).when(objectMapper).writeValue(any(Writer.class), anyString());
        InternalModelResponse response = moduleApi.updateBeforeSave(new InternalModelHolder(internalModel), createHosPersonal());
        assertEquals(internalModel, response.getInternalModel());
    }

    @Test
    public void testUpdateBeforeSigning() throws Exception {
        final String internalModel = "internal model";
        when(objectMapper.readValue(anyString(), eq(SjukpenningUtokadUtlatande.class)))
                .thenReturn(ScenarioFinder.getInternalScenario("pass-minimal").asInternalModel());
        doAnswer(a -> ((Writer) a.getArguments()[0]).append(internalModel)).when(objectMapper).writeValue(any(Writer.class), anyString());
        InternalModelResponse response = moduleApi.updateBeforeSigning(new InternalModelHolder(internalModel), createHosPersonal(), null);
        assertEquals(internalModel, response.getInternalModel());
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

        Utlatande utlatande = SjukpenningUtokadUtlatande.builder().setId(intygId).setGrundData(gd).setTextVersion("").build();

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
        return new CreateNewDraftHolder(
                "certificateId", createHosPersonal(),
                new Patient("fornamn", "mellannamn", "efternamn", new Personnummer("personnummer"), "postadress", "postnummer", "postort"));
    }

    private HoSPersonal createHosPersonal() {
        return new HoSPersonal("hsaId", "namn", "forskrivarkod", "befattning", null, new Vardenhet("hsaId", "namn",
                "postadress", "postnummer", "postort", "telefonnummer", "epost", "arbetsplatskod", new Vardgivare("hsaId", "namn")));
    }

    private RegisterCertificateResponseType createReturnVal(ResultCodeType res) {
        RegisterCertificateResponseType retVal = new RegisterCertificateResponseType();
        ResultType value = new ResultType();
        value.setResultCode(res);
        retVal.setResult(value);
        return retVal;
    }
}