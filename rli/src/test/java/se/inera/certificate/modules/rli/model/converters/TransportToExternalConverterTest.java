package se.inera.certificate.modules.rli.model.converters;

import java.util.ArrayList;
import java.util.List;

import iso.v21090.dt.v1.CD;
import iso.v21090.dt.v1.II;

import org.joda.time.Chronology;
import org.joda.time.DateTimeFieldType;
import org.joda.time.LocalDateTime;
import org.joda.time.Partial;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.assertEquals;

import riv.insuranceprocess.healthreporting._2.HosPersonalType;
import se.inera.certificate.common.v1.PartialDateInterval;
import se.inera.certificate.common.v1.PatientType;
import se.inera.certificate.common.v1.ObservationType;
import se.inera.certificate.common.v1.UtforarrollType;

/*import se.inera.certificate.model.Aktivitet;
import se.inera.certificate.model.Referens;
import se.inera.certificate.model.Vardkontakt;
*/
import se.inera.certificate.model.Observation;
import se.inera.certificate.model.Id;
import se.inera.certificate.model.Kod;
import se.inera.certificate.model.HosPersonal;
import se.inera.certificate.model.PartialInterval;
import se.inera.certificate.model.Patient;

import se.inera.certificate.modules.rli.model.external.Arrangemang;
import se.inera.certificate.modules.rli.model.external.Utlatande;
import se.inera.certificate.modules.rli.model.converters.TransportToExternalConverter;

/**
 * Test class for TransportToExternal,
 * contains methods for setting up Utlatande using both the transport model and the external model,
 * and populating each with mock data
 * 
 * @author erik
 *
 */

public class TransportToExternalConverterTest {
	
	private se.inera.certificate.common.v1.Utlatande transportModel;
	private Utlatande correctExternalModel;
	private Chronology chrono;
	private TransportToExternalConverter converter;
	private Utlatande utlatande;
	
	/**
	 * Sets up an instance of the transport model with mock data for testing purposes
	 * 
	 * @return se.inera.certificate.common.v1.Utlatande 
	 */
	private se.inera.certificate.common.v1.Utlatande createTransportModel(){
		
		se.inera.certificate.common.v1.Utlatande tmpUtl
			= new se.inera.certificate.common.v1.Utlatande();
		
		
		II ii = new II();
		ii.setIdentifierName("testID");
		ii.setExtension("testID");
		tmpUtl.setUtlatandeId( ii );
		
		CD cd = new CD();
		cd.setCode("testCode");
		tmpUtl.setTypAvUtlatande(cd);
		
		tmpUtl.setSkickatdatum(new LocalDateTime(2001, 12, 1, 12, 1) );
		
		HosPersonalType hsp = new HosPersonalType();
		hsp.setFullstandigtNamn("Skapad af Skapadson");
		tmpUtl.setSkapadAv(hsp);
		
		tmpUtl.setSigneringsdatum(new LocalDateTime (2002, 12, 1, 12, 1) );
		
		PatientType pt = new PatientType();
		II patientID = new II();
		patientID.setIdentifierName("patientID");
		patientID.setExtension("patientID");
		pt.setPersonId(patientID);
		pt.setAdress("Testvägen 1");
		tmpUtl.setPatient(pt);
		
		se.inera.certificate.rli.v1.Arrangemang arr = new se.inera.certificate.rli.v1.Arrangemang();
		PartialDateInterval pdi = new PartialDateInterval();
		
		pdi.setFrom(new Partial(chrono)
	     .with(DateTimeFieldType.dayOfWeek(), 5)
	     .with(DateTimeFieldType.hourOfDay(), 12)
	     .with(DateTimeFieldType.minuteOfHour(), 20));
		
		pdi.setTom(new Partial(chrono)
	     .with(DateTimeFieldType.dayOfWeek(), 6)
	     .with(DateTimeFieldType.hourOfDay(), 12)
	     .with(DateTimeFieldType.minuteOfHour(), 20));
		arr.setArrangemangstid(pdi);
		
		arr.setArrangemangstyp(cd);
		
		arr.setAvbestallningsdatum(new Partial(chrono)
			.with(DateTimeFieldType.dayOfMonth(),2)
			.with(DateTimeFieldType.year(), 2002)
			.with(DateTimeFieldType.monthOfYear(), 11));
		
		arr.setBokningsdatum( new Partial(chrono)
			.with(DateTimeFieldType.dayOfMonth(), 1)
			.with(DateTimeFieldType.year(), 2002)
			.with(DateTimeFieldType.monthOfYear(), 12));
		
		tmpUtl.setArrangemang(arr);
		
		//Create observation and utforare for observation
		ObservationType observation = new ObservationType();
		observation.setObservationskod( cd );
		observation.setObservationsperiod(pdi);
		
		UtforarrollType utforare = new UtforarrollType();
		utforare.setUtforartyp(cd);
		
		observation.setUtforsAv(utforare);
		tmpUtl.getObservations().add(observation);
		
		
		return tmpUtl;
	}
	
