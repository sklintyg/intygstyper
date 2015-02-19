package se.inera.certificate.modules.fk7263.model.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.mockito.Matchers.anyString;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.joda.time.LocalDateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import se.inera.certificate.clinicalprocess.healthcond.certificate.certificatestatusupdateforcareresponder.v1.CertificateStatusUpdateForCareType;
import se.inera.certificate.clinicalprocess.healthcond.certificate.types.v1.HandelsekodKodRestriktion;
import se.inera.certificate.codes.Diagnoskodverk;
import se.inera.certificate.integration.json.CustomObjectMapper;
import se.inera.certificate.modules.fk7263.model.converter.util.ConverterUtil;
import se.inera.certificate.modules.service.WebcertModuleService;
import se.inera.certificate.modules.support.api.notification.FragaSvar;
import se.inera.certificate.modules.support.api.notification.HandelseType;
import se.inera.certificate.modules.support.api.notification.NotificationMessage;

@RunWith(MockitoJUnitRunner.class)
public class InternalToNotificationTest {

    private static final Logger LOG = LoggerFactory.getLogger(InternalToNotification.class);

    private static final String FK7263 = "fk7263";

    private static final String LOGISK_ADRESS = "123456789";

    private static final String INTYGS_ID = "intyg-1";
    
    @Mock
    private WebcertModuleService mockModuleService;
    
    @Spy
    private ConverterUtil converterUtil = setupConverterUtil();
    
    @InjectMocks
    private InternalToNotification converter;
    
    @Before
    public void setupMockDiagnosAnswer() {
        when(mockModuleService.validateDiagnosisCode(anyString(), any(Diagnoskodverk.class))).then(new Answer<Boolean>() {
            @Override
            public Boolean answer(InvocationOnMock invocation) throws Throwable {
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
                
                if (diagnoskod.equals("S47")) {
                    return Boolean.TRUE;
                }
                
                return Boolean.FALSE;
            }
        });
    }
    
    @Test
    public void testWithFullyPopulatedUtlatande() throws Exception {
        String json = readRequestFromFile("InternalToNotificationTest/utlatande-intyg-1.json");
        
        NotificationMessage msg = new NotificationMessage(INTYGS_ID, FK7263, LocalDateTime.now(), HandelseType.INTYGSUTKAST_ANDRAT, LOGISK_ADRESS, json, FragaSvar.getEmpty());
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
        assertEquals(HandelseType.INTYGSUTKAST_ANDRAT.toString(), res.getUtlatande().getHandelse().getHandelsekod().getDisplayName());
        
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
        assertNotNull(res.getUtlatande().getDiagnos().getDisplayName());
        assertTrue(res.getUtlatande().getDiagnos().getDisplayName().contains("Klämskada"));
        
        assertEquals(2, res.getUtlatande().getArbetsformaga().size());
        
        assertNotNull(res.getUtlatande().getFragorOchSvar());
    }
    
    @Test
    public void testWithSignedMinimal() throws Exception {
        String json = readRequestFromFile("InternalToNotificationTest/utlatande-intyg-2.json");
        
        NotificationMessage msg = new NotificationMessage("intyg-2", FK7263, LocalDateTime.now(), HandelseType.INTYGSUTKAST_SIGNERAT, LOGISK_ADRESS, json, FragaSvar.getEmpty());
        CertificateStatusUpdateForCareType res = converter.createCertificateStatusUpdateForCareType(msg);
        
        assertNotNull(res.getUtlatande());
        
        assertEquals(HandelsekodKodRestriktion.HAN_2.value(), res.getUtlatande().getHandelse().getHandelsekod().getCode());
        assertEquals(HandelseType.INTYGSUTKAST_SIGNERAT.toString(), res.getUtlatande().getHandelse().getHandelsekod().getDisplayName());
        
        // is signed
        assertNotNull(res.getUtlatande().getSigneringsdatum());
        
        // no diagnosis in this one
        assertNull(res.getUtlatande().getDiagnos());
        
        assertEquals(1, res.getUtlatande().getArbetsformaga().size());
    }
    
    @Test
    public void testWithInvalidDiagnosisCode() throws Exception {
        String json = readRequestFromFile("InternalToNotificationTest/utlatande-intyg-3.json");
        
        NotificationMessage msg = new NotificationMessage("intyg-3", FK7263, LocalDateTime.now(), HandelseType.INTYGSUTKAST_ANDRAT, LOGISK_ADRESS, json, FragaSvar.getEmpty());
        CertificateStatusUpdateForCareType res = converter.createCertificateStatusUpdateForCareType(msg);
        
        assertNotNull(res.getUtlatande());
        
        assertEquals(HandelsekodKodRestriktion.HAN_11.value(), res.getUtlatande().getHandelse().getHandelsekod().getCode());
        assertEquals(HandelseType.INTYGSUTKAST_ANDRAT.toString(), res.getUtlatande().getHandelse().getHandelsekod().getDisplayName());
        
        // is not signed
        assertNull(res.getUtlatande().getSigneringsdatum());
        
        // no diagnosis in this one since the one supplied is invalid
        assertNull(res.getUtlatande().getDiagnos());
    }
    
    @Test
    public void testWithQuestionFromFK() throws Exception {
        String json = readRequestFromFile("InternalToNotificationTest/utlatande-intyg-2.json");
        
        FragaSvar fs = new FragaSvar(1, 0, 0, 0);
        NotificationMessage msg = new NotificationMessage("intyg-2", FK7263, LocalDateTime.now(), HandelseType.FRAGA_FRAN_FK, LOGISK_ADRESS, json, fs);
        CertificateStatusUpdateForCareType res = converter.createCertificateStatusUpdateForCareType(msg);
        
        assertNotNull(res.getUtlatande());
        
        assertEquals(HandelsekodKodRestriktion.HAN_6.value(), res.getUtlatande().getHandelse().getHandelsekod().getCode());
        assertEquals(HandelseType.FRAGA_FRAN_FK.toString(), res.getUtlatande().getHandelse().getHandelsekod().getDisplayName());
        
        // is signed
        assertNotNull(res.getUtlatande().getSigneringsdatum());
        
        // no diagnosis in this one
        assertNull(res.getUtlatande().getDiagnos());
        
        assertEquals(1, res.getUtlatande().getArbetsformaga().size());
        
        assertNotNull(res.getUtlatande().getFragorOchSvar());
        assertEquals(1, res.getUtlatande().getFragorOchSvar().getAntalFragor());
        assertEquals(0, res.getUtlatande().getFragorOchSvar().getAntalSvar());
    }
    
    private ConverterUtil setupConverterUtil() {
        ConverterUtil co = new ConverterUtil();
        co.setObjectMapper(new CustomObjectMapper());
        return co;
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
