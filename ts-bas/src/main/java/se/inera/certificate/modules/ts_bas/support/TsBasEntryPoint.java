package se.inera.certificate.modules.ts_bas.support;

import org.springframework.stereotype.Component;

import se.inera.certificate.modules.support.ModuleEntryPoint;
import se.inera.certificate.modules.support.api.ModuleApi;
import se.inera.certificate.modules.ts_bas.rest.ModuleServiceWrapper;

@Component
public class TsBasEntryPoint implements ModuleEntryPoint {

    private final ModuleApi moduleApi;

    public TsBasEntryPoint() {
        this.moduleApi = new ModuleServiceWrapper();
    }

    @Override
    public String getModuleName() {
        return "ts-bas";
    }

    @Override
    public ModuleApi getModuleApi() {
        return moduleApi;
    }

    @Override
    public String getModuleScriptPath() {
        return "/js/module";
    }
}
