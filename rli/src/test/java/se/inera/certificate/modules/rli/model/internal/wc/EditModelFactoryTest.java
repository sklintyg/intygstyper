package se.inera.certificate.modules.rli.model.internal.wc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import se.inera.certificate.modules.rli.model.converter.ConverterException;
import se.inera.certificate.modules.rli.model.converter.WebcertModelFactory;
import se.inera.certificate.modules.rli.rest.dto.CreateNewDraftCertificateHolder;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class EditModelFactoryTest {
    WebcertModelFactory factory;

    @Before
    public void setUp() throws Exception {
        factory = new WebcertModelFactory();
    }

    @Test
    public void testCreateEditableModel() throws JsonParseException, JsonMappingException, IOException {
        CreateNewDraftCertificateHolder draftCertHolder = new ObjectMapper().readValue(new ClassPathResource(
                "initial-parameters.json").getFile(), CreateNewDraftCertificateHolder.class);

        se.inera.certificate.modules.rli.model.internal.wc.Utlatande utlatande = null;

        try {
            utlatande = factory.createNewWebcertDraft(draftCertHolder);
        } catch (ConverterException e) {
            e.printStackTrace();
        }

        assertNotNull(utlatande);
        assertNotNull(utlatande.getSkapadAv());

        /** Just verify some stuff from the json to make sure all is well.. */
        assertEquals("testID", utlatande.getUtlatandeid());
        assertEquals("johnny appleseed", utlatande.getPatient().getFullstandigtNamn());

        /** Kinda stupid, but verify that correct dateformat is used */
        assertEquals(LocalDate.now().toString(), utlatande.getUndersokning().getUndersokningsdatum());
    }
}
