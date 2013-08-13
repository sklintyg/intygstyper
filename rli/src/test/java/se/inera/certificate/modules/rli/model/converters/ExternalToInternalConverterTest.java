package se.inera.certificate.modules.rli.model.converters;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import se.inera.certificate.modules.rli.model.external.Arrangemang;
import se.inera.certificate.modules.rli.model.external.common.Id;
import se.inera.certificate.modules.rli.model.external.common.Kod;
import se.inera.certificate.modules.rli.model.external.common.Patient;

public class ExternalToInternalConverterTest {
	
	private ExternalToInternalConverterImpl converter;
	
	@Before
	public void setUp() {
		converter = new ExternalToInternalConverterImpl();
	}
	

	@Test
	public void testConvertToIntPatient() {
				
		Patient extPatient = buildPatient();
		
		se.inera.certificate.modules.rli.model.internal.Patient res = converter.convertToIntPatient(extPatient);
				
		assertNotNull(res);
		
		assertEquals("19121212-1212", res.getPersonId());
		assertEquals("Abel Baker", res.getForNamn());
		assertEquals("Smith Doe", res.getEfterNamn());
		assertEquals("Abel Baker Smith Doe", res.getFullstandigtNamn());
		
	}

	private Patient buildPatient() {
		
		Patient pat = new Patient();
		
		pat.setPersonId(new Id("PersonId", "19121212-1212"));
		pat.setFornamns(Arrays.asList("Abel", "Baker"));
		pat.setEfternamns(Arrays.asList("Smith", "Doe"));
				
		return pat;
	}


	@Test
	public void testConvertToIntArrangemang() {
		
		Arrangemang extArr = buildArrangemang();
		se.inera.certificate.modules.rli.model.internal.Arrangemang res = converter.convertToIntArrangemang(extArr);
		
		assertNotNull(res);
		assertNotNull(res.getArrangemangsTyp());
		assertNotNull(res.getBokningsReferens());
		
	}
	
	private Arrangemang buildArrangemang() {
		
		Arrangemang arr = new Arrangemang();
		arr.setArrangemangstyp(new Kod("420008001"));
		arr.setBokningsreferens("1234567-890");
		arr.setPlats("Långtbortistan");
		
		return arr;
	}
	
	
}
