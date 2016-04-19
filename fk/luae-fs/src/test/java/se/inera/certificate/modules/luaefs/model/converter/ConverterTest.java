package se.inera.certificate.modules.luaefs.model.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
import java.util.stream.Collectors;

import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.transform.stream.StreamSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.oclc.purl.dsdl.svrl.SchematronOutputType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.helger.schematron.svrl.SVRLHelper;

import se.inera.certificate.modules.fkparent.integration.RegisterCertificateValidator;
import se.inera.certificate.modules.fkparent.model.converter.RegisterCertificateTestValidator;
import se.inera.certificate.modules.fkparent.model.validator.InternalValidatorUtil;
import se.inera.certificate.modules.luaefs.model.converter.util.ConverterUtil;
import se.inera.certificate.modules.luaefs.model.internal.LuaefsUtlatande;
import se.inera.certificate.modules.luaefs.validator.InternalDraftValidator;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v2.RegisterCertificateType;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v2.ObjectFactory;
import se.riv.clinicalprocess.healthcond.certificate.types.v2.DatePeriodType;

@ContextConfiguration(locations = {"/module-config.xml", "/test-config.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class ConverterTest {

    @Autowired
    @Qualifier("luaefs-objectMapper")
    private ObjectMapper objectMapper;
    private ConverterUtil converterUtil;

    @Before
    public void setUp() {
        converterUtil = new ConverterUtil();
        converterUtil.setObjectMapper(objectMapper);
    }

    @Test
    public void doSchematronValidationSjukersattning() throws Exception {
        String xmlContents = Resources.toString(getResource("transport/luaefs-2.xml"), Charsets.UTF_8);

        RegisterCertificateTestValidator generalValidator = new RegisterCertificateTestValidator();
        assertTrue(generalValidator.validateGeneral(xmlContents));

        RegisterCertificateValidator validator = new RegisterCertificateValidator("sjukpenning-utokat.sch"); //TODO: byta till aktivitetsersattning-fs.sch
        SchematronOutputType result = validator.validateSchematron(new StreamSource(new ByteArrayInputStream(xmlContents.getBytes(Charsets.UTF_8))));

        assertEquals(0, SVRLHelper.getAllFailedAssertions(result).size());
    }

    @Test
    public void outputJsonFromXml() throws Exception {

        String xmlContents = Resources.toString(getResource("transport/luaefs-2.xml"), Charsets.UTF_8);
        RegisterCertificateType transport = JAXB.unmarshal(new StringReader(xmlContents), RegisterCertificateType.class);

        String json = getJsonFromTransport(transport);
        LuaefsUtlatande utlatandeFromJson = converterUtil.fromJsonString(json);
        //System.out.println(json);

        RegisterCertificateType transportConvertedALot = InternalToTransport.convert(utlatandeFromJson);
        String convertedXML = getXmlFromModel(transportConvertedALot);

        // Do schematron validation on the xml-string from the converted transport format
        RegisterCertificateValidator validator = new RegisterCertificateValidator("sjukpenning-utokat.sch");//TODO: byta till aktivitetsersattning-fs.sch
        SchematronOutputType result = validator.validateSchematron(new StreamSource(new ByteArrayInputStream(convertedXML.getBytes(Charsets.UTF_8))));
        assertEquals(getErrorString(result), 0, SVRLHelper.getAllFailedAssertions(result).size());

        // Why not validate internal model as well?
        InternalDraftValidator internalValidator = new InternalDraftValidator(new InternalValidatorUtil());
        internalValidator.validateDraft(utlatandeFromJson);
    }

    private String getErrorString(SchematronOutputType result) {
        StringBuilder errorMsg = new StringBuilder();
        SVRLHelper.getAllFailedAssertions(result).stream()
                .map(e -> e.getText())
                .collect(Collectors.toList())
                .forEach(e -> errorMsg.append(e));
        return errorMsg.toString();
    }

    private static URL getResource(String href) {
        return Thread.currentThread().getContextClassLoader().getResource(href);
    }

    private String getXmlFromModel(RegisterCertificateType transport) throws IOException, JAXBException {
        StringWriter sw = new StringWriter();
        JAXBContext jaxbContext = JAXBContext.newInstance(RegisterCertificateType.class, DatePeriodType.class);
        ObjectFactory objectFactory = new ObjectFactory();
        JAXBElement<RegisterCertificateType> requestElement = objectFactory.createRegisterCertificate(transport);
        jaxbContext.createMarshaller().marshal(requestElement, sw);
        return sw.toString();
    }

    private String getJsonFromTransport(RegisterCertificateType transport) throws ConverterException {
        StringWriter jsonWriter = new StringWriter();
        LuaefsUtlatande internal = TransportToInternal.convert(transport.getIntyg());
        try {
            objectMapper.writeValue(jsonWriter, internal);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonWriter.toString();
    }

}
