/**
 * Copyright (C) 2013 Inera AB (http://www.inera.se)
 *
 * This file is part of Inera Certificate Modules (http://code.google.com/p/inera-certificate-modules).
 *
 * Inera Certificate Modules is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Inera Certificate Modules is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.inera.certificate.modules.rli.model.validator;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;

import se.inera.certificate.integration.json.CustomObjectMapper;
import se.inera.certificate.modules.rli.model.external.Utlatande;
import se.inera.certificate.modules.rli.validator.ExternalValidator;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ExternalValidatorTest {

    private ExternalValidator validator;

    @Before
    public void setUp() throws Exception {
        validator = new ExternalValidator();
    }

    private Utlatande buildTestUtlatande(String filename) throws IOException {
        ObjectMapper mapper = new CustomObjectMapper();
        Utlatande utlatande = null;

        InputStream is = this.getClass().getResourceAsStream(filename);
        utlatande = mapper.readValue(is, Utlatande.class);

        return utlatande;
    }

    @Test
    public void testValidate() throws IOException {
        Utlatande utlatande = buildTestUtlatande("/rli-example-1.json");
        List<String> validationErrors = validator.validate(utlatande);

        assertTrue(StringUtils.join(validationErrors, ", "), validationErrors.isEmpty());
    }

    @Test
    public void testValidateWithErrors() throws IOException {
        Utlatande utlatande = buildTestUtlatande("/rli-example-1-with-errors.json");
        List<String> validationErrors = validator.validate(utlatande);

        assertTrue(!validationErrors.isEmpty());
    }
}
