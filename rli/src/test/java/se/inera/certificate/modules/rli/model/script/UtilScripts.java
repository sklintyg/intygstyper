package se.inera.certificate.modules.rli.model.script;

import java.io.File;
import java.io.IOException;

import javax.xml.bind.JAXB;

import org.springframework.core.io.ClassPathResource;

import se.inera.certificate.integration.json.CustomObjectMapper;
import se.inera.certificate.modules.rli.model.converters.TransportToExternalConverter;
import se.inera.certificate.modules.rli.model.converters.TransportToExternalConverterImpl;
import se.inera.certificate.modules.rli.model.external.Utlatande;

/**
 * Utility "script" that creates external JSON from XML using
 * se.inera.certificate
 * .modules.rli.model.converters.TransportToExternalConverter
 * 
 * @author erik
 * 
 */
public final class UtilScripts {

    private TransportToExternalConverter transportConverter;

    private CustomObjectMapper objectMapper;

    private String outputDir = null;

    private UtilScripts(String outputDir) {
        this.outputDir = outputDir;
        this.transportConverter = new TransportToExternalConverterImpl();
        this.objectMapper = new CustomObjectMapper();
    }

    public void buildJSONFromXML(String[] xmlFileNames) {

        for (String xmlFileName : xmlFileNames) {
            System.out.println("Generating JSON from " + xmlFileName);
            buildExternalFromFile(xmlFileName);
        }

    }

    /**
     * Creates a JSON-file with the same name as the XML-file sent as input
     * 
     * @param xmlFileName
     *            The name of the XML to be converted
     */
    private void buildExternalFromFile(String xmlFileName) {

        if (xmlFileName.indexOf(".xml") == 0) {
            System.err.println("Incorrect filetype submitted");
            return;
        }

        se.inera.certificate.common.v1.Utlatande utlatande = null;
        try {
            utlatande = JAXB.unmarshal(new ClassPathResource(xmlFileName).getInputStream(),
                    se.inera.certificate.common.v1.Utlatande.class);
        } catch (IOException ie) {
            ie.printStackTrace();
        }
        writeExternalToFile(transportConverter.transportToExternal(utlatande), xmlFileName);
    }

    private void writeExternalToFile(Utlatande utlatande, String filename) {
        try {

            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(makeFilePath(filename)), utlatande);

        } catch (IOException ie) {
            ie.printStackTrace();
        }
    }

    private String makeFilePath(String xmlFileName) {
        StringBuilder sb = new StringBuilder();
        sb.append(outputDir).append("/");
        sb.append(xmlFileName.substring(0, xmlFileName.lastIndexOf('.')));
        sb.append(".json");
        return sb.toString();
    }

    public static void main(String[] args) {

        if (args == null || args.length == 0) {
            System.err.println("No file names supplied");
            return;
        }

        String outputDir = System.getProperty("outputDir");

        if (outputDir == null) {
            System.err.println("No outputdir supplied");
            return;
        }

        UtilScripts exec = new UtilScripts(outputDir);
        exec.buildJSONFromXML(args);
    }
}
