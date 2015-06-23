package se.inera.certificate.modules.ts_diabetes.support;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import se.inera.certificate.modules.support.ApplicationOrigin;
import se.inera.certificate.modules.support.ModuleEntryPoint;
import se.inera.certificate.modules.support.api.ModuleApi;
import se.inera.certificate.modules.support.feature.ModuleFeaturesFactory;
import se.inera.certificate.modules.ts_diabetes.rest.TsDiabetesModuleApi;

public class TsDiabetesEntryPoint implements ModuleEntryPoint {

    private static final String TRANSPORTSTYRELSEN_LOGICAL_ADRESS = "TS";
    public static final String MODULE_ID = "ts-diabetes";

    @Autowired
    private TsDiabetesModuleApi tsDiabetesModuleService;

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

    /*@Override
    public String getDefaultRecieverLogicalAddress() {
        return TRANSPORTSTYRELSEN_LOGICAL_ADRESS;
    }*/

    @Override
    public ModuleApi getModuleApi() {
        return tsDiabetesModuleService;
    }

    @Override
    public Map<String, Boolean> getModuleFeatures() {
        return ModuleFeaturesFactory.getFeatures("ts-diabetes-features.properties");
    }

    @Override
    public String getModuleCssPath(ApplicationOrigin originator) {
        switch (originator) {
        case MINA_INTYG:
            return "/web/webjars/ts-diabetes/minaintyg/css/ts-diabetes.css";
        case WEBCERT:
            return "/web/webjars/ts-diabetes/webcert/css/ts-diabetes.css";
        default:
            return null;
        }
    }

    @Override
    public String getModuleScriptPath(ApplicationOrigin originator) {
        switch (originator) {
        case MINA_INTYG:
            return "/web/webjars/ts-diabetes/minaintyg/js/module";
        case WEBCERT:
            return "/web/webjars/ts-diabetes/webcert/module";
        default:
            return null;
        }
    }

    @Override
    public String getModuleDependencyDefinitionPath(ApplicationOrigin originator) {
        switch (originator) {
        case MINA_INTYG:
            return "/web/webjars/ts-diabetes/minaintyg/js/module-deps.json";
        case WEBCERT:
            return "/web/webjars/ts-diabetes/webcert/module-deps.json";
        default:
            return null;
        }
    }

	@Override
	public String getDefaultRecipient() {
		return TRANSPORTSTYRELSEN_LOGICAL_ADRESS;
	}
}
