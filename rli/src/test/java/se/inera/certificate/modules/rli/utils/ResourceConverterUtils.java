package se.inera.certificate.modules.rli.utils;

import java.io.File;
import java.io.IOException;

import javax.xml.bind.JAXB;

import se.inera.certificate.integration.json.CustomObjectMapper;
import se.inera.certificate.rli.model.v1.Utlatande;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ResourceConverterUtils {

    private static final ObjectMapper OBJECT_MAPPER = new CustomObjectMapper();

    public static Utlatande toTransport(File resource) throws IOException {
        return JAXB.unmarshal(resource, Utlatande.class);
    }

    public static se.inera.certificate.modules.rli.model.external.Utlatande toExternal(File resource)
            throws IOException {
        return OBJECT_MAPPER.readValue(resource, se.inera.certificate.modules.rli.model.external.Utlatande.class);
    }

    public static se.inera.certificate.modules.rli.model.internal.mi.Utlatande toInternalMI(File resource)
            throws IOException {
        return OBJECT_MAPPER.readValue(resource, se.inera.certificate.modules.rli.model.internal.mi.Utlatande.class);
    }

    public static se.inera.certificate.modules.rli.model.internal.wc.Utlatande toInternalWC(File resource)
            throws IOException {
        return OBJECT_MAPPER.readValue(resource, se.inera.certificate.modules.rli.model.internal.wc.Utlatande.class);
    }
}
