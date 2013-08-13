package se.inera.certificate.modules.rli.model.converters;

import java.util.ArrayList;
import java.util.List;

import se.inera.certificate.common.v1.AktivitetType;
import se.inera.certificate.common.v1.ObservationType;
import se.inera.certificate.common.v1.PatientType;

import se.inera.certificate.model.Aktivitet;
import se.inera.certificate.model.HosPersonal;
import se.inera.certificate.model.Observation;
import se.inera.certificate.model.PartialInterval;
import se.inera.certificate.model.Patient;
import se.inera.certificate.model.Vardenhet;
import se.inera.certificate.model.Vardgivare;

import se.inera.certificate.modules.rli.model.external.Utlatande;
import se.inera.certificate.modules.rli.model.external.Arrangemang;

//We might want to move IsoTypeConverter or think of some other way to access it, should not live here maybe.
import se.inera.certificate.modules.rli.model.converters.IsoTypeConverter;

import riv.insuranceprocess.healthreporting._2.EnhetType;
import riv.insuranceprocess.healthreporting._2.HosPersonalType;
import riv.insuranceprocess.healthreporting._2.VardgivareType;


/**
 * Converter between transport and external model 
 * @author erik
 *
 */
public class TransportToExternalConverter {

	/**
	 * Converts from the transport format (se.inera.certificate.common.v1.Utlatande) to
	 * the external format (se.inera.certificate.modules.rli.model.external.Utlatande)
	 * 
	 * @param source Utlatande in the transport format to be converted to external format
	 * @return se.inera.certificate.modules.rli.model.external.Utlatande
	 */
	public Utlatande transportToExternal(se.inera.certificate.common.v1.Utlatande source) {
		
		Utlatande externalModel = new Utlatande();

		externalModel.setId(IsoTypeConverter.toId(source.getUtlatandeId()));
		
		externalModel.setTyp(IsoTypeConverter.toKod(source.getTypAvUtlatande()));

		externalModel.setKommentars(source.getKommentars());
		
		externalModel.setSigneringsDatum(source.getSigneringsdatum());
		
		externalModel.setSkickatDatum(source.getSkickatdatum());

		externalModel.setPatient(convert(source.getPatient()));

		externalModel.setSkapadAv(convert(source.getSkapadAv()));

		externalModel.setAktiviteter(convertAktiviteter(source.getAktivitets()));

		externalModel.setObservations(convertObservations(source.getObservations()));
		
		externalModel.setArrangemang(convert(source.getArrangemang()));
				
		return externalModel;
	}

	/**
	 * Iterates over a list of objects of the type ObservationType and converts them to
	 * type Observation
	 * 
	 * @param source List of objects of type ObservationType to be converted
	 * @return List of objects of type Observation
	 */
	private List<Observation> convertObservations(List<ObservationType> source) {
		List<Observation> observations = new ArrayList<Observation>();

		for (ObservationType ot : source){
			Observation observation = convert(ot);
			if (observation != null)
				observations.add(observation);
		}
		return observations;
	}
	
	/**
	 * Converts an object from ObservationType to Observation,
	 * this needs more work.
	 * 
	 * @param source ObservationType to be converted
	 * @return object of type Observation
	 */
	private Observation convert (ObservationType source){
		Observation observation = new Observation();
		
		if (source.getObservationsperiod() != null){
			observation.setObservationsPeriod( new PartialInterval(source.getObservationsperiod().getFrom(), 
					source.getObservationsperiod().getTom()));
		}
		observation.setObservationsKod( IsoTypeConverter.toKod( source.getObservationskod() ));
		observation.setBeskrivning("placeholder");
		
		
		// Needs to be added to model
		//observation.setPrognos(source.g);	
		//observation.setObservationsKategori();
		//observation.setVarde();
		
		return observation;
		
	}
	
	/**
	 * Internal converter for converting Arrangemang from the transportmodel for use in the external model
	 * (mainly converts from isoType CD and II to Kod and Id)
	 * 
	 * @param source Arrangemang in transport format (se.inera.certificate.rli.v1.Arrangemang) to be converted to external format
	 * @return se.inera.certificate.modules.rli.model.external.Arrangemang, or null if source is null
	 */
	private static Arrangemang convert (se.inera.certificate.rli.v1.Arrangemang source){
		if( source == null) return null;
		Arrangemang arrangemang = new Arrangemang();
		
		arrangemang.setArrangemangstid(source.getArrangemangstid());
		arrangemang.setArrangemangstyp( IsoTypeConverter.toKod( source.getArrangemangstyp() ) );
		arrangemang.setAvbestallningsdatum( source.getAvbestallningsdatum() );
		arrangemang.setBokningsdatum( source.getBokningsdatum() );
		arrangemang.setBokningsreferens( source.getBokningsreferens() );
		
		arrangemang.setPlats( source.getPlats() );
		
		return arrangemang;
	}
	
