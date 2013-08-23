/**
 * 
 */
package se.inera.certificate.modules.rli.validator;

import java.util.ArrayList;
import java.util.List;

import se.inera.certificate.modules.rli.model.external.Arrangemang;
import se.inera.certificate.modules.rli.model.external.Utlatande;
import se.inera.certificate.modules.rli.model.external.Aktivitet;
import se.inera.certificate.modules.rli.model.external.common.HosPersonal;
import se.inera.certificate.modules.rli.model.external.common.Observation;
import se.inera.certificate.modules.rli.model.external.common.Patient;
import se.inera.certificate.modules.rli.model.external.common.Rekommendation;
import se.inera.certificate.validate.IdValidator;
import se.inera.certificate.validate.SimpleIdValidatorBuilder;

import se.inera.certificate.modules.rli.model.codes.AktivitetsKod;
/**
 * @author erik
 * 
 */
public class ExternalValidatorImpl implements ExternalValidator {

	private IdValidator idValidator;

	public ExternalValidatorImpl() {
		idValidator = new SimpleIdValidatorBuilder().withPersonnummerValidator(true)
				.withSamordningsnummerValidator(true).build();
	}

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

	/*
	 * (non-Javadoc)
	 * 
	 * Validates that required attributes connected with the actual class
	 * Utlatande are present
	 * 
	 */
	private void validateUtlatande(Utlatande utlatande,
			List<String> validationErrors) {

		if (utlatande.getId() == null) {
			validationErrors.add("No Utlatande ID found");
		}

		if (utlatande.getTyp() == null) {
			validationErrors.add("No Utlatandetyp found");
		}

		if (utlatande.getSigneringsdatum() == null) {
			validationErrors.add("No signeringsdatum found");
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * Validates Arrangemang in Utlatande First makes sure Arrangemang is not
	 * null, if so, the required attributes are validated in turn and any errors
	 * are added to validation error list
	 * 
	 */
	private void validateArrangemang(Utlatande utlatande,
			List<String> validationErrors) {
		Arrangemang arrangemang = utlatande.getArrangemang();

		if (arrangemang == null) {
			validationErrors.add("Arrangemang was null");
		} else {

			if (arrangemang.getBokningsdatum() == null) {
				validationErrors.add("No bokningsdatum found in arrangemang");
			}

			if (arrangemang.getArrangemangstid() == null) {
				validationErrors.add("No arrangemangstid found in arrangemang");
			} 

			if (arrangemang.getPlats() == null) {
				validationErrors.add("No plats found in arrangemang");
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * Validates Arrangemang in Utlatande First makes sure Arrangemang is not
	 * null, if so, the required attributes are validated in turn and any errors
	 * are added to validation error list
	 * 
	 */
	private void validateSkapasAv(Utlatande utlatande, List<String> validationErrors) {
		HosPersonal skapatAv = utlatande.getSkapadAv();

		if (skapatAv == null) {
			validationErrors.add("Skapat av was null");
		} else {

			if (skapatAv.getPersonalId() == null) {
				validationErrors.add("No personalId found in Skapas av");
			}

			if (skapatAv.getFullstandigtNamn() == null) {
				validationErrors.add("No fullstandigt namn found in Skapas av");
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * Make sure Utlatande contains 1..* Observation
	 * 
	 */
	private void validateObservationer(Utlatande utlatande, List<String> validationErrors) {
		List<Observation> observationer = utlatande.getObservationer();

		if (observationer.isEmpty()) {
			validationErrors.add("No observations found");
		} else {
			for (Observation obs : observationer) {
				if (obs.getObservationsperiod() == null) {
					validationErrors.add("No observationsperiod found in: "
							+ obs.getObservationskod().getCode() + "\n");
				}
			}

		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * Validate Aktiviteter,
	 */
	private void validateAktiviteter(Utlatande utlatande, List<String> validationErrors) {
		List<Aktivitet> aktiviteter = utlatande.getAktiviteter();

		if (aktiviteter.isEmpty()) {
			validationErrors.add("No aktiviteter found");
		} else {
			/*
			 * Make sure Utlatande contains 1..2 Aktiviteter and nothing
			 * else
			 */
			if (aktiviteter.size() < 1 && aktiviteter.size() > 2) {
				validationErrors
						.add("Utlatande does not contain 1 or 2 Aktiviteter");
			} else {
				/*
				 * Make sure all Aktiviteter contains the required attribute Aktivitetskod
				 */
				for (Aktivitet akt : aktiviteter) {
					if (akt.getAktivitetskod() == null || 
							akt.getAktivitetskod().getCodeSystem().equals(AktivitetsKod.KLINISK_UNDERSOKNING)
									|| akt.getAktivitetskod().getCodeSystem().equals(AktivitetsKod.OMVARDNADSATGARD)) {
						validationErrors.add("No valid aktivitetskod found in: "+ akt + "\n");
					}
				}
			}
		}
	}
	/*
	 * (non-Javadoc)
	 * Make sure Utlatande contains 1..* Rekommendation
	 */
	private void validateRekommendationer(Utlatande utlatande, List<String> validationErrors) {
		List<Rekommendation> rekommendationer = utlatande.getRekommendationer();
		if (rekommendationer.isEmpty()) {
			validationErrors.add("No Rekommendation found");
		} else {
			for (Rekommendation rek : rekommendationer) {
				if (rek.getRekommendationskod() == null) {
					validationErrors.add("No rekommendationskod found in: "
							+ rek.getRekommendationskod().getCode() + "\n");
				}
				if (rek.getSjukdomskannedom() == null) {
					validationErrors.add("No sjukdomskannedom found in: "
							+ rek.getRekommendationskod().getCode() + "\n");
				}
			}
		}
	}
	
	/* (non-Javadoc)
	 * 
	 * Make sure Utlatande contains 1 Patient
	 */
	private void validatePatient(Utlatande utlatande, List<String> validationErrors) {
	
		Patient patient = utlatande.getPatient();

		if (patient == null) {
			validationErrors.add("Patient was null");
		}
		else {
			validationErrors.addAll(idValidator.validate(patient.getPersonId()));
			
			/*
			 * Make sure Fornamn and Efternamn contains at least one item
			 */
			if (patient.getFornamns().isEmpty()){
				validationErrors.add("At least one Fornamn must be provided for Patient");
			}
			if (patient.getEfternamns().isEmpty()){
				validationErrors.add("At least one Efternamn must be provided for Patient");
			}
		}
	}
}
