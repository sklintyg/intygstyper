package se.inera.certificate.modules.fk7263.utils;

/**
 * Thrown when an expected scenario wasn't found.
 */
public class ScenarioNotFoundException extends Exception {

    private static final long serialVersionUID = 2092187161098644931L;

    public ScenarioNotFoundException(String scenario, String model) {
        super(String.format("Could not find %s model scenario %s", model, scenario));
    }

    public ScenarioNotFoundException(String scenario, String model, Throwable cause) {
        super(String.format("Could not find %s model scenario %s", model, scenario), cause);
    }
}
