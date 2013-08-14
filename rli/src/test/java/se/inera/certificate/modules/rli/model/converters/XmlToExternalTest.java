package se.inera.certificate.modules.rli.model.converters;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

import se.inera.certificate.modules.rli.model.external.Utlatande;
import se.inera.certificate.modules.rli.model.external.common.Aktivitet;
import se.inera.certificate.modules.rli.model.external.common.Observation;
import se.inera.certificate.modules.rli.model.external.common.Rekommendation;
import se.inera.certificate.modules.rli.model.converters.TransportToExternalConverterImpl;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.joda.time.LocalDateTime;

public class XmlToExternalTest {
	
	private Utlatande extUtlatande;
	private TransportToExternalConverterImpl converter;

	@Before
	public void init(){
		se.inera.certificate.common.v1.Utlatande source = unMarshallXml();
		converter = new TransportToExternalConverterImpl();
		extUtlatande = converter.transportToExternal(source);
	}
	
	@Test
	public void testUtlatandeTyp(){
		assertEquals("RLI", extUtlatande.getTyp().getCode());
	}
	
	@Test
	public void testUtlatandeId(){
		assertEquals("39f80245-9730-4d76-aaff-b04a2f3cfbe7", extUtlatande.getId().getExtension());
	}	
	
	@Test
	public void testUtlatandeKommentarer(){
		assertEquals("Övriga upplysningar", extUtlatande.getKommentarer().get(0));
	}
	
	@Test
	public void testSigneringsDatum(){
		LocalDateTime signeratDate = new LocalDateTime("2013-08-12T11:25:00");
		assertEquals(signeratDate, extUtlatande.getSigneringsdatum());
	}
	
	@Test 
	public void testSkickatDatum(){
		LocalDateTime skickatDate = new LocalDateTime("2013-08-12T11:25:30");	
		assertEquals(skickatDate, extUtlatande.getSkickatdatum());
	}
	
	@Test
	public void testPatient(){
		assertEquals("1.2.752.129.2.1.3.1", extUtlatande.getPatient().getPersonId().getRoot());
		assertEquals("191212121212", extUtlatande.getPatient().getPersonId().getExtension());
		assertEquals("Test", extUtlatande.getPatient().getFornamns().get(0));
		assertEquals("Testsson", extUtlatande.getPatient().getEfternamns().get(0));
		assertEquals("Teststigen 1, 123 45 Stockholm", extUtlatande.getPatient().getAdress());
	}
	
	@Test 
	public void testSkapadAv(){
		assertEquals("1.2.752.129.2.1.4.1", extUtlatande.getSkapadAv().getPersonalId().getRoot());
		assertEquals("191010101010", extUtlatande.getSkapadAv().getPersonalId().getExtension());
		assertEquals("Doktor Alban", extUtlatande.getSkapadAv().getFullstandigtNamn());
		assertEquals("ABC123", extUtlatande.getSkapadAv().getForskrivarkod());
		
		//skapad av ->enhet
		assertEquals("1.2.752.129.2.1.4.1", extUtlatande.getSkapadAv().getEnhet().getEnhetsId().getRoot());
		assertEquals("vardenhet_test", extUtlatande.getSkapadAv().getEnhet().getEnhetsId().getExtension());
		assertEquals("Testenheten", extUtlatande.getSkapadAv().getEnhet().getEnhetsnamn());
		
		//skapad av -> enhet -> vardgivare
		assertEquals("1.2.752.129.2.1.4.1", extUtlatande.getSkapadAv().getEnhet().getVardgivare().getVardgivareId().getRoot());
		assertEquals("vardgivare_test", extUtlatande.getSkapadAv().getEnhet().getVardgivare().getVardgivareId().getExtension());
		assertEquals("Testvårdgivaren", extUtlatande.getSkapadAv().getEnhet().getVardgivare().getVardgivarnamn());
	}
	
