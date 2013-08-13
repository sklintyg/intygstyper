package se.inera.certificate.modules.rli.model.external.common;

import org.joda.time.Partial;

public class PartialDateInterval {

	private Partial from;
	private Partial tom;

	public PartialDateInterval(Partial from, Partial tom) {
		this.from = from;
		this.tom = tom;
	}

	public Partial getFrom() {
		return from;
	}

	public void setFrom(Partial from) {
		this.from = from;
	}

	public Partial getTom() {
		return tom;
	}

	public void setTom(Partial tom) {
		this.tom = tom;
	}
}
