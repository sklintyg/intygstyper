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

public final class RespConstants {

    public static final String GRUNDFORMEDICINSKTUNDERLAG_CODE_SYSTEM = "KV_FKMU_0001";
    public static final String TYP_AV_SYSSELSATTNING_CODE_SYSTEM = "KV_FKMU_0002";
    public static final String SJUKSKRIVNING_CODE_SYSTEM = "KV_FKMU_0003";
    public static final String ARBETSLIVSINRIKTADE_ATGARDER_CODE_SYSTEM = "KV_FKMU_0004";
    public static final String UNDERLAG_CODE_SYSTEM = "KV_FKMU_0005";
    public static final String PROGNOS_CODE_SYSTEM = "KV_FKMU_0006";
    public static final String PROGNOS_DAGAR_TILL_ARBETE_CODE_SYSTEM = "KV_FKMU_0007";

    public static final String GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1 = "1";
    public static final String GRUNDFORMEDICINSKTUNDERLAG_SVAR_JSON_ID_1 = "baseratPa";
    public static final String GRUNDFORMEDICINSKTUNDERLAG_TYP_DELSVAR_ID_1 = "1.1";
    public static final String GRUNDFORMEDICINSKTUNDERLAG_DATUM_DELSVAR_ID_1 = "1.2";
    public static final String GRUNDFORMEDICINSKTUNDERLAG_ANNANBESKRIVNING_DELSVAR_ID_1 = "1.3";
    public static final String GRUNDFORMEDICINSKTUNDERLAG_UNDERSOKNING_AV_PATIENT_SVAR_JSON_ID_1 = "undersokningAvPatienten";
    public static final String GRUNDFORMEDICINSKTUNDERLAG_JOURNALUPPGIFTER_SVAR_JSON_ID_1 = "journaluppgifter";
    public static final String GRUNDFORMEDICINSKTUNDERLAG_ANHORIGS_BESKRIVNING_SVAR_JSON_ID_1 = "anhorigsBeskrivningAvPatienten";
    public static final String GRUNDFORMEDICINSKTUNDERLAG_ANNAT_SVAR_JSON_ID_1 = "annatGrundForMU";
    public static final String GRUNDFORMEDICINSKTUNDERLAG_TELEFONKONTAKT_PATIENT_SVAR_JSON_ID_1 = "telefonkontaktMedPatienten";
    public static final String GRUNDFORMEDICINSKTUNDERLAG_BESKRIVNING_DELSVAR_JSON_ID_1 = "annatGrundForMUBeskrivning";
    public static final String MOTIVERING_TILL_INTE_BASERAT_PA_UNDERLAG_ID_1 = "motiveringTillInteBaseratPaUndersokning";

    public static final String KANNEDOM_SVAR_ID_2 = "2";
    public static final String KANNEDOM_DELSVAR_ID_2 = "2.1";
    public static final String KANNEDOM_SVAR_JSON_ID_2 = "kannedomOmPatient";

    public static final String UNDERLAGFINNS_SVAR_ID_3 = "3";
    public static final String UNDERLAGFINNS_DELSVAR_ID_3 = "3.1";
    public static final String UNDERLAGFINNS_SVAR_JSON_ID_3 = "underlagFinns";

    public static final String UNDERLAG_SVAR_ID_4 = "4";
    public static final String UNDERLAG_TYP_DELSVAR_ID_4 = "4.1";
    public static final String UNDERLAG_DATUM_DELSVAR_ID_4 = "4.2";
    public static final String UNDERLAG_HAMTAS_FRAN_DELSVAR_ID_4 = "4.3";
    public static final String UNDERLAG_SVAR_JSON_ID_4 = "underlag";

    public static final String SJUKDOMSFORLOPP_SVAR_ID_5 = "5";
    public static final String SJUKDOMSFORLOPP_DELSVAR_ID_5 = "5.1";
    public static final String SJUKDOMSFORLOPP_SVAR_JSON_ID_5 = "sjukdomsforlopp";

