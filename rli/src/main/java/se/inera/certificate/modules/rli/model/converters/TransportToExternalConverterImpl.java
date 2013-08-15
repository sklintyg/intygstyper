package se.inera.certificate.modules.rli.model.converters;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import riv.insuranceprocess.healthreporting._2.EnhetType;
import riv.insuranceprocess.healthreporting._2.HosPersonalType;
import riv.insuranceprocess.healthreporting._2.VardgivareType;
import se.inera.certificate.common.v1.AktivitetType;
import se.inera.certificate.common.v1.ObservationType;
import se.inera.certificate.common.v1.PatientRelationType;
import se.inera.certificate.common.v1.PatientType;
import se.inera.certificate.common.v1.RekommendationType;
import se.inera.certificate.common.v1.UtforarrollType;
import se.inera.certificate.modules.rli.model.external.Arrangemang;
import se.inera.certificate.modules.rli.model.external.Utlatande;
import se.inera.certificate.modules.rli.model.external.common.Aktivitet;
import se.inera.certificate.modules.rli.model.external.common.Enhet;
import se.inera.certificate.modules.rli.model.external.common.HosPersonal;
import se.inera.certificate.modules.rli.model.external.common.Observation;
import se.inera.certificate.modules.rli.model.external.common.PartialDateInterval;
import se.inera.certificate.modules.rli.model.external.common.Patient;
import se.inera.certificate.modules.rli.model.external.common.PatientRelation;
import se.inera.certificate.modules.rli.model.external.common.Rekommendation;
import se.inera.certificate.modules.rli.model.external.common.Utforarroll;
import se.inera.certificate.modules.rli.model.external.common.Vardgivare;

/**
 * Converter between transport and external model 
 * @author erik
 *
 */
public class TransportToExternalConverterImpl implements TransportToExternalConverter{
	
	private static final Logger LOG = LoggerFactory.getLogger(TransportToExternalConverterImpl.class);

	public TransportToExternalConverterImpl(){
		
	}
	/**
	 * Converts from the transport format (se.inera.certificate.common.v1.Utlatande) to
	 * the external format (se.inera.certificate.modules.rli.model.external.Utlatande)
	 * 
	 * @param source Utlatande in the transport format to be converted to external format
	 * @return se.inera.certificate.modules.rli.model.external.Utlatande
	 */
	@Override
	public Utlatande transportToExternal(se.inera.certificate.common.v1.Utlatande source) {
		
		LOG.debug("Starting conversion");
		
		Utlatande externalModel = new Utlatande();

		externalModel.setId(IsoTypeConverter.toId(source.getUtlatandeId()));
		
		externalModel.setTyp(IsoTypeConverter.toKod(source.getTypAvUtlatande()));

		externalModel.setKommentarer(source.getKommentars());
		
		externalModel.setSigneringsdatum(source.getSigneringsdatum());
		
		externalModel.setSkickatdatum(source.getSkickatdatum());
		
		//Convertions from here on

		externalModel.setPatient(convertPatient(source.getPatient()));

		externalModel.setSkapadAv(convertHosPersonal(source.getSkapadAv()));

		externalModel.setAktiviteter(convertAktiviteter(source.getAktivitets()));

		externalModel.setObservationer(convertObservations(source.getObservations()));
		
		externalModel.setArrangemang(convertArrangemang(source.getArrangemang()));	
		
		externalModel.setRekommendationer(convertRekommendationer(source.getRekommendations()));
		
		return externalModel;
	}
	/**
	 * Convert List of RekommendationType to List of Rekommendation
	 * @param source List of RekommendationType to be converted
	 * @return List of Rekommendation
	 */
	List<Rekommendation> convertRekommendationer(List<RekommendationType> source) {
		LOG.debug("Converting rekommendationer");
		
		List<Rekommendation> rekommendationer = new ArrayList<Rekommendation>();
		for (RekommendationType toConvert : source){
			Rekommendation rekommendation = convertRekommendation(toConvert);
			if (rekommendation != null){
				rekommendationer.add(rekommendation);
			}
		}
		return rekommendationer;
	}
	
