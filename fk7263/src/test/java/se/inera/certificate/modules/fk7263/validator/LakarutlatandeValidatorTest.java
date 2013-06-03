package se.inera.certificate.modules.fk7263.validator;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import se.inera.certificate.integration.v1.Aktivitetskod;
import se.inera.certificate.integration.v1.Lakarutlatande;
import se.inera.certificate.integration.v1.Sysselsattning;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;

/**
 * @author andreaskaltenbach
 */
public class LakarutlatandeValidatorTest {

    private static Unmarshaller UNMARSHALLER;

    @BeforeClass
    public static void setupJaxb() throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(Lakarutlatande.class);
        UNMARSHALLER = jaxbContext.createUnmarshaller();
    }

    private Lakarutlatande lakarutlatande() throws IOException, JAXBException {
        // read request from file
        JAXBElement<Lakarutlatande> request = UNMARSHALLER.unmarshal(new StreamSource(new ClassPathResource("fk7263.xml").getInputStream()), Lakarutlatande.class);
        return request.getValue();
    }

    @Test
    public void testHappyCase() throws Exception {
        assertEquals(0, new LakarutlatandeValidator(lakarutlatande()).validate().size());
    }

    @Test
    public void testMissingPatientFirstName() throws Exception {
        Lakarutlatande lakarutlatande = lakarutlatande();
        lakarutlatande.getPatient().setFornamn(null);

        assertEquals(1, new LakarutlatandeValidator(lakarutlatande).validate().size());
    }

    @Test
    public void testMissingPatientLastName() throws Exception {
        Lakarutlatande lakarutlatande = lakarutlatande();
        lakarutlatande.getPatient().setEfternamn(null);

        assertEquals(1, new LakarutlatandeValidator(lakarutlatande).validate().size());
    }

    @Test
    public void testMissingComment() throws Exception {
        Lakarutlatande lakarutlatande = lakarutlatande();
        lakarutlatande.setKommentar(null);

        assertEquals(1, new LakarutlatandeValidator(lakarutlatande).validate().size());
    }

    @Test
    public void testMissingVardkontaktOrReferens() throws Exception {
        Lakarutlatande lakarutlatande = lakarutlatande();

        // remove all vardkontakter
        lakarutlatande.getVardkontakts().clear();

        // remove all referenser
        lakarutlatande.getReferens().clear();

        assertEquals(1, new LakarutlatandeValidator(lakarutlatande).validate().size());
    }

    @Test
    public void testMissingBedomtTillstand() throws Exception {
        Lakarutlatande lakarutlatande = lakarutlatande();
        lakarutlatande.setBedomtTillstand(null);

        assertEquals(1, new LakarutlatandeValidator(lakarutlatande).validate().size());
    }

    @Test
    public void testMissingArbetsbeskrivning() throws Exception {
        Lakarutlatande lakarutlatande = lakarutlatande();
        lakarutlatande.getAktivitetsbegransning().getArbetsformaga().getSysselsattnings().add(Sysselsattning.NUVARANDE_ARBETE);
        lakarutlatande.getAktivitetsbegransning().getArbetsformaga().setArbetsuppgift(null);

        assertEquals(1, new LakarutlatandeValidator(lakarutlatande).validate().size());
    }

    @Test
    public void testDoubleRessatt() throws Exception {
        Lakarutlatande lakarutlatande = lakarutlatande();

        // set two activities with conflicting activity code
        lakarutlatande.getAktivitets().get(0).setAktivitetskod(Aktivitetskod.FORANDRAT_RESSATT_TILL_ARBETSPLATSEN_AR_AKTUELLT);
        lakarutlatande.getAktivitets().get(1).setAktivitetskod(Aktivitetskod.FORANDRAT_RESSATT_TILL_ARBETSPLATSEN_AR_EJ_AKTUELLT);

        assertEquals(1, new LakarutlatandeValidator(lakarutlatande).validate().size());
    }
}
