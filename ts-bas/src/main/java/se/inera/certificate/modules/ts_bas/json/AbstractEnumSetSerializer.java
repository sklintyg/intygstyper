package se.inera.certificate.modules.ts_bas.json;

import java.io.IOException;
import java.util.EnumSet;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Helper class that instructs Jackson to parse {@link EnumSet}s to JSON like:
 * <p>
 * <code>{ENUM1 : true, ENUM2 : false, ENUM3 : false}</code>
 * <p>
 * for a EnumSet containing <code>ENUM1</code> out of an enum with the enum values <code>ENUM1</code>,
 * <code>ENUM2</code> and <code>ENUM3</code>
 * 
 * A concrete class must be created for the specific enum type to use:
 * 
 * <pre>
 * public static class EnumTypeEnumSetSerializer extends AbstractEnumSetSerializer&lt;EnumType&gt; {
 *     protected EnumTypeEnumSetSerializer() {
 *         super(EnumType.class);
 *     }
 * }
 * </pre>
 * 
 * Annotate your EnumSet with:
 * <p>
 * <code>@JsonSerialize(using = EnumTypeEnumSetSerializer.class)</code>
 * 
 * @see JsonSerialize
 * @param <E>
 *            An enum type.
 */
public class AbstractEnumSetSerializer<E extends Enum<E>> extends JsonSerializer<EnumSet<E>> {

    private final Class<E> enumType;

    protected AbstractEnumSetSerializer(Class<E> enumType) {
        this.enumType = enumType;
    }

    @Override
    public void serialize(EnumSet<E> enumSet, JsonGenerator jgen, SerializerProvider provider) throws IOException,
            JsonProcessingException {
        jgen.writeStartObject();
        for (E enumValue : enumType.getEnumConstants()) {
            jgen.writeBooleanField(enumValue.name(), enumSet.contains(enumValue));
        }
        jgen.writeEndObject();
    }
}