	/**
	 * Sets up a correct external model from based on the same mock data as used in createTransportModel()
	 * for testing purposes
	 * 
	 * @return se.inera.certificate.modules.rli.model.external.Utlatande
	 */
	private Utlatande createCorrectExternalModel() {
		Utlatande tmpUtl = new Utlatande();
		
		tmpUtl.setId(new Id("testID"));
	
		tmpUtl.setTyp(new Kod("testCode"));
	
		
		tmpUtl.setSkickatDatum(new LocalDateTime(2001, 12, 1, 12, 1) );
		
		HosPersonal hsp = new HosPersonal();
		hsp.setNamn("Skapad af Skapadson");
		
		tmpUtl.setSkapadAv(hsp);
		
		tmpUtl.setSigneringsDatum(new LocalDateTime (2002, 12, 1, 12, 1) );
		
		Patient pt = new Patient();
		pt.setId(new Id("patientID"));
		
		
		tmpUtl.setPatient(pt);
		
		Arrangemang arr = new Arrangemang();
		
		PartialDateInterval pdi = new PartialDateInterval();
		
		pdi.setFrom(new Partial(chrono)
	     .with(DateTimeFieldType.dayOfWeek(), 5)
	     .with(DateTimeFieldType.hourOfDay(), 12)
	     .with(DateTimeFieldType.minuteOfHour(), 20));
		
		pdi.setTom(new Partial(chrono)
	     .with(DateTimeFieldType.dayOfWeek(), 6)
	     .with(DateTimeFieldType.hourOfDay(), 12)
	     .with(DateTimeFieldType.minuteOfHour(), 20));
		arr.setArrangemangstid(pdi);
		
		arr.setArrangemangstyp(new Kod("testCode"));
		
		arr.setAvbestallningsdatum(new Partial(chrono)
			.with(DateTimeFieldType.dayOfMonth(),2)
			.with(DateTimeFieldType.year(), 2002)
			.with(DateTimeFieldType.monthOfYear(), 11));
		
		arr.setBokningsdatum( new Partial(chrono)
			.with(DateTimeFieldType.dayOfMonth(), 1)
			.with(DateTimeFieldType.year(), 2002)
			.with(DateTimeFieldType.monthOfYear(), 12));
		
		tmpUtl.setArrangemang(arr);
		
		Observation observation = new Observation();
		observation.setObservationsKod(new Kod("testCode"));
		observation.setObservationsPeriod(new PartialInterval( pdi.getFrom(), pdi.getTom() ));
		List<Observation> obsList = new ArrayList<Observation>();
		obsList.add(observation);
		
		tmpUtl.setObservations(obsList);
		
		return tmpUtl;
	}
	
	@Before
	public void init(){
		//Initiate the respective Utlatande
		transportModel = createTransportModel();
		correctExternalModel = createCorrectExternalModel();
		//Initiate converter
		converter = new TransportToExternalConverter();
		
		//Setup a new instance of Utlatande to store the converted object
		utlatande = new Utlatande();
		//Convert from transport to extarnal model
		utlatande = converter.transportToExternal(transportModel);
	}
	
	@Test
	public void testConvertId(){		
		assertEquals("ID in converted model (checked against transport) was: " 
				+ utlatande.getId().getExtension() + "\n", 
				transportModel.getUtlatandeId().getExtension(), utlatande.getId().getExtension());
		
		assertEquals("ID in converted model (checked against correctExternal) was: " 
				+ utlatande.getId().getExtension() + "\n", 
				correctExternalModel.getId().getExtension(), utlatande.getId().getExtension());
	}
	
