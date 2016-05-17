package se.inera.certificate.modules.luae_na.model.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
import java.util.stream.Collectors;

import javax.xml.bind.JAXB;
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
import se.inera.certificate.modules.luae_na.model.converter.util.ConverterUtil;
import se.inera.certificate.modules.luae_na.model.internal.AktivitetsersattningNAUtlatande;
import se.inera.certificate.modules.luae_na.validator.InternalDraftValidator;
import se.inera.intyg.common.support.common.enumerations.Diagnoskodverk;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.support.modules.service.WebcertModuleService;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v2.RegisterCertificateType;

@ContextConfiguration(locations = {"/module-config.xml", "/test-config.xml"})
@RunWith(SpringJUnit4ClassRunner.class)

public class ConverterTest {

    @Autowired
    @Qualifier("luae_na-objectMapper")
    private ObjectMapper objectMapper;
    private ConverterUtil converterUtil;

    private WebcertModuleService webcertModuleService;

    @Before
    public void setUp() {
        converterUtil = new ConverterUtil();
        converterUtil.setObjectMapper(objectMapper);
        webcertModuleService = new WebcertModuleService() {

            @Override
            public boolean validateDiagnosisCode(String codeFragment, Diagnoskodverk codeSystem) {
                return true;
            }

            @Override
            public boolean validateDiagnosisCode(String codeFragment, String codeSystemStr) {
                return true;
            }

            @Override
            public String getDescriptionFromDiagnosKod(String code, String codeSystemStr) {
                return "";
            }
        };
    }

    @Test
    public void doSchematronValidationSjukersattning() throws Exception {
        String xmlContents = Resources.toString(getResource("sjukersattning3.xml"), Charsets.UTF_8);

        RegisterCertificateTestValidator generalValidator = new RegisterCertificateTestValidator();
        assertTrue(generalValidator.validateGeneral(xmlContents));

        RegisterCertificateValidator validator = new RegisterCertificateValidator("luae_na.sch");
        SchematronOutputType result = validator.validateSchematron(new StreamSource(new ByteArrayInputStream(xmlContents.getBytes(Charsets.UTF_8))));

        //System.out.println(SVRLHelper.getAllFailedAssertions(result).get(0).getText());
        assertEquals(0, SVRLHelper.getAllFailedAssertions(result).size());
    }

    @Test
    public void outputJsonFromXml() throws Exception {

        String xmlContents = Resources.toString(getResource("sjukersattning3.xml"), Charsets.UTF_8);
        RegisterCertificateType transport = JAXB.unmarshal(new StringReader(xmlContents), RegisterCertificateType.class);

        String json = getJsonFromTransport(transport);
        AktivitetsersattningNAUtlatande utlatandeFromJson = converterUtil.fromJsonString(json);
        System.out.println(json);

        RegisterCertificateType transportConvertedALot = InternalToTransport.convert(utlatandeFromJson);
        String convertedXML = getXmlFromModel(transportConvertedALot);

        // Do schematron validation on the xml-string from the converted transport format
        RegisterCertificateValidator validator = new RegisterCertificateValidator("luae_na.sch");
        SchematronOutputType result = validator.validateSchematron(new StreamSource(new ByteArrayInputStream(convertedXML.getBytes(Charsets.UTF_8))));

        //System.out.println(SVRLHelper.getAllFailedAssertions(result).get(0).getText());
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

    private String getXmlFromModel(RegisterCertificateType transport) throws IOException {
        StringWriter sw = new StringWriter();
        JAXB.marshal(transport, sw);
        return sw.toString();
    }

    private String getJsonFromTransport(RegisterCertificateType transport) throws ConverterException {
        StringWriter jsonWriter = new StringWriter();
        AktivitetsersattningNAUtlatande internal = TransportToInternal.convert(transport.getIntyg());
        try {
            objectMapper.writeValue(jsonWriter, internal);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonWriter.toString();
    }

}