	/**
	 * Iterates through a list of AktivitetType and converts each subsequent item from AktivitetType 
	 * to the Aktivitet that the external model understands.
	 * 
	 * @param source the list of AktivitetType's to be converted
	 * @return a List containing Aktiviteter
	 */
    private static List<Aktivitet> convertAktiviteter(List<AktivitetType> source) {
        List<Aktivitet> aktiviteter = new ArrayList<>();
        for (AktivitetType aktivitet : source) {
            aktiviteter.add(convert(aktivitet));
        }
        return aktiviteter;
    }

    /**
     * Converts an object of AktivitetType to Aktivitet
     * 
     * @param source the object of AktivitetType to be converted
     * @return Aktivitet
     */
    private static Aktivitet convert(AktivitetType source) {
        Aktivitet aktivitet = new Aktivitet();
        aktivitet.setBeskrivning(source.getBeskrivning());
        aktivitet.setAktivitetskod(IsoTypeConverter.toKod(source.getAktivitetskod()));
        return aktivitet;
    }

    /**
     * Converts HosPersonalType to HosPersonal, changing isoType II to Id
     * 
     * @param source HosPersonalType to be converted
     * @return HosPersonal, or null if source is null
     */
    private static HosPersonal convert(HosPersonalType source) {
        if (source == null) return null;

        HosPersonal hosPersonal = new HosPersonal();
        hosPersonal.setId(IsoTypeConverter.toId(source.getPersonalId()));
        hosPersonal.setNamn(source.getFullstandigtNamn());
        hosPersonal.setForskrivarkod(source.getForskrivarkod());
        hosPersonal.setVardenhet(convert(source.getEnhet()));
        
        return hosPersonal;
    }
    
    /**
     * Converts PatientType to Patient
     * 
     * @param source PatientType to be converted
     * @return Patient, or null if source is null
     */
    private static Patient convert(PatientType source) {
        if (source == null) return null;

        Patient patient = new Patient();
        patient.setId(IsoTypeConverter.toId(source.getPersonId()));

        if (!source.getFornamns().isEmpty()) {
            patient.setFornamns(source.getFornamns());
        }
        if (!source.getEfternamns().isEmpty()) {
            patient.setEfternamns(source.getEfternamns());
        }
        if (!source.getMellannamns().isEmpty()) {
            patient.setMellannamns(source.getMellannamns());
        }  

        return patient;
    }
    
    /**
     * Converts EnhetType to Vardenhet 
     * 
     * @param source EnhetType to be converted
     * @return Vardenhet, or null if source is null
     */
	private static Vardenhet convert(EnhetType source) {
		if (source == null)
			return null;

		Vardenhet vardenhet = new Vardenhet();
		vardenhet.setId(IsoTypeConverter.toId(source.getEnhetsId()));
		vardenhet.setNamn(source.getEnhetsnamn());
		vardenhet.setArbetsplatskod(IsoTypeConverter.toId(source.getArbetsplatskod()));
		vardenhet.setPostadress(source.getPostadress());
		vardenhet.setPostnummer(source.getPostnummer());
		vardenhet.setPostort(source.getPostort());
		vardenhet.setEpost(source.getEpost());
		vardenhet.setTelefonnummer(source.getTelefonnummer());
		vardenhet.setVardgivare(convert(source.getVardgivare()));
		
		return vardenhet;
	}
	
	/**
	 * Converts VardgivareType to Vardgivare
	 * 
	 * @param source VardgivareType to be converted
	 * @return Vardgivare, or null if source is null
	 */
	private static Vardgivare convert(VardgivareType source) {
		if (source == null)
			return null;

		Vardgivare vardgivare = new Vardgivare();
		vardgivare.setId(IsoTypeConverter.toId(source.getVardgivareId()));
		vardgivare.setNamn(source.getVardgivarnamn());
		
		return vardgivare;
	}

}
