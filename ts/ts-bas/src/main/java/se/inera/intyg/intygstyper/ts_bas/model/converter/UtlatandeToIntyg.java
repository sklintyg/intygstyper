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

package se.inera.intyg.intygstyper.ts_bas.model.converter;

import static se.inera.intyg.common.support.modules.converter.InternalConverterUtil.CERTIFICATE_CODE_SYSTEM;
import static se.inera.intyg.common.support.modules.converter.InternalConverterUtil.aCV;
import static se.inera.intyg.common.support.modules.converter.InternalConverterUtil.aSvar;
import static se.inera.intyg.intygstyper.ts_parent.codes.RespConstants.INTYG_AVSER_CODE_SYSTEM;
import static se.inera.intyg.intygstyper.ts_parent.codes.RespConstants.INTYG_AVSER_DELSVAR_ID_1;
import static se.inera.intyg.intygstyper.ts_parent.codes.RespConstants.INTYG_AVSER_SVAR_ID_1;
import static se.inera.intyg.intygstyper.ts_parent.codes.RespConstants.NOT_AVAILABLE;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import se.inera.intyg.common.support.modules.converter.InternalConverterUtil;
import se.inera.intyg.intygstyper.ts_bas.model.internal.IntygAvserKategori;
import se.inera.intyg.intygstyper.ts_bas.model.internal.Utlatande;
import se.inera.intyg.intygstyper.ts_bas.support.TsBasEntryPoint;
import se.inera.intyg.intygstyper.ts_parent.codes.IntygAvserKod;
import se.riv.clinicalprocess.healthcond.certificate.types.v2.TypAvIntyg;
import se.riv.clinicalprocess.healthcond.certificate.v2.Intyg;
import se.riv.clinicalprocess.healthcond.certificate.v2.Svar;

public final class UtlatandeToIntyg {

    private UtlatandeToIntyg() {
    }

    public static Intyg convert(Utlatande source) {
        Intyg intyg = InternalConverterUtil.getIntyg(source);
        complementArbetsplatskodIfMissing(intyg);
        intyg.setTyp(getTypAvIntyg(source));
        intyg.getSvar().addAll(getSvar(source));
        return intyg;
    }

    private static TypAvIntyg getTypAvIntyg(Utlatande source) {
        TypAvIntyg typAvIntyg = new TypAvIntyg();
        typAvIntyg.setCode(source.getTyp().toUpperCase());
        typAvIntyg.setCodeSystem(CERTIFICATE_CODE_SYSTEM);
        typAvIntyg.setDisplayName(TsBasEntryPoint.MODULE_NAME);
        return typAvIntyg;
    }

    private static void complementArbetsplatskodIfMissing(Intyg intyg) {
        if (StringUtils.isBlank(intyg.getSkapadAv().getEnhet().getArbetsplatskod().getExtension())) {
            intyg.getSkapadAv().getEnhet().getArbetsplatskod().setExtension(NOT_AVAILABLE);
        }
    }

    private static List<Svar> getSvar(Utlatande source) {
        List<Svar> svars = new ArrayList<>();

        // for now this is the only relevant question (for MI)
        if (source.getIntygAvser() != null) {
            for (IntygAvserKategori korkortstyp : source.getIntygAvser().getKorkortstyp()) {
                IntygAvserKod intygAvser = IntygAvserKod.valueOf(korkortstyp.name());
                svars.add(aSvar(INTYG_AVSER_SVAR_ID_1)
                        .withDelsvar(INTYG_AVSER_DELSVAR_ID_1, aCV(INTYG_AVSER_CODE_SYSTEM, intygAvser.getCode(), intygAvser.getDescription()))
                        .build());
            }
        }

        return svars;
    }

}
