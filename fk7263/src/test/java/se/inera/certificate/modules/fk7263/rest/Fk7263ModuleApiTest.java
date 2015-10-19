package se.inera.certificate.modules.fk7263.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static se.inera.certificate.common.enumerations.Recipients.FK;

import java.io.IOException;
import java.io.StringWriter;

import org.apache.commons.io.FileUtils;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.Difference;
import org.custommonkey.xmlunit.DifferenceConstants;
import org.custommonkey.xmlunit.DifferenceListener;
import org.custommonkey.xmlunit.ElementNameAndTextQualifier;
import org.custommonkey.xmlunit.XMLUnit;
import org.joda.time.LocalDateTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.w3.wsaddressing10.AttributedURIType;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import se.inera.certificate.integration.json.CustomObjectMapper;
import se.inera.certificate.modules.fk7263.model.converter.InternalToTransport;
import se.inera.certificate.modules.fk7263.model.internal.Utlatande;
import se.inera.certificate.modules.fk7263.utils.ResourceConverterUtils;
import se.inera.certificate.modules.fk7263.utils.ScenarioFinder;
import se.inera.certificate.modules.fk7263.utils.ScenarioNotFoundException;
import se.inera.certificate.modules.support.api.dto.CreateDraftCopyHolder;
import se.inera.certificate.modules.support.api.dto.CreateNewDraftHolder;
import se.inera.certificate.modules.support.api.dto.HoSPersonal;
import se.inera.certificate.modules.support.api.dto.InternalModelHolder;
import se.inera.certificate.modules.support.api.dto.InternalModelResponse;
import se.inera.certificate.modules.support.api.dto.Patient;
import se.inera.certificate.modules.support.api.dto.Personnummer;
import se.inera.certificate.modules.support.api.dto.Vardenhet;
import se.inera.certificate.modules.support.api.dto.Vardgivare;
import se.inera.certificate.modules.support.api.exception.ModuleException;
import se.inera.certificate.modules.support.api.exception.ModuleSystemException;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificate.rivtabp20.v3.RegisterMedicalCertificateResponderInterface;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificateresponder.v3.RegisterMedicalCertificateResponseType;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificateresponder.v3.RegisterMedicalCertificateType;
import se.inera.intyg.common.schemas.insuranceprocess.healthreporting.utils.ResultOfCallUtil;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author andreaskaltenbach
 */

@ContextConfiguration(locations = ("/fk7263-test-config.xml"))
@RunWith(SpringJUnit4ClassRunner.class)
public class Fk7263ModuleApiTest {

    public static final String TESTFILE_UTLATANDE         = "Fk7263ModuleApiTest/utlatande.json";
    public static final String TESTFILE_UTLATANDE_MINIMAL = "Fk7263ModuleApiTest/utlatande-minimal.json";
    public static final String TESTFILE_UTLATANDE_FAIL    = "Fk7263ModuleApiTest/utlatande-fail.json";

    private RegisterMedicalCertificateResponderInterface registerMedicalCertificateClient;

    @Autowired
    private Fk7263ModuleApi fk7263ModuleApi;

    @Autowired
    private ObjectMapper objectMapper;

    @Before
    public void setUpMocks() {
        registerMedicalCertificateClient = Mockito.mock(RegisterMedicalCertificateResponderInterface.class);
        fk7263ModuleApi.setRegisterMedicalCertificateClient(registerMedicalCertificateClient);
    }

    @Test
    public void testPdfFileName() {
        Utlatande intyg = new Utlatande();
        se.inera.certificate.model.common.internal.Patient patient = new se.inera.certificate.model.common.internal.Patient();
        patient.setPersonId(new Personnummer("19121212-1212"));
        intyg.getGrundData().setPatient(patient);
        // TODO Create a proper test when model has been updated.
        // assertEquals("lakarutlatande_19121212-1212_20110124-20110331.pdf", fk7263ModuleApi.pdfFileName(intyg));
    }

