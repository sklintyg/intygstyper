package se.inera.intyg.intygstyper.fk7263.utils;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import se.inera.intyg.intygstyper.fk7263.model.converter.EmptyStringUtil;

public class EmptyStringUtilsTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testEscapeWhitespace() {
        assertEquals(String.format("Input %s did not match expected escaped output %s", "%32%32", "  ")
                , "%32%32", EmptyStringUtil.escapeEmptyString("  "));

        assertEquals(String.format("Input %s did not match expected escaped output %s", "%32%32", "  ")
                , "%32", EmptyStringUtil.escapeEmptyString(" "));

        assertEquals(String.format("Input %s did not match expected escaped output %s", "%9%10", "\t\n")
                , "%09%10", EmptyStringUtil.escapeEmptyString("\t\n"));

    }

    @Test
    public void testUnescapeWhitespace() {
        assertEquals(String.format("Output %s did not match expected unescaped string %s", "  ", "%32%32"),
                "  ", EmptyStringUtil.unescapeEmptyString("%32%32"));

        assertEquals(String.format("Input %s did not match expected escaped output %s", " ", "%32")
                , " ", EmptyStringUtil.unescapeEmptyString("%32"));

        assertEquals(String.format("Input %s did not match expected escaped output %s", "\t\n", "%09%10")
                , "\t\n", EmptyStringUtil.unescapeEmptyString("%09%10"));
    }

    @Test
    public void testEscapeThenUnescapeWhitespace() {
        String original = "\t\n";
        String escaped = EmptyStringUtil.escapeEmptyString(original);
        assertEquals("Escaped and unescaped did not match original string", original, EmptyStringUtil.unescapeEmptyString(escaped));
    }

    @Test
    public void testEscapeNonEmptyString() {
        String string = "Awesome test string";
        assertEquals(string, EmptyStringUtil.escapeEmptyString(string));
    }

    @Test
    public void testUnescapeNonEmptyString() {
        String string = "Awesome test string";
        assertEquals(string, EmptyStringUtil.unescapeEmptyString(string));
    }

}
