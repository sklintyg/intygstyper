package se.inera.certificate.modules.ts_bas.pdf.xpath;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.w3c.dom.Node;

/**
 * An xPath expression that is evaluated as an date string result. The output date format can be specified.
 */
public class DateXPathExpression extends AbstractXPathExpression<String> {

    private final DateTimeFormatter dateFormat;

    /**
     * Creates a boolean xPath expression from an xPath string.
     * 
     * @param xPathString
     *            The xPath string resulting in a boolean result.
     * @param dateFormat
     *            The expected output date format.
     */
    public DateXPathExpression(String xPathString, String dateFormat) {
        super(xPathString, XPathConstants.STRING);
        this.dateFormat = DateTimeFormat.forPattern(dateFormat);
    }

    @Override
    public String evaluate(XPath xPath, Node document) throws XPathExpressionException {
        String xmlDate = super.evaluate(xPath, document);
        LocalDateTime dateTime = LocalDateTime.parse(xmlDate);
        return dateTime.toString(dateFormat);
    }
}
