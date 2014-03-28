package se.inera.certificate.modules.fk7263.rest;

import static org.junit.Assert.assertTrue;
import static se.inera.certificate.modules.support.api.dto.TransportModelVersion.LEGACY_LAKARUTLATANDE;
import static se.inera.certificate.modules.support.api.dto.TransportModelVersion.UTLATANDE_V1;

import java.io.IOException;

import javax.xml.bind.JAXBContext;

import junit.framework.Assert;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import se.inera.certificate.integration.json.CustomObjectMapper;
import se.inera.certificate.modules.fk7263.model.external.Fk7263Utlatande;
import se.inera.certificate.modules.fk7263.model.internal.Fk7263Intyg;
import se.inera.certificate.modules.support.api.dto.ExternalModelHolder;
import se.inera.certificate.modules.support.api.dto.TransportModelHolder;
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
    private JAXBContext jaxbContext;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private CustomObjectMapper objectMapper;

    @Test
    public void testSchemaValidationDuringUnmarshall() throws IOException, ModuleException {
        String utlatande = FileUtils
                .readFileToString(new ClassPathResource("Fk7263ModuleApiTest/utlatande_invalid.xml").getFile());
        try {
            fk7263ModuleApi.unmarshall(new TransportModelHolder(utlatande));

        } catch (ModuleValidationException e) {
            assertTrue(e.getValidationEntries().size() == 1);
        }
    }

    @Test
    public void testUnmarshallLegacyTransport() throws Exception {
        String utlatande = FileUtils.readFileToString(new ClassPathResource(
                "Fk7263ModuleApiTest/registerMedicalCertificate.xml").getFile());
        fk7263ModuleApi.unmarshall(new TransportModelHolder(utlatande));
    }

    @Test
    public void testUnmarshallUtlatande() throws Exception {
        String utlatande = FileUtils.readFileToString(new ClassPathResource("Fk7263ModuleApiTest/utlatande.xml")
                .getFile());

        fk7263ModuleApi.unmarshall(new TransportModelHolder(utlatande));
    }

    @Test
    public void testMarshallWithVersion_1_0() throws IOException, ModuleException {
        Fk7263Utlatande utlatande = new CustomObjectMapper().readValue(new ClassPathResource(
                "Fk7263ModuleApiTest/utlatande.json").getFile(), Fk7263Utlatande.class);

        String actual = fk7263ModuleApi.marshall(createExternalHolder(utlatande), LEGACY_LAKARUTLATANDE)
                .getTransportModel();

        assertTrue(actual.contains("lakarutlatande"));
    }

    @Test
    public void testMarshallWithVersion_2_0() throws IOException, ModuleException {
        Fk7263Utlatande utlatande = new CustomObjectMapper().readValue(new ClassPathResource(
                "Fk7263ModuleApiTest/utlatande.json").getFile(), Fk7263Utlatande.class);

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
        intyg.setPatientPersonnummer("19121212-1212");
        // TODO Create a proper test when model has been updated.
        // assertEquals("lakarutlatande_19121212-1212_20110124-20110331.pdf", fk7263ModuleApi.pdfFileName(intyg));
    }

    private ExternalModelHolder createExternalHolder(
            se.inera.certificate.modules.fk7263.model.external.Fk7263Utlatande externalModel)
            throws JsonProcessingException {
        return new ExternalModelHolder(mapper.writeValueAsString(externalModel));
    }

}
