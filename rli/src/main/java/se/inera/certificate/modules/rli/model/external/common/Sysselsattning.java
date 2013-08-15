package se.inera.certificate.modules.rli.model.external.common;

import org.joda.time.LocalDate;

public class Sysselsattning {

	private Kod typAvSysselsattning;
	private LocalDate datum;

	public Kod getTypAvSysselsattning() {
		return typAvSysselsattning;
	}

	public void setTypAvSysselsattning(Kod typAvSysselsattning) {
		this.typAvSysselsattning = typAvSysselsattning;
	}

	public LocalDate getDatum() {
		return datum;
	}

	public void setDatum(LocalDate datum) {
		this.datum = datum;
	}
}
