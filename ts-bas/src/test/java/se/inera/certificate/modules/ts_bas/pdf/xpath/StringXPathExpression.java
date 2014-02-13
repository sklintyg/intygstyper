package se.inera.certificate.modules.ts_bas.pdf.xpath;

import javax.xml.xpath.XPathConstants;

public class StringXPathExpression extends AbstractXPathExpression<String> {

    public StringXPathExpression(String xPathString) {
        super(xPathString, XPathConstants.STRING);
    }
}
