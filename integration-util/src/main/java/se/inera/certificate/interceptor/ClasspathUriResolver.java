package se.inera.certificate.interceptor;

import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;

import org.springframework.core.io.ClassPathResource;

/**
 * @author andreaskaltenbach
 */
public class ClasspathUriResolver implements URIResolver {

    @Override
    public Source resolve(String href, String base) throws TransformerException {
        try {
            return new StreamSource(new ClassPathResource(href).getInputStream());
        } catch (IOException e) {
            throw new TransformerException("Failed to load resource " + href + " from classpath.", e);
        }
    }
}