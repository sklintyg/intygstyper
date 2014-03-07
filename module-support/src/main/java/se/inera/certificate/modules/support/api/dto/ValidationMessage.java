package se.inera.certificate.modules.support.api.dto;

import org.springframework.util.Assert;

public class ValidationMessage {

    private final String field;

    private final String message;

    public ValidationMessage(String field, String message) {
        Assert.hasText(field, "'field' must not be empty");
        Assert.hasText(message, "'message' must not be empty");
        this.field = field;
        this.message = message;
    }

    public String getField() {
        return field;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return field + ":" + message;
    }
}
