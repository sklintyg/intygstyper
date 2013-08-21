/**
 * 
 */
package se.inera.certificate.modules.rli.validator;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import se.inera.certificate.modules.rli.model.external.Arrangemang;
import se.inera.certificate.modules.rli.model.external.Utlatande;
import se.inera.certificate.modules.rli.model.external.common.Aktivitet;
import se.inera.certificate.modules.rli.model.external.common.HosPersonal;
import se.inera.certificate.modules.rli.model.external.common.Observation;
import se.inera.certificate.modules.rli.model.external.common.Patient;
import se.inera.certificate.modules.rli.model.external.common.Rekommendation;

/**
 * @author erik
 * 
 */
public class ExternalValidatorImpl implements ExternalValidator {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * se.inera.certificate.modules.rli.validator.ExternalValidator#validate()
	 */
	@Override
	public List<String> validate(Utlatande utlatande) {
		
		List<String> validationErrors = new ArrayList<String>();
		
		validateUtlatande(utlatande, validationErrors);
		validatePatient(utlatande, validationErrors);
		validateSkapasAv(utlatande, validationErrors);
		validateRekommendationer(utlatande, validationErrors);
		validateAktiviteter(utlatande, validationErrors);
		validateObservationer(utlatande, validationErrors);
		validateArrangemang(utlatande, validationErrors);
		
		return validationErrors;
	}
	
	private void validateUtlatande(Utlatande utlatande, List<String> validationErrors){
		
		if (utlatande.getId() == null){
			validationErrors.add("No Utlatande ID found");
		}
		
		if (utlatande.getTyp() == null){
			validationErrors.add("No Utlatandetyp found");
		}
		
		if (utlatande.getSigneringsdatum() == null){
			validationErrors.add("No signeringsdatum found");
		}
		
	}

	private void validateArrangemang(Utlatande utlatande, List<String> validationErrors) {
		Arrangemang arrangemang = utlatande.getArrangemang();
		
		if (arrangemang == null) {
			validationErrors.add("Arrangemang was null");
		}
		else {
		
			if (arrangemang.getBokningsdatum() == null){
				validationErrors.add("No bokningsdatum found in arrangemang");
			}
			
			if (arrangemang.getArrangemangstid() == null){
				validationErrors.add("No arrangemangstid found in arrangemang");
			}
			
			if (arrangemang.getPlats() == null){
				validationErrors.add("No plats found in arrangemang");
			}
		}
	}

	private void validateSkapasAv(Utlatande utlatande, List<String> validationErrors) {
		HosPersonal skapatAv = utlatande.getSkapadAv();
		
		if (skapatAv == null) {
			validationErrors.add("Skapat av was null");
		}
		else {

			if (skapatAv.getPersonalId() == null){
				validationErrors.add("No personalId found in Skapas av");
			}
			
			if (skapatAv.getFullstandigtNamn() == null){
				validationErrors.add("No fullstandigt namn found in Skapas av");
			}
		}
	}

	private void validateObservationer(Utlatande utlatande, List<String> validationErrors) {
		List<Observation> observationer = utlatande.getObservationer();
		
		if (observationer.isEmpty()) {
			validationErrors.add("No observations found");
		}
		else {
			for (Observation obs : observationer){
				if (obs.getObservationsperiod() == null){
					validationErrors.add("No observationsperiod found in: " + obs.getObservationskod().getCode() + "\n");
				}
			}
			
		}
		
	}

	private void validateAktiviteter(Utlatande utlatande, List<String> validationErrors) {
		List<Aktivitet> aktiviteter = utlatande.getAktiviteter();
		
		if (aktiviteter.isEmpty()) {
			validationErrors.add("No aktiviteter found");
		}
		else {
			for (Aktivitet akt : aktiviteter){
				if (akt.getAktivitetskod() == null){
					validationErrors.add("No aktivitetskod found in: " + akt + "\n");
				}
			}
		}
	}

	private void validateRekommendationer(Utlatande utlatande, List<String> validationErrors) {
		List<Rekommendation> rekommendationer = utlatande.getRekommendationer();
		if (rekommendationer.isEmpty()) {
			validationErrors.add("No rekommendationer found");
		}
		else {
			for (Rekommendation rek : rekommendationer){
				if (rek.getRekommendationskod() == null){
					validationErrors.add("No rekommendationskod found in: " + rek.getRekommendationskod().getCode()+ "\n");
				}				
				if (rek.getSjukdomskannedom() == null){
					validationErrors.add("No sjukdomskannedom found in: " + rek.getRekommendationskod().getCode()+ "\n");
				}
			}
		}
	}

	private void validatePatient(Utlatande utlatande, List<String> validationErrors) {
		
		String PERSON_NUMBER_REGEX = "[0-9]{8}[-+][0-9]{4}";
		
		Patient patient = utlatande.getPatient();
		if (patient == null) {
			validationErrors.add("Patient was null");
		}
		else {
			String personNumber = patient.getPersonId().getExtension();
			if (personNumber == null || !Pattern.matches(PERSON_NUMBER_REGEX, personNumber)) {
				validationErrors.add("Wrong format for person-id! Valid format is YYYYMMDD-XXXX or YYYYMMDD+XXXX.");
			}
			
		}

	}

}
