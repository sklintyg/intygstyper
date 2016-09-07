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

import java.time.LocalDateTime;

import se.inera.intyg.common.support.model.common.internal.*;
import se.inera.intyg.common.support.modules.support.api.dto.Personnummer;

public final class IntygTestDataBuilder {

    private IntygTestDataBuilder() {
    }

    public static Utlatande getUtlatande() {
        return new Utlatande() {
                private GrundData grundData = IntygTestDataBuilder.getGrundData();

            @Override
            public String getTyp() {
                return "testTyp";
            }

            @Override
            public String getTextVersion() {
                return "1.0";
            }

            @Override
            public String getId() {
                return "test-id";
            }

            @Override
            public GrundData getGrundData() {
                return grundData;
            }
        };
    }

    public static GrundData getGrundData() {
        GrundData grundData = new GrundData();
        grundData.setSigneringsdatum(LocalDateTime.now());
        grundData.setSkapadAv(getHosPersonal());
        grundData.setPatient(getPatient());
        return grundData;
    }

    private static Patient getPatient() {
        Patient patient = new Patient();
        patient.setEfternamn("Olsson");
        patient.setFornamn("Olivia");
        patient.setFullstandigtNamn("Olivia Olsson");
        patient.setPersonId(new Personnummer("19270310-4321"));
        patient.setPostadress("Pgatan 2");
        patient.setPostnummer("100 20");
        patient.setPostort("Stadby gärde");
        return patient;
    }

    private static HoSPersonal getHosPersonal() {
        HoSPersonal personal = new HoSPersonal();
        personal.setVardenhet(getVardenhet());
        personal.setForskrivarKod("09874321");
        personal.setFullstandigtNamn("Karl Karlsson");
        personal.setPersonId("19650708-1234");
        personal.getBefattningar().add("Klinikchef");
        personal.getBefattningar().add("Forskningsledare");
        personal.getSpecialiteter().add("Kirurg");
        return personal;
    }

    private static Vardenhet getVardenhet() {
        Vardenhet vardenhet = new Vardenhet();
        Vardgivare vardgivare = new Vardgivare();
        vardgivare.setVardgivarnamn("VG1");
        vardgivare.setVardgivarid("12345678");
        vardenhet.setVardgivare(vardgivare);
        vardenhet.setTelefonnummer("0812341234");
        vardenhet.setArbetsplatsKod("45312");
        vardenhet.setEnhetsid("123456789");
        vardenhet.setEnhetsnamn("VE1");
        vardenhet.setEpost("ve1@vg1.se");
        vardenhet.setPostadress("Enhetsg. 1");
        vardenhet.setPostnummer("100 10");
        vardenhet.setPostort("Stadby");
        return vardenhet;
    }

}
