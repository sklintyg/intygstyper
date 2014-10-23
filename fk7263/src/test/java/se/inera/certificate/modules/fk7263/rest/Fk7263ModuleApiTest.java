package se.inera.certificate.modules.fk7263.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static se.inera.certificate.modules.support.api.dto.TransportModelVersion.LEGACY_LAKARUTLATANDE;
import static se.inera.certificate.modules.support.api.dto.TransportModelVersion.UTLATANDE_V1;

import java.io.IOException;

import junit.framework.Assert;

import org.apache.commons.io.FileUtils;
import org.joda.time.LocalDateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import se.inera.certificate.integration.json.CustomObjectMapper;
import se.inera.certificate.modules.fk7263.model.external.Fk7263Utlatande;
import se.inera.certificate.modules.fk7263.model.internal.Fk7263Intyg;
import se.inera.certificate.modules.fk7263.utils.ResourceConverterUtils;
import se.inera.certificate.modules.support.api.dto.CreateNewDraftHolder;
import se.inera.certificate.modules.support.api.dto.ExternalModelHolder;
import se.inera.certificate.modules.support.api.dto.HoSPersonal;
import se.inera.certificate.modules.support.api.dto.InternalModelHolder;
import se.inera.certificate.modules.support.api.dto.InternalModelResponse;
import se.inera.certificate.modules.support.api.dto.Patient;
import se.inera.certificate.modules.support.api.dto.TransportModelHolder;
import se.inera.certificate.modules.support.api.dto.Vardenhet;
import se.inera.certificate.modules.support.api.dto.Vardgivare;
import se.inera.certificate.modules.support.api.exception.ModuleException;
import se.inera.certificate.modules.support.api.exception.ModuleValidationException;
import se.inera.certificate.modules.support.api.exception.ModuleVersionUnsupportedException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author andreaskaltenbach
 */

@ContextConfiguration(locations = ("/fk7263-test-config.xml"))
@RunWith(SpringJUnit4ClassRunner.class)
public class Fk7263ModuleApiTest {

    @Autowired
    private se.inera.certificate.modules.support.api.ModuleApi fk7263ModuleApi;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private CustomObjectMapper objectMapper;

    @Test
    public void testSchemaValidationDuringUnmarshall() throws IOException, ModuleException {
        String utlatande = FileUtils.readFileToString(new ClassPathResource("Fk7263ModuleApiTest/utlatande_invalid.xml").getFile());
        try {
            fk7263ModuleApi.unmarshall(new TransportModelHolder(utlatande));
        } catch (ModuleValidationException e) {
            assertTrue(e.getValidationEntries().size() == 1);
        }
    }

    @Test
    public void testUnmarshallLegacyTransport() throws Exception {
        String utlatande = FileUtils.readFileToString(new ClassPathResource("Fk7263ModuleApiTest/registerMedicalCertificate.xml").getFile());
        fk7263ModuleApi.unmarshall(new TransportModelHolder(utlatande));
    }

    @Test
    public void testUnmarshallUtlatande() throws Exception {
        String utlatande = FileUtils.readFileToString(new ClassPathResource("Fk7263ModuleApiTest/utlatande.xml").getFile());

        fk7263ModuleApi.unmarshall(new TransportModelHolder(utlatande));
    }

    @Test
    public void testMarshallWithVersion_1_0() throws IOException, ModuleException {
        Fk7263Utlatande utlatande = new CustomObjectMapper().readValue(
                new ClassPathResource("Fk7263ModuleApiTest/utlatande.json").getFile(), Fk7263Utlatande.class);

        String actual = fk7263ModuleApi.marshall(createExternalHolder(utlatande), LEGACY_LAKARUTLATANDE).getTransportModel();

        assertTrue(actual.contains("lakarutlatande"));
    }

    @Test
    public void testMarshallWithVersion_2_0() throws IOException, ModuleException {
        Fk7263Utlatande utlatande = new CustomObjectMapper().readValue(
                new ClassPathResource("Fk7263ModuleApiTest/utlatande.json").getFile(), Fk7263Utlatande.class);

        String actual = fk7263ModuleApi.marshall(createExternalHolder(utlatande), UTLATANDE_V1).getTransportModel();

        assertTrue(actual.contains("utlatande"));
    }

