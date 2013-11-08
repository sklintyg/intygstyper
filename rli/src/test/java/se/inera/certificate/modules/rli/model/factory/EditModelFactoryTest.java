package se.inera.certificate.modules.rli.model.factory;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import se.inera.certificate.model.HosPersonal;
import se.inera.certificate.modules.rli.rest.dto.CreateNewDraftCertificateHolder;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class EditModelFactoryTest {
    EditModelFactoryImpl factory;

    @Before
    public void setUp() throws Exception {
        factory = new EditModelFactoryImpl();
    }

    @Test
    public void testCreateEditableModel() throws JsonParseException, JsonMappingException, IOException {
        CreateNewDraftCertificateHolder draftCertHolder = new CreateNewDraftCertificateHolder();

        HosPersonal skapadAv = new ObjectMapper().readValue(new ClassPathResource("initial-parameters.json").getFile(),
                HosPersonal.class);

        draftCertHolder.setCertificateId("new_ID");
        draftCertHolder.setSkapadAv(skapadAv);

        se.inera.certificate.modules.rli.model.edit.Utlatande utlatande = factory.createEditableUtlatande(draftCertHolder);

        assertNotNull(utlatande);
        assertNotNull(utlatande.getSkapadAv());
    }
}
