package se.inera.certificate.modules.support.api.dto;

public class ValidationMessage {

    private String field;

    private String message;

    public ValidationMessage() {

    }

    public ValidationMessage(String field, String message) {
        this.field = field;
        this.message = message;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return field + ":" + message;
    }
}
