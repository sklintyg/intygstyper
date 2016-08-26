package se.inera.intyg.intygstyper.ts_parent.rest;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

import java.io.*;
import java.lang.reflect.Field;
import java.util.ArrayList;

import javax.xml.bind.JAXB;

import org.apache.commons.io.FileUtils;
import org.joda.time.LocalDateTime;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.io.ClassPathResource;
import org.w3.wsaddressing10.AttributedURIType;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;

import se.inera.ifv.insuranceprocess.healthreporting.revokemedicalcertificate.rivtabp20.v1.RevokeMedicalCertificateResponderInterface;
import se.inera.ifv.insuranceprocess.healthreporting.revokemedicalcertificateresponder.v1.RevokeMedicalCertificateRequestType;
import se.inera.ifv.insuranceprocess.healthreporting.revokemedicalcertificateresponder.v1.RevokeMedicalCertificateResponseType;
import se.inera.intyg.common.schemas.insuranceprocess.healthreporting.utils.ResultOfCallUtil;
import se.inera.intyg.common.support.model.common.internal.*;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.support.model.util.ModelCompareUtil;
import se.inera.intyg.common.support.modules.support.ApplicationOrigin;
import se.inera.intyg.common.support.modules.support.api.dto.*;
import se.inera.intyg.common.support.modules.support.api.exception.*;
import se.inera.intyg.common.util.integration.integration.json.CustomObjectMapper;
import se.inera.intyg.intygstyper.ts_parent.model.converter.WebcertModelFactory;
import se.inera.intyg.intygstyper.ts_parent.pdf.PdfGenerator;
import se.inera.intyg.intygstyper.ts_parent.pdf.PdfGeneratorException;
import se.inera.intyg.intygstyper.ts_parent.validator.InternalDraftValidator;
import se.riv.clinicalprocess.healthcond.certificate.getCertificate.v1.GetCertificateResponseType;
import se.riv.clinicalprocess.healthcond.certificate.types.v2.IntygId;
import se.riv.clinicalprocess.healthcond.certificate.v2.Intyg;

@RunWith(MockitoJUnitRunner.class)
public class TsParentModuleApiTest {

    private static final String INTYG_ID = "test-id";
    private static final String LOGICAL_ADDRESS = "logicalAddress";
    private static File getCertificateFile;
    private static File revokeCertificateFile;
    private static Utlatande utlatande;
    private static String json;

    @Mock
    private InternalDraftValidator<Utlatande> internalDraftValidator;

    @Mock
    private WebcertModelFactory<Utlatande> webcertModelFactory;

    @Mock
    private PdfGenerator<Utlatande> pdfGenerator;

    @Mock
    private ModelCompareUtil<Utlatande> modelCompareUtil;

    @Mock
    private RevokeMedicalCertificateResponderInterface revokeCertificateClient;

    @Spy
    private ObjectMapper objectMapper = new CustomObjectMapper();

    @SuppressWarnings("unchecked")
    @InjectMocks
    TsParentModuleApi<Utlatande> moduleApi = mock(TsParentModuleApi.class, Mockito.CALLS_REAL_METHODS);

    @BeforeClass
    public static void set() throws Exception {
        getCertificateFile = new ClassPathResource("getCertificate.xml").getFile();
        revokeCertificateFile = new ClassPathResource("revokeCertificate.xml").getFile();
        json = FileUtils.readFileToString(new ClassPathResource("utlatande.json").getFile(), Charsets.UTF_8);
        utlatande = new CustomObjectMapper().readValue(json, TestUtlatande.class);
    }

    @Before
    public void setup() throws Exception {
        Field field = TsParentModuleApi.class.getDeclaredField("type");
        field.setAccessible(true);
        field.set(moduleApi, TestUtlatande.class);
    }

    @Test
    public void testValidateDraft() throws Exception {
        when(internalDraftValidator.validateDraft(any(Utlatande.class)))
                .thenReturn(new ValidateDraftResponse(ValidationStatus.VALID, new ArrayList<>()));

        ValidateDraftResponse res = moduleApi.validateDraft(json);

        assertNotNull(res);
        assertEquals(ValidationStatus.VALID, res.getStatus());
        verify(internalDraftValidator).validateDraft(any(Utlatande.class));
    }