    @Test
    public void testMarshallWithInvalidVersion() throws IOException, ModuleException {
        Fk7263Utlatande utlatande = new CustomObjectMapper().readValue(new ClassPathResource(
                "Fk7263ModuleApiTest/utlatande.json").getFile(), Fk7263Utlatande.class);

        try {
            fk7263ModuleApi.marshall(createExternalHolder(utlatande), null);
            Assert.fail("Expected ModuleVersionUnsupportedException");

        } catch (ModuleVersionUnsupportedException ignore) {
        }
    }

    @Test
    public void testPdfFileName() {
        Fk7263Intyg intyg = new Fk7263Intyg();
        se.inera.certificate.model.common.internal.Patient patient = new se.inera.certificate.model.common.internal.Patient();
        patient.setPersonId("19121212-1212");
        intyg.getIntygMetadata().setPatient(patient);
        // TODO Create a proper test when model has been updated.
        // assertEquals("lakarutlatande_19121212-1212_20110124-20110331.pdf", fk7263ModuleApi.pdfFileName(intyg));
    }

    @Test
    public void updateChangesHosPersonalInfo() throws IOException, ModuleException {
        Fk7263Utlatande utlatande = new CustomObjectMapper().readValue(new ClassPathResource(
                "Fk7263ModuleApiTest/utlatande.json").getFile(), Fk7263Utlatande.class);

        InternalModelResponse internalModelResponse = fk7263ModuleApi.convertExternalToInternal(createExternalHolder(utlatande));
        InternalModelHolder holder = new InternalModelHolder(internalModelResponse.getInternalModel());
        Vardgivare vardgivare = new Vardgivare("vardgivarId", "vardgivarNamn");
        Vardenhet vardenhet = new Vardenhet("enhetId", "enhetNamn", "", "", "", "", "", "", vardgivare);
        HoSPersonal hosPerson = new HoSPersonal("nyId", "nyNamn", "nyForskrivarkod", "nyBefattning", null, vardenhet);
        LocalDateTime signingDate = LocalDateTime.parse("2014-08-01");
        InternalModelResponse updatedHolder = fk7263ModuleApi.updateInternal(holder, hosPerson, signingDate);
        Fk7263Intyg updatedIntyg = mapper.readValue(updatedHolder.getInternalModel(), Fk7263Intyg.class);

        assertEquals(signingDate, updatedIntyg.getIntygMetadata().getSigneringsdatum());
        assertEquals("nyId", updatedIntyg.getIntygMetadata().getSkapadAv().getPersonId());
        assertEquals("nyNamn", updatedIntyg.getIntygMetadata().getSkapadAv().getFullstandigtNamn());
        assertEquals("nyForskrivarkod", updatedIntyg.getIntygMetadata().getSkapadAv().getForskrivarKod());
        assertEquals(utlatande.getSkapadAv().getVardenhet().getNamn(), updatedIntyg.getIntygMetadata().getSkapadAv().getVardenhet().getEnhetsnamn());
    }

    @Test
    public void copyContainsOriginalData() throws IOException, ModuleException {
        Fk7263Utlatande utlatande = new CustomObjectMapper().readValue(new ClassPathResource("Fk7263ModuleApiTest/utlatande.json").getFile(), Fk7263Utlatande.class);

        InternalModelResponse holder = fk7263ModuleApi.createNewInternalFromTemplate(createNewDraftHolder(), createExternalHolder(utlatande));

        assertNotNull(holder);
        Fk7263Intyg creatededUtlatande = ResourceConverterUtils.toInternal(holder.getInternalModel());
        assertEquals("2011-03-07", creatededUtlatande.getNedsattMed50().getFrom().getDate());
    }

    private CreateNewDraftHolder createNewDraftHolder() {
        Vardgivare vardgivare = new Vardgivare("hsaId0", "vardgivare");
        Vardenhet vardenhet = new Vardenhet("hsaId1", "namn", null, null, null, null, null, null, vardgivare);
        HoSPersonal hosPersonal = new HoSPersonal("Id1", "Grodan Boll", "forskrivarkod", "befattning", null,  vardenhet);
        Patient patient = new Patient("Kalle", null, "Kula", "19121212-1212", null, null, null);
        return new CreateNewDraftHolder("Id1", hosPersonal, patient);
    }

    private ExternalModelHolder createExternalHolder(se.inera.certificate.modules.fk7263.model.external.Fk7263Utlatande externalModel)
            throws JsonProcessingException {
        return new ExternalModelHolder(mapper.writeValueAsString(externalModel));
    }
}
