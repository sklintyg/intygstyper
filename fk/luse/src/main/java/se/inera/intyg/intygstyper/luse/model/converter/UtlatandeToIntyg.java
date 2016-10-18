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

package se.inera.intyg.intygstyper.luse.model.converter;

import static se.inera.intyg.common.support.Constants.KV_INTYGSTYP_CODE_SYSTEM;
import static se.inera.intyg.common.support.modules.converter.InternalConverterUtil.aCV;
import static se.inera.intyg.common.support.modules.converter.InternalConverterUtil.aSvar;
import static se.inera.intyg.common.support.modules.converter.InternalConverterUtil.addIfNotBlank;
import static se.inera.intyg.intygstyper.fkparent.model.converter.RespConstants.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import se.inera.intyg.common.support.common.enumerations.Diagnoskodverk;
import se.inera.intyg.common.support.common.util.StringUtil;
import se.inera.intyg.common.support.modules.converter.InternalConverterUtil;
import se.inera.intyg.common.support.modules.converter.InternalConverterUtil.SvarBuilder;
import se.inera.intyg.intygstyper.fkparent.model.converter.RespConstants.ReferensTyp;
import se.inera.intyg.intygstyper.fkparent.model.internal.*;
import se.inera.intyg.intygstyper.luse.model.internal.LuseUtlatande;
import se.inera.intyg.intygstyper.luse.support.LuseEntryPoint;
import se.riv.clinicalprocess.healthcond.certificate.types.v2.TypAvIntyg;
import se.riv.clinicalprocess.healthcond.certificate.v2.Intyg;
import se.riv.clinicalprocess.healthcond.certificate.v2.Svar;

public final class UtlatandeToIntyg {

    private UtlatandeToIntyg() {
    }

    public static Intyg convert(LuseUtlatande source) {
        Intyg intyg = InternalConverterUtil.getIntyg(source);
        intyg.setTyp(getTypAvIntyg(source));
        intyg.getSvar().addAll(getSvar(source));
        return intyg;
    }

    private static TypAvIntyg getTypAvIntyg(LuseUtlatande source) {
        TypAvIntyg typAvIntyg = new TypAvIntyg();
        typAvIntyg.setCode(source.getTyp().toUpperCase());
        typAvIntyg.setCodeSystem(KV_INTYGSTYP_CODE_SYSTEM);
        typAvIntyg.setDisplayName(LuseEntryPoint.MODULE_NAME);
        return typAvIntyg;
    }

