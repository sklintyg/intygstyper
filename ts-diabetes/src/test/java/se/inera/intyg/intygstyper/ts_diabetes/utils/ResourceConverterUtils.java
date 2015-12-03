package se.inera.intyg.intygstyper.ts_diabetes.utils;

import java.io.File;
import java.io.IOException;

import javax.xml.bind.JAXB;

import se.inera.intyg.common.util.integration.integration.json.CustomObjectMapper;
import se.inera.intygstjanster.ts.services.RegisterTSDiabetesResponder.v1.RegisterTSDiabetesType;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ResourceConverterUtils {

    private static final ObjectMapper OBJECT_MAPPER = new CustomObjectMapper();

    public static RegisterTSDiabetesType toTransport(File resource) throws IOException {
        return JAXB.unmarshal(resource, RegisterTSDiabetesType.class);
    }


    public static se.inera.intyg.intygstyper.ts_diabetes.model.internal.Utlatande toInternal(File resource)
            throws IOException {
        return OBJECT_MAPPER.readValue(resource,
                se.inera.intyg.intygstyper.ts_diabetes.model.internal.Utlatande.class);
    }

    public static se.inera.intyg.intygstyper.ts_diabetes.model.internal.Utlatande toInternal(String resource)
            throws IOException {
        return OBJECT_MAPPER.readValue(resource,
                se.inera.intyg.intygstyper.ts_diabetes.model.internal.Utlatande.class);
    }

}
