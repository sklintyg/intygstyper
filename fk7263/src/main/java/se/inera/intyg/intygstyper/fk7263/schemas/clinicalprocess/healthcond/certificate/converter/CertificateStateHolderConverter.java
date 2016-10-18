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

package se.inera.intyg.intygstyper.fk7263.schemas.clinicalprocess.healthcond.certificate.converter;

import se.riv.clinicalprocess.healthcond.certificate.v1.StatusType;
import se.riv.clinicalprocess.healthcond.certificate.v1.UtlatandeStatus;
import se.inera.intyg.common.support.modules.support.api.CertificateStateHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author andreaskaltenbach
 */
public final class CertificateStateHolderConverter {

    private CertificateStateHolderConverter() {
    }

    public static List<UtlatandeStatus> toCertificateStatusType(List<CertificateStateHolder> source) {
        if (source == null || source.isEmpty()) {
            return Collections.emptyList();
        }

        List<UtlatandeStatus> states = new ArrayList<>();
        for (CertificateStateHolder state : source) {
            states.add(toCertificateStatusType(state));
        }
        return states;
    }

    private static UtlatandeStatus toCertificateStatusType(CertificateStateHolder source) {
        UtlatandeStatus status = new UtlatandeStatus();
        status.setTarget(source.getTarget());
        status.setTimestamp(source.getTimestamp());
        status.setType(StatusType.valueOf(source.getState().name()));
        return status;
    }
}
