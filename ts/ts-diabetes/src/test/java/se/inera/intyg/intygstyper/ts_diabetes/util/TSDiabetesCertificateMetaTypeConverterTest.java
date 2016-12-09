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

package se.inera.intyg.intygstyper.ts_diabetes.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import org.junit.Test;

import se.inera.intyg.common.support.model.CertificateState;
import se.inera.intyg.common.support.model.Status;
import se.inera.intyg.common.support.modules.support.api.dto.CertificateMetaData;
import se.inera.intygstjanster.ts.services.v1.*;

public class TSDiabetesCertificateMetaTypeConverterTest {

    @Test
    public void testToCertificateMetaData() {
        final String certificateId = "certificateId";
        final String certificateType = "certificateType";
        final String issuerName = "issuer name";
        final String facilityName = "facility name";
        final String signDate = "2016-10-11T14:54:33";
        final String additionalInfo = "additional info";
        IntygMeta source = new IntygMeta();
        source.setAdditionalInfo(additionalInfo);
        source.setAvailable("true");
        IntygStatus status = new IntygStatus();
        status.setType(se.inera.intygstjanster.ts.services.v1.Status.RECEIVED);
        status.setTarget("target");
        status.setTimestamp("2016-10-11T13:32:33");
        source.getStatus().add(status);
        TSDiabetesIntyg intyg = new TSDiabetesIntyg();
        intyg.setIntygsId(certificateId);
        intyg.setIntygsTyp(certificateType);
        intyg.setGrundData(new GrundData());
        intyg.getGrundData().setSkapadAv(new SkapadAv());
        intyg.getGrundData().getSkapadAv().setFullstandigtNamn(issuerName);
        intyg.getGrundData().getSkapadAv().setVardenhet(new Vardenhet());
        intyg.getGrundData().getSkapadAv().getVardenhet().setEnhetsnamn(facilityName);
        intyg.getGrundData().setSigneringsTidstampel(signDate);
        CertificateMetaData res = TSDiabetesCertificateMetaTypeConverter.toCertificateMetaData(source, intyg);

        assertNotNull(res);
        assertEquals(certificateId, res.getCertificateId());
        assertEquals(certificateType, res.getCertificateType());
        assertEquals(issuerName, res.getIssuerName());
        assertEquals(facilityName, res.getFacilityName());
        assertEquals(signDate, res.getSignDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        assertEquals(additionalInfo, res.getAdditionalInfo());
        assertTrue(res.isAvailable());
        assertNotNull(res.getStatus());
        assertEquals(1, res.getStatus().size());
    }

    @Test
    public void testToCertificateMetaDataNotAvailable() {
        IntygMeta source = new IntygMeta();
        source.setAvailable("false");
        TSDiabetesIntyg intyg = new TSDiabetesIntyg();
        intyg.setGrundData(new GrundData());
        intyg.getGrundData().setSkapadAv(new SkapadAv());
        intyg.getGrundData().getSkapadAv().setVardenhet(new Vardenhet());
        intyg.getGrundData().setSigneringsTidstampel("2016-10-11T14:54:33");
        CertificateMetaData res = TSDiabetesCertificateMetaTypeConverter.toCertificateMetaData(source, intyg);

        assertNotNull(res);
        assertFalse(res.isAvailable());
        assertNotNull(res.getStatus());
        assertTrue(res.getStatus().isEmpty());
    }

    @Test
    public void testToStatusList() {
        final String target1 = "target1";
        final String timestamp1 = "2016-10-11T10:10:00";
        final String target2 = "target2";
        final String timestamp2 = "2016-09-11T10:10:00";
        IntygStatus source1 = new IntygStatus();
        source1.setTarget(target1);
        source1.setTimestamp(timestamp1);
        source1.setType(se.inera.intygstjanster.ts.services.v1.Status.SENT);
        IntygStatus source2 = new IntygStatus();
        source2.setTarget(target2);
        source2.setTimestamp(timestamp2);
        source2.setType(se.inera.intygstjanster.ts.services.v1.Status.CANCELLED);
        List<Status> res = TSDiabetesCertificateMetaTypeConverter.toStatusList(Arrays.asList(source1, source2));

        assertNotNull(res);
        assertEquals(2, res.size());
        assertEquals(target1, res.get(0).getTarget());
        assertEquals(LocalDateTime.parse(timestamp1), res.get(0).getTimestamp());
        assertEquals(CertificateState.SENT, res.get(0).getType());
        assertEquals(target2, res.get(1).getTarget());
        assertEquals(LocalDateTime.parse(timestamp2), res.get(1).getTimestamp());
        assertEquals(CertificateState.CANCELLED, res.get(1).getType());
    }

    @Test
    public void testToStatusListNull() {
        List<Status> res = TSDiabetesCertificateMetaTypeConverter.toStatusList(null);

        assertNotNull(res);
        assertTrue(res.isEmpty());
    }

    @Test
    public void testToStatusListEmptyList() {
        List<Status> res = TSDiabetesCertificateMetaTypeConverter.toStatusList(new ArrayList<>());

        assertNotNull(res);
        assertTrue(res.isEmpty());
    }

    @Test
    public void testToStatus() {
        final String target = "target";
        final String timestamp = "2016-10-11T10:10:00";
        IntygStatus source = new IntygStatus();
        source.setTarget(target);
        source.setTimestamp(timestamp);
        source.setType(se.inera.intygstjanster.ts.services.v1.Status.RECEIVED);
        Status res = TSDiabetesCertificateMetaTypeConverter.toStatus(source);

        assertNotNull(res);
        assertEquals(target, res.getTarget());
        assertEquals(LocalDateTime.parse(timestamp), res.getTimestamp());
        assertEquals(CertificateState.RECEIVED, res.getType());
    }
}
