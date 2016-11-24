/*
 * Copyright (C) 2016 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package se.inera.intyg.intygstyper.ts_parent.json;

import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

/**
 * Helper class that instructs Jackson to parse JSON like below to {@link EnumSet}.
 * <p/>
 * <code>[{type: "ENUM1", selected : true}, {type: "ENUM2", selected : false}, {type: "ENUM3", selected : false}]</code>
 * <p/>
 * for a EnumSet containing <code>ENUM1</code> out of an enum with the enum values <code>ENUM1</code>,
 * <code>ENUM2</code> and <code>ENUM3</code>
 * <p/>
 * A concrete class must be created for the specific enum type to use:
 * <p/>
 * <pre>
 * public static class EnumTypeEnumSetDeserializer extends AbstractEnumSetDeserializer&lt;EnumType&gt; {
 *     protected EnumTypeEnumSetDeserializer() {
 *         super(EnumType.class);
 *     }
 * }
 * </pre>
 * <p/>
 * Annotate your EnumSet with:
 * <p/>
 * <code>@JsonDeserialize(using = EnumTypeEnumSetDeserializer.class)</code>
 *
 * @param <E> An enum type.
 */
public abstract class AbstractEnumSetDeserializer<E extends Enum<E>> extends JsonDeserializer<EnumSet<E>> {

    private final Class<E> enumType;

    private final Map<String, E> enums;

    protected AbstractEnumSetDeserializer(Class<E> enumType) {
        this.enumType = enumType;
        this.enums = new HashMap<>();
        for (E enumValue : enumType.getEnumConstants()) {
            this.enums.put(enumValue.name(), enumValue);
        }
    }

    @Override
    public EnumSet<E> deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        EnumSet<E> enumSet = EnumSet.noneOf(enumType);

        while (jp.nextToken() != JsonToken.END_ARRAY) {
            String enumName = null;
            Boolean enumIsSet = null;
            while (jp.nextToken() != JsonToken.END_OBJECT) {
                String field = jp.getCurrentName();
                jp.nextToken();
                if ("type".equals(field)) {
                    enumName = jp.getValueAsString();
                } else if ("selected".equals(field)) {
                    enumIsSet = jp.getValueAsBoolean();
                }
            }
            if (enumName != null && enumIsSet != null) {
                if (enumIsSet) {
                    enumSet.add(enums.get(enumName));
                }
            }
        }

        return enumSet;
    }
}
