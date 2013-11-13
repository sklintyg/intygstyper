package se.inera.certificate.modules.rli.build.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.xml.bind.JAXB;

import org.apache.commons.io.FilenameUtils;

import se.inera.certificate.integration.json.CustomObjectMapper;
import se.inera.certificate.modules.rli.model.converters.ConverterException;
import se.inera.certificate.modules.rli.model.converters.TransportToExternalConverter;
import se.inera.certificate.modules.rli.model.converters.TransportToExternalConverter;
import se.inera.certificate.modules.rli.model.external.Utlatande;

/**
 * Utility "script" that creates external JSON from XML using se.inera.certificate
 * .modules.rli.model.converters.TransportToExternalConverter
 * 
 * @author erik
 * 
 */
public final class XmlToJsonConverter {

    private TransportToExternalConverter transportConverter;

    private CustomObjectMapper objectMapper;

    private XmlToJsonConverter() {
        this.transportConverter = new TransportToExternalConverter();
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
            utlatande = JAXB
                    .unmarshal(new FileInputStream(xmlFileName), se.inera.certificate.common.v1.Utlatande.class);
            writeExternalToFile(transportConverter.transportToExternal(utlatande), generateJsonFilename(xmlFileName));
        } catch (IOException ie) {
            ie.printStackTrace();
        } catch (ConverterException e) {
            e.printStackTrace();
        }
    }

    private void writeExternalToFile(Utlatande utlatande, File filename) {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(filename, utlatande);

        } catch (IOException ie) {
            ie.printStackTrace();
        }
    }

    private File generateJsonFilename(String xmlFileName) {
        String path = FilenameUtils.getFullPath(xmlFileName);
        String filenameBase = FilenameUtils.getBaseName(xmlFileName);
        return new File(path, filenameBase + ".json");
    }

    public static void main(String[] args) {
        if (args == null || args.length == 0) {
            System.err.println("No file names supplied");
            return;
        }

        XmlToJsonConverter exec = new XmlToJsonConverter();
        exec.buildJSONFromXML(args);
    }
}
