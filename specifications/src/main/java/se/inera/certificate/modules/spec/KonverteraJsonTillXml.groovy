package se.inera.certificate.modules.spec

import static groovyx.net.http.ContentType.JSON
import static groovyx.net.http.ContentType.XML
import groovy.json.JsonSlurper
import groovyx.net.http.RESTClient

import org.codehaus.groovy.ast.stmt.CatchStatement;
import org.springframework.core.io.ClassPathResource

/**
 * 
 * @author erik
 *
 */
class KonverteraJsonTillXml{

    String typ
    String filnamn
	String test_variable
    
    public String isValid() {

        def restClient = new RESTClient(System.getProperty("modules.baseUrl"))
        def response = restClient.put(
                path: typ + '/api/marshall',
                body: jsonPayload(),
                contentType: XML,
                requestContentType: JSON
        )
		
		def certificate = response.getData()
		
		try {
			assert certificate.id.extension == test_variable
		} catch (AssertionError e) {
			return "nej" 
		}
		
        return "ja"
    }

    private jsonPayload() {
		def jsonText = new InputStreamReader(new ClassPathResource(filnamn).getInputStream()).getText()
		return jsonText
    }
}
