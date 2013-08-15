package se.inera.certificate.modules.rli.model.converters;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import se.inera.certificate.model.HosPersonal;
import se.inera.certificate.model.Id;
import se.inera.certificate.model.Kod;
import se.inera.certificate.model.Patient;
import se.inera.certificate.model.Vardenhet;
import se.inera.certificate.model.Vardgivare;
import se.inera.certificate.modules.rli.model.external.Arrangemang;
import se.inera.certificate.modules.rli.model.internal.HoSPersonal;
import se.inera.certificate.modules.rli.model.internal.Utfardare;

public class ExternalToInternalConverterTest {

	private ExternalToInternalConverterImpl converter;

	@Before
	public void setUp() {
		converter = new ExternalToInternalConverterImpl();
	}

	@Test
	public void testConvertToIntPatient() {

		Patient extPatient = buildPatient();

		se.inera.certificate.modules.rli.model.internal.Patient res = converter
				.convertToIntPatient(extPatient);

		assertNotNull(res);

		assertEquals("19121212-1212", res.getPersonId());
		assertEquals("Abel Baker", res.getForNamn());
		assertEquals("Smith Doe", res.getEfterNamn());
		assertEquals("Abel Baker Smith Doe", res.getFullstandigtNamn());

	}

	private Patient buildPatient() {

		Patient pat = new Patient();

		pat.setId(new Id("PersonId", "19121212-1212"));
		pat.setFornamns(Arrays.asList("Abel", "Baker"));
		pat.setEfternamns(Arrays.asList("Smith", "Doe"));

		return pat;
	}

	@Test
	public void testConvertToIntArrangemang() {

		Arrangemang extArr = buildArrangemang();
		se.inera.certificate.modules.rli.model.internal.Arrangemang res = converter
				.convertToIntArrangemang(extArr);

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

	@Test
	public void testConvertToIntHoSPersonal() {

		HosPersonal extHoSPersonal = buildHoSPersonal();
		HoSPersonal res = converter.convertToIntHoSPersonal(extHoSPersonal);

		assertNotNull(res);

	}

	@Test
	public void testConvertToIntUtfardare() {

		HosPersonal extHoSPersonal = buildHoSPersonal();
		Utfardare res = converter.convertToIntUtfardare(extHoSPersonal);

		assertNotNull(res);

		assertNotNull(res.getHosPersonal());
		assertNotNull(res.getVardenhet());
	}

	@Test
	public void testConvertToIntVardenhet() {
		
		Vardenhet extVardenhet = buildVardenhet();
		se.inera.certificate.modules.rli.model.internal.Vardenhet res = converter.convertToIntVardenhet(extVardenhet);
		
		assertNotNull(res);
		
		assertNotNull(res.getEnhetsId());
		assertNotNull(res.getEnhetsNamn());
		assertNotNull(res.getPostAddress());
		assertNotNull(res.getPostNummer());
		assertNotNull(res.getPostOrt());
		assertNotNull(res.getePost());
		assertNotNull(res.getTelefonNummer());
		assertNotNull(res.getVardgivare());
	}
	
	private HosPersonal buildHoSPersonal() {

		HosPersonal hosPers = new HosPersonal();

		hosPers.setId(new Id("19101010-1010"));
		hosPers.setNamn("Börje Dengroth");
		hosPers.setForskrivarkod("12345-67");

		Vardenhet vardenhet = buildVardenhet();
		hosPers.setVardenhet(vardenhet);

		return hosPers;
	}

	private Vardenhet buildVardenhet() {

		Vardenhet vardenhet = new Vardenhet();

		vardenhet.setId(new Id("123-456"));
		vardenhet.setArbetsplatskod(new Id("1234-56"));
		vardenhet.setNamn("Tolvberga Vårdcentral");
		vardenhet.setPostadress("Nollstigen 12");
		vardenhet.setPostnummer("12345");
		vardenhet.setPostort("Tolvberga");
		vardenhet.setTelefonnummer("012-345678");
		vardenhet.setEpost("ingen@alls.nu");

		Vardgivare vardgivare = buildVardgivare();
		vardenhet.setVardgivare(vardgivare);

		return vardenhet;
	}

	private Vardgivare buildVardgivare() {

		Vardgivare vardgivare = new Vardgivare();

		vardgivare.setId(new Id("1234567"));
		vardgivare.setNamn("Landstinget");

		return vardgivare;

	}

}
