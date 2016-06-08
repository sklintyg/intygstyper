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

package se.inera.certificate.modules.sjukpenning_utokad.support;

import org.springframework.beans.factory.annotation.Autowired;

import se.inera.certificate.modules.sjukpenning_utokad.rest.SjukpenningUtokadModuleApi;
import se.inera.intyg.common.support.modules.support.ApplicationOrigin;
import se.inera.intyg.common.support.modules.support.ModuleEntryPoint;
import se.inera.intyg.common.support.modules.support.api.ModuleApi;
import se.inera.intyg.common.support.modules.support.feature.ModuleFeaturesFactory;

import java.util.Map;

public class SjukpenningUtokadEntryPoint implements ModuleEntryPoint {

    public static final String DEFAULT_RECIPIENT_ID = "FK";

    public static final String MODULE_ID = "lisu";
    public static final String MODULE_NAME = "Läkarintyg för sjukpenning utökat";
    public static final String MODULE_DESCRIPTION = "Läkarintyg för sjukpenning utökat";

    @Autowired
    private SjukpenningUtokadModuleApi moduleApi;

    @Override
    public String getDefaultRecipient() {
        return DEFAULT_RECIPIENT_ID;
    }

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
    public ModuleApi getModuleApi() {
        return moduleApi;
    }

    @Override
    public Map<String, Boolean> getModuleFeatures() {
        return ModuleFeaturesFactory.getFeatures("sjukpenning-utokad-features.properties");
    }

    @Override
    public String getModuleCssPath(ApplicationOrigin originator) {
        switch (originator) {
        case MINA_INTYG:
            return "/web/webjars/lisu/minaintyg/css/sjukpenning-utokad.css";
        case WEBCERT:
            return "/web/webjars/lisu/webcert/css/sjukpenning-utokad.css";
        default:
            return null;
        }
    }

    @Override
    public String getModuleScriptPath(ApplicationOrigin originator) {
        switch (originator) {
        case MINA_INTYG:
            return "/web/webjars/lisu/minaintyg/js/module";
        case WEBCERT:
            return "/web/webjars/lisu/webcert/module";
        default:
            return null;
        }
    }

    @Override
    public String getModuleDependencyDefinitionPath(ApplicationOrigin originator) {
        switch (originator) {
        case MINA_INTYG:
            return "/web/webjars/lisu/minaintyg/js/module-deps.json";
        case WEBCERT:
            return "/web/webjars/lisu/webcert/module-deps.json";
        default:
            return null;
        }
    }
}