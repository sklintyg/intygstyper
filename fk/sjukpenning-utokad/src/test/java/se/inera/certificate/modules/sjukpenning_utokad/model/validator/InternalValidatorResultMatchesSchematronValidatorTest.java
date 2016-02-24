package se.inera.certificate.modules.sjukpenning_utokad.model.validator;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;

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
import se.inera.certificate.modules.sjukpenning_utokad.model.converter.InternalToTransport;
import se.inera.certificate.modules.sjukpenning_utokad.model.converter.util.ConverterUtil;
import se.inera.certificate.modules.sjukpenning_utokad.model.internal.SjukpenningUtokadUtlatande;
import se.inera.certificate.modules.sjukpenning_utokad.validator.InternalDraftValidator;
import se.inera.intyg.common.support.modules.support.api.dto.ValidateDraftResponse;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationStatus;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v2.ObjectFactory;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v2.RegisterCertificateType;
import se.riv.clinicalprocess.healthcond.certificate.types.v2.DatePeriodType;

@ContextConfiguration(locations = ("/module-config.xml"))
@RunWith(SpringJUnit4ClassRunner.class)
public class InternalValidatorResultMatchesSchematronValidatorTest {

    @Autowired
    @Qualifier("sjukpenning-utokad-objectMapper")
    private ObjectMapper objectMapper;
    private ConverterUtil converterUtil;

    @Before
    public void setUp() {
        converterUtil = new ConverterUtil();
        converterUtil.setObjectMapper(objectMapper);
    }

    @Test
    public void testSjukpenningUtokad1InternalValidationMatchesSchematron() throws Exception {
        doInternalAndSchematronValidation("sjukpenning-utokat-1");
    }

    @Test
    public void testSjukpenningUtokadMinimalInternalValidationMatchesSchematron() throws Exception {
        doInternalAndSchematronValidation("sjukpenning-utokat-minimal");
    }

    private void doInternalAndSchematronValidation(String filename) throws Exception {
        String json = Resources.toString(getResource(filename + ".json"), Charsets.UTF_8);
        SjukpenningUtokadUtlatande utlatandeFromJson = converterUtil.fromJsonString(json);

        InternalDraftValidator internalValidator = new InternalDraftValidator();
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);

        assertTrue("Internal validation failed", internalValidationResponse.getStatus().equals(ValidationStatus.VALID));

        RegisterCertificateType intyg = InternalToTransport.convert(utlatandeFromJson);
        String convertedXML = getXmlFromModel(intyg);

        RegisterCertificateValidator validator = new RegisterCertificateValidator("sjukpenning-utokat.sch");
        SchematronOutputType result = validator.validateSchematron(new StreamSource(new ByteArrayInputStream(convertedXML.getBytes(Charsets.UTF_8))));

        assertTrue("Schematronvalidation failed", SVRLHelper.getAllFailedAssertions(result).size() == 0);
    }

    private String getXmlFromModel(RegisterCertificateType transport) throws IOException, JAXBException {
        StringWriter sw = new StringWriter();
        JAXBContext jaxbContext = JAXBContext.newInstance(RegisterCertificateType.class, DatePeriodType.class);
        ObjectFactory objectFactory = new ObjectFactory();
        JAXBElement<RegisterCertificateType> requestElement = objectFactory.createRegisterCertificate(transport);
        jaxbContext.createMarshaller().marshal(requestElement, sw);
        return sw.toString();
    }

    private static URL getResource(String href) {
        return Thread.currentThread().getContextClassLoader().getResource(href);
    }
}
