package se.inera.certificate.modules.fk7263.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;

import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.joda.time.LocalDateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.w3.wsaddressing10.AttributedURIType;

import se.inera.certificate.modules.fk7263.model.internal.Utlatande;
import se.inera.certificate.modules.fk7263.utils.ResourceConverterUtils;
import se.inera.certificate.modules.support.api.dto.*;
import se.inera.certificate.modules.support.api.exception.ModuleException;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificate.v3.rivtabp20.RegisterMedicalCertificateResponderInterface;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificateresponder.v3.RegisterMedicalCertificateResponseType;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificateresponder.v3.RegisterMedicalCertificateType;
import se.inera.ifv.insuranceprocess.healthreporting.utils.ResultOfCallUtil;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author andreaskaltenbach
 */

@ContextConfiguration(locations = ("/fk7263-test-config.xml"))
@RunWith(SpringJUnit4ClassRunner.class)
public class Fk7263ModuleApiTest {

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
        patient.setPersonId("19121212-1212");
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
        assertEquals(utlatande.getGrundData().getSkapadAv().getVardenhet().getEnhetsnamn(), updatedIntyg.getGrundData().getSkapadAv().getVardenhet().getEnhetsnamn());
    }

    private Utlatande getUtlatandeFromFile() throws IOException {
        return objectMapper.readValue(new ClassPathResource(
                "Fk7263ModuleApiTest/utlatande.json").getFile(), Utlatande.class);
    }

    @Test
    public void copyContainsOriginalData() throws IOException, ModuleException {
        Utlatande utlatande = getUtlatandeFromFile();

        InternalModelResponse holder = fk7263ModuleApi.createNewInternalFromTemplate(createNewDraftHolder(), createInternalHolder(utlatande));

        assertNotNull(holder);
        Utlatande creatededUtlatande = ResourceConverterUtils.toInternal(holder.getInternalModel());
        assertEquals("2011-03-07", creatededUtlatande.getNedsattMed50().getFrom().getDate());
    }
    
    @Test
    public void testSendCertificateToRecipient() throws Exception {
        InternalModelHolder internalModel = new InternalModelHolder(FileUtils.readFileToString(new ClassPathResource("Fk7263ModuleApiTest/utlatande.json").getFile()));
        AttributedURIType address = new AttributedURIType();
        address.setValue("logicalAddress");
        RegisterMedicalCertificateResponseType response = new RegisterMedicalCertificateResponseType();
        response.setResult(ResultOfCallUtil.okResult());
        Mockito.when(registerMedicalCertificateClient.registerMedicalCertificate(Mockito.any(AttributedURIType.class), Mockito.any(RegisterMedicalCertificateType.class))).thenReturn(response);
        fk7263ModuleApi.sendCertificateToRecipient(internalModel, "logicalAddress");
        verify(registerMedicalCertificateClient).registerMedicalCertificate(Mockito.eq(address), Mockito.any(RegisterMedicalCertificateType.class));
    }

    private CreateNewDraftHolder createNewDraftHolder() {
        Vardgivare vardgivare = new Vardgivare("hsaId0", "vardgivare");
        Vardenhet vardenhet = new Vardenhet("hsaId1", "namn", null, null, null, null, null, null, vardgivare);
        HoSPersonal hosPersonal = new HoSPersonal("Id1", "Grodan Boll", "forskrivarkod", "befattning", null,  vardenhet);
        Patient patient = new Patient("Kalle", null, "Kula", "19121212-1212", null, null, null);
        return new CreateNewDraftHolder("Id1", hosPersonal, patient);
    }

    private InternalModelHolder createInternalHolder(Utlatande internalModel)
            throws JsonProcessingException {
        return new InternalModelHolder(objectMapper.writeValueAsString(internalModel));
    }
}
