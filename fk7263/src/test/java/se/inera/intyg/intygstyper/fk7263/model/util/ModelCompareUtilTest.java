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

package se.inera.intyg.intygstyper.fk7263.model.util;

import java.io.IOException;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import se.inera.intyg.common.support.common.enumerations.Diagnoskodverk;
import se.inera.intyg.common.support.model.InternalLocalDateInterval;
import se.inera.intyg.common.support.modules.service.WebcertModuleService;
import se.inera.intyg.intygstyper.fk7263.model.internal.Utlatande;

import com.fasterxml.jackson.databind.ObjectMapper;

@ContextConfiguration(locations = ("/fk7263-test-config.xml"))
@RunWith(SpringJUnit4ClassRunner.class)
public class ModelCompareUtilTest extends TestCase {

    public static final String CORRECT_DIAGNOSKOD_FROM_FILE = "S47";
    public static final String CORRECT_DIAGNOSKOD2 = "B88";

    @Autowired
    private ObjectMapper objectMapper;

    private ModelCompareUtil modelCompareUtil;

    @Before
    public void setup() {
        modelCompareUtil = new ModelCompareUtil();
        WebcertModuleService mockModuleService = new WebcertModuleService() {
            @Override
            public boolean validateDiagnosisCode(String codeFragment, String codeSystem) {
                return compareCodes(codeFragment);
            }

            @Override
            public boolean validateDiagnosisCode(String codeFragment, Diagnoskodverk codeSystem) {
                return compareCodes(codeFragment);
            }
            
            private boolean compareCodes(String codeFragment) {
                return CORRECT_DIAGNOSKOD_FROM_FILE.equals(codeFragment) || CORRECT_DIAGNOSKOD2.equals(codeFragment);
            }
        };
        modelCompareUtil.setModuleService(mockModuleService);
    }

    @Test
    public void testModelIsNotChanged() throws Exception {
        Utlatande utlatandeOld = getUtlatandeFromFile();
        Utlatande utlatandeNew = utlatandeOld;
        assertFalse(modelCompareUtil.modelDiffers(utlatandeOld, utlatandeNew));
    }

    @Test
    public void testModelIsChangedNedsattningsgradNull() throws Exception {
        Utlatande utlatandeOld = getUtlatandeFromFile();
        Utlatande utlatandeNew = getUtlatandeFromFile();
        utlatandeNew.setNedsattMed100(null);

        assertTrue(modelCompareUtil.modelDiffers(utlatandeOld, utlatandeNew));
    }

    @Test
    public void testModelIsChangedNedsattningsgradDate() throws Exception {
        Utlatande utlatandeOld = getUtlatandeFromFile();
        Utlatande utlatandeNew = getUtlatandeFromFile();

        // Change the date and ensure this is recognized as a change in the model
        utlatandeNew.setNedsattMed100(new InternalLocalDateInterval("2011-03-03", "2011-04-04"));

        assertTrue(modelCompareUtil.modelDiffers(utlatandeOld, utlatandeNew));
    }

    @Test
    public void testModelIsNotChangedIfInvalidInterval() throws Exception {
        Utlatande utlatandeOld = getUtlatandeFromFile();
        Utlatande utlatandeNew = getUtlatandeFromFile();

        // Change the dates and ensure this is not recognized as a change in the model
        utlatandeOld.setNedsattMed100(null);
        utlatandeNew.setNedsattMed100(new InternalLocalDateInterval("2011-03-03", "2011-04"));

        assertFalse(modelCompareUtil.modelDiffers(utlatandeOld, utlatandeNew));
    }

    @Test
    public void testModelIsNotChangedBetweenTwoInvalidIntervals() throws Exception {
        Utlatande utlatandeOld = getUtlatandeFromFile();
        Utlatande utlatandeNew = getUtlatandeFromFile();

        // Change the date and ensure this is not recognized as a change in the model
        utlatandeOld.setNedsattMed100(new InternalLocalDateInterval("2011-03-03", "2011"));
        utlatandeNew.setNedsattMed100(new InternalLocalDateInterval("2011-03-03", "2011-04"));

        assertFalse(modelCompareUtil.modelDiffers(utlatandeOld, utlatandeNew));
    }

    @Test
    public void testModelIsChangedWhenIntervalIsValid() throws Exception {
        Utlatande utlatandeOld = getUtlatandeFromFile();
        Utlatande utlatandeNew = getUtlatandeFromFile();

        // Change the date to a valid date and ensure this is recognized as a change in the model
        utlatandeOld.setNedsattMed100(null);
        utlatandeNew.setNedsattMed100(new InternalLocalDateInterval("2011-03-03", "2011-04-04"));

        assertTrue(modelCompareUtil.modelDiffers(utlatandeOld, utlatandeNew));
    }

