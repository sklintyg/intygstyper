package se.inera.certificate.modules.rli.support;

import org.springframework.beans.factory.annotation.Autowired;

import se.inera.certificate.modules.rli.rest.ModuleService;
import se.inera.certificate.modules.support.ModuleEntryPoint;
import se.inera.certificate.modules.support.api.ModuleApi;

public class RliEntryPoint implements ModuleEntryPoint {

    @Autowired
    private ModuleService rliModuleService;

    @Override
    public String getModuleId() {
        return "ts-diabetes";
    }

    @Override
    public String getModuleName() {
        return "Transportstyrelsens läkarintyg, diabetes";
    }

    @Override
    public String getModuleDescription() {
        return "Läkarintyg diabetes avseende lämpligheten att inneha körkort m.m.";
    }

    @Override
    public String getDefaultRecieverLogicalAddress() {
        return null;
    }

    @Override
    public ModuleApi getModuleApi() {
        return rliModuleService;
    }

    @Override
    public String getModuleCssPath() {
        return "/webcert/css/rli.css";
    }

    @Override
    public String getModuleScriptPath() {
        return "/webcert/js/module";
    }
}
