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

package se.inera.intyg.intygstyper.fk7263.model.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.time.LocalDateTime;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import se.inera.intyg.common.support.Constants;
import se.inera.intyg.common.support.common.enumerations.Diagnoskodverk;
import se.inera.intyg.common.support.common.enumerations.HandelsekodEnum;
import se.inera.intyg.common.support.model.common.internal.*;
import se.inera.intyg.common.support.modules.service.WebcertModuleService;
import se.inera.intyg.common.support.modules.support.api.dto.Personnummer;
import se.inera.intyg.common.support.modules.support.api.notification.*;
import se.inera.intyg.common.util.integration.integration.json.CustomObjectMapper;
import se.inera.intyg.intygstyper.fk7263.model.internal.Utlatande;
import se.riv.clinicalprocess.healthcond.certificate.certificatestatusupdateforcareresponder.v1.CertificateStatusUpdateForCareType;
import se.riv.clinicalprocess.healthcond.certificate.types.v1.HandelsekodKodRestriktion;

@RunWith(MockitoJUnitRunner.class)
public class Fk7263InternalToNotificationTest {

    private static final Logger LOG = LoggerFactory.getLogger(Fk7263InternalToNotificationTest.class);

    private static final String FK7263 = "fk7263";

    private static final String LOGISK_ADRESS = "123456789";

    private static final String INTYGS_ID = "intyg-1";

    private static final String JSON = readRequestFromFile("InternalToNotificationTest/utlatande-intyg-1.json");

    @Mock
    private WebcertModuleService mockModuleService;

    @Spy
    private CustomObjectMapper objectMapper;

    @InjectMocks
    private Fk7263InternalToNotification converter;

    @Before
    public void setupMockDiagnosAnswer() {
        when(mockModuleService.validateDiagnosisCode(anyString(), any(Diagnoskodverk.class))).then(invocation -> {
            Object[] arguments = invocation.getArguments();
            String diagnoskod = (String) arguments[0];
            Diagnoskodverk kodverk = (Diagnoskodverk) arguments[1];

            if (kodverk == null) {
                LOG.error("No kodverk supplied");
                return Boolean.FALSE;
            }

            if (diagnoskod == null) {
                return Boolean.FALSE;
            }

            if (diagnoskod.equals("S47") || diagnoskod.equals("M51-")) {
                return Boolean.TRUE;
            }

            return Boolean.FALSE;
        });
    }

