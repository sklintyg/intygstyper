package se.inera.certificate.modules.rli.model.converter;

/**
 * Exception for converting services.
 * 
 * @author nikpet
 * 
 */
public class ConverterException extends Exception {

    private static final long serialVersionUID = 1L;

    public ConverterException() {
        super();
    }

    public ConverterException(String message) {
        super(message);
    }

    public ConverterException(String message, Throwable cause) {
        super(message, cause);
    }

}
