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

/**
 * Utility that aids in creating an XML validator from complex XSD files (which include and/or import other XSD files).
 * Create an instance of the builder and register all resources (XSD files) that the builder needs to know about. When a
 * {@link Schema} is generated the root XSD must be specified.
 * <p>
 * Example:
 * 
 * <pre>
 * SchemaValidatorBuilder schemaValidatorBuilder = new SchemaValidatorBuilder();
 * Source rootSource = schemaValidatorBuilder.registerResource(&quot;root-schema.xsd&quot;);
 * schemaValidatorBuilder.registerResource(&quot;included-schema.xsd&quot;);
 * schemaValidatorBuilder.registerResource(&quot;imported-schema.xsd&quot;);
 * Schema schema = schemaValidatorBuilder.build(rootSource);
 * </pre>
 */
public class SchemaValidatorBuilder {

    private final List<Source> sources;

    public SchemaValidatorBuilder() {
        this.sources = new ArrayList<>();
    }

    /**
     * Register an resource that can be fount on the classpath.
     * 
     * @param classPathResouce
     *            The XSD classpath resource path.
     * 
     * @return The {@link Source} representation of the resource. Useful when calling {@link #build(Source)} if the
     *         specified resource is the root source.
     * 
     * @throws IOException
     *             if the resource could not be found.
     */
    public Source registerResource(String classPathResouce) throws IOException {
        Source source = new StreamSource(new ClassPathResource(classPathResouce).getInputStream());
        // We define the systemId as the last path segment of the classPathResource (the filename).
        source.setSystemId(getLastPathSegment(classPathResouce));
        sources.add(source);

        return source;
    }

    /**
     * Builds a {@link Schema} that can be used to validate XML agains the complex XSD schema.
     * 
     * @param rootSource
     *            The XSD that is the root schema (including and importing other schemas).
     * 
     * @return A schema used for validation.
     * 
     * @throws SAXException
     *             If the schema could not be generated.
     */
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

    /**
     * A simple {@link LSResourceResolver} implementation that resolves resources based on the sysmteId against those
     * registered in the builder. <br>
     * NOTE: The resources are returned as input streams, meaning they can only be read once.
     */
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
