package se.inera.certificate.modules.rli.model.internal;

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

	public OrsakAvbokning getOrsakForAvbokning() {
		return orsakForAvbokning;
	}

	public void setOrsakForAvbokning(OrsakAvbokning orsakForAvbokning) {
		this.orsakForAvbokning = orsakForAvbokning;
	}
	
}
