package se.inera.certificate.modules.spec
import groovy.xml.MarkupBuilder
import groovyx.net.http.RESTClient

import static groovyx.net.http.ContentType.XML
/**
 *
 * @author andreaskaltenbach
 */
class ValideraIntyg {

    String typ
    String intyg
    String personnr
    String resenar
    String resmal

    String kommentar

    public String fel() {

        def restClient = new RESTClient(System.getProperty("modules.baseUrl"))
        restClient.post(
                path: typ + '/api/valid',
                body: xmlPayload(),
                requestContentType: XML
        )
    }

    private xmlPayload() {
        def writer = new StringWriter()
        def xml = new MarkupBuilder(writer)
        xml.lakarutlatande(xmlns: 'urn:intyg:1') {
            id { mkp.yield intyg }
            typ { mkp.yield typ }
            signeringsdatum { mkp.yield '2011-01-26T00:00:00.000+01:00' }
            patient {
                id { mkp.yield intyg }
                fornamn { mkp.yield 'Carl' }
                efternamn { mkp.yield 'Bildt' }
            }
            skapadAv {
                id { mkp.yield 'Personal HSA-ID' }
                namn { mkp.yield 'En Läkare' }
            }
            vardenhet {
                id { mkp.yield 'VardenhetY' }
                namn { mkp.yield 'Kir mott' }
                vardgivare {
                    id { mkp.yield 'VardgivarId' }
                    namn { mkp.yield 'Landstinget Norrland' }
                }
            }
        }

        println writer.toString()
    }


}
