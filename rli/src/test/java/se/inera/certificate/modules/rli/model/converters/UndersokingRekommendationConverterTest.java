package se.inera.certificate.modules.rli.model.converters;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import se.inera.certificate.model.Kod;
import se.inera.certificate.model.Observation;
import se.inera.certificate.modules.rli.model.codes.ObservationsKod;
import se.inera.certificate.modules.rli.model.external.Utlatande;
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
		obs.setObservationsKod(new Kod(observationsKod.getCode()));
		
		utlatande.setObservations(Arrays.asList(obs));
		
		return utlatande;
	}
	
}
