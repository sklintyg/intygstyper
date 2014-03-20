package ${package}.${artifactId-safe}.utils;

import junit.framework.AssertionFailedError;

import org.unitils.reflectionassert.ReflectionAssert;
import org.unitils.reflectionassert.ReflectionComparatorMode;

public class ModelAssert {

    public static void assertEquals(Object expected, Object actual) throws AssertionError {
        try {
            ReflectionAssert.assertReflectionEquals(expected, actual, ReflectionComparatorMode.LENIENT_ORDER);
        } catch (AssertionFailedError e) {
            throw new AssertionError(e.getMessage(), e);
        }
    }

    public static void assertEquals(String message, Object expected, Object actual) throws AssertionError {
        try {
            ReflectionAssert.assertReflectionEquals(message, expected, actual, ReflectionComparatorMode.LENIENT_ORDER);
        } catch (AssertionFailedError e) {
            throw new AssertionError(e.getMessage(), e);
        }
    }
}
