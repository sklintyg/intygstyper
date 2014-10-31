package se.inera.certificate.modules.support.api.exception;

import java.util.Collections;
import java.util.List;

import se.inera.certificate.model.util.Strings;

public class ModuleValidationException extends ModuleException {

    private static final long serialVersionUID = -4808921021590102478L;

    private final List<String> validationEntries;

    public ModuleValidationException(List<String> validationEntries) {
        super(Strings.join(",", validationEntries));
        this.validationEntries = validationEntries;
    }

    public ModuleValidationException(List<String> validationEntries, String message, Throwable cause) {
        super(message, cause);
        this.validationEntries = validationEntries;
    }

    public ModuleValidationException(List<String> validationEntries, String message) {
        super(message);
        this.validationEntries = validationEntries;
    }

    public ModuleValidationException(List<String> validationEntries, Throwable cause) {
        super(Strings.join(",", validationEntries), cause);
        this.validationEntries = validationEntries;
    }

    public List<String> getValidationEntries() {
        return Collections.unmodifiableList(validationEntries);
    }
}
