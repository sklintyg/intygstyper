package se.inera.certificate.modules.fk7263.validator;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import se.inera.certificate.integration.json.CustomObjectMapper;
import se.inera.certificate.model.Id;
import se.inera.certificate.modules.fk7263.model.external.Fk7263Utlatande;

/**
 * @author andreaskaltenbach, marced
 */
public class ProgrammaticLegacyTransportSchemaValidatorTest {

    private Fk7263Utlatande getValidUtlatande() throws IOException {
        // read valid certificate from file
        return new CustomObjectMapper().readValue(new ClassPathResource(
                "ProgrammaticLegacyTransportSchemaValidatorTest/external_model_utlatande.json").getFile(),
                Fk7263Utlatande.class);
    }

    @Test
    public void testHappyCase() throws Exception {
        assertEquals(0, new ProgrammaticLegacyTransportSchemaValidator(getValidUtlatande()).validate().size());
    }

    @Test
    public void testMissingUtlatandeTyp() throws Exception {
        Fk7263Utlatande utlatande = getValidUtlatande();

        utlatande.setTyp(null);

        assertEquals(2, new ProgrammaticLegacyTransportSchemaValidator(utlatande).validate().size());
    }

    @Test
    public void testInvalidUtlatandeTypCodeSystem() throws Exception {
        Fk7263Utlatande utlatande = getValidUtlatande();

        utlatande.getTyp().setCodeSystem(null);

        assertEquals(1, new ProgrammaticLegacyTransportSchemaValidator(utlatande).validate().size());
    }

    @Test
    public void testInvalidUtlatandeTypCode() throws Exception {
        Fk7263Utlatande utlatande = getValidUtlatande();

        utlatande.getTyp().setCode(null);

        assertEquals(1, new ProgrammaticLegacyTransportSchemaValidator(utlatande).validate().size());
    }

    @Test
    public void testMissingUtlatandeId() throws Exception {
        Fk7263Utlatande utlatande = getValidUtlatande();

        utlatande.setId(null);

        assertEquals(1, new ProgrammaticLegacyTransportSchemaValidator(utlatande).validate().size());
    }

    @Test
    public void testMissingUtlatandeIdRoot() throws Exception {
        Fk7263Utlatande utlatande = getValidUtlatande();

        utlatande.getId().setRoot("");

        assertEquals(1, new ProgrammaticLegacyTransportSchemaValidator(utlatande).validate().size());
    }

    @Test
    public void testMissingSigneringsDatum() throws Exception {
        Fk7263Utlatande utlatande = getValidUtlatande();

        // remove signeringsdatum
        utlatande.setSigneringsdatum(null);

        assertEquals(1, new ProgrammaticLegacyTransportSchemaValidator(utlatande).validate().size());
    }

    @Test
    public void testMissingSkickatDatum() throws Exception {
        Fk7263Utlatande utlatande = getValidUtlatande();

        // remove skickatdatum
        utlatande.setSkickatdatum(null);

        assertEquals(1, new ProgrammaticLegacyTransportSchemaValidator(utlatande).validate().size());
    }

    @Test
    public void testMissingPatient() throws Exception {
        Fk7263Utlatande utlatande = getValidUtlatande();

        utlatande.setPatient(null);

        assertEquals(1, new ProgrammaticLegacyTransportSchemaValidator(utlatande).validate().size());
    }

    @Test
    public void testMissingPatientId() throws Exception {
        Fk7263Utlatande utlatande = getValidUtlatande();

        utlatande.getPatient().setId(null);

        assertEquals(1, new ProgrammaticLegacyTransportSchemaValidator(utlatande).validate().size());
    }

    @Test
    public void testInvalidPatientIdRoot() throws Exception {
        Fk7263Utlatande utlatande = getValidUtlatande();

        utlatande.getPatient().setId(new Id("1111111", "19121212-1212"));

        assertEquals(1, new ProgrammaticLegacyTransportSchemaValidator(utlatande).validate().size());
    }

    @Test
    public void testInvalidPatientIdExtension() throws Exception {
        Fk7263Utlatande utlatande = getValidUtlatande();

        utlatande.getPatient().setId(new Id("1.2.752.129.2.1.3.1", "1233333"));

        assertEquals(1, new ProgrammaticLegacyTransportSchemaValidator(utlatande).validate().size());
    }

    @Test
    public void testInvalidPatientName() throws Exception {
        Fk7263Utlatande utlatande = getValidUtlatande();

        // remove name
        utlatande.getPatient().setEfternamn(null);

        assertEquals(1, new ProgrammaticLegacyTransportSchemaValidator(utlatande).validate().size());
    }

    @Test
    public void testInvalidHosPersonalNamn() throws Exception {
        Fk7263Utlatande utlatande = getValidUtlatande();

        // break hos-personal id extension
        utlatande.getSkapadAv().setNamn(null);

        assertEquals(1, new ProgrammaticLegacyTransportSchemaValidator(utlatande).validate().size());
    }

}
