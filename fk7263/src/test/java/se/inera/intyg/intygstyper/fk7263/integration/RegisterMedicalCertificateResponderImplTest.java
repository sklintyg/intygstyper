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
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.xml.bind.*;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.io.ClassPathResource;

import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificateresponder.v3.RegisterMedicalCertificateResponseType;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificateresponder.v3.RegisterMedicalCertificateType;
import se.inera.ifv.insuranceprocess.healthreporting.v2.ResultCodeEnum;
import se.inera.intyg.common.support.integration.module.exception.CertificateAlreadyExistsException;
import se.inera.intyg.common.support.modules.support.ModuleEntryPoint;
import se.inera.intyg.common.support.modules.support.api.CertificateHolder;
import se.inera.intyg.common.support.modules.support.api.ModuleContainerApi;
import se.inera.intyg.common.util.integration.integration.json.CustomObjectMapper;
import se.inera.intyg.intygstyper.fk7263.model.converter.TransportToInternal;
import se.inera.intyg.intygstyper.fk7263.model.converter.util.ConverterUtil;
import se.inera.intyg.intygstyper.fk7263.model.internal.Utlatande;
import se.inera.intyg.intygstyper.fk7263.rest.Fk7263ModuleApi;

@RunWith(MockitoJUnitRunner.class)
public class RegisterMedicalCertificateResponderImplTest {

    private CustomObjectMapper objectMapper = new CustomObjectMapper();

    @Mock
    private ModuleEntryPoint moduleEntryPoint = mock(ModuleEntryPoint.class);

    @Mock
    private Fk7263ModuleApi moduleRestApi = mock(Fk7263ModuleApi.class);

    @Mock
    private ModuleContainerApi moduleContainer = mock(ModuleContainerApi.class);

    private RegisterMedicalCertificateType request;
    private String xml;
    private Utlatande utlatande;
    private CertificateHolder certificateHolder;


    @InjectMocks
    private RegisterMedicalCertificateResponderImpl responder = new RegisterMedicalCertificateResponderImpl();

    @Before
    public void initializeResponder() throws JAXBException {
        responder.initializeJaxbContext();
    }

    @Before
    public void prepareRequest() throws Exception {

        ClassPathResource file = new ClassPathResource(
                "RegisterMedicalCertificateResponderImplTest/fk7263.xml");

        JAXBContext context = JAXBContext.newInstance(RegisterMedicalCertificateType.class);
        JAXBElement<RegisterMedicalCertificateType> registerMedicalCertificate = context.createUnmarshaller().unmarshal(
                new StreamSource(file.getInputStream()), RegisterMedicalCertificateType.class);
        request = registerMedicalCertificate.getValue();

        xml = FileUtils.readFileToString(file.getFile());
        utlatande = TransportToInternal.convert(request.getLakarutlatande());
        certificateHolder = ConverterUtil.toCertificateHolder(utlatande);
        certificateHolder.setOriginalCertificate(xml);
        when(moduleRestApi.getModuleContainer()).thenReturn(moduleContainer);
    }

    @Test
    public void testReceiveCertificate() throws Exception {

        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.OK, response.getResult().getResultCode());

        Mockito.verify(moduleContainer, Mockito.only()).certificateReceived(any(CertificateHolder.class));
    }

    @Test
    public void testReceiveCertificateWiretapped() throws Exception {

        responder.setWireTapped(true);
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.OK, response.getResult().getResultCode());
        ArgumentCaptor<CertificateHolder> certificateHolderCaptor = ArgumentCaptor.forClass(CertificateHolder.class);
        Mockito.verify(moduleContainer, Mockito.only()).certificateReceived(certificateHolderCaptor.capture());
        assertTrue(certificateHolderCaptor.getValue().isWireTapped());
    }

    @Test
    public void testWithExistingCertificate() throws Exception {
        Mockito.doThrow(new CertificateAlreadyExistsException(request.getLakarutlatande().getLakarutlatandeId())).when(moduleContainer).certificateReceived(any(CertificateHolder.class));

        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);
        assertEquals(ResultCodeEnum.INFO, response.getResult().getResultCode());
    }

    @Test
    public void testWithInvalidCertificate() throws Exception {
        request.getLakarutlatande().setSkapadAvHosPersonal(null);
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);
        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
    }
}
