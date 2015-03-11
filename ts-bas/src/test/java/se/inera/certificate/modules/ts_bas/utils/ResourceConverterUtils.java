package se.inera.certificate.modules.ts_bas.utils;

import java.io.File;
import java.io.IOException;

import javax.xml.bind.JAXB;

import se.inera.certificate.integration.json.CustomObjectMapper;
import se.inera.certificate.modules.ts_bas.model.internal.Utlatande;
import se.inera.intygstjanster.ts.services.v1.TSBasIntyg;

import com.fasterxml.jackson.databind.ObjectMapper;

public final class ResourceConverterUtils {

    private ResourceConverterUtils() {
    }

    private static final ObjectMapper OBJECT_MAPPER = new CustomObjectMapper();

    public static TSBasIntyg toTransport(File resource) throws IOException {
        return JAXB.unmarshal(resource, TSBasIntyg.class);
    }

    public static Utlatande toInternal(File resource)
            throws IOException {
        return OBJECT_MAPPER.readValue(resource, Utlatande.class);
    }

    public static Utlatande toInternal(String resource)
            throws IOException {
        return OBJECT_MAPPER.readValue(resource, Utlatande.class);
    }
}
