package se.inera.intyg.intygstyper.luse.model.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import org.apache.commons.collections.CollectionUtils;
import org.joda.time.LocalDateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import se.inera.intyg.common.services.texts.IntygTextsService;
import se.inera.intyg.common.support.model.common.internal.*;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.support.model.converter.util.WebcertModelFactoryUtil;
import se.inera.intyg.common.support.modules.support.api.dto.CreateNewDraftHolder;
import se.inera.intyg.common.support.modules.support.api.dto.Personnummer;
import se.inera.intyg.intygstyper.luse.model.internal.LuseUtlatande;
import se.inera.intyg.intygstyper.luse.support.LuseEntryPoint;

@RunWith(MockitoJUnitRunner.class)
public class WebcertModelFactoryTest {

    private static final String INTYG_ID = "intyg-123";

    @Mock
    private IntygTextsService intygTextsService;

    @InjectMocks
    WebcertModelFactoryImpl modelFactory;

    @Test
    public void testHappyPath() throws ConverterException {
        when(intygTextsService.getLatestVersion(LuseEntryPoint.MODULE_ID)).thenReturn("1.0");
        LuseUtlatande draft = modelFactory.createNewWebcertDraft(buildNewDraftData(INTYG_ID));
        assertNotNull(draft);
        assertEquals("VG1", draft.getGrundData().getSkapadAv().getVardenhet().getVardgivare().getVardgivarid());
        assertEquals("VE1", draft.getGrundData().getSkapadAv().getVardenhet().getEnhetsid());
        assertEquals("TST12345678", draft.getGrundData().getSkapadAv().getPersonId());
        assertEquals("191212121212", draft.getGrundData().getPatient().getPersonId().getPersonnummerWithoutDash());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullUtlatandeIdThrowsIllegalArgumentException() throws ConverterException {
        when(intygTextsService.getLatestVersion(LuseEntryPoint.MODULE_ID)).thenReturn("1.0");
        modelFactory.createNewWebcertDraft(buildNewDraftData(null));
    }

    @Test(expected = ConverterException.class)
    public void testBlankUtlatandeIdThrowsIllegalArgumentException() throws ConverterException {
        when(intygTextsService.getLatestVersion(LuseEntryPoint.MODULE_ID)).thenReturn("1.0");
        modelFactory.createNewWebcertDraft(buildNewDraftData(" "));
    }

    @Test
    public void testUpdateSkapadAv() throws ConverterException {
        when(intygTextsService.getLatestVersion(LuseEntryPoint.MODULE_ID)).thenReturn("1.0");
        LuseUtlatande draft = modelFactory.createNewWebcertDraft(buildNewDraftData(INTYG_ID));
        WebcertModelFactoryUtil.updateSkapadAv(draft, buildHosPersonal(), LocalDateTime.now());
    }

    @Test
    public void testCreateNewWebcertDraftDoesNotGenerateIncompleteSvarInTransportFormat() throws ConverterException {
        // this to follow schema during CertificateStatusUpdateForCareV2
        when(intygTextsService.getLatestVersion(LuseEntryPoint.MODULE_ID)).thenReturn("1.0");
        LuseUtlatande draft = modelFactory.createNewWebcertDraft(buildNewDraftData(INTYG_ID));
        assertTrue(CollectionUtils.isEmpty(InternalToTransport.convert(draft).getIntyg().getSvar()));
    }

    private CreateNewDraftHolder buildNewDraftData(String intygId) {
        CreateNewDraftHolder draftHolder = new CreateNewDraftHolder(intygId, buildHosPersonal(), buildPatient());
        return draftHolder;
    }

    private Patient buildPatient() {
        Patient patient = new Patient();
        patient.setFornamn("fornamn");
        patient.setEfternamn("efternamn");
        patient.setPersonId(new Personnummer("19121212-1212"));
        return patient;
    }

    private HoSPersonal buildHosPersonal() {
        HoSPersonal hosPerson = new HoSPersonal();
        hosPerson.setPersonId("TST12345678");
        hosPerson.setFullstandigtNamn("Doktor A");
        hosPerson.setVardenhet(createVardenhet());
        return hosPerson;
    }

    private Vardenhet createVardenhet() {
        Vardenhet vardenhet = new Vardenhet();
        vardenhet.setEnhetsid("VE1");
        vardenhet.setEnhetsnamn("ve1");
        vardenhet.setVardgivare(new Vardgivare());
        vardenhet.getVardgivare().setVardgivarid("VG1");
        vardenhet.getVardgivare().setVardgivarnamn("vg1");
        return vardenhet;
    }
}
