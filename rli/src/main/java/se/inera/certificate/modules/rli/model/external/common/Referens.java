package se.inera.certificate.modules.rli.model.external.common;

import org.joda.time.LocalDate;

public class Referens {

	private Kod referenstyp;
	private LocalDate referensdatum;
	private String beskrivning;

	public Kod getReferenstyp() {
		return referenstyp;
	}

	public void setReferenstyp(Kod referenstyp) {
		this.referenstyp = referenstyp;
	}

	public LocalDate getReferensdatum() {
		return referensdatum;
	}

	public void setReferensdatum(LocalDate referensdatum) {
		this.referensdatum = referensdatum;
	}

	public String getBeskrivning() {
		return beskrivning;
	}

	public void setBeskrivning(String beskrivning) {
		this.beskrivning = beskrivning;
	}
}
