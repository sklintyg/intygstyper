package se.inera.certificate.modules.rli.model.external.common;

import java.util.List;

public class PatientRelation {

	private Kod relationskategori;
	private List<Kod> relationTyps;
	private Id personId;
	private List<String> fornamns;
	private List<String> efternamns;
	private List<String> mellannamns;
	private List<String> adresses;

	public Kod getRelationskategori() {
		return relationskategori;
	}

	public void setRelationskategori(Kod relationskategori) {
		this.relationskategori = relationskategori;
	}

	public List<Kod> getRelationTyps() {
		return relationTyps;
	}

	public void setRelationTyps(List<Kod> relationTyps) {
		this.relationTyps = relationTyps;
	}

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

	public List<String> getAdresses() {
		return adresses;
	}

	public void setAdresses(List<String> adresses) {
		this.adresses = adresses;
	}
}
