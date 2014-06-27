package se.inera.certificate.modules.ts_bas.support;

import org.springframework.beans.factory.annotation.Autowired;

import se.inera.certificate.modules.support.ApplicationOrigin;
import se.inera.certificate.modules.support.ModuleEntryPoint;
import se.inera.certificate.modules.support.api.ModuleApi;
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
    public String getModuleCssPath(ApplicationOrigin originator) {
        switch (originator) {
        case MINA_INTYG:
            return "/minaintyg/css/ts-bas.css";
        case WEBCERT:
            return "/webcert/css/ts-bas.css";
        default:
        }
        return null;
    }

    @Override
    public String getModuleScriptPath(ApplicationOrigin originator) {
        switch (originator) {
        case MINA_INTYG:
            return "/minaintyg/js/module";
        case WEBCERT:
            return "/webcert/js/module";
        default:
        }
        return null;
    }
}
