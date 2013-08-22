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

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import se.inera.certificate.common.v1.AktivitetType;
import se.inera.certificate.common.v1.ObservationType;
import se.inera.certificate.common.v1.PatientType;
import se.inera.certificate.common.v1.RekommendationType;
import se.inera.certificate.common.v1.Utlatande;
import se.inera.certificate.model.Id;
import se.inera.certificate.model.Kod;
import se.inera.certificate.modules.rli.model.external.common.Aktivitet;
import se.inera.certificate.modules.rli.model.external.common.Arbetsuppgift;
import se.inera.certificate.modules.rli.model.external.common.Enhet;
import se.inera.certificate.modules.rli.model.external.common.Observation;
import se.inera.certificate.modules.rli.model.external.common.Patient;
import se.inera.certificate.modules.rli.model.external.common.PatientRelation;
import se.inera.certificate.modules.rli.model.external.common.Rekommendation;
import se.inera.certificate.modules.rli.model.external.common.Vardgivare;

public class ExternalToTransportConverterTest {

	private static ExternalToTransportConverterImpl converter;
	@Before
	public void setUp() throws Exception {
		converter = new ExternalToTransportConverterImpl();
	}

	@Test
	public void convertPatientExternalToTransportTest() {
		Patient source = buildPatient();
		PatientType patient = converter.convertPatient(source);
		
		assertEquals("Testvägen 23", patient.getAdress());	
	}
	
	@Test
	public void convertObservationExternalToTransportTest(){
		Observation source = buildObservation();
		ObservationType obs = converter.convertObservation(source);
		
		assertEquals("SNOMED-CT", obs.getObservationskod().getCode());
	}
	
	@Test
	public void convertAktivitetExternalToTransportTest(){
		Aktivitet source = buildAktivitet();
		AktivitetType akt = converter.convertAktivitet(source);
		assertEquals("AktivitetKod",akt.getAktivitetskod().getCode());
		assertEquals("Enhetsnamn", akt.getUtforsVidEnhet().getEnhetsnamn());
		assertEquals("Vårdenhet", akt.getUtforsVidEnhet().getArbetsplatskod().getExtension());
	}
	@Test
	public void convertRekommendationExternalToTransportTest(){
		Rekommendation source = buildRekommendation();
		RekommendationType rType = converter.convertRekommendation(source);
		assertEquals("Rek-kod", rType.getRekommendationskod().getCode());
		assertEquals("Sjuk-kännedom", rType.getSjukdomskannedom().getCode());
		assertEquals("Rek-beskrivning", rType.getBeskrivning());			
	}
	
	@Test
	public void convertKommentarer(){
		se.inera.certificate.modules.rli.model.external.Utlatande source = 
				new se.inera.certificate.modules.rli.model.external.Utlatande();
		List<String> l = new ArrayList<String>();
		l.add("KOMMENTARER");
		source.getKommentarer().addAll(l);
		
		Utlatande utl = converter.externalToTransport(source);
		
		List<String> komList = utl.getKommentars();
		
		assertEquals("KOMMENTARER", komList.get(0));
		
	}
		
	private Rekommendation buildRekommendation() {
		Rekommendation rek = new Rekommendation();
		rek.setRekommendationskod(new Kod("Rek-kod"));
		rek.setSjukdomskannedom(new Kod("Sjuk-kännedom"));
		rek.setBeskrivning("Rek-beskrivning");
		return rek;
	}

	private Patient buildPatient(){
		Patient patient = new Patient();
		patient.setAdress("Testvägen 23");
		List<Arbetsuppgift> arbetsuppgifts = new ArrayList<Arbetsuppgift>();
		Arbetsuppgift a = new Arbetsuppgift();
		a.setTypAvArbetsuppgift("Testare");
		arbetsuppgifts.add(a);
		patient.getArbetsuppgifts().addAll(arbetsuppgifts);
		List<String> eN = new ArrayList<String>();
		eN.add("Testsson");		
		List<String> fN = new ArrayList<String>();
		fN.add("Test");
		List<String> mN = new ArrayList<String>();
		mN.add("von");

		patient.getEfternamns().addAll(eN);
		patient.getFornamns().addAll(fN);
		patient.getMellannamns().addAll(mN);
		
		List<PatientRelation> pR = new ArrayList<PatientRelation>();
		
		PatientRelation patientRelation = new PatientRelation();
	
		List<String> relationAdr = new ArrayList<String>();
		relationAdr.add("Relationsvägen 1");
		
		Id relationId = new Id();
		relationId.setExtension("relationID");
		
		List<String> relationForNamn = new ArrayList<String>();
		relationForNamn.add("RelationNamn");
		
		List<String> relationEfterNamn = new ArrayList<String>();
		relationEfterNamn.add("RelationEfterNamn");
		
		List<Kod> relationsTyps = new ArrayList<Kod>();
		relationsTyps.add(new Kod("RelationsTypsKod"));
		
		patientRelation.getEfternamns().addAll(relationEfterNamn);
		patientRelation.getFornamns().addAll(relationForNamn);
		patientRelation.getAdresses().addAll(relationAdr);
		patientRelation.setPersonId(relationId);
		patientRelation.setRelationskategori(new Kod("relationsKategori"));
		patientRelation.getRelationTyps().addAll(relationsTyps);
		patientRelation.setRelationskategori(new Kod("Gifta"));
		
		pR.add(patientRelation);
		
		
		patient.getPatientRelations().addAll(pR);
		
		return patient;
	}
	
	private Observation buildObservation(){
		Observation observation = new Observation();
		observation.setBeskrivning("Observationsbeskrivning");
		observation.setForekomst(true);
		observation.setObservationskod(new Kod("SNOMED-CT"));
		return observation;
	}
	
	/*private Arrangemang buildArrangemang(){
		Arrangemang arr = new Arrangemang();
		arr.setArrangemangstyp(new Kod("resa"));
		arr.setBokningsreferens("bokningsreferens");
		arr.setPlats("New York");
		return arr;
	}*/
	
	private Enhet buildEnhet(){
		Enhet enhet = new Enhet();
		enhet.setArbetsplatskod(new Id("Vårdenhet"));
		enhet.setEnhetsId(new Id("enhetsId"));
		enhet.setEnhetsnamn("Enhetsnamn");
		
		Vardgivare vardgivare = new Vardgivare();
		vardgivare.setVardgivareId(new Id("vårdgivarID"));
		vardgivare.setVardgivarnamn("Vårdgivarnamn");
		
		enhet.setVardgivare(vardgivare);
		return enhet;
	}
	private Aktivitet buildAktivitet(){
		Aktivitet aktivitet = new Aktivitet();
		aktivitet.setAktivitetskod(new Kod("AktivitetKod"));
		aktivitet.setUtforsVidEnhet(buildEnhet());
		return aktivitet;
	}

}
