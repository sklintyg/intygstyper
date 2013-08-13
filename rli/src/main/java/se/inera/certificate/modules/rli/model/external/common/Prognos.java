package se.inera.certificate.modules.rli.model.external.common;

public class Prognos {

	private Kod prognoskod;
	private String beskrivning;
	private PartialDateInterval period;

	public Kod getPrognoskod() {
		return prognoskod;
	}

	public void setPrognoskod(Kod prognoskod) {
		this.prognoskod = prognoskod;
	}

	public String getBeskrivning() {
		return beskrivning;
	}

	public void setBeskrivning(String beskrivning) {
		this.beskrivning = beskrivning;
	}

	public PartialDateInterval getPeriod() {
		return period;
	}

	public void setPeriod(PartialDateInterval period) {
		this.period = period;
	}
}
