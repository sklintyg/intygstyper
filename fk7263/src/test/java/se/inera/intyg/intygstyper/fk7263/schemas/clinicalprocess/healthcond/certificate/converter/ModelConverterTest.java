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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import org.junit.Test;

import se.inera.intyg.common.support.model.CertificateState;
import se.inera.intyg.common.support.modules.support.api.CertificateHolder;
import se.inera.intyg.common.support.modules.support.api.CertificateStateHolder;
import se.riv.clinicalprocess.healthcond.certificate.v1.CertificateMetaType;
import se.riv.clinicalprocess.healthcond.certificate.v1.StatusType;

public class ModelConverterTest {

    @Test
    public void testToCertificateMetaType() {
        String certificateId = "certificateId";
        String certificateType = "certificateType";
        String validFromDate = "2016-10-11";
        String validToDate = "2016-10-14";
        String signingDoctorName = "signingDoctorName";
        String careUnitName = "careUnitName";
        String additionalInfo = "additionalInfo";
        LocalDateTime signedDate = LocalDateTime.now();
        CertificateHolder source = buildCertificateHolder(certificateId, certificateType, validFromDate, validToDate, signingDoctorName, careUnitName,
                signedDate, additionalInfo, false);

        CertificateMetaType res = ModelConverter.toCertificateMetaType(source);

        assertNotNull(res);
        assertEquals(certificateId, res.getCertificateId());
        assertEquals(certificateType, res.getCertificateType());
        assertEquals(validFromDate, res.getValidFrom().format(DateTimeFormatter.ISO_LOCAL_DATE));
        assertEquals(validToDate, res.getValidTo().format(DateTimeFormatter.ISO_LOCAL_DATE));
        assertEquals(signingDoctorName, res.getIssuerName());
        assertEquals(careUnitName, res.getFacilityName());
        assertEquals(signedDate.format(DateTimeFormatter.ISO_LOCAL_DATE), res.getSignDate().format(DateTimeFormatter.ISO_LOCAL_DATE));
        assertEquals(additionalInfo, res.getComplemantaryInfo());
        assertEquals("true", res.getAvailable());
        assertEquals(1, res.getStatus().size());
        assertEquals(StatusType.RECEIVED, res.getStatus().get(0).getType());
    }

    @Test
    public void testToCertificateMetaTypeDateMissing() {
        CertificateHolder source = buildCertificateHolder("certificateId", "certificateType", "2016-10-11", null, "signingDoctorName", "careUnitName",
                LocalDateTime.now(), "additionalInfo", false);

        CertificateMetaType res = ModelConverter.toCertificateMetaType(source);

        assertNotNull(res);
        assertNull(res.getValidTo());
    }

    @Test
    public void testToCertificateMetaTypeDeleted() {
        CertificateHolder source = buildCertificateHolder("certificateId", "certificateType", "2016-10-11", "2016-10-14", "signingDoctorName", "careUnitName",
                LocalDateTime.now(), "additionalInfo", true);

        CertificateMetaType res = ModelConverter.toCertificateMetaType(source);

        assertNotNull(res);
        assertEquals("false", res.getAvailable());
    }

    @Test
    public void testToCertificateMetaTypeSignedDateMissing() {
        CertificateHolder source = buildCertificateHolder("certificateId", "certificateType", "2016-10-11", "2016-10-14", "signingDoctorName", "careUnitName",
                null, "additionalInfo", false);

        CertificateMetaType res = ModelConverter.toCertificateMetaType(source);

        assertNotNull(res);
        assertNull(res.getSignDate());
    }

    private CertificateHolder buildCertificateHolder(String certificateId, String certificateType, String validFromDate, String validToDate,
            String signingDoctorName, String careUnitName, LocalDateTime signedDate, String additionalInfo, boolean isDeleted) {
        CertificateHolder source = new CertificateHolder();
        source.setId(certificateId);
        source.setType(certificateType);
        source.setValidFromDate(validFromDate);
        source.setValidToDate(validToDate);
        source.setSigningDoctorName(signingDoctorName);
        source.setCareUnitName(careUnitName);
        source.setSignedDate(signedDate);
        source.setAdditionalInfo(additionalInfo);
        source.setCertificateStates(new ArrayList<>());
        source.getCertificateStates().add(new CertificateStateHolder("target", CertificateState.RECEIVED, LocalDateTime.now().minusDays(2)));
        source.setDeleted(isDeleted);
        return source;
    }
}
