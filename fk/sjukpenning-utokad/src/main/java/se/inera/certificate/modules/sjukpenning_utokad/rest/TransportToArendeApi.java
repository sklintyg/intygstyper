package se.inera.certificate.modules.sjukpenning_utokad.rest;

import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import se.inera.certificate.modules.sjukpenning_utokad.model.internal.SjukpenningUtokadUtlatande;
import se.inera.intyg.common.support.model.common.internal.Utlatande;

public class TransportToArendeApi {

    public static Map<String, Object> getModuleSpecificArendeParameters(Utlatande utlatande) {
        List<String> filledPositions = new ArrayList<>();
        SjukpenningUtokadUtlatande utokatUtlatande = (SjukpenningUtokadUtlatande) utlatande;
        if (utokatUtlatande.getUndersokningAvPatienten() != null) {
            filledPositions.add(GRUNDFORMEDICINSKTUNDERLAG_UNDERSOKNING_AV_PATIENT_SVAR_JSON_ID_1);
        }
        if (utokatUtlatande.getJournaluppgifter() != null) {
            filledPositions.add(GRUNDFORMEDICINSKTUNDERLAG_JOURNALUPPGIFTER_SVAR_JSON_ID_1);
        }
        if (utokatUtlatande.getTelefonkontaktMedPatienten() != null) {
            filledPositions.add(GRUNDFORMEDICINSKTUNDERLAG_TELEFONKONTAKT_PATIENT_SVAR_JSON_ID_1);
        }
        if (utokatUtlatande.getAnnatGrundForMU() != null) {
            filledPositions.add(GRUNDFORMEDICINSKTUNDERLAG_ANNAT_SVAR_JSON_ID_1);
        }
        Map<String, Object> result = new HashMap<>();
        result.put(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1, filledPositions);
        return result;
    }

}
