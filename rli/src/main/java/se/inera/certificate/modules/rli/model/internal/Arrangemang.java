/**
 * Copyright (C) 2013 Inera AB (http://www.inera.se)
 *
 * This file is part of Inera Certificate Modules (http://code.google.com/p/inera-certificate-modules).
 *
 * Inera Certificate Modules is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Inera Certificate Modules is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.inera.certificate.modules.rli.model.internal;

import org.joda.time.LocalDate;

import se.inera.certificate.modules.rli.model.codes.ArrangemangsTyp;

public class Arrangemang {

	private String bokningsReferens;
	
	private String bokningsDatum;
	
	private String plats;
	
	private String arrangemangStartDatum;
	
	private String arrangemangSlutDatum;
	
	private ArrangemangsTyp arrangemangsTyp;
	
	private String avbestallningsDatum;
		
	public Arrangemang() {
	
	}

	public String getBokningsReferens() {
		return bokningsReferens;
	}

	public void setBokningsReferens(String bokningsReferens) {
		this.bokningsReferens = bokningsReferens;
	}

	public String getBokningsDatum() {
		return bokningsDatum;
	}

	public void setBokningsDatum(String bokningsDatum) {
		this.bokningsDatum = bokningsDatum;
	}

	public String getPlats() {
		return plats;
	}

	public void setPlats(String plats) {
		this.plats = plats;
	}

	public String getArrangemangStartDatum() {
		return arrangemangStartDatum;
	}

	public void setArrangemangStartDatum(String arrangemangStartDatum) {
		this.arrangemangStartDatum = arrangemangStartDatum;
	}

	public String getArrangemangSlutDatum() {
		return arrangemangSlutDatum;
	}

	public void setArrangemangSlutDatum(String arrangemangSlutDatum) {
		this.arrangemangSlutDatum = arrangemangSlutDatum;
	}

	public ArrangemangsTyp getArrangemangsTyp() {
		return arrangemangsTyp;
	}

	public void setArrangemangsTyp(ArrangemangsTyp arrangemangsTyp) {
		this.arrangemangsTyp = arrangemangsTyp;
	}

	public String getAvbestallningsDatum() {
		return avbestallningsDatum;
	}

	public void setAvbestallningsDatum(String avbestallningsDatum) {
		this.avbestallningsDatum = avbestallningsDatum;
	}

}
