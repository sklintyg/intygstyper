package se.inera.certificate.modules.rli.model.external.common;

public class HosPersonal {

	private Id personalId;
	private String fullstandigtNamn;
	private String forskrivarkod;
	private Enhet enhet;

	public Id getPersonalId() {
		return personalId;
	}

	public void setPersonalId(Id personalId) {
		this.personalId = personalId;
	}

	public String getFullstandigtNamn() {
		return fullstandigtNamn;
	}

	public void setFullstandigtNamn(String fullstandigtNamn) {
		this.fullstandigtNamn = fullstandigtNamn;
	}

	public String getForskrivarkod() {
		return forskrivarkod;
	}

	public void setForskrivarkod(String forskrivarkod) {
		this.forskrivarkod = forskrivarkod;
	}

	public Enhet getEnhet() {
		return enhet;
	}

	public void setEnhet(Enhet enhet) {
		this.enhet = enhet;
	}
}