    @Test
    public void updateChangesHosPersonalInfo() throws IOException, ModuleException {
        Utlatande utlatande = getUtlatandeFromFile();

        InternalModelHolder holder = createInternalHolder(utlatande);
        Vardgivare vardgivare = new Vardgivare("vardgivarId", "vardgivarNamn");
        Vardenhet vardenhet = new Vardenhet("enhetId", "enhetNamn", "", "", "", "", "", "", vardgivare);
        HoSPersonal hosPerson = new HoSPersonal("nyId", "nyNamn", "nyForskrivarkod", "nyBefattning", null, vardenhet);
        LocalDateTime signingDate = LocalDateTime.parse("2014-08-01");
        InternalModelResponse updatedHolder = fk7263ModuleApi.updateBeforeSigning(holder, hosPerson, signingDate);
        Utlatande updatedIntyg = objectMapper.readValue(updatedHolder.getInternalModel(), Utlatande.class);

        assertEquals(signingDate, updatedIntyg.getGrundData().getSigneringsdatum());
        assertEquals("nyId", updatedIntyg.getGrundData().getSkapadAv().getPersonId());
        assertEquals("nyNamn", updatedIntyg.getGrundData().getSkapadAv().getFullstandigtNamn());
        assertEquals("nyForskrivarkod", updatedIntyg.getGrundData().getSkapadAv().getForskrivarKod());
        assertEquals(utlatande.getGrundData().getSkapadAv().getVardenhet().getEnhetsnamn(), updatedIntyg.getGrundData().getSkapadAv().getVardenhet()
                .getEnhetsnamn());
    }

    @Test
    public void copyContainsOriginalData() throws IOException, ModuleException {
        Utlatande utlatande = getUtlatandeFromFile();

        Patient patient = new Patient("Kalle", null, "Kula", "19121212-1212", null, null, null);
        CreateDraftCopyHolder copyHolder = createDraftCopyHolder(patient);

        InternalModelResponse holder = fk7263ModuleApi.createNewInternalFromTemplate(copyHolder, createInternalHolder(utlatande));

        assertNotNull(holder);
        Utlatande creatededUtlatande = ResourceConverterUtils.toInternal(holder.getInternalModel());
        assertEquals("2011-03-07", creatededUtlatande.getNedsattMed50().getFrom().getDate());
        assertEquals("Kalle", creatededUtlatande.getGrundData().getPatient().getFornamn());
        assertEquals("Kula", creatededUtlatande.getGrundData().getPatient().getEfternamn());
    }

    @Test
    public void copyContainsOriginalPersondetails() throws IOException, ModuleException {
        Utlatande utlatande = getUtlatandeFromFile();

        // create copyholder without Patient in it
        CreateDraftCopyHolder copyHolder = createDraftCopyHolder(null);

        InternalModelResponse holder = fk7263ModuleApi.createNewInternalFromTemplate(copyHolder, createInternalHolder(utlatande));

        assertNotNull(holder);
        Utlatande creatededUtlatande = ResourceConverterUtils.toInternal(holder.getInternalModel());
        assertEquals("Test Testorsson", creatededUtlatande.getGrundData().getPatient().getEfternamn());
    }

    @Test
    public void copyContainsNewPersonnummer() throws IOException, ModuleException {

        Personnummer newSSN = new Personnummer("19121212-1414");

        Utlatande utlatande = getUtlatandeFromFile();

        Patient patient = new Patient("Kalle", null, "Kula", "19121212-1212", null, null, null);
        CreateDraftCopyHolder copyHolder = createDraftCopyHolder(patient);
        copyHolder.setNewPersonnummer(newSSN);

        InternalModelResponse holder = fk7263ModuleApi.createNewInternalFromTemplate(copyHolder, createInternalHolder(utlatande));
        assertNotNull(holder);

        Utlatande creatededUtlatande = ResourceConverterUtils.toInternal(holder.getInternalModel());
        assertEquals("Kalle", creatededUtlatande.getGrundData().getPatient().getFornamn());
        assertEquals("Kula", creatededUtlatande.getGrundData().getPatient().getEfternamn());
        assertEquals(newSSN, creatededUtlatande.getGrundData().getPatient().getPersonId());
    }

