package se.inera.certificate.modules.ts_bas.support;

import org.springframework.beans.factory.annotation.Autowired;

import se.inera.certificate.modules.support.ModuleEntryPoint;
import se.inera.certificate.modules.support.api.ModuleApi;
import se.inera.certificate.modules.ts_bas.rest.ModuleServiceWrapper;

public class TsBasEntryPoint implements ModuleEntryPoint {

    @Autowired
    private ModuleServiceWrapper moduleServiceWrapper;

    @Override
    public String getModuleId() {
        return "ts-bas";
    }

    @Override
    public String getModuleName() {
        return "Transportstyrelsens läkarintyg, bas";
    }

    @Override
    public String getModuleDescription() {
        // TODO
        return "Läkarintyg - avseende högre körkortsberhörigheter eller taxiförarlegitimation - på begäran från Transportstyrelsen";
    }

    @Override
    public ModuleApi getModuleApi() {
        return moduleServiceWrapper;
    }

    @Override
    public String getModuleScriptPath() {
        return "/webcert/js/module";
    }
}
