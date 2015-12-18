package se.inera.certificate.modules.sjukersattning.model.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.net.URL;

import javax.xml.transform.stream.StreamSource;

import org.junit.Test;
import org.oclc.purl.dsdl.svrl.SchematronOutputType;

import se.inera.certificate.modules.fkparent.model.converter.RegisterCertificateValidator;
import se.inera.certificate.modules.sjukersattning.integration.RegisterSjukersattningValidator;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.helger.schematron.svrl.SVRLHelper;
import com.helger.schematron.svrl.SVRLWriter;

public class InternalToTransportTest2 {

    @Test
    public void doSchematronValidationSjukersattning() throws Exception {
        String xmlContents = Resources.toString(getResource("sjukersattning2.xml"), Charsets.UTF_8);

        RegisterCertificateValidator generalValidator = new RegisterCertificateValidator();
        assertTrue(generalValidator.validateGeneral(xmlContents));

        RegisterSjukersattningValidator validator = new RegisterSjukersattningValidator("sjukersattning.sch");
        SchematronOutputType result = validator.validateSchematron(new StreamSource(new ByteArrayInputStream(xmlContents.getBytes(Charsets.UTF_8))));

        System.out.println(SVRLWriter.createXMLString(result));

        assertEquals(0, SVRLHelper.getAllFailedAssertions(result).size());
    }

    @Test
    public void doSchematronValidationSjukpenningUtokat() throws Exception {
        String xmlContents = Resources.toString(getResource("sjukpenning-utokat2.xml"), Charsets.UTF_8);

        RegisterCertificateValidator generalValidator = new RegisterCertificateValidator();
        assertTrue(generalValidator.validateGeneral(xmlContents));

        RegisterSjukersattningValidator validator = new RegisterSjukersattningValidator("sjukpenning-utokat.sch");
        SchematronOutputType result = validator.validateSchematron(new StreamSource(new ByteArrayInputStream(xmlContents.getBytes(Charsets.UTF_8))));

        System.out.println(SVRLWriter.createXMLString(result));

        assertEquals(0, SVRLHelper.getAllFailedAssertions(result).size());
    }

    @Test
    public void doSchematronValidationSjukpenning() throws Exception {
        String xmlContents = Resources.toString(getResource("sjukpenning2.xml"), Charsets.UTF_8);

        RegisterCertificateValidator generalValidator = new RegisterCertificateValidator();
        assertTrue(generalValidator.validateGeneral(xmlContents));

        RegisterSjukersattningValidator validator = new RegisterSjukersattningValidator("sjukpenning.sch");
        SchematronOutputType result = validator.validateSchematron(new StreamSource(new ByteArrayInputStream(xmlContents.getBytes(Charsets.UTF_8))));

        System.out.println(SVRLWriter.createXMLString(result));

        assertEquals(0, SVRLHelper.getAllFailedAssertions(result).size());
    }

    @Test
    public void doSchematronValidationAktivitetsersattningNA() throws Exception {
        String xmlContents = Resources.toString(getResource("aktivitetsersattning-na2.xml"), Charsets.UTF_8);

        RegisterCertificateValidator generalValidator = new RegisterCertificateValidator();
        assertTrue(generalValidator.validateGeneral(xmlContents));

        RegisterSjukersattningValidator validator = new RegisterSjukersattningValidator("aktivitetsersattning-na.sch");
        SchematronOutputType result = validator.validateSchematron(new StreamSource(new ByteArrayInputStream(xmlContents.getBytes(Charsets.UTF_8))));

        System.out.println(SVRLWriter.createXMLString(result));

        assertEquals(0, SVRLHelper.getAllFailedAssertions(result).size());
    }

    @Test
    public void doSchematronValidationAktivitetsersattningFS() throws Exception {
        String xmlContents = Resources.toString(getResource("aktivitetsersattning-fs2.xml"), Charsets.UTF_8);

        RegisterCertificateValidator generalValidator = new RegisterCertificateValidator();
        assertTrue(generalValidator.validateGeneral(xmlContents));

        RegisterSjukersattningValidator validator = new RegisterSjukersattningValidator("aktivitetsersattning-fs.sch");
        SchematronOutputType result = validator.validateSchematron(new StreamSource(new ByteArrayInputStream(xmlContents.getBytes(Charsets.UTF_8))));

        System.out.println(SVRLWriter.createXMLString(result));

        assertEquals(0, SVRLHelper.getAllFailedAssertions(result).size());
    }

    private static URL getResource(String href) {
        return Thread.currentThread().getContextClassLoader().getResource(href);
    }

}
