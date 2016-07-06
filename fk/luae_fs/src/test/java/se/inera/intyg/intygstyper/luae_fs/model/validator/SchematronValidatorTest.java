package se.inera.intyg.intygstyper.luae_fs.model.validator;

import static org.junit.Assert.assertEquals;
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

    private static final RegisterCertificateValidator VALIDATOR = new RegisterCertificateValidator("luae_fs.sch");

    static {
        // avoid com.helger debug log
        GlobalDebug.setDebugModeDirect(false);
    }

    @Test
    public void validXmlPassesTest() throws Exception {
        String inputXml = Resources.toString(getResource("transport/scenarios/pass-minimal.xml"), Charsets.UTF_8);
        ValidateXmlResponse response = XmlValidator.validate(VALIDATOR, inputXml);
        assertTrue(response.getValidationErrors().isEmpty());
    }

    @Test
    public void failsWhenDiagnosMissing() throws Exception {
        String inputXml = Resources.toString(getResource("transport/scenarios/fail-diagnos-saknas.xml"), Charsets.UTF_8);
        ValidateXmlResponse response = XmlValidator.validate(VALIDATOR, inputXml);
        assertEquals(1, response.getValidationErrors().size());
    }

    @Test
    public void failsWhenFunktionsnedsattningDebutMissing() throws Exception {
        String inputXml = Resources.toString(getResource("transport/scenarios/fail-funktionsnedsattning-debut-saknas.xml"), Charsets.UTF_8);
        ValidateXmlResponse response = XmlValidator.validate(VALIDATOR, inputXml);
        assertEquals(1, response.getValidationErrors().size());
    }

    @Test
    public void failsWhenFunktionsnedsattningPaverkanMissing() throws Exception {
        String inputXml = Resources.toString(getResource("transport/scenarios/fail-funktionsnedsattning-paverkan-saknas.xml"), Charsets.UTF_8);
        ValidateXmlResponse response = XmlValidator.validate(VALIDATOR, inputXml);
        assertEquals(1, response.getValidationErrors().size());
    }

    @Test
    public void failsWhenKannedomOmPatientMissing() throws Exception {
        String inputXml = Resources.toString(getResource("transport/scenarios/fail-baseratpa-kannedom-om-patient-saknas.xml"), Charsets.UTF_8);
        ValidateXmlResponse response = XmlValidator.validate(VALIDATOR, inputXml);
        assertEquals(1, response.getValidationErrors().size());
    }

    @Test
    public void failsWhenFinnsUnderlagMissing() throws Exception {
        String inputXml = Resources.toString(getResource("transport/scenarios/fail-underlagfinnes-men-ej-angivet.xml"), Charsets.UTF_8);
        ValidateXmlResponse response = XmlValidator.validate(VALIDATOR, inputXml);
        assertEquals(1, response.getValidationErrors().size());
    }

    @Test
    public void failsWhenJournaluppgiftHasInvalidDate() throws Exception {
        String inputXml = Resources.toString(getResource("transport/scenarios/fail-baseratpa-journaluppgift-felaktigt-datumformat.xml"),
                Charsets.UTF_8);
        ValidateXmlResponse response = XmlValidator.validate(VALIDATOR, inputXml);
        assertEquals(1, response.getValidationErrors().size());
    }

    @Test
    public void failsWhenBaseratPaUndersokningSenareAnKannedom() throws Exception {
        String inputXml = Resources.toString(getResource("transport/scenarios/fail-baseratpa-undersokning-datum-tidigare-an-kannedom.xml"),
                Charsets.UTF_8);
        ValidateXmlResponse response = XmlValidator.validate(VALIDATOR, inputXml);
        assertEquals(1, response.getValidationErrors().size());
    }

    @Test
    public void failsWhenBaseratPaAnhorigBeskrivningDatumSenareAnKannedom() throws Exception {
        String inputXml = Resources.toString(getResource("transport/scenarios/fail-baseratpa-anhorigbeskrivning-datum-tidigare-an-kannedom.xml"),
                Charsets.UTF_8);
        ValidateXmlResponse response = XmlValidator.validate(VALIDATOR, inputXml);
        assertEquals(1, response.getValidationErrors().size());
    }

    @Test
    public void failsWhenBaseratPaSaknas() throws Exception {
        String inputXml = Resources.toString(getResource("transport/scenarios/fail-baseratpa-saknas.xml"), Charsets.UTF_8);
        ValidateXmlResponse response = XmlValidator.validate(VALIDATOR, inputXml);
        assertEquals(1, response.getValidationErrors().size());
    }

    @Test
    public void failsWhenAnnatDatumMissingButBeskrivningExists() throws Exception {
        String inputXml = Resources.toString(getResource("transport/scenarios/fail-baseratpa-annat-datum-finns-beskrivning-saknas.xml"),
                Charsets.UTF_8);
        ValidateXmlResponse response = XmlValidator.validate(VALIDATOR, inputXml);
        assertEquals(1, response.getValidationErrors().size());
    }

    @Test
    public void failsWhenUnderlagFinnesAndHamtasFranHasWhitespaces() throws Exception {
        String inputXml = Resources.toString(getResource("transport/scenarios/fail-underlagfinnes-whitespace.xml"), Charsets.UTF_8);
        ValidateXmlResponse response = XmlValidator.validate(VALIDATOR, inputXml);
        assertEquals(1, response.getValidationErrors().size());
    }

    @Test
    public void failsWhenUnderlagFinnesMenEjAngivet() throws Exception {
        String inputXml = Resources.toString(getResource("transport/scenarios/fail-underlagfinnes-ej-men-angivet.xml"), Charsets.UTF_8);
        ValidateXmlResponse response = XmlValidator.validate(VALIDATOR, inputXml);
        assertEquals(1, response.getValidationErrors().size());
    }

    @Test
    public void failsDiagnosCodeLessThan3Chars() throws Exception {
        String inputXml = Resources.toString(getResource("transport/scenarios/fail-diagnos-kod-med-mindre-an-tre-positioner.xml"), Charsets.UTF_8);
        ValidateXmlResponse response = XmlValidator.validate(VALIDATOR, inputXml);
        assertEquals(1, response.getValidationErrors().size());
    }

    @Test
    public void failsPshychatricDiagnosCodeLessThan4Chars() throws Exception {
        String inputXml = Resources.toString(getResource("transport/scenarios/fail-diagnos-psykisk-diagnoskod-fel-antal-tecken.xml"), Charsets.UTF_8);
        ValidateXmlResponse response = XmlValidator.validate(VALIDATOR, inputXml);
        assertEquals(1, response.getValidationErrors().size());
    }

    @Test
    public void failsWhenFourDiagnoser() throws Exception {
        String inputXml = Resources.toString(getResource("transport/scenarios/fail-diagnos-fyradiagnoser.xml"), Charsets.UTF_8);
        ValidateXmlResponse response = XmlValidator.validate(VALIDATOR, inputXml);
        assertEquals(1, response.getValidationErrors().size());
    }

    @Test
    public void failsWhenKontaktWithFkIsFalseButReasonStated() throws Exception {
        String inputXml = Resources.toString(getResource("transport/scenarios/fail-motiveringkontaktangivet-men-onskas-ej.xml"), Charsets.UTF_8);
        ValidateXmlResponse response = XmlValidator.validate(VALIDATOR, inputXml);
        assertEquals(1, response.getValidationErrors().size());
    }

    @Test
    public void failsWhenGrundForMuIsRepeated() throws Exception {
        String inputXml = Resources.toString(getResource("transport/fail-grundformuupprepas.xml"), Charsets.UTF_8);
        ValidateXmlResponse response = XmlValidator.validate(VALIDATOR, inputXml);
        assertEquals(1, response.getValidationErrors().size());
    }

    @Test
    public void failsWhenFunktionsnedsattningHasOnlyWhitespaces() throws Exception {
        String inputXml = Resources.toString(getResource("transport/scenarios/fail-funktionsnedsattning-whitespace.xml"), Charsets.UTF_8);
        ValidateXmlResponse response = XmlValidator.validate(VALIDATOR, inputXml);
        assertEquals(2, response.getValidationErrors().size());
    }

    private static URL getResource(String href) {
        return Thread.currentThread().getContextClassLoader().getResource(href);
    }
}
