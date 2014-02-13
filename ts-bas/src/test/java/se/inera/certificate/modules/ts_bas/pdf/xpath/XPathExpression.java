package se.inera.certificate.modules.ts_bas.pdf.xpath;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Node;

public interface XPathExpression<T> {

    T evaluate(XPath xPath, Node document) throws XPathExpressionException;
}