    @Test
    public void testSendCertificateWhenRecipientIsOtherThanFk() throws Exception {
        InternalModelHolder internalModel = new InternalModelHolder(FileUtils.readFileToString(new ClassPathResource(
                TESTFILE_UTLATANDE).getFile()));

        AttributedURIType address = new AttributedURIType();
        address.setValue("logicalAddress");

        RegisterMedicalCertificateResponseType response = new RegisterMedicalCertificateResponseType();
        response.setResult(ResultOfCallUtil.okResult());

        // Wen
        when(registerMedicalCertificateClient.registerMedicalCertificate(
                any(AttributedURIType.class), any(RegisterMedicalCertificateType.class))).thenReturn(response);

        // Then
        fk7263ModuleApi.sendCertificateToRecipient(internalModel, "logicalAddress", null);

        // Verify
        verify(registerMedicalCertificateClient).registerMedicalCertificate(eq(address), Mockito.any(RegisterMedicalCertificateType.class));
    }

    @Test
    public void testSendFullCertificateWhenRecipientIsFk() throws Exception {

        // Given
        InternalModelHolder internalModel = new InternalModelHolder(FileUtils.readFileToString(new ClassPathResource(
                TESTFILE_UTLATANDE).getFile()));

        AttributedURIType address = new AttributedURIType();
        address.setValue("logicalAddress");

        RegisterMedicalCertificateResponseType response = new RegisterMedicalCertificateResponseType();
        response.setResult(ResultOfCallUtil.okResult());

        // When
        when(registerMedicalCertificateClient.registerMedicalCertificate(
                any(AttributedURIType.class), any(RegisterMedicalCertificateType.class))).thenReturn(response);

        // Then
        fk7263ModuleApi.sendCertificateToRecipient(internalModel, "logicalAddress", FK.toString());

        // Verify
        verify(registerMedicalCertificateClient).registerMedicalCertificate(Mockito.eq(address), any(RegisterMedicalCertificateType.class));
    }

    @Test
    public void testSendMinimalCertificateWhenRecipientIsFk() throws Exception {

        // Given
        InternalModelHolder internalModel = new InternalModelHolder(FileUtils.readFileToString(new ClassPathResource(
                TESTFILE_UTLATANDE_MINIMAL).getFile()));

        AttributedURIType address = new AttributedURIType();
        address.setValue("logicalAddress");

        RegisterMedicalCertificateResponseType response = new RegisterMedicalCertificateResponseType();
        response.setResult(ResultOfCallUtil.okResult());

        // When
        when(registerMedicalCertificateClient.registerMedicalCertificate(
                any(AttributedURIType.class), any(RegisterMedicalCertificateType.class))).thenReturn(response);

        // Then
        fk7263ModuleApi.sendCertificateToRecipient(internalModel, "logicalAddress", FK.toString());

        // Verify
        verify(registerMedicalCertificateClient).registerMedicalCertificate(eq(address), any(RegisterMedicalCertificateType.class));
    }

    @Test(expected = ModuleException.class)
    public void whenFkIsRecipientAndBadCertificateThenThrowException() throws Exception {

        // Given
        InternalModelHolder internalModel = new InternalModelHolder(FileUtils.readFileToString(new ClassPathResource(
                TESTFILE_UTLATANDE_FAIL).getFile()));

        AttributedURIType address = new AttributedURIType();
        address.setValue("logicalAddress");

        // Then
        fk7263ModuleApi.sendCertificateToRecipient(internalModel, "logicalAddress", FK.toString());
    }

    @Test
    public void whenFkIsRecipientThenSetCodeSystemToICD10() throws Exception {
        Utlatande utlatande = getUtlatandeFromFile();
        RegisterMedicalCertificateType request = InternalToTransport.getJaxbObject(utlatande);

        request = fk7263ModuleApi.whenFkIsRecipientThenSetCodeSystemToICD10(request);

        String expected = Fk7263ModuleApi.CODESYSTEMNAME_ICD10;
        String actual = request.getLakarutlatande().getMedicinsktTillstand().getTillstandskod().getCodeSystemName();
        assertEquals(expected, actual);
    }

    @Test(expected = ModuleException.class)
    public void whenFkIsRecipientAndNotSmittskyddAndNoMedicinsktTillstandThenThrowException() throws Exception {
        Utlatande utlatande = getUtlatandeFromFile();
        RegisterMedicalCertificateType request = InternalToTransport.getJaxbObject(utlatande);

        request.getLakarutlatande().setMedicinsktTillstand(null);

        fk7263ModuleApi.whenFkIsRecipientThenSetCodeSystemToICD10(request);
    }

