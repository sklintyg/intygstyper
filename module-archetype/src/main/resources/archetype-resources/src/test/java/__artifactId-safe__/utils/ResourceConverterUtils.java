#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.${artifactId-safe}.utils;

import java.io.File;
import java.io.IOException;

import javax.xml.bind.JAXB;

import se.inera.certificate.integration.json.CustomObjectMapper;
import se.inera.certificate.integration.rest.dto.CertificateContentMeta;
import ${package}.${artifactId-safe}.rest.dto.CertificateContentHolder;
import se.inera.certificate.${artifactId-safe}.model.v1.Utlatande;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ResourceConverterUtils {

    private static final ObjectMapper OBJECT_MAPPER = new CustomObjectMapper();

    public static Utlatande toTransport(File resource) throws IOException {
        return JAXB.unmarshal(resource, Utlatande.class);
    }

    public static ${package}.${artifactId-safe}.model.external.Utlatande toExternal(File resource)
            throws IOException {
        return OBJECT_MAPPER.readValue(resource, ${package}.${artifactId-safe}.model.external.Utlatande.class);
    }

    public static CertificateContentHolder toExternalWithHolder(File resource) throws IOException {
        return wrapExternalWithHolder(toExternal(resource));
    }

    public static CertificateContentHolder wrapExternalWithHolder(
            ${package}.${artifactId-safe}.model.external.Utlatande externalModel) throws IOException {
        CertificateContentHolder result = new CertificateContentHolder();

        result.setCertificateContent(externalModel);

        CertificateContentMeta meta = new CertificateContentMeta();
        meta.setId(result.getCertificateContent().getId().getRoot());
        meta.setType(result.getCertificateContent().getTyp().getCode());
        meta.setPatientId(result.getCertificateContent().getPatient().getId().getExtension());

        result.setCertificateContentMeta(meta);

        return result;
    }

    public static ${package}.${artifactId-safe}.model.internal.mi.Utlatande toInternalMI(File resource)
            throws IOException {
        return OBJECT_MAPPER.readValue(resource, ${package}.${artifactId-safe}.model.internal.mi.Utlatande.class);
    }

    public static ${package}.${artifactId-safe}.model.internal.wc.Utlatande toInternalWC(File resource)
            throws IOException {
        return OBJECT_MAPPER.readValue(resource, ${package}.${artifactId-safe}.model.internal.wc.Utlatande.class);
    }
}
