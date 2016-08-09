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

import static org.junit.Assert.assertEquals;
import static se.inera.intyg.intygstyper.ts_parent.json.EnumSetSerializerDeserializerTest.TestEnum.ONE;
import static se.inera.intyg.intygstyper.ts_parent.json.EnumSetSerializerDeserializerTest.TestEnum.THREE;
import static se.inera.intyg.intygstyper.ts_parent.json.EnumSetSerializerDeserializerTest.TestEnum.TWO;

import java.util.EnumSet;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class EnumSetSerializerDeserializerTest {

    /**
     * A simple enum for testing.
     */
    public static enum TestEnum {
        ONE, TWO, THREE
    };

    /**
     * A simple test class with an <code>EnumSet</code> of the <code>TestEnum</code>.
     */
    public static class TestClass {

        @JsonSerialize(using = EnumSetSerializer.class)
        @JsonDeserialize(using = EnumSetDeserializer.class)
        private final EnumSet<TestEnum> field;

        public TestClass() {
            field = EnumSet.noneOf(TestEnum.class);
        }

        public TestClass(TestEnum value) {
            field = EnumSet.of(value);
        }

        public TestClass(TestEnum value, TestEnum... values) {
            field = EnumSet.of(value, values);
        }

        // Implementing equals for easy test assertion.
        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            TestClass other = (TestClass) obj;
            if (field == null) {
                if (other.field != null) {
                    return false;
                }
            } else if (!field.equals(other.field)) {
                return false;
            }
            return true;
        }

        // Implementing toString for a meaningful assertion message.
        @Override
        public String toString() {
            return field.toString();
        }
    }

    /**
     * The expected JSON syntax.
     * <p>
     * <code>{"field":[{"type":"ONE","selected":true},{"type":"TWO","selected":false},...]}</code>
     */
    public static final String JSON_STRING = "{\"field\":[{\"type\":\"ONE\",\"selected\":%s},{\"type\":\"TWO\",\"selected\":%s},{\"type\":\"THREE\",\"selected\":%s}]}";

    /**
     * Test that EnumSets are serialized as expected.
     *
     * @throws Exception
     */
    @Test
    public void testEnumSetSerializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        {
            String result = mapper.writeValueAsString(new TestClass());
            assertEquals(String.format(JSON_STRING, false, false, false), result);
        }
        {
            String result = mapper.writeValueAsString(new TestClass(ONE));
            assertEquals(String.format(JSON_STRING, true, false, false), result);
        }
        {
            String result = mapper.writeValueAsString(new TestClass(TWO));
            assertEquals(String.format(JSON_STRING, false, true, false), result);
        }
        {
            String result = mapper.writeValueAsString(new TestClass(THREE));
            assertEquals(String.format(JSON_STRING, false, false, true), result);
        }
        {
            String result = mapper.writeValueAsString(new TestClass(ONE, TWO));
            assertEquals(String.format(JSON_STRING, true, true, false), result);
        }
        {
            String result = mapper.writeValueAsString(new TestClass(ONE, THREE));
            assertEquals(String.format(JSON_STRING, true, false, true), result);
        }
        {
            String result = mapper.writeValueAsString(new TestClass(TWO, THREE));
            assertEquals(String.format(JSON_STRING, false, true, true), result);
        }
        {
            String result = mapper.writeValueAsString(new TestClass(ONE, TWO, THREE));
            assertEquals(String.format(JSON_STRING, true, true, true), result);
        }
    }

    /**
     * Test that EnumSets are deserialized as expected.
     *
     * @throws Exception
     */
    @Test
    public void testEnumSetDeserializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        {
            TestClass result = mapper.readValue(String.format(JSON_STRING, false, false, false), TestClass.class);
            assertEquals(new TestClass(), result);
        }
        {
            TestClass result = mapper.readValue(String.format(JSON_STRING, true, false, false), TestClass.class);
            assertEquals(new TestClass(ONE), result);
        }
        {
            TestClass result = mapper.readValue(String.format(JSON_STRING, false, true, false), TestClass.class);
            assertEquals(new TestClass(TWO), result);
        }
        {
            TestClass result = mapper.readValue(String.format(JSON_STRING, false, false, true), TestClass.class);
            assertEquals(new TestClass(THREE), result);
        }
        {
            TestClass result = mapper.readValue(String.format(JSON_STRING, true, true, false), TestClass.class);
            assertEquals(new TestClass(ONE, TWO), result);
        }
        {
            TestClass result = mapper.readValue(String.format(JSON_STRING, true, false, true), TestClass.class);
            assertEquals(new TestClass(ONE, THREE), result);
        }
        {
            TestClass result = mapper.readValue(String.format(JSON_STRING, false, true, true), TestClass.class);
            assertEquals(new TestClass(TWO, THREE), result);
        }
        {
            TestClass result = mapper.readValue(String.format(JSON_STRING, true, true, true), TestClass.class);
            assertEquals(new TestClass(ONE, TWO, THREE), result);
        }
    }

    public static class EnumSetSerializer extends AbstractEnumSetSerializer<TestEnum> {
        protected EnumSetSerializer() {
            super(TestEnum.class);
        }
    }

    public static class EnumSetDeserializer extends AbstractEnumSetDeserializer<TestEnum> {
        protected EnumSetDeserializer() {
            super(TestEnum.class);
        }
    }
}
