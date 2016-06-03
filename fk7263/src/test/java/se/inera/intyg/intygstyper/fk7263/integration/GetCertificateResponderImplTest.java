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
package se.inera.intyg.intygstyper.fk7263.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static se.inera.ifv.insuranceprocess.healthreporting.v2.ResultCodeEnum.ERROR;
import static se.inera.ifv.insuranceprocess.healthreporting.v2.ResultCodeEnum.INFO;
import static se.inera.ifv.insuranceprocess.healthreporting.v2.ResultCodeEnum.OK;

import java.io.File;

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

import se.inera.ifv.insuranceprocess.healthreporting.getcertificateresponder.v1.GetCertificateRequestType;
import se.inera.ifv.insuranceprocess.healthreporting.getcertificateresponder.v1.GetCertificateResponseType;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificateresponder.v3.ObjectFactory;
import se.inera.ifv.insuranceprocess.healthreporting.v2.ErrorIdEnum;
import se.inera.intyg.common.support.integration.module.exception.InvalidCertificateException;
import se.inera.intyg.common.support.integration.module.exception.MissingConsentException;
import se.inera.intyg.common.support.modules.support.api.CertificateHolder;
import se.inera.intyg.common.support.modules.support.api.ModuleContainerApi;
import se.inera.intyg.common.support.modules.support.api.dto.Personnummer;
import se.inera.intyg.common.util.integration.integration.json.CustomObjectMapper;
import se.inera.intyg.intygstyper.fk7263.model.converter.util.ConverterUtil;
import se.inera.intyg.intygstyper.fk7263.model.internal.Utlatande;
import se.inera.intyg.intygstyper.fk7263.rest.Fk7263ModuleApi;

/**
 * @author andreaskaltenbach
 */
@RunWith(MockitoJUnitRunner.class)
public class GetCertificateResponderImplTest {

    private static final Personnummer civicRegistrationNumber = new Personnummer("19350108-1234");
    private static final String certificateId = "123456";

    private CustomObjectMapper objectMapper = new CustomObjectMapper();

    @InjectMocks
    private GetCertificateResponderImpl responder;

    @Mock
    private ModuleContainerApi moduleContainer;

    @Mock
    private Fk7263ModuleApi moduleRestApi;

    @Mock
    private JAXBContext jaxbContext;

    @Mock
    private ObjectFactory objectFactory;

    @Mock
    private Marshaller marshaller;

    @Before
    public void setUpMocks() throws Exception {
        when(jaxbContext.createMarshaller()).thenReturn(marshaller);
    }

    @Test
    public void getCertificate() throws Exception {
        String document = FileUtils.readFileToString(new ClassPathResource("GetCertificateResponderImplTest/maximalt-fk7263-internal.json").getFile());
        Utlatande utlatande = objectMapper.readValue(document, Utlatande.class);
        CertificateHolder certificate = ConverterUtil.toCertificateHolder(utlatande);
        File file = new ClassPathResource("GetCertificateResponderImplTest/fk7263.xml").getFile();
        String xmlFile = FileUtils.readFileToString(file);
        certificate.setOriginalCertificate(xmlFile);

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
        Utlatande utlatande = objectMapper.readValue(document, Utlatande.class);
        CertificateHolder certificate = ConverterUtil.toCertificateHolder(utlatande);
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
