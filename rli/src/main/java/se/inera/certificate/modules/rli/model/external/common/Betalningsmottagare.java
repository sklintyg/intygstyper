package se.inera.certificate.modules.rli.model.external.common;

import se.inera.certificate.model.Id;
import se.inera.certificate.model.Kod;

public class Betalningsmottagare {

	private String namn;
	private String postadress;
	private String postnummer;
	private String postort;
	private Id organisationsnummer;
	private String plusgiroEllerBankgiro;
	private Boolean begarArvode;
	private String arvodeBegart;
	private Kod skattesedel;

	public String getNamn() {
		return namn;
	}

	public void setNamn(String namn) {
		this.namn = namn;
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

	public Id getOrganisationsnummer() {
		return organisationsnummer;
	}

	public void setOrganisationsnummer(Id organisationsnummer) {
		this.organisationsnummer = organisationsnummer;
	}

	public String getPlusgiroEllerBankgiro() {
		return plusgiroEllerBankgiro;
	}

	public void setPlusgiroEllerBankgiro(String plusgiroEllerBankgiro) {
		this.plusgiroEllerBankgiro = plusgiroEllerBankgiro;
	}

	public Boolean getBegarArvode() {
		return begarArvode;
	}

	public void setBegarArvode(Boolean begarArvode) {
		this.begarArvode = begarArvode;
	}

	public String getArvodeBegart() {
		return arvodeBegart;
	}

	public void setArvodeBegart(String arvodeBegart) {
		this.arvodeBegart = arvodeBegart;
	}

	public Kod getSkattesedel() {
		return skattesedel;
	}

	public void setSkattesedel(Kod skattesedel) {
		this.skattesedel = skattesedel;
	}
}
