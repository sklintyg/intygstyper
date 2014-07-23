package se.inera.certificate.modules.support.api.exception;

public class ModuleCopyNotSupportedException extends ModuleException {

    private static final long serialVersionUID = 1L;

    public ModuleCopyNotSupportedException(String message, Throwable cause) {
        super(message, cause);
    }

    public ModuleCopyNotSupportedException(String message) {
        super(message);
    }
}