    @Test
    public void testWithFullyPopulatedUtlatande() throws Exception {
        String json = readRequestFromFile("InternalToNotificationTest/utlatande-intyg-1.json");

        NotificationMessage msg = new NotificationMessage(INTYGS_ID, FK7263, LocalDateTime.now(), HandelsekodEnum.ANDRAT, LOGISK_ADRESS,
                json, FragorOchSvar.getEmpty(), null, null, SchemaVersion.VERSION_1, null);
        CertificateStatusUpdateForCareType res = converter.createCertificateStatusUpdateForCareType(msg);

        assertNotNull(res.getUtlatande());

        assertNotNull(res.getUtlatande().getUtlatandeId());
        assertEquals(INTYGS_ID, res.getUtlatande().getUtlatandeId().getExtension());
        assertEquals("FK7263", res.getUtlatande().getTypAvUtlatande().getCode());

        assertNotNull(res.getUtlatande().getHandelse());
        assertNotNull(res.getUtlatande().getHandelse().getHandelsekod());
        assertNotNull(res.getUtlatande().getHandelse().getHandelsetidpunkt());

        assertNotNull(res.getUtlatande().getHandelse().getHandelsekod().getCodeSystem());
        assertNotNull(res.getUtlatande().getHandelse().getHandelsekod().getCodeSystemName());
        assertEquals(HandelsekodKodRestriktion.HAN_11.value(), res.getUtlatande().getHandelse().getHandelsekod().getCode());
        assertEquals("INTYGSUTKAST_ANDRAT", res.getUtlatande().getHandelse().getHandelsekod().getDisplayName());

        // Not signed yet
        assertNull(res.getUtlatande().getSigneringsdatum());

        assertNotNull(res.getUtlatande().getPatient().getPersonId());

        assertNotNull(res.getUtlatande().getSkapadAv());
        assertNotNull(res.getUtlatande().getSkapadAv().getFullstandigtNamn());
        assertNotNull(res.getUtlatande().getSkapadAv().getPersonalId());
        assertNotNull(res.getUtlatande().getSkapadAv().getEnhet());
        assertNotNull(res.getUtlatande().getSkapadAv().getEnhet().getEnhetsId());
        assertNotNull(res.getUtlatande().getSkapadAv().getEnhet().getEnhetsId().getExtension());
        assertNotNull(res.getUtlatande().getSkapadAv().getEnhet().getEnhetsId().getRoot());
        assertNotNull(res.getUtlatande().getSkapadAv().getEnhet().getEnhetsnamn());

        assertEquals("S47", res.getUtlatande().getDiagnos().getCode());
        assertEquals(Diagnoskodverk.ICD_10_SE.getCodeSystem(), res.getUtlatande().getDiagnos().getCodeSystem());
        assertEquals(Diagnoskodverk.ICD_10_SE.getCodeSystemName(), res.getUtlatande().getDiagnos().getCodeSystemName());
        assertNotNull(res.getUtlatande().getDiagnos().getDisplayName());
        assertTrue(res.getUtlatande().getDiagnos().getDisplayName().contains("Klämskada"));

        assertEquals(2, res.getUtlatande().getArbetsformaga().size());

        assertNotNull(res.getUtlatande().getFragorOchSvar());
    }

    @Test
    public void testWithFullyPopulatedUtlatandeButDifferentDiagnosisCode() throws Exception {
        String json = readRequestFromFile("InternalToNotificationTest/utlatande-intyg-1b.json");

        NotificationMessage msg = new NotificationMessage("intyg-1b", FK7263, LocalDateTime.now(), HandelsekodEnum.ANDRAT, LOGISK_ADRESS,
                json, FragorOchSvar.getEmpty(), null, null, SchemaVersion.VERSION_1, null);

        CertificateStatusUpdateForCareType res = converter.createCertificateStatusUpdateForCareType(msg);

        assertNotNull(res.getUtlatande());

        assertEquals("M51-", res.getUtlatande().getDiagnos().getCode());
        assertEquals(Diagnoskodverk.KSH_97_P.getCodeSystem(), res.getUtlatande().getDiagnos().getCodeSystem());
        assertEquals(Diagnoskodverk.KSH_97_P.getCodeSystemName(), res.getUtlatande().getDiagnos().getCodeSystemName());
        assertNotNull(res.getUtlatande().getDiagnos().getDisplayName());
        assertTrue(res.getUtlatande().getDiagnos().getDisplayName().contains("Diskbråck"));

        assertEquals(2, res.getUtlatande().getArbetsformaga().size());

        assertNotNull(res.getUtlatande().getFragorOchSvar());
    }

    @Test
    public void testWithSignedMinimal() throws Exception {
        String json = readRequestFromFile("InternalToNotificationTest/utlatande-intyg-2.json");

        NotificationMessage msg = new NotificationMessage("intyg-2", FK7263, LocalDateTime.now(), HandelsekodEnum.SIGNAT, LOGISK_ADRESS,
                json, FragorOchSvar.getEmpty(), null, null, SchemaVersion.VERSION_1, null);
        CertificateStatusUpdateForCareType res = converter.createCertificateStatusUpdateForCareType(msg);

        assertNotNull(res.getUtlatande());

        assertEquals(HandelsekodKodRestriktion.HAN_2.value(), res.getUtlatande().getHandelse().getHandelsekod().getCode());
        assertEquals("INTYGSUTKAST_SIGNERAT", res.getUtlatande().getHandelse().getHandelsekod().getDisplayName());

        // is signed
        assertNotNull(res.getUtlatande().getSigneringsdatum());

        // no diagnosis in this one
        assertNull(res.getUtlatande().getDiagnos());

        assertEquals(1, res.getUtlatande().getArbetsformaga().size());
    }

