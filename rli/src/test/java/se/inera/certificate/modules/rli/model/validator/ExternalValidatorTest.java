package se.inera.certificate.modules.rli.model.validator;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import se.inera.certificate.integration.json.CustomObjectMapper;
import se.inera.certificate.modules.rli.model.external.Utlatande;
import se.inera.certificate.modules.rli.validator.ExternalValidatorImpl;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ExternalValidatorTest {

    private ExternalValidatorImpl validator;

    @Before
    public void setUp() throws Exception {
        validator = new ExternalValidatorImpl();
    }

    private Utlatande buildTestUtlatande(String filename) {
        ObjectMapper mapper = new CustomObjectMapper();
        Utlatande utlatande = new Utlatande();

        try {
            utlatande = mapper.readValue(new InputStreamReader(new ClassPathResource(filename).getInputStream()),
                    Utlatande.class);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return utlatande;
    }

    @Test
    public void testValidate() {
        Utlatande utlatande = buildTestUtlatande("rli-example-1-external.json");
        List<String> validationErrors = validator.validate(utlatande);

        assertEquals("Could not parse the SSN '191212121212' (format should be 'yyyyMMdd-nnnn')",
                validationErrors.get(0));
    }
}
