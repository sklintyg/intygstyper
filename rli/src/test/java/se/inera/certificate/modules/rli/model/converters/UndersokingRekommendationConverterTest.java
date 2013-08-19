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

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import se.inera.certificate.modules.rli.model.codes.ObservationsKod;
import se.inera.certificate.modules.rli.model.external.Utlatande;
import se.inera.certificate.modules.rli.model.external.common.Kod;
import se.inera.certificate.modules.rli.model.external.common.Observation;
import se.inera.certificate.modules.rli.model.internal.OrsakAvbokning;

public class UndersokingRekommendationConverterTest {

	private UndersokingRekommendationConverter converter;
	
	private se.inera.certificate.modules.rli.model.internal.Utlatande intUtlatande;
		
	@Before
	public void setUp() {
		this.converter = new UndersokingRekommendationConverter();
		this.intUtlatande = new se.inera.certificate.modules.rli.model.internal.Utlatande();
	}
	
	@Test
	public void testPopulateUndersokningRekommendationMedSjukdom() {
		
		Utlatande extUtlatande = buildExterntUtlatande(ObservationsKod.SJUKDOM);
		
		converter.populateUndersokningRekommendation(extUtlatande , intUtlatande);
		
		assertNotNull(intUtlatande.getOrsakForAvbokning());
		assertEquals(OrsakAvbokning.RESENAR_SJUK, intUtlatande.getOrsakForAvbokning());
		
	}
	
	@Test
	public void testPopulateUndersokningRekommendationMedGravid() {
		
		Utlatande extUtlatande = buildExterntUtlatande(ObservationsKod.GRAVIDITET);
		
		converter.populateUndersokningRekommendation(extUtlatande , intUtlatande);
		
		assertNotNull(intUtlatande.getOrsakForAvbokning());
		assertEquals(OrsakAvbokning.RESENAR_GRAVID, intUtlatande.getOrsakForAvbokning());
		
	}

	private Utlatande buildExterntUtlatande(ObservationsKod observationsKod) {
		
		Utlatande utlatande = new Utlatande();
		
		Observation obs = new Observation();
		obs.setObservationskod(new Kod(observationsKod.getCode()));
		
		utlatande.getObservationer().addAll(Arrays.asList(obs));
		
		return utlatande;
	}
	
}
