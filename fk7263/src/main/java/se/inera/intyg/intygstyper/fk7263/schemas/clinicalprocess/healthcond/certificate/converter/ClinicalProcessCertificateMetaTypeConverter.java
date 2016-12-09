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

import java.util.ArrayList;
import java.util.List;

import se.inera.intyg.common.support.model.CertificateState;
import se.inera.intyg.common.support.model.Status;
import se.inera.intyg.common.support.modules.support.api.dto.CertificateMetaData;
import se.riv.clinicalprocess.healthcond.certificate.v1.CertificateMetaType;
import se.riv.clinicalprocess.healthcond.certificate.v1.UtlatandeStatus;

public final class ClinicalProcessCertificateMetaTypeConverter {

    private ClinicalProcessCertificateMetaTypeConverter() {
    }

    public static CertificateMetaData toCertificateMetaData(CertificateMetaType certificateMetaType) {
        CertificateMetaData metaData = new CertificateMetaData();
        metaData.setCertificateId(certificateMetaType.getCertificateId());
        metaData.setCertificateType(certificateMetaType.getCertificateType());
        metaData.setValidFrom(certificateMetaType.getValidFrom());
        metaData.setValidTo(certificateMetaType.getValidTo());
        metaData.setIssuerName(certificateMetaType.getIssuerName());
        metaData.setFacilityName(certificateMetaType.getFacilityName());
        metaData.setSignDate(certificateMetaType.getSignDate());
        metaData.setAdditionalInfo(certificateMetaType.getComplemantaryInfo());
        metaData.setAvailable(certificateMetaType.getAvailable().toLowerCase().equals("true"));
        List<Status> statuses = toStatusList(certificateMetaType.getStatus());
        metaData.setStatus(statuses);
        return metaData;
    }

    public static List<Status> toStatusList(List<UtlatandeStatus> certificateStatuses) {
        List<Status> statuses = new ArrayList<>(certificateStatuses.size());
        for (UtlatandeStatus certificateStatus : certificateStatuses) {
            statuses.add(toStatus(certificateStatus));
        }
        return statuses;
    }

    public static Status toStatus(UtlatandeStatus certificateStatus) {
        return new Status(CertificateState.valueOf(certificateStatus.getType().value()), certificateStatus.getTarget(),
                certificateStatus.getTimestamp());
    }
}
