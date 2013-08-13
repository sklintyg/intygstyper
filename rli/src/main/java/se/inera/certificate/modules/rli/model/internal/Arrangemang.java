package se.inera.certificate.modules.rli.model.internal;

import org.joda.time.LocalDate;

public class Arrangemang {

	private String bokningsReferens;
	
	private LocalDate bokningsDatum;
	
	private String plats;
	
	private LocalDate arrangemangStartDatum;
	
	private LocalDate arrangemangSlutDatum;
	
	private String arrangemangsTyp;
	
	private LocalDate avbestallningsDatum;
		
	public Arrangemang() {
	
	}

	public String getBokningsReferens() {
		return bokningsReferens;
	}

	public void setBokningsReferens(String bokningsReferens) {
		this.bokningsReferens = bokningsReferens;
	}

	public LocalDate getBokningsDatum() {
		return bokningsDatum;
	}

	public void setBokningsDatum(LocalDate bokningsDatum) {
		this.bokningsDatum = bokningsDatum;
	}

	public String getPlats() {
		return plats;
	}

	public void setPlats(String plats) {
		this.plats = plats;
	}

	public LocalDate getArrangemangStartDatum() {
		return arrangemangStartDatum;
	}

	public void setArrangemangStartDatum(LocalDate arrangemangStartDatum) {
		this.arrangemangStartDatum = arrangemangStartDatum;
	}

	public LocalDate getArrangemangSlutDatum() {
		return arrangemangSlutDatum;
	}

	public void setArrangemangSlutDatum(LocalDate arrangemangSlutDatum) {
		this.arrangemangSlutDatum = arrangemangSlutDatum;
	}

	public String getArrangemangsTyp() {
		return arrangemangsTyp;
	}

	public void setArrangemangsTyp(String arrangemangsTyp) {
		this.arrangemangsTyp = arrangemangsTyp;
	}

	public LocalDate getAvbestallningsDatum() {
		return avbestallningsDatum;
	}

	public void setAvbestallningsDatum(LocalDate avbestallningsDatum) {
		this.avbestallningsDatum = avbestallningsDatum;
	}

}
