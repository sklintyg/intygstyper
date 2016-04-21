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

package se.inera.certificate.modules.luae_fs.model.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import org.joda.time.LocalDateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import se.inera.certificate.modules.luae_fs.model.converter.util.ConverterUtil;
import se.inera.certificate.modules.luae_fs.model.internal.LuaefsUtlatande;
import se.inera.certificate.modules.luae_fs.model.internal.LuaefsUtlatande.Builder;
import se.inera.certificate.modules.luae_fs.support.LuaefsEntryPoint;
import se.inera.intyg.common.support.common.enumerations.HandelsekodEnum;
import se.inera.intyg.common.support.model.common.internal.*;
import se.inera.intyg.common.support.modules.support.api.dto.Personnummer;
import se.inera.intyg.common.support.modules.support.api.notification.*;
import se.riv.clinicalprocess.healthcond.certificate.certificatestatusupdateforcareresponder.v2.CertificateStatusUpdateForCareType;

@RunWith(MockitoJUnitRunner.class)
public class InternalToNotificationTest {

    private static final String LOGISK_ADRESS = "123456789";

    private static final String INTYG_TYP = LuaefsEntryPoint.MODULE_ID;

    @Mock
    private ConverterUtil converterUtil;

    @InjectMocks
    private InternalToNotification converter;

    @Test
    public void testCreateCertificateStatusUpdateForCareType() throws Exception {
        when(converterUtil.fromJsonString(anyString())).thenReturn(
                buildLuaefsUtlatande("intygsId", "enhetsId", "enhetsnamn", "patientPersonId",
                        "skapadAvFullstandigtNamn", "skapadAvPersonId", LocalDateTime.now()));

        NotificationMessage msg = new NotificationMessage("intygsId", INTYG_TYP, LocalDateTime.now(), HandelseType.INTYGSUTKAST_ANDRAT,
                LOGISK_ADRESS, "", FragorOchSvar.getEmpty(),
                NotificationVersion.VERSION_2);
        CertificateStatusUpdateForCareType res = converter.createCertificateStatusUpdateForCareType(msg);

        assertEquals("LUAE_FS", res.getIntyg().getTyp().getCode());
        assertNotNull(res.getIntyg().getTyp().getCodeSystem());
        assertNotNull(res.getIntyg().getTyp().getDisplayName());
        assertEquals(HandelsekodEnum.ANDRAT.value(), res.getHandelse().getHandelsekod().getCode());
    }

    private LuaefsUtlatande buildLuaefsUtlatande(String intygsId, String enhetsId, String enhetsnamn,
                                                 String patientPersonId, String skapadAvFullstandigtNamn, String skapadAvPersonId, LocalDateTime signeringsdatum) {
        Builder template = LuaefsUtlatande.builder();
        template.setId(intygsId);
        GrundData grundData = new GrundData();
        HoSPersonal skapadAv = new HoSPersonal();
        Vardenhet vardenhet = new Vardenhet();
        vardenhet.setEnhetsid(enhetsId);
        vardenhet.setEnhetsnamn(enhetsnamn);
        Vardgivare vardgivare = new Vardgivare();
        vardgivare.setVardgivarid("vardgivarid");
        vardgivare.setVardgivarnamn("vardgivarnamn");
        vardenhet.setVardgivare(vardgivare);
        skapadAv.setVardenhet(vardenhet);
        skapadAv.setFullstandigtNamn(skapadAvFullstandigtNamn);
        skapadAv.setPersonId(skapadAvPersonId);
        grundData.setSkapadAv(skapadAv);
        Patient patient = new Patient();
        Personnummer personId = new Personnummer(patientPersonId);
        patient.setPersonId(personId);
        grundData.setPatient(patient);
        grundData.setSigneringsdatum(signeringsdatum);
        template.setGrundData(grundData);
        template.setTextVersion("");
        return template.build();
    }

}
