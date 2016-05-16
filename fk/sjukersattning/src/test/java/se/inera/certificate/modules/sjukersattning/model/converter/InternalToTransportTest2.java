package se.inera.certificate.modules.sjukersattning.model.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.net.URL;

import javax.xml.transform.stream.StreamSource;

import org.junit.Test;
import org.oclc.purl.dsdl.svrl.SchematronOutputType;

import se.inera.certificate.modules.fkparent.integration.RegisterCertificateValidator;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.helger.schematron.svrl.SVRLHelper;
import com.helger.schematron.svrl.SVRLWriter;
import se.inera.certificate.modules.fkparent.model.converter.RegisterCertificateTestValidator;

public class InternalToTransportTest2 {

    @Test
    public void doSchematronValidationSjukersattning() throws Exception {
        String xmlContents = Resources.toString(getResource("sjukersattning3.xml"), Charsets.UTF_8);

        RegisterCertificateTestValidator generalValidator = new RegisterCertificateTestValidator();
        assertTrue(generalValidator.validateGeneral(xmlContents));

        RegisterCertificateValidator validator = new RegisterCertificateValidator("sjukersattning.sch");
        SchematronOutputType result = validator.validateSchematron(new StreamSource(new ByteArrayInputStream(xmlContents.getBytes(Charsets.UTF_8))));

        System.out.println(SVRLWriter.createXMLString(result));

        assertEquals(0, SVRLHelper.getAllFailedAssertions(result).size());
    }

    private static URL getResource(String href) {
        return Thread.currentThread().getContextClassLoader().getResource(href);
    }

}
