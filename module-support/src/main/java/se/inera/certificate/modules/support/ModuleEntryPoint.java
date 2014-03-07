package se.inera.certificate.modules.support;

import org.springframework.stereotype.Component;
import se.inera.certificate.modules.support.api.ModuleApi;

/**
 * Defines the contract for modules so they can be discovered by an application.
 */
@Component
public interface ModuleEntryPoint {

    /**
     * Returns the unique name of the module. The name should only contain the a-z, 0-9 and '_' characters.
     * 
     * @return A unique module name.
     */
    String getModuleName();

    /**
     * Returns the module specific implementation of the module API.
     * 
     * @return A module API implementation.
     */
    ModuleApi getModuleApi();

    /**
     * Returns the module script path.
     * 
     * @return The module script path.
     */
    String getModuleScriptPath();
}
