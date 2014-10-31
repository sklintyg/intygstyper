package se.inera.certificate.modules.support.api.exception;

public class ModuleConverterException extends ModuleException {

    private static final long serialVersionUID = 4055150962655732547L;

    public ModuleConverterException() {
        super();
    }

    public ModuleConverterException(String message, Throwable cause) {
        super(message, cause);
    }

    public ModuleConverterException(String message) {
        super(message);
    }

    public ModuleConverterException(Throwable cause) {
        super(cause);
    }
}
