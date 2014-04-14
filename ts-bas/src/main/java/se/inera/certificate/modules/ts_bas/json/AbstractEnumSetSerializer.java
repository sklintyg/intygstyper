package se.inera.certificate.modules.ts_bas.json;

import java.io.IOException;
import java.util.EnumSet;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * Helper class that instructs Jackson to parse {@link EnumSet}s to JSON as follows.
 * <p/>
 * <code>[{type: "ENUM1", selected : true}, {type: "ENUM2", selected : false}, {type: "ENUM3", selected : false}]</code>
 * <p/>
 * for a EnumSet containing <code>ENUM1</code> out of an enum with the enum values <code>ENUM1</code>,
 * <code>ENUM2</code> and <code>ENUM3</code>
 * <p/>
 * A concrete class must be created for the specific enum type to use:
 * <p/>
 * <pre>
 * public static class EnumTypeEnumSetSerializer extends AbstractEnumSetSerializer&lt;EnumType&gt; {
 *     protected EnumTypeEnumSetSerializer() {
 *         super(EnumType.class);
 *     }
 * }
 * </pre>
 * <p/>
 * Annotate your EnumSet with:
 * <p/>
 * <code>@JsonSerialize(using = EnumTypeEnumSetSerializer.class)</code>
 *
 * @param <E> An enum type.
 * @see com.fasterxml.jackson.databind.annotation.JsonSerialize
 */
public class AbstractEnumSetSerializer<E extends Enum<E>> extends JsonSerializer<EnumSet<E>> {

    private final Class<E> enumType;

    protected AbstractEnumSetSerializer(Class<E> enumType) {
        this.enumType = enumType;
    }

    @Override
    public void serialize(EnumSet<E> enumSet, JsonGenerator jgen, SerializerProvider provider) throws IOException {

        jgen.writeStartArray();
        for (E enumValue : enumType.getEnumConstants()) {
            jgen.writeStartObject();
            jgen.writeStringField("type", enumValue.name());
            jgen.writeBooleanField("selected", enumSet.contains(enumValue));
            jgen.writeEndObject();
        }
        jgen.writeEndArray();
    }
}
