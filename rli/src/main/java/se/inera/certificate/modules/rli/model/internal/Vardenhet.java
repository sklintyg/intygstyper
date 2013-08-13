package se.inera.certificate.modules.rli.model.internal;

public class Vardenhet {

	private String enhetsId;
	
	private String enhetsNamn;
	
	private String postAddress;
	
	private String postNummer;
	
	private String postOrt;
	
	private String telefonNummer;
	
	private String ePost;
	
	private Vardgivare vardgivare;
	
	public Vardenhet() {
	
	}

	public String getEnhetsId() {
		return enhetsId;
	}

	public void setEnhetsId(String enhetsId) {
		this.enhetsId = enhetsId;
	}

	public String getEnhetsNamn() {
		return enhetsNamn;
	}

	public void setEnhetsNamn(String enhetsNamn) {
		this.enhetsNamn = enhetsNamn;
	}

	public String getPostAddress() {
		return postAddress;
	}

	public void setPostAddress(String postAddress) {
		this.postAddress = postAddress;
	}

	public String getPostNummer() {
		return postNummer;
	}

	public void setPostNummer(String postNummer) {
		this.postNummer = postNummer;
	}

	public String getPostOrt() {
		return postOrt;
	}

	public void setPostOrt(String postOrt) {
		this.postOrt = postOrt;
	}

	public String getTelefonNummer() {
		return telefonNummer;
	}

	public void setTelefonNummer(String telefonNummer) {
		this.telefonNummer = telefonNummer;
	}

	public String getePost() {
		return ePost;
	}

	public void setePost(String ePost) {
		this.ePost = ePost;
	}

	public Vardgivare getVardgivare() {
		return vardgivare;
	}

	public void setVardgivare(Vardgivare vardgivare) {
		this.vardgivare = vardgivare;
	}
	
}
