package se.inera.certificate.modules.spec

import static groovyx.net.http.ContentType.JSON
import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import groovyx.net.http.RESTClient

import org.springframework.core.io.ClassPathResource
import se.inera.certificate.spec.util.RestClientFixture



public class RLIntyg extends RestClientFixture {
    
    // set by fitNesse
    int rad
    String personnr
    String utfärdat
	String utfärdare
	String enhet
    String orsak
    String mall
    String resmål
    
    String typ = "RLI"
    
    String giltigtFrån
    String giltigtTill   
	
	String template
    String certId
    String msgBody
    
	
	public void reset() {
		mall = "M"
		utfärdare = "EnUtfärdare"
		enhet = "EnVårdEnhet"
		template = null
	}
	
    public void execute() {
        
        def restClient = new RESTClient(baseUrl)
		
        giltigtFrån = utfärdat
        
        giltigtTill = new Date().parse("yyyy-MM-dd", utfärdat).plus(90).format("yyyy-MM-dd")
        
        certId = "${mall}-${personnr}-${rad}"
        
        def res = restClient.post(
                    path: 'certificate',
                    body: makeCertificateJsonFromTemplate(),
                    requestContentType: JSON
                    )
    }

    private makeCertificateJsonFromTemplate() {
        [id:certId,
            type:typ,
            civicRegistrationNumber:personnr,
            signedDate:utfärdat,
            signingDoctorName: utfärdare,
            validFromDate:giltigtFrån,
            validToDate:giltigtTill,
            careUnitName: enhet,
			states:
			  [
				 [state:"RECEIVED",
				  target:"MI",
				  timestamp:"2013-08-05T14:30:03.227"
				 ]
			  ],
            document: "\"" + makeCertificateContents() + "\""
        ]
    }

    private makeCertificateContents() {
        // slurping the template
        def certificate = new JsonSlurper().parse(new InputStreamReader(new ClassPathResource("${mall}-template.json").getInputStream()))
                
        // setting the certificate ID
        certificate.id.extension = certId

        // setting personnr in certificate XML
        certificate.patient.id.extension = personnr

		certificate.skapadAv.namn = utfärdare
		certificate.skapadAv.vardenhet.id.extension = enhet
		certificate.skapadAv.vardenhet.namn = enhet
		
        // setting the signing date, from date and to date
        certificate.signeringsdatum = utfärdat
        certificate.skickatdatum = utfärdat
        
        certificate.arrangemang.plats = resmål

        JsonOutput.toJson(certificate)
    }
}
