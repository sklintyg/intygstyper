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

package se.inera.intyg.intygstyper.ts_bas.support;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import se.inera.intyg.common.support.modules.support.ApplicationOrigin;
import se.inera.intyg.common.support.modules.support.ModuleEntryPoint;
import se.inera.intyg.common.support.modules.support.api.ModuleApi;
import se.inera.intyg.common.support.modules.support.feature.ModuleFeaturesFactory;
import se.inera.intyg.intygstyper.ts_bas.rest.TsBasModuleApi;

public class TsBasEntryPoint implements ModuleEntryPoint {

    private static final String DEFAULT_RECIPIENT_ID = "TS";
    public static final String MODULE_ID = "ts-bas";
    public static final String MODULE_NAME = "Transportstyrelsens läkarintyg";
    public static final String MODULE_DESCRIPTION = "Läkarintyg - avseende högre körkortsbehörigheter eller taxiförarlegitimation - på begäran av Transportstyrelsen";
    private static final String MODULE_DETAILED_DESCRIPTION = "<p>Transportstyrelsens läkarintyg ska användas vid förlängd giltighet av högre behörighet från 45 år, ansökan om körkortstillstånd för grupp II och III och vid ansökan om taxiförarlegitimation. Transportstyrelsens läkarintyg kan även användas när Transportstyrelsen i annat fall begärt ett allmänt läkarintyg avseende lämplighet att inneha körkort.</p>Specialistintyg finns bl.a. för alkohol, läkemedel, synfunktion, Alkolås m.m. Se <a href=\"http://www.transportstyrelsen.se\" target=\"_blank\">www.transportstyrelsen.se</a>.";
    public static final String KV_UTLATANDETYP_INTYG_CODE = "TSTRK1007";

    @Autowired
    private TsBasModuleApi moduleApi;

    @Override
    public String getModuleId() {
        return MODULE_ID;
    }

    @Override
    public String getModuleName() {
        return MODULE_NAME;
    }

    @Override
    public String getModuleDescription() {
        return MODULE_DESCRIPTION;
    }

    @Override
    public String getDetailedModuleDescription() {
        return MODULE_DETAILED_DESCRIPTION;
    }

    @Override
    public ModuleApi getModuleApi() {
        return moduleApi;
    }

    @Override
    public Map<String, Boolean> getModuleFeatures() {
        return ModuleFeaturesFactory.getFeatures(MODULE_ID, "ts-bas-features.properties");
    }

    @Override
    public String getModuleCssPath(ApplicationOrigin originator) {
        switch (originator) {
        case MINA_INTYG:
            return "/web/webjars/ts-bas/minaintyg/css/ts-bas.css";
        case WEBCERT:
            return "/web/webjars/ts-bas/webcert/css/ts-bas.css";
        default:
            return null;
        }
    }

    @Override
    public String getModuleScriptPath(ApplicationOrigin originator) {
        switch (originator) {
        case MINA_INTYG:
            return "/web/webjars/ts-bas/minaintyg/js/module";
        case WEBCERT:
            return "/web/webjars/ts-bas/webcert/module";
        default:
            return null;
        }
    }

    @Override
    public String getModuleDependencyDefinitionPath(ApplicationOrigin originator) {
        switch (originator) {
        case MINA_INTYG:
            return "/web/webjars/ts-bas/minaintyg/js/module-deps.json";
        case WEBCERT:
            return "/web/webjars/ts-bas/webcert/module-deps.json";
        default:
            return null;
        }
    }

    @Override
    public String getDefaultRecipient() {
        return DEFAULT_RECIPIENT_ID;
    }

    @Override
    public String getExternalId() {
        return KV_UTLATANDETYP_INTYG_CODE;
    }
}
