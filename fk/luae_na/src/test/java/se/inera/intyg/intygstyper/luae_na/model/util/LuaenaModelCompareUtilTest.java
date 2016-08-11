package se.inera.intyg.intygstyper.luae_na.model.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;

import java.io.IOException;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.core.io.ClassPathResource;

import com.fasterxml.jackson.databind.ObjectMapper;

import se.inera.intyg.common.support.model.InternalDate;
import se.inera.intyg.common.support.modules.service.WebcertModuleService;
import se.inera.intyg.common.util.integration.integration.json.CustomObjectMapper;
import se.inera.intyg.intygstyper.fkparent.model.internal.Diagnos;
import se.inera.intyg.intygstyper.luae_na.model.internal.LuaenaUtlatande;
import se.inera.intyg.intygstyper.luae_na.model.internal.LuaenaUtlatande.Builder;

@RunWith(MockitoJUnitRunner.class)
public class LuaenaModelCompareUtilTest {

    public static final String CORRECT_DIAGNOSKOD_FROM_FILE = "S47";
    public static final String CORRECT_DIAGNOSKOD2 = "A00-";

    @Spy
    private ObjectMapper objectMapper = new CustomObjectMapper();

    @Mock
    private WebcertModuleService moduleService;

    @InjectMocks
    private LuaenaModelCompareUtil modelCompareUtil;

    @Before
    public void setup() {
        Answer<Boolean> mockAnswer = new Answer<Boolean>() {
            @Override
            public Boolean answer(InvocationOnMock invocation) {
               String codeFragment = (String) invocation.getArguments()[0];
               return CORRECT_DIAGNOSKOD_FROM_FILE.equals(codeFragment) || CORRECT_DIAGNOSKOD2.equals(codeFragment);
            }
        };
        doAnswer(mockAnswer).when(moduleService).validateDiagnosisCode(anyString(), anyString());
    }

    @Test
    public void testModelIsValid() throws Exception {
        LuaenaUtlatande utlatande = getUtlatandeFromFile("utlatande");
        assertTrue(modelCompareUtil.isValidForNotification(utlatande));
    }

    @Test
    public void testModelIsValidDiagnoskodChanged() throws Exception {
        LuaenaUtlatande utlatandeOld = getUtlatandeFromFile("utlatande");
        Builder builder = utlatandeOld.toBuilder();
        LuaenaUtlatande utlatandeNew = builder.setDiagnoser(Arrays.asList(Diagnos.create("A00-", "KSH_97_P", "Kolera", ""))).build();

        assertTrue(modelCompareUtil.isValidForNotification(utlatandeNew));
    }

    @Test
    public void testModelIsInvalidDiagnosInvalid() throws Exception {
        LuaenaUtlatande utlatandeOld = getUtlatandeFromFile("utlatande");
        Builder builder = utlatandeOld.toBuilder();
        LuaenaUtlatande utlatandeNew = builder.setDiagnoser(Arrays.asList(Diagnos.create("BLAH", "ICD_10_SE", "Klämskada skuldra", ""))).build();

        assertFalse(modelCompareUtil.isValidForNotification(utlatandeNew));
    }

    @Test
    public void testModelIsInvalidWithInvalidDate() throws Exception {
        LuaenaUtlatande utlatandeOld = getUtlatandeFromFile("utlatande"); 
        Builder builder = utlatandeOld .toBuilder();
        LuaenaUtlatande utlatandeNew = builder.setUndersokningAvPatienten(new InternalDate("2016-1")).build();

        assertFalse(modelCompareUtil.isValidForNotification(utlatandeNew));
    }


    private LuaenaUtlatande getUtlatandeFromFile(String file) throws IOException {
        return objectMapper.readValue(new ClassPathResource(
                "LuaenaModelCompareUtil/"+ file + ".json").getFile(), LuaenaUtlatande.class);
    }

}
