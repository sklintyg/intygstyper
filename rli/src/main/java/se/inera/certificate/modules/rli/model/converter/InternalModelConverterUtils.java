package se.inera.certificate.modules.rli.model.converter;

import se.inera.certificate.model.Id;
import se.inera.certificate.model.Kod;

public final class InternalModelConverterUtils {

    private InternalModelConverterUtils() {

    }

    public static String getValueFromKod(Kod kod) {
        return (kod != null) ? kod.getCode() : null;
    }

    public static String getExtensionFromId(Id id) {
        return (id != null) ? id.getExtension() : null;
    }

    public static String getRootFromId(Id id) {
        return (id != null) ? id.getRoot() : null;
    }
}
