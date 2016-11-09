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

import org.junit.Test;

import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Utlatande;
import se.riv.clinicalprocess.healthcond.certificate.revokeCertificate.v1.RevokeCertificateType;

public class InternalToRevokeTest {

    @Test
    public void convertRevokeCertificateRequest() throws Exception {
        String meddelande = "meddelande";
        Utlatande utlatande = IntygTestDataBuilder.getUtlatande();
        HoSPersonal skapatAv = utlatande.getGrundData().getSkapadAv();
        RevokeCertificateType res = InternalToRevoke.convert(utlatande, skapatAv, meddelande);

        assertNotNull(res.getIntygsId().getRoot());
        assertEquals(utlatande.getId(), res.getIntygsId().getExtension());
        assertEquals(meddelande, res.getMeddelande());
        assertEquals("1.2.752.129.2.1.3.1", res.getPatientPersonId().getRoot());
        assertEquals(utlatande.getGrundData().getPatient().getPersonId().getPersonnummerWithoutDash(),res.getPatientPersonId().getExtension());
        assertEquals(skapatAv.getPersonId(), res.getSkickatAv().getPersonalId().getExtension());
        assertEquals(skapatAv.getFullstandigtNamn(), res.getSkickatAv().getFullstandigtNamn());
        assertNotNull(res.getSkickatAv().getEnhet());
        assertNotNull(res.getSkickatTidpunkt());
    }
}
