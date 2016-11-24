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
package se.inera.intyg.intygstyper.luae_na.model.converter;

import static se.inera.intyg.common.support.modules.converter.TransportConverterUtil.getCVSvarContent;
import static se.inera.intyg.common.support.modules.converter.TransportConverterUtil.getStringContent;
import static se.inera.intyg.intygstyper.fkparent.model.converter.RespConstants.*;
import static se.inera.intyg.intygstyper.fkparent.model.converter.TransportToInternalUtil.handleDiagnos;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import se.inera.intyg.common.support.model.InternalDate;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.support.modules.converter.TransportConverterUtil;
import se.inera.intyg.intygstyper.fkparent.model.converter.RespConstants;
import se.inera.intyg.intygstyper.fkparent.model.internal.*;
import se.inera.intyg.intygstyper.luae_na.model.internal.LuaenaUtlatande;
import se.riv.clinicalprocess.healthcond.certificate.types.v2.CVType;
import se.riv.clinicalprocess.healthcond.certificate.v2.Intyg;
import se.riv.clinicalprocess.healthcond.certificate.v2.Svar;
import se.riv.clinicalprocess.healthcond.certificate.v2.Svar.Delsvar;

public final class TransportToInternal {

    private static final int TILLAGGSFRAGA_START = 9001;

    private TransportToInternal() {
    }

    public static LuaenaUtlatande convert(Intyg source) throws ConverterException {
        LuaenaUtlatande.Builder utlatande = LuaenaUtlatande.builder();
        utlatande.setId(source.getIntygsId().getExtension());
        utlatande.setGrundData(TransportConverterUtil.getGrundData(source));
        utlatande.setTextVersion(source.getVersion());
        setSvar(utlatande, source);
        return utlatande.build();
    }

