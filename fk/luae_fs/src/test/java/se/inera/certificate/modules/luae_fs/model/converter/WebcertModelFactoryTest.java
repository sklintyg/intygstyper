package se.inera.certificate.modules.luae_fs.model.converter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import se.inera.certificate.modules.luae_fs.model.internal.LuaefsUtlatande;
import se.inera.certificate.modules.luae_fs.support.LuaefsEntryPoint;
import se.inera.intyg.common.services.texts.IntygTextsService;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.support.modules.support.api.dto.CreateNewDraftHolder;
import se.inera.intyg.common.support.modules.support.api.dto.HoSPersonal;
import se.inera.intyg.common.support.modules.support.api.dto.Patient;
import se.inera.intyg.common.support.modules.support.api.dto.Personnummer;
import se.inera.intyg.common.support.modules.support.api.dto.Vardenhet;
import se.inera.intyg.common.support.modules.support.api.dto.Vardgivare;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

/**
 * Created by eriklupander on 2016-04-20.
 */
@RunWith(MockitoJUnitRunner.class)
public class WebcertModelFactoryTest {

    private static final String INTYG_ID = "intyg-123";
    @Mock
    private IntygTextsService intygTextsService;

    @InjectMocks
    WebcertModelFactory testee;

    @Test
    public void testHappyPath() throws ConverterException {
        when(intygTextsService.getLatestVersion(LuaefsEntryPoint.MODULE_ID)).thenReturn("1.0");
        LuaefsUtlatande newWebcertDraft = testee.createNewWebcertDraft(buildNewDraftData());
        assertNotNull(newWebcertDraft);
    }

    private CreateNewDraftHolder buildNewDraftData() {
        CreateNewDraftHolder draftHolder = new CreateNewDraftHolder(INTYG_ID, buildHosPersonal(), buildPatient());
        return draftHolder;
    }

    private Patient buildPatient() {
        Patient patient = new Patient("Test", "Prov", "Testorsson", new Personnummer("19121212-1212"), "Gågatan", "12345", "Staden");

        return patient;
    }

    private HoSPersonal buildHosPersonal() {

        Vardgivare vardgivare = new Vardgivare("VG1", "Vardgivaren");
        Vardenhet vardenhet = new Vardenhet("VE1", "Sjukhuset", "Plåstergatan", null, null, null, null, null, vardgivare);
        HoSPersonal skapadAv = new HoSPersonal("TST12345678", "Dr Dengroth", "1234", "Proktolog", null, vardenhet);

        return skapadAv;
    }
}
