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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import se.inera.intyg.common.support.model.CertificateState;
import se.inera.intyg.common.support.model.Status;
import se.inera.intyg.common.support.modules.support.api.dto.CertificateMetaData;
import se.riv.clinicalprocess.healthcond.certificate.v1.*;

public class ClinicalProcessCertificateMetaTypeConverterTest {

    @Test
    public void testToCertificateMetaData() {
        final String certificateId = "certificateId";
        final String certificateType = "certificateType";
        final LocalDate validFrom = LocalDate.now().minusDays(2);
        final LocalDate validTo = LocalDate.now();
        final String issuerName = "issuer name";
        final String facilityName = "facility name";
        final LocalDateTime signDate = LocalDateTime.now();
        final String complementaryInfo = "complementary info";

        CertificateMetaType source = new CertificateMetaType();
        source.setCertificateId(certificateId);
        source.setCertificateType(certificateType);
        source.setValidFrom(validFrom);
        source.setValidTo(validTo);
        source.setIssuerName(issuerName);
        source.setFacilityName(facilityName);
        source.setSignDate(signDate);
        source.setComplemantaryInfo(complementaryInfo);
        source.setAvailable("true");
        source.getStatus().add(buildStatus("target", StatusType.RECEIVED, LocalDateTime.now()));
        CertificateMetaData res = ClinicalProcessCertificateMetaTypeConverter.toCertificateMetaData(source);

        assertNotNull(res);
        assertEquals(certificateId, res.getCertificateId());
        assertEquals(certificateType, res.getCertificateType());
        assertEquals(validFrom, res.getValidFrom());
        assertEquals(validTo, res.getValidTo());
        assertEquals(issuerName, res.getIssuerName());
        assertEquals(facilityName, res.getFacilityName());
        assertEquals(signDate, res.getSignDate());
        assertEquals(complementaryInfo, res.getAdditionalInfo());
        assertTrue(res.isAvailable());
        assertEquals(1, res.getStatus().size());
    }

    @Test
    public void testToCertificateMetaDataAvailableFalse() {
        CertificateMetaType source = new CertificateMetaType();
        source.setAvailable("false");
        CertificateMetaData res = ClinicalProcessCertificateMetaTypeConverter.toCertificateMetaData(source);

        assertNotNull(res);
        assertFalse(res.isAvailable());
        assertTrue(res.getStatus().isEmpty());
    }

    @Test
    public void testToStatusList() {
        final String target1 = "target1";
        final String target2 = "target2";
        final String target3 = "target3";
        final LocalDateTime timestamp1 = LocalDateTime.now();
        final LocalDateTime timestamp2 = LocalDateTime.now().minusDays(2);
        final LocalDateTime timestamp3 = LocalDateTime.now().minusDays(1);
        List<UtlatandeStatus> source = new ArrayList<>();
        source.add(buildStatus(target1, StatusType.CANCELLED, timestamp1));
        source.add(buildStatus(target2, StatusType.RECEIVED, timestamp2));
        source.add(buildStatus(target3, StatusType.SENT, timestamp3));
        List<Status> res = ClinicalProcessCertificateMetaTypeConverter.toStatusList(source);

        assertNotNull(res);
        assertEquals(3, res.size());
        assertEquals(target1, res.get(0).getTarget());
        assertEquals(CertificateState.CANCELLED, res.get(0).getType());
        assertEquals(timestamp1, res.get(0).getTimestamp());
        assertEquals(target2, res.get(1).getTarget());
        assertEquals(CertificateState.RECEIVED, res.get(1).getType());
        assertEquals(timestamp2, res.get(1).getTimestamp());
        assertEquals(target3, res.get(2).getTarget());
        assertEquals(CertificateState.SENT, res.get(2).getType());
        assertEquals(timestamp3, res.get(2).getTimestamp());
    }

    @Test
    public void testToStatus() {
        final String target = "target";
        final LocalDateTime timestamp = LocalDateTime.now();
        UtlatandeStatus source = buildStatus(target, StatusType.DELETED, timestamp);
        Status res = ClinicalProcessCertificateMetaTypeConverter.toStatus(source);

        assertNotNull(res);
        assertEquals(target, res.getTarget());
        assertEquals(CertificateState.DELETED, res.getType());
        assertEquals(timestamp, res.getTimestamp());
    }

    private UtlatandeStatus buildStatus(String target, StatusType type, LocalDateTime timestamp) {
        UtlatandeStatus status = new UtlatandeStatus();
        status.setTarget(target);
        status.setTimestamp(timestamp);
        status.setType(type);
        return status;
    }
}