    @Test
    public void testWithMissingDiagnosis() throws Exception {
        String json = readRequestFromFile("InternalToNotificationTest/utlatande-intyg-3.json");

        NotificationMessage msg = new NotificationMessage("intyg-3", FK7263, LocalDateTime.now(), HandelsekodEnum.ANDRAT, LOGISK_ADRESS,
                json, FragorOchSvar.getEmpty(), null, null, SchemaVersion.VERSION_1, null);
        CertificateStatusUpdateForCareType res = converter.createCertificateStatusUpdateForCareType(msg);

        assertNotNull(res.getUtlatande());

        assertEquals(HandelsekodKodRestriktion.HAN_11.value(), res.getUtlatande().getHandelse().getHandelsekod().getCode());
        assertEquals("INTYGSUTKAST_ANDRAT", res.getUtlatande().getHandelse().getHandelsekod().getDisplayName());

        // is not signed
        assertNull(res.getUtlatande().getSigneringsdatum());

        // no diagnosis in this one since it is missing in the input data
        assertNull(res.getUtlatande().getDiagnos());
    }

    @Test
    public void testWithInvalidDiagnosisCode() throws Exception {
        String json = readRequestFromFile("InternalToNotificationTest/utlatande-intyg-3b.json");

        NotificationMessage msg = new NotificationMessage("intyg-3b", FK7263, LocalDateTime.now(), HandelsekodEnum.ANDRAT, LOGISK_ADRESS,
                json, FragorOchSvar.getEmpty(), null, null, SchemaVersion.VERSION_1, null);
        CertificateStatusUpdateForCareType res = converter.createCertificateStatusUpdateForCareType(msg);

        assertNotNull(res.getUtlatande());

        assertEquals(HandelsekodKodRestriktion.HAN_11.value(), res.getUtlatande().getHandelse().getHandelsekod().getCode());
        assertEquals("INTYGSUTKAST_ANDRAT", res.getUtlatande().getHandelse().getHandelsekod().getDisplayName());

        // is not signed
        assertNull(res.getUtlatande().getSigneringsdatum());

        // no diagnosis in this one since the one supplied is invalid
        assertNull(res.getUtlatande().getDiagnos());
    }

    @Test
    public void testWithQuestionFromFK() throws Exception {
        String json = readRequestFromFile("InternalToNotificationTest/utlatande-intyg-2.json");

        FragorOchSvar fs = new FragorOchSvar(1, 0, 0, 0);
        NotificationMessage msg = new NotificationMessage("intyg-2", FK7263, LocalDateTime.now(), HandelsekodEnum.NYFRFM, LOGISK_ADRESS, json, fs,
                null, null, SchemaVersion.VERSION_1, null);
        CertificateStatusUpdateForCareType res = converter.createCertificateStatusUpdateForCareType(msg);

        assertNotNull(res.getUtlatande());

        assertEquals(HandelsekodKodRestriktion.HAN_6.value(), res.getUtlatande().getHandelse().getHandelsekod().getCode());
        assertEquals("FRAGA_FRAN_FK", res.getUtlatande().getHandelse().getHandelsekod().getDisplayName());

        // is signed
        assertNotNull(res.getUtlatande().getSigneringsdatum());

        // no diagnosis in this one
        assertNull(res.getUtlatande().getDiagnos());

        assertEquals(1, res.getUtlatande().getArbetsformaga().size());
        assertNotNull(res.getUtlatande().getArbetsformaga().get(0).getPeriod().getFrom());
        assertNotNull(res.getUtlatande().getArbetsformaga().get(0).getPeriod().getTom());
        assertNotNull(res.getUtlatande().getArbetsformaga().get(0).getVarde());

        assertNotNull(res.getUtlatande().getFragorOchSvar());
        assertEquals(1, res.getUtlatande().getFragorOchSvar().getAntalFragor());
        assertEquals(0, res.getUtlatande().getFragorOchSvar().getAntalSvar());
    }

