package se.inera.certificate.modules.rli.support;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import se.inera.certificate.modules.rli.rest.ModuleService;
import se.inera.certificate.modules.support.ApplicationOrigin;
import se.inera.certificate.modules.support.ModuleEntryPoint;
import se.inera.certificate.modules.support.api.ModuleApi;

public class RliEntryPoint implements ModuleEntryPoint {

    @Autowired
    private ModuleService rliModuleService;

    @Override
    public String getModuleId() {
        return "rli";
    }

    @Override
    public String getModuleName() {
        return "Intyg vid avbeställd resa";
    }

    @Override
    public String getModuleDescription() {
        return "Intyg vid avbeställd resa";
    }

    @Override
    public String getDefaultRecieverLogicalAddress() {
        return "TS";
    }

    @Override
    public ModuleApi getModuleApi() {
        return rliModuleService;
    }

    @Override
    public boolean isModuleFragaSvarAvailable() {
        return false;
    }

    @Override
    public String getModuleCssPath(ApplicationOrigin originator) {
        switch (originator) {
        case MINA_INTYG:
            return "/intyg/css/rli.css";
        case WEBCERT:
            return "/web/webjars/rli/webcert/css/rli.css";
        default:
        }
        return null;
    }

    @Override
    public String getModuleScriptPath(ApplicationOrigin originator) {
        switch (originator) {
        case MINA_INTYG:
            return "/intyg/js/module";
        case WEBCERT:
            return "/web/webjars/rli/webcert/js/module";
        default:
            return null;
        }
    }

    @Override
    public String getModuleDependencyDefinitionPath(ApplicationOrigin originator) {
        switch (originator) {
        case MINA_INTYG:
            return null;
        case WEBCERT:
            return "/web/webjars/rli/webcert/js/module-deps.json";
        default:
            return null;
        }
    }

    @Override
    public Map<String, Boolean> getModuleFeatures() {
        // TODO Auto-generated method stub
        return null;
    }
}
