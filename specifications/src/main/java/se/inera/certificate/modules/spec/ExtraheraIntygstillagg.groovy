package se.inera.certificate.modules.spec

import groovy.xml.MarkupBuilder
import groovyx.net.http.RESTClient

import static groovyx.net.http.ContentType.JSON
import static groovyx.net.http.ContentType.XML

/**
 *
 * @author andreaskaltenbach
 */
class ExtraheraIntygstillagg {

    String typ
    String resenar
    String resmal

    String kommentar

    public String tillag() {

        def restClient = new RESTClient(System.getProperty("modules.baseUrl"))
        def response = restClient.post(
                path: typ + '/api/extension',
                body: xmlPayload(),
                contentType: JSON,
                requestContentType: XML
        )
        return response.data
    }

    private xmlPayload() {
        def writer = new StringWriter()
        def xml = new MarkupBuilder(writer)
        xml.lakarutlatande(xmlns: 'urn:intyg:1') {
            id { mkp.yield 'intyg-1' }
            typ { mkp.yield typ }
            signeringsdatum { mkp.yield '2011-01-26T00:00:00.000+01:00' }
            patient {
                id { mkp.yield '19001122-3344' }
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

            if (resmal) {
                resmal { mkp.yield resmal }
            }
            if (resenar) {
                resenar { mkp.yield resenar }
            }
        }

        writer.toString()
    }
}
