package se.inera.certificate.modules.fk7263.utils;

import java.io.File;
import java.io.IOException;

import javax.xml.bind.JAXB;

import se.inera.certificate.fk7263.insuranceprocess.healthreporting.registermedicalcertificate.v3.RegisterMedicalCertificate;
import se.inera.certificate.integration.json.CustomObjectMapper;

import com.fasterxml.jackson.databind.ObjectMapper;

public final class ResourceConverterUtils {

    private ResourceConverterUtils() {
    }

    private static final ObjectMapper OBJECT_MAPPER = new CustomObjectMapper();

    public static RegisterMedicalCertificate toTransport(File resource) throws IOException {
        return JAXB.unmarshal(resource, RegisterMedicalCertificate.class);
    }

    public static se.inera.certificate.modules.fk7263.model.internal.Utlatande toInternal(File resource)
            throws IOException {
        return OBJECT_MAPPER.readValue(resource, se.inera.certificate.modules.fk7263.model.internal.Utlatande.class);
    }

    public static se.inera.certificate.modules.fk7263.model.internal.Utlatande toInternal(String resource)
            throws IOException {
        return OBJECT_MAPPER.readValue(resource, se.inera.certificate.modules.fk7263.model.internal.Utlatande.class);
    }
}
