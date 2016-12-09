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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import se.inera.intyg.common.support.model.CertificateState;
import se.inera.intyg.common.support.model.StatusKod;
import se.inera.intyg.common.support.modules.support.api.CertificateStateHolder;
import se.riv.clinicalprocess.healthcond.certificate.v2.IntygsStatus;

public class CertificateStateHolderConverterTest {

    @Test
    public void testToIntygsStatusTypeNull() {
        List<IntygsStatus> res = CertificateStateHolderConverter.toIntygsStatusType(null);

        assertNotNull(res);
        assertTrue(res.isEmpty());
    }

    @Test
    public void testToIntygsStatusTypeEmptyList() {
        List<IntygsStatus> res = CertificateStateHolderConverter.toIntygsStatusType(new ArrayList<>());

        assertNotNull(res);
        assertTrue(res.isEmpty());
    }

    @Test
    public void testToIntygsStatusType() {
        final LocalDateTime timestamp1 = LocalDateTime.now();
        final LocalDateTime timestamp2 = LocalDateTime.now().minusDays(2);
        final LocalDateTime timestamp3 = LocalDateTime.now().minusDays(1);
        final LocalDateTime timestamp4 = LocalDateTime.now().minusDays(1);
        final LocalDateTime timestamp5 = LocalDateTime.now().minusDays(1);
        List<CertificateStateHolder> source = new ArrayList<>();
        source.add(new CertificateStateHolder("FK", CertificateState.CANCELLED, timestamp1));
        source.add(new CertificateStateHolder("TS", CertificateState.RECEIVED, timestamp2));
        source.add(new CertificateStateHolder("HV", CertificateState.SENT, timestamp3));
        source.add(new CertificateStateHolder("MI", CertificateState.DELETED, timestamp4));
        source.add(new CertificateStateHolder("FK", CertificateState.RESTORED, timestamp5));
        List<IntygsStatus> res = CertificateStateHolderConverter.toIntygsStatusType(source);

        assertNotNull(res);
        assertEquals(5, res.size());
        assertEquals("FKASSA", res.get(0).getPart().getCode());
        assertEquals(StatusKod.CANCEL.name(), res.get(0).getStatus().getCode());
        assertEquals(timestamp1, res.get(0).getTidpunkt());
        assertEquals("TRANSP", res.get(1).getPart().getCode());
        assertEquals(StatusKod.RECEIV.name(), res.get(1).getStatus().getCode());
        assertEquals(timestamp2, res.get(1).getTidpunkt());
        assertEquals("HSVARD", res.get(2).getPart().getCode());
        assertEquals(StatusKod.SENTTO.name(), res.get(2).getStatus().getCode());
        assertEquals(timestamp3, res.get(2).getTidpunkt());
        assertEquals("INVANA", res.get(3).getPart().getCode());
        assertEquals(StatusKod.DELETE.name(), res.get(3).getStatus().getCode());
        assertEquals(timestamp4, res.get(3).getTidpunkt());
        assertEquals("FKASSA", res.get(4).getPart().getCode());
        assertEquals(StatusKod.RESTOR.name(), res.get(4).getStatus().getCode());
        assertEquals(timestamp5, res.get(4).getTidpunkt());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToIntygsStatusTypeInvalidStatus() {
        List<CertificateStateHolder> source = new ArrayList<>();
        source.add(new CertificateStateHolder("FK", CertificateState.UNHANDLED, LocalDateTime.now()));
        CertificateStateHolderConverter.toIntygsStatusType(source);
    }

    @Test
    public void testToIntygsStatusTypeSetsCodeSystemAndDisplayNameOfStatus() {
        List<CertificateStateHolder> source = new ArrayList<>();
        source.add(new CertificateStateHolder("FK", CertificateState.SENT, LocalDateTime.now()));
        List<IntygsStatus> res = CertificateStateHolderConverter.toIntygsStatusType(source);

        assertNotNull(res);
        assertEquals(StatusKod.SENTTO.name(), res.get(0).getStatus().getCode());
        assertEquals("9871cd17-8755-4ed9-b894-ff3729e775a4", res.get(0).getStatus().getCodeSystem());
        assertEquals("SENT", res.get(0).getStatus().getDisplayName());
    }

    @Test
    public void testToIntygsStatusTypeSetsCodeSystemAndDisplayNameOfPart() {
        List<CertificateStateHolder> source = new ArrayList<>();
        source.add(new CertificateStateHolder("FK", CertificateState.SENT, LocalDateTime.now()));
        List<IntygsStatus> res = CertificateStateHolderConverter.toIntygsStatusType(source);

        assertNotNull(res);
        assertEquals("FKASSA", res.get(0).getPart().getCode());
        assertEquals("769bb12b-bd9f-4203-a5cd-fd14f2eb3b80", res.get(0).getPart().getCodeSystem());
        assertEquals("Försäkringskassan", res.get(0).getPart().getDisplayName());
    }
}