    public static final String DIAGNOS_SVAR_ID_6 = "6";
    public static final String DIAGNOS_BESKRIVNING_DELSVAR_ID_6 = "6.1";
    public static final String DIAGNOS_DELSVAR_ID_6 = "6.2";
    public static final String BIDIAGNOS_1_BESKRIVNING_DELSVAR_ID_6 = "6.3";
    public static final String BIDIAGNOS_1_DELSVAR_ID_6 = "6.4";
    public static final String BIDIAGNOS_2_BESKRIVNING_DELSVAR_ID_6 = "6.5";
    public static final String BIDIAGNOS_2_DELSVAR_ID_6 = "6.6";
    public static final String DIAGNOS_SVAR_JSON_ID_6 = "diagnoser";

    public static final String DIAGNOSGRUND_SVAR_ID_7 = "7";
    public static final String DIAGNOSGRUND_DELSVAR_ID_7 = "7.1";
    public static final String NYDIAGNOS_SVAR_ID_45 = "45";
    public static final String DIAGNOSGRUND_NYBEDOMNING_DELSVAR_ID_45 = "45.1";
    public static final String DIAGNOS_FOR_NY_BEDOMNING_DELSVAR_ID_45 = "45.2";
    public static final String DIAGNOSGRUND_SVAR_JSON_ID_7 = "diagnosgrund";
    public static final String DIAGNOSGRUND_NY_BEDOMNING_SVAR_JSON_ID_45 = "nyBedomningDiagnosgrund";
    public static final String DIAGNOS_FOR_NY_BEDOMNING_SVAR_JSON_ID_45 = "diagnosForNyBedomning";

    public static final String FUNKTIONSNEDSATTNING_INTELLEKTUELL_SVAR_ID_8 = "8";
    public static final String FUNKTIONSNEDSATTNING_INTELLEKTUELL_DELSVAR_ID_8 = "8.1";
    public static final String FUNKTIONSNEDSATTNING_INTELLEKTUELL_SVAR_JSON_ID_8 = "funktionsnedsattningIntellektuell";

    public static final String FUNKTIONSNEDSATTNING_KOMMUNIKATION_SVAR_ID_9 = "9";
    public static final String FUNKTIONSNEDSATTNING_KOMMUNIKATION_DELSVAR_ID_9 = "9.1";
    public static final String FUNKTIONSNEDSATTNING_KOMMUNIKATION_SVAR_JSON_ID_9 = "funktionsnedsattningKommunikation";

    public static final String FUNKTIONSNEDSATTNING_KONCENTRATION_SVAR_ID_10 = "10";
    public static final String FUNKTIONSNEDSATTNING_KONCENTRATION_DELSVAR_ID_10 = "10.1";
    public static final String FUNKTIONSNEDSATTNING_KONCENTRATION_SVAR_JSON_ID_10 = "funktionsnedsattningKoncentration";

    public static final String FUNKTIONSNEDSATTNING_PSYKISK_SVAR_ID_11 = "11";
    public static final String FUNKTIONSNEDSATTNING_PSYKISK_DELSVAR_ID_11 = "11.1";
    public static final String FUNKTIONSNEDSATTNING_PSYKISK_SVAR_JSON_ID_11 = "funktionsnedsattningPsykisk";

    public static final String FUNKTIONSNEDSATTNING_SYNHORSELTAL_SVAR_ID_12 = "12";
    public static final String FUNKTIONSNEDSATTNING_SYNHORSELTAL_DELSVAR_ID_12 = "12.1";
    public static final String FUNKTIONSNEDSATTNING_SYNHORSELTAL_SVAR_JSON_ID_12 = "funktionsnedsattningSynHorselTal";

    public static final String FUNKTIONSNEDSATTNING_BALANSKOORDINATION_SVAR_ID_13 = "13";
    public static final String FUNKTIONSNEDSATTNING_BALANSKOORDINATION_DELSVAR_ID_13 = "13.1";
    public static final String FUNKTIONSNEDSATTNING_BALANSKOORDINATION_SVAR_JSON_ID_13 = "funktionsnedsattningBalansKoordination";

    public static final String FUNKTIONSNEDSATTNING_ANNAN_SVAR_ID_14 = "14";
    public static final String FUNKTIONSNEDSATTNING_ANNAN_DELSVAR_ID_14 = "14.1";
    public static final String FUNKTIONSNEDSATTNING_ANNAN_SVAR_JSON_ID_14 = "funktionsnedsattningAnnan";

