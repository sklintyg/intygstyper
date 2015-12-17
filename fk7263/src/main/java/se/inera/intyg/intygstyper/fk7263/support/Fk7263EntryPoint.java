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
    public ModuleApi getModuleApi() {
        return moduleApi;
    }

    @Override
    public Map<String, Boolean> getModuleFeatures() {
        return ModuleFeaturesFactory.getFeatures("fk7263-features.properties");
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
}