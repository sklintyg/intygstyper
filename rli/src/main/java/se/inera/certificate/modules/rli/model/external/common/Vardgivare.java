package se.inera.certificate.modules.rli.model.external.common;

public class Vardgivare {

	private Id vardgivareId;
	private String vardgivarnamn;

	public Id getVardgivareId() {
		return vardgivareId;
	}

	public void setVardgivareId(Id vardgivareId) {
		this.vardgivareId = vardgivareId;
	}

	public String getVardgivarnamn() {
		return vardgivarnamn;
	}

	public void setVardgivarnamn(String vardgivarnamn) {
		this.vardgivarnamn = vardgivarnamn;
	}
}
