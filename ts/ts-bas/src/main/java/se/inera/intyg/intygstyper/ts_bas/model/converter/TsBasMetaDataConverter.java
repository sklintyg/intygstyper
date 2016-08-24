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

package se.inera.intyg.intygstyper.ts_bas.model.converter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import se.inera.intyg.common.support.model.CertificateState;
import se.inera.intyg.common.support.model.Status;
import se.inera.intyg.common.support.modules.support.api.dto.CertificateMetaData;
import se.inera.intygstjanster.ts.services.v1.*;

public final class TsBasMetaDataConverter {

    private TsBasMetaDataConverter() {
    }

    public static CertificateMetaData toCertificateMetaData(IntygMeta intygMeta, TSBasIntyg intyg) {
        CertificateMetaData metaData = new CertificateMetaData();
        metaData.setCertificateId(intyg.getIntygsId());
        metaData.setCertificateType(intyg.getIntygsTyp());
        metaData.setIssuerName(intyg.getGrundData().getSkapadAv().getFullstandigtNamn());
        metaData.setFacilityName(intyg.getGrundData().getSkapadAv().getVardenhet().getEnhetsnamn());
        metaData.setSignDate(LocalDateTime.parse(intyg.getGrundData().getSigneringsTidstampel(), DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        metaData.setAdditionalInfo(intygMeta.getAdditionalInfo());
        metaData.setAvailable(intygMeta.getAvailable().toLowerCase().equals("true"));
        List<Status> statuses = toStatusList(intygMeta.getStatus());
        metaData.setStatus(statuses);
        return metaData;
    }

    public static List<Status> toStatusList(List<IntygStatus> certificateStatuses) {
        List<Status> statuses = certificateStatuses != null ? new ArrayList<>(certificateStatuses.size()) : Collections.<Status> emptyList();
        if (certificateStatuses != null) {
            for (IntygStatus certificateStatus : certificateStatuses) {
                if (certificateStatus != null) {
                    statuses.add(toStatus(certificateStatus));
                }
            }
        }
        return statuses;
    }

    public static Status toStatus(IntygStatus certificateStatus) {
        return new Status(CertificateState.valueOf(certificateStatus.getType().value()),
                certificateStatus.getTarget(),
                LocalDateTime.parse(certificateStatus.getTimestamp(), DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    }
}
