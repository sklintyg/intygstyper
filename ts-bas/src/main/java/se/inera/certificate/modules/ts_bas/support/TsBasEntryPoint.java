package se.inera.certificate.modules.ts_bas.support;

import org.springframework.beans.factory.annotation.Autowired;

import se.inera.certificate.modules.support.ModuleEntryPoint;
import se.inera.certificate.modules.support.api.ModuleApi;
import se.inera.certificate.modules.ts_bas.rest.ModuleServiceWrapper;

public class TsBasEntryPoint implements ModuleEntryPoint {

    @Autowired
    private final ModuleServiceWrapper moduleServiceWrapper;

    public TsBasEntryPoint() {
        this.moduleServiceWrapper = new ModuleServiceWrapper();
    }

    @Override
    public String getModuleName() {
        return "ts-bas";
    }

    @Override
    public ModuleApi getModuleApi() {
        return moduleServiceWrapper;
    }

    @Override
    public String getModuleScriptPath() {
        return "/js/module";
    }
}
