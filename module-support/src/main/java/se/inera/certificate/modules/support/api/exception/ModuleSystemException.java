package se.inera.certificate.modules.support.api.exception;

public class ModuleSystemException extends ModuleException {

    private static final long serialVersionUID = -2598625429265609018L;

    public ModuleSystemException() {
    }

    public ModuleSystemException(String message, Throwable cause) {
        super(message, cause);
    }

    public ModuleSystemException(String message) {
        super(message);
    }

    public ModuleSystemException(Throwable cause) {
        super(cause);
    }
}
