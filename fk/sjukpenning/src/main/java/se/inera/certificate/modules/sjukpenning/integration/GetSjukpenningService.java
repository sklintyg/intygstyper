/*
 * Copyright (C) 2015 Inera AB (http://www.inera.se)
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


package se.inera.certificate.modules.sjukpenning.integration;

import java.io.ByteArrayInputStream;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.ws.Provider;
import javax.xml.ws.Service;
import javax.xml.ws.ServiceMode;
import javax.xml.ws.WebServiceProvider;

@WebServiceProvider
@ServiceMode(value = Service.Mode.MESSAGE)
public class GetSjukpenningService implements Provider<Source> {

    @Override
    public Source invoke(Source request) {
        DOMSource response = new DOMSource();
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            StreamResult result = new StreamResult(System.out);
            transformer.transform(request, result);
            System.out.println("\n\n\n");

            String responseString = "<result xmlns=\"urn:riv:clinicalprocess:healthcond:certificate:2\"><resultCode>OK</resultCode></result>";
            ByteArrayInputStream is = new ByteArrayInputStream(responseString.getBytes());
            SOAPMessage greetMeResponse = MessageFactory.newInstance().createMessage(null, is);
            is.close();
            response.setNode(greetMeResponse.getSOAPBody().extractContentAsDocument());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return response;
    }

}
