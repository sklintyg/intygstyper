package se.inera.certificate.modules.ts_bas.support;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import se.inera.certificate.modules.support.ApplicationOrigin;
import se.inera.certificate.modules.support.ModuleEntryPoint;
import se.inera.certificate.modules.support.api.ModuleApi;
import se.inera.certificate.modules.support.feature.ModuleFeaturesFactory;
import se.inera.certificate.modules.ts_bas.rest.TsBasModuleApi;

public class TsBasEntryPoint implements ModuleEntryPoint {

    private static final String TRANSPORTSTYRELSEN_LOGICAL_ADRESS = "TS";

    public static final String MODULE_ID = "ts-bas";
    public static final String MODULE_NAME = "Transportstyrelsens läkarintyg";
    public static final String MODULE_DESCRIPTION = "Läkarintyg - avseende högre körkortsbehörigheter eller taxiförarlegitimation - på begäran av Transportstyrelsen";

    @Autowired
    private TsBasModuleApi moduleService;

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
        return moduleService;
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
        return TRANSPORTSTYRELSEN_LOGICAL_ADRESS;
    }
}