    public static final String FUNKTIONSNEDSATTNING_DEBUT_SVAR_ID_15 = "15";
    public static final String FUNKTIONSNEDSATTNING_DEBUT_DELSVAR_ID_15 = "15.1";
    public static final String FUNKTIONSNEDSATTNING_DEBUT_SVAR_JSON_ID_15 = "funktionsnedsattningDebut";

    public static final String FUNKTIONSNEDSATTNING_PAVERKAN_SVAR_ID_16 = "16";
    public static final String FUNKTIONSNEDSATTNING_PAVERKAN_DELSVAR_ID_16 = "16.1";
    public static final String FUNKTIONSNEDSATTNING_PAVERKAN_SVAR_JSON_ID_16 = "funktionsnedsattningPaverkan";

    public static final String AKTIVITETSBEGRANSNING_SVAR_ID_17 = "17";
    public static final String AKTIVITETSBEGRANSNING_DELSVAR_ID_17 = "17.1";
    public static final String AKTIVITETSBEGRANSNING_SVAR_JSON_ID_17 = "aktivitetsbegransning";

    public static final String AVSLUTADBEHANDLING_SVAR_ID_18 = "18";
    public static final String AVSLUTADBEHANDLING_DELSVAR_ID_18 = "18.1";
    public static final String AVSLUTADBEHANDLING_SVAR_JSON_ID_18 = "avslutadBehandling";

    public static final String PAGAENDEBEHANDLING_SVAR_ID_19 = "19";
    public static final String PAGAENDEBEHANDLING_DELSVAR_ID_19 = "19.1";
    public static final String PAGAENDEBEHANDLING_SVAR_JSON_ID_19 = "pagaendeBehandling";

    public static final String PLANERADBEHANDLING_SVAR_ID_20 = "20";
    public static final String PLANERADBEHANDLING_DELSVAR_ID_20 = "20.1";
    public static final String PLANERADBEHANDLING_SVAR_JSON_ID_20 = "planeradBehandling";

    public static final String SUBSTANSINTAG_SVAR_ID_21 = "21";
    public static final String SUBSTANSINTAG_DELSVAR_ID_21 = "21.1";
    public static final String SUBSTANSINTAG_SVAR_JSON_ID_21 = "substansintag";

    public static final String MEDICINSKAFORUTSATTNINGARFORARBETE_SVAR_ID_22 = "22";
    public static final String MEDICINSKAFORUTSATTNINGARFORARBETE_DELSVAR_ID_22 = "22.1";
    public static final String MEDICINSKAFORUTSATTNINGARFORARBETE_SVAR_JSON_ID_22 = "medicinskaForutsattningarForArbete";

    public static final String FORMAGATROTSBEGRANSNING_SVAR_ID_23 = "23";
    public static final String FORMAGATROTSBEGRANSNING_DELSVAR_ID_23 = "23.1";
    public static final String FORMAGATROTSBEGRANSNING_SVAR_JSON_ID_23 = "formagaTrotsBegransning";

    public static final String FORSLAG_TILL_ATGARD_SVAR_ID_24 = "24";
    public static final String FORSLAG_TILL_ATGARD_DELSVAR_ID_24 = "24.1";
    public static final String FORSLAG_TILL_ATGARD_SVAR_JSON_ID_24 = "forslagTillAtgard";

    public static final String OVRIGT_SVAR_ID_25 = "25";
    public static final String OVRIGT_DELSVAR_ID_25 = "25.1";
    public static final String OVRIGT_SVAR_JSON_ID_25 = "ovrigt";

    public static final String KONTAKT_ONSKAS_SVAR_ID_26 = "26";
    public static final String KONTAKT_ONSKAS_DELSVAR_ID_26 = "26.1";
    public static final String KONTAKT_ONSKAS_SVAR_JSON_ID_26 = "kontaktMedFk";
    public static final String ANLEDNING_TILL_KONTAKT_DELSVAR_JSON_ID_26 = "anledningTillKontakt";
    public static final String ANLEDNING_TILL_KONTAKT_DELSVAR_ID_26 = "26.2";

    public static final String AVSTANGNING_SMITTSKYDD_SVAR_ID_27 = "27";
    public static final String AVSTANGNING_SMITTSKYDD_DELSVAR_ID_27 = "27.1";
    public static final String AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27 = "avstangningSmittskydd";

