package se.inera.certificate.modules.sjukersattning.support;

import static se.inera.certificate.common.enumerations.Recipients.FK;

import org.springframework.beans.factory.annotation.Autowired;
import se.inera.certificate.modules.sjukersattning.rest.SjukersattningModuleApi;
import se.inera.certificate.modules.support.ApplicationOrigin;
import se.inera.certificate.modules.support.ModuleEntryPoint;
import se.inera.certificate.modules.support.api.ModuleApi;
import se.inera.certificate.modules.support.feature.ModuleFeaturesFactory;

import java.util.Map;

public class SjukersattningEntryPoint implements ModuleEntryPoint {

    public static final String DEFAULT_RECIPIENT_ID = FK.toString();

    public static final String MODULE_ID = "sjukersattning";
    public static final String MODULE_NAME = "Läkarintyg, sjukersättning";
    public static final String MODULE_DESCRIPTION = "Läkarintyg enligt 3 kap, 8 § lagen (1962:381) om allmän försäkring";

    @Autowired
    private SjukersattningModuleApi moduleApi;

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
        return ModuleFeaturesFactory.getFeatures("sjukersattning-features.properties");
    }

    @Override
    public String getModuleCssPath(ApplicationOrigin originator) {
        switch (originator) {
        case MINA_INTYG:
            return "/web/webjars/sjukersattning/minaintyg/css/sjukersattning.css";
        case WEBCERT:
            return "/web/webjars/sjukersattning/webcert/css/sjukersattning.css";
        default:
            return null;
        }
    }

    @Override
    public String getModuleScriptPath(ApplicationOrigin originator) {
        switch (originator) {
        case MINA_INTYG:
            return "/web/webjars/sjukersattning/minaintyg/js/module";
        case WEBCERT:
            return "/web/webjars/sjukersattning/webcert/module";
        default:
            return null;
        }
    }

    @Override
    public String getModuleDependencyDefinitionPath(ApplicationOrigin originator) {
        switch (originator) {
        case MINA_INTYG:
            return "/web/webjars/sjukersattning/minaintyg/js/module-deps.json";
        case WEBCERT:
            return "/web/webjars/sjukersattning/webcert/module-deps.json";
        default:
            return null;
        }
    }
}
