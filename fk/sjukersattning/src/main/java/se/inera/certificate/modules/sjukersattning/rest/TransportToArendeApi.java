package se.inera.certificate.modules.sjukersattning.rest;

import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import se.inera.certificate.modules.sjukersattning.model.internal.SjukersattningUtlatande;
import se.inera.intyg.common.support.model.common.internal.Utlatande;

public class TransportToArendeApi {

    public static Map<String, Object> getModuleSpecificArendeParameters(Utlatande utlatande) {
        List<String> filledPositions = new ArrayList<String>();
        SjukersattningUtlatande sjukersutlatande = (SjukersattningUtlatande) utlatande;
        if (sjukersutlatande.getUndersokningAvPatienten() != null) {
            filledPositions.add(GRUNDFORMEDICINSKTUNDERLAG_UNDERSOKNING_AV_PATIENT_SVAR_JSON_ID_1);
        }
        if (sjukersutlatande.getJournaluppgifter() != null) {
            filledPositions.add(GRUNDFORMEDICINSKTUNDERLAG_JOURNALUPPGIFTER_SVAR_JSON_ID_1);
        }
        if (sjukersutlatande.getAnhorigsBeskrivningAvPatienten() != null) {
            filledPositions.add(GRUNDFORMEDICINSKTUNDERLAG_ANHORIGS_BESKRIVNING_SVAR_JSON_ID_1);
        }
        if (sjukersutlatande.getAnnatGrundForMU() != null) {
            filledPositions.add(GRUNDFORMEDICINSKTUNDERLAG_ANNAT_SVAR_JSON_ID_1);
        }
        Map<String, Object> result = new HashMap<>();
        result.put(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1, filledPositions);
        return result;
    }

}
