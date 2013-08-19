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
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import se.inera.certificate.modules.rli.model.external.Arrangemang;
import se.inera.certificate.modules.rli.model.internal.HoSPersonal;
import se.inera.certificate.modules.rli.model.internal.Utfardare;
import se.inera.certificate.modules.rli.model.external.common.Enhet;
import se.inera.certificate.modules.rli.model.external.common.HosPersonal;
import se.inera.certificate.modules.rli.model.external.common.Id;
import se.inera.certificate.modules.rli.model.external.common.Kod;
import se.inera.certificate.modules.rli.model.external.common.Patient;
import se.inera.certificate.modules.rli.model.external.common.Vardgivare;

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

		pat.setPersonId(new Id("PersonId", "19121212-1212"));
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
		
		Enhet extVardenhet = buildVardenhet();
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

		hosPers.setPersonalId(new Id("19101010-1010"));
		hosPers.setFullstandigtNamn("Börje Dengroth");
		hosPers.setForskrivarkod("12345-67");

		Enhet vardenhet = buildVardenhet();
		hosPers.setEnhet(vardenhet);

		return hosPers;
	}

	private Enhet buildVardenhet() {

		Enhet vardenhet = new Enhet();

		vardenhet.setEnhetsId(new Id("123-456"));
		vardenhet.setArbetsplatskod(new Id("1234-56"));
		vardenhet.setEnhetsnamn("Tolvberga Vårdcentral");
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

		vardgivare.setVardgivareId(new Id("1234567"));
		vardgivare.setVardgivarnamn("Landstinget");

		return vardgivare;

	}

}