    private static List<Svar> getSvar(LuseUtlatande source) {
        List<Svar> svars = new ArrayList<>();

        int grundForMUInstans = 1;
        if (source.getUndersokningAvPatienten() != null) {
            svars.add(aSvar(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1, grundForMUInstans++)
                    .withDelsvar(GRUNDFORMEDICINSKTUNDERLAG_TYP_DELSVAR_ID_1,
                            aCV(GRUNDFORMEDICINSKTUNDERLAG_CODE_SYSTEM, ReferensTyp.UNDERSOKNING.transportId, ReferensTyp.UNDERSOKNING.label))
                    .withDelsvar(GRUNDFORMEDICINSKTUNDERLAG_DATUM_DELSVAR_ID_1, source.getUndersokningAvPatienten().asLocalDate().toString())
                    .build());
        }
        if (source.getJournaluppgifter() != null) {
            svars.add(aSvar(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1, grundForMUInstans++)
                    .withDelsvar(GRUNDFORMEDICINSKTUNDERLAG_TYP_DELSVAR_ID_1,
                            aCV(GRUNDFORMEDICINSKTUNDERLAG_CODE_SYSTEM, ReferensTyp.JOURNAL.transportId, ReferensTyp.JOURNAL.label))
                    .withDelsvar(GRUNDFORMEDICINSKTUNDERLAG_DATUM_DELSVAR_ID_1, source.getJournaluppgifter().asLocalDate().toString()).build());
        }
        if (source.getAnhorigsBeskrivningAvPatienten() != null) {
            svars.add(aSvar(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1, grundForMUInstans++)
                    .withDelsvar(GRUNDFORMEDICINSKTUNDERLAG_TYP_DELSVAR_ID_1,
                            aCV(GRUNDFORMEDICINSKTUNDERLAG_CODE_SYSTEM, ReferensTyp.ANHORIGSBESKRIVNING.transportId,
                                    ReferensTyp.ANHORIGSBESKRIVNING.label))
                    .withDelsvar(GRUNDFORMEDICINSKTUNDERLAG_DATUM_DELSVAR_ID_1, source.getAnhorigsBeskrivningAvPatienten().asLocalDate().toString())
                    .build());
        }
        if (source.getAnnatGrundForMU() != null) {
            svars.add(aSvar(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1, grundForMUInstans++)
                    .withDelsvar(GRUNDFORMEDICINSKTUNDERLAG_TYP_DELSVAR_ID_1,
                            aCV(GRUNDFORMEDICINSKTUNDERLAG_CODE_SYSTEM, ReferensTyp.ANNAT.transportId, ReferensTyp.ANNAT.label))
                    .withDelsvar(GRUNDFORMEDICINSKTUNDERLAG_DATUM_DELSVAR_ID_1, source.getAnnatGrundForMU().asLocalDate().toString())
                    .withDelsvar(GRUNDFORMEDICINSKTUNDERLAG_ANNANBESKRIVNING_DELSVAR_ID_1, source.getAnnatGrundForMUBeskrivning()).build());
        }

        if (source.getKannedomOmPatient() != null) {
            svars.add(aSvar(KANNEDOM_SVAR_ID_2).withDelsvar(KANNEDOM_DELSVAR_ID_2, source.getKannedomOmPatient().asLocalDate().toString()).build());
        }

        if (source.getUnderlagFinns() != null) {
            svars.add(aSvar(UNDERLAGFINNS_SVAR_ID_3).withDelsvar(UNDERLAGFINNS_DELSVAR_ID_3, source.getUnderlagFinns().toString()).build());
        }

        int underlagInstans = 1;
        for (Underlag underlag : source.getUnderlag()) {
            svars.add(
                    aSvar(UNDERLAG_SVAR_ID_4, underlagInstans++).withDelsvar(UNDERLAG_TYP_DELSVAR_ID_4,
                            aCV(UNDERLAG_CODE_SYSTEM, underlag.getTyp().getId(), underlag.getTyp().getLabel()))
                            .withDelsvar(UNDERLAG_DATUM_DELSVAR_ID_4,
                                    underlag.getDatum() != null ? underlag.getDatum().asLocalDate().toString() : null)
                            .withDelsvar(UNDERLAG_HAMTAS_FRAN_DELSVAR_ID_4, underlag.getHamtasFran()).build());
        }

        addIfNotBlank(svars, SJUKDOMSFORLOPP_SVAR_ID_5, SJUKDOMSFORLOPP_DELSVAR_ID_5, source.getSjukdomsforlopp());

        // Handle diagnoser
        SvarBuilder diagnosSvar = aSvar(DIAGNOS_SVAR_ID_6);
        for (int i = 0; i < source.getDiagnoser().size(); i++) {
            Diagnos diagnos = source.getDiagnoser().get(i);
            if (diagnos.getDiagnosKod() == null) {
                continue;
            }
            Diagnoskodverk diagnoskodverk = Diagnoskodverk.valueOf(diagnos.getDiagnosKodSystem());
            switch (i) {
            case 0:
                diagnosSvar.withDelsvar(DIAGNOS_DELSVAR_ID_6,
                        aCV(diagnoskodverk.getCodeSystem(), diagnos.getDiagnosKod(), diagnos.getDiagnosDisplayName()))
                        .withDelsvar(DIAGNOS_BESKRIVNING_DELSVAR_ID_6, diagnos.getDiagnosBeskrivning());
                break;
            case 1:
                diagnosSvar.withDelsvar(BIDIAGNOS_1_DELSVAR_ID_6,
                        aCV(diagnoskodverk.getCodeSystem(), diagnos.getDiagnosKod(), diagnos.getDiagnosDisplayName()))
                        .withDelsvar(BIDIAGNOS_1_BESKRIVNING_DELSVAR_ID_6, diagnos.getDiagnosBeskrivning());
                break;
            case 2:
                diagnosSvar.withDelsvar(BIDIAGNOS_2_DELSVAR_ID_6,
                        aCV(diagnoskodverk.getCodeSystem(), diagnos.getDiagnosKod(), diagnos.getDiagnosDisplayName()))
                        .withDelsvar(BIDIAGNOS_2_BESKRIVNING_DELSVAR_ID_6, diagnos.getDiagnosBeskrivning());
                break;
            default:
                throw new IllegalArgumentException();
            }
        }
        if (CollectionUtils.isNotEmpty(diagnosSvar.delSvars)) {
            svars.add(diagnosSvar.build());
        }

        if (source.getDiagnosgrund() != null) {
            svars.add(aSvar(DIAGNOSGRUND_SVAR_ID_7).withDelsvar(DIAGNOSGRUND_DELSVAR_ID_7, source.getDiagnosgrund()).build());
        }

        if (source.getNyBedomningDiagnosgrund() != null) {
            if (source.getNyBedomningDiagnosgrund()) {
                svars.add(
                        aSvar(NYDIAGNOS_SVAR_ID_45)
                                .withDelsvar(DIAGNOSGRUND_NYBEDOMNING_DELSVAR_ID_45, source.getNyBedomningDiagnosgrund().toString())
                                .withDelsvar(DIAGNOS_FOR_NY_BEDOMNING_DELSVAR_ID_45, source.getDiagnosForNyBedomning())
                                .build());
            } else {
                svars.add(aSvar(NYDIAGNOS_SVAR_ID_45)
                        .withDelsvar(DIAGNOSGRUND_NYBEDOMNING_DELSVAR_ID_45, Boolean.FALSE.toString()).build());
            }
        }

        addIfNotBlank(svars, FUNKTIONSNEDSATTNING_INTELLEKTUELL_SVAR_ID_8, FUNKTIONSNEDSATTNING_INTELLEKTUELL_DELSVAR_ID_8,
                source.getFunktionsnedsattningIntellektuell());
        addIfNotBlank(svars, FUNKTIONSNEDSATTNING_KOMMUNIKATION_SVAR_ID_9, FUNKTIONSNEDSATTNING_KOMMUNIKATION_DELSVAR_ID_9,
                source.getFunktionsnedsattningKommunikation());
        addIfNotBlank(svars, FUNKTIONSNEDSATTNING_KONCENTRATION_SVAR_ID_10, FUNKTIONSNEDSATTNING_KONCENTRATION_DELSVAR_ID_10,
                source.getFunktionsnedsattningKoncentration());
        addIfNotBlank(svars, FUNKTIONSNEDSATTNING_PSYKISK_SVAR_ID_11, FUNKTIONSNEDSATTNING_PSYKISK_DELSVAR_ID_11,
                source.getFunktionsnedsattningPsykisk());
        addIfNotBlank(svars, FUNKTIONSNEDSATTNING_SYNHORSELTAL_SVAR_ID_12, FUNKTIONSNEDSATTNING_SYNHORSELTAL_DELSVAR_ID_12,
                source.getFunktionsnedsattningSynHorselTal());
        addIfNotBlank(svars, FUNKTIONSNEDSATTNING_BALANSKOORDINATION_SVAR_ID_13, FUNKTIONSNEDSATTNING_BALANSKOORDINATION_DELSVAR_ID_13,
                source.getFunktionsnedsattningBalansKoordination());
        addIfNotBlank(svars, FUNKTIONSNEDSATTNING_ANNAN_SVAR_ID_14, FUNKTIONSNEDSATTNING_ANNAN_DELSVAR_ID_14, source.getFunktionsnedsattningAnnan());
        addIfNotBlank(svars, AKTIVITETSBEGRANSNING_SVAR_ID_17, AKTIVITETSBEGRANSNING_DELSVAR_ID_17, source.getAktivitetsbegransning());
        addIfNotBlank(svars, AVSLUTADBEHANDLING_SVAR_ID_18, AVSLUTADBEHANDLING_DELSVAR_ID_18, source.getAvslutadBehandling());
        addIfNotBlank(svars, PAGAENDEBEHANDLING_SVAR_ID_19, PAGAENDEBEHANDLING_DELSVAR_ID_19, source.getPagaendeBehandling());
        addIfNotBlank(svars, PLANERADBEHANDLING_SVAR_ID_20, PLANERADBEHANDLING_DELSVAR_ID_20, source.getPlaneradBehandling());
        addIfNotBlank(svars, SUBSTANSINTAG_SVAR_ID_21, SUBSTANSINTAG_DELSVAR_ID_21, source.getSubstansintag());
        addIfNotBlank(svars, MEDICINSKAFORUTSATTNINGARFORARBETE_SVAR_ID_22, MEDICINSKAFORUTSATTNINGARFORARBETE_DELSVAR_ID_22,
                source.getMedicinskaForutsattningarForArbete());
        addIfNotBlank(svars, FORMAGATROTSBEGRANSNING_SVAR_ID_23, FORMAGATROTSBEGRANSNING_DELSVAR_ID_23, source.getFormagaTrotsBegransning());
        addIfNotBlank(svars, OVRIGT_SVAR_ID_25, OVRIGT_DELSVAR_ID_25, buildOvrigaUpplysningar(source));

        if (source.getKontaktMedFk() != null) {
            if (source.getKontaktMedFk() && !StringUtils.isBlank(source.getAnledningTillKontakt())) {
                svars.add(aSvar(KONTAKT_ONSKAS_SVAR_ID_26).withDelsvar(KONTAKT_ONSKAS_DELSVAR_ID_26, source.getKontaktMedFk().toString())
                        .withDelsvar(ANLEDNING_TILL_KONTAKT_DELSVAR_ID_26, source.getAnledningTillKontakt()).build());
            } else {
                svars.add(aSvar(KONTAKT_ONSKAS_SVAR_ID_26).withDelsvar(KONTAKT_ONSKAS_DELSVAR_ID_26, source.getKontaktMedFk().toString()).build());
            }
        }

        for (Tillaggsfraga tillaggsfraga : source.getTillaggsfragor()) {
            addIfNotBlank(svars, tillaggsfraga.getId(), tillaggsfraga.getId() + ".1", tillaggsfraga.getSvar());
        }

        return svars;
    }

    private static String buildOvrigaUpplysningar(LuseUtlatande source) {
        String motiveringTillInteBaseratPaUndersokning = null;
        String ovrigt = null;

        // Since INTYG-2949, we have to concatenate information in the Övrigt-fält again... 
        if (!StringUtils.isBlank(source.getMotiveringTillInteBaseratPaUndersokning())) {
            motiveringTillInteBaseratPaUndersokning = "Motivering till varför utlåtandet inte baseras på undersökning av patienten: " + 
                    source.getMotiveringTillInteBaseratPaUndersokning();
        }

        if (!StringUtils.isBlank(source.getOvrigt())) {
            ovrigt = source.getOvrigt();
        }

        String ret = StringUtil.join("\n\n", motiveringTillInteBaseratPaUndersokning, ovrigt);
        return !StringUtils.isBlank(ret) ? ret : null;
    }
}
