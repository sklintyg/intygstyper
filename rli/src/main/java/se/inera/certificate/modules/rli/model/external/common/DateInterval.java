package se.inera.certificate.modules.rli.model.external.common;

import org.joda.time.LocalDate;

public class DateInterval {

	private LocalDate from;
	private LocalDate tom;

	public LocalDate getFrom() {
		return from;
	}

	public void setFrom(LocalDate from) {
		this.from = from;
	}

	public LocalDate getTom() {
		return tom;
	}

	public void setTom(LocalDate tom) {
		this.tom = tom;
	}
}