	@Test
	public void testAktivitet(){
		List<Aktivitet> aktiviteter = extUtlatande.getAktiviteter();
		for (Aktivitet a : aktiviteter){ 
			assertEquals("AV020", a.getAktivitetskod().getCode());
			assertEquals("1.2.752.116.1.3.2.1.4", a.getAktivitetskod().getCodeSystem());
			assertEquals("KVÅ", a.getAktivitetskod().getCodeSystemName());
			
			assertEquals("1.2.752.129.2.1.4.1", a.getUtforsVidEnhet().getEnhetsId().getRoot());
			assertEquals("vardenhet_test", a.getUtforsVidEnhet().getEnhetsId().getExtension());
			assertEquals("Testenheten", a.getUtforsVidEnhet().getEnhetsnamn());
			
			assertEquals("1.2.752.129.2.1.4.1", a.getUtforsVidEnhet().getVardgivare().getVardgivareId().getRoot());
			assertEquals("vardgivare_test", a.getUtforsVidEnhet().getVardgivare().getVardgivareId().getExtension());
			assertEquals("Testvårdgivaren", a.getUtforsVidEnhet().getVardgivare().getVardgivarnamn());			
		}
	}
	
	@Test
	public void testRekommendation(){
		List<Rekommendation> rekommendationer = extUtlatande.getRekommendationer();
		for (Rekommendation rekommendation : rekommendationer){
			assertEquals("REK1", rekommendation.getRekommendationskod().getCode());
			assertEquals("kv_rekommendation_intyg", rekommendation.getRekommendationskod().getCodeSystemName());
			assertEquals("SJK1", rekommendation.getSjukdomskannedom().getCode());
			assertEquals("SJK1", rekommendation.getSjukdomskannedom().getCode());
			assertEquals("???", rekommendation.getSjukdomskannedom().getCodeSystem());
			assertEquals("kv_sjukdomskännedom_intyg", rekommendation.getSjukdomskannedom().getCodeSystemName());
		}
	}
	
	@Test
	public void testObservation(){
		List<Observation> observationer = extUtlatande.getObservationer();
		for (Observation observation : observationer){
			assertEquals("39104002", observation.getObservationskod().getCode());
			assertEquals("1.2.752.116.2.1.1.1", observation.getObservationskod().getCodeSystem());
			assertEquals("SNOMED-CT", observation.getObservationskod().getCodeSystemName());
		}
	}
	
	@Test
	public void testArrangemang(){
		assertEquals("12345678-90",extUtlatande.getArrangemang().getBokningsreferens());
		assertEquals("2013-01-01",extUtlatande.getArrangemang().getBokningsdatum().toString());
		assertEquals("2013-07-22",extUtlatande.getArrangemang().getArrangemangstid().getFrom().toString());
		assertEquals("2013-08-02",extUtlatande.getArrangemang().getArrangemangstid().getTom().toString());
		assertEquals("2013-08",extUtlatande.getArrangemang().getAvbestallningsdatum().toString());
		assertEquals("420008001", extUtlatande.getArrangemang().getArrangemangstyp().getCode());
		assertEquals("1.2.752.116.2.1.1.1", extUtlatande.getArrangemang().getArrangemangstyp().getCodeSystem());
		assertEquals("SNOMED-CT", extUtlatande.getArrangemang().getArrangemangstyp().getCodeSystemName());
		assertEquals("New York", extUtlatande.getArrangemang().getPlats());
	}
	
	private se.inera.certificate.common.v1.Utlatande unMarshallXml(){
		try{
			JAXBContext context = 
					JAXBContext.newInstance("se.inera.certificate.common.v1");
			se.inera.certificate.common.v1.Utlatande utlatande;
			Unmarshaller unmarshaller = context.createUnmarshaller();
			
			Source src = new StreamSource(new FileInputStream("src/test/resources/rli-example-1.xml"));
			
			JAXBElement<se.inera.certificate.common.v1.Utlatande> jaxbElement = 
					(JAXBElement<se.inera.certificate.common.v1.Utlatande>)
						unmarshaller.unmarshal(src, se.inera.certificate.common.v1.Utlatande.class);
			
			utlatande = jaxbElement.getValue();
			return utlatande;

		} catch (JAXBException je){
			je.printStackTrace();
			return null;
		} catch (IOException ie){
			ie.printStackTrace();
			return null;
		} 
	}

}
