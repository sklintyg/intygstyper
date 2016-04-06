package se.inera.certificate.modules.aktivitetsersattning.support;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import se.inera.certificate.modules.sjukersattning.rest.AktivitetsersattningNAModuleApi;
import se.inera.intyg.common.support.modules.support.ApplicationOrigin;
import se.inera.intyg.common.support.modules.support.ModuleEntryPoint;
import se.inera.intyg.common.support.modules.support.api.ModuleApi;
import se.inera.intyg.common.support.modules.support.feature.ModuleFeaturesFactory;

public class AktivitetsersattningEntryPoint implements ModuleEntryPoint {
    public static final String DEFAULT_RECIPIENT_ID = "FK";
    
    public static final String MODULE_ID = "luae_na";
    public static final String MODULE_NAME = "Läkarutlåtande för aktivitetsersättning";
    public static final String MODULE_DESCRIPTION = "Läkarintyg enligt 3 kap, 8 § lagen (1962:381) om allmän försäkring";
    
    @Autowired
    private AktivitetsersattningNAModuleApi moduleApi;

    public String getModuleId() {
        return MODULE_ID;
    }

    public String getModuleName() {
        return MODULE_NAME;
    }

    public String getModuleDescription() {
        return MODULE_DESCRIPTION;
    }

    public String getDefaultRecipient() {
        return DEFAULT_RECIPIENT_ID;
    }

    public ModuleApi getModuleApi() {
        return moduleApi;
    }

    public Map<String, Boolean> getModuleFeatures() {
        return ModuleFeaturesFactory.getFeatures("aktivitetsersattning-na.properties");
    }

    public String getModuleCssPath(ApplicationOrigin originator) {
        return null;
//        switch (originator) {
//        case MINA_INTYG:
//            return "/web/webjars/luae_na/minaintyg/css/aktivitetsersattning-na.css";
//        case WEBCERT:
//            return "/web/webjars/luae_na/webcert/css/aktivitetsersattning-na.css";
//        default:
//            return null;
//        }
    }

    public String getModuleScriptPath(ApplicationOrigin originator) {
        return null;
//        switch (originator) {
//        case MINA_INTYG:
//            return "/web/webjars/luae_na/minaintyg/js/module";
//        case WEBCERT:
//            return "/web/webjars/luae_na/webcert/module";
//        default:
//            return null;
//        }
    }

    public String getModuleDependencyDefinitionPath(ApplicationOrigin originator) {
        return null;
//        switch (originator) {
//        case MINA_INTYG:
//            return "/web/webjars/luae_na/minaintyg/js/module-deps.json";
//        case WEBCERT:
//            return "/web/webjars/luae_na/webcert/module-deps.json";
//        default:
//            return null;
//        }
    }

}