	/**
	 * Convert RekommendationType to Rekommendation
	 * @param source RekommendationType 
	 * @return Rekommendation, or null if source is null
	 */
	private Rekommendation convertRekommendation(RekommendationType source) {
		if (source == null){
			return null;
		}
		Rekommendation rekommendation = new Rekommendation();
		
		rekommendation.setBeskrivning(source.getBeskrivning());
		rekommendation.setRekommendationskod(IsoTypeConverter.toKod(source.getRekommendationskod()));
		rekommendation.setSjukdomskannedom(IsoTypeConverter.toKod(source.getSjukdomskannedom()));
		return rekommendation;
	}

	/**
	 * Iterates over a list of objects of the type ObservationType and converts them to
	 * type Observation
	 * 
	 * @param source List of objects of type ObservationType to be converted
	 * @return List of objects of type Observation
	 */
	List<Observation> convertObservations(List<ObservationType> source) {
		
		LOG.debug("Converting observationer");
		
		List<Observation> observations = new ArrayList<Observation>();

		for (ObservationType ot : source){
			Observation observation = convertObservation(ot);
			if (observation != null){
				observations.add(observation);
			}
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
	private Observation convertObservation(ObservationType source){
		Observation observation = new Observation();
		
		if (source.getObservationsperiod() != null){
			observation.setObservationsperiod(new PartialDateInterval(source.getObservationsperiod().getFrom(), 
					source.getObservationsperiod().getTom()));
		}
		observation.setObservationskod(IsoTypeConverter.toKod(source.getObservationskod()));
		observation.setUtforsAv(convertUtforarroll(source.getUtforsAv()));
		return observation;
	}
	
	/**
	 * Converts a list of UtforarrollType to Utforarroll
	 * @param source List of UtforarrollType 
	 * @return List of Utforarroll
	 */
	List<Utforarroll> convertUtforarroller(List<UtforarrollType> source){
		LOG.debug("Converting utforarroller");
		
		List<Utforarroll> utforsAvs = new ArrayList<>();
        for (UtforarrollType toConvert: source){
        	if (toConvert != null){
        		utforsAvs.add(convertUtforarroll(toConvert));
        	}
        }
        return utforsAvs;
	}
	
	/**
	 * Convert UtforarrollType to Utforarroll
	 * @param source UtforarrollType
	 * @return Utforarroll, or null if source is null
	 */
	private Utforarroll convertUtforarroll(UtforarrollType source){
		if (source == null){
			return null;
		}
		Utforarroll utforarroll = new Utforarroll();
		utforarroll.setAntasAv(convertHosPersonal(source.getAntasAv()));
		utforarroll.setUtforartyp(IsoTypeConverter.toKod(source.getUtforartyp()));
		return utforarroll;
	}
	
	/**
	 * Convert transport model Arrangemang to external model arrangemang
	 * @param source se.inera.certificate.rli.v1.Arrangemang
	 * @return import se.inera.certificate.modules.rli.model.external.Arrangemang
	 */
	Arrangemang convertArrangemang(se.inera.certificate.rli.v1.Arrangemang source){	
		LOG.debug("Converting arrangemang");
		
		if( source == null){
			return null;
		}
		Arrangemang arrangemang = new Arrangemang();
		
		arrangemang.setArrangemangstid(source.getArrangemangstid());
		arrangemang.setArrangemangstyp(IsoTypeConverter.toKod( source.getArrangemangstyp()));
		arrangemang.setAvbestallningsdatum(source.getAvbestallningsdatum());
		arrangemang.setBokningsdatum(source.getBokningsdatum());
		arrangemang.setBokningsreferens(source.getBokningsreferens());
		arrangemang.setPlats(source.getPlats());
		return arrangemang;
	}
	
	/**
	 * Iterates through a list of AktivitetType and converts each subsequent item from AktivitetType 
	 * to the Aktivitet that the external model understands.
	 * 
	 * @param source the list of AktivitetType's to be converted
	 * @return a List containing Aktiviteter
	 */
    List<Aktivitet> convertAktiviteter(List<AktivitetType> source) {
    	
    	LOG.debug("Converting aktiviteter");
    	
        List<Aktivitet> aktiviteter = new ArrayList<>();
        for (AktivitetType aktivitet : source) {
        	if (source != null){
        		aktiviteter.add(convertAktivitet(aktivitet));
        	}
        }
        return aktiviteter;
    }

    /**
     * Converts an object of AktivitetType to Aktivitet
     * 
     * @param source the object of AktivitetType to be converted
     * @return Aktivitet
     */
    private Aktivitet convertAktivitet(AktivitetType source) {
    	if (source == null){
    		return null;
    	}
        Aktivitet aktivitet = new Aktivitet();
        aktivitet.setBeskrivning(source.getBeskrivning());
        aktivitet.setAktivitetskod(IsoTypeConverter.toKod(source.getAktivitetskod()));
        if (source.getAktivitetstid() != null){
        	aktivitet.setAktivitetstid(new PartialDateInterval(source.getAktivitetstid().getFrom(),
        			source.getAktivitetstid().getTom()));
        }
        aktivitet.setBeskrivning(source.getBeskrivning());
        aktivitet.setUtforsVidEnhet(convertEnhet(source.getUtforsVidEnhet()));
        aktivitet.setUtforsAvs(convertUtforarroller(source.getUtforsAvs()));
        
        return aktivitet;
    }

    /**
     * Converts HosPersonalType to HosPersonal, changing isoType II to Id
     * 
     * @param source HosPersonalType to be converted
     * @return HosPersonal, or null if source is null
     */
    HosPersonal convertHosPersonal(HosPersonalType source) {
    	
    	LOG.debug("Converting HosPersonal");
        if (source == null){
        	return null;
        }

        HosPersonal hosPersonal = new HosPersonal();
        hosPersonal.setPersonalId(IsoTypeConverter.toId(source.getPersonalId()));
        hosPersonal.setFullstandigtNamn(source.getFullstandigtNamn());
        hosPersonal.setForskrivarkod(source.getForskrivarkod());
        hosPersonal.setEnhet(convertEnhet(source.getEnhet()));
        
        return hosPersonal;
    }
    
    /**
     * Converts PatientType to Patient
     * 
     * @param source PatientType to be converted
     * @return Patient, or null if source is null
     */
    Patient convertPatient(PatientType source) {
    	LOG.debug("Converting patient");
    	
        if (source == null){
        	return null;
        }

        Patient patient = new Patient();
        patient.setPersonId(IsoTypeConverter.toId(source.getPersonId()));
        patient.setAdress(source.getAdress());
        patient.setPatientRelations(convertPatientRelations(source.getPatientRelations()));

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
    
    private List<PatientRelation> convertPatientRelations(List<PatientRelationType> source) {
		// TODO Maybe implement
		return null;
	}
    
    @SuppressWarnings("unused")
	private PatientRelation convertPatientRelation(PatientRelationType source){
    	// TODO Implement if implementing convertPatientRelations 
    	return null;
    }
	/**
     * Converts EnhetType to Enhet 
     * 
     * @param source EnhetType to be converted
     * @return Enhet, or null if source is null
     */

	Enhet convertEnhet(EnhetType source) {
		LOG.debug("Converting enhet");
		if (source == null){
			return null;
		}
		Enhet vardenhet = new Enhet();
		vardenhet.setEnhetsId(IsoTypeConverter.toId(source.getEnhetsId()));
		vardenhet.setEnhetsnamn(source.getEnhetsnamn());
		vardenhet.setArbetsplatskod(IsoTypeConverter.toId(source.getArbetsplatskod()));
		vardenhet.setPostadress(source.getPostadress());
		vardenhet.setPostnummer(source.getPostnummer());
		vardenhet.setPostort(source.getPostort());
		vardenhet.setEpost(source.getEpost());
		vardenhet.setTelefonnummer(source.getTelefonnummer());
		vardenhet.setVardgivare(convertVardgivare(source.getVardgivare()));
		
		return vardenhet;
	}
	
	/**
	 * Converts VardgivareType to Vardgivare
	 * 
	 * @param source VardgivareType to be converted
	 * @return Vardgivare, or null if source is null
	 */
	Vardgivare convertVardgivare(VardgivareType source) {
		
		LOG.debug("Converting vardgivare");
		if (source == null){
			return null;
		}
		Vardgivare vardgivare = new Vardgivare();
		vardgivare.setVardgivareId(IsoTypeConverter.toId(source.getVardgivareId()));
		vardgivare.setVardgivarnamn(source.getVardgivarnamn());
		
		return vardgivare;
	}

}
