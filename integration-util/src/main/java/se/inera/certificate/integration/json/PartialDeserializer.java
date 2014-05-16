package se.inera.certificate.integration.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.joda.time.Partial;
import se.inera.certificate.schema.adapter.PartialAdapter;

import java.io.IOException;

import static com.fasterxml.jackson.core.JsonToken.VALUE_STRING;

/**
 * @author andreaskaltenbach
 */
public class PartialDeserializer extends StdDeserializer<Partial> {

    public PartialDeserializer() {
        super(Partial.class);
    }

    @Override
    public Partial deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException {

        if (jp.getCurrentToken() != VALUE_STRING) {
            throw ctxt.wrongTokenException(jp, VALUE_STRING, "expected JSON String");

        }

        return PartialAdapter.parsePartial(jp.getText().trim());
    }
}