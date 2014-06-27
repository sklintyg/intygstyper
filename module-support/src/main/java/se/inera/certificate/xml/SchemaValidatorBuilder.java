package se.inera.certificate.xml;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.SAXException;

public class SchemaValidatorBuilder {

    private final List<Source> sources;

    public SchemaValidatorBuilder() {
        this.sources = new ArrayList<>();
    }

    public Source registerResource(String classPathResouce) throws IOException {
        Source source = new StreamSource(new ClassPathResource(classPathResouce).getInputStream());
        source.setSystemId(getLastPathSegment(classPathResouce));
        sources.add(source);

        return source;
    }

    public Schema build(Source rootSource) throws SAXException {
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        schemaFactory.setResourceResolver(new ResourceResolver());

        return schemaFactory.newSchema(rootSource);
    }

    private String getLastPathSegment(String path) {
        if (path.contains("/")) {
            return StringUtils.substringAfterLast(path, "/");
        }
        return path;
    }

    private class ResourceResolver implements LSResourceResolver {

        @Override
        public LSInput resolveResource(String type, String namespaceURI, String publicId, String systemId, String baseURI) {
            systemId = getLastPathSegment(systemId);

            for (Source source : sources) {
                if (source.getSystemId().equals(systemId)) {
                    InputStream resourceAsStream = ((StreamSource) source).getInputStream();
                    return new LSInputImpl(publicId, systemId, resourceAsStream);
                }
            }

            return null;
        }

        private class LSInputImpl implements LSInput {

            private String publicId;

            private String systemId;

            private BufferedInputStream inputStream;

            public LSInputImpl(String publicId, String sysId, InputStream input) {
                this.publicId = publicId;
                this.systemId = sysId;
                this.inputStream = new BufferedInputStream(input);
            }

            public String getPublicId() {
                return publicId;
            }

            public void setPublicId(String publicId) {
                this.publicId = publicId;
            }

            @Override
            public String getBaseURI() {
                return null;
            }

            @Override
            public InputStream getByteStream() {
                return inputStream;
            }

            @Override
            public boolean getCertifiedText() {
                return false;
            }

            @Override
            public Reader getCharacterStream() {
                return null;
            }

            @Override
            public String getEncoding() {
                return null;
            }

            @Override
            public String getStringData() {
                return null;
            }

            @Override
            public void setBaseURI(String baseURI) {
            }

            @Override
            public void setByteStream(InputStream byteStream) {
            }

            @Override
            public void setCertifiedText(boolean certifiedText) {
            }

            @Override
            public void setCharacterStream(Reader characterStream) {
            }

            @Override
            public void setEncoding(String encoding) {
            }

            @Override
            public void setStringData(String stringData) {
            }

            public String getSystemId() {
                return systemId;
            }

            public void setSystemId(String systemId) {
                this.systemId = systemId;
            }
        }
    }
}
