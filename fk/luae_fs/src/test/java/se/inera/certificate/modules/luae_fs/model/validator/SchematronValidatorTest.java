package se.inera.certificate.modules.luae_fs.model.validator;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import org.junit.Test;
import se.inera.certificate.modules.fkparent.integration.RegisterCertificateValidator;
import se.inera.certificate.modules.fkparent.model.validator.XmlValidator;
import se.inera.intyg.common.support.modules.support.api.dto.ValidateXmlResponse;

import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SchematronValidatorTest {

    private static final RegisterCertificateValidator VALIDATOR = new RegisterCertificateValidator("aktivitetsersattning-fs.sch");

    @Test
    public void validXmlPassesTest() throws Exception {
        String inputXml = Resources.toString(getResource("transport/scenarios/pass-minimal.xml"), Charsets.UTF_8);
        ValidateXmlResponse response = XmlValidator.validate(VALIDATOR, inputXml);
        response.getValidationErrors().forEach(e -> System.out.println(e));
        assertTrue(response.getValidationErrors().isEmpty());
    }

    @Test
    public void validXmlFailsTestWhenDiagnosMissing() throws Exception {
        String inputXml = Resources.toString(getResource("transport/scenarios/fail-diagnossaknas.xml"), Charsets.UTF_8);
        ValidateXmlResponse response = XmlValidator.validate(VALIDATOR, inputXml);
        response.getValidationErrors().forEach(e -> System.out.println(e));
        assertEquals(1, response.getValidationErrors().size());
    }

    @Test
    public void validXmlFailsTestWhenFinnsUnderlagMissing() throws Exception {
        String inputXml = Resources.toString(getResource("transport/scenarios/fail-nounderlagfinnes.xml"), Charsets.UTF_8);
        ValidateXmlResponse response = XmlValidator.validate(VALIDATOR, inputXml);
        response.getValidationErrors().forEach(e -> System.out.println(e));
        assertEquals(1, response.getValidationErrors().size());
    }

    @Test
    public void validXmlFailsTestWhenFourDiagnoser() throws Exception {
        String inputXml = Resources.toString(getResource("transport/scenarios/fail-fyradiagnoser.xml"), Charsets.UTF_8);
        ValidateXmlResponse response = XmlValidator.validate(VALIDATOR, inputXml);
        response.getValidationErrors().forEach(e -> System.out.println(e));
        assertEquals(1, response.getValidationErrors().size());
    }

//    @Test
//    public void validXmlPassesTest() throws Exception {
//        String inputXml = Resources.toString(getResource("transport/luae_fs-2.xml"), Charsets.UTF_8);
//        ValidateXmlResponse response = XmlValidator.validate(VALIDATOR, inputXml);
//        assertTrue(response.getValidationErrors().isEmpty());
//    }
//
//    @Test
//    public void invalidAntalDiagnoser() throws Exception {
//        String inputXml = Resources.toString(getResource("transport/diagnosMaxTreDiagnoser.xml"), Charsets.UTF_8);
//        ValidateXmlResponse response = XmlValidator.validate(VALIDATOR, inputXml);
//        assertFalse(response.getValidationErrors().isEmpty());
//    }

    private static URL getResource(String href) {
        return Thread.currentThread().getContextClassLoader().getResource(href);
    }
}
