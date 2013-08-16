package se.inera.certificate.modules.spec

import static groovyx.net.http.ContentType.JSON
import static groovyx.net.http.ContentType.XML
import groovyx.net.http.RESTClient

import org.codehaus.groovy.ast.stmt.CatchStatement;
import org.springframework.core.io.ClassPathResource

/**
 * 
 * @author erik
 *
 */
class KonverteraXmlViaREST{

    String typ
    String filnamn
	String utlatande_id_extension
    
    public String matchandeJson() {

        def restClient = new RESTClient(System.getProperty("modules.baseUrl"))
        def response = restClient.put(
                path: typ + '/api/unmarshall',
                body: xmlPayload(),
                contentType: JSON,
                requestContentType: XML
        )
		
		def certificate = response.data
		
		try {
			assert certificate.id.extension == utlatande_id_extension
		} catch (AssertionError e) {
			return "nej"
		}
		
        return "ja"
    }

    private xmlPayload() {

		return new InputStreamReader(new ClassPathResource(filnamn).getInputStream()).getText()
    }
}
