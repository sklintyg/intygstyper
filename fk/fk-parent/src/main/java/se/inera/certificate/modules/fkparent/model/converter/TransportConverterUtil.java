package se.inera.certificate.modules.fkparent.model.converter;

import javax.xml.bind.JAXBElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import se.riv.clinicalprocess.healthcond.certificate.types.v2.CVType;
import se.riv.clinicalprocess.healthcond.certificate.v2.Svar.Delsvar;

public class TransportConverterUtil {

    private static final Logger LOG = LoggerFactory.getLogger(TransportConverterUtil.class);

    /**
     * Attempt to parse a non-empty String from a Delsvar.
     *
     * @param delsvar The Delsvar to parse.
     *
     * @return The non-empty String content of the Delsvar.
     */
    public static String getStringContent(Delsvar delsvar) {
        String string = delsvar.getContent().stream()
                .map(content -> ((String) content).trim())
                .filter(content -> (!content.isEmpty()))
                .reduce("", String::concat);
        return string;
    }

    @SuppressWarnings("unchecked")
    public static CVType getCVSvarContent(Delsvar delsvar) {
        CVType cvType = new CVType();
        for (Object o : delsvar.getContent()) {
            if (o instanceof Node) {
                Node node = (Node) o;
                NodeList list = node.getChildNodes();
                for (int i = 0; i < list.getLength(); i++) {
                    switch (list.item(i).getNodeName()) {
                    case "ns3:code":
                        cvType.setCode(list.item(i).getTextContent());
                        break;
                    case "ns3:codeSystem":
                        cvType.setCodeSystem(list.item(i).getTextContent());
                        break;
                    default:
                        LOG.debug("Unexpected element found while parsing CVType");
                        break;
                    }
                }
            } else if (o instanceof JAXBElement) {
                return ((JAXBElement<CVType>) o).getValue();
            }
        }
        return cvType.getCode() != null && cvType.getCodeSystem() != null ? cvType : null;
    }
}