    @Test
    public void testCreateNewInternal() throws Exception {
        CreateNewDraftHolder draftCertificateHolder = new CreateNewDraftHolder(INTYG_ID, new HoSPersonal(), new Patient());
        String res = moduleApi.createNewInternal(draftCertificateHolder);

        assertNotNull(res);
        verify(webcertModelFactory).createNewWebcertDraft(draftCertificateHolder);
    }

    @Test(expected = ModuleConverterException.class)
    public void testCreateNewInternalConverterException() throws Exception {
        when(webcertModelFactory.createNewWebcertDraft(any(CreateNewDraftHolder.class))).thenThrow(new ConverterException());
        moduleApi.createNewInternal(new CreateNewDraftHolder(INTYG_ID, new HoSPersonal(), new Patient()));
    }

    @Test
    public void testCreateNewInternalFromTemplate() throws Exception {
        CreateDraftCopyHolder draftCopyHolder = new CreateDraftCopyHolder(INTYG_ID, new HoSPersonal());
        String res = moduleApi.createNewInternalFromTemplate(draftCopyHolder, json);

        assertNotNull(res);
        verify(webcertModelFactory).createCopy(eq(draftCopyHolder), any(Utlatande.class));
    }

    @Test(expected = ModuleConverterException.class)
    public void testCreateNewInternalFromTemplateConverterException() throws Exception {
        when(webcertModelFactory.createCopy(any(CreateDraftCopyHolder.class), any(Utlatande.class))).thenThrow(new ConverterException());
        moduleApi.createNewInternalFromTemplate(new CreateDraftCopyHolder(INTYG_ID, new HoSPersonal()), json);
    }

    @Test
    public void testCreateRenewalFromTemplate() throws Exception {
        CreateDraftCopyHolder draftCopyHolder = new CreateDraftCopyHolder(INTYG_ID, new HoSPersonal());
        String res = moduleApi.createRenewalFromTemplate(draftCopyHolder, json);

        assertNotNull(res);
        verify(webcertModelFactory).createCopy(eq(draftCopyHolder), any(Utlatande.class));
    }

    @Test(expected = ModuleConverterException.class)
    public void testCreateRenewalFromTemplateConverterException() throws Exception {
        when(webcertModelFactory.createCopy(any(CreateDraftCopyHolder.class), any(Utlatande.class))).thenThrow(new ConverterException());
        moduleApi.createRenewalFromTemplate(new CreateDraftCopyHolder(INTYG_ID, new HoSPersonal()), json);
    }

    @Test
    public void testPdf() throws Exception {
        final ApplicationOrigin applicationOrigin = ApplicationOrigin.INTYGSTJANST;
        final String fileName = "file name";
        when(pdfGenerator.generatePDF(any(Utlatande.class), any(ApplicationOrigin.class))).thenReturn(new byte[0]);
        when(pdfGenerator.generatePdfFilename(any(Utlatande.class))).thenReturn(fileName);

        PdfResponse res = moduleApi.pdf(json, new ArrayList<>(), applicationOrigin);
        assertNotNull(res);
        assertEquals(fileName, res.getFilename());
        verify(pdfGenerator).generatePDF(any(Utlatande.class), eq(applicationOrigin));
        verify(pdfGenerator).generatePdfFilename(any(Utlatande.class));
    }

    @Test(expected = ModuleSystemException.class)
    public void testPdfPdfGeneratorException() throws Exception {
        when(pdfGenerator.generatePDF(any(Utlatande.class), any(ApplicationOrigin.class))).thenThrow(new PdfGeneratorException());

        moduleApi.pdf(json, new ArrayList<>(), ApplicationOrigin.INTYGSTJANST);
    }

    @Test(expected = ModuleException.class)
    public void testPdfEmployer() throws Exception {
        moduleApi.pdfEmployer("internalModel", new ArrayList<>(), ApplicationOrigin.INTYGSTJANST, null);
    }

