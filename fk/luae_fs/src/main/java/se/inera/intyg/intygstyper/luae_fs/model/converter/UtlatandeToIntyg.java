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

import static se.inera.intyg.common.support.Constants.KV_INTYGSTYP_CODE_SYSTEM;
import static se.inera.intyg.common.support.modules.converter.InternalConverterUtil.aCV;
import static se.inera.intyg.common.support.modules.converter.InternalConverterUtil.aSvar;
import static se.inera.intyg.common.support.modules.converter.InternalConverterUtil.addIfNotBlank;
import static se.inera.intyg.intygstyper.fkparent.model.converter.InternalToTransportUtil.handleDiagnosSvar;
import static se.inera.intyg.intygstyper.fkparent.model.converter.RespConstants.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import se.inera.intyg.common.support.modules.converter.InternalConverterUtil;
import se.inera.intyg.intygstyper.fkparent.model.converter.RespConstants;
import se.inera.intyg.intygstyper.fkparent.model.internal.Tillaggsfraga;
import se.inera.intyg.intygstyper.fkparent.model.internal.Underlag;
import se.inera.intyg.intygstyper.luae_fs.model.internal.LuaefsUtlatande;
import se.inera.intyg.intygstyper.luae_fs.support.LuaefsEntryPoint;
import se.riv.clinicalprocess.healthcond.certificate.types.v2.TypAvIntyg;
import se.riv.clinicalprocess.healthcond.certificate.v2.Intyg;
import se.riv.clinicalprocess.healthcond.certificate.v2.Svar;

public final class UtlatandeToIntyg {

    private UtlatandeToIntyg() {
    }

    public static Intyg convert(LuaefsUtlatande source) {
        Intyg intyg = InternalConverterUtil.getIntyg(source);
        intyg.setTyp(getTypAvIntyg(source));
        intyg.getSvar().addAll(getSvar(source));
        return intyg;
    }

    private static TypAvIntyg getTypAvIntyg(LuaefsUtlatande source) {
        TypAvIntyg typAvIntyg = new TypAvIntyg();
        typAvIntyg.setCode(source.getTyp().toUpperCase());
        typAvIntyg.setCodeSystem(KV_INTYGSTYP_CODE_SYSTEM);
        typAvIntyg.setDisplayName(LuaefsEntryPoint.MODULE_NAME);
        return typAvIntyg;
    }

    private static List<Svar> getSvar(LuaefsUtlatande source) {
        List<Svar> svars = new ArrayList<>();

        int grundForMUInstans = 1;
        if (source.getUndersokningAvPatienten() != null) {
            svars.add(aSvar(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1, grundForMUInstans++)
                    .withDelsvar(GRUNDFORMEDICINSKTUNDERLAG_TYP_DELSVAR_ID_1,
                            aCV(GRUNDFORMEDICINSKTUNDERLAG_CODE_SYSTEM, RespConstants.ReferensTyp.UNDERSOKNING.transportId, RespConstants.ReferensTyp.UNDERSOKNING.label))
                    .withDelsvar(GRUNDFORMEDICINSKTUNDERLAG_DATUM_DELSVAR_ID_1, source.getUndersokningAvPatienten().asLocalDate().toString())
                    .build());
        }
        if (source.getJournaluppgifter() != null) {
            svars.add(aSvar(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1, grundForMUInstans++)
                    .withDelsvar(GRUNDFORMEDICINSKTUNDERLAG_TYP_DELSVAR_ID_1,
                            aCV(GRUNDFORMEDICINSKTUNDERLAG_CODE_SYSTEM, RespConstants.ReferensTyp.JOURNAL.transportId, RespConstants.ReferensTyp.JOURNAL.label))
                    .withDelsvar(GRUNDFORMEDICINSKTUNDERLAG_DATUM_DELSVAR_ID_1, source.getJournaluppgifter().asLocalDate().toString()).build());
        }
        if (source.getAnhorigsBeskrivningAvPatienten() != null) {
            svars.add(aSvar(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1, grundForMUInstans++)
                    .withDelsvar(GRUNDFORMEDICINSKTUNDERLAG_TYP_DELSVAR_ID_1,
                            aCV(GRUNDFORMEDICINSKTUNDERLAG_CODE_SYSTEM, RespConstants.ReferensTyp.ANHORIGSBESKRIVNING.transportId, RespConstants.ReferensTyp.ANHORIGSBESKRIVNING.label))
                    .withDelsvar(GRUNDFORMEDICINSKTUNDERLAG_DATUM_DELSVAR_ID_1, source.getAnhorigsBeskrivningAvPatienten().asLocalDate().toString())
                    .build());
        }
        if (source.getAnnatGrundForMU() != null) {
            svars.add(aSvar(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1, grundForMUInstans++)
                    .withDelsvar(GRUNDFORMEDICINSKTUNDERLAG_TYP_DELSVAR_ID_1,
                            aCV(GRUNDFORMEDICINSKTUNDERLAG_CODE_SYSTEM, RespConstants.ReferensTyp.ANNAT.transportId, RespConstants.ReferensTyp.ANNAT.label))
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

        handleDiagnosSvar(svars, source.getDiagnoser());

        addIfNotBlank(svars, FUNKTIONSNEDSATTNING_DEBUT_SVAR_ID_15, FUNKTIONSNEDSATTNING_DEBUT_DELSVAR_ID_15, source.getFunktionsnedsattningDebut());
        addIfNotBlank(svars, FUNKTIONSNEDSATTNING_PAVERKAN_SVAR_ID_16, FUNKTIONSNEDSATTNING_PAVERKAN_DELSVAR_ID_16, source.getFunktionsnedsattningPaverkan());


        /* End complex object */

        addIfNotBlank(svars, OVRIGT_SVAR_ID_25, OVRIGT_DELSVAR_ID_25, source.getOvrigt());

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

}
