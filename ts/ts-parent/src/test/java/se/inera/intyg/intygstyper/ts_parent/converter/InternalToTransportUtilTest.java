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
package se.inera.intyg.intygstyper.ts_parent.converter;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;

import org.junit.Test;

import se.inera.intyg.common.support.Constants;
import se.inera.intyg.common.support.model.common.internal.*;
import se.inera.intyg.common.support.modules.support.api.dto.Personnummer;
import se.inera.intyg.intygstyper.ts_parent.model.converter.InternalToTransportUtil;

public class InternalToTransportUtilTest {

    @Test
    public void testConvertWithMillisInTimestamp() {
        GrundData grundData = new GrundData();
        grundData.setPatient(new Patient());
        grundData.getPatient().setPersonId(new Personnummer("19121212-1212"));
        grundData.setSigneringsdatum(LocalDateTime.of(2012, 8, 12, 12, 10, 42, 543));
        grundData.setSkapadAv(new HoSPersonal());
        grundData.getSkapadAv().setVardenhet(new Vardenhet());
        grundData.getSkapadAv().getVardenhet().setVardgivare(new Vardgivare());
        se.inera.intygstjanster.ts.services.v1.GrundData res = InternalToTransportUtil.buildGrundData(grundData);
        assertEquals("2012-08-12T12:10:42", res.getSigneringsTidstampel()); // millis is not valid in transport
    }

    @Test
    public void testPersonnummerRoot() {
        final String personnummer = "19121212-1212";
        GrundData grundData = new GrundData();
        Patient patient = new Patient();
        patient.setPersonId(new Personnummer(personnummer));
        grundData.setPatient(patient);
        grundData.setSigneringsdatum(LocalDateTime.now());
        HoSPersonal skapadAv = new HoSPersonal();
        Vardenhet vardenhet = new Vardenhet();
        vardenhet.setVardgivare(new Vardgivare());
        skapadAv.setVardenhet(vardenhet);
        grundData.setSkapadAv(skapadAv);
        se.inera.intygstjanster.ts.services.v1.GrundData res = InternalToTransportUtil.buildGrundData(grundData);
        assertEquals(Constants.PERSON_ID_OID, res.getPatient().getPersonId().getRoot());
        assertEquals(personnummer, res.getPatient().getPersonId().getExtension());
    }

    @Test
    public void testSamordningRoot() {
        final String personnummer = "19800191-0002";
        GrundData grundData = new GrundData();
        Patient patient = new Patient();
        patient.setPersonId(new Personnummer(personnummer));
        grundData.setPatient(patient);
        grundData.setSigneringsdatum(LocalDateTime.now());
        HoSPersonal skapadAv = new HoSPersonal();
        Vardenhet vardenhet = new Vardenhet();
        vardenhet.setVardgivare(new Vardgivare());
        skapadAv.setVardenhet(vardenhet);
        grundData.setSkapadAv(skapadAv);
        se.inera.intygstjanster.ts.services.v1.GrundData res = InternalToTransportUtil.buildGrundData(grundData);
        assertEquals(Constants.SAMORDNING_ID_OID, res.getPatient().getPersonId().getRoot());
        assertEquals(personnummer, res.getPatient().getPersonId().getExtension());
    }
}
