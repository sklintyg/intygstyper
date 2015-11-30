package se.inera.certificate.modules.fk7263.model.converter.util;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import se.inera.intyg.common.util.integration.integration.json.CustomObjectMapper;
import se.inera.certificate.modules.fk7263.model.internal.Utlatande;
import se.inera.intyg.common.support.modules.support.api.CertificateHolder;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleException;


public class ConverterUtilTest {

    private static ConverterUtil converterUtil = new ConverterUtil();
    private static CustomObjectMapper objectMapper = new CustomObjectMapper();
    static {
        converterUtil.setObjectMapper(objectMapper);
    }
    private String json = readRequestFromFile("/ConverterUtilTest/minimalt-fk7263-internal.json");

    @Test
    public void testConvertFromJsonString() throws ModuleException {
        CertificateHolder holder = converterUtil.toCertificateHolder(json);
        Assert.assertEquals("id", holder.getId());
        Assert.assertEquals("Enhetsid", holder.getCareUnitId());
        Assert.assertEquals("VardgivarId", holder.getCareGiverId());
        Assert.assertSame(json, holder.getDocument());
    }
    
    @Test
    public void testConvertFromUtlatande() throws ModuleException {
        Utlatande utlatande = converterUtil.fromJsonString(json);
        String formattedJson = converterUtil.toJsonString(utlatande);
        CertificateHolder holder = converterUtil.toCertificateHolder(utlatande);
        Assert.assertEquals("id", holder.getId());
        Assert.assertEquals("Enhetsid", holder.getCareUnitId());
        Assert.assertEquals("VardgivarId", holder.getCareGiverId());
        Assert.assertEquals(formattedJson, holder.getDocument());
    }
        
    private static String readRequestFromFile(String filePath) {
        try {
            ClassPathResource resource = new ClassPathResource(filePath);
            return IOUtils.toString(resource.getInputStream(), "UTF-8");
        } catch (IOException e) {
            return null;
        }
   }

}
