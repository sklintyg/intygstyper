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

package se.inera.intyg.intygstyper.fk7263.model.converter;

import static se.inera.intyg.common.support.modules.converter.InternalConverterUtil.CERTIFICATE_CODE_SYSTEM;
import static se.inera.intyg.common.support.modules.converter.InternalConverterUtil.aCV;
import static se.inera.intyg.common.support.modules.converter.InternalConverterUtil.aDatePeriod;
import static se.inera.intyg.common.support.modules.converter.InternalConverterUtil.aSvar;

import java.util.ArrayList;
import java.util.List;

import se.inera.intyg.common.support.model.InternalLocalDateInterval;
import se.inera.intyg.common.support.modules.converter.InternalConverterUtil;
import se.inera.intyg.intygstyper.fk7263.model.internal.Utlatande;
import se.inera.intyg.intygstyper.fk7263.support.Fk7263EntryPoint;
import se.riv.clinicalprocess.healthcond.certificate.types.v2.TypAvIntyg;
import se.riv.clinicalprocess.healthcond.certificate.v2.Intyg;
import se.riv.clinicalprocess.healthcond.certificate.v2.Svar;

public final class UtlatandeToIntyg {

    private static final String CERTIFICATE_DISPLAY_NAME = Fk7263EntryPoint.MODULE_NAME;
    private static final String SJUKSKRIVNING_CODE_SYSTEM = "KV_FKMU_0003";
    private static final String NEDSATT_MED_100_CODE = "1";
    private static final String NEDSATT_MED_75_CODE = "2";
    private static final String NEDSATT_MED_50_CODE = "3";
    private static final String NEDSATT_MED_25_CODE = "4";
    private static final String BEHOV_AV_SJUKSKRIVNING_SVAR_ID_32 = "32";
    private static final String BEHOV_AV_SJUKSKRIVNING_NIVA_DELSVARSVAR_ID_32 = "32.1";
    private static final String BEHOV_AV_SJUKSKRIVNING_PERIOD_DELSVARSVAR_ID_32 = "32.2";

    private UtlatandeToIntyg() {
    }

    public static Intyg convert(Utlatande source) {
        Intyg intyg = InternalConverterUtil.getIntyg(source);
        intyg.setTyp(getTypAvIntyg(source));
        intyg.getSvar().addAll(getSvar(source));
        return intyg;
    }

    private static TypAvIntyg getTypAvIntyg(Utlatande source) {
        TypAvIntyg typAvIntyg = new TypAvIntyg();
        typAvIntyg.setCode(source.getTyp().toUpperCase());
        typAvIntyg.setCodeSystem(CERTIFICATE_CODE_SYSTEM);
        typAvIntyg.setDisplayName(CERTIFICATE_DISPLAY_NAME);
        return typAvIntyg;
    }

    private static List<Svar> getSvar(Utlatande source) {
        List<Svar> svars = new ArrayList<>();

        // for now this is the only relevant question (for MI)
        if (source.getNedsattMed100() != null) {
            svars.add(createBehovAvSjukskrivningSvar(NEDSATT_MED_100_CODE, source.getNedsattMed100()));
        }
        if (source.getNedsattMed75() != null) {
            svars.add(createBehovAvSjukskrivningSvar(NEDSATT_MED_75_CODE, source.getNedsattMed75()));
        }
        if (source.getNedsattMed50() != null) {
            svars.add(createBehovAvSjukskrivningSvar(NEDSATT_MED_50_CODE, source.getNedsattMed50()));
        }
        if (source.getNedsattMed25() != null) {
            svars.add(createBehovAvSjukskrivningSvar(NEDSATT_MED_25_CODE, source.getNedsattMed25()));
        }

        return svars;
    }

    private static Svar createBehovAvSjukskrivningSvar(String code, InternalLocalDateInterval interval) {
        return aSvar(BEHOV_AV_SJUKSKRIVNING_SVAR_ID_32).withDelsvar(BEHOV_AV_SJUKSKRIVNING_NIVA_DELSVARSVAR_ID_32,
                aCV(SJUKSKRIVNING_CODE_SYSTEM, code, SJUKSKRIVNING_CODE_SYSTEM))
                .withDelsvar(BEHOV_AV_SJUKSKRIVNING_PERIOD_DELSVARSVAR_ID_32,
                        aDatePeriod(interval.fromAsLocalDate(), interval.tomAsLocalDate()))
                .build();
    }

}
