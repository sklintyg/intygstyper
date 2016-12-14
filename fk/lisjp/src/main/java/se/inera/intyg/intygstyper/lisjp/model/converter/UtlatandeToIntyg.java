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

package se.inera.intyg.intygstyper.lisjp.model.converter;

import static se.inera.intyg.common.support.Constants.KV_INTYGSTYP_CODE_SYSTEM;
import static se.inera.intyg.common.support.modules.converter.InternalConverterUtil.*;
import static se.inera.intyg.intygstyper.fkparent.model.converter.InternalToTransportUtil.handleDiagnosSvar;
import static se.inera.intyg.intygstyper.fkparent.model.converter.RespConstants.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Joiner;

import se.inera.intyg.common.support.modules.converter.InternalConverterUtil;
import se.inera.intyg.intygstyper.fkparent.model.converter.RespConstants;
import se.inera.intyg.intygstyper.fkparent.model.internal.Tillaggsfraga;
import se.inera.intyg.intygstyper.lisjp.model.internal.ArbetslivsinriktadeAtgarder;
import se.inera.intyg.intygstyper.lisjp.model.internal.LisjpUtlatande;
import se.inera.intyg.intygstyper.lisjp.model.internal.Sjukskrivning;
import se.inera.intyg.intygstyper.lisjp.model.internal.Sysselsattning;
import se.inera.intyg.intygstyper.lisjp.support.LisjpEntryPoint;
import se.riv.clinicalprocess.healthcond.certificate.types.v2.TypAvIntyg;
import se.riv.clinicalprocess.healthcond.certificate.v2.Intyg;
import se.riv.clinicalprocess.healthcond.certificate.v2.Svar;

public final class UtlatandeToIntyg {

    private UtlatandeToIntyg() {
    }

    public static Intyg convert(LisjpUtlatande source) {
        Intyg intyg = InternalConverterUtil.getIntyg(source);
        intyg.setTyp(getTypAvIntyg(source));
        intyg.getSvar().addAll(getSvar(source));
        return intyg;
    }

    private static TypAvIntyg getTypAvIntyg(LisjpUtlatande source) {
        TypAvIntyg typAvIntyg = new TypAvIntyg();
        typAvIntyg.setCode(source.getTyp().toUpperCase());
        typAvIntyg.setCodeSystem(KV_INTYGSTYP_CODE_SYSTEM);
        typAvIntyg.setDisplayName(LisjpEntryPoint.MODULE_NAME);
        return typAvIntyg;
    }

