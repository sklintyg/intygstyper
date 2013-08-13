package se.inera.certificate.modules.rli.model.converters;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.UUID;

import org.junit.Test;

import se.inera.certificate.model.Id;
import se.inera.certificate.model.Kod;
import se.inera.certificate.model.Patient;
import se.inera.certificate.modules.rli.model.external.Utlatande;

public class ExternalToInternalConverterTest {

	@Test
	public void testFromViewModel() {
		
		ExternalToInternalConverter converter = new ExternalToInternalConverter();
		
		Utlatande extUtlatande = buildExtUtlatande();
		
		se.inera.certificate.modules.rli.model.internal.Utlatande res = converter.fromExternalToInternal(extUtlatande);
		
		assertNotNull(res);
		assertNotNull(res.getUtlatandeId());
		assertNotNull(res.getTypAvUtlatande());
		
		assertNotNull(res.getPatient());
		
		se.inera.certificate.modules.rli.model.internal.Patient pat = res.getPatient();
		assertEquals("19121212-1212", pat.getPersonId());
		assertEquals("Abel Baker", pat.getForNamn());
		assertEquals("Smith Doe", pat.getEfterNamn());
		assertEquals("Abel Baker Smith Doe", pat.getFullstandigtNamn());
		
	}

	private Utlatande buildExtUtlatande() {
		
		Utlatande utl = new Utlatande();
		utl.setId(new Id("uuid", UUID.randomUUID().toString()));
		utl.setTyp(new Kod("RLI"));
		
		Patient pat = buildPatient();
		utl.setPatient(pat);
		
		return utl;
	}

	private Patient buildPatient() {
		
		Patient pat = new Patient();
		
		pat.setId(new Id("PersonId", "19121212-1212"));
		pat.setFornamns(Arrays.asList("Abel", "Baker"));
		pat.setEfternamns(Arrays.asList("Smith", "Doe"));
				
		return pat;
	}
	
}