    public static final String TYP_AV_SYSSELSATTNING_SVAR_ID_28 = "28";
    public static final String TYP_AV_SYSSELSATTNING_DELSVAR_ID_28 = "28.1";
    public static final String TYP_AV_SYSSELSATTNING_SVAR_JSON_ID_28 = "sysselsattning";

    public static final String NUVARANDE_ARBETE_SVAR_ID_29 = "29";
    public static final String NUVARANDE_ARBETE_DELSVAR_ID_29 = "29.1";
    public static final String NUVARANDE_ARBETE_SVAR_JSON_ID_29 = "nuvarandeArbete";

    public static final String ARBETSMARKNADSPOLITISKT_PROGRAM_SVAR_ID_30 = "30";
    public static final String ARBETSMARKNADSPOLITISKT_PROGRAM_DELSVAR_ID_30 = "30.1";
    public static final String ARBETSMARKNADSPOLITISKT_PROGRAM_SVAR_JSON_ID_30 = "arbetsmarknadspolitisktProgram";

    public static final String BEHOV_AV_SJUKSKRIVNING_SVAR_ID_32 = "32";
    public static final String BEHOV_AV_SJUKSKRIVNING_NIVA_DELSVARSVAR_ID_32 = "32.1";
    public static final String BEHOV_AV_SJUKSKRIVNING_PERIOD_DELSVARSVAR_ID_32 = "32.2";
    public static final String BEHOV_AV_SJUKSKRIVNING_SVAR_JSON_ID_32 = "sjukskrivningar";
    public static final String MOTIVERING_TILL_TIDIGT_STARTDATUM_FOR_SJUKSKRIVNING_ID = "motiveringTillTidigtStartdatumForSjukskrivning";

    public static final String ARBETSTIDSFORLAGGNING_SVAR_ID_33 = "33";
    public static final String ARBETSTIDSFORLAGGNING_OM_DELSVAR_ID_33 = "33.1";
    public static final String ARBETSTIDSFORLAGGNING_MOTIVERING_SVAR_ID_33 = "33.2";
    public static final String ARBETSTIDSFORLAGGNING_SVAR_JSON_ID_33 = "arbetstidsforlaggning";
    public static final String ARBETSTIDSFORLAGGNING_MOTIVERING_SVAR_JSON_ID_33 = "arbetstidsforlaggningMotivering";

    public static final String ARBETSRESOR_SVAR_ID_34 = "34";
    public static final String ARBETSRESOR_OM_DELSVAR_ID_34 = "34.1";
    public static final String ARBETSRESOR_SVAR_JSON_ID_34 = "arbetsresor";

    public static final String FUNKTIONSNEDSATTNING_SVAR_ID_35 = "35";
    public static final String FUNKTIONSNEDSATTNING_DELSVAR_ID_35 = "35.1";
    public static final String FUNKTIONSNEDSATTNING_SVAR_JSON_ID_35 = "funktionsnedsattning";

    public static final String FORSAKRINGSMEDICINSKT_BESLUTSSTOD_SVAR_ID_37 = "37";
    public static final String FORSAKRINGSMEDICINSKT_BESLUTSSTOD_DELSVAR_ID_37 = "37.1";
    public static final String FORSAKRINGSMEDICINSKT_BESLUTSSTOD_SVAR_JSON_ID_37 = "forsakringsmedicinsktBeslutsstod";

    public static final String PROGNOS_SVAR_ID_39 = "39";
    public static final String PROGNOS_BESKRIVNING_DELSVAR_ID_39 = "39.1";
    public static final String PROGNOS_DAGAR_TILL_ARBETE_DELSVAR_ID_39 = "39.3";
    public static final String PROGNOS_SVAR_JSON_ID_39 = "prognos";

    public static final String ARBETSLIVSINRIKTADE_ATGARDER_SVAR_ID_40 = "40";
    public static final String ARBETSLIVSINRIKTADE_ATGARDER_VAL_DELSVAR_ID_40 = "40.1";
    public static final String ARBETSLIVSINRIKTADE_ATGARDER_SVAR_JSON_ID_40 = "arbetslivsinriktadeAtgarder";
    public static final String ARBETSLIVSINRIKTADE_ATGARDER_BESKRIVNING_SVAR_ID_44 = "44";
    public static final String ARBETSLIVSINRIKTADE_ATGARDER_BESKRIVNING_DELSVAR_ID_44 = "44.1";
    public static final String ARBETSLIVSINRIKTADE_ATGARDER_BESKRIVNING_SVAR_JSON_ID_44 = "arbetslivsinriktadeAtgarderBeskrivning";

