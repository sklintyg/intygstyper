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

import java.time.LocalDate;

import se.inera.intyg.intygstyper.fk7263.schemas.clinicalprocess.healthcond.certificate.builder.ClinicalProcessCertificateMetaTypeBuilder;
import se.inera.intyg.common.support.modules.support.api.CertificateHolder;
import se.riv.clinicalprocess.healthcond.certificate.v1.CertificateMetaType;

/**
 * @author andreaskaltenbach
 */
public final class ModelConverter {

    private ModelConverter() {
    }

    public static CertificateMetaType toCertificateMetaType(CertificateHolder source) {

        ClinicalProcessCertificateMetaTypeBuilder builder = new ClinicalProcessCertificateMetaTypeBuilder()
                .certificateId(source.getId())
                .certificateType(source.getType())
                .validity(toLocalDate(source.getValidFromDate()), toLocalDate(source.getValidToDate()))
                .issuerName(source.getSigningDoctorName())
                .facilityName(source.getCareUnitName())
                .signDate(source.getSignedDate())
                .available(String.valueOf(!source.isDeleted()))
                .complemantaryInfo(source.getAdditionalInfo());

        CertificateMetaType meta = builder.build();

        meta.getStatus().addAll(CertificateStateHolderConverter.toCertificateStatusType(source.getCertificateStates()));
        return builder.build();
    }


    private static LocalDate toLocalDate(String date) {
        if (date == null) {
            return null;
        }
        return LocalDate.parse(date);
    }

}
