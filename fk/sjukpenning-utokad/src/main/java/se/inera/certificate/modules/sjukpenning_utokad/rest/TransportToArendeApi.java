/*
 * Copyright (C) 2016 Inera AB (http://www.inera.se)
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
package se.inera.certificate.modules.sjukpenning_utokad.rest;

import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import se.inera.certificate.modules.sjukpenning_utokad.model.internal.SjukpenningUtokadUtlatande;
import se.inera.intyg.common.support.model.common.internal.Utlatande;

public final class TransportToArendeApi {

    private TransportToArendeApi() {
    }

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
