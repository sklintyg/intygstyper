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
package se.inera.certificate.modules.luae_na.support;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import se.inera.certificate.modules.luae_na.rest.AktivitetsersattningNAModuleApi;
import se.inera.intyg.common.support.modules.support.ApplicationOrigin;
import se.inera.intyg.common.support.modules.support.ModuleEntryPoint;
import se.inera.intyg.common.support.modules.support.api.ModuleApi;
import se.inera.intyg.common.support.modules.support.feature.ModuleFeaturesFactory;

public class AktivitetsersattningNAEntryPoint implements ModuleEntryPoint {
    public static final String DEFAULT_RECIPIENT_ID = "FK";

    public static final String MODULE_ID = "luae_na";
    public static final String MODULE_NAME = "Läkarutlåtande för aktivitetsersättning";
    public static final String MODULE_DESCRIPTION = "Läkarintyg enligt 3 kap, 8 § lagen (1962:381) om allmän försäkring";

    @Autowired
    private AktivitetsersattningNAModuleApi moduleApi;

    public String getModuleId() {
        return MODULE_ID;
    }

    public String getModuleName() {
        return MODULE_NAME;
    }

    public String getModuleDescription() {
        return MODULE_DESCRIPTION;
    }

    public String getDefaultRecipient() {
        return DEFAULT_RECIPIENT_ID;
    }

    public ModuleApi getModuleApi() {
        return moduleApi;
    }

    public Map<String, Boolean> getModuleFeatures() {
        return ModuleFeaturesFactory.getFeatures("luae_na-features.properties");
    }

    public String getModuleCssPath(ApplicationOrigin originator) {
         switch (originator) {
         case MINA_INTYG:
         return "/web/webjars/luae_na/minaintyg/css/luae_na.css";
         case WEBCERT:
         return "/web/webjars/luae_na/webcert/css/luae_na.css";
         default:
         return null;
         }
    }

    public String getModuleScriptPath(ApplicationOrigin originator) {
         switch (originator) {
         case MINA_INTYG:
         return "/web/webjars/luae_na/minaintyg/js/module";
         case WEBCERT:
         return "/web/webjars/luae_na/webcert/module";
         default:
         return null;
         }
    }

    public String getModuleDependencyDefinitionPath(ApplicationOrigin originator) {
         switch (originator) {
         case MINA_INTYG:
         return "/web/webjars/luae_na/minaintyg/js/module-deps.json";
         case WEBCERT:
         return "/web/webjars/luae_na/webcert/module-deps.json";
         default:
         return null;
         }
    }

}
