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
	String typ_av_utlatande
	String patient_id
	String patient_fornamn
	String patient_efternamn
	String skapadAv_enhetsnamn
    
    public String matchandeJson() {

        def restClient = new RESTClient(System.getProperty("modules.baseUrl"))
        def response = restClient.post(
                path: typ + '/api/unmarshall',
                body: xmlPayload(),
                contentType: JSON,
                requestContentType: XML
        )
		
		def certificate = response.data
		
		try {
			assert certificate.id.extension == utlatande_id_extension
			assert certificate.typ.code == typ_av_utlatande
			assert certificate.patient.id.extension == patient_id
			assert certificate.patient.fornamn[0] == patient_fornamn
			assert certificate.patient.efternamn == patient_efternamn
			assert certificate.skapadAv.vardenhet.namn == skapadAv_enhetsnamn
			
		} catch (AssertionError e) {
			return "nej"
		}
		
        return "ja"
    }

    private xmlPayload() {

		return new InputStreamReader(new ClassPathResource(filnamn).getInputStream()).getText()
    }
}