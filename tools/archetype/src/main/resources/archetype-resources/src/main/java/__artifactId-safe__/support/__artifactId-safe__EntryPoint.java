#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.${artifactId-safe}.support;

import org.springframework.beans.factory.annotation.Autowired;

import ${package}.support.ApplicationOrigin;
import ${package}.support.ModuleEntryPoint;
import ${package}.support.api.ModuleApi;
import ${package}.${artifactId-safe}.rest.ModuleService;

public class ${artifactId-safe}EntryPoint implements ModuleEntryPoint {

    // TODO: Change default logical address here
    private static final String DEFAULT_LOGICAL_ADRESS = "XX";

    @Autowired
    private ModuleService moduleService;

    @Override
    public String getModuleId() {
        return "${artifactId}";
    }

    @Override
    public String getModuleName() {
        // TODO: Change name of intyg here
        return "--- Name of intyg ---";
    }

    @Override
    public String getModuleDescription() {
        // TODO: Change desciption of intyg here
        return "---Description of intyg ---";
    }

    @Override
    public String getDefaultRecieverLogicalAddress() {
        return DEFAULT_LOGICAL_ADRESS;
    }

    @Override
    public ModuleApi getModuleApi() {
        return moduleService;
    }

    @Override
    public boolean isModuleFragaSvarAvailable() {
        // TODO: Change if intyg has fråga/svar or not here 
        return false;
    }

    @Override
    public String getModuleCssPath(ApplicationOrigin originator) {
        switch (originator) {
        case MINA_INTYG:
            return "/minaintyg/css/${artifactId}.css";
        case WEBCERT:
            return "/web/webjars/${artifactId}/webcert/css/${artifactId}.css";
        default:
            return null;
        }
    }

    @Override
    public String getModuleScriptPath(ApplicationOrigin originator) {
        switch (originator) {
        case MINA_INTYG:
            return "/minaintyg/js/module";
        case WEBCERT:
            return "/web/webjars/${artifactId}/webcert/js/module";
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
            return "/web/webjars/${artifactId}/webcert/js/module-deps.json";
        default:
            return null;
        }
    }
}