    public static final String TILLAGGSFRAGOR_SVAR_JSON_ID = "tillaggsfragor";
    public static final int TILLAGGSFRAGOR_START = 9000;

    public static final String GRUNDDATA_SVAR_JSON_ID = "grundData";
    public static final String TEXTVERSION_JSON_ID = "textVersion";
    public static final String ID_JSON_ID = "id";

    private RespConstants() {
    }

    public static String getJsonPropertyFromFrageId(String frageId) {
        switch (frageId) {
        case GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1:
            return GRUNDFORMEDICINSKTUNDERLAG_SVAR_JSON_ID_1;
        case KANNEDOM_SVAR_ID_2:
            return KANNEDOM_SVAR_JSON_ID_2;
        case UNDERLAGFINNS_SVAR_ID_3:
            return UNDERLAGFINNS_SVAR_JSON_ID_3;
        case UNDERLAG_SVAR_ID_4:
            return UNDERLAG_SVAR_JSON_ID_4;
        case SJUKDOMSFORLOPP_SVAR_ID_5:
            return SJUKDOMSFORLOPP_SVAR_JSON_ID_5;
        case DIAGNOS_SVAR_ID_6:
            return DIAGNOS_SVAR_JSON_ID_6;
        case DIAGNOSGRUND_SVAR_ID_7:
            return DIAGNOSGRUND_SVAR_JSON_ID_7;
        case NYDIAGNOS_SVAR_ID_45:
            return DIAGNOSGRUND_NY_BEDOMNING_SVAR_JSON_ID_45;
        case FUNKTIONSNEDSATTNING_INTELLEKTUELL_SVAR_ID_8:
            return FUNKTIONSNEDSATTNING_INTELLEKTUELL_SVAR_JSON_ID_8;
        case FUNKTIONSNEDSATTNING_KOMMUNIKATION_SVAR_ID_9:
            return FUNKTIONSNEDSATTNING_KOMMUNIKATION_SVAR_JSON_ID_9;
        case FUNKTIONSNEDSATTNING_KONCENTRATION_SVAR_ID_10:
            return FUNKTIONSNEDSATTNING_KONCENTRATION_SVAR_JSON_ID_10;
        case FUNKTIONSNEDSATTNING_PSYKISK_SVAR_ID_11:
            return FUNKTIONSNEDSATTNING_PSYKISK_SVAR_JSON_ID_11;
        case FUNKTIONSNEDSATTNING_SYNHORSELTAL_SVAR_ID_12:
            return FUNKTIONSNEDSATTNING_SYNHORSELTAL_SVAR_JSON_ID_12;
        case FUNKTIONSNEDSATTNING_BALANSKOORDINATION_SVAR_ID_13:
            return FUNKTIONSNEDSATTNING_BALANSKOORDINATION_SVAR_JSON_ID_13;
        case FUNKTIONSNEDSATTNING_ANNAN_SVAR_ID_14:
            return FUNKTIONSNEDSATTNING_ANNAN_SVAR_JSON_ID_14;
        case FUNKTIONSNEDSATTNING_DEBUT_SVAR_ID_15:
            return FUNKTIONSNEDSATTNING_DEBUT_SVAR_JSON_ID_15;
        case FUNKTIONSNEDSATTNING_PAVERKAN_SVAR_ID_16:
            return FUNKTIONSNEDSATTNING_PAVERKAN_SVAR_JSON_ID_16;
        case AKTIVITETSBEGRANSNING_SVAR_ID_17:
            return AKTIVITETSBEGRANSNING_SVAR_JSON_ID_17;
        case PAGAENDEBEHANDLING_SVAR_ID_19:
            return PAGAENDEBEHANDLING_SVAR_JSON_ID_19;
        case AVSLUTADBEHANDLING_SVAR_ID_18:
            return AVSLUTADBEHANDLING_SVAR_JSON_ID_18;
        case PLANERADBEHANDLING_SVAR_ID_20:
            return PLANERADBEHANDLING_SVAR_JSON_ID_20;
        case SUBSTANSINTAG_SVAR_ID_21:
            return SUBSTANSINTAG_SVAR_JSON_ID_21;
        case MEDICINSKAFORUTSATTNINGARFORARBETE_SVAR_ID_22:
            return MEDICINSKAFORUTSATTNINGARFORARBETE_SVAR_JSON_ID_22;
        case FORMAGATROTSBEGRANSNING_SVAR_ID_23:
            return FORMAGATROTSBEGRANSNING_SVAR_JSON_ID_23;
        case FORSLAG_TILL_ATGARD_SVAR_ID_24:
            return FORSLAG_TILL_ATGARD_SVAR_JSON_ID_24;
        case OVRIGT_SVAR_ID_25:
            return OVRIGT_SVAR_JSON_ID_25;
        case KONTAKT_ONSKAS_SVAR_ID_26:
            return KONTAKT_ONSKAS_SVAR_JSON_ID_26;
        case ANLEDNING_TILL_KONTAKT_DELSVAR_ID_26:
            return ANLEDNING_TILL_KONTAKT_DELSVAR_JSON_ID_26;
        case AVSTANGNING_SMITTSKYDD_SVAR_ID_27:
            return AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27;
        case TYP_AV_SYSSELSATTNING_SVAR_ID_28:
            return TYP_AV_SYSSELSATTNING_SVAR_JSON_ID_28;
        case NUVARANDE_ARBETE_SVAR_ID_29:
            return NUVARANDE_ARBETE_SVAR_JSON_ID_29;
        case ARBETSMARKNADSPOLITISKT_PROGRAM_SVAR_ID_30:
            return ARBETSMARKNADSPOLITISKT_PROGRAM_SVAR_JSON_ID_30;
        case BEHOV_AV_SJUKSKRIVNING_SVAR_ID_32:
            return BEHOV_AV_SJUKSKRIVNING_SVAR_JSON_ID_32;
        case ARBETSTIDSFORLAGGNING_SVAR_ID_33:
            return ARBETSTIDSFORLAGGNING_SVAR_JSON_ID_33;
        case ARBETSRESOR_SVAR_ID_34:
            return ARBETSRESOR_SVAR_JSON_ID_34;
        case FUNKTIONSNEDSATTNING_SVAR_ID_35:
            return FUNKTIONSNEDSATTNING_SVAR_JSON_ID_35;
        case FORSAKRINGSMEDICINSKT_BESLUTSSTOD_SVAR_ID_37:
            return FORSAKRINGSMEDICINSKT_BESLUTSSTOD_SVAR_JSON_ID_37;
        case PROGNOS_SVAR_ID_39:
            return PROGNOS_SVAR_JSON_ID_39;
        case ARBETSLIVSINRIKTADE_ATGARDER_SVAR_ID_40:
            return ARBETSLIVSINRIKTADE_ATGARDER_SVAR_JSON_ID_40;
        case ARBETSLIVSINRIKTADE_ATGARDER_BESKRIVNING_SVAR_ID_44:
            return ARBETSLIVSINRIKTADE_ATGARDER_BESKRIVNING_SVAR_JSON_ID_44;
        default:
            throw new IllegalArgumentException("The supplied value of frageId " + frageId + " is not supported.");
        }
    }

    public enum ReferensTyp {
        UNDERSOKNING("UNDERSOKNING", "Min undersökning av patienten"),
        TELEFONKONTAKT("TELEFONKONTAKT", "Min telefonkontakt med patienten"),
        JOURNAL("JOURNALUPPGIFTER", "Journaluppgifter från den"),
        ANHORIGSBESKRIVNING("ANHORIG", "Anhörigs beskrivning av patienten"),
        ANNAT("ANNAT", "Annat");

        public final String transportId;
        public final String label;

        ReferensTyp(String transportId, String label) {
            this.transportId = transportId;
            this.label = label;
        }

        public static ReferensTyp byTransportId(String transportId) {
            for (ReferensTyp referensTyp : values()) {
                if (referensTyp.transportId.equals(transportId)) {
                    return referensTyp;
                }
            }
            throw new IllegalArgumentException();
        }
    }

}
