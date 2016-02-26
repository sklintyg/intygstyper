package se.inera.certificate.modules.sjukpenning_utokad.model.validator;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.net.URL;

import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

import se.inera.certificate.modules.fkparent.integration.RegisterCertificateValidator;
import se.inera.certificate.modules.fkparent.model.validator.XmlValidator;
import se.inera.intyg.common.support.modules.support.api.dto.ValidateXmlResponse;

public class SchematronValidatorTest {

    private static final RegisterCertificateValidator VALIDATOR = new RegisterCertificateValidator("sjukpenning-utokat.sch");

    @Test
    public void brokenXmlFailsTest() throws Exception {
        String inputXml = Resources.toString(getResource("transport/sjukpenning-utokat2_broken.xml"), Charsets.UTF_8);
        ValidateXmlResponse response = XmlValidator.validate(VALIDATOR, inputXml);
        response.getValidationErrors().forEach(e -> System.out.println(e));
        assertFalse(response.getValidationErrors().isEmpty());
    }

    @Test
    public void validXmlPassesTest() throws Exception {
        String inputXml = Resources.toString(getResource("transport/sjukpenning-utokat2.xml"), Charsets.UTF_8);
        ValidateXmlResponse response = XmlValidator.validate(VALIDATOR, inputXml);
        assertTrue(response.getValidationErrors().isEmpty());
    }

    @Test
    public void invalidAntalDiagnoser() throws Exception {
        String inputXml = Resources.toString(getResource("transport/diagnosMaxTreDiagnoser.xml"), Charsets.UTF_8);
        ValidateXmlResponse response = XmlValidator.validate(VALIDATOR, inputXml);
        assertFalse(response.getValidationErrors().isEmpty());
    }

    private static URL getResource(String href) {
        return Thread.currentThread().getContextClassLoader().getResource(href);
    }
}