    private static void setSvar(LuaenaUtlatande.Builder utlatande, Intyg source) throws ConverterException {
        List<Underlag> underlag = new ArrayList<>();
        List<Diagnos> diagnoser = new ArrayList<>();
        List<Tillaggsfraga> tillaggsfragor = new ArrayList<>();

        for (Svar svar : source.getSvar()) {
            switch (svar.getId()) {
            case GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1:
                handleGrundForMedicinsktUnderlag(utlatande, svar);
                break;
            case KANNEDOM_SVAR_ID_2:
                handleKannedom(utlatande, svar);
                break;
            case UNDERLAGFINNS_SVAR_ID_3:
                handleUnderlagFinns(utlatande, svar);
                break;
            case UNDERLAG_SVAR_ID_4:
                handleUnderlag(underlag, svar);
                break;
            case SJUKDOMSFORLOPP_SVAR_ID_5:
                handleSjukdomsForlopp(utlatande, svar);
                break;
            case DIAGNOS_SVAR_ID_6:
                handleDiagnos(diagnoser, svar);
                break;
            case DIAGNOSGRUND_SVAR_ID_7:
                handleDiagnosgrund(utlatande, svar);
                break;
            case NYDIAGNOS_SVAR_ID_45:
                    handleNyDiagnos(utlatande, svar);
                    break;
            case FUNKTIONSNEDSATTNING_INTELLEKTUELL_SVAR_ID_8:
                handleFunktionsNedsattningIntellektuell(utlatande, svar);
                break;
            case FUNKTIONSNEDSATTNING_KOMMUNIKATION_SVAR_ID_9:
                handleFunktionsNedsattningKommunikation(utlatande, svar);
                break;
            case FUNKTIONSNEDSATTNING_KONCENTRATION_SVAR_ID_10:
                handleFunktionsNedsattningKoncentration(utlatande, svar);
                break;
            case FUNKTIONSNEDSATTNING_PSYKISK_SVAR_ID_11:
                handleFunktionsNedsattningPsykisk(utlatande, svar);
                break;
            case FUNKTIONSNEDSATTNING_SYNHORSELTAL_SVAR_ID_12:
                handleFunktionsNedsattningSynHorselTal(utlatande, svar);
                break;
            case FUNKTIONSNEDSATTNING_BALANSKOORDINATION_SVAR_ID_13:
                handleFunktionsNedsattningBalansKoordination(utlatande, svar);
                break;
            case FUNKTIONSNEDSATTNING_ANNAN_SVAR_ID_14:
                handleFunktionsNedsattningAnnan(utlatande, svar);
                break;
            case AKTIVITETSBEGRANSNING_SVAR_ID_17:
                handleAktivitetsbegransning(utlatande, svar);
                break;
            case AVSLUTADBEHANDLING_SVAR_ID_18:
                handleAvslutadBehandling(utlatande, svar);
                break;
            case PAGAENDEBEHANDLING_SVAR_ID_19:
                handlePagaendeBehandling(utlatande, svar);
                break;
            case PLANERADBEHANDLING_SVAR_ID_20:
                handlePlaneradBehandling(utlatande, svar);
                break;
            case SUBSTANSINTAG_SVAR_ID_21:
                handleSubstansintag(utlatande, svar);
                break;
            case MEDICINSKAFORUTSATTNINGARFORARBETE_SVAR_ID_22:
                handleMedicinskaForutsattningarForArbete(utlatande, svar);
                break;
            case FORMAGATROTSBEGRANSNING_SVAR_ID_23:
                handleFormagaTrotsBegransning(utlatande, svar);
                break;
            case FORSLAG_TILL_ATGARD_SVAR_ID_24:
                handleForslagTillAtgard(utlatande, svar);
                break;
            case OVRIGT_SVAR_ID_25:
                handleOvrigt(utlatande, svar);
                break;
            case KONTAKT_ONSKAS_SVAR_ID_26:
                handleOnskarKontakt(utlatande, svar);
                break;
            default:
                if (StringUtils.isNumeric(svar.getId()) && Integer.parseInt(svar.getId()) >= TILLAGGSFRAGA_START) {
                    handleTillaggsfraga(tillaggsfragor, svar);
                }
                break;
            }
        }

        utlatande.setUnderlag(underlag);
        utlatande.setDiagnoser(diagnoser);
        utlatande.setTillaggsfragor(tillaggsfragor);
    }