    @Test
    public void testWithIncompleteNedsattning() throws Exception {
        String json = readRequestFromFile("InternalToNotificationTest/utlatande-intyg-4.json");

        NotificationMessage msg = new NotificationMessage("intyg-4", FK7263, LocalDateTime.now(), HandelsekodEnum.ANDRAT, LOGISK_ADRESS,
                json, FragorOchSvar.getEmpty(), null, null, SchemaVersion.VERSION_1, null);
        CertificateStatusUpdateForCareType res = converter.createCertificateStatusUpdateForCareType(msg);

        assertNotNull(res.getUtlatande());

        assertEquals(HandelsekodKodRestriktion.HAN_11.value(), res.getUtlatande().getHandelse().getHandelsekod().getCode());
        assertEquals("INTYGSUTKAST_ANDRAT", res.getUtlatande().getHandelse().getHandelsekod().getDisplayName());

        // no diagnosis in this one
        assertNull(res.getUtlatande().getDiagnos());

        // should contain zero since it is incomplete
        assertEquals(0, res.getUtlatande().getArbetsformaga().size());
    }

    @Test
    public void testPersonnummerRoot() throws Exception {
        Utlatande utlatande = new Utlatande();
        GrundData grundData = new GrundData();
        Patient patient = new Patient();
        final String pnr = "19121212-1212";
        patient.setPersonId(new Personnummer(pnr));
        grundData.setPatient(patient);
        HoSPersonal skapadAv = new HoSPersonal();
        Vardenhet vardenhet = new Vardenhet();
        vardenhet.setVardgivare(new Vardgivare());
        skapadAv.setVardenhet(vardenhet);
        grundData.setSkapadAv(skapadAv);
        utlatande.setGrundData(grundData);
        doReturn(utlatande).when(objectMapper).readValue(anyString(), eq(Utlatande.class));
        NotificationMessage msg = new NotificationMessage("intyg-4", FK7263, LocalDateTime.now(), HandelsekodEnum.ANDRAT, LOGISK_ADRESS,
                "", FragorOchSvar.getEmpty(), null, null, SchemaVersion.VERSION_1, null);
        CertificateStatusUpdateForCareType res = converter.createCertificateStatusUpdateForCareType(msg);
        assertEquals(pnr, res.getUtlatande().getPatient().getPersonId().getExtension());
        assertEquals(Constants.PERSON_ID_OID, res.getUtlatande().getPatient().getPersonId().getRoot());
    }

    @Test
    public void testSamordningRoot() throws Exception {
        Utlatande utlatande = new Utlatande();
        GrundData grundData = new GrundData();
        Patient patient = new Patient();
        final String pnr = "19800191-0002";
        patient.setPersonId(new Personnummer(pnr));
        grundData.setPatient(patient);
        HoSPersonal skapadAv = new HoSPersonal();
        Vardenhet vardenhet = new Vardenhet();
        vardenhet.setVardgivare(new Vardgivare());
        skapadAv.setVardenhet(vardenhet);
        grundData.setSkapadAv(skapadAv);
        utlatande.setGrundData(grundData);
        doReturn(utlatande).when(objectMapper).readValue(anyString(), eq(Utlatande.class));
        NotificationMessage msg = new NotificationMessage("intyg-4", FK7263, LocalDateTime.now(), HandelsekodEnum.ANDRAT, LOGISK_ADRESS,
                "", FragorOchSvar.getEmpty(), null, null, SchemaVersion.VERSION_1, null);
        CertificateStatusUpdateForCareType res = converter.createCertificateStatusUpdateForCareType(msg);
        assertEquals(pnr, res.getUtlatande().getPatient().getPersonId().getExtension());
        assertEquals(Constants.SAMORDNING_ID_OID, res.getUtlatande().getPatient().getPersonId().getRoot());
    }

