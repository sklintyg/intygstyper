package se.inera.certificate.modules.rli.model.internal;

import se.inera.certificate.modules.rli.model.codes.RekommendationsKod;
import se.inera.certificate.modules.rli.model.codes.SjukdomsKannedom;

public class Rekommendation {

	private RekommendationsKod rekommendationsKod;
	
	private SjukdomsKannedom sjukdomsKannedom;
	
	private String beskrivning;
	
	public Rekommendation() {
	
	}

	public RekommendationsKod getRekommendationsKod() {
		return rekommendationsKod;
	}

	public void setRekommendationsKod(RekommendationsKod rekommendationsKod) {
		this.rekommendationsKod = rekommendationsKod;
	}

	public SjukdomsKannedom getSjukdomsKannedom() {
		return sjukdomsKannedom;
	}

	public void setSjukdomsKannedom(SjukdomsKannedom sjukdomsKannedom) {
		this.sjukdomsKannedom = sjukdomsKannedom;
	}

	public String getBeskrivning() {
		return beskrivning;
	}

	public void setBeskrivning(String beskrivning) {
		this.beskrivning = beskrivning;
	}
	
}