    @Test
    public void testShouldNotifyTrue() throws Exception {
        when(modelCompareUtil.isValidForNotification(any(Utlatande.class))).thenReturn(true);
        boolean res = moduleApi.shouldNotify("", json);
        assertTrue(res);
        verify(modelCompareUtil).isValidForNotification(any(Utlatande.class));
    }

    @Test
    public void testShouldNotifyFalse() throws Exception {
        when(modelCompareUtil.isValidForNotification(any(Utlatande.class))).thenReturn(false);
        boolean res = moduleApi.shouldNotify("", json);
        assertFalse(res);
        verify(modelCompareUtil).isValidForNotification(any(Utlatande.class));
    }

    @Test
    public void testUpdateBeforeSave() throws Exception {
        final String otherHosPersonalName = "Other Person";

        HoSPersonal hosPersonal = new HoSPersonal();
        hosPersonal.setFullstandigtNamn(otherHosPersonalName);
        String res = moduleApi.updateBeforeSave(json, hosPersonal);
        assertNotNull(res);
        assertEquals(otherHosPersonalName, moduleApi.getInternal(res).getGrundData().getSkapadAv().getFullstandigtNamn());
        assertNull(moduleApi.getInternal(res).getGrundData().getSigneringsdatum());
    }

    @Test(expected = ModuleException.class)
    public void testUpdateBeforeSaveInvalidJson() throws Exception {
        moduleApi.updateBeforeSave("invalidJson", new HoSPersonal());
    }

    @Test
    public void testUpdateBeforeSigning() throws Exception {
        final String otherHosPersonalName = "Other Person";
        final LocalDateTime signDate = LocalDateTime.now();

        HoSPersonal hosPersonal = new HoSPersonal();
        hosPersonal.setFullstandigtNamn(otherHosPersonalName);
        String res = moduleApi.updateBeforeSigning(json, hosPersonal, signDate);
        assertNotNull(res);
        assertEquals(otherHosPersonalName, moduleApi.getInternal(res).getGrundData().getSkapadAv().getFullstandigtNamn());
        assertEquals(signDate, moduleApi.getInternal(res).getGrundData().getSigneringsdatum());
    }

    @Test(expected = ModuleException.class)
    public void testUpdateBeforeSigningInvalidJson() throws Exception {
        moduleApi.updateBeforeSigning("invalidJson", new HoSPersonal(), LocalDateTime.now());
    }

    @Test
    public void testGetUtlatandeFromJson() throws Exception {
        Utlatande res = moduleApi.getUtlatandeFromJson(json);
        assertNotNull(res);
        assertEquals(INTYG_ID, res.getId());
        assertNotNull(res.getGrundData());
    }

    @Test(expected = IOException.class)
    public void testGetUtlatandeFromJsonInvalidJson() throws Exception {
        moduleApi.getUtlatandeFromJson("invalidJson");
    }

    @Test
    public void testGetIntygFromUtlatande() throws Exception {
        Intyg intyg = new Intyg();
        intyg.setIntygsId(new IntygId());
        intyg.getIntygsId().setExtension(INTYG_ID);
        doReturn(intyg).when(moduleApi).utlatandeToIntyg(any(Utlatande.class));
        Intyg res = moduleApi.getIntygFromUtlatande(new TestUtlatande());
        assertNotNull(res);
        assertEquals(INTYG_ID, res.getIntygsId().getExtension());
    }

    @Test(expected = ModuleException.class)
    public void testGetIntygFromUtlatandeConverterException() throws Exception {
        doThrow(new ConverterException()).when(moduleApi).utlatandeToIntyg(any(Utlatande.class));
        moduleApi.getIntygFromUtlatande(new TestUtlatande());
    }

