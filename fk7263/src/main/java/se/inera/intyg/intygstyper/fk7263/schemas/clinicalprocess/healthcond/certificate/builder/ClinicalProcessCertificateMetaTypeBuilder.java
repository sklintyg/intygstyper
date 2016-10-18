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

package se.inera.intyg.intygstyper.fk7263.schemas.clinicalprocess.healthcond.certificate.builder;

import java.time.LocalDate;
import java.time.LocalDateTime;

import se.riv.clinicalprocess.healthcond.certificate.v1.CertificateMetaType;
import se.riv.clinicalprocess.healthcond.certificate.v1.StatusType;
import se.riv.clinicalprocess.healthcond.certificate.v1.UtlatandeStatus;


public class ClinicalProcessCertificateMetaTypeBuilder {

    private CertificateMetaType metaType;

    public ClinicalProcessCertificateMetaTypeBuilder() {
        metaType = new CertificateMetaType();
    }

    public CertificateMetaType build() {
        return metaType;
    }

    public ClinicalProcessCertificateMetaTypeBuilder certificateId(String certificateId) {
        metaType.setCertificateId(certificateId);
        return this;
    }

    public ClinicalProcessCertificateMetaTypeBuilder certificateType(String certificateType) {
        metaType.setCertificateType(certificateType);
        return this;
    }

    public ClinicalProcessCertificateMetaTypeBuilder validity(LocalDate fromDate, LocalDate toDate) {
        metaType.setValidFrom(fromDate);
        metaType.setValidTo(toDate);
        return this;
    }

    public ClinicalProcessCertificateMetaTypeBuilder issuerName(String issuerName) {
        metaType.setIssuerName(issuerName);
        return this;
    }

    public ClinicalProcessCertificateMetaTypeBuilder facilityName(String facilityName) {
        metaType.setFacilityName(facilityName);
        return this;
    }

    public ClinicalProcessCertificateMetaTypeBuilder signDate(LocalDateTime signDate) {
        metaType.setSignDate(signDate);
        return this;
    }

    public ClinicalProcessCertificateMetaTypeBuilder available(String available) {
        metaType.setAvailable(available);
        return this;
    }

    public ClinicalProcessCertificateMetaTypeBuilder status(StatusType status, String target, LocalDateTime timestamp) {
        UtlatandeStatus certificateStatusType = new UtlatandeStatus();
        certificateStatusType.setTarget(target);
        certificateStatusType.setTimestamp(timestamp);
        certificateStatusType.setType(status);
        metaType.getStatus().add(certificateStatusType);
        return this;
    }

    public ClinicalProcessCertificateMetaTypeBuilder status(UtlatandeStatus certificateStatusType) {
        metaType.getStatus().add(certificateStatusType);
        return this;
    }

    public ClinicalProcessCertificateMetaTypeBuilder complemantaryInfo(String complemantaryInfo) {
        metaType.setComplemantaryInfo(complemantaryInfo);
        return this;
    }
}
