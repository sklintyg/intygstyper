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
import static org.mockito.Mockito.when;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.io.ClassPathResource;

import se.inera.intyg.clinicalprocess.healthcond.certificate.getmedicalcertificateforcare.v1.GetMedicalCertificateForCareRequestType;
import se.inera.intyg.clinicalprocess.healthcond.certificate.getmedicalcertificateforcare.v1.GetMedicalCertificateForCareResponseType;
import se.inera.intyg.common.support.integration.module.exception.InvalidCertificateException;
import se.inera.intyg.common.support.modules.support.api.CertificateHolder;
import se.inera.intyg.common.support.modules.support.api.ModuleContainerApi;
import se.inera.intyg.common.support.modules.support.api.dto.Personnummer;
import se.inera.intyg.intygstyper.fk7263.support.Fk7263EntryPoint;
import se.riv.clinicalprocess.healthcond.certificate.v1.ErrorIdType;
import se.riv.clinicalprocess.healthcond.certificate.v1.ResultCodeType;

@RunWith(MockitoJUnitRunner.class)
public class GetMedicalCertificateForCareResponderImplTest {

    private static final Personnummer PERSON_ID = new Personnummer("19121212-1212");
    private static final String INTYG_ID = "123456";

    @InjectMocks
    private GetMedicalCertificateForCareResponderImpl responder;

    @Mock
    private ModuleContainerApi moduleContainer;

    @Test
    public void getMedicalCertificateForCare()  throws Exception {
        when(moduleContainer.getCertificate(INTYG_ID, PERSON_ID, false)).thenReturn(createCertificateHolder());

        GetMedicalCertificateForCareResponseType response = responder.getMedicalCertificateForCare(null, createGetMedicalCertificateForCareRequest());

        verify(moduleContainer).getCertificate(INTYG_ID, PERSON_ID, false);
        assertEquals(ResultCodeType.OK, response.getResult().getResultCode());
        assertNotNull(response.getMeta());
        assertEquals(INTYG_ID, response.getMeta().getCertificateId());
        assertEquals(Fk7263EntryPoint.MODULE_ID, response.getMeta().getCertificateType());
        assertNotNull(response.getLakarutlatande());
        assertEquals(INTYG_ID, response.getLakarutlatande().getLakarutlatandeId());
        assertEquals(PERSON_ID.getPersonnummer(), response.getLakarutlatande().getPatient().getPersonId().getExtension());
    }

