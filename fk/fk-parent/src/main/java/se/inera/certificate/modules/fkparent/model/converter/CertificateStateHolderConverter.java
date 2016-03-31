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

package se.inera.certificate.modules.fkparent.model.converter;


import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.PART_CODE_SYSTEM;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.STATUS_KOD_CODE_SYSTEM;

import java.util.*;

import se.inera.intyg.common.support.model.CertificateState;
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
        Statuskod statuskod = new Statuskod();
        statuskod.setCodeSystem(STATUS_KOD_CODE_SYSTEM);
        switch (state) {
            case RECEIVED:
                statuskod.setCode("RECEIV");
                statuskod.setDisplayName("RECEIVED");
                break;
            case SENT:
                statuskod.setCode("SENTTO");
                statuskod.setDisplayName("SENT");
                break;
            case CANCELLED:
                statuskod.setCode("CANCEL");
                statuskod.setDisplayName("CANCELLED");
                break;
            case DELETED:
                statuskod.setCode("DELETE");
                statuskod.setDisplayName("DELETED");
                break;
            case RESTORED:
                statuskod.setCode("RESTOR");
                statuskod.setDisplayName("RESTORED");
                break;
            default:
                throw new IllegalArgumentException(state.toString());
        }
        return statuskod;
    }

    private static Part toPart(String target) {
        Part part = new Part();
        part.setCodeSystem(PART_CODE_SYSTEM);
        switch (target) {
            case "FK":
                part.setCode("FKASSA");
                part.setDisplayName("Försäkringskassan");
                break;
            case "MI":
                part.setCode("INVANA");
                part.setDisplayName("Invånaren");
                break;
            case "TS":
                part.setCode("TRANSP");
                part.setDisplayName("Transportstyrelsen");
                break;
            case "HV":
                part.setCode("HSVARD");
                part.setDisplayName("Hälso- och sjukvården");
                break;
            default:
                throw new IllegalArgumentException(target);
        }
        return part;
    }
}