    @Test(expected = ModuleException.class)
    public void whenFkIsRecipientAndNotSmittskyddAndNoTillstandskodThenThrowException() throws Exception {
        Utlatande utlatande = getUtlatandeFromFile();
        RegisterMedicalCertificateType request = InternalToTransport.getJaxbObject(utlatande);

        request.getLakarutlatande().getMedicinsktTillstand().setTillstandskod(null);

        fk7263ModuleApi.whenFkIsRecipientThenSetCodeSystemToICD10(request);
    }

    @Test
    public void testMarshall() throws ScenarioNotFoundException, ModuleException, IOException, SAXException {
        Utlatande internal = objectMapper.readValue(new ClassPathResource("Fk7263ModuleApiTest/utlatande.json").getFile(), Utlatande.class);
        StringWriter writer = new StringWriter();
        try {
            objectMapper.writeValue(writer, internal);
        } catch (IOException e) {
            throw new ModuleException("Failed to serialize internal model", e);
        }
        String xml = writer.toString();
        String actual = fk7263ModuleApi.marshall(xml);
        String expected = FileUtils.readFileToString(new ClassPathResource("Fk7263ModuleApiTest/utlatande.xml").getFile());

        XMLUnit.setIgnoreWhitespace(true);
        XMLUnit.setIgnoreAttributeOrder(true);
        Diff diff = XMLUnit.compareXML(expected, actual);
        diff.overrideDifferenceListener(new NamespacePrefixNameIgnoringListener());
        diff.overrideElementQualifier(new ElementNameAndTextQualifier());
        Assert.assertTrue(diff.toString(), diff.similar());
    }

    private class NamespacePrefixNameIgnoringListener implements DifferenceListener {
        public int differenceFound(Difference difference) {
            if (DifferenceConstants.NAMESPACE_PREFIX_ID == difference.getId()) {
                // differences in namespace prefix IDs are ok (eg. 'ns1' vs 'ns2'), as long as the namespace URI is the
                // same
                return RETURN_IGNORE_DIFFERENCE_NODES_IDENTICAL;
            } else {
                return RETURN_ACCEPT_DIFFERENCE;
            }
        }

        public void skippedComparison(Node control, Node test) {
        }
    }

    private Utlatande getUtlatandeFromFile() throws IOException {
        return new CustomObjectMapper().readValue(new ClassPathResource(
                TESTFILE_UTLATANDE).getFile(), Utlatande.class);
    }

    private Utlatande fromJsonString(String s) throws ModuleException {
        try {
            return objectMapper.readValue(s, Utlatande.class);
        } catch (IOException e) {
            throw new ModuleSystemException("Failed to deserialize internal model", e);
        }
    }

    private String toJsonString(Utlatande utlatande) throws ModuleException {
        StringWriter writer = new StringWriter();
        try {
            objectMapper.writeValue(writer, utlatande);
        } catch (IOException e) {
            throw new ModuleException("Failed to serialize internal model", e);
        }
        return writer.toString();
    }

    private CreateDraftCopyHolder createDraftCopyHolder(Patient patient) {
        Vardgivare vardgivare = new Vardgivare("hsaId0", "vardgivare");
        Vardenhet vardenhet = new Vardenhet("hsaId1", "namn", null, null, null, null, null, null, vardgivare);
        HoSPersonal hosPersonal = new HoSPersonal("Id1", "Grodan Boll", "forskrivarkod", "befattning", null, vardenhet);

        CreateDraftCopyHolder holder = new CreateDraftCopyHolder("Id1", hosPersonal);

        if (patient != null) {
            holder.setPatient(patient);
        }

        return holder;
    }

    private CreateNewDraftHolder createNewDraftHolder() {
        Vardgivare vardgivare = new Vardgivare("hsaId0", "vardgivare");
        Vardenhet vardenhet = new Vardenhet("hsaId1", "namn", null, null, null, null, null, null, vardgivare);
        HoSPersonal hosPersonal = new HoSPersonal("Id1", "Grodan Boll", "forskrivarkod", "befattning", null, vardenhet);
        Patient patient = new Patient("Kalle", null, "Kula", "19121212-1212", null, null, null);
        return new CreateNewDraftHolder("Id1", hosPersonal, patient);
    }

    private InternalModelHolder createInternalHolder(Utlatande utlatande) throws ModuleException {
        //return new InternalModelHolder(objectMapper.writeValueAsString(utlatande));
        return new InternalModelHolder(toJsonString(utlatande));
    }
}
