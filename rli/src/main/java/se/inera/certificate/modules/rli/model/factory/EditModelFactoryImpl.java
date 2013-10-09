package se.inera.certificate.modules.rli.model.factory;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.inera.certificate.modules.rli.model.edit.HoSPersonal;
import se.inera.certificate.modules.rli.model.edit.Utlatande;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Factory for creating a editable model.
 * 
 * @author nikpet
 * 
 */
public class EditModelFactoryImpl implements EditModelFactory {

    private static final Logger LOG = LoggerFactory.getLogger(EditModelFactoryImpl.class);

    /*
     * (non-Javadoc)
     * 
     * @see se.inera.certificate.modules.rli.model.factory.EditModelFactory#createEditableUtlatande(java.lang.String,
     * java.util.Map)
     */
    @Override
    public Utlatande createEditableUtlatande(String certificateId, Map<String, Object> certificateData) {

        Utlatande utlatande = new Utlatande();

        utlatande.setUtlatandeid(certificateId);

        for (Entry<String, Object> entry : certificateData.entrySet()) {
            switch (entry.getKey()) {
            case "skapadAv":
                if (entry.getValue() instanceof Map) {
                    // Would be nice to get rid of this cast.
                    populateWithSkapadAv(utlatande, (Map<String, Object>) entry.getValue());
                }
                break;

            default:
                LOG.warn("Unknown type of certificate data '{}'", entry.getKey());
                break;
            }
        }

        return utlatande;
    }

    private void populateWithSkapadAv(Utlatande utlatande, Map<String, Object> source) {
        if (source == null) {
            LOG.error("skapadAv was null");
            return;
        }

        ObjectMapper mapper = new ObjectMapper();
        HoSPersonal fromHosPersonal = null;
        String jsonString = null;

        try {
            jsonString = createJsonStringFromMap(source);
            fromHosPersonal = mapper.readValue(jsonString, HoSPersonal.class);

        } catch (IOException e) {
            LOG.error("Got exception with message " + e.getMessage());
            return;
        }

        if (fromHosPersonal == null) {
            LOG.error("Failed to create HosPersonal from JSON, got null");
            return;
        }
        utlatande.setSkapadAv(fromHosPersonal);
    }

    /**
     * Create a json string from a Map<String, object>
     * 
     * @param source
     *            Map<String, object> to convert
     * @return JSON formatted string
     * @throws IOException
     */
    private String createJsonStringFromMap(Map<String, Object> source) throws IOException {
        StringWriter writer = new StringWriter();
        JsonGenerator jsonGenerator = new JsonFactory().createJsonGenerator(writer);
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(jsonGenerator, source);
        jsonGenerator.close();

        return writer.toString();
    }

}
