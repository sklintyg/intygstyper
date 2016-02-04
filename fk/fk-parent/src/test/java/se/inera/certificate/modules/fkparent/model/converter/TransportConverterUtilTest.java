package se.inera.certificate.modules.fkparent.model.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.riv.clinicalprocess.healthcond.certificate.types.v2.CVType;
import se.riv.clinicalprocess.healthcond.certificate.v2.Svar.Delsvar;

public class TransportConverterUtilTest {

    private static final String NAMESPACE = "urn:riv:clinicalprocess:healthcond:certificate:types:2";

    @Test
    public void testGetCVTypeContentSuccess() throws Exception {
        String code = "1";
        String codeSystem = "TEST";
        Delsvar delsvar = buildCVTypeDelsvar(code, codeSystem);
        CVType actual = TransportConverterUtil.getCVSvarContent(delsvar);
        CVType expected = buildCVType(code, codeSystem);
        assertEquals(expected.getCode(), actual.getCode());
        assertEquals(expected.getCodeSystem(), actual.getCodeSystem());
    }

    @Test
    public void testGetStringContentWithWhitespaceSuccess() throws ParserConfigurationException {
        String expected = "String with freetext";
        Delsvar delsvar = buildStringDelsvar(Arrays.asList("\n", "    ", expected, "\n", "    "));
        String actual = TransportConverterUtil.getStringContent(delsvar);
        assertEquals(expected, actual);
    }

    private Delsvar buildStringDelsvar(List<String> content) {
        Delsvar delsvar = new Delsvar();
        delsvar.getContent().addAll(content);
        return delsvar;
    }

    @Test
    public void testGetCVTypeDifferentContentFails() throws Exception {
        String code = "1";

        Delsvar delsvar = buildCVTypeDelsvar(code, "ANOTHER");
        CVType actual = TransportConverterUtil.getCVSvarContent(delsvar);

        CVType expected = buildCVType(code, "TEST");
        assertEquals(expected.getCode(), actual.getCode());
        assertFalse(expected.getCodeSystem().equals(actual.getCodeSystem()));
    }

    @Test(expected = ConverterException.class)
    public void testGetCVTypeNullContent() throws Exception {
        Delsvar delsvar = new Delsvar();
        delsvar.getContent().add(null);
        TransportConverterUtil.getCVSvarContent(delsvar);
    }

    private CVType buildCVType(String code, String codeSystem) {
        CVType cvType = new CVType();
        cvType.setCode(code);
        cvType.setCodeSystem(codeSystem);
        return cvType;
    }

    private Delsvar buildCVTypeDelsvar(String code, String codeSystem) throws Exception {
        Delsvar delsvar = new Delsvar();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document xmlDoc = factory.newDocumentBuilder().newDocument();

        Node cv = null;
        cv = xmlDoc.createElementNS(NAMESPACE, "ns3:cv");
        Node nodeCode = xmlDoc.createElementNS(NAMESPACE, "ns3:code");
        nodeCode.setTextContent(code);
        Node nodeCodeSystem = xmlDoc.createElementNS(NAMESPACE, "ns3:codeSystem");
        nodeCodeSystem.setTextContent(codeSystem);

        cv.appendChild(nodeCode);
        cv.appendChild(nodeCodeSystem);
        
        delsvar.getContent().add(cv);

        return delsvar;
    }

}
