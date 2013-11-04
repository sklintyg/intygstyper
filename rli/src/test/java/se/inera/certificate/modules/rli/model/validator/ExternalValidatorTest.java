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
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import se.inera.certificate.integration.json.CustomObjectMapper;
import se.inera.certificate.model.Kod;
import se.inera.certificate.model.Rekommendation;
import se.inera.certificate.modules.rli.model.external.Utlatande;
import se.inera.certificate.modules.rli.validator.ExternalValidatorImpl;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ExternalValidatorTest {

    private ExternalValidatorImpl validator;

    @Before
    public void setUp() throws Exception {
        validator = new ExternalValidatorImpl();
    }

    private Utlatande buildTestUtlatande(String filename) {
        ObjectMapper mapper = new CustomObjectMapper();
        Utlatande utlatande = null;

        try {
            InputStream is = this.getClass().getResourceAsStream(filename);
            utlatande = mapper.readValue(is, Utlatande.class);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return utlatande;
    }

    @Test
    public void testValidateRekommendationskod() {
        
        List<String> valErrors = new ArrayList<String>();
        
        Utlatande utlatande = buildTestUtlatande("/rli-example-1.json");
        
        Kod sjkCode = new Kod();
        sjkCode.setCode("SJK1");
        sjkCode.setCodeSystem("kv_rekommendation_intyg");
        
        Kod validCode = new Kod();
        validCode.setCode("REK1");
        validCode.setCodeSystem("kv_rekommendation_intyg");
        
        Kod invalidCode = new Kod();
        invalidCode.setCode("REK3");
        invalidCode.setCodeSystem("kv_rekommendation_intyg");

        Rekommendation validRekommendation = new Rekommendation();
        validRekommendation.setRekommendationskod(validCode);
        validRekommendation.setSjukdomskannedom(sjkCode);

        utlatande.getRekommendationer().clear();
        utlatande.getRekommendationer().add(validRekommendation);
        validator.validateRekommendationer(utlatande, valErrors);
        assertTrue(valErrors.isEmpty());
        
        Rekommendation invalidRekommendation = new Rekommendation();
        invalidRekommendation.setRekommendationskod(invalidCode);
        
        utlatande.getRekommendationer().clear();
        utlatande.getRekommendationer().add(invalidRekommendation);
        validator.validateRekommendationer(utlatande, valErrors);
        assertTrue(valErrors.contains("No valid rekommendationskod found"));
        assertTrue(valErrors.contains("No valid sjukdomskannedomskod found"));
        
    }
    @Test
    public void testValidate() {
        Utlatande utlatande = buildTestUtlatande("/rli-example-1.json");
        List<String> validationErrors = validator.validate(utlatande);
        for (String s : validationErrors){
            System.out.println(s);
        }
        assertTrue(validationErrors.isEmpty());

    }
    
    @Test
    public void testValidateWithErrors() {
        Utlatande utlatande = buildTestUtlatande("/rli-example-1-with-errors.json");
        List<String> validationErrors = validator.validate(utlatande);

        //General utlatande stuff:
        assertTrue(validationErrors.contains("No Utlatande ID found"));

        //Skapas av related:
        assertTrue(validationErrors.contains("PersonalId should be an HSA-ID with root: 1.2.752.129.2.1.4.1"));

        //Patient related:
        assertTrue(validationErrors
                .contains("Could not parse the SSN '191212121212' (format should be 'yyyyMMdd-nnnn')"));
        
        assertTrue(validationErrors.contains("An Efternamn must be provided for Patient"));
        
        //Arrangemang related:
        assertTrue(validationErrors.contains("Code in arrangemang must be SNOMED-CT code: 420008001"));
        
        //Aktivitets related:
        assertTrue(validationErrors
                .contains("Aktivitetstid must be specified for Aktivitet of type Klinisk Undersokning (AV020 / UNS)"));
        
        //Rekommendation related:
        assertTrue(validationErrors.contains("No valid rekommendationskod found"));
        
        assertTrue(validationErrors.contains("No valid sjukdomskannedomskod found"));
        
    }
}
