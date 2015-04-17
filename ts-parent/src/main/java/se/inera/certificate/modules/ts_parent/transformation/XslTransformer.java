package se.inera.certificate.modules.ts_parent.transformation;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import net.sf.saxon.TransformerFactoryImpl;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.google.common.base.Throwables;

public class XslTransformer {

    private Templates templates;

    private DocumentBuilderFactory documentBuilderFactory;

    public XslTransformer(String xslHref) {
        initializeTransformerFactory(xslHref);
        initializeDocumentBuilder();
    }

    public String transform(String incomingXML) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            StreamResult transformedXml = new StreamResult(outputStream);
            Transformer transformer = templates.newTransformer();

            transformer.transform(new DOMSource(getDocument(incomingXML)), transformedXml);

            return outputStream.toString(StandardCharsets.UTF_8.name());
        } catch (Exception e) {
            throw Throwables.propagate(e);
        }
    }

    private void initializeTransformerFactory(String xslHref) {
        TransformerFactory factory = new TransformerFactoryImpl();
        factory.setURIResolver(new URIResolver() {
            @Override
            public Source resolve(String href, String base) throws TransformerException {
                return new StreamSource(getResourceAsStream(href));
            }
        });
        try {
            templates = factory.newTemplates(new StreamSource(getResourceAsStream(xslHref)));
        } catch (TransformerConfigurationException e) {
            throw Throwables.propagate(e);
        }
    }

    private void initializeDocumentBuilder() {
        documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setNamespaceAware(true);
        documentBuilderFactory.setIgnoringComments(true);
        documentBuilderFactory.setIgnoringElementContentWhitespace(true);
    }

    private InputStream getResourceAsStream(String href) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(href);
    }

    private Document getDocument(String incomingXML) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        return documentBuilder.parse(new InputSource(new StringReader(incomingXML)));
    }

}