    @Test
    public void testTransformToStatisticsService() throws Exception {
        final String inputString = "input string";
        String res = moduleApi.transformToStatisticsService(inputString);
        assertEquals(inputString, res);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testvalidateXml() throws Exception {
        moduleApi.validateXml("xmlBody");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetModuleSpecificArendeParameters() throws Exception {
        moduleApi.getModuleSpecificArendeParameters(new TestUtlatande(), new ArrayList<>());
    }

    @Test
    public void testGetAdditionalInfo() throws Exception {
        Intyg intyg = JAXB.unmarshal(getCertificateFile, GetCertificateResponseType.class).getIntyg();
        assertEquals("C1, C1E, C", moduleApi.getAdditionalInfo(intyg));
    }

    @Test
    public void testGetAdditionalInfoConverterException() throws Exception {
        Intyg intyg = JAXB.unmarshal(getCertificateFile, GetCertificateResponseType.class).getIntyg();
        intyg.getSvar().get(0).getDelsvar().get(0).getContent().clear();
        assertNull(moduleApi.getAdditionalInfo(intyg));
    }

    @Test
    public void testGetAdditionalInfoNoTypes() throws Exception {
        Intyg intyg = JAXB.unmarshal(getCertificateFile, GetCertificateResponseType.class).getIntyg();
        intyg.getSvar().clear();
        assertNull(moduleApi.getAdditionalInfo(intyg));
    }

    @Test
    public void testRevokeCertificate() throws Exception {
        String xmlBody = FileUtils.readFileToString(revokeCertificateFile, Charsets.UTF_8);
        RevokeMedicalCertificateResponseType revokeResponse = new RevokeMedicalCertificateResponseType();
        revokeResponse.setResult(ResultOfCallUtil.okResult());
        when(revokeCertificateClient.revokeMedicalCertificate(any(AttributedURIType.class), any(RevokeMedicalCertificateRequestType.class)))
                .thenReturn(revokeResponse);

        moduleApi.revokeCertificate(xmlBody, LOGICAL_ADDRESS);
        ArgumentCaptor<AttributedURIType> attributedUriCaptor = ArgumentCaptor.forClass(AttributedURIType.class);
        ArgumentCaptor<RevokeMedicalCertificateRequestType> parametersCaptor = ArgumentCaptor.forClass(RevokeMedicalCertificateRequestType.class);
        verify(revokeCertificateClient).revokeMedicalCertificate(attributedUriCaptor.capture(), parametersCaptor.capture());
        assertNotNull(parametersCaptor.getValue());
        assertEquals(LOGICAL_ADDRESS, attributedUriCaptor.getValue().getValue());
        assertEquals(INTYG_ID, parametersCaptor.getValue().getRevoke().getLakarutlatande().getLakarutlatandeId());
    }

    @Test(expected = ExternalServiceCallException.class)
    public void testRevokeCertificateResponseError() throws Exception {
        String xmlBody = FileUtils.readFileToString(revokeCertificateFile, Charsets.UTF_8);
        RevokeMedicalCertificateResponseType revokeResponse = new RevokeMedicalCertificateResponseType();
        revokeResponse.setResult(ResultOfCallUtil.failResult("error"));
        when(revokeCertificateClient.revokeMedicalCertificate(any(AttributedURIType.class), any(RevokeMedicalCertificateRequestType.class)))
                .thenReturn(revokeResponse);

        moduleApi.revokeCertificate(xmlBody, LOGICAL_ADDRESS);
    }

    @Test
    public void testCreateRevokeRequest() throws Exception {
        final String meddelande = "meddelande";

        String res = moduleApi.createRevokeRequest(utlatande, utlatande.getGrundData().getSkapadAv(), meddelande);
        RevokeMedicalCertificateRequestType resultObject = JAXB.unmarshal(new StringReader(res), RevokeMedicalCertificateRequestType.class);
        assertNotNull(resultObject);
        assertEquals(meddelande, resultObject.getRevoke().getMeddelande());
        assertEquals(INTYG_ID, resultObject.getRevoke().getLakarutlatande().getLakarutlatandeId());
    }

    public static class TestUtlatande implements Utlatande {

        private String typ;

        private String id;

        private String textVersion;

        private GrundData grundData = new GrundData();

        @Override
        public String getId() {
            return id;
        }

        @Override
        public String getTyp() {
            return typ;
        }

        @Override
        public GrundData getGrundData() {
            return grundData;
        }

        @Override
        public String getTextVersion() {
            return textVersion;
        }
    }

}
