package se.inera.intyg.intygstyper.lisu.model.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static se.inera.intyg.intygstyper.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_ANNAT_SVAR_JSON_ID_1;
import static se.inera.intyg.intygstyper.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_JOURNALUPPGIFTER_SVAR_JSON_ID_1;
import static se.inera.intyg.intygstyper.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_TELEFONKONTAKT_PATIENT_SVAR_JSON_ID_1;
import static se.inera.intyg.intygstyper.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_UNDERSOKNING_AV_PATIENT_SVAR_JSON_ID_1;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import se.inera.intyg.common.support.model.InternalDate;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.intygstyper.lisu.model.internal.LisuUtlatande;

@RunWith(MockitoJUnitRunner.class)
public class SvarIdHelperTest {

    private final InternalDate INTERNAL_DATE = new InternalDate();
    private SvarIdHelperImpl svarIdHelper = new SvarIdHelperImpl();

    @Test
    public void testCalculateFrageIdHandleForGrundForMUNoValues() throws ConverterException {
        List<String> res = svarIdHelper.calculateFrageIdHandleForGrundForMU(buildUtlatande(null, null, null, null));
        assertNotNull(res);
        assertTrue(res.isEmpty());
    }

    @Test
    public void testCalculateFrageIdHandleForGrundForMUUndersokningAvPatienten() throws ConverterException {
        List<String> res = svarIdHelper.calculateFrageIdHandleForGrundForMU(buildUtlatande(INTERNAL_DATE, null, null, null));
        assertNotNull(res);
        assertEquals(1, res.size());
        assertEquals(GRUNDFORMEDICINSKTUNDERLAG_UNDERSOKNING_AV_PATIENT_SVAR_JSON_ID_1, res.get(0));
    }

    @Test
    public void testCalculateFrageIdHandleForGrundForMUJournalUppgifter() throws ConverterException {
        List<String> res = svarIdHelper.calculateFrageIdHandleForGrundForMU(buildUtlatande(null, INTERNAL_DATE, null, null));
        assertNotNull(res);
        assertEquals(1, res.size());
        assertEquals(GRUNDFORMEDICINSKTUNDERLAG_JOURNALUPPGIFTER_SVAR_JSON_ID_1, res.get(0));
    }

    @Test
    public void testCalculateFrageIdHandleForGrundForMUTelefonkontaktMedPatienten() throws ConverterException {
        List<String> res = svarIdHelper.calculateFrageIdHandleForGrundForMU(buildUtlatande(null, null, INTERNAL_DATE, null));
        assertNotNull(res);
        assertEquals(1, res.size());
        assertEquals(GRUNDFORMEDICINSKTUNDERLAG_TELEFONKONTAKT_PATIENT_SVAR_JSON_ID_1, res.get(0));
    }

    @Test
    public void testCalculateFrageIdHandleForGrundForMUAnnatGrundForMU() throws ConverterException {
        List<String> res = svarIdHelper.calculateFrageIdHandleForGrundForMU(buildUtlatande(null, null, null, INTERNAL_DATE));
        assertNotNull(res);
        assertEquals(1, res.size());
        assertEquals(GRUNDFORMEDICINSKTUNDERLAG_ANNAT_SVAR_JSON_ID_1, res.get(0));
    }

    @Test
    public void testCalculateFrageIdHandleForGrundForMUAll() throws ConverterException {
        List<String> res = svarIdHelper.calculateFrageIdHandleForGrundForMU(buildUtlatande(INTERNAL_DATE, INTERNAL_DATE, INTERNAL_DATE, INTERNAL_DATE));
        assertNotNull(res);
        assertEquals(4, res.size());
        assertEquals(GRUNDFORMEDICINSKTUNDERLAG_UNDERSOKNING_AV_PATIENT_SVAR_JSON_ID_1, res.get(0));
        assertEquals(GRUNDFORMEDICINSKTUNDERLAG_JOURNALUPPGIFTER_SVAR_JSON_ID_1, res.get(1));
        assertEquals(GRUNDFORMEDICINSKTUNDERLAG_TELEFONKONTAKT_PATIENT_SVAR_JSON_ID_1, res.get(2));
        assertEquals(GRUNDFORMEDICINSKTUNDERLAG_ANNAT_SVAR_JSON_ID_1, res.get(3));
    }

    private LisuUtlatande buildUtlatande(InternalDate undersokningAvPatienten, InternalDate journaluppgifter,
            InternalDate telefonkontaktMedPatienten, InternalDate annatGrundForMU) {
        return LisuUtlatande.builder()
                .setId("intygId")
                .setGrundData(new GrundData())
                .setTextVersion("v1.0")
                .setUndersokningAvPatienten(undersokningAvPatienten)
                .setJournaluppgifter(journaluppgifter)
                .setTelefonkontaktMedPatienten(telefonkontaktMedPatienten)
                .setAnnatGrundForMU(annatGrundForMU)
                .build();
    }
}