    @Test
    public void testModelIsChangedWhenIntervalBecomesValid() throws Exception {
        Utlatande utlatandeOld = getUtlatandeFromFile();
        Utlatande utlatandeNew = getUtlatandeFromFile();

        // Change the date to a valid date and ensure this is recognized as a change in the model
        utlatandeOld.setNedsattMed100(new InternalLocalDateInterval("2011-03-03", "2011-04"));
        utlatandeNew.setNedsattMed100(new InternalLocalDateInterval("2011-03-03", "2011-04-04"));

        assertTrue(modelCompareUtil.modelDiffers(utlatandeOld, utlatandeNew));
    }

    @Test
    public void testModelIsChangedWhenIntervalBecomesInvalid() throws Exception {
        Utlatande utlatandeOld = getUtlatandeFromFile();
        Utlatande utlatandeNew = getUtlatandeFromFile();

        // Change the date and ensure this is recognized as a change in the model
        utlatandeNew.setNedsattMed100(new InternalLocalDateInterval("2011-03-03", "2011-04"));

        assertTrue(modelCompareUtil.modelDiffers(utlatandeOld, utlatandeNew));
    }

    @Test
    public void testModelIsChangedDiagnoskod() throws Exception {
        Utlatande utlatandeOld = getUtlatandeFromFile();
        Utlatande utlatandeNew = getUtlatandeFromFile();

        // Mess with the diagnose and make sure the change registers
        utlatandeNew.setDiagnosKod(CORRECT_DIAGNOSKOD2);

        assertTrue(modelCompareUtil.modelDiffers(utlatandeOld, utlatandeNew));
    }

    @Test
    public void testModelIsNotChangedOnOtherDiagnoskod() throws Exception {
        Utlatande utlatandeOld = getUtlatandeFromFile();
        Utlatande utlatandeNew = getUtlatandeFromFile();

        // Mess with the diagnose and make sure the change registers
        utlatandeNew.setDiagnosKod2(CORRECT_DIAGNOSKOD2);
        utlatandeNew.setDiagnosKod3(CORRECT_DIAGNOSKOD2);

        assertFalse(modelCompareUtil.modelDiffers(utlatandeOld, utlatandeNew));
    }

    @Test
    public void testModelIsChangedDiagnosbeskrivning1() throws Exception {
        Utlatande utlatandeOld = getUtlatandeFromFile();
        Utlatande utlatandeNew = getUtlatandeFromFile();

        // Mess with the diagnose and make sure the change registers
        utlatandeNew.setDiagnosBeskrivning1("BLAH");

        assertTrue(modelCompareUtil.modelDiffers(utlatandeOld, utlatandeNew));
    }

    @Test
    public void testModelIsNotChangedOnOtherDiagnosbeskrivning() throws Exception {
        Utlatande utlatandeOld = getUtlatandeFromFile();
        Utlatande utlatandeNew = getUtlatandeFromFile();

        // Mess with the diagnose and make sure the change registers
        utlatandeNew.setDiagnosBeskrivning2("BLAH");
        utlatandeNew.setDiagnosBeskrivning3("BLAH");
        utlatandeNew.setDiagnosBeskrivning("BLAH");
        utlatandeNew.setSamsjuklighet(false);

        assertFalse(modelCompareUtil.modelDiffers(utlatandeOld, utlatandeNew));
    }

    @Test
    public void testModelIsUnchangedDiagnosFromEmptyToInvalid() throws Exception {
        Utlatande utlatandeOld = getUtlatandeFromFile();
        Utlatande utlatandeNew = getUtlatandeFromFile();

        utlatandeOld.setDiagnosKod(null);
        utlatandeNew.setDiagnosKod("BLAH");

        assertFalse(modelCompareUtil.modelDiffers(utlatandeOld, utlatandeNew));
    }

    @Test
    public void testModelIsUnchangedDiagnosFromInvalidToInvalid() throws Exception {
        Utlatande utlatandeOld = getUtlatandeFromFile();
        Utlatande utlatandeNew = getUtlatandeFromFile();

        utlatandeOld.setDiagnosKod("BLAH");
        utlatandeNew.setDiagnosKod("BLAH");

        assertFalse(modelCompareUtil.modelDiffers(utlatandeOld, utlatandeNew));
    }

    @Test
    public void testModelIsUnchangedDiagnosFromOneInvalidToOtherInvalid() throws Exception {
        Utlatande utlatandeOld = getUtlatandeFromFile();
        Utlatande utlatandeNew = getUtlatandeFromFile();

        utlatandeOld.setDiagnosKod("BLAH");
        utlatandeNew.setDiagnosKod("BLOH");

        assertFalse(modelCompareUtil.modelDiffers(utlatandeOld, utlatandeNew));
    }

    private Utlatande getUtlatandeFromFile() throws IOException {
        return objectMapper.readValue(new ClassPathResource(
                "Fk7263ModuleApiTest/utlatande.json").getFile(), Utlatande.class);
    }

}
