package se.inera.certificate.modules.rli.model.script;

import java.io.File;
import java.io.IOException;

import javax.xml.bind.JAXB;

import org.springframework.core.io.ClassPathResource;

import se.inera.certificate.integration.json.CustomObjectMapper;
import se.inera.certificate.modules.rli.model.external.Utlatande;

/**
 * Utility "script" that creates external JSON from XML using
 * se.inera.certificate.modules.rli.model.converters.TransportToExternalConverter
 * 
 * @author erik
 * 
 */
public class UtilScripts {

    private static se.inera.certificate.modules.rli.model.converters.TransportToExternalConverterImpl transportConverter = new se.inera.certificate.modules.rli.model.converters.TransportToExternalConverterImpl();

    private static CustomObjectMapper objectMapper = new CustomObjectMapper();

    /**
     * Creates a JSON-file with the same name as the XML-file sent as input
     * 
     * @param filename
     *            The name of the XML to be converted
     */
    private static void buildExternalFromFile(String filename) {
        String parts[] = filename.split("\\.");
        String name = parts[0];
        if (!parts[1].equals("xml")) {
            System.out.println("Incorrect filetype submitted");
            return;
        }
        se.inera.certificate.common.v1.Utlatande utlatande = null;
        try {
            utlatande = JAXB.unmarshal(new ClassPathResource(filename).getInputStream(),
                    se.inera.certificate.common.v1.Utlatande.class);
        } catch (IOException ie) {
            ie.printStackTrace();
        }
        writeExternalToFile(transportConverter.transportToExternal(utlatande), name);
    }

    private static void writeExternalToFile(Utlatande utlatande, String filename) {
        try {

            objectMapper.writerWithDefaultPrettyPrinter().writeValue(
                    new File("./src/test/resources/" + filename + ".json"), utlatande);

        } catch (IOException ie) {
            ie.printStackTrace();
        }
    }

    public static void main(String args[]) {
        buildExternalFromFile(args[0]);

    }
}
