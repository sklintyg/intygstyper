package se.inera.certificate.modules.rli.model.converters;

import static org.junit.Assert.assertEquals;
import iso.v21090.dt.v1.CD;
import iso.v21090.dt.v1.II;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTimeFieldType;
import org.joda.time.Partial;
import org.junit.Before;
import org.junit.Test;

import riv.insuranceprocess.healthreporting._2.HosPersonalType;
import se.inera.certificate.common.v1.ObservationType;
import se.inera.certificate.common.v1.PartialDateInterval;
import se.inera.certificate.common.v1.PatientType;
import se.inera.certificate.common.v1.UtforarrollType;
import se.inera.certificate.modules.rli.model.external.Arrangemang;
import se.inera.certificate.modules.rli.model.external.common.HosPersonal;
import se.inera.certificate.modules.rli.model.external.common.Observation;
import se.inera.certificate.modules.rli.model.external.common.Patient;

/**
 * Test class for TransportToExternal,
 * contains methods for setting up Utlatande using both the transport model and the external model,
 * and populating each with mock data
 * 
 * @author erik
 *
 */

public class TransportToExternalConverterTest {
	
	private TransportToExternalConverterImpl converter;

	private ObservationType buildObservationType(){
		CD cd = new CD();
		cd.setCode("testCode");
		
		PartialDateInterval pdi = new PartialDateInterval();
		
		pdi.setFrom(new Partial()
	     .with(DateTimeFieldType.dayOfWeek(), 5)
	     .with(DateTimeFieldType.hourOfDay(), 12)
	     .with(DateTimeFieldType.minuteOfHour(), 20));
		
		pdi.setTom(new Partial()
	     .with(DateTimeFieldType.dayOfWeek(), 6)
	     .with(DateTimeFieldType.hourOfDay(), 12)
	     .with(DateTimeFieldType.minuteOfHour(), 20));
		
		ObservationType observation = new ObservationType();
		observation.setObservationskod( cd );
		observation.setObservationsperiod(pdi);
		
		UtforarrollType utforare = new UtforarrollType();
		utforare.setUtforartyp(cd);
		HosPersonalType hpt = new HosPersonalType();
		hpt.setFullstandigtNamn("Utforar Utforarsson");
		utforare.setAntasAv(hpt);
		
		observation.setUtforsAv(utforare);
		return observation;
	}
	
	private PatientType buildPatientType(){
		PatientType pt = new PatientType();
		II patientID = new II();
		patientID.setIdentifierName("patientID");
		patientID.setExtension("patientID");
		pt.setPersonId(patientID);
		pt.setAdress("Testgatan 23");
		return pt;
	}
	
	private se.inera.certificate.rli.v1.Arrangemang buildArrangemang(){
	
		se.inera.certificate.rli.v1.Arrangemang arr = new se.inera.certificate.rli.v1.Arrangemang();

		PartialDateInterval pdi = new PartialDateInterval();
		
		pdi.setFrom(new Partial()
	     .with(DateTimeFieldType.dayOfWeek(), 5)
	     .with(DateTimeFieldType.hourOfDay(), 12)
	     .with(DateTimeFieldType.minuteOfHour(), 20));
		
		pdi.setTom(new Partial()
	     .with(DateTimeFieldType.dayOfWeek(), 6)
	     .with(DateTimeFieldType.hourOfDay(), 12)
	     .with(DateTimeFieldType.minuteOfHour(), 20));
		arr.setArrangemangstid(pdi);
		
		CD cd = new CD();
		cd.setCode("testCode");
		arr.setArrangemangstyp(cd);
	
		arr.setBokningsreferens("bokningsReferens");
		arr.setPlats("arrangemangsPlats");
	
		
		arr.setAvbestallningsdatum(new Partial()
			.with(DateTimeFieldType.dayOfMonth(),2)
			.with(DateTimeFieldType.year(), 2002)
			.with(DateTimeFieldType.monthOfYear(), 11));
		
		arr.setBokningsdatum( new Partial()
			.with(DateTimeFieldType.dayOfMonth(), 1)
			.with(DateTimeFieldType.year(), 2002)
			.with(DateTimeFieldType.monthOfYear(), 12));
		
		return arr;
	}

	
	@Before
	public void setUp(){
		//Initiate converter
		converter = new TransportToExternalConverterImpl();
		
	}
	
	@Test
	public void testConvertPatient(){
		PatientType transportPatient = buildPatientType();
		
		Patient externalPatient = converter.convertPatient(transportPatient);
		
		assertEquals(transportPatient.getPersonId().getExtension(), 
				externalPatient.getPersonId().getExtension());
		
		assertEquals(transportPatient.getAdress(), externalPatient.getAdress());
		
		
	}
	@Test 
	public void testConvertSkapadAv(){
		
		HosPersonalType transportSkapadAv = new HosPersonalType();
		transportSkapadAv.setFullstandigtNamn("Skapad af Skapadson");
		
		HosPersonal externalSkapadAv = converter.convertHosPersonal(transportSkapadAv);		
		
		assertEquals(transportSkapadAv.getFullstandigtNamn(), externalSkapadAv.getFullstandigtNamn());

		assertEquals(transportSkapadAv.getEnhet(), externalSkapadAv.getEnhet());
		
		assertEquals(transportSkapadAv.getForskrivarkod(), externalSkapadAv.getForskrivarkod());

	}
	
	@Test
	public void testConvertArrangemang(){
		se.inera.certificate.rli.v1.Arrangemang transportArr = 
				buildArrangemang();
		Arrangemang externalArr = converter.convertArrangemang(transportArr);
		
		
		assertEquals(transportArr.getBokningsreferens(),
				externalArr.getBokningsreferens());

		assertEquals(transportArr.getPlats(),
				externalArr.getPlats());
		
		assertEquals(transportArr.getArrangemangstid().getFrom(),
				externalArr.getArrangemangstid().getFrom());
		
		assertEquals(transportArr.getArrangemangstid().getTom(),
				externalArr.getArrangemangstid().getTom());
		
		assertEquals(transportArr.getArrangemangstyp().getCode().toString(),
				externalArr.getArrangemangstyp().getCode().toString());
		
		assertEquals(transportArr.getAvbestallningsdatum(), externalArr.getAvbestallningsdatum());
		
		assertEquals(transportArr.getBokningsdatum(), externalArr.getBokningsdatum());
	}
	
		
	@Test
	public void testConvertObservation(){
		ObservationType transportObs = buildObservationType();
		List<ObservationType> transportList = new ArrayList<>();
		List<Observation> externalList = new ArrayList<>();
		
		transportList.add(transportObs);
		externalList = converter.convertObservations(transportList);
		Observation externalObs = externalList.get(0);
		
		assertEquals(transportObs.getObservationsperiod().getFrom(), 
				externalObs.getObservationsperiod().getFrom());

		assertEquals(transportObs.getObservationsperiod().getTom(), 
				externalObs.getObservationsperiod().getTom());
		
		assertEquals(transportObs.getUtforsAv().getAntasAv().getFullstandigtNamn(),
				externalObs.getUtforsAv().getAntasAv().getFullstandigtNamn());
	}

}