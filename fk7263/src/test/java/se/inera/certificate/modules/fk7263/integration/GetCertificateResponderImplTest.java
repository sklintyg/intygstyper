/**
 * Copyright (C) 2013 Inera AB (http://www.inera.se)
 *
 * This file is part of Inera Certificate (http://code.google.com/p/inera-certificate).
 *
 * Inera Certificate is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Inera Certificate is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.inera.certificate.modules.fk7263.integration;

import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;
import static se.inera.ifv.insuranceprocess.healthreporting.v2.ResultCodeEnum.ERROR;
import static se.inera.ifv.insuranceprocess.healthreporting.v2.ResultCodeEnum.INFO;
import static se.inera.ifv.insuranceprocess.healthreporting.v2.ResultCodeEnum.OK;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.io.ClassPathResource;

import se.inera.intyg.common.util.integration.integration.json.CustomObjectMapper;
import se.inera.intyg.common.support.integration.module.exception.CertificateRevokedException;
import se.inera.intyg.common.support.integration.module.exception.InvalidCertificateException;
import se.inera.intyg.common.support.integration.module.exception.MissingConsentException;
import se.inera.certificate.modules.fk7263.model.converter.util.ConverterUtil;
import se.inera.certificate.modules.fk7263.rest.Fk7263ModuleApi;
import se.inera.intyg.common.support.modules.support.api.CertificateHolder;
import se.inera.intyg.common.support.modules.support.api.ModuleContainerApi;
import se.inera.intyg.common.support.modules.support.api.dto.Personnummer;
import se.inera.ifv.insuranceprocess.healthreporting.getcertificateresponder.v1.GetCertificateRequestType;
import se.inera.ifv.insuranceprocess.healthreporting.getcertificateresponder.v1.GetCertificateResponseType;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificateresponder.v3.ObjectFactory;
import se.inera.ifv.insuranceprocess.healthreporting.v2.ErrorIdEnum;

/**
 * @author andreaskaltenbach
 */
@RunWith(MockitoJUnitRunner.class)
public class GetCertificateResponderImplTest {

    private static final Personnummer civicRegistrationNumber = new Personnummer("19350108-1234");
    private static final String certificateId = "123456";
    
    private CustomObjectMapper objectMapper = new CustomObjectMapper();
    private ConverterUtil converterUtil = new ConverterUtil();
    
    @InjectMocks
    private GetCertificateResponderImpl responder = new GetCertificateResponderImpl();

    @Mock
    private ModuleContainerApi moduleContainer = mock(ModuleContainerApi.class);

    @Mock
    private Fk7263ModuleApi moduleRestApi = mock(Fk7263ModuleApi.class);

    @Mock
    private JAXBContext jaxbContext = mock(JAXBContext.class);

    @Mock
    private ObjectFactory objectFactory = mock(ObjectFactory.class);

    @Mock
    private Marshaller marshaller = mock(Marshaller.class);
    
    @Before
    public void setUpMocks() throws Exception {
        converterUtil.setObjectMapper(objectMapper);
        responder.setConverterUtil(converterUtil);
        when(moduleRestApi.getModuleContainer()).thenReturn(moduleContainer);
        when(jaxbContext.createMarshaller()).thenReturn(marshaller);
    }
    
    @Test
    public void getCertificate() throws Exception {
        String document = FileUtils.readFileToString(new ClassPathResource("GetCertificateResponderImplTest/maximalt-fk7263-internal.json").getFile());
        CertificateHolder certificate = converterUtil.toCertificateHolder(document);

        when(moduleContainer.getCertificate(certificateId, civicRegistrationNumber, true)).thenReturn(certificate);

        GetCertificateRequestType parameters = createGetCertificateRequest(civicRegistrationNumber, certificateId);

        GetCertificateResponseType response = responder.getCertificate(null, parameters);

        verify(moduleContainer).getCertificate(certificateId, civicRegistrationNumber, true);

        assertNotNull(response.getMeta());
        assertEquals(OK, response.getResult().getResultCode());
    }