    @Test
    public void testHan1() throws Exception {
        CertificateStatusUpdateForCareType res = converter.createCertificateStatusUpdateForCareType(new NotificationMessage("intyg-2", FK7263, LocalDateTime.now(), HandelsekodEnum.SKAPAT, LOGISK_ADRESS,
                JSON, FragorOchSvar.getEmpty(), null, null, SchemaVersion.VERSION_1, null));

        assertEquals(HandelsekodKodRestriktion.HAN_1.value(), res.getUtlatande().getHandelse().getHandelsekod().getCode());
        assertEquals("INTYGSUTKAST_SKAPAT", res.getUtlatande().getHandelse().getHandelsekod().getDisplayName());
    }

    @Test
    public void testHan2() throws Exception {
        CertificateStatusUpdateForCareType res = converter.createCertificateStatusUpdateForCareType(new NotificationMessage("intyg-2", FK7263, LocalDateTime.now(), HandelsekodEnum.SIGNAT, LOGISK_ADRESS,
                JSON, FragorOchSvar.getEmpty(), null, null, SchemaVersion.VERSION_1, null));

        assertEquals(HandelsekodKodRestriktion.HAN_2.value(), res.getUtlatande().getHandelse().getHandelsekod().getCode());
        assertEquals("INTYGSUTKAST_SIGNERAT", res.getUtlatande().getHandelse().getHandelsekod().getDisplayName());
    }

    @Test
    public void testHan3() throws Exception {
        CertificateStatusUpdateForCareType res = converter.createCertificateStatusUpdateForCareType(new NotificationMessage("intyg-2", FK7263, LocalDateTime.now(), HandelsekodEnum.SKICKA, LOGISK_ADRESS,
                JSON, FragorOchSvar.getEmpty(), null, null, SchemaVersion.VERSION_1, null));

        assertEquals(HandelsekodKodRestriktion.HAN_3.value(), res.getUtlatande().getHandelse().getHandelsekod().getCode());
        assertEquals("INTYG_SKICKAT_FK", res.getUtlatande().getHandelse().getHandelsekod().getDisplayName());
    }

    @Test
    public void testHan4() throws Exception {
        CertificateStatusUpdateForCareType res = converter.createCertificateStatusUpdateForCareType(new NotificationMessage("intyg-2", FK7263, LocalDateTime.now(), HandelsekodEnum.RADERA, LOGISK_ADRESS,
                JSON, FragorOchSvar.getEmpty(), null, null, SchemaVersion.VERSION_1, null));

        assertEquals(HandelsekodKodRestriktion.HAN_4.value(), res.getUtlatande().getHandelse().getHandelsekod().getCode());
        assertEquals("INTYGSUTKAST_RADERAT", res.getUtlatande().getHandelse().getHandelsekod().getDisplayName());
    }

    @Test
    public void testHan5() throws Exception {
        CertificateStatusUpdateForCareType res = converter.createCertificateStatusUpdateForCareType(new NotificationMessage("intyg-2", FK7263, LocalDateTime.now(), HandelsekodEnum.MAKULE, LOGISK_ADRESS,
                JSON, FragorOchSvar.getEmpty(), null, null, SchemaVersion.VERSION_1, null));

        assertEquals(HandelsekodKodRestriktion.HAN_5.value(), res.getUtlatande().getHandelse().getHandelsekod().getCode());
        assertEquals("INTYG_MAKULERAT", res.getUtlatande().getHandelse().getHandelsekod().getDisplayName());
    }

    @Test
    public void testHan6() throws Exception {
        CertificateStatusUpdateForCareType res = converter.createCertificateStatusUpdateForCareType(new NotificationMessage("intyg-2", FK7263, LocalDateTime.now(), HandelsekodEnum.NYFRFM, LOGISK_ADRESS,
                JSON, FragorOchSvar.getEmpty(), null, null, SchemaVersion.VERSION_1, null));

        assertEquals(HandelsekodKodRestriktion.HAN_6.value(), res.getUtlatande().getHandelse().getHandelsekod().getCode());
        assertEquals("FRAGA_FRAN_FK", res.getUtlatande().getHandelse().getHandelsekod().getDisplayName());
    }

