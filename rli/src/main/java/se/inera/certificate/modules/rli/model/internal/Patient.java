package se.inera.certificate.modules.rli.model.internal;

public class Patient {

	private String personId;
	
	private String fullstandigtNamn;
	
	private String forNamn;
	
	private String efterNamn;
	
	private String mellanNamn;
	
	private String postAdress;
	
	public Patient() {
	
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public String getFullstandigtNamn() {
		return fullstandigtNamn;
	}

	public void setFullstandigtNamn(String fullstandigtNamn) {
		this.fullstandigtNamn = fullstandigtNamn;
	}

	public String getForNamn() {
		return forNamn;
	}

	public void setForNamn(String forNamn) {
		this.forNamn = forNamn;
	}

	public String getEfterNamn() {
		return efterNamn;
	}

	public void setEfterNamn(String efterNamn) {
		this.efterNamn = efterNamn;
	}

	public String getMellanNamn() {
		return mellanNamn;
	}

	public void setMellanNamn(String mellanNamn) {
		this.mellanNamn = mellanNamn;
	}

	public String getPostAdress() {
		return postAdress;
	}

	public void setPostAdress(String postAdress) {
		this.postAdress = postAdress;
	}
	
}
