package se.inera.certificate.modules.sjukersattning.model.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
import java.util.stream.Collectors;

import javax.xml.bind.JAXB;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.oclc.purl.dsdl.svrl.SchematronOutputType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.xml.sax.SAXException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.helger.schematron.svrl.SVRLHelper;
import com.helger.schematron.svrl.SVRLWriter;

import se.inera.certificate.modules.fkparent.integration.RegisterCertificateValidator;
import se.inera.certificate.modules.fkparent.model.converter.RegisterCertificateTestValidator;
import se.inera.certificate.modules.sjukersattning.model.internal.SjukersattningUtlatande;
import se.inera.intyg.common.support.xml.SchemaValidatorBuilder;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v2.RegisterCertificateType;

@ContextConfiguration(locations = ("/module-config.xml"))
@RunWith(SpringJUnit4ClassRunner.class)
public class InternalToTransportTest {

    @Autowired
    @Qualifier("sjukersattning-objectMapper")
    private ObjectMapper objectMapper;

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

    private void meh() throws IOException, SAXException {
        final String CLINICAL_CORE_SCHEMA = "core_components/clinicalprocess_healthcond_certificate_2.0.xsd";

        final String CLINICAL_TYPES_SCHEMA = "core_components/clinicalprocess_healthcond_certificate_types_2.0.xsd";

        final String CLINIAL_REGISTER_SCHEMA = "interactions/RegisterCertificateInteraction/RegisterCertificateResponder_2.0.xsd";

        SchemaValidatorBuilder schemaValidatorBuilder = new SchemaValidatorBuilder();
        Source rootSource = schemaValidatorBuilder.registerResource(CLINIAL_REGISTER_SCHEMA);
        schemaValidatorBuilder.registerResource(CLINICAL_TYPES_SCHEMA);
        schemaValidatorBuilder.registerResource(CLINICAL_CORE_SCHEMA);
        Schema schema = schemaValidatorBuilder.build(rootSource);
    }
    
    @Test
    public void outputJsonFromXml() throws Exception {

        String xmlContents = Resources.toString(getResource("sjukersattning3.xml"), Charsets.UTF_8);

        RegisterCertificateType transport = JAXB.unmarshal(new StringReader(xmlContents), RegisterCertificateType.class);

        transport.getIntyg().getSvar().stream()
            .forEach(e -> System.out.println(e.getDelsvar()
                    .stream()
                    .map(l -> "DelsvarId: " + l.getId() + " Content: " + l.getContent().toString())
                    .collect(Collectors.toList())));
        StringWriter sw = new StringWriter();
        JAXB.marshal(transport, sw);
        String xmlAgain = sw.toString();
        System.out.println(xmlAgain);
//        SjukersattningUtlatande internal = TransportToInternal.convert(transport.getIntyg());
//        try {
//            objectMapper.writeValue(sw, internal);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        System.out.println(sw.toString());
    }

    private static URL getResource(String href) {
        return Thread.currentThread().getContextClassLoader().getResource(href);
    }

}
