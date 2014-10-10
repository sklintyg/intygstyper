package se.inera.certificate.integration.json;

import static com.fasterxml.jackson.core.JsonToken.VALUE_STRING;

import java.io.IOException;

import se.inera.certificate.model.InternalDate;
import se.inera.certificate.schema.adapter.InternalDateAdapter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class InternalDateDeserializer extends StdDeserializer<InternalDate> {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public InternalDateDeserializer() {
        super(InternalDate.class);
    }

    @Override
    public InternalDate deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException {

        if (jp.getCurrentToken() != VALUE_STRING) {
            throw ctxt.wrongTokenException(jp, VALUE_STRING, "expected JSON String");

        }

        return InternalDateAdapter.parseInternalDate(jp.getText().trim());
    }
}
