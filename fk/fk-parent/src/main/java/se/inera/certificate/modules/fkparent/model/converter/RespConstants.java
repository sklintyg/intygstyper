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

package se.inera.certificate.modules.fkparent.model.converter;

public final class RespConstants {
    private RespConstants() {
    }

    public static final int UNDERSOKNING_AV_PATIENT = 1;
    public static final int JOURNALUPPGIFTER = 3;
    public static final int ANHORIGSBESKRIVNING = 4;
    public static final int ANNAT = 5;

    public static final String CERTIFICATE_CODE_SYSTEM = "f6fb361a-e31d-48b8-8657-99b63912dd9b";
    public static final String GRUNDFORMEDICINSKTUNDERLAG_CODE_SYSTEM = "KV_FKMU_0001";
    public static final String HSA_CODE_SYSTEM = "1.2.752.129.2.1.4.1";
    public static final String PERSON_ID_CODE_SYSTEM = "1.2.752.129.2.1.3.3";
    public static final String UNDERLAG_CODE_SYSTEM = "KV_FKMU_0005";
    public static final String BEFATTNING_CODE_SYSTEM = "1.2.752.129.2.2.1.4";
    public static final String ARBETSPLATSKOD_CODE_SYSTEM = "1.2.752.29.4.71";

    public static final String GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID = "1";
    public static final String GRUNDFORMEDICINSKTUNDERLAG_TYP_DELSVAR_ID = "1.1";
    public static final String GRUNDFORMEDICINSKTUNDERLAG_DATUM_DELSVAR_ID = "1.2";
    public static final String GRUNDFORMEDICINSKTUNDERLAG_ANNANBESKRIVNING_DELSVAR_ID = "1.3";
    public static final String KANNEDOM_SVAR_ID = "2";
    public static final String KANNEDOM_DELSVAR_ID = "2.1";
    public static final String UNDERLAGFINNS_SVAR_ID = "3";
    public static final String UNDERLAGFINNS_DELSVAR_ID = "3.1";
    public static final String UNDERLAG_SVAR_ID = "4";
    public static final String UNDERLAG_TYP_DELSVAR_ID = "4.1";
    public static final String UNDERLAG_DATUM_DELSVAR_ID = "4.2";
    public static final String UNDERLAG_BILAGA_DELSVAR_ID = "4.3";
    public static final String SJUKDOMSFORLOPP_SVAR_ID = "5";
    public static final String SJUKDOMSFORLOPP_DELSVAR_ID = "5.1";
    public static final String DIAGNOS_SVAR_ID = "6";
    public static final String DIAGNOS_BESKRIVNING_DELSVAR_ID = "6.1";
    public static final String DIAGNOS_DELSVAR_ID = "6.2";
    public static final String DIAGNOSGRUND_SVAR_ID = "7";
    public static final String DIAGNOSGRUND_DELSVAR_ID = "7.1";
    public static final String DIAGNOSGRUND_NYBEDOMNING_DELSVAR_ID = "7.2";
    public static final String FUNKTIONSNEDSATTNING_INTELLEKTUELL_SVAR_ID = "8";
    public static final String FUNKTIONSNEDSATTNING_INTELLEKTUELL_DELSVAR_ID = "8.1";
    public static final String FUNKTIONSNEDSATTNING_KOMMUNIKATION_SVAR_ID = "9";
    public static final String FUNKTIONSNEDSATTNING_KOMMUNIKATION_DELSVAR_ID = "9.1";
    public static final String FUNKTIONSNEDSATTNING_KONCENTRATION_SVAR_ID = "10";
    public static final String FUNKTIONSNEDSATTNING_KONCENTRATION_DELSVAR_ID = "10.1";
    public static final String FUNKTIONSNEDSATTNING_PSYKISK_SVAR_ID = "11";
    public static final String FUNKTIONSNEDSATTNING_PSYKISK_DELSVAR_ID = "11.1";
    public static final String FUNKTIONSNEDSATTNING_SYNHORSELTAL_SVAR_ID = "12";
    public static final String FUNKTIONSNEDSATTNING_SYNHORSELTAL_DELSVAR_ID = "12.1";
    public static final String FUNKTIONSNEDSATTNING_BALANSKOORDINATION_SVAR_ID = "13";
    public static final String FUNKTIONSNEDSATTNING_BALANSKOORDINATION_DELSVAR_ID = "13.1";
    public static final String FUNKTIONSNEDSATTNING_ANNAN_SVAR_ID = "14";
    public static final String FUNKTIONSNEDSATTNING_ANNAN_DELSVAR_ID = "14.1";
    public static final String AKTIVITETSBEGRANSNING_SVAR_ID = "17";
    public static final String AKTIVITETSBEGRANSNING_DELSVAR_ID = "17.1";
    public static final String AVSLUTADBEHANDLING_SVAR_ID = "18";
    public static final String AVSLUTADBEHANDLING_DELSVAR_ID = "18.1";
    public static final String PAGAENDEBEHANDLING_SVAR_ID = "19";
    public static final String PAGAENDEBEHANDLING_DELSVAR_ID = "19.1";
    public static final String PLANERADBEHANDLING_SVAR_ID = "20";
    public static final String PLANERADBEHANDLING_DELSVAR_ID = "20.1";
    public static final String SUBSTANSINTAG_SVAR_ID = "21";
    public static final String SUBSTANSINTAG_DELSVAR_ID = "21.1";
    public static final String MEDICINSKAFORUTSATTNINGARFORARBETE_SVAR_ID = "22";
    public static final String MEDICINSKAFORUTSATTNINGARFORARBETE_DELSVAR_ID = "22.1";
    public static final String AKTIVITETSFORMAGA_SVAR_ID = "23";
    public static final String AKTIVITETSFORMAGA_DELSVAR_ID = "23.1";
    public static final String OVRIGT_SVAR_ID = "25";
    public static final String OVRIGT_DELSVAR_ID = "25.1";
    public static final String KONTAKT_ONSKAS_SVAR_ID = "26";
    public static final String KONTAKT_ONSKAS_DELSVAR_ID = "26.1";
    public static final String ANLEDNING_TILL_KONTAKT_DELSVAR_ID = "26.2";

    public enum ReferensTyp {
        UNDERSOKNING(1), TELEFONKONTAKT(2), JOURNAL(3), ANHORIGSBESKRIVNING(4), ANNAT(5), UNKNOWN(-1);

        public final int transport;

        ReferensTyp(int transport) {
            this.transport = transport;
        }

        public static ReferensTyp byTransport(String transport) {
            for (ReferensTyp referensTyp : values()) {
                if (referensTyp.transport == Integer.parseInt(transport)) {
                    return referensTyp;
                }
            }
            throw new IllegalArgumentException();
        }
    }

}
