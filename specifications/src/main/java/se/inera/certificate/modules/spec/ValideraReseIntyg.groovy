package se.inera.certificate.modules.spec

import static groovyx.net.http.ContentType.JSON
import static groovyx.net.http.ContentType.TEXT
import groovy.json.JsonSlurper
import groovy.xml.MarkupBuilder
import groovyx.net.http.RESTClient

import org.springframework.core.io.ClassPathResource
import se.inera.certificate.spec.util.RestClientFixture

/**
 *
 * @author erik
 */
class ValideraReseIntyg extends RestClientFixture{

	String typ
	String intyg

	String kommentar

	public String fel() {

		def restClient = new RESTClient(System.getProperty("modules.baseUrl"))
		try {
			def response = restClient.post(
					path: typ + '/api/valid',
					body: jsonPayload(),
					contentType: TEXT,
					requestContentType: JSON
					)
			return response.status
		} catch (exception) {
			return exception.response.status
			
		}
	}

	private jsonPayload() {
		return new InputStreamReader(new ClassPathResource(intyg).getInputStream()).getText()
	}
}
