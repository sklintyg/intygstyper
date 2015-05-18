package se.inera.certificate.modules.fk7263.support;

import static se.inera.certificate.common.enumerations.Recipients.FK;

import org.springframework.beans.factory.annotation.Autowired;
import se.inera.certificate.modules.fk7263.rest.Fk7263ModuleApi;
import se.inera.certificate.modules.support.ApplicationOrigin;
import se.inera.certificate.modules.support.ModuleEntryPoint;
import se.inera.certificate.modules.support.api.ModuleApi;
import se.inera.certificate.modules.support.feature.ModuleFeaturesFactory;

import java.util.Map;

public class Fk7263EntryPoint implements ModuleEntryPoint {

    public static final String DEFAULT_RECIPIENT_ID = FK.toString();

    public static final String MODULE_ID = "fk7263";
    public static final String MODULE_NAME = "Läkarintyg FK 7263";
    public static final String MODULE_DESCRIPTION = "Läkarintyg enligt 3 kap, 8 § lagen (1962:381) om allmän försäkring";

    @Autowired
    private Fk7263ModuleApi moduleApi;

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
        return ModuleFeaturesFactory.getFeatures("fk7263-features.properties");
    }

    @Override
    public String getModuleCssPath(ApplicationOrigin originator) {
        switch (originator) {
        case MINA_INTYG:
            return "/web/webjars/fk7263/minaintyg/css/fk7263.css";
        case WEBCERT:
            return "/web/webjars/fk7263/webcert/css/fk7263.css";
        default:
            return null;
        }
    }

    @Override
    public String getModuleScriptPath(ApplicationOrigin originator) {
        switch (originator) {
        case MINA_INTYG:
            return "/web/webjars/fk7263/minaintyg/js/module";
        case WEBCERT:
            return "/web/webjars/fk7263/webcert/js/module";
        default:
            return null;
        }
    }

    @Override
    public String getModuleDependencyDefinitionPath(ApplicationOrigin originator) {
        switch (originator) {
        case MINA_INTYG:
            return "/web/webjars/fk7263/minaintyg/js/module-deps.json";
        case WEBCERT:
            return "/web/webjars/fk7263/webcert/js/module-deps.json";
        default:
            return null;
        }
    }
}
