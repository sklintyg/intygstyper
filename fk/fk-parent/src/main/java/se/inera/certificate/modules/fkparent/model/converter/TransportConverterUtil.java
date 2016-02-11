/*
 * Copyright (C) 2016 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.inera.certificate.modules.fkparent.model.converter;

import javax.xml.bind.JAXBElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.riv.clinicalprocess.healthcond.certificate.types.v2.CVType;
import se.riv.clinicalprocess.healthcond.certificate.v2.Svar.Delsvar;

public final class TransportConverterUtil {

    private static final Logger LOG = LoggerFactory.getLogger(TransportConverterUtil.class);

    private TransportConverterUtil() {
    }

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

    /**
     * Attempt to parse a CVType from a Delsvar.
     * @param delsvar The Delsvar to parse.
     * @return CVType
     * @throws ConverterException
     */
    public static CVType getCVSvarContent(Delsvar delsvar) throws ConverterException {
        for (Object o : delsvar.getContent()) {
            if (o instanceof Node) {
                CVType cvType = new CVType();
                Node node = (Node) o;
                NodeList list = node.getChildNodes();
                for (int i = 0; i < list.getLength(); i++) {
                    String textContent = list.item(i).getTextContent();
                    switch (list.item(i).getNodeName()) {
                    case "ns3:code":
                        cvType.setCode(textContent);
                        break;
                    case "ns3:codeSystem":
                        cvType.setCodeSystem(textContent);
                        break;
                    case "ns3:codeSystemVersion":
                        cvType.setCodeSystemVersion(textContent);
                        break;
                    case "ns3:codeSystemName":
                        cvType.setCodeSystemName(textContent);
                        break;
                    case "ns3:displayName":
                        cvType.setDisplayName(textContent);
                        break;
                    case "ns3:originalText":
                        cvType.setOriginalText(textContent);
                        break;
                    default:
                        LOG.debug("Unexpected element found while parsing CVType");
                        break;
                    }
                }
                if (cvType.getCode() == null || cvType.getCodeSystem() == null) {
                    throw new ConverterException("Error while converting CVType, missing mandatory field");
                }
                return cvType;
            } else if (o instanceof JAXBElement) {
                @SuppressWarnings("unchecked")
                JAXBElement<CVType> jaxbCvType = ((JAXBElement<CVType>) o);
                return jaxbCvType.getValue();
            }
        }
        throw new ConverterException("Unexpected outcome while converting CVType");
    }
}
