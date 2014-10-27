package se.inera.certificate.modules.support;

import java.util.Map;
import java.util.Set;

import se.inera.certificate.modules.support.api.ModuleApi;
import se.inera.certificate.modules.support.feature.ModuleFeature;

/**
 * Defines the contract for modules so they can be discovered by an application.
 */
public interface ModuleEntryPoint {

    /**
     * Returns the unique id for the module. The id should only contain the a-z, 0-9 and '_' characters.
     *
     * @return A unique module id.
     */
    String getModuleId();

    /**
     * Returns the friendly name of the module, suitable for display in the GUI.
     */
    String getModuleName();

    /**
     * Returns description for the module, suitable for display in the GUI.
     */
    String getModuleDescription();

    /**
     * Returns the logical address of the default receiver of this module, or <code>null</code> if no default receiver
     * is specified.
     *
     * @return The logical address of the default receiver or <code>null</code> if none is specified.
     */
    String getDefaultRecieverLogicalAddress();

    /**
     * Returns the module specific implementation of the module API.
     *
     * @return A module API implementation.
     */
    ModuleApi getModuleApi();
    
    /**
     * Returns a Map containing which {@code se.inera.certificate.modules.support.feature.ModuleFeature} that this module will support and what state these have.
     * 
     * @return
     */
    Map<String, Boolean> getModuleFeatures();

    /**
     * Returns the module css path.
     *
     * @return The module css path for the calling application.
     * @path originator The calling application.
     */
    String getModuleCssPath(ApplicationOrigin originator);

    /**
     * Returns the module script path.
     *
     * @return The module script path for the calling application.
     * @path originator The calling application.
     */
    String getModuleScriptPath(ApplicationOrigin originator);

    /**
     * Returns the path to a resource containing the dependencies for the module.
     *
     * @return The path to the module dependency definition for the calling application.
     * @path originator The calling application.
     */
    String getModuleDependencyDefinitionPath(ApplicationOrigin originator);
}