	@Test
	public void testConvertTyp(){
		assertEquals("Typ in converted model (checked against transport) was: " 
				+ utlatande.getTyp().getCode() + "\n",
				transportModel.getTypAvUtlatande().getCode(), utlatande.getTyp().getCode());
		
		assertEquals("Typ in converted model (checked against correctExternal) was: " 
				+ utlatande.getTyp().getCode() + "\n",
				correctExternalModel.getTyp().getCode(), utlatande.getTyp().getCode());
	}
	
	@Test
	public void testConvertSigneringsDatum(){
		assertEquals("Sign date in converted model (checked against transport) was: "
				+ utlatande.getSigneringsDatum() + "\n",
				transportModel.getSigneringsdatum(), utlatande.getSigneringsDatum());
		
		assertEquals("Sign date in converted model (checked against correctExternal) was: "
				+ utlatande.getSigneringsDatum() + "\n",
				correctExternalModel.getSigneringsDatum(), utlatande.getSigneringsDatum());
	}
	
	@Test
	public void testConvertSkickatDatum(){		
		assertEquals("SkickatDatum in converted model (checked against transportModel) was: "
				+ utlatande.getSkickatDatum() + "\n",
				transportModel.getSkickatdatum(), utlatande.getSkickatDatum());
		
		assertEquals("SkickatDatum in converted model (checked against correctExternal) was: " 
				+ utlatande.getSkickatDatum() + "\n",
				correctExternalModel.getSkickatDatum(), utlatande.getSkickatDatum());
	}		
	@Test
	public void testConvertPatient(){
		assertEquals("Patient in converted model (checked against transportModel) was: " 
				+ utlatande.getPatient().getId().getExtension() + "\n",
				transportModel.getPatient().getPersonId().getExtension(), utlatande.getPatient().getId().getExtension());
		
		assertEquals("Patient in converted model (checked against correctExternal) was: " 
				+ utlatande.getPatient().getId().getExtension() + "\n",
				correctExternalModel.getPatient().getId().getExtension(), utlatande.getPatient().getId().getExtension());
	}
	@Test 
	public void testConvertSkapadAv(){
		assertEquals("Skapad av in converted model (checked against transportModel) was: " 
				+ utlatande.getSkapadAv().getNamn() + "\n",
				transportModel.getSkapadAv().getFullstandigtNamn(), utlatande.getSkapadAv().getNamn());
		
		assertEquals("Skapad av in converted model (as checked against correctExternal) was: "
				+ utlatande.getSkapadAv().getNamn() + "\n",
				correctExternalModel.getSkapadAv().getNamn(), utlatande.getSkapadAv().getNamn());
	}
	@Test
	public void testConvertArrangemang(){
		//arrangemang -> arrangemangsTyp used to assert
		assertEquals("Arrangemang in converted model (checked against transport) was: " 
				+ utlatande.getArrangemang().getArrangemangstyp().getCode() + "\n",
				transportModel.getArrangemang().getArrangemangstyp().getCode(), 
				utlatande.getArrangemang().getArrangemangstyp().getCode());

		assertEquals("Arrangemang in converted model (checked against correctExternal) was: " 
				+ utlatande.getArrangemang().getArrangemangstyp().getCode() + "\n",
				correctExternalModel.getArrangemang().getArrangemangstyp().getCode(), 
				utlatande.getArrangemang().getArrangemangstyp().getCode());
	}
	
	@Test
	public void testConvertObservation(){
		//Observation -> observationkod used to assert
		assertEquals("Observationskod in converted model (checked against transportModel) was: "
				+ utlatande.getObservations().get(0) + "\n", 
				transportModel.getObservations().get(0).getObservationskod().getCode(),
				utlatande.getObservations().get(0).getObservationsKod().getCode());
		
		assertEquals("Observationskod in converted model (checked against correctExternalModel) was: "
				+ utlatande.getObservations().get(0) + "\n", 
				correctExternalModel.getObservations().get(0).getObservationsKod().getCode(),
				utlatande.getObservations().get(0).getObservationsKod().getCode());
		
	}

}