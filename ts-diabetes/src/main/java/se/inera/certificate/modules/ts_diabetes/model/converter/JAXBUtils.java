package se.inera.certificate.modules.ts_diabetes.model.converter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;

import org.w3c.dom.Node;

import se.inera.certificate.modules.ts_diabetes.model.internal.Utlatande;


public final class JAXBUtils {

    private static JAXBContext jaxbContext;

    private JAXBUtils() {
    }

    /**
     * Helper method that resolves XSD elements defined with <code>xsd:anyType</code>. One or more accepted Java types
     * should be supplied. If the element couldn't be resolved to any of those, <code>null</code> is returned. The
     * implementation resolves both elements realized as {@link Node} and POJOs.
     * <p/>
     * Note that the if the element is a {@link Node}, the first successful JAXB unmarshall will be returned. Therefore
     * the order of the accepted types might be important.
     *
     * @param element       The element that should be resolved to one of the accepted types.
     * @param acceptedTypes The types that are accepted.
     * @return An object (or sub type thereof) of one of the accepted types, or <code>null</code> otherwise.
     * @throws JAXBException
     */
    public static Object resolveAnyType(Object element, Class<?>... acceptedTypes) throws JAXBException {
        if (jaxbContext == null) {
            jaxbContext = JAXBContext.newInstance(Utlatande.class);
        }

        if (element instanceof Node) {
            Source domSource = new DOMSource((Node) element);
            for (Class<?> acceptedType : acceptedTypes) {
                Object result = jaxbContext.createUnmarshaller().unmarshal(domSource, acceptedType).getValue();
                if (result != null) {
                    return result;
                }
            }

        } else {
            for (Class<?> acceptedType : acceptedTypes) {
                if (acceptedType.isAssignableFrom(element.getClass())) {
                    return element;
                }
            }
        }

        return null;
    }
}