    @Test
    public void testHan7() throws Exception {
        CertificateStatusUpdateForCareType res = converter.createCertificateStatusUpdateForCareType(new NotificationMessage("intyg-2", FK7263, LocalDateTime.now(), HandelsekodEnum.NYSVFM, LOGISK_ADRESS,
                JSON, FragorOchSvar.getEmpty(), null, null, SchemaVersion.VERSION_1, null));

        assertEquals(HandelsekodKodRestriktion.HAN_7.value(), res.getUtlatande().getHandelse().getHandelsekod().getCode());
        assertEquals("SVAR_FRAN_FK", res.getUtlatande().getHandelse().getHandelsekod().getDisplayName());
    }

    @Test
    public void testHan8() throws Exception {
        CertificateStatusUpdateForCareType res = converter.createCertificateStatusUpdateForCareType(new NotificationMessage("intyg-2", FK7263, LocalDateTime.now(), HandelsekodEnum.NYFRFV, LOGISK_ADRESS,
                JSON, FragorOchSvar.getEmpty(), null, null, SchemaVersion.VERSION_1, null));

        assertEquals(HandelsekodKodRestriktion.HAN_8.value(), res.getUtlatande().getHandelse().getHandelsekod().getCode());
        assertEquals("FRAGA_TILL_FK", res.getUtlatande().getHandelse().getHandelsekod().getDisplayName());
    }

    @Test
    public void testHan9() throws Exception {
        CertificateStatusUpdateForCareType res = converter.createCertificateStatusUpdateForCareType(new NotificationMessage("intyg-2", FK7263, LocalDateTime.now(), HandelsekodEnum.HANFRFM, LOGISK_ADRESS,
                JSON, FragorOchSvar.getEmpty(), null, null, SchemaVersion.VERSION_1, null));

        assertEquals(HandelsekodKodRestriktion.HAN_9.value(), res.getUtlatande().getHandelse().getHandelsekod().getCode());
        assertEquals("FRAGA_FRAN_FK_HANTERAD", res.getUtlatande().getHandelse().getHandelsekod().getDisplayName());
    }

    @Test
    public void testHan10() throws Exception {
        CertificateStatusUpdateForCareType res = converter.createCertificateStatusUpdateForCareType(new NotificationMessage("intyg-2", FK7263, LocalDateTime.now(), HandelsekodEnum.HANFRFV, LOGISK_ADRESS,
                JSON, FragorOchSvar.getEmpty(), null, null, SchemaVersion.VERSION_1, null));

        assertEquals(HandelsekodKodRestriktion.HAN_10.value(), res.getUtlatande().getHandelse().getHandelsekod().getCode());
        assertEquals("SVAR_FRAN_FK_HANTERAD", res.getUtlatande().getHandelse().getHandelsekod().getDisplayName());
    }

    @Test
    public void testHan11() throws Exception {
        CertificateStatusUpdateForCareType res = converter.createCertificateStatusUpdateForCareType(new NotificationMessage("intyg-2", FK7263, LocalDateTime.now(), HandelsekodEnum.ANDRAT, LOGISK_ADRESS,
                JSON, FragorOchSvar.getEmpty(), null, null, SchemaVersion.VERSION_1, null));

        assertEquals(HandelsekodKodRestriktion.HAN_11.value(), res.getUtlatande().getHandelse().getHandelsekod().getCode());
        assertEquals("INTYGSUTKAST_ANDRAT", res.getUtlatande().getHandelse().getHandelsekod().getDisplayName());
    }

    private static String readRequestFromFile(String filePath) {
        try {
            LOG.info("Reading test data from: {}", filePath);
            ClassPathResource resource = new ClassPathResource(filePath);
            return IOUtils.toString(resource.getInputStream(), "UTF-8");
        } catch (IOException e) {
            LOG.error("Could not read test data from: {}, error {}", filePath, e.getMessage());
            return null;
        }
    }
}
