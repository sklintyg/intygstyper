package se.inera.certificate.modules.rli.model.external.common;

public class Enhet {

	private Id enhetsId;
	private Id arbetsplatskod;
	private String enhetsnamn;
	private String postadress;
	private String postnummer;
	private String postort;
	private String telefonnummer;
	private String epost;
	private Vardgivare vardgivare;

	public Id getEnhetsId() {
		return enhetsId;
	}

	public void setEnhetsId(Id enhetsId) {
		this.enhetsId = enhetsId;
	}

	public Id getArbetsplatskod() {
		return arbetsplatskod;
	}

	public void setArbetsplatskod(Id arbetsplatskod) {
		this.arbetsplatskod = arbetsplatskod;
	}

	public String getEnhetsnamn() {
		return enhetsnamn;
	}

	public void setEnhetsnamn(String enhetsnamn) {
		this.enhetsnamn = enhetsnamn;
	}

	public String getPostadress() {
		return postadress;
	}

	public void setPostadress(String postadress) {
		this.postadress = postadress;
	}

	public String getPostnummer() {
		return postnummer;
	}

	public void setPostnummer(String postnummer) {
		this.postnummer = postnummer;
	}

	public String getPostort() {
		return postort;
	}

	public void setPostort(String postort) {
		this.postort = postort;
	}

	public String getTelefonnummer() {
		return telefonnummer;
	}

	public void setTelefonnummer(String telefonnummer) {
		this.telefonnummer = telefonnummer;
	}

	public String getEpost() {
		return epost;
	}

	public void setEpost(String epost) {
		this.epost = epost;
	}

	public Vardgivare getVardgivare() {
		return vardgivare;
	}

	public void setVardgivare(Vardgivare vardgivare) {
		this.vardgivare = vardgivare;
	}
}
