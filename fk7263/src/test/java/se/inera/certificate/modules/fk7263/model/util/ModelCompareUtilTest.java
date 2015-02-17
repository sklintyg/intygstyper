package se.inera.certificate.modules.fk7263.model.util;

import java.io.IOException;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import se.inera.certificate.codes.Diagnoskodverk;
import se.inera.certificate.model.InternalLocalDateInterval;
import se.inera.certificate.modules.fk7263.model.internal.Utlatande;
import se.inera.certificate.modules.service.WebcertModuleService;

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
    public void testModelIsChangedDiagnoskod() throws Exception {
        Utlatande utlatandeOld = getUtlatandeFromFile();
        Utlatande utlatandeNew = getUtlatandeFromFile();

        // Mess with the diagnose and make sure the change registers
        utlatandeNew.setDiagnosKod(CORRECT_DIAGNOSKOD2);

        assertTrue(modelCompareUtil.modelDiffers(utlatandeOld, utlatandeNew));
    }

    @Test
    public void testModelIsChangedDiagnosbeskrivning() throws Exception {
        Utlatande utlatandeOld = getUtlatandeFromFile();
        Utlatande utlatandeNew = getUtlatandeFromFile();

        // Mess with the diagnose and make sure the change registers
        utlatandeNew.setDiagnosBeskrivning1("BLAH");

        assertTrue(modelCompareUtil.modelDiffers(utlatandeOld, utlatandeNew));
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
