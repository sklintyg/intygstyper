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
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;

import java.time.LocalDate;
import java.util.Optional;

import javax.xml.bind.*;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.io.ClassPathResource;

import iso.v21090.dt.v1.CD;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.*;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificateresponder.v3.RegisterMedicalCertificateResponseType;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificateresponder.v3.RegisterMedicalCertificateType;
import se.inera.ifv.insuranceprocess.healthreporting.v2.ErrorIdEnum;
import se.inera.ifv.insuranceprocess.healthreporting.v2.ResultCodeEnum;
import se.inera.intyg.common.support.integration.module.exception.CertificateAlreadyExistsException;
import se.inera.intyg.common.support.modules.support.ModuleEntryPoint;
import se.inera.intyg.common.support.modules.support.api.CertificateHolder;
import se.inera.intyg.common.support.modules.support.api.ModuleContainerApi;
import se.inera.intyg.intygstyper.fk7263.model.converter.TransportToInternal;
import se.inera.intyg.intygstyper.fk7263.model.converter.util.ConverterUtil;
import se.inera.intyg.intygstyper.fk7263.model.internal.Utlatande;
import se.inera.intyg.intygstyper.fk7263.rest.Fk7263ModuleApi;

@RunWith(MockitoJUnitRunner.class)
public class RegisterMedicalCertificateResponderImplTest {

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
    }

    @Test
    public void testReceiveCertificate() throws Exception {

        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.OK, response.getResult().getResultCode());

        Mockito.verify(moduleContainer, Mockito.only()).certificateReceived(any(CertificateHolder.class));
    }

    @Test
    public void testRegisterMedicalCertificateTomtTypAvUtlatande() throws Exception {
        request.getLakarutlatande().setTypAvUtlatande("");
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.OK, response.getResult().getResultCode());

        Mockito.verify(moduleContainer, Mockito.only()).certificateReceived(any(CertificateHolder.class));
    }

    @Test
    public void testRegisterMedicalCertificateSaknarTypAvUtlatande() throws Exception {
        request.getLakarutlatande().setTypAvUtlatande(null);
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.OK, response.getResult().getResultCode());

        Mockito.verify(moduleContainer, Mockito.only()).certificateReceived(any(CertificateHolder.class));
    }

    @Test
    public void testRegisterMedicalCertificateGodtyckligtTypAvUtlatande() throws Exception {
        request.getLakarutlatande().setTypAvUtlatande("godtycklig string");
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.OK, response.getResult().getResultCode());

        Mockito.verify(moduleContainer, Mockito.only()).certificateReceived(any(CertificateHolder.class));
    }

    @Test
    public void testRegisterMedicalCertificateUtanAktivitetsbegransningFalt5() throws Exception {
        request.getLakarutlatande().getAktivitet().clear();
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.OK, response.getResult().getResultCode());

        Mockito.verify(moduleContainer, Mockito.only()).certificateReceived(any(CertificateHolder.class));
    }

    @Test
    public void testRegisterMedicalCertificateUtanPrognosangivelseFalt10() throws Exception {
        request.getLakarutlatande().getFunktionstillstand().stream().filter(ft -> ft.getArbetsformaga() != null)
                .forEach(ft -> ft.getArbetsformaga().setPrognosangivelse(null));
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.OK, response.getResult().getResultCode());

        Mockito.verify(moduleContainer, Mockito.only()).certificateReceived(any(CertificateHolder.class));
    }

    @Test
    public void testRegisterMedicalCertificateSaknadNedsattningsgrad() throws Exception {
        request.getLakarutlatande().getFunktionstillstand().stream()
                .filter(ft -> ft.getArbetsformaga() != null && !ft.getArbetsformaga().getArbetsformagaNedsattning().isEmpty())
                .forEach(ft -> ft.getArbetsformaga().getArbetsformagaNedsattning().stream().forEach(n -> n.setNedsattningsgrad(null)));
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Validation Error(s) found: Validation Error:No arbetsformaganedsattning element found 8b!.",
                response.getResult().getErrorText());

        Mockito.verifyZeroInteractions(moduleContainer);
    }

    @Test
    public void testRegisterMedicalCertificateSaknatSigneringsdatum() throws Exception {
        request.getLakarutlatande().setSigneringsdatum(null);
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Validation Error(s) found: Validation Error:Field 14: No signeringsDatum found!", response.getResult().getErrorText());

        Mockito.verifyZeroInteractions(moduleContainer);
    }

    @Test
    public void testRegisterMedicalCertificateSaknatSkickatDatum() throws Exception {
        request.getLakarutlatande().setSkickatDatum(null);
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Validation Error(s) found: Validation Error:Header: No or wrong skickatDatum found!", response.getResult().getErrorText());

        Mockito.verifyZeroInteractions(moduleContainer);
    }

    @Test
    public void testRegisterMedicalCertificateSaknatPersonId() throws Exception {
        request.getLakarutlatande().getPatient().setPersonId(null);
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Validation Error(s) found: Validation Error:No Patient Id found!", response.getResult().getErrorText());

        Mockito.verifyZeroInteractions(moduleContainer);
    }

    @Test
    public void testRegisterMedicalCertificateTomtPersonId() throws Exception {
        request.getLakarutlatande().getPatient().getPersonId().setExtension("");
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Validation Error(s) found: Validation Error:No Patient Id found!", response.getResult().getErrorText());

        Mockito.verifyZeroInteractions(moduleContainer);
    }

    @Test
    public void testRegisterMedicalCertificateSaknatReferensdatum() throws Exception {
        request.getLakarutlatande().getReferens().stream().forEach(r -> r.setDatum(null));
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Validation Error(s) found: Validation Error:Field 4: Referens is missing datum\n" +
                "Validation Error:Field 4: Referens is missing datum\n" +
                "Validation Error:No or wrong date for referens - journal found!\n" +
                "Validation Error:No or wrong date for referens - annat found!", response.getResult().getErrorText());

        Mockito.verifyZeroInteractions(moduleContainer);
    }

    @Test
    public void testRegisterMedicalCertificateSaknatReferensdatumSmL() throws Exception {
        request.getLakarutlatande().getAktivitet().clear();
        request.getLakarutlatande().getAktivitet().add(new AktivitetType());
        request.getLakarutlatande().getAktivitet().get(0).setAktivitetskod(Aktivitetskod.AVSTANGNING_ENLIGT_SM_L_PGA_SMITTA);
        request.getLakarutlatande().getReferens().stream().forEach(r -> r.setDatum(null));
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Validation Error(s) found: Validation Error:Field 4: Referens is missing datum\n" +
                "Validation Error:Field 4: Referens is missing datum", response.getResult().getErrorText());

        Mockito.verifyZeroInteractions(moduleContainer);
    }

    @Test
    public void testRegisterMedicalCertificateSaknatIntygId() throws Exception {
        request.getLakarutlatande().setLakarutlatandeId(null);
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Validation Error(s) found: Validation Error:Head: Utlatande Id is mandatory!", response.getResult().getErrorText());

        Mockito.verifyZeroInteractions(moduleContainer);
    }

    @Test
    public void testRegisterMedicalCertificateSaknadPatient() throws Exception {
        request.getLakarutlatande().setPatient(null);
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Validation Error(s) found: Validation Error:No Patient element found!", response.getResult().getErrorText());

        Mockito.verifyZeroInteractions(moduleContainer);
    }

    @Test
    public void testRegisterMedicalCertificateFelaktigPersonIdKod() throws Exception {
        request.getLakarutlatande().getPatient().getPersonId().setRoot("invalid");
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Validation Error(s) found: Validation Error:Wrong o.i.d. for Patient Id! Should be 1.2.752.129.2.1.3.1 or 1.2.752.129.2.1.3.3",
                response.getResult().getErrorText());

        Mockito.verifyZeroInteractions(moduleContainer);
    }

    @Test
    public void testRegisterMedicalCertificateFelaktigtPersonnr() throws Exception {
        request.getLakarutlatande().getPatient().getPersonId().setExtension("invalid");
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Validation Error(s) found: Validation Error:Wrong format for person-id! Valid format is YYYYMMDD-XXXX or YYYYMMDD+XXXX.",
                response.getResult().getErrorText());

        Mockito.verifyZeroInteractions(moduleContainer);
    }

    @Test
    public void testRegisterMedicalCertificatePersonnrUtanSekelsiffror() throws Exception {
        request.getLakarutlatande().getPatient().getPersonId().setExtension("121212-1212");
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Validation Error(s) found: Validation Error:Wrong format for person-id! Valid format is YYYYMMDD-XXXX or YYYYMMDD+XXXX.",
                response.getResult().getErrorText());

        Mockito.verifyZeroInteractions(moduleContainer);
    }

    @Test
    public void testRegisterMedicalCertificatePersonnrUtanBindestreckKorrigeras() throws Exception {
        request.getLakarutlatande().getPatient().getPersonId().setExtension("191212121212");
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.OK, response.getResult().getResultCode());

        ArgumentCaptor<CertificateHolder> certificateCaptor = ArgumentCaptor.forClass(CertificateHolder.class);
        Mockito.verify(moduleContainer, Mockito.only()).certificateReceived(certificateCaptor.capture());
        assertEquals("19121212-1212", certificateCaptor.getValue().getCivicRegistrationNumber().getPersonnummer());
    }

    @Test
    public void testRegisterMedicalCertificateSaknatPatientnamn() throws Exception {
        request.getLakarutlatande().getPatient().setFullstandigtNamn(null);
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Validation Error(s) found: Validation Error:No Patient fullstandigtNamn elements found or set!",
                response.getResult().getErrorText());

        Mockito.verifyZeroInteractions(moduleContainer);
    }

    @Test
    public void testRegisterMedicalCertificateSaknadHoSPersonal() throws Exception {
        request.getLakarutlatande().setSkapadAvHosPersonal(null);
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Validation Error(s) found: Validation Error:No SkapadAvHosPersonal element found!", response.getResult().getErrorText());

        Mockito.verifyZeroInteractions(moduleContainer);
    }

    @Test
    public void testRegisterMedicalCertificateSaknadPersonalId() throws Exception {
        request.getLakarutlatande().getSkapadAvHosPersonal().setPersonalId(null);
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Validation Error(s) found: Validation Error:No personal-id found!\n" +
                "Validation Error:Wrong o.i.d. for personalId! Should be 1.2.752.129.2.1.4.1", response.getResult().getErrorText());

        Mockito.verifyZeroInteractions(moduleContainer);
    }

    @Test
    public void testRegisterMedicalCertificateFelaktigPersonalIdKod() throws Exception {
        request.getLakarutlatande().getSkapadAvHosPersonal().getPersonalId().setRoot("invalid");
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Validation Error(s) found: Validation Error:Wrong o.i.d. for personalId! Should be 1.2.752.129.2.1.4.1",
                response.getResult().getErrorText());

        Mockito.verifyZeroInteractions(moduleContainer);
    }

    @Test
    public void testRegisterMedicalCertificateTomtPersonalId() throws Exception {
        request.getLakarutlatande().getSkapadAvHosPersonal().getPersonalId().setExtension("");
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Validation Error(s) found: Validation Error:No personal-id found!", response.getResult().getErrorText());

        Mockito.verifyZeroInteractions(moduleContainer);
    }

    @Test
    public void testRegisterMedicalCertificateSaknatPersonalnamn() throws Exception {
        request.getLakarutlatande().getSkapadAvHosPersonal().setFullstandigtNamn(null);
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Validation Error(s) found: Validation Error:No skapadAvHosPersonal fullstandigtNamn found.",
                response.getResult().getErrorText());

        Mockito.verifyZeroInteractions(moduleContainer);
    }

    @Test
    public void testRegisterMedicalCertificateTomtPersonalnamn() throws Exception {
        request.getLakarutlatande().getSkapadAvHosPersonal().setFullstandigtNamn("");
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Validation Error(s) found: Validation Error:No skapadAvHosPersonal fullstandigtNamn found.",
                response.getResult().getErrorText());

        Mockito.verifyZeroInteractions(moduleContainer);
    }

    @Test
    public void testRegisterMedicalCertificateSaknadEnhet() throws Exception {
        request.getLakarutlatande().getSkapadAvHosPersonal().setEnhet(null);
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Validation Error(s) found: Validation Error:No enhet element found!", response.getResult().getErrorText());

        Mockito.verifyZeroInteractions(moduleContainer);
    }

    @Test
    public void testRegisterMedicalCertificateSaknatEnhetId() throws Exception {
        request.getLakarutlatande().getSkapadAvHosPersonal().getEnhet().setEnhetsId(null);
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Validation Error(s) found: Validation Error:No enhets-id found!\n" +
                "Validation Error:Wrong o.i.d. for enhetsId! Should be 1.2.752.129.2.1.4.1", response.getResult().getErrorText());

        Mockito.verifyZeroInteractions(moduleContainer);
    }

    @Test
    public void testRegisterMedicalCertificateFelaktigEnhetIdKod() throws Exception {
        request.getLakarutlatande().getSkapadAvHosPersonal().getEnhet().getEnhetsId().setRoot("invalid");
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Validation Error(s) found: Validation Error:Wrong o.i.d. for enhetsId! Should be 1.2.752.129.2.1.4.1",
                response.getResult().getErrorText());

        Mockito.verifyZeroInteractions(moduleContainer);
    }

    @Test
    public void testRegisterMedicalCertificateTomtEnhetId() throws Exception {
        request.getLakarutlatande().getSkapadAvHosPersonal().getEnhet().getEnhetsId().setExtension("");
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Validation Error(s) found: Validation Error:No enhets-id found!", response.getResult().getErrorText());

        Mockito.verifyZeroInteractions(moduleContainer);
    }

    @Test
    public void testRegisterMedicalCertificateSaknatEnhetnamn() throws Exception {
        request.getLakarutlatande().getSkapadAvHosPersonal().getEnhet().setEnhetsnamn(null);
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Validation Error(s) found: Validation Error:No enhetsnamn found!", response.getResult().getErrorText());

        Mockito.verifyZeroInteractions(moduleContainer);
    }

    @Test
    public void testRegisterMedicalCertificateTomtEnhetnamn() throws Exception {
        request.getLakarutlatande().getSkapadAvHosPersonal().getEnhet().setEnhetsnamn("");
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Validation Error(s) found: Validation Error:No enhetsnamn found!", response.getResult().getErrorText());

        Mockito.verifyZeroInteractions(moduleContainer);
    }

    @Test
    public void testRegisterMedicalCertificateSaknadEnhetpostaddress() throws Exception {
        request.getLakarutlatande().getSkapadAvHosPersonal().getEnhet().setPostadress(null);
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Validation Error(s) found: Validation Error:No postadress found for enhet!", response.getResult().getErrorText());

        Mockito.verifyZeroInteractions(moduleContainer);
    }

    @Test
    public void testRegisterMedicalCertificateTomEnhetpostaddress() throws Exception {
        request.getLakarutlatande().getSkapadAvHosPersonal().getEnhet().setPostadress("");
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Validation Error(s) found: Validation Error:No postadress found for enhet!", response.getResult().getErrorText());

        Mockito.verifyZeroInteractions(moduleContainer);
    }

    @Test
    public void testRegisterMedicalCertificateSaknatEnhetpostnummer() throws Exception {
        request.getLakarutlatande().getSkapadAvHosPersonal().getEnhet().setPostnummer(null);
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Validation Error(s) found: Validation Error:No postnummer found for enhet!", response.getResult().getErrorText());

        Mockito.verifyZeroInteractions(moduleContainer);
    }

    @Test
    public void testRegisterMedicalCertificateTomtEnhetpostnummer() throws Exception {
        request.getLakarutlatande().getSkapadAvHosPersonal().getEnhet().setPostnummer("");
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Validation Error(s) found: Validation Error:No postnummer found for enhet!", response.getResult().getErrorText());

        Mockito.verifyZeroInteractions(moduleContainer);
    }

    @Test
    public void testRegisterMedicalCertificateSaknatEnhetpostort() throws Exception {
        request.getLakarutlatande().getSkapadAvHosPersonal().getEnhet().setPostort(null);
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Validation Error(s) found: Validation Error:No postort found for enhet!", response.getResult().getErrorText());

        Mockito.verifyZeroInteractions(moduleContainer);
    }

    @Test
    public void testRegisterMedicalCertificateTomtEnhetpostort() throws Exception {
        request.getLakarutlatande().getSkapadAvHosPersonal().getEnhet().setPostort("");
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Validation Error(s) found: Validation Error:No postort found for enhet!", response.getResult().getErrorText());

        Mockito.verifyZeroInteractions(moduleContainer);
    }

    @Test
    public void testRegisterMedicalCertificateSaknatEnhettelefonnummer() throws Exception {
        request.getLakarutlatande().getSkapadAvHosPersonal().getEnhet().setTelefonnummer(null);
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Validation Error(s) found: Validation Error:No telefonnummer found for enhet!", response.getResult().getErrorText());

        Mockito.verifyZeroInteractions(moduleContainer);
    }

    @Test
    public void testRegisterMedicalCertificateTomtEnhettelefonnummer() throws Exception {
        request.getLakarutlatande().getSkapadAvHosPersonal().getEnhet().setTelefonnummer("");
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Validation Error(s) found: Validation Error:No telefonnummer found for enhet!", response.getResult().getErrorText());

        Mockito.verifyZeroInteractions(moduleContainer);
    }

    @Test
    public void testRegisterMedicalCertificateSaknadVardgivare() throws Exception {
        request.getLakarutlatande().getSkapadAvHosPersonal().getEnhet().setVardgivare(null);
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Validation Error(s) found: Validation Error:No vardgivare element found!", response.getResult().getErrorText());

        Mockito.verifyZeroInteractions(moduleContainer);
    }

    @Test
    public void testRegisterMedicalCertificateSaknadVardgivareId() throws Exception {
        request.getLakarutlatande().getSkapadAvHosPersonal().getEnhet().getVardgivare().setVardgivareId(null);
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Validation Error(s) found: Validation Error:No vardgivare-id found!\n" +
                "Validation Error:Wrong o.i.d. for vardgivareId! Should be 1.2.752.129.2.1.4.1", response.getResult().getErrorText());

        Mockito.verifyZeroInteractions(moduleContainer);
    }

    @Test
    public void testRegisterMedicalCertificateFelaktigVardgivareIdKod() throws Exception {
        request.getLakarutlatande().getSkapadAvHosPersonal().getEnhet().getVardgivare().getVardgivareId().setRoot("invalid");
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Validation Error(s) found: Validation Error:Wrong o.i.d. for vardgivareId! Should be 1.2.752.129.2.1.4.1",
                response.getResult().getErrorText());

        Mockito.verifyZeroInteractions(moduleContainer);
    }

    @Test
    public void testRegisterMedicalCertificateTomtVardgivareId() throws Exception {
        request.getLakarutlatande().getSkapadAvHosPersonal().getEnhet().getVardgivare().getVardgivareId().setExtension("");
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Validation Error(s) found: Validation Error:No vardgivare-id found!", response.getResult().getErrorText());

        Mockito.verifyZeroInteractions(moduleContainer);
    }

    @Test
    public void testRegisterMedicalCertificateSaknatVardgivarenamn() throws Exception {
        request.getLakarutlatande().getSkapadAvHosPersonal().getEnhet().getVardgivare().setVardgivarnamn(null);
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Validation Error(s) found: Validation Error:No vardgivarenamn found!", response.getResult().getErrorText());

        Mockito.verifyZeroInteractions(moduleContainer);
    }

    @Test
    public void testRegisterMedicalCertificateSaknadArbetsplatskod() throws Exception {
        request.getLakarutlatande().getSkapadAvHosPersonal().getEnhet().setArbetsplatskod(null);
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Validation Error(s) found: Validation Error:No arbetsplatskod element found!", response.getResult().getErrorText());

        Mockito.verifyZeroInteractions(moduleContainer);
    }

    @Test
    public void testRegisterMedicalCertificateFelaktigArbetsplatskodKod() throws Exception {
        request.getLakarutlatande().getSkapadAvHosPersonal().getEnhet().getArbetsplatskod().setRoot("invalid");
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Validation Error(s) found: Validation Error:Wrong o.i.d for arbetsplatskod, should be 1.2.752.29.4.71",
                response.getResult().getErrorText());

        Mockito.verifyZeroInteractions(moduleContainer);
    }

    @Test
    public void testRegisterMedicalCertificateTomArbetsplatskod() throws Exception {
        request.getLakarutlatande().getSkapadAvHosPersonal().getEnhet().getArbetsplatskod().setExtension("");
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Validation Error(s) found: Validation Error:No arbetsplatskod found!", response.getResult().getErrorText());

        Mockito.verifyZeroInteractions(moduleContainer);
    }

    @Test
    public void testRegisterMedicalCertificateSaknadFunktionstillstandAktivitet() throws Exception {
        Optional<FunktionstillstandType> kroppsfunktion = request.getLakarutlatande().getFunktionstillstand().stream()
                .filter(ft -> ft.getTypAvFunktionstillstand() == TypAvFunktionstillstand.KROPPSFUNKTION).findFirst();
        request.getLakarutlatande().getFunktionstillstand().clear();
        request.getLakarutlatande().getFunktionstillstand().add(kroppsfunktion.get());
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Validation Error(s) found: Validation Error:No funktionstillstand - aktivitet element found!\n" +
                "Validation Error:No arbetsformaga element found for field 8a!\n" +
                "Validation Error:No arbetsformaga element found 8b!.", response.getResult().getErrorText());

        Mockito.verifyZeroInteractions(moduleContainer);
    }

    @Test
    public void testRegisterMedicalCertificateSaknadMedicinsktTillstand() throws Exception {
        request.getLakarutlatande().setMedicinsktTillstand(null);
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Validation Error(s) found: Validation Error:No medicinsktTillstand element found!", response.getResult().getErrorText());

        Mockito.verifyZeroInteractions(moduleContainer);
    }

    @Test
    public void testRegisterMedicalCertificateSaknadDiagnoskod() throws Exception {
        request.getLakarutlatande().getMedicinsktTillstand().setTillstandskod(null);
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Validation Error(s) found: Validation Error:No tillstandskod in medicinsktTillstand found!\n" +
                "Validation Error:Wrong code system name for medicinskt tillstand - tillstandskod (diagnoskod)! Should be ICD-10 OR KSH97P",
                response.getResult().getErrorText());

        Mockito.verifyZeroInteractions(moduleContainer);
    }

    @Test
    public void testRegisterMedicalCertificateSaknadDiagnoskodSmL() throws Exception {
        request.getLakarutlatande().getAktivitet().clear();
        request.getLakarutlatande().getAktivitet().add(new AktivitetType());
        request.getLakarutlatande().getAktivitet().get(0).setAktivitetskod(Aktivitetskod.AVSTANGNING_ENLIGT_SM_L_PGA_SMITTA);
        request.getLakarutlatande().setMedicinsktTillstand(null);
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.OK, response.getResult().getResultCode());

        Mockito.verify(moduleContainer, Mockito.only()).certificateReceived(any(CertificateHolder.class));
    }

    @Test
    public void testRegisterMedicalCertificateSaknadDiagnoskodSystem() throws Exception {
        request.getLakarutlatande().getMedicinsktTillstand().setTillstandskod(new CD());
        request.getLakarutlatande().getMedicinsktTillstand().getTillstandskod().setCode("M25");
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals(
                "Validation Error(s) found: Validation Error:Wrong code system name for medicinskt tillstand - tillstandskod (diagnoskod)! Should be ICD-10 OR KSH97P",
                response.getResult().getErrorText());

        Mockito.verifyZeroInteractions(moduleContainer);
    }

    @Test
    public void testRegisterMedicalCertificateFelaktigDiagnoskodSystem() throws Exception {
        request.getLakarutlatande().getMedicinsktTillstand().setTillstandskod(new CD());
        request.getLakarutlatande().getMedicinsktTillstand().getTillstandskod().setCodeSystem("invalid");
        request.getLakarutlatande().getMedicinsktTillstand().getTillstandskod().setCode("M25");
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals(
                "Validation Error(s) found: Validation Error:Wrong code system name for medicinskt tillstand - tillstandskod (diagnoskod)! Should be ICD-10 OR KSH97P",
                response.getResult().getErrorText());

        Mockito.verifyZeroInteractions(moduleContainer);
    }

    @Test
    public void testRegisterMedicalCertificateTomtBedomtTillstandBeskrivning() throws Exception {
        request.getLakarutlatande().getBedomtTillstand().setBeskrivning("");
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.OK, response.getResult().getResultCode());

        Mockito.verify(moduleContainer, Mockito.only()).certificateReceived(any(CertificateHolder.class));
    }

    @Test
    public void testRegisterMedicalCertificateSaknadBedomtTillstandBeskrivning() throws Exception {
        request.getLakarutlatande().getBedomtTillstand().setBeskrivning(null);
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Validation Error(s) found: Validation Error:Beskrivning must be set for Falt3 Aktuellt Sjukdomsforlopp",
                response.getResult().getErrorText());

        Mockito.verifyZeroInteractions(moduleContainer);
    }

    @Test
    public void testRegisterMedicalCertificateSaknadFunktionstillstandKroppsfunktion() throws Exception {
        Optional<FunktionstillstandType> aktivitet = request.getLakarutlatande().getFunktionstillstand().stream()
                .filter(ft -> ft.getTypAvFunktionstillstand() == TypAvFunktionstillstand.AKTIVITET).findFirst();
        request.getLakarutlatande().getFunktionstillstand().clear();
        request.getLakarutlatande().getFunktionstillstand().add(aktivitet.get());
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Validation Error(s) found: Validation Error:No funktionstillstand - kroppsfunktion element found!",
                response.getResult().getErrorText());

        Mockito.verifyZeroInteractions(moduleContainer);
    }

    @Test
    public void testRegisterMedicalCertificateSaknadFunktionstillstandKroppsfunktionSmL() throws Exception {
        request.getLakarutlatande().getAktivitet().clear();
        request.getLakarutlatande().getAktivitet().add(new AktivitetType());
        request.getLakarutlatande().getAktivitet().get(0).setAktivitetskod(Aktivitetskod.AVSTANGNING_ENLIGT_SM_L_PGA_SMITTA);
        Optional<FunktionstillstandType> aktivitet = request.getLakarutlatande().getFunktionstillstand().stream()
                .filter(ft -> ft.getTypAvFunktionstillstand() == TypAvFunktionstillstand.AKTIVITET).findFirst();
        request.getLakarutlatande().getFunktionstillstand().clear();
        request.getLakarutlatande().getFunktionstillstand().add(aktivitet.get());
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.OK, response.getResult().getResultCode());

        Mockito.verify(moduleContainer, Mockito.only()).certificateReceived(any(CertificateHolder.class));
    }

    @Test
    public void testRegisterMedicalCertificateTomFunktionstillstandKroppsfunktionBeskrivning() throws Exception {
        request.getLakarutlatande().getFunktionstillstand().stream()
                .filter(ft -> ft.getTypAvFunktionstillstand() == TypAvFunktionstillstand.KROPPSFUNKTION).forEach(ft -> ft.setBeskrivning(""));
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Validation Error(s) found: Validation Error:No beskrivning in funktionstillstand - kroppsfunktion found!",
                response.getResult().getErrorText());

        Mockito.verifyZeroInteractions(moduleContainer);
    }

    @Test
    public void testRegisterMedicalCertificateTomFunktionstillstandKroppsfunktionBeskrivningSmL() throws Exception {
        request.getLakarutlatande().getAktivitet().clear();
        request.getLakarutlatande().getAktivitet().add(new AktivitetType());
        request.getLakarutlatande().getAktivitet().get(0).setAktivitetskod(Aktivitetskod.AVSTANGNING_ENLIGT_SM_L_PGA_SMITTA);
        request.getLakarutlatande().getFunktionstillstand().stream()
                .filter(ft -> ft.getTypAvFunktionstillstand() == TypAvFunktionstillstand.KROPPSFUNKTION).forEach(ft -> ft.setBeskrivning(""));
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.OK, response.getResult().getResultCode());

        Mockito.verify(moduleContainer, Mockito.only()).certificateReceived(any(CertificateHolder.class));
    }

    @Test
    public void testRegisterMedicalCertificateSaknadFunktionstillstandKroppsfunktionBeskrivning() throws Exception {
        request.getLakarutlatande().getFunktionstillstand().stream()
                .filter(ft -> ft.getTypAvFunktionstillstand() == TypAvFunktionstillstand.KROPPSFUNKTION).forEach(ft -> ft.setBeskrivning(null));
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Validation Error(s) found: Validation Error:No beskrivning in funktionstillstand - kroppsfunktion found!",
                response.getResult().getErrorText());

        Mockito.verifyZeroInteractions(moduleContainer);
    }

    @Test
    public void testRegisterMedicalCertificateSaknadFunktionstillstandKroppsfunktionBeskrivningSmL() throws Exception {
        request.getLakarutlatande().getAktivitet().clear();
        request.getLakarutlatande().getAktivitet().add(new AktivitetType());
        request.getLakarutlatande().getAktivitet().get(0).setAktivitetskod(Aktivitetskod.AVSTANGNING_ENLIGT_SM_L_PGA_SMITTA);
        request.getLakarutlatande().getFunktionstillstand().stream()
                .filter(ft -> ft.getTypAvFunktionstillstand() == TypAvFunktionstillstand.KROPPSFUNKTION).forEach(ft -> ft.setBeskrivning(null));
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.OK, response.getResult().getResultCode());

        Mockito.verify(moduleContainer, Mockito.only()).certificateReceived(any(CertificateHolder.class));
    }

    @Test
    public void testRegisterMedicalCertificateSaknadeVardkontakterReferenser() throws Exception {
        request.getLakarutlatande().getVardkontakt().clear();
        request.getLakarutlatande().getReferens().clear();
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Validation Error(s) found: Validation Error:No vardkontakt or referens element found ! At least one must be set!",
                response.getResult().getErrorText());

        Mockito.verifyZeroInteractions(moduleContainer);
    }

    @Test
    public void testRegisterMedicalCertificateSaknadeVardkontakterReferenserSmL() throws Exception {
        request.getLakarutlatande().getAktivitet().clear();
        request.getLakarutlatande().getAktivitet().add(new AktivitetType());
        request.getLakarutlatande().getAktivitet().get(0).setAktivitetskod(Aktivitetskod.AVSTANGNING_ENLIGT_SM_L_PGA_SMITTA);
        request.getLakarutlatande().getVardkontakt().clear();
        request.getLakarutlatande().getReferens().clear();
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.OK, response.getResult().getResultCode());

        Mockito.verify(moduleContainer, Mockito.only()).certificateReceived(any(CertificateHolder.class));
    }

    @Test
    public void testRegisterMedicalCertificateSaknadeVardkontaktTid() throws Exception {
        request.getLakarutlatande().getVardkontakt().stream().filter(vk -> vk.getVardkontakttyp() == Vardkontakttyp.MIN_UNDERSOKNING_AV_PATIENTEN)
                .forEach(vk -> vk.setVardkontaktstid(null));
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Validation Error(s) found: Validation Error:No or wrong date for vardkontakt - min undersokning av patienten found!",
                response.getResult().getErrorText());

        Mockito.verifyZeroInteractions(moduleContainer);
    }

    @Test
    public void testRegisterMedicalCertificateTomAktivitetsbegransningBeskrivning() throws Exception {
        request.getLakarutlatande().getFunktionstillstand().stream()
                .filter(ft -> ft.getTypAvFunktionstillstand() == TypAvFunktionstillstand.AKTIVITET).forEach(ft -> ft.setBeskrivning(""));
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.OK, response.getResult().getResultCode());

        Mockito.verify(moduleContainer, Mockito.only()).certificateReceived(any(CertificateHolder.class));
    }

    @Test
    public void testRegisterMedicalCertificateSaknadBeskrivningVidRekommendationOvrigt() throws Exception {
        request.getLakarutlatande().getAktivitet().clear();
        request.getLakarutlatande().getAktivitet().add(new AktivitetType());
        request.getLakarutlatande().getAktivitet().get(0).setAktivitetskod(Aktivitetskod.OVRIGT);
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Validation Error(s) found: Validation Error:Beskrivning must be set for Aktivitet Rekommendation Ovrigt",
                response.getResult().getErrorText());

        Mockito.verifyZeroInteractions(moduleContainer);
    }

    @Test
    public void testRegisterMedicalCertificateSaknadBeskrivningVidRekommendationOvrigtSmL() throws Exception {
        request.getLakarutlatande().getAktivitet().clear();
        request.getLakarutlatande().getAktivitet().add(new AktivitetType());
        request.getLakarutlatande().getAktivitet().get(0).setAktivitetskod(Aktivitetskod.OVRIGT);
        request.getLakarutlatande().getAktivitet().add(new AktivitetType());
        request.getLakarutlatande().getAktivitet().get(1).setAktivitetskod(Aktivitetskod.AVSTANGNING_ENLIGT_SM_L_PGA_SMITTA);
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Validation Error(s) found: Validation Error:Beskrivning must be set for Aktivitet Rekommendation Ovrigt",
                response.getResult().getErrorText());

        Mockito.verifyZeroInteractions(moduleContainer);
    }

    @Test
    public void testRegisterMedicalCertificateTomBeskrivningVidRekommendationOvrigt() throws Exception {
        request.getLakarutlatande().getAktivitet().clear();
        request.getLakarutlatande().getAktivitet().add(new AktivitetType());
        request.getLakarutlatande().getAktivitet().get(0).setAktivitetskod(Aktivitetskod.OVRIGT);
        request.getLakarutlatande().getAktivitet().get(0).setBeskrivning("");
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.OK, response.getResult().getResultCode());

        Mockito.verify(moduleContainer, Mockito.only()).certificateReceived(any(CertificateHolder.class));
    }

    @Test
    public void testRegisterMedicalCertificateSaknadBeskrivningVidBehandlingVarden() throws Exception {
        request.getLakarutlatande().getAktivitet().clear();
        request.getLakarutlatande().getAktivitet().add(new AktivitetType());
        request.getLakarutlatande().getAktivitet().get(0)
                .setAktivitetskod(Aktivitetskod.PLANERAD_ELLER_PAGAENDE_BEHANDLING_ELLER_ATGARD_INOM_SJUKVARDEN);
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals(
                "Validation Error(s) found: Validation Error:Beskrivning must be set for Aktivitet Rekommendation Planerad eller pågående åtgärd inom sjukvården",
                response.getResult().getErrorText());

        Mockito.verifyZeroInteractions(moduleContainer);
    }

    @Test
    public void testRegisterMedicalCertificateSaknadBeskrivningVidBehandlingVardenSmL() throws Exception {
        request.getLakarutlatande().getAktivitet().clear();
        request.getLakarutlatande().getAktivitet().add(new AktivitetType());
        request.getLakarutlatande().getAktivitet().get(0)
                .setAktivitetskod(Aktivitetskod.PLANERAD_ELLER_PAGAENDE_BEHANDLING_ELLER_ATGARD_INOM_SJUKVARDEN);
        request.getLakarutlatande().getAktivitet().add(new AktivitetType());
        request.getLakarutlatande().getAktivitet().get(1).setAktivitetskod(Aktivitetskod.AVSTANGNING_ENLIGT_SM_L_PGA_SMITTA);
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals(
                "Validation Error(s) found: Validation Error:Beskrivning must be set for Aktivitet Rekommendation Planerad eller pågående åtgärd inom sjukvården",
                response.getResult().getErrorText());

        Mockito.verifyZeroInteractions(moduleContainer);
    }

    @Test
    public void testRegisterMedicalCertificateTomBeskrivningVidBehandlingVarden() throws Exception {
        request.getLakarutlatande().getAktivitet().clear();
        request.getLakarutlatande().getAktivitet().add(new AktivitetType());
        request.getLakarutlatande().getAktivitet().get(0)
                .setAktivitetskod(Aktivitetskod.PLANERAD_ELLER_PAGAENDE_BEHANDLING_ELLER_ATGARD_INOM_SJUKVARDEN);
        request.getLakarutlatande().getAktivitet().get(0).setBeskrivning("");
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.OK, response.getResult().getResultCode());

        Mockito.verify(moduleContainer, Mockito.only()).certificateReceived(any(CertificateHolder.class));
    }

    @Test
    public void testRegisterMedicalCertificateSaknadBeskrivningVidBehandlingAnnan() throws Exception {
        request.getLakarutlatande().getAktivitet().clear();
        request.getLakarutlatande().getAktivitet().add(new AktivitetType());
        request.getLakarutlatande().getAktivitet().get(0).setAktivitetskod(Aktivitetskod.PLANERAD_ELLER_PAGAENDE_ANNAN_ATGARD);
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals(
                "Validation Error(s) found: Validation Error:Beskrivning must be set for Aktivitet Rekommendation Planerad eller pågående annan atgärd",
                response.getResult().getErrorText());

        Mockito.verifyZeroInteractions(moduleContainer);
    }

    @Test
    public void testRegisterMedicalCertificateSaknadBeskrivningVidBehandningAnnanSmL() throws Exception {
        request.getLakarutlatande().getAktivitet().clear();
        request.getLakarutlatande().getAktivitet().add(new AktivitetType());
        request.getLakarutlatande().getAktivitet().get(0).setAktivitetskod(Aktivitetskod.PLANERAD_ELLER_PAGAENDE_ANNAN_ATGARD);
        request.getLakarutlatande().getAktivitet().add(new AktivitetType());
        request.getLakarutlatande().getAktivitet().get(1).setAktivitetskod(Aktivitetskod.AVSTANGNING_ENLIGT_SM_L_PGA_SMITTA);
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals(
                "Validation Error(s) found: Validation Error:Beskrivning must be set for Aktivitet Rekommendation Planerad eller pågående annan atgärd",
                response.getResult().getErrorText());

        Mockito.verifyZeroInteractions(moduleContainer);
    }

    @Test
    public void testRegisterMedicalCertificateTomBeskrivningVidBehandlingAnnan() throws Exception {
        request.getLakarutlatande().getAktivitet().clear();
        request.getLakarutlatande().getAktivitet().add(new AktivitetType());
        request.getLakarutlatande().getAktivitet().get(0).setAktivitetskod(Aktivitetskod.PLANERAD_ELLER_PAGAENDE_ANNAN_ATGARD);
        request.getLakarutlatande().getAktivitet().get(0).setBeskrivning("");
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.OK, response.getResult().getResultCode());

        Mockito.verify(moduleContainer, Mockito.only()).certificateReceived(any(CertificateHolder.class));
    }

    @Test
    public void testRegisterMedicalCertificateSaknadKommentarReferensAnnat() throws Exception {
        request.getLakarutlatande().getReferens().clear();
        request.getLakarutlatande().getReferens().add(new ReferensType());
        request.getLakarutlatande().getReferens().get(0).setReferenstyp(Referenstyp.ANNAT);
        request.getLakarutlatande().getReferens().get(0).setDatum(LocalDate.now());
        request.getLakarutlatande().setKommentar(null);
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Validation Error(s) found: Validation Error:Upplysningar should contain data as field 4 or fields 10 is checked.",
                response.getResult().getErrorText());

        Mockito.verifyZeroInteractions(moduleContainer);
    }

    @Test
    public void testRegisterMedicalCertificateSaknadKommentarPrognosGarEjAttBedomma() throws Exception {
        request.getLakarutlatande().getFunktionstillstand().stream()
                .filter(ft -> ft.getTypAvFunktionstillstand() == TypAvFunktionstillstand.AKTIVITET)
                .forEach(ft -> ft.getArbetsformaga().setPrognosangivelse(Prognosangivelse.DET_GAR_INTE_ATT_BEDOMMA));
        request.getLakarutlatande().setKommentar(null);
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Validation Error(s) found: Validation Error:Upplysningar should contain data as field 4 or fields 10 is checked.",
                response.getResult().getErrorText());

        Mockito.verifyZeroInteractions(moduleContainer);
    }

    @Test
    public void testRegisterMedicalCertificateSaknadArbetsformaga() throws Exception {
        request.getLakarutlatande().getFunktionstillstand().stream()
                .filter(ft -> ft.getTypAvFunktionstillstand() == TypAvFunktionstillstand.AKTIVITET)
                .forEach(ft -> ft.setArbetsformaga(null));
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Validation Error(s) found: Validation Error:No arbetsformaga element found for field 8a!\n" +
                "Validation Error:No arbetsformaga element found 8b!.", response.getResult().getErrorText());

        Mockito.verifyZeroInteractions(moduleContainer);
    }

    @Test
    public void testRegisterMedicalCertificateSaknadSysselsattning() throws Exception {
        request.getLakarutlatande().getFunktionstillstand().stream()
                .filter(ft -> ft.getTypAvFunktionstillstand() == TypAvFunktionstillstand.AKTIVITET)
                .forEach(ft -> ft.getArbetsformaga().getSysselsattning().clear());
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals(
                "Validation Error(s) found: Validation Error:No sysselsattning element found for field 8a! Nuvarande arbete, arbestloshet or foraldraledig should be set.",
                response.getResult().getErrorText());

        Mockito.verifyZeroInteractions(moduleContainer);
    }

    @Test
    public void testRegisterMedicalCertificateSaknadSysselsattningSmL() throws Exception {
        request.getLakarutlatande().getAktivitet().clear();
        request.getLakarutlatande().getAktivitet().add(new AktivitetType());
        request.getLakarutlatande().getAktivitet().get(0).setAktivitetskod(Aktivitetskod.AVSTANGNING_ENLIGT_SM_L_PGA_SMITTA);
        request.getLakarutlatande().getFunktionstillstand().stream()
                .filter(ft -> ft.getTypAvFunktionstillstand() == TypAvFunktionstillstand.AKTIVITET)
                .forEach(ft -> ft.getArbetsformaga().getSysselsattning().clear());
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.OK, response.getResult().getResultCode());

        Mockito.verify(moduleContainer, Mockito.only()).certificateReceived(any(CertificateHolder.class));
    }

    @Test
    public void testRegisterMedicalCertificateSaknadArbetsuppgift() throws Exception {
        request.getLakarutlatande().getFunktionstillstand().stream()
                .filter(ft -> ft.getTypAvFunktionstillstand() == TypAvFunktionstillstand.AKTIVITET)
                .forEach(ft -> ft.getArbetsformaga().setArbetsuppgift(null));
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Validation Error(s) found: Validation Error:No arbetsuppgift element found when arbete set in field 8a!.",
                response.getResult().getErrorText());

        Mockito.verifyZeroInteractions(moduleContainer);
    }

    @Test
    public void testRegisterMedicalCertificateSaknadArbetsuppgiftSmL() throws Exception {
        request.getLakarutlatande().getAktivitet().clear();
        request.getLakarutlatande().getAktivitet().add(new AktivitetType());
        request.getLakarutlatande().getAktivitet().get(0).setAktivitetskod(Aktivitetskod.AVSTANGNING_ENLIGT_SM_L_PGA_SMITTA);
        request.getLakarutlatande().getFunktionstillstand().stream()
                .filter(ft -> ft.getTypAvFunktionstillstand() == TypAvFunktionstillstand.AKTIVITET)
                .forEach(ft -> ft.getArbetsformaga().setArbetsuppgift(null));
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.OK, response.getResult().getResultCode());

        Mockito.verify(moduleContainer, Mockito.only()).certificateReceived(any(CertificateHolder.class));
    }

    @Test
    public void testRegisterMedicalCertificateSaknadArbetsuppgiftBeskrivning() throws Exception {
        request.getLakarutlatande().getFunktionstillstand().stream()
                .filter(ft -> ft.getTypAvFunktionstillstand() == TypAvFunktionstillstand.AKTIVITET)
                .forEach(ft -> ft.getArbetsformaga().getArbetsuppgift().setTypAvArbetsuppgift(null));
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Validation Error(s) found: Validation Error:No typAvArbetsuppgift element found!",
                response.getResult().getErrorText());

        Mockito.verifyZeroInteractions(moduleContainer);
    }

    @Test
    public void testRegisterMedicalCertificateSaknadArbetsuppgiftBeskrivningSmL() throws Exception {
        request.getLakarutlatande().getAktivitet().clear();
        request.getLakarutlatande().getAktivitet().add(new AktivitetType());
        request.getLakarutlatande().getAktivitet().get(0).setAktivitetskod(Aktivitetskod.AVSTANGNING_ENLIGT_SM_L_PGA_SMITTA);
        request.getLakarutlatande().getFunktionstillstand().stream()
                .filter(ft -> ft.getTypAvFunktionstillstand() == TypAvFunktionstillstand.AKTIVITET)
                .forEach(ft -> ft.getArbetsformaga().getArbetsuppgift().setTypAvArbetsuppgift(null));
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Validation Error(s) found: Validation Error:No typAvArbetsuppgift element found!",
                response.getResult().getErrorText());

        Mockito.verifyZeroInteractions(moduleContainer);
    }

    @Test
    public void testRegisterMedicalCertificateTomArbetsuppgiftBeskrivning() throws Exception {
        request.getLakarutlatande().getFunktionstillstand().stream()
                .filter(ft -> ft.getTypAvFunktionstillstand() == TypAvFunktionstillstand.AKTIVITET)
                .forEach(ft -> ft.getArbetsformaga().getArbetsuppgift().setTypAvArbetsuppgift(""));
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Validation Error(s) found: Validation Error:No typAvArbetsuppgift found when arbete set in field 8a!.",
                response.getResult().getErrorText());

        Mockito.verifyZeroInteractions(moduleContainer);
    }

    @Test
    public void testRegisterMedicalCertificateTomArbetsuppgiftBeskrivningSmL() throws Exception {
        request.getLakarutlatande().getAktivitet().clear();
        request.getLakarutlatande().getAktivitet().add(new AktivitetType());
        request.getLakarutlatande().getAktivitet().get(0).setAktivitetskod(Aktivitetskod.AVSTANGNING_ENLIGT_SM_L_PGA_SMITTA);
        request.getLakarutlatande().getFunktionstillstand().stream()
                .filter(ft -> ft.getTypAvFunktionstillstand() == TypAvFunktionstillstand.AKTIVITET)
                .forEach(ft -> ft.getArbetsformaga().getArbetsuppgift().setTypAvArbetsuppgift(""));
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.OK, response.getResult().getResultCode());

        Mockito.verify(moduleContainer, Mockito.only()).certificateReceived(any(CertificateHolder.class));
    }

    @Test
    public void testRegisterMedicalCertificateSaknadVaraktighet() throws Exception {
        request.getLakarutlatande().getFunktionstillstand().stream()
                .filter(ft -> ft.getTypAvFunktionstillstand() == TypAvFunktionstillstand.AKTIVITET)
                .forEach(ft -> ft.getArbetsformaga().getArbetsformagaNedsattning().clear());
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Validation Error(s) found: Validation Error:No arbetsformaganedsattning element found 8b!.",
                response.getResult().getErrorText());

        Mockito.verifyZeroInteractions(moduleContainer);
    }

    @Test
    public void testRegisterMedicalCertificateSaknadVaraktighetFrom() throws Exception {
        request.getLakarutlatande().getFunktionstillstand().stream()
                .filter(ft -> ft.getTypAvFunktionstillstand() == TypAvFunktionstillstand.AKTIVITET)
                .forEach(ft -> ft.getArbetsformaga().getArbetsformagaNedsattning().get(0).setVaraktighetFrom(null));
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Validation Error(s) found: Validation Error:No or wrong date for helt nedsatt from date found!",
                response.getResult().getErrorText());

        Mockito.verifyZeroInteractions(moduleContainer);
    }

    @Test
    public void testRegisterMedicalCertificateSaknadVaraktighetTom() throws Exception {
        request.getLakarutlatande().getFunktionstillstand().stream()
                .filter(ft -> ft.getTypAvFunktionstillstand() == TypAvFunktionstillstand.AKTIVITET)
                .forEach(ft -> ft.getArbetsformaga().getArbetsformagaNedsattning().get(0).setVaraktighetTom(null));
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Validation Error(s) found: Validation Error:No or wrong date for helt nedsatt tom date found!",
                response.getResult().getErrorText());

        Mockito.verifyZeroInteractions(moduleContainer);
    }

    @Test
    public void testRegisterMedicalCertificateOverlappandeVaraktigheter() throws Exception {
        final LocalDate from = LocalDate.now().minusDays(1);
        final LocalDate to = LocalDate.now().plusDays(1);
        request.getLakarutlatande().getFunktionstillstand().stream()
                .filter(ft -> ft.getTypAvFunktionstillstand() == TypAvFunktionstillstand.AKTIVITET)
                .forEach(ft -> {
                    ft.getArbetsformaga().getArbetsformagaNedsattning().clear();
                    ft.getArbetsformaga().getArbetsformagaNedsattning().add(new ArbetsformagaNedsattningType());
                    ft.getArbetsformaga().getArbetsformagaNedsattning().add(new ArbetsformagaNedsattningType());
                    ft.getArbetsformaga().getArbetsformagaNedsattning().get(0).setNedsattningsgrad(Nedsattningsgrad.HELT_NEDSATT);
                    ft.getArbetsformaga().getArbetsformagaNedsattning().get(0).setVaraktighetFrom(from);
                    ft.getArbetsformaga().getArbetsformagaNedsattning().get(0).setVaraktighetTom(to);
                    ft.getArbetsformaga().getArbetsformagaNedsattning().get(1).setNedsattningsgrad(Nedsattningsgrad.NEDSATT_MED_1_2);
                    ft.getArbetsformaga().getArbetsformagaNedsattning().get(1).setVaraktighetFrom(from);
                    ft.getArbetsformaga().getArbetsformagaNedsattning().get(1).setVaraktighetTom(to);
                });
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.OK, response.getResult().getResultCode());

        Mockito.verify(moduleContainer, Mockito.only()).certificateReceived(any(CertificateHolder.class));
    }

    @Test
    public void testRegisterMedicalCertificateMultiplaRessatt() throws Exception {
        request.getLakarutlatande().getAktivitet().clear();
        request.getLakarutlatande().getAktivitet().add(new AktivitetType());
        request.getLakarutlatande().getAktivitet().get(0).setAktivitetskod(Aktivitetskod.FORANDRAT_RESSATT_TILL_ARBETSPLATSEN_AR_AKTUELLT);
        request.getLakarutlatande().getAktivitet().add(new AktivitetType());
        request.getLakarutlatande().getAktivitet().get(1).setAktivitetskod(Aktivitetskod.FORANDRAT_RESSATT_TILL_ARBETSPLATSEN_AR_EJ_AKTUELLT);
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Validation Error(s) found: Validation Error:Only one forandrat ressatt could be set for field 11.",
                response.getResult().getErrorText());

        Mockito.verifyZeroInteractions(moduleContainer);
    }

    @Test
    public void testWithExistingCertificate() throws Exception {
        Mockito.doThrow(new CertificateAlreadyExistsException(request.getLakarutlatande().getLakarutlatandeId())).when(moduleContainer)
                .certificateReceived(any(CertificateHolder.class));

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
