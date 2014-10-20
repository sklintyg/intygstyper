package se.inera.certificate.modules.ts_bas.support;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import se.inera.certificate.modules.support.ApplicationOrigin;
import se.inera.certificate.modules.support.ModuleEntryPoint;
import se.inera.certificate.modules.support.api.ModuleApi;
import se.inera.certificate.modules.support.feature.ModuleFeaturesFactory;
import se.inera.certificate.modules.ts_bas.rest.ModuleService;

public class TsBasEntryPoint implements ModuleEntryPoint {

    private static final String TRANSPORTSTYRELSEN_LOGICAL_ADRESS = "TS";

    @Autowired
    private ModuleService moduleService;

    @Override
    public String getModuleId() {
        return "ts-bas";
    }

    @Override
    public String getModuleName() {
        return "Transportstyrelsens läkarintyg";
    }

    @Override
    public String getModuleDescription() {
        // TODO
        return "Läkarintyg - avseende högre körkortsberhörigheter eller taxiförarlegitimation - på begäran från Transportstyrelsen";
    }

    @Override
    public String getDefaultRecieverLogicalAddress() {
        return TRANSPORTSTYRELSEN_LOGICAL_ADRESS;
    }

    @Override
    public ModuleApi getModuleApi() {
        return moduleService;
    }

    @Override
    public boolean isModuleFragaSvarAvailable() {
        return false;
    }

    @Override
    public Map<String, Boolean> getModuleFeatures() {
        return ModuleFeaturesFactory.getFeatures("ts-bas-features.properties");
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
            return "/web/webjars/ts-bas/webcert/js/module";
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
            return "/web/webjars/ts-bas/webcert/js/module-deps.json";
        default:
            return null;
        }
    }
}
