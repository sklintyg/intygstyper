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

package se.inera.intyg.intygstyper.ts_diabetes.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import se.inera.intyg.common.support.integration.module.exception.CertificateAlreadyExistsException;
import se.inera.intyg.common.support.integration.module.exception.InvalidCertificateException;
import se.inera.intyg.common.support.modules.support.api.CertificateHolder;
import se.inera.intyg.common.support.modules.support.api.ModuleContainerApi;
import se.inera.intyg.intygstyper.ts_diabetes.utils.ScenarioFinder;
import se.inera.intyg.intygstyper.ts_diabetes.validator.transport.TransportValidatorInstance;
import se.inera.intygstjanster.ts.services.RegisterTSDiabetesResponder.v1.RegisterTSDiabetesResponseType;
import se.inera.intygstjanster.ts.services.RegisterTSDiabetesResponder.v1.RegisterTSDiabetesType;
import se.inera.intygstjanster.ts.services.v1.*;

@RunWith(MockitoJUnitRunner.class)
public class RegisterTSDiabetesResponderImplTest {

    private static final String LOGICAL_ADDRESS = "logicalAddress";

    @Mock
    private ModuleContainerApi moduleContainer;

    @InjectMocks
    private RegisterTSDiabetesResponderImpl responder;

    @Test
    public void testRegisterTSDiabetes() throws Exception {
        RegisterTSDiabetesType request = ScenarioFinder.getTransportScenario("valid-minimal").asTransportModel();
        RegisterTSDiabetesResponseType res = responder.registerTSDiabetes(LOGICAL_ADDRESS, request);

        assertNotNull(res);
        assertEquals(ResultCodeType.OK, res.getResultat().getResultCode());

        ArgumentCaptor<CertificateHolder> certificateHolderCaptor = ArgumentCaptor.forClass(CertificateHolder.class);
        verify(moduleContainer).certificateReceived(certificateHolderCaptor.capture());

        assertNotNull(certificateHolderCaptor.getValue());
        assertNotNull(certificateHolderCaptor.getValue().getOriginalCertificate());
    }

    @Test
    public void testRegisterTSDiabetesCertificateAlreadyExists() throws Exception {
        RegisterTSDiabetesType request = ScenarioFinder.getTransportScenario("valid-minimal").asTransportModel();
        doThrow(new CertificateAlreadyExistsException("intygId")).when(moduleContainer).certificateReceived(any(CertificateHolder.class));
        RegisterTSDiabetesResponseType res = responder.registerTSDiabetes(LOGICAL_ADDRESS, request);

        assertNotNull(res);
        assertEquals(ResultCodeType.INFO, res.getResultat().getResultCode());
        assertEquals("Certificate already exists", res.getResultat().getResultText());

        verify(moduleContainer).certificateReceived(any(CertificateHolder.class));
    }

    @Test
    public void testRegisterTSDiabetesInvalidCertificate() throws Exception {
        RegisterTSDiabetesType request = ScenarioFinder.getTransportScenario("valid-minimal").asTransportModel();
        doThrow(new InvalidCertificateException("intygId", null)).when(moduleContainer).certificateReceived(any(CertificateHolder.class));
        RegisterTSDiabetesResponseType res = responder.registerTSDiabetes(LOGICAL_ADDRESS, request);

        assertNotNull(res);
        assertEquals(ResultCodeType.ERROR, res.getResultat().getResultCode());
        assertEquals(ErrorIdType.APPLICATION_ERROR, res.getResultat().getErrorId());
        assertEquals("Invalid certificate ID", res.getResultat().getResultText());

        verify(moduleContainer).certificateReceived(any(CertificateHolder.class));
    }

    @Test
    public void testRegisterTSDiabetesValidationError() throws Exception {
        TransportValidatorInstance validatorMock = mock(TransportValidatorInstance.class);
        when(validatorMock.validate(any(TSDiabetesIntyg.class))).thenReturn(Arrays.asList("validationerror1"));
        ReflectionTestUtils.setField(responder, "validator", validatorMock);

        RegisterTSDiabetesType request = ScenarioFinder.getTransportScenario("valid-minimal").asTransportModel();
        RegisterTSDiabetesResponseType res = responder.registerTSDiabetes(LOGICAL_ADDRESS, request);

        assertNotNull(res);
        assertEquals(ResultCodeType.ERROR, res.getResultat().getResultCode());
        assertEquals(ErrorIdType.VALIDATION_ERROR, res.getResultat().getErrorId());
        assertTrue(res.getResultat().getResultText().startsWith("Validation Error(s) found:"));

        verifyZeroInteractions(moduleContainer);
    }

    @Test(expected = RuntimeException.class)
    public void testRegisterTSDiabetesJAXBException() throws Exception {
        JAXBContext jaxbContextMock = mock(JAXBContext.class);
        when(jaxbContextMock.createMarshaller()).thenThrow(new JAXBException(""));
        ReflectionTestUtils.setField(responder, "jaxbContext", jaxbContextMock);
        RegisterTSDiabetesType request = ScenarioFinder.getTransportScenario("valid-minimal").asTransportModel();

        responder.registerTSDiabetes(LOGICAL_ADDRESS, request);
    }

    @Test(expected = RuntimeException.class)
    public void testRegisterTSDiabetesException() throws Exception {
        JAXBContext jaxbContextMock = mock(JAXBContext.class);
        when(jaxbContextMock.createMarshaller()).thenThrow(new IllegalArgumentException());
        ReflectionTestUtils.setField(responder, "jaxbContext", jaxbContextMock);
        RegisterTSDiabetesType request = ScenarioFinder.getTransportScenario("valid-minimal").asTransportModel();

        responder.registerTSDiabetes(LOGICAL_ADDRESS, request);
    }
}
