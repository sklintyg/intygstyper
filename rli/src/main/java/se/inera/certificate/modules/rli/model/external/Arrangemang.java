package se.inera.certificate.modules.rli.model.external;

import org.joda.time.Partial;

import se.inera.certificate.common.v1.PartialDateInterval;
import se.inera.certificate.model.Kod;

public class Arrangemang {

	private String bokningsreferens;

	private Partial bokningsdatum;

	private PartialDateInterval arrangemangstid;

	private Partial avbestallningsdatum;

	private Kod arrangemangstyp;

	private String plats;

	public String getBokningsreferens() {
		return bokningsreferens;
	}

	public void setBokningsreferens(String bokningsreferens) {
		this.bokningsreferens = bokningsreferens;
	}

	public Partial getBokningsdatum() {
		return bokningsdatum;
	}

	public void setBokningsdatum(Partial bokningsdatum) {
		this.bokningsdatum = bokningsdatum;
	}

	public PartialDateInterval getArrangemangstid() {
		return arrangemangstid;
	}

	public void setArrangemangstid(PartialDateInterval arrangemangstid) {
		this.arrangemangstid = arrangemangstid;
	}

	public Partial getAvbestallningsdatum() {
		return avbestallningsdatum;
	}

	public void setAvbestallningsdatum(Partial avbestallningsdatum) {
		this.avbestallningsdatum = avbestallningsdatum;
	}

	public Kod getArrangemangstyp() {
		return arrangemangstyp;
	}

	public void setArrangemangstyp(Kod arrangemangstyp) {
		this.arrangemangstyp = arrangemangstyp;
	}

	public String getPlats() {
		return plats;
	}

	public void setPlats(String plats) {
		this.plats = plats;
	}
}
