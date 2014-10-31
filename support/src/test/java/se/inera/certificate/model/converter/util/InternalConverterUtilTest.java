package se.inera.certificate.model.converter.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static se.inera.certificate.model.converter.util.InternalConverterUtil.detectIfSamordningsNummer;

import org.junit.Test;

public class InternalConverterUtilTest {

    @Test(expected = RuntimeException.class)
    public void testWithBlankString() {
        String in = "";
        detectIfSamordningsNummer(in);
    }

    @Test(expected = RuntimeException.class)
    public void testWithTooShortInput() {
        String in = "197001";
        detectIfSamordningsNummer(in);
    }

    @Test(expected = RuntimeException.class)
    public void testWithGarbageInput() {
        String in = "BlaHongaBla";
        detectIfSamordningsNummer(in);
    }

    @Test
    public void testWithPersonnummerHigh() {
        String in = "19701231-1234";
        assertFalse(detectIfSamordningsNummer(in));
    }

    @Test
    public void testWithPersonnummerLow() {
        String in = "19700101-1234";
        assertFalse(detectIfSamordningsNummer(in));
    }

    @Test
    public void testWithSamordningsnummerHigh() {
        String in = "19701291-1234";
        assertTrue(detectIfSamordningsNummer(in));
    }

    @Test
    public void testWithSamordningsnummerLow() {
        String in = "19701261-1234";
        assertTrue(detectIfSamordningsNummer(in));
    }
}
