package se.inera.certificate.modules.ts_diabetes.support;

import org.springframework.beans.factory.annotation.Autowired;

import se.inera.certificate.modules.support.ModuleEntryPoint;
import se.inera.certificate.modules.support.api.ModuleApi;
import se.inera.certificate.modules.ts_diabetes.rest.ModuleService;

public class TsDiabetesEntryPoint implements ModuleEntryPoint {

    @Autowired
    private ModuleService tsDiabetesModuleService;

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
        return tsDiabetesModuleService;
    }

    @Override
    public String getModuleCssPath() {
        return "/webcert/css/ts-diabetes.css";
    }

    @Override
    public String getModuleScriptPath() {
        return "/webcert/js/module";
    }
}
