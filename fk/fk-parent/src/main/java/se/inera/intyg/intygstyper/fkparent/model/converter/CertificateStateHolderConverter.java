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

package se.inera.intyg.intygstyper.fkparent.model.converter;

import static se.inera.intyg.common.support.Constants.KV_PART_CODE_SYSTEM;
import static se.inera.intyg.common.support.Constants.KV_STATUS_CODE_SYSTEM;

import java.util.*;

import se.inera.intyg.common.support.common.enumerations.PartKod;
import se.inera.intyg.common.support.model.CertificateState;
import se.inera.intyg.common.support.model.StatusKod;
import se.inera.intyg.common.support.modules.support.api.CertificateStateHolder;
import se.riv.clinicalprocess.healthcond.certificate.types.v2.Part;
import se.riv.clinicalprocess.healthcond.certificate.types.v2.Statuskod;
import se.riv.clinicalprocess.healthcond.certificate.v2.IntygsStatus;

public final class CertificateStateHolderConverter {


    private CertificateStateHolderConverter() {
    }

    public static List<IntygsStatus> toIntygsStatusType(List<CertificateStateHolder> source) {
        if (source == null || source.isEmpty()) {
            return Collections.emptyList();
        }

        List<IntygsStatus> states = new ArrayList<>();
        for (CertificateStateHolder state : source) {
            states.add(toIntygsStatusType(state));
        }
        return states;
    }

    private static IntygsStatus toIntygsStatusType(CertificateStateHolder source) {
        IntygsStatus status = new IntygsStatus();
        status.setStatus(toStatuskod(source.getState()));
        status.setTidpunkt(source.getTimestamp());
        status.setPart(toPart(source.getTarget()));

        return status;
    }

    private static Statuskod toStatuskod(CertificateState state) {
        StatusKod statuskodEnum = toStatusKod(state);
        Statuskod statuskod = new Statuskod();
        statuskod.setCodeSystem(KV_STATUS_CODE_SYSTEM);
        statuskod.setCode(statuskodEnum.name());
        statuskod.setDisplayName(statuskodEnum.getDisplayName());
        return statuskod;
    }

    private static StatusKod toStatusKod(CertificateState state) {
        switch (state) {
        case RECEIVED:
            return StatusKod.RECEIV;
        case SENT:
            return StatusKod.SENTTO;
        case CANCELLED:
            return StatusKod.CANCEL;
        case DELETED:
            return StatusKod.DELETE;
        case RESTORED:
            return StatusKod.RESTOR;
        default:
            throw new IllegalArgumentException(state.toString());
        }
    }

    private static Part toPart(String target) {
        PartKod partKod = PartKod.fromValue(target);
        Part part = new Part();
        part.setCode(partKod.name());
        part.setCodeSystem(KV_PART_CODE_SYSTEM);
        part.setDisplayName(partKod.getDisplayName());
        return part;
    }
}
