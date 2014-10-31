package se.inera.certificate.modules.support.api.exception;

public class ModuleVersionUnsupportedException extends ModuleException {

    private static final long serialVersionUID = -1877787826921083411L;

    public ModuleVersionUnsupportedException() {
        super();
    }

    public ModuleVersionUnsupportedException(String message, Throwable cause) {
        super(message, cause);
    }

    public ModuleVersionUnsupportedException(String message) {
        super(message);
    }

    public ModuleVersionUnsupportedException(Throwable cause) {
        super(cause);
    }
}