    @Test
    public void getCertificateWithUnknownCertificateId() throws Exception {

        when(moduleContainer.getCertificate(certificateId, civicRegistrationNumber, true)).thenThrow(new InvalidCertificateException("123456", null));

        GetCertificateRequestType parameters = createGetCertificateRequest(civicRegistrationNumber, certificateId);

        GetCertificateResponseType response = responder.getCertificate(null, parameters);

        assertNull(response.getMeta());
        assertNull(response.getCertificate());
        assertEquals(ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Unknown certificate ID: 123456", response.getResult().getErrorText());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void getCertificateWithoutConsent() throws Exception {

        when(moduleContainer.getCertificate(certificateId, civicRegistrationNumber, true)).thenThrow(MissingConsentException.class);

        GetCertificateRequestType parameters = createGetCertificateRequest(civicRegistrationNumber, certificateId);

        GetCertificateResponseType response = responder.getCertificate(null, parameters);

        assertNull(response.getMeta());
        assertNull(response.getCertificate());
        assertEquals(ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
    }

    @Test
    public void getRevokedCertificate() throws Exception {

        String document = FileUtils.readFileToString(new ClassPathResource("GetCertificateResponderImplTest/maximalt-fk7263-internal.json").getFile());
        CertificateHolder certificate = converterUtil.toCertificateHolder(document);
        certificate.setRevoked(true);

        when(moduleContainer.getCertificate(certificateId, civicRegistrationNumber, true)).thenReturn(certificate);

        GetCertificateRequestType parameters = createGetCertificateRequest(civicRegistrationNumber, certificateId);

        GetCertificateResponseType response = responder.getCertificate(null, parameters);

        assertNull(response.getMeta());
        assertNull(response.getCertificate());
        assertEquals(INFO, response.getResult().getResultCode());
        assertEquals("Certificate '123456' has been revoked", response.getResult().getInfoText());
    }

    @Test
    public void getCertificateWithNullCertificateId() {
        GetCertificateRequestType request = createGetCertificateRequest(new Personnummer(""), null);
        GetCertificateResponseType response = responder.getCertificate(null, request);
        assertEquals(response.getResult().getErrorId(), ErrorIdEnum.VALIDATION_ERROR);
        verifyZeroInteractions(moduleRestApi);
    }

    @Test
    public void getCertificateWithBlankCertificateId() {
        GetCertificateRequestType request = createGetCertificateRequest(new Personnummer(""), "");
        GetCertificateResponseType response = responder.getCertificate(null, request);
        assertEquals(response.getResult().getErrorId(), ErrorIdEnum.VALIDATION_ERROR);
        verifyZeroInteractions(moduleRestApi);
    }

    @Test
    public void getCertificateWithNullIdentityNumber() {
        GetCertificateRequestType request = createGetCertificateRequest(new Personnummer(null), certificateId);
        GetCertificateResponseType response = responder.getCertificate(null, request);
        assertEquals(response.getResult().getErrorId(), ErrorIdEnum.VALIDATION_ERROR);
        verifyZeroInteractions(moduleRestApi);
    }

    @Test
    public void getCertificateWithBlankIdentityNumber() {
        GetCertificateRequestType request = createGetCertificateRequest(new Personnummer(""), certificateId);
        GetCertificateResponseType response = responder.getCertificate(null, request);
        assertEquals(response.getResult().getErrorId(), ErrorIdEnum.VALIDATION_ERROR);
        verifyZeroInteractions(moduleRestApi);
    }

    private GetCertificateRequestType createGetCertificateRequest(Personnummer civicRegistrationNumber, String certificateId) {
        GetCertificateRequestType parameters = new GetCertificateRequestType();
        parameters.setNationalIdentityNumber(civicRegistrationNumber.getPersonnummer());
        parameters.setCertificateId(certificateId);
        return parameters;
    }
}
