package se.inera.certificate.integration.json;

import java.io.IOException;

import se.inera.certificate.model.InternalDate;
import se.inera.certificate.schema.adapter.InternalDateAdapter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class InternalDateSerializer extends StdSerializer<InternalDate> {

    public InternalDateSerializer() {
        super(InternalDate.class);
    }

    @Override
    public void serialize(InternalDate date, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeString(InternalDateAdapter.printInternalDate(date));
    }
}
