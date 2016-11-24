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

package se.inera.intyg.intygstyper.luae_fs.model.converter;

import static se.inera.intyg.common.support.modules.converter.TransportConverterUtil.getCVSvarContent;
import static se.inera.intyg.common.support.modules.converter.TransportConverterUtil.getGrundData;
import static se.inera.intyg.common.support.modules.converter.TransportConverterUtil.getStringContent;
import static se.inera.intyg.intygstyper.fkparent.model.converter.RespConstants.*;
import static se.inera.intyg.intygstyper.fkparent.model.converter.TransportToInternalUtil.handleDiagnos;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import se.inera.intyg.common.support.model.InternalDate;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.intygstyper.fkparent.model.converter.RespConstants;
import se.inera.intyg.intygstyper.fkparent.model.internal.*;
import se.inera.intyg.intygstyper.luae_fs.model.internal.LuaefsUtlatande;
import se.inera.intyg.intygstyper.luae_fs.model.internal.LuaefsUtlatande.Builder;
import se.riv.clinicalprocess.healthcond.certificate.types.v2.CVType;
import se.riv.clinicalprocess.healthcond.certificate.v2.Intyg;
import se.riv.clinicalprocess.healthcond.certificate.v2.Svar;
import se.riv.clinicalprocess.healthcond.certificate.v2.Svar.Delsvar;

public final class TransportToInternal {

    private static final int TILLAGGSFRAGA_START = 9001;

    private TransportToInternal() {
    }

    public static LuaefsUtlatande convert(Intyg source) throws ConverterException {
        Builder utlatande = LuaefsUtlatande.builder();
        utlatande.setId(source.getIntygsId().getExtension());
        utlatande.setGrundData(getGrundData(source));
        utlatande.setTextVersion(source.getVersion());
        setSvar(utlatande, source);
        return utlatande.build();
    }

    private static void setSvar(Builder utlatande, Intyg source) throws ConverterException {
        List<Diagnos> diagnoser = new ArrayList<>();
        List<Tillaggsfraga> tillaggsfragor = new ArrayList<>();
        List<Underlag> underlag = new ArrayList<>();

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
            case DIAGNOS_SVAR_ID_6:
                handleDiagnos(diagnoser, svar);
                break;
            case FUNKTIONSNEDSATTNING_DEBUT_SVAR_ID_15:
                handleFunktionsnedsattningDebut(utlatande, svar);
                break;
            case FUNKTIONSNEDSATTNING_PAVERKAN_SVAR_ID_16:
                handleFunktionsnedsattningPaverkan(utlatande, svar);
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

        utlatande.setDiagnoser(diagnoser);
        utlatande.setTillaggsfragor(tillaggsfragor);
        utlatande.setUnderlag(underlag);
    }

    private static void handleFunktionsnedsattningDebut(Builder utlatande, Svar svar) {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case FUNKTIONSNEDSATTNING_DEBUT_DELSVAR_ID_15:
                    utlatande.setFunktionsnedsattningDebut(getStringContent(delsvar));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    private static void handleFunktionsnedsattningPaverkan(Builder utlatande, Svar svar) {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case FUNKTIONSNEDSATTNING_PAVERKAN_DELSVAR_ID_16:
                    utlatande.setFunktionsnedsattningPaverkan(getStringContent(delsvar));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }


    private static void handleUnderlagFinns(Builder utlatande, Svar svar) {
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


    private static void handleGrundForMedicinsktUnderlag(Builder utlatande, Svar svar) throws ConverterException {
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

    private static void handleKannedom(Builder utlatande, Svar svar) {
        Delsvar delsvar = svar.getDelsvar().get(0);
        switch (delsvar.getId()) {
            case KANNEDOM_DELSVAR_ID_2:
                utlatande.setKannedomOmPatient(new InternalDate(getStringContent(delsvar)));
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    private static void handleOvrigt(Builder utlatande, Svar svar) {
        Delsvar delsvar = svar.getDelsvar().get(0);
        switch (delsvar.getId()) {
        case OVRIGT_DELSVAR_ID_25:
            utlatande.setOvrigt(getStringContent(delsvar));
            break;
        default:
            throw new IllegalArgumentException();
        }
    }

    private static void handleOnskarKontakt(Builder utlatande, Svar svar) {
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
