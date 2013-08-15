package se.inera.certificate.modules.rli.model.converters;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import se.inera.certificate.common.v1.PatientType;

import se.inera.certificate.modules.rli.model.external.common.Kod;
import se.inera.certificate.modules.rli.model.external.common.Arbetsuppgift;
import se.inera.certificate.modules.rli.model.external.common.Id;
import se.inera.certificate.modules.rli.model.external.common.Patient;
import se.inera.certificate.modules.rli.model.external.common.PatientRelation;

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
	
	private Patient buildPatient(){
		Patient patient = new Patient();
		patient.setAdress("Testvägen 23");
		List<Arbetsuppgift> arbetsuppgifts = new ArrayList<Arbetsuppgift>();
		Arbetsuppgift a = new Arbetsuppgift();
		a.setTypAvArbetsuppgift("Testare");
		arbetsuppgifts.add(a);
		patient.setArbetsuppgifts(arbetsuppgifts);
		List<String> eN = new ArrayList<String>();
		eN.add("Testsson");		
		List<String> fN = new ArrayList<String>();
		fN.add("Test");
		List<String> mN = new ArrayList<String>();
		mN.add("von");

		patient.setEfternamns(eN);
		patient.setFornamns(fN);
		patient.setMellannamns(mN);
		
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
		
		patientRelation.setEfternamns(relationEfterNamn);
		patientRelation.setFornamns(relationForNamn);
		patientRelation.setAdresses(relationAdr);
		patientRelation.setPersonId(relationId);
		patientRelation.setRelationskategori(new Kod("relationsKategori"));
		patientRelation.setRelationTyps(relationsTyps);
		patientRelation.setRelationskategori(new Kod("Gifta"));
		
		pR.add(patientRelation);
		
		
		patient.setPatientRelations(pR);
		
		return patient;
	}

}
