package se.inera.certificate.modules.rli.model.external.common;

public class Rekommendation {

	private Kod rekommendationskod;
	private String beskrivning;
	private Kod sjukdomskannedom;

	public Kod getRekommendationskod() {
		return rekommendationskod;
	}

	public void setRekommendationskod(Kod rekommendationskod) {
		this.rekommendationskod = rekommendationskod;
	}

	public String getBeskrivning() {
		return beskrivning;
	}

	public void setBeskrivning(String beskrivning) {
		this.beskrivning = beskrivning;
	}

	public Kod getSjukdomskannedom() {
		return sjukdomskannedom;
	}

	public void setSjukdomskannedom(Kod sjukdomskannedom) {
		this.sjukdomskannedom = sjukdomskannedom;
	}
}
