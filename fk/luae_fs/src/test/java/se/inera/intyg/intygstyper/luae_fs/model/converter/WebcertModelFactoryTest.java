package se.inera.intyg.intygstyper.luae_fs.model.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import org.joda.time.LocalDateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import se.inera.intyg.common.services.texts.IntygTextsService;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.support.model.converter.util.WebcertModelFactoryUtil;
import se.inera.intyg.common.support.modules.support.api.dto.*;
import se.inera.intyg.intygstyper.luae_fs.model.internal.LuaefsUtlatande;
import se.inera.intyg.intygstyper.luae_fs.support.LuaefsEntryPoint;

/**
 * Created by eriklupander on 2016-04-20.
 */
@RunWith(MockitoJUnitRunner.class)
public class WebcertModelFactoryTest {

    private static final String INTYG_ID = "intyg-123";
    @Mock
    private IntygTextsService intygTextsService;

    @InjectMocks
    WebcertModelFactoryImpl testee;

    @Test
    public void testHappyPath() throws ConverterException {
        when(intygTextsService.getLatestVersion(LuaefsEntryPoint.MODULE_ID)).thenReturn("1.0");
        LuaefsUtlatande draft = testee.createNewWebcertDraft(buildNewDraftData(INTYG_ID));
        assertNotNull(draft);
        assertEquals("VG1", draft.getGrundData().getSkapadAv().getVardenhet().getVardgivare().getVardgivarid());
        assertEquals("VE1", draft.getGrundData().getSkapadAv().getVardenhet().getEnhetsid());
        assertEquals("TST12345678", draft.getGrundData().getSkapadAv().getPersonId());
        assertEquals("191212121212", draft.getGrundData().getPatient().getPersonId().getPersonnummerWithoutDash());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullUtlatandeIdThrowsIllegalArgumentException() throws ConverterException {
        when(intygTextsService.getLatestVersion(LuaefsEntryPoint.MODULE_ID)).thenReturn("1.0");
        testee.createNewWebcertDraft(buildNewDraftData(null));
    }

    @Test(expected = ConverterException.class)
    public void testBlankUtlatandeIdThrowsIllegalArgumentException() throws ConverterException {
        when(intygTextsService.getLatestVersion(LuaefsEntryPoint.MODULE_ID)).thenReturn("1.0");
        testee.createNewWebcertDraft(buildNewDraftData(" "));
    }

    @Test
    public void testUpdateSkapadAv() throws ConverterException {
        when(intygTextsService.getLatestVersion(LuaefsEntryPoint.MODULE_ID)).thenReturn("1.0");
        LuaefsUtlatande draft = testee.createNewWebcertDraft(buildNewDraftData(INTYG_ID));
        WebcertModelFactoryUtil.updateSkapadAv(draft, buildHosPersonal(), LocalDateTime.now());
    }

    private CreateNewDraftHolder buildNewDraftData(String intygId) {
        CreateNewDraftHolder draftHolder = new CreateNewDraftHolder(intygId, buildHosPersonal(), buildPatient());
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