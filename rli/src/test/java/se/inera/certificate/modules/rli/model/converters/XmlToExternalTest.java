/**
 * Copyright (C) 2013 Inera AB (http://www.inera.se)
 *
 * This file is part of Inera Certificate Modules (http://code.google.com/p/inera-certificate-modules).
 *
 * Inera Certificate Modules is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Inera Certificate Modules is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.inera.certificate.modules.rli.model.converters;

import static org.junit.Assert.assertEquals;

import java.io.InputStream;
import java.util.List;

import javax.xml.bind.JAXB;

import org.joda.time.LocalDateTime;
import org.junit.Before;
import org.junit.Test;

import se.inera.certificate.modules.rli.model.external.Utlatande;
import se.inera.certificate.modules.rli.model.external.common.Aktivitet;
import se.inera.certificate.modules.rli.model.external.common.Observation;
import se.inera.certificate.modules.rli.model.external.common.Rekommendation;

public class XmlToExternalTest {
	
	private TransportToExternalConverter converter;

	@Before
	public void setUp(){
		converter = new TransportToExternalConverterImpl();
	}
	
	@Test
	public void testXmlToExternal(){
		Utlatande extUtlatande = converter.transportToExternal(unmarshallXml("rli-example-1.xml"));
		
		assertEquals("RLI", extUtlatande.getTyp().getCode());

		assertEquals("39f80245-9730-4d76-aaff-b04a2f3cfbe7", extUtlatande.getId().getExtension());

		assertEquals("Övriga upplysningar", extUtlatande.getKommentarer().get(0));

		LocalDateTime signeratDate = new LocalDateTime("2013-08-12T11:25:00");
		assertEquals(signeratDate, extUtlatande.getSigneringsdatum());

		LocalDateTime skickatDate = new LocalDateTime("2013-08-12T11:25:30");	
		assertEquals(skickatDate, extUtlatande.getSkickatdatum());
	
		assertEquals("1.2.752.129.2.1.3.1", extUtlatande.getPatient().getPersonId().getRoot());
		assertEquals("191212121212", extUtlatande.getPatient().getPersonId().getExtension());
		assertEquals("Test", extUtlatande.getPatient().getFornamns().get(0));
		assertEquals("Testsson", extUtlatande.getPatient().getEfternamns().get(0));
		assertEquals("Teststigen 1, 123 45 Stockholm", extUtlatande.getPatient().getAdress());
	
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

		List<Rekommendation> rekommendationer = extUtlatande.getRekommendationer();
		for (Rekommendation rekommendation : rekommendationer){
			assertEquals("REK1", rekommendation.getRekommendationskod().getCode());
			assertEquals("kv_rekommendation_intyg", rekommendation.getRekommendationskod().getCodeSystemName());
			assertEquals("SJK2", rekommendation.getSjukdomskannedom().getCode());
			assertEquals("???", rekommendation.getSjukdomskannedom().getCodeSystem());
			assertEquals("kv_sjukdomskännedom_intyg", rekommendation.getSjukdomskannedom().getCodeSystemName());
		}
	
		List<Observation> observationer = extUtlatande.getObservationer();
		for (Observation observation : observationer){
			assertEquals("39104002", observation.getObservationskod().getCode());
			assertEquals("1.2.752.116.2.1.1.1", observation.getObservationskod().getCodeSystem());
			assertEquals("SNOMED-CT", observation.getObservationskod().getCodeSystemName());
		}

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
	
	private se.inera.certificate.common.v1.Utlatande unmarshallXml(String resource){
		InputStream stream = this.getClass().getClassLoader().getResourceAsStream(resource);
		return JAXB.unmarshal(stream, se.inera.certificate.common.v1.Utlatande.class);
	}
}
