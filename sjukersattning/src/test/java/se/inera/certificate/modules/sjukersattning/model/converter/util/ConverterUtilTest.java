package se.inera.certificate.modules.sjukersattning.model.converter.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import se.inera.certificate.modules.sjukersattning.model.converter.TransportToInternalTest;
import se.inera.certificate.modules.sjukersattning.model.internal.SjukersattningUtlatande;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import se.inera.intyg.common.support.modules.support.api.CertificateHolder;
import se.inera.intyg.common.util.integration.integration.json.CustomObjectMapper;

public class ConverterUtilTest {

    @Test
    public void testConversion() throws Exception {
        ObjectMapper mapper = new CustomObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        ConverterUtil converterUtil = new ConverterUtil();
        converterUtil.setObjectMapper(mapper);
        SjukersattningUtlatande originalUtlatande = TransportToInternalTest.getUtlatande();
        CertificateHolder certificateHolder = converterUtil.toCertificateHolder(originalUtlatande);
        System.out.println(certificateHolder.getDocument());
        SjukersattningUtlatande convertedUtlatande = converterUtil.fromJsonString(certificateHolder.getDocument());
        assertEquals(originalUtlatande, convertedUtlatande);
    }

}
