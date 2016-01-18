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

package se.inera.intyg.intygstyper.ts_parent.transformation;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.URIResolver;
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
