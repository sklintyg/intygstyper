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
package se.inera.intyg.intygstyper.lisjp.model.converter;

import static se.inera.intyg.intygstyper.fkparent.model.converter.RespConstants.*;

import java.util.ArrayList;
import java.util.List;

import se.inera.intyg.intygstyper.fkparent.model.converter.RespConstants;
import se.inera.intyg.intygstyper.fkparent.model.converter.SvarIdHelper;
import se.inera.intyg.intygstyper.lisjp.model.internal.LisjpUtlatande;

public class SvarIdHelperImpl implements SvarIdHelper<LisjpUtlatande> {

    @Override
    public List<String> calculateFrageIdHandleForGrundForMU(LisjpUtlatande utlatande) {
        List<String> filledPositions = new ArrayList<>();
        filledPositions.add(RespConstants.getJsonPropertyFromFrageId(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1));
        if (utlatande.getUndersokningAvPatienten() != null) {
            filledPositions.add(GRUNDFORMEDICINSKTUNDERLAG_UNDERSOKNING_AV_PATIENT_SVAR_JSON_ID_1);
        }
        if (utlatande.getJournaluppgifter() != null) {
            filledPositions.add(GRUNDFORMEDICINSKTUNDERLAG_JOURNALUPPGIFTER_SVAR_JSON_ID_1);
        }
        if (utlatande.getTelefonkontaktMedPatienten() != null) {
            filledPositions.add(GRUNDFORMEDICINSKTUNDERLAG_TELEFONKONTAKT_PATIENT_SVAR_JSON_ID_1);
        }
        if (utlatande.getAnnatGrundForMU() != null) {
            filledPositions.add(GRUNDFORMEDICINSKTUNDERLAG_ANNAT_SVAR_JSON_ID_1);
        }
        return filledPositions;
    }
}
