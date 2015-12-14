package se.inera.certificate.modules.sjukersattning.model.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.net.URL;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.transform.stream.StreamSource;

import org.junit.Test;
import org.oclc.purl.dsdl.svrl.SchematronOutputType;

import se.inera.certificate.modules.fkparent.model.converter.RegisterCertificateValidator;
import se.inera.certificate.modules.sjukersattning.integration.RegisterSjukersattningValidator;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v2.ObjectFactory;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v2.RegisterCertificateType;

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
    public void doSchematronValidationSjukpenningFordjupat() throws Exception {
        String xmlContents = Resources.toString(getResource("sjukpenning2.xml"), Charsets.UTF_8);

        RegisterCertificateValidator generalValidator = new RegisterCertificateValidator();
        assertTrue(generalValidator.validateGeneral(xmlContents));

        RegisterSjukersattningValidator validator = new RegisterSjukersattningValidator("sjukpenning-fordjupat.sch");
        SchematronOutputType result = validator.validateSchematron(new StreamSource(new ByteArrayInputStream(xmlContents.getBytes(Charsets.UTF_8))));

        System.out.println(SVRLWriter.createXMLString(result));

        assertEquals(0, SVRLHelper.getAllFailedAssertions(result).size());
    }

    private String xmlToString(RegisterCertificateType registerCertificate) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(RegisterCertificateType.class);
        ObjectFactory objectFactory = new ObjectFactory();

        StringWriter stringWriter = new StringWriter();
        JAXBElement<RegisterCertificateType> requestElement = objectFactory.createRegisterCertificate(registerCertificate);
        jaxbContext.createMarshaller().marshal(requestElement, stringWriter);
        String result = stringWriter.toString();
        System.out.println(result);
        return result;
    }

    private static URL getResource(String href) {
        return Thread.currentThread().getContextClassLoader().getResource(href);
    }

}
