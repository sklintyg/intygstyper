/*
 * Copyright (C) 2015 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package se.inera.intyg.intygstyper.fk7263.model.converter.util;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import se.inera.intyg.common.support.modules.support.api.CertificateHolder;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleException;
import se.inera.intyg.common.util.integration.integration.json.CustomObjectMapper;
import se.inera.intyg.intygstyper.fk7263.model.internal.Utlatande;


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
