package se.inera.certificate.modules.rli.model.internal;

import se.inera.certificate.modules.rli.model.codes.ObservationsKod;

public class Tillstand {

	private OrsakAvbokning orsakForAvbokning;
	
	private Graviditet graviditet;
	
	public Tillstand() {
	
	}
	
	public Graviditet getGraviditet() {
		return graviditet;
	}

	public void setGraviditet(Graviditet graviditet) {
		this.graviditet = graviditet;
	}
	
}
