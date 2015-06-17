package se.inera.certificate.modules.sjukpenning.support;

import static se.inera.certificate.common.enumerations.Recipients.FK;

import org.springframework.beans.factory.annotation.Autowired;
import se.inera.certificate.modules.sjukpenning.rest.SjukpenningModuleApi;
import se.inera.certificate.modules.support.ApplicationOrigin;
import se.inera.certificate.modules.support.ModuleEntryPoint;
import se.inera.certificate.modules.support.api.ModuleApi;
import se.inera.certificate.modules.support.feature.ModuleFeaturesFactory;

import java.util.Map;

public class SjukpenningEntryPoint implements ModuleEntryPoint {

    public static final String DEFAULT_RECIPIENT_ID = FK.toString();

    public static final String MODULE_ID = "sjukpenning";
    public static final String MODULE_NAME = "Läkarintyg";
    public static final String MODULE_DESCRIPTION = "Läkarintyg enligt 3 kap, 8 § lagen (1962:381) om allmän försäkring";

    @Autowired
    private SjukpenningModuleApi moduleApi;

    @Override
    public String getDefaultRecipient() {
        return DEFAULT_RECIPIENT_ID;
    }

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
        return moduleApi;
    }

    @Override
    public Map<String, Boolean> getModuleFeatures() {
        return ModuleFeaturesFactory.getFeatures("sjukpenning-features.properties");
    }

    @Override
    public String getModuleCssPath(ApplicationOrigin originator) {
        switch (originator) {
        case MINA_INTYG:
            return "/web/webjars/sjukpenning/minaintyg/css/sjukpenning.css";
        case WEBCERT:
            return "/web/webjars/sjukpenning/webcert/css/sjukpenning.css";
        default:
            return null;
        }
    }

    @Override
    public String getModuleScriptPath(ApplicationOrigin originator) {
        switch (originator) {
        case MINA_INTYG:
            return "/web/webjars/sjukpenning/minaintyg/js/module";
        case WEBCERT:
            return "/web/webjars/sjukpenning/webcert/module";
        default:
            return null;
        }
    }

    @Override
    public String getModuleDependencyDefinitionPath(ApplicationOrigin originator) {
        switch (originator) {
        case MINA_INTYG:
            return "/web/webjars/sjukpenning/minaintyg/js/module-deps.json";
        case WEBCERT:
            return "/web/webjars/sjukpenning/webcert/module-deps.json";
        default:
            return null;
        }
    }
}