    private static List<Svar> getSvar(LisjpUtlatande source) {
        List<Svar> svars = new ArrayList<>();

        addIfNotNull(svars, AVSTANGNING_SMITTSKYDD_SVAR_ID_27, AVSTANGNING_SMITTSKYDD_DELSVAR_ID_27, source.getAvstangningSmittskydd());

        getGrundForMUSvar(source, svars);

        int sysselsattningInstans = 1;
        if (source.getSysselsattning() != null) {
            for (Sysselsattning sysselsattning : source.getSysselsattning()) {
                if (sysselsattning.getTyp() != null) {
                    svars.add(aSvar(TYP_AV_SYSSELSATTNING_SVAR_ID_28, sysselsattningInstans++).withDelsvar(TYP_AV_SYSSELSATTNING_DELSVAR_ID_28,
                            aCV(TYP_AV_SYSSELSATTNING_CODE_SYSTEM, sysselsattning.getTyp().getId(), sysselsattning.getTyp().getLabel()))
                            .build());
                }
            }
        }

        addIfNotBlank(svars, NUVARANDE_ARBETE_SVAR_ID_29, NUVARANDE_ARBETE_DELSVAR_ID_29, source.getNuvarandeArbete());

        addIfNotBlank(svars, ARBETSMARKNADSPOLITISKT_PROGRAM_SVAR_ID_30, ARBETSMARKNADSPOLITISKT_PROGRAM_DELSVAR_ID_30,
                source.getArbetsmarknadspolitisktProgram());

        handleDiagnosSvar(svars, source.getDiagnoser());

        addIfNotBlank(svars, FUNKTIONSNEDSATTNING_SVAR_ID_35, FUNKTIONSNEDSATTNING_DELSVAR_ID_35, source.getFunktionsnedsattning());
        addIfNotBlank(svars, AKTIVITETSBEGRANSNING_SVAR_ID_17, AKTIVITETSBEGRANSNING_DELSVAR_ID_17, source.getAktivitetsbegransning());
        addIfNotBlank(svars, PAGAENDEBEHANDLING_SVAR_ID_19, PAGAENDEBEHANDLING_DELSVAR_ID_19, source.getPagaendeBehandling());
        addIfNotBlank(svars, PLANERADBEHANDLING_SVAR_ID_20, PLANERADBEHANDLING_DELSVAR_ID_20, source.getPlaneradBehandling());

        int sjukskrivningInstans = 1;
        for (Sjukskrivning sjukskrivning : source.getSjukskrivningar()) {
            svars.add(aSvar(BEHOV_AV_SJUKSKRIVNING_SVAR_ID_32, sjukskrivningInstans++).withDelsvar(BEHOV_AV_SJUKSKRIVNING_NIVA_DELSVARSVAR_ID_32,
                    aCV(SJUKSKRIVNING_CODE_SYSTEM, sjukskrivning.getSjukskrivningsgrad().getId(),
                            sjukskrivning.getSjukskrivningsgrad().getLabel()))
                    .withDelsvar(BEHOV_AV_SJUKSKRIVNING_PERIOD_DELSVARSVAR_ID_32,
                            aDatePeriod(sjukskrivning.getPeriod().fromAsLocalDate(), sjukskrivning.getPeriod().tomAsLocalDate()))
                    .build());
        }

        addIfNotBlank(svars, FORSAKRINGSMEDICINSKT_BESLUTSSTOD_SVAR_ID_37, FORSAKRINGSMEDICINSKT_BESLUTSSTOD_DELSVAR_ID_37,
                source.getForsakringsmedicinsktBeslutsstod());

        if (source.getArbetstidsforlaggning() != null) {
            if (source.getArbetstidsforlaggning() && !StringUtils.isBlank(source.getArbetstidsforlaggningMotivering())) {
                svars.add(aSvar(ARBETSTIDSFORLAGGNING_SVAR_ID_33)
                        .withDelsvar(ARBETSTIDSFORLAGGNING_OM_DELSVAR_ID_33, source.getArbetstidsforlaggning().toString())
                        .withDelsvar(ARBETSTIDSFORLAGGNING_MOTIVERING_SVAR_ID_33, source.getArbetstidsforlaggningMotivering()).build());
            } else {
                svars.add(aSvar(ARBETSTIDSFORLAGGNING_SVAR_ID_33).withDelsvar(ARBETSTIDSFORLAGGNING_OM_DELSVAR_ID_33,
                        source.getArbetstidsforlaggning().toString()).build());
            }
        }

        if (source.getArbetsresor() != null) {
            svars.add(aSvar(ARBETSRESOR_SVAR_ID_34).withDelsvar(ARBETSRESOR_OM_DELSVAR_ID_34, source.getArbetsresor().toString()).build());
        }

        if (source.getPrognos() != null && source.getPrognos().getTyp() != null) {
            if (source.getPrognos().getDagarTillArbete() != null) {
                svars.add(aSvar(PROGNOS_SVAR_ID_39).withDelsvar(PROGNOS_BESKRIVNING_DELSVAR_ID_39,
                        aCV(PROGNOS_CODE_SYSTEM, source.getPrognos().getTyp().getId(),
                                source.getPrognos().getTyp().getLabel()))
                        .withDelsvar(PROGNOS_DAGAR_TILL_ARBETE_DELSVAR_ID_39,
                                aCV(PROGNOS_DAGAR_TILL_ARBETE_CODE_SYSTEM, source.getPrognos().getDagarTillArbete().getId(),
                                        source.getPrognos().getDagarTillArbete().getLabel())).build());
            } else {
                svars.add(aSvar(PROGNOS_SVAR_ID_39).withDelsvar(PROGNOS_BESKRIVNING_DELSVAR_ID_39,
                        aCV(PROGNOS_CODE_SYSTEM, source.getPrognos().getTyp().getId(),
                                source.getPrognos().getTyp().getLabel()))
                        .build());
            }
        }

        int arbetslivsinriktadeAtgarderInstans = 1;
        for (ArbetslivsinriktadeAtgarder atgarder : source.getArbetslivsinriktadeAtgarder()) {
            svars.add(aSvar(ARBETSLIVSINRIKTADE_ATGARDER_SVAR_ID_40, arbetslivsinriktadeAtgarderInstans++).withDelsvar(ARBETSLIVSINRIKTADE_ATGARDER_VAL_DELSVAR_ID_40,
                    aCV(ARBETSLIVSINRIKTADE_ATGARDER_CODE_SYSTEM, atgarder.getTyp().getId(), atgarder.getTyp().getLabel())).build());
        }

        addIfNotBlank(svars, ARBETSLIVSINRIKTADE_ATGARDER_BESKRIVNING_SVAR_ID_44, ARBETSLIVSINRIKTADE_ATGARDER_BESKRIVNING_DELSVAR_ID_44, source.getArbetslivsinriktadeAtgarderBeskrivning());

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

    private static void getGrundForMUSvar(LisjpUtlatande source, List<Svar> svars) {
        int grundForMUInstans = 1;
        if (source.getUndersokningAvPatienten() != null && source.getUndersokningAvPatienten().isValidDate()) {
            svars.add(aSvar(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1, grundForMUInstans++).withDelsvar(GRUNDFORMEDICINSKTUNDERLAG_TYP_DELSVAR_ID_1,
                    aCV(GRUNDFORMEDICINSKTUNDERLAG_CODE_SYSTEM, RespConstants.ReferensTyp.UNDERSOKNING.transportId, RespConstants.ReferensTyp.UNDERSOKNING.label))
                    .withDelsvar(GRUNDFORMEDICINSKTUNDERLAG_DATUM_DELSVAR_ID_1, source.getUndersokningAvPatienten().asLocalDate().toString())
                    .build());
        }

        if (source.getTelefonkontaktMedPatienten() != null && source.getTelefonkontaktMedPatienten().isValidDate()) {
            svars.add(aSvar(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1, grundForMUInstans++).withDelsvar(GRUNDFORMEDICINSKTUNDERLAG_TYP_DELSVAR_ID_1,
                    aCV(GRUNDFORMEDICINSKTUNDERLAG_CODE_SYSTEM, RespConstants.ReferensTyp.TELEFONKONTAKT.transportId, RespConstants.ReferensTyp.TELEFONKONTAKT.label))
                    .withDelsvar(GRUNDFORMEDICINSKTUNDERLAG_DATUM_DELSVAR_ID_1, source.getTelefonkontaktMedPatienten().asLocalDate().toString())
                    .build());
        }

        if (source.getJournaluppgifter() != null && source.getJournaluppgifter().isValidDate()) {
            svars.add(aSvar(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1, grundForMUInstans++).withDelsvar(GRUNDFORMEDICINSKTUNDERLAG_TYP_DELSVAR_ID_1,
                    aCV(GRUNDFORMEDICINSKTUNDERLAG_CODE_SYSTEM, RespConstants.ReferensTyp.JOURNAL.transportId, RespConstants.ReferensTyp.JOURNAL.label))
                    .withDelsvar(GRUNDFORMEDICINSKTUNDERLAG_DATUM_DELSVAR_ID_1, source.getJournaluppgifter().asLocalDate().toString()).build());
        }

        if (source.getAnnatGrundForMU() != null && source.getAnnatGrundForMU().isValidDate()) {
            svars.add(aSvar(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1, grundForMUInstans++).withDelsvar(GRUNDFORMEDICINSKTUNDERLAG_TYP_DELSVAR_ID_1,
                    aCV(GRUNDFORMEDICINSKTUNDERLAG_CODE_SYSTEM, RespConstants.ReferensTyp.ANNAT.transportId, RespConstants.ReferensTyp.ANNAT.label))
                    .withDelsvar(GRUNDFORMEDICINSKTUNDERLAG_DATUM_DELSVAR_ID_1, source.getAnnatGrundForMU().asLocalDate().toString())
                    .withDelsvar(GRUNDFORMEDICINSKTUNDERLAG_ANNANBESKRIVNING_DELSVAR_ID_1, source.getAnnatGrundForMUBeskrivning()).build());
        }
    }

    private static String buildOvrigaUpplysningar(LisjpUtlatande source) {
        String motiveringTillInteBaseratPaUndersokning = null;
        String motiveringTillTidigSjukskrivning = null;
        String ovrigt = null;

        // Since INTYG-2949, we have to concatenate information in the Övrigt-fält again...
        if (!StringUtils.isBlank(source.getMotiveringTillInteBaseratPaUndersokning())) {
            motiveringTillInteBaseratPaUndersokning = "Motivering till varför utlåtandet inte baseras på undersökning av patienten: "
                    + source.getMotiveringTillInteBaseratPaUndersokning();
        }

        // INTYG-3207 motiveringTillTidigtStartdatumForSjukskrivning
        if (!StringUtils.isBlank(source.getMotiveringTillTidigtStartdatumForSjukskrivning())) {
            motiveringTillTidigSjukskrivning = "Orsak för att starta perioden mer än 7 dagar bakåt i tiden: "
                    + source.getMotiveringTillTidigtStartdatumForSjukskrivning();
        }

        if (!StringUtils.isBlank(source.getOvrigt())) {
            ovrigt = source.getOvrigt();
        }

        String ret = Joiner.on("\n").skipNulls().join(motiveringTillInteBaseratPaUndersokning, motiveringTillTidigSjukskrivning, ovrigt);
        return !StringUtils.isBlank(ret) ? ret : null;
    }

}