    @Test
    public void getMedicalCertificateForCareWithUnknownCertificateId() throws Exception {
        when(moduleContainer.getCertificate(INTYG_ID, PERSON_ID, false)).thenThrow(new InvalidCertificateException("123456", PERSON_ID));

        GetMedicalCertificateForCareResponseType response = responder.getMedicalCertificateForCare(null, createGetMedicalCertificateForCareRequest());

        assertEquals(ResultCodeType.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdType.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Certificate '123456' does not exist for user '9a8b138a666f84da32e9383b49a15f46f6e08d2c492352aa0dfcc3f993773b0d'", response.getResult().getResultText());
        assertNull(response.getMeta());
        assertNull(response.getLakarutlatande());
    }

    @Test
    public void getMedicalCertificateForCareWrongType() throws Exception {
        CertificateHolder certificateHolder = new CertificateHolder();
        certificateHolder.setType("luse");
        when(moduleContainer.getCertificate(INTYG_ID, PERSON_ID, false)).thenReturn(certificateHolder);

        GetMedicalCertificateForCareResponseType response = responder.getMedicalCertificateForCare(null, createGetMedicalCertificateForCareRequest());

        assertEquals(ResultCodeType.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdType.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Certificate '123456' does not exist for user '9a8b138a666f84da32e9383b49a15f46f6e08d2c492352aa0dfcc3f993773b0d'", response.getResult().getResultText());
        assertNull(response.getMeta());
        assertNull(response.getLakarutlatande());
    }

    @Test
    public void getMedicalCertificateForCareWrongCivicRegistrationNumber() throws Exception {
        CertificateHolder certificateHolder = new CertificateHolder();
        certificateHolder.setType("fk7263");
        certificateHolder.setCivicRegistrationNumber(new Personnummer("19010101-0101"));
        when(moduleContainer.getCertificate(INTYG_ID, PERSON_ID, false)).thenReturn(certificateHolder);

        GetMedicalCertificateForCareResponseType response = responder.getMedicalCertificateForCare(null, createGetMedicalCertificateForCareRequest());

        assertEquals(ResultCodeType.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdType.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("nationalIdentityNumber mismatch", response.getResult().getResultText());
        assertNull(response.getMeta());
        assertNull(response.getLakarutlatande());
    }

    @Test
    public void getMedicalCertificateForCareDeletedByCareGiver() throws Exception {
        CertificateHolder certificateHolder = createCertificateHolder();
        certificateHolder.setDeletedByCareGiver(true);
        when(moduleContainer.getCertificate(INTYG_ID, PERSON_ID, false)).thenReturn(certificateHolder);

        GetMedicalCertificateForCareResponseType response = responder.getMedicalCertificateForCare(null, createGetMedicalCertificateForCareRequest());

        assertEquals(ResultCodeType.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdType.APPLICATION_ERROR, response.getResult().getErrorId());
        assertEquals("Certificate '123456' has been deleted by care giver", response.getResult().getResultText());
        assertNull(response.getMeta());
        assertNull(response.getLakarutlatande());
    }

    @Test
    public void getMedicalCertificateForCareRevoked()  throws Exception {
        CertificateHolder certificate = createCertificateHolder();
        certificate.setRevoked(true);
        when(moduleContainer.getCertificate(INTYG_ID, PERSON_ID, false)).thenReturn(certificate);

        GetMedicalCertificateForCareResponseType response = responder.getMedicalCertificateForCare(null, createGetMedicalCertificateForCareRequest());

        verify(moduleContainer).getCertificate(INTYG_ID, PERSON_ID, false);
        assertEquals(ResultCodeType.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdType.REVOKED, response.getResult().getErrorId());
        assertEquals("Certificate '123456' has been revoked", response.getResult().getResultText());
        assertNotNull(response.getMeta());
        assertEquals(INTYG_ID, response.getMeta().getCertificateId());
        assertEquals(Fk7263EntryPoint.MODULE_ID, response.getMeta().getCertificateType());
        assertNotNull(response.getLakarutlatande());
        assertEquals(INTYG_ID, response.getLakarutlatande().getLakarutlatandeId());
        assertEquals(PERSON_ID.getPersonnummer(), response.getLakarutlatande().getPatient().getPersonId().getExtension());
    }

    @Test
    public void getMedicalCertificateForCareNoCivicRegistrationNumber()  throws Exception {
        when(moduleContainer.getCertificate(INTYG_ID, null, false)).thenReturn(createCertificateHolder());

        GetMedicalCertificateForCareRequestType request = createGetMedicalCertificateForCareRequest();
        request.setNationalIdentityNumber(null);
        GetMedicalCertificateForCareResponseType response = responder.getMedicalCertificateForCare(null, request);

        verify(moduleContainer).getCertificate(INTYG_ID, null, false);
        assertEquals(ResultCodeType.OK, response.getResult().getResultCode());
        assertNotNull(response.getMeta());
        assertEquals(INTYG_ID, response.getMeta().getCertificateId());
        assertEquals(Fk7263EntryPoint.MODULE_ID, response.getMeta().getCertificateType());
        assertNotNull(response.getLakarutlatande());
        assertEquals(INTYG_ID, response.getLakarutlatande().getLakarutlatandeId());
        assertEquals(PERSON_ID.getPersonnummer(), response.getLakarutlatande().getPatient().getPersonId().getExtension());
    }

    private CertificateHolder createCertificateHolder() throws Exception {
        CertificateHolder certificate = new CertificateHolder();
        certificate.setId(INTYG_ID);
        certificate.setType("fk7263");
        certificate.setCivicRegistrationNumber(PERSON_ID);
        File file = new ClassPathResource("GetMedicalCertificateForCareResponderImplTest/fk7263.xml").getFile();
        String xmlFile = FileUtils.readFileToString(file);
        certificate.setOriginalCertificate(xmlFile);
        return certificate;
    }

    private GetMedicalCertificateForCareRequestType createGetMedicalCertificateForCareRequest() {
        GetMedicalCertificateForCareRequestType parameters = new GetMedicalCertificateForCareRequestType();
        parameters.setCertificateId(INTYG_ID);
        parameters.setNationalIdentityNumber(PERSON_ID.getPersonnummer());
        return parameters;
    }
}
