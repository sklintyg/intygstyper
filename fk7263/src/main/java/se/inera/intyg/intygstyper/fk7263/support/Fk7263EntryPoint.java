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

package se.inera.intyg.intygstyper.fk7263.support;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import se.inera.intyg.common.support.modules.support.ApplicationOrigin;
import se.inera.intyg.common.support.modules.support.ModuleEntryPoint;
import se.inera.intyg.common.support.modules.support.api.ModuleApi;
import se.inera.intyg.common.support.modules.support.feature.ModuleFeaturesFactory;
import se.inera.intyg.intygstyper.fk7263.rest.Fk7263ModuleApi;

public class Fk7263EntryPoint implements ModuleEntryPoint {

    public static final String DEFAULT_RECIPIENT_ID = "FK";

    public static final String MODULE_ID = "fk7263";
    public static final String MODULE_NAME = "Läkarintyg FK 7263";
    public static final String MODULE_DESCRIPTION = "Läkarintyg enligt 3 kap, 8 § lagen (1962:381) om allmän försäkring";
    private static final String MODULE_DETAILED_DESCRIPTION = "Läkarintyget används av Försäkringskassan för att bedöma om patienten har rätt till sjukpenning. Av intyget ska det framgå hur sjukdomen påverkar patientens arbetsförmåga och hur länge patienten behöver vara sjukskriven.";

    @Autowired
    private Fk7263ModuleApi moduleApi;

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
    public String getDetailedModuleDescription() {
        return MODULE_DETAILED_DESCRIPTION;
    }

    @Override
    public ModuleApi getModuleApi() {
        return moduleApi;
    }

    @Override
    public Map<String, Boolean> getModuleFeatures() {
        return ModuleFeaturesFactory.getFeatures(MODULE_ID, "fk7263-features.properties");
    }

    @Override
    public String getModuleCssPath(ApplicationOrigin originator) {
        switch (originator) {
        case MINA_INTYG:
            return "/web/webjars/fk7263/minaintyg/css/fk7263.css";
        case WEBCERT:
            return "/web/webjars/fk7263/webcert/css/fk7263.css";
        default:
            return null;
        }
    }

    @Override
    public String getModuleScriptPath(ApplicationOrigin originator) {
        switch (originator) {
        case MINA_INTYG:
            return "/web/webjars/fk7263/minaintyg/js/module";
        case WEBCERT:
            return "/web/webjars/fk7263/webcert/module";
        default:
            return null;
        }
    }

    @Override
    public String getModuleDependencyDefinitionPath(ApplicationOrigin originator) {
        switch (originator) {
        case MINA_INTYG:
            return "/web/webjars/fk7263/minaintyg/js/module-deps.json";
        case WEBCERT:
            return "/web/webjars/fk7263/webcert/module-deps.json";
        default:
            return null;
        }
    }

    @Override
    public String getExternalId() {
        return MODULE_ID.toUpperCase();
    }
}
