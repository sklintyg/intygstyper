package se.inera.certificate.modules.rli.model.external.common;

import java.util.List;

public class Patient {

	private Id personId;
	private List<String> fornamns;
	private List<String> efternamns;
	private List<String> mellannamns;
	private String adress;
	private List<PatientRelation> patientRelations;
	private List<Arbetsuppgift> arbetsuppgifts;
	private List<Sysselsattning> sysselsattnings;

	public Id getPersonId() {
		return personId;
	}

	public void setPersonId(Id personId) {
		this.personId = personId;
	}

	public List<String> getFornamns() {
		return fornamns;
	}

	public void setFornamns(List<String> fornamns) {
		this.fornamns = fornamns;
	}

	public List<String> getEfternamns() {
		return efternamns;
	}

	public void setEfternamns(List<String> efternamns) {
		this.efternamns = efternamns;
	}

	public List<String> getMellannamns() {
		return mellannamns;
	}

	public void setMellannamns(List<String> mellannamns) {
		this.mellannamns = mellannamns;
	}

	public String getAdress() {
		return adress;
	}

	public void setAdress(String adress) {
		this.adress = adress;
	}

	public List<PatientRelation> getPatientRelations() {
		return patientRelations;
	}

	public void setPatientRelations(List<PatientRelation> patientRelations) {
		this.patientRelations = patientRelations;
	}

	public List<Arbetsuppgift> getArbetsuppgifts() {
		return arbetsuppgifts;
	}

	public void setArbetsuppgifts(List<Arbetsuppgift> arbetsuppgifts) {
		this.arbetsuppgifts = arbetsuppgifts;
	}

	public List<Sysselsattning> getSysselsattnings() {
		return sysselsattnings;
	}

	public void setSysselsattnings(List<Sysselsattning> sysselsattnings) {
		this.sysselsattnings = sysselsattnings;
	}
}
