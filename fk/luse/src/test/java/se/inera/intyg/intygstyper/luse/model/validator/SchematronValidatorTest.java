package se.inera.intyg.intygstyper.luse.model.validator;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.net.URL;

import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.helger.commons.debug.GlobalDebug;

import se.inera.intyg.common.support.modules.support.api.dto.ValidateXmlResponse;
import se.inera.intyg.intygstyper.fkparent.integration.RegisterCertificateValidator;
import se.inera.intyg.intygstyper.fkparent.model.validator.XmlValidator;

public class SchematronValidatorTest {

    private static final RegisterCertificateValidator LUSE_VALIDATOR = new RegisterCertificateValidator("luse.sch");

    static {
        // avoid com.helger debug log
        GlobalDebug.setDebugModeDirect(false);
    }

    @Test
    public void brokenXmlFails() throws Exception {
        String inputXml = Resources.toString(getResource("luse2_broken.xml"), Charsets.UTF_8);
        ValidateXmlResponse response = XmlValidator.validate(LUSE_VALIDATOR, inputXml);
        assertFalse(response.getValidationErrors().isEmpty());
    }

    @Test
    public void diagnosMedBidiagnos2UtanBidiagnos1Fails() throws Exception {
        String inputXml = Resources.toString(getResource("med-bidiagnos2-utan-bidiagnos1_broken.xml"), Charsets.UTF_8);
        ValidateXmlResponse response = XmlValidator.validate(LUSE_VALIDATOR, inputXml);
        assertFalse(response.getValidationErrors().isEmpty());
    }

    @Test
    public void validXmlPasses() throws Exception {
        String inputXml = Resources.toString(getResource("luse2.xml"), Charsets.UTF_8);
        ValidateXmlResponse response = XmlValidator.validate(LUSE_VALIDATOR, inputXml);
        assertTrue(response.getValidationErrors().isEmpty());
    }

    private static URL getResource(String href) {
        return Thread.currentThread().getContextClassLoader().getResource(href);
    }
}
