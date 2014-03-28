package se.inera.certificate.modules.fk7263.support;

import org.springframework.beans.factory.annotation.Autowired;

import se.inera.certificate.modules.fk7263.rest.Fk7263ModuleApi;
import se.inera.certificate.modules.support.ModuleEntryPoint;
import se.inera.certificate.modules.support.api.ModuleApi;

public class Fk7263EntryPoint implements ModuleEntryPoint {

    private static final String FORSAKRINGSKASSAN_LOGICAL_ADDRESS = "FK";

    @Autowired
    private Fk7263ModuleApi moduleApi;

    @Override
    public String getModuleId() {
        return "fk7263";
    }

    @Override
    public String getModuleName() {
        return "Läkarintyg FK 7263";
    }

    @Override
    public String getModuleDescription() {
        // TODO
        return "Läkarintyg enligt 3 kap, 8 § lagen (1962:381) om allmän försäkring";
    }

    @Override
    public String getDefaultRecieverLogicalAddress() {
        return FORSAKRINGSKASSAN_LOGICAL_ADDRESS;
    }

    @Override
    public ModuleApi getModuleApi() {
        return moduleApi;
    }

    @Override
    public String getModuleScriptPath() {
        return "/webcert/js/module";
    }
}