    private static void handleGrundForMedicinsktUnderlag(LuaenaUtlatande.Builder utlatande, Svar svar) throws ConverterException {
        InternalDate grundForMedicinsktUnderlagDatum = null;
        RespConstants.ReferensTyp grundForMedicinsktUnderlagTyp = RespConstants.ReferensTyp.ANNAT;
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
            case GRUNDFORMEDICINSKTUNDERLAG_DATUM_DELSVAR_ID_1:
                grundForMedicinsktUnderlagDatum = new InternalDate(getStringContent(delsvar));
                break;
            case GRUNDFORMEDICINSKTUNDERLAG_TYP_DELSVAR_ID_1:
                String referensTypString = getCVSvarContent(delsvar).getCode();
                grundForMedicinsktUnderlagTyp = RespConstants.ReferensTyp.byTransportId(referensTypString);
                break;
            case GRUNDFORMEDICINSKTUNDERLAG_ANNANBESKRIVNING_DELSVAR_ID_1:
                utlatande.setAnnatGrundForMUBeskrivning(getStringContent(delsvar));
                break;
            default:
                throw new IllegalArgumentException();
            }
        }

        switch (grundForMedicinsktUnderlagTyp) {
        case UNDERSOKNING:
            utlatande.setUndersokningAvPatienten(grundForMedicinsktUnderlagDatum);
            break;
        case JOURNAL:
            utlatande.setJournaluppgifter(grundForMedicinsktUnderlagDatum);
            break;
        case ANHORIGSBESKRIVNING:
            utlatande.setAnhorigsBeskrivningAvPatienten(grundForMedicinsktUnderlagDatum);
            break;
        case ANNAT:
            utlatande.setAnnatGrundForMU(grundForMedicinsktUnderlagDatum);
            break;
        default:
            throw new IllegalArgumentException();
        }
    }

    private static void handleKannedom(LuaenaUtlatande.Builder utlatande, Svar svar) {
        Delsvar delsvar = svar.getDelsvar().get(0);
        switch (delsvar.getId()) {
        case KANNEDOM_DELSVAR_ID_2:
            utlatande.setKannedomOmPatient(new InternalDate(getStringContent(delsvar)));
            break;
        default:
            throw new IllegalArgumentException();
        }
    }

    private static void handleUnderlagFinns(LuaenaUtlatande.Builder utlatande, Svar svar) {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
            case UNDERLAGFINNS_DELSVAR_ID_3:
                utlatande.setUnderlagFinns(Boolean.valueOf(getStringContent(delsvar)));
                break;
            default:
                throw new IllegalArgumentException();
            }
        }
    }

    private static void handleUnderlag(List<Underlag> underlag, Svar svar) throws ConverterException {
        Underlag.UnderlagsTyp underlagsTyp = Underlag.UnderlagsTyp.OVRIGT;
        InternalDate date = null;
        String hamtasFran = null;
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
            case UNDERLAG_TYP_DELSVAR_ID_4:
                CVType typ = getCVSvarContent(delsvar);
                underlagsTyp = Underlag.UnderlagsTyp.fromId(typ.getCode());
                break;
            case UNDERLAG_DATUM_DELSVAR_ID_4:
                date = new InternalDate(getStringContent(delsvar));
                break;
            case UNDERLAG_HAMTAS_FRAN_DELSVAR_ID_4:
                hamtasFran = getStringContent(delsvar);
                break;
            default:
                throw new IllegalArgumentException();
            }
        }
        underlag.add(Underlag.create(underlagsTyp, date, hamtasFran));
    }

    private static void handleSjukdomsForlopp(LuaenaUtlatande.Builder utlatande, Svar svar) {
        Delsvar delsvar = svar.getDelsvar().get(0);
        switch (delsvar.getId()) {
        case SJUKDOMSFORLOPP_DELSVAR_ID_5:
            utlatande.setSjukdomsforlopp(getStringContent(delsvar));
            break;
        default:
            throw new IllegalArgumentException();
        }
    }

    private static void handleDiagnosgrund(LuaenaUtlatande.Builder utlatande, Svar svar) {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case DIAGNOSGRUND_DELSVAR_ID_7:
                    utlatande.setDiagnosgrund(getStringContent(delsvar));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    private static void handleNyDiagnos(LuaenaUtlatande.Builder utlatande, Svar svar) {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case DIAGNOSGRUND_NYBEDOMNING_DELSVAR_ID_45:
                    utlatande.setNyBedomningDiagnosgrund(Boolean.valueOf(getStringContent(delsvar)));
                    break;
                case DIAGNOS_FOR_NY_BEDOMNING_DELSVAR_ID_45:
                    utlatande.setDiagnosForNyBedomning(getStringContent(delsvar));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    private static void handleFunktionsNedsattningIntellektuell(LuaenaUtlatande.Builder utlatande, Svar svar) {
        Delsvar delsvar = svar.getDelsvar().get(0);
        switch (delsvar.getId()) {
        case FUNKTIONSNEDSATTNING_INTELLEKTUELL_DELSVAR_ID_8:
            utlatande.setFunktionsnedsattningIntellektuell(getStringContent(delsvar));
            break;
        default:
            throw new IllegalArgumentException();
        }
    }

    private static void handleFunktionsNedsattningKommunikation(LuaenaUtlatande.Builder utlatande, Svar svar) {
        Delsvar delsvar = svar.getDelsvar().get(0);
        switch (delsvar.getId()) {
        case FUNKTIONSNEDSATTNING_KOMMUNIKATION_DELSVAR_ID_9:
            utlatande.setFunktionsnedsattningKommunikation(getStringContent(delsvar));
            break;
        default:
            throw new IllegalArgumentException();
        }
    }

    private static void handleFunktionsNedsattningKoncentration(LuaenaUtlatande.Builder utlatande, Svar svar) {
        Delsvar delsvar = svar.getDelsvar().get(0);
        switch (delsvar.getId()) {
        case FUNKTIONSNEDSATTNING_KONCENTRATION_DELSVAR_ID_10:
            utlatande.setFunktionsnedsattningKoncentration(getStringContent(delsvar));
            break;
        default:
            throw new IllegalArgumentException();
        }
    }

    private static void handleFunktionsNedsattningPsykisk(LuaenaUtlatande.Builder utlatande, Svar svar) {
        Delsvar delsvar = svar.getDelsvar().get(0);
        switch (delsvar.getId()) {
        case FUNKTIONSNEDSATTNING_PSYKISK_DELSVAR_ID_11:
            utlatande.setFunktionsnedsattningPsykisk(getStringContent(delsvar));
            break;
        default:
            throw new IllegalArgumentException();
        }
    }

    private static void handleFunktionsNedsattningSynHorselTal(LuaenaUtlatande.Builder utlatande, Svar svar) {
        Delsvar delsvar = svar.getDelsvar().get(0);
        switch (delsvar.getId()) {
        case FUNKTIONSNEDSATTNING_SYNHORSELTAL_DELSVAR_ID_12:
            utlatande.setFunktionsnedsattningSynHorselTal(getStringContent(delsvar));
            break;
        default:
            throw new IllegalArgumentException();
        }
    }

    private static void handleFunktionsNedsattningBalansKoordination(LuaenaUtlatande.Builder utlatande, Svar svar) {
        Delsvar delsvar = svar.getDelsvar().get(0);
        switch (delsvar.getId()) {
        case FUNKTIONSNEDSATTNING_BALANSKOORDINATION_DELSVAR_ID_13:
            utlatande.setFunktionsnedsattningBalansKoordination(getStringContent(delsvar));
            break;
        default:
            throw new IllegalArgumentException();
        }
    }

    private static void handleFunktionsNedsattningAnnan(LuaenaUtlatande.Builder utlatande, Svar svar) {
        Delsvar delsvar = svar.getDelsvar().get(0);
        switch (delsvar.getId()) {
        case FUNKTIONSNEDSATTNING_ANNAN_DELSVAR_ID_14:
            utlatande.setFunktionsnedsattningAnnan(getStringContent(delsvar));
            break;
        default:
            throw new IllegalArgumentException();
        }
    }

    private static void handleAktivitetsbegransning(LuaenaUtlatande.Builder utlatande, Svar svar) {
        Delsvar delsvar = svar.getDelsvar().get(0);
        switch (delsvar.getId()) {
        case AKTIVITETSBEGRANSNING_DELSVAR_ID_17:
            utlatande.setAktivitetsbegransning(getStringContent(delsvar));
            break;
        default:
            throw new IllegalArgumentException();
        }
    }

    private static void handlePagaendeBehandling(LuaenaUtlatande.Builder utlatande, Svar svar) {
        Delsvar delsvar = svar.getDelsvar().get(0);
        switch (delsvar.getId()) {
        case PAGAENDEBEHANDLING_DELSVAR_ID_19:
            utlatande.setPagaendeBehandling(getStringContent(delsvar));
            break;
        default:
            throw new IllegalArgumentException();
        }
    }

    private static void handleAvslutadBehandling(LuaenaUtlatande.Builder utlatande, Svar svar) {
        Delsvar delsvar = svar.getDelsvar().get(0);
        switch (delsvar.getId()) {
        case AVSLUTADBEHANDLING_DELSVAR_ID_18:
            utlatande.setAvslutadBehandling(getStringContent(delsvar));
            break;
        default:
            throw new IllegalArgumentException();
        }
    }

    private static void handlePlaneradBehandling(LuaenaUtlatande.Builder utlatande, Svar svar) {
        Delsvar delsvar = svar.getDelsvar().get(0);
        switch (delsvar.getId()) {
        case PLANERADBEHANDLING_DELSVAR_ID_20:
            utlatande.setPlaneradBehandling(getStringContent(delsvar));
            break;
        default:
            throw new IllegalArgumentException();
        }
    }

    private static void handleSubstansintag(LuaenaUtlatande.Builder utlatande, Svar svar) {
        Delsvar delsvar = svar.getDelsvar().get(0);
        switch (delsvar.getId()) {
        case SUBSTANSINTAG_DELSVAR_ID_21:
            utlatande.setSubstansintag(getStringContent(delsvar));
            break;
        default:
            throw new IllegalArgumentException();
        }
    }

    private static void handleMedicinskaForutsattningarForArbete(LuaenaUtlatande.Builder utlatande, Svar svar) {
        Delsvar delsvar = svar.getDelsvar().get(0);
        switch (delsvar.getId()) {
        case MEDICINSKAFORUTSATTNINGARFORARBETE_DELSVAR_ID_22:
            utlatande.setMedicinskaForutsattningarForArbete(getStringContent(delsvar));
            break;
        default:
            throw new IllegalArgumentException();
        }
    }

    private static void handleFormagaTrotsBegransning(LuaenaUtlatande.Builder utlatande, Svar svar) {
        Delsvar delsvar = svar.getDelsvar().get(0);
        switch (delsvar.getId()) {
        case FORMAGATROTSBEGRANSNING_DELSVAR_ID_23:
            utlatande.setFormagaTrotsBegransning(getStringContent(delsvar));
            break;
        default:
            throw new IllegalArgumentException();
        }
    }

  private static void handleForslagTillAtgard(LuaenaUtlatande.Builder utlatande, Svar svar) {
  Delsvar delsvar = svar.getDelsvar().get(0);
  switch (delsvar.getId()) {
  case FORSLAG_TILL_ATGARD_DELSVAR_ID_24:
      utlatande.setForslagTillAtgard(getStringContent(delsvar));
      break;
  default:
      throw new IllegalArgumentException();
  }
}

    private static void handleOvrigt(LuaenaUtlatande.Builder utlatande, Svar svar) {
        Delsvar delsvar = svar.getDelsvar().get(0);
        switch (delsvar.getId()) {
        case OVRIGT_DELSVAR_ID_25:
            utlatande.setOvrigt(getStringContent(delsvar));
            return;
        default:
            throw new IllegalArgumentException();
        }
    }

    private static void handleOnskarKontakt(LuaenaUtlatande.Builder utlatande, Svar svar) {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
            case KONTAKT_ONSKAS_DELSVAR_ID_26:
                utlatande.setKontaktMedFk(Boolean.valueOf(getStringContent(delsvar)));
                break;
            case ANLEDNING_TILL_KONTAKT_DELSVAR_ID_26:
                utlatande.setAnledningTillKontakt(getStringContent(delsvar));
                break;
            default:
                throw new IllegalArgumentException();
            }
        }
    }

    private static void handleTillaggsfraga(List<Tillaggsfraga> tillaggsFragor, Svar svar) {
        // En tilläggsfråga har endast ett delsvar
        if (svar.getDelsvar().size() > 1) {
            throw new IllegalArgumentException();
        }

        Delsvar delsvar = svar.getDelsvar().get(0);
        // Kontrollera att ID matchar
        if (delsvar.getId().equals(svar.getId() + ".1")) {
            tillaggsFragor.add(Tillaggsfraga.create(svar.getId(), getStringContent(delsvar)));
        } else {
            throw new IllegalArgumentException();
        }
    }
}